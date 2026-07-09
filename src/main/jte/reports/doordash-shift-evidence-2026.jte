@import com.gigwager.model.DriverShiftReport
@import com.gigwager.model.SeoMeta
@import com.gigwager.util.AppConstants
@import java.util.List

@param SeoMeta seoMeta
@param List<DriverShiftReport> driverShiftReports
@param int reportCount
@param long citySpecificCount
@param long netCheckCount
@param double averageReportedNetHourly
@param String lastUpdated
@param String evidenceJsonLd = "{}"
@param Boolean noIndex = false

@template.layout.main(
    noIndex = noIndex,
    seoMeta = seoMeta,
    content = @`
        <script type="application/ld+json">
        $unsafe{evidenceJsonLd}
        </script>

        <div class="bg-slate-950 text-white py-16 px-6">
            <div class="max-w-6xl mx-auto">
                <nav class="text-sm text-slate-400 mb-6 flex items-center gap-2 flex-wrap">
                    <a href="/" class="hover:text-emerald-300">Home</a>
                    <span>&gt;</span>
                    <a href="/salary/directory" class="hover:text-emerald-300">City Earnings</a>
                    <span>&gt;</span>
                    <span class="text-white">DoorDash shift evidence</span>
                </nav>
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-3">Updated ${lastUpdated}</p>
                <h1 class="text-4xl md:text-6xl font-black leading-tight mb-5">
                    DoorDash Driver Shift Evidence 2026
                    <span class="block text-emerald-300">Gross, Miles, Active Time, Net Checks</span>
                </h1>
                <p class="text-xl text-slate-300 max-w-4xl">
                    Reviewed public shift reports and driver discussions used to sanity-check DoorDash gross pay, active time, dash time, mileage, and modeled net pay after the IRS mileage proxy and self-employment tax assumptions.
                </p>
            </div>
        </div>

        <section class="max-w-6xl mx-auto px-6 -mt-8 relative z-10 mb-10">
            <div class="bg-white rounded-3xl border border-slate-200 shadow-xl overflow-hidden">
                <div class="grid md:grid-cols-4 divide-y md:divide-y-0 md:divide-x divide-slate-100">
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Reviewed reports</p>
                        <p class="text-3xl font-black text-slate-900">${reportCount}</p>
                    </div>
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">City-specific reports</p>
                        <p class="text-3xl font-black text-slate-900">${citySpecificCount}</p>
                    </div>
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Net-check reports</p>
                        <p class="text-3xl font-black text-slate-900">${netCheckCount}</p>
                    </div>
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Avg reported net check</p>
                        <p class="text-3xl font-black text-slate-900">$${String.format("%.2f", averageReportedNetHourly)}/hr</p>
                    </div>
                </div>
            </div>
        </section>

        <main class="max-w-6xl mx-auto px-6 pb-16 space-y-12">
            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Modeled vs reported</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">Why this evidence hub exists</h2>
                <p class="text-slate-700 leading-relaxed max-w-4xl">
                    GigVerdict city pages are modeled estimates. This page is the reported-evidence layer: public shift tests, city-specific driver discussions, and active-time examples that show where gross screenshots can diverge from take-home pay. When a report includes gross pay, miles, and time, we calculate a rough net check using the same mileage and self-employment tax assumptions used across the site.
                </p>
            </section>

            <section class="bg-white rounded-3xl border border-slate-200 overflow-hidden shadow-sm">
                <div class="bg-slate-900 px-6 py-4">
                    <h2 class="text-xl font-black text-white">DoorDash shift evidence table</h2>
                    <p class="text-sm text-slate-300 mt-1">Gross, clocks, mileage, source label, and modeled net checks where enough data exists.</p>
                </div>
                <div class="overflow-x-auto">
                    <table class="w-full min-w-[78rem] text-sm">
                        <thead>
                            <tr class="bg-slate-50 border-b border-slate-200">
                                <th scope="col" class="px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase">Market</th>
                                <th scope="col" class="px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase">Reported result</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Gross</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Active</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Dash time</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Miles</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Gross/hr</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Net check</th>
                                <th scope="col" class="px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase">Source</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-100">
                            @for(DriverShiftReport report : driverShiftReports)
                                <tr>
                                    <td class="px-4 py-4">
                                        <strong class="block text-slate-900">${report.cityName()}, ${report.state()}</strong>
                                        <span class="text-xs text-slate-500">${report.shiftWindow()}</span>
                                    </td>
                                    <td class="px-4 py-4 text-slate-700">
                                        <strong class="block text-slate-900">${report.reportedResult()}</strong>
                                        <span class="text-xs text-slate-500">${report.takeaway()}</span>
                                    </td>
                                    <td class="px-4 py-4 text-right font-bold text-slate-700">
                                        @if(report.hasGrossPay())
                                            $${String.format("%.2f", report.grossPay())}
                                        @else
                                            -
                                        @endif
                                    </td>
                                    <td class="px-4 py-4 text-right text-slate-700">
                                        @if(report.hasActiveHours())
                                            ${String.format("%.1f", report.activeHours())}h
                                        @else
                                            -
                                        @endif
                                    </td>
                                    <td class="px-4 py-4 text-right text-slate-700">
                                        @if(report.hasOnlineHours())
                                            ${String.format("%.1f", report.onlineHours())}h
                                        @else
                                            -
                                        @endif
                                    </td>
                                    <td class="px-4 py-4 text-right text-slate-700">
                                        @if(report.hasMiles())
                                            ${report.miles()} mi
                                        @else
                                            -
                                        @endif
                                    </td>
                                    <td class="px-4 py-4 text-right text-slate-700">
                                        @if(report.hasOnlineHours())
                                            $${String.format("%.2f", report.grossPerOnlineHour())}/hr
                                        @else
                                            @if(report.hasActiveHours())
                                                $${String.format("%.2f", report.grossPerActiveHour())}/hr
                                            @else
                                                -
                                            @endif
                                        @endif
                                    </td>
                                    <td class="px-4 py-4 text-right font-black text-emerald-700">
                                        @if(report.hasEstimatedNetCheck())
                                            $${String.format("%.2f", report.estimatedNetHourlyAfterMileageAndTax())}/hr
                                        @else
                                            Needs miles + time
                                        @endif
                                    </td>
                                    <td class="px-4 py-4">
                                        <a href="${report.sourceUrl()}" target="_blank" rel="noopener nofollow" class="font-bold text-emerald-700 hover:text-emerald-800">
                                            ${report.sourceLabel()}
                                        </a>
                                    </td>
                                </tr>
                            @endfor
                        </tbody>
                    </table>
                </div>
            </section>

            <section class="grid lg:grid-cols-3 gap-4">
                <a href="/reports/doordash-driver-hourly-pay-2026" class="rounded-3xl bg-white border border-slate-200 p-6 hover:border-emerald-300 transition-colors">
                    <strong class="block text-slate-900 text-lg mb-2">DoorDash hourly pay report</strong>
                    <span class="text-sm text-slate-600">Eight-city modeled pay dataset that uses this evidence layer for trust checks.</span>
                </a>
                <a href="/best-cities/doordash" class="rounded-3xl bg-white border border-slate-200 p-6 hover:border-emerald-300 transition-colors">
                    <strong class="block text-slate-900 text-lg mb-2">Highest-paying DoorDash cities</strong>
                    <span class="text-sm text-slate-600">Compare net hourly rankings after mileage and tax assumptions.</span>
                </a>
                <a href="/doordash/how-much-can-you-make-in-3-hours" class="rounded-3xl bg-white border border-slate-200 p-6 hover:border-emerald-300 transition-colors">
                    <strong class="block text-slate-900 text-lg mb-2">DoorDash 3-hour shift estimate</strong>
                    <span class="text-sm text-slate-600">Translate shift evidence into a short peak-window earnings plan.</span>
                </a>
                <a href="/doordash/can-you-make-100-a-day" class="rounded-3xl bg-white border border-slate-200 p-6 hover:border-emerald-300 transition-colors">
                    <strong class="block text-slate-900 text-lg mb-2">Can DoorDash make $100/day?</strong>
                    <span class="text-sm text-slate-600">Use the evidence table to check hours, miles, and gross needed for the target.</span>
                </a>
                <a href="/doordash/after-gas" class="rounded-3xl bg-white border border-slate-200 p-6 hover:border-emerald-300 transition-colors">
                    <strong class="block text-slate-900 text-lg mb-2">DoorDash after gas</strong>
                    <span class="text-sm text-slate-600">Translate public gross reports into take-home math.</span>
                </a>
                <a href="/doordash/pay-per-mile" class="rounded-3xl bg-white border border-slate-200 p-6 hover:border-emerald-300 transition-colors">
                    <strong class="block text-slate-900 text-lg mb-2">DoorDash pay per mile</strong>
                    <span class="text-sm text-slate-600">Compare reported miles with offer-floor logic.</span>
                </a>
            </section>

            <section class="bg-slate-50 rounded-3xl border border-slate-200 p-8">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500 mb-2">Citation block</p>
                <h2 class="text-2xl font-black text-slate-900 mb-4">Suggested citation</h2>
                <p class="text-sm text-slate-700 leading-relaxed">
                    GigVerdict, "DoorDash Driver Shift Evidence 2026," updated ${lastUpdated}. Public shift evidence table for DoorDash gross pay, active time, dash time, miles, and modeled net checks. ${AppConstants.BASE_URL}/reports/doordash-driver-shift-evidence-2026
                </p>
            </section>
        </main>
    `
)
