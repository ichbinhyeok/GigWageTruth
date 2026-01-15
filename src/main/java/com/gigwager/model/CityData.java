package com.gigwager.model;

import java.util.Arrays;
import java.util.Optional;

public enum CityData {
    // --- EXISTING CITIES (10) ---
    AUSTIN("austin", "Austin", "TX", 2.90, 7.25, MarketTier.MED, 0.85,
            "Austin has strong nightlife delivery demand on the famous 6th Street entertainment district. " +
                    "SXSW and ACL festivals create massive surge periods, but traffic congestion downtown can hurt MPG."),

    HOUSTON("houston", "Houston", "TX", 2.85, 7.25, MarketTier.MED, 0.90,
            "Houston's sprawling layout means longer trips and better highway MPG, but summer heat forces A/C usage that drains fuel. "
                    +
                    "Medical Center and Energy Corridor provide consistent corporate delivery demand."),

    SAN_FRANCISCO("san-francisco", "San Francisco", "CA", 4.90, 18.07, MarketTier.HIGH, 0.65,
            "SF presents extreme parking challenges and steep hills that kill fuel economy. " +
                    "However, high minimum wage and dense demand make short trips profitable if you cherry-pick."),

    NEW_YORK("new-york", "New York", "NY", 3.75, 16.00, MarketTier.HIGH, 0.60,
            "NYC traffic is brutal. Expect frequent dead time in gridlock and expensive tolls. " +
                    "Manhattan delivery density is unmatched, but parking tickets can destroy your profit margin."),

    LOS_ANGELES("los-angeles", "Los Angeles", "CA", 4.60, 16.78, MarketTier.HIGH, 0.70,
            "LA requires highway driving between zones, burning more gas but offering consistent earnings. " +
                    "Beach cities tip better, but you'll compete with aggressive multi-appers."),

    DALLAS("dallas", "Dallas", "TX", 2.95, 7.25, MarketTier.MED, 0.88,
            "Dallas offers a good balance: relatively cheap gas, sprawling suburbs with steady demand. " +
                    "Uptown and Deep Ellum have strong weekend nightlife orders."),

    PHOENIX("phoenix", "Phoenix", "AZ", 3.40, 14.35, MarketTier.MED, 0.92,
            "Phoenix heat in summer is extreme (110°F+), forcing constant A/C and reducing fuel efficiency by 15%. " +
                    "Scottsdale tips well, but long distances between deliveries are common."),

    CHICAGO("chicago", "Chicago", "IL", 3.90, 15.80, MarketTier.MED, 0.75,
            "Chicago winters are harsh and snow reduces delivery speed. Loop area has dense demand but nightmare parking. "
                    +
                    "Tolls on highways can add up quickly if you're not careful."),

    MIAMI("miami", "Miami", "FL", 3.40, 12.00, MarketTier.MED, 0.80,
            "Miami Beach offers high tips but brutal tourist traffic. " +
                    "Year-round warm weather means no winter slowdown, and Latin food delivery is consistently high volume."),

    SEATTLE("seattle", "Seattle", "WA", 4.50, 19.97, MarketTier.HIGH, 0.78,
            "Seattle rain makes driving slower and more dangerous, increasing wait times. " +
                    "Tech workers tip well, and Capitol Hill has strong nightlife demand."),

    // --- NEW CITIES (40+) ---

    ATLANTA("atlanta", "Atlanta", "GA", 3.20, 7.25, MarketTier.MED, 0.70,
            "Atlanta traffic is notorious. Stick to the Perimeter or focused neighborhoods like Buckhead to avoid gridlock."),

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
