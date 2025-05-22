package com.example.weather.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HourlyWeather {
    private String timeValue;
    private double temperature;
    private String weatherIcon;
    private double rain;
    private double pop;

    @JsonProperty("hourly")
    public void setHourlyWeather(Map<String, Object> hourly) {
        Integer unixTime = (Integer) hourly.get("dt");
        if (unixTime != null) {
            this.timeValue = new SimpleDateFormat("h:mm a", Locale.ENGLISH).format(Date.from(Instant.ofEpochSecond(unixTime)));
        }
        this.temperature = ((Number) hourly.get("temp")).doubleValue();
        this.pop = ((Number) hourly.get("pop")).doubleValue();
        this.rain = hourly.containsKey("rain") ? ((Number) ((Map<String, Object>) hourly.get("rain")).get("1h")).doubleValue() : 0;

        List<Map<String, Object>> weatherEntries = (List<Map<String, Object>>) hourly.get("weather");
        if (!weatherEntries.isEmpty()) {
            Map<String, Object> weather = weatherEntries.get(0);
            this.weatherIcon = "http://openweathermap.org/img/wn/" + weather.get("icon") + ".png";
        }
    }
}
