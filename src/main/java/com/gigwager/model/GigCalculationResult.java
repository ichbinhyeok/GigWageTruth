package com.gigwager.model;

public record GigCalculationResult(
                double gross,
                double tips,
                double bonuses,
                double totalEarnings,
                double hours,
                double activeTime,
                double activeDriveTime,
                double waitTime,
                double baseMiles,
                double effectiveMiles,
                double gasCost,
                double otherCost,
                double totalDeduction,
                double profitBeforeTax,
                double taxReserve,
                double takeHome,
                double realHourly,
                double activeHourly,
                String biggestLeakLabel,
                double biggestLeakAmount,
                String calculationMode,
                String vehicleId) {

        public boolean hasScenario() {
                return totalEarnings > 0 || baseMiles > 0 || hours > 0;
        }

        public boolean usesAdvancedMode() {
                return "advanced".equalsIgnoreCase(calculationMode);
        }
}
