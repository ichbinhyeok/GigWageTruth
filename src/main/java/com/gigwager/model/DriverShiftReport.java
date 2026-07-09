package com.gigwager.model;

import com.gigwager.util.AppConstants;

public record DriverShiftReport(
        String app,
        String citySlug,
        String cityName,
        String state,
        String evidenceType,
        String shiftWindow,
        String reportedResult,
        Double grossPay,
        Double activeHours,
        Double onlineHours,
        Integer miles,
        Integer deliveries,
        String takeaway,
        String sourceLabel,
        String sourceUrl) {

    public boolean hasGrossPay() {
        return grossPay != null && grossPay > 0;
    }

    public boolean hasActiveHours() {
        return activeHours != null && activeHours > 0;
    }

    public boolean hasOnlineHours() {
        return onlineHours != null && onlineHours > 0;
    }

    public boolean hasMiles() {
        return miles != null && miles > 0;
    }

    public boolean hasDeliveries() {
        return deliveries != null && deliveries > 0;
    }

    public boolean hasEstimatedNetCheck() {
        return hasGrossPay() && hasMiles() && (hasOnlineHours() || hasActiveHours());
    }

    public double grossPerActiveHour() {
        if (!hasGrossPay() || !hasActiveHours()) {
            return 0;
        }
        return grossPay / activeHours;
    }

    public double grossPerOnlineHour() {
        if (!hasGrossPay() || !hasOnlineHours()) {
            return 0;
        }
        return grossPay / onlineHours;
    }

    public double dollarsPerMile() {
        if (!hasGrossPay() || !hasMiles()) {
            return 0;
        }
        return grossPay / miles;
    }

    public double estimatedNetProfitAfterMileageAndTax() {
        if (!hasGrossPay() || !hasMiles()) {
            return 0;
        }
        double mileageProxy = miles * AppConstants.IRS_MILEAGE_RATE;
        double taxableProfit = grossPay - mileageProxy;
        double taxes = Math.max(0, taxableProfit * AppConstants.SELF_EMPLOYMENT_TAX_RATE);
        return grossPay - mileageProxy - taxes;
    }

    public double estimatedNetHourlyAfterMileageAndTax() {
        if (!hasEstimatedNetCheck()) {
            return 0;
        }
        double hours = hasOnlineHours() ? onlineHours : activeHours;
        return estimatedNetProfitAfterMileageAndTax() / hours;
    }
}
