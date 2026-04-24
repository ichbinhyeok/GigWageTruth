@import com.gigwager.model.SeoMeta
@import com.gigwager.dto.CityRankingDto
@import java.util.List

@param String app
@param String appName
@param List<CityRankingDto> topCities
@param CityRankingDto topCity
@param Long indexedCityCount
@param String lastUpdated
@param String bestCitiesUrl = ""
@param String calculatorUrl = ""
@param String coverageUrl = ""
@param String directoryUrl = ""
@param String topCityReportUrl = ""
@param String comparisonUrl = ""
@param String comparisonCityName = ""
@param String appHubSchemaJsonLd = "{}"
@param SeoMeta seoMeta
@param Boolean noIndex = false

@template.layout.main(
    seoMeta = seoMeta,
    noIndex = noIndex,
    content = @`
        <div class="bg-slate-50 min-h-screen py-16 px-6">
            <div class="max-w-4xl mx-auto">
                <script type="application/ld+json">
                $unsafe{appHubSchemaJsonLd}
                </script>

                <div class="text-center mb-12">
                    <span class="inline-block px-4 py-1 rounded-full bg-emerald-100 text-emerald-800 font-bold text-sm tracking-wider mb-4 uppercase">
                        ${appName} Hub
                    </span>
                    <h1 class="text-4xl lg:text-5xl font-black text-slate-900 mb-6 tracking-tight">
                        ${appName} City Pay Calculators
                        <span class="block text-slate-900">Net Earnings After Expenses</span>
                    </h1>
                    <p class="text-xl text-slate-600 max-w-2xl mx-auto">
                        Use this hub when you want one place to move between city pay calculators, compare pages, the ranking page, the main calculator, and
                        @if (coverageUrl != null && !coverageUrl.isBlank())
                            the coverage guide.
                        @else
                            the main directory.
                        @endif
                        Every city calculator shows part-time, side-hustle, and full-time scenarios. Last updated: ${lastUpdated}.
                    </p>
                    <div class="flex flex-wrap items-center justify-center gap-3 mt-6">
                        <a href="${bestCitiesUrl}" onclick="gtag('event', 'cta_click', { 'event_category': 'seo_app_hub', 'event_label': 'compare_highest_paying_cities' })" class="inline-flex items-center justify-center px-5 py-3 rounded-xl bg-slate-900 hover:bg-slate-800 text-white font-bold transition-colors">
                            Compare city rankings
                        </a>
                        <a href="${topCityReportUrl}" onclick="gtag('event', 'cta_click', { 'event_category': 'seo_app_hub', 'event_label': 'open_top_city_report' })" class="inline-flex items-center justify-center px-5 py-3 rounded-xl bg-white border border-slate-200 hover:border-emerald-200 hover:bg-emerald-50 text-slate-900 font-bold transition-colors">
                            Open ${topCity.city().getCityName()}
                        </a>
                        @if (comparisonUrl != null && !comparisonUrl.isBlank())
                            <a href="${comparisonUrl}" onclick="gtag('event', 'cta_click', { 'event_category': 'seo_app_hub', 'event_label': 'open_city_compare_page' })" class="inline-flex items-center justify-center px-5 py-3 rounded-xl bg-white border border-slate-200 hover:border-indigo-200 hover:bg-indigo-50 text-slate-900 font-bold transition-colors">
                                Compare in ${comparisonCityName}
                            </a>
                        @endif
                        @if (coverageUrl != null && !coverageUrl.isBlank())
                            <a href="${coverageUrl}" onclick="gtag('event', 'cta_click', { 'event_category': 'seo_app_hub', 'event_label': 'check_official_coverage_guide' })" class="inline-flex items-center justify-center px-5 py-3 rounded-xl bg-white border border-slate-200 hover:border-sky-200 hover:bg-sky-50 text-slate-900 font-bold transition-colors">
                                Check official coverage guide
                            </a>
                        @endif
                    </div>
                </div>

                <div class="bg-sky-50 border border-sky-200 rounded-2xl p-6 mb-12">
                    <h2 class="text-lg font-bold text-sky-950 mb-2">Use this page by intent</h2>
                        <p class="text-sm text-sky-900 leading-relaxed">
                        This hub is for comparing ${appName} calculator baselines across cities. It is not the ranking page,
                        and
                        @if (coverageUrl != null && !coverageUrl.isBlank())
                            it is not an official coverage list.
                        @else
                            it is not an official coverage directory.
                        @endif
                        Open a city calculator when you care about one market, the compare page when you are choosing between apps,
                        the ranking page when you only want the leaders, and the calculator when your own miles or hours differ from the baseline.
                    </p>
                </div>

                <div class="grid md:grid-cols-3 gap-4 mb-12">
                    <div class="bg-white rounded-2xl border border-slate-200 shadow-sm p-6">
                        <p class="text-xs font-bold uppercase tracking-wider text-slate-500 mb-2">Top side-hustle market</p>
                        <h2 class="text-2xl font-black text-slate-900">${topCity.city().getCityName()}</h2>
                        <p class="text-emerald-600 font-bold text-lg mt-1">$${String.format("%.2f", topCity.netHourly())}/hr net</p>
                        <p class="text-sm text-slate-500 mt-3">Current ranking leader after mileage proxy and self-employment tax assumptions.</p>
                    </div>
                    <div class="bg-white rounded-2xl border border-slate-200 shadow-sm p-6">
                        <p class="text-xs font-bold uppercase tracking-wider text-slate-500 mb-2">Calculators live</p>
                        <h2 class="text-2xl font-black text-slate-900">${indexedCityCount}</h2>
                        <p class="text-sm text-slate-500 mt-3">Indexable city calculators with detailed pay scenarios and internal links into the calculator and tax tools.</p>
                    </div>
                    <div class="bg-white rounded-2xl border border-slate-200 shadow-sm p-6">
                        <p class="text-xs font-bold uppercase tracking-wider text-slate-500 mb-2">Included in every city page</p>
                        <h2 class="text-2xl font-black text-slate-900">PT, SH, FT</h2>
                        <p class="text-sm text-slate-500 mt-3">Each city calculator includes part-time, side-hustle, and full-time scenarios instead of one flat payout number.</p>
                    </div>
                </div>

                <div class="grid lg:grid-cols-2 gap-4 mb-12">
                    <a href="${topCityReportUrl}" class="bg-white rounded-2xl border border-slate-200 shadow-sm p-6 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                        <p class="text-xs font-bold uppercase tracking-wider text-slate-500 mb-2">Start here</p>
                        <h2 class="text-xl font-bold text-slate-900 mb-2">Open a city pay calculator</h2>
                        <p class="text-sm text-slate-600">Use the city calculator when you want concrete weekly gross, miles, hours, taxes, and adjustable calculator links for one market.</p>
                    </a>
                    @if (comparisonUrl != null && !comparisonUrl.isBlank())
                        <a href="${comparisonUrl}" class="bg-white rounded-2xl border border-slate-200 shadow-sm p-6 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                            <p class="text-xs font-bold uppercase tracking-wider text-slate-500 mb-2">For app choice</p>
                            <h2 class="text-xl font-bold text-slate-900 mb-2">Compare Uber vs DoorDash in ${comparisonCityName}</h2>
                            <p class="text-sm text-slate-600">Use the compare page when your real question is which app pays more after expenses in one market.</p>
                        </a>
                    @endif
                    <a href="${bestCitiesUrl}" class="bg-white rounded-2xl border border-slate-200 shadow-sm p-6 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                        <p class="text-xs font-bold uppercase tracking-wider text-slate-500 mb-2">For ranking intent</p>
                        <h2 class="text-xl font-bold text-slate-900 mb-2">See the full best-cities list</h2>
                        <p class="text-sm text-slate-600">Use the ranking page when your question is purely which city pay baseline is highest for ${appName} right now.</p>
                    </a>
                    <a href="${calculatorUrl}" class="bg-white rounded-2xl border border-slate-200 shadow-sm p-6 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                        <p class="text-xs font-bold uppercase tracking-wider text-slate-500 mb-2">For your own numbers</p>
                        <h2 class="text-xl font-bold text-slate-900 mb-2">Run the calculator</h2>
                        <p class="text-sm text-slate-600">Adjust gross, miles, hours, and vehicle assumptions if your routine is stronger or weaker than the model baseline.</p>
                    </a>
                    @if (coverageUrl != null && !coverageUrl.isBlank())
                        <a href="${coverageUrl}" class="bg-white rounded-2xl border border-slate-200 shadow-sm p-6 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                            <p class="text-xs font-bold uppercase tracking-wider text-slate-500 mb-2">For coverage intent</p>
                            <h2 class="text-xl font-bold text-slate-900 mb-2">Check the coverage guide</h2>
                            <p class="text-sm text-slate-600">Use the official city directory flow before assuming a market is active, then come back here for calculator-based pay analysis.</p>
                        </a>
                    @endif
                </div>

                <div class="bg-white rounded-3xl p-8 lg:p-12 border border-slate-200 shadow-sm mb-12">
                    <h2 class="text-2xl font-bold text-slate-900 mb-4">What "Net Hourly After Expenses" means here</h2>
                    <div class="prose prose-slate max-w-none text-slate-600">
                        <p>Gross payout does not tell you whether a market is actually worth working. <strong>Net hourly pay</strong> is the take-home estimate after mileage-based operating costs and self-employment tax assumptions.</p>
                        <p>This hub is intentionally sorted by the <strong>side-hustle</strong> scenario because that matches the most common "pay by city" query intent. If you open a city calculator, you will also see part-time and full-time scenarios so you can judge whether the market still works at a different pace.</p>
                        <h3 class="text-xl font-bold mt-6 mb-2">How the model works:</h3>
                        <ul class="list-disc pl-5 mt-2 space-y-2">
                            <li><strong>Gross pay:</strong> Estimated from local market tiers, traffic friction, and wage proxies.</li>
                            <li><strong>Operating cost proxy:</strong> We deduct the 2026 IRS standard mileage rate of $0.725 per mile as a stand-in for gas, maintenance, and depreciation.</li>
                            <li><strong>Tax drag:</strong> We deduct an estimated 15.3% self-employment tax from taxable profit.</li>
                        </ul>
                        <p class="mt-4 italic text-sm text-slate-500">These are planning estimates built for decision-making, not tax filing or guarantees of platform earnings.</p>
                    </div>
                </div>

                <div class="bg-white rounded-3xl overflow-hidden border border-slate-200 shadow-sm mb-8">
                    <div class="bg-slate-900 p-6 flex justify-between items-center">
                        <div>
                            <h2 class="text-xl font-bold text-white">Top 10 cities for ${appName} side-hustle pay</h2>
                            <p class="text-sm text-slate-300 mt-1">Use this as a quick shortlist, then open the city calculator for the deeper scenario table.</p>
                        </div>
                        <a href="${bestCitiesUrl}" class="text-emerald-400 hover:text-emerald-300 font-bold text-sm">See full net pay ranking &rarr;</a>
                    </div>
                    <div class="overflow-x-auto">
                        <table class="w-full text-left border-collapse">
                            <thead>
                                <tr class="bg-slate-50 border-b border-slate-200">
                                    <th class="p-4 font-bold text-slate-700">City</th>
                                    <th class="p-4 font-bold text-slate-700">Net Hourly</th>
                                    <th class="p-4 font-bold text-slate-700">Work Level</th>
                                    <th class="p-4 font-bold text-slate-700 text-right">Details</th>
                                </tr>
                            </thead>
                            <tbody>
                                @for(CityRankingDto ranking : topCities)
                                    <tr class="border-b border-slate-100 hover:bg-slate-50 transition-colors">
                                        <td class="p-4 font-medium text-slate-900">${ranking.city().getCityName()}</td>
                                        <td class="p-4 font-bold text-emerald-600">$${String.format("%.2f", ranking.netHourly())}/hr</td>
                                        <td class="p-4 text-slate-600">${ranking.workLevel()}</td>
                                        <td class="p-4 text-right">
                                            <a href="/salary/${app}/${ranking.city().getSlug()}" class="inline-block px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold rounded-lg text-sm transition-colors">Open Calculator</a>
                                        </td>
                                    </tr>
                                @endfor
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="bg-white rounded-3xl p-8 lg:p-10 border border-slate-200 shadow-sm mt-12">
                    <h3 class="text-2xl font-bold text-slate-900 mb-6">Choose the next page by intent</h3>
                    <div class="grid md:grid-cols-2 gap-4">
                        <a href="${directoryUrl}" class="rounded-2xl border border-slate-200 px-5 py-4 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                            <p class="font-bold text-slate-900">Full city calculator directory</p>
                            <p class="text-sm text-slate-600 mt-1">Browse every indexed market, not just the current leaders.</p>
                        </a>
                        @if (comparisonUrl != null && !comparisonUrl.isBlank())
                            <a href="${comparisonUrl}" class="rounded-2xl border border-slate-200 px-5 py-4 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                                <p class="font-bold text-slate-900">Compare apps in ${comparisonCityName}</p>
                                <p class="text-sm text-slate-600 mt-1">Check the side-by-side winner before you go deeper into single-app city pages.</p>
                            </a>
                        @endif
                        <a href="${bestCitiesUrl}" class="rounded-2xl border border-slate-200 px-5 py-4 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                            <p class="font-bold text-slate-900">Best-cities ranking</p>
                            <p class="text-sm text-slate-600 mt-1">Stay in ranking mode if you are comparing markets for a move or a trip.</p>
                        </a>
                        <a href="${calculatorUrl}" class="rounded-2xl border border-slate-200 px-5 py-4 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                            <p class="font-bold text-slate-900">Calculator</p>
                            <p class="text-sm text-slate-600 mt-1">Plug in your own mileage and hours when the baseline feels too optimistic or too conservative.</p>
                        </a>
                        @if (coverageUrl != null && !coverageUrl.isBlank())
                            <a href="${coverageUrl}" class="rounded-2xl border border-slate-200 px-5 py-4 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                                <p class="font-bold text-slate-900">Official coverage guide</p>
                                <p class="text-sm text-slate-600 mt-1">Confirm that Uber is active in the metro before you optimize around pay.</p>
                            </a>
                        @endif
                    </div>
                </div>

                <div class="bg-white rounded-3xl p-8 lg:p-10 border border-slate-200 shadow-sm mt-12">
                    <h3 class="text-2xl font-bold text-slate-900 mb-6">Frequently Asked Questions</h3>
                    <div class="space-y-4">
                        <details class="group border border-slate-200 rounded-2xl p-4">
                            <summary class="font-semibold text-slate-900 cursor-pointer">How do ${appName} city pay calculators compare right now?</summary>
                            <p class="text-slate-600 mt-3">In the current side-hustle model, ${topCity.city().getCityName()} leads this hub at about $${String.format("%.2f", topCity.netHourly())}/hr net. That is a directional estimate after mileage proxy and self-employment tax assumptions, not a guarantee of live app payouts.</p>
                        </details>
                        <details class="group border border-slate-200 rounded-2xl p-4">
                            <summary class="font-semibold text-slate-900 cursor-pointer">Why is the table sorted by side-hustle instead of full-time?</summary>
                            <p class="text-slate-600 mt-3">Most "pay by city" searches are really asking which market looks strongest at a typical weekly pace. Side-hustle is the cleanest baseline for that comparison. Each city calculator still includes part-time and full-time scenarios if your routine is heavier.</p>
                        </details>
                        <details class="group border border-slate-200 rounded-2xl p-4">
                            <summary class="font-semibold text-slate-900 cursor-pointer">What should I click after this page?</summary>
                            <p class="text-slate-600 mt-3">Open a city calculator when you care about one market, the best-cities page when you only want rankings, and the main calculator when your mileage or hours differ from the default model. This hub currently links ${indexedCityCount} indexable city calculators.</p>
                        </details>
                    </div>
                </div>
            </div>
        </div>
    `
)
