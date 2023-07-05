package com.example.myapplication;

import java.io.IOException;
import java.io.ObjectOutputStream;

class ChunkHandler implements Runnable{
    private Chunk chunk;
    ObjectOutputStream out2 = null;

    public ChunkHandler(Chunk chunk, ObjectOutputStream out){
        this.chunk = chunk;
        this.out2 = out;
    }


    double[] calculateAverageSpeed(double dis, String t){
        double seconds = 0;
        seconds = (Integer.parseInt(t.substring(3,5)) * 60) + Integer.parseInt(t.substring(6,8));
        double ar[]=new double[2];
        double avgSpeed = dis/seconds;
        ar[0]=avgSpeed;
        ar[1]=seconds;

        return ar;
    }

    double calculateDistance(){
        double distance = 0;
        int i = 0;
        Waypoint prevWaypoint = null;
        for (Waypoint current_waypoint : chunk.getList()){ //getLongt() w.getLat()
            if (i == 0){
                prevWaypoint = current_waypoint;
                i++;
                continue;
            }
            double radius = 6371000;

            // Convert latitude and longitude from degrees to radians
            double lat1Rad = Math.toRadians(prevWaypoint.getLat());
            double lon1Rad = Math.toRadians(prevWaypoint.getLongt());
            double lat2Rad = Math.toRadians(current_waypoint.getLat());
            double lon2Rad = Math.toRadians(current_waypoint.getLongt());

            double deltaLat = lat2Rad - lat1Rad;
            double deltaLon = lon2Rad - lon1Rad;

            double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
                    Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                            Math.sin(deltaLon/2) * Math.sin(deltaLon/2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double current_distance = radius * c;
            distance+=current_distance;
            prevWaypoint=current_waypoint;
            i++;
        }
        return distance;
    }

    double calculateElevation(){
        int i = 0;
        double sum = 0;
        Waypoint prevWaypoint = null;
        for (Waypoint w : chunk.getList()){
            if (i == 0){
                prevWaypoint = w;
                i++;
                continue;
            }
            if (w.getElevation() > prevWaypoint.getElevation()){
                sum = (w.getElevation() - prevWaypoint.getElevation()) + sum;
                prevWaypoint = w;
            }
            if (w.getElevation() <= prevWaypoint.getElevation()){
                prevWaypoint = w;
            }
        }
        return sum;
    }

    String calculateTime(){
        int i = 0;
        String time = null;
        int seconds = 0;
        int minutes = 0;
        int hour = 0;
        Waypoint prevWaypoint = null;
        for (Waypoint w : chunk.getList()){
            if (i == 0){
                prevWaypoint = w;
                i++;
                continue;
            }
            if (Integer.parseInt(w.getTime().substring(0,2)) == Integer.parseInt(prevWaypoint.getTime().substring(0, 2))){
                if (Integer.parseInt(w.getTime().substring(3,5)) == Integer.parseInt(prevWaypoint.getTime().substring(3,5))){
                    seconds = (Integer.parseInt(w.getTime().substring(6,8)) - Integer.parseInt(prevWaypoint.getTime().substring(6,8))) + seconds;
                    minutes = 0 + minutes;
                    if (seconds > 59) {
                        seconds = seconds - 60;
                        minutes = minutes + 1;
                    }
                    if (seconds < 10){
                        time = hour + "0:0" + minutes + ":0" + seconds;
                    }
                    else{ time = hour + "0:0" + minutes + ":" + seconds; }

                }
                else{
                    if (Integer.parseInt(w.getTime().substring(6,8)) < Integer.parseInt(prevWaypoint.getTime().substring(6, 8))){
                        seconds = ((60 - Integer.parseInt(prevWaypoint.getTime().substring(6,8))) + Integer.parseInt(w.getTime().substring(6,8))) + seconds;
                        minutes = ((Integer.parseInt(w.getTime().substring(3,5)) - Integer.parseInt(prevWaypoint.getTime().substring(3,5))) - 1) + minutes;
                        if (seconds > 59) {
                            seconds = seconds - 60;
                            minutes = minutes + 1;
                        }
                        if (minutes < 10 && seconds < 10){ time = hour + "0:0" + minutes + ":0" + seconds; }
                        if (minutes < 10 && seconds > 10){ time = hour + "0:0" + minutes + ":" + seconds; }
                        if (minutes > 10 && seconds < 10){ time = hour + "0:" + minutes + ":0" + seconds; }
                        if (minutes > 10 && seconds > 10){ time = hour + "0:" + minutes + ":" + seconds; }
                    }
                    else {
                        seconds = (Integer.parseInt(w.getTime().substring(6,8)) - Integer.parseInt(prevWaypoint.getTime().substring(6,8))) + seconds;
                        minutes = Integer.parseInt(w.getTime().substring(3,5)) - Integer.parseInt(prevWaypoint.getTime().substring(3,5)) + minutes;
                        if (seconds > 59) {
                            seconds = seconds - 60;
                            minutes = minutes + 1;
                        }
                        if (minutes < 10 && seconds < 10){ time = hour + "0:0" + minutes + ":0" + seconds; }
                        if (minutes < 10 && seconds > 10){ time = hour + "0:0" + minutes + ":" + seconds; }
                        if (minutes > 10 && seconds < 10){ time = hour + "0:" + minutes + ":0" + seconds; }
                        if (minutes > 10 && seconds > 10){ time = hour + "0:" + minutes + ":" + seconds; }
                    }
                }
            }
            else{
                if (Integer.parseInt(w.getTime().substring(6,8)) < Integer.parseInt(prevWaypoint.getTime().substring(6, 8))){
                    seconds = ((60 - Integer.parseInt(prevWaypoint.getTime().substring(6,8))) + Integer.parseInt(w.getTime().substring(6,8))) + seconds;
                    minutes = (Integer.parseInt(w.getTime().substring(3,5)) + 59) - Integer.parseInt(prevWaypoint.getTime().substring(3,5)) + minutes;
                    if (seconds > 59) {
                        seconds = seconds - 60;
                        minutes = minutes + 1;
                    }
                    if (minutes < 10 && seconds < 10){ time = hour + "0:0" + minutes + ":0" + seconds; }
                    if (minutes < 10 && seconds > 10){ time = hour + "0:0" + minutes + ":" + seconds; }
                    if (minutes > 10 && seconds < 10){ time = hour + "0:" + minutes + ":0" + seconds; }
                    if (minutes > 10 && seconds > 10){ time = hour + "0:" + minutes + ":" + seconds; }
                }
                else {
                    seconds = (Integer.parseInt(w.getTime().substring(6,8)) - Integer.parseInt(prevWaypoint.getTime().substring(6,8))) + seconds;
                    minutes = Integer.parseInt(w.getTime().substring(3,5)) - Integer.parseInt(prevWaypoint.getTime().substring(3,5)) + minutes;
                    if (seconds > 59) {
                        seconds = seconds - 60;
                        minutes = minutes + 1;
                    }
                    if (minutes < 10 && seconds < 10){ time = hour + "0:0" + minutes + ":0" + seconds; }
                    if (minutes < 10 && seconds > 10){ time = hour + "0:0" + minutes + ":" + seconds; }
                    if (minutes > 10 && seconds < 10){ time = hour + "0:" + minutes + ":0" + seconds; }
                    if (minutes > 10 && seconds > 10){ time = hour + "0:" + minutes + ":" + seconds; }
                }

            }
            i++;
            prevWaypoint = w;
        }
        return time;
    }

    @Override
    public void run()  {
        synchronized (out2) {
            System.out.println("------------------------------");
            chunk.setElevation(calculateElevation());
            System.out.println("Elevation of chunk " + chunk.getId() + " is " + chunk.getElevation());
            chunk.setDistance(calculateDistance());
            System.out.println("Distance of chunk " + chunk.getId() + " is " + chunk.getDistance());
            System.out.println("Time of chunk " + chunk.getId() + " is " + chunk.getTime());
            String stringtime=calculateTime();
            chunk.setAvgSpeed(calculateAverageSpeed(chunk.getDistance(), stringtime)[0]);
            System.out.println("Average speed of chunk " + chunk.getId() + " is " + chunk.getAvgSpeed());
            chunk.setTime(calculateAverageSpeed(chunk.getDistance(), stringtime)[1]);
            System.out.println("------------------------------");
            try {
                out2.writeObject(chunk);
                out2.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}