package com.gigwager.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigwager.model.VehiclePreset;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VehiclePresetCatalog {

        private final ObjectMapper objectMapper;
        private final Map<String, VehiclePreset> presets = new HashMap<>();

        public VehiclePresetCatalog() {
                this(new ObjectMapper());
        }

        @Autowired
        public VehiclePresetCatalog(ObjectMapper objectMapper) {
                this.objectMapper = objectMapper;
        }

        @PostConstruct
        void loadPresets() {
                presets.clear();
                presets.putAll(readPresets());
        }

        public VehiclePreset find(String vehicleId) {
                if (presets.isEmpty()) {
                        presets.putAll(readPresets());
                }

                VehiclePreset preset = presets.get(vehicleId);
                if (preset != null) {
                        return preset;
                }
                return presets.getOrDefault("irs-standard",
                                new VehiclePreset("irs-standard", "IRS Standard Rate 2026", "standard", 0.725, null,
                                                null, null, null, null, null));
        }

        private Map<String, VehiclePreset> readPresets() {
                try (InputStream inputStream = new ClassPathResource("static/vehicle-presets.json").getInputStream()) {
                        List<VehiclePreset> presetList = objectMapper.readValue(inputStream,
                                        new TypeReference<List<VehiclePreset>>() {
                                        });
                        Map<String, VehiclePreset> loaded = new HashMap<>();
                        for (VehiclePreset preset : presetList) {
                                loaded.put(preset.id(), preset);
                        }
                        return loaded;
                } catch (IOException e) {
                        throw new IllegalStateException("Failed to load vehicle presets from classpath", e);
                }
        }
}
