import java.net.*;
import java.io.*;

class Worker{// implements Runnable{

    private int id;
    //ip address;

    Worker(int id){
        //super();
        this.id = id;
    }

    int getId(){
        return this.id;
    }

    @Override
    void run(){
        Master.reduce(key,value);
    }
}