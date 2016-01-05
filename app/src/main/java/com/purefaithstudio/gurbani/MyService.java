package com.purefaithstudio.gurbani;

import android.app.Service;
import android.content.Intent;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.spoledge.aacdecoder.MultiPlayer;
import com.spoledge.aacdecoder.PlayerCallback;

public class MyService extends Service {
    public static final String SEND = "com.purefaithstudio.gurbani";
    public static MultiPlayer multiPlayer = null;
    public static boolean isPlaying = false, replay = false;
    private String RADIO_STATION_URL = "default", CURRENT_URL = "";
    PlayerCallback playerCallback = new PlayerCallback() {
        @Override
        public void playerStarted() {
            Intent intent = new Intent(SEND);
            intent.setAction("com.purefaithstudio.gurbani.Register");
            sendBroadcast(intent);
            isPlaying = true;
            replay = false;
        }

        @Override
        public void playerPCMFeedBuffer(boolean b, int i, int i1) {

        }

        @Override
        public void playerStopped(int i) {
            isPlaying = false;
            multiPlayer = null;
            if (replay) {
                multiPlayer = new MultiPlayer(playerCallback);
                multiPlayer.playAsync(RADIO_STATION_URL);
                isPlaying = true;
            }
        }

        @Override
        public void playerException(Throwable throwable) {

        }

        @Override
        public void playerMetadata(String s, String s1) {

        }

        @Override
        public void playerAudioTrackCreated(AudioTrack audioTrack) {
        }
    };
    private boolean ifStopped;

    public MyService() {
    }

    public static void stop() {
        if (multiPlayer != null) {
            multiPlayer.stop();
            Log.i("Service123", "Player Stopped");
        }
    }

    public static boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Service123", "Player object created");
        // multiPlayer = new MultiPlayer();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    //start Play
    public void startPlaying() {
        multiPlayer = new MultiPlayer(playerCallback);
        multiPlayer.playAsync(RADIO_STATION_URL);
        isPlaying = true;
        Log.i("Service123", "Played");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (multiPlayer != null)
            multiPlayer.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Toast.makeText(getApplicationContext(), "OnstartCommnad",Toast.LENGTH_LONG).show();
        if (intent != null) {
            Bundle b = intent.getExtras();
            // if (b != null)
            this.RADIO_STATION_URL = b.getString("key");
        } else {
            this.stopSelf();
        }
        //Toast.makeText(getApplicationContext(), "" + RADIO_STATION_URL, Toast.LENGTH_SHORT).show();

        if (RADIO_STATION_URL != null && !CURRENT_URL.equals(RADIO_STATION_URL)) {
            CURRENT_URL = RADIO_STATION_URL;
            Log.i("Service123", "starting play");
            if (multiPlayer == null)
                startPlaying();
            else
                synchronized (multiPlayer) {
                    replay = true;
                    multiPlayer.stop();
                }
        }
        return START_STICKY;

    }
}
