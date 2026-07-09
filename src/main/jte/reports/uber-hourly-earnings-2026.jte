@import com.gigwager.model.CityEarningsSnapshot
@import com.gigwager.model.SeoMeta
@import com.gigwager.util.AppConstants
@import java.util.List

@param SeoMeta seoMeta
@param List<CityEarningsSnapshot> snapshots
@param CityEarningsSnapshot topSnapshot
@param double averageNetHourly
@param String lastUpdated
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
                    <span class="text-white">Uber hourly earnings report</span>
                </nav>

                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-3">Updated ${lastUpdated}</p>
                <h1 class="text-4xl md:text-6xl font-black leading-tight mb-5">
                    Uber Driver Hourly Earnings 2026
                    <span class="block text-emerald-300">City Net Pay Report</span>
                </h1>
                <p class="text-xl text-slate-300 max-w-3xl">
                    A focused comparison of Uber driver hourly earnings in Atlanta, Los Angeles, Austin, Chicago, Houston, and Orlando after mileage and self-employment tax assumptions.
                </p>
            </div>
        </div>

        <section class="max-w-5xl mx-auto px-6 -mt-8 relative z-10 mb-10">
            <div class="bg-white rounded-3xl border border-slate-200 shadow-xl overflow-hidden">
                <div class="grid md:grid-cols-3 divide-y md:divide-y-0 md:divide-x divide-slate-100">
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
                </div>
            </div>
        </section>

        <main class="max-w-5xl mx-auto px-6 pb-16 space-y-12">
            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Direct answer</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">What is the average Uber driver hourly earnings in 2026?</h2>
                <p class="text-slate-700 leading-relaxed max-w-3xl">
                    In this GigVerdict city sample, estimated Uber net hourly earnings average <strong>$${String.format("%.2f", averageNetHourly)}/hr after mileage and self-employment tax assumptions</strong>. Start with Atlanta, Los Angeles, Austin, Chicago, Houston, and Orlando because airport timing, traffic, event demand, and home-to-home miles change the real take-home number.
                </p>
            </section>

            <section class="bg-white rounded-3xl border border-slate-200 overflow-hidden shadow-sm">
                <div class="bg-slate-900 px-6 py-4">
                    <h2 class="text-xl font-black text-white">Uber hourly earnings snapshots</h2>
                    <p class="text-sm text-slate-300 mt-1">Side-hustle baseline after mileage proxy and self-employment tax assumptions.</p>
                </div>
                <div class="overflow-x-auto">
                    <table class="w-full min-w-[56rem] text-sm">
                        <thead>
                            <tr class="bg-slate-50 border-b border-slate-200">
                                <th scope="col" class="sticky left-0 z-20 bg-slate-50 px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase shadow-[1px_0_0_0_rgba(148,163,184,0.25)]">City</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Net hourly</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Gross/wk</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Miles/wk</th>
                                <th scope="col" class="px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase">Reader question</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Report</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-100">
                            @for(CityEarningsSnapshot snapshot : snapshots)
                                <tr>
                                    <td class="sticky left-0 z-10 bg-white px-4 py-4 shadow-[1px_0_0_0_rgba(226,232,240,1)]">
                                        <strong class="block text-slate-900">${snapshot.city().getCityName()}, ${snapshot.city().getState()}</strong>
                                        <span class="text-xs text-slate-500">${snapshot.acquisitionNote()}</span>
                                    </td>
                                    <td class="px-4 py-4 text-right font-black text-emerald-700">$${String.format("%.2f", snapshot.scenario().getNetHourly())}/hr</td>
                                    <td class="px-4 py-4 text-right text-slate-700">$${snapshot.scenario().getGrossWeekly()}</td>
                                    <td class="px-4 py-4 text-right text-slate-700">${snapshot.scenario().getMiles()}</td>
                                    <td class="px-4 py-4 text-slate-600">
                                        <strong class="block text-slate-900">${snapshot.primaryQuery()}</strong>
                                        <span class="text-xs">${snapshot.secondaryQuery()}</span>
                                    </td>
                                    <td class="px-4 py-4 text-right">
                                        <a href="/salary/uber/${snapshot.city().getSlug()}" class="inline-flex items-center justify-center rounded-xl bg-slate-900 px-4 py-2 text-white font-bold hover:bg-slate-800 transition-colors">
                                            Open city
                                        </a>
                                    </td>
                                </tr>
                            @endfor
                        </tbody>
                    </table>
                </div>
            </section>

            <section class="bg-emerald-50 rounded-3xl border border-emerald-200 p-8">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Crawl and referral bridge</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">Use the strongest city URL for the question</h2>
                <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-3">
                    @for(CityEarningsSnapshot snapshot : snapshots)
                        <article class="bg-white rounded-2xl border border-emerald-100 p-5">
                            <h3 class="text-lg font-bold text-slate-900 mb-2">${snapshot.city().getCityName()} hourly earnings path</h3>
                            <div class="space-y-2 text-sm">
                                <a href="/salary/uber/${snapshot.city().getSlug()}" class="block font-bold text-emerald-700 hover:text-emerald-800">City earnings report</a>
                                <a href="/salary/uber/${snapshot.city().getSlug()}/after-gas" class="block text-slate-700 hover:text-emerald-800">After gas and mileage</a>
                                <a href="/salary/uber/${snapshot.city().getSlug()}/active-time" class="block text-slate-700 hover:text-emerald-800">Active time vs online time</a>
                                <a href="/salary/uber/${snapshot.city().getSlug()}/100-a-day" class="block text-slate-700 hover:text-emerald-800">$100/day target</a>
                            </div>
                        </article>
                    @endfor
                </div>
            </section>

            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500 mb-2">Citation block</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">Shareable source text</h2>
                <div class="grid lg:grid-cols-2 gap-4">
                    <div class="rounded-2xl bg-slate-50 border border-slate-200 p-5">
                        <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-2">Canonical URL</p>
                        <p class="text-sm font-bold text-slate-900 break-all">${AppConstants.BASE_URL}/reports/uber-driver-hourly-earnings-2026</p>
                    </div>
                    <div class="rounded-2xl bg-slate-50 border border-slate-200 p-5">
                        <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-2">Suggested citation</p>
                        <p class="text-sm text-slate-700 leading-relaxed">
                            GigVerdict, "Uber Driver Hourly Earnings 2026: City Net Pay Report," updated ${lastUpdated}. Estimates compare city net hourly pay after mileage and self-employment tax assumptions.
                        </p>
                    </div>
                </div>
            </section>
        </main>
    `
)
