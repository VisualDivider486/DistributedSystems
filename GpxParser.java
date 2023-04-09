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
            File xmlFile = new File("segment1.gpx"); // Replace with the path to your XML file
            ArrayList<String> lat_list =new ArrayList<String>();
            ArrayList<String> lon_list =new ArrayList<String>();
            ArrayList<String> element_list =new ArrayList<String>();
            ArrayList<String> time_list =new ArrayList<String>();
           // String creator="";
            
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList wptNodes = doc.getElementsByTagName("wpt");
            NodeList gpxNode =doc.getElementsByTagName("gpx");
            Element gpxElement = (Element) gpxNode.item(0);
            String creator = gpxElement.getAttribute("creator");
            
            
            for (int i = 0; i < wptNodes.getLength(); i++) {
                Element wptElement = (Element) wptNodes.item(i);
                String lat = wptElement.getAttribute("lat");
                String lon = wptElement.getAttribute("lon");
                String ele = wptElement.getElementsByTagName("ele").item(0).getTextContent();
                String time = wptElement.getElementsByTagName("time").item(0).getTextContent();

                System.out.println("Latitude: " + lat);lat_list.add(lat);
                System.out.println("Longitude: " + lon);lon_list.add(lon);
                System.out.println("Elevation: " + ele);element_list.add(ele);
                System.out.println("Time: " + time);time_list.add(time);
                System.out.println();
            }
            for(String lat:lat_list) {
            	System.out.println("lat is ---  "+lat);
            	
            }
            System.out.println("creator was "+creator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
