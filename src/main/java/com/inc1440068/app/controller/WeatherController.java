package com.inc1440068.app.controller;

import com.inc1440068.app.model.WeatherInfo;
import com.inc1440068.app.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public ResponseEntity<WeatherInfo> getWeather(@RequestParam(defaultValue = "Charlotte") String city) {
        WeatherInfo weatherInfo = weatherService.getWeather(city);
        return ResponseEntity.ok(weatherInfo);
    }
} 