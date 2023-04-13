import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Master{ //implements Runnable{

    private static int numOfWorkers;
    private ServerSocket serverSocket;
    private Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    private int port;

    private ArrayList <Worker> listOfWorkers;

    Master(){

    }

    Master(int numOfWorkers){
        this.numOfWorkers = numOfWorkers;
        for (int i = 1; i <= numOfWorkers; i++){
            Worker worker = new Worker(i);
            System.out.println("Creating worker number.. " + worker.getId());
            listOfWorkers.add(worker);
        }
    }

    int getNumOfWorkers(){
        return this.numOfWorkers;
    }

    void openServer(){
        try{
            this.serverSocket = new ServerSocket(port);
            while (true){
                System.out.println("Waiting for connection..");
                this.socket = serverSocket.accept();
                System.out.println("Worker connected");
                this.out = new ObjectOutputStream(socket.getOutputStream());
                this.in = new ObjectInputStream(socket.getInputStream());
                //new Thread.run();
            }
        } catch(IOException ioException){
			ioException.printStackTrace();
		} finally{
			try{
				this.serverSocket.close();
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
        } catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}