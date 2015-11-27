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
    public static MultiPlayer multiPlayer;
    private String RADIO_STATION_URL;
    private boolean isPlaying = false, isstopped = false;
    private boolean flag;
    PlayerCallback playerCallback = new PlayerCallback() {
        @Override
        public void playerStarted() {
            flag = false;
            Intent intent = new Intent(SEND);
            intent.setAction("com.purefaithstudio.gurbani.Register");
            sendBroadcast(intent);
        }

        @Override
        public void playerPCMFeedBuffer(boolean b, int i, int i1) {

        }

        @Override
        public void playerStopped(int i) {

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

    public MyService() {
    }

    public static void stop() {
        if (multiPlayer != null) {
            multiPlayer.stop();
            multiPlayer=null;
        }
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
        isPlaying = true;
        stop();
        multiPlayer = new MultiPlayer(playerCallback);
        multiPlayer.playAsync(RADIO_STATION_URL);
        Log.i("Service123", "Played");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

        if (RADIO_STATION_URL != null) {
            Log.i("Service123", "starting play");
            startPlaying();
        }
        return super.onStartCommand(intent, flags, startId);

    }
}
