package com.example.demo.service;

import com.example.demo.repository.WeatherRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherService {

    private static final String OPEN_WEATHER_MAP_API_KEY = "7d97458ed13bf2d4e4922317047e6172";

    private static final String OPEN_WEATHER_MAP_API_URL = "https://api.openweathermap.org/data/2.5/weather";

    private static RestClient restClient = RestClient.builder().baseUrl(OPEN_WEATHER_MAP_API_URL).build();

    @Autowired
    private WeatherRepo weatherRepo;

    public String getWeather(String city, String country) {
        String fullWeatherJSON = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/")
                        .queryParam("q", city + "," + country)
                        .queryParam("appid", OPEN_WEATHER_MAP_API_KEY)
                        .build())
                .retrieve()
                .body(String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
           JsonNode jsonNode = mapper.readTree(fullWeatherJSON);

            // Navigate to the weather array and get the first object
            JsonNode weatherNode = jsonNode.get("weather").get(0);

            // Get the value of the 'description' field
            String description = weatherNode.get("description").asText();

            return description;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
