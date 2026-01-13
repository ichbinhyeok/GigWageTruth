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

@template.layout.main(
    seoMeta = seoMeta,
    content = @`
        <!-- Hero Section -->
        <div class="bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 pt-20 pb-24 px-6">
            <div class="max-w-4xl mx-auto">
                <div class="inline-block px-4 py-1 bg-emerald-500/10 border border-emerald-500/30 rounded-full mb-6">
                    <span class="text-emerald-400 text-sm font-bold uppercase tracking-wider">${city.getCityName()}, ${city.getState()}</span>
                </div>
                
                <h1 class="text-4xl md:text-6xl font-black text-white mb-6 leading-tight">
                    Real ${appName} Earnings in ${city.getCityName()}
                    <span class="block text-emerald-400 mt-2">(2026 Truth)</span>
                </h1>
                
                <p class="text-xl text-slate-300 max-w-2xl mb-8">
                    Stop guessing. See your <strong>true hourly wage</strong> after ${city.getCityName()}'s 
                    gas prices ($${String.format("%.2f", city.getGasPrice())}/gal), vehicle costs, and taxes.
                </p>

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
                    ${city.getMarketDescription()}
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
                    <table class="w-full">
                        <thead class="bg-slate-900 text-white">
                            <tr>
                                <th class="px-6 py-4 text-left text-sm font-bold uppercase">Work Level</th>
                                <th class="px-6 py-4 text-right text-sm font-bold uppercase">Gross/Week</th>
                                <th class="px-6 py-4 text-right text-sm font-bold uppercase">Net Profit</th>
                                <th class="px-6 py-4 text-right text-sm font-bold uppercase">Real $/hr</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-200">
                            @for(var scenario : scenarios)
                                <tr class="hover:bg-slate-50 transition">
                                    <td class="px-6 py-4 font-medium text-slate-900">${scenario.getName()}</td>
                                    <td class="px-6 py-4 text-right text-slate-700">$${scenario.getGrossWeekly()}</td>
                                    <td class="px-6 py-4 text-right font-bold text-emerald-600">$${String.format("%.2f", scenario.getNetProfit())}</td>
                                    <td class="px-6 py-4 text-right text-xl font-bold text-slate-900">$${String.format("%.2f", scenario.getNetHourly())}</td>
                                </tr>
                            @endfor
                        </tbody>
                    </table>
                </div>
                <p class="text-xs text-slate-500 mt-4">
                    <strong>Note:</strong> These are estimated scenarios based on typical ${city.getCityName()} conditions. 
                    Actual results vary by vehicle efficiency, cherry-picking skill, and surge timing.
                </p>
            </section>

            <!-- CTA: Deep Calculator -->
            <section class="bg-gradient-to-br from-emerald-600 to-emerald-700 rounded-2xl p-10 text-center shadow-xl">
                <h3 class="text-2xl font-bold text-white mb-3">Calculate YOUR Exact Take-Home</h3>
                <p class="text-emerald-100 mb-6 max-w-xl mx-auto">
                    These scenarios use averages. Want precision? Enter your exact miles, hours, and vehicle type.
                </p>
                <a href="/${app}?gross=${featuredScenario.getGrossWeekly()}&miles=${featuredScenario.getMiles()}&hours=${featuredScenario.getHours()}" 
                   class="inline-block px-8 py-4 bg-white text-emerald-700 font-bold text-lg rounded-xl hover:bg-emerald-50 transition shadow-lg">
                    Adjust These Numbers &rarr;
                </a>
            </section>

        </div>
    `
)
