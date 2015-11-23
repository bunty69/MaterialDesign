package com.purefaithstudio.gurbani;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by MY System on 11/22/2015.
 */
public class PlayerControler {

    private static String RADIO_STATION_URL = null;
    private final Context context;
    private Bundle b1;
    private boolean pause;
    private boolean registered;
    private boolean flag;
    private boolean isPlaying;
    private RecorderThread recorderThread;
    private boolean stop;
    private boolean isRecording;
    private InputStream inputstream;
    private File folder;
    private FileOutputStream fileOutputStream;

    public PlayerControler(Context context) {
        this.context = context;
    }

    public void play(Intent playService, String RADIO_STATION_URL) {
        this.RADIO_STATION_URL = RADIO_STATION_URL;
        if (pause == true) {
            pause = false;
            MyService.player.start();
        } else {
            b1 = new Bundle();
            b1.putString("key", RADIO_STATION_URL);
            playService.putExtras(b1);
            //registerReceiver(receiver, new IntentFilter("com.purefaithstudio.gurbani.Register"));
            registered = true;
            context.startService(playService);
            flag = true;
            //progress.show();
            //no use
            //   ProgressThread progressThread = new ProgressThread();
        }

    }

    public void stopPlay() {
        isPlaying = false;
        if (MyService.player.isPlaying()) {
            pause = true;
            MyService.player.pause();

        } else {
            //unregisterReceiver(receiver);
            registered = false;
        }
    }

    public void startRecord() {
        recorderThread = new RecorderThread();
        recorderThread.start();
    }

    public void stopRecord() {
        if (isPlaying)
            stop = true;
        stopRecord();
        Toast.makeText(context, "Recorded", Toast.LENGTH_LONG);
    }

    private void startRecordIO() {
        try {
            URL url = new URL(RADIO_STATION_URL);
            inputstream = url.openStream();
            final String FOLDER_PATH = Environment
                    .getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "songs";

            folder = new File(FOLDER_PATH);
            if (!folder.exists()) {
                folder.mkdir();
            }
            fileOutputStream = new FileOutputStream(new File(FOLDER_PATH + File.separator + "sample.mp3"));
            int c;

            while ((c = inputstream.read()) != -1 && isRecording) {

                ((FileOutputStream) fileOutputStream).write(c);

            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public class RecorderThread extends Thread {
        @Override
        public void run() {
            super.run();
            isRecording = true;
            startRecordIO();
        }
    }
}


