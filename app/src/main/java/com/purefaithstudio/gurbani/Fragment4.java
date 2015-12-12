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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.ImageView;

import com.shephertz.app42.paas.sdk.android.upload.Upload;
import java.util.ArrayList;

public class Fragment4 extends Fragment implements UpDownAdapter.ClickListener, SearchView.OnQueryTextListener, AudioManager.OnAudioFocusChangeListener {
    ArrayList<Upload.File> shabaddata;
    private UpDownAdapter upDownAdapter;
    private Context context;
    private RecyclerView recyclerView;
    SearchView searchView;
    SearchHandler searcher;
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
    private boolean togglePlay;
    private Intent intent, i;
    private Bundle b;
    private int currentPosition = -3;
    private View currentView;
    private boolean serviceStarted;

    public Fragment4() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MainActivity.apm.getExtra("ab");
        setHasOptionsMenu(true);
        searcher= new SearchHandler();
        shabaddata=new ArrayList<>();
        shabaddata=MainActivity.apm.getFileArrayList();//ye mil jata hai..na yes tera
        intent = new Intent(getActivity().getApplicationContext(), Mp3PlayerService.class);
        Log.i("Playercheck", "Intent created");
        getActivity().getApplicationContext().registerReceiver(receiver, new IntentFilter("com.purefaithstudio.gurbani.Mp3Player"));
        mAudioManager = (AudioManager) getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        MainActivity.setTrackerScreenName("shabad");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment4, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_F4);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        upDownAdapter= new UpDownAdapter(rootView.getContext(), shabaddata);//ismein hai ...datay enhi mil pata serach pe??
        upDownAdapter.setClickListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(upDownAdapter);
        i = new Intent(rootView.getContext(), Second.class);
        b = new Bundle();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            SearchView searchView= (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(View view, int position, String url) {
        if (!togglePlay) {
            if (pause || !serviceStarted)
                play(view, position,url);
        } else {
            if (serviceStarted)
                stop(view, position,url);
        }
    }

    public void search(String searchString)
    {
            upDownAdapter.updateList(searcher.search(searchString));
       // Log.i("Harsim", "fragsearch:"+SearchHandler.search(searchString).get(0).getUserName());
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        search(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    private void stop(View view, int position,String url) {
        togglePlay = false;
        if (Mp3PlayerService.player.isPlaying()) {
            //((ImageView) view).setImageResource(R.drawable.play);
            if (!(currentPosition == position)) {
                //((ImageView) currentView).setImageResource(R.drawable.play);
                getActivity().getApplicationContext().stopService(intent);
                Log.i("Playercheck", "Service stoped played next");
                serviceStarted = false;
                play(view, position,url);
            } else {
                Mp3PlayerService.player.pause();
                pause = true;
                Log.i("Playercheck", "pause called");
            }
        }
    }

    private void play(View view, int position,String url) {
        //start playing now
        togglePlay = true;
        //mp3player=new Mp3PlayerService(position);
        //((ImageView) view).setImageResource(R.drawable.stop_blue);
        if (!(currentPosition == position)) {
            currentPosition = position;
            //((ImageView) currentView).setImageResource(R.drawable.play);
            currentView = view;
            Bundle b = new Bundle();
            b.putString("url",url);
            intent.putExtras(b);
            getActivity().getApplicationContext().startService(intent);
            Log.i("Playercheck", "service started again");
        } else {
            //((ImageView) currentView).setImageResource(R.drawable.stop_blue);
            Mp3PlayerService.player.start();
            pause = false;
            Log.i("Playercheck", "play again/pause previously");
        }
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
}