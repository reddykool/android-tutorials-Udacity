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

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.pets.data.PetsContract.PetsEntry;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String LOG_TAG = "EditorActivity";
    private static final int EDITOR_LOADER_ID = 2;
    private static final int EDIT_MODE = 0;
    private static final int NEW_MODE = 1;

    /** EditText field to enter the pet's name */
    private EditText mNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mBreedEditText;

    /** EditText field to enter the pet's weight */
    private EditText mWeightEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mGenderSpinner;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = PetsEntry.GENDER_UNKNOWN;

    // Uri of the list item selected to launch in "Edit mode"
    private Uri mEditItemUri;

    //Edit mode or New entry
    private int mEditorMode;

    //to keep track if any changes done in the editor fields
    boolean mHasPetChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mEditItemUri = getIntent().getData();
        if(mEditItemUri == null) {
            //"Add New entry" mode
            Log.i(LOG_TAG, "OnCreate: New pet ");
            setTitle(getString(R.string.editor_activity_title_new_pet));
            mEditorMode = NEW_MODE;
        } else {
            //"edit existing entry" mode
            Log.i(LOG_TAG, "OnCreate : Edit pet: Uri:: " + mEditItemUri.toString());
            setTitle(getString(R.string.editor_activity_title_edit_pet));
            // Prepare the loader to fetch the details of this pet item.
            // Framework will either re-connect with an existing loader, or start a new one.
            getSupportLoaderManager().initLoader(EDITOR_LOADER_ID, null, this);
            mEditorMode = EDIT_MODE;
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        View.OnTouchListener touchlistener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mHasPetChanged = true;
                return false;
            }
        };
        mNameEditText.setOnTouchListener(touchlistener);
        mBreedEditText.setOnTouchListener(touchlistener);
        mWeightEditText.setOnTouchListener(touchlistener);
        mGenderSpinner.setOnTouchListener(touchlistener);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = PetsEntry.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = PetsEntry.GENDER_FEMALE; // Female
                    } else {
                        mGender = PetsEntry.GENDER_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = PetsEntry.GENDER_UNKNOWN; // Unknown
            }
        });
    }

    //Gets user input value to save in database
    private boolean savePet() {
        //Get user input values
        String petName = mNameEditText.getText().toString().trim();
        String petBreed = mBreedEditText.getText().toString().trim();
        String petWeightStr = mWeightEditText.getText().toString().trim();

        boolean nameEmpty = TextUtils.isEmpty(petName);
        boolean breedEmpty = TextUtils.isEmpty(petBreed);
        boolean weightEmpty = TextUtils.isEmpty(petWeightStr);
        if(nameEmpty && breedEmpty && weightEmpty && mGender == PetsEntry.GENDER_UNKNOWN)
        {
            // User might have pressed save option by mistake without entering any details.
            // nothing to save. return immediately
            Toast.makeText(this, "Please enter the details to save", Toast.LENGTH_SHORT).show();
            //could not complete save. return false
            return false;
        }
        if(nameEmpty) {
            Toast.makeText(this, "Please enter the pet name to save", Toast.LENGTH_SHORT).show();
            //could not complete save. return false
            return false;
        }

        //by default pet weight is 0;
        int petWeight = 0;
        if(!weightEmpty) {
            petWeight = Integer.parseInt(petWeightStr);
        }

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PetsEntry.COLUMN_PET_NAME, petName);
        values.put(PetsEntry.COLUMN_PET_BREED, petBreed);
        values.put(PetsEntry.COLUMN_PET_GENDER, mGender);
        values.put(PetsEntry.COLUMN_PET_WEIGHT, petWeight);

        if(mEditorMode == NEW_MODE) {
            // Insert the new row, returning the primary key value of the new row
            Uri newRow = getContentResolver().insert(PetsEntry.CONTENT_URI, values);
            if( newRow == null) {
                Toast.makeText(this, R.string.pet_save_failed, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, R.string.pet_saved, Toast.LENGTH_SHORT).show();
            }
        } else if(mEditorMode == EDIT_MODE) {
            // Update the current item,
            int rowsUpdated = getContentResolver().update(mEditItemUri, values, null, null);
            if( rowsUpdated == 0) {
                Toast.makeText(this, R.string.pet_update_failed, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.pet_updated, Toast.LENGTH_SHORT).show();
            }
        }
        //save operation was complete
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                //Save the input to db
                boolean saveComplete = savePet();
                if(saveComplete) {
                    //exit editor activity
                    finish();
                }
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // if nothing changed in Editor, Navigate back to parent activity (CatalogActivity)
                if(!mHasPetChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardDialogListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // user selected to Discard changes. Hnece Navigate Up
                                if(dialogInterface != null)
                                    dialogInterface.dismiss();
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardDialogListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of creating a Cursor through
        // content resolver/provider in background thread..
        return new CursorLoader(this, mEditItemUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Set the editor fields according to the returned cursor values of particular item.
        cursor.moveToFirst();
        int nameColumnIndex = cursor.getColumnIndex(PetsEntry.COLUMN_PET_NAME);
        mNameEditText.setText(cursor.getString(nameColumnIndex));

        int breedColumnIndex = cursor.getColumnIndex(PetsEntry.COLUMN_PET_BREED);
        mBreedEditText.setText(cursor.getString(breedColumnIndex));

        int weightColumnIndex = cursor.getColumnIndex(PetsEntry.COLUMN_PET_WEIGHT);
        mWeightEditText.setText(String.valueOf(cursor.getInt(weightColumnIndex)));

        int genderColumnIndex = cursor.getColumnIndex(PetsEntry.COLUMN_PET_GENDER);
        mGenderSpinner.setSelection(cursor.getInt(genderColumnIndex));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Clear All fields
        mNameEditText.getText().clear();
        mBreedEditText.getText().clear();
        mWeightEditText.getText().clear();
        mGenderSpinner.setSelection(0);
    }

    @Override
    public void onBackPressed() {
        if(!mHasPetChanged) {
            //if no changes made, continue with default back operation
            super.onBackPressed();
            return;
        }
        // Changes were made. show alert message to user and proceed accordingly
        // Create a click listener to handle the user confirming that
        // changes should be discarded.
        DialogInterface.OnClickListener discardDialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //User clicked on Discard buton. discard changes and close editor
                if(dialogInterface != null)
                    dialogInterface.dismiss();
                finish();
            }
        };
        // Show a dialog that notifies the user they have unsaved changes
        showUnsavedChangesDialog(discardDialogListener);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardDialogListener) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        // 2.1 message to be shown in dialog
        dialogBuilder.setMessage(R.string.save_alert_dialog_message);
        // 2.2 one of the option button to keep editing and continue in Editor
        dialogBuilder.setPositiveButton(R.string.save_alert_keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Keep editing. dismiss the dialog
                if(dialogInterface != null)
                    dialogInterface.dismiss();
            }
        });
        // 2.3 one of the option button to discard and exit Editor from respective caller context
        dialogBuilder.setNegativeButton(R.string.save_alert_discard, discardDialogListener);

        // 3. Create the Alert Dialog and show
        AlertDialog dialog =  dialogBuilder.create();
        dialog.show();
    }
}