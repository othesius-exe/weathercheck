package com.example.android.weathercheck;

/**
 * Class to describe a WeatherObject
 */

public class WeatherObject {

    private Double mLatitude;

    private Double mLongitude;

    private String mIcon;

    private Double mTemperature;

    private Double mHumidity;

    private Double mWindDirection;

    public WeatherObject(String icon, Double temp, Double humidity, Double windDirection) {

        mIcon = icon;
        mTemperature = temp;
        mHumidity = humidity;
        mWindDirection = windDirection;
    }

    public String getIcon() {
        return mIcon;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public Double getTemperature() {
        return mTemperature;
    }

    public Double getHumidity() {
        return mHumidity;
    }

    public Double getWindDirection() {
        return mWindDirection;
    }

    public Double setLatitude(Double latitude) {
        mLatitude = latitude;
        return mLatitude;
    }

    public Double setLongitude(Double longitude) {
        mLongitude = longitude;
        return mLongitude;
    }

    public String toString() {
        return "Icon Descriptor: " + mIcon + " "
                + "Temperature: " + mTemperature + " "
                + "Humidity: " + mHumidity;
    }
}
