package com.roadster.service;
import java.net.http.*;
import java.net.URI;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadster.models.PoliceStation;
import com.roadster.models.Driver;

public class ApiService {
    private static final String API_URL = "https://172f1abdbfab.ngrok-free.app/api/";

    public static List<PoliceStation> fetchPoliceStations() throws Exception {
        String apiUrl = API_URL + "police-stations";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), new TypeReference<List<PoliceStation>>() {});
    }

    /**
     * Fetches police stations with their districts from the API
     * Returns a list of arrays where each array contains [station_name, district_name]
     */
    public static List<String[]> fetchPoliceStationsWithDistricts() throws Exception {
        String apiUrl = API_URL + "police-stations/with-districts";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("ngrok-skip-browser-warning", "true") // Add header to skip ngrok browser warning
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), new TypeReference<List<String[]>>() {});
    }

    /**
     * Fetches police stations filtered by district
     * @param district The district to filter by (e.g., "Chattogram", "Dhaka")
     * @return List of police station names in the specified district
     */
    public static List<String> fetchPoliceStationsByDistrict(String district) throws Exception {
        List<String[]> allStations = fetchPoliceStationsWithDistricts();
        List<String> filteredStations = new ArrayList<>();

        for (String[] station : allStations) {
            if (station.length >= 2 && station[1].equalsIgnoreCase(district)) {
                filteredStations.add(station[0]);
            }
        }

        return filteredStations;
    }

    /**
     * Gets all unique districts from the police stations data
     * @return List of unique district names
     */
    public static List<String> fetchAllDistricts() throws Exception {
        List<String[]> allStations = fetchPoliceStationsWithDistricts();
        Set<String> uniqueDistricts = new HashSet<>();

        for (String[] station : allStations) {
            if (station.length >= 2) {
                uniqueDistricts.add(station[1]);
            }
        }

        return new ArrayList<>(uniqueDistricts);
    }

    /**
     * Gets police stations as a Map with district as key and list of stations as value
     * @return Map<District, List<Station Names>>
     */
    public static Map<String, List<String>> fetchPoliceStationsGroupedByDistrict() throws Exception {
        List<String[]> allStations = fetchPoliceStationsWithDistricts();
        Map<String, List<String>> groupedStations = new HashMap<>();

        for (String[] station : allStations) {
            if (station.length >= 2) {
                String district = station[1];
                String stationName = station[0];

                groupedStations.computeIfAbsent(district, k -> new ArrayList<>()).add(stationName);
            }
        }

        return groupedStations;
    }

    // ==================== DRIVER METHODS ====================

    /**
     * Fetches all drivers from the API
     * @return List of Driver objects
     */
    public static List<Driver> fetchAllDrivers() throws Exception {
        String apiUrl = API_URL + "drivers";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("ngrok-skip-browser-warning", "true")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), new TypeReference<List<Driver>>() {});
    }

    /**
     * Fetches drivers filtered by district
     * @param district The district to filter by (e.g., "Chattogram", "Dhaka")
     * @return List of drivers in the specified district
     */
    public static List<Driver> fetchDriversByDistrict(String district) throws Exception {
        List<Driver> allDrivers = fetchAllDrivers();
        List<Driver> filteredDrivers = new ArrayList<>();

        for (Driver driver : allDrivers) {
            if (driver.getDistrict().equalsIgnoreCase(district)) {
                filteredDrivers.add(driver);
            }
        }

        return filteredDrivers;
    }

    /**
     * Fetches a specific driver by ID
     * @param driverId The ID of the driver to fetch
     * @return Driver object or null if not found
     */
    public static Driver fetchDriverById(int driverId) throws Exception {
        String apiUrl = API_URL + "drivers/" + driverId;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("ngrok-skip-browser-warning", "true")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 404) {
            return null; // Driver not found
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), Driver.class);
    }

    /**
     * Searches drivers by name (case-insensitive partial match)
     * @param searchTerm The search term to match against driver names
     * @return List of drivers whose names contain the search term
     */
    public static List<Driver> searchDriversByName(String searchTerm) throws Exception {
        List<Driver> allDrivers = fetchAllDrivers();
        List<Driver> matchingDrivers = new ArrayList<>();

        String searchLower = searchTerm.toLowerCase();
        for (Driver driver : allDrivers) {
            if (driver.getName().toLowerCase().contains(searchLower)) {
                matchingDrivers.add(driver);
            }
        }

        return matchingDrivers;
    }

    /**
     * Searches drivers by license number (exact or partial match)
     * @param licenseNumber The license number to search for
     * @return List of drivers with matching license numbers
     */
    public static List<Driver> searchDriversByLicense(String licenseNumber) throws Exception {
        List<Driver> allDrivers = fetchAllDrivers();
        List<Driver> matchingDrivers = new ArrayList<>();

        for (Driver driver : allDrivers) {
            if (driver.getLicenseNumber().contains(licenseNumber)) {
                matchingDrivers.add(driver);
            }
        }

        return matchingDrivers;
    }

    /**
     * Gets drivers grouped by district
     * @return Map<District, List<Driver>>
     */
    public static Map<String, List<Driver>> fetchDriversGroupedByDistrict() throws Exception {
        List<Driver> allDrivers = fetchAllDrivers();
        Map<String, List<Driver>> groupedDrivers = new HashMap<>();

        for (Driver driver : allDrivers) {
            String district = driver.getDistrict();
            groupedDrivers.computeIfAbsent(district, k -> new ArrayList<>()).add(driver);
        }

        return groupedDrivers;
    }

    /**
     * Gets unique districts from drivers data
     * @return List of unique district names from drivers
     */
    public static List<String> fetchDriverDistricts() throws Exception {
        List<Driver> allDrivers = fetchAllDrivers();
        Set<String> uniqueDistricts = new HashSet<>();

        for (Driver driver : allDrivers) {
            uniqueDistricts.add(driver.getDistrict());
        }

        return new ArrayList<>(uniqueDistricts);
    }

    /**
     * Gets driver count statistics by district
     * @return Map<District, Integer> with driver counts
     */
    public static Map<String, Integer> getDriverCountByDistrict() throws Exception {
        Map<String, List<Driver>> groupedDrivers = fetchDriversGroupedByDistrict();
        Map<String, Integer> driverCounts = new HashMap<>();

        for (Map.Entry<String, List<Driver>> entry : groupedDrivers.entrySet()) {
            driverCounts.put(entry.getKey(), entry.getValue().size());
        }

        return driverCounts;
    }
}