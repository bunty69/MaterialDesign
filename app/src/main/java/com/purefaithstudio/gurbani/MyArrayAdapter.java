package com.purefaithstudio.gurbani;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by MY System on 3/20/2015.
 */
public class MyArrayAdapter extends RecyclerView.Adapter<MyArrayAdapter.ViewHolder> {
    public Context context;
    private String fontUrl = "fonts/AnmolLipi2.ttf";
    private Information[] itemsData;
    private ClickListener clickListener;

    public MyArrayAdapter(Context context, Information[] itemsdata) {
        this.itemsData = itemsdata;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row, null);

        ViewHolder viewholder = new ViewHolder(itemLayoutView);
        //Log.d("Harjas","Here i am!!");
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.textViewTitle.setText(itemsData[i].getTitle());
        viewHolder.textViewTitle.setTextSize(MainActivity.getitemTextSize());
        viewHolder.imgViewIcon.setImageResource(itemsData[i].getImageUrl());
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return itemsData.length;
    }

    public interface ClickListener {
        public void itemClicked(View view, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public ImageView imgViewIcon;
        public ImageView play;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.textview1);
            play = (ImageView) itemView.findViewById(R.id.path_play_iconID);
            if (MainActivity.font == "hindi") fontUrl = "fonts/GurbaniHindi.ttf";
            else if (MainActivity.font == "punjabi") fontUrl = "fonts/AnmolLipi2.ttf";
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontUrl);
            //typeface.isBold();
            textViewTitle.setTypeface(typeface, Typeface.BOLD);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getPosition();
                    if (clickListener != null) {
                        clickListener.itemClicked(v, getPosition());
                    }
                }
            });
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getPosition();
                    if (clickListener != null) {
                        clickListener.itemClicked(v, getPosition());
                    }
                }
            });
            imgViewIcon = (ImageView) itemView.findViewById(R.id.listicon);
        }

      /*  @Override
        public void onClick(View v) {
            int position = getPosition();
            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition());
            }

        }*/
    }
}
