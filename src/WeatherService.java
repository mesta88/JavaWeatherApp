/**
 * Weather Application
 * Author: Megan Stanford
 * Description:
 *     This service handles the retrieval of weather data by using the HERE API.
 *     It geocodes a location and fetches the weather data for the corresponding latitude and longitude.
 */

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherService {

    /**
     * The API key is hardcoded for demonstration purposes to simplify the review process.
     * In a production environment, I would store API keys in environment variables
     * or secure configuration files.
     */
    private static final String HERE_API_KEY = "lXXc0SJQuwfC0OOincG83h8OdRaL5ZkLriTK5OykPok";
    private static final String HERE_GEOCODING_URL = "https://geocode.search.hereapi.com/v1/geocode";
    private static final String HERE_WEATHER_URL = "https://weather.ls.hereapi.com/weather/1.0/report.json";

    /**
     * Retrieves weather data for the given location input.
     * Returns both the formatted location and weather data.
     *
     * @param locationInput The location input provided by the user.
     * @return A string array containing the location and weather data, or null if the data could not be retrieved.
     */
    public static String[] getWeather(String locationInput) {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        // Normalize the input to handle state abbreviations
        String[] locationParts = locationInput.split(",");
        if (locationParts.length >= 2) {
            String state = locationParts[1].trim();
            String fullStateName = StateMapper.getStateFullName(state);
            locationParts[1] = fullStateName; // Replace abbreviation with full name
            locationInput = String.join(", ", locationParts);
        }

        // Use HERE Geocoding API to get lat/lon from city, state, country
        String geoData = getHereGeocodingData(client, locationInput);
        if (geoData == null) {
            System.err.println("Failed to retrieve geocoding data.");
            return null;
        }

        JSONObject geoJson = new JSONObject(geoData);
        JSONArray items = geoJson.getJSONArray("items");
        if (items.length() == 0) {
            System.err.println("Location not found.");
            return null;
        }

        JSONObject location = items.getJSONObject(0);
        JSONObject position = location.getJSONObject("position");
        double lat = position.getDouble("lat");
        double lon = position.getDouble("lng");
        String address = location.getJSONObject("address").getString("label");

        // Use lat/lon to get the weather data
        String requestUrl = HERE_WEATHER_URL + "?product=observation&latitude=" + lat + "&longitude=" + lon + "&apiKey=" + HERE_API_KEY;

        // Uncomment the line below for debugging purposes
        // System.out.println("Request URL: " + requestUrl);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String weatherData = response.body();

            // Return both the location and weather data
            return new String[]{address, weatherData};
        } catch (Exception e) {
            System.err.println("An error occurred while fetching weather data: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves geocoding data for the given location using the HERE Geocoding API.
     *
     * @param client        The HTTP client used to send the request.
     * @param locationInput The location input provided by the user.
     * @return A JSON string containing geocoding data, or null if the data could not be retrieved.
     */
    private static String getHereGeocodingData(HttpClient client, String locationInput) {
        try {
            String encodedLocation = URLEncoder.encode(locationInput.trim(), StandardCharsets.UTF_8.name());
            String geoRequestUrl = HERE_GEOCODING_URL + "?q=" + encodedLocation + "&apiKey=" + HERE_API_KEY;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(geoRequestUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            System.err.println("An error occurred while fetching geocoding data: " + e.getMessage());
            return null;
        }
    }
}
