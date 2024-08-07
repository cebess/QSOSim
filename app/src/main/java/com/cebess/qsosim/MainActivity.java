package com.cebess.qsosim;
/**
 * This is the main for the QSOSim android application
 * <li>"@Copyright 2016"</li>
 * @author <a href="mailto:chas.bess@gmail.com">Charles Bess (AD5EN)</a>
 * @since 1.1 August 2024
 * @return the main activity for the app
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // global variables
    public static String ProjectName;// = "QSOSim";
    public static final Boolean DEBUG = false; // used in the QSO generator

    // Declare the User Interface elements
    EditText generatedQSOEditText;

    Button generateButton;
    Button sendButton;
    Button stopButton;
    Button infoButton;
    Button M1PlusButton,M2PlusButton,M3PlusButton,M4PlusButton;
    Button M1MinusButton, M2MinusButton, M3MinusButton, M4MinusButton;
    SeekBar toneControlSeekBar;
    SeekBar speedSeekBar;
    TextView toneTextView;
    TextView speedTextView;
    CheckBox noisecheckBox;
    CheckBox QRMcheckBox;
    CheckBox hideQSOCheckBox;
    CheckBox farnsworthCheckBox;

    //Declare the preferences strings
    SharedPreferences sharedpreferences;
    public static final String mypref = ProjectName + "pref";
    public static final String speedstring = "speed";
    public static final String hidestring = "hide";
    public static final String tonestring = "tone";
    public static final String farnsworthString = "farns";
    public static final String memory1String ="Memory1";
    public static final String memory2String ="Memory2";
    public static final String memory3String ="Memory3";
    public static final String memory4String ="Memory4";

    //variables
    public static boolean farnsworth = false;
    int XmitSpeed = 13;
    int sendTone = 1000;
    Intent morseServiceIntent = null;

    // Declare the memory locations
    String Memory1 = "";
    String Memory2 = "";
    String Memory3 = "";
    String Memory4 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectName = getString(R.string.app_name);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // define the graphic elements
        generatedQSOEditText = findViewById(R.id.generatedQSOEditText);
        speedTextView = findViewById(R.id.speedTextView);
        initializeXmitField(13);
        generateButton = findViewById(R.id.generateButton);
        sendButton = findViewById(R.id.sendButton);
        stopButton = findViewById(R.id.stopButton);
        infoButton = findViewById(R.id.infoButton);
        M1PlusButton = findViewById(R.id.M1PlusButton);
        M2PlusButton = findViewById(R.id.M2PlusButton);
        M3PlusButton = findViewById(R.id.M3PlusButton);
        M4PlusButton = findViewById(R.id.M4PlusButton);
        M1MinusButton= findViewById(R.id.M1MinusButton);
        M2MinusButton= findViewById(R.id.M2MinusButton);
        M3MinusButton= findViewById(R.id.M3MinusButton);
        M4MinusButton= findViewById(R.id.M4MinusButton);
        toneControlSeekBar= findViewById(R.id.toneControlSeekBar);
        speedSeekBar = findViewById(R.id.speedSeekBar);
        toneTextView = findViewById(R.id.toneTextView);
        noisecheckBox= findViewById(R.id.QRNcheckBox);
        hideQSOCheckBox = findViewById(R.id.hideQSOCheckBox);
        QRMcheckBox =findViewById(R.id.QRMcheckBox);
        farnsworthCheckBox = findViewById(R.id.farnsworthCheckBox);

        // add the click listeners
        generateButton.setOnClickListener(btnGenerateListener);
        sendButton.setOnClickListener(btnSendListener);
        stopButton.setOnClickListener(btnStopListener);
        infoButton.setOnClickListener(btnInfoListener);
        M1PlusButton.setOnClickListener(btnSaveListener);
        M2PlusButton.setOnClickListener(btnSaveListener);
        M3PlusButton.setOnClickListener(btnSaveListener);
        M4PlusButton.setOnClickListener(btnSaveListener);
        M1MinusButton.setOnClickListener(btnRecallListener);
        M2MinusButton.setOnClickListener(btnRecallListener);
        M3MinusButton.setOnClickListener(btnRecallListener);
        M4MinusButton.setOnClickListener(btnRecallListener);

        //load preferences
        sharedpreferences = getSharedPreferences(mypref, Context.MODE_PRIVATE);
        loadPref();
        stopButton.setEnabled(false);

        noisecheckBox.setOnClickListener(src -> {
            savePref();
            if (noisecheckBox.isChecked()) {
                Intent noiseServiceIntent= new Intent(this, MusicService.class);
                // Start the MusicService using the Intent
                startService(noiseServiceIntent);

            }
            else
            {
                Intent noiseServiceIntent = new Intent(this, MusicService.class);
                stopService(noiseServiceIntent);
            }
        });

        QRMcheckBox.setOnClickListener(src -> {
            savePref();
            if (QRMcheckBox.isChecked()) {
                Intent QRMServiceIntent= new Intent(this, QRMService.class);
                // Start the MusicService using the Intent
                startService(QRMServiceIntent);

            }
            else
            {
                Intent QRMServiceIntent = new Intent(this, QRMService.class);
                stopService(QRMServiceIntent);
            }
        });

        hideQSOCheckBox.setOnClickListener(src -> {
            savePref();
            QSOCheckBoxCheck();
        });

        farnsworthCheckBox.setOnClickListener(src -> {
            savePref();
            farnsworth_check();
            stopMorseService();
        });

        toneControlSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 50;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(MainActivity.this, "seek bar progress:" + progressChanged,Toast.LENGTH_SHORT).show();
                //set the tone value
                sendTone = 10*progressChanged+500;
                //change the text field
                toneTextView.setText(Integer.toString(sendTone));
                savePref();
                stopMorseService();
                }
            });

        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 50;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(MainActivity.this, "seek bar progress:" + progressChanged,Toast.LENGTH_SHORT).show();
                //set the speed value
                XmitSpeed = progressChanged;
                //change the text field
                speedTextView.setText(Integer.toString(XmitSpeed));
                savePref();
                stopMorseService();
            }
        });
    }
    private void stop_services() {
        Intent noiseServiceIntent = new Intent(this, MusicService.class);
        stopService(noiseServiceIntent);
        Intent QRMServiceIntent = new Intent(this, QRMService.class);
        stopService(QRMServiceIntent);
        stopMorseService();
    }
    @Override
    protected void onStop() {
        /// stop any running services that may be running
        stop_services();
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        /// make sure to stop services that may be running
        stop_services();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        /// check to see if there are any services that need to be started
        if (noisecheckBox.isChecked()) {
            Intent noiseServiceIntent= new Intent(this, MusicService.class);
            // Start the MusicService using the Intent
            startService(noiseServiceIntent);
        }
        if (QRMcheckBox.isChecked()) {
            Intent QRMServiceIntent= new Intent(this, QRMService.class);
            // Start the MusicService using the Intent
            startService(QRMServiceIntent);

        }
        super.onStart();
    }

    private void farnsworth_check()
    {
        // set a global farnsworth boolean
        farnsworth = farnsworthCheckBox.isChecked();
    }

    private void QSOCheckBoxCheck()
    {
        if (hideQSOCheckBox.isChecked()) {
            // hide the QSO text box
            generatedQSOEditText.setVisibility(View.INVISIBLE);
        }
        else
        {
            // un-hide the QSO text box
            generatedQSOEditText.setVisibility(View.VISIBLE);
        }
    }

    private void generateMessage() {
        try {
            XmitSpeed = Integer.parseInt(speedTextView.getText().toString());
            if (XmitSpeed < 5 || XmitSpeed > 50) {
                //this should never be displayed now that I use a tracking bar
                Toast.makeText(getApplicationContext(), "The transmit speed must be between 5 and 50, inclusive.", Toast.LENGTH_SHORT).show();
                initializeXmitField (13);
            } else {
                RandomQSO myQSO = new RandomQSO(XmitSpeed);
                generatedQSOEditText.setText(myQSO.toString());
            }
        } catch (IOException e) {
            Log.e(MainActivity.ProjectName,"IO exception: " + e.getMessage());
            finish();
        } catch (java.text.ParseException e) {
            Log.e(MainActivity.ProjectName,"Parse exception: " + e.getMessage());
            finish();
        }
    }

    // add the listeners
    // action for the Generate button
    private final OnClickListener btnGenerateListener = v -> {
        stopMorseService();
        generateMessage();
    };

    // action for the saving button
    private final OnClickListener btnSaveListener = new OnClickListener() {
        public void onClick(View v){

            Button buttonClicked = (Button)v;
            String buttonText = buttonClicked.getText().toString();
            switch (buttonText) {
                case "M1+":
                    Memory1 = generatedQSOEditText.getText().toString();
                    break;
                case "M2+":
                    Memory2 = generatedQSOEditText.getText().toString();
                    break;
                case "M3+":
                    Memory3 = generatedQSOEditText.getText().toString();
                    break;
                case "M4+":
                    Memory4 = generatedQSOEditText.getText().toString();
                    break;
                default: break; // should never end up here
            }
            savePref();
        }
    };

    // action for the stop button
    private final OnClickListener btnStopListener = v -> {
        stopMorseService();
        };

    private void stopMorseService() {
        if (morseServiceIntent!= null) {
            stopService(morseServiceIntent);
            morseServiceIntent = null;
            stopButton.setEnabled(false);
        }
    }

    // action for the info button
    private final OnClickListener btnInfoListener = v -> {
        String myInfo = getString(R.string.info_data);
        generatedQSOEditText.setText(myInfo);
        if (hideQSOCheckBox.isChecked()) {
            hideQSOCheckBox.setChecked(false);
            // un-hide the QSO text box
            generatedQSOEditText.setVisibility(View.VISIBLE);
        }
    };

    // action for the recall button
    private final OnClickListener btnRecallListener = new OnClickListener() {
        public void onClick(View v){
            Button buttonClicked = (Button)v;
            String buttonText = buttonClicked.getText().toString();
            switch (buttonText) {
                case "M1-":
                    generatedQSOEditText.setText(Memory1);
                    break;
                case "M2-":
                    generatedQSOEditText.setText(Memory2);
                    break;
                case "M3-":
                    generatedQSOEditText.setText(Memory3);
                    break;
                case "M4-":
                    generatedQSOEditText.setText(Memory4);
                    break;
                default: break;    // should never end up here
            }

        }
    };


    // action for the send button
    private final OnClickListener btnSendListener = new OnClickListener() {
        public void onClick(View v) {
            XmitSpeed = Integer.parseInt(speedTextView.getText().toString());
            savePref();
            if (XmitSpeed < 5 || XmitSpeed > 50) {
                //display in short period of time
                Toast.makeText(getApplicationContext(), "The transmit speed must be between 5 and 50, inclusive.", Toast.LENGTH_SHORT).show();
                initializeXmitField (13);
            } else {
                morseServiceIntent = new Intent(getBaseContext(), MorsePlayerService.class);
                morseServiceIntent.putExtra("message",generatedQSOEditText.getText().toString());
                morseServiceIntent.putExtra("speed",XmitSpeed);
                morseServiceIntent.putExtra("tone",sendTone);
                startService(morseServiceIntent);
                stopButton.setEnabled(true);
            }
        }
    };

private void initializeXmitField(int speed) {
    XmitSpeed = speed;
    speedTextView.setText(Integer.toString(XmitSpeed));
    }

private void loadPref() {

    if(sharedpreferences.contains(hidestring)) {
        hideQSOCheckBox.setChecked(sharedpreferences.getBoolean(hidestring, false));
        QSOCheckBoxCheck();
    }

    if(sharedpreferences.contains(farnsworthString)) {
        farnsworthCheckBox.setChecked(sharedpreferences.getBoolean(farnsworthString, false));
        farnsworth_check();
    }

    if(sharedpreferences.contains(speedstring)) {
        //update speed based on preferences
        initializeXmitField(sharedpreferences.getInt(speedstring, 13));
    } else {
        XmitSpeed = 13;
    }
    speedSeekBar.setProgress(XmitSpeed);

    if (sharedpreferences.contains(tonestring)) {
        sendTone = sharedpreferences.getInt(tonestring, 1000);
    } else {
        sendTone = 1000;
    }
    //change the text field
    toneTextView.setText(Integer.toString(sendTone));
    //set the SeekBar value
    int toneValue = (sendTone-500)/10;
    toneControlSeekBar.setProgress(toneValue);

    if(sharedpreferences.contains(memory1String)) {
        //update memory entry
        Memory1 = sharedpreferences.getString(memory1String,"");
    }

    if(sharedpreferences.contains(memory2String)) {
        //update memory entry
        Memory2 = sharedpreferences.getString(memory2String,"");
    }

    if(sharedpreferences.contains(memory3String)) {
        //update memory entry
        Memory3 = sharedpreferences.getString(memory3String,"");
    }

    if(sharedpreferences.contains(memory4String)) {
        //update memory entry
        Memory4 = sharedpreferences.getString(memory4String,"");
    }
}

    private void savePref() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(hidestring, hideQSOCheckBox.isChecked());
        editor.putBoolean(farnsworthString,farnsworthCheckBox.isChecked());
        editor.putInt(speedstring,XmitSpeed);
        editor.putInt(tonestring,sendTone);
        editor.putString(memory1String,Memory1);
        editor.putString(memory2String,Memory2);
        editor.putString(memory3String,Memory3);
        editor.putString(memory4String,Memory4);
        editor.apply(); //post the changes
    }
}