package com.example.patrick.complexgui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.patrick.complexgui.SettingsActivity;
import com.example.patrick.complexgui.info.MusicFile;
import com.example.patrick.complexgui.info.MusicFileList;
import com.example.patrick.complexgui.R;

/**
 * Created by Patrick on 2017-12-25.
 */

public class GridAdapter extends BaseAdapter {
        private Context mContext;
        private MusicFileList list;
        private TextView textView;

        public GridAdapter(Context c,MusicFileList list) {
            mContext = c;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                grid = inflater.inflate(R.layout.grid_single, null);
                textView = (TextView) grid.findViewById(R.id.grid_text);
                ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
                MusicFile file = list.get(position);
                textView.setText(file.toString());
                imageView.setImageResource(file.getImage());
                readPreference();
            } else {
                grid = convertView;
            }

            return grid;
        }

    //Method that sets UI Elements to User Preference
    private void readPreference(){
        //Retrieve preferences from storage
        SharedPreferences prefs = mContext.getSharedPreferences(SettingsActivity.FILE_NAME, Context.MODE_PRIVATE);
        String prefTextColour = prefs.getString(SettingsActivity.SHARED_KEY_2, null);

        /*
        Assign corresponding preferences with UI element
         */

        if(prefTextColour != null) {
            for(int i = 0; i < SettingsActivity.TEXT_COLOURS.length; i++) {
                //Change colour of toolbar
                if (prefTextColour.equals(mContext.getResources().getString(SettingsActivity.TEXT_COLOURS[i]))) {
                   textView.setTextColor(ContextCompat.getColor(mContext, R.color.textColourBlack + i));
                }
            }
        }
    }
}
