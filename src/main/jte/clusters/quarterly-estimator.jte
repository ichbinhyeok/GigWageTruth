@import com.gigwager.model.GigCalculationRequest
@import com.gigwager.model.GigCalculationResult
@import com.gigwager.util.AppConstants
@param String customTitle
@param GigCalculationRequest scenarioRequest
@param GigCalculationResult scenarioResult
@param String scenarioApp = "gig"
@param String scenarioSource = "direct"
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new com.gigwager.model.SeoMeta(
        customTitle,
        "Plan your quarterly self-employment taxes without making tax season the whole business. Estimate a reserve and connect it to your weekly take-home routine.",
        AppConstants.BASE_URL + "/taxes/quarterly-estimator",
        AppConstants.BASE_URL + "/og-image.jpg"
    ),
    content = @`
        <div
            class="bg-slate-50 min-h-screen py-16 px-6"
            x-data="{
                app: '${scenarioApp}',
                source: '${scenarioSource}',
                gross: ${scenarioRequest.gross()},
                miles: ${scenarioRequest.miles()},
                hours: ${scenarioRequest.hours()},
                tips: ${scenarioRequest.tips()},
                bonuses: ${scenarioRequest.bonuses()},
                activeTime: ${scenarioRequest.activeTime()},
                gasPrice: ${scenarioRequest.gasPrice()},
                taxRate: ${scenarioRequest.taxRate()},
                roundTrip: ${scenarioRequest.roundTrip()},
                calculationMode: '${scenarioRequest.calculationMode()}',
                vehicleId: '${scenarioRequest.vehicleId()}',
                customMpg: ${scenarioRequest.customMpg()},
                customMaintenance: ${scenarioRequest.customMaintenance()},
                customDepreciation: ${scenarioRequest.customDepreciation()},
                weeklyTaxReserve: ${scenarioResult.taxReserve()},
                vehicleCostReserve: ${scenarioResult.totalDeduction()},
                quarterlyReserve: ${scenarioResult.taxReserve()} * 13,
                showPersonalizedBanner: ${scenarioResult.hasScenario()},
                buildScenarioHref(path, extra = {}, includeKeys = null) {
                    const base = {
                        app: this.app,
                        gross: this.gross,
                        miles: this.miles,
                        hours: this.hours,
                        tips: this.tips,
                        bonuses: this.bonuses,
                        activeTime: this.activeTime,
                        gasPrice: this.gasPrice,
                        taxRate: this.taxRate,
                        roundTrip: this.roundTrip,
                        calculationMode: this.calculationMode,
                        vehicleId: this.vehicleId,
                        customMpg: this.customMpg,
                        customMaintenance: this.customMaintenance,
                        customDepreciation: this.customDepreciation
                    };
                    const keys = includeKeys || Object.keys(base);
                    const params = new URLSearchParams();
                    keys.forEach((key) => params.set(key, String(base[key])));
                    Object.entries(extra).forEach(([key, value]) => params.set(key, String(value)));
                    return path + '?' + params.toString();
                },
                get appName() {
                    if (this.app === 'uber') return 'Uber';
                    if (this.app === 'doordash') return 'DoorDash';
                    return 'gig';
                }
            }"
        >
            <div class="max-w-5xl mx-auto grid lg:grid-cols-[1.1fr_0.9fr] gap-8 items-start">
                <div class="bg-white p-8 md:p-10 rounded-[2rem] shadow-sm border border-slate-200 relative overflow-hidden">
                    <div class="absolute top-0 left-0 w-full h-2 bg-amber-500"></div>

                    <div x-show="showPersonalizedBanner" x-cloak class="mt-4 mb-8 bg-amber-50 border border-amber-200 rounded-2xl p-6 shadow-sm text-left relative overflow-hidden">
                        <div class="absolute top-0 left-0 w-2 h-full bg-amber-400"></div>
                        <div class="flex items-start gap-4">
                            <div class="bg-amber-100 p-2 rounded-full text-amber-600 shrink-0 mt-1">
                                <span class="text-xl">$</span>
                            </div>
                            <div>
                                <h3 class="text-lg font-bold text-amber-900 mb-1"><span x-text="appName"></span> tax reserve snapshot</h3>
                                <p class="text-amber-800 text-sm">
                                    On a week like this, reserve about <strong>$<span x-text="weeklyTaxReserve.toFixed(0)"></span></strong>.
                                    If the pattern holds, the quarterly reserve is roughly <strong>$<span x-text="quarterlyReserve.toFixed(0)"></span></strong>.
                                </p>
                            </div>
                        </div>
                    </div>

                    <p class="text-xs font-black uppercase tracking-[0.22em] text-amber-700 mb-4">Support Module</p>
                    <h1 class="text-4xl font-black text-slate-900 mb-4 tracking-tight">Quarterly tax planner</h1>
                    <p class="text-xl text-slate-600 mb-8 max-w-2xl">
                        Taxes matter, but they are not the whole business. Treat this page as one part of the weekly keep-more-money system.
                    </p>

                    <div class="bg-slate-50 p-6 rounded-2xl border border-slate-200 text-left mb-8">
                        <p class="text-slate-700 font-medium mb-4">
                            If you expect to owe more than $1,000 for the year, the IRS generally expects estimated payments as you go.
                            The habit is not "panic in April." The habit is a weekly reserve.
                        </p>
                        <ul class="list-disc pl-5 text-slate-600 space-y-2 mb-4">
                            <li>Q1 due: April 15</li>
                            <li>Q2 due: June 15</li>
                            <li>Q3 due: September 15</li>
                            <li>Q4 due: January 15</li>
                        </ul>
                    </div>

                    <div class="grid md:grid-cols-3 gap-4 mb-8">
                        <div class="rounded-2xl border border-slate-200 bg-white p-5">
                            <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500 mb-2">Weekly reserve</p>
                            <p class="text-3xl font-black text-amber-700">$<span x-text="weeklyTaxReserve.toFixed(0)"></span></p>
                            <p class="mt-2 text-sm text-slate-600">Move this amount out of reach when the week closes.</p>
                        </div>
                        <div class="rounded-2xl border border-slate-200 bg-white p-5">
                            <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500 mb-2">Quarterly estimate</p>
                            <p class="text-3xl font-black text-slate-900">$<span x-text="quarterlyReserve.toFixed(0)"></span></p>
                            <p class="mt-2 text-sm text-slate-600">This is the quarter-sized number the weekly reserve is trying to protect.</p>
                        </div>
                        <div class="rounded-2xl border border-slate-200 bg-white p-5">
                            <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500 mb-2">Vehicle cost reserve</p>
                            <p class="text-3xl font-black text-slate-900">$<span x-text="vehicleCostReserve.toFixed(0)"></span></p>
                            <p class="mt-2 text-sm text-slate-600">If your mileage or vehicle setup is wrong, the reserve number is lying to you.</p>
                        </div>
                    </div>
                </div>

                <div class="space-y-6">
                    <div class="bg-slate-900 text-white p-7 rounded-[2rem] shadow-xl shadow-slate-900/10">
                        <p class="text-xs font-black uppercase tracking-[0.22em] text-emerald-300 mb-3">Main Flow</p>
                        <h2 class="text-2xl font-black mb-3">Need the weekly plan, not just the tax number?</h2>
                        <p class="text-sm text-slate-300 leading-relaxed mb-5">
                            Open the full setup kit if you want the reserve, mileage, and next-step workflow in one place.
                        </p>
                        <a :href="buildScenarioHref('/profit-setup-kit', { source: 'quarterly_estimator' })"
                           class="block text-center rounded-2xl bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-black px-5 py-4 transition-colors">
                            Open the setup kit
                        </a>
                    </div>

                    <div class="bg-white p-7 rounded-[2rem] border border-slate-200 shadow-sm">
                        <p class="text-xs font-black uppercase tracking-[0.22em] text-slate-500 mb-3">Practical next steps</p>
                        <div class="space-y-3 text-sm">
                            <a :href="buildScenarioHref('/' + (app === 'doordash' ? 'doordash' : 'uber'))" class="flex items-center justify-between rounded-2xl border border-slate-200 px-4 py-3 font-semibold text-slate-700 hover:border-emerald-200 hover:text-emerald-700 transition-colors">
                                <span>Run the calculator again</span>
                                <span>&rarr;</span>
                            </a>
                            <a :href="buildScenarioHref('/vehicle-cost/cost-per-mile', { source: 'quarterly_estimator' }, ['app', 'miles'])" class="flex items-center justify-between rounded-2xl border border-slate-200 px-4 py-3 font-semibold text-slate-700 hover:border-orange-200 hover:text-orange-700 transition-colors">
                                <span>Check cost per mile</span>
                                <span>&rarr;</span>
                            </a>
                            <a :href="buildScenarioHref('/insurance/rideshare-basics', { source: 'quarterly_estimator' }, ['app'])" class="flex items-center justify-between rounded-2xl border border-slate-200 px-4 py-3 font-semibold text-slate-700 hover:border-sky-200 hover:text-sky-700 transition-colors">
                                <span>Review coverage risk</span>
                                <span>&rarr;</span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `
)
