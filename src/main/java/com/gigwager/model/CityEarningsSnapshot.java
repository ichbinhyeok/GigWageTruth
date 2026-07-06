package com.gigwager.model;

public record CityEarningsSnapshot(
                CityData city,
                CityScenario scenario,
                String primaryQuery,
                String secondaryQuery,
                String acquisitionNote) {
}
