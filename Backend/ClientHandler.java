package com.example.myapplication;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

class ClientHandler implements Runnable {
    private Socket socket;
    private Master master;
    private ObjectOutputStream out;

    private ArrayList<Chunk> chunks_list=new ArrayList<Chunk>();

    public ClientHandler(Socket socket,Master master,ObjectOutputStream outx) throws IOException {
        this.socket = socket;
        this.master = master;
        this.out =outx;
    }
    public void roundRobin(String key) throws IOException {
        //
        int num_of_workers= (master.getoutputlist()).size();
        int num_of_worker=0;
        for (Chunk small_chunk : chunks_list) {
            small_chunk.setNameOfRoute(key);
            //small_chunk.setClientOutput(this.out);
            if(num_of_worker==num_of_workers) {
                num_of_worker = 0;
            }
            master.getoutputlist().get(num_of_worker).writeObject(small_chunk);
            num_of_worker++;
        }

    }
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] xmlData = outputStream.toByteArray();

            // Now you can process the received XML data
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new ByteArrayInputStream(xmlData));
            doc.getDocumentElement().normalize();

            NodeList wptNodes = doc.getElementsByTagName("wpt");
            NodeList gpxNode =doc.getElementsByTagName("gpx");
            Element gpxElement = (Element) gpxNode.item(0);
            String creator = gpxElement.getAttribute("creator");

            ArrayList<Waypoint> all_waypoints_list=new ArrayList<Waypoint>();
            Waypoint waypoint;

            for (int i = 0; i < wptNodes.getLength(); i++) {
                Element wptElement = (Element) wptNodes.item(i);

                double lat = Double.parseDouble(wptElement.getAttribute("lat"));
                double lon = Double.parseDouble(wptElement.getAttribute("lon"));
                float ele =Float.parseFloat(wptElement.getElementsByTagName("ele").item(0).getTextContent());

                String time = wptElement.getElementsByTagName("time").item(0).getTextContent().substring(11,19);

                waypoint =new Waypoint(time,ele,lat,lon);
                all_waypoints_list.add(waypoint);

            }

            int i = 0;
            int j = 0;
            Chunk chunk = new Chunk(j,creator);

            for (Waypoint w : all_waypoints_list){
                chunk.insert(w);
                i++;
                if (i%5 == 0 ){
                    Waypoint lastWaypoint = w;
                    chunks_list.add(chunk);
                    j++;
                    chunk = new Chunk(j,creator);
                    chunk.insert(lastWaypoint);
                    i++;
                }


            }
            if (chunk.getList().size() > 1){ // if there are less than 5 waypoints in the last chunk
                chunks_list.add(chunk);
            }
            String key="";


            key=master.addUser(creator,chunks_list.size(),out);//onoma route

            roundRobin(key);//pros8etei to kleidi sta chunks

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
