package com.purefaithstudio.gurbani;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyService2 extends Service {
    private boolean isRecording = false;
    private InputStream recordingStream;
    private String RADIO_STATION_URL;
    private RecorderThread recorderThread;
    private int time;
    private HttpURLConnection connection;

    public MyService2() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onStart(Intent intent, int startId) {
    }


    public void startRecording() {
        BufferedOutputStream writer = null;
        try {
            URL url = new URL(RADIO_STATION_URL);
            Log.e("harjas0", "Here....");
            System.setProperty("http.keepAlive", "false");

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            if (connection == null)
                Toast.makeText(getApplicationContext(), "Null hai..", Toast.LENGTH_SHORT).show();
            connection.setUseCaches(false);
            connection.connect();
            Log.e("harjas1", "Here....");
            if (recordingStream == null)
                Log.e("harjas2", "Here....");
            Log.e("harjas3", "Here....");
            final String FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "songs";
            Log.e("harjas4", "Here....");
            File folder = new File(FOLDER_PATH);
            if (!folder.exists()) {
                folder.mkdir();
            }
            Log.e("harjas5", "Here....");
            writer = new BufferedOutputStream(new FileOutputStream(new File(FOLDER_PATH
                    + File.separator + "sample.mp3")));
            recordingStream = connection.getInputStream();

            Log.e("harjas6", "Here....");


            final int BUFFER_SIZE = 100;

            byte[] buffer = new byte[BUFFER_SIZE];

            while (recordingStream.read(buffer, 0, BUFFER_SIZE) != -1 && isRecording) {
                writer.write(buffer, 0, BUFFER_SIZE);
                writer.flush();
            }
            Log.e("harjas4", "Here....");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                recordingStream.close();

                writer.flush();
                Log.e("harjas5", "Here....");
                writer.close();
//                recordingStream.close();

                Log.e("harjas6", "Here....");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle b = intent.getExtras();
        this.RADIO_STATION_URL = b.getString("key2");
        Toast.makeText(getApplicationContext(), "Recording..." + RADIO_STATION_URL, Toast.LENGTH_SHORT).show();
        isRecording = true;
        recorderThread = new RecorderThread(this);

        recorderThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
        Toast.makeText(getApplicationContext(), "Recorded...", Toast.LENGTH_SHORT).show();
    }

    public void stopRecording() {
        try {
            isRecording = false;
            if (recordingStream != null) {

                recordingStream.close();
                Toast.makeText(getApplicationContext(), "Recording stream closed...", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class RecorderThread extends Thread {


        public RecorderThread(MyService2 myService2) {

        }

        @Override
        public void run() {
            super.run();
            isRecording = true;

            startRecording();
        }
    }


}
