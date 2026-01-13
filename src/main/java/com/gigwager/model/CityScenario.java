package com.gigwager.model;

public class CityScenario {
    private final String name;
    private final int grossWeekly;
    private final int miles;
    private final int hours;
    private final double netProfit;
    private final double netHourly;

    public CityScenario(String name, int grossWeekly, int miles, int hours,
            double netProfit, double netHourly) {
        this.name = name;
        this.grossWeekly = grossWeekly;
        this.miles = miles;
        this.hours = hours;
        this.netProfit = netProfit;
        this.netHourly = netHourly;
    }

    public String getName() {
        return name;
    }

    public int getGrossWeekly() {
        return grossWeekly;
    }

    public int getMiles() {
        return miles;
    }

    public int getHours() {
        return hours;
    }

    public double getNetProfit() {
        return netProfit;
    }

    public double getNetHourly() {
        return netHourly;
    }
}
