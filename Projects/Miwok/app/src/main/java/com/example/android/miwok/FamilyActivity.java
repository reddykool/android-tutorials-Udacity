package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

    private static final boolean DEBUG = true;
    //Keep the class name handy to log  data.
    private static final String LOG_TAG = FamilyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(DEBUG)
            Log.i(LOG_TAG, " OnCreate() ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new FamilyFragment())
                .commit();

    }

    @Override
    protected void onPause() {
        if(DEBUG)
            Log.i(LOG_TAG, " OnPause() ");
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(DEBUG)
            Log.i(LOG_TAG, " OnResume() ");
        super.onResume();
    }

    @Override
    protected void onStop() {
        if(DEBUG)
            Log.i(LOG_TAG, " OnStop() ");
        super.onStop();
    }

    @Override
    protected void onStart() {
        if(DEBUG)
            Log.i(LOG_TAG, " OnStart() ");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        if(DEBUG)
            Log.i(LOG_TAG, " OnDestroy() ");
        super.onDestroy();
    }
}
