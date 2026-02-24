package com.gigwager.model;

import java.util.HashMap;
import java.util.Map;

public enum WorkLevel {
        PART_TIME("part-time", "Part-time", "⏰", 0, 10, 100,
                        "Perfect for testing the waters or earning extra weekend cash without quitting your day job."),

        SIDE_HUSTLE("side-hustle", "Side-Hustle", "💼", 1, 25, 250,
                        "The sweet spot for most drivers: meaningful income without the full-time commitment."),

        FULL_TIME("full-time", "Full-time", "🚗", 2, 40, 400,
                        "Making this your primary income? The math changes—let's talk taxes, insurance, and sustainability.");

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

        /**
         * Generate unique "What This Means" section based on work level
         * Anti-Thin Content Strategy: 200+ words of unique insights
         */
        public String getWorkLevelMeaning(String appName, String cityName, CityLocalData localData) {
                switch (this) {
                        case PART_TIME:
                                return String.format(
                                                "<p class=\"text-slate-700 leading-relaxed mb-4\">" +
                                                                "<strong>%s hours per week</strong> means you're treating %s as a <strong>hobby income</strong> source, not a business. "
                                                                +
                                                                "For tax purposes, keep in mind: reporting thresholds change. Regardless of forms received, income is taxable and must be reported. "
                                                                +
                                                                "</p>" +
                                                                "<p class=\"text-slate-700 leading-relaxed mb-4\">" +
                                                                "The real strategy at this level is <strong>time optimization</strong>. You can't afford to waste Saturday mornings sitting in the "
                                                                + localData.airport() + " queue. "
                                                                +
                                                                "In %s, focus on the highest-demand windows: Friday/Saturday nights near "
                                                                + localData.nightlifeDistrict()
                                                                + " (8 PM - 2 AM) for rideshare, or lunch rushes (11 AM - 1 PM) for delivery. "
                                                                +
                                                                "Your goal is maximizing dollars-per-online-hour, not total volume."
                                                                +
                                                                "</p>" +
                                                                "<p class=\"text-slate-700 leading-relaxed\">" +
                                                                "<strong>Burnout Risk:</strong> Low. Since you're only working 10 hours/week, you can quit anytime without financial panic. "
                                                                +
                                                                "Use this level to experiment with different neighborhoods and time slots to find what works before scaling up."
                                                                +
                                                                "</p>",
                                                hoursPerWeek, appName, cityName);

                        case SIDE_HUSTLE:
                                return String.format(
                                                "<p class=\"text-slate-700 leading-relaxed mb-4\">" +
                                                                "At <strong>%s hours per week</strong>, you're in the 'side-hustle optimization zone.' This is where most drivers settle long-term "
                                                                +
                                                                "because it balances meaningful income (~$500-700/week gross in %s) with maintaining a primary job's stability and benefits."
                                                                +
                                                                "</p>" +
                                                                "<p class=\"text-slate-700 leading-relaxed mb-4\">" +
                                                                "<strong>Tax Complexity Increases:</strong> You may receive a 1099 form, and you need to start thinking about <strong>quarterly estimated tax payments</strong>. "
                                                                +
                                                                "If your day job withholds taxes and this is supplemental income, you might owe a surprise tax bill in April unless you adjust your W-4 withholding or make quarterly payments. "
                                                                +
                                                                "On the bright side, you can deduct mileage. For 2026, the IRS standard mileage rate is 72.5¢ per mile. We use this as an estimation proxy, not tax advice. This often wipes out most taxable profit at this level."
                                                                +
                                                                "</p>" +
                                                                "<p class=\"text-slate-700 leading-relaxed mb-4\">" +
                                                                "<strong>Vehicle Strategy:</strong> At 250 miles/week (13,000 miles/year), you're putting serious wear on your car. "
                                                                +
                                                                "If you're driving a 2015 sedan worth $8,000, you're depreciating it by roughly $2,000-3,000 per year just from %s mileage. "
                                                                +
                                                                "Consider whether upgrading to a high-MPG hybrid (like a Prius) makes financial sense—run the numbers on fuel savings vs. car payment."
                                                                +
                                                                "</p>" +
                                                                "<p class=\"text-slate-700 leading-relaxed\">" +
                                                                "<strong>Burnout Risk:</strong> Moderate. Working evenings/weekends consistently can erode work-life balance. "
                                                                +
                                                                "Many drivers at this level experience 'motivation crashes' after 6-8 months. Combat this by setting concrete savings goals (e.g., '$10K emergency fund in 12 months')."
                                                                +
                                                                "</p>",
                                                hoursPerWeek, cityName, appName);

                        case FULL_TIME:
                                return String.format(
                                                "<p class=\"text-slate-700 leading-relaxed mb-4\">" +
                                                                "<strong>%s hours per week</strong> means %s is now your <strong>primary livelihood</strong>. The entire financial equation changes. "
                                                                +
                                                                "You're no longer comparing this to 'extra beer money'—you're competing with the stability of a W-2 job that offers health insurance, "
                                                                +
                                                                "paid time off, and employer-matched retirement contributions. Let's talk about what most gig platforms won't tell you."
                                                                +
                                                                "</p>" +
                                                                "<p class=\"text-slate-700 leading-relaxed mb-4\">" +
                                                                "<strong>Health Insurance Reality:</strong> You'll likely need to purchase coverage through the ACA Marketplace (Healthcare.gov). "
                                                                +
                                                                "In %s, premiums can range from roughly $300-600/month for a mid-tier plan depending on age and household size (check Healthcare.gov for current rates). "
                                                                +
                                                                "<em>This is not included in the hourly wage estimates above.</em> Factor this into your 'real take-home' calculations."
                                                                +
                                                                "</p>" +
                                                                "<p class=\"text-slate-700 leading-relaxed mb-4\">" +
                                                                "<strong>Retirement Planning:</strong> As a 1099 contractor, there's no employer 401(k) match. However, you can open a <strong>Solo 401(k)</strong> "
                                                                +
                                                                "or SEP IRA with generous contribution limits (check IRS.gov for current year limits). The catch? You need to be disciplined enough to set money aside regularly. "
                                                                +
                                                                "Many full-time gig drivers skip this and may end up with minimal retirement savings."
                                                                +
                                                                "</p>" +
                                                                "<p class=\"text-slate-700 leading-relaxed mb-4\">" +
                                                                "<strong>Vehicle Depreciation Accelerates:</strong> At 400 miles/week (20,800 miles/year), you'll burn through a car in 3-4 years. "
                                                                +
                                                                "Budget $200-300/month for 'vehicle replacement fund' on top of maintenance. In %s, if you're stuck in traffic frequently, "
                                                                +
                                                                "brake pads and transmission wear will hit you harder than highway-focused markets."
                                                                +
                                                                "</p>" +
                                                                "<p class=\"text-slate-700 leading-relaxed\">" +
                                                                "<strong>Burnout Risk:</strong> <span class=\"text-red-600 font-bold\">High.</span> Sitting in a car 8 hours/day, 5 days/week triggers "
                                                                +
                                                                "physical issues (back pain, carpal tunnel) and mental fatigue. Most full-time drivers quit or scale back within 18 months. "
                                                                +
                                                                "If you're going to do this, treat it like a <em>business</em>: track every expense, optimize routes obsessively, and have a 12-month exit plan to upskill into something more sustainable."
                                                                +
                                                                "</p>",
                                                hoursPerWeek, appName, cityName, cityName);

                        default:
                                return "";
                }
        }

        /**
         * Generate unique "Tax & Money Strategy" section
         */
        public String getTaxStrategy(String appName, String cityName, CityLocalData localData) {
                switch (this) {
                        case PART_TIME:
                                return "<h3 class=\"text-xl font-bold text-slate-900 mb-3\">💰 Tax Strategy: Keep It Simple</h3>"
                                                +
                                                "<ul class=\"list-disc pl-5 space-y-2 text-slate-700\">" +
                                                "<li><strong>Don't Panic:</strong> Reporting thresholds change. Regardless of forms received, income is taxable and must be reported. Check current IRS 1099-K guidance.</li>"
                                                +
                                                "<li><strong>Mileage Tracking:</strong> Use a simple app like Stride or Everlance. For 2026, the IRS standard mileage rate is 72.5¢ per mile. We use this as an estimation proxy, not tax advice. Every mile driven for "
                                                + appName + " = 72.5¢ deduction.</li>" +
                                                "<li><strong>Quarterly Taxes May Not Apply:</strong> At lower income levels, the IRS penalty for skipping estimated payments is often minimal. However, check your total tax liability to be sure.</li>"
                                                +
                                                "<li><strong>Student Loan Borrowers:</strong> Gig income can affect your Income-Driven Repayment (IDR) plan. Report it honestly to avoid recertification issues.</li>"
                                                +
                                                "</ul>" +
                                                "<p class=\"text-xs text-slate-400 mt-3 italic\">Tax information is for educational purposes only — not tax advice. Consult a qualified CPA. Last reviewed: 2026-02.</p>";

                        case SIDE_HUSTLE:
                                return "<h3 class=\"text-xl font-bold text-slate-900 mb-3\">💰 Tax Strategy: Get Organized</h3>"
                                                +
                                                "<ul class=\"list-disc pl-5 space-y-2 text-slate-700\">" +
                                                "<li><strong>Quarterly Estimated Taxes:</strong> If you owe more than $1,000 in taxes, the IRS expects quarterly payments (April 15, June 15, Sept 15, Jan 15). "
                                                +
                                                "Use Form 1040-ES. Missing these triggers penalties.</li>" +
                                                "<li><strong>Self-Employment Tax Shock:</strong> You'll pay 15.3% SE tax on net profit (after mileage deduction). This catches people off guard.</li>"
                                                +
                                                "<li><strong>Should You Form an LLC?</strong> Probably not at this level. The cost ($50-500 depending on state) outweighs benefits unless you're earning $30K+/year from gig work.</li>"
                                                +
                                                "<li><strong>Deduction Goldmine:</strong> Phone bill (business use %), car washes, toll fees, parking, even that air freshener—if it's for "
                                                + appName + ", it's deductible.</li>" +
                                                "<li><strong>Audit Risk:</strong> Generally low for typical side-hustle earners, but always keep clean records and honest deductions.</li>"
                                                +
                                                "</ul>" +
                                                "<p class=\"text-xs text-slate-400 mt-3 italic\">Tax information is for educational purposes only — not tax advice. Consult a qualified CPA. Last reviewed: 2026-02.</p>";

                        case FULL_TIME:
                                return "<h3 class=\"text-xl font-bold text-slate-900 mb-3\">💰 Tax Strategy: Treat This Like a Business</h3>"
                                                +
                                                "<ul class=\"list-disc pl-5 space-y-2 text-slate-700\">" +
                                                "<li><strong>Quarterly Estimated Taxes:</strong> Depending on your earnings, you may owe several thousand dollars per quarter in taxes (federal + state). A common guideline: set aside 25-30% of net profit in a separate savings account to avoid surprises.</li>"
                                                +
                                                "<li><strong>Retirement Tax Strategy:</strong> Consider a Solo 401(k) or SEP IRA. You can contribute as both 'employee' and 'employer' (check IRS.gov for current limits). "
                                                +
                                                "This can significantly reduce taxable income. Consult a CPA for the optimal setup based on your earnings.</li>"
                                                +
                                                "<li><strong>Health Insurance Deduction:</strong> You can deduct 100% of Marketplace premiums as an 'above-the-line' deduction (not itemized). "
                                                +
                                                "This is huge in " + cityName + " where premiums are expensive.</li>" +
                                                "<li><strong>Actual Expense Method:</strong> At this mileage level (20K+ miles/year), consider tracking actual car expenses (gas, repairs, depreciation) "
                                                +
                                                "instead of the standard mileage rate. Run both calculations—sometimes actual saves you $2,000-3,000 more.</li>"
                                                +
                                                "<li><strong>S-Corp Election:</strong> If you're netting significant income, talk to a CPA about electing S-Corp status. This can allow you to split income into 'wages' and 'distributions' to potentially reduce SE tax. The optimal threshold varies by state.</li>"
                                                +
                                                "<li><strong>Audit Risk:</strong> Moderate. Full-time gig drivers may face higher scrutiny if they claim 100% business use of a personal vehicle or report consecutive years of losses.</li>"
                                                +
                                                "</ul>" +
                                                "<p class=\"text-xs text-slate-400 mt-3 italic\">Tax information is for educational purposes only — not tax advice. Consult a qualified CPA. Last reviewed: 2026-02.</p>";

                        default:
                                return "";
                }
        }

        /**
         * Generate unique "Day in the Life" section
         */
        public String getDayInTheLife(String appName, String cityName, CityData city, CityLocalData localData) {
                switch (this) {
                        case PART_TIME:
                                return String.format(
                                                "<h3 class=\"text-xl font-bold text-slate-900 mb-3\">📅 A Typical Part-Time Shift in %s</h3>"
                                                                +
                                                                "<div class=\"bg-slate-50 rounded-xl p-6 border border-slate-200\">"
                                                                +
                                                                "<p class=\"text-slate-700 mb-4\"><strong>Scenario:</strong> Saturday Night, 8 PM - 12 AM (4 hours)</p>"
                                                                +
                                                                "<ul class=\"space-y-3 text-slate-700\">" +
                                                                "<li><strong>7:45 PM:</strong> Fill up gas ($%s/gal in %s). Top off washer fluid. Grab water bottle and phone charger.</li>"
                                                                +
                                                                "<li><strong>8:00 PM:</strong> Go online near "
                                                                + localData.nightlifeDistrict()
                                                                + ". First ping comes in 3 minutes.</li>"
                                                                +
                                                                "<li><strong>8:00 - 10:00 PM:</strong> Complete 4-5 short trips. Mix of bar pickups and restaurant deliveries. Avg $12-15 per trip.</li>"
                                                                +
                                                                "<li><strong>10:00 PM:</strong> Quick bathroom break at a 24-hour diner (pro tip: always know your pit stop locations).</li>"
                                                                +
                                                                "<li><strong>10:15 PM - 12:00 AM:</strong> Late-night surge kicks in. Complete 3 more trips at 1.3x-1.5x multiplier.</li>"
                                                                +
                                                                "<li><strong>12:00 AM:</strong> Log off. Total: 7-8 trips, $80-100 gross, 40 miles driven.</li>"
                                                                +
                                                                "</ul>" +
                                                                "<p class=\"text-sm text-slate-600 mt-4 italic\">Why this works: You're hitting peak demand without the 'dead zone' hours. "
                                                                +
                                                                "4-hour shifts prevent burnout and keep you fresh.</p>"
                                                                +
                                                                "</div>",
                                                cityName, String.format("%.2f", city.getGasPrice()), cityName);

                        case SIDE_HUSTLE:
                                return String.format(
                                                "<h3 class=\"text-xl font-bold text-slate-900 mb-3\">📅 A Typical Side-Hustle Week in %s</h3>"
                                                                +
                                                                "<div class=\"bg-slate-50 rounded-xl p-6 border border-slate-200\">"
                                                                +
                                                                "<p class=\"text-slate-700 mb-4\"><strong>Schedule:</strong> 3 weeknight evenings + 1 weekend day = 25 hours/week</p>"
                                                                +
                                                                "<ul class=\"space-y-3 text-slate-700\">" +
                                                                "<li><strong>Monday, Wednesday, Friday (6 PM - 10 PM):</strong> Dinner rush for %s. Position near high-density restaurant zones. "
                                                                +
                                                                "Expect 5-6 deliveries per shift. Total: 12 hours/week.</li>"
                                                                +
                                                                "<li><strong>Saturday (11 AM - 9 PM, with 2-hour break):</strong> The 'grind day.' Lunch rush (11 AM - 2 PM), break, then dinner/nightlife (5 PM - 9 PM). "
                                                                +
                                                                "This single day often generates 30-40%% of your weekly income. Total: 8 hours.</li>"
                                                                +
                                                                "<li><strong>Sunday (Optional Flex Day):</strong> 3-5 hours during NFL games or brunch hours. Use this to hit weekly income goals if you fell short.</li>"
                                                                +
                                                                "</ul>" +
                                                                "<p class=\"text-slate-700 mt-4\"><strong>Weekly Reality Check:</strong></p>"
                                                                +
                                                                "<ul class=\"list-disc pl-5 space-y-1 text-slate-700\">"
                                                                +
                                                                "<li>Gross Income: $500-700/week</li>" +
                                                                "<li>Gas Cost (250 mi @ $%s/gal, 25 MPG): $%s</li>" +
                                                                "<li>Net After Mileage Deduction & Taxes: ~$350-450/week</li>"
                                                                +
                                                                "<li>Monthly Take-Home: <strong>$1,400-1,800</strong></li>"
                                                                +
                                                                "</ul>" +
                                                                "<p class=\"text-sm text-slate-600 mt-4 italic\">The mental cost: You're working 25 hours on top of your day job. "
                                                                +
                                                                "By month 6, you'll either have a specific savings goal keeping you motivated, or you'll scale back to part-time. Plan accordingly.</p>"
                                                                +
                                                                "</div>",
                                                cityName, appName, String.format("%.2f", city.getGasPrice()),
                                                String.format("%.0f", (250.0 / 25.0) * city.getGasPrice()));

                        case FULL_TIME:
                                return String.format(
                                                "<h3 class=\"text-xl font-bold text-slate-900 mb-3\">📅 A Typical Full-Time Day in %s</h3>"
                                                                +
                                                                "<div class=\"bg-slate-50 rounded-xl p-6 border border-slate-200\">"
                                                                +
                                                                "<p class=\"text-slate-700 mb-4\"><strong>Monday - Friday Routine:</strong></p>"
                                                                +
                                                                "<ul class=\"space-y-3 text-slate-700\">" +
                                                                "<li><strong>7:00 AM - 9:00 AM:</strong> Morning commute surge. Position near business districts or "
                                                                + localData.airport() + ". "
                                                                +
                                                                "%s rides to corporate offices. 4-5 trips, $50-70 gross.</li>"
                                                                +
                                                                "<li><strong>9:00 AM - 11:00 AM:</strong> Dead zone. Either go offline and rest, or switch to grocery delivery (Instacart) to stay productive.</li>"
                                                                +
                                                                "<li><strong>11:00 AM - 1:30 PM:</strong> Lunch rush. %s office workers ordering food. Park near "
                                                                + localData.shoppingDistrict()
                                                                + " or restaurant clusters. 6-7 deliveries, $60-80 gross.</li>"
                                                                +
                                                                "<li><strong>1:30 PM - 4:30 PM:</strong> Another dead zone. This is when you do vehicle maintenance, errands, or nap. Don't burn gas chasing $4 orders.</li>"
                                                                +
                                                                "<li><strong>5:00 PM - 8:00 PM:</strong> Evening commute + dinner rush. The second money window. 7-8 trips, $80-100 gross.</li>"
                                                                +
                                                                "<li><strong>Total Active Time:</strong> 8 hours logged, but only ~6 hours 'on trips.' You'll spend 2 hours waiting/driving to pickups.</li>"
                                                                +
                                                                "</ul>" +
                                                                "<p class=\"text-slate-700 mt-4\"><strong>Daily Reality:</strong></p>"
                                                                +
                                                                "<ul class=\"list-disc pl-5 space-y-1 text-slate-700\">"
                                                                +
                                                                "<li>Gross Income: $180-250/day</li>" +
                                                                "<li>Miles Driven: ~80 miles</li>" +
                                                                "<li>Gas Cost: $%s (assuming 25 MPG)</li>" +
                                                                "<li>Net After All Deductions: ~$100-130/day</li>" +
                                                                "<li>Monthly (22 working days): <strong>$2,200-2,900</strong></li>"
                                                                +
                                                                "</ul>" +
                                                                "<p class=\"text-sm text-red-600 font-semibold mt-4\">⚠️ Subtract health insurance ($400-600/mo), vehicle replacement fund ($200/mo), and you're netting ~$1,600-2,100/mo. "
                                                                +
                                                                "This is why most full-time drivers in %s eventually transition to traditional employment or use gig work as a bridge, not a destination.</p>"
                                                                +
                                                                "</div>",
                                                cityName,
                                                appName.equals("Uber") ? "Rideshare" : "Delivery",
                                                appName.equals("Uber") ? "DoorDash" : "Uber Eats",
                                                String.format("%.0f", (80.0 / 25.0) * city.getGasPrice()),
                                                cityName);

                        default:
                                return "";
                }
        }

        /**
         * Generate work-level specific "Best Practices" section
         */
        public String getBestPractices(String appName, String cityName, CityData city, CityLocalData localData) {
                StringBuilder practices = new StringBuilder();
                practices.append(
                                "<h3 class=\"text-xl font-bold text-slate-900 mb-3\">🎯 Pro Tips for " + displayName
                                                + " Drivers</h3>");
                practices.append("<ul class=\"space-y-3 text-slate-700\">");

                switch (this) {
                        case PART_TIME:
                                practices.append(
                                                "<li><strong>Cherry-Pick Strategically:</strong> At 10 hours/week, you can afford to decline low-value orders. Be aware that acceptance rate thresholds vary by platform and region — check your app's current driver requirements.</li>");
                                practices.append(
                                                "<li><strong>Know Your 'Strike Zone':</strong> Identify the 2-3 neighborhoods in ")
                                                .append(cityName)
                                                .append(" where you consistently get high-value pings, and stick to them.</li>");
                                practices.append(
                                                "<li><strong>Avoid the " + localData.airport()
                                                                + " Queue:</strong> Unless you live 5 minutes from the airport, sitting in a 45-minute queue for a $20 ride is a terrible use of your limited time.</li>");
                                if (city.isHighTraffic()) {
                                        practices.append("<li><strong>Traffic = Your Enemy:</strong> ").append(cityName)
                                                        .append(
                                                                        " congestion will murder your hourly rate at this level. Work nights/weekends when roads are clear.</li>");
                                }
                                practices.append(
                                                "<li><strong>Cash Out Weekly:</strong> Most platforms let you instant-transfer earnings for $0.50-1.50 fee. Do it weekly to keep gig money separate from your 'real' budget.</li>");
                                break;

                        case SIDE_HUSTLE:
                                practices.append(
                                                "<li><strong>Master the 'Stacking' Game:</strong> Accept 2-3 orders from the same restaurant cluster to maximize efficiency. In ")
                                                .append(cityName)
                                                .append(", this can boost your hourly rate by 30%.</li>");
                                practices.append(
                                                "<li><strong>Invest in Comfort:</strong> At 25 hrs/week, you need better equipment. Buy a good phone mount, seat cushion, and insulated delivery bag. These aren't luxuries—they're tools.</li>");
                                practices.append(
                                                "<li><strong>Track EVERYTHING:</strong> Use apps like Hurdlr or Stride to auto-track mileage. At ~13,000 miles/year, your mileage deduction could be substantial (multiply miles × current IRS rate). Don't leave this money on the table.</li>");
                                if (city.getGasPrice() > 4.0) {
                                        practices.append("<li><strong>Gas Strategy in High-Cost ").append(cityName)
                                                        .append(
                                                                        ":</strong> Use GasBuddy to find cheap stations. A 20¢/gal difference = $260/year savings at your mileage.</li>");
                                }
                                practices.append(
                                                "<li><strong>The 'Two-Week Test':</strong> Every 6 months, take two full weeks off. If you feel massive relief, you're approaching burnout—scale back before you hate it.</li>");
                                practices.append(
                                                "<li><strong>Develop a 'Bad Night' Protocol:</strong> If you're 2 hours in and only made $20, go home. Don't chase sunk costs—tomorrow will be better.</li>");
                                break;

                        case FULL_TIME:
                                practices.append("<li><strong>Diversify Platforms:</strong> Never rely on just ")
                                                .append(appName)
                                                .append(". Run Uber + DoorDash + Instacart simultaneously. When one is slow, switch.</li>");
                                practices.append(
                                                "<li><strong>Health is Wealth:</strong> Buy a lumbar support pillow. Do stretches every 2 hours. Sit-related injuries (sciatica, carpal tunnel) can end your career at this level.</li>");
                                practices.append(
                                                "<li><strong>Build a 3-Month Emergency Fund:</strong> Gig income is volatile. A platform deactivation or car breakdown can kill your income overnight. $6,000-10,000 in savings is non-negotiable.</li>");
                                if (city.isHighCost()) {
                                        practices.append("<li><strong>High-Cost ").append(cityName).append(
                                                        " Reality:</strong> Your net income won't cover a $2,000/mo apartment. Most full-time drivers here live with roommates or 1 hour outside the city.</li>");
                                }
                                practices.append(
                                                "<li><strong>The 12-Month Rule:</strong> Treat full-time gig work as a 12-month sprint, not a marathon. Use this year to save aggressively while learning a new skill (coding, CDL license, real estate) for your next career move.</li>");
                                practices.append(
                                                "<li><strong>Vehicle Purchase Strategy:</strong> Don't buy new. Buy a 3-5 year old Toyota Prius or Honda Civic with 50K miles for $12K-15K. You'll recoup the investment in fuel savings within 18 months.</li>");
                                practices.append(
                                                "<li><strong>Peak Hours Are Non-Negotiable:</strong> Work 7-9 AM and 5-8 PM daily. These 5 hours generate 60% of your income. The rest of the day is optional based on your monthly targets.</li>");
                                break;
                }

                practices.append("</ul>");
                return practices.toString();
        }
}
