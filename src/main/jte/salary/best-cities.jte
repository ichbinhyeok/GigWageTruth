@import com.gigwager.model.SeoMeta
@import com.gigwager.dto.CityRankingDto
@import com.gigwager.model.DriverShiftReport
@import java.util.List
@import com.gigwager.util.AppConstants

@param String app
@param String appName
@param List<CityRankingDto> rankedCities
@param CityRankingDto topRankedCity
@param String lastUpdated
@param SeoMeta seoMeta
@param Boolean noIndex = false
@param com.gigwager.service.DataLayerService dataLayerService
@param String itemListJsonLd = "{}"
@param String faqJsonLd = "{}"
@param String appHubUrl = ""
@param String coverageUrl = ""
@param String topCityReportUrl = ""
@param String coverageGuideTitle = ""
@param String coverageGuideDescription = ""
@param String methodologyUrl = "/methodology"
@param String topThreeCities = ""
@param int currentYear = 2026
@param int rankedCityCount = 0
@param double leadOverRunnerUp = 0
@param CityRankingDto runnerUpCity
@param List<DriverShiftReport> driverShiftReports = java.util.List.of()

@template.layout.main(
    seoMeta = seoMeta,
    noIndex = noIndex,
    content = @`
        <div class="bg-slate-50 min-h-screen py-16 px-6">
            <div class="max-w-4xl mx-auto">
                <div class="text-center mb-12">
                    <h1 class="text-4xl lg:text-5xl font-black text-slate-900 mb-6 tracking-tight">
                        @if(app.equals("doordash"))
                            Best Cities for DoorDash Drivers in ${currentYear}
                            <span class="block text-emerald-600">Highest Net Pay After Gas and Mileage</span>
                        @else
                            Best Cities to ${appName} in ${currentYear}
                            <span class="block text-emerald-600">Highest Net Pay After Expenses</span>
                        @endif
                    </h1>
                    <p class="text-xl text-slate-600 max-w-2xl mx-auto" id="serp-answer">
                        @if(app.equals("doordash"))
                            If you searched for <strong>highest paying DoorDash cities near me</strong> or <strong>best cities to DoorDash</strong>, start with the closest metro in this after-expenses ranking. <strong>${topRankedCity.city().getCityName()}</strong> currently leads at about <strong>$${String.format("%.2f", topRankedCity.netHourly())}/hr net</strong>, and the top 3 are <strong>${topThreeCities}</strong>.
                        @else
                            The highest-paying ${appName} city in ${currentYear} is <strong>${topRankedCity.city().getCityName()}</strong> at about <strong>$${String.format("%.2f", topRankedCity.netHourly())}/hr net after expenses</strong>. The current top 3 are <strong>${topThreeCities}</strong>.
                        @endif
                    </p>
                    <p class="text-sm text-slate-500 max-w-2xl mx-auto mt-3">
                        These rankings compare estimated driver earnings after mileage and self-employment tax assumptions. Updated ${lastUpdated}.
                    </p>
                    <div class="flex flex-wrap items-center justify-center gap-3 mt-4 text-xs font-semibold text-slate-600" data-nosnippet>
                        <span class="px-3 py-1 rounded-full bg-white border border-slate-200">Updated ${lastUpdated}</span>
                        <span class="px-3 py-1 rounded-full bg-white border border-slate-200">After-expenses estimate</span>
                        <span class="px-3 py-1 rounded-full bg-white border border-slate-200">Mileage + SE tax assumptions</span>
                        <a href="${methodologyUrl}" class="px-3 py-1 rounded-full bg-white border border-slate-200 hover:border-emerald-300 hover:text-emerald-700 transition-colors">Methodology</a>
                    </div>
                </div>

                <div class="grid sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
                    <div class="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm text-left">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500 mb-2">
                            @if(app.equals("doordash"))
                                Best City to Dash
                            @else
                                #1 City
                            @endif
                        </p>
                        <h2 class="text-2xl font-black text-slate-900">${topRankedCity.city().getCityName()}</h2>
                        <p class="text-sm text-emerald-600 font-bold mt-2">$${String.format("%.2f", topRankedCity.netHourly())}/hr net</p>
                    </div>
                    <div class="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm text-left">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500 mb-2">Top 3</p>
                        <h2 class="text-xl font-black text-slate-900">${topThreeCities}</h2>
                        <p class="text-sm text-slate-600 mt-2">The strongest markets in the current side-hustle model.</p>
                    </div>
                    <div class="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm text-left">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500 mb-2">Lead Over #2</p>
                        <h2 class="text-2xl font-black text-slate-900">$${String.format("%.2f", leadOverRunnerUp)}/hr</h2>
                        <p class="text-sm text-slate-600 mt-2">${topRankedCity.city().getCityName()} over ${runnerUpCity.city().getCityName()}.</p>
                    </div>
                    <div class="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm text-left">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500 mb-2">Cities Ranked</p>
                        <h2 class="text-2xl font-black text-slate-900">${rankedCityCount}</h2>
                        <p class="text-sm text-slate-600 mt-2">Every market links to a deeper city earnings page.</p>
                    </div>
                </div>

                @if(app.equals("doordash"))
                    <section class="bg-white border border-emerald-200 rounded-2xl p-6 mb-8 text-left">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-600 mb-2">DoorDash city search guide</p>
                        <h2 class="text-2xl font-black text-slate-900 mb-3">Best cities to DoorDash near me: use pay first, then availability</h2>
                        <p class="text-slate-600 mb-4">
                            The fastest way to use this list is to find your nearest metro in the ranking, open its city earnings page, then confirm whether DoorDash is accepting or active in that market. A city can look strong on net hourly pay and still be a bad target if onboarding or zone access is limited.
                        </p>
                        <div class="grid md:grid-cols-3 gap-3">
                            <a href="#ranking-table" onclick="gtag('event', 'cta_click', { 'event_category': 'seo_best_cities', 'event_label': 'jump_to_doordash_city_ranking' })" class="block p-4 rounded-xl border border-slate-200 hover:border-emerald-300 hover:bg-emerald-50 transition-colors">
                                <span class="block text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-1">Step 1</span>
                                <span class="block font-black text-slate-900">Compare all ranked cities</span>
                            </a>
                            <a href="${topCityReportUrl}" onclick="gtag('event', 'cta_click', { 'event_category': 'seo_best_cities', 'event_label': 'open_top_doordash_city_report' })" class="block p-4 rounded-xl border border-slate-200 hover:border-amber-300 hover:bg-amber-50 transition-colors">
                                <span class="block text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-1">Step 2</span>
                                <span class="block font-black text-slate-900">Open ${topRankedCity.city().getCityName()} pay details</span>
                            </a>
                            <a href="${coverageUrl}" onclick="gtag('event', 'cta_click', { 'event_category': 'seo_best_cities', 'event_label': 'check_doordash_availability_after_ranking' })" class="block p-4 rounded-xl border border-slate-200 hover:border-sky-300 hover:bg-sky-50 transition-colors">
                                <span class="block text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-1">Step 3</span>
                                <span class="block font-black text-slate-900">Check where you can dash</span>
                            </a>
                            <a href="/reports/doordash-driver-shift-evidence-2026" class="block p-4 rounded-xl border border-slate-200 hover:border-emerald-300 hover:bg-emerald-50 transition-colors md:col-span-3">
                                <span class="block text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-1">Evidence layer</span>
                                <span class="block font-black text-slate-900">Open DoorDash driver shift evidence: gross, miles, active time, dash time, and net checks</span>
                            </a>
                            <a href="/doordash/can-you-make-100-a-day" class="block p-4 rounded-xl border border-slate-200 hover:border-emerald-300 hover:bg-emerald-50 transition-colors">
                                <span class="block text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-1">Daily target</span>
                                <span class="block font-black text-slate-900">Can you make $100/day with DoorDash?</span>
                            </a>
                            <a href="/doordash/after-gas" class="block p-4 rounded-xl border border-slate-200 hover:border-emerald-300 hover:bg-emerald-50 transition-colors">
                                <span class="block text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-1">Expense check</span>
                                <span class="block font-black text-slate-900">DoorDash after gas and mileage</span>
                            </a>
                            <a href="/doordash/pay-per-mile" class="block p-4 rounded-xl border border-slate-200 hover:border-emerald-300 hover:bg-emerald-50 transition-colors">
                                <span class="block text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-1">Offer floor</span>
                                <span class="block font-black text-slate-900">DoorDash pay per mile</span>
                            </a>
                            <a href="/doordash/adjustment-pay-calculator" class="block p-4 rounded-xl border border-slate-200 hover:border-emerald-300 hover:bg-emerald-50 transition-colors md:col-span-3">
                                <span class="block text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-1">Adjustment pay</span>
                                <span class="block font-black text-slate-900">Calculate DoorDash Prop 22, NYC, or Seattle adjustment pay before comparing cities</span>
                            </a>
                        </div>
                    </section>

                    <section class="bg-slate-900 rounded-3xl p-6 mb-8 text-white">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-2">DoorDash city pay dataset</p>
                        <h2 class="text-3xl font-black mb-4">Not just a list: compare net hourly, hours to $100, weekly net, and local demand signals</h2>
                        <p class="text-slate-300 max-w-3xl mb-6">
                            Generic search results usually stop at "best DoorDash cities" without showing the math. This table exposes the actual side-hustle baseline behind the ranking: after-expenses net hourly, the estimated hours needed to clear $100, the 20-hour weekly net proxy, mileage pressure, and the local zones that can make or break active time.
                        </p>
                        <div class="grid md:grid-cols-3 gap-3">
                            <div class="rounded-2xl bg-white/5 border border-white/10 p-5">
                                <p class="text-xs font-black uppercase tracking-[0.16em] text-emerald-300 mb-2">Click reason</p>
                                <p class="font-bold">Drivers can see whether a nearby city is actually worth testing before opening the full report.</p>
                            </div>
                            <div class="rounded-2xl bg-white/5 border border-white/10 p-5">
                                <p class="text-xs font-black uppercase tracking-[0.16em] text-emerald-300 mb-2">Quality gate</p>
                                <p class="font-bold">Every city links to a deeper earnings page, not a dead-end keyword page.</p>
                            </div>
                            <div class="rounded-2xl bg-white/5 border border-white/10 p-5">
                                <p class="text-xs font-black uppercase tracking-[0.16em] text-emerald-300 mb-2">Use case</p>
                                <p class="font-bold">Best cities to DoorDash, highest paying DoorDash cities near me, and $100/day planning.</p>
                            </div>
                        </div>
                    </section>

                    @template.components.driver_shift_reports(
                        appName = "DoorDash",
                        cityName = "top DoorDash cities",
                        modeledNetHourly = topRankedCity.netHourly(),
                        driverShiftReports = driverShiftReports,
                        sectionClass = "mb-10"
                    )
                @endif

                <div class="bg-white border border-slate-200 rounded-2xl p-5 mb-8" data-nosnippet>
                    <p class="text-sm text-slate-600">
                        Use this page when your question is <strong>which cities pay the most for ${appName}</strong>.
                        If your real question is <strong>whether a market is active</strong>, use the coverage guide first and then come back here for the earnings ranking.
                    </p>
                </div>

                <div class="grid md:grid-cols-3 gap-4 mb-8" data-nosnippet>
                    <a href="${appHubUrl}" class="block bg-white p-6 rounded-xl border border-slate-200 shadow-sm hover:border-emerald-200 hover:bg-emerald-50/40 transition-colors text-left">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-600 mb-2">Need More Context?</p>
                        <h2 class="text-xl font-black text-slate-900 mb-2">${appName} Driver Earnings by City</h2>
                        <p class="text-sm text-slate-600">Open the market hub for city earnings pages, app calculator paths, and broader pay-by-city context.</p>
                    </a>
                    <a href="${topCityReportUrl}" class="block bg-white p-6 rounded-xl border border-slate-200 shadow-sm hover:border-amber-200 hover:bg-amber-50/50 transition-colors text-left">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-amber-600 mb-2">Want The Leader?</p>
                        <h2 class="text-xl font-black text-slate-900 mb-2">${topRankedCity.city().getCityName()} Driver Earnings</h2>
                        <p class="text-sm text-slate-600">Jump straight into the current top-ranked ${appName} city page for the local pay and market breakdown.</p>
                    </a>
                    <a href="${coverageUrl}" class="block bg-white p-6 rounded-xl border border-slate-200 shadow-sm hover:border-sky-200 hover:bg-sky-50/50 transition-colors text-left">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-sky-600 mb-2">Need Coverage First?</p>
                        <h2 class="text-xl font-black text-slate-900 mb-2">${coverageGuideTitle}</h2>
                        <p class="text-sm text-slate-600">${coverageGuideDescription}</p>
                    </a>
                </div>

                <div class="bg-blue-50 p-6 rounded-xl border border-blue-200 text-left mb-8" data-nosnippet>
                    <h2 class="text-blue-900 font-bold mb-2">How This Driver Earnings Ranking Works</h2>
                    <ul class="list-disc pl-5 text-blue-800 text-sm space-y-1">
                        <li><strong>Ranking Criteria:</strong> Sorts strictly by estimated Net Hourly Pay for a "side-hustle" driver.</li>
                        <li><strong>Input Weights:</strong> We apply a traffic multiplier and a market tier multiplier (demand baseline) to estimate gross hourly revenue.</li>
                        <li><strong>Deductions:</strong> We apply the @template.components.irs_rate_snippet() mileage proxy plus an assumed 15.3% SE tax. Local gas prices are shown as market context, not a direct variable in this ranking formula.</li>
                        <li><strong>Answer first:</strong> This page is designed to answer which city pays drivers the most, not whether a market is active.</li>
                        <li><strong>Need coverage instead?</strong> Use the <a href="${coverageUrl}" class="font-bold underline hover:text-blue-900">${coverageGuideTitle}</a> before assuming a market is active.</li>
                    </ul>
                </div>

                @if(app.equals("doordash"))
                    <section class="bg-white rounded-3xl border border-slate-200 p-6 mb-10 shadow-sm">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-amber-700 mb-2">Search-result gap check</p>
                        <h2 class="text-2xl font-black text-slate-900 mb-4">What this page answers that thin DoorDash city lists usually skip</h2>
                        <div class="grid md:grid-cols-3 gap-4">
                            <div class="rounded-2xl border border-slate-200 p-5">
                                <h3 class="font-black text-slate-900 mb-2">Pay ranking is not enough</h3>
                                <p class="text-sm text-slate-600">A high gross market can still be weak after mileage, parking, wait time, and self-employment tax.</p>
                            </div>
                            <div class="rounded-2xl border border-slate-200 p-5">
                                <h3 class="font-black text-slate-900 mb-2">Near me needs a decision rule</h3>
                                <p class="text-sm text-slate-600">Use the closest metro in the table, then compare hours to $100 and the 20-hour weekly net before changing zones.</p>
                            </div>
                            <div class="rounded-2xl border border-slate-200 p-5">
                                <h3 class="font-black text-slate-900 mb-2">Availability still matters</h3>
                                <p class="text-sm text-slate-600">The best-paying city is only useful if DoorDash onboarding and local dash zones are active for you.</p>
                            </div>
                        </div>
                    </section>
                @endif

                <!-- Top 5 Highlight Cards -->
                <div class="mb-12">
                    <h2 class="text-2xl font-bold text-slate-900 mb-6">Why These Cities Lead The ${appName} Net Pay Ranking</h2>
                    <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                        @for(int i = 0; i < Math.min(rankedCities.size(), 5); i++)
                            !{CityRankingDto ranking = rankedCities.get(i);}
                            !{com.gigwager.model.CityLocalData local = dataLayerService.getLocalData(ranking.city().getSlug());}
                            <div class="bg-white p-6 rounded-xl border border-emerald-100 shadow-sm relative overflow-hidden">
                                <div class="absolute top-0 left-0 w-1 h-full bg-emerald-500"></div>
                                <h3 class="font-bold text-slate-900 text-lg flex items-center gap-2">
                                    <span class="bg-emerald-100 text-emerald-800 font-black text-xs px-2 py-1 rounded-full">#${i + 1}</span>
                                    ${ranking.city().getCityName()}
                                </h3>
                                <p class="text-xs text-slate-500 mt-1 mb-3">Est. Net Hourly: <strong class="text-emerald-600">$${String.format("%.2f", ranking.netHourly())}</strong></p>
                                <p class="text-sm text-slate-600 italic">
                                    "High surge density near ${local != null && local.nightlifeDistrict() != null ? local.nightlifeDistrict() : "downtown"} and consistent order volume around ${local != null && local.shoppingDistrict() != null ? local.shoppingDistrict() : "commercial areas"} keeps active time highly optimized."
                                </p>
                            </div>
                        @endfor
                    </div>
                </div>

                <div id="ranking-table" class="bg-white rounded-3xl overflow-hidden border border-slate-200 shadow-sm">
                    <div class="bg-slate-900 px-6 py-5">
                        <h2 class="text-2xl font-black text-white">${appName} city earnings dataset</h2>
                        <p class="text-sm text-slate-300 mt-1">Side-hustle baseline with after-expenses net pay, target hours, mileage pressure, and city-specific demand notes.</p>
                    </div>
                    <div class="overflow-x-auto">
                        <table class="w-full min-w-[72rem] text-left border-collapse">
                            <thead>
                                <tr class="bg-slate-900 text-white border-b border-slate-700">
                                    <th class="p-4 font-bold w-16 text-center">Rank</th>
                                    <th class="p-4 font-bold">City</th>
                                    <th class="p-4 font-bold">Net Hourly Pay</th>
                                    <th class="p-4 font-bold">Hours to $100</th>
                                    <th class="p-4 font-bold">20-hour weekly net</th>
                                    <th class="p-4 font-bold">Mileage pressure</th>
                                    <th class="p-4 font-bold">Local demand signal</th>
                                    <th class="p-4 font-bold text-right">Details</th>
                                </tr>
                            </thead>
                            <tbody>
                                @for(int i = 0; i < rankedCities.size(); i++)
                                    !{CityRankingDto ranking = rankedCities.get(i);}
                                    !{com.gigwager.model.CityScenario scenario = ranking.scenario();}
                                    !{com.gigwager.model.CityLocalData local = dataLayerService.getLocalData(ranking.city().getSlug());}
                                    !{double hoursToHundred = ranking.netHourly() > 0 ? 100.0 / ranking.netHourly() : 0;}
                                    !{double weeklyNetAtTwenty = ranking.netHourly() * 20.0;}
                                    !{int weeklyMiles = scenario != null ? scenario.getMiles() : 250;}
                                    <tr class="border-b border-slate-100 hover:bg-slate-50 transition-colors">
                                        <td class="p-4 font-bold text-center text-slate-400">#${i + 1}</td>
                                        <td class="p-4">
                                            <strong class="block text-slate-900">${ranking.city().getCityName()}, ${ranking.city().getState()}</strong>
                                            <span class="text-xs text-slate-500">${ranking.workLevel()} baseline</span>
                                            <a href="/salary/${app}/${ranking.city().getSlug()}" class="mt-2 inline-flex lg:hidden px-3 py-2 bg-slate-100 hover:bg-emerald-50 text-emerald-700 font-bold rounded-lg text-xs transition-colors border border-slate-200 hover:border-emerald-200">Open city earnings</a>
                                        </td>
                                        <td class="p-4 font-black text-emerald-600">$${String.format("%.2f", ranking.netHourly())}/hr</td>
                                        <td class="p-4 text-slate-700 font-bold">${String.format("%.1f", hoursToHundred)} hrs</td>
                                        <td class="p-4 text-slate-700 font-bold">$${String.format("%.0f", weeklyNetAtTwenty)}</td>
                                        <td class="p-4 text-slate-600 text-sm">${weeklyMiles} mi/wk</td>
                                        <td class="p-4 text-slate-600 text-sm">
                                            ${local.nightlifeDistrict()} + ${local.shoppingDistrict()}
                                        </td>
                                        <td class="p-4 text-right">
                                            <div class="flex flex-col items-end gap-2">
                                                <a href="/salary/${app}/${ranking.city().getSlug()}" class="inline-block px-4 py-2 bg-slate-100 hover:bg-emerald-50 text-emerald-700 font-bold rounded-lg text-sm transition-colors border border-slate-200 hover:border-emerald-200">Open city earnings</a>
                                                <a href="/salary/${app}/${ranking.city().getSlug()}/side-hustle" class="text-xs font-bold text-slate-500 hover:text-emerald-700 underline decoration-slate-300">Side-hustle detail</a>
                                                @if(app.equals("doordash"))
                                                    <a href="/salary/${app}/${ranking.city().getSlug()}/hourly-pay" class="text-xs font-bold text-slate-500 hover:text-emerald-700 underline decoration-slate-300">Hourly pay page</a>
                                                    <a href="/salary/${app}/${ranking.city().getSlug()}/best-areas" class="text-xs font-bold text-slate-500 hover:text-emerald-700 underline decoration-slate-300">Best areas page</a>
                                                @endif
                                                <a href="/salary/${app}/${ranking.city().getSlug()}/100-a-day" class="text-xs font-bold text-slate-500 hover:text-emerald-700 underline decoration-slate-300">$100/day path</a>
                                            </div>
                                        </td>
                                    </tr>
                                @endfor
                            </tbody>
                        </table>
                    </div>
                </div>

                <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm mt-10">
                    <h2 class="text-3xl font-black text-slate-900 mb-6">Frequently Asked Questions</h2>
                    <div class="space-y-6">
                        <div>
                            <h3 class="text-xl font-bold text-slate-900 mb-2">What is the highest-paying ${appName} city in ${currentYear}?</h3>
                            <p class="text-slate-600">${topRankedCity.city().getCityName()} currently leads our after-expenses ranking at about $${String.format("%.2f", topRankedCity.netHourly())}/hr net based on mileage and self-employment tax assumptions.</p>
                        </div>
                        <div>
                            <h3 class="text-xl font-bold text-slate-900 mb-2">Is this a coverage list or an earnings ranking?</h3>
                            <p class="text-slate-600">This page is an after-expenses earnings ranking. Use the <a href="${coverageUrl}" class="font-bold underline hover:text-slate-900">${coverageGuideTitle}</a> when your question is whether a market is active.</p>
                        </div>
                        <div>
                            <h3 class="text-xl font-bold text-slate-900 mb-2">How many ${appName} city earnings pages are ranked here?</h3>
                            <p class="text-slate-600">GigVerdict currently ranks ${rankedCityCount} city earnings pages here, and every row links to a deeper city page with calculator and tax paths.</p>
                        </div>
                        @if(app.equals("doordash"))
                            <div>
                                <h3 class="text-xl font-bold text-slate-900 mb-2">How should I use this if I only want to make $100 a day with DoorDash?</h3>
                                <p class="text-slate-600">Use the hours-to-$100 column first, then open the matching $100/day path for your closest city. A lower-ranked nearby market can beat a top-ranked distant market once commute miles and zone access are counted.</p>
                            </div>
                        @endif
                    </div>
                </section>
            </div>
        </div>

        <!-- PR8-5: ItemList Structured Data for Rankings -->
        <script type="application/ld+json">
        $unsafe{itemListJsonLd}
        </script>
        <script type="application/ld+json">
        $unsafe{faqJsonLd}
        </script>
    `
)
