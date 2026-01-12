package com.gigwager.service;

import com.gigwager.util.AppConstants;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    public double calculateGasCost(double miles, double mpg, double gasPrice) {
        if (mpg <= 0)
            return 0;
        return (miles / mpg) * gasPrice;
    }

    public double calculateDepreciation(double miles) {
        return miles * AppConstants.IRS_MILEAGE_RATE_2024;
    }

    public double calculateTax(double grossRevenue, double deductibleExpenses) {
        double taxableIncome = grossRevenue - deductibleExpenses;
        if (taxableIncome < 0)
            taxableIncome = 0;
        return taxableIncome * AppConstants.SELF_EMPLOYMENT_TAX_RATE;
    }

    public double calculateNetProfit(double grossRevenue, double miles, double mpg, double gasPrice,
            double otherExpenses) {
        // Corrected Logic (User Request):
        // 1. IRS Rate ($0.67) ALREADY includes Gas, Insurance, Maintenance.
        // So we DO NOT subtract Gas Cost separately in the verdict.
        // 2. Tax is calculated on (Gross - IRS Deduction), i.e., Net Profit.

        double irsDeduction = calculateDepreciation(miles);
        double tax = calculateTax(grossRevenue, irsDeduction);

        return grossRevenue - irsDeduction - tax - otherExpenses;
    }

    public String getVerdict(double netHourlyWage) {
        if (netHourlyWage < AppConstants.MIN_WAGE_BURGER_KING) {
            return "STOP DRIVING";
        }
        return "KEEP GRINDING";
    }

    public String getVerdictColor(double netHourlyWage) {
        if (netHourlyWage < AppConstants.MIN_WAGE_BURGER_KING) {
            return "text-rose-600";
        }
        return "text-emerald-600";
    }
}
