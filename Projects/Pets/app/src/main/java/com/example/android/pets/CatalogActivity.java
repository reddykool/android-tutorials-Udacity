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
package com.example.android.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.data.PetsContract.PetsEntry;
import com.example.android.pets.data.PetsDbHelper;

import java.net.URI;
import java.util.zip.Inflater;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String LOG_TAG = "CatalogActivity";
    private static final int PETS_LOADER_ID = 1;

    PetsCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Get the listveiw and set cursor adapter to load the cursor table into listview using
        // our custom cursor adapter
        ListView displayView = (ListView) findViewById(R.id.list_view_pet);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        displayView.setEmptyView(emptyView);

        // Initialise cursor adapter with empty cursor. Loader on finished will update with
        // correct queried cursor data
        mCursorAdapter = new PetsCursorAdapter(this, null, false);
        //Set this cursor adapter to listview.
        displayView.setAdapter(mCursorAdapter);

        //Handle click on list item
        displayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Uri currentPetUri = ContentUris.withAppendedId(PetsEntry.CONTENT_URI, id);
                Log.i(LOG_TAG, "On item click: Uri:: " + currentPetUri.toString());
                Log.i(LOG_TAG, "On item click: Pos:  " + position + " id: " + id);
                Intent editorIntent = new Intent(CatalogActivity.this, EditorActivity.class);
                editorIntent.setData(currentPetUri);
                startActivity(editorIntent);
            }
        });

        // Prepare the loader. Either re-connect with an existing one, or start a new one.
        getSupportLoaderManager().initLoader(PETS_LOADER_ID, null, this);
    }

    private void insertPet() {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PetsEntry.COLUMN_PET_NAME, "Toftof");
        values.put(PetsEntry.COLUMN_PET_BREED, "Terrier-Femi");
        values.put(PetsEntry.COLUMN_PET_GENDER, PetsEntry.GENDER_FEMALE);
        values.put(PetsEntry.COLUMN_PET_WEIGHT, 7);

        // Insert the new row, returning the uri value of the new row
        Uri newRow = getContentResolver().insert(PetsEntry.CONTENT_URI, values);
        if( newRow == null) {
            Toast.makeText(this, "Pet(Dummy) save FAILED", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Pet(Dummy) saved", Toast.LENGTH_SHORT).show();
        }
        Log.i(LOG_TAG, " Pet(dummy) saved: Uri:  "+newRow.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projection = {PetsEntry._ID, PetsEntry.COLUMN_PET_NAME, PetsEntry.COLUMN_PET_BREED};
        // Now create and return a CursorLoader that will take care of creating a Cursor through
        // content resolver/provider help in background thread..
        return new CursorLoader(this, PetsEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished() above is about to be
        // closed.  We need to make sure we are no longer using it.
        mCursorAdapter.swapCursor(null);
    }
}
