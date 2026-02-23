@import com.gigwager.model.SeoMeta
@import com.gigwager.dto.CityRankingDto
@import java.util.List

@param String app
@param String appName
@param List<CityRankingDto> topCities
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
                    <span class="inline-block px-4 py-1 rounded-full bg-emerald-100 text-emerald-800 font-bold text-sm tracking-wider mb-4 uppercase">
                        ${appName} Hub
                    </span>
                    <h1 class="text-4xl lg:text-5xl font-black text-slate-900 mb-6 tracking-tight">
                        Real ${appName} Pay Calculator & City Guide
                    </h1>
                    <p class="text-xl text-slate-600 max-w-2xl mx-auto">
                        Stop guessing your take-home pay. See real net hourly estimates for ${appName} drivers after gas and taxes in major cities. Last updated: ${lastUpdated}.
                    </p>
                </div>

                <div class="bg-white rounded-3xl p-8 lg:p-12 border border-slate-200 shadow-sm mb-12">
                    <h2 class="text-2xl font-bold text-slate-900 mb-4">What is "Net Hourly After Expenses"?</h2>
                    <div class="prose prose-slate max-w-none text-slate-600">
                        <p>Most ${appName} drivers look at their gross earnings and think they're doing well, but that number is a dangerous illusion. <strong>Net hourly pay</strong> is what you actually keep after deducting your true business costs.</p>
                        <h3 class="text-xl font-bold mt-6 mb-2">How our calculation works:</h3>
                        <ul class="list-disc pl-5 mt-2 space-y-2">
                            <li><strong>Gross Pay:</strong> Estimated based on local market economy tiers and minimum wage baselines.</li>
                            <li><strong>Vehicle Expenses:</strong> We deduct the IRS Standard Mileage Rate (72.5¢ per mile for 2026), which acts as a proxy for gas, maintenance, and vehicle depreciation.</li>
                            <li><strong>Self-Employment Tax:</strong> We deduct an estimated 15.3% tax on your net profit (Medicare & Social Security).</li>
                        </ul>
                        <p class="mt-4 italic text-sm text-slate-500">Note: These are personalized estimates to help you understand your True Cost of Ownership. This is not financial or tax advice.</p>
                    </div>
                </div>

                <div class="bg-white rounded-3xl overflow-hidden border border-slate-200 shadow-sm mb-8">
                    <div class="bg-slate-900 p-6 flex justify-between items-center">
                        <h2 class="text-xl font-bold text-white">Top 10 Cities for ${appName}</h2>
                        <a href="/best-cities/${app}" class="text-emerald-400 hover:text-emerald-300 font-bold text-sm">See Full Ranking &rarr;</a>
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
                                            <a href="/salary/${app}/${ranking.city().getSlug()}" class="inline-block px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold rounded-lg text-sm transition-colors">View Report</a>
                                        </td>
                                    </tr>
                                @endfor
                            </tbody>
                        </table>
                    </div>
                </div>
                
                <div class="text-center mt-12">
                    <h3 class="text-2xl font-bold text-slate-900 mb-6">Explore the Directory</h3>
                    <a href="/salary/directory" class="inline-block px-8 py-4 bg-emerald-600 hover:bg-emerald-700 text-white font-bold rounded-xl shadow-lg shadow-emerald-600/20 transition-all">View All Cities Directory</a>
                </div>
            </div>
        </div>
    `
)
