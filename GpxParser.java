import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.ArrayList;

public class GpxParser {
    public static void main(String[] args) {
        try {
            File xmlFile = new File("filename.gpx"); // Replace with the path to your XML file
            //ArrayList<String> lat_list =new ArrayList<String>();
            //ArrayList<String> lon_list =new ArrayList<String>();
            //ArrayList<String> element_list =new ArrayList<String>();
            //ArrayList<String> time_list =new ArrayList<String>();
           // String creator="";
            
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList wptNodes = doc.getElementsByTagName("wpt");
            NodeList gpxNode =doc.getElementsByTagName("gpx");
            Element gpxElement = (Element) gpxNode.item(0);
            String creator = gpxElement.getAttribute("creator");
            
            ArrayList<Waypoint> all_waypoints_list;
            Waypoint waypoint;
            
            for (int i = 0; i < wptNodes.getLength(); i++) {
                Element wptElement = (Element) wptNodes.item(i);
                double lat = wptElement.getAttribute("lat");
                double lon = wptElement.getAttribute("lon");
                float ele = wptElement.getElementsByTagName("ele").item(0).getTextContent();
                String time = wptElement.getElementsByTagName("time").item(0).getTextContent();
                
                waypoint =new Waypoint(time,ele,lat,lon);
                all_waypoints_list.add(waypoint);
                
                //System.out.println("Latitude: " + lat);lat_list.add(lat);
                //System.out.println("Longitude: " + lon);lon_list.add(lon);
                //System.out.println("Elevation: " + ele);element_list.add(ele);
                //System.out.println("Time: " + time);time_list.add(time);
                System.out.println();
            }
            for(Waypoint a:all_waypoints_list){
                System.out.println("lat is "+a.lat");   
            }
            System.out.println("creator was "+creator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
