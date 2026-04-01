@import com.gigwager.util.AppConstants
@import com.gigwager.model.GigCalculationRequest
@import com.gigwager.model.GigCalculationResult
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
        "Build a weekly take-home plan for gig work. Estimate what you actually keep, what to reserve for taxes, and which leak to fix first.",
        AppConstants.BASE_URL + "/profit-setup-kit",
        AppConstants.BASE_URL + "/og-image.jpg"
    ),
    content = @`
        <div
            class="min-h-screen bg-slate-950 text-white"
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
                takeHome: ${scenarioResult.takeHome()},
                realHourly: ${scenarioResult.realHourly()},
                taxReserve: ${scenarioResult.taxReserve()},
                biggestLeakLabel: '${scenarioResult.biggestLeakLabel()}',
                biggestLeakAmount: ${scenarioResult.biggestLeakAmount()},
                hasScenario: ${scenarioResult.hasScenario()},
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
            <div class="max-w-6xl mx-auto px-6 py-16 lg:py-20">
                <div class="grid lg:grid-cols-[1.2fr_0.8fr] gap-10 lg:gap-14 items-start">
                    <div>
                        <p class="text-xs font-black uppercase tracking-[0.24em] text-emerald-300 mb-5">
                            Weekly Keep-More-Money System
                        </p>
                        <h1 class="text-4xl md:text-6xl font-black tracking-tight leading-[0.95] max-w-3xl">
                            Build a weekly profit plan around what you actually keep.
                        </h1>
                        <p class="mt-6 text-lg md:text-xl text-slate-300 max-w-2xl leading-relaxed">
                            Taxes are one lane, not the whole game. Use this setup kit to protect your take-home,
                            reserve cash for quarterly payments, and fix the biggest leak in your workflow.
                        </p>

                        <div
                            x-show="hasScenario"
                            x-cloak
                            class="mt-8 rounded-3xl border border-emerald-400/20 bg-emerald-400/10 p-6 md:p-7"
                        >
                            <p class="text-xs font-black uppercase tracking-[0.22em] text-emerald-200 mb-3">
                                Personalized Snapshot
                            </p>
                            <h2 class="text-2xl font-black text-white mb-3">
                                <span x-text="appName"></span> week from your latest numbers
                            </h2>
                            <p class="text-sm text-emerald-100/90 leading-relaxed mb-6">
                                This is the kind of week the kit will help you manage: what you keep, what to reserve,
                                and what to fix first.
                            </p>
                            <div class="grid sm:grid-cols-2 xl:grid-cols-4 gap-3">
                                <div class="rounded-2xl bg-slate-950/50 border border-white/10 p-4">
                                    <p class="text-xs font-bold uppercase tracking-wide text-slate-400">Take-home</p>
                                    <p class="mt-2 text-2xl font-black text-white">
                                        $<span x-text="takeHome.toFixed(0)"></span>
                                    </p>
                                </div>
                                <div class="rounded-2xl bg-slate-950/50 border border-white/10 p-4">
                                    <p class="text-xs font-bold uppercase tracking-wide text-slate-400">Real hourly</p>
                                    <p class="mt-2 text-2xl font-black text-white">
                                        $<span x-text="realHourly.toFixed(2)"></span>/hr
                                    </p>
                                </div>
                                <div class="rounded-2xl bg-slate-950/50 border border-white/10 p-4">
                                    <p class="text-xs font-bold uppercase tracking-wide text-slate-400">Tax reserve</p>
                                    <p class="mt-2 text-2xl font-black text-amber-300">
                                        $<span x-text="taxReserve.toFixed(0)"></span>
                                    </p>
                                </div>
                                <div class="rounded-2xl bg-slate-950/50 border border-white/10 p-4">
                                    <p class="text-xs font-bold uppercase tracking-wide text-slate-400" x-text="biggestLeakLabel"></p>
                                    <p class="mt-2 text-2xl font-black text-rose-300">
                                        $<span x-text="biggestLeakAmount.toFixed(0)"></span>
                                    </p>
                                </div>
                            </div>
                        </div>

                        <div class="mt-10 grid md:grid-cols-3 gap-4">
                            <div class="rounded-3xl border border-white/10 bg-white/5 p-6">
                                <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-400 mb-3">1. Lock the week</p>
                                <h3 class="text-xl font-black text-white mb-3">Know the real number.</h3>
                                <p class="text-sm text-slate-300 leading-relaxed">
                                    Convert app payout into actual weekly take-home after mileage proxy and self-employment tax.
                                </p>
                            </div>
                            <div class="rounded-3xl border border-white/10 bg-white/5 p-6">
                                <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-400 mb-3">2. Reserve the risk</p>
                                <h3 class="text-xl font-black text-white mb-3">Treat tax as a weekly habit.</h3>
                                <p class="text-sm text-slate-300 leading-relaxed">
                                    Quarterly due dates matter, but the operating habit is weekly: set money aside before it disappears.
                                </p>
                            </div>
                            <div class="rounded-3xl border border-white/10 bg-white/5 p-6">
                                <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-400 mb-3">3. Fix the leak</p>
                                <h3 class="text-xl font-black text-white mb-3">Miles, coverage, or low-rate work.</h3>
                                <p class="text-sm text-slate-300 leading-relaxed">
                                    Pick the first margin problem to solve instead of reading five disconnected guides.
                                </p>
                            </div>
                        </div>

                        <div class="mt-10 rounded-3xl border border-white/10 bg-white/5 p-6 md:p-8">
                            <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-400 mb-4">What you get</p>
                            <div class="grid md:grid-cols-2 gap-4 text-sm text-slate-200">
                                <div class="rounded-2xl bg-slate-900/70 border border-white/10 p-4">
                                    Weekly take-home checklist
                                </div>
                                <div class="rounded-2xl bg-slate-900/70 border border-white/10 p-4">
                                    Tax set-aside rhythm with quarterly reminders
                                </div>
                                <div class="rounded-2xl bg-slate-900/70 border border-white/10 p-4">
                                    Mileage tracking starter stack
                                </div>
                                <div class="rounded-2xl bg-slate-900/70 border border-white/10 p-4">
                                    Next-step map for vehicle cost and coverage
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="lg:sticky lg:top-24">
                        <div class="rounded-[2rem] border border-white/10 bg-white text-slate-900 p-7 md:p-8 shadow-2xl shadow-black/30">
                            <p class="text-xs font-black uppercase tracking-[0.22em] text-emerald-700 mb-3">
                                Free Setup Kit
                            </p>
                            <h2 class="text-3xl font-black tracking-tight leading-tight mb-3">
                                Send me the weekly profit plan.
                            </h2>
                            <p class="text-sm text-slate-600 leading-relaxed mb-6">
                                Start with the kit, then come back to the calculator each week. Taxes stay optional,
                                but the reserve workflow is included.
                            </p>

                            <form
                                action="https://formspree.io/f/mqkenryp"
                                method="POST"
                                class="space-y-4"
                                onsubmit="gtag('event','cta_click',{'event_category':'lead','event_label':'profit_setup_kit'});"
                            >
                                <input type="hidden" name="lead_type" value="profit_setup_kit">
                                <input type="hidden" name="source_page" x-bind:value="source">
                                <input type="hidden" name="app" x-bind:value="appName">
                                <input type="hidden" name="weekly_gross" x-bind:value="gross ? gross.toFixed(2) : ''">
                                <input type="hidden" name="weekly_miles" x-bind:value="miles ? miles.toFixed(0) : ''">
                                <input type="hidden" name="weekly_hours" x-bind:value="hours ? hours.toFixed(1) : ''">
                                <input type="hidden" name="weekly_take_home" x-bind:value="takeHome ? takeHome.toFixed(2) : ''">
                                <input type="hidden" name="weekly_tax_reserve" x-bind:value="taxReserve ? taxReserve.toFixed(2) : ''">

                                <div>
                                    <label class="block text-xs font-bold uppercase tracking-wide text-slate-500 mb-2">
                                        Email
                                    </label>
                                    <input
                                        type="email"
                                        name="email"
                                        placeholder="you@example.com"
                                        required
                                        class="w-full rounded-2xl border border-slate-200 px-4 py-4 text-base font-medium text-slate-900 focus:border-emerald-500 focus:outline-none focus:ring-2 focus:ring-emerald-500/20"
                                    >
                                </div>

                                <div>
                                    <label class="block text-xs font-bold uppercase tracking-wide text-slate-500 mb-2">
                                        What needs fixing first?
                                    </label>
                                    <select
                                        name="priority"
                                        class="w-full rounded-2xl border border-slate-200 px-4 py-4 text-base font-medium text-slate-900 focus:border-emerald-500 focus:outline-none focus:ring-2 focus:ring-emerald-500/20"
                                    >
                                        <option value="weekly_take_home">Weekly take-home</option>
                                        <option value="tax_reserve">Tax reserve</option>
                                        <option value="mileage_cost">Mileage and vehicle cost</option>
                                        <option value="insurance_risk">Insurance risk</option>
                                    </select>
                                </div>

                                <button
                                    type="submit"
                                    class="w-full rounded-2xl bg-emerald-600 px-5 py-4 text-base font-black text-white transition hover:bg-emerald-500"
                                >
                                    Send my setup kit
                                </button>
                            </form>

                            <p class="mt-4 text-xs text-slate-500 leading-relaxed">
                                One simple starting point for weekly take-home, mileage, and tax reserve. No calculator login required.
                            </p>

                            <div class="mt-6 pt-6 border-t border-slate-200 space-y-3 text-sm">
                                <a
                                    :href="buildScenarioHref('/' + (app === 'doordash' ? 'doordash' : 'uber'))"
                                    class="flex items-center justify-between rounded-2xl border border-slate-200 px-4 py-3 font-semibold text-slate-700 transition hover:border-emerald-200 hover:text-emerald-700"
                                >
                                    <span>Re-open calculator</span>
                                    <span>&rarr;</span>
                                </a>
                                <a
                                    :href="buildScenarioHref('/taxes/quarterly-estimator', { source: 'profit_setup_kit' })"
                                    class="flex items-center justify-between rounded-2xl border border-slate-200 px-4 py-3 font-semibold text-slate-700 transition hover:border-amber-200 hover:text-amber-700"
                                >
                                    <span>Optional tax planner</span>
                                    <span>&rarr;</span>
                                </a>
                                <a
                                    :href="buildScenarioHref('/vehicle-cost/cost-per-mile', { source: 'profit_setup_kit' }, ['app', 'miles'])"
                                    class="flex items-center justify-between rounded-2xl border border-slate-200 px-4 py-3 font-semibold text-slate-700 transition hover:border-orange-200 hover:text-orange-700"
                                >
                                    <span>Cost per mile check</span>
                                    <span>&rarr;</span>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script type="application/ld+json">
            {
              "@context": "https://schema.org",
              "@graph": [{
                "@type": "BreadcrumbList",
                "itemListElement": [{
                  "@type": "ListItem",
                  "position": 1,
                  "name": "Home",
                  "item": "${AppConstants.BASE_URL}/"
                },{
                  "@type": "ListItem",
                  "position": 2,
                  "name": "Gig Profit Setup Kit"
                }]
              }, {
                "@type": "FAQPage",
                "mainEntity": [{
                  "@type": "Question",
                  "name": "Is the setup kit only about taxes?",
                  "acceptedAnswer": {
                    "@type": "Answer",
                    "text": "No. Taxes are one part of the weekly profit system. The kit is centered on take-home, tax reserve, mileage discipline, and the first margin leak to fix."
                  }
                }, {
                  "@type": "Question",
                  "name": "Who is this for?",
                  "acceptedAnswer": {
                    "@type": "Answer",
                    "text": "Gig drivers and couriers who want a weekly operating plan for what they actually keep after mileage proxy and self-employment tax."
                  }
                }]
              }]
            }
            </script>
        </div>
    `
)
