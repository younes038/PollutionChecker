package com.example.younes.pollutionchecker.model;

import java.util.ArrayList;

/**
 * Created by younes on 02/04/2017.
 */

public class GlobalSearch {
    private String dt;
    private String term;
    private ArrayList<ResultSearch> results;

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public ArrayList<ResultSearch> getResults() {
        return results;
    }

    public void setResults(ArrayList<ResultSearch> results) {
        this.results = results;
    }
}
