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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {

    private static final boolean DEBUG = true;
    MediaPlayer mMediaPlayer;
    AudioManager mAudioManager;

    //Keep the class name handy to log  data.
    private static final String LOG_TAG = PhrasesFragment.class.getSimpleName();

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
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
    AudioManager.OnAudioFocusChangeListener mAfChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
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


    public PhrasesFragment() {
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
        words.add(new Word(R.raw.phrase_where_are_you_going, "Where are you going?", "minto wuksus"));
        words.add(new Word(R.raw.phrase_what_is_your_name, "What is your name?", "tinnә oyaase'nә"));
        words.add(new Word(R.raw.phrase_my_name_is, "My name is...", "oyaaset..."));
        words.add(new Word(R.raw.phrase_how_are_you_feeling, "How are you feeling?", "michәksәs?"));
        words.add(new Word(R.raw.phrase_im_feeling_good, "I’m feeling good.", "kuchi achit"));
        words.add(new Word(R.raw.phrase_are_you_coming, "Are you coming?", "әәnәs'aa?"));
        words.add(new Word(R.raw.phrase_yes_im_coming, "Yes, I’m coming", "hәә’ әәnәm"));
        words.add(new Word(R.raw.phrase_im_coming, "I’m coming.", "әәnәm"));
        words.add(new Word(R.raw.phrase_lets_go, "Let’s go.", "yoowutis"));
        words.add(new Word(R.raw.phrase_come_here, "Come here.", "әnni'nem"));

        if(DEBUG)
            Log.i(LOG_TAG, words.toString());

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView phrasesItemList = (ListView) rootView.findViewById(R.id.list_view);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        phrasesItemList.setAdapter(itemsAdapter);

        // Set a click listener to play the audio when the list item is clicked on
        phrasesItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the {@link Word} object at the given position the user clicked on
                Word itemClicked = words.get(position);

                //Release MediaPlayer if currently exists, as we will play new audio file
                releaseMediaPlayer();

                if(DEBUG)
                    Log.i(LOG_TAG, "Word @:"+ position + " is " + itemClicked);

                int result = mAudioManager.requestAudioFocus(mAfChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word ans start
                    if(DEBUG)
                        Log.i(LOG_TAG, " MediaPlayer:Create & Start ");
                    mMediaPlayer = MediaPlayer.create(getActivity(), itemClicked.getAudioResourceId());
                    mMediaPlayer.start();

                    // Setup a listener on the mediaPLayer , so that we can release the resources when ply completed.
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
