package com.gigwager;

import com.gigwager.model.CityData;
import com.gigwager.model.CityIntentPage;
import com.gigwager.model.WorkLevel;
import com.gigwager.service.CityRichContentRepository;
import com.gigwager.service.PageIndexPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class PageIndexPolicyServiceTest {

    private CityRichContentRepository cityRichContentRepository;
    private PageIndexPolicyService pageIndexPolicyService;

    @BeforeEach
    public void setup() {
        cityRichContentRepository = Mockito.mock(CityRichContentRepository.class);
        pageIndexPolicyService = new PageIndexPolicyService(cityRichContentRepository);
    }

    @Test
    public void testHighTierCityIsIndexable() {
        // San Francisco is HIGH tier
        CityData sf = CityData.SAN_FRANCISCO;
        when(cityRichContentRepository.hasRichCitedContent(anyString())).thenReturn(true);
        assertTrue(pageIndexPolicyService.isCityReportIndexable(sf));
    }

    @Test
    public void testMedTierCityWithDataBlockIsIndexable() {
        // Austin is MED tier
        CityData austin = CityData.AUSTIN;
        when(cityRichContentRepository.hasRichCitedContent("austin")).thenReturn(true);
        assertTrue(pageIndexPolicyService.isCityReportIndexable(austin));
    }

    @Test
    public void testMedTierCityWithoutDataBlockIsNotIndexable() {
        // A generic MED tier city with no specific local data layer
        CityData fresno = CityData.FRESNO;
        when(cityRichContentRepository.hasRichCitedContent(anyString())).thenReturn(false);
        assertFalse(pageIndexPolicyService.isCityReportIndexable(fresno));
    }

    @Test
    public void testWorkLevelIndexability() {
        CityData sf = CityData.SAN_FRANCISCO;
        when(cityRichContentRepository.hasRichCitedContent("san-francisco")).thenReturn(true);
        when(cityRichContentRepository.hasWorkLevelContent("san-francisco", "side-hustle")).thenReturn(true);
        when(cityRichContentRepository.hasWorkLevelContent("san-francisco", "part-time")).thenReturn(true);
        when(cityRichContentRepository.hasWorkLevelContent("san-francisco", "full-time")).thenReturn(true);

        assertTrue(pageIndexPolicyService.isWorkLevelReportIndexable(sf, WorkLevel.SIDE_HUSTLE));
        assertTrue(pageIndexPolicyService.isWorkLevelReportIndexable(sf, WorkLevel.PART_TIME));
        assertTrue(pageIndexPolicyService.isWorkLevelReportIndexable(sf, WorkLevel.FULL_TIME));

        // Unindexable city shouldn't index its side hustle page either
        CityData fresno = CityData.FRESNO;
        when(cityRichContentRepository.hasRichCitedContent(anyString())).thenReturn(false);
        assertFalse(pageIndexPolicyService.isWorkLevelReportIndexable(fresno, WorkLevel.SIDE_HUSTLE));
    }

    @Test
    public void testIntentIndexabilityUsesPriorityIntentSet() {
        CityData denver = CityData.DENVER;
        when(cityRichContentRepository.hasRichCitedContent("denver")).thenReturn(true);

        assertTrue(pageIndexPolicyService.isCityIntentPageIndexable(denver, "doordash", CityIntentPage.DAILY_100));
        assertTrue(pageIndexPolicyService.isCityIntentPageIndexable(denver, "doordash", CityIntentPage.BEST_AREAS));
        assertFalse(pageIndexPolicyService.isCityIntentPageIndexable(denver, "uber", CityIntentPage.BEST_AREAS));
        assertFalse(pageIndexPolicyService.isCityIntentPageIndexable(denver, "doordash", CityIntentPage.MONTHLY_1000));
    }
}
