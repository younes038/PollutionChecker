package com.example.younes.pollutionchecker.model;

import java.util.ArrayList;

/**
 * Created by younes on 02/04/2017.
 */

public class ResultSearch {
    private StationSearch s;
    private ArrayList<String> n;
    private int x;
    private String c;
    private int z;

    public StationSearch getS() {
        return s;
    }

    public void setS(StationSearch s) {
        this.s = s;
    }

    public ArrayList<String> getN() {
        return n;
    }

    public void setN(ArrayList<String> n) {
        this.n = n;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
