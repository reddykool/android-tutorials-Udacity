package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.data.PetsContract.PetsEntry;

import java.util.zip.Inflater;

/**
 * Created by Reddyz on 06-03-2017.
 */
/**
 * {@link PetsCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class PetsCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link PetsCursorAdapter}.
     *
     * @param context The context
     * @param cursor       The cursor from which to get the data.
     * @param autoRequery
     */
    public PetsCursorAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param viewGroup  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        return view;
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Get the textview's for name and breed from list view
        TextView nameTV = (TextView) view.findViewById(R.id.list_item_name);
        TextView breedTV = (TextView) view.findViewById(R.id.list_item_breed);

        //get the current cursor item name and breed.
        String name = cursor.getString(cursor.getColumnIndex(PetsEntry.COLUMN_PET_NAME));
        String breed = cursor.getString(cursor.getColumnIndex(PetsEntry.COLUMN_PET_BREED));

        if(TextUtils.isEmpty(breed)) {
            breed = context.getString(R.string.unknwon_breed_msg);
        }

        //set name and breed values to thier views
        nameTV.setText(name);
        breedTV.setText(breed);
    }
}
