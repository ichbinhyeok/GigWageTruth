@import com.gigwager.model.CityData
@import com.gigwager.model.CityIntentEvidence
@import com.gigwager.model.CityIntentMetric
@import com.gigwager.model.CityIntentPage
@import com.gigwager.model.CityScenario
@import com.gigwager.model.DriverFieldNote
@import com.gigwager.model.DriverShiftReport
@import com.gigwager.model.PageEvidenceProfile
@import com.gigwager.model.SearchResultPattern
@import com.gigwager.model.SeoMeta
@import com.gigwager.model.content.CityRichContent
@import com.gigwager.model.content.PersonaQuote
@import com.gigwager.model.content.SourceCitation
@import com.gigwager.model.content.WorkLevelRichContent
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
@param PageEvidenceProfile pageEvidenceProfile = null
@param CityRichContent localRichContent = null
@param WorkLevelRichContent localSideHustleContent = null
@param double daily100Hours = 0
@param int daily100Miles = 0
@param int daily100Gross = 0
@param double daily200Hours = 0
@param int daily200Miles = 0
@param int daily200Gross = 0
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
                    @elseif(intentPage == CityIntentPage.NIGHTS_WEEKENDS && app.equals("doordash"))
                        Best Hours to DoorDash in ${city.getCityName()}
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

        @template.components.page_evidence_profile(
            pageEvidenceProfile = pageEvidenceProfile,
            sectionClass = "max-w-4xl mx-auto px-6 mt-8 mb-0"
        )

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

            @if(localRichContent != null && localRichContent.seo() != null && localSideHustleContent != null)
                <section class="bg-emerald-50 rounded-3xl border border-emerald-100 p-8 shadow-sm">
                    <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Local operating context</p>
                    <h2 class="text-3xl font-black text-slate-900 mb-4">
                        What changes the ${intentPage.getDisplayName().toLowerCase()} math in ${city.getCityName()}
                    </h2>
                    <p class="text-lg leading-relaxed text-slate-800 mb-4">${localRichContent.seo().heroHook()}</p>
                    <p class="leading-relaxed text-slate-700 mb-6">${localSideHustleContent.localStrategyText()}</p>
                    @if(localSideHustleContent.painPoints() != null && !localSideHustleContent.painPoints().isEmpty())
                        <div class="grid md:grid-cols-3 gap-3 mb-6">
                            @for(String painPoint : localSideHustleContent.painPoints())
                                <div class="bg-white rounded-2xl border border-emerald-100 px-5 py-4 text-sm font-semibold text-slate-700">
                                    ${painPoint}
                                </div>
                            @endfor
                        </div>
                    @endif
                    @if(localSideHustleContent.personaQuotes() != null && !localSideHustleContent.personaQuotes().isEmpty())
                        <div class="grid md:grid-cols-2 gap-4 mb-6">
                            @for(PersonaQuote quote : localSideHustleContent.personaQuotes())
                                <blockquote class="bg-slate-950 text-white rounded-2xl px-5 py-5">
                                    <p class="leading-relaxed text-slate-200 mb-3">&ldquo;${quote.quote()}&rdquo;</p>
                                    <footer class="text-xs font-bold uppercase tracking-[0.14em] text-emerald-300">
                                        ${quote.displayName()} | ${quote.attributionType().name().toLowerCase().replace('_', ' ')}
                                    </footer>
                                </blockquote>
                            @endfor
                        </div>
                    @endif
                    @if(localRichContent.seo().sources() != null && !localRichContent.seo().sources().isEmpty())
                        <div class="border-t border-emerald-200 pt-5">
                            <p class="text-xs font-black uppercase tracking-[0.16em] text-emerald-800 mb-3">Local sources checked</p>
                            <div class="flex flex-wrap gap-x-5 gap-y-2 text-sm">
                                @for(SourceCitation source : localRichContent.seo().sources())
                                    <a href="${source.url()}" rel="noopener noreferrer" target="_blank" class="font-semibold text-emerald-800 underline decoration-emerald-300 underline-offset-4 hover:text-emerald-950">
                                        ${source.publisher()}: ${source.title()}
                                    </a>
                                @endfor
                            </div>
                        </div>
                    @endif
                </section>
            @endif

            <section class="bg-white rounded-3xl border border-slate-200 overflow-hidden shadow-sm">
                <div class="bg-slate-900 px-6 py-4">
                    <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-2">Goal math</p>
                    <h2 class="text-2xl font-black text-white">${city.getCityName()} ${appName} target calculator</h2>
                    <p class="text-sm text-slate-300 mt-1">The useful answer is not only pay per hour. It is how many real hours and miles the target requires.</p>
                </div>
                <div class="overflow-x-auto">
                    <table class="w-full min-w-[48rem] text-sm">
                        <thead>
                            <tr class="bg-slate-50 border-b border-slate-200">
                                <th scope="col" class="px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase">Target</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Modeled hours</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Modeled miles</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Gross needed</th>
                                <th scope="col" class="px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase">Best use</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-100">
                            <tr>
                                <td class="px-4 py-4 font-bold text-slate-900">$100 net day</td>
                                <td class="px-4 py-4 text-right text-slate-700">${String.format("%.1f", daily100Hours)} hrs</td>
                                <td class="px-4 py-4 text-right text-slate-700">${daily100Miles} mi</td>
                                <td class="px-4 py-4 text-right font-black text-emerald-700">$${daily100Gross}</td>
                                <td class="px-4 py-4 text-slate-600">Dinner, weekend, or two short peaks.</td>
                            </tr>
                            <tr>
                                <td class="px-4 py-4 font-bold text-slate-900">$200 net day</td>
                                <td class="px-4 py-4 text-right text-slate-700">${String.format("%.1f", daily200Hours)} hrs</td>
                                <td class="px-4 py-4 text-right text-slate-700">${daily200Miles} mi</td>
                                <td class="px-4 py-4 text-right font-black text-emerald-700">$${daily200Gross}</td>
                                <td class="px-4 py-4 text-slate-600">Long stretch goal; compare against fatigue and dead miles.</td>
                            </tr>
                            <tr>
                                <td class="px-4 py-4 font-bold text-slate-900">Per-mile floor</td>
                                <td class="px-4 py-4 text-right text-slate-700">${scenario.getHours()} hrs/wk</td>
                                <td class="px-4 py-4 text-right text-slate-700">${scenario.getMiles()} mi/wk</td>
                                <td class="px-4 py-4 text-right font-black text-emerald-700">$${String.format("%.2f", scenario.getGrossWeekly() / (double) scenario.getMiles())}/mi</td>
                                <td class="px-4 py-4 text-slate-600">Reject offers that look busy but fail after route miles.</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </section>

            @if(app.equals("doordash"))
                <section class="bg-emerald-50 rounded-3xl border border-emerald-100 p-8 shadow-sm">
                    <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">High-intent city routes</p>
                    <h2 class="text-3xl font-black text-slate-900 mb-4">Stay in this ${city.getCityName()} DoorDash cluster</h2>
                    <div class="grid md:grid-cols-2 gap-3">
                        <a href="/salary/doordash/${city.getSlug()}/after-gas" class="rounded-2xl bg-white border border-emerald-100 px-5 py-4 hover:border-emerald-300 transition-colors">
                            <strong class="block text-slate-900">DoorDash pay in ${city.getCityName()} after gas</strong>
                            <span class="text-sm text-slate-600">Fuel, miles, and take-home estimate.</span>
                        </a>
                        <a href="/salary/doordash/${city.getSlug()}/nights-weekends" class="rounded-2xl bg-white border border-emerald-100 px-5 py-4 hover:border-emerald-300 transition-colors">
                            <strong class="block text-slate-900">Best DoorDash hours in ${city.getCityName()}</strong>
                            <span class="text-sm text-slate-600">Dinner, weekend, and slow-window check.</span>
                        </a>
                        <a href="/salary/doordash/${city.getSlug()}/100-a-day" class="rounded-2xl bg-white border border-emerald-100 px-5 py-4 hover:border-emerald-300 transition-colors">
                            <strong class="block text-slate-900">Can you make $100/day in ${city.getCityName()}?</strong>
                            <span class="text-sm text-slate-600">Daily target math for this market.</span>
                        </a>
                        <a href="/salary/doordash/${city.getSlug()}/uber-eats-vs-doordash" class="rounded-2xl bg-white border border-emerald-100 px-5 py-4 hover:border-emerald-300 transition-colors">
                            <strong class="block text-slate-900">Uber Eats vs DoorDash in ${city.getCityName()}</strong>
                            <span class="text-sm text-slate-600">Delivery-app comparison after expenses.</span>
                        </a>
                    </div>
                </section>
            @endif

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
