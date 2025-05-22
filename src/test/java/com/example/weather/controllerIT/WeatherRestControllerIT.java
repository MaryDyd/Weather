package com.example.weather.controllerIT;

import com.example.weather.WeatherApplication;
import com.example.weather.domain.WeatherData;
import com.example.weather.domain.enums.Role;
import com.example.weather.entity.UserEntity;
import com.example.weather.repository.UserRepository;
import com.example.weather.security.config.TestSecurityConfig;
import com.example.weather.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@SpringBootTest(classes = {WeatherApplication.class, TestSecurityConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherRestControllerIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String userToken;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        UserEntity user = userRepository.save(UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .username("testuser")
                .role(Role.ADMIN)
                .build());

        userToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole());
    }

    @Test
    void testGetWeatherByCity() throws IOException {
        webTestClient.get()
                .uri("/api/v1/weather?city=Kyiv")
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WeatherData.class)
                .consumeWith(response -> {
                    WeatherData weather = response.getResponseBody();
                    assertThat(weather).isNotNull();
                    assertThat(weather.getLocation().getCityName()).isEqualTo("Kyiv");
                    assertThat(weather.getLocation().getCountry()).isEqualTo("UA");
                });
    }

    @Test
    void testGetWeatherByCityUnauthorized() {
        webTestClient.get()
                .uri("/api/v1/weather?city=Kyiv")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.message").value(msg -> assertTrue(msg.toString().contains("Access Denied")));
    }
}
