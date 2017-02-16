package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Reddyz on 14-02-2017.
 */
public class EarthquakeAdapter extends ArrayAdapter<EarthquakeData>{

    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(Context context, int resource, List<EarthquakeData> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }

        EarthquakeData currentItem = getItem(position);

        //Set the magnitude as per the stored data
        double magnitude = currentItem.getMagnitude();
        TextView magnitudeText = (TextView) listItemView.findViewById(R.id.magnitudeText);
        String formattedMag = formatMagnitude(magnitude);
        magnitudeText.setText(formattedMag);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeText.getBackground();
        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(magnitude);
        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        //get the stored place info and Split it into offset and Primary location info.
        String originalPlaceStr = currentItem.getPlace();
        // Default offset place text
        String offsetPlace = getContext().getString(R.string.near_the);
        String primaryPlace = originalPlaceStr;

        if(originalPlaceStr.contains(LOCATION_SEPARATOR))
        {
            int splitPos = originalPlaceStr.lastIndexOf(LOCATION_SEPARATOR) + LOCATION_SEPARATOR.length();
            // Offset place : form start upto the marker end
            offsetPlace = originalPlaceStr.substring(0, splitPos);
            //Primary place : from the marker end till place end
            primaryPlace = originalPlaceStr.substring(splitPos);
        }

       //Set the corresponding text fields for offset and primary info
        TextView offsetPlaceText = (TextView) listItemView.findViewById(R.id.offsetPlaceText);
        offsetPlaceText.setText(offsetPlace);

        TextView primaryPlaceText = (TextView) listItemView.findViewById(R.id.primaryPlaceText);
        primaryPlaceText.setText(primaryPlace);

        // Get stored time in milliseconds and format to readable date and time formats thru Date object
        Date dateObject = new Date(currentItem.getTime());
        String formattedDate = formatDate(dateObject);
        String formattedTime = formatTime(dateObject);

        // Set corresponding readable date and time in respective textViews.
        TextView dateText = (TextView) listItemView.findViewById(R.id.dateText);
        dateText.setText(formattedDate);

        TextView timeText = (TextView) listItemView.findViewById(R.id.timeText);
        timeText.setText(formattedTime);

        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {
        int colorResourceId;
        //int MagFLoor = (int) Math.floor(magnitude);
        switch ((int)magnitude) {
            case 0:
            case 1 :
                colorResourceId = R.color.magnitude1;
                break;
            case 2:
                colorResourceId = R.color.magnitude2;
                break;
            case 3:
                colorResourceId = R.color.magnitude3;
                break;
            case 4:
                colorResourceId = R.color.magnitude4;
                break;
            case 5:
                colorResourceId = R.color.magnitude5;
                break;
            case 6:
                colorResourceId = R.color.magnitude6;
                break;
            case 7:
                colorResourceId = R.color.magnitude7;
                break;
            case 8:
                colorResourceId = R.color.magnitude8;
                break;
            case 9:
                colorResourceId = R.color.magnitude9;
                break;
            case 10:
            default:
                colorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), colorResourceId);
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(magnitude);
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        return timeFormat.format(dateObject);
    }
}
