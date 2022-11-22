import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.text.html.HTML;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class TagCounter extends DefaultHandler {
    Map<String, Integer> tags = new HashMap<String, Integer>();
    BusStop busStop;
    ArrayList<BusStop> stops;

    double[] args;

    public TagCounter(String[] arguments) {
        this.args = new double[arguments.length];
        for(int i = 0; i < arguments.length; i ++) {
            this.args[i] = Double.parseDouble(arguments[i]);
        }
    }



    public void startElement(String namespaceURI,
                             String sName, String qName, Attributes attributes)
            throws SAXException {

        //1. Feladat
        if(tags.containsKey(qName)) {
            tags.replace(qName, tags.get(qName) + 1);
        }
        else {
            tags.put(qName, 1);
        }

        //2. Feladat
        if(Objects.equals(qName, "node")) {
            busStop = new BusStop();
            busStop.lat = Double.parseDouble(attributes.getValue("lat"));
            busStop.lon = Double.parseDouble(attributes.getValue("lon"));
        } else if (Objects.equals(qName, "tag")) {
            if(Objects.equals(attributes.getValue("v"), "bus_stop")) {
                busStop.valid = !busStop.valid;
            } else if (Objects.equals(attributes.getValue("k"), "name")) {
                busStop.name = attributes.getValue("v");
            } else if (Objects.equals(attributes.getValue("k"), "old_name")) {
                busStop.oldName = attributes.getValue("v");
            } else if (Objects.equals(attributes.getValue("k"), "wheelchair")) {
                busStop.wheelChair = attributes.getValue("v");
            }
        }
    }

    @Override
    public void endElement(String uri,
                    String localName, String qName) {
        if(Objects.equals(qName, "node")) {
            if(busStop.valid) {
                /*System.out.println("Megálló:");
                System.out.print( "\tNév: " + busStop.name);
                if(busStop.oldName != null)
                    System.out.print(" (" + busStop.oldName + ')');
                System.out.println("\n\tKerekesszék: " + busStop.wheelChair + "\n");*/

                busStop.distance = dist1(busStop.lat, busStop.lon, args[0], args[1]);

                if(stops == null) {
                    stops = new ArrayList<BusStop>(1);
                }
                else {
                    ArrayList<BusStop> temp = new ArrayList<BusStop>(stops.size() + 1);
                    for (int i = 0; i < stops.size(); i++) {
                        temp.add(i, stops.get(i));
                    }
                    stops = temp;
                }

                if(stops.size() > 0) {
                    int j = 0;
                    while(j < stops.size()) {
                        if (stops.get(j) != null) {
                            if(busStop.distance > stops.get(j).distance)
                                j ++;
                            else break;
                        }
                    }
                    //System.out.println("");

                    stops.add(j, busStop);
                }
                else {
                    stops.add(0, busStop);
                    System.out.println("elso" + busStop.distance);
                }

                busStop = null;
            }
        }
    }


    public void endDocument() throws SAXException {
        System.out.println("End document: " + stops.size());
        /*BiConsumer<String, Integer> TagConsumer = (tag, number) -> {
            System.out.println(tag + " " + number);
        };
        tags.forEach(TagConsumer);*/

        for(int i = 0; i < stops.size(); i ++) {
            System.out.println("Megálló:");
            System.out.print( "\tNév: " + stops.get(i).name);
            if(stops.get(i).oldName != null)
                System.out.print(" (" + stops.get(i).oldName + ')');
            System.out.println("\n\tKerekesszék: " + stops.get(i).wheelChair);
            System.out.println("\tTávolság: " + stops.get(i).distance + '\n');
        }
    }

    public static void main(String[] args) {
        DefaultHandler h = new TagCounter(args);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser p = factory.newSAXParser();
            p.parse(new java.io.File("bme.xml"), h);
        } catch (Exception e) {e.printStackTrace();}



    }

    double dist1(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // metres
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double dphi = phi2-phi1;
        double dl = Math.toRadians(lon2-lon1);
        double a = Math.sin(dphi/2) * Math.sin(dphi/2) +
                Math.cos(phi1) * Math.cos(phi2) *
                        Math.sin(dl/2) * Math.sin(dl/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d;
    }

    public class BusStop {
        String name = null;
        String oldName = null;
        String wheelChair = null;
        boolean valid = false;
        double distance;
        double lat;
        double lon;

        public BusStop() {
            wheelChair = "none";
            valid = false;
        }
    }
}
