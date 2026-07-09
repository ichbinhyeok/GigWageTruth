@import com.gigwager.model.CityData
@import com.gigwager.model.CityIntentEvidence
@import com.gigwager.model.CityIntentMetric
@import com.gigwager.model.CityIntentPage
@import com.gigwager.model.CityScenario
@import com.gigwager.model.DriverFieldNote
@import com.gigwager.model.DriverShiftReport
@import com.gigwager.model.SearchResultPattern
@import com.gigwager.model.SeoMeta
@import java.util.List

@param SeoMeta seoMeta
@param String app
@param String appName
@param CityData city
@param CityIntentPage intentPage
@param CityScenario scenario
@param String lastUpdated
@param String parentPageUrl
@param String calculatorUrl
@param String bestCitiesUrl
@param String answerHtml
@param String cityIntentJsonLd = "{}"
@param List<DriverFieldNote> driverFieldNotes = java.util.List.of()
@param List<DriverShiftReport> driverShiftReports = java.util.List.of()
@param List<CityIntentMetric> intentMetrics = java.util.List.of()
@param List<CityIntentEvidence> intentEvidencePatterns = java.util.List.of()
@param List<SearchResultPattern> searchResultPatterns = java.util.List.of()
@param Boolean noIndex = false

@template.layout.main(
    noIndex = noIndex,
    seoMeta = seoMeta,
    content = @`
        <script type="application/ld+json">
        $unsafe{cityIntentJsonLd}
        </script>

        <div class="bg-slate-950 text-white py-16 px-6">
            <div class="max-w-4xl mx-auto">
                <nav class="text-sm text-slate-400 mb-6 flex items-center gap-2 flex-wrap">
                    <a href="/" class="hover:text-emerald-300">Home</a>
                    <span>&gt;</span>
                    <a href="/salary/directory" class="hover:text-emerald-300">City Earnings</a>
                    <span>&gt;</span>
                    <a href="${parentPageUrl}" class="hover:text-emerald-300">${city.getCityName()} ${appName}</a>
                    <span>&gt;</span>
                    <span class="text-white">${intentPage.getDisplayName()}</span>
                </nav>

                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-3">${city.getCityName()}, ${city.getState()} | Updated ${lastUpdated}</p>
                <h1 class="text-4xl md:text-6xl font-black leading-tight mb-5">
                    @if(intentPage == CityIntentPage.APP_COMPARISON)
                        Uber Eats vs DoorDash in ${city.getCityName()}
                    @else
                        ${appName} ${city.getCityName()} ${intentPage.getDisplayName()}
                    @endif
                    <span class="block text-emerald-300">$${String.format("%.2f", scenario.getNetHourly())}/hr Net</span>
                </h1>
                <p class="text-xl text-slate-300 max-w-3xl">${intentPage.getUserIntent()}</p>
            </div>
        </div>

        <section class="max-w-4xl mx-auto px-6 -mt-8 relative z-10">
            <div class="bg-white rounded-3xl border border-slate-200 shadow-xl overflow-hidden">
                <div class="grid md:grid-cols-4 divide-y md:divide-y-0 md:divide-x divide-slate-100">
                    <div class="p-5">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Net hourly</p>
                        <p class="text-2xl font-black text-slate-900">$${String.format("%.2f", scenario.getNetHourly())}</p>
                    </div>
                    <div class="p-5">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Gross weekly</p>
                        <p class="text-2xl font-black text-slate-900">$${scenario.getGrossWeekly()}</p>
                    </div>
                    <div class="p-5">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Miles/week</p>
                        <p class="text-2xl font-black text-slate-900">${scenario.getMiles()}</p>
                    </div>
                    <div class="p-5">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Hours/week</p>
                        <p class="text-2xl font-black text-slate-900">${scenario.getHours()}</p>
                    </div>
                </div>
            </div>
        </section>

        <main class="max-w-4xl mx-auto px-6 py-12 space-y-10">
            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Direct answer</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">
                    ${appName} ${city.getCityName()} ${intentPage.getSearchPhrase()} estimate
                </h2>
                <div class="prose prose-slate max-w-none text-slate-700">
                    $unsafe{answerHtml}
                </div>
            </section>

            @if(intentMetrics != null && !intentMetrics.isEmpty())
                <section class="bg-slate-900 rounded-3xl border border-slate-800 p-8 text-white shadow-sm">
                    <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-2">Data-backed checks</p>
                    <h2 class="text-3xl font-black mb-5">${intentPage.getDisplayName()} metrics for ${city.getCityName()}</h2>
                    <div class="grid md:grid-cols-3 gap-4">
                        @for(CityIntentMetric metric : intentMetrics)
                            <article class="rounded-2xl bg-white/5 border border-white/10 p-5">
                                <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-400 mb-2">${metric.label()}</p>
                                <p class="text-3xl font-black text-emerald-300 mb-2">${metric.value()}</p>
                                <p class="text-sm text-slate-300 leading-relaxed">${metric.description()}</p>
                            </article>
                        @endfor
                    </div>
                </section>
            @endif

            @if(intentEvidencePatterns != null && !intentEvidencePatterns.isEmpty())
                <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                    <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Evidence patterns</p>
                    <h2 class="text-3xl font-black text-slate-900 mb-3">What the source data says to check</h2>
                    <p class="text-slate-600 max-w-3xl mb-6">
                        These cards turn driver discussions, official pay docs, field tests, and large driver datasets into the specific checks this page is trying to answer.
                    </p>
                    <div class="grid md:grid-cols-3 gap-4">
                        @for(CityIntentEvidence evidence : intentEvidencePatterns)
                            <article class="rounded-2xl border border-slate-200 bg-slate-50 p-5">
                                <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-2">${evidence.label()}</p>
                                <h3 class="text-lg font-bold text-slate-900 mb-2">${evidence.title()}</h3>
                                <p class="text-sm text-slate-600 leading-relaxed mb-4">${evidence.summary()}</p>
                                <a href="${evidence.sourceUrl()}" target="_blank" rel="noopener nofollow" class="text-sm font-bold text-emerald-700 hover:text-emerald-800">
                                    Source: ${evidence.sourceLabel()} &rarr;
                                </a>
                            </article>
                        @endfor
                    </div>
                </section>
            @endif

            @template.components.search_result_patterns(
                appName = appName,
                cityName = city.getCityName(),
                intentLabel = intentPage.getDisplayName(),
                netHourly = scenario.getNetHourly(),
                searchResultPatterns = searchResultPatterns,
                sectionClass = "max-w-4xl mx-auto"
            )

            @template.components.driver_field_notes(
                appName = appName,
                cityName = city.getCityName(),
                netHourly = scenario.getNetHourly(),
                driverFieldNotes = driverFieldNotes,
                sectionClass = "max-w-4xl mx-auto"
            )

            @template.components.driver_shift_reports(
                appName = appName,
                cityName = city.getCityName(),
                modeledNetHourly = scenario.getNetHourly(),
                driverShiftReports = driverShiftReports,
                sectionClass = "max-w-4xl mx-auto"
            )

            @template.components.driver_report_form(
                app = app,
                appName = appName,
                cityName = city.getCityName(),
                citySlug = city.getSlug(),
                sourcePage = "city_intent_" + intentPage.getSlug(),
                sourcePath = "/salary/" + app + "/" + city.getSlug() + "/" + intentPage.getSlug(),
                netHourly = scenario.getNetHourly(),
                sectionClass = "max-w-4xl mx-auto"
            )

            <section class="bg-slate-50 rounded-3xl border border-slate-200 p-8">
                <h2 class="text-2xl font-black text-slate-900 mb-4">Use the next page by intent</h2>
                <div class="grid md:grid-cols-2 gap-3">
                    <a href="${parentPageUrl}" class="rounded-2xl bg-white border border-slate-200 px-5 py-4 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                        <strong class="block text-slate-900">Full ${city.getCityName()} ${appName} earnings page</strong>
                        <span class="text-sm text-slate-600">Compare part-time, side-hustle, and full-time scenarios.</span>
                    </a>
                    <a href="${calculatorUrl}" class="rounded-2xl bg-white border border-slate-200 px-5 py-4 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                        <strong class="block text-slate-900">Adjust the calculator</strong>
                        <span class="text-sm text-slate-600">Change gross, miles, hours, and gas price.</span>
                    </a>
                    <a href="/salary/${app}/${city.getSlug()}/side-hustle" class="rounded-2xl bg-white border border-slate-200 px-5 py-4 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                        <strong class="block text-slate-900">Side-hustle detail</strong>
                        <span class="text-sm text-slate-600">Use the 25-hour weekly baseline for planning.</span>
                    </a>
                    <a href="${bestCitiesUrl}" class="rounded-2xl bg-white border border-slate-200 px-5 py-4 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                        <strong class="block text-slate-900">Compare highest-paying cities</strong>
                        <span class="text-sm text-slate-600">See where this city sits in the ranking.</span>
                    </a>
                </div>
            </section>

            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <h2 class="text-2xl font-black text-slate-900 mb-4">Related ${city.getCityName()} ${appName} questions</h2>
                <div class="grid md:grid-cols-2 gap-3">
                    @for(CityIntentPage relatedIntent : CityIntentPage.values())
                        @if(relatedIntent != intentPage && relatedIntent.isSupportedForApp(app))
                            <a href="/salary/${app}/${city.getSlug()}/${relatedIntent.getSlug()}" class="rounded-2xl border border-slate-200 px-5 py-4 hover:border-emerald-200 hover:bg-emerald-50 transition-colors">
                                <strong class="block text-slate-900">${relatedIntent.getDisplayName()}</strong>
                                <span class="text-sm text-slate-600">${relatedIntent.getUserIntent()}</span>
                            </a>
                        @endif
                    @endfor
                </div>
            </section>
        </main>
    `
)
