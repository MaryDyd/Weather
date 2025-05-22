package com.example.weather.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyWeather {
    private double morningTemperature;
    private double dayTemperature;
    private double nightTemperature;

    @JsonProperty("temp")
    public void setTemp(Map<String, Object> daily) {
        this.dayTemperature = ((Number) daily.get("day")).doubleValue();
        this.morningTemperature = ((Number) daily.get("morn")).doubleValue();
        this.nightTemperature = ((Number) daily.get("night")).doubleValue();
    }
}
