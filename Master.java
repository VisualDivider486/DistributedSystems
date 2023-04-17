import java.io.*;
import java.net.*;
import java.util.*;

class Master{ //implements Runnable{

    private ArrayList<Integer> ports = new ArrayList<Integer>();
    private ServerSocket serverSocket;
    private Socket socket;
    private int numOfPorts;
    ObjectInputStream in;
    ObjectOutputStream out;

    Master(){
        BufferedReader reader = null;
        try{
			reader = new BufferedReader(new FileReader("configfile.txt"));
		}
		catch (FileNotFoundException e ){
			System.err.println("Error opening file!");
        }
        try{
            String line = reader.readLine();
            while (line != null){
                if (line.trim().startsWith("PORT ")){
                    int port = Integer.parseInt(line.trim().substring(5));
                    ports.add(port);
                    numOfPorts++;
                }
                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e){
            System.out.println("Error reading line ...");
        }
    }

    int getNumOfPorts(){
        return this.numOfPorts;
    }

    ArrayList<Integer> getPorts(){
        return this.ports;
    }
    
    void openServer(){
        try{
            this.serverSocket = new ServerSocket(5000);
            int i = 0;
            while (i < getNumOfPorts()){
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

    void printList(){
        System.out.println(">>>>>>>Printing List..");
        for (Integer z : ports){
            System.out.println(z);
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
        Master master = new Master();
        master.printList();
        master.openServer();
        master.disconnect();
    }
}