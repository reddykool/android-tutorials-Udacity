package com.reddyz.testmediaplayer;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mPlayer;

    @Override
    protected void onPause() {
        super.onPause();
        mPlayer.stop();
        mPlayer.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playButon = (Button) findViewById(R.id.play_button);
        Button pauseButton = (Button) findViewById(R.id.pause_button);
        Button skipButton = (Button) findViewById(R.id.skip_button);

        mPlayer = MediaPlayer.create(this,R.raw.roja);
        int duration = mPlayer.getDuration();
        final int skipInterval = duration/5;

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(MainActivity.this, "completed.. restarting :)", Toast.LENGTH_SHORT).show();
                mp.start();
            }
        });

        playButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.start();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.pause();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPos = mPlayer.getCurrentPosition();
                mPlayer.seekTo(curPos + skipInterval);
            }
        });


    }
}
