package com.example.android.weathercheck;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.weathercheck.utils.WeatherAdapter;
import com.example.android.weathercheck.utils.WeatherLoader;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class WeatherActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<WeatherObject>> {

    private static final String LOG_TAG = WeatherActivity.class.getSimpleName();

    private String WEATHER_QUERY_URL = "https://api.darksky.net/forecast/";
    private String API_KEY = "";
    private Double SAMPLE_LONGITUDE = 40.71;
    private Double SAMPLE_LATITUDE = 74.00;
    private Double LONGITUDE = 0.0;
    private Double LATITUDE = 0.0;
    private String LOCATION_SEPARATOR = ",";
    private String PARAM_SEPARATOR = "/";
    private String mFullUrl;
    private String CITY;
    private static final int WEATHER_LOADER_ID = 1;
    private LoaderManager mLoaderManager;
    private ArrayList<WeatherObject> mWeatherList;
    private static final int MY_PERMISSION_REQUEST_READ_FINE_LOCATION = 2;
    private Geocoder geocoder;
    private List<Address> addresses;

    // Views for displaying data
    private TextView tempView;
    private ImageView iconView;
    private TextView cityView;
    private Toolbar toolbar;
    private WeatherAdapter mWeatherAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        API_KEY = getResources().getString(R.string.api_key);
        mWeatherAdapter = new WeatherAdapter(this, new ArrayList<WeatherObject>());
        mWeatherList = new ArrayList<>();

        // Assign views to variables
        tempView = (TextView) findViewById(R.id.temp_text_view);
        iconView = (ImageView) findViewById(R.id.weather_image_view);

        // Make sure the user is connected to the net
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // Check that the user has allowed permissions for location
        if (ContextCompat.checkSelfPermission(WeatherActivity.this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Use location data?")
                        .setMessage("Allow weather check to use your location?")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_READ_FINE_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_READ_FINE_LOCATION);
            }
        }

        // Get users Latitude and Longitude
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        if (location != null) {
            LONGITUDE = location.getLongitude();
            LATITUDE = location.getLatitude();
            Log.v(LOG_TAG, "Location " + location);

            // Create the full query url for the users location
            mFullUrl = WEATHER_QUERY_URL + API_KEY + PARAM_SEPARATOR + LATITUDE + LOCATION_SEPARATOR + LONGITUDE;

        } else {
            mFullUrl = WEATHER_QUERY_URL + API_KEY + PARAM_SEPARATOR + SAMPLE_LATITUDE + LOCATION_SEPARATOR + SAMPLE_LONGITUDE;
        }

        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if (location != null) {
                    LONGITUDE = location.getLongitude();
                    LATITUDE = location.getLatitude();
                    Log.v(LOG_TAG, "Location " + location);


                    // Create the full query url for the users location
                    mFullUrl = WEATHER_QUERY_URL + API_KEY + PARAM_SEPARATOR + LATITUDE + LOCATION_SEPARATOR + LONGITUDE;

                } else {
                    mFullUrl = WEATHER_QUERY_URL + API_KEY + PARAM_SEPARATOR + SAMPLE_LATITUDE + LOCATION_SEPARATOR + SAMPLE_LONGITUDE;
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        mLoaderManager = getSupportLoaderManager();
        // If they are connected, start the loader
        if (isConnected) {
            mLoaderManager.initLoader(WEATHER_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<WeatherObject>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "Creating Loader...");
        return new WeatherLoader(this, mFullUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<WeatherObject>> loader, List<WeatherObject> data) {
        Log.i(LOG_TAG, "Load finished!");
        if (data != null && !data.isEmpty()) {
            mWeatherList.addAll(data);
            mWeatherAdapter.addAll(mWeatherList);
            Log.v(LOG_TAG, "Weather in adapter: " + mWeatherList.toString());
        }
    }

    @Override
    public void onLoaderReset(Loader<List<WeatherObject>> loader) {
        mLoaderManager.restartLoader(WEATHER_LOADER_ID, null, this);
    }
}
