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

/**
 * Created by MY System on 4/1/2015.
 */
public class Fragment3 extends Fragment implements MyArrayAdapter.ClickListener {
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private MyArrayAdapter arrayAdapter;
    View rootView;
    private Information[] itemdata = {new Information("Gallary-1", R.drawable.image),
            new Information("Gallary-2", R.drawable.image)
    };

    public Fragment3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment3, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_three);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        arrayAdapter = new MyArrayAdapter(rootView.getContext(), itemdata);
        arrayAdapter.setClickListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(arrayAdapter);

        return rootView;
    }

    @Override
    public void itemClicked(View view, int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(rootView.getContext(), Gallary_one.class));
                break;
            case 1:
                startActivity(new Intent(rootView.getContext(), Gallary_two.class));
                break;
        }
    }
}
