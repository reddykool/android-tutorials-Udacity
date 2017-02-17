package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Reddyz on 17-02-2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<EarthquakeData>> {
    public static final String LOG_TAG = EarthquakeLoader.class.getName();

    String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        Log.i(LOG_TAG, "EarthquakeLoader() ..");
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "onStartLoading() ..");
        forceLoad();
    }

    @Override
    public List<EarthquakeData> loadInBackground() {
        Log.i(LOG_TAG, "loadInBackground() ..");
        if(mUrl == null)
            return null;

        List<EarthquakeData> result = QueryUtils.fetchEarthquakeData(mUrl);
        return result;
    }
}
