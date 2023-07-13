import java.net.*;
import java.util.*;
import java.io.*;
import java.util.regex.Pattern;
import java.lang.instrument.ClassDefinition;

class Worker{

    private InetAddress ip;
    private int port;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String master_host;
    private static int i = 5001;

    Worker(String master_host){
        try{
            this.ip = InetAddress.getLocalHost();
        } catch(UnknownHostException u){}
        this.port = i;
        this.master_host=master_host;
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
            this.socket = new Socket(master_host,7000);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());

            while(true) {

                Chunk chunk = (Chunk)in.readObject();

                System.out.println("chunk got");// molis sunde8ei o client 8a parei to chunk tou ap ton kapoion

                ChunkHandler chunkHandler = new ChunkHandler(chunk,out);
                Thread worker_of_worker = new Thread(chunkHandler);
                worker_of_worker.start();

            }
    }
    private static final String IP_ADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                    + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                    + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                    + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public static boolean isValidIP(String ipAddress) {
        return Pattern.matches(IP_ADDRESS_PATTERN, ipAddress);
    }

    private static String ask_for_localhost(){
        //boolean incorrect_yes_no=true;
        while(true){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Is Master on the same localhost as worker? Answer y/n:");
            String answer = scanner.nextLine();
            if(answer.equals("y")){
                return "localhost";
            }else if(answer.equals("n")){
                while(true){
                    Scanner scanner1 =new Scanner(System.in);
                    System.out.println("Provide local address of Master:");
                    String mstr_ip=scanner1.nextLine();
                    if(isValidIP(mstr_ip)){
                        return(mstr_ip);
                    }
                }
            }else{
                System.out.println("Not acceptable answer..Answer with y/n");
            }
        }


    }

    public static void main(String args[]) throws ClassNotFoundException, IOException, InterruptedException {
        String ip_master=ask_for_localhost();
        Worker w = new Worker(ip_master);
        System.out.println(w.toString());
        System.out.println(">>>>>>>>Attempting connection with server..");
        w.connect();

    }
}
