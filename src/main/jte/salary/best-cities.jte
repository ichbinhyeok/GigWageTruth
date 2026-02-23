@import com.gigwager.model.SeoMeta
@import com.gigwager.dto.CityRankingDto
@import java.util.List
@import com.gigwager.util.AppConstants

@param String app
@param String appName
@param List<CityRankingDto> rankedCities
@param String lastUpdated
@param SeoMeta seoMeta
@param Boolean noIndex = false

@template.layout.main(
    seoMeta = seoMeta,
    noIndex = noIndex,
    content = @`
        <div class="bg-slate-50 min-h-screen py-16 px-6">
            <div class="max-w-4xl mx-auto">
                <div class="text-center mb-12">
                    <h1 class="text-4xl lg:text-5xl font-black text-slate-900 mb-6 tracking-tight">
                        Best Cities to Drive for ${appName}
                    </h1>
                    <p class="text-xl text-slate-600 max-w-2xl mx-auto">
                        Ranked dynamically by estimated net hourly take-home pay, after deducting local gas prices and taxes.
                    </p>
                </div>

                <div class="bg-blue-50 p-6 rounded-xl border border-blue-200 text-left mb-8">
                    <p class="text-blue-800 text-sm font-medium"><strong>Disclaimer:</strong> This ranking is based on automated algorithm estimates. It calculates net hourly pay using the IRS Standard Mileage Rate (${AppConstants.IRS_MILEAGE_RATE * 100}¢/mi) and a 15.3% Self-Employment Tax rate on projected gross earnings. Your actual results will vary depending on your specific vehicle, driving habits, and tax situation.</p>
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
            </div>
        </div>

        <!-- PR8-5: ItemList Structured Data for Rankings -->
        <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "ItemList",
          "name": "Best Cities for ${appName} Drivers",
          "description": "Rankings of US cities based on ${appName} net hourly pay after expenses.",
          "itemListElement": [
            @for(int i = 0; i < Math.min(rankedCities.size(), 20); i++)
            {
              "@type": "ListItem",
              "position": ${i + 1},
              "name": "${rankedCities.get(i).city().getCityName()}",
              "url": "${AppConstants.BASE_URL}/salary/${app}/${rankedCities.get(i).city().getSlug()}"
            }${i < Math.min(rankedCities.size(), 20) - 1 ? "," : ""}
            @endfor
          ]
        }
        </script>
    `
)
