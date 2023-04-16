import java.net.*;
import java.util.*;
import java.io.*;

class Worker implements Runnable{

    private int id;
    private int port;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ArrayList<Worker> listOfWorkers = new ArrayList<Worker>();

    Worker(){

    }

    Worker(int id, int port){
        this.id = id;
        this.port = port;
    }

    int getId(){
        return this.id;
    }

    int getPort(){
        return this.port;
    }

    ArrayList<Worker> getList(){
        return listOfWorkers;
    }

    void setId(int id){
        this.id = id;
    }

    void setPort(int port){
        this.port = port;
    }

    public String toString(){
        return "Hi im worker " + getId() + " with port# " + getPort();
    }

    void createWorkers(){
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
                if (line.trim().equals("WORKER")){
                    line = reader.readLine();
                    if (line.trim().equals("{")){
                        line = reader.readLine();
                        while(!line.trim().equals("}")){
                            if (line.trim().startsWith("ID ")){
                                id = Integer.parseInt(line.trim().substring(3));
                            } else if(line.trim().startsWith("PORT ")){
                                port = Integer.parseInt(line.trim().substring(5));
                            }
                            line = reader.readLine();
                        }
                        Worker worker = new Worker(id, port);
                        listOfWorkers.add(worker);
                    }
                } 
                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e){
            System.out.println("Error reading line ...");
        }
    } 

    @Override
    public void run(){
        try{
            this.socket = new Socket("localhost",5000);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            String message = "Hello server, I'm worker " + getId() + " and port# " + getPort();
            this.out.writeUTF(message);
            this.out.flush();
            System.out.println(message);
        } catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				in.close();	out.close();
				socket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
    }

    void printList(){
        System.out.println(">>>>>>>Printing List..");
        for (Worker z : listOfWorkers){
            System.out.println(z);
        }
    }

    public static void main(String args[]){
        Worker w = new Worker();
        System.out.println(">>>>Creating workers..");
        w.createWorkers();
        w.printList();
        System.out.println(">>>>>>>>Attempting connection with server..");
        for (Worker x : w.getList()){
            x.run();
        }
        
    }
    
}