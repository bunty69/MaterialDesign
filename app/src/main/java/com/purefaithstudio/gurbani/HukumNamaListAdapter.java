package com.purefaithstudio.gurbani;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by MY System on 12/13/2015.
 */
public class HukumNamaListAdapter extends RecyclerView.Adapter<HukumNamaListAdapter.MyViewHolder> {
    String data[];
    Context context;
    ClickListener clickListener;

    public HukumNamaListAdapter(Context context, String[] data) {
        this.data = data;
        this.context = context;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_row_hukum, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.textView.setText(data[i]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public interface ClickListener {
        public void itemClicked(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textview1);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();
            if (clickListener != null) {
                clickListener.itemClicked(v, position);
            }
        }
    }
}
