package com.example.weather.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.weather.domain.*;
import com.example.weather.service.impl.WeatherServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceImplTest {

    @Mock
    private ConstantsAPI constantsAPI;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    private String city;
    private Location mockLocation;
    private WeatherData mockWeatherData;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        city = "Dnipro";
        mockLocation = new Location(city, "UA", 50.4501, 30.5234, "22 May, 14:30 PM");
        mockWeatherData = new WeatherData(new CurrentWeather(), mockLocation, new ArrayList<>(), new DailyWeather());

        weatherService = spy(new WeatherServiceImpl(constantsAPI, restTemplate, mapper));

        JSONObject coordJson = new JSONObject();
        coordJson.put("coord", new JSONObject().put("lat", 50.4501).put("lon", 30.5234));
        coordJson.put("sys", new JSONObject().put("country", "UA"));

        ResponseEntity<String> coordResp = ResponseEntity.ok(coordJson.toString());
        lenient().when(restTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenReturn(coordResp);

        lenient().doReturn(mockLocation).when(weatherService).getLocation(anyString());
        lenient().doReturn(mockWeatherData).when(weatherService).getWeatherData(any(Location.class), anyString());
    }

    @Test
    void testGetLocation() {
        Location location = weatherService.getLocation(city);

        assertNotNull(location);
        assertEquals(city, location.getCityName());
        assertEquals("UA", location.getCountry());
        assertEquals(50.4501, location.getLatitude());
        assertEquals(30.5234, location.getLongitude());
    }

    @Test
    void testGetWeatherByCity() throws JsonProcessingException {
        WeatherData weatherData = weatherService.getWeatherByCity(city);

        assertNotNull(weatherData);
        assertEquals(city, weatherData.getLocation().getCityName());
        verify(weatherService, times(1)).getLocation(city);
        verify(weatherService, times(1)).getWeatherData(mockLocation, city);
    }
}
