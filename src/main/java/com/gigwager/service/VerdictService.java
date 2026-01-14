package com.gigwager.service;

import com.gigwager.model.Verdict;
import org.springframework.stereotype.Service;
import java.util.List;
import com.gigwager.util.BrandConstants;

@Service
public class VerdictService {

        public Verdict calculateVerdict(double gross, double miles, double hours, String appName) {
                // Core Calculation Logic (Matches Frontend JS)
                double expenses = miles * 0.725; // 2026 IRS Rate
                double profit = gross - expenses;
                double taxes = Math.max(0, profit * 0.153);
                double netHourly = (profit - taxes) / hours;

                return getVerdictForWage(netHourly, appName);
        }

        private Verdict getVerdictForWage(double wage, String appName) {
                if (wage < 8.0) {
                        return new Verdict(
                                        "LOSS ZONE",
                                        "STOP DRIVING IMMEDIATELY. YOU ARE LOSING MONEY.",
                                        List.of(
                                                        "You are not making a wage. You are liquidating your vehicle's equity for quick cash. Every mile you drive destroys more value in your car than you earn in profit.",
                                                        "After accounting for the IRS standard costs of $0.725/mile (gas, depreciation, maintenance), your net profit is effectively zero or negative. You are effectively paying "
                                                                        + appName + " for the privilege of working.",
                                                        "This is not a job; it is a financial trap. The cash in your hand is just a loan from your future car repairs."),
                                        "Stop driving immediately and secure a W-2 income source.",
                                        "red",
                                        BrandConstants.WHY_LOSS,
                                        BrandConstants.WHO_LOSS,
                                        BrandConstants.WORST_LOSS,
                                        BrandConstants.DNA);
                } else if (wage < 15.0) {
                        return new Verdict(
                                        "SURVIVAL ZONE",
                                        "YOU ARE EARNING BELOW MINIMUM WAGE.",
                                        List.of(
                                                        "You are technically profitable, but your earnings are dangerously fragile. One bad repair or a minor accident will wipe out months of profit.",
                                                        "At this level, you are earning less than a fast-food cashier, but with 100% of the risk. You have no benefits, no overtime, and no safety net.",
                                                        "This income might cover gas and groceries, but it builds no long-term wealth. You are treading water."),
                                        "Recalculate your hours or switch strategies before your car depreciates further.",
                                        "orange",
                                        BrandConstants.WHY_SURVIVAL,
                                        BrandConstants.WHO_SURVIVAL,
                                        BrandConstants.WORST_SURVIVAL,
                                        BrandConstants.DNA);
                } else if (wage < 25.0) {
                        return new Verdict(
                                        "WORKABLE ZONE",
                                        "DECENT GIG INCOME, BUT REQUIRES OPTIMIZATION.",
                                        List.of(
                                                        "You are making a real profit. Your strategy is working better than most drivers. You likely drive a fuel-efficient car or know how to pick good hours.",
                                                        "However, you haven't maxed out your potential. You are trading time for money at a fair rate, but you could likely squeeze out another $5-10/hr with better multi-apping strategies.",
                                                        "Keep doing what you're doing, but look for small edges to increase efficiency."),
                                        "Adopt multi-apping or schedule optimization to reclaim your lost margin.",
                                        "yellow",
                                        BrandConstants.WHY_WORKABLE,
                                        BrandConstants.WHO_WORKABLE,
                                        BrandConstants.WORST_WORKABLE,
                                        BrandConstants.DNA);
                } else {
                        return new Verdict(
                                        "OPTIMIZATION ZONE",
                                        "ELITE TIER. YOU HAVE CRACKED THE CODE.",
                                        List.of(
                                                        "You are in the top 5% of gig workers. You treat this like a business, not a job. You understand cost-per-mile and time management perfectly.",
                                                        "At this level, " + appName
                                                                        + " is a legitimate high-income tool. You are likely driving a hybrid/EV or have mastered market timing.",
                                                        "Your biggest risk now is complacency. Markets change. Keep adapting to stay in this bracket."),
                                        "Scale this specific strategy immediately while the market inefficiency exists.",
                                        "emerald",
                                        BrandConstants.WHY_OPTIMIZATION,
                                        BrandConstants.WHO_OPTIMIZATION,
                                        BrandConstants.WORST_OPTIMIZATION,
                                        BrandConstants.DNA);
                }
        }
}
