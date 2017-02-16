package com.example.android.quakereport;

import java.util.Date;

/**
 * Created by Reddyz on 14-02-2017.
 */
public class EarthquakeData {
    double mMagnitude;
    String mPlace;
    long  mTimeInMilliSeconds;
    String mUrl;

    public EarthquakeData(double magnitude, String place, long time, String url) {
        this.mMagnitude = magnitude;
        this.mPlace = place;
        this.mTimeInMilliSeconds = time;
        this.mUrl = url;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getPlace() {
        return mPlace;
    }

    public long getTime() {
        return mTimeInMilliSeconds;
    }

    public String getUrl() {
        return mUrl;
    }
}
