package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Reddyz on 04-03-2017.
 */

public class PetsProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = PetsProvider.class.getSimpleName();

    //db helper to access database
    PetsDbHelper mDbHelper;

    /** URI matcher code for the content URI for the pets table */
    private static final int PETS = 100;
    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int PETS_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(PetsContract.CONTENT_AUTHORITY, PetsContract.PATH_PETS, PETS);
        sUriMatcher.addURI(PetsContract.CONTENT_AUTHORITY, PetsContract.PATH_PETS + "/#", PETS_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new PetsDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Get readable database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor = null;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS :
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = db.query(PetsContract.PetsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            case PETS_ID :
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = PetsContract.PetsEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = db.query(PetsContract.PetsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        //Register notification uri for content resolver to listen for changes in this uri anytime later
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS :
                return PetsContract.PetsEntry.CONTENT_LIST_TYPE;
            case PETS_ID :
                return PetsContract.PetsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Invalid MIME type Uri");
        }
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS :
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertPet(Uri uri, ContentValues values) {
        // Validate input content values
        String name = values.getAsString(PetsContract.PetsEntry.COLUMN_PET_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        Integer gender = values.getAsInteger(PetsContract.PetsEntry.COLUMN_PET_GENDER);
        if (gender == null || !PetsContract.PetsEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Pet requires a valid gender");
        }

        Integer weight = values.getAsInteger(PetsContract.PetsEntry.COLUMN_PET_WEIGHT);
        if (weight == null || weight < 0) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }

        // Get writeable database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Insert the new pet with the given values
        long newRowId = db.insert(PetsContract.PetsEntry.TABLE_NAME, null, values);

        //If insert failed.?.?. return null Uri.
        if( newRowId == -1 ) {
            return null;
        } else {
            // If insert was successful, then notify all listeners that the data at the
            // given URI has changed
            getContext().getContentResolver().notifyChange(uri, null);
            // Once we know the ID of the new row in the table,
            // return the new URI with the ID appended to the end of it
            return ContentUris.withAppendedId(uri, newRowId);
        }
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;
        // Get writeable database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                // Delete all rows that match the selection and selection args
                rowsDeleted =  db.delete(PetsContract.PetsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PETS_ID:
                // Delete a single row given by the ID in the URI
                selection = PetsContract.PetsEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted  = db.delete(PetsContract.PetsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if(rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        //return number of rows deleted
        return rowsDeleted;
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int rowsUpdated = 0;
        if(contentValues.size() == 0) {
            return rowsUpdated;
        }

        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS :
                rowsUpdated = updatePets(uri, contentValues, selection, selectionArgs);
                break;
            case PETS_ID :
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = PetsContract.PetsEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = updatePets(uri, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot update with this uri" + uri);

        }
        return rowsUpdated;
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updatePets(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Validate input content values
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if(values.containsKey(PetsContract.PetsEntry.COLUMN_PET_NAME)) {
            String name = values.getAsString(PetsContract.PetsEntry.COLUMN_PET_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if(values.containsKey(PetsContract.PetsEntry.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(PetsContract.PetsEntry.COLUMN_PET_GENDER);
            if (gender == null || !PetsContract.PetsEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires a valid gender");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if(values.containsKey(PetsContract.PetsEntry.COLUMN_PET_WEIGHT)) {
            Integer weight = values.getAsInteger(PetsContract.PetsEntry.COLUMN_PET_WEIGHT);
            if (weight == null || weight < 0) {
                throw new IllegalArgumentException("Pet requires weight");
            }
        }
        // Get writeable database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Update the pet with the given values
        int rowsUpdated = db.update(PetsContract.PetsEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if(rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows updated
        return rowsUpdated;
    }
}
