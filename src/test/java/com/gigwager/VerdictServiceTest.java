package com.gigwager;

import com.gigwager.model.Verdict;
import com.gigwager.service.VerdictService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VerdictServiceTest {

    private final VerdictService service = new VerdictService();

    @Test
    public void testZeroDivisionPrevention() {
        // Appname, Gross, Miles, Hours
        Verdict verdictZero = service.calculateVerdict(100.0, 50.0, 0.0, "Uber");
        assertNotNull(verdictZero, "Verdict should not be null when hours are zero");
        assertEquals("LOSS ZONE", verdictZero.level(),
                "Hours=0 should fallback to a 0 net hourly, placing it in LOSS ZONE");

        Verdict verdictNegative = service.calculateVerdict(100.0, 50.0, -5.0, "DoorDash");
        assertNotNull(verdictNegative, "Verdict should not be null when hours are negative");
        assertEquals("LOSS ZONE", verdictNegative.level(), "Hours<0 should also fallback similarly safely");
    }

    @Test
    public void testNormalCalculation() {
        // $200 gross, 100 miles, 10 hours =>
        // Expenses = 100 * 0.725 = 72.5
        // Profit = 200 - 72.5 = 127.5
        // Taxes = 127.5 * 0.153 = 19.5075
        // Net = 127.5 - 19.5075 = 107.9925
        // Net Hourly = 107.9925 / 10 = ~10.79
        // 10.79 is SURVIVAL ZONE (<15)

        Verdict verdict = service.calculateVerdict(200.0, 100.0, 10.0, "Uber");
        assertEquals("SURVIVAL ZONE", verdict.level(), "Should correctly calculate typical side hustle");
    }
}
