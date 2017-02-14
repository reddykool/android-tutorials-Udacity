package com.example.android.miwok;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Reddyz on 15-01-2017.
 * {@link WordAdapter} is an {@link ArrayAdapter} that can provide the layout for each list
 * based on a data source, which is a list of {@link Word} objects.
 **/
public class WordAdapter extends ArrayAdapter<Word> {

    private static final boolean DEBUG = false;
    //Keep the class name handy to log  data.
    private static final String LOG_TAG = WordAdapter.class.getSimpleName();
    private static int newViewCount=0;
    private static int reuseViewCount=0;
    private int mListBackgroundColor;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context    The current context. Used to inflate the layout file.
     * @param words A List of Word objects to display in a list
     * @param listBackgroundColor Resource id of background color for the list items.
     */
    public WordAdapter(Context context, ArrayList<Word> words, int listBackgroundColor) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews, the adapter is not
        // going to use this second argument, so it can be any value. we pass on 0 here.
        super(context, 0, words);
        mListBackgroundColor = listBackgroundColor;
    }


    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemListView = convertView;

        // Check if the existing view is being reused, otherwise inflate the view
        if(itemListView == null) {
            itemListView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            LinearLayout textContainerLayout = (LinearLayout) itemListView.findViewById(R.id.text_container);
            textContainerLayout.setBackgroundResource(mListBackgroundColor);
            if(DEBUG) {
                newViewCount++;
                Log.i(LOG_TAG, "creating NEW view: " + newViewCount);
            }
        } else {
            if(DEBUG) {
                reuseViewCount++;
                Log.i(LOG_TAG, "RE-USE the view: " + reuseViewCount);
            }
        }

        // Get the {@link Word} object located at this position in the list
        Word currentWord = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID first_text
        TextView defaultWordView = (TextView) itemListView.findViewById(R.id.first_text);
        // Get the miwok word from the current Word object and set this text on the name TextView
        defaultWordView.setText(currentWord.getMiwokTranslation());

        // Find the TextView in the list_item.xml layout with the ID second_text
        TextView miwokWordView = (TextView) itemListView.findViewById(R.id.second_text);
        // Get the default word from the current Word object and set this text on the name TextView
        miwokWordView.setText(currentWord.getDefaultTranslation());

        // Find the ImageView in the list_item.xml layout with the ID imageView
        ImageView imageView = (ImageView) itemListView.findViewById(R.id.imageView);
        if(currentWord.hasImage()){
            // Get the corresponding image resource from the current Word object and set it to image view.
            imageView.setImageResource(currentWord.getImageResourceId());
            imageView.setVisibility(View.VISIBLE);
        } else {
            // Hide the imageView, as there is not associated image.
            imageView.setVisibility(View.GONE);
        }

        // Return the whole list item layout (containing 2 TextViews )
        // so that it can be shown in the ListView
        return itemListView;
    }
}
