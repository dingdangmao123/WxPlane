package com.example.administrator.wxplane.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.wxplane.R;

/**
 * Created by Administrator on 2017/8/27 0027.
 */

public class scoreAdapter extends RecyclerView.Adapter<scoreAdapter.ViewHolder> {
    public String[] data = null;
    public scoreAdapter(String[] data) {
        this.data = data;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scoreitem,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.tv.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public ViewHolder(View view){
            super(view);
            tv = (TextView) view.findViewById(R.id.tv);
        }
    }
}
