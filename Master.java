import java.io.*;
import java.net.*;
import java.util.*;

class Master{

    private ArrayList<Integer> ports = new ArrayList<Integer>();
    private ArrayList<Integer> ids = new ArrayList<Integer>();
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

    ArrayList<Integer> getIds(){
        return this.ids;
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

    void openClientServer(){

        try {
            ServerSocket serverSocket = new ServerSocket(6000);
            System.out.println("Server started. Listening for Client connections on port 6000...");
            int i = 0;
            while (i<1) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected. Starting new thread...");

                Thread thread = new Thread(new ClientHandler(socket));
                thread.start();

                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void printList(){
        System.out.println(">>>>>>>Printing List..");
        for (Integer z : ports){
            System.out.println(z);
        }
    }

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
        for (int i = 1; i <= master.getNumOfPorts(); i++){
            MasterMultithread myThread = new MasterMultithread(i);
            Thread masterThread = new Thread(myThread);
            masterThread.start();
        }
        //master.openClientServer();
        master.openServer();
        master.disconnect();
    }
}