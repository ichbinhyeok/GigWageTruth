@import com.gigwager.model.DriverShiftReport
@import java.util.List

@param String appName
@param String cityName = ""
@param double modeledNetHourly = 0
@param List<DriverShiftReport> driverShiftReports = java.util.List.of()
@param String sectionClass = "max-w-5xl mx-auto px-6 mb-12"

@if(driverShiftReports != null && !driverShiftReports.isEmpty())
    <section class="${sectionClass}">
        <div class="bg-white rounded-3xl border border-slate-200 p-6 md:p-8 shadow-sm">
            <div class="mb-6">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Published shift evidence</p>
                <h2 class="text-3xl font-black text-slate-900 mb-3">
                    Real ${appName} shift signals${cityName != null && !cityName.isBlank() ? " for " + cityName : ""}
                </h2>
                <p class="text-slate-600 max-w-3xl">
                    These are reviewed public field tests and driver discussions, not scraped personal data. When a source reports gross pay, hours, miles, or deliveries, GigVerdict shows those fields separately so the modeled $${String.format("%.2f", modeledNetHourly)}/hr net estimate can be checked against real-world shift friction.
                </p>
            </div>

            <div class="grid lg:grid-cols-2 gap-4">
                @for(DriverShiftReport report : driverShiftReports)
                    <article class="rounded-2xl border border-slate-200 bg-slate-50 p-5">
                        <div class="flex flex-wrap items-center gap-2 mb-3">
                            <span class="rounded-full bg-emerald-100 px-3 py-1 text-xs font-black uppercase tracking-[0.12em] text-emerald-800">${report.evidenceType()}</span>
                            <span class="rounded-full bg-white px-3 py-1 text-xs font-bold text-slate-600 border border-slate-200">${report.shiftWindow()}</span>
                        </div>
                        <h3 class="text-xl font-black text-slate-900 mb-2">${report.cityName()}</h3>
                        <p class="text-sm font-bold text-slate-700 mb-4">${report.reportedResult()}</p>

                        <div class="grid grid-cols-2 md:grid-cols-4 gap-2 mb-4 text-sm">
                            <div class="rounded-xl bg-white border border-slate-200 p-3">
                                <p class="text-[10px] uppercase tracking-wider text-slate-500 font-bold mb-1">Gross</p>
                                <p class="font-black text-slate-900">
                                    @if(report.hasGrossPay())
                                        $${String.format("%.0f", report.grossPay())}
                                    @else
                                        Not reported
                                    @endif
                                </p>
                            </div>
                            <div class="rounded-xl bg-white border border-slate-200 p-3">
                                <p class="text-[10px] uppercase tracking-wider text-slate-500 font-bold mb-1">Active hr</p>
                                <p class="font-black text-slate-900">
                                    @if(report.hasActiveHours())
                                        ${String.format("%.1f", report.activeHours())}
                                    @else
                                        Not reported
                                    @endif
                                </p>
                            </div>
                            <div class="rounded-xl bg-white border border-slate-200 p-3">
                                <p class="text-[10px] uppercase tracking-wider text-slate-500 font-bold mb-1">Dash/online</p>
                                <p class="font-black text-slate-900">
                                    @if(report.hasOnlineHours())
                                        ${String.format("%.1f", report.onlineHours())}
                                    @else
                                        Not reported
                                    @endif
                                </p>
                            </div>
                            <div class="rounded-xl bg-white border border-slate-200 p-3">
                                <p class="text-[10px] uppercase tracking-wider text-slate-500 font-bold mb-1">Miles</p>
                                <p class="font-black text-slate-900">
                                    @if(report.hasMiles())
                                        ${report.miles()}
                                    @else
                                        Not reported
                                    @endif
                                </p>
                            </div>
                        </div>

                        @if(report.hasEstimatedNetCheck())
                            <div class="rounded-2xl bg-slate-900 p-4 text-white mb-4">
                                <p class="text-xs font-black uppercase tracking-[0.16em] text-emerald-300 mb-2">Modeled net check from reported gross + miles</p>
                                <div class="grid grid-cols-3 gap-3 text-sm">
                                    <div>
                                        <p class="text-slate-400 text-xs">Gross/hr</p>
                                        <p class="font-black">$${String.format("%.2f", report.hasOnlineHours() ? report.grossPerOnlineHour() : report.grossPerActiveHour())}</p>
                                    </div>
                                    <div>
                                        <p class="text-slate-400 text-xs">$/mile</p>
                                        <p class="font-black">$${String.format("%.2f", report.dollarsPerMile())}</p>
                                    </div>
                                    <div>
                                        <p class="text-slate-400 text-xs">Net/hr</p>
                                        <p class="font-black text-emerald-300">$${String.format("%.2f", report.estimatedNetHourlyAfterMileageAndTax())}</p>
                                    </div>
                                </div>
                            </div>
                        @endif

                        @if(report.hasDeliveries())
                            <p class="text-xs text-slate-500 mb-3">Reported deliveries: <strong>${report.deliveries()}</strong></p>
                        @endif
                        <p class="text-sm text-slate-600 leading-relaxed mb-4">${report.takeaway()}</p>
                        <a href="${report.sourceUrl()}" target="_blank" rel="noopener nofollow" class="text-sm font-bold text-emerald-700 hover:text-emerald-800">
                            Source: ${report.sourceLabel()} &rarr;
                        </a>
                    </article>
                @endfor
            </div>
        </div>
    </section>
@endif
