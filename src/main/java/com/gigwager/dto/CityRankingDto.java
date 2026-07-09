package com.gigwager.dto;

import com.gigwager.model.CityData;
import com.gigwager.model.CityScenario;

public record CityRankingDto(CityData city, double netHourly, String workLevel, CityScenario scenario) {
    public CityRankingDto(CityData city, double netHourly, String workLevel) {
        this(city, netHourly, workLevel, null);
    }
}
