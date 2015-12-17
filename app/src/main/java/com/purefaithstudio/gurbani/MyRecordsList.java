package com.purefaithstudio.gurbani;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;

/**
 * Created by MY System on 12/16/2015.
 */
public class MyRecordsList extends ArrayAdapter<File> {
    private final Context context;
    private final int resource;
    File[] files;
    private TextView textView;

    public MyRecordsList(Context context, int resource, File[] objects) {
        super(context, resource, objects);
        this.files = objects;
        this.resource = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mview = convertView;
        if (mview == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mview = layoutInflater.inflate(resource, null);
        }
        File file=files[position];
        textView=(TextView)mview.findViewById(R.id.textview1);
        textView.setText(file.getName());
        Log.i("RecordShow",file.getName());
        return mview;
    }
}
