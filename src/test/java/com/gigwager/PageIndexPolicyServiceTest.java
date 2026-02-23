package com.gigwager;

import com.gigwager.model.CityData;
import com.gigwager.model.WorkLevel;
import com.gigwager.service.DataLayerService;
import com.gigwager.service.PageIndexPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class PageIndexPolicyServiceTest {

    private DataLayerService dataLayerService;
    private PageIndexPolicyService pageIndexPolicyService;

    @BeforeEach
    public void setup() {
        dataLayerService = Mockito.mock(DataLayerService.class);
        pageIndexPolicyService = new PageIndexPolicyService(dataLayerService);
    }

    @Test
    public void testHighTierCityIsIndexable() {
        // San Francisco is HIGH tier
        CityData sf = CityData.SAN_FRANCISCO;
        when(dataLayerService.hasRichLocalData(anyString())).thenReturn(true);
        assertTrue(pageIndexPolicyService.isCityReportIndexable(sf));
    }

    @Test
    public void testMedTierCityWithDataBlockIsIndexable() {
        // Austin is MED tier
        CityData austin = CityData.AUSTIN;
        when(dataLayerService.hasRichLocalData("austin")).thenReturn(true);
        assertTrue(pageIndexPolicyService.isCityReportIndexable(austin));
    }

    @Test
    public void testMedTierCityWithoutDataBlockIsNotIndexable() {
        // A generic MED tier city with no specific local data layer
        CityData fresno = CityData.FRESNO;
        when(dataLayerService.hasRichLocalData(anyString())).thenReturn(false);
        assertFalse(pageIndexPolicyService.isCityReportIndexable(fresno));
    }

    @Test
    public void testWorkLevelIndexability() {
        // We only index SIDE_HUSTLE work levels for indexable cities
        CityData sf = CityData.SAN_FRANCISCO;
        when(dataLayerService.hasRichLocalData("san-francisco")).thenReturn(true);

        assertTrue(pageIndexPolicyService.isWorkLevelReportIndexable(sf, WorkLevel.SIDE_HUSTLE));
        assertFalse(pageIndexPolicyService.isWorkLevelReportIndexable(sf, WorkLevel.PART_TIME));
        assertFalse(pageIndexPolicyService.isWorkLevelReportIndexable(sf, WorkLevel.FULL_TIME));

        // Unindexable city shouldn't index its side hustle page either
        CityData fresno = CityData.FRESNO;
        when(dataLayerService.hasRichLocalData(anyString())).thenReturn(false);
        assertFalse(pageIndexPolicyService.isWorkLevelReportIndexable(fresno, WorkLevel.SIDE_HUSTLE));
    }
}
