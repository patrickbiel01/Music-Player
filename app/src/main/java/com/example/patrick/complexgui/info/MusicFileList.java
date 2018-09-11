package com.example.patrick.complexgui.info;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.patrick.complexgui.R;

import java.util.ArrayList;

/**
 * Created by Patrick on 2017-12-24.
 */

/*
Class to contain MusicFile objects
 */

public class MusicFileList extends ArrayList<MusicFile> implements Parcelable{

    //Adds songs to list
    public MusicFileList(){
        add(new MusicFile(R.mipmap.stadium_arcadium, R.raw.snow, "Red Hot Chili Peppers", "Snow"));
        add(new MusicFile(R.mipmap.stadium_arcadium, R.raw.dani_california, "Red Hot Chili Peppers", "Dani California"));
        add(new MusicFile(R.mipmap.by_the_way, R.raw.zephyr, "Red Hot Chili Peppers", "Zephyr"));
        add(new MusicFile(R.mipmap.by_the_way, R.raw.by_the_way, "Red Hot Chili Peppers", "By the Way"));
        add(new MusicFile(R.mipmap.californication, R.raw.scar_tissue, "Red Hot Chili Peppers", "Scar Tissue"));
        add(new MusicFile(R.mipmap.californication, R.raw.californication, "Red Hot Chili Peppers", "Californication"));
        add(new MusicFile(R.mipmap.californication, R.raw.around_the_world, "Red Hot Chili Peppers", "Around the World"));
    }

    /*
    Parcelable implementation: Describes how to write object when put into a bundle
     */

    private int mData;
    private int mCover;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
        out.writeInt(mCover);
    }

    public static final Parcelable.Creator<MusicFileList> CREATOR
            = new Parcelable.Creator<MusicFileList>() {
        public MusicFileList createFromParcel(Parcel in) {
            return new MusicFileList(in);
        }

        public MusicFileList[] newArray(int size) {
            return new MusicFileList[size];
        }
    };

    private MusicFileList(Parcel in) {
        mData = in.readInt();
        mCover = get(mData).getImage();
    }
}