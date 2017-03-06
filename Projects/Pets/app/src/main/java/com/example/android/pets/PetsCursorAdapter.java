package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
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

public class PetsCursorAdapter extends CursorAdapter {

    public PetsCursorAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Get the textview's for name and breed from list view
        TextView nameTV = (TextView) view.findViewById(R.id.list_item_name);
        TextView breedTV = (TextView) view.findViewById(R.id.list_item_breed);

        //get the current cursor item name and breed.
        String name = cursor.getString(cursor.getColumnIndex(PetsEntry.COLUMN_PET_NAME));
        String breed = cursor.getString(cursor.getColumnIndex(PetsEntry.COLUMN_PET_BREED));

        //set name and breed values to thier views
        nameTV.setText(name);
        breedTV.setText(breed);
    }
}
