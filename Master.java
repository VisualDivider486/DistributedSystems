import java.io.*;
import java.net.*;
import java.util.*;

class Master{ //implements Runnable{

    private static int numOfWorkers;
    private ServerSocket serverSocket;
    private Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;

    Master(){

    }

    Master(int numOfWorkers){
        this.numOfWorkers = numOfWorkers;
    }

    int getNumOfWorkers(){
        return this.numOfWorkers;
    }
    
    void openServer(){
        try{
            this.serverSocket = new ServerSocket(5000);
            int i = 0;
            while (i <= 3){
                System.out.println("Waiting for connection..");
                this.socket = serverSocket.accept();
                System.out.println("Worker connected");
                this.out = new ObjectOutputStream(socket.getOutputStream());
                this.in = new ObjectInputStream(socket.getInputStream());
                System.out.println(in.readUTF());     
                i++;
                //new Thread.run();
            }
        } catch(IOException ioException){
			ioException.printStackTrace();
		} /*catch(ClassNotFoundException e){
            e.printStackTrace();
        }*/ finally{
			try{
				this.serverSocket.close();
                this.socket.close();
			} catch(IOException ioException){
				ioException.printStackTrace();
			}
		} 
    }

    /*@Override
    void run(){
        try{
          //map(key,value);
        }

        catch(Exception e){
            System.out.println("Exception is caught");
        }
    }*/

    void disconnect(){
        try{
            in.close();
            out.close();
            System.out.println("Server shutting down..");
        } catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    public static void main(String args[]){
        Master master = new Master(4);
        System.out.println("The number of workers is " + master.getNumOfWorkers());
        master.openServer();
        master.disconnect();
    }
}