package com.example.weather.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class WeatherData {
    private CurrentWeather weather;
    private Location location;
    private List<HourlyWeather> hourlyWeather;
    private DailyWeather dailyWeather;
}
