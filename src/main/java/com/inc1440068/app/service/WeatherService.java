package com.inc1440068.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inc1440068.app.model.WeatherInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    public WeatherInfo getWeather(String city) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, city, apiKey);
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            WeatherInfo weatherInfo = new WeatherInfo();
            weatherInfo.setCity(root.path("name").asText())
                    .setTemperature(root.path("main").path("temp").asText() + "°C")
                    .setHumidity(root.path("main").path("humidity").asText() + "%")
                    .setWindSpeed(root.path("wind").path("speed").asText() + " m/s")
                    .setDescription(root.path("weather").get(0).path("description").asText());

            return weatherInfo;
        } catch (Exception e) {
            logger.error("Error fetching weather data", e);
            throw new RuntimeException("Failed to fetch weather data", e);
        }
    }
} 