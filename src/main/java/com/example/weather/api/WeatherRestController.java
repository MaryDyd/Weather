package com.example.weather.api;

import com.example.weather.service.WeatherService;
import com.example.weather.domain.WeatherData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
@Tag(name = "Weather API", description = "Get weather by city name")
public class WeatherRestController {
    private final WeatherService weatherService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get weather by city", description = "Returns weather data for the specified city")
    public WeatherData getWeather(@RequestParam String city) throws IOException {
        return weatherService.getWeatherByCity(city);
    }
}
