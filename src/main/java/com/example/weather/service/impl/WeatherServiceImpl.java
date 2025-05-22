package com.example.weather.service.impl;

import com.example.weather.domain.*;
import com.example.weather.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final ConstantsAPI constantsAPI;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    public WeatherData getWeatherByCity(String city) throws JsonProcessingException {
        Location location = getLocation(city);
        return getWeatherData(location, city);
    }

    public Location getLocation(String city) {
        String coordUrl = constantsAPI.getLocationApiUrl() + city + constantsAPI.getAppId() + constantsAPI.getApiKey();
        ResponseEntity<String> coordResp = restTemplate.exchange(coordUrl, HttpMethod.GET, null, String.class);
        JSONObject coordJson = new JSONObject(coordResp.getBody());

        double lat = coordJson.getJSONObject("coord").getDouble("lat");
        double lon = coordJson.getJSONObject("coord").getDouble("lon");
        String country = coordJson.getJSONObject("sys").optString("country", "Unknown");

        return new Location(city, country, lat, lon, new SimpleDateFormat("dd MMM, hh:mm a", Locale.ENGLISH).format(new Date()));
    }

    public WeatherData getWeatherData(Location location, String city) throws JsonProcessingException {
        String weatherUrl = constantsAPI.getWeatherApiUrl() + location.getLatitude() + constantsAPI.getUrlLon()
                + location.getLongitude() + constantsAPI.getUrlExtra() + constantsAPI.getApiKey();
        ResponseEntity<String> weatherResp = restTemplate.exchange(weatherUrl, HttpMethod.GET, null, String.class);
        JSONObject weatherJson = new JSONObject(weatherResp.getBody());

        CurrentWeather weather = parseCurrentWeather(weatherJson);
        DailyWeather dailyWeather = parseDailyWeather(weatherJson);
        List<HourlyWeather> hourlyWeather = parseHourlyWeather(weatherJson);

        return new WeatherData(weather, location, hourlyWeather, dailyWeather);
    }

    private CurrentWeather parseCurrentWeather(JSONObject weatherJson) {
        CurrentWeather weather = new CurrentWeather();
        weather.setCurrent(weatherJson.getJSONObject("current").toMap());

        if (weather.getVisibility() != null) {
            weather.setVisibility(weather.getVisibility() / 1000);
        }
        weather.setWindSpeed(weather.getWindSpeed() * 3.6);

        if (weather.getSunrise() != null && weather.getSunset() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            weather.setSunriseValue(dateFormat.format(Date.from(Instant.ofEpochSecond(weather.getSunrise()))));
            weather.setSunsetValue(dateFormat.format(Date.from(Instant.ofEpochSecond(weather.getSunset()))));
        }

        return weather;
    }

    private DailyWeather parseDailyWeather(JSONObject weatherJson) throws JsonProcessingException {
        return mapper.readValue(weatherJson.getJSONArray("daily").get(0).toString(), DailyWeather.class);
    }

    private List<HourlyWeather> parseHourlyWeather(JSONObject weatherJson) {
        List<HourlyWeather> hourlyWeather = new ArrayList<>();
        JSONArray hourlyArray = weatherJson.getJSONArray("hourly");

        for (int i = 0; i < Math.min(10, hourlyArray.length()); i++) {
            HourlyWeather hourly = new HourlyWeather();
            hourly.setHourlyWeather(hourlyArray.getJSONObject(i).toMap());
            hourlyWeather.add(hourly);
        }

        return hourlyWeather;
    }
}
