package com.example.patrick.complexgui.fragments;

import android.app.ListFragment;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Patrick on 2017-12-24.
 */

public class VerticalListFragment extends ListFragment {

    private OnItemClick onItemClick;

   public interface OnItemClick {
        void onItemClicked(int position);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (onItemClick != null) {
            onItemClick.onItemClicked(position);
        }
    }
}