@import com.gigwager.model.SeoMeta
@import com.gigwager.model.CityData
@import com.gigwager.model.CityScenario
@import com.gigwager.model.WorkLevel
@import com.gigwager.model.content.PersonaQuote
@import com.gigwager.model.content.SourceCitation
@import java.util.List
@import com.gigwager.util.AppConstants
@param SeoMeta seoMeta
@param String app
@param String appName
@param CityData city
@param WorkLevel workLevel
@param CityScenario scenario
@param String lastUpdated
@param String otherApp
@param String otherAppName
@param String otherAppUrl
@param String parentPageUrl
@param String workLevelMeaning
@param String taxStrategy
@param String dayInTheLife
@param String bestPractices
@param String pageStructureType = "TYPE_A"
@param String heroHook = null
@param List personaQuotes = null
@param List sourceCitations = null
@param String methodologyVersion = "legacy-v1"
@param String contentType = "legacy_template"
@param Boolean requiresEditorialReview = false
@param String editorialReviewLabel = "Editorial review status unavailable"
@param String lastVerifiedAt = null
@param Double richNetMin = null
@param Double richNetMax = null
@param List similarCities
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = seoMeta,
    content = @`
        <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@graph": [
            {
              "@type": "BreadcrumbList",
              "itemListElement": [{
                "@type": "ListItem",
                "position": 1,
                "name": "Salary Directory",
                "item": "${AppConstants.BASE_URL}/salary/directory"
              },{
                "@type": "ListItem",
                "position": 2,
                "name": "${city.getCityName()} ${appName} Earnings",
                "item": "${AppConstants.BASE_URL}${parentPageUrl}"
              },{
                "@type": "ListItem",
                "position": 3,
                "name": "${workLevel.getDisplayName()}"
              }]
            },
            {
              "@type": "FAQPage",
              "mainEntity": [{
                "@type": "Question",
                "name": "How much does a ${workLevel.getDisplayName()} ${appName} driver make in ${city.getCityName()}?",
                "acceptedAnswer": {
                  "@type": "Answer",
                  "text": "A ${workLevel.getDisplayName()} ${appName} driver in ${city.getCityName()} working ${workLevel.getHoursPerWeek()} hours per week can expect to take home approximately $${String.format("%.2f", scenario.getNetProfit())} per week after deducting estimated mileage and self-employment taxes."
                }
              }, {
                "@type": "Question",
                "name": "Is ${appName} better than ${otherAppName} for ${workLevel.getDisplayName()} drivers in ${city.getCityName()}?",
                "acceptedAnswer": {
                  "@type": "Answer",
                  "text": "Local factors like ${city.getMarketTier()} demand and ${String.format("$%.2f", city.getGasPrice())} per gallon gas prices affect profitability. Compare this page with ${otherAppName} data to see which app fits your market timing."
                }
              }]
            }
          ]
        }
        </script>

        <div class="bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 pt-20 pb-24 px-6">
            <div class="max-w-4xl mx-auto">
                <nav class="text-sm text-slate-400 mb-4 flex items-center gap-2 flex-wrap">
                    <a href="/" class="hover:text-emerald-400 transition">Home</a>
                    <span class="text-slate-600">&gt;</span>
                    <a href="/salary/directory" class="hover:text-emerald-400 transition">Salary Directory</a>
                    <span class="text-slate-600">&gt;</span>
                    <a href="${parentPageUrl}" class="hover:text-emerald-400 transition">${city.getCityName()} ${appName}</a>
                    <span class="text-slate-600">&gt;</span>
                    <span class="text-white">${workLevel.getDisplayName()} Deep Dive</span>
                </nav>

                <div class="flex items-center gap-3 mb-6 flex-wrap">
                    <div class="inline-block px-4 py-2 bg-emerald-500/10 border border-emerald-500/30 rounded-full">
                        <span class="text-emerald-400 text-sm font-bold uppercase tracking-wider">${workLevel.getEmoji()} ${workLevel.getDisplayName()}</span>
                    </div>
                    <div class="inline-block px-4 py-1 bg-white/5 border border-white/10 rounded-full">
                        <span class="text-slate-300 text-xs font-semibold uppercase tracking-wider">${city.getCityName()}, ${city.getState()}</span>
                    </div>
                    <span class="text-slate-400 text-xs font-medium uppercase tracking-wider">Last Updated: ${lastUpdated}</span>
                </div>

                <h1 class="text-4xl md:text-6xl font-black text-white mb-4 leading-tight">
                    ${workLevel.getDisplayName()} ${appName} Earnings in ${city.getCityName()}
                </h1>
                @if(heroHook != null && !heroHook.isBlank())
                    <p class="text-xl text-slate-300 max-w-3xl mb-8">${heroHook}</p>
                @else
                    <p class="text-xl text-slate-300 max-w-2xl mb-8">${workLevel.getTagline()}</p>
                @endif

                <div class="bg-white/5 backdrop-blur-sm border border-white/10 rounded-2xl p-8">
                    <div class="flex items-baseline gap-3 mb-2">
                        <span class="text-sm font-bold text-slate-400 uppercase tracking-wider">Estimated Weekly Take-Home</span>
                        <span class="text-xs px-3 py-1 bg-slate-700 text-slate-300 rounded-md">${scenario.getHours()} hrs/week, ${scenario.getMiles()} mi</span>
                    </div>
                    <div class="flex items-baseline gap-2 mb-4">
                        <span class="text-6xl md:text-7xl font-black text-emerald-400">$${String.format("%.2f", scenario.getNetHourly())}</span>
                        <span class="text-2xl text-slate-400 font-medium">/hr</span>
                    </div>
                    <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mt-6 pt-6 border-t border-white/10">
                        <div>
                            <div class="text-xs text-slate-500 uppercase tracking-wider mb-1">Weekly Gross</div>
                            <div class="text-xl font-bold text-white">$${scenario.getGrossWeekly()}</div>
                        </div>
                        <div>
                            <div class="text-xs text-slate-500 uppercase tracking-wider mb-1">Net Profit</div>
                            <div class="text-xl font-bold text-emerald-400">$${String.format("%.0f", scenario.getNetProfit())}</div>
                        </div>
                        <div>
                            <div class="text-xs text-slate-500 uppercase tracking-wider mb-1">Hours/Week</div>
                            <div class="text-xl font-bold text-white">${scenario.getHours()}</div>
                        </div>
                        <div>
                            <div class="text-xs text-slate-500 uppercase tracking-wider mb-1">Miles/Week</div>
                            <div class="text-xl font-bold text-white">${scenario.getMiles()}</div>
                        </div>
                    </div>
                </div>

                @if(richNetMin != null && richNetMax != null)
                    <div class="mt-6 bg-emerald-500/10 border border-emerald-400/30 rounded-xl p-4">
                        <p class="text-emerald-200 text-sm">
                            Editorial observed range: <strong>$${String.format("%.2f", richNetMin)}-$${String.format("%.2f", richNetMax)}/hr</strong>
                        </p>
                    </div>
                @endif

                <div class="mt-8 bg-white/5 border border-white/10 rounded-xl p-4 text-slate-300">
                    <h4 class="text-sm font-bold text-white mb-2">Calculation Assumptions</h4>
                    <p class="text-xs mb-2">
                        These are estimated scenarios based on typical ${city.getCityName()} conditions. Actual results vary by vehicle efficiency, trip mix, acceptance behavior, and market timing.
                    </p>
                    <p class="text-xs text-slate-400">
                        Net profit is estimated by applying the IRS standard mileage rate and 15.3% self-employment tax to modeled weekly gross income. For 2026, the IRS standard mileage rate is 72.5 cents per mile. This is educational content, not tax advice.
                    </p>
                </div>

                @if(sourceCitations != null && !sourceCitations.isEmpty())
                    <div class="mt-6 bg-emerald-500/10 border border-emerald-400/30 rounded-xl p-4 text-emerald-100">
                        <p class="text-xs uppercase tracking-wider font-bold mb-1">Source-backed page</p>
                        <p class="text-sm">
                            ${sourceCitations.size()} editorial sources linked below
                            @if(lastVerifiedAt != null && !lastVerifiedAt.isBlank())
                                | Last verified: <strong>${lastVerifiedAt}</strong>
                            @endif
                            | Method: <strong>${methodologyVersion}</strong>
                        </p>
                        @if(requiresEditorialReview)
                            <p class="text-xs mt-2 text-amber-200">Editorial review: <strong>${editorialReviewLabel}</strong></p>
                        @else
                            <p class="text-xs mt-2 text-emerald-200">Editorial review: <strong>${editorialReviewLabel}</strong></p>
                        @endif
                    </div>
                @endif
            </div>
        </div>

        <div class="max-w-4xl mx-auto px-6 py-16 space-y-12">

            @if("TYPE_B".equals(pageStructureType))
                <section class="bg-white rounded-2xl p-8 shadow-sm border border-slate-200">
                    <div class="prose prose-slate max-w-none">$unsafe{dayInTheLife}</div>
                </section>

                <section class="bg-white rounded-2xl p-8 shadow-sm border border-slate-200">
                    <h2 class="text-3xl font-bold text-slate-900 mb-6 flex items-center gap-3">
                        <span class="text-3xl">${workLevel.getEmoji()}</span>
                        What ${workLevel.getDisplayName()} Means in ${city.getCityName()}
                    </h2>
                    <div class="prose prose-slate max-w-none">$unsafe{workLevelMeaning}</div>
                </section>

                @if(personaQuotes != null && !personaQuotes.isEmpty())
                    <section class="bg-slate-900 rounded-2xl p-8 border border-slate-700 text-white">
                        <h3 class="text-2xl font-bold mb-4">Persona Validation Snapshot</h3>
                        <p class="text-slate-300 text-sm mb-6">These voices are labeled by persona type so users can evaluate perspective and bias.</p>
                        <div class="grid md:grid-cols-2 gap-4">
                            @for(var quoteObj : personaQuotes)
                                <article class="bg-white/5 border border-white/10 rounded-xl p-5">
                                    <p class="text-xs uppercase tracking-wider text-emerald-300 mb-2">${((PersonaQuote) quoteObj).personaType().name().replace('_', ' ')}</p>
                                    <blockquote class="text-slate-100 leading-relaxed">"${((PersonaQuote) quoteObj).quote()}"</blockquote>
                                    <p class="text-xs text-slate-400 mt-3">${((PersonaQuote) quoteObj).displayName()} | ${((PersonaQuote) quoteObj).attributionType().name().replace('_', ' ')}</p>
                                </article>
                            @endfor
                        </div>
                    </section>
                @endif

                <section class="bg-gradient-to-br from-emerald-50 to-emerald-100 rounded-2xl p-8 border-2 border-emerald-200">
                    <div class="prose prose-slate max-w-none">$unsafe{bestPractices}</div>
                </section>

                <section class="bg-slate-50 rounded-2xl p-8 border border-slate-200">
                    <div class="prose prose-slate max-w-none">$unsafe{taxStrategy}</div>
                </section>

            @else
                @if("TYPE_C".equals(pageStructureType))
                <section class="bg-slate-50 rounded-2xl p-8 border border-slate-200">
                    <div class="prose prose-slate max-w-none">$unsafe{taxStrategy}</div>
                </section>

                <section class="bg-gradient-to-br from-emerald-50 to-emerald-100 rounded-2xl p-8 border-2 border-emerald-200">
                    <div class="prose prose-slate max-w-none">$unsafe{bestPractices}</div>
                </section>

                @if(personaQuotes != null && !personaQuotes.isEmpty())
                    <section class="bg-slate-900 rounded-2xl p-8 border border-slate-700 text-white">
                        <h3 class="text-2xl font-bold mb-4">Persona Validation Snapshot</h3>
                        <p class="text-slate-300 text-sm mb-6">These voices are labeled by persona type so users can evaluate perspective and bias.</p>
                        <div class="grid md:grid-cols-2 gap-4">
                            @for(var quoteObj : personaQuotes)
                                <article class="bg-white/5 border border-white/10 rounded-xl p-5">
                                    <p class="text-xs uppercase tracking-wider text-emerald-300 mb-2">${((PersonaQuote) quoteObj).personaType().name().replace('_', ' ')}</p>
                                    <blockquote class="text-slate-100 leading-relaxed">"${((PersonaQuote) quoteObj).quote()}"</blockquote>
                                    <p class="text-xs text-slate-400 mt-3">${((PersonaQuote) quoteObj).displayName()} | ${((PersonaQuote) quoteObj).attributionType().name().replace('_', ' ')}</p>
                                </article>
                            @endfor
                        </div>
                    </section>
                @endif

                <section class="bg-white rounded-2xl p-8 shadow-sm border border-slate-200">
                    <h2 class="text-3xl font-bold text-slate-900 mb-6 flex items-center gap-3">
                        <span class="text-3xl">${workLevel.getEmoji()}</span>
                        What ${workLevel.getDisplayName()} Means in ${city.getCityName()}
                    </h2>
                    <div class="prose prose-slate max-w-none">$unsafe{workLevelMeaning}</div>
                </section>

                <section class="bg-white rounded-2xl p-8 shadow-sm border border-slate-200">
                    <div class="prose prose-slate max-w-none">$unsafe{dayInTheLife}</div>
                </section>

                @else
                <section class="bg-white rounded-2xl p-8 shadow-sm border border-slate-200">
                    <h2 class="text-3xl font-bold text-slate-900 mb-6 flex items-center gap-3">
                        <span class="text-3xl">${workLevel.getEmoji()}</span>
                        What ${workLevel.getDisplayName()} Means in ${city.getCityName()}
                    </h2>
                    <div class="prose prose-slate max-w-none">$unsafe{workLevelMeaning}</div>
                </section>

                <section class="bg-slate-50 rounded-2xl p-8 border border-slate-200">
                    <div class="prose prose-slate max-w-none">$unsafe{taxStrategy}</div>
                </section>

                <section class="bg-white rounded-2xl p-8 shadow-sm border border-slate-200">
                    <div class="prose prose-slate max-w-none">$unsafe{dayInTheLife}</div>
                </section>

                @if(personaQuotes != null && !personaQuotes.isEmpty())
                    <section class="bg-slate-900 rounded-2xl p-8 border border-slate-700 text-white">
                        <h3 class="text-2xl font-bold mb-4">Persona Validation Snapshot</h3>
                        <p class="text-slate-300 text-sm mb-6">These voices are labeled by persona type so users can evaluate perspective and bias.</p>
                        <div class="grid md:grid-cols-2 gap-4">
                            @for(var quoteObj : personaQuotes)
                                <article class="bg-white/5 border border-white/10 rounded-xl p-5">
                                    <p class="text-xs uppercase tracking-wider text-emerald-300 mb-2">${((PersonaQuote) quoteObj).personaType().name().replace('_', ' ')}</p>
                                    <blockquote class="text-slate-100 leading-relaxed">"${((PersonaQuote) quoteObj).quote()}"</blockquote>
                                    <p class="text-xs text-slate-400 mt-3">${((PersonaQuote) quoteObj).displayName()} | ${((PersonaQuote) quoteObj).attributionType().name().replace('_', ' ')}</p>
                                </article>
                            @endfor
                        </div>
                    </section>
                @endif

                <section class="bg-gradient-to-br from-emerald-50 to-emerald-100 rounded-2xl p-8 border-2 border-emerald-200">
                    <div class="prose prose-slate max-w-none">$unsafe{bestPractices}</div>
                </section>
                @endif
            @endif

            @if(sourceCitations != null && !sourceCitations.isEmpty())
                <section class="bg-white rounded-2xl p-8 shadow-sm border border-slate-200">
                    <h3 class="text-xl font-bold text-slate-900 mb-3">Sources and Methodology</h3>
                    <p class="text-sm text-slate-600 mb-2">Methodology version: <strong>${methodologyVersion}</strong></p>
                    <p class="text-sm text-slate-600 mb-2">Content classification: <strong>${contentType}</strong></p>
                    @if(requiresEditorialReview)
                        <p class="text-sm text-amber-700 mb-5">Editorial review: <strong>${editorialReviewLabel}</strong></p>
                    @else
                        <p class="text-sm text-emerald-700 mb-5">Editorial review: <strong>${editorialReviewLabel}</strong></p>
                    @endif
                    <ul class="space-y-3">
                        @for(var sourceObj : sourceCitations)
                            <li class="text-sm text-slate-700">
                                <a class="text-emerald-700 hover:text-emerald-800 font-semibold" href="${((SourceCitation) sourceObj).url()}" target="_blank" rel="noopener nofollow">${((SourceCitation) sourceObj).title()}</a>
                                <span class="text-slate-500"> | ${((SourceCitation) sourceObj).publisher()} | Checked ${((SourceCitation) sourceObj).checkedAt()}</span>
                            </li>
                        @endfor
                    </ul>
                </section>
            @endif

            <section class="bg-white rounded-2xl p-8 shadow-sm border border-slate-200">
                <h3 class="text-xl font-bold text-slate-900 mb-4">Frequently Asked Questions</h3>
                <div class="space-y-4">
                    <details class="group border border-slate-200 rounded-xl p-4">
                        <summary class="font-semibold text-slate-900 cursor-pointer">How much does a ${workLevel.getDisplayName()} ${appName} driver make in ${city.getCityName()}?</summary>
                        <p class="text-slate-600 mt-3">A ${workLevel.getDisplayName()} ${appName} driver in ${city.getCityName()} working ${workLevel.getHoursPerWeek()} hours per week can expect to take home approximately $${String.format("%.2f", scenario.getNetProfit())} per week after deducting estimated mileage and self-employment taxes.</p>
                    </details>
                    <details class="group border border-slate-200 rounded-xl p-4">
                        <summary class="font-semibold text-slate-900 cursor-pointer">Is ${appName} better than ${otherAppName} for ${workLevel.getDisplayName()} drivers in ${city.getCityName()}?</summary>
                        <p class="text-slate-600 mt-3">Local factors like ${city.getMarketTier()} demand and ${String.format("$%.2f", city.getGasPrice())} per gallon gas prices affect profitability. Compare this page with ${otherAppName} data to see which app fits your market timing.</p>
                    </details>
                </div>
            </section>

            <section class="bg-slate-100 rounded-2xl p-8 border border-slate-300">
                <h3 class="text-xl font-bold text-slate-900 mb-4">Explore Other Work Levels in ${city.getCityName()}</h3>
                <p class="text-slate-700 mb-6">Compare the same market across different weekly hour commitments.</p>
                <div class="grid md:grid-cols-3 gap-4">
                    @if(workLevel != WorkLevel.PART_TIME)
                        <a href="/salary/${app}/${city.getSlug()}/part-time"
                           class="block p-4 bg-white border-2 border-emerald-200 rounded-lg hover:shadow-md hover:border-emerald-400 transition">
                            <div class="font-bold text-slate-900">Part-Time Guide</div>
                            <p class="text-xs text-slate-600 mt-1">10 hrs/week</p>
                        </a>
                    @endif

                    @if(workLevel != WorkLevel.SIDE_HUSTLE)
                        <a href="/salary/${app}/${city.getSlug()}/side-hustle"
                           class="block p-4 bg-white border-2 border-amber-200 rounded-lg hover:shadow-md hover:border-amber-400 transition">
                            <div class="font-bold text-slate-900">Side-Hustle Guide</div>
                            <p class="text-xs text-slate-600 mt-1">25 hrs/week</p>
                        </a>
                    @endif

                    @if(workLevel != WorkLevel.FULL_TIME)
                        <a href="/salary/${app}/${city.getSlug()}/full-time"
                           class="block p-4 bg-white border-2 border-blue-200 rounded-lg hover:shadow-md hover:border-blue-400 transition">
                            <div class="font-bold text-slate-900">Full-Time Guide</div>
                            <p class="text-xs text-slate-600 mt-1">40 hrs/week</p>
                        </a>
                    @endif
                </div>
            </section>

            <section class="bg-gradient-to-br from-slate-800 to-slate-900 rounded-2xl p-8 text-white shadow-xl">
                <h3 class="text-2xl font-bold mb-3">Compare With ${otherAppName} at the Same Work Level</h3>
                <p class="text-slate-300 mb-4">
                    You are viewing ${appName} ${workLevel.getDisplayName().toLowerCase()} estimates. Open the equivalent ${otherAppName} page for a direct app-to-app comparison in ${city.getCityName()}.
                </p>
                <a href="${otherAppUrl}"
                   class="inline-block px-6 py-3 bg-white text-slate-900 font-bold rounded-lg hover:bg-slate-100 transition shadow-lg">
                    View ${otherAppName} ${workLevel.getDisplayName()} Report
                </a>
            </section>

            <section class="mt-8 pt-8 border-t border-slate-200">
                <h4 class="text-sm font-bold text-slate-500 uppercase tracking-wider mb-6">
                    Other ${city.getMarketTier()} Tier Markets
                </h4>
                <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                    @for(var simCityObj : similarCities)
                        <a href="/salary/${app}/${((CityData) simCityObj).getSlug()}/${workLevel.getSlug()}"
                           class="flex items-center gap-3 p-4 bg-white rounded-xl border border-slate-200 hover:border-emerald-400 hover:shadow-md transition group">
                            <div class="flex flex-col">
                                <span class="text-sm font-bold text-slate-900 group-hover:text-emerald-700">${((CityData) simCityObj).getCityName()}</span>
                                <span class="text-xs text-slate-500">${appName} ${workLevel.getDisplayName()}</span>
                            </div>
                        </a>
                    @endfor
                </div>
            </section>

            <section class="bg-red-50/50 rounded-2xl p-8 border border-red-200 mb-8 mt-8">
                <h3 class="text-xl font-bold text-slate-900 mb-2">Risk Checks Before Scaling Hours</h3>
                <p class="text-slate-600 mb-6 text-sm">Based on modeled ${appName} weekly assumptions for ${city.getCityName()}.</p>

                <div class="grid md:grid-cols-3 gap-4">
                    <div class="bg-white p-5 rounded-xl border-2 border-red-100 hover:border-red-500 transition-all">
                        <h4 class="font-bold text-slate-900 mb-2">Quarterly Tax Risk</h4>
                        <p class="text-xs text-slate-600 mb-3">
                            Estimated quarterly self-employment tax exposure: <strong>$${String.format("%.0f", Math.max(0, (scenario.getGrossWeekly() - (scenario.getMiles() * AppConstants.IRS_MILEAGE_RATE)) * AppConstants.SELF_EMPLOYMENT_TAX_RATE) * 13 )}</strong>.
                        </p>
                        <a href="/taxes/quarterly-estimator" class="text-xs font-bold text-red-600 hover:text-red-700">Open quarterly estimator</a>
                    </div>

                    <div class="bg-white p-5 rounded-xl border-2 border-red-100 hover:border-red-500 transition-all">
                        <h4 class="font-bold text-slate-900 mb-2">Vehicle Depreciation</h4>
                        <p class="text-xs text-slate-600 mb-3">
                            This schedule can add roughly <strong>${scenario.getMiles() * 52} miles</strong> per year to your vehicle.
                        </p>
                        <a href="/vehicle-cost/cost-per-mile" class="text-xs font-bold text-red-600 hover:text-red-700">Open cost-per-mile calculator</a>
                    </div>

                    <div class="bg-white p-5 rounded-xl border-2 border-red-100 hover:border-red-500 transition-all relative">
                        <div class="absolute -right-2 -top-2 bg-amber-500 text-white text-[10px] font-bold px-2 py-1 rounded-full uppercase tracking-wide border-2 border-white">Critical</div>
                        <h4 class="font-bold text-slate-900 mb-2">Insurance Gap</h4>
                        <p class="text-xs text-slate-600 mb-3">
                            Market traffic intensity in ${city.getCityName()} can increase collision frequency. Confirm rideshare endorsement details before increasing weekly miles.
                        </p>
                        <a href="/insurance/rideshare-basics" class="text-xs font-bold text-red-600 hover:text-red-700">Review coverage basics</a>
                    </div>
                </div>
            </section>

            <section class="text-center">
                <a href="${parentPageUrl}"
                   class="inline-block px-8 py-4 bg-slate-200 text-slate-900 font-bold text-lg rounded-xl hover:bg-slate-300 transition">
                    Back to ${city.getCityName()} ${appName} Overview
                </a>
            </section>

        </div>
    `
)

