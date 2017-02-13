package com.example.younes.pollutionchecker.model;

import java.util.ArrayList;

/**
 * Created by elyandoy on 13/02/2017.
 */

public class MessageObject {
    long timestamp;
    CityObject city;
    ArrayList<IaqiObject> iaqi;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public CityObject getCity() {
        return city;
    }

    public void setCity(CityObject city) {
        this.city = city;
    }

    public ArrayList<IaqiObject> getIaqi() {
        return iaqi;
    }

    public void setIaqi(ArrayList<IaqiObject> iaqi) {
        this.iaqi = iaqi;
    }
}
