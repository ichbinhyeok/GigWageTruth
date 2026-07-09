package com.gigwager.model;

public record DoorDashMoneyIntent(
                String slug,
                String displayName,
                String searchPhrase,
                String headline,
                String answerLead,
                String primaryMetricLabel,
                String primaryMetricValue,
                String secondaryMetricLabel,
                String secondaryMetricValue,
                int grossLow,
                int grossHigh,
                int netLow,
                int netHigh,
                double hoursLow,
                double hoursHigh,
                int milesLow,
                int milesHigh,
                String bestWindow,
                String failureMode,
                String decisionRule) {

        public String path() {
                return "/doordash/" + slug;
        }

        public String grossRange() {
                return "$" + grossLow + "-$" + grossHigh;
        }

        public String netRange() {
                return "$" + netLow + "-$" + netHigh;
        }

        public String hourRange() {
                if (Double.compare(hoursLow, hoursHigh) == 0) {
                        return String.format("%.1f hrs", hoursLow);
                }
                return String.format("%.1f-%.1f hrs", hoursLow, hoursHigh);
        }

        public String mileRange() {
                return milesLow + "-" + milesHigh + " mi";
        }
}
