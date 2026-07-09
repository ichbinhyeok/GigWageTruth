@import com.gigwager.model.CityEarningsSnapshot
@import com.gigwager.model.DriverShiftReport
@import com.gigwager.model.SeoMeta
@import com.gigwager.util.AppConstants
@import java.util.List

@param SeoMeta seoMeta
@param List<CityEarningsSnapshot> snapshots
@param CityEarningsSnapshot topSnapshot
@param double averageNetHourly
@param String lastUpdated
@param List<DriverShiftReport> driverShiftReports = java.util.List.of()
@param String reportJsonLd = "{}"
@param Boolean noIndex = false

@template.layout.main(
    noIndex = noIndex,
    seoMeta = seoMeta,
    content = @`
        <script type="application/ld+json">
        $unsafe{reportJsonLd}
        </script>

        <div class="bg-slate-950 text-white py-16 px-6">
            <div class="max-w-5xl mx-auto">
                <nav class="text-sm text-slate-400 mb-6 flex items-center gap-2 flex-wrap">
                    <a href="/" class="hover:text-emerald-300">Home</a>
                    <span>&gt;</span>
                    <a href="/salary/directory" class="hover:text-emerald-300">City Earnings</a>
                    <span>&gt;</span>
                    <span class="text-white">DoorDash hourly pay report</span>
                </nav>

                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-3">Updated ${lastUpdated}</p>
                <h1 class="text-4xl md:text-6xl font-black leading-tight mb-5">
                    DoorDash Driver Hourly Pay 2026
                    <span class="block text-emerald-300">City Earnings Report</span>
                </h1>
                <p class="text-xl text-slate-300 max-w-3xl">
                    A city-by-city DoorDash hourly pay dataset for drivers who need more than a national average. Compare net hourly pay, weekly net, hours to $100, and the local follow-up page for each market.
                </p>
            </div>
        </div>

        <section class="max-w-5xl mx-auto px-6 -mt-8 relative z-10 mb-10">
            <div class="bg-white rounded-3xl border border-slate-200 shadow-xl overflow-hidden">
                <div class="grid md:grid-cols-4 divide-y md:divide-y-0 md:divide-x divide-slate-100">
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Cities compared</p>
                        <p class="text-3xl font-black text-slate-900">${snapshots.size()}</p>
                    </div>
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Average net hourly</p>
                        <p class="text-3xl font-black text-slate-900">$${String.format("%.2f", averageNetHourly)}/hr</p>
                    </div>
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Highest snapshot</p>
                        <p class="text-3xl font-black text-slate-900">${topSnapshot.city().getCityName()}</p>
                    </div>
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Fastest $100 path</p>
                        <p class="text-3xl font-black text-slate-900">${String.format("%.1f", 100.0 / topSnapshot.scenario().getNetHourly())} hrs</p>
                    </div>
                </div>
            </div>
        </section>

        <main class="max-w-5xl mx-auto px-6 pb-16 space-y-12">
            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Direct answer</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">What is the average DoorDash driver hourly pay in 2026?</h2>
                <p class="text-slate-700 leading-relaxed max-w-3xl">
                    In this GigVerdict city sample, estimated DoorDash net hourly pay averages <strong>$${String.format("%.2f", averageNetHourly)}/hr after mileage and self-employment tax assumptions</strong>. The better question is local: New York, Denver, Phoenix, Indianapolis, San Jose, Dallas, Los Angeles, and Chicago behave differently because parking, traffic, order density, and zone access change active time.
                </p>
            </section>

            <section class="bg-white rounded-3xl border border-slate-200 overflow-hidden shadow-sm">
                <div class="bg-slate-900 px-6 py-4">
                    <h2 class="text-xl font-black text-white">DoorDash hourly pay city snapshots</h2>
                    <p class="text-sm text-slate-300 mt-1">Side-hustle baseline after mileage proxy and self-employment tax assumptions.</p>
                </div>
                <div class="overflow-x-auto">
                    <table class="w-full min-w-[66rem] text-sm">
                        <thead>
                            <tr class="bg-slate-50 border-b border-slate-200">
                                <th scope="col" class="px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase">City</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Net hourly</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Net/wk</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Hours to $100</th>
                                <th scope="col" class="px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase">Search query to defend</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Report</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-100">
                            @for(CityEarningsSnapshot snapshot : snapshots)
                                <tr>
                                    <td class="px-4 py-4">
                                        <strong class="block text-slate-900">${snapshot.city().getCityName()}, ${snapshot.city().getState()}</strong>
                                        <span class="text-xs text-slate-500">${snapshot.acquisitionNote()}</span>
                                    </td>
                                    <td class="px-4 py-4 text-right font-black text-emerald-700">$${String.format("%.2f", snapshot.scenario().getNetHourly())}/hr</td>
                                    <td class="px-4 py-4 text-right text-slate-700">$${String.format("%.0f", snapshot.scenario().getNetProfit())}</td>
                                    <td class="px-4 py-4 text-right text-slate-700">${String.format("%.1f", 100.0 / snapshot.scenario().getNetHourly())}</td>
                                    <td class="px-4 py-4 text-slate-600">
                                        <strong class="block text-slate-900">${snapshot.primaryQuery()}</strong>
                                        <span class="text-xs">${snapshot.secondaryQuery()}</span>
                                    </td>
                                    <td class="px-4 py-4 text-right">
                                        <a href="/salary/doordash/${snapshot.city().getSlug()}" class="inline-flex items-center justify-center rounded-xl bg-slate-900 px-4 py-2 text-white font-bold hover:bg-slate-800 transition-colors">
                                            Open city
                                        </a>
                                    </td>
                                </tr>
                            @endfor
                        </tbody>
                    </table>
                </div>
            </section>

            @template.components.driver_shift_reports(
                appName = "DoorDash",
                cityName = "the 2026 hourly pay sample",
                modeledNetHourly = averageNetHourly,
                driverShiftReports = driverShiftReports,
                sectionClass = "max-w-5xl mx-auto"
            )

            <section class="bg-emerald-50 rounded-3xl border border-emerald-200 p-8">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Driver decision paths</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">Use the right page for the job you are trying to answer</h2>
                <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-3">
                    @for(CityEarningsSnapshot snapshot : snapshots)
                        <article class="bg-white rounded-2xl border border-emerald-100 p-5">
                            <h3 class="text-lg font-bold text-slate-900 mb-2">${snapshot.city().getCityName()} DoorDash pay path</h3>
                            <div class="space-y-2 text-sm">
                                <a href="/salary/doordash/${snapshot.city().getSlug()}" class="block font-bold text-emerald-700 hover:text-emerald-800">City earnings report</a>
                                <a href="/salary/doordash/${snapshot.city().getSlug()}/after-gas" class="block text-slate-700 hover:text-emerald-800">After gas and mileage</a>
                                <a href="/salary/doordash/${snapshot.city().getSlug()}/active-time" class="block text-slate-700 hover:text-emerald-800">Active time vs dash time</a>
                                <a href="/salary/doordash/${snapshot.city().getSlug()}/100-a-day" class="block text-slate-700 hover:text-emerald-800">$100/day target</a>
                            </div>
                        </article>
                    @endfor
                </div>
            </section>

            <section class="grid lg:grid-cols-2 gap-4">
                <article class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                    <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500 mb-2">Ranking bridge</p>
                    <h2 class="text-2xl font-black text-slate-900 mb-3">Looking for the highest paying DoorDash cities near you?</h2>
                    <p class="text-slate-600 mb-5">Use the city ranking when you want the full ordered list instead of this eight-city hourly-pay snapshot.</p>
                    <a href="/best-cities/doordash" class="inline-flex items-center justify-center rounded-xl bg-emerald-600 px-5 py-3 text-white font-bold hover:bg-emerald-700 transition-colors">Open DoorDash city ranking</a>
                </article>
                <article class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                    <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500 mb-2">Availability bridge</p>
                    <h2 class="text-2xl font-black text-slate-900 mb-3">Pay does not matter if your zone is not open</h2>
                    <p class="text-slate-600 mb-5">Check DoorDash availability before assuming a high-ranking city is active for onboarding or dashing.</p>
                    <a href="/doordash/where-you-can-dash" class="inline-flex items-center justify-center rounded-xl bg-slate-900 px-5 py-3 text-white font-bold hover:bg-slate-800 transition-colors">Check where you can dash</a>
                </article>
            </section>

            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500 mb-2">Citation block</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">Shareable source text</h2>
                <div class="grid lg:grid-cols-2 gap-4">
                    <div class="rounded-2xl bg-slate-50 border border-slate-200 p-5">
                        <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-2">Canonical URL</p>
                        <p class="text-sm font-bold text-slate-900 break-all">${AppConstants.BASE_URL}/reports/doordash-driver-hourly-pay-2026</p>
                    </div>
                    <div class="rounded-2xl bg-slate-50 border border-slate-200 p-5">
                        <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-2">Suggested citation</p>
                        <p class="text-sm text-slate-700 leading-relaxed">
                            GigVerdict, "DoorDash Driver Hourly Pay 2026: City Earnings Report," updated ${lastUpdated}. Estimates compare city net hourly pay after mileage and self-employment tax assumptions.
                        </p>
                    </div>
                </div>
            </section>
        </main>
    `
)
