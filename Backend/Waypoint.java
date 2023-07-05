package com.example.myapplication;

import java.io.Serializable;

class Waypoint implements Serializable{
    String time;
    float elevation;
    double lat;
    double longt;

    Waypoint(String time,float elevation,double lat,double longt){

        this.time=time;
        this.elevation=elevation;
        this.lat=lat;
        this.longt=longt;
    }

    double getLat(){
        return lat;
    }

    double getLongt(){
        return longt;
    }

    String getTime(){
        return time;
    }

    float getElevation(){
        return elevation;
    }

    public String toString(){
        return "Time: " + getTime() + " Elevation: " + getElevation() + " Latitude: " + getLat() + " Longtitude: " + getLongt();
    }
}

