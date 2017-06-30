package com.example.android.weathercheck.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.weathercheck.R;
import com.example.android.weathercheck.WeatherObject;

import java.util.ArrayList;

/**
 * Class to load WeatherObjects into custom layout.
 */

public class WeatherAdapter extends ArrayAdapter<WeatherObject> {

    private static final String LOG_TAG = WeatherAdapter.class.getSimpleName();

    public WeatherAdapter(Activity context, ArrayList<WeatherObject> weatherObjects) {
        super(context, 0, weatherObjects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_weather, parent, false);
        }

        WeatherObject currentWeather = getItem(position);

        ImageView iconView = (ImageView) convertView.findViewById(R.id.weather_image_view);
        TextView tempView = (TextView) convertView.findViewById(R.id.temp_text_view);

        String temperature = currentWeather.getTemperature().toString();
        tempView.setText(temperature);

        Log.v(LOG_TAG, "Temperature " + temperature);

        return convertView;
    }

}
