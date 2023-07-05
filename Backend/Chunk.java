package com.example.myapplication;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

class Chunk implements Serializable{

    private ArrayList<Waypoint> chunk_list = new ArrayList<Waypoint>();
    private int chunkId;
    private String userId;

    private double elevation;
    private double total_elevation;
    private double others_total_elevation;

    private double distance;
    private double total_distance;
    private double others_total_distance;

    private double time;
    private double total_time;
    private double others_total_time;

    private String route_name;
    private double avgSpeed;
    private ObjectOutputStream return_out;

    public Chunk(int chunk, String userId){
        this.chunkId = chunk;
        this.userId = userId;
    }
    void setNameOfRoute(String route_name){
        this.route_name=route_name;
    }


    String getNameOfRoute(){
        return this.route_name;
    }

    void setClientOutput(ObjectOutputStream out){
        this.return_out=out;
    }

    ObjectOutputStream getClientOutput(){
        return this.return_out;
    }

    void setElevation(double ele){
        this.elevation = ele;
    }
    void setTotalElevation(double tot_ele){
        this.total_elevation = tot_ele;
    }
    void setOthersElevation(double oth_ele){
        this.others_total_elevation = oth_ele;
    }

    void setDistance(double dis){
        this.distance = dis;
    }
    void setTotalDistance(double tot_dis){this.total_distance = tot_dis;}
    void setOthersDistance(double oth_dis){this.others_total_distance = oth_dis;}

    void setTime(double time){
        this.time = time;
    }
    void setTotalTime(double tot_time){this.total_time=tot_time;}
    void setOthersTime(double oth_time){this.others_total_time = oth_time;}


    void setAvgSpeed(double speed){
        this.avgSpeed = speed;
    }
    double getAvgSpeed(){
        return avgSpeed;
    }

    double getDistance(){
        return distance;
    }
    double getTotalDistance(){return total_distance;}
    double getOthersTotalDistance(){return others_total_distance;}


    double getTime(){
        return time;
    }
    double getTotalTime(){return total_time;}
    double getOthersTotalTime(){return others_total_time;}


    double getElevation(){
        return elevation;
    }
    double getTotalElevation(){return total_elevation;}
    double getOthersTotalElevation(){return others_total_elevation;}



    int getId(){
        return this.chunkId;
    }

    String getUser(){
        return this.userId;
    }

    public ArrayList<Waypoint> getList(){
        return chunk_list;
    }

    void insert(Waypoint w){
        this.chunk_list.add(w);
    }

    int size(){
        return chunk_list.size();
    }

    String printTable(){
        String all="";
        for (Waypoint w : chunk_list){
            all += w.getElevation()+" ";
        }
        return all;
    }

    public String toString(){
        return "Hi I'm chunk " + getId() + " with stats time: " + getTime() + " elevation: " + getElevation() + " avgspeed: " + getAvgSpeed() + " distance: " + getDistance() + " meters";
    }
}