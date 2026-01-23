@import com.gigwager.model.SeoMeta
@import com.gigwager.model.CityData
@import com.gigwager.model.CityScenario
@import java.util.List

@param SeoMeta seoMeta
@param String app
@param String appName
@param CityData city
@param List<CityScenario> scenarios
@param CityScenario featuredScenario
@param String lastUpdated
@param String otherApp
@param String otherAppName
@param String otherAppUrl
@param java.util.List<com.gigwager.model.CityData> similarCities

@template.layout.main(
    seoMeta = seoMeta,
    content = @`
        <!-- Hero Section -->
        <div class="bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 pt-20 pb-24 px-6">
            <div class="max-w-4xl mx-auto">
                <!-- Breadcrumb Navigation (Hub & Spoke Silo) -->
                <nav class="text-sm text-slate-400 mb-4 flex items-center gap-2">
                    <a href="/" class="hover:text-emerald-400 transition">Home</a>
                    <span class="text-slate-600">›</span>
                    <a href="/salary/directory" class="hover:text-emerald-400 transition">Salary Directory</a>
                    <span class="text-slate-600">›</span>
                    <span class="text-white">${city.getCityName()} ${appName} Earnings</span>
                </nav>
                <div class="flex items-center gap-4 mb-6">
                    <div class="inline-block px-4 py-1 bg-emerald-500/10 border border-emerald-500/30 rounded-full">
                        <span class="text-emerald-400 text-sm font-bold uppercase tracking-wider">${city.getCityName()}, ${city.getState()}</span>
                    </div>
                    <span class="text-slate-400 text-xs font-medium uppercase tracking-wider">Last Updated: ${lastUpdated}</span>
                </div>
                
                <h1 class="text-4xl md:text-6xl font-black text-white mb-6 leading-tight">
                    Real ${appName} Earnings in ${city.getCityName()}
                    <span class="block text-emerald-400 mt-2">(2026 Truth)</span>
                </h1>
                
                <p class="text-xl text-slate-300 max-w-2xl mb-8">
                    Stop guessing. See your <strong>true hourly wage</strong> after ${city.getCityName()}'s 
                    gas prices ($${String.format("%.2f", city.getGasPrice())}/gal), vehicle costs, and taxes.
                </p>

                <!-- AD SLOT 1: High Visibility (Top) -->
                <div class="mb-8 p-4 bg-slate-800/50 rounded-xl border border-white/10 text-center min-h-[100px] flex items-center justify-center">
                    <span class="text-xs text-slate-500 font-mono tracking-widest uppercase">Sponsored</span>
                    <!-- AdSense Code Placeholder for Top Slot -->
                    <!-- <ins class="adsbygoogle" style="display:block" ... ></ins> -->
                </div>

                <!-- Featured Scenario Card -->
                <div class="bg-white/5 backdrop-blur-sm border border-white/10 rounded-2xl p-8">
                    <div class="flex items-baseline gap-3 mb-2">
                        <span class="text-sm font-bold text-slate-400 uppercase tracking-wider">Estimated Take-Home</span>
                        <span class="text-xs px-2 py-1 bg-slate-700 text-slate-300 rounded-md">${featuredScenario.getName()}</span>
                    </div>
                    <div class="flex items-baseline gap-2 mb-4">
                        <span class="text-5xl font-black text-emerald-400">$${String.format("%.2f", featuredScenario.getNetHourly())}</span>
                        <span class="text-xl text-slate-400 font-medium">/hr</span>
                    </div>
                    <p class="text-sm text-slate-400">
                        Based on typical ${city.getCityName()} conditions: $${featuredScenario.getGrossWeekly()}/wk gross, 
                        ${featuredScenario.getMiles()} mi, ${featuredScenario.getHours()} hrs
                    </p>
                </div>
            </div>
        </div>

        <!-- Main Content -->
        <div class="max-w-4xl mx-auto px-6 py-16 space-y-16">

            <!-- City-Specific Market Context (Handwritten, Non-Calculated) -->
            <section class="bg-white rounded-2xl p-8 shadow-sm border border-slate-200">
                <h2 class="text-2xl font-bold text-slate-900 mb-4 flex items-center gap-2">
                    <span class="text-2xl">📍</span>
                    What Makes ${city.getCityName()} Different?
                </h2>
                <p class="text-slate-700 leading-relaxed mb-4">
                    $unsafe{city.getMarketDescription()}
                </p>
                
                <!-- Narrative Branching: Cost Context -->
                @if(city.isCheapGas())
                    <div class="bg-emerald-50 border border-emerald-200 rounded-xl p-4 mt-4">
                        <p class="text-emerald-900 text-sm">
                            <strong>💰 Gas Price Advantage:</strong> Lucky for you, ${city.getCityName()} gas prices 
                            ($${String.format("%.2f", city.getGasPrice())}/gal) are about 20% below the national average. 
                            This significantly improves your profit margin compared to coastal cities.
                        </p>
                    </div>
                @endif

                @if(city.isHighTraffic())
                    <div class="bg-amber-50 border border-amber-200 rounded-xl p-4 mt-4">
                        <p class="text-amber-900 text-sm">
                            <strong>⚠️ Traffic Warning:</strong> Stop-and-go traffic in ${city.getCityName()} kills MPG 
                            and increases wear on brakes. Factor in 15-20% higher vehicle costs than highway-focused markets.
                        </p>
                    </div>
                @endif

                @if(city.isHighCost())
                    <div class="bg-red-50 border border-red-200 rounded-xl p-4 mt-4">
                        <p class="text-red-900 text-sm">
                            <strong>💸 High Cost Market:</strong> ${city.getCityName()} has one of the highest costs of living 
                            in the US. While minimum wage is $${String.format("%.2f", city.getMinWage())}/hr, housing and parking 
                            expenses can eat your earnings quickly.
                        </p>
                    </div>
                @endif
            </section>
            
            <!-- Internal Link: The Hidden Truth (Orphan Defense) -->
            <section class="bg-red-50 rounded-2xl p-8 border-2 border-red-200 shadow-sm relative overflow-hidden group">
                <div class="absolute -right-4 -top-4 text-8xl text-red-500/10 font-black group-hover:scale-110 transition-transform">TRUTH</div>
                <div class="relative z-10">
                    <h3 class="text-xl font-bold text-red-900 mb-3 flex items-center gap-2">
                        <span>📉</span> The Hidden Cost of Driving in ${city.getCityName()}
                    </h3>
                    <p class="text-red-800 mb-6">
                        Your car is losing value every mile you drive. Most drivers in ${city.getCityName()} ignore depreciation until it's too late.
                        Read our deep dive on why your "$100 day" is actually closer to $60.
                    </p>
                    <a href="/blog/hidden-costs" class="inline-block px-6 py-3 bg-red-600 text-white font-bold rounded-lg hover:bg-red-700 transition shadow-md">
                        Read the Truth &rarr;
                    </a>
                </div>
            </section>

            <!-- AD SLOT 2: In-Content (Middle) -->
            <div class="w-full bg-slate-50 rounded-xl border border-slate-200 text-center min-h-[150px] flex flex-col items-center justify-center overflow-hidden">
                <span class="text-[10px] text-slate-300 font-mono tracking-widest uppercase mb-2">Advertisement</span>
                <!-- Google AdSense Auto Ads Filler -->
            </div>

            <!-- App-Specific Narrative -->
            <section class="bg-slate-50 rounded-2xl p-8 border border-slate-200">
                <h2 class="text-2xl font-bold text-slate-900 mb-4">
                    ${appName} in ${city.getCityName()}: What to Expect
                </h2>
                
                @if(app.equals("uber"))
                    <div class="prose prose-slate max-w-none">
                        <p class="text-slate-700">
                            Rideshare drivers in ${city.getCityName()} face unique challenges. Airport queues can lock up 
                            your time without pay, and dead miles (driving to pickup locations) reduce your effective hourly rate.
                        </p>
                        <p class="text-slate-700">
                            <strong>Strategy tip:</strong> Focus on short trips in dense areas during surge hours. 
                            Avoid long airport runs unless the surge multiplier is 1.5x or higher.
                        </p>
                    </div>
                @else
                    <div class="prose prose-slate max-w-none">
                        <p class="text-slate-700">
                            DoorDash parking in ${city.getCityName()} downtown areas can consume 10-15 minutes per hour, 
                            directly cutting into your earnings. Restaurant wait times vary wildly by neighborhood.
                        </p>
                        <p class="text-slate-700">
                            <strong>Strategy tip:</strong> Stack orders when possible and cherry-pick deliveries with 
                            high tip-to-distance ratios ($2+ per mile minimum).
                        </p>
                    </div>
                @endif
            </section>

            <!-- Scenario Comparison Table -->
            <section>
                <h2 class="text-2xl font-bold text-slate-900 mb-6">Estimated Earnings by Work Level</h2>
                <div class="bg-white rounded-2xl border border-slate-200 overflow-hidden">
                    <!-- Mobile: Enable horizontal scroll -->
                    <div class="overflow-x-auto">
                    <table class="w-full">
                        <thead class="bg-slate-900 text-white">
                            <tr>
                                <th class="px-3 md:px-6 py-3 md:py-4 text-left text-xs md:text-sm font-bold uppercase whitespace-nowrap">Work Level</th>
                                <th class="px-3 md:px-6 py-3 md:py-4 text-right text-xs md:text-sm font-bold uppercase whitespace-nowrap">Gross/Week</th>
                                <th class="px-3 md:px-6 py-3 md:py-4 text-right text-xs md:text-sm font-bold uppercase whitespace-nowrap">Net Profit</th>
                                <th class="px-3 md:px-6 py-3 md:py-4 text-right text-xs md:text-sm font-bold uppercase whitespace-nowrap">Real $/hr</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-200">
                            @for(var scenario : scenarios)
                                <tr class="hover:bg-slate-50 transition">
                                    <td class="px-3 md:px-6 py-3 md:py-4 font-medium text-slate-900 text-sm md:text-base whitespace-nowrap">${scenario.getName()}</td>
                                    <td class="px-3 md:px-6 py-3 md:py-4 text-right text-slate-700 text-sm md:text-base whitespace-nowrap">$${scenario.getGrossWeekly()}</td>
                                    <td class="px-3 md:px-6 py-3 md:py-4 text-right font-bold text-emerald-600 text-sm md:text-base whitespace-nowrap">$${String.format("%.2f", scenario.getNetProfit())}</td>
                                    <td class="px-3 md:px-6 py-3 md:py-4 text-right text-lg md:text-xl font-bold text-slate-900 whitespace-nowrap">$${String.format("%.2f", scenario.getNetHourly())}</td>
                                </tr>
                            @endfor
                        </tbody>
                    </table>
                    </div>
                </div>
                <p class="text-xs text-slate-500 mt-4">
                    <strong>Note:</strong> These are estimated scenarios based on typical ${city.getCityName()} conditions. 
                    Actual results vary by vehicle efficiency, cherry-picking skill, and surge timing.
                </p>
            </section>

            <!-- New Content: Best Times and Areas -->
            <section class="bg-white rounded-2xl p-8 border border-slate-200">
                <h2 class="text-2xl font-bold text-slate-900 mb-4">Best Times and Areas to Drive in ${city.getCityName()}</h2>
                <div class="prose prose-slate max-w-none text-slate-700">
                    @if(city.isHighTraffic())
                        <p>
                            <strong>Avoid the Gridlock:</strong> ${city.getCityName()} is notorious for congestion.
                            Driving during rush hour (7-9 AM and 4-6 PM) will kill your hourly rate due to idling and low mileage.
                            Focus on <strong>early mornings</strong> or <strong>late-night weekends</strong> when streets are clearer.
                        </p>
                    @elseif(city.isCheapGas())
                        <p>
                            <strong>Distance is Your Friend:</strong> With gas prices at $${String.format("%.2f", city.getGasPrice())}/gal,
                            you have an advantage. Don't be afraid to take longer highway trips to suburbs where tipping might be better.
                            The math works out in your favor compared to high-gas cities.
                        </p>
                    @elseif(city.isHighCost())
                        <p>
                            <strong>Chase the Surge:</strong> In a high-cost market like ${city.getCityName()}, base rates often aren't enough.
                            You need to be aggressive about positioning yourself in <strong>premium zones</strong> and only driving during
                            peak surge/promo windows to make the living wage numbers work.
                        </p>
                    @else
                        <p>
                            <strong>Follow the Demand:</strong> Focus on high-density commercial/restaurant districts during lunch (11 AM - 1 PM)
                            and dinner (5 PM - 8 PM) rushes. In ${city.getCityName()}, consistency is key—stick to areas with
                            proven order volume rather than chasing "hot spots" that disappear quickly.
                        </p>
                    @endif
                </div>
            </section>

            <!-- New Content: Common Expenses -->
            <section class="bg-slate-50 rounded-2xl p-8 border border-slate-200">
                <h2 class="text-2xl font-bold text-slate-900 mb-4">Common Expenses ${appName} Drivers in ${city.getCityName()} Overlook</h2>
                <ul class="space-y-3 text-slate-700 list-disc pl-5">
                    @if(city.isHighTraffic() || city.isHighCost())
                        <li>
                            <strong>Parking Tickets:</strong> In dense areas of ${city.getCityName()}, one $50 ticket can wipe out 3 hours of profit.
                            Always factor in legal parking time or commercial loading zones.
                        </li>
                        <li>
                            <strong>Brake & Transmission Wear:</strong> Stop-and-go traffic accelerates wear.
                            Budget 20% more for maintenance than highway drivers.
                        </li>
                    @else
                        <li>
                            <strong>Highway Miles:</strong> You'll rack up miles faster in ${city.getCityName()}.
                            Tires and oil changes will come around more frequently than you expect.
                        </li>
                    @endif
                    <li>
                        <strong>Rideshare Insurance:</strong> Personal auto policies often deny coverage for gig work accidents.
                        Make sure you have a gap endorsement.
                    </li>
                    <li>
                        <strong>The Tax Bill:</strong> Remember, the numbers above include 15.3% self-employment tax.
                        If you don't save this, tax season will be a shock.
                    </li>
                </ul>
            </section>

            <!-- FAQ Schema JSON-LD -->
            <script type="application/ld+json">
            {
              "@context": "https://schema.org",
              "@type": "FAQPage",
              "mainEntity": [{
                "@type": "Question",
                "name": "How are these ${appName} earnings calculated?",
                "acceptedAnswer": {
                  "@type": "Answer",
                  "text": "We calculate 'real' net profit by starting with estimated gross income levels for ${city.getMarketTier()} markets, then subtracting fuel costs ($${String.format("%.2f", city.getGasPrice())}/gal), vehicle depreciation (using the IRS standard rate of 72.5 cents/mile), and estimated self-employment taxes (15.3%). This reveals your true take-home pay."
                }
              }, {
                "@type": "Question",
                "name": "Why do earnings in ${city.getCityName()} differ from other cities?",
                "acceptedAnswer": {
                  "@type": "Answer",
                  "text": "Earnings vary due to local factors. ${city.getCityName()} has specific gas prices ($${String.format("%.2f", city.getGasPrice())}/gal), traffic conditions, and market demand tiers (${city.getMarketTier()}). High-cost cities often have higher gross pay but also higher expenses, while lower-cost cities might offer better effective margins."
                }
              }, {
                "@type": "Question",
                "name": "Do these numbers include taxes?",
                "acceptedAnswer": {
                  "@type": "Answer",
                  "text": "Yes. Our models deduct the standardized IRS mileage deduction from your gross income to find taxable profit, then apply a 15.3% self-employment tax estimate. This is crucial because gig drivers are independent contractors (1099), not employees."
                }
              }]
            }
            </script>

            <!-- AD SLOT 3: Bottom (Before CTA) -->
            <div class="w-full bg-white rounded-xl border border-slate-200 text-center min-h-[150px] flex flex-col items-center justify-center overflow-hidden mb-8">
                <span class="text-[10px] text-slate-300 font-mono tracking-widest uppercase mb-2">Sponsored Content</span>
            </div>

            <!-- Work-Level Deep-Dive Links (Internal Silo) -->
            <section class="bg-white rounded-2xl p-8 border border-slate-200 mb-8">
                <h3 class="text-2xl font-bold text-slate-900 mb-3">
                    📊 Want a Deep-Dive for Your Work Level?
                </h3>
                <p class="text-slate-600 mb-6">
                    The table above shows all three scenarios. Choose yours below for tax strategy, time optimization, and work-level specific insights.
                </p>
                <div class="grid md:grid-cols-3 gap-4">
                    <a href="/salary/${app}/${city.getSlug()}/part-time" 
                       class="block p-6 bg-emerald-50 border-2 border-emerald-200 rounded-xl hover:shadow-lg hover:border-emerald-400 transition group">
                        <div class="text-4xl mb-3">⏰</div>
                        <div class="font-bold text-lg text-slate-900 group-hover:text-emerald-700 transition">Part-Time ${appName} in ${city.getCityName()}</div>
                        <p class="text-sm text-slate-600 mt-2">10 hrs/week strategy, tax tips, best hours to drive</p>
                    </a>

                    <a href="/salary/${app}/${city.getSlug()}/side-hustle" 
                       class="block p-6 bg-amber-50 border-2 border-amber-200 rounded-xl hover:shadow-lg hover:border-amber-400 transition group">
                        <div class="text-4xl mb-3">💼</div>
                        <div class="font-bold text-lg text-slate-900 group-hover:text-amber-700 transition">Side-Hustle ${appName} in ${city.getCityName()}</div>
                        <p class="text-sm text-slate-600 mt-2">25 hrs/week balance, quarterly taxes, vehicle strategy</p>
                    </a>

                    <a href="/salary/${app}/${city.getSlug()}/full-time" 
                       class="block p-6 bg-blue-50 border-2 border-blue-200 rounded-xl hover:shadow-lg hover:border-blue-400 transition group">
                        <div class="text-4xl mb-3">🚗</div>
                        <div class="font-bold text-lg text-slate-900 group-hover:text-blue-700 transition">Full-Time ${appName} in ${city.getCityName()}</div>
                        <p class="text-sm text-slate-600 mt-2">40 hrs/week reality, health insurance, retirement planning</p>
                    </a>
                </div>
            </section>

            <!-- Cross-App Comparison "VS" Table (Higher Engagement) -->
            <section class="bg-gradient-to-br from-slate-100 to-slate-200 rounded-2xl p-8 border-2 border-slate-300 shadow-sm mb-8">
                <div class="flex items-center justify-between mb-6">
                    <h3 class="text-2xl font-bold text-slate-900 flex items-center gap-2">
                        <span class="text-3xl">🥊</span> ${appName} vs ${otherAppName} in ${city.getCityName()}
                    </h3>
                    <span class="px-3 py-1 bg-slate-900 text-white text-xs font-bold rounded-full uppercase tracking-wider">Analysis</span>
                </div>
                
                <div class="bg-white rounded-xl overflow-hidden shadow-sm border border-slate-200">
                    <table class="w-full">
                        <thead class="bg-slate-50 border-b border-slate-200">
                            <tr>
                                <th class="px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase tracking-wider">Feature</th>
                                <th class="px-4 py-3 text-center text-xs font-bold text-slate-900 uppercase tracking-wider bg-emerald-50/50">${appName}</th>
                                <th class="px-4 py-3 text-center text-xs font-bold text-slate-500 uppercase tracking-wider">${otherAppName}</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-100">
                            <tr>
                                <td class="px-4 py-4 text-sm font-medium text-slate-900">Market Tier</td>
                                <td class="px-4 py-4 text-center text-sm font-bold text-emerald-700 bg-emerald-50/30">${city.getMarketTier()}</td>
                                <td class="px-4 py-4 text-center text-sm text-slate-500">${city.getMarketTier()}</td>
                            </tr>
                            <tr>
                                <td class="px-4 py-4 text-sm font-medium text-slate-900">Traffic Impact</td>
                                <td class="px-4 py-4 text-center text-sm font-bold bg-emerald-50/30 text-slate-700">
                                    ${city.isHighTraffic() ? "High (Start/Stop)" : "Moderate"}
                                </td>
                                <td class="px-4 py-4 text-center text-sm text-slate-500">
                                    ${city.isHighTraffic() ? "High (Delays)" : "Moderate"}
                                </td>
                            </tr>
                            <tr class="bg-slate-50/50">
                                <td class="px-4 py-4 text-sm font-bold text-slate-900">Verdict</td>
                                <td class="px-4 py-4 text-center bg-emerald-50/50">
                                    <span class="inline-block px-2 py-1 bg-emerald-100 text-emerald-800 text-xs font-bold rounded">Active View</span>
                                </td>
                                <td class="px-4 py-4 text-center">
                                    <a href="${otherAppUrl}" class="text-emerald-600 hover:text-emerald-800 font-bold text-sm underline decoration-2">
                                        Check Rates &rarr;
                                    </a>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="mt-6 text-center">
                     <p class="text-slate-600 mb-4 text-sm">
                        Curious if ${otherAppName} pays better in ${city.getCityName()}?
                    </p>
                    <a href="${otherAppUrl}" 
                       class="inline-block px-6 py-3 bg-white border border-slate-300 text-slate-900 font-bold rounded-lg hover:bg-slate-50 transition shadow-sm">
                        Compare with ${otherAppName} in ${city.getCityName()} now &rarr;
                    </a>
                </div>
            </section>

            <!-- Internal Linking Silo (Bottom) -->
            <section class="mb-12">
                <h4 class="text-sm font-bold text-slate-400 uppercase tracking-wider mb-4 border-b border-slate-200 pb-2">
                    Other High-Demand ${city.getMarketTier()} Markets
                </h4>
                <div class="grid grid-cols-2 md:grid-cols-4 gap-3">
                    @for(com.gigwager.model.CityData simCity : similarCities)
                        <a href="/salary/${app}/${simCity.getSlug()}" 
                           class="block p-3 bg-white rounded-lg border border-slate-100 hover:border-emerald-300 hover:shadow-sm transition text-center">
                            <div class="text-xl mb-1">📍</div>
                            <div class="text-xs font-bold text-slate-700">${simCity.getCityName()}</div>
                            <div class="text-[10px] text-slate-400">View Earnings</div>
                        </a>
                    @endfor
                </div>
            </section>

            <!-- CTA: Deep Calculator -->
            <section class="bg-gradient-to-br from-emerald-600 to-emerald-700 rounded-2xl p-10 text-center shadow-xl">
                <h3 class="text-2xl font-bold text-white mb-3">Calculate YOUR Exact Take-Home</h3>
                <p class="text-emerald-100 mb-6 max-w-xl mx-auto">
                    These scenarios use averages. Want precision? Enter your exact miles, hours, and vehicle type.
                </p>
                <a href="/${app}?gross=${featuredScenario.getGrossWeekly()}&miles=${featuredScenario.getMiles()}&hours=${featuredScenario.getHours()}&gasPrice=${city.getGasPrice()}" 
                   class="inline-block px-8 py-4 bg-white text-emerald-700 font-bold text-lg rounded-xl hover:bg-emerald-50 transition shadow-lg">
                    Adjust These Numbers &rarr;
                </a>
            </section>

        </div>
    `
)
