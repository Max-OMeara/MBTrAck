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

    public static boolean isWithinRangeLatitude(double value, double min, double max) {
        return value >= min && value <= max;
    }

    public static boolean isWithinRangeLongitude(double value, double max, double min) {
        return value >= min && value <= max;
    }
    
    public static String locationToStation(double latitude, double longitude){ //-71.135604
        if(isWithinRangeLatitude(latitude, 42.346070, 42.348812) && isWithinRangeLongitude(longitude, -71.141312, -71.138946)){ // new range
            return "Warren";
        }
        else if(isWithinRangeLatitude(latitude, 42.348812, 42.348271) && isWithinRangeLongitude(longitude, -71.1362035, -71.138946)){ // new range
            return "Allston";
        }
        else if(isWithinRangeLatitude(latitude, 42.349498, 42.3509465) && isWithinRangeLongitude(longitude, -71.1329905, -71.1284615)){
            return "Griggs";
        }
        else if(isWithinRangeLatitude(latitude, 42.3509465, 42.3516735) && isWithinRangeLongitude(longitude, -71.1284615, -71.1226255)){
            return "Packards";
        }
        else if(isWithinRangeLatitude(latitude, 42.3516735, 42.351301) && isWithinRangeLongitude(longitude, -71.1226255, -71.117346)){
            return "Babcock";
        }
        else if(isWithinRangeLatitude(latitude, 42.351301, 42.3505315) && isWithinRangeLongitude(longitude, -71.117346, -71.1109485)){
            return "Amory";
        }
        else if(isWithinRangeLatitude(latitude, 42.3505315, 42.349903) && isWithinRangeLongitude(longitude, -71.1109485, -71.105716)){
            return "BU Central";
        }
        else if(isWithinRangeLatitude(latitude, 42.349903, 42.349474) && isWithinRangeLongitude(longitude, -71.105716, -71.1021555)){
            return "BU East";
        }
        else if(isWithinRangeLatitude(latitude, 42.349474, 42.3490315) && isWithinRangeLongitude(longitude, -71.1021555, -71.097809)){
            return "Blandford";
        }
        else if(isWithinRangeLatitude(latitude, 42.3490315, 42.3483945) && isWithinRangeLongitude(longitude, -71.097809, -71.091759)){
            return "Kenmore";
        }
        else if(isWithinRangeLatitude(latitude, 42.3483945, 42.3489655) && isWithinRangeLongitude(longitude, -71.091759, -71.082818)){
            return "Hynes";
        }
        else if(isWithinRangeLatitude(latitude, 42.3489655, 42.350856) && isWithinRangeLongitude(longitude, -71.082818, -71.074502)){
            return "Copely";
        }
        else if(isWithinRangeLatitude(latitude, 42.350856, 42.3524105) && isWithinRangeLongitude(longitude, -71.074502, -71.067907)){ // Working
            return "Arlington";
        }
        else if(isWithinRangeLatitude(latitude, 42.3524105, 42.3547195) && isWithinRangeLongitude(longitude, -71.067907, -71.063467)){
            return "Boylston";
        }
        else if(isWithinRangeLatitude(latitude, 42.3547195, 42.3580025) && isWithinRangeLongitude(longitude, -71.063467, -71.060829)){ // Working
            return "Park";
        }
        else if(isWithinRangeLatitude(latitude, 42.3580025, 42.359661) && isWithinRangeLongitude(longitude, -71.060829, -71.059258)){ // Working
            return "Gov";
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
