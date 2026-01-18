@import com.gigwager.model.SeoMeta
@import com.gigwager.model.CityData
@import com.gigwager.model.CityScenario
@import com.gigwager.model.WorkLevel
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
@param String validThrough
@param String datePosted
@param String schemaDescription
@param java.util.List<com.gigwager.model.CityData> similarCities

@template.layout.main(
    seoMeta = seoMeta,
    content = @`
        <!-- JobPosting Schema -->
        <script type="application/ld+json">
        {
          "@context": "https://schema.org/",
          "@type": "JobPosting",
          "title": "${appName} Driver in ${city.getCityName()} (${workLevel.getDisplayName()})",
          "description": "$unsafe{schemaDescription} Find out if driving for ${appName} in ${city.getCityName()} is worth it with GigWageTruth's real earnings data.",
          "identifier": {
            "@type": "PropertyValue",
            "name": "GigWageTruth",
            "value": "${city.getSlug()}-${app}-${workLevel.getSlug()}"
          },
          "datePosted": "${datePosted}",
          "validThrough": "${validThrough}",
          "employmentType": "${workLevel == WorkLevel.FULL_TIME ? "FULL_TIME" : "PART_TIME"}",
          "hiringOrganization" : {
            "@type" : "Organization",
            "name" : "GigWageTruth",
            "sameAs" : "${AppConstants.BASE_URL}",
            "logo" : "${AppConstants.BASE_URL}/favicon.png"
          },
          "jobLocationType": "TELECOMMUTE",
          "applicantLocationRequirements": {
            "@type": "Country",
            "name": "US"
          },
          "baseSalary": {
            "@type": "MonetaryAmount",
            "currency": "USD",
            "value": {
              "@type": "QuantitativeValue",
              "minValue": ${String.format("%.2f", scenario.getNetHourly() * 0.90)},
              "maxValue": ${String.format("%.2f", scenario.getNetHourly() * 1.15)},
              "unitText": "HOUR"
            }
          }
        }
        </script>
        
        <!-- Aggressive FAQ Schema for SERP Real Estate -->
        <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "FAQPage",
          "mainEntity": [{
            "@type": "Question",
            "name": "How much does ${appName} really pay in ${city.getCityName()}?",
            "acceptedAnswer": {
              "@type": "Answer",
              "text": "In 2026, the real net earnings for ${appName} drivers in ${city.getCityName()} are approximately $${String.format("%.2f", scenario.getNetHourly())}/hour after accounting for gas ($${String.format("%.2f", city.getGasPrice())}/gal), vehicle depreciation, and taxes. Gross earnings are typically higher, but expenses eat up about 30-40% of revenue."
            }
          }, {
            "@type": "Question",
            "name": "Is doing ${appName} in ${city.getCityName()} worth it?",
            "acceptedAnswer": {
              "@type": "Answer",
              "text": "It depends on your vehicle. With local gas prices at $${String.format("%.2f", city.getGasPrice())}/gal, driving a gas-guzzler will significantly lower your hourly wage. ${workLevel.getDisplayName()} drivers in ${city.getCityName()} typically see the best results by ${city.isHighTraffic() ? "avoiding peak traffic hours" : "focusing on peak dining hours"}."
            }
          }]
        }
        </script>

        <!-- Hero Section -->
        <div class="bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 pt-20 pb-24 px-6">
            <div class="max-w-4xl mx-auto">
                <!-- Breadcrumb Navigation -->
                <nav class="text-sm text-slate-400 mb-4 flex items-center gap-2 flex-wrap">
                    <a href="/" class="hover:text-emerald-400 transition">Home</a>
                    <span class="text-slate-600">›</span>
                    <a href="/salary/directory" class="hover:text-emerald-400 transition">Salary Directory</a>
                    <span class="text-slate-600">›</span>
                    <a href="${parentPageUrl}" class="hover:text-emerald-400 transition">${city.getCityName()} ${appName}</a>
                    <span class="text-slate-600">›</span>
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
                
                <p class="text-xl text-slate-300 max-w-2xl mb-8">
                    ${workLevel.getTagline()}
                </p>

                <!-- Featured Scenario Card -->
                <div class="bg-white/5 backdrop-blur-sm border border-white/10 rounded-2xl p-8">
                    <div class="flex items-baseline gap-3 mb-2">
                        <span class="text-sm font-bold text-slate-400 uppercase tracking-wider">Your Real Take-Home</span>
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
            </div>
        </div>

        <!-- Main Content -->
        <div class="max-w-4xl mx-auto px-6 py-16 space-y-12">

            <!-- What This Work Level Means (UNIQUE CONTENT #1) -->
            <section class="bg-white rounded-2xl p-8 shadow-sm border border-slate-200">
                <h2 class="text-3xl font-bold text-slate-900 mb-6 flex items-center gap-3">
                    <span class="text-3xl">${workLevel.getEmoji()}</span>
                    What ${workLevel.getDisplayName()} Really Means in ${city.getCityName()}
                </h2>
                <div class="prose prose-slate max-w-none">
                    $unsafe{workLevelMeaning}
                </div>
            </section>

            <!-- Tax Strategy Section (UNIQUE CONTENT #2) -->
            <section class="bg-slate-50 rounded-2xl p-8 border border-slate-200">
                <div class="prose prose-slate max-w-none">
                    $unsafe{taxStrategy}
                </div>
            </section>

            <!-- Day in the Life (UNIQUE CONTENT #3) -->
            <section class="bg-white rounded-2xl p-8 shadow-sm border border-slate-200">
                <div class="prose prose-slate max-w-none">
                    $unsafe{dayInTheLife}
                </div>
            </section>

            <!-- Best Practices (UNIQUE CONTENT #4) -->
            <section class="bg-gradient-to-br from-emerald-50 to-emerald-100 rounded-2xl p-8 border-2 border-emerald-200">
                <div class="prose prose-slate max-w-none">
                    $unsafe{bestPractices}
                </div>
            </section>

            <!-- Cross-Links to Other Work Levels (Internal Silo) -->
            <section class="bg-slate-100 rounded-2xl p-8 border border-slate-300">
                <h3 class="text-xl font-bold text-slate-900 mb-4">
                    📊 Explore Other Work Levels in ${city.getCityName()}
                </h3>
                <p class="text-slate-700 mb-6">
                    Not sure ${workLevel.getDisplayName()} is right for you? Compare with other work levels:
                </p>
                <div class="grid md:grid-cols-3 gap-4">
                    @if(workLevel != WorkLevel.PART_TIME)
                        <a href="/salary/${app}/${city.getSlug()}/part-time" 
                           class="block p-4 bg-white border-2 border-emerald-200 rounded-lg hover:shadow-md hover:border-emerald-400 transition">
                            <div class="text-2xl mb-2">⏰</div>
                            <div class="font-bold text-slate-900">Part-Time Guide</div>
                            <p class="text-xs text-slate-600 mt-1">10 hrs/week</p>
                        </a>
                    @endif

                    @if(workLevel != WorkLevel.SIDE_HUSTLE)
                        <a href="/salary/${app}/${city.getSlug()}/side-hustle" 
                           class="block p-4 bg-white border-2 border-amber-200 rounded-lg hover:shadow-md hover:border-amber-400 transition">
                            <div class="text-2xl mb-2">💼</div>
                            <div class="font-bold text-slate-900">Side-Hustle Guide</div>
                            <p class="text-xs text-slate-600 mt-1">25 hrs/week</p>
                        </a>
                    @endif

                    @if(workLevel != WorkLevel.FULL_TIME)
                        <a href="/salary/${app}/${city.getSlug()}/full-time" 
                           class="block p-4 bg-white border-2 border-blue-200 rounded-lg hover:shadow-md hover:border-blue-400 transition">
                            <div class="text-2xl mb-2">🚗</div>
                            <div class="font-bold text-slate-900">Full-Time Guide</div>
                            <p class="text-xs text-slate-600 mt-1">40 hrs/week</p>
                        </a>
                    @endif
                </div>
            </section>

            <!-- Cross-App Comparison CTA -->
            <section class="bg-gradient-to-br from-slate-800 to-slate-900 rounded-2xl p-8 text-white shadow-xl">
                <div class="flex items-start gap-4">
                    <div class="text-4xl">🔄</div>
                    <div class="flex-1">
                        <h3 class="text-2xl font-bold mb-3">
                            Compare: ${workLevel.getDisplayName()} ${otherAppName} in ${city.getCityName()}
                        </h3>
                        <p class="text-slate-300 mb-4">
                            You're viewing ${appName} ${workLevel.getDisplayName().toLowerCase()} data. 
                            See how ${otherAppName} compares at the same work level in ${city.getCityName()}.
                        </p>
                        <a href="${otherAppUrl}" 
                           class="inline-block px-6 py-3 bg-white text-slate-900 font-bold rounded-lg hover:bg-slate-100 transition shadow-lg">
                            View ${otherAppName} ${workLevel.getDisplayName()} Report →
                        </a>
                    </div>
                </div>
            </section>

            <!-- Internal Linking Silo: Similar Cities -->
            <section class="mt-8 pt-8 border-t border-slate-200">
                <h4 class="text-sm font-bold text-slate-500 uppercase tracking-wider mb-6">
                    Other High-Demand ${city.getMarketTier()} Markets
                </h4>
                <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                    @for(com.gigwager.model.CityData simCity : similarCities)
                        <a href="/salary/${app}/${simCity.getSlug()}/${workLevel.getSlug()}" 
                           class="flex items-center gap-3 p-4 bg-white rounded-xl border border-slate-200 hover:border-emerald-400 hover:shadow-md transition group">
                            <span class="text-2xl">📍</span>
                            <div class="flex flex-col">
                                <span class="text-sm font-bold text-slate-900 group-hover:text-emerald-700">
                                    ${simCity.getCityName()}
                                </span>
                                <span class="text-xs text-slate-500">
                                    ${appName} ${workLevel.getDisplayName()}
                                </span>
                            </div>
                        </a>
                    @endfor
                </div>
            </section>

            <!-- Back to Overview -->
            <section class="text-center">
                <a href="${parentPageUrl}" 
                   class="inline-block px-8 py-4 bg-slate-200 text-slate-900 font-bold text-lg rounded-xl hover:bg-slate-300 transition">
                    ← Back to ${city.getCityName()} ${appName} Overview
                </a>
            </section>

        </div>
    `
)
