@import com.gigwager.model.DoorDashDurationEstimate
@import com.gigwager.model.DoorDashMoneyIntent
@import com.gigwager.model.DriverShiftReport
@import com.gigwager.model.SeoMeta
@import java.util.List

@param SeoMeta seoMeta
@param DoorDashMoneyIntent intent
@param List<DoorDashMoneyIntent> moneyIntents
@param List<DoorDashDurationEstimate> durationEstimates
@param List<DriverShiftReport> driverShiftReports = java.util.List.of()
@param String lastUpdated
@param String moneyIntentJsonLd = "{}"
@param Boolean noIndex = false

@template.layout.main(
    noIndex = noIndex,
    seoMeta = seoMeta,
    content = @`
        <script type="application/ld+json">
        $unsafe{moneyIntentJsonLd}
        </script>

        <div class="bg-slate-950 text-white py-16 px-6">
            <div class="max-w-5xl mx-auto">
                <nav class="text-sm text-slate-400 mb-6 flex items-center gap-2 flex-wrap">
                    <a href="/" class="hover:text-emerald-300">Home</a>
                    <span>&gt;</span>
                    <a href="/doordash" class="hover:text-emerald-300">DoorDash Calculator</a>
                    <span>&gt;</span>
                    <span class="text-white">${intent.displayName()}</span>
                </nav>
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-3">Updated ${lastUpdated}</p>
                <h1 class="text-4xl md:text-6xl font-black leading-tight mb-5">
                    ${intent.headline()}
                    <span class="block text-emerald-300">${intent.primaryMetricValue()} ${intent.primaryMetricLabel()}</span>
                </h1>
                <p class="text-xl text-slate-300 max-w-4xl">
                    ${intent.answerLead()} This page keeps gross, net, hours, miles, and the failure mode visible so the answer is not just a payout screenshot.
                </p>
            </div>
        </div>

        <section class="max-w-5xl mx-auto px-6 -mt-8 relative z-10 mb-10">
            <div class="bg-white rounded-3xl border border-slate-200 shadow-xl overflow-hidden">
                <div class="grid md:grid-cols-4 divide-y md:divide-y-0 md:divide-x divide-slate-100">
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Gross range</p>
                        <p class="text-3xl font-black text-slate-900">${intent.grossRange()}</p>
                    </div>
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Net range</p>
                        <p class="text-3xl font-black text-slate-900">${intent.netRange()}</p>
                    </div>
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Modeled hours</p>
                        <p class="text-3xl font-black text-slate-900">${intent.hourRange()}</p>
                    </div>
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Mileage load</p>
                        <p class="text-3xl font-black text-slate-900">${intent.mileRange()}</p>
                    </div>
                </div>
            </div>
        </section>

        <main class="max-w-5xl mx-auto px-6 pb-16 space-y-12">
            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Direct answer</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">${intent.searchPhrase()}?</h2>
                <p class="text-slate-700 leading-relaxed max-w-4xl">
                    ${intent.answerLead()} The working range is <strong>${intent.grossRange()} gross</strong> and <strong>${intent.netRange()} net</strong> across about <strong>${intent.hourRange()}</strong> and <strong>${intent.mileRange()}</strong>. The estimate assumes the driver is tracking miles, setting aside tax, and not treating gas as the only cost.
                </p>
            </section>

            <section class="grid lg:grid-cols-3 gap-4">
                <article class="rounded-3xl bg-emerald-50 border border-emerald-100 p-6">
                    <p class="text-xs font-black uppercase tracking-[0.16em] text-emerald-700 mb-2">Best setup</p>
                    <h3 class="text-xl font-black text-slate-900 mb-2">${intent.bestWindow()}</h3>
                    <p class="text-sm text-slate-600">This is the version of the shift where the search query has a realistic shot at matching take-home pay.</p>
                </article>
                <article class="rounded-3xl bg-amber-50 border border-amber-100 p-6">
                    <p class="text-xs font-black uppercase tracking-[0.16em] text-amber-700 mb-2">Failure mode</p>
                    <h3 class="text-xl font-black text-slate-900 mb-2">Where the number breaks</h3>
                    <p class="text-sm text-slate-600">${intent.failureMode()}</p>
                </article>
                <article class="rounded-3xl bg-slate-50 border border-slate-200 p-6">
                    <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-2">Driver rule</p>
                    <h3 class="text-xl font-black text-slate-900 mb-2">${intent.secondaryMetricValue()} ${intent.secondaryMetricLabel()}</h3>
                    <p class="text-sm text-slate-600">${intent.decisionRule()}</p>
                </article>
            </section>

            <section class="bg-white rounded-3xl border border-slate-200 overflow-hidden shadow-sm">
                <div class="bg-slate-900 px-6 py-4">
                    <h2 class="text-xl font-black text-white">DoorDash money-intent calculator table</h2>
                    <p class="text-sm text-slate-300 mt-1">The winner pattern is a fast answer plus enough math to reject bad shifts.</p>
                </div>
                <div class="overflow-x-auto">
                    <table class="w-full min-w-[56rem] text-sm">
                        <thead>
                            <tr class="bg-slate-50 border-b border-slate-200">
                                <th scope="col" class="px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase">Question</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Gross</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Net</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Hours</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Miles</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Open</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-100">
                            @for(DoorDashMoneyIntent item : moneyIntents)
                                <tr class="${item.slug().equals(intent.slug()) ? "bg-emerald-50" : ""}">
                                    <td class="px-4 py-4">
                                        <p class="font-bold text-slate-900">${item.headline()}</p>
                                        <p class="text-xs text-slate-500 mt-1">${item.searchPhrase()}</p>
                                    </td>
                                    <td class="px-4 py-4 text-right text-slate-700">${item.grossRange()}</td>
                                    <td class="px-4 py-4 text-right font-black text-emerald-700">${item.netRange()}</td>
                                    <td class="px-4 py-4 text-right text-slate-700">${item.hourRange()}</td>
                                    <td class="px-4 py-4 text-right text-slate-700">${item.mileRange()}</td>
                                    <td class="px-4 py-4 text-right">
                                        <a href="${item.path()}" class="font-bold text-emerald-700 hover:text-emerald-800">Open</a>
                                    </td>
                                </tr>
                            @endfor
                        </tbody>
                    </table>
                </div>
            </section>

            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">City-specific routes</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">Turn the generic money query into a city plan</h2>
                <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-3">
                    <a href="/salary/doordash/phoenix/after-gas" class="rounded-2xl bg-slate-50 border border-slate-200 px-5 py-4 hover:border-emerald-300 transition-colors">
                        <strong class="block text-slate-900">DoorDash pay in Phoenix after gas</strong>
                        <span class="text-sm text-slate-600">Local fuel, miles, and net hourly check.</span>
                    </a>
                    <a href="/salary/doordash/dallas/nights-weekends" class="rounded-2xl bg-slate-50 border border-slate-200 px-5 py-4 hover:border-emerald-300 transition-colors">
                        <strong class="block text-slate-900">Best DoorDash hours in Dallas</strong>
                        <span class="text-sm text-slate-600">Dinner, weekend, and slow-window planning.</span>
                    </a>
                    <a href="/salary/doordash/las-vegas/100-a-day" class="rounded-2xl bg-slate-50 border border-slate-200 px-5 py-4 hover:border-emerald-300 transition-colors">
                        <strong class="block text-slate-900">Can DoorDash make $100/day in Las Vegas?</strong>
                        <span class="text-sm text-slate-600">Tourism-market hours and mileage target.</span>
                    </a>
                    <a href="/salary/doordash/chicago/uber-eats-vs-doordash" class="rounded-2xl bg-slate-50 border border-slate-200 px-5 py-4 hover:border-emerald-300 transition-colors">
                        <strong class="block text-slate-900">DoorDash vs Uber Eats in Chicago</strong>
                        <span class="text-sm text-slate-600">Compare delivery-app net pay and active-time risk.</span>
                    </a>
                    <a href="/salary/doordash/dallas/per-mile" class="rounded-2xl bg-slate-50 border border-slate-200 px-5 py-4 hover:border-emerald-300 transition-colors">
                        <strong class="block text-slate-900">DoorDash Dallas pay per mile</strong>
                        <span class="text-sm text-slate-600">Offer floor and dead-mile check.</span>
                    </a>
                    <a href="/salary/doordash/phoenix/how-much-can-you-make" class="rounded-2xl bg-slate-50 border border-slate-200 px-5 py-4 hover:border-emerald-300 transition-colors">
                        <strong class="block text-slate-900">How much can you make in Phoenix?</strong>
                        <span class="text-sm text-slate-600">Weekly, daily, and hourly city math.</span>
                    </a>
                </div>
            </section>

            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500 mb-2">Duration bridge</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">If the real question is shift length</h2>
                <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-3">
                    @for(DoorDashDurationEstimate estimate : durationEstimates)
                        <a href="/doordash/how-much-can-you-make-in-${estimate.slug()}" class="rounded-2xl bg-slate-50 border border-slate-200 px-5 py-4 hover:border-emerald-300 transition-colors">
                            <strong class="block text-slate-900">DoorDash ${estimate.displayName()} earnings</strong>
                            <span class="text-sm text-slate-600">$${estimate.netLow()}-$${estimate.netHigh()} net estimate.</span>
                        </a>
                    @endfor
                </div>
            </section>

            <section class="bg-emerald-50 rounded-3xl border border-emerald-200 p-8 shadow-sm">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Local minimum pay bridge</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">If the missing piece is adjustment pay</h2>
                <p class="text-slate-700 leading-relaxed max-w-4xl mb-5">
                    California Prop 22, New York City, and Seattle can change the answer because adjustment pay compares local guaranteed earnings against DoorDash pay before tips. Use the dedicated calculator before treating a gross payout as the final result.
                </p>
                <a href="/doordash/adjustment-pay-calculator" class="inline-flex items-center justify-center rounded-xl bg-slate-900 px-5 py-3 text-white font-bold hover:bg-slate-800 transition-colors">
                    Open DoorDash adjustment pay calculator
                </a>
            </section>

            @template.components.driver_shift_reports(
                appName = "DoorDash",
                cityName = intent.displayName() + " money-intent checks",
                modeledNetHourly = (intent.netLow() + intent.netHigh()) / 2.0 / Math.max(intent.hoursHigh(), 1.0),
                driverShiftReports = driverShiftReports,
                sectionClass = "max-w-5xl mx-auto"
            )

            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <h2 class="text-3xl font-black text-slate-900 mb-6">Frequently Asked Questions</h2>
                <div class="space-y-6">
                    <div>
                        <h3 class="text-xl font-bold text-slate-900 mb-2">${intent.headline()}</h3>
                        <p class="text-slate-600">${intent.answerLead()} Typical gross is ${intent.grossRange()} and typical net is ${intent.netRange()} across about ${intent.hourRange()}.</p>
                    </div>
                    <div>
                        <h3 class="text-xl font-bold text-slate-900 mb-2">What makes this DoorDash estimate fail?</h3>
                        <p class="text-slate-600">${intent.failureMode()}</p>
                    </div>
                    <div>
                        <h3 class="text-xl font-bold text-slate-900 mb-2">What rule should a driver use first?</h3>
                        <p class="text-slate-600">${intent.decisionRule()}</p>
                    </div>
                </div>
            </section>
        </main>
    `
)
