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
                        "Use this when deciding whether the city is worth driving at a realistic side-hustle pace.");

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

        public static Optional<CityIntentPage> fromSlug(String slug) {
                for (CityIntentPage page : values()) {
                        if (page.slug.equalsIgnoreCase(slug)) {
                                return Optional.of(page);
                        }
                }
                return Optional.empty();
        }
}
