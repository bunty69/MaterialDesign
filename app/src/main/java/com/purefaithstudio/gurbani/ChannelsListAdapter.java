package com.purefaithstudio.gurbani;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by MY System on 11/22/2015.
 */
public class ChannelsListAdapter extends RecyclerView.Adapter<ChannelsListAdapter.MyViewHolder> {
    ChannelData[] channelDatas;
    Context context;
    private ClickListener clickListener;

    public ChannelsListAdapter(Context context, ChannelData[] channelDatas) {
        this.channelDatas = channelDatas;
        this.context = context;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder,int i) {
            String name=channelDatas[i].name;
            if(channelDatas[i].name.length()>20) {
                name=name.substring(0,20)+"...";
            }
            myViewHolder.textView.setText(name);
            myViewHolder.textView.setTextSize(MainActivity.getitemTextSize()-5);
            myViewHolder.icon.setImageResource(R.drawable.khanda);

    }

    @Override
    public int getItemCount() {
        return channelDatas.length;
    }

    interface ClickListener {
        void itemClicked(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.listicon);
                textView = (TextView) itemView.findViewById(R.id.textview1);
                itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();
            if (clickListener != null)
                clickListener.itemClicked(v, position);
        }
    }
}
