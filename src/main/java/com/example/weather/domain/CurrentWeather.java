package com.example.weather.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentWeather {
    private String weatherMain;
    private String weatherDescription;
    private String weatherIcon;
    private double temperature;
    private double feelsLike;
    private Integer pressure;
    private Integer humidity;
    private Integer visibility;
    private double windSpeed;
    private Integer windDegree;
    private Integer sunrise;
    private Integer sunset;
    private String sunriseValue;
    private String sunsetValue;
    private double pop;


    @JsonProperty("current")
    public void setCurrent(Map<String, Object> current) {
        this.temperature = ((Number) current.get("temp")).doubleValue();
        this.feelsLike = ((Number) current.get("feels_like")).doubleValue();
        this.pressure = (Integer) current.get("pressure");
        this.humidity = (Integer) current.get("humidity");
        this.visibility = (Integer) current.get("visibility");
        this.windSpeed = ((Number) current.get("wind_speed")).doubleValue();
        this.windDegree = (Integer) current.get("wind_deg");
        this.sunrise = (Integer) current.get("sunrise");
        this.sunset = (Integer) current.get("sunset");

        List<Map<String, Object>> weatherEntries = (List<Map<String, Object>>) current.get("weather");
        if (!weatherEntries.isEmpty()) {
            Map<String, Object> weather = weatherEntries.get(0);
            this.weatherMain = (String) weather.get("main");
            this.weatherDescription = (String) weather.get("description");
            this.weatherIcon = "http://openweathermap.org/img/wn/" + weather.get("icon") + "@4x.png";
        }
    }
}
