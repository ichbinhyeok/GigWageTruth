package com.gigwager.model;

public record DoorDashDurationEstimate(
        String slug,
        String displayName,
        String searchPhrase,
        double hours,
        int grossLow,
        int grossHigh,
        int denseGrossLow,
        int denseGrossHigh,
        int netLow,
        int netHigh,
        String bestWindow,
        String weakWindow,
        String strategyNote) {

    public boolean isWeekly() {
        return "week".equals(slug);
    }

    public double midpointGross() {
        return (grossLow + grossHigh) / 2.0;
    }

    public double midpointNet() {
        return (netLow + netHigh) / 2.0;
    }

    public double netHourlyLow() {
        return hours <= 0 ? 0 : netLow / hours;
    }

    public double netHourlyHigh() {
        return hours <= 0 ? 0 : netHigh / hours;
    }
}
