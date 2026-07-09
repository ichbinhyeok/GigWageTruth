package com.gigwager.model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum CalculatorIntentPage {
    DOORDASH_EARNINGS(
            "doordash",
            "earnings-calculator",
            "DoorDash Earnings Calculator 2026: Net Pay After Gas",
            "Calculate DoorDash earnings after gas, mileage, waiting time, and self-employment tax before you chase a weekly or daily target.",
            "DoorDash Earnings Calculator",
            "Net pay after gas, miles, and taxes",
            "Use this page when the query is not just 'what does DoorDash pay' but 'what do I keep after the car cost is counted.'",
            "/doordash?gross=650&miles=320&hours=25&source=doordash_earnings_calculator",
            "Run DoorDash earnings",
            "$650 weekly gross",
            "320 weekly miles",
            "25 online hours",
            List.of(
                    "Start with gross pay before tips are separated out.",
                    "Enter all business miles, including dead miles back to a hot zone.",
                    "Use online or dash time for hourly reality, not only active delivery time.",
                    "Compare the result with the $100/day and after-gas pages before adding more shifts."),
            List.of(
                    new Metric("Net hourly", "Gross minus mileage proxy and self-employment tax, divided by all hours."),
                    new Metric("Cost pressure", "Miles multiplied by the IRS mileage proxy so high-mile shifts are not overrated."),
                    new Metric("Target stress", "How many hours and miles the same baseline needs for $100/day.")),
            List.of(
                    new RelatedLink("DoorDash gas calculator", "/doordash/gas-calculator",
                            "Check whether fuel and full mileage pressure break the shift margin."),
                    new RelatedLink("DoorDash $100/day math", "/doordash/can-you-make-100-a-day",
                            "Use the calculator result to test whether a daily net target is realistic."),
                    new RelatedLink("DoorDash after gas", "/doordash/after-gas",
                            "Separate gross pay from what remains after fuel and vehicle cost."),
                    new RelatedLink("DoorDash pay per mile", "/doordash/pay-per-mile",
                            "Use an order floor before accepting long-distance offers.")),
            List.of(
                    new Faq("How do I calculate DoorDash earnings after expenses?",
                            "Enter gross pay, total miles, all hours, gas price, and vehicle assumptions. The calculator subtracts a mileage-cost proxy and self-employment tax so the output is closer to take-home pay than app gross."),
                    new Faq("Should I use active time or dash time?",
                            "Use dash time or all online time when judging hourly pay. Active time is useful for platform checks, but unpaid waiting and repositioning decide whether the shift was worth it."),
                    new Faq("Why does the calculator use miles?",
                            "Miles are the main hidden cost. Two DoorDash shifts with the same gross pay can have very different net pay if one burns twice the miles."))),

    DOORDASH_GAS(
            "doordash",
            "gas-calculator",
            "DoorDash Gas Calculator 2026: Cost Per Shift + Net Pay",
            "Estimate DoorDash gas cost, mileage pressure, and take-home pay before a dinner rush, weekend shift, or full-day dash.",
            "DoorDash Gas Calculator",
            "Fuel cost plus full mileage pressure",
            "Use this page when the query is about whether gas destroys the shift margin, not just the posted payout.",
            "/doordash?gross=120&miles=72&hours=6&gasPrice=3.55&source=doordash_gas_calculator",
            "Run gas-cost scenario",
            "$120 shift gross",
            "72 shift miles",
            "6 dash hours",
            List.of(
                    "Enter total shift miles, not only delivery miles.",
                    "Use your local gas price when it is above the national default.",
                    "Check cost per mile before accepting long suburban orders.",
                    "Compare the result against after-gas and pay-per-mile pages."),
            List.of(
                    new Metric("Gas burn", "Estimated fuel spend based on miles and MPG."),
                    new Metric("Vehicle cost", "Mileage proxy for fuel, maintenance, tire wear, and depreciation."),
                    new Metric("Net shift", "What remains after the mileage proxy and tax reserve are counted.")),
            List.of(
                    new RelatedLink("DoorDash after gas", "/doordash/after-gas",
                            "See what drivers actually keep after fuel and vehicle cost."),
                    new RelatedLink("Cost per mile tool", "/vehicle-cost/cost-per-mile",
                            "Tune the vehicle-cost assumption if your MPG or maintenance cost differs."),
                    new RelatedLink("DoorDash pay per mile", "/doordash/pay-per-mile",
                            "Set a minimum order floor before the next shift.")),
            List.of(
                    new Faq("How much should I subtract for gas on DoorDash?",
                            "Use total miles, MPG, and gas price for fuel. Then compare that with the broader mileage-cost proxy because fuel is only one part of the real vehicle cost."),
                    new Faq("Does DoorDash pay for gas?",
                            "DoorDash pay does not automatically reimburse every driver for gas. The calculator treats gas and mileage as costs you need to subtract from gross pay."),
                    new Faq("What is a good DoorDash gas calculator input?",
                            "A useful shift input includes gross pay, all dash miles, online hours, local gas price, and whether the route requires dead miles back home."))),

    DOORDASH_MILEAGE_DEDUCTION(
            "doordash",
            "mileage-deduction-calculator",
            "DoorDash Mileage Deduction Calculator 2026: IRS Mile Math",
            "Estimate the DoorDash mileage deduction, tax reserve, and net earnings effect from business miles without treating the deduction as cash profit.",
            "DoorDash Mileage Deduction Calculator",
            "IRS mile math without fake profit",
            "Use this page when the query is tax-adjacent: the mileage deduction can reduce taxable income, but it is not the same as cash in hand.",
            "/doordash?gross=900&miles=480&hours=32&source=doordash_mileage_deduction_calculator",
            "Estimate mileage impact",
            "$900 weekly gross",
            "480 business miles",
            "32 dash hours",
            List.of(
                    "Track business miles from app-on to final repositioning.",
                    "Keep tax reserve separate from fuel spend.",
                    "Do not count the deduction as extra revenue.",
                    "Use the quarterly estimator after the mileage check."),
            List.of(
                    new Metric("Mileage deduction", "Business miles multiplied by the IRS standard mileage rate proxy used on-site."),
                    new Metric("Taxable-profit check", "Gross pay minus mileage proxy before self-employment tax assumptions."),
                    new Metric("Quarterly reserve", "Estimated tax pressure routed into the quarterly estimator.")),
            List.of(
                    new RelatedLink("Quarterly tax estimator", "/taxes/quarterly-estimator",
                            "Turn the mileage output into a tax-reserve estimate."),
                    new RelatedLink("Gig worker tax guide", "/blog/tax-guide",
                            "Review standard mileage versus actual expenses before filing."),
                    new RelatedLink("DoorDash earnings calculator", "/doordash/earnings-calculator",
                            "Run the same miles through take-home pay math.")),
            List.of(
                    new Faq("Can DoorDash drivers deduct mileage?",
                            "DoorDash drivers generally track business miles and may use mileage deductions when eligible. The page is informational, not tax advice."),
                    new Faq("Is the mileage deduction the same as gas reimbursement?",
                            "No. A deduction can reduce taxable income; it is not a cash reimbursement from DoorDash."),
                    new Faq("What miles should I enter?",
                            "Enter business miles tied to dashing, including repositioning that is part of the work pattern. Do not include personal miles."))),

    UBER_PAY(
            "uber",
            "pay-calculator",
            "Uber Pay Calculator 2026: Net Driver Pay After Expenses",
            "Calculate Uber driver pay after mileage, gas, dead time, and tax reserve instead of trusting app gross or per-trip screenshots.",
            "Uber Pay Calculator",
            "Net driver pay after expenses",
            "Use this page when the query asks what Uber pays and the useful answer is net hourly after the car cost.",
            "/uber?gross=950&miles=420&hours=34&source=uber_pay_calculator",
            "Run Uber pay",
            "$950 weekly gross",
            "420 weekly miles",
            "34 online hours",
            List.of(
                    "Use online hours, not only active-trip time.",
                    "Include airport queue, pickup, and repositioning miles.",
                    "Check net hourly before judging a weekly gross screenshot.",
                    "Compare city pages when your market is unusually expensive or spread out."),
            List.of(
                    new Metric("Net hourly", "Estimated pay after mileage proxy and self-employment tax."),
                    new Metric("Dead-mile drag", "The cost of pickup and repositioning miles that do not show up in gross pay."),
                    new Metric("City variance", "Links into city earnings pages when local gas, traffic, and demand change the result.")),
            List.of(
                    new RelatedLink("Uber after expenses", "/uber-after-expenses",
                            "See why gross pay and real hourly pay diverge."),
                    new RelatedLink("Uber hourly earnings report", "/reports/uber-driver-hourly-earnings-2026",
                            "Compare city-level Uber net pay snapshots."),
                    new RelatedLink("Uber city earnings hub", "/salary/uber",
                            "Move from calculator math to local market pages.")),
            List.of(
                    new Faq("How do I calculate Uber pay after expenses?",
                            "Enter weekly gross, all online hours, business miles, gas price, and vehicle assumptions. The calculator subtracts mileage and tax pressure before showing net hourly."),
                    new Faq("Why is Uber gross pay misleading?",
                            "Gross pay excludes fuel, depreciation, maintenance, unpaid waiting, pickup distance, and tax reserve. Those costs decide whether the work was worth the time."),
                    new Faq("Should Uber drivers calculate by trip or by week?",
                            "Trip math helps reject bad rides, but weekly math exposes whether the full routine produces enough net hourly pay."))),

    UBER_INCOME(
            "uber",
            "income-calculator",
            "Uber Income Calculator 2026: Weekly Net Pay Estimate",
            "Estimate weekly Uber income after mileage, tax reserve, and waiting time so a full-time or side-hustle plan does not rely on gross revenue.",
            "Uber Income Calculator",
            "Weekly income after car cost",
            "Use this page when the query asks income, salary, or weekly take-home rather than a single trip payout.",
            "/uber?gross=1250&miles=560&hours=45&source=uber_income_calculator",
            "Estimate Uber income",
            "$1,250 weekly gross",
            "560 weekly miles",
            "45 online hours",
            List.of(
                    "Model the whole week, not the best day.",
                    "Include unpaid waiting and repositioning time.",
                    "Use the city hub when local traffic or airport strategy changes the weekly pattern.",
                    "Check quarterly tax reserve before treating net pay as spendable income."),
            List.of(
                    new Metric("Weekly net", "Estimated weekly take-home after mileage proxy and tax reserve."),
                    new Metric("Hourly floor", "The minimum net hourly result needed before adding more hours."),
                    new Metric("Mileage load", "How much of the weekly income depends on heavy vehicle use.")),
            List.of(
                    new RelatedLink("Quarterly tax estimator", "/taxes/quarterly-estimator",
                            "Estimate tax reserve from the same weekly income inputs."),
                    new RelatedLink("Uber city hub", "/salary/uber",
                            "Compare the weekly model against local city baselines."),
                    new RelatedLink("Best cities for Uber", "/best-cities/uber",
                            "See which markets look stronger after expenses.")),
            List.of(
                    new Faq("What is a realistic Uber income calculator input?",
                            "Use your real weekly gross, online hours, miles, tips, bonuses, gas price, and vehicle cost. A best-day screenshot is not enough."),
                    new Faq("Can Uber income be estimated monthly?",
                            "Yes, but weekly math is cleaner because miles, demand, and schedule windows change fast. Multiply a stable weekly net only after checking consistency."),
                    new Faq("Why does the calculator focus on net income?",
                            "Drivers spend money to create gross income. Net income is the number left after the car and tax pressure are counted."))),

    UBER_TLC_PAY(
            "uber",
            "tlc-pay-calculator",
            "TLC Pay Calculator for Uber Drivers 2026: NYC Net Pay",
            "Estimate TLC-style Uber pay in New York with gross, miles, hours, vehicle cost, and tax reserve separated before you judge a full-time plan.",
            "TLC Pay Calculator for Uber Drivers",
            "NYC gross is not NYC take-home",
            "Use this page for TLC and NYC driver-pay calculator searches where vehicle cost, waiting time, and full-time hours change the answer.",
            "/uber?gross=1450&miles=520&hours=48&gasPrice=3.80&source=uber_tlc_pay_calculator",
            "Run TLC pay scenario",
            "$1,450 weekly gross",
            "520 weekly miles",
            "48 online hours",
            List.of(
                    "Model full online hours, not only passenger time.",
                    "Include pickup, repositioning, and return-home mileage.",
                    "Treat vehicle payment, insurance, and TLC-specific overhead as separate checks.",
                    "Open the New York city page after the calculator result."),
            List.of(
                    new Metric("NYC net hourly", "Gross minus mileage proxy and tax pressure divided by online hours."),
                    new Metric("Full-time risk", "High gross can hide weak net pay when hours and miles are heavy."),
                    new Metric("Local follow-up", "Routes into the New York Uber page for market-specific context.")),
            List.of(
                    new RelatedLink("Uber New York earnings", "/salary/uber/new-york",
                            "Compare the TLC scenario with the NYC city model."),
                    new RelatedLink("Uber income calculator", "/uber/income-calculator",
                            "Switch from TLC-specific math to weekly net-income planning."),
                    new RelatedLink("Vehicle cost per mile", "/vehicle-cost/cost-per-mile",
                            "Tune the car-cost assumption if the TLC vehicle setup is expensive.")),
            List.of(
                    new Faq("What does a TLC pay calculator need to include?",
                            "It needs gross pay, online hours, all business miles, fuel, vehicle cost, and tax reserve. Gross alone is not enough for NYC driver pay."),
                    new Faq("Is TLC gross pay the same as take-home pay?",
                            "No. Vehicle cost, insurance, financing, mileage, toll-related friction, and taxes can pull take-home pay far below gross."),
                    new Faq("Why link this to the Uber New York page?",
                            "The calculator handles the math, while the city page keeps the NYC-specific earnings context and related local assumptions.")));

    private final String app;
    private final String slug;
    private final String title;
    private final String description;
    private final String headline;
    private final String subhead;
    private final String directAnswer;
    private final String calculatorUrl;
    private final String ctaLabel;
    private final String benchmarkGross;
    private final String benchmarkMiles;
    private final String benchmarkHours;
    private final List<String> inputChecks;
    private final List<Metric> metrics;
    private final List<RelatedLink> relatedLinks;
    private final List<Faq> faqs;

    CalculatorIntentPage(
            String app,
            String slug,
            String title,
            String description,
            String headline,
            String subhead,
            String directAnswer,
            String calculatorUrl,
            String ctaLabel,
            String benchmarkGross,
            String benchmarkMiles,
            String benchmarkHours,
            List<String> inputChecks,
            List<Metric> metrics,
            List<RelatedLink> relatedLinks,
            List<Faq> faqs) {
        this.app = app;
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.headline = headline;
        this.subhead = subhead;
        this.directAnswer = directAnswer;
        this.calculatorUrl = calculatorUrl;
        this.ctaLabel = ctaLabel;
        this.benchmarkGross = benchmarkGross;
        this.benchmarkMiles = benchmarkMiles;
        this.benchmarkHours = benchmarkHours;
        this.inputChecks = inputChecks;
        this.metrics = metrics;
        this.relatedLinks = relatedLinks;
        this.faqs = faqs;
    }

    public static Optional<CalculatorIntentPage> from(String app, String slug) {
        return Arrays.stream(values())
                .filter(page -> page.app.equals(app))
                .filter(page -> page.slug.equals(slug))
                .findFirst();
    }

    public static List<CalculatorIntentPage> all() {
        return List.of(values());
    }

    public String app() {
        return app;
    }

    public String appName() {
        return "doordash".equals(app) ? "DoorDash" : "Uber";
    }

    public String slug() {
        return slug;
    }

    public String path() {
        return "/" + app + "/" + slug;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public String headline() {
        return headline;
    }

    public String subhead() {
        return subhead;
    }

    public String directAnswer() {
        return directAnswer;
    }

    public String calculatorUrl() {
        return calculatorUrl;
    }

    public String ctaLabel() {
        return ctaLabel;
    }

    public String benchmarkGross() {
        return benchmarkGross;
    }

    public String benchmarkMiles() {
        return benchmarkMiles;
    }

    public String benchmarkHours() {
        return benchmarkHours;
    }

    public List<String> inputChecks() {
        return inputChecks;
    }

    public List<Metric> metrics() {
        return metrics;
    }

    public List<RelatedLink> relatedLinks() {
        return relatedLinks;
    }

    public List<Faq> faqs() {
        return faqs;
    }

    public String appHubPath() {
        return "/salary/" + app;
    }

    public String appCalculatorPath() {
        return "/" + app;
    }

    public record Metric(String label, String detail) {
    }

    public record RelatedLink(String label, String path, String description) {
    }

    public record Faq(String question, String answer) {
    }
}
