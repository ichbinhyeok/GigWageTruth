package com.gigwager.model;

public enum WorkLevel {
        PART_TIME("part-time", "Part-time", "PT", 0, 10, 100,
                        "Best for testing a market or earning weekend cash without depending on gig income."),

        SIDE_HUSTLE("side-hustle", "Side-Hustle", "SH", 1, 25, 250,
                        "The practical middle ground: meaningful extra income without building your whole budget around the app."),

        FULL_TIME("full-time", "Full-time", "FT", 2, 40, 400,
                        "If this is primary income, the math changes. Insurance, tax planning, downtime, and vehicle replacement matter.");

        private final String slug;
        private final String displayName;
        private final String emoji;
        private final int index;
        private final int hoursPerWeek;
        private final int milesPerWeek;
        private final String tagline;

        WorkLevel(String slug, String displayName, String emoji, int index, int hoursPerWeek, int milesPerWeek,
                        String tagline) {
                this.slug = slug;
                this.displayName = displayName;
                this.emoji = emoji;
                this.index = index;
                this.hoursPerWeek = hoursPerWeek;
                this.milesPerWeek = milesPerWeek;
                this.tagline = tagline;
        }

        public String getSlug() {
                return slug;
        }

        public String getDisplayName() {
                return displayName;
        }

        public String getEmoji() {
                return emoji;
        }

        public int getIndex() {
                return index;
        }

        public int getHoursPerWeek() {
                return hoursPerWeek;
        }

        public int getMilesPerWeek() {
                return milesPerWeek;
        }

        public String getTagline() {
                return tagline;
        }

        public static WorkLevel fromSlug(String slug) {
                for (WorkLevel level : values()) {
                        if (level.slug.equalsIgnoreCase(slug)) {
                                return level;
                        }
                }
                throw new IllegalArgumentException("Invalid work level: " + slug);
        }

        public String getWorkLevelMeaning(String appName, String cityName, CityLocalData localData) {
                return switch (this) {
                        case PART_TIME -> String.format(
                                        "<p class=\"text-slate-700 leading-relaxed mb-4\">"
                                                        + "<strong>%d hours per week</strong> means %s is still a test, not a household-income plan. "
                                                        + "The useful question is whether a few peak windows in %s beat the time and car cost."
                                                        + "</p>"
                                                        + "<p class=\"text-slate-700 leading-relaxed mb-4\">"
                                                        + "For this level, skip weak all-day averages. Try Friday or Saturday nights near %s, lunch windows near %s, and short shifts where parking and pickup time are predictable."
                                                        + "</p>"
                                                        + "<p class=\"text-slate-700 leading-relaxed\">"
                                                        + "<strong>Burnout risk:</strong> Low, as long as you keep it optional. Use part-time work to learn the market before adding more hours."
                                                        + "</p>",
                                        hoursPerWeek,
                                        appName,
                                        cityName,
                                        localData.nightlifeDistrict(),
                                        localData.shoppingDistrict());
                        case SIDE_HUSTLE -> String.format(
                                        "<p class=\"text-slate-700 leading-relaxed mb-4\">"
                                                        + "At <strong>%d hours per week</strong>, %s becomes a real side business. The estimate needs to survive gas, unpaid waiting, route miles, and self-employment tax."
                                                        + "</p>"
                                                        + "<p class=\"text-slate-700 leading-relaxed mb-4\">"
                                                        + "In %s, this is usually the best testing level because you can work multiple demand windows without turning every slow hour into sunk cost. Track gross pay, online time, active time, and odometer miles for two full weeks."
                                                        + "</p>"
                                                        + "<p class=\"text-slate-700 leading-relaxed\">"
                                                        + "<strong>Burnout risk:</strong> Moderate. If the work has no specific savings target, motivation usually fades faster than the spreadsheet suggests."
                                                        + "</p>",
                                        hoursPerWeek,
                                        appName,
                                        cityName);
                        case FULL_TIME -> String.format(
                                        "<p class=\"text-slate-700 leading-relaxed mb-4\">"
                                                        + "<strong>%d hours per week</strong> makes %s a primary-income experiment. That means the benchmark is no longer weekend cash; it is a W-2 job with benefits, paid time off, and steadier risk."
                                                        + "</p>"
                                                        + "<p class=\"text-slate-700 leading-relaxed mb-4\">"
                                                        + "Full-time drivers in %s need a replacement-car fund, health-insurance plan, tax reserves, and a rule for shutting down weak hours. A good gross week can still be a weak net week if traffic and dead miles climb."
                                                        + "</p>"
                                                        + "<p class=\"text-slate-700 leading-relaxed\">"
                                                        + "<strong>Burnout risk:</strong> High. Treat this as a business with an exit plan, not a permanent default."
                                                        + "</p>",
                                        hoursPerWeek,
                                        appName,
                                        cityName);
                };
        }

        public String getTaxStrategy(String appName, String cityName, CityLocalData localData) {
                return switch (this) {
                        case PART_TIME -> "<h3 class=\"text-xl font-bold text-slate-900 mb-3\">Tax strategy: keep it simple</h3>"
                                        + "<ul class=\"list-disc pl-5 space-y-2 text-slate-700\">"
                                        + "<li><strong>Report the income:</strong> Reporting thresholds can change, but taxable income still needs to be reported even if a form is not issued.</li>"
                                        + "<li><strong>Track business miles:</strong> For 2026, the IRS standard mileage rate is 72.5 cents per mile. We use it as an estimation proxy, not tax advice.</li>"
                                        + "<li><strong>Separate the money:</strong> Keep gig deposits separate so the work does not disappear into normal spending.</li>"
                                        + "<li><strong>Do not overbuild:</strong> At low hours, a clean mileage log and simple expense folder matter more than forming an entity.</li>"
                                        + "</ul>"
                                        + "<p class=\"text-xs text-slate-400 mt-3 italic\">Educational only, not tax advice. Consult a qualified tax professional. Last reviewed: 2026-02.</p>";
                        case SIDE_HUSTLE -> "<h3 class=\"text-xl font-bold text-slate-900 mb-3\">Tax strategy: get organized</h3>"
                                        + "<ul class=\"list-disc pl-5 space-y-2 text-slate-700\">"
                                        + "<li><strong>Plan for quarterly taxes:</strong> If your total tax bill is large enough, estimated payments may be required. Check current IRS 1040-ES guidance.</li>"
                                        + "<li><strong>Watch self-employment tax:</strong> Net profit can be subject to 15.3% self-employment tax before regular income tax.</li>"
                                        + "<li><strong>Save by default:</strong> Many drivers set aside 25-30% of net profit until they know their personal tax situation.</li>"
                                        + "<li><strong>Deduct carefully:</strong> Phone, tolls, parking, car washes, and delivery gear can matter when they are genuinely business use for "
                                        + appName + ".</li>"
                                        + "</ul>"
                                        + "<p class=\"text-xs text-slate-400 mt-3 italic\">Educational only, not tax advice. Consult a qualified tax professional. Last reviewed: 2026-02.</p>";
                        case FULL_TIME -> "<h3 class=\"text-xl font-bold text-slate-900 mb-3\">Tax strategy: treat it like a business</h3>"
                                        + "<ul class=\"list-disc pl-5 space-y-2 text-slate-700\">"
                                        + "<li><strong>Use a tax reserve account:</strong> Full-time income can create large federal, state, and self-employment tax bills.</li>"
                                        + "<li><strong>Compare mileage methods:</strong> At high mileage, standard mileage and actual expense tracking should both be reviewed with a professional.</li>"
                                        + "<li><strong>Check health insurance treatment:</strong> Marketplace premiums and self-employed health insurance rules can change the real net number in "
                                        + cityName + ".</li>"
                                        + "<li><strong>Consider retirement accounts:</strong> Solo 401(k) and SEP IRA options can matter if net profit is meaningful.</li>"
                                        + "</ul>"
                                        + "<p class=\"text-xs text-slate-400 mt-3 italic\">Educational only, not tax advice. Consult a qualified tax professional. Last reviewed: 2026-02.</p>";
                };
        }

        public String getDayInTheLife(String appName, String cityName, CityData city, CityLocalData localData) {
                return switch (this) {
                        case PART_TIME -> String.format(
                                        "<h3 class=\"text-xl font-bold text-slate-900 mb-3\">A realistic part-time shift in %s</h3>"
                                                        + "<div class=\"bg-slate-50 rounded-xl p-6 border border-slate-200\">"
                                                        + "<p class=\"text-slate-700 mb-4\"><strong>Scenario:</strong> Saturday night, 8 PM - 12 AM.</p>"
                                                        + "<ul class=\"space-y-3 text-slate-700\">"
                                                        + "<li><strong>7:45 PM:</strong> Start with gas at $%.2f/gal and a clear mileage baseline.</li>"
                                                        + "<li><strong>8:00 PM:</strong> Go online near %s instead of roaming across town.</li>"
                                                        + "<li><strong>8:00 - 10:00 PM:</strong> Take short trips or compact delivery routes; decline orders that push you away from the active zone.</li>"
                                                        + "<li><strong>10:15 PM - 12:00 AM:</strong> Work the strongest late window, then stop before low-value repositioning eats the shift.</li>"
                                                        + "</ul>"
                                                        + "<p class=\"text-sm text-slate-600 mt-4 italic\">The goal is not total volume. It is a clean, short shift where the odometer still makes sense.</p>"
                                                        + "</div>",
                                        cityName,
                                        city.getGasPrice(),
                                        localData.nightlifeDistrict());
                        case SIDE_HUSTLE -> String.format(
                                        "<h3 class=\"text-xl font-bold text-slate-900 mb-3\">A realistic side-hustle week in %s</h3>"
                                                        + "<div class=\"bg-slate-50 rounded-xl p-6 border border-slate-200\">"
                                                        + "<p class=\"text-slate-700 mb-4\"><strong>Schedule:</strong> three weeknight evenings plus one weekend block.</p>"
                                                        + "<ul class=\"space-y-3 text-slate-700\">"
                                                        + "<li><strong>Weeknights:</strong> Work dinner or commute windows for %s and avoid dead mid-afternoon time.</li>"
                                                        + "<li><strong>Weekend block:</strong> Use the longest shift when demand is strongest, not when you are simply available.</li>"
                                                        + "<li><strong>Weekly check:</strong> Compare gross pay, %d modeled miles, gas, and tax reserve before deciding whether the week was actually good.</li>"
                                                        + "</ul>"
                                                        + "<p class=\"text-sm text-slate-600 mt-4 italic\">This level only works when the schedule is repeatable without damaging your main job or recovery time.</p>"
                                                        + "</div>",
                                        cityName,
                                        appName,
                                        milesPerWeek);
                        case FULL_TIME -> String.format(
                                        "<h3 class=\"text-xl font-bold text-slate-900 mb-3\">A realistic full-time day in %s</h3>"
                                                        + "<div class=\"bg-slate-50 rounded-xl p-6 border border-slate-200\">"
                                                        + "<p class=\"text-slate-700 mb-4\"><strong>Routine:</strong> two or three peak windows, with weak hours intentionally skipped.</p>"
                                                        + "<ul class=\"space-y-3 text-slate-700\">"
                                                        + "<li><strong>Morning:</strong> Test commute or airport-adjacent demand around %s only if the return miles are controlled.</li>"
                                                        + "<li><strong>Lunch:</strong> Work dense restaurant clusters around %s when parking friction is manageable.</li>"
                                                        + "<li><strong>Evening:</strong> Treat dinner or bar-close demand as the main shift, then stop when the last trips would create dead miles.</li>"
                                                        + "</ul>"
                                                        + "<p class=\"text-sm text-red-600 font-semibold mt-4\">At full-time hours, health insurance, vehicle replacement, and unpaid waiting decide whether the work is sustainable.</p>"
                                                        + "</div>",
                                        cityName,
                                        localData.airport(),
                                        localData.shoppingDistrict());
                };
        }

        public String getBestPractices(String appName, String cityName, CityData city, CityLocalData localData) {
                StringBuilder practices = new StringBuilder();
                practices.append("<h3 class=\"text-xl font-bold text-slate-900 mb-3\">Field rules for ")
                                .append(displayName)
                                .append(" drivers</h3>");
                practices.append("<ul class=\"space-y-3 text-slate-700\">");

                switch (this) {
                        case PART_TIME -> {
                                practices.append("<li><strong>Use a strike zone:</strong> Pick 2-3 neighborhoods in ")
                                                .append(cityName)
                                                .append(" and learn them before adding more hours.</li>");
                                practices.append("<li><strong>Protect the shift:</strong> Decline trips that pull you far from ")
                                                .append(localData.nightlifeDistrict())
                                                .append(" or your chosen restaurant cluster.</li>");
                                practices.append("<li><strong>Track the odometer:</strong> A short shift can look good in-app and still fail after miles.</li>");
                        }
                        case SIDE_HUSTLE -> {
                                practices.append("<li><strong>Review every two weeks:</strong> Compare gross pay, online hours, active hours, and miles before increasing your schedule.</li>");
                                practices.append("<li><strong>Invest only where it pays:</strong> A phone mount, insulated bag, and mileage app are useful. A bigger car payment usually is not.</li>");
                                practices.append("<li><strong>Use the shutdown rule:</strong> If two hours are weak, stop or move to a proven zone instead of chasing sunk cost.</li>");
                                if (city.getGasPrice() > 4.0) {
                                        practices.append("<li><strong>Fuel discipline:</strong> In ")
                                                        .append(cityName)
                                                        .append(", even a 20 cents/gal gap matters across ")
                                                        .append(milesPerWeek)
                                                        .append(" weekly miles.</li>");
                                }
                        }
                        case FULL_TIME -> {
                                practices.append("<li><strong>Diversify platforms:</strong> Do not depend on one app for every hour of the week.</li>");
                                practices.append("<li><strong>Budget replacement cost:</strong> At ")
                                                .append(milesPerWeek)
                                                .append(" modeled miles per week, the car is a business asset wearing down.</li>");
                                practices.append("<li><strong>Keep an exit plan:</strong> Full-time gig work should fund a next step, not trap you in a thin-margin loop.</li>");
                                if (city.isHighCost()) {
                                        practices.append("<li><strong>High-cost reality:</strong> In ")
                                                        .append(cityName)
                                                        .append(", rent and insurance can erase a headline gross number fast.</li>");
                                }
                        }
                }

                practices.append("</ul>");
                return practices.toString();
        }
}
