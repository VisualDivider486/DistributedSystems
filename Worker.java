import java.net.*;
import java.util.ArrayList;
import java.io.*;

class Worker implements Runnable{

    private int id;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    //ip address;

    Worker(int id){
        this.id = id;
    }

    int getId(){
        return this.id;
    }

    public String toString(){
        return "Hi im worker " + getId();
    }

    @Override
    public void run(){
        try{
            this.socket = new Socket("localhost",5000);
            //socket.connect(5000);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            String message = "Hello server";
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

    public static void main(String args[]){
        Worker worker = new Worker(1);
        worker.run();
    }
    
}