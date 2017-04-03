package com.example.younes.pollutionchecker.model;

import java.util.ArrayList;

public class StationNearest {
    private String nlo;
    private String nna;
    private int t;
    private String pol;
    private String x;
    private String v;
    private String u;
    private String key;
    private int d;
    private ArrayList<Double> geo;

    public String getNlo() {
        return nlo;
    }

    public void setNlo(String nlo) {
        this.nlo = nlo;
    }

    public String getNna() {
        return nna;
    }

    public void setNna(String nna) {
        this.nna = nna;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public ArrayList<Double> getGeo() {
        return geo;
    }

    public void setGeo(ArrayList<Double> geo) {
        this.geo = geo;
    }
}
