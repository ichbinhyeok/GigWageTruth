package com.gigwager;

import com.gigwager.util.AppConstants;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PricingAssumptionsTest {

    @Test
    void usesTheCorrect2026MileageRateForEachHalfOfTheYear() {
        assertEquals(0.725, AppConstants.irsMileageRate(LocalDate.of(2026, 6, 30)), 0.0001);
        assertEquals(0.76, AppConstants.irsMileageRate(LocalDate.of(2026, 7, 1)), 0.0001);
        assertEquals(0.76, AppConstants.IRS_MILEAGE_RATE, 0.0001);
    }

    @Test
    void selfEmploymentTaxEstimateUsesTheTaxableEarningsFactor() {
        assertEquals(141.2955, AppConstants.estimateSelfEmploymentTax(1000.0), 0.0001);
        assertEquals(0.0, AppConstants.estimateSelfEmploymentTax(-50.0), 0.0001);
    }

    @Test
    void publicTaxCopyDistinguishesPlanningReserveFromTaxCalculation() throws Exception {
        String calculator = Files.readString(Path.of("src/main/jte/pages/calculator.jte"));
        String methodology = Files.readString(Path.of("src/main/jte/methodology.jte"));
        String taxGuide = Files.readString(Path.of("src/main/jte/clusters/taxes.jte"));
        String ranking = Files.readString(Path.of("src/main/jte/salary/best-cities.jte"));

        assertTrue(calculator.contains("planning reserve, not an exact bill"));
        assertFalse(calculator.contains("Don't let the IRS take 15.3% of your gross"));
        assertTrue(methodology.contains("generally applied to 92.35% of net earnings"));
        assertTrue(taxGuide.contains("15.3% rate to 92.35% of that amount"));
        assertTrue(ranking.contains("15.3% rate to 92.35% of remaining profit"));
    }

    @Test
    void browserAndPresetRatesMatchTheServerRate() throws Exception {
        String calculator = Files.readString(Path.of("src/main/resources/static/js/calculator-core.js"));
        String presets = Files.readString(Path.of("src/main/resources/static/vehicle-presets.json"));

        assertTrue(calculator.contains("costPerMile: 0.76"));
        assertTrue(calculator.contains("effectiveMiles * 0.76"));
        assertTrue(presets.contains("\"costPerMile\": 0.76"));
        assertFalse(calculator.contains("effectiveMiles * 0.725"));
    }

    @Test
    void calculatorTracksAPrivacySafeOrganicConversionFunnel() throws Exception {
        String calculator = Files.readString(Path.of("src/main/resources/static/js/calculator-core.js"));

        assertTrue(calculator.contains("calculator_result_view"));
        assertTrue(calculator.contains("calculator_start"));
        assertTrue(calculator.contains("calculator_complete"));
        assertTrue(calculator.contains("result_band"));
        int trackingMethodStart = calculator.indexOf("trackCalculatorEvent(eventName");
        int trackingMethodEnd = calculator.indexOf("async fetchVerdict()", trackingMethodStart);
        String trackingMethod = calculator.substring(trackingMethodStart, trackingMethodEnd);
        assertFalse(trackingMethod.contains("gross"));
        assertFalse(trackingMethod.contains("miles"));
        assertFalse(trackingMethod.contains("hours"));
    }
}
