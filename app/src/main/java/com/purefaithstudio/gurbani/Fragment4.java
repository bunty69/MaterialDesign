package com.purefaithstudio.gurbani;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.shephertz.app42.paas.sdk.android.upload.Upload;

import java.util.ArrayList;

public class Fragment4 extends Fragment implements UpDownAdapter.ClickListener, SearchView.OnQueryTextListener, AudioManager.OnAudioFocusChangeListener {
    ArrayList<Upload.File> shabaddata;
    SearchView searchView;
    SearchHandler searcher;
    private UpDownAdapter upDownAdapter;
    private Context context;
    private RecyclerView recyclerView;
    private PlayerControllerMp3 playerController;
    private Wait wait;
    private boolean pause = true;
    private AudioManager mAudioManager;
    private View currentView;
    private boolean serviceStarted;
    private ImageView playIcon;
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            wait.dismiss();
            Toggler.checkSetState(playIcon);
        }
    };
    private RelativeLayout controller;
    private TextView currentlyPlayingText;
    private boolean playIconEnabled = true;
    private String name = "None Selected";
    private int Pos = 0;
    private View rootView;
    private Boolean lostfocus=false;

    public Fragment4() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity().getApplicationContext();
            setHasOptionsMenu(true);
            searcher = new SearchHandler();
            shabaddata = new ArrayList<>();
            shabaddata = MainActivity.apm.getFileArrayList();
            Log.i("Playercheck", "Intent created");
            context.registerReceiver(receiver, new IntentFilter("com.purefaithstudio.gurbani.Mp3Player"));
            mAudioManager = (AudioManager) getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            MainActivity.setTrackerScreenName("shabad");
            wait = new Wait();
            playerController = new PlayerControllerMp3(getActivity().getApplicationContext());
            Toggler.resetStates();
            if (!NetworkConnectionDetector.isConnectingToInternet(context) || !MainActivity.apm.load)
                Toast.makeText(context, "Failed To Connect!!! Check your Internet Connection", Toast.LENGTH_LONG);
        } catch (Exception e) {
            Log.i("AppNitnem", "cannot create Fragment1");
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            rootView = inflater.inflate(R.layout.fragment_fragment4, container, false);
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyler_F4);
            LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
            upDownAdapter = new UpDownAdapter(rootView.getContext(), shabaddata);//ismein hai ...datay enhi mil pata serach pe??
            upDownAdapter.setClickListener(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(upDownAdapter);
            playIcon = (ImageView) rootView.findViewById(R.id.play4);
            controller = (RelativeLayout) rootView.findViewById(R.id.controller4);
            currentlyPlayingText = (TextView) rootView.findViewById(R.id.current_play_text4);
            currentlyPlayingText.setText(name);
            playIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Toggler.ifStateNull()) {
                        if (Toggler.check()) {//we ar pausing audio
                            playerController.audioPause();
                            Toggler.checkSetState(playIcon);
                        } else {//state is playing
                            playerController.audioResume();
                            Toggler.checkSetState(playIcon);
                            try {
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
                    } else
                        Toast.makeText(context, "Select Any Item to Play!!", Toast.LENGTH_SHORT).show();
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
                                             }
            );
            return rootView;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (MainActivity.isMyServiceRunning(Mp3PlayerService.class, context))
            playerController.stop();
        mAudioManager.abandonAudioFocus(this);
        getActivity().getApplicationContext().unregisterReceiver(receiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fagement4, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_bar) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(View view, int position, String url) {
        this.Pos = position;
        controller.setVisibility(View.VISIBLE);
        String shbddata=shabaddata.get(Pos).getName();
        if(shbddata.length()>22)
            shbddata=shbddata.substring(0,22)+"...";
        currentlyPlayingText.setText(shbddata);
        playAudio(url);
    }

    public void search(String searchString) {
        if(searchString.equals(""))
        {
            shabaddata = MainActivity.apm.getFileArrayList();
        }
        shabaddata = searcher.search(searchString);
        upDownAdapter.updateList(shabaddata);
        // Log.i("Harsim", "fragsearch:"+SearchHandler.search(searchString).get(0).getUserName());
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        search(s.replace(" ", "_"));
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    private void playAudio(String path) {
        Log.i("RecordShow", "play Path " + path);
        wait = new Wait();
        wait.show(getFragmentManager(), "tag");
        playerController.type=1;
        playerController.play(path);
    }


    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange <= 0) {
            //LOSS -> PAUSE
            if (Toggler.check() && !Toggler.ifStateNull()) {
                playerController.audioPause();
                lostfocus = true;
            }
            // Log.i("Playercheck", "pause called");
        } else {
            //GAIN -> PLAY
            if(lostfocus) {
                if (!Toggler.check() && !Toggler.ifStateNull())
                    playerController.audioResume();
                    lostfocus=false;
            }
        }
    }
}