package com.purefaithstudio.gurbani;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class MyService extends Service {
    public static MediaPlayer player;
    private String RADIO_STATION_URL;
    private boolean isPlaying=false,isstopped=false;
    private boolean flag;
    public static final String SEND = "com.purefaithstudio.gurbani";


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();

     }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    //start Play
    public void startPlaying() {
       isPlaying=true;
        try {
            player.prepareAsync();
        }catch (IllegalStateException e){
            Log.e("harjas","illegal state excep");}
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();
                //isPlaying=true;
                flag=false;
                Intent intent=new Intent(SEND);
                intent.setAction("com.purefaithstudio.gurbani.Register");
                sendBroadcast(intent);
                Toast.makeText(getApplicationContext(),"Playing.....",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // Toast.makeText(getApplicationContext(),"Destroyed...",Toast.LENGTH_SHORT).show();
        if (player.isPlaying() || isPlaying) {
            //player.pause();

                player.stop();
                player.release();

                Toast.makeText(getApplicationContext(),"Playback Stop...",Toast.LENGTH_SHORT).show();
        }
        /*else
        player.release();
*/
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // Toast.makeText(getApplicationContext(), "OnstartCommnad",Toast.LENGTH_LONG).show();
         if(intent!=null) {
             Bundle b = intent.getExtras();
            // if (b != null)
                 this.RADIO_STATION_URL = b.getString("key");
         }
        else{
             this.stopSelf();
         }
        //Toast.makeText(getApplicationContext(), "" + RADIO_STATION_URL, Toast.LENGTH_SHORT).show();
        try {
            if(RADIO_STATION_URL!=null)
            player.setDataSource(RADIO_STATION_URL);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(RADIO_STATION_URL!=null) {
           startPlaying();
        }

        return super.onStartCommand(intent, flags, startId);

    }

}
