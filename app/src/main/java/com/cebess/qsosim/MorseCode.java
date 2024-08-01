package com.cebess.qsosim;

/**
 * Created by chasb on 11/26/2016.
 */

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * MorseCode holds a Morse Code character to synthesize.<p>
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
 * This module is Java 1.1 compatible.
 */
public class MorseCode
{
    private final char                letter;                         /* Symbol to type       */
    private final String              label;                          /* Button display       */
    private final String              code;                           /* Dots and dashes      */


    /*
     * This is static but, if so, bogus exceptions aren't
     * thrown as there is no "main" running yet. Note that
     * there are place-holder slots here so that the button
     * panel looks nice. These can be identified by
     * null code strings
     */
    /**
     * This is a vector of Morse code characters, providing the
     * name (that the user types or clicks) and the dit/dah sequence.
     */
    public static final MorseCode[] morse       = {
            new MorseCode('A',      ".-"            ),
            new MorseCode('B',      "-..."          ),
            new MorseCode('C',      "-.-."          ),
            new MorseCode('D',      "-.."           ),
            new MorseCode('E',      "."             ),
            new MorseCode('F',      "..-."          ),
            new MorseCode('G',      "--."           ),
            new MorseCode('H',      "...."          ),
            new MorseCode('I',      ".."            ),
            new MorseCode('J',      ".---"          ),

            new MorseCode('K',      "-.-"           ),
            new MorseCode('L',      ".-.."          ),
            new MorseCode('M',      "--"            ),
            new MorseCode('N',      "-."            ),
            new MorseCode('O',      "---"           ),
            new MorseCode('P',      ".--."          ),
            new MorseCode('Q',      "--.-"          ),
            new MorseCode('R',      ".-."           ),
            new MorseCode('S',      "..."           ),
            new MorseCode('T',      "-"             ),

            new MorseCode('U',      "..-"           ),
            new MorseCode('V',      "...-"          ),
            new MorseCode('W',      ".--"           ),
            new MorseCode('X',      "-..-"          ),
            new MorseCode('Y',      "-.--"          ),
            new MorseCode('Z',      "--.."          ),
            new MorseCode('/',      "-..-."         ),
            new MorseCode('.',      ".-.-.-"        ),
            new MorseCode(',',      "--..--"        ),
            new MorseCode('?',      "..--.."        ),
/*
 * Numbers
 */
            new MorseCode('0',      "-----"         ),
            new MorseCode('1',      ".----"         ),
            new MorseCode('2',      "..---"         ),
            new MorseCode('3',      "...--"         ),
            new MorseCode('4',      "....-"         ),
            new MorseCode('5',      "....."         ),
            new MorseCode('6',      "-...."         ),
            new MorseCode('7',      "--..."         ),
            new MorseCode('8',      "---.."         ),
            new MorseCode('9',      "----."         ),
/*
 * Punctuation and prosigns
 */
            new MorseCode('\u0000', null            ),  /* Silence for layout   */
            new MorseCode('\u0000', null            ),  /* Silence for layout   */

            new MorseCode('=', "= BT", "-...-"      ),  /* Double-dash == BT    */
            new MorseCode('+', "+ AR",  ".-.-."     ),  /* End of message == AR */
            new MorseCode('#', "# SK", "...-.-"     ),  /* End of work == SK    */
            new MorseCode(' ', "  SP", ""           ),  /* Space                */


    };
    /**
     * The MorseCode space character is needed elsewhere.
     */
    public static final MorseCode space         = new MorseCode(' ', "sp", " "  );
    /**
     * This table lets callers find the Morse code representation
     * of a character entered in the text field.
     */
    private static final Hashtable symbols      = new Hashtable();

    static {
        for (int i = 0; i < MorseCode.morse.length; i++) {
            String key          = String.valueOf(MorseCode.morse[i].getLetter());
            symbols.put(key, MorseCode.morse[i]);
        }
    }

    /**
     * Create a MorseCode sequence that can convert this Morse Code sequence
     * to an audio sequence.
     * @param letter            The name of this symbol: 'A', 'B', etc.
     * @param code              The Morse Code: ".-", "-...", etc.
     *  Code must contain only '.', '-', and space characters.
     */
    public MorseCode(
            char                letter,
            String              code                    /* Null for "marker"    */
    )
    {
        this(letter, String.valueOf(letter), code);
    }
    /**
     * Create a MorseCode sequence that can convert this Morse Code sequence
     * to an audio sequence.
     * @param letter            The name of this symbol: 'A', 'B', etc.
     * @param label             The name of this symbol as a String Object.
     * @param code              The Morse Code: ".-", "-...", etc.
     *  Code must contain only '.', '-', and space characters.
     */
    public MorseCode(
            char                letter,                 /* Symbol to type       */
            String              label,                  /* Label for the button */
            String              code                    /* Null for "marker"    */
    )
    {
        this.letter             = letter;
        this.label              = label;
        this.code               = code;
    }
    /**
     * Return this symbol's letter.
     * @return the symbol's letter.
     */
    public char getLetter()
    {
        return (letter);
    }
    /**
     * Return this symbol's label.
     * @return the symbol's label.
     */
    public String getLabel()
    {
        return (label);
    }
    /**
     * Return this symbol's Morse code sequence.
     * @return the symbol's Morse code sequence.
     */
    public String getCode()
    {
        return (code);
    }
    /**
     * Return this symbol's letter.
     * @return the symbol's letter.
     */
    public String toString()
    {
        return (String.valueOf(getLetter()));
    }

    /*
     * The following static methods retrieve symbols from the Hashtable.
     */

    /**
     * Convert a string to a vector of symbols.
     * @param source            The string to convert
     * @return a vector of MorseCode symbols.
     */
    public static MorseCode[] getCodeTokens(
            String              source
    )
    {
        Vector tokens           = new Vector(source.length());
        StringTokenizer t       = new StringTokenizer(
                source,
                " \t\n",
                true
        );
        while (t.hasMoreTokens()) {
            String word                 = t.nextToken().toUpperCase();
            if ("\t".equals(word) || "\n".equals(word)) {
                word                    = " ";
            }
            MorseCode codeElement       = (MorseCode) symbols.get(word);
            if (codeElement != null) {
                tokens.addElement(codeElement); /* AR, BT, or similar   */
            }
            else {
                char[] chars            = word.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    codeElement         = getMorseCode(chars[i]);
                    if (codeElement != null) {
                        tokens.addElement(codeElement);
                    }
                }
            }
        }
        MorseCode[] result              = new MorseCode[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            result[i]                   = (MorseCode) tokens.elementAt(i);
        }
        return (result);
    }
    /**
     * Convert a letter to a Morse Code symbol. If the letter is unknown,
     * return a space symbol.
     * @param letter            The letter to convert
     * @return a MorseCode symbol.
     */
    public static MorseCode getMorseCode(
            char                letter
    )
    {
        return (getMorseCode(String.valueOf(letter)));
    }
    /**
     * Convert a letter stored in a string to a Morse Code symbol.
     * If the letter is unknown, return a space symbol.
     * @param letter            The letter to convert
     * @return a MorseCode symbol.
     */
    public static MorseCode getMorseCode(
            String              letter
    )
    {
        MorseCode result        = (MorseCode) symbols.get(letter);
        if (result == null) {
            result              = MorseCode.space;
        }
        return (result);
    }
}
