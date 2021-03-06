package com.purefaithstudio.gurbani;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;

import java.io.File;


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

    private boolean registered;

    private Context context;
    private boolean flag = false;
    private TextView currentlyPlayingText;
    private ToggleButton recordIcon;
    private boolean startRecord;
    private Wait wait=null;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Track", "recieved");
            if(wait != null  && wait.isAdded() && wait.isResumed()) {
                wait.dismiss();
                unRegisterForBroadCast();
            }
        }
    };
    private RelativeLayout controller;

    public Fragment2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        playService = new Intent(context, MyService.class);
        playerControler = new PlayerControler(context, getActivity());
        MainActivity.setTrackerScreenName("Live stream");
        wait = new Wait();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment2, container, false);
        playIcon = (ImageView) rootView.findViewById(R.id.play);
        recordIcon = (ToggleButton) rootView.findViewById(R.id.record);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.channels_list);

        currentlyPlayingText = (TextView) rootView.findViewById(R.id.current_play_text);
        controller = (RelativeLayout) rootView.findViewById(R.id.controller);
        //loading
        //progressBar = (ProgressBar)rootView.findViewById(R.id.progress_circle);


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


        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    controller.setVisibility(View.GONE);
                }
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    controller.setVisibility(View.VISIBLE);
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
        this.position = position;
        controller.setVisibility(View.VISIBLE);
        registerForBroadCast();

        flag = true;

        Log.i("Track", "gif Started");
        wait.show(getFragmentManager(), "tag1");
        playerControler.setPlayerControllerText(currentlyPlayingText, channelDatas[position].name);
        playerControler.play(playService, channelDatas[position].link);
        playIconEnabled = false;
        playIcon.setImageResource(R.drawable.stop_blue);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (MainActivity.isMyServiceRunning(MyService.class, context))
            context.stopService(playService);
        if (registered)
            unRegisterForBroadCast();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment2, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.show) {
            Fragment fragment = new ShowRecordedFiles();
            Bundle bundle = new Bundle();
            bundle.putString("path", Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Recording");
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("tag").commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
