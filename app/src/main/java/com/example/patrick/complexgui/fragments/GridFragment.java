package com.example.patrick.complexgui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.patrick.complexgui.adapter.GridAdapter;
import com.example.patrick.complexgui.MainActivity;
import com.example.patrick.complexgui.info.MusicFileList;
import com.example.patrick.complexgui.R;

/**
 * Created by Patrick on 2017-12-25.
 */

public class GridFragment extends Fragment {
    MusicFileList list;
    private static final String KEY = MainActivity.KEY;
    private OnItemClick onItemClick;

    public interface OnItemClick {
        void onItemClicked(int position);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_fragment, container, false);
        if(list != null){
            setContent(list, view);
        }
        return view;
    }

    private void setContent(final MusicFileList list, View view){
        GridView gridView = (GridView) view.findViewById(R.id.gridview);
        GridAdapter adapter = new GridAdapter(getActivity(), list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClick.onItemClicked(position);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            list = (MusicFileList) bundle.get(KEY);
        }
    }
}
