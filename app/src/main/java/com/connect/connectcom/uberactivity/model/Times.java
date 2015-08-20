package com.connect.connectcom.uberactivity.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Times {

    @SerializedName("times") private List<Time> times;
    private boolean ok = true;

    public List<Time> getTimes() {
        return times;
    }

    public void setTimes(List<Time> times) {
        this.times = times;
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    public String toString() {
        return "Times{" +
               "times=" + times +
               '}';
    }

    public static Times buildErrorObject() {
        Times times = new Times();
        times.ok = false;
        return times;
    }
}
