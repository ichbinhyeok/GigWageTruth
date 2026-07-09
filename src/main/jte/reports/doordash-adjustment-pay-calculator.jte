@import com.gigwager.model.DoorDashAdjustmentScenario
@import com.gigwager.model.SeoMeta
@import java.util.List

@param SeoMeta seoMeta
@param List<DoorDashAdjustmentScenario> scenarios
@param String lastUpdated
@param String adjustmentJsonLd = "{}"
@param Boolean noIndex = false

@template.layout.main(
    noIndex = noIndex,
    seoMeta = seoMeta,
    content = @`
        <script type="application/ld+json">
        $unsafe{adjustmentJsonLd}
        </script>

        !{DoorDashAdjustmentScenario defaultScenario = scenarios.get(0);}

        <div class="bg-slate-950 text-white py-16 px-6">
            <div class="max-w-5xl mx-auto">
                <nav class="text-sm text-slate-400 mb-6 flex items-center gap-2 flex-wrap">
                    <a href="/" class="hover:text-emerald-300">Home</a>
                    <span>&gt;</span>
                    <a href="/doordash" class="hover:text-emerald-300">DoorDash Calculator</a>
                    <span>&gt;</span>
                    <span class="text-white">Adjustment pay calculator</span>
                </nav>
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-3">Updated ${lastUpdated}</p>
                <h1 class="text-4xl md:text-6xl font-black leading-tight mb-5">
                    DoorDash Adjustment Pay Calculator
                    <span class="block text-emerald-300">Prop 22, NYC, Seattle</span>
                </h1>
                <p class="text-xl text-slate-300 max-w-4xl">
                    Estimate DoorDash adjustment pay from active time, active miles, DoorDash pay before tips, and the local guaranteed earnings rule. The calculator starts with official DoorDash examples for California Prop 22, NYC minimum earnings, and Seattle adjusted pay.
                </p>
            </div>
        </div>

        <section class="max-w-5xl mx-auto px-6 -mt-8 relative z-10 mb-10">
            <div class="bg-white rounded-3xl border border-slate-200 shadow-xl overflow-hidden">
                <div class="grid md:grid-cols-4 divide-y md:divide-y-0 md:divide-x divide-slate-100">
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Selected market</p>
                        <p id="adjustment-selected-market" class="text-3xl font-black text-slate-900">${defaultScenario.marketLabel()}</p>
                    </div>
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Guaranteed minimum</p>
                        <p id="adjustment-summary-guarantee" class="text-3xl font-black text-slate-900">$${String.format("%.2f", defaultScenario.guaranteedMinimum())}</p>
                    </div>
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">Pay adjustment</p>
                        <p id="adjustment-summary-amount" class="text-3xl font-black text-emerald-700">$${String.format("%.2f", defaultScenario.estimatedAdjustment())}</p>
                    </div>
                    <div class="p-6">
                        <p class="text-xs font-black uppercase tracking-wider text-slate-500 mb-1">With tips</p>
                        <p id="adjustment-summary-total" class="text-3xl font-black text-slate-900">$${String.format("%.2f", defaultScenario.totalWithTips())}</p>
                    </div>
                </div>
            </div>
        </section>

        <main class="max-w-5xl mx-auto px-6 pb-16 space-y-12">
            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Direct answer</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">How DoorDash adjustment pay is calculated</h2>
                <p id="adjustment-direct-answer" class="text-slate-700 leading-relaxed max-w-4xl">
                    DoorDash adjustment pay is the difference between the local guaranteed earnings floor and DoorDash pay before tips. If the guaranteed minimum is $239.80 and DoorDash pay before tips is $190.00, the adjustment is $49.80. Tips are added after that comparison in the official California and NYC examples.
                </p>
            </section>

            <section class="grid lg:grid-cols-[1.15fr_0.85fr] gap-6 items-start">
                <div class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                    <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Calculator</p>
                    <h2 class="text-3xl font-black text-slate-900 mb-5">Pay adjustment estimate</h2>
                    <div class="grid sm:grid-cols-2 gap-4">
                        <label class="block">
                            <span class="block text-sm font-bold text-slate-700 mb-2">Active hours</span>
                            <input id="adjustment-active-hours" type="number" inputmode="decimal" min="0" step="0.01" value="${String.format("%.2f", defaultScenario.activeHours())}" class="w-full rounded-xl border border-slate-300 px-4 py-3 text-slate-900 font-bold focus:border-emerald-500 focus:outline-none" />
                        </label>
                        <label class="block">
                            <span class="block text-sm font-bold text-slate-700 mb-2">Active miles</span>
                            <input id="adjustment-active-miles" type="number" inputmode="decimal" min="0" step="0.1" value="${String.format("%.1f", defaultScenario.activeMiles())}" class="w-full rounded-xl border border-slate-300 px-4 py-3 text-slate-900 font-bold focus:border-emerald-500 focus:outline-none" />
                        </label>
                        <label class="block">
                            <span class="block text-sm font-bold text-slate-700 mb-2">DoorDash pay before tips</span>
                            <input id="adjustment-doordash-pay" type="number" inputmode="decimal" min="0" step="0.01" value="${String.format("%.2f", defaultScenario.doordashPayBeforeTips())}" class="w-full rounded-xl border border-slate-300 px-4 py-3 text-slate-900 font-bold focus:border-emerald-500 focus:outline-none" />
                        </label>
                        <label class="block">
                            <span class="block text-sm font-bold text-slate-700 mb-2">Tips</span>
                            <input id="adjustment-tips" type="number" inputmode="decimal" min="0" step="0.01" value="${String.format("%.2f", defaultScenario.tips())}" class="w-full rounded-xl border border-slate-300 px-4 py-3 text-slate-900 font-bold focus:border-emerald-500 focus:outline-none" />
                        </label>
                        <label class="block">
                            <span class="block text-sm font-bold text-slate-700 mb-2">Guaranteed hourly rate</span>
                            <input id="adjustment-hourly-rate" type="number" inputmode="decimal" min="0" step="0.01" value="${String.format("%.2f", defaultScenario.minimumHourlyRate())}" class="w-full rounded-xl border border-slate-300 px-4 py-3 text-slate-900 font-bold focus:border-emerald-500 focus:outline-none" />
                        </label>
                        <label class="block">
                            <span class="block text-sm font-bold text-slate-700 mb-2">Active-mile rate</span>
                            <input id="adjustment-mileage-rate" type="number" inputmode="decimal" min="0" step="0.01" value="${String.format("%.2f", defaultScenario.mileageRate())}" class="w-full rounded-xl border border-slate-300 px-4 py-3 text-slate-900 font-bold focus:border-emerald-500 focus:outline-none" />
                        </label>
                    </div>

                    <div class="mt-6 flex flex-wrap gap-2">
                        @for(DoorDashAdjustmentScenario scenario : scenarios)
                            <button type="button"
                                    class="rounded-xl border border-slate-200 bg-slate-50 px-4 py-2 text-sm font-black text-slate-800 hover:border-emerald-300 hover:bg-emerald-50 transition-colors adjustment-preset"
                                    data-hours="${String.format("%.2f", scenario.activeHours())}"
                                    data-miles="${String.format("%.1f", scenario.activeMiles())}"
                                    data-pay="${String.format("%.2f", scenario.doordashPayBeforeTips())}"
                                    data-tips="${String.format("%.2f", scenario.tips())}"
                                    data-hourly="${String.format("%.2f", scenario.minimumHourlyRate())}"
                                    data-mileage="${String.format("%.2f", scenario.mileageRate())}"
                                    data-market="${scenario.marketLabel()}"
                                    data-rule="${scenario.ruleLabel()}">
                                ${scenario.marketLabel()}
                            </button>
                        @endfor
                    </div>
                </div>

                <aside class="bg-slate-900 rounded-3xl p-8 text-white shadow-sm">
                    <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-2">Result</p>
                    <h2 class="text-2xl font-black mb-5">Adjustment output</h2>
                    <div class="space-y-4">
                        <div class="rounded-2xl bg-white/5 border border-white/10 p-5">
                            <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-400 mb-1">Guaranteed minimum</p>
                            <p id="adjustment-guarantee" class="text-4xl font-black text-white">$${String.format("%.2f", defaultScenario.guaranteedMinimum())}</p>
                        </div>
                        <div class="rounded-2xl bg-emerald-500/10 border border-emerald-300/30 p-5">
                            <p class="text-xs font-black uppercase tracking-[0.16em] text-emerald-200 mb-1">Pay adjustment estimate</p>
                            <p id="adjustment-amount" class="text-4xl font-black text-emerald-300">$${String.format("%.2f", defaultScenario.estimatedAdjustment())}</p>
                        </div>
                        <div class="rounded-2xl bg-white/5 border border-white/10 p-5">
                            <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-400 mb-1">Total with tips</p>
                            <p id="adjustment-total" class="text-4xl font-black text-white">$${String.format("%.2f", defaultScenario.totalWithTips())}</p>
                        </div>
                    </div>
                    <p id="adjustment-formula" class="text-sm text-slate-300 mt-5 leading-relaxed">
                        Guaranteed minimum = active hours x hourly rate + active miles x mileage rate. Adjustment = max(0, guaranteed minimum - DoorDash pay before tips).
                    </p>
                </aside>
            </section>

            <section class="bg-white rounded-3xl border border-slate-200 overflow-hidden shadow-sm">
                <div class="bg-slate-900 px-6 py-4">
                    <h2 class="text-xl font-black text-white">Official DoorDash adjustment pay examples</h2>
                    <p class="text-sm text-slate-300 mt-1">These examples mirror the official source math, then expose the inputs drivers actually change.</p>
                </div>
                <div class="overflow-x-auto">
                    <table class="w-full min-w-[72rem] text-sm">
                        <thead>
                            <tr class="bg-slate-50 border-b border-slate-200">
                                <th scope="col" class="px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase">Market</th>
                                <th scope="col" class="px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase">Rule</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Active</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Miles</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Guarantee</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">DoorDash pay</th>
                                <th scope="col" class="px-4 py-3 text-right text-xs font-bold text-slate-500 uppercase">Adjustment</th>
                                <th scope="col" class="px-4 py-3 text-left text-xs font-bold text-slate-500 uppercase">Source</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-100">
                            @for(DoorDashAdjustmentScenario scenario : scenarios)
                                <tr>
                                    <td class="px-4 py-4">
                                        <strong class="block text-slate-900">${scenario.marketLabel()}</strong>
                                        <span class="text-xs text-slate-500">${scenario.ruleLabel()}</span>
                                    </td>
                                    <td class="px-4 py-4 text-slate-600 max-w-sm">${scenario.localRule()}</td>
                                    <td class="px-4 py-4 text-right text-slate-700">${scenario.activeTimeLabel()}</td>
                                    <td class="px-4 py-4 text-right text-slate-700">${scenario.activeMilesLabel()}</td>
                                    <td class="px-4 py-4 text-right font-black text-slate-900">$${String.format("%.2f", scenario.guaranteedMinimum())}</td>
                                    <td class="px-4 py-4 text-right text-slate-700">$${String.format("%.2f", scenario.doordashPayBeforeTips())}</td>
                                    <td class="px-4 py-4 text-right font-black text-emerald-700">$${String.format("%.2f", scenario.estimatedAdjustment())}</td>
                                    <td class="px-4 py-4">
                                        <a href="${scenario.sourceUrl()}" target="_blank" rel="noopener" class="font-bold text-emerald-700 hover:text-emerald-800">${scenario.sourceLabel()}</a>
                                    </td>
                                </tr>
                            @endfor
                        </tbody>
                    </table>
                </div>
            </section>

            <section class="grid lg:grid-cols-3 gap-4">
                @for(DoorDashAdjustmentScenario scenario : scenarios)
                    <article class="bg-white rounded-3xl border border-slate-200 p-6 shadow-sm">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500 mb-2">${scenario.marketLabel()}</p>
                        <h3 class="text-xl font-black text-slate-900 mb-3">${scenario.ruleLabel()}</h3>
                        <p class="text-sm text-slate-600 leading-relaxed mb-4">${scenario.officialExample()}</p>
                        <div class="grid grid-cols-2 gap-2 text-xs">
                            <div class="rounded-xl bg-slate-50 border border-slate-200 p-3">
                                <strong class="block text-slate-900">${scenario.hourlyRateLabel()}</strong>
                                <span class="text-slate-500">Hourly line</span>
                            </div>
                            <div class="rounded-xl bg-slate-50 border border-slate-200 p-3">
                                <strong class="block text-slate-900">${scenario.mileageRateLabel()}</strong>
                                <span class="text-slate-500">Mileage line</span>
                            </div>
                        </div>
                    </article>
                @endfor
            </section>

            <section class="bg-emerald-50 rounded-3xl border border-emerald-200 p-8">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Where this fits</p>
                <h2 class="text-3xl font-black text-slate-900 mb-4">Adjustment pay is not the same as normal DoorDash pay</h2>
                <p class="text-slate-700 leading-relaxed max-w-4xl mb-6">
                    In most markets, DoorDash pay is base pay, promotions, and tips. Adjustment pay matters when a local rule creates a guaranteed earnings floor, then compares that floor against DoorDash pay before tips. Use the pages below when your question is broader than California, NYC, or Seattle.
                </p>
                <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-3">
                    <a href="/reports/doordash-driver-hourly-pay-2026" class="rounded-2xl bg-white border border-emerald-100 px-5 py-4 hover:border-emerald-300 transition-colors">
                        <strong class="block text-slate-900">DoorDash driver hourly pay 2026</strong>
                        <span class="text-sm text-slate-600">City pay after mileage and tax assumptions.</span>
                    </a>
                    <a href="/reports/doordash-driver-shift-evidence-2026" class="rounded-2xl bg-white border border-emerald-100 px-5 py-4 hover:border-emerald-300 transition-colors">
                        <strong class="block text-slate-900">DoorDash shift evidence</strong>
                        <span class="text-sm text-slate-600">Reported gross, miles, active time, and net checks.</span>
                    </a>
                    <a href="/doordash/after-gas" class="rounded-2xl bg-white border border-emerald-100 px-5 py-4 hover:border-emerald-300 transition-colors">
                        <strong class="block text-slate-900">DoorDash after gas</strong>
                        <span class="text-sm text-slate-600">Take-home after gas, mileage, and tax pressure.</span>
                    </a>
                    <a href="/salary/doordash/san-francisco/hourly-pay" class="rounded-2xl bg-white border border-emerald-100 px-5 py-4 hover:border-emerald-300 transition-colors">
                        <strong class="block text-slate-900">San Francisco DoorDash hourly pay</strong>
                        <span class="text-sm text-slate-600">California city path after expenses.</span>
                    </a>
                    <a href="/salary/doordash/new-york/hourly-pay" class="rounded-2xl bg-white border border-emerald-100 px-5 py-4 hover:border-emerald-300 transition-colors">
                        <strong class="block text-slate-900">New York DoorDash hourly pay</strong>
                        <span class="text-sm text-slate-600">NYC active-time context and city math.</span>
                    </a>
                    <a href="/salary/doordash/seattle/hourly-pay" class="rounded-2xl bg-white border border-emerald-100 px-5 py-4 hover:border-emerald-300 transition-colors">
                        <strong class="block text-slate-900">Seattle DoorDash hourly pay</strong>
                        <span class="text-sm text-slate-600">Seattle local pay and mileage context.</span>
                    </a>
                </div>
            </section>

            <section class="bg-white rounded-3xl border border-slate-200 p-8 shadow-sm">
                <h2 class="text-3xl font-black text-slate-900 mb-6">Frequently Asked Questions</h2>
                <div class="space-y-6">
                    <div>
                        <h3 class="text-xl font-bold text-slate-900 mb-2">What is DoorDash adjustment pay?</h3>
                        <p class="text-slate-600">It is the difference between a local guaranteed earnings floor and DoorDash pay before tips when the guaranteed amount is higher.</p>
                    </div>
                    <div>
                        <h3 class="text-xl font-bold text-slate-900 mb-2">Does DoorDash adjustment pay include tips?</h3>
                        <p class="text-slate-600">The official California and NYC examples compare guaranteed earnings against DoorDash pay before tips, then add customer tips on top of the adjusted DoorDash pay.</p>
                    </div>
                    <div>
                        <h3 class="text-xl font-bold text-slate-900 mb-2">Is this legal or tax advice?</h3>
                        <p class="text-slate-600">No. This is an educational estimator based on official DoorDash support examples. Check the current official page for your market before relying on the number.</p>
                    </div>
                </div>
            </section>
        </main>

        <script>
        (function () {
            function numberValue(id) {
                var element = document.getElementById(id);
                var parsed = parseFloat(element ? element.value : "0");
                if (!Number.isFinite(parsed)) {
                    return 0;
                }
                if (parsed < 0) {
                    element.value = "0";
                    return 0;
                }
                return parsed;
            }

            function money(value) {
                return "$" + value.toFixed(2);
            }

            function updateAdjustment() {
                var selectedMarket = document.getElementById("adjustment-selected-market").textContent || "selected market";
                var activeHours = numberValue("adjustment-active-hours");
                var activeMiles = numberValue("adjustment-active-miles");
                var doordashPay = numberValue("adjustment-doordash-pay");
                var tips = numberValue("adjustment-tips");
                var hourlyRate = numberValue("adjustment-hourly-rate");
                var mileageRate = numberValue("adjustment-mileage-rate");
                var guarantee = (activeHours * hourlyRate) + (activeMiles * mileageRate);
                var adjustment = Math.max(0, guarantee - doordashPay);
                var total = doordashPay + adjustment + tips;

                document.getElementById("adjustment-summary-guarantee").textContent = money(guarantee);
                document.getElementById("adjustment-summary-amount").textContent = money(adjustment);
                document.getElementById("adjustment-summary-total").textContent = money(total);
                document.getElementById("adjustment-guarantee").textContent = money(guarantee);
                document.getElementById("adjustment-amount").textContent = money(adjustment);
                document.getElementById("adjustment-total").textContent = money(total);
                document.getElementById("adjustment-direct-answer").textContent =
                    "For the selected " + selectedMarket + " inputs, the guaranteed minimum is " + money(guarantee) +
                    ". DoorDash pay before tips is " + money(doordashPay) +
                    ", so the estimated adjustment is " + money(adjustment) +
                    ". Tips are added after that comparison, putting the total at " + money(total) + ".";
                document.getElementById("adjustment-formula").textContent =
                    "Guaranteed minimum = " + activeHours.toFixed(2) + " active hours x " + money(hourlyRate) +
                    " + " + activeMiles.toFixed(1) + " active miles x " + money(mileageRate) +
                    ". Adjustment = max(0, " + money(guarantee) + " - " + money(doordashPay) + ").";
            }

            function setPreset(button) {
                document.getElementById("adjustment-active-hours").value = button.dataset.hours;
                document.getElementById("adjustment-active-miles").value = button.dataset.miles;
                document.getElementById("adjustment-doordash-pay").value = button.dataset.pay;
                document.getElementById("adjustment-tips").value = button.dataset.tips;
                document.getElementById("adjustment-hourly-rate").value = button.dataset.hourly;
                document.getElementById("adjustment-mileage-rate").value = button.dataset.mileage;
                document.getElementById("adjustment-selected-market").textContent = button.dataset.market || "Selected market";
                updateAdjustment();
            }

            document.addEventListener("DOMContentLoaded", function () {
                [
                    "adjustment-active-hours",
                    "adjustment-active-miles",
                    "adjustment-doordash-pay",
                    "adjustment-tips",
                    "adjustment-hourly-rate",
                    "adjustment-mileage-rate"
                ].forEach(function (id) {
                    var element = document.getElementById(id);
                    if (element) {
                        element.addEventListener("input", updateAdjustment);
                    }
                });

                Array.prototype.forEach.call(document.querySelectorAll(".adjustment-preset"), function (button) {
                    button.addEventListener("click", function () {
                        setPreset(button);
                    });
                });

                updateAdjustment();
            });
        })();
        </script>
    `
)
