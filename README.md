# Weather Application

## Overview

This Weather Application is a Java-based program that retrieves and displays weather data for a user-specified location. The application supports various input formats, including city names, state abbreviations, full state names, and zip codes. It utilizes the HERE Geocoding and Weather APIs to provide accurate and up-to-date information.

## Features

- User Input Options:
  - City (e.g., "Denver")
  - City, State (e.g., "Denver, CO" or "Denver, Colorado")
  - City, State, Country (e.g., "Denver, CO, US" or "Denver, Colorado, United States")
  - Zip Code (e.g., "80246")
  
- API Integration:
  - Geocoding: Converts user-provided locations into latitude and longitude using the HERE Geocoding API.
  - Weather Data: Retrieves current weather data for the specified location using the HERE Weather API.

- Modular Design:
  - Main Class: Handles user interaction and controls the program flow.
  - WeatherService Class: Manages API requests and processes the returned data.
  - StateMapper Class: Provides mapping between state abbreviations and full state names for accurate geocoding.

## Setup

1. Clone the Repository:
   ```bash
   git clone https://github.com/mesta88/JavaWeatherApp.git
   cd JavaWeatherApp
   ```

2. Dependencies:
   - This project requires Java 11 or higher.
   - Ensure that your Java environment is properly set up.

3. Running the Application:
   - Compile and run the `Main` class using your preferred IDE or command line:
     ```bash
     javac Main.java
     java Main
     ```

## Notes

- API Key: The API key for the HERE APIs is hardcoded in the `WeatherService` class for demonstration purposes. See the comment in the code for more details.

- Exception Handling: The program includes basic exception handling for API requests and data processing.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Author

- Megan Stanford - [GitHub Profile](https://github.com/mesta88)
