package com.example.younes.pollutionchecker.model;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by elyandoy on 27/03/2017.
 */

public class SearchObject {
    private int idx;
    private String cityName;

    public SearchObject(int idx, String cityName) {
        this.idx = idx;
        this.cityName = cityName;    }

    public int getIdx() {
        return idx;
    }
    public void setidx(int idx) {
        this.idx = idx;
    }

    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}


