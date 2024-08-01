package com.cebess.qsosim;


/**
 * This is the main sound service for the QSOSender android application
 * * Copyright &copy; 2016
 *      <a href="mailto:chas.bess@gmail.com">Charles Bess (AD5EN)</a>.
 *
 *
 * @version 3v3
 *
 */

import static com.cebess.qsosim.MainActivity.farnsworth;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.IBinder;
import android.util.Log;

/* A bunch of this wave-generating code is copied from
 * http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
 * though I had to fix a bugger length problem.
 */

public class MorsePlayerService extends Service {
    private static final String TAG = "MorsePlayerService";
    Thread audioThread;
    private boolean isPlaying = false;

    private final int SAMPLE_RATE = 8000;
    double duration;  // in seconds
    private int wpmSpeed;
    private int toneHertz;
    int numSamples;
    double[] sample;
    private byte[] ditSnd;
    private byte[] dahSnd;
    private byte[] pauseInnerSnd;
    private MorseBit[] pattern;
    private final AKASignaler signaler = AKASignaler.getInstance();
    private String currentMessage;  // message to play in morse

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Morse player Service Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service Morse player Started");

        String myMessage = intent.getStringExtra("message");
        if (null == myMessage) {
            myMessage = "np message";
        }
        setMessage(myMessage);
        setTone(intent.getIntExtra("tone", 1000));
        setSpeed(intent.getIntExtra("speed",13));
        buildSounds();

        if (!isPlaying) {
            isPlaying = true;
            audioThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    playMorse();
                }
            });
            audioThread.start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isPlaying = false;
        if (signaler.audioTrack != null) {
            Log.d(TAG, "Morse player Destroy Pre stop");
            signaler.audioTrack.stop();
        }
        super.onDestroy();
        Log.d(TAG, "Morse player Service Destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    // Generate 'dit','dah' and empty sine wave tones of the proper lengths.
    private void buildSounds() {
        // where (1200 / wpm) = element length in milliseconds
        duration = (1200.0 / wpmSpeed) * .001;
        numSamples = (int) (duration * SAMPLE_RATE - 1);
        double sineMagnitude = 1; // starting with a dummy value for absolute normalized value of sine wave
        double CUTOFF = 0.1; // threshold for whether sine wave is near zero crossing
        double phaseAngle = 2 * Math.PI / (SAMPLE_RATE / toneHertz);
        while (sineMagnitude > CUTOFF) {
            numSamples++;
            //check to see if  is near zero-crossing to avoid clicks when sound cuts off
            sineMagnitude = Math.abs(Math.sin(phaseAngle * numSamples));
        }
        sample = new double[numSamples];
        ditSnd = new byte[2 * numSamples];
        dahSnd = new byte[6 * numSamples];
        pauseInnerSnd = new byte[2 * numSamples];

        for (int i = 0; i < numSamples; i++) {
            sample[i] = Math.sin(phaseAngle * i);
        }
        // convert to 16 bit pcm sound array; assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            final short val = (short) ((dVal * 32767)); // scale to maximum amplitude
            // in 16 bit wav PCM, first byte is the low order byte
            ditSnd[idx++] = (byte) (val & 0x00ff);
            ditSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
        for (int i = 0; i < (dahSnd.length); i++) {
            dahSnd[i] = ditSnd[i % ditSnd.length];
        }
        for (int i = 0; i < (pauseInnerSnd.length); i++) {
            pauseInnerSnd[i] = 0;

        }
    }

    private void setMessage(String message) {
        currentMessage = message;
    }

    private void setSpeed(int speed) {
        wpmSpeed = speed;
    }

    private void setTone(int hertz) {

        toneHertz = hertz;
    }

    // The main method of this class; runs exactly once in a standalone thread.
    public void playMorse() {
        Log.i(TAG, "Now playing morse code...");
        Log.d(TAG, "currentMessage is: '" + currentMessage + "'");
        pattern = MorseConverter.pattern(currentMessage);

        Log.d(TAG, "Pre calculate message size");
        // Calculate size of data we're going to push.
        int msgSize = calculateMessageSize();

// Calculate the minimum buffer size
        int minSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

// Create an AudioTrack using the builder
        signaler.audioTrack = new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())
                .setAudioFormat(new AudioFormat.Builder()
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .build())
                .setBufferSizeInBytes(minSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build();

        signaler.msgSize = minSize;

        // Start playing sound out of the buffer
        signaler.audioTrack.play();

        // Start pushing sound data into the AudioTrack's buffer.
        pushSoundData();
    }

    private int calculateMessageSize() {
        int msgSize = 0;
        int pauseLength = farnsworth ? pauseInnerSnd.length / 2 : pauseInnerSnd.length;
        int dotLength = farnsworth ? ditSnd.length / 2 : ditSnd.length;
        int dashLength = farnsworth ? dahSnd.length / 2 : dahSnd.length;
        int letterGapMultiplier = farnsworth ? 6 : 3;
        int wordGapMultiplier = 7;

        for (MorseBit bit : pattern) {
            switch (bit) {
                case GAP:
                    msgSize += pauseLength;
                    break;
                case DOT:
                    msgSize += dotLength;
                    break;
                case DASH:
                    msgSize += dashLength;
                    break;
                case LETTER_GAP:
                    msgSize += letterGapMultiplier * pauseInnerSnd.length;
                    break;
                case WORD_GAP:
                    msgSize += wordGapMultiplier * pauseInnerSnd.length;
                    break;
                default:
                    break;
            }
        }
        return msgSize;
    }

    private void pushSoundData() {
        int pauseLength = farnsworth ? pauseInnerSnd.length / 2 : pauseInnerSnd.length;
        int dotLength = farnsworth ? ditSnd.length / 2 : ditSnd.length;
        int dashLength = farnsworth ? dahSnd.length / 2 : dahSnd.length;
        int letterGapMultiplier = farnsworth ? 6 : 3;
        int wordGapMultiplier = 7;

        for (MorseBit bit : pattern) {
            switch (bit) {
                case GAP:
                    signaler.audioTrack.write(pauseInnerSnd, 0, pauseLength);
                    break;
                case DOT:
                    signaler.audioTrack.write(ditSnd, 0, dotLength);
                    break;
                case DASH:
                    signaler.audioTrack.write(dahSnd, 0, dashLength);
                    break;
                case LETTER_GAP:
                    for (int i = 0; i < letterGapMultiplier; i++)
                        signaler.audioTrack.write(pauseInnerSnd, 0, pauseInnerSnd.length);
                    break;
                case WORD_GAP:
                    for (int i = 0; i < wordGapMultiplier; i++)
                        signaler.audioTrack.write(pauseInnerSnd, 0, pauseInnerSnd.length);
                    break;
                default:
                    break;
            }
        }
    }
}