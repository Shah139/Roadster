package com.roadster.service;
import java.net.http.*;
import java.net.URI;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadster.models.PoliceStation;
import com.roadster.models.Driver;

public class ApiService {
    private static final String API_URL = "https://roadster-backend.onrender.com/api/";

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

    // ==================== CROSSWALK METHODS ====================

    /**
     * Fetches crosswalk status data for a specific district
     * @param district The district to get crosswalk data for
     * @return List of Map containing crosswalk data
     */
    public static List<Map<String, Object>> fetchCrosswalksByDistrict(String district) throws Exception {
        try {
            String apiUrl = API_URL + "crosswalks/" + district;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("ngrok-skip-browser-warning", "true")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200 || !response.body().trim().startsWith("[")) {
                // Return fallback data if API is not available or returns non-JSON
                List<Map<String, Object>> fallbackData = new ArrayList<>();
                Map<String, Object> crosswalk1 = new HashMap<>();
                crosswalk1.put("crosswalkId", 1);
                crosswalk1.put("location", "Main Street & 1st Avenue");
                crosswalk1.put("status", "ACTIVE");
                crosswalk1.put("lastMaintenance", "2025-08-15");
                
                Map<String, Object> crosswalk2 = new HashMap<>();
                crosswalk2.put("crosswalkId", 2);
                crosswalk2.put("location", "Park Road & Central Ave");
                crosswalk2.put("status", "MAINTENANCE");
                crosswalk2.put("lastMaintenance", "2025-09-01");
                
                fallbackData.add(crosswalk1);
                fallbackData.add(crosswalk2);
                return fallbackData;
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            System.err.println("API error for crosswalks, using fallback data: " + e.getMessage());
            // Return fallback data
            List<Map<String, Object>> fallbackData = new ArrayList<>();
            Map<String, Object> crosswalk = new HashMap<>();
            crosswalk.put("crosswalkId", 1);
            crosswalk.put("location", "Main Street");
            crosswalk.put("status", "ACTIVE");
            crosswalk.put("lastMaintenance", "2025-08-15");
            fallbackData.add(crosswalk);
            return fallbackData;
        }
    }

    // ==================== REPORTS METHODS ====================

    /**
     * Fetches report counts for a specific district
     * @param district The district to get report data for
     * @return List of Map containing report data
     */
    public static List<Map<String, Object>> fetchReportsByDistrict(String district) throws Exception {
        try {
            String apiUrl = API_URL + "reports/" + district;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("ngrok-skip-browser-warning", "true")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200 || !response.body().trim().startsWith("[")) {
                // Return fallback data if API is not available or returns non-JSON
                List<Map<String, Object>> fallbackData = new ArrayList<>();
                Map<String, Object> report1 = new HashMap<>();
                report1.put("name", "GEC Circle Intersection");
                report1.put("count", 1);
                
                Map<String, Object> report2 = new HashMap<>();
                report2.put("name", "Panchlaish Model Thana");
                report2.put("count", 1);
                
                Map<String, Object> report3 = new HashMap<>();
                report3.put("name", "Kotwali Thana");
                report3.put("count", 1);
                
                fallbackData.add(report1);
                fallbackData.add(report2);
                fallbackData.add(report3);
                return fallbackData;
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            System.err.println("API error for reports, using fallback data: " + e.getMessage());
            // Return fallback data
            List<Map<String, Object>> fallbackData = new ArrayList<>();
            Map<String, Object> report1 = new HashMap<>();
            report1.put("name", "GEC Circle Intersection");
            report1.put("count", 1);
            
            Map<String, Object> report2 = new HashMap<>();
            report2.put("name", "Panchlaish Model Thana");
            report2.put("count", 1);
            
            fallbackData.add(report1);
            fallbackData.add(report2);
            return fallbackData;
        }
    }

    // ==================== TRAFFIC METHODS ====================

    /**
     * Fetches traffic levels for a specific district
     * @param district The district to get traffic data for
     * @return List of Map containing traffic data
     */
    public static List<Map<String, Object>> fetchTrafficByDistrict(String district) throws Exception {
        try {
            String apiUrl = API_URL + "traffic/" + district;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("ngrok-skip-browser-warning", "true")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200 || !response.body().trim().startsWith("[")) {
                // Return fallback data if API is not available or returns non-JSON
                List<Map<String, Object>> fallbackData = new ArrayList<>();
                Map<String, Object> highTraffic = new HashMap<>();
                highTraffic.put("trafficLevel", "High Traffic");
                highTraffic.put("areaCount", 6);
                
                Map<String, Object> mediumTraffic = new HashMap<>();
                mediumTraffic.put("trafficLevel", "Medium Traffic");
                mediumTraffic.put("areaCount", 7);
                
                Map<String, Object> lowTraffic = new HashMap<>();
                lowTraffic.put("trafficLevel", "Low Traffic");
                lowTraffic.put("areaCount", 3);
                
                fallbackData.add(highTraffic);
                fallbackData.add(mediumTraffic);
                fallbackData.add(lowTraffic);
                return fallbackData;
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            System.err.println("API error for traffic, using fallback data: " + e.getMessage());
            // Return fallback data
            List<Map<String, Object>> fallbackData = new ArrayList<>();
            Map<String, Object> highTraffic = new HashMap<>();
            highTraffic.put("trafficLevel", "High Traffic");
            highTraffic.put("areaCount", 6);
            
            Map<String, Object> mediumTraffic = new HashMap<>();
            mediumTraffic.put("trafficLevel", "Medium Traffic");
            mediumTraffic.put("areaCount", 7);
            
            fallbackData.add(highTraffic);
            fallbackData.add(mediumTraffic);
            return fallbackData;
        }
    }

    // ==================== CRIME DATA METHODS ====================

    /**
     * Fetches crime statistics for pie chart (not filtered by district)
     * @return List of Map containing crime data
     */
    public static List<Map<String, Object>> fetchCrimeData() throws Exception {
        try {
            String apiUrl = API_URL + "crimes/by-district";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("ngrok-skip-browser-warning", "true")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200 || !response.body().trim().startsWith("[")) {
                // Return fallback data if API is not available or returns non-JSON
                List<Map<String, Object>> fallbackData = new ArrayList<>();
                Map<String, Object> crime1 = new HashMap<>();
                crime1.put("district", "Chattogram");
                crime1.put("count", 2);
                
                Map<String, Object> crime2 = new HashMap<>();
                crime2.put("district", "Dhaka");
                crime2.put("count", 7);
                
                Map<String, Object> crime3 = new HashMap<>();
                crime3.put("district", "Sylhet");
                crime3.put("count", 1);
                
                Map<String, Object> crime4 = new HashMap<>();
                crime4.put("district", "Khulna");
                crime4.put("count", 3);
                
                Map<String, Object> crime5 = new HashMap<>();
                crime5.put("district", "Rajshahi");
                crime5.put("count", 2);
                
                fallbackData.add(crime1);
                fallbackData.add(crime2);
                fallbackData.add(crime3);
                fallbackData.add(crime4);
                fallbackData.add(crime5);
                return fallbackData;
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            System.err.println("API error for crime data, using fallback data: " + e.getMessage());
            // Return fallback crime data with district format
            List<Map<String, Object>> fallbackData = new ArrayList<>();
            Map<String, Object> crime1 = new HashMap<>();
            crime1.put("district", "Chattogram");
            crime1.put("count", 2);
            
            Map<String, Object> crime2 = new HashMap<>();
            crime2.put("district", "Dhaka");
            crime2.put("count", 7);
            
            Map<String, Object> crime3 = new HashMap<>();
            crime3.put("district", "Sylhet");
            crime3.put("count", 1);
            
            fallbackData.add(crime1);
            fallbackData.add(crime2);
            fallbackData.add(crime3);
            return fallbackData;
        }
    }

    // ==================== AREA CRIME RATE METHODS ====================

    /**
     * Fetches crime rates by area for a specific district
     * @param district The district to get area crime rate data for
     * @return List of Map containing area crime rate data
     */
    public static List<Map<String, Object>> fetchAreaCrimeRatesByDistrict(String district) throws Exception {
        try {
            String apiUrl = API_URL + "crime-rates/" + district;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("ngrok-skip-browser-warning", "true")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200 || !response.body().trim().startsWith("[")) {
                // Return fallback data if API is not available or returns non-JSON
                return getAreaCrimeRatesFallbackData();
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            System.err.println("API error for area crime rates, using fallback data: " + e.getMessage());
            return getAreaCrimeRatesFallbackData();
        }
    }

    private static List<Map<String, Object>> getAreaCrimeRatesFallbackData() {
        List<Map<String, Object>> fallbackData = new ArrayList<>();
        
        // Sample areas with crime rates (Dhaka areas as example)
        String[][] areas = {
            {"Mirpur", "0.15"}, {"Pallabi", "0.12"}, {"Kafrul", "0.13"}, {"Tejgaon", "0.18"},
            {"Gulshan", "0.05"}, {"Cantonment", "0.04"}, {"Badda", "0.1"}, {"Khilkhet", "0.09"},
            {"Dhanmondi", "0.14"}, {"Shahbagh", "0.16"}, {"New Market", "0.15"}, {"Lalbagh", "0.17"},
            {"Motijheel", "0.2"}, {"Ramna", "0.19"}, {"Jatrabari", "0.18"}, {"Uttara", "0.1"},
            {"Mohammadpur", "0.16"}, {"Adabor", "0.14"}, {"Shah Ali", "0.11"}, {"Darus-Salam", "0.12"}
        };

        for (String[] area : areas) {
            Map<String, Object> crimeRate = new HashMap<>();
            crimeRate.put("name", area[0]);
            crimeRate.put("crimeRate", Double.parseDouble(area[1]));
            fallbackData.add(crimeRate);
        }

        return fallbackData;
    }

    // ==================== AREA CONGESTION METHODS ====================

    /**
     * Fetches area congestion levels for a specific district
     * @param district The district to get area congestion data for
     * @return List of Map containing area congestion data
     */
    public static List<Map<String, Object>> fetchAreaCongestionByDistrict(String district) throws Exception {
        try {
            String apiUrl = API_URL + "area-congestion/" + district;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("ngrok-skip-browser-warning", "true")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200 || !response.body().trim().startsWith("[")) {
                // Return fallback data if API is not available or returns non-JSON
                return getAreaCongestionFallbackData();
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            System.err.println("API error for area congestion, using fallback data: " + e.getMessage());
            return getAreaCongestionFallbackData();
        }
    }

    private static List<Map<String, Object>> getAreaCongestionFallbackData() {
        List<Map<String, Object>> fallbackData = new ArrayList<>();
        
        // Sample areas with congestion levels (Dhaka areas as example)
        String[][] areas = {
            {"Mirpur", "0.8"}, {"Pallabi", "0.7"}, {"Kafrul", "0.75"}, {"Tejgaon", "0.9"},
            {"Gulshan", "0.6"}, {"Cantonment", "0.5"}, {"Badda", "0.7"}, {"Khilkhet", "0.65"},
            {"Dhanmondi", "0.8"}, {"Shahbagh", "0.9"}, {"New Market", "0.85"}, {"Lalbagh", "0.95"},
            {"Motijheel", "1.0"}, {"Ramna", "0.9"}, {"Jatrabari", "0.85"}, {"Uttara", "0.6"},
            {"Mohammadpur", "0.8"}, {"Adabor", "0.75"}, {"Shah Ali", "0.6"}, {"Darus-Salam", "0.7"}
        };

        for (String[] area : areas) {
            Map<String, Object> congestion = new HashMap<>();
            congestion.put("name", area[0]);
            congestion.put("congestionLevel", area[1]);
            fallbackData.add(congestion);
        }

        return fallbackData;
    }
}