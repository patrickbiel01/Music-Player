package com.example.patrick.complexgui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.patrick.complexgui.info.MusicFile;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Button mToolBarColour,mTextColour, mSeekColour;
    private Toolbar toolbar;
    private SharedPrefs holder;
    private ArrayList<String> textColour;

    //Keys for SharedPreference
    public static final String KEY = "key";
    public static final String FILE_NAME = "one", SHARED_KEY_1 = "one",
            SHARED_KEY_2 = "two", SHARED_KEY_3 = "three";
    //List of Colour in the Toolbar Colour Menu
    public static final int[] COLOURS = {R.string.color_blue, R.string.color_ForestGreen, R.string.color_red};
    //List of Colours in the Text Colour Menu
    public static final int[] TEXT_COLOURS = {R.string.textBlack, R.string.textGrey};
    public static final int[] PROGRESS_COLOURS = {R.string.seekBlue, R.string.seekGreen, R.string.seekRed};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inflate chosen xml
        setContentView(R.layout.activity_settings);

        /*
        Declare objects for UI elements in xml
         */

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back_icon);
        mToolBarColour = (Button) findViewById(R.id.toolbarColour);
        mTextColour = (Button) findViewById(R.id.textColour);
        mSeekColour = (Button) findViewById(R.id.progressColour);

        //Object for inner class
        holder = new SharedPrefs();
        //Initiate reference file for SharedPreferences
        pref = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        //Transform String-resource (int) array into a String Array
        textColour = new ArrayList<>();
        for(int text:TEXT_COLOURS){
            textColour.add(getResources().getString(text));
        }

    }

    /*
    After UI is drawn or activity re-entered
     */

    @Override
    protected void onResume() {
        super.onResume();
        //Set UI elements to pre-specified User's choice
        holder.readPreference();

        /*
        When the back button on the toolbar is clicked goes back to List
         */

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //When the Toolbar Colour Button is clicked...
        mToolBarColour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Display the menu
                 */

                final PopupMenu menu = new PopupMenu(SettingsActivity.this, v);
                menu.inflate(R.menu.toolbar_colour);
                menu.show();

                //When an item on the menu is clicked,
                // set preference to the item chosen
                menu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        editor = pref.edit();
                        editor.putString(SHARED_KEY_1, (String) item.getTitle());
                        editor.apply();
                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                        menu.dismiss();
                        holder.readPreference();
                        return true;
                    }
                });

            }
        });

        //When the Text Colour Button is clicked...
        mTextColour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Display the menu
                 */

                final PopupMenu menu = new PopupMenu(SettingsActivity.this, v);
                menu.inflate(R.menu.text_colour);
                menu.show();

                //When an item on the menu is clicked,
                // set preference to the item chosen
                menu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        editor = pref.edit();
                        editor.putString(SHARED_KEY_2, (String) item.getTitle());
                        editor.apply();
                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                        menu.dismiss();
                        holder.readPreference();
                        return true;
                    }
                });
            }
        });

        //When the Progressbar Colour Button is clicked...
        mSeekColour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Display the menu
                 */

                final PopupMenu menu = new PopupMenu(SettingsActivity.this, v);
                menu.inflate(R.menu.slider_menu);
                menu.show();

                //When an item on the menu is clicked,
                // set preference to the item chosen
                menu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        editor = pref.edit();
                        editor.putString(SHARED_KEY_3, (String) item.getTitle());
                        editor.apply();
                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                        menu.dismiss();
                        holder.readPreference();
                        return true;
                    }
                });

            }
        });
    }

     /*
    If activity is exited, but application not destroyed
     */

    @Override
    protected void onPause() {
        super.onPause();
        //Displays a message that says: "Changes Applied"
        Toast.makeText(SettingsActivity.this, "Changes Applied", Toast.LENGTH_SHORT).show();
    }

    /*
    Inner class to handle shared preferences
     */

    class SharedPrefs{
        String prefToolbarColour;
        String prefTextColour;

        //Method that sets UI Elements to User Preference
        private void readPreference(){
            //Retrieve preferences from storage
            SharedPreferences prefs = getSharedPreferences(SettingsActivity.FILE_NAME, Context.MODE_PRIVATE);
            prefToolbarColour = prefs.getString(SettingsActivity.SHARED_KEY_1, null);
            prefTextColour = prefs.getString(SettingsActivity.SHARED_KEY_2, null);

            /*
            Assign corresponding preferences with UI element
            */

                if(prefToolbarColour != null && prefTextColour != null) {
                    //Change colour of toolbar
                    for(int i = 0; i < COLOURS.length; i++) {
                        if (prefToolbarColour.equals(getResources().getString(COLOURS[i]))) {
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(SettingsActivity.this, R.color.toolbarColorBlue + i)));
                        }
                    }

                    for(int i = 0; i < TEXT_COLOURS.length; i++){
                        //Change colour of text
                        if (prefTextColour.equals(getResources().getString(SettingsActivity.TEXT_COLOURS[i]))) {
                            mToolBarColour.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.textColourBlack + i));
                            mTextColour.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.textColourBlack + i));
                            mSeekColour.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.textColourBlack + i));
                        }
                    }
                }
        }

        /*
        Method that return the list value of a preference
         */

        public int textColour(){
            int returnValue = 0;
            for (int i = 0; i< TEXT_COLOURS.length; i++){
                if(prefTextColour.equals(getResources().getString(TEXT_COLOURS[i]))){
                    returnValue = i;
                }
            }
            return returnValue;
        }

    }

}