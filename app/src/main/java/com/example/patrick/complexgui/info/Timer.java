package com.example.patrick.complexgui.info;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.patrick.complexgui.MusicActivity;

/**
 * Created by Patrick on 2018-01-03.
 */

public class Timer extends AsyncTask<Void, Void, Void> {

    private SeekBar progressBar;
    private MediaPlayer player;
    private TextView songCurrentTime;

    //Constructor to get UI objects from Activity
    public Timer(SeekBar progressBar, TextView songCurrentTime,MediaPlayer player){
        this.progressBar = progressBar;
        this.songCurrentTime = songCurrentTime;
        this.player = player;
    }

    /*
        While the song is still playing, update the
            progress every 100 miliseconds
         */

    @Override
    protected Void doInBackground(Void... params) {
        while (player.getCurrentPosition() < player.getDuration()){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {}

            //Call for onProgressUpdate
            publishProgress();

            //If thread is cancelled, stop execution
            if (isCancelled()) {
                break;
            }
        }
        return null;
    }

    //Updates UI thread when called
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

        int currentPosition = player.getCurrentPosition();
        progressBar.setProgress(currentPosition);

        songCurrentTime.setText(MusicActivity.generateTime(currentPosition));
    }
}
