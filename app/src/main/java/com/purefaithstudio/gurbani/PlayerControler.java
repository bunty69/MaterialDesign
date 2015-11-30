package com.purefaithstudio.gurbani;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
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
    private boolean stopRecord;
    private boolean isRecording;
    private InputStream inputstream;
    private File folder;
    private FileOutputStream fileOutputStream;
    private File foldernew;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private String name;
    private String newname;

    public PlayerControler(Context context,Activity activity) {
        this.context = context;
        setAlertDialog(activity);
    }

    private void setAlertDialog(Activity activity) {
        alertDialogBuilder = new AlertDialog.Builder(activity);
        final View view= LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.getuserinputdialog,null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                name = ((EditText)view.findViewById(R.id.userinputedittext)).getText().toString();
                newname = name + ".mp3";
                move(folder + File.separator + "sample.mp3", newname);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });
        alertDialog = alertDialogBuilder.create();
    }

    public void getInput() {
        alertDialog.show();
    }
    public void play(Intent playService, String RADIO_STATION_URL) {
        this.RADIO_STATION_URL = RADIO_STATION_URL;
        pause = false;
        b1 = new Bundle();
        isPlaying=true;
        b1.putString("key", RADIO_STATION_URL);
        playService.putExtras(b1);
        //registerReceiver(receiver, new IntentFilter("com.purefaithstudio.gurbani.Register"));
        registered = true;
        context.startService(playService);
        Log.i("Tag", "Service starred");
        flag = true;
        //progress.show();
        //no use
        //   ProgressThread progressThread = new ProgressThread();

    }

    public void stopPlay(Intent playService) {
        isPlaying = false;
        pause = true;
        context.stopService(playService);
        //MyService.replay = true;
        //MyService.multiPlayer.stop();
        //unregisterReceiver(receiver);
        registered = false;

    }

    public void startRecord() {
        recorderThread = new RecorderThread();
        recorderThread.start();
    }

    public void stopRecord() {
        if (isPlaying)
            stopRecord = true;
        stopRecordIO();
        Toast.makeText(context, "Recorded", Toast.LENGTH_LONG);
    }

    private void stopRecordIO() {
        try {
            isRecording = false;
            if (inputstream != null) {

                inputstream.close();
                //Toast.makeText(getApplicationContext(), "Recording stream closed...", Toast.LENGTH_SHORT).show();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (stopRecord)
            getInput();
        else move(folder + File.separator + "sample.mp3", "default.mp3");
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

    public void setPlayerControllerText(TextView currentlyPlayingText, String text) {
        if (text.length() > 15)
            text = text.substring(0, 15) + "...";
        currentlyPlayingText.setText(text);
    }

    public class RecorderThread extends Thread {
        @Override
        public void run() {
            super.run();
            isRecording = true;
            startRecordIO();
        }
    }
    public void move(String path, String newname) {
        File old = new File(path);
        foldernew = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Recording");
        if (!foldernew.exists()) {
            foldernew.mkdir();
        }
        File to = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Recording/" + newname);
        old.renameTo(to);
    }
}


