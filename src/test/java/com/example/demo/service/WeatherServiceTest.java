
package com.example.demo.service;

import com.example.demo.entity.Weather;
import com.example.demo.exception.CityNotFoundException;
import com.example.demo.repository.WeatherRepo;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WeatherServiceTest {

    @Mock
    private WeatherRepo weatherRepo;

    @Mock
    private RestClient mockRestClient;

    @InjectMocks
    private WeatherService weatherService;

    private String citySignature = "867574bbf93313c36297748104b53592"; // Assuming this is the MD5 of cityAndCountry


    @Test
    public void testGetWeatherDescFromDB() {
        Weather mockWeather = new Weather(citySignature, "Clouds");
        when(weatherRepo.findById(anyString())).thenReturn(Optional.of(mockWeather));

        String result = weatherService.getWeatherDesc("Melbourne", "AU");
        assertEquals("Clouds", result);
        verify(weatherRepo).findById(anyString()); // Verify weatherRepo.findById was called
    }


    @Test
    public void testCityNotFoundException() {
        when(weatherRepo.findById(anyString())).thenReturn(Optional.empty());
        Mockito.when(mockRestClient.get()).thenThrow(CityNotFoundException.class);
        assertThrows(CityNotFoundException.class, () -> weatherService.getWeatherDesc("Unknown", "Country"));
    }

}