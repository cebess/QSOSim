package com.cebess.qsosim;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by chasb on 8/6/2024.
 * It is a clone of the Music service
 */

public class QRMService extends Service {

    private final String TAG = "QRMService";

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "QRM Service Created");

        // Initialize the MediaPlayer and set it to loop
        mediaPlayer = MediaPlayer.create(this, R.raw.qrm);
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "QRM Service Started");

        // Start the MediaPlayer
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }

        // If the service is killed, restart it with the last intent
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "QRM Service Destroyed");

        // Release the MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

}
