package com.example.younes.pollutionchecker.model;

import java.util.ArrayList;

public class MessageObject {
    private long timestamp;
    private CityObject city;
    private ArrayList<IaqiObject> iaqi;

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
