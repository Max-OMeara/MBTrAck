package Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    private static final String API_KEY = "";
    private static final String BASE_URL = "https://api-v3.mbta.com/vehicles?filter[route]=Green-B";

    public static void main(String[] args) {
        printLineLocations();
    }

    public static boolean isWithinRangeLongitude(double value, double min, double max) {
        return value >= min && value <= max;
    }
    
    public static String locationToStation(double latitude, double longitude){ //-71.135604
        if(isWithinRangeLongitude(longitude, -71.14103911295325, -71.1391021606729)){ // new range
            return "Warren";
        }
        else if(isWithinRangeLongitude(longitude, -71.1391021606729, -71.13591507424903)){ // new range
            return "Allston";
        }
        else if(isWithinRangeLongitude(longitude, -71.13591507424903, -71.13281401970175)){ // new range
            return "Griggs";
        }
        else if(isWithinRangeLongitude(longitude, -71.13281401970175, -71.12800849093668)){ // new range
            return "Harvard Ave";
        }
        else if(isWithinRangeLongitude(longitude, -71.12800849093668, -71.12258154210207)){ // new range
            return "Packards";
        }
        else if(isWithinRangeLongitude(longitude, -71.12258154210207, -71.11735404770603)){ // new range
            return "Babcock";
        }
        else if(isWithinRangeLongitude(longitude, -71.11735404770603, -71.1103529839365)){ // new range
            return "Amory";
        }
        else if(isWithinRangeLongitude(longitude, -71.1103529839365, -71.10583693586047)){ // new range
            return "BU Central";
        }
        else if(isWithinRangeLongitude(longitude, -71.10583693586047, -71.10220543356607)){ // new range
            return "BU East";
        }
        else if(isWithinRangeLongitude(longitude, -71.10220543356607, -71.09755123833298)){ // new range
            return "Blandford";
        }
        else if(isWithinRangeLongitude(longitude, -71.09755123833298, -71.09157048916403)){ // new range
            return "Kenmore";
        }
        else if(isWithinRangeLongitude(longitude, -71.09157048916403, -71.08283834424013)){ // new range
            return "Hynes";
        }
        else if(isWithinRangeLongitude(longitude, -71.08283834424013, 71.07428387483759)){ // new range
            return "Copely";
        }
        else if(isWithinRangeLongitude(longitude, 71.07428387483759, -71.06756822617031)){ // Working
            return "Arlington";
        }
        else if(isWithinRangeLongitude(longitude, -71.06756822617031, -71.06331594392393)){ // new range
            return "Boylston";
        }
        else if(isWithinRangeLongitude(longitude, -71.06331594392393, -71.06041496108917)){ // new range
            return "Park";
        }
        else if(isWithinRangeLongitude(longitude, -71.06041496108917, -71.05825188655594)){ // new range
            return "Gov";
        }
        else if(longitude < -71.14103911295325){
            return "Past Warren";
        }
        else if(longitude > -71.05825188655594){
            return "Past Gov";
        }
        else if(latitude == 42.341 && longitude == -71.16742){
            return "Not in use";
        }
        else{
            return "unknown";
        }

        // Lat and Long of every Green Line-B Station

        // 42.359661, -71.059258 // gov
        // 42.356344, -71.062400 // park
        // 42.353095, -71.064534 // boylston
        // 42.351726, -71.071280 // arlington
        // 42.349986, -71.077724 // copely
        // 42.347945, -71.087912 // hynes
        // 42.348844, -71.095606 // kenmore
        // 42.349219, -71.100012 // blandford
        // 42.349729, -71.104299 // bu east
        // 42.350077, -71.107133 // bu central
        // 42.350986, -71.114764 // amory
        // 42.351616, -71.119928 // babcock
        // 42.351731, -71.125323 // pack
        // 42.350162, -71.131600 // harvard ave
        // 42.348834, -71.134381 // griggs
        // 42.348734, -71.138026 // allston
        // 42.348895, -71.139788 // warren
    }

    public static void printLineLocations() {
        try {
            String urlString = BASE_URL + "&api_key=" + API_KEY;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            JSONObject jsonResponse = new JSONObject(content.toString());
            JSONArray dataArray = jsonResponse.getJSONArray("data");

            if (dataArray.isEmpty()) {
                System.out.println("No Line train locations found.");
                System.out.println("API Response: " + content);
            } else {
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject trainData = dataArray.getJSONObject(i);
                    JSONObject attributes = trainData.getJSONObject("attributes");
                    double latitude = attributes.getDouble("latitude");
                    double longitude = attributes.getDouble("longitude");
                    // cconvert lat and long to String of stop name
                    String location = locationToStation(latitude,longitude);
                    String routeId = trainData.getJSONObject("relationships").getJSONObject("route").getJSONObject("data").getString("id");

                    System.out.println("Train on route " + routeId + ": Latitude = " + latitude + ", Longitude = " + longitude + " which is : " + location);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
