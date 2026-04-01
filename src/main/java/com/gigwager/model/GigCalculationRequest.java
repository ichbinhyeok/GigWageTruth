package com.gigwager.model;

public record GigCalculationRequest(
                double gross,
                double miles,
                double hours,
                double tips,
                double bonuses,
                double activeTime,
                double gasPrice,
                double taxRate,
                boolean roundTrip,
                String calculationMode,
                String vehicleId,
                double customMpg,
                double customMaintenance,
                double customDepreciation) {

        public GigCalculationRequest {
                calculationMode = calculationMode == null || calculationMode.isBlank() ? "standard"
                                : calculationMode;
                vehicleId = vehicleId == null || vehicleId.isBlank() ? "irs-standard" : vehicleId;
        }

        public static GigCalculationRequest standard(double gross, double miles, double hours) {
                return new GigCalculationRequest(
                                gross,
                                miles,
                                hours,
                                0.0,
                                0.0,
                                0.0,
                                3.50,
                                15.3,
                                false,
                                "standard",
                                "irs-standard",
                                25.0,
                                0.12,
                                0.15);
        }

        public static GigCalculationRequest fromInputs(
                        Double gross,
                        Double miles,
                        Double hours,
                        Double tips,
                        Double bonuses,
                        Double activeTime,
                        Double gasPrice,
                        Double taxRate,
                        Boolean roundTrip,
                        String calculationMode,
                        String vehicleId,
                        Double customMpg,
                        Double customMaintenance,
                        Double customDepreciation) {
                return new GigCalculationRequest(
                                gross != null ? gross : 0.0,
                                miles != null ? miles : 0.0,
                                hours != null ? hours : 0.0,
                                tips != null ? tips : 0.0,
                                bonuses != null ? bonuses : 0.0,
                                activeTime != null ? activeTime : 0.0,
                                gasPrice != null ? gasPrice : 3.50,
                                taxRate != null ? taxRate : 15.3,
                                roundTrip != null && roundTrip,
                                calculationMode,
                                vehicleId,
                                customMpg != null ? customMpg : 25.0,
                                customMaintenance != null ? customMaintenance : 0.12,
                                customDepreciation != null ? customDepreciation : 0.15);
        }

        public boolean usesAdvancedMode() {
                return "advanced".equalsIgnoreCase(calculationMode);
        }
}
