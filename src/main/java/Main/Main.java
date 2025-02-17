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
    
    public static String locationToStation(double latitude, double longitude){
        if(isWithinRangeLongitude(longitude, -71.14103911295325, -71.1391021606729)){
            return "Warren";
        }
        else if(isWithinRangeLongitude(longitude, -71.1391021606729, -71.13591507424903)){ 
            return "Allston";
        }
        else if(isWithinRangeLongitude(longitude, -71.13591507424903, -71.13281401970175)){
            return "Griggs";
        }
        else if(isWithinRangeLongitude(longitude, -71.13281401970175, -71.12800849093668)){
            return "Harvard Ave";
        }
        else if(isWithinRangeLongitude(longitude, -71.12800849093668, -71.12258154210207)){
            return "Packards";
        }
        else if(isWithinRangeLongitude(longitude, -71.12258154210207, -71.11735404770603)){ 
            return "Babcock";
        }
        else if(isWithinRangeLongitude(longitude, -71.11735404770603, -71.1103529839365)){ 
            return "Amory";
        }
        else if(isWithinRangeLongitude(longitude, -71.1103529839365, -71.10583693586047)){ 
            return "BU Central";
        }
        else if(isWithinRangeLongitude(longitude, -71.10583693586047, -71.10220543356607)){ 
            return "BU East";
        }
        else if(isWithinRangeLongitude(longitude, -71.10220543356607, -71.09755123833298)){
            return "Blandford";
        }
        else if(isWithinRangeLongitude(longitude, -71.09755123833298, -71.09157048916403)){
            return "Kenmore";
        }
        else if(isWithinRangeLongitude(longitude, -71.09157048916403, -71.08283834424013)){
            return "Hynes";
        }
        else if(isWithinRangeLongitude(longitude, -71.08283834424013, 71.07428387483759)){ 
            return "Copely";
        }
        else if(isWithinRangeLongitude(longitude, 71.07428387483759, -71.06756822617031)){
            return "Arlington";
        }
        else if(isWithinRangeLongitude(longitude, -71.06756822617031, -71.06331594392393)){
            return "Boylston";
        }
        else if(isWithinRangeLongitude(longitude, -71.06331594392393, -71.06041496108917)){ 
            return "Park";
        }
        else if(isWithinRangeLongitude(longitude, -71.06041496108917, -71.05825188655594)){ 
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
