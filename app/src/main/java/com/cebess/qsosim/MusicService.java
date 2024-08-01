package com.cebess.qsosim;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by chasb on 12/5/2016.
 * Updated 7/29/2024
 */

public class MusicService extends Service {

    @SuppressWarnings("unused")
    private final String TAG = "MusicService";

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Noise Service Created");

        // Initialize the MediaPlayer and set it to loop
        mediaPlayer = MediaPlayer.create(this, R.raw.hamnoise);
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Noise Service Started");

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
        Log.d(TAG, "Noise Service Destroyed");

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
