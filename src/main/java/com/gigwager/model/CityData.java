package com.gigwager.model;

import java.util.Arrays;
import java.util.Optional;

public enum CityData {
    AUSTIN("austin", "Austin", "TX", 2.90, 7.25, MarketTier.MED, 0.85,
            "Austin has strong nightlife delivery demand on the famous 6th Street entertainment district. " +
                    "SXSW and ACL festivals create massive surge periods, but traffic congestion downtown can hurt MPG."),

    HOUSTON("houston", "Houston", "TX", 2.85, 7.25, MarketTier.MED, 0.90,
            "Houston's sprawling layout means longer trips and better highway MPG, but summer heat forces A/C usage that drains fuel. "
                    +
                    "Medical Center and Energy Corridor provide consistent corporate delivery demand."),

    SAN_FRANCISCO("san-francisco", "San Francisco", "CA", 4.50, 16.99, MarketTier.HIGH, 0.65,
            "SF presents extreme parking challenges and steep hills that kill fuel economy. " +
                    "However, high minimum wage and dense demand make short trips profitable if you cherry-pick."),

    NEW_YORK("new-york", "New York", "NY", 3.75, 16.00, MarketTier.HIGH, 0.60,
            "NYC traffic is brutal. Expect frequent dead time in gridlock and expensive tolls. " +
                    "Manhattan delivery density is unmatched, but parking tickets can destroy your profit margin."),

    LOS_ANGELES("los-angeles", "Los Angeles", "CA", 4.20, 16.78, MarketTier.HIGH, 0.70,
            "LA requires highway driving between zones, burning more gas but offering consistent earnings. " +
                    "Beach cities tip better, but you'll compete with aggressive multi-appers."),

    DALLAS("dallas", "Dallas", "TX", 2.95, 7.25, MarketTier.MED, 0.88,
            "Dallas offers a good balance: relatively cheap gas, sprawling suburbs with steady demand. " +
                    "Uptown and Deep Ellum have strong weekend nightlife orders."),

    PHOENIX("phoenix", "Phoenix", "AZ", 3.10, 14.35, MarketTier.MED, 0.92,
            "Phoenix heat in summer is extreme (110°F+), forcing constant A/C and reducing fuel efficiency by 15%. " +
                    "Scottsdale tips well, but long distances between deliveries are common."),

    CHICAGO("chicago", "Chicago", "IL", 3.60, 15.00, MarketTier.MED, 0.75,
            "Chicago winters are harsh and snow reduces delivery speed. Loop area has dense demand but nightmare parking. "
                    +
                    "Tolls on highways can add up quickly if you're not careful."),

    MIAMI("miami", "Miami", "FL", 3.40, 12.00, MarketTier.MED, 0.80,
            "Miami Beach offers high tips but brutal tourist traffic. " +
                    "Year-round warm weather means no winter slowdown, and Latin food delivery is consistently high volume."),

    SEATTLE("seattle", "Seattle", "WA", 3.90, 16.69, MarketTier.HIGH, 0.78,
            "Seattle rain makes driving slower and more dangerous, increasing wait times. " +
                    "Tech workers tip well, and Capitol Hill has strong nightlife demand.");

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
