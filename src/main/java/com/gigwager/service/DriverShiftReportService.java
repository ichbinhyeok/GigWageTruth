package com.gigwager.service;

import com.gigwager.model.DriverShiftReport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverShiftReportService {

    private final List<DriverShiftReport> reports = List.of(
            new DriverShiftReport(
                    "doordash",
                    "benchmark",
                    "National DoorDash field test",
                    "US",
                    "Field test",
                    "mixed lunch/dinner",
                    "$86 gross over 6.5 hours and 90 miles",
                    86.0,
                    6.5,
                    6.5,
                    90,
                    null,
                    "This is the cleanest public reminder that gross payout and take-home pay are different numbers once mileage is counted.",
                    "NerdWallet DoorDash pay test",
                    "https://www.nerdwallet.com/finance/learn/how-much-does-doordash-pay"),
            new DriverShiftReport(
                    "doordash",
                    "atlanta",
                    "Georgia DoorDash sample",
                    "GA",
                    "Driver discussion",
                    "weekly mixed shifts",
                    "$560.31 gross, 74 deliveries, 19h24m active time, 23h39m dash time",
                    560.31,
                    19.4,
                    23.65,
                    null,
                    74,
                    "The active-time number looked much stronger than the all-in dash-time number, so both clocks need to be visible.",
                    "Georgia DoorDash active-time post",
                    "https://www.reddit.com/r/DoorDashDrivers/comments/1r0oqfo/560_week_in_ga_not_atlanta_74_del_19_active_hrs/"),
            new DriverShiftReport(
                    "doordash",
                    "denver",
                    "Denver suburb report",
                    "CO",
                    "Driver discussion",
                    "suburban dashing",
                    "$12-$15/hr around Centennial-style suburban work",
                    null,
                    null,
                    null,
                    null,
                    null,
                    "The useful lesson is not the exact number; it is that suburb mileage and wait time can pull a strong city model down fast.",
                    "Denver DoorDash driver discussion",
                    "https://www.reddit.com/r/doordash_drivers/comments/1j4uofw/is_anybody_dashing_around_denver_area_if_yes_how/"),
            new DriverShiftReport(
                    "doordash",
                    "phoenix",
                    "Phoenix daily-target report",
                    "AZ",
                    "Driver discussion",
                    "slow-market days",
                    "$100/day became hard when order volume slowed",
                    null,
                    null,
                    null,
                    null,
                    null,
                    "A $100 target page needs hours and miles, not motivational copy. Slow volume changes the target math.",
                    "Phoenix DoorDash slowdown discussion",
                    "https://www.reddit.com/r/doordash_drivers/comments/nb6oi7/doordash_been_slow_lately_its_been_hard_to_get/"),
            new DriverShiftReport(
                    "doordash",
                    "san-jose",
                    "San Jose active-time report",
                    "CA",
                    "Driver discussion",
                    "California active-time math",
                    "Prop 22 and active miles change the pay calculation",
                    null,
                    null,
                    null,
                    null,
                    null,
                    "California pages need active-time and waiting-time checks instead of a single gross screenshot.",
                    "San Jose Prop 22 driver discussion",
                    "https://www.reddit.com/r/doordash_drivers/comments/p5m8is/minimum_wage_ca/"),
            new DriverShiftReport(
                    "doordash",
                    "dallas",
                    "DFW dollar-per-mile report",
                    "TX",
                    "Driver discussion",
                    "spread-out metro orders",
                    "Drivers discuss dollar-per-mile floors before accepting DoorDash orders",
                    null,
                    null,
                    null,
                    null,
                    null,
                    "In DFW, home-to-home miles matter more than restaurant-to-dropoff miles because zone crossings can eat the shift.",
                    "DoorDash dollar-per-mile discussion",
                    "https://www.reddit.com/r/doordash_drivers/comments/1s39uhj/whats_your_minimum_mile_to_accept_doordash_orders/"),
            new DriverShiftReport(
                    "doordash",
                    "minneapolis",
                    "Minnesota suburb report",
                    "MN",
                    "Driver discussion",
                    "suburb comparison",
                    "Drivers compare Saint Paul, Woodbury, White Bear, and Eagan against downtown friction",
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Local zone choice can beat a citywide average when parking and downtown wait time are the real bottleneck.",
                    "Minnesota DoorDash market discussion",
                    "https://www.reddit.com/r/doordash_drivers/comments/14u1c5e/does_anyone_dash_in_minnesota_i_moved_from/"),
            new DriverShiftReport(
                    "uber",
                    "benchmark",
                    "National Uber field test",
                    "US",
                    "Field test",
                    "rideshare driving",
                    "10 active hours, 10 trips, and 305 miles",
                    null,
                    10.0,
                    null,
                    305,
                    10,
                    "Uber screenshots need a mileage check because a strong fare day can still put heavy depreciation on the car.",
                    "NerdWallet Uber pay test",
                    "https://www.nerdwallet.com/finance/learn/how-much-does-an-uber-driver-make"),
            new DriverShiftReport(
                    "uber",
                    "chicago",
                    "Chicago airport and bar-close report",
                    "IL",
                    "Driver discussion",
                    "airport and late-night windows",
                    "Drivers separate airport/business-trip windows from generic all-day driving",
                    null,
                    null,
                    null,
                    null,
                    null,
                    "The city average is less useful than knowing which windows avoid unpaid waiting and weak ride density.",
                    "AskChicago Uber/Lyft summer thread",
                    "https://www.reddit.com/r/AskChicago/comments/1rep6pm/do_chicago_lyftuber_drivers_make_good_money_in/"),
            new DriverShiftReport(
                    "uber",
                    "orlando",
                    "Orlando weekend tourism report",
                    "FL",
                    "Driver discussion",
                    "Friday-Sunday tourism",
                    "Strong weekend gross still needs a fuel-cost check",
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Tourism demand can create good windows, but fuel and I-4 time decide whether the net hourly number holds.",
                    "Orlando Uber weekend earnings discussion",
                    "https://www.reddit.com/r/uberdrivers/comments/somguu/how_much_are_uber_drivers_making_in_orlando_with/"));

    public List<DriverShiftReport> getReportsForApp(String app) {
        return reports.stream()
                .filter(report -> report.app().equals(app))
                .collect(Collectors.toList());
    }

    public List<DriverShiftReport> getReportsForCity(String app, String citySlug) {
        return reports.stream()
                .filter(report -> report.app().equals(app))
                .filter(report -> "benchmark".equals(report.citySlug()) || report.citySlug().equals(citySlug))
                .collect(Collectors.toList());
    }
}
