package com.gigwager.model;

import java.util.Optional;

public enum CityIntentPage {
        AFTER_GAS("after-gas", "After Gas", "after fuel and vehicle costs",
                        "Use this when the gross payout looks fine but fuel and mileage may erase the shift."),
        PER_MILE("per-mile", "Pay Per Mile", "per mile",
                        "Use this when the real question is whether the offers clear a dollar-per-mile floor."),
        ACTIVE_TIME("active-time", "Active Time", "active time versus online or dash time",
                        "Use this when app-reported active time may hide unpaid waiting and repositioning time."),
        WORTH_IT("worth-it", "Worth It", "worth it after expenses",
                        "Use this when deciding whether the city is worth driving at a realistic side-hustle pace."),
        DAILY_100("100-a-day", "$100 a Day", "make $100 a day",
                        "Use this when the question is how many real hours and miles it takes to clear a $100 net day."),
        HOURLY_PAY("hourly-pay", "Hourly Pay", "hourly pay",
                        "Use this when the question is the city-specific hourly pay number after mileage, tax, and waiting time."),
        HOW_MUCH_CAN_YOU_MAKE("how-much-can-you-make", "How Much Can You Make", "how much can you make",
                        "Use this when comparing realistic weekly, daily, and hourly DoorDash earnings in one city."),
        BEST_AREAS("best-areas", "Best Areas", "best areas to work",
                        "Use this when choosing zones inside a city instead of trusting a citywide average."),
        APP_COMPARISON("uber-eats-vs-doordash", "Uber Eats vs DoorDash", "Uber Eats vs DoorDash pay",
                        "Use this when comparing delivery-app pay, mileage, active time, and availability in the same city."),
        MONTHLY_1000("1000-a-month", "$1,000 a Month", "make $1,000 a month",
                        "Use this when planning a monthly side-income target instead of judging one shift."),
        NIGHTS_WEEKENDS("nights-weekends", "Nights and Weekends", "nights and weekends",
                        "Use this when the plan is to drive around a 9-to-5 schedule instead of all-day availability.");

        private final String slug;
        private final String displayName;
        private final String searchPhrase;
        private final String userIntent;

        CityIntentPage(String slug, String displayName, String searchPhrase, String userIntent) {
                this.slug = slug;
                this.displayName = displayName;
                this.searchPhrase = searchPhrase;
                this.userIntent = userIntent;
        }

        public String getSlug() {
                return slug;
        }

        public String getDisplayName() {
                return displayName;
        }

        public String getSearchPhrase() {
                return searchPhrase;
        }

        public String getUserIntent() {
                return userIntent;
        }

        public boolean isSupportedForApp(String app) {
                if ("doordash".equalsIgnoreCase(app)) {
                        return true;
                }
                return switch (this) {
                        case HOURLY_PAY, HOW_MUCH_CAN_YOU_MAKE, BEST_AREAS, APP_COMPARISON -> false;
                        default -> true;
                };
        }

        public static Optional<CityIntentPage> fromSlug(String slug) {
                for (CityIntentPage page : values()) {
                        if (page.slug.equalsIgnoreCase(slug)) {
                                return Optional.of(page);
                        }
                }
                return Optional.empty();
        }
}
