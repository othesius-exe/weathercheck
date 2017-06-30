package com.example.android.weathercheck.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.weathercheck.WeatherObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Caleb on 6/28/17.
 */

public class QueryUtils {

    // LOG_TAG
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    // Query the DarkSky API
    public static List<WeatherObject> fetchWeatherData(String requestUrl) {

        // Create a URL Object
        URL url = createUrl(requestUrl);

        // Perform Http request and receive JSON response
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error closing input stream...", e);
        }

        List<WeatherObject> weatherObjects = extractWeatherFromJson(jsonResponse);
        return weatherObjects;
    }

    // Create a url to query
    private static URL createUrl(String stringUrl) {

        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Bad Url.", e);
        }

        return url;
    }

    // Make an Http request to the Url
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        // Try to open a connection
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the connection is successful (Response code == 200)
            // Read the stream and parse the response

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Failed to connect: Response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Problem retrieving JSON from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    // Convert InputStream into a string containing the JSON response
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();
    }

    // Build a WeatherObject from the JSON response

    public static List<WeatherObject> extractWeatherFromJson(String weatherJson) {
        String icon = "";

        Double temperature = 0.0;

        Double humidity = 0.0;

        Double windDirection = 0.0;

        // Make sure the json is not empty
        if (TextUtils.isEmpty(weatherJson)) {
            return null;
        }

        // Create an ArrayList to Store WeatherObjects in
        ArrayList<WeatherObject> weatherObjectArrayList = new ArrayList<>();

        // Parse the JSON response using key:value pairs to get desired info
        try {
            JSONObject baseJsonResponse = new JSONObject(weatherJson);
            JSONObject currentForecastObject = baseJsonResponse.getJSONObject("currently");


            for (int i = 0; i < currentForecastObject.length(); i++) {

                if (currentForecastObject.has("icon")) {
                    icon = currentForecastObject.getString("icon");
                }

                if (currentForecastObject.has("temperature")) {
                    temperature = currentForecastObject.getDouble("temperature");
                }

                if (currentForecastObject.has("humidity")) {
                    humidity = currentForecastObject.getDouble("humidity");
                }

                if (currentForecastObject.has("windBearing")) {
                    windDirection = currentForecastObject.getDouble("windBearing");
                }

                WeatherObject weather = new WeatherObject(icon, temperature, humidity, windDirection);
                weatherObjectArrayList.add(weather);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Trouble parsing JSON...");
        }

        return weatherObjectArrayList;
    }
}
