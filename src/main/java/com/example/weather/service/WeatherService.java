package com.example.weather.service;

import com.example.weather.domain.WeatherData;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface WeatherService {
    WeatherData getWeatherByCity(String city) throws JsonProcessingException;
}
