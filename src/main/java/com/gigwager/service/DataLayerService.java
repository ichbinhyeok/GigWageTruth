package com.gigwager.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigwager.model.CityLocalData;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataLayerService {
    private Map<String, CityLocalData> cityDataMap = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            cityDataMap = mapper.readValue(
                    new ClassPathResource("data/cities_local.json").getInputStream(),
                    new TypeReference<Map<String, CityLocalData>>() {
                    });
        } catch (IOException e) {
            System.err.println("Failed to load cities_local.json: " + e.getMessage());
        }
    }

    public CityLocalData getLocalData(String citySlug) {
        // Fallback to generic terms if city isn't in the JSON yet
        return cityDataMap.getOrDefault(citySlug,
                new CityLocalData("the downtown area", "the local shopping center", "the airport", "the main highway"));
    }

    public boolean hasLocalData(String citySlug) {
        return cityDataMap.containsKey(citySlug);
    }

    public boolean hasRichLocalData(String citySlug) {
        if (!cityDataMap.containsKey(citySlug)) {
            return false;
        }
        CityLocalData data = cityDataMap.get(citySlug);
        // Script generated defaults: "Downtown", "Main Street", "Regional Airport",
        // "Local Highway"
        if ("Downtown".equals(data.nightlifeDistrict()) && "Main Street".equals(data.shoppingDistrict())) {
            return false; // It has data, but it's just the default low-quality script output
        }
        return true;
    }
}
