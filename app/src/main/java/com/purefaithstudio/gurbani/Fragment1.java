package com.purefaithstudio.gurbani;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shephertz.app42.paas.sdk.android.upload.Upload;

import java.util.ArrayList;


/**
 * Created by MY System on 4/1/2015.
 */
public class Fragment1 extends Fragment implements MyArrayAdapter.ClickListener, AudioManager.OnAudioFocusChangeListener {
    public static int height;
    String largeText;
    String pathText;
    private Intent i;
    private Bundle b;
    private int title;
    private Display display;
    private Information[] itemdata = {new Information("cOpeI swihb", R.drawable.khanda),
            new Information("suKmnI swihb", R.drawable.khanda),
            new Information("jpujI swihb", R.drawable.khanda),
            new Information("rhrwis swihb", R.drawable.khanda),
            new Information("AnMdU swihb", R.drawable.khanda),
            new Information("jwpu swihb", R.drawable.khanda),
            new Information("Awsw dI vwr", R.drawable.khanda),
            new Information("qÍ pRswid sv`Xy", R.drawable.khanda)};

    private boolean togglePlay;
    private Intent intent;
    private int currentPosition = -3;
    private View currentView;
    private boolean serviceStarted;
    private String[] names = {"chaupaisahib", "sukhmanisahib", "japjisahib", "rehrassahib", "anandsahib", "jaapsahib", "asadivar", "tavprasad"};
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!serviceStarted)
                serviceStarted = true;
            if (Mp3PlayerService.oncomplete)
                serviceStarted = false;
        }
    };
    private boolean pause = true;
    private AudioManager mAudioManager;

    public Fragment1() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        display = getActivity().getWindowManager().getDefaultDisplay();
        setitemSize(display);
        intent = new Intent(getActivity().getApplicationContext(), Mp3PlayerService.class);
        Log.i("Playercheck", "Intent created");
        getActivity().getApplicationContext().registerReceiver(receiver, new IntentFilter("com.purefaithstudio.gurbani.Mp3Player"));
        mAudioManager = (AudioManager) getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        MainActivity.setTrackerScreenName("path");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment1, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_one);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(rootView.getContext(), itemdata);
        arrayAdapter.setClickListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(arrayAdapter);
        i = new Intent(rootView.getContext(), Second.class);
        b = new Bundle();
        return rootView;
    }

    @Override
    public void itemClicked(View view, int position) {
        if (view.getId() == R.id.path_play_iconID) {
            if (!togglePlay) {
                if (pause || !serviceStarted)
                    play(view, position);
            } else {
                if (serviceStarted)
                    stop(view, position);
            }
        } else {
            switch (position) {
                case 0:
                    title = R.string.title_activity_second;
                    largeText = getString(R.string.large_text2);
                    pathText = getString(R.string.chaupai);
                    addToIntent();
                    break;
                case 1:
                    title = R.string.title_activity_third;
                    largeText = getString(R.string.large_text1);
                    pathText = getString(R.string.sukhmani);
                    addToIntent();
                    break;

                case 2:
                    title = R.string.title_activity_fourth;
                    largeText = getString(R.string.large_text3);
                    pathText = getString(R.string.japji_sahib);
                    addToIntent();
                    break;
                case 3:
                    title = R.string.title_activity_fifth;
                    largeText = getString(R.string.large_text4);
                    pathText = getString(R.string.rehras);
                    addToIntent();
                    break;
                case 4:
                    title = R.string.title_activity_sixth;
                    largeText = getString(R.string.large_text5);
                    pathText = getString(R.string.anand_sahib);
                    addToIntent();
                    break;
                case 5:
                    title = R.string.title_activity_seven;
                    largeText = getString(R.string.large_text6);
                    pathText = getString(R.string.jaap_sahib);
                    addToIntent();
                    break;
                case 6:
                    title = R.string.title_activity_eight;
                    largeText = getString(R.string.large_text7);
                    pathText = getString(R.string.asadivar);
                    addToIntent();
                    break;
                case 7:
                    title = R.string.title_activity_nine;
                    largeText = getString(R.string.large_text8);
                    pathText = getString(R.string.tavprasad);
                    addToIntent();
                    break;
            }
        }
    }

    private void addToIntent() {
        b.putString("key1", largeText);
        b.putString("key2", pathText);
        b.putInt("key3", title);
        i.putExtras(b);
        startActivity(i);
    }

    private void stop(View view, int position) {
        togglePlay = false;
        if (Mp3PlayerService.player.isPlaying()) {
            ((ImageView) view).setImageResource(R.drawable.play);
            if (!(currentPosition == position)) {
                ((ImageView) currentView).setImageResource(R.drawable.play);
                getActivity().getApplicationContext().stopService(intent);
                Log.i("Playercheck", "Service stoped played next");
                serviceStarted = false;

                play(view, position);
            } else {
                Mp3PlayerService.player.pause();
                pause = true;
                Log.i("Playercheck", "pause called");
            }
        }
    }

    private void play(View view, int position) {
        //start playing now
        togglePlay = true;
        //mp3player=new Mp3PlayerService(position);
        ((ImageView) view).setImageResource(R.drawable.stop_blue);
        if (!(currentPosition == position)) {
            currentPosition = position;
            //((ImageView) currentView).setImageResource(R.drawable.play);
            currentView = view;
            Bundle b = new Bundle();
            b.putString("url",getUrl(position));
            //b.putInt("key", position);
            intent.putExtras(b);
            getActivity().getApplicationContext().startService(intent);
            Log.i("Playercheck", "service started again");
        } else {
            ((ImageView) currentView).setImageResource(R.drawable.stop_blue);
            Mp3PlayerService.player.start();
            pause = false;
            Log.i("Playercheck", "play again/pause previously");
        }
    }

    public void setitemSize(Display display) {
        height = display.getHeight();
        height = 4 * (height / 100);
        Log.i("Size", height + "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if (isMyServiceRunning(Mp3PlayerService.class))
            getActivity().getApplicationContext().stopService(intent);*/
        mAudioManager.abandonAudioFocus(this);
        getActivity().getApplicationContext().unregisterReceiver(receiver);
    }

    public Intent getIntent() {
        return intent;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

        if (focusChange <= 0) {
            //LOSS -> PAUSE
            if (Mp3PlayerService.player.isPlaying()) {
                Mp3PlayerService.player.pause();
                pause = true;
            }
            // Log.i("Playercheck", "pause called");
        } else {
            //GAIN -> PLAY
            if (pause) {
                pause = false;
                Mp3PlayerService.player.start();
            }
        }
    }
    private String getUrl(int position) throws NullPointerException {
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
    }
}
