package com.purefaithstudio.gurbani;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.shephertz.app42.paas.sdk.android.upload.Upload;

import java.io.IOException;
import java.util.ArrayList;

public class Mp3PlayerService extends Service {

    public static MediaPlayer player;
    public static boolean oncomplete;

    public Mp3PlayerService() {
    }

    public void init(String url) throws Exception {
        player = new MediaPlayer();
        try {
            player.setDataSource(url);
            // player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Log.i("Playercheck", "init completed");
        //play audio
        player.prepareAsync();
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i("Playercheck", "Player errors-" + what + extra);
                player.stop();
                player.release();
                player = null;
                return true;
            }
        });
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                try {
                    Thread.sleep(5000);
                    mp.start();
                    send();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.i("Playercheck", "mp.prepared");

            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
                send();
                oncomplete = true;
                Mp3PlayerService.this.stopSelf();
            }
        });
        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i("Playercheck", "buffering.." + percent);
                /*if (percent > 27 && !isPlayed) {
                    player.start();
                    isPlayed = true;
                    Log.i("Playercheck", "mp.stated");
                    send();
                }*/
            }
        });

    }

    private void send() {
        Intent intent = new Intent("com.purefaithstudio.gurbani");
        intent.setAction("com.purefaithstudio.gurbani.Mp3Player");
        sendBroadcast(intent);
    }

    /*private String getUrl(int position) throws NullPointerException {
        ArrayList<Upload.File> files = MainActivity.apm.getFileArrayList();
        String url = "";
        for (Upload.File file : files) {
            if (names[position].equals(file.getName())) {
                url = file.getUrl();
                Log.i("Playercheck", "Url founded pos:" + position + "  " + url);
                break;
            }
            Log.i("Playercheck", "Advance for");
        }
        return url;
    }*/


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
            String url = intent.getExtras().getString("url");
            try {
                init(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*int position = intent.getExtras().getInt("key");
            try {
                init(position);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        } else this.stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
            Log.i("Playercheck", "Service OnDestroy:" + Thread.currentThread().getId());
        }
    }
}