package com.cebess.qsosim;

/**
 * Created by chasb on 11/26/2016.
 */

import android.util.Log;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PushbackReader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * RandomSentence compiles a grammar that generates random sentences
 * when requested. It is based on RSGEN.C, part of the Decus C
 * distribution and, originally, on James Gimpel's "Algorithms in SNOBOL4."
 *
 * <p>
 * Copyright &copy; 1999-2000
 *      <a href="mailto:minow@pobox.com">Martin Minow</a>.
 *      All Rights Reserved.
 * </p>
 * <p>
 * <small>
 * Permission to use, copy, modify, and redistribute this software and its
 * documentation for personal, non-commercial use is hereby granted provided that
 * this copyright notice and appropriate documentation appears in all copies. This
 * software may not be distributed for fee or as part of commercial, "shareware,"
 * and/or not-for-profit endevors including, but not limited to, CD-ROM collections,
 * online databases, and subscription services without specific license.  The
 * author makes no expressed or implied warranty of any kind and assumes no
 * responsibility for errors or omissions. No liability is assumed for any incidental
 * or consequental damages in connection with or arising out of the use of the
 * information or program.
 * </small>
 * </p>
 *
 * @author <a href="mailto:minow@pobox.com">Martin Minow</a>
 * @version 1.1
 */
public class RandomSentence extends Hashtable
{
    /**
     * Construct a RandomSentence generator for a given grammar.
     * A grammar is a list of rules, each rule is terminated
     * by a newline.
     * @param sourceGrammar     The grammar to generate.
     */
    public RandomSentence(
            String              sourceGrammar
    )
            throws IOException, ParseException
    {
        super();
        addRule(sourceGrammar);
    }
    /**
     * Expand a string by replacing all non-terminal elements
     * by some (random) choice among their rewrites. Note: we try
     * not to insert extraneous blanks.
     * @param text      Text to expand: a combination of terminal
     *                  and non-terminal symbols.
     */
    public String expand(
            String              text
    )
    {
        StringBuffer work       = new StringBuffer(text.length() * 3);
        StringBuffer name       = new StringBuffer(32);
        boolean gettingName     = false;
        int length              = text.length();
        for (int i = 0; i < length; i++) {
            char ch             = text.charAt(i);
            if (gettingName) {
                name.append(ch);
                if (ch == '>') {
                    Rule rule   = (Rule) get(name.toString());
                    if (rule != null) {
                        work.append(expand(getRewrite(rule)));
                    }
                    else { /* Random text with <> characters */
                        work.append(name);
                    }
                    gettingName = false;
                    name.setLength(0);
                }
            }
            else if (ch == '<') {
                name.setLength(0);
                name.append(ch);
                gettingName             = true;
            }
            else {
                work.append(ch);
            }
        }
        work.append(name);
        return (work.toString());
    }
    /**
     * Select a random rewrite (right-hand side).
     * @param rule    The rule to expand
     * @return a rewrite (this may expand to a rule).
     */
    private String getRewrite(
            Rule                rule
    )
    {
        String result           = rule.name;
        try {
            int weight          = (int) Math.floor(Math.random() * rule.weightSum);
            Enumeration e       = rule.terms.elements();
            while (e.hasMoreElements()) {
                StringTokenizer st = new StringTokenizer((String) e.nextElement(), " ", true);
                int thisWeight  = Integer.parseInt(st.nextToken());
                if ((weight -= thisWeight) < 0) {
                    st.nextToken(); /* Skip over the space */
                    result      = st.nextToken("\uFFFF"); /* Get the rest       */
                    break;
                }
            }
        }
        catch (NoSuchElementException e) {
            Log.e(MainActivity.ProjectName,"Corrupted rule rewrite \n" + rule);
            Log.e(MainActivity.ProjectName,"Rewrite parse error: " + e);
        }
        catch (NumberFormatException e) {
            Log.e(MainActivity.ProjectName,"Corrupted rule rewrite \n" + rule);
            Log.e(MainActivity.ProjectName,"Weight format error: " + e);
        }
        return (result);
    }

    /**
     * Append some more rules to the grammar. Each rule
     * is one element in the string vector. If called after
     * some rules were created, the new rules will be added.
     * Execute RandomSentence.clear() to clean out the mess.
     * Rules with the same name replace (without comment) the
     * previous rule. This is useful if some randomly-generated
     * text must appear in two or more places in a message.
     * @param sourceGrammar     A vector of rule strings.
     */
    public void addRule(
            String[]            sourceGrammar
    )
            throws IOException, ParseException
    {
//        for (int i = 0; i < sourceGrammar.length; i++) {
//            addThisRule(sourceGrammar[i]);
        for (String newRule:sourceGrammar) {
            addThisRule(newRule);
        }
    }
    /**
     * Append some more rules to the grammar. The rules are
     * stored in an input file. Print an error message if
     * the input is invalid.
     * @param reader    A text file (or object that acts like
     *                  a text file) that will define this grammar.
     */
    public void addRule(
            LineNumberReader    reader
    )
            throws IOException, ParseException
    {
        try {
            String thisLine     = null;
            while ((thisLine = reader.readLine()) != null) {
                addThisRule(thisLine);
            }
        }
        catch (IOException e) {
            throw new IOException(
                    "RandomSentence reader failed on line "
                            + reader.getLineNumber()
                            + ": " + e
            );
        }
    }
    /**
     * Append one or more rule to the grammar. Individual rules
     * are delimited by newlines.
     * @param rule      The rule as a string.
     */
    public void addRule(
            String              rule
    )
            throws IOException, ParseException
    {
        StringTokenizer st      = new StringTokenizer(rule, "\n");
        while (st.hasMoreTokens()) {
            addThisRule(st.nextToken());
        }
    }
    private void addThisRule(
            String              rule
    )
            throws IOException, ParseException
    {
        PushbackReader in       = new PushbackReader(new StringReader(rule));
        addThisRule(in);
    }
    /**
     * Append a new rule to the grammar. If the rule name (the
     * left-hand side element) was previously defined, it will
     * be replaced. The rule is a single (i.e., one line) string.
     * Syntax:
     *  <rule>          :==     <rule name> <whitespace> <rule terms>
     *  <rule name>     :==     '<' <text string> '>'
     *  <rule terms>    :==     <rule term>
     *                  ||      <rule term> '|' <rule terms>
     */
    private static final char   EOL     = '\u0000';

    private void addThisRule(
            PushbackReader      in
    )
            throws IOException, ParseException
    {
        String ruleName         = "";
        boolean ok              = true;
        char ch                 = skipWhitespace(in);
        ok = (ch != EOL && ch != '[');
        if (ok) {
            ruleName            = readName(in);
            ok                  = (ruleName != null);
            if (!ok) {
                throw syntaxError("Malformed rule name", in);
            }
        }
        if (ok) {
            ch                  = skipWhitespace(in);
            ok                  = (ch != EOL);
            if (!ok) {
                throw syntaxError("Expecting rule body after rule name", in);
            }
        }
        if (ok) {
            Rule rule           = new Rule(ruleName);
            readRuleBody(rule, in);     /* Bad errors cause exceptions  */
            put(ruleName, rule);
        }
    }

    /**
     * Debug: write all rules to the standard output device.
     */
    public void writeRules()
    {
        if (isEmpty()) {
            Log.e(MainActivity.ProjectName,"No rules have been compiled");
        }
        else {
            Log.e(MainActivity.ProjectName,"Grammar table has " + size() + " elements");
            Enumeration e       = elements();
            while (e.hasMoreElements()) {
                Log.e(MainActivity.ProjectName,"element: " + e.nextElement());
            }
        }
    }

    /**
     * Read a rule name, return the name if successful, null if not.
     * (Null is a legitimate result when expanding, but not when
     * compiling rules).
     */
    private String readName(
            PushbackReader      in
    )
            throws IOException
    {
        skipWhitespace(in);
        StringBuffer work       = new StringBuffer(32);
        String result           = "";
        int c                   = 0;
        if ((c = in.read()) != '<') {
            result              = null;
        }
        if (result != null) {
            work.append((char) c);
            while ((c = in.read()) != -1) {
                work.append((char) c);
                if (c == '>') {
                    break;
                }
            }
        }
        if (result != null && c == '>') {
            result              = work.toString();
        }
        else {
            result              = null;
        }
        return (result);
    }
    /**
     * Read all terms for this rule. When we are called, the input
     * reader points to the first character in the rule body (after
     * any whitespace). If successful, this method returns a newly
     * constructed Rule object containing all terms for this rule.
     * On return, the rule is compiled as follows:
     *  weightSum               has the sum of all term weights
     *  terms                   has a vector of all rewrites.
     */
    private void readRuleBody(
            Rule                rule,
            PushbackReader      in
    )
            throws ParseException, IOException
    {
        while (skipWhitespace(in) != EOL) {
            int weight          = readWeight(in);
            rule.weightSum      += weight;
            skipWhitespace(in);
            StringBuffer work   = new StringBuffer(128);
            work.append(weight + " ");  /* This term's weight   */
            /*
             * Read this rewrite (right-hand side)
             */
            skipWhitespace(in);
            int c               = 0;
            while ((c = in.read()) != -1 && c != '|') {
                work.append((char) c);
            }
            String term         = work.toString().trim();
            rule.terms.addElement(term);
            if (c == '|') {
                c = in.read();
            }
        }
    }

    /**
     * Read a weight delimted by [number ], return one
     * if there is no weight.
     */
    private int readWeight(
            PushbackReader      in
    )
            throws ParseException, IOException
    {
        char ch                 = skipWhitespace(in);
        int result              = 1;
        if (ch == '[') {
            in.read();          /* Skip over the [ delimiter    */
            skipWhitespace(in);
            result              = 0;
            int c               = 0;
            while ((c = in.read()) >= 0 && Character.isDigit((char) c)) {
                result          *= 10;
                result          += (c - '0');
            }
            if (c != -1) {
                in.unread(c);
            }
            ch                  = skipWhitespace(in);
            if (ch == ']') {
                in.read();      /* Skip over the ] delimiter    */
            }
            else {
                throw syntaxError("Malformed [weight] in term", in);
            }
        }
        return (result);
    }
    /**
     * Skip over whitespace in the input stream. Return the
     * next (non-whitespace) character. Note that the non-whitespace
     * character is pushed back onto the stream. For example, if
     * the input is " foo ", skipWhitespace returns 'f' and the
     * input is now "foo ". Return EOL on blank lines and I/O errors.
     */
    private char skipWhitespace(
            PushbackReader      in
    )
            throws IOException
    {
        int c                   = -1;
        while ((c = in.read()) != -1) {
            if (!Character.isWhitespace((char) c)) {
                break;
            }
        }
        if (c != -1) {
            in.unread(c);
        }
        return ((c == -1) ? EOL : ((char) c));
    }
    /**
     * Write an error message to the error log. Setting the
     * input reader to the end of the rule.
     */
    private ParseException syntaxError(
            String              message,
            PushbackReader      in
    )
    {
        StringBuffer work       = new StringBuffer();
        work.append("Syntax error: " + message);
        try {
            in.reset();
            StringBuffer line   = new StringBuffer(128);
            int c                       = 0;
            while ((c = in.read()) != -1) {
                line.append((char) c);
            }
            work.append(", rule is \"" + work + "\"");
        }
        catch (IOException e) {
            work.append(" (" + e + ")");
        }
        return (new ParseException(work.toString(), 0));
    }
    /**
     * For debugging, list all <symbols> on the right-hand side
     * that are not also rule names.
     */
    public int checkAllSymbols()
    {
        Enumeration e           = elements();
        int errorCount          = 0;
        while (e.hasMoreElements()) {
            Rule rule           = (Rule) e.nextElement();
            Enumeration terms   = rule.terms.elements();
            while (terms.hasMoreElements()) {
                errorCount      += checkTermSymbols(rule.name, (String) terms.nextElement());
            }
        }
        switch (errorCount) {
            case 0: Log.d(MainActivity.ProjectName,"No errors: all symbols were defined");
                break;
            case 1: Log.e(MainActivity.ProjectName,"One symbol was undefined");
                break;
            default: Log.e(MainActivity.ProjectName,errorCount + " symbols were undefined");
                break;
        }
        return (errorCount);
    }
    private int checkTermSymbols(
            String              ruleName,
            String              term
    )
    {
        StringBuffer name       = new StringBuffer(32);
        boolean gettingName     = false;
        int errorCount          = 0;
        int length              = term.length();
        for (int i = 0; i < length; i++) {
            char ch             = term.charAt(i);
            if (gettingName) {
                name.append(ch);
                if (ch == '>') {
                    Rule rule   = (Rule) get(name.toString());
                    if (rule == null) {
                        Log.e(MainActivity.ProjectName,"Rule " + ruleName
                                + " has undefined symbol \""
                                + name
                                + "\""
                        );
                        ++errorCount;
                    }
                    gettingName = false;
                    name.setLength(0);
                }
            }
            else if (ch == '<') {
                name.setLength(0);
                name.append(ch);
                gettingName             = true;
            }
            else {
                /* Ignore normal text */
            }
        }
        return (errorCount);
    }

    /**
     * The Rule inner class defines a single rule. RandomSentence manipulates
     * the Rule variables directly.
     */
    class Rule {
        /**
         * The name of the rule, for debugging only.
         */
        public String           name            = null;         /* For debugging only   */
        /**
         * The sum of all weights of this rule. This is needed to choose between
         * alternative expansions.
         */
        public int              weightSum       = 0;            /* Sum of term weights  */
        /**
         * A vector of right-hand side rule rewrites.
         */
        public Vector           terms           = new Vector(); /* All terms are here   */

        /**
         * Create a Rule with this name.
         * @param ruleName      The name of the rule (for debugging).
         */
        public Rule(
                String      ruleName
        )
        {
            this.name           = ruleName;
        }
        /**
         * Debug only: return the rule and its terms. The last line is not
         * newline-delimited so that Log.e(MainActivity.ProjectName,term.toString()) works
         * as expected.
         */
        public String toString()
        {
            StringBuffer work   = new StringBuffer(name + " [" + weightSum + "] :==");
            work.append("{" + terms.size() + "} ");
            Enumeration e = terms.elements();
            while (e.hasMoreElements()) {
                work.append("\n  \"" + e.nextElement() + "\"");
            }
            return (work.toString());
        }
    }


}
