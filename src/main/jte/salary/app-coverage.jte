@import com.gigwager.model.CityData
@import com.gigwager.model.SeoMeta
@import java.util.List
@import java.util.Map

@param String app
@param String appName
@param String coverageVerb = "Drive"
@param String lastUpdated
@param int coveredCityCount
@param Map<String, List<CityData>> coverageByRegion
@param String officialSourceName = ""
@param String officialSourceSummary = ""
@param String officialSourceUrl = ""
@param String officialSourceCtaLabel = ""
@param String coverageFaqJsonLd = "{}"
@param SeoMeta seoMeta
@param Boolean noIndex = false

@template.layout.main(
    seoMeta = seoMeta,
    noIndex = noIndex,
    content = @`
        <div class="bg-slate-50 min-h-screen py-16 px-6">
            <div class="max-w-6xl mx-auto">
                <div class="text-center mb-10">
                    <span class="inline-flex items-center gap-2 px-4 py-1 rounded-full bg-sky-100 text-sky-800 font-bold text-sm tracking-wide uppercase mb-4">
                        Coverage Guide
                    </span>
                    <h1 class="text-4xl lg:text-5xl font-black text-slate-900 mb-6 tracking-tight">
                        Where You Can ${coverageVerb} for ${appName} in the US
                    </h1>
                    <p class="text-xl text-slate-600 max-w-3xl mx-auto">
                        ${officialSourceSummary} Then compare take-home pay in our covered markets. Updated ${lastUpdated}.
                    </p>
                </div>

                <div class="grid md:grid-cols-3 gap-4 mb-10">
                    <div class="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
                        <p class="text-sm font-semibold uppercase tracking-wide text-slate-500 mb-2">Source of truth</p>
                        <p class="text-2xl font-black text-slate-900 mb-2">${officialSourceName}</p>
                        <p class="text-sm text-slate-600">${officialSourceSummary}</p>
                    </div>
                    <div class="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
                        <p class="text-sm font-semibold uppercase tracking-wide text-slate-500 mb-2">Covered calculators</p>
                        <p class="text-2xl font-black text-emerald-600 mb-2">${coveredCityCount} city pages</p>
                        <p class="text-sm text-slate-600">Each city calculator estimates net hourly pay after mileage, fuel, and self-employment tax assumptions.</p>
                    </div>
                    <div class="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
                        <p class="text-sm font-semibold uppercase tracking-wide text-slate-500 mb-2">Best next step</p>
                        <p class="text-2xl font-black text-slate-900 mb-2">Verify first, compare second</p>
                        <p class="text-sm text-slate-600">Check official availability, then open your city calculator or the net pay ranking.</p>
                    </div>
                </div>

                <div class="grid lg:grid-cols-2 gap-6 mb-10">
                    <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                        <h2 class="text-2xl font-bold text-slate-900 mb-4">Official coverage check</h2>
                        <p class="text-slate-600 mb-4">
                            If your question is "Can I ${coverageVerb.toLowerCase()} for ${appName} in my city?" this is the first place to check.
                        </p>
                        <ul class="list-disc pl-5 text-slate-600 space-y-2 mb-6">
                            <li>Use the official source to verify the city or metro where you want to work.</li>
                            <li>Check any local onboarding rules or city-specific driver requirements there.</li>
                            <li>Then come back here to compare pay and market tradeoffs.</li>
                        </ul>
                        <a href="${officialSourceUrl}" target="_blank" rel="noopener noreferrer"
                           onclick="gtag('event', 'cta_click', { 'event_category': 'seo_coverage_hub', 'event_label': 'open_official_${app}_coverage_source' })"
                           class="inline-flex items-center justify-center px-6 py-3 bg-slate-900 hover:bg-slate-800 text-white font-bold rounded-xl transition-colors">
                            ${officialSourceCtaLabel}
                        </a>
                    </section>

                    <section class="bg-white rounded-3xl border border-emerald-200 p-8 shadow-sm">
                        <h2 class="text-2xl font-bold text-slate-900 mb-4">What GigVerdict adds</h2>
                        <p class="text-slate-600 mb-4">
                            The official source answers availability. Our pages answer the next question: "What might I actually keep after expenses in that market?"
                        </p>
                        <div class="space-y-3">
                            <a href="/salary/${app}" onclick="gtag('event', 'cta_click', { 'event_category': 'seo_coverage_hub', 'event_label': 'open_${app}_pay_reports' })" class="flex items-center justify-between rounded-2xl border border-slate-200 px-5 py-4 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                                <span>
                                    <strong class="block text-slate-900">All ${appName} city calculators</strong>
                                    <span class="text-sm text-slate-600">Browse our city hub for estimated net hourly pay.</span>
                                </span>
                                <span class="font-bold text-emerald-700">Open</span>
                            </a>
                            <a href="/best-cities/${app}" onclick="gtag('event', 'cta_click', { 'event_category': 'seo_coverage_hub', 'event_label': 'open_highest_paying_cities' })" class="flex items-center justify-between rounded-2xl border border-slate-200 px-5 py-4 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                                <span>
                                    <strong class="block text-slate-900">Highest-paying city ranking</strong>
                                    <span class="text-sm text-slate-600">See which cities rank highest on our net earnings model.</span>
                                </span>
                                <span class="font-bold text-emerald-700">Compare</span>
                            </a>
                            <a href="/salary/directory" onclick="gtag('event', 'cta_click', { 'event_category': 'seo_coverage_hub', 'event_label': 'open_full_city_directory' })" class="flex items-center justify-between rounded-2xl border border-slate-200 px-5 py-4 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                                <span>
                                    <strong class="block text-slate-900">Full city calculator directory</strong>
                                    <span class="text-sm text-slate-600">Jump straight to any covered market across both apps.</span>
                                </span>
                                <span class="font-bold text-emerald-700">Browse</span>
                            </a>
                        </div>
                    </section>
                </div>

                <section class="bg-slate-900 rounded-3xl p-8 lg:p-10 text-white mb-10">
                    <h2 class="text-2xl font-bold mb-4">How to use this page</h2>
                    <div class="grid md:grid-cols-3 gap-4">
                        <div class="rounded-2xl bg-white/10 p-5 border border-white/10">
                            <p class="text-xs uppercase tracking-[0.2em] text-slate-300 mb-2">Step 1</p>
                            <p class="font-semibold">Confirm availability in the official ${appName} flow first.</p>
                        </div>
                        <div class="rounded-2xl bg-white/10 p-5 border border-white/10">
                            <p class="text-xs uppercase tracking-[0.2em] text-slate-300 mb-2">Step 2</p>
                            <p class="font-semibold">Open the matching GigVerdict city pay calculator for your market.</p>
                        </div>
                        <div class="rounded-2xl bg-white/10 p-5 border border-white/10">
                            <p class="text-xs uppercase tracking-[0.2em] text-slate-300 mb-2">Step 3</p>
                            <p class="font-semibold">Compare net pay, taxes, and mileage assumptions before you commit.</p>
                        </div>
                    </div>
                </section>

                <section class="mb-10">
                    <div class="flex items-end justify-between gap-4 mb-6">
                        <div>
                            <h2 class="text-3xl font-black text-slate-900">Covered ${appName} markets by region</h2>
                            <p class="text-slate-600 mt-2">These are the city pay calculators we currently publish. Availability still needs to be verified in the official ${appName} flow.</p>
                        </div>
                    </div>
                    <div class="grid lg:grid-cols-2 gap-6">
                        @for (Map.Entry<String, List<CityData>> regionEntry : coverageByRegion.entrySet())
                            @if (!regionEntry.getValue().isEmpty())
                                <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                                    <div class="flex items-center justify-between gap-4 mb-5">
                                        <h3 class="text-2xl font-bold text-slate-900">${regionEntry.getKey()}</h3>
                                        <span class="text-sm font-semibold text-slate-500">${regionEntry.getValue().size()} markets</span>
                                    </div>
                                    <div class="grid sm:grid-cols-2 gap-3">
                                        @for (CityData city : regionEntry.getValue())
                                            <a href="/salary/${app}/${city.getSlug()}"
                                               class="rounded-2xl border border-slate-200 px-4 py-3 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                                                <span class="block font-semibold text-slate-900">${city.getCityName()}, ${city.getState()}</span>
                                                <span class="text-sm text-slate-600">Open estimated pay calculator</span>
                                            </a>
                                        @endfor
                                    </div>
                                </section>
                            @endif
                        @endfor
                    </div>
                </section>

                <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm mb-10">
                    <h2 class="text-3xl font-black text-slate-900 mb-6">Frequently Asked Questions</h2>
                    <div class="space-y-6">
                        <div>
                            <h3 class="text-xl font-bold text-slate-900 mb-2">Is this the official ${appName} coverage list?</h3>
                            <p class="text-slate-600">No. This page is a guided hub. Use the official ${appName} flow to verify whether ${appName} is active in your target city or metro area.</p>
                        </div>
                        <div>
                            <h3 class="text-xl font-bold text-slate-900 mb-2">Does GigVerdict cover every city where ${appName} operates?</h3>
                            <p class="text-slate-600">No. We currently publish ${coveredCityCount} city pay calculators. Our coverage focuses on major U.S. markets where we can offer a useful earnings estimate.</p>
                        </div>
                        <div>
                            <h3 class="text-xl font-bold text-slate-900 mb-2">What should I read after I confirm my city is active?</h3>
                            <p class="text-slate-600">Open the matching city pay calculator first. If you are comparing multiple markets, use the best-cities ranking to see which locations produce stronger estimated net pay.</p>
                        </div>
                    </div>
                </section>

                <div class="text-center">
                    <a href="/salary/${app}" onclick="gtag('event', 'cta_click', { 'event_category': 'seo_coverage_hub', 'event_label': 'browse_all_${app}_pay_reports_footer' })" class="inline-flex items-center justify-center px-8 py-4 bg-emerald-600 hover:bg-emerald-700 text-white font-bold rounded-xl shadow-lg shadow-emerald-600/20 transition-colors">
                        Browse all ${appName} city calculators
                    </a>
                </div>
            </div>
        </div>

        <script type="application/ld+json">
        $unsafe{coverageFaqJsonLd}
        </script>
    `
)
