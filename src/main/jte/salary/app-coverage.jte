@import com.gigwager.model.CityData
@import com.gigwager.model.SeoMeta
@import java.util.List
@import java.util.Map

@param String app
@param String appName
@param String lastUpdated
@param int coveredCityCount
@param Map<String, List<CityData>> coverageByRegion
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
                        Where You Can Drive for ${appName} in the US
                    </h1>
                    <p class="text-xl text-slate-600 max-w-3xl mx-auto">
                        Use ${appName}'s official driver city directory to confirm coverage, then compare take-home pay in our covered markets. Updated ${lastUpdated}.
                    </p>
                </div>

                <div class="grid md:grid-cols-3 gap-4 mb-10">
                    <div class="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
                        <p class="text-sm font-semibold uppercase tracking-wide text-slate-500 mb-2">Source of truth</p>
                        <p class="text-2xl font-black text-slate-900 mb-2">Official Uber city directory</p>
                        <p class="text-sm text-slate-600">Coverage can change by city and metro area, so confirm availability there first.</p>
                    </div>
                    <div class="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
                        <p class="text-sm font-semibold uppercase tracking-wide text-slate-500 mb-2">Covered reports</p>
                        <p class="text-2xl font-black text-emerald-600 mb-2">${coveredCityCount} city pages</p>
                        <p class="text-sm text-slate-600">Each report estimates net hourly pay after mileage, fuel, and self-employment tax assumptions.</p>
                    </div>
                    <div class="bg-white rounded-2xl border border-slate-200 p-6 shadow-sm">
                        <p class="text-sm font-semibold uppercase tracking-wide text-slate-500 mb-2">Best next step</p>
                        <p class="text-2xl font-black text-slate-900 mb-2">Verify first, compare second</p>
                        <p class="text-sm text-slate-600">Check official availability, then open your city report or the earnings ranking.</p>
                    </div>
                </div>

                <div class="grid lg:grid-cols-2 gap-6 mb-10">
                    <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                        <h2 class="text-2xl font-bold text-slate-900 mb-4">Official coverage check</h2>
                        <p class="text-slate-600 mb-4">
                            If your question is "Is Uber active in my city?" this is the first place to check. Uber maintains its own driver city directory for the United States.
                        </p>
                        <ul class="list-disc pl-5 text-slate-600 space-y-2 mb-6">
                            <li>Use the official directory to verify the city or metro where you want to drive.</li>
                            <li>Check any local onboarding rules or city-specific driver requirements there.</li>
                            <li>Then come back here to compare pay and market tradeoffs.</li>
                        </ul>
                        <a href="https://www.uber.com/us/en/e/drive/cities/" target="_blank" rel="noopener noreferrer"
                           onclick="gtag('event', 'cta_click', { 'event_category': 'seo_coverage_hub', 'event_label': 'open_official_uber_directory' })"
                           class="inline-flex items-center justify-center px-6 py-3 bg-slate-900 hover:bg-slate-800 text-white font-bold rounded-xl transition-colors">
                            Open Uber's official city directory
                        </a>
                    </section>

                    <section class="bg-white rounded-3xl border border-emerald-200 p-8 shadow-sm">
                        <h2 class="text-2xl font-bold text-slate-900 mb-4">What GigVerdict adds</h2>
                        <p class="text-slate-600 mb-4">
                            The official directory answers availability. Our pages answer the next question: "What might I actually keep after expenses in that market?"
                        </p>
                        <div class="space-y-3">
                            <a href="/salary/${app}" onclick="gtag('event', 'cta_click', { 'event_category': 'seo_coverage_hub', 'event_label': 'open_uber_pay_reports' })" class="flex items-center justify-between rounded-2xl border border-slate-200 px-5 py-4 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                                <span>
                                    <strong class="block text-slate-900">All ${appName} pay reports</strong>
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
                                    <strong class="block text-slate-900">Full city directory</strong>
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
                            <p class="font-semibold">Confirm availability in Uber's own city directory.</p>
                        </div>
                        <div class="rounded-2xl bg-white/10 p-5 border border-white/10">
                            <p class="text-xs uppercase tracking-[0.2em] text-slate-300 mb-2">Step 2</p>
                            <p class="font-semibold">Open the matching GigVerdict city report for your market.</p>
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
                            <p class="text-slate-600 mt-2">These are the city pay reports we currently publish. Availability still needs to be verified in the official Uber directory.</p>
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
                                                <span class="text-sm text-slate-600">Open estimated pay report</span>
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
                            <h3 class="text-xl font-bold text-slate-900 mb-2">Is this the official Uber coverage list?</h3>
                            <p class="text-slate-600">No. This page is a guided hub. Use Uber's official city directory to verify whether Uber is active in your target city or metro area.</p>
                        </div>
                        <div>
                            <h3 class="text-xl font-bold text-slate-900 mb-2">Does GigVerdict cover every city where Uber operates?</h3>
                            <p class="text-slate-600">No. We currently publish ${coveredCityCount} city pay reports. Our coverage focuses on major U.S. markets where we can offer a useful earnings estimate.</p>
                        </div>
                        <div>
                            <h3 class="text-xl font-bold text-slate-900 mb-2">What should I read after I confirm my city is active?</h3>
                            <p class="text-slate-600">Open the matching city pay report first. If you are comparing multiple markets, use the best-cities ranking to see which locations produce stronger estimated net pay.</p>
                        </div>
                    </div>
                </section>

                <div class="text-center">
                    <a href="/salary/${app}" onclick="gtag('event', 'cta_click', { 'event_category': 'seo_coverage_hub', 'event_label': 'browse_all_uber_pay_reports_footer' })" class="inline-flex items-center justify-center px-8 py-4 bg-emerald-600 hover:bg-emerald-700 text-white font-bold rounded-xl shadow-lg shadow-emerald-600/20 transition-colors">
                        Browse all ${appName} pay reports
                    </a>
                </div>
            </div>
        </div>

        <script type="application/ld+json">
        $unsafe{coverageFaqJsonLd}
        </script>
    `
)
