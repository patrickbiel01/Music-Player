package com.example.patrick.complexgui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.patrick.complexgui.adapter.RecyclerAdapter;
import com.example.patrick.complexgui.info.MusicFile;
import com.example.patrick.complexgui.info.MusicFileList;
import com.example.patrick.complexgui.info.Timer;

public class MusicActivity extends AppCompatActivity implements RecyclerAdapter.ItemClickListener{

    private ImageView cover;
    private TextView artist, song, songCurrentTime, songDuration;
    private Button play, back, forward;
    private SeekBar progressBar;
    private RecyclerAdapter adapter;
    private Toolbar toolbar;

    private Bundle bundle;
    private Timer timer;
    private int coverID, songID, position,screenDimension = 36;
    private String contentArtist, contentSong;
    private MediaPlayer mediaPlayer;
    private MusicFileList files = MainActivity.list;

    public static int mNotificationId = 001;
    private NotificationManager mNotifyMgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inflate chosen xml
        setContentView(R.layout.activity_music);

        /*
        Declare objects for UI elements in xml
         */

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back_icon);
        cover = (ImageView)findViewById(R.id.albumArt);
        artist = (TextView)findViewById(R.id.artist);
        song = (TextView)findViewById(R.id.song);
        songCurrentTime = (TextView)findViewById(R.id.currentTime);
        songDuration = (TextView)findViewById(R.id.songDuration);
        play = (Button)findViewById(R.id.playButton);
        back = (Button)findViewById(R.id.backButton);
        forward = (Button)findViewById(R.id.forwardButton);
        progressBar = (SeekBar) findViewById(R.id.progress);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.horizontalScroll);

        /*
        Retrieve information from intent that called the activity
         */

        Intent intent = getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {
            //Retrieve data from Bundle
            coverID = bundle.getInt(MainActivity.EXTRA_KEY_1);
            contentArtist = bundle.getString(MainActivity.EXTRA_KEY_2);
            contentSong = bundle.getString(MainActivity.EXTRA_KEY_3);
            position = bundle.getInt(MainActivity.EXTRA_KEY_4);
            songID = bundle.getInt(MainActivity.EXTRA_KEY_5);

            //Set retrieved information to UI components
            cover.setImageResource(coverID);
            song.setText(contentSong);
            artist.setText(contentArtist);
            toolbar.setTitle(contentSong);
        }

        /*
        Fill horizontal scrollbar with list
         */

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        adapter = new RecyclerAdapter(this, files, position);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //Create object to play song
        mediaPlayer = MediaPlayer.create(this, songID);
        //Set maximum value of scrollbar
        progressBar.setMax(mediaPlayer.getDuration());

        //Enable Wakelock
        mediaPlayer.setScreenOnWhilePlaying(true);

        //Setting the TextView to display Song Length
        int max = mediaPlayer.getDuration();
        String time = generateTime(max);
        songDuration.setText(time);
    }

    /*
    After UI is drawn or activity re-entered
     */

    @Override
    protected void onResume() {
        super.onResume();
        //Set UI elements to pre-specified User's choice
        readPreference();

        //Start Playing Music
        mediaPlayer.start();

        //If notification is present, clear it from the bar
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(mNotificationId);

        /*
        Run separate thread that will update progressbar
         */

        timer = new Timer(progressBar, songCurrentTime, mediaPlayer);
        timer.execute();

        /*
        If the user manually changes the progress of the SeekBar, move to that location
         */

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*
        When the back button on the toolbar is clicked goes back to List
         */

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                Intent i = new Intent(MusicActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        /*
        Move to next song on list
         */

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicFile file;
                if(position + 1 < files.size()) {
                    file = files.get(position + 1);
                    setUpIntent(file, position + 1);
                }else {
                    file = files.get(0);
                    setUpIntent(file, 0);
                }
                mediaPlayer.stop();
            }
        });

        /*
        Move to previous song on list
         */

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicFile file;
                if(position - 1 >= 0) {
                    file = files.get(position - 1);
                    setUpIntent(file, position - 1);
                }else {
                    file = files.get(files.size() - 1);
                    setUpIntent(file, files.size() - 1);
                }
                mediaPlayer.stop();
            }
        });

        /*
        Toggle the play Button to start or stop the song
            based on the previous state
         */

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    play.setBackgroundResource(R.mipmap.play);
                }
                else {
                    mediaPlayer.start();
                    play.setBackgroundResource(R.mipmap.pause);
                }
            }
        });
    }

    /*
    If activity is exited, but application not destroyed
     */

    @Override
    public void onPause(){
        super.onPause();
        //Generate Notification to re-enter activity
        createAndGenerateNotifcation();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mediaPlayer.stop();
    }


    /*
    ItemClick Listener for horizontal scrollbar
     */

    @Override
    public void onItemClick(View view, int position) {
        MusicFile text = files.get(position);
        //Stop playing music
        mediaPlayer.stop();
        //Launch specified activity
        setUpIntent(text, position);
    }

    /*
    Show settings icon in the toolbar
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_music, menu);
        return true;
    }

    /*
    Called when settings icon is clicked
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Get id of item clicked
        int id = item.getItemId();

        /*
        Call SettingsActivity if item clicked is the settings icon
         */

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Method that sets UI Elements to User Preference
    private void readPreference(){
        //Retrieve preferences from storage
        SharedPreferences prefs = getSharedPreferences(SettingsActivity.FILE_NAME, Context.MODE_PRIVATE);
        String prefToolbarColour = prefs.getString(SettingsActivity.SHARED_KEY_1, null);
        String prefTextColour = prefs.getString(SettingsActivity.SHARED_KEY_2, null);
        String prefProgressColour = prefs.getString(SettingsActivity.SHARED_KEY_3, null);

        /*
        Assign corresponding preferences with UI element
         */

        if(prefToolbarColour != null && prefTextColour != null) {
            //Change colour of toolbar
            for(int i = 0; i < SettingsActivity.COLOURS.length; i++) {
                if (prefToolbarColour.equals(getResources().getString(SettingsActivity.COLOURS[i]))) {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.toolbarColorBlue + i)));
                }
}

//Change colour of text
            for(int i = 0; i < SettingsActivity.TEXT_COLOURS.length; i++){
                if (prefTextColour.equals(getResources().getString(SettingsActivity.TEXT_COLOURS[i]))) {
                    song.setTextColor(ContextCompat.getColor(this, R.color.textColourBlack + i));
                    artist.setTextColor(ContextCompat.getColor(this, R.color.textColourBlack + i));
                    songDuration.setTextColor(ContextCompat.getColor(this, R.color.textColourBlack + i));
                    songCurrentTime.setTextColor(ContextCompat.getColor(this, R.color.textColourBlack + i));
                }
        }

        //Change colour of Progressbar
        for(int i = 0; i < SettingsActivity.PROGRESS_COLOURS.length; i++){
            if (prefProgressColour.equals(getResources().getString(SettingsActivity.PROGRESS_COLOURS[i]))) {
                progressBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter
                        (ContextCompat.getColor(this, R.color.seekColourBlue + i), PorterDuff.Mode.SRC_IN));
                progressBar.getThumb().setColorFilter(new PorterDuffColorFilter
                        (ContextCompat.getColor(this, R.color.seekColourBlue + i), PorterDuff.Mode.SRC_IN));
            }
        }

        }
    }

    /*
    Create and start a clone activity
     */

    private void setUpIntent(MusicFile file, int position){
        Intent i = new Intent(this, MusicActivity.class);
        i.putExtra(MainActivity.EXTRA_KEY_1, file.getImage());
        i.putExtra(MainActivity.EXTRA_KEY_2, file.getArtist());
        i.putExtra(MainActivity.EXTRA_KEY_3, file.getTitle());
        i.putExtra(MainActivity.EXTRA_KEY_4, position);
        i.putExtra(MainActivity.EXTRA_KEY_5, file.getSong());
        startActivity(i);
    }



    private void createAndGenerateNotifcation() {
        //Object to retrieve info from
        MusicFile file = files.get(position);

        //Fit title to one line
        String name = files.get(position).toString();
        if(name.length() >= screenDimension){
            StringBuilder sb = new StringBuilder();
            sb.append(name.substring(0, screenDimension - 3));
            sb.append("...");
            name = sb.toString();
        }

        /*
        Set attributes for custom Notification
         */

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notification);
        contentView.setImageViewResource(R.id.image, file.getImage());
        contentView.setTextViewText(R.id.title, name);

        /*
        Draw Custom Notification
         */
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.app_icon)
                        .setContent(contentView);

        /*
        Setup clone activity with additional information on the play time
         */

        Intent resultIntent = getPackageManager()
                .getLaunchIntentForPackage(getPackageName())
                .setPackage(null)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        //Stop the thread that updates progress
        timer.cancel(true);

        /*
        Will call clone activity when notification is clicked
         */

        //PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        mBuilder.setContentIntent(pendingIntent);

        //Show notification
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    /*
    Turns milliseconds into minutes and seconds
     */

    public static String generateTime(int currentTimeMilli){
        String time = new SimpleDateFormat("mm:ss:SSS").format(new Date(currentTimeMilli));
        return time;
    }

}