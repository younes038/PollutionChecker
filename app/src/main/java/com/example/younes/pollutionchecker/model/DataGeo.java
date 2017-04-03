package com.example.younes.pollutionchecker.model;

public class DataGeo {
    private int aqi;
    private int idx;
    private CityGeo cityGeo;
    private String dominentPol;
    private TimeGeo time;

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public CityGeo getCityGeo() {
        return cityGeo;
    }

    public void setCityGeo(CityGeo cityGeo) {
        this.cityGeo = cityGeo;
    }

    public String getDominentPol() {
        return dominentPol;
    }

    public void setDominentPol(String dominentPol) {
        this.dominentPol = dominentPol;
    }

    public TimeGeo getTime() {
        return time;
    }

    public void setTime(TimeGeo time) {
        this.time = time;
    }
}
