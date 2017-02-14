package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {

    private static final boolean DEBUG = true;

    /** Handles playback of all the sound files */
    MediaPlayer mMediaPlayer;
    /** Handles audio focus when playing a sound file */
    AudioManager mAudioManager;

    //Keep the class name handy to log  data.
    private static final String LOG_TAG = NumbersFragment.class.getSimpleName();

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if(DEBUG)
                Log.i(LOG_TAG, " MediaPlayer:onCompletion ");

            mAudioManager.abandonAudioFocus(mAfChangeListener);
            releaseMediaPlayer();
        }
    };

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mAfChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Permanent loss of audio focus.
                        // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                        // Stop playback immediately and clean up resources
                        if(DEBUG)
                            Log.i(LOG_TAG, " onAudioFocusChange : AUDIOFOCUS_LOSS ");

                        releaseMediaPlayer();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                        // Pause playback
                        // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                        // short amount of time.
                        // Pause playback and reset player to the start of the file. That way, we can
                        // play the word from the beginning when we resume playback.
                        if(DEBUG)
                            Log.i(LOG_TAG, " onAudioFocusChange : AUDIOFOCUS_LOSS_TRANSIENT ");

                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Lower the volume, keep playing
                        // The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that our app is allowed
                        // to continue playing sound but at a lower volume.  But we will pause & reset
                        if(DEBUG)
                            Log.i(LOG_TAG, " onAudioFocusChange : AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ");

                        mMediaPlayer.pause();
                        //mMediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Your app has been granted audio focus again
                        // Raise volume to normal, restart playback if necessary
                        // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                        if(DEBUG)
                            Log.i(LOG_TAG, " onAudioFocusChange : AUDIOFOCUS_GAIN ");

                        mMediaPlayer.start();
                    }
                }
            };


    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(DEBUG)
            Log.i(LOG_TAG, " onCreateView() ");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word(R.raw.kolavaridi, "one", "lutti", R.drawable.number_one));
        words.add(new Word(R.raw.number_two, "two", "otiiko", R.drawable.number_two));
        words.add(new Word(R.raw.number_three, "three", "tolookosu", R.drawable.number_three));
        words.add(new Word(R.raw.number_four, "four", "oyyisa", R.drawable.number_four));
        words.add(new Word(R.raw.number_five, "five", "massokka", R.drawable.number_five));
        words.add(new Word(R.raw.businessman, "six", "temmokka", R.drawable.number_six));
        words.add(new Word(R.raw.number_seven, "seven", "kenekaku", R.drawable.number_seven));
        words.add(new Word(R.raw.number_eight, "eight", "kawinta", R.drawable.number_eight));
        words.add(new Word(R.raw.number_nine, "nine", "wo’e", R.drawable.number_nine));
        words.add(new Word(R.raw.number_ten, "ten", "na’aacha", R.drawable.number_ten));

        if(DEBUG)
            Log.i(LOG_TAG, words.toString());

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView numberListView = (ListView) rootView.findViewById(R.id.list_view);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        numberListView.setAdapter(itemsAdapter);

        // Set a click listener to play the audio when the list item is clicked on
        numberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the {@link Word} object at the given position the user clicked on
                //Word itemClicked = (Word)parent.getItemAtPosition(position); // This is also possible.
                Word itemClicked = words.get(position); //Access from within class

                //Release MediaPlayer if currently exists, as we will play new audio file
                releaseMediaPlayer();

                if(DEBUG)
                    Log.i(LOG_TAG, "Word @:"+ position + " is " + itemClicked);

                int result = mAudioManager.requestAudioFocus(mAfChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word and start
                    if(DEBUG)
                        Log.i(LOG_TAG, " MediaPlayer:Create & Start ");
                    mMediaPlayer = MediaPlayer.create(getActivity(), itemClicked.getAudioResourceId());
                    mMediaPlayer.start();

                    // Setup a listener on the mediaPLayer , so that we can release the resources when play completed.
                    mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
                if(DEBUG)
                    Toast.makeText(getActivity(), "clicked item: "+ itemClicked.getDefaultTranslation(), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        if(DEBUG)
            Log.i(LOG_TAG, " releaseMediaPlayer ");
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
        }
    }

    @Override
    public void onStop() {
        if(DEBUG)
            Log.i(LOG_TAG, " OnStop() ");
        super.onStop();
    }

    @Override
    public void onPause() {
        if(DEBUG)
            Log.i(LOG_TAG, " OnPause() ");
        super.onPause();
        mAudioManager.abandonAudioFocus(mAfChangeListener);
        releaseMediaPlayer();
    }

    @Override
    public void onResume() {
        if(DEBUG)
            Log.i(LOG_TAG, " OnResume() ");
        super.onResume();
    }

    @Override
    public void onStart() {
        if(DEBUG)
            Log.i(LOG_TAG, " OnStart() ");
        super.onStart();
    }

    @Override
    public void onDestroy() {
        if(DEBUG)
            Log.i(LOG_TAG, " OnDestroy() ");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        if(DEBUG)
            Log.i(LOG_TAG, " onDestroyView() ");
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        if(DEBUG)
            Log.i(LOG_TAG, " onAttach() ");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        if(DEBUG)
            Log.i(LOG_TAG, " onDetach() ");
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(DEBUG)
            Log.i(LOG_TAG, " onCreate() ");
        super.onCreate(savedInstanceState);
    }
}
