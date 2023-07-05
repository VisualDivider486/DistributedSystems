package com.example.myapplication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

class Worker{

    private InetAddress ip;
    private int port;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private static int i = 5001;

    Worker(){
        try{
            this.ip = InetAddress.getLocalHost();
        } catch(UnknownHostException u){}
        this.port = i;
        i++;

    }

    InetAddress getIp(){
        return this.ip;
    }

    int getPort(){
        return this.port;
    }

    public String toString(){
        return "Hi im worker " + getIp() + " with port# " + getPort();
    }


    void connect() throws IOException, ClassNotFoundException, UnknownHostException, InterruptedException {
        //try{
        this.socket = new Socket("localhost",7000);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        while(true) {
            Chunk chunk = (Chunk)in.readObject();
            ChunkHandler chunkHandler = new ChunkHandler(chunk,out);
            Thread worker_of_worker = new Thread(chunkHandler);
            worker_of_worker.start();

        }


    }

    public static void main(String args[]) throws ClassNotFoundException, IOException, InterruptedException {

        Worker w = new Worker();
        System.out.println(w.toString());
        System.out.println(">>>>>>>>Attempting connection with server..");
        w.connect();

    }
}
