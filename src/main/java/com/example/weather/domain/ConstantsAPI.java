package com.example.weather.domain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ConstantsAPI {
    @Value("${location.url}")
    private String locationApiUrl;

    @Value("${weather.urlLat}")
    private String weatherApiUrl;

    @Value("${weather.urlLon}")
    private String urlLon;

    @Value("${location.appId}")
    private String appId;

    @Value("${weather.urlExtra}")
    private String urlExtra;

    @Value("${weather.appKey}")
    private String apiKey;
}
