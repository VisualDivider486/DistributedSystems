package com.example.myapplication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class WorkerHandler implements Runnable{

    private int id;
    private Socket socket;
    private int priority;
    private int chunksSent;
    private final Object lock = new Object();


    ObjectOutputStream output;
    ObjectInputStream input;
    private Master master;

    public WorkerHandler(Socket socket, int id,ObjectOutputStream out,ObjectInputStream in,Master master) throws IOException {
        this.socket = socket;
        this.id = id;
        this.master = master;
        this.output = out;
        this.input = in;
    }



    @Override
    public void run() {
        while(true) {

            try {
                synchronized (lock) {
                    Chunk chunk = (Chunk) input.readObject();
                    master.map(chunk);//mapping phase
                    System.out.println("     ------");
                    System.out.println();
                    System.out.println("     ------");
                }
            } catch (ClassNotFoundException | IOException e ) {
                e.printStackTrace();
            } finally {
                synchronized (lock) {
                    // Release the lock
                }
            }

        }
    }
}
