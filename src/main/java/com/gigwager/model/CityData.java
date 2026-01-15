package com.gigwager.model;

import java.util.Arrays;
import java.util.Optional;

public enum CityData {
        // --- EXISTING CITIES (10) ---
        AUSTIN("austin", "Austin", "TX", 2.90, 7.25, MarketTier.MED, 0.85,
                        "Austin has strong nightlife delivery demand on the famous 6th Street entertainment district. "
                                        +
                                        "SXSW and ACL festivals create massive surge periods, but traffic congestion downtown can hurt MPG."),

        HOUSTON("houston", "Houston", "TX", 2.85, 7.25, MarketTier.MED, 0.90,
                        "Houston's sprawling layout means longer trips and better highway MPG, but summer heat forces A/C usage that drains fuel. "
                                        +
                                        "Medical Center and Energy Corridor provide consistent corporate delivery demand."),

        SAN_FRANCISCO("san-francisco", "San Francisco", "CA", 4.90, 18.07, MarketTier.HIGH, 0.65,
                        "SF presents extreme parking challenges and steep hills that kill fuel economy. " +
                                        "However, high minimum wage and dense demand make short trips profitable if you cherry-pick."),

        NEW_YORK("new-york", "New York", "NY", 3.75, 16.00, MarketTier.HIGH, 0.60,
                        "<b>The 'Expert' Reality:</b> NYC is the only market in the US that requires a TLC (Taxi & Limousine Commission) license to drive for Uber/Lyft. If you don't have TLC plates, you can't drive rideshare here. Providing 'DoorDash' delivery via e-bike is often more profitable ($25-30/hr) than driving a car due to the crushing congestion in Manhattan. <br><br>"
                                        +
                                        "<b>Strategy:</b> Avoid Midtown during rush hour at all costs. The real money is in the morning airport runs (JFK/LGA) from the outer boroughs, or working the late-night shifts in Williamsburg and Lower East Side where traffic flows but demand stays high. Watch out for 'Congestion Pricing' zones which will eat into your net profit."),

        LOS_ANGELES("los-angeles", "Los Angeles", "CA", 4.60, 16.78, MarketTier.HIGH, 0.70,
                        "<b>The 'Expert' Reality:</b> LA is not one city; it's 50 cities trenchoat-stacked together. The '405' is a parking lot from 3 PM to 7 PM. Experienced drivers know that 'Prop 22' guarantees you a minimum earning floor, but the real winners are those who master the 'LAXit' airport lot system or stick to the Westside (Santa Monica, WeHo) on Friday nights. <br><br>"
                                        +
                                        "<b>Strategy:</b> Don't chase surges across town; you'll spend 45 minutes driving 10 miles. Pick a 'zone' (e.g., The Valley OR The Westside) and stay there. Movie premieres in Hollywood often close streets—check Waze before you start."),

        DALLAS("dallas", "Dallas", "TX", 2.95, 7.25, MarketTier.MED, 0.88,
                        "Dallas offers a good balance: relatively cheap gas, sprawling suburbs with steady demand. " +
                                        "Uptown and Deep Ellum have strong weekend nightlife orders."),

        PHOENIX("phoenix", "Phoenix", "AZ", 3.40, 14.35, MarketTier.MED, 0.92,
                        "<b>The 'Expert' Reality:</b> Phoenix is the 'Valley of the Sun'—and the sprawl. You will drive more miles here than almost anywhere else. However, the grid system makes driving easy and efficient (high MPG). The biggest factor is the 'Snowbird' season (Oct-April) vs. the blistering Summer. Summer earnings can drop 30% as population leaves. <br><br>"
                                        +
                                        "<b>Strategy:</b> Park near Old Town Scottsdale on weekends for the highest tips in the state. During summer days, focus on 'Shop & Deliver' grocery orders; people don't want to leave their A/C houses, and they tip well for someone to brave the heat for them."),

        CHICAGO("chicago", "Chicago", "IL", 3.90, 15.80, MarketTier.MED, 0.75,
                        "<b>The 'Expert' Reality:</b> Chicago driving has two enemies: Winter and 'Lower Wacker Drive'. Your GPS <i>will</i> fail you on Lower Wacker; learn the layout manually or avoid it. Parking in 'The Loop' is virtually impossible for food delivery without risking a ticket. <br><br>"
                                        +
                                        "<b>Strategy:</b> The 'Gold Coast' and 'River North' offer consistent high-ticket orders, but the smart money often moves to neighborhoods like Wicker Park or Lincoln Park to find easier parking and shorter wait times. Buy good winter tires—they are an investment, not an expense."),

        MIAMI("miami", "Miami", "FL", 3.40, 12.00, MarketTier.MED, 0.80,
                        "Miami Beach offers high tips but brutal tourist traffic. " +
                                        "Year-round warm weather means no winter slowdown, and Latin food delivery is consistently high volume."),

        SEATTLE("seattle", "Seattle", "WA", 4.50, 19.97, MarketTier.HIGH, 0.78,
                        "Seattle rain makes driving slower and more dangerous, increasing wait times. " +
                                        "Tech workers tip well, and Capitol Hill has strong nightlife demand."),

        // --- NEW CITIES (40+) ---

        ATLANTA("atlanta", "Atlanta", "GA", 3.20, 7.25, MarketTier.MED, 0.70,
                        "<b>The 'Expert' Reality:</b> Atlanta traffic is legendary. The 'Connector' (I-75/85) is a trap. Experienced drivers know that Hartsfield-Jackson Airport is the busiest in the world, but the queue lot can hold 300+ cars. Don't sit there unless the queue is < 50. <br><br>"
                                        +
                                        "<b>Strategy:</b> 'Buckhead' is where the money is, but traffic is dense. The 'northern arc' (Sandy Springs to Alpharetta) offers wealthy suburbs with longer trips and better tips, often safer and less stressful than downtown runs."),

        BOSTON("boston", "Boston", "MA", 3.60, 15.00, MarketTier.HIGH, 0.65,
                        "Boston's confusing street layout and winter weather make navigation tough. Student population drives high delivery volume."),

        DENVER("denver", "Denver", "CO", 3.10, 18.29, MarketTier.MED, 0.85,
                        "Altitude and hills can affect MPG. Snow days offer massive surge pricing opportunities for brave drivers."),

        LAS_VEGAS("las-vegas", "Las Vegas", "NV", 4.10, 11.25, MarketTier.HIGH, 0.80,
                        "The Strip is high-traffic but high-tip. Late-night demand is 24/7 unlike any other city."),

        PHILADELPHIA("philadelphia", "Philadelphia", "PA", 3.50, 7.25, MarketTier.MED, 0.75,
                        "Philly parking is tight. Center City offers density, but watch out for Parking Authority enforcement."),

        SAN_DIEGO("san-diego", "San Diego", "CA", 4.80, 16.85, MarketTier.HIGH, 0.82,
                        "Perfect weather means year-round consistent driving. Gas is expensive, so efficiency is key."),

        SAN_JOSE("san-jose", "San Jose", "CA", 4.90, 17.55, MarketTier.HIGH, 0.75,
                        "Silicon Valley money means good tips, but traffic during commute hours is paralyzing."),

        SAN_ANTONIO("san-antonio", "San Antonio", "TX", 2.75, 7.25, MarketTier.MED, 0.90,
                        "Spread out with military bases. Low cost of living and cheap gas help margins."),

        JACKSONVILLE("jacksonville", "Jacksonville", "FL", 3.10, 12.00, MarketTier.LOW, 0.95,
                        "Huge geographic area. You will drive a lot of miles, but highway speeds help MPG."),

        FORT_WORTH("fort-worth", "Fort Worth", "TX", 2.90, 7.25, MarketTier.MED, 0.88,
                        "Stockyards and downtown offering good weekend surges. Together with Dallas, creates a massive metroplex."),

        COLUMBUS("columbus", "Columbus", "OH", 3.30, 10.45, MarketTier.MED, 0.85,
                        "OSU campus drives huge food delivery volume. Steady, reliable mid-west market."),

        INDIANAPOLIS("indianapolis", "Indianapolis", "IN", 3.25, 7.25, MarketTier.MED, 0.88,
                        "Easy grid layout makes navigation simple. Convention center events can create mini-surges."),

        CHARLOTTE("charlotte", "Charlotte", "NC", 3.30, 7.25, MarketTier.MED, 0.85,
                        "Banking hub means good corporate lunch orders. Traffic is getting worse but manageable."),

        WASHINGTON_DC("washington-dc", "Washington", "DC", 3.80, 17.00, MarketTier.HIGH, 0.60,
                        "High wages but nightmare traffic and strict parking enforcement. Best for bike/scooter in downtown."),

        NASHVILLE("nashville", "Nashville", "TN", 3.10, 7.25, MarketTier.MED, 0.80,
                        "Tourism on Broadway drives ride demand. Bachelorette parties equal big tips but rowdy passengers."),

        PORTLAND("portland", "Portland", "OR", 4.20, 15.45, MarketTier.MED, 0.80,
                        "Bike-friendly culture can make car delivery tricky. eco-conscious customers appreciate hybrids/EVs."),

        DETROIT("detroit", "Detroit", "MI", 3.40, 10.33, MarketTier.LOW, 0.85,
                        "Motor City has spread out suburbs. Insurance rates are high, eating into profits."),

        MEMPHIS("memphis", "Memphis", "TN", 3.00, 7.25, MarketTier.LOW, 0.88,
                        "Low cost of living and cheap gas. Delivery volume is steady in pop-dense areas."),

        LOUISVILLE("louisville", "Louisville", "KY", 3.20, 7.25, MarketTier.LOW, 0.88,
                        "Derby time is the Super Bowl for drivers. Rest of the year is steady."),

        BALTIMORE("baltimore", "Baltimore", "MD", 3.60, 15.00, MarketTier.MED, 0.75,
                        "Inner Harbor has consistent demand. Watch out for rougher neighborhoods at night."),

        MILWAUKEE("milwaukee", "Milwaukee", "WI", 3.30, 7.25, MarketTier.MED, 0.85,
                        "Summer festivals are great. Winter driving requires a reliable vehicle and snow tires."),

        ALBUQUERQUE("albuquerque", "Albuquerque", "NM", 3.20, 12.00, MarketTier.LOW, 0.90,
                        "High altitude. Breaking Bad tours actually drive some tourism!"),

        TUCSON("tucson", "Tucson", "AZ", 3.30, 14.35, MarketTier.LOW, 0.92,
                        "University of Arizona is the main demand driver. Snowbirds in winter increase restaurant volume."),

        FRESNO("fresno", "Fresno", "CA", 4.70, 16.00, MarketTier.MED, 0.85,
                        "Central Valley ag hub. Gas is expensive but not as bad as the coast."),

        SACRAMENTO("sacramento", "Sacramento", "CA", 4.60, 16.00, MarketTier.MED, 0.85,
                        "State capital with government workers ordering lunch. Hot summers."),

        KANSAS_CITY("kansas-city", "Kansas City", "MO", 3.10, 12.30, MarketTier.MED, 0.88,
                        "BBQ joint deliveries are huge. Spans two states (MO/KS) so watch your tax tracking."),

        ATLANTA_SUB("marietta", "Marietta", "GA", 3.15, 7.25, MarketTier.MED, 0.85,
                        "Suburban strategy works well here. Less traffic than downtown Atlanta."),

        OMAHA("omaha", "Omaha", "NE", 3.20, 12.00, MarketTier.LOW, 0.90,
                        "Warren Buffett's town. Steady, predictable, low stress driving."),

        RALEIGH("raleigh", "Raleigh", "NC", 3.25, 7.25, MarketTier.MED, 0.85,
                        "Research Triangle Park means smart, tech-savvy customers and good tips."),

        VIRGINIA_BEACH("virginia-beach", "Virginia Beach", "VA", 3.30, 12.00, MarketTier.MED, 0.85,
                        "Summer tourist season is the main event. Winter can be quiet."),

        MINNEAPOLIS("minneapolis", "Minneapolis", "MN", 3.40, 10.85, MarketTier.MED, 0.78,
                        "Skyway system in winter makes downtown delivery easier for walkers, harder for drivers parking."),

        TULSA("tulsa", "Tulsa", "OK", 2.90, 7.25, MarketTier.LOW, 0.92,
                        "Very cheap gas. One of the best profit margins if you have a fuel efficient car."),

        ARLINGTON("arlington", "Arlington", "TX", 2.95, 7.25, MarketTier.MED, 0.85,
                        "Home of the Cowboys. Game days are insane money makers."),

        NEW_ORLEANS("new-orleans", "New Orleans", "LA", 3.10, 7.25, MarketTier.MED, 0.70,
                        "Tourists everywhere. French Quarter driving is impossible, stick to outskirts or deliveries."),

        WICHITA("wichita", "Wichita", "KS", 3.00, 7.25, MarketTier.LOW, 0.92,
                        "Air Capital of the World. Steady blue-collar demand."),

        CLEVELAND("cleveland", "Cleveland", "OH", 3.35, 10.45, MarketTier.LOW, 0.85,
                        "Lake effect snow in winter is no joke. Only drive with 4WD/AWD in storms."),

        BAKERSFIELD("bakersfield", "Bakersfield", "CA", 4.80, 16.00, MarketTier.LOW, 0.88,
                        "Oil country. High gas prices but consistent work."),

        HONOLULU("honolulu", "Honolulu", "HI", 4.95, 14.00, MarketTier.HIGH, 0.70,
                        "Paradise tax means highest gas in the US. But tourists tip like crazy."),

        ORLANDO("orlando", "Orlando", "FL", 3.30, 12.00, MarketTier.MED, 0.75,
                        "Theme park capital. Driving families to Disney is guaranteed money, but traffic on I-4 is hell."),

        TAMPA("tampa", "Tampa", "FL", 3.35, 12.00, MarketTier.MED, 0.82,
                        "Growing fast. MacDill AFB and snowbirds keep it busy.");

        private final String slug;
        private final String cityName;
        private final String state;
        private final double gasPrice;
        private final double minWage;
        private final MarketTier marketTier;
        private final double trafficFactor; // 1.0 = normal, < 1.0 = congested
        private final String marketDescription;

        CityData(String slug, String cityName, String state, double gasPrice, double minWage,
                        MarketTier marketTier, double trafficFactor, String marketDescription) {
                this.slug = slug;
                this.cityName = cityName;
                this.state = state;
                this.gasPrice = gasPrice;
                this.minWage = minWage;
                this.marketTier = marketTier;
                this.trafficFactor = trafficFactor;
                this.marketDescription = marketDescription;
        }

        public String getSlug() {
                return slug;
        }

        public String getCityName() {
                return cityName;
        }

        public String getState() {
                return state;
        }

        public double getGasPrice() {
                return gasPrice;
        }

        public double getMinWage() {
                return minWage;
        }

        public MarketTier getMarketTier() {
                return marketTier;
        }

        public double getTrafficFactor() {
                return trafficFactor;
        }

        public String getMarketDescription() {
                return marketDescription;
        }

        public boolean isCheapGas() {
                return gasPrice < 3.20; // Below national average
        }

        public boolean isHighCost() {
                return marketTier == MarketTier.HIGH;
        }

        public boolean isHighTraffic() {
                return trafficFactor < 0.80;
        }

        public static Optional<CityData> fromSlug(String slug) {
                return Arrays.stream(values())
                                .filter(city -> city.slug.equalsIgnoreCase(slug))
                                .findFirst();
        }

        public enum MarketTier {
                LOW(150, 350, 700), // Part-time, Side-hustle, Full-time weekly gross
                MED(200, 500, 1000),
                HIGH(300, 700, 1400);

                private final int partTimeGross;
                private final int sideHustleGross;
                private final int fullTimeGross;

                MarketTier(int partTimeGross, int sideHustleGross, int fullTimeGross) {
                        this.partTimeGross = partTimeGross;
                        this.sideHustleGross = sideHustleGross;
                        this.fullTimeGross = fullTimeGross;
                }

                public int getPartTimeGross() {
                        return partTimeGross;
                }

                public int getSideHustleGross() {
                        return sideHustleGross;
                }

                public int getFullTimeGross() {
                        return fullTimeGross;
                }
        }
}
