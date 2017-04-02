package com.example.younes.pollutionchecker.model;

import java.util.ArrayList;

public class IaqiObject {
    private String p;
    private ArrayList<Integer> v;
    private String i;

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public ArrayList<Integer> getV() {
        return v;
    }

    public void setV(ArrayList<Integer> v) {
        this.v = v;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }
}
