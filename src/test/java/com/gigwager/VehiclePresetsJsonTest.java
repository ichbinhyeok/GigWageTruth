package com.gigwager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VehiclePresetsJsonTest {

    @Test
    public void vehiclePresetsShouldBeValidJson() throws Exception {
        Path path = Path.of("src/main/resources/static/vehicle-presets.json");
        String json = Files.readString(path);
        new ObjectMapper().readTree(json);
        assertTrue(json.contains("\"irs-standard\""),
                "vehicle-presets.json must include the IRS standard preset");
    }
}

