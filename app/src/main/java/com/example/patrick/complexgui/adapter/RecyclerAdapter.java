package com.example.patrick.complexgui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patrick.complexgui.info.MusicFile;
import com.example.patrick.complexgui.info.MusicFileList;
import com.example.patrick.complexgui.R;

import java.util.ArrayList;

/**
 * Created by Patrick on 2017-12-28.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<MusicFile> list;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public RecyclerAdapter(Context context, MusicFileList list, int position) {
        this.mInflater = LayoutInflater.from(context);
        ArrayList<MusicFile> copy = list;
        //copy.remove(position);
        this.list = copy;
    }

    // inflates the row layout from xml when needed
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_single, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        int coverID = list.get(position).getImage();
        String song = list.get(position).getTitle();
        holder.imageView.setImageResource(coverID);
        holder.textView.setText(song);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return list.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.recyclerImage);
            textView = (TextView) itemView.findViewById(R.id.recyclerText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return list.get(id).toString();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}