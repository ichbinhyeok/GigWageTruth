package com.gigwager.service;

import com.gigwager.model.GigCalculationRequest;
import com.gigwager.model.GigCalculationResult;
import com.gigwager.model.VehiclePreset;
import com.gigwager.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GigCalculationService {

        private static final double DEFAULT_GAS_PRICE = 3.50;
        private static final double DEFAULT_CUSTOM_MPG = 25.0;
        private static final double DEFAULT_CUSTOM_MAINTENANCE = 0.12;
        private static final double DEFAULT_CUSTOM_DEPRECIATION = 0.15;
        private static final double DEFAULT_EV_KWH_PER_100_MILES = 25.0;
        private static final double DEFAULT_EV_ELECTRICITY_COST = 0.15;

        private final VehiclePresetCatalog vehiclePresetCatalog;

        public GigCalculationService() {
                this(new VehiclePresetCatalog());
        }

        @Autowired
        public GigCalculationService(VehiclePresetCatalog vehiclePresetCatalog) {
                this.vehiclePresetCatalog = vehiclePresetCatalog;
        }

        public GigCalculationResult calculate(GigCalculationRequest request) {
                double gross = sanitizeMoney(request.gross());
                double tips = sanitizeMoney(request.tips());
                double bonuses = sanitizeMoney(request.bonuses());
                double hours = Math.max(0, request.hours());
                double activeTime = Math.max(0, Math.min(request.activeTime(), hours));
                double activeDriveTime = activeTime > 0 ? activeTime : hours;
                double waitTime = activeTime > 0 ? Math.max(0, hours - activeTime) : 0;
                double baseMiles = Math.max(0, request.miles());
                double effectiveMiles = request.roundTrip() ? baseMiles * 2 : baseMiles;
                double totalEarnings = gross + tips + bonuses;
                double normalizedTaxRate = normalizeTaxRate(request.taxRate());

                CostBreakdown costBreakdown = request.usesAdvancedMode()
                                ? calculateAdvancedVehicleCosts(request, effectiveMiles)
                                : new CostBreakdown(0.0, effectiveMiles * AppConstants.IRS_MILEAGE_RATE,
                                                "irs-standard");

                double totalDeduction = costBreakdown.gasCost() + costBreakdown.otherCost();
                double profitBeforeTax = totalEarnings - totalDeduction;
                double taxReserve = profitBeforeTax > 0 ? profitBeforeTax * normalizedTaxRate : 0.0;
                double takeHome = totalEarnings - totalDeduction - taxReserve;
                double realHourly = hours > 0 ? takeHome / hours : 0.0;
                double activeHourly = activeDriveTime > 0 ? takeHome / activeDriveTime : 0.0;
                double biggestLeakAmount = Math.max(totalDeduction, taxReserve);
                String biggestLeakLabel = totalDeduction >= taxReserve ? "Mileage + car cost" : "Tax reserve";

                return new GigCalculationResult(
                                gross,
                                tips,
                                bonuses,
                                totalEarnings,
                                hours,
                                activeTime,
                                activeDriveTime,
                                waitTime,
                                baseMiles,
                                effectiveMiles,
                                costBreakdown.gasCost(),
                                costBreakdown.otherCost(),
                                totalDeduction,
                                profitBeforeTax,
                                taxReserve,
                                takeHome,
                                realHourly,
                                activeHourly,
                                biggestLeakLabel,
                                biggestLeakAmount,
                                request.calculationMode(),
                                costBreakdown.vehicleId());
        }

        private CostBreakdown calculateAdvancedVehicleCosts(GigCalculationRequest request, double effectiveMiles) {
                VehiclePreset preset = vehiclePresetCatalog.find(request.vehicleId());
                String vehicleId = preset.id() != null ? preset.id() : request.vehicleId();
                double gasCost = 0.0;

                if (preset.isElectric()) {
                        double kwhUsed = (effectiveMiles / 100.0)
                                        * positiveOrDefault(preset.kwhPer100MilesOr(DEFAULT_EV_KWH_PER_100_MILES),
                                                        DEFAULT_EV_KWH_PER_100_MILES);
                        gasCost = kwhUsed
                                        * positiveOrDefault(
                                                        preset.avgElectricityCostOr(DEFAULT_EV_ELECTRICITY_COST),
                                                        DEFAULT_EV_ELECTRICITY_COST);
                } else if (!preset.isBikeOrWalker()) {
                        double mpg = preset.isCustom()
                                        ? positiveOrDefault(request.customMpg(), DEFAULT_CUSTOM_MPG)
                                        : positiveOrDefault(preset.mpgOr(DEFAULT_CUSTOM_MPG), DEFAULT_CUSTOM_MPG);
                        double gasPrice = positiveOrDefault(request.gasPrice(),
                                        preset.avgGasPriceOr(DEFAULT_GAS_PRICE));
                        gasCost = mpg > 0 ? (effectiveMiles / mpg) * gasPrice : 0.0;
                }

                double maintenance = preset.isCustom()
                                ? positiveOrDefault(request.customMaintenance(), DEFAULT_CUSTOM_MAINTENANCE)
                                : positiveOrDefault(
                                                preset.maintenanceCostPerMileOr(DEFAULT_CUSTOM_MAINTENANCE),
                                                DEFAULT_CUSTOM_MAINTENANCE);
                double depreciation = preset.isCustom()
                                ? positiveOrDefault(request.customDepreciation(), DEFAULT_CUSTOM_DEPRECIATION)
                                : positiveOrDefault(
                                                preset.depreciationCostPerMileOr(DEFAULT_CUSTOM_DEPRECIATION),
                                                DEFAULT_CUSTOM_DEPRECIATION);

                double otherCost = effectiveMiles * (maintenance + depreciation);
                return new CostBreakdown(gasCost, otherCost, vehicleId);
        }

        private double sanitizeMoney(double value) {
                return value < 0 ? 0 : value;
        }

        private double positiveOrDefault(double value, double fallback) {
                return value > 0 ? value : fallback;
        }

        private double normalizeTaxRate(double rawTaxRate) {
                double effectiveRate = rawTaxRate > 1 ? rawTaxRate / 100.0 : rawTaxRate;
                if (effectiveRate <= 0 || effectiveRate > 1) {
                        return AppConstants.SELF_EMPLOYMENT_TAX_RATE;
                }
                return effectiveRate;
        }

        private record CostBreakdown(double gasCost, double otherCost, String vehicleId) {
        }
}
