package com.example.patrick.complexgui;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.patrick.complexgui.fragments.GridFragment;
import com.example.patrick.complexgui.fragments.VerticalListFragment;
import com.example.patrick.complexgui.info.MusicFile;
import com.example.patrick.complexgui.info.MusicFileList;

public class MainActivity extends AppCompatActivity {
    public static final MusicFileList list = new MusicFileList();
    private VerticalListFragment verticalList;
    private GridFragment gridFragment;
    private TabLayout tabLayout;
    private int textStyle = R.layout.list_text_black;

    //Keys for Bundles
    public static final String KEY = "song";
    public static final String EXTRA_KEY_1 = "cover";
    public static final String EXTRA_KEY_2 = "artist";
    public static final String EXTRA_KEY_3 = "title";
    public static final String EXTRA_KEY_4 = "format";
    public static final String EXTRA_KEY_5 = "songID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inflate chosen xml
        setContentView(R.layout.activity_main);

        /*
        Declare objects for UI elements in xml
         */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.addTab(tabLayout.newTab().setText("Grid"));

        /*
        Initiate the GridFragment and pass the data
         */

        gridFragment = new GridFragment();
        Bundle bundleG = new Bundle();
        bundleG.putParcelable(KEY, list);
        gridFragment.setArguments(bundleG);

        /*
        Initiate the VerticalListFragment and pass the data
         */

        verticalList = new VerticalListFragment();
        Bundle bundleV = new Bundle();
        bundleV.putParcelable(KEY, list);
        verticalList.setArguments(bundleV);

        /*
        Run and display the VerticalListFragment
         */

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, verticalList);
        transaction.commit();
    }

    /*
    After UI is drawn or activity re-entered
     */

    @Override
    protected void onResume() {
        super.onResume();
        //Set UI elements to pre-specified User's choice
        readPreference();

        //Set the style of the list text
        ArrayAdapter<MusicFile> adapter = new ArrayAdapter<>(this, textStyle, list);
        verticalList.setListAdapter(adapter);

        /*
        When a list item is clicked, starts associated activity
         */

        verticalList.setOnItemClick(new VerticalListFragment.OnItemClick() {
            @Override
            public void onItemClicked(int position) {
                MusicFile text = list.get(position);
                Intent i = new Intent(MainActivity.this, MusicActivity.class);
                i.putExtra(EXTRA_KEY_1, text.getImage());
                i.putExtra(EXTRA_KEY_2, text.getArtist());
                i.putExtra(EXTRA_KEY_3, text.getTitle());
                i.putExtra(EXTRA_KEY_4, position);
                i.putExtra(EXTRA_KEY_5, text.getSong());
                startActivity(i);
            }
        });

        /*
        When a grid item is clicked, starts associated activity
         */

        gridFragment.setOnItemClick(new GridFragment.OnItemClick() {
            public void onItemClicked(int position) {
                MusicFile text = list.get(position);
                Intent i = new Intent(MainActivity.this, MusicActivity.class);
                i.putExtra(EXTRA_KEY_1, text.getImage());
                i.putExtra(EXTRA_KEY_2, text.getArtist());
                i.putExtra(EXTRA_KEY_3, text.getTitle());
                i.putExtra(EXTRA_KEY_4, position);
                i.putExtra(EXTRA_KEY_5, text.getSong());
                startActivity(i);
            }
        });

        /*
        Switches between grid and list views upon User Input
         */

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                if(tab.getText().equals("Grid")) {
                    trans.replace(R.id.fragmentContainer, gridFragment);
                }else {
                    trans.replace(R.id.fragmentContainer, verticalList);
                }
                trans.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /*
    Show settings icon in the toolbar
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
    Called when settings icon is clicked
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

        /*
        Assign corresponding preferences with UI element
         */

        if(prefToolbarColour != null && prefTextColour != null) {
            for (int i = 0; i < SettingsActivity.COLOURS.length; i++) {
                //Change colour of toolbar
                if (prefToolbarColour.equals(getResources().getString(SettingsActivity.COLOURS[i]))) {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.toolbarColorBlue + i)));
                }
            }

            switch (prefTextColour) {
                case "Black":
                    textStyle = R.layout.list_text_black;
                    break;
                case "Grey":
                    textStyle = R.layout.list_text_grey;
                    break;
                }
            }
        }
}