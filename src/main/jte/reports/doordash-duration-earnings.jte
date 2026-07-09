@import com.gigwager.model.DoorDashDurationEstimate
@import com.gigwager.model.DoorDashMoneyIntent
@import com.gigwager.model.DriverShiftReport
@import com.gigwager.model.SeoMeta
@import java.util.List

@param SeoMeta seoMeta
@param DoorDashDurationEstimate estimate
@param List<DoorDashDurationEstimate> allEstimates
@param List<DoorDashMoneyIntent> moneyIntents = java.util.List.of()
@param List<DriverShiftReport> driverShiftReports = java.util.List.of()
@param String lastUpdated
@param String durationJsonLd = "{}"
@param Boolean noIndex = false

@template.layout.main(
    noIndex = noIndex,
    seoMeta = seoMeta,
    content = @`
        <script type="application/ld+json">
        $unsafe{durationJsonLd}
        </script>

        <div class="bg-slate-950 text-white py-16 px-6">
            <div class="max-w-5xl mx-auto">
                <nav class="text-sm text-slate-400 mb-6 flex items-center gap-2 flex-wrap">
                    <a href="/" class="hover:text-emerald-300">Home</a>
                    <span>&gt;</span>
                    <a href="/doordash" class="hover:text-emerald-300">DoorDash Calculator</a>
                    <span>&gt;</span>
                    <span class="text-white">${estimate.displayName()} estimate</span>
                </nav>
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-3">Updated ${lastUpdated}</p>
                <h1 class="text-4xl md:text-6xl font-black leading-tight mb-5">
                    How Much Can You Make with DoorDash in ${estimate.displayName()}?
                    <span class="block text-emerald-300">$${estimate.netLow()}-$${estimate.netHigh()} Net Estimate</span>
                </h1>
                <p class="text-xl text-slate-300 max-w-4xl">
                    A direct gross-to-net DoorDash shift estimate for ${estimate.searchPhrase()}, with peak-window guidance, dense-market upside, and mileage/tax caveats.
                </p>
            </div>
        </div>

        <section class="max-w-5xl mx-auto px-6 -mt-8 relative z-10 mb-10">
            <div class="bg-white rounded-3xl border border-slate-200 shadow-xl overflow-hidden">
                <div class="grid md:grid-cols-4 divide-y md:divide-y-0 md:divide-x divide-slate-100">
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Typical gross</p>
                        <p class="text-3xl font-black text-slate-900">$${estimate.grossLow()}-$${estimate.grossHigh()}</p>
                    </div>
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Typical net</p>
                        <p class="text-3xl font-black text-slate-900">$${estimate.netLow()}-$${estimate.netHigh()}</p>
                    </div>
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Net hourly</p>
                        <p class="text-3xl font-black text-slate-900">$${String.format("%.2f", estimate.netHourlyLow())}-$${String.format("%.2f", estimate.netHourlyHigh())}</p>
                    </div>
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Dense-market gross</p>
                        <p class="text-3xl font-black text-slate-900">$${estimate.denseGrossLow()}-$${estimate.denseGrossHigh()}</p>
                    </div>
                </div>
            </div>
        </section>

        <main class="max-w-5xl mx-auto px-6 pb-16 space-y-12">
            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Direct answer</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">${estimate.searchPhrase()}?</h2>
                <p class="text-slate-700 leading-relaxed max-w-4xl">
                    A typical DoorDash ${estimate.displayName().toLowerCase(java.util.Locale.US)} plan models about <strong>$${estimate.grossLow()}-$${estimate.grossHigh()} gross</strong> and roughly <strong>$${estimate.netLow()}-$${estimate.netHigh()} net</strong> after mileage, vehicle-cost pressure, and self-employment tax reserves. In dense markets, the gross upside can stretch toward <strong>$${estimate.denseGrossLow()}-$${estimate.denseGrossHigh()}</strong>, but only when pickup wait time and return miles stay low.
                </p>
            </section>

            <section class="grid lg:grid-cols-3 gap-4">
                <article class="rounded-3xl bg-emerald-50 border border-emerald-100 p-6">
                    <p class="text-xs font-black uppercase tracking-[0.16em] text-emerald-700 mb-2">Best window</p>
                    <h3 class="text-xl font-black text-slate-900 mb-2">${estimate.bestWindow()}</h3>
                    <p class="text-sm text-slate-600">The estimate assumes the driver is not spending most of the block waiting for pings or repositioning across zones.</p>
                </article>
                <article class="rounded-3xl bg-amber-50 border border-amber-100 p-6">
                    <p class="text-xs font-black uppercase tracking-[0.16em] text-amber-700 mb-2">Weak window</p>
                    <h3 class="text-xl font-black text-slate-900 mb-2">${estimate.weakWindow()}</h3>
                    <p class="text-sm text-slate-600">This is where gross pay can look acceptable while all-in hourly pay drops after unpaid time.</p>
                </article>
                <article class="rounded-3xl bg-slate-50 border border-slate-200 p-6">
                    <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-2">Planning note</p>
                    <h3 class="text-xl font-black text-slate-900 mb-2">Gross is not take-home</h3>
                    <p class="text-sm text-slate-600">${estimate.strategyNote()}</p>
                </article>
            </section>

            <section class="bg-white rounded-3xl border border-slate-200 overflow-hidden shadow-sm">
                <div class="bg-slate-900 px-6 py-4">
                    <h2 class="text-xl font-black text-white">DoorDash duration estimates</h2>
                    <p class="text-sm text-slate-300 mt-1">Use these adjacent pages when the searcher's real question is a different shift length.</p>
                </div>
                <div class="overflow-x-auto">
                    <table class="w-full min-w-[52rem] text-sm">
                        <thead>
                            <tr class="bg-slate-50 border-b border-slate-200">
                                <th scope="col" class="px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase">Shift length</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Gross</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Net</th>
                                <th scope="col" class="px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase">Best window</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Open</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-100">
                            @for(DoorDashDurationEstimate item : allEstimates)
                                <tr class="${item.slug().equals(estimate.slug()) ? "bg-emerald-50" : ""}">
                                    <td class="px-4 py-4 font-bold text-slate-900">${item.displayName()}</td>
                                    <td class="px-4 py-4 text-right text-slate-700">$${item.grossLow()}-$${item.grossHigh()}</td>
                                    <td class="px-4 py-4 text-right font-black text-emerald-700">$${item.netLow()}-$${item.netHigh()}</td>
                                    <td class="px-4 py-4 text-slate-600">${item.bestWindow()}</td>
                                    <td class="px-4 py-4 text-right">
                                        <a href="/doordash/how-much-can-you-make-in-${item.slug()}" class="font-bold text-emerald-700 hover:text-emerald-800">Open</a>
                                    </td>
                                </tr>
                            @endfor
                        </tbody>
                    </table>
                </div>
            </section>

            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Money-intent bridge</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">If the real question is the cash target</h2>
                <p class="text-slate-600 max-w-3xl mb-6">
                    Strong pSEO pages do not leave the next question unanswered. Use these pages when the searcher cares less about ${estimate.displayName().toLowerCase(java.util.Locale.US)} and more about the daily target, gas drag, or per-mile floor.
                </p>
                <div class="grid md:grid-cols-2 gap-3">
                    @for(DoorDashMoneyIntent item : moneyIntents)
                        <a href="${item.path()}" class="rounded-2xl bg-slate-50 border border-slate-200 px-5 py-4 hover:border-emerald-300 transition-colors">
                            <strong class="block text-slate-900">${item.headline()}</strong>
                            <span class="text-sm text-slate-600">${item.netRange()} net | ${item.hourRange()} | ${item.mileRange()}</span>
                        </a>
                    @endfor
                </div>
            </section>

            <section class="bg-slate-900 rounded-3xl border border-slate-800 p-8 text-white shadow-sm">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-2">Goal stress test</p>
                <h2 class="text-3xl font-black mb-5">Can this shift support $100/day or $200/day?</h2>
                <div class="grid md:grid-cols-3 gap-4">
                    <article class="rounded-2xl bg-white/5 border border-white/10 p-5">
                        <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-400 mb-2">$100/day check</p>
                        <p class="text-3xl font-black text-emerald-300 mb-2">
                            @if(estimate.netHigh() >= 100)
                                Possible
                            @else
                                Needs more time
                            @endif
                        </p>
                        <p class="text-sm text-slate-300">This ${estimate.displayName().toLowerCase(java.util.Locale.US)} model nets $${estimate.netLow()}-$${estimate.netHigh()}; a $100 net day needs the upper end or a second peak block.</p>
                    </article>
                    <article class="rounded-2xl bg-white/5 border border-white/10 p-5">
                        <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-400 mb-2">$200/day check</p>
                        <p class="text-3xl font-black text-emerald-300 mb-2">
                            @if(estimate.netHigh() >= 200)
                                Stretch
                            @else
                                Not enough
                            @endif
                        </p>
                        <p class="text-sm text-slate-300">$200 net usually needs a long two-peak plan, not just one ${estimate.displayName().toLowerCase(java.util.Locale.US)} shift.</p>
                    </article>
                    <article class="rounded-2xl bg-white/5 border border-white/10 p-5">
                        <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-400 mb-2">After-gas check</p>
                        <p class="text-3xl font-black text-emerald-300 mb-2">$${String.format("%.2f", estimate.netHourlyLow())}-$${String.format("%.2f", estimate.netHourlyHigh())}/hr</p>
                        <p class="text-sm text-slate-300">This is the modeled net hourly range after mileage and tax pressure, not only the app payout.</p>
                    </article>
                </div>
            </section>

            @template.components.driver_shift_reports(
                appName = "DoorDash",
                cityName = estimate.displayName() + " DoorDash shifts",
                modeledNetHourly = estimate.midpointNet() / estimate.hours(),
                driverShiftReports = driverShiftReports,
                sectionClass = "max-w-5xl mx-auto"
            )

            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <h2 class="text-3xl font-black text-slate-900 mb-6">Frequently Asked Questions</h2>
                <div class="space-y-6">
                    <div>
                        <h3 class="text-xl font-bold text-slate-900 mb-2">How much can you make with DoorDash in ${estimate.displayName().toLowerCase(java.util.Locale.US)}?</h3>
                        <p class="text-slate-600">A typical DoorDash ${estimate.displayName().toLowerCase(java.util.Locale.US)} block models about $${estimate.grossLow()}-$${estimate.grossHigh()} gross and $${estimate.netLow()}-$${estimate.netHigh()} net after mileage and tax reserves.</p>
                    </div>
                    <div>
                        <h3 class="text-xl font-bold text-slate-900 mb-2">What is the best time to DoorDash for this target?</h3>
                        <p class="text-slate-600">${estimate.bestWindow()}</p>
                    </div>
                    <div>
                        <h3 class="text-xl font-bold text-slate-900 mb-2">What makes this estimate fall apart?</h3>
                        <p class="text-slate-600">${estimate.weakWindow()} ${estimate.strategyNote()}</p>
                    </div>
                </div>
            </section>

            <section class="grid lg:grid-cols-3 gap-4">
                <a href="/reports/doordash-driver-shift-evidence-2026" class="rounded-3xl bg-white border border-slate-200 p-6 hover:border-emerald-300 transition-colors">
                    <strong class="block text-slate-900 text-lg mb-2">DoorDash shift evidence</strong>
                    <span class="text-sm text-slate-600">Gross, miles, active time, dash time, and modeled net checks.</span>
                </a>
                <a href="/reports/doordash-driver-hourly-pay-2026" class="rounded-3xl bg-white border border-slate-200 p-6 hover:border-emerald-300 transition-colors">
                    <strong class="block text-slate-900 text-lg mb-2">DoorDash hourly pay 2026</strong>
                    <span class="text-sm text-slate-600">City-by-city pay dataset after mileage and tax assumptions.</span>
                </a>
                <a href="/doordash/adjustment-pay-calculator" class="rounded-3xl bg-white border border-slate-200 p-6 hover:border-emerald-300 transition-colors">
                    <strong class="block text-slate-900 text-lg mb-2">DoorDash adjustment pay calculator</strong>
                    <span class="text-sm text-slate-600">Check whether active-time minimum pay changes the shift result.</span>
                </a>
                <a href="/salary/doordash/phoenix/how-much-can-you-make" class="rounded-3xl bg-white border border-slate-200 p-6 hover:border-emerald-300 transition-colors">
                    <strong class="block text-slate-900 text-lg mb-2">City-specific earning path</strong>
                    <span class="text-sm text-slate-600">Compare the general duration estimate against a local city model.</span>
                </a>
            </section>
        </main>
    `
)
