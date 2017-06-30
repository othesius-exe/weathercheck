package com.example.android.weathercheck.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.weathercheck.WeatherObject;

import java.util.List;

/**
 *
 */

public class WeatherLoader extends AsyncTaskLoader<List<WeatherObject>> {

    private String LOG_TAG = WeatherLoader.class.getSimpleName();

    private String mUrl;

    public WeatherLoader(Context context, String url){
        super(context);
        Log.v(LOG_TAG, "Url in loader: " + url);
        mUrl = url;
    }

    protected void onStartLoading() {
        forceLoad();
        Log.i(LOG_TAG, "Retrieving weather...");
    }

    @Override
    public List<WeatherObject> loadInBackground() {
        Log.i(LOG_TAG, "Populating weather data...");
        if (mUrl == null) {
            return null;
        }
        List<WeatherObject> weatherObjects = QueryUtils.fetchWeatherData(mUrl);
        return weatherObjects;
    }
}
