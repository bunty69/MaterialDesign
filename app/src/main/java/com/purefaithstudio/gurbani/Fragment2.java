package com.purefaithstudio.gurbani;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * Created by MY System on 4/1/2015.
 */
public class Fragment2 extends Fragment implements ChannelsListAdapter.ClickListener {

    View rootView;

    //"http://192.69.212.61:8020/stream"-harimandir sahib
    /*String[] string = new String[]{"Select", "Sri Harimandir Sahib", "Simran","AKHAND PATH-SRI GURU GRANTH SAHIB JI",
    "TAKHAT HAZUR SAHIB","THE CLASSICS","GURBANI KATHA","STORIES","DASMESH DARBAR","GURDWARA DUKH NIWARAN SAHIB","GURDWARA SAN JOSE"};
        String[] channel_link = new String[]{"http://192.69.212.61:8020/stream",
            "http://192.69.212.61:8016/stream","http://192.69.212.61:8018/stream","http://192.69.212.61:8038/stream",
        "http://192.69.212.61:8501/stream","http://192.69.212.61:8013/stream","http://192.69.212.61:8017/stream",
        "http://192.69.212.61:8036/stream","http://192.69.212.61:8037/stream","http://192.69.212.61:8031/stream"};
        */
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
    Spinner spinner;
    int check = 0;
    private Bundle b1;
    private Intent i1;
    private RecyclerView recyclerView;
    private Intent playService;
    private PlayerControler playerControler;
    private ImageView playIcon;
    private int position;
    private boolean playIconEnabled = true;

    public Fragment2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playService = new Intent(getActivity().getApplicationContext(), MyService.class);
        playerControler = new PlayerControler(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment2, container, false);
        playIcon = (ImageView) rootView.findViewById(R.id.play);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.channels_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        ChannelsListAdapter channelsListAdapter = new ChannelsListAdapter(getActivity().getApplicationContext(), channelDatas);
        recyclerView.setLayoutManager(linearLayoutManager);
        channelsListAdapter.setClickListener(this);
        recyclerView.setAdapter(channelsListAdapter);
        playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playIconEnabled == true) {
                    playIconEnabled = false;
                    playIcon.setImageResource(R.drawable.stop1);
                    playerControler.play(playService, channelDatas[position].link);
                } else {
                    playIconEnabled = true;
                    playIcon.setImageResource(R.drawable.play1);
                    playerControler.stopPlay();
                }
            }
        });
        return rootView;
    }

    @Override
    public void itemClicked(View view, int position) {
        this.position = position;
        playerControler.play(playService, channelDatas[position].link);
        playIconEnabled = false;
        playIcon.setImageResource(R.drawable.stop1);
    }
}
