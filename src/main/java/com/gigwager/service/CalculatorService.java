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
        double gasCost = calculateGasCost(miles, mpg, gasPrice);

        // Note: IRS rate includes gas, but for 'real' cashflow perception handling,
        // we strictly follow the user's manifesto formula:
        // Gross - (Miles / MPG * Gas Price) - (Miles * $0.67 IRS Rate) - (Gross *
        // 15.3%)
        // Wait, deducting BOTH gas AND IRS rate (which covers gas) is double counting
        // slightly in 'accounting' terms,
        // BUT for 'Brutal Verdict' on 'Economic Cost', deducting the IRS rate
        // (depreciation+maintenance+gas) is correct.
        // Adding explicit Gas Cost on top of IRS rate is aggressive.
        // Let's stick to the User's requested formula in the manifesto:
        // Formula: Gross - (Miles / MPG * Gas Price) - (Miles * $0.67 IRS Rate) -
        // (Gross * 15.3%)

        double irsDeduction = calculateDepreciation(miles);
        double tax = grossRevenue * AppConstants.SELF_EMPLOYMENT_TAX_RATE; // Simplified tax on Gross as per manifesto
                                                                           // prompt?
        // Usually tax is on Net, but manifesto said: (Gross * 15.3%). This is a
        // simpler, more 'Brutal' estimation.

        return grossRevenue - gasCost - irsDeduction - tax - otherExpenses;
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
