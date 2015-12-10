package com.purefaithstudio.gurbani;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by MY System on 4/1/2015.
 */
public class Fragment2 extends Fragment implements ChannelsListAdapter.ClickListener {

    View rootView;
    ChannelData channelDatas[] = {new ChannelData("Sri Harimandir Sahib", "http://192.69.212.61:8020/stream"),
            new ChannelData("Simran", "http://192.69.212.61:8016/stream"),
            new ChannelData("AKHAND PATH-SRI GURU GRANTH SAHIB JI", "http://192.69.212.61:8018/stream"),
            new ChannelData("TAKHAT HAZUR SAHIB", "http://192.69.212.61:8038/stream"),
            new ChannelData("THE CLASSICS", "http://192.69.212.61:8501/stream"),
            new ChannelData("GURBANI KATHA", "http://192.69.212.61:8013/stream"),
            new ChannelData("STORIES", "http://192.69.212.61:8017/stream"),
            new ChannelData("DASMESH DARBAR", "http://192.69.212.61:8036/stream"),
            new ChannelData("GURDWARA DUKH NIWARAN SAHIB", "http://192.69.212.61:8037/stream"),
            new ChannelData("GURDWARA SAN JOSE", "http://192.69.212.61:8031/stream")};

    private RecyclerView recyclerView;
    private Intent playService;
    private PlayerControler playerControler;
    private ImageView playIcon;
    private int position;
    private boolean playIconEnabled = true;
    private TextView textview;
    private GifDrawable gifFromResource;
    private boolean registered;
    private GifImageView gifImageView;
    private Context context;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Track", "recieved");
            gifFromResource.stop();
            gifImageView.setVisibility(View.GONE);
            unRegisterForBroadCast();
        }
    };
    private boolean flag = false;
    private TextView currentlyPlayingText;
    private ImageView recordIcon;
    private boolean startRecord;

    public Fragment2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        playService = new Intent(context, MyService.class);
        playerControler = new PlayerControler(context, getActivity());
        MainActivity.setTrackerScreenName("Live stream");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment2, container, false);
        playIcon = (ImageView) rootView.findViewById(R.id.play);
        recordIcon = (ImageView) rootView.findViewById(R.id.record);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.channels_list);
        textview = (TextView) rootView.findViewById(R.id.random);
        gifImageView = (GifImageView) rootView.findViewById(R.id.gif_image);
        currentlyPlayingText = (TextView) rootView.findViewById(R.id.current_play_text);
        //gif for loading
        try {
            gifFromResource = new GifDrawable(getResources(), R.drawable.loading);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        ChannelsListAdapter channelsListAdapter = new ChannelsListAdapter(getActivity().getApplicationContext(), channelDatas);
        recyclerView.setLayoutManager(linearLayoutManager);
        channelsListAdapter.setClickListener(this);
        recyclerView.setAdapter(channelsListAdapter);
        playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playIconEnabled) {//state is stop
                    playIconEnabled = false;
                    playIcon.setImageResource(R.drawable.stop_blue);
                    playerControler.play(playService, channelDatas[position].link);
                } else {//state is playing
                    playIconEnabled = true;
                    playIcon.setImageResource(R.drawable.play);
                    try {
                        playerControler.stopPlay(playService);
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                if (MainActivity.interstitialAd.isLoaded()) {
                                    MainActivity.interstitialAd.show();
                                    // Toast.makeText(getApplicationContext(),"Showing Interstitial", Toast.LENGTH_SHORT).show();
                                } else {
                                    //AdRequest interstitialRequest = new
                                    AdRequest adRequest = new AdRequest.Builder().addTestDevice("ACCD210AA5526186C01EC1A5372676C6")
                                            .build();
                                    Log.i("admob", "requested");
                                    MainActivity.interstitialAd.loadAd(adRequest);
                                    //  Toast.makeText(getApplicationContext(),"Loading Interstitial", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        recordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startRecord) {
                    playerControler.startRecord();
                    startRecord = true;
                } else {
                    playerControler.stopRecord();
                    startRecord = false;
                }
            }
        });
        return rootView;
    }

    private void registerForBroadCast() {
        context.registerReceiver(receiver, new IntentFilter("com.purefaithstudio.gurbani.Register"));
        registered = true;
    }

    public void unRegisterForBroadCast() {
        context.unregisterReceiver(receiver);
        registered = false;
    }

    @Override
    public void itemClicked(View view, int position) {
        this.position = position - 1;
        registerForBroadCast();
        if (flag == false)
            gifImageView.setImageDrawable(gifFromResource);
        flag = true;
        gifFromResource.start();
        Log.i("Track", "gif Started");
        playerControler.setPlayerControllerText(currentlyPlayingText, channelDatas[position - 1].name);
        playerControler.play(playService, channelDatas[position - 1].link);
        playIconEnabled = false;
        playIcon.setImageResource(R.drawable.stop_blue);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (registered)
            unRegisterForBroadCast();
    }
}
