package com.app.kpp.Models;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.Date;
import java.util.Objects;

public class Train {
    private String starting_sration,end_station,date_departure,date_arival,time_start, time_end;
    Date date = new Date();

    public Train(){

    }

    public Train(String starting_sration, String end_station, String date_departure, String date_arival, String time_start, String time_end) {
        this.starting_sration = starting_sration;
        this.end_station = end_station;
        this.date_departure = date_departure;
        this.date_arival = date_arival;
        this.time_start = time_start;
        this.time_end = time_end;
    }

    public String getStarting_sration() {
        return starting_sration;
    }

    public void setStarting_sration(String starting_sration) {
        this.starting_sration = starting_sration;
    }

    public String getEnd_station() {
        return end_station;
    }

    public void setEnd_station(String end_station) {
        this.end_station = end_station;
    }

    public String getDate_departure() {
        return date_departure;
    }

    public void setDate_departure(String date_departure) {
        this.date_departure = date_departure;
    }

    public String getDate_arival() {
        return date_arival;
    }

    public void setDate_arival(String date_arival) {
        this.date_arival = date_arival;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    @Override
    public String toString() {
        return " Train: " +
                 starting_sration + "-" +
                 end_station + "; " +
                 date_departure + "-" +
                 date_arival + "; " +
                 time_start + "-" +
                 time_end + "; "+"\n" ;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Train)) return false;
        Train train = (Train) o;
        return Objects.equals(starting_sration, train.starting_sration);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public int hashCode() {
        return Objects.hash(starting_sration);
    }
}
