package com.gigwager;

import com.gigwager.controller.ProgrammaticSeoController;
import com.gigwager.model.CityData;
import com.gigwager.model.CityScenario;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WageProxyDifferentiationTest {

    @Test
    public void highWageCitiesShouldNotCollapseToSameGrossEstimate() throws Exception {
        ProgrammaticSeoController controller = new ProgrammaticSeoController(null, null, null, null);
        Method calculateScenario = ProgrammaticSeoController.class.getDeclaredMethod(
                "calculateScenario",
                String.class, int.class, int.class, int.class, CityData.class, String.class);
        calculateScenario.setAccessible(true);

        CityScenario sanFrancisco = (CityScenario) calculateScenario.invoke(
                controller, "test", 1000, 100, 10, CityData.SAN_FRANCISCO, "uber");
        CityScenario seattle = (CityScenario) calculateScenario.invoke(
                controller, "test", 1000, 100, 10, CityData.SEATTLE, "uber");

        assertNotEquals(sanFrancisco.getGrossWeekly(), seattle.getGrossWeekly(),
                "High-cost cities should not collapse to identical gross estimates");
        assertTrue(seattle.getGrossWeekly() > sanFrancisco.getGrossWeekly(),
                "Higher min-wage market should preserve higher gross proxy");
    }

    @Test
    public void wageProxyShouldStayWithinConfiguredBounds() throws Exception {
        ProgrammaticSeoController controller = new ProgrammaticSeoController(null, null, null, null);
        Method calculateWageProxy = ProgrammaticSeoController.class.getDeclaredMethod("calculateWageProxy", CityData.class);
        calculateWageProxy.setAccessible(true);

        double tulsaProxy = (double) calculateWageProxy.invoke(controller, CityData.TULSA);
        double seattleProxy = (double) calculateWageProxy.invoke(controller, CityData.SEATTLE);

        assertTrue(tulsaProxy >= 0.85 && tulsaProxy <= 1.85, "Low-cost proxy should remain in bounds");
        assertTrue(seattleProxy >= 0.85 && seattleProxy <= 1.85, "High-cost proxy should remain in bounds");
    }
}

