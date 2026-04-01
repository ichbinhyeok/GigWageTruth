@import com.gigwager.model.Verdict
@import com.gigwager.model.GigCalculationResult
@param Verdict verdict
@param GigCalculationResult calculationResult

<section class="bg-white border-2 border-${verdict.themeColor()}-100 rounded-3xl p-8 md:p-12 shadow-xl shadow-${verdict.themeColor()}-900/5 relative overflow-hidden" id="verdict-card-container">
    <div class="absolute top-0 left-0 w-full h-2 bg-${verdict.themeColor()}-500"></div>
    <span class="inline-block px-4 py-1 rounded-full bg-${verdict.themeColor()}-100 text-${verdict.themeColor()}-800 font-bold text-sm tracking-wider mb-6">VERDICT: ${verdict.level()}</span>
    <h2 class="text-3xl md:text-4xl font-extrabold text-slate-900 mb-4 leading-tight">${verdict.headline()}</h2>
    <p class="text-base text-slate-600 max-w-3xl leading-relaxed mb-8">
        Use the verdict as a decision signal, then protect the week with a reserve plan and one clear next fix.
    </p>

    <div class="prose prose-lg prose-slate max-w-none text-slate-600 space-y-4">
        @for(String paragraph : verdict.paragraphs())
            <p>${paragraph}</p>
        @endfor
    </div>

    <div class="mt-8 grid sm:grid-cols-2 xl:grid-cols-4 gap-4">
        <div class="rounded-2xl bg-slate-50 border border-slate-200 p-4">
            <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500">Take-home this week</p>
            <p class="mt-2 text-3xl font-black text-slate-900">
                $<span x-text="typeof totalNet !== 'undefined' ? totalNet.toFixed(0) : '${String.format("%.0f", calculationResult.takeHome())}'">${String.format("%.0f", calculationResult.takeHome())}</span>
            </p>
        </div>
        <div class="rounded-2xl bg-slate-50 border border-slate-200 p-4">
            <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500">Real hourly</p>
            <p class="mt-2 text-3xl font-black text-slate-900">
                $<span x-text="typeof netHourly !== 'undefined' ? netHourly.toFixed(2) : '${String.format("%.2f", calculationResult.realHourly())}'">${String.format("%.2f", calculationResult.realHourly())}</span>/hr
            </p>
        </div>
        <div class="rounded-2xl bg-slate-50 border border-slate-200 p-4">
            <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500">Tax reserve</p>
            <p class="mt-2 text-3xl font-black text-amber-600">
                $<span x-text="typeof taxCost !== 'undefined' ? taxCost.toFixed(0) : '${String.format("%.0f", calculationResult.taxReserve())}'">${String.format("%.0f", calculationResult.taxReserve())}</span>
            </p>
        </div>
        <div class="rounded-2xl bg-slate-50 border border-slate-200 p-4">
            <p class="text-xs font-black uppercase tracking-[0.18em] text-slate-500" x-text="typeof biggestLeakLabel !== 'undefined' ? biggestLeakLabel : '${calculationResult.biggestLeakLabel()}'">${calculationResult.biggestLeakLabel()}</p>
            <p class="mt-2 text-3xl font-black text-rose-600">
                $<span x-text="typeof biggestLeakAmount !== 'undefined' ? biggestLeakAmount.toFixed(0) : '${String.format("%.0f", calculationResult.biggestLeakAmount())}'">${String.format("%.0f", calculationResult.biggestLeakAmount())}</span>
            </p>
        </div>
    </div>

    <div class="mt-8 space-y-6 text-${verdict.themeColor()}-900 border-t border-slate-100 pt-8">
        <div>
            <h3 class="font-bold mb-2">Why this verdict?</h3>
            <p>${verdict.why()}</p>
        </div>

        <p><strong>Who should ignore this verdict:</strong> ${verdict.who()}</p>

        <p><strong>Worst case if you choose wrong:</strong> ${verdict.worstCase()}</p>

        <p class="text-sm text-slate-500 italic text-center pt-4 border-t border-slate-100/50">${verdict.brandMessage()}</p>
    </div>

    <div class="mt-8 pt-8 border-t border-slate-100">
        <div class="rounded-3xl bg-slate-900 text-white p-6 md:p-7">
            <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-3">Protect this week</p>
            <h3 class="text-2xl font-black mb-3">Do not stop at the number.</h3>
            <p class="text-slate-300 max-w-2xl leading-relaxed mb-6">
                Build the weekly plan around this scenario: reserve the tax cash, tighten the mileage routine,
                and pick the first leak to fix.
            </p>
            <div class="flex flex-wrap gap-3">
                <a
                    :href="buildScenarioUrl('/profit-setup-kit', { source: 'verdict_card' })"
                    onclick="gtag('event', 'cta_click', { 'event_category': 'funnel', 'event_label': 'open_profit_setup_kit' })"
                    class="inline-flex items-center justify-center px-5 py-3 rounded-2xl bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-black transition-colors"
                >
                    Build my weekly profit plan
                </a>
                <button onclick="navigator.clipboard.writeText(document.getElementById('verdict-card-container').innerText); alert('Copied summary!')" class="px-4 py-3 bg-white/10 text-white font-bold rounded-2xl hover:bg-white/15 text-sm transition">
                    Copy summary
                </button>
                <button onclick="window.print()" class="px-4 py-3 bg-white/10 text-white font-bold rounded-2xl hover:bg-white/15 text-sm transition">
                    Print or save
                </button>
            </div>
        </div>

        <div class="grid md:grid-cols-3 gap-4 mt-6">
            <div class="bg-slate-50 p-5 rounded-2xl border border-slate-200 flex flex-col justify-between">
                <div>
                    <h4 class="font-bold text-slate-900 mb-2">Tax reserve</h4>
                    <p class="text-sm text-slate-600 mb-4">Keep taxes as an option, but make the reserve a weekly habit before quarter-end sneaks up.</p>
                </div>
                <a :href="buildScenarioUrl('/taxes/quarterly-estimator', { source: 'verdict_card' })" onclick="gtag('event', 'cta_click', { 'event_category': 'funnel', 'event_label': 'estimate_taxes' })" class="block text-center py-3 bg-white text-slate-900 rounded-xl text-sm font-bold border border-slate-200 hover:border-amber-200 hover:text-amber-700">Open tax planner</a>
            </div>

            <div class="bg-slate-50 p-5 rounded-2xl border border-slate-200 flex flex-col justify-between">
                <div>
                    <h4 class="font-bold text-slate-900 mb-2">Mileage and car cost</h4>
                    <p class="text-sm text-slate-600 mb-4">If the week is weak, this is usually the first place where the margin disappears.</p>
                </div>
                <a :href="buildScenarioUrl('/vehicle-cost/cost-per-mile', { source: 'verdict_card' }, ['miles'])" onclick="gtag('event', 'cta_click', { 'event_category': 'funnel', 'event_label': 'view_car_cost' })" class="block text-center py-3 bg-white text-slate-900 rounded-xl text-sm font-bold border border-slate-200 hover:border-orange-200 hover:text-orange-700">Check cost per mile</a>
            </div>

            <div class="bg-slate-50 p-5 rounded-2xl border border-slate-200 flex flex-col justify-between">
                <div>
                    <h4 class="font-bold text-slate-900 mb-2">Coverage risk</h4>
                    <p class="text-sm text-slate-600 mb-4">A good week can still implode if your personal policy leaves a gap while the app is on.</p>
                </div>
                <a :href="buildScenarioUrl('/insurance/rideshare-basics', { source: 'verdict_card' }, ['app'])" onclick="gtag('event', 'cta_click', { 'event_category': 'funnel', 'event_label': 'check_coverage' })" class="block text-center py-3 bg-white text-slate-900 rounded-xl text-sm font-bold border border-slate-200 hover:border-sky-200 hover:text-sky-700">Check coverage</a>
            </div>
        </div>
    </div>
</section>
