package com.example.younes.pollutionchecker.model;

public class CityObject {
    private String name;
    private String url;
    private int idx;
    private String id;
    private String[] geo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getGeo() {
        return geo;
    }

    public void setGeo(String[] geo) {
        this.geo = geo;
    }
}
