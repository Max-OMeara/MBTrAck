package Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    private static final String API_KEY = "";
    private static final String BASE_URL = "https://api-v3.mbta.com/vehicles?filter[route]=Green-C";

    public static void main(String[] args) {
        printLineLocations();
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
                    String routeId = trainData.getJSONObject("relationships").getJSONObject("route").getJSONObject("data").getString("id");

                    System.out.println("Train on route " + routeId + ": Latitude = " + latitude + ", Longitude = " + longitude);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
