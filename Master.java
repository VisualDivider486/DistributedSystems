import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Master{ //implements Runnable{

    private static int numOfWorkers;
    private ServerSocket serverSocket;
    private Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;

    private ArrayList <Worker> listOfWorkers;

    Master(){

    }

    Master(int numOfWorkers){
        this.numOfWorkers = numOfWorkers;
        listOfWorkers = new ArrayList<Worker>();
        for (int i = 1; i <= numOfWorkers; i++){
            Worker worker = new Worker(i);
            System.out.println("Creating worker number.. " + worker.getId());
            System.out.println(worker);
            listOfWorkers.add(worker);
            System.out.println("eftiaksa ton ergath");
            System.out.println(listOfWorkers.size());
        }
    }

    int getNumOfWorkers(){
        return this.numOfWorkers;
    }

    public ArrayList<Worker> getList(){
        return this.listOfWorkers;
    }

    

    void openServer(){
        try{
            this.serverSocket = new ServerSocket(5000);
            while (true){
                System.out.println("Waiting for connection..");
                this.socket = serverSocket.accept();
                System.out.println("Worker connected");
                this.out = new ObjectOutputStream(socket.getOutputStream());
                this.in = new ObjectInputStream(socket.getInputStream());
                System.out.println(in.readUTF());
                if (!in.readUTF().isEmpty() ){
                    break;
                }      
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
        } catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    public static void main(String args[]){
        Master master = new Master(Integer.parseInt(args[0]));
        System.out.println("The number of workers is " + master.getNumOfWorkers());
        master.openServer();
        System.out.println(master.getList().size());
        for (Worker worker : master.getList()){
            System.out.print("mphka sto loop");
            worker.run();
        }
    }
}