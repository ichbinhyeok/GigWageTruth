package com.gigwager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VehiclePreset(
                String id,
                String name,
                String type,
                Double costPerMile,
                Double mpg,
                Double maintenanceCostPerMile,
                Double depreciationCostPerMile,
                Double avgGasPrice,
                Double avgElectricityCost,
                Double kwhPer100Miles) {

        public boolean isElectric() {
                return "electric".equalsIgnoreCase(type);
        }

        public boolean isBikeOrWalker() {
                return "bike".equalsIgnoreCase(type) || "walker".equalsIgnoreCase(type);
        }

        public boolean isCustom() {
                return "custom".equalsIgnoreCase(id);
        }

        public double mpgOr(double fallback) {
                return mpg != null ? mpg : fallback;
        }

        public double maintenanceCostPerMileOr(double fallback) {
                return maintenanceCostPerMile != null ? maintenanceCostPerMile : fallback;
        }

        public double depreciationCostPerMileOr(double fallback) {
                return depreciationCostPerMile != null ? depreciationCostPerMile : fallback;
        }

        public double avgGasPriceOr(double fallback) {
                return avgGasPrice != null ? avgGasPrice : fallback;
        }

        public double avgElectricityCostOr(double fallback) {
                return avgElectricityCost != null ? avgElectricityCost : fallback;
        }

        public double kwhPer100MilesOr(double fallback) {
                return kwhPer100Miles != null ? kwhPer100Miles : fallback;
        }
}
