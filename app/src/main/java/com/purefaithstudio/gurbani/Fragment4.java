package com.purefaithstudio.gurbani;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shephertz.app42.paas.sdk.android.upload.Upload;

import java.util.ArrayList;


public class Fragment4 extends Fragment implements UpDownAdapter.ClickListener {
    ArrayList<Upload.File> shabaddata;
    private UpDownAdapter upDownAdapter;
    private Context context;
    private RecyclerView recyclerView;
    Searcher searcher;

    public Fragment4() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MainActivity.apm.getExtra("ab");
        searcher= new Searcher();
        shabaddata=new ArrayList<>();
        shabaddata=MainActivity.apm.getFileArrayList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment4, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_F4);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        UpDownAdapter arrayAdapter = new UpDownAdapter(rootView.getContext(), shabaddata);
        arrayAdapter.setClickListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(arrayAdapter);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void itemClicked(View v, int position) {

    }

    public void search(String searchString)
    {
        upDownAdapter.updateList(searcher.search(searchString));
    }

}
