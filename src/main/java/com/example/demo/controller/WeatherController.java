package com.example.demo.controller;

import com.example.demo.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    /**
     * Get weather description
     *
     * @param city
     * @param country
     * @return
     */
    @GetMapping("/query")
    public ResponseEntity getWeather(@RequestParam String city, @RequestParam String country) {
        String desc = weatherService.getWeatherDesc(city, country);

        return ResponseEntity.ok(desc);
    }
}
