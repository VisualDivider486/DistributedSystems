package com.example.myapplication;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{

    public Client(){

    }



    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        System.out.print("Specify the name of the file: ");

        Scanner sc = new Scanner(System.in);
        String msg = sc.nextLine();

        String fileName = "gpxs/"+msg;


        Socket socket = new Socket("localhost", 8000);

        OutputStream outputStream = socket.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(fileName);

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        socket.shutdownOutput();

        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        System.out.println("File sent successfully.");

        System.out.println("Waiting for results to come in...");

        Chunk result = (Chunk) in.readObject();
        System.out.println("-------CURRENT ROUTE STATS FOR "+result.getUser()+"--------");
        System.out.println("Distance is "+result.getDistance());
        System.out.println("Elevation is "+result.getElevation());
        System.out.println("Average speed is  "+result.getAvgSpeed()+ " m/s");
        System.out.println("Time is "+result.getTime());

        System.out.println("----------------------------------------------");
        System.out.println("-------------TOTAL STATS FOR "+result.getUser()+"---------");
        System.out.println("total distance is "+result.getTotalDistance());
        System.out.println("total elevation is "+result.getTotalElevation());
        System.out.println("total time in seconds in "+result.getTotalTime());
        System.out.println("----------------------------------------------");
        System.out.println("-------------TOTAL STATS FOR OTHERS AVERAGE---------");
        System.out.println("Others total distance is "+result.getOthersTotalDistance());
        System.out.println("Others total elevation is "+result.getOthersTotalElevation());
        System.out.println("Others total time in seconds in "+result.getOthersTotalTime());

        System.out.println("Results are in, closing socket");
        in.close();
        socket.close();
        fileInputStream.close();

    }
}
