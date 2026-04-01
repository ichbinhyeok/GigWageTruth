package com.gigwager;

import com.gigwager.model.GigCalculationRequest;
import com.gigwager.model.GigCalculationResult;
import com.gigwager.service.GigCalculationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GigCalculationServiceTest {

    private final GigCalculationService service = new GigCalculationService();

    @Test
    void standardCalculationUsesSharedTakeHomeMath() {
        GigCalculationResult result = service.calculate(GigCalculationRequest.standard(1000.0, 800.0, 40.0));

        assertEquals(580.0, result.totalDeduction(), 0.0001);
        assertEquals(64.26, result.taxReserve(), 0.0001);
        assertEquals(355.74, result.takeHome(), 0.0001);
        assertEquals(8.8935, result.realHourly(), 0.0001);
        assertEquals("Mileage + car cost", result.biggestLeakLabel());
    }

    @Test
    void advancedModeHonorsVehicleAndRoundTripInputs() {
        GigCalculationRequest request = GigCalculationRequest.fromInputs(
                900.0,
                300.0,
                20.0,
                100.0,
                50.0,
                15.0,
                4.0,
                15.3,
                true,
                "advanced",
                "custom",
                30.0,
                0.10,
                0.20);

        GigCalculationResult result = service.calculate(request);

        assertEquals(600.0, result.effectiveMiles(), 0.0001);
        assertEquals(80.0, result.gasCost(), 0.0001);
        assertEquals(180.0, result.otherCost(), 0.0001);
        assertEquals(260.0, result.totalDeduction(), 0.0001);
        assertEquals(120.87, result.taxReserve(), 0.0001);
        assertEquals(669.13, result.takeHome(), 0.0001);
        assertEquals(33.4565, result.realHourly(), 0.0001);
        assertEquals(44.6087, result.activeHourly(), 0.0001);
    }
}
