package com.example.myapplication;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Master{
    private ArrayList<Waypoint> all_waypoints_list = new ArrayList<Waypoint>();
    private ArrayList<Chunk> chunks = new ArrayList<Chunk>();
    private ArrayList<ObjectOutputStream> outputlist =new ArrayList<ObjectOutputStream>();
    private HashMap<String ,ArrayList<Integer>> users_track_hashmap =new HashMap<String ,ArrayList<Integer>>();
    private HashMap<String ,Chunk[]> intermidiate_chunks_of_routes=new HashMap<String ,Chunk[]>();
    private HashMap<String ,ObjectOutputStream> users_out =new HashMap<String ,ObjectOutputStream>();
    private HashMap<String ,double[]> totals =new HashMap<String , double[]>();
    private ServerSocket serverSocket1;
    private ServerSocket serverSocket2;
    private Socket socket;

    private int numOfWorkers;
    ObjectInputStream in;
    ObjectOutputStream out;

    Master(){
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader("configfile.txt"));
        }
        catch (FileNotFoundException e ){
            System.err.println("---Custom error-----Error opening file!");
        }
        try{
            String line = reader.readLine();
            while (line != null){
                if (line.trim().startsWith("WORKERS ")){
                    numOfWorkers = Integer.parseInt(line.trim().substring(8));
                }
                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e){
            System.out.println("Error reading line ...");
        }
    }//mexri edw diabazei posa atoma na perimenei.

    public int getNumOfWorkers(){
        return this.numOfWorkers;
    }

    public void addUserOutput(String name,ObjectOutputStream out) throws InterruptedException, IOException {
        users_out.put(name,out);
    }
    public synchronized String addUser(String newuser,int size_of_chunk,ObjectOutputStream out) throws InterruptedException, IOException { //sychronized locks
        boolean foundUser=false;
        for(Map.Entry<String,ArrayList<Integer>> entry : users_track_hashmap.entrySet()){
            if(newuser.equals(entry.getKey())) {

                foundUser = true;
                entry.getValue().add(1);//track_string_name
                Chunk[] chunks_received = new Chunk[size_of_chunk];//intermidiate
                String name_of_route = newuser  +" route_" +(entry.getValue().size());//intermidiate
                intermidiate_chunks_of_routes.put(name_of_route,chunks_received);
                addUserOutput(name_of_route,out);
                return name_of_route;
            }
        }
        if(foundUser==false) {
            double[] totals_ar=new double[3];
            totals.put(newuser,totals_ar);
            ArrayList<Integer> routes_of_user = new ArrayList<Integer>(); //usertrack
            routes_of_user.add(1);
            users_track_hashmap.put(newuser, routes_of_user);//add entry to track
            Chunk[] chunks_received = new Chunk[size_of_chunk];//intermidiate
            String name_of_route = newuser + " route_1";//intermidiate
            intermidiate_chunks_of_routes.put(name_of_route, chunks_received);
            addUserOutput(name_of_route,out);
            return name_of_route;
        }
        return "";
    }
    public synchronized void  map(Chunk chunk) throws IOException { //sychronized locks etc
        for(Map.Entry<String,Chunk[]> selected :intermidiate_chunks_of_routes.entrySet()){
            if(selected.getKey().equals(chunk.getNameOfRoute())){
                System.out.println("the chunk route is       "+chunk.getNameOfRoute());
                selected.getValue()[chunk.getId()]=chunk;

                boolean all_in=true;
                for(Chunk a:selected.getValue()){
                    boolean foundnull=false;
                    if(a==null){
                        System.out.println("chunk is null");
                        all_in=false;
                        continue;
                    }else{
                        System.out.println("Got chunk with id: "+a.getId());
                    }
                }if(all_in){reduce(chunk.getNameOfRoute());}
            }
        }
    }

    public synchronized void reduce(String route) throws IOException {
        System.out.println("Reduce completed");
        Chunk result=new Chunk(-1,"fake");
        Chunk fake_chunk = new Chunk(-2,"fake");
        System.out.println("------------------------");
        System.out.println("Total routes are: ");
        for(Map.Entry<String,Chunk[]> selected :intermidiate_chunks_of_routes.entrySet()){
            System.out.println(selected.getKey());
            if(selected.getKey().equals(route)){
                double eleve=0;
                double time=0;
                double distance=0;
                double avg_speed=0;
                int counter=0;
                for(Chunk chunk:selected.getValue()){
                    counter++;
                    fake_chunk=chunk;
                    eleve+=chunk.getElevation();
                    time+=chunk.getTime();
                    distance+=chunk.getDistance();
                    avg_speed+=chunk.getAvgSpeed();

                }
                //for current chunk

                result =new Chunk(-1, fake_chunk.getUser());
                result.setElevation(eleve);
                result.setDistance(distance);
                result.setAvgSpeed(avg_speed/counter);
                result.setTime(time);

                double others_dis=0;
                double others_ele=0;
                double others_time=0;

                //for total user
                for(Map.Entry<String,double[]> entry : totals.entrySet()){
                    if(entry.getKey().equals(result.getUser())){
                        entry.getValue()[0]+=result.getDistance();//total distance
                        entry.getValue()[1]+=result.getElevation();//total elevation
                        entry.getValue()[2]+=result.getTime();//total time
                        result.setTotalDistance(entry.getValue()[0]);
                        result.setTotalElevation( entry.getValue()[1]);
                        result.setTotalTime(entry.getValue()[2]);
                    }else{
                        others_dis+=entry.getValue()[0];
                        others_ele+=entry.getValue()[1];
                        others_time+=entry.getValue()[2];
                    }
                }

                //others average data
                if(totals.size()==1){
                    result.setOthersDistance(0);
                    result.setOthersElevation(0);
                    result.setOthersTime(0);
                }else{
                    result.setOthersDistance(others_dis/(totals.size()-1));
                    result.setOthersElevation(others_ele/(totals.size()-1));
                    result.setOthersTime(others_time/(totals.size()-1));
                }
                // Send data back to Client
                for(Map.Entry<String,ObjectOutputStream> entry : users_out.entrySet()){
                    if(entry.getKey().equals(route)){
                        entry.getValue().writeObject(result);
                        entry.getValue().flush();

                    }

                }

            }

        }
        System.out.println("------------------------");
    }

    ArrayList<ObjectOutputStream> getoutputlist(){return outputlist;}


    ArrayList<Waypoint> getWaypoints(){
        return this.all_waypoints_list;
    }

    ArrayList<Chunk> getChunks(){
        return this.chunks;
    }

    void insert(Chunk chunk){
        this.chunks.add(chunk);
    }

    void openServer(Master master){
        try{
            serverSocket1 = new ServerSocket(7000);
            int i = 1;
            while (i <= numOfWorkers){
                System.out.println("Waiting for worker connection..");
                Socket socket = serverSocket1.accept();
                System.out.println("Worker "+i+" connected ");
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());


                WorkerHandler myThread = new WorkerHandler(socket,i,out,in,master);//

                Thread masterThread = new Thread(myThread);
                masterThread.start();
                i++;

                outputlist.add(out);


            }
        } catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    void openClientServer(Master master){

        try {

            serverSocket2 = new ServerSocket(8000);
            System.out.println("Server started. Listening for Client connections on port 8000...");


            while (true) {
                Socket socket = serverSocket2.accept();
                ObjectOutputStream outx = new ObjectOutputStream(socket.getOutputStream());

                System.out.println("Client connected.");
                System.out.println();

                ClientHandler client =new ClientHandler(socket,master,outx);
                Thread thread = new Thread(client);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String args[]){
        Master master = new Master();
        System.out.println("---Master started----");
        System.out.println("---Waiting for workers---");
        master.openServer(master);
        System.out.println("---Workers started----");
        System.out.println("---Waiting for clients---");
        //arxizei prwta workers meta client kai osous client thelw
        master.openClientServer(master);
        System.out.println("---Done with clients");
    }
}
