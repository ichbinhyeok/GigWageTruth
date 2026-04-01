@import com.gigwager.model.SeoMeta
@import com.gigwager.dto.CityRankingDto
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
@param String topThreeCities = ""
@param int currentYear = 2026
@param int rankedCityCount = 0
@param double leadOverRunnerUp = 0
@param CityRankingDto runnerUpCity

@template.layout.main(
    seoMeta = seoMeta,
    noIndex = noIndex,
    content = @`
        <div class="bg-slate-50 min-h-screen py-16 px-6">
            <div class="max-w-4xl mx-auto">
                <div class="text-center mb-12">
                    <h1 class="text-4xl lg:text-5xl font-black text-slate-900 mb-6 tracking-tight">
                        Highest-Paying Cities for ${appName} Drivers in ${currentYear}
                    </h1>
                    <p class="text-xl text-slate-600 max-w-2xl mx-auto">
                        ${topRankedCity.city().getCityName()} currently leads our after-expenses model at about <strong>$${String.format("%.2f", topRankedCity.netHourly())}/hr net</strong>. Top markets right now: <strong>${topThreeCities}</strong>.
                    </p>
                    <p class="text-sm text-slate-500 max-w-2xl mx-auto mt-3">
                        These rankings estimate take-home pay after mileage and self-employment tax assumptions. Updated ${lastUpdated}.
                    </p>
                </div>

                <div class="grid sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
                    <div class="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm text-left">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500 mb-2">#1 City</p>
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
                        <p class="text-sm text-slate-600 mt-2">Every market links to a deeper city earnings report.</p>
                    </div>
                </div>

                <div class="bg-white border border-slate-200 rounded-2xl p-5 mb-8">
                    <p class="text-sm text-slate-600">
                        Use this page when your question is <strong>which cities pay the most for ${appName}</strong>.
                        If your real question is <strong>whether a market is active</strong>, use the coverage guide first and then come back here for the earnings ranking.
                    </p>
                </div>

                <div class="grid md:grid-cols-3 gap-4 mb-8">
                    <a href="${appHubUrl}" class="block bg-white p-6 rounded-xl border border-slate-200 shadow-sm hover:border-emerald-200 hover:bg-emerald-50/40 transition-colors text-left">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-600 mb-2">Need More Context?</p>
                        <h2 class="text-xl font-black text-slate-900 mb-2">${appName} Earnings Hub</h2>
                        <p class="text-sm text-slate-600">Open the market hub for full city reports, calculator paths, and broader pay-by-city context.</p>
                    </a>
                    <a href="${topCityReportUrl}" class="block bg-white p-6 rounded-xl border border-slate-200 shadow-sm hover:border-amber-200 hover:bg-amber-50/50 transition-colors text-left">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-amber-600 mb-2">Want The Leader?</p>
                        <h2 class="text-xl font-black text-slate-900 mb-2">${topRankedCity.city().getCityName()} Full Report</h2>
                        <p class="text-sm text-slate-600">Jump straight into the current top-ranked ${appName} city report for the local pay and market breakdown.</p>
                    </a>
                    <a href="${coverageUrl}" class="block bg-white p-6 rounded-xl border border-slate-200 shadow-sm hover:border-sky-200 hover:bg-sky-50/50 transition-colors text-left">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-sky-600 mb-2">Need Coverage First?</p>
                        <h2 class="text-xl font-black text-slate-900 mb-2">${coverageGuideTitle}</h2>
                        <p class="text-sm text-slate-600">${coverageGuideDescription}</p>
                    </a>
                </div>

                <div class="bg-blue-50 p-6 rounded-xl border border-blue-200 text-left mb-8">
                    <h2 class="text-blue-900 font-bold mb-2">How This After-Expenses Ranking Works</h2>
                    <ul class="list-disc pl-5 text-blue-800 text-sm space-y-1">
                        <li><strong>Ranking Criteria:</strong> Sorts strictly by estimated Net Hourly Pay for a "side-hustle" driver.</li>
                        <li><strong>Input Weights:</strong> We apply a traffic multiplier and a market tier multiplier (demand baseline) to estimate gross hourly revenue.</li>
                        <li><strong>Deductions:</strong> We apply the @template.components.irs_rate_snippet() mileage proxy plus an assumed 15.3% SE tax. Local gas prices are shown as market context, not a direct variable in this ranking formula.</li>
                        <li><strong>Answer first:</strong> This page is designed to answer which city pays the most, not whether a market is active.</li>
                        <li><strong>Need coverage instead?</strong> Use the <a href="${coverageUrl}" class="font-bold underline hover:text-blue-900">${coverageGuideTitle}</a> before assuming a market is active.</li>
                    </ul>
                </div>

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

                <div class="bg-white rounded-3xl overflow-hidden border border-slate-200 shadow-sm">
                    <div class="overflow-x-auto">
                        <table class="w-full text-left border-collapse">
                            <thead>
                                <tr class="bg-slate-900 text-white border-b border-slate-700">
                                    <th class="p-4 font-bold w-16 text-center">Rank</th>
                                    <th class="p-4 font-bold">City</th>
                                    <th class="p-4 font-bold">Net Hourly Pay</th>
                                    <th class="p-4 font-bold">Work Level</th>
                                    <th class="p-4 font-bold text-right">Full Report</th>
                                </tr>
                            </thead>
                            <tbody>
                                @for(int i = 0; i < rankedCities.size(); i++)
                                    !{CityRankingDto ranking = rankedCities.get(i);}
                                    <tr class="border-b border-slate-100 hover:bg-slate-50 transition-colors">
                                        <td class="p-4 font-bold text-center text-slate-400">#${i + 1}</td>
                                        <td class="p-4 font-bold text-slate-900">${ranking.city().getCityName()}</td>
                                        <td class="p-4 font-black text-emerald-600">$${String.format("%.2f", ranking.netHourly())}/hr</td>
                                        <td class="p-4 text-slate-600 text-sm">${ranking.workLevel()}</td>
                                        <td class="p-4 text-right">
                                            <a href="/salary/${app}/${ranking.city().getSlug()}" class="inline-block px-4 py-2 bg-slate-100 hover:bg-emerald-50 text-emerald-700 font-bold rounded-lg text-sm transition-colors border border-slate-200 hover:border-emerald-200">View City</a>
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
                            <p class="text-slate-600">This page is an earnings ranking. Use the <a href="${coverageUrl}" class="font-bold underline hover:text-slate-900">${coverageGuideTitle}</a> when your question is whether a market is active.</p>
                        </div>
                        <div>
                            <h3 class="text-xl font-bold text-slate-900 mb-2">How many ${appName} city reports are ranked here?</h3>
                            <p class="text-slate-600">GigVerdict currently ranks ${rankedCityCount} city reports here, and every row links to a deeper city page with calculator and tax paths.</p>
                        </div>
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
