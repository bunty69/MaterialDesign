package com.purefaithstudio.gurbani;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class Audio_one extends ActionBarActivity implements OnClickListener {

    public String RADIO_STATION_URL;
    public AlertDialog.Builder alertDialogBuilder;
    Intent playService, recordService;
    boolean flag = false;
    private Button buttonPlay;
    private Button buttonStopPlay;
    private Button buttonRecord;
    private Button buttonStopRecord;
    private InputStream recordingStream = null;
    private boolean isRecording = false;
    private Bundle b1;
    private Bundle b2;
    private URLConnection connection;
    private RecorderThread recorderThread;
    private File folder;
    private String newname;
    private View view;
    private EditText edittext;
    private String name = null;
    private String str;
    private File foldernew;
    private boolean isPlaying = false;
    private AlertDialog alertDialog;
    private ProgressDialog progress;
    private boolean pause = false;
    private boolean isServiceOn = false;
    private PhoneStateListener phoneStateListener;
    private FileOutputStream fileOutputStream;
    private InputStream inputstream;
    private boolean isBackground = false;
    private long timer = 60000;
    private boolean registered = false;
    private boolean stop = false;
    private boolean progressCompleted = false;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            progress.dismiss();
            progressCompleted = true;
        }
    };
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_one);
        new PlayerControler(this);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                Log.i("admob", "failed");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i("admob", "loaded");
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.i("admob", "closed");
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    //Incoming call: Pause music
                    MyService.player.pause();
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    //Not in call: Play music
                    if (isServiceOn)
                        MyService.player.start();
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    //A call is dialing, active or on hold
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }


        initializeUIElements();
        alertDialogBuilder = new AlertDialog.Builder(this);
        Bundle b = getIntent().getExtras();
        if (b != null)
            this.RADIO_STATION_URL = b.getString("key");
        //Toast.makeText(getApplicationContext(), "" + RADIO_STATION_URL, Toast.LENGTH_SHORT).show();
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.getuserinputdialog, null);
        edittext = (EditText) view.findViewById(R.id.userinputedittext);
        alertDialogBuilder.setView(view);


        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                name = edittext.getText().toString();
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


    public void initializeUIElements() {
        buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonStopPlay = (Button) findViewById(R.id.buttonStopPlay);
        buttonRecord = (Button) findViewById(R.id.buttonRecord);
        buttonStopRecord = (Button) findViewById(R.id.buttonStopRecord);
        //set listeners
        buttonPlay.setOnClickListener(this);
        buttonStopPlay.setOnClickListener(this);
        buttonRecord.setOnClickListener(this);
        buttonStopRecord.setOnClickListener(this);
        playService = new Intent(this, MyService.class);
        recordService = new Intent(this, MyService2.class);
        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCanceledOnTouchOutside(true);
        progress.setCancelable(true);
        progress.setMessage("Wait");
    }


    @Override
    public void onClick(View v) {
        if (v == buttonPlay) {

            isPlaying = true;
            isServiceOn = true;
            progressCompleted = false;
            buttonPlay.setEnabled(false);
            buttonRecord.setEnabled(true);
            buttonStopPlay.setEnabled(true);
            if (pause == true) {
                pause = false;
                MyService.player.start();
            } else {
                b1 = new Bundle();
                b1.putString("key", RADIO_STATION_URL);
                playService.putExtras(b1);
                registerReceiver(receiver, new IntentFilter("com.purefaithstudio.gurbani.Register"));
                registered = true;
                startService(playService);
                flag = true;
                progress.show();
                //no use
                ProgressThread progressThread = new ProgressThread();
            }
        } else if (v == buttonStopPlay) {
            isPlaying = false;
            buttonStopPlay.setEnabled(false);
            buttonPlay.setEnabled(true);
            buttonRecord.setEnabled(false);
            if (MyService.player.isPlaying()) {
                pause = true;
                MyService.player.pause();
                //display ad
                if (interstitialAd.isLoaded())
                    interstitialAd.show();
            } else {
                unregisterReceiver(receiver);
                registered = false;
            }
            // buttonStopRecord.setEnabled(false);
            //stopService(playService);
            //stopService(recordService);
        } else if (v == buttonRecord) {
            buttonRecord.setEnabled(false);
            buttonStopRecord.setEnabled(true);

            recorderThread = new RecorderThread();
            recorderThread.start();

        } else if (v == buttonStopRecord) {
            if (isPlaying)
                buttonRecord.setEnabled(true);
            buttonStopRecord.setEnabled(false);
            stop = true;
            stopRecord();
            Toast.makeText(getApplicationContext(), "Recorded", Toast.LENGTH_LONG);
        }

    }

    private void stopRecord() {
        try {
            isRecording = false;
            if (inputstream != null) {

                inputstream.close();
                //Toast.makeText(getApplicationContext(), "Recording stream closed...", Toast.LENGTH_SHORT).show();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (stop)
            getInput();
        else move(folder + File.separator + "sample.mp3", "default.mp3");
    }

    private void startRecord() {
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

    @Override
    protected void onStop() {
        super.onStop();
        isBackground = true;
    }

    @Override
    protected void onDestroy() {
        if (isServiceOn == true) {
            if (registered)
                unregisterReceiver(receiver);//open bug here
            stopService(playService);
            if (!progressCompleted) {
                progress.dismiss();
                progressCompleted = true;
            }
            //  MyService.stop();
        }
        if (isRecording == true)
            stopRecord();
        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isBackground = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isBackground = true;
    }

    //get input file name
    public void getInput() {
        alertDialog.show();


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

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        Log.i("admob", "requested");
        interstitialAd.loadAd(adRequest);
    }

    public class RecorderThread extends Thread {
        @Override
        public void run() {
            super.run();
            isRecording = true;

            startRecord();
        }
    }

    public class ProgressThread extends Thread {
        @Override
        public void run() {
            int time = 0;
            int total = 100;
            while (time < total && flag == true) {
                time = time + 5;
                progress.setProgress(time);

                if (progress.isShowing() == false) flag = false;
            }
            progress.dismiss();
        }
    }

}

