package com.gigwager.dto;

import com.gigwager.model.CityData;

public record CityRankingDto(CityData city, double netHourly, String workLevel) {
}
