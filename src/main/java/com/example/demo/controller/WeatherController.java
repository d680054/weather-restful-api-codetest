package com.example.demo.controller;

import com.example.demo.service.WeatherService;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    //search weather by city name and country name
    @GetMapping("/query")
    public String getWeather(@RequestParam String city, @RequestParam String country) {

        if (!StringUtils.hasText(city)) {
            throw new IllegalArgumentException("City cannot be empty or null");
        }


        String encodedCity = encodeUrlParam(city);
        String encodedCountry = encodeUrlParam(country);

        return weatherService.getWeather(encodedCity, encodedCountry);
    }

    private String encodeUrlParam(String param) {
        try {
            return java.net.URLEncoder.encode(param, "UTF-8").replace("+", "%20");
        } catch (Exception e) {
            throw new RuntimeException("Error encoding URL parameter", e);
        }
    }

}
