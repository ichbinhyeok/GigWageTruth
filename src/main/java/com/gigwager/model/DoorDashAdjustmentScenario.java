package com.gigwager.model;

import java.util.Locale;

public record DoorDashAdjustmentScenario(
                String marketLabel,
                String ruleLabel,
                String localRule,
                double activeHours,
                double activeMiles,
                double doordashPayBeforeTips,
                double tips,
                double minimumHourlyRate,
                double mileageRate,
                String officialExample,
                String sourceLabel,
                String sourceUrl) {

        public double guaranteedMinimum() {
                return (activeHours * minimumHourlyRate) + (activeMiles * mileageRate);
        }

        public double estimatedAdjustment() {
                return Math.max(0, guaranteedMinimum() - doordashPayBeforeTips);
        }

        public double totalWithTips() {
                return doordashPayBeforeTips + estimatedAdjustment() + tips;
        }

        public String activeTimeLabel() {
                if (activeHours < 1.0) {
                        return String.format(Locale.US, "%.0f min", activeHours * 60.0);
                }
                if (Double.compare(activeHours, Math.rint(activeHours)) == 0) {
                        return String.format(Locale.US, "%.0f hrs", activeHours);
                }
                return String.format(Locale.US, "%.1f hrs", activeHours);
        }

        public String activeMilesLabel() {
                if (activeMiles <= 0) {
                        return "No mileage line";
                }
                return String.format(Locale.US, "%.1f mi", activeMiles);
        }

        public String hourlyRateLabel() {
                return String.format(Locale.US, "$%.2f/hr", minimumHourlyRate);
        }

        public String mileageRateLabel() {
                if (mileageRate <= 0) {
                        return "No mileage rate";
                }
                return String.format(Locale.US, "$%.2f/mi", mileageRate);
        }
}
