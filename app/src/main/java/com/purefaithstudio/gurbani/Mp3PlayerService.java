package com.purefaithstudio.gurbani;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class Mp3PlayerService extends Service implements MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener,MediaPlayer.OnPreparedListener,MediaPlayer.OnBufferingUpdateListener {

    public static MediaPlayer player;
    public static boolean oncomplete,isprepared=false;
    private boolean isMpPlayed=false;
    private ToggleListener toggleListener;

    public Mp3PlayerService() {
    }

    public void setToggleListener(ToggleListener toggleListener) {
        this.toggleListener = toggleListener;
    }

    public void init(String url) throws Exception {
        player = new MediaPlayer();
        try {
            player.setDataSource(url);
            Log.i("RecordShow", "datasource " + url);
            // player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Log.i("Playercheck", "mp.init completed");
        //play audio
        player.prepareAsync();
        player.setOnErrorListener(this);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnBufferingUpdateListener(this);

    }

    private void send(Boolean run) {
        Intent intent = new Intent("com.purefaithstudio.gurbani");
        intent.putExtra("run",run);
        intent.setAction("com.purefaithstudio.gurbani.Mp3Player");
        sendBroadcast(intent);
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
            String url = intent.getExtras().getString("url");
            try {
                init(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("Playercheck","mpservice is working");
            /*int position = intent.getExtras().getInt("key");
            try {
                init(position);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        } else {
            Log.i("Playercheck","mpservice Intent Empty destroying self");
            this.stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (player != null) {
                player.stop();
                player.release();
                isprepared=false;
                isMpPlayed = false;
                player = null;
                Log.i("Playercheck", "mpService OnDestroy:" + Thread.currentThread().getId());
            }

        } catch (Exception e) {
            Log.i("Playercheck","mp error on destroy MP3Service");
            e.printStackTrace();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

        /*if (isprepared) {
            long duration = mp.getDuration();
            duration = 100 / ((duration / 1000) / 60);
            Log.i("buffer", "buffering.." + percent + "  " + duration);
            if (percent > duration && duration != 0) {
                if (!mp.isPlaying() && !isMpPlayed) {
                    mp.start();
                    Log.i("Playercheck", "mp.started from buffer");
                    //Toast.makeText(getApplicationContext(), "Playing", Toast.LENGTH_LONG);
                    send(true);
                }
                isMpPlayed=true;
            }
        }*/
        //send(true);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.stop();
        mp.release();
        isprepared=false;
        player = null;
        Log.i("Playercheck","stop complete");
        // Toast.makeText(getApplicationContext(), "finished", Toast.LENGTH_LONG);
        send(false);
        oncomplete = true;
        Mp3PlayerService.this.stopSelf();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i("Playercheck", "Player errors-" + what + extra);
        //Toast.makeText(getApplicationContext(),"Unable to connect Try again",Toast.LENGTH_LONG);
        mp.stop();
        mp.release();
        isprepared=false;
        mp = null;
        //send(false);
        //Mp3PlayerService.this.stopSelf();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        Log.i("Playercheck", "mp.prepared");
        // Toast.makeText(getApplicationContext(), "Ready To Play", Toast.LENGTH_SHORT);
            mp.start();
            isprepared=true;
            send(true);
            Log.i("Playercheck", "mp.start 0");

    }

    public interface ToggleListener {
        public void check();
    }
}