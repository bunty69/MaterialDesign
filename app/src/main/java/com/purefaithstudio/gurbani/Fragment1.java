package com.purefaithstudio.gurbani;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by MY System on 4/1/2015.
 */
public class Fragment1 extends Fragment implements MyArrayAdapter.ClickListener {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    MyArrayAdapter arrayAdapter;
    View rootView;
    String largeText;
    String pathText;
    private Intent i;
    private Bundle b;
    private int title;
    private String[] fontUrl = {"fonts/AnmolLipi2.ttf", "fonts/GurbaniHindi.ttf"};
    private String font;
    private Display display;
    private Information[] itemdata = {new Information("cOpeI swihb", R.drawable.khanda),
            new Information("suKmnI swihb", R.drawable.khanda),
            new Information("jpujI swihb", R.drawable.khanda),
            new Information("rhrwis swihb", R.drawable.khanda),
            new Information("AnMdU swihb", R.drawable.khanda),
            new Information("jwpu swihb", R.drawable.khanda),
            new Information("Awsw dI vwr", R.drawable.khanda),
            new Information("qÍ pRswid sv`Xy", R.drawable.khanda)};
    public static int height;

    public Fragment1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        display = getActivity().getWindowManager().getDefaultDisplay();
        setitemSize(display);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment1, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_one);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        arrayAdapter = new MyArrayAdapter(rootView.getContext(), itemdata);
        arrayAdapter.setClickListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(arrayAdapter);
        i = new Intent(rootView.getContext(), Second.class);
        b = new Bundle();
        return rootView;
    }

    @Override
    public void itemClicked(View view, int position) {
        switch (position) {
            case 0:
                title = R.string.title_activity_second;
                largeText = getString(R.string.large_text2);
                pathText = getString(R.string.chaupai);

                b.putString("key1", largeText);
                b.putString("key2", pathText);
                b.putInt("key3", title);
                i.putExtras(b);
                startActivity(i);
                break;
            case 1:
                title = R.string.title_activity_third;
                largeText = getString(R.string.large_text1);
                pathText = getString(R.string.sukhmani);
                b.putString("key1", largeText);
                b.putString("key2", pathText);
                b.putInt("key3", title);
                i.putExtras(b);
                startActivity(i);
                break;

            case 2:
                title = R.string.title_activity_fourth;

                largeText = getString(R.string.large_text3);
                pathText = getString(R.string.japji_sahib);

                b.putString("key1", largeText);
                b.putString("key2", pathText);
                b.putInt("key3", title);
                i.putExtras(b);
                startActivity(i);
                break;
            case 3:
                title = R.string.title_activity_fifth;
                largeText = getString(R.string.large_text4);
                pathText = getString(R.string.rehras);
                b.putString("key1", largeText);
                b.putString("key2", pathText);
                b.putInt("key3", title);
                i.putExtras(b);
                startActivity(i);
                break;
            case 4:
                title = R.string.title_activity_sixth;
                largeText = getString(R.string.large_text5);
                pathText = getString(R.string.anand_sahib);
                b.putString("key1", largeText);
                b.putString("key2", pathText);
                b.putInt("key3", title);
                i.putExtras(b);
                startActivity(i);
                break;
            case 5:
                title = R.string.title_activity_seven;
                largeText = getString(R.string.large_text6);
                pathText = getString(R.string.jaap_sahib);
                b.putString("key1", largeText);
                b.putString("key2", pathText);
                b.putInt("key3", title);
                i.putExtras(b);
                startActivity(i);
                break;
            case 6:
                title = R.string.title_activity_eight;
                largeText = getString(R.string.large_text7);
                pathText = getString(R.string.asadivar);
                b.putString("key1", largeText);
                b.putString("key2", pathText);
                b.putInt("key3", title);
                i.putExtras(b);
                startActivity(i);
                break;
            case 7:
                title = R.string.title_activity_nine;
                largeText = getString(R.string.large_text8);
                pathText = getString(R.string.tavprasad);
                b.putString("key1", largeText);
                b.putString("key2", pathText);
                b.putInt("key3", title);
                i.putExtras(b);
                startActivity(i);
                break;
        }
    }
    public void setitemSize(Display display) {
        height = display.getHeight();
        height = 4*(height / 100);
        Log.i("Size", height + "");
    }
}
