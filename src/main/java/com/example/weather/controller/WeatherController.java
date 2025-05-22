package com.example.weather.controller;

import com.example.weather.service.WeatherService;
import com.example.weather.domain.WeatherData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam String city, Model model) throws IOException {
        WeatherData weatherData = weatherService.getWeatherByCity(city);
        model.addAttribute("weather", weatherData.getWeather());
        model.addAttribute("location", weatherData.getLocation());
        model.addAttribute("hourlyWeather", weatherData.getHourlyWeather());
        model.addAttribute("daily", weatherData.getDailyWeather());
        return "index";
    }
}
