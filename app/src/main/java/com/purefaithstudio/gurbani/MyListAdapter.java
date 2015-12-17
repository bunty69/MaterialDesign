package com.purefaithstudio.gurbani;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by MY System on 4/7/2015.
 */
public class MyListAdapter extends ArrayAdapter<String> {
    private Typeface typeface;
    private String fontUrl = "fonts/AnmolLipi2.ttf";
    private String[] list;
    private Context mContext;
    private int id;


    public MyListAdapter(Context context, int textViewResourceId, String[] list) {
        super(context, textViewResourceId, list);
        switch (MainActivity.font) {
            case "hindi":
                fontUrl = "fonts/GurbaniHindi.ttf";
                break;
            case "punjabi":
                fontUrl = "fonts/AnmolLipi2.ttf";
                break;
        }
        if (MainActivity.font != "roman")
            typeface = Typeface.createFromAsset(getContext().getAssets(), fontUrl);
        this.id = textViewResourceId;
        this.mContext = context;
        this.list = list;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = convertView;
        if (mView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }
        TextView t = (TextView) mView.findViewById(R.id.textView);
        if (MainActivity.font != "roman")
            t.setTypeface(typeface, Typeface.BOLD);
        t.setTextSize(MainActivity.getitemTextSize());
        t.setText(list[position]);
        return mView;
    }
}
