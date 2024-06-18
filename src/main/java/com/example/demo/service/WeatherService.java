package com.example.demo.service;

import com.example.demo.entity.Weather;
import com.example.demo.exception.CityNotFoundException;
import com.example.demo.repository.WeatherRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class WeatherService {

    private static final String OPEN_WEATHER_MAP_API_KEY = "7d97458ed13bf2d4e4922317047e6172";

    private static final String OPEN_WEATHER_MAP_API_URL = "https://api.openweathermap.org/data/2.5/weather";

    private static RestClient restClient = RestClient.builder().baseUrl(OPEN_WEATHER_MAP_API_URL).build();

    @Autowired
    private WeatherRepo weatherRepo;

    /**
     * Get weather description from DB or call the external service if data doesn't exist
     *
     * @param city
     * @param country
     * @return
     */
    public String getWeatherDesc(String city, String country) {
        String cityAndCountry = String.format("%s,%s", city, country);
        String citySignature = DigestUtils.md5Hex(cityAndCountry);
        Weather weather = weatherRepo.findById(citySignature).orElseGet(() -> {
            String fullWeatherJSON = restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/")
                            .queryParam("q", cityAndCountry)
                            .queryParam("appid", OPEN_WEATHER_MAP_API_KEY)
                            .build())
                    .retrieve()
                    .onStatus(status -> status.value() == 404, (request, response) -> {
                        throw new CityNotFoundException("City not Found");
                    })
                    .body(String.class);

            String weatherDescription = extractDescription(fullWeatherJSON);
            return saveWeather(citySignature, weatherDescription);
        });

        return weather.getDesc();
    }

    /**
     * Saves the weather description to the database
     * @param citySignature
     * @param description
     * @return
     */
    private Weather saveWeather(String citySignature, String description) {
        return weatherRepo.save(new Weather(citySignature, description));
    }

    /*
     * Extracts the description from the full weather JSON string
     */
    private String extractDescription(String fullWeatherJSON) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(fullWeatherJSON);
            JsonNode weatherNode = jsonNode.get("weather").get(0);
            String description = weatherNode.get("description").asText();

            return description;
        } catch (Exception e) {
            log.error("Error extracting description from JSON: {}", e.getMessage());
            throw new RuntimeException("Server Error, Please contact administrator");
        }
    }
}
