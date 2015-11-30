package com.purefaithstudio.gurbani;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.shephertz.app42.paas.sdk.android.upload.Upload;

import java.io.IOException;
import java.util.ArrayList;

public class Mp3PlayerService extends Service {

    public static MediaPlayer player;
    private String[] names = {"chaupaisahib", "sukmanisahib", "japjisahib", "rehrassahib", "anandsahib", "jaapsahib", "asadivar", "tavprasad"};
    private int currentPosition = -5;

    public Mp3PlayerService() {

    }

    public void init(int position) {
        String url = "";
        player = new MediaPlayer();
        url = getUrl(position);
        try {
            player.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Playercheck", "init completed");
        playAudio();
    }

    private String getUrl(int position) {
        ArrayList<Upload.File> files = Fragment1.apm.getFileArrayList();
        String url = "";
        for (Upload.File file : files) {
            if (names[position].equals(file.getName())) {
                url = file.getUrl();
                break;
            }
            Log.i("Playercheck", "Advance for");
        }
        return url;
    }

    public void playAudio() {
        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                Log.i("Playercheck", "mp.start");
                Intent intent = new Intent("com.purefaithstudio.gurbani");
                intent.setAction("com.purefaithstudio.gurbani.Mp3Player");
                sendBroadcast(intent);
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int position = intent.getExtras().getInt("key");
            init(position);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player.isPlaying()) {
            player.stop();
            player.release();
            Log.i("Playercheck", "Service OnDestroy:"+ Thread.currentThread().getId());
        }
    }
}