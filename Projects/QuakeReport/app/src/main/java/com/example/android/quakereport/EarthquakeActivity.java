/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthquakeData>>{

    private static final String QUAKE_REQUEST_URL =
            "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=5.5&limit=20";
    private static final int QUAKE_LOADER_ID = 1;

    private static final String LOG_TAG = EarthquakeActivity.class.getName();
    private EarthquakeAdapter mAdapter;

    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        //FInd empty state text view and set to listView for empty state usage
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_text_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        // Create a new {@link EarthquakeAdapter} empty list of earthquakes
        mAdapter = new EarthquakeAdapter( this, R.layout.earthquake_list_item, new ArrayList<EarthquakeData>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthquakeData currentItem = (EarthquakeData)parent.getItemAtPosition(position);

                Uri detailUri = Uri.parse(currentItem.getUrl());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, detailUri);
                //browserIntent.setData(detailUri);
                if(browserIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(browserIntent);
                }
            }
        });

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        Log.i(LOG_TAG, "onCreate: Init loader..");
        getLoaderManager().initLoader(QUAKE_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<EarthquakeData>> onCreateLoader(int id, Bundle args) {
        //create new loader object extended from asyncloader class.
        Log.i(LOG_TAG, "onCreateLoader ..");
        return new EarthquakeLoader(EarthquakeActivity.this, QUAKE_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<EarthquakeData>> loader, List<EarthquakeData> data) {
        Log.i(LOG_TAG, "onLoadFinished ..");
        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.empty_state_text);
        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if(data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<EarthquakeData>> loader) {
        Log.i(LOG_TAG, "onLoaderReset ..");
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
