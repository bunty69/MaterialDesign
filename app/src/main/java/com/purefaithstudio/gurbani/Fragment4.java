package com.purefaithstudio.gurbani;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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

import com.shephertz.app42.paas.sdk.android.upload.Upload;

import java.util.ArrayList;


public class Fragment4 extends Fragment implements UpDownAdapter.ClickListener, SearchView.OnQueryTextListener {
    ArrayList<Upload.File> shabaddata;
    private UpDownAdapter upDownAdapter;
    private Context context;
    private RecyclerView recyclerView;
    SearchView searchView;
    Searcher searcher;

    public Fragment4() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MainActivity.apm.getExtra("ab");
        setHasOptionsMenu(true);
        searcher= new Searcher();
        shabaddata=new ArrayList<>();
        shabaddata=MainActivity.apm.getFileArrayList();//ye mil jata hai..na yes tera
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
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    public void itemClicked(View v, int position) {
        Log.i("Harsim",""+position);
    }

    public void search(String searchString)
    {
            upDownAdapter.updateList(searcher.search(searchString));
       // Log.i("Harsim", "fragsearch:"+searcher.search(searchString).get(0).getUserName());
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
}
