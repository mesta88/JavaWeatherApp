/**
 * Weather Application
 * Author: Megan Stanford
 * Description:
 *     This program retrieves and displays weather data for a user-specified location.
 *     It supports input formats like city names, state abbreviations, full state names, and zip codes.
 *     The program loops until the user chooses to exit.
 */

import org.json.JSONObject;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String locationInput;

        while (true) {
            System.out.println("\nEnter a location in one of the following formats (or type 'Exit' to quit):");
            System.out.println("1. City");
            System.out.println("2. City, State");
            System.out.println("3. City, State, Country");
            System.out.println("4. Zip Code");
            locationInput = scanner.nextLine();

            if (locationInput.equalsIgnoreCase("Exit")) {
                System.out.println("Exiting program...");
                break;
            }

            String[] weatherData = WeatherService.getWeather(locationInput);
            if (weatherData != null) {
                // Print the location
                System.out.println("\n" + weatherData[0]);

                // Print formatted weather data
                printFormattedWeatherData(weatherData[1]);
            } else {
                System.out.println("Failed to retrieve weather data for " + locationInput);
            }
        }
    }

    /**
     * Formats and prints weather data based on the provided JSON string.
     * Extracts temperature, humidity, and description from the JSON.
     *
     * @param weatherJsonData The JSON string containing weather data.
     */
    private static void printFormattedWeatherData(String weatherJsonData) {
        if (weatherJsonData.isEmpty()) {
            System.out.println("Weather data is unavailable.");
            return;
        }

        JSONObject jsonObject = new JSONObject(weatherJsonData);

        JSONObject observations = jsonObject.optJSONObject("observations");
        if (observations != null) {
            JSONObject locationData = observations.optJSONArray("location").optJSONObject(0);
            if (locationData != null) {
                JSONObject observation = locationData.optJSONArray("observation").optJSONObject(0);

                double tempC = observation != null ? observation.optDouble("temperature", Double.NaN) : Double.NaN;
                double tempF = !Double.isNaN(tempC) ? (tempC * 9 / 5) + 32 : Double.NaN;
                int humidity = observation != null ? observation.optInt("humidity", -1) : -1;
                String description = observation != null ? observation.optString("description", "No description available") : "No description available";

                System.out.println("Temperature: " + String.format("%.2f°C / %.2f°F", tempC, tempF));
                if (humidity != -1) {
                    System.out.println("Humidity: " + humidity + "%");
                } else {
                    System.out.println("Humidity data unavailable.");
                }
                System.out.println("Weather: " + description);
            } else {
                System.out.println("No observation data available.");
            }
        } else {
            System.out.println("No observations available in the provided data.");
        }
    }
}
