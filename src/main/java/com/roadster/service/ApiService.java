package com.roadster.service;
import java.net.http.*;
import java.net.URI;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadster.models.PoliceStation;

public class ApiService {
    private static final String API_URL = "https://40347cd4e0c3.ngrok-free.app/api/";

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
}