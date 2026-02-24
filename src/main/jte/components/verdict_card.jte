@import com.gigwager.model.Verdict
@param Verdict verdict
@param Double estimatedTaxes = null
@param Double estimatedVehicleCost = null

<section class="bg-white border-2 border-${verdict.themeColor()}-100 rounded-3xl p-8 md:p-12 shadow-xl shadow-${verdict.themeColor()}-900/5 relative overflow-hidden" id="verdict-card-container">
    <div class="absolute top-0 left-0 w-full h-2 bg-${verdict.themeColor()}-500"></div>
    <span class="inline-block px-4 py-1 rounded-full bg-${verdict.themeColor()}-100 text-${verdict.themeColor()}-800 font-bold text-sm tracking-wider mb-6">VERDICT: ${verdict.level()}</span>
    <h2 class="text-3xl md:text-4xl font-extrabold text-slate-900 mb-6 leading-tight">${verdict.headline()}</h2>
    
    <div class="prose prose-lg prose-slate max-w-none text-slate-600 space-y-4">
        @for(String paragraph : verdict.paragraphs())
            <p>${paragraph}</p>
        @endfor
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
        <p class="text-lg font-bold text-${verdict.themeColor()}-700 italic mb-6">"${verdict.cta()}"</p>
        
        <div class="flex flex-wrap gap-4 mb-4">
            <button onclick="navigator.clipboard.writeText(document.getElementById('verdict-card-container').innerText); alert('Copied summary!')" class="px-4 py-2 bg-slate-100 text-slate-700 font-bold rounded-lg hover:bg-slate-200 text-sm transition">📋 Copy summary</button>
            <button onclick="window.print()" class="px-4 py-2 bg-slate-100 text-slate-700 font-bold rounded-lg hover:bg-slate-200 text-sm transition">🖨️ Print/Save</button>
        </div>
        
        <!-- Personalized Risk Trigger Cards -->
        @if(estimatedTaxes != null && estimatedVehicleCost != null)
        <div class="bg-slate-50 rounded-2xl p-6 border border-slate-200 mt-6 action-cards-dedupe">
            <h3 class="text-xl font-bold text-slate-900 mb-2">What to do next</h3>
            <p class="text-slate-600 mb-6 text-sm">Take action on your real numbers.</p>
            
            <div class="grid md:grid-cols-3 gap-4">
                <!-- Tax Risk Card -->
                <div class="bg-white p-5 rounded-xl border border-slate-200 hover:border-slate-400 transition-all flex flex-col justify-between">
                    <div>
                        <h4 class="font-bold text-slate-900 flex items-center gap-2 mb-2">🏦 Taxes Estimate</h4>
                        <p class="text-xs text-slate-600 mb-4">You may owe <strong>$${String.format("%.0f", estimatedTaxes * 50)}</strong>/yr in SE tax.</p>
                    </div>
                    <a :href="'/taxes/quarterly-estimator?app=' + selectedApp + '&gross=' + gross" onclick="gtag('event', 'cta_click', { 'event_category': 'funnel', 'event_label': 'estimate_taxes' })" class="block text-center py-2 bg-slate-900 text-white rounded-lg text-sm font-bold hover:bg-slate-800">Estimate Taxes</a>
                </div>
                
                <!-- Vehicle Cost Card -->
                <div class="bg-white p-5 rounded-xl border border-slate-200 hover:border-slate-400 transition-all flex flex-col justify-between">
                    <div>
                        <h4 class="font-bold text-slate-900 flex items-center gap-2 mb-2">🚗 Cost-per-mile</h4>
                        <p class="text-xs text-slate-600 mb-4">You used <strong>$${String.format("%.0f", estimatedVehicleCost)}</strong> in car equity this week.</p>
                    </div>
                    <a :href="'/vehicle-cost/cost-per-mile?app=' + selectedApp + '&miles=' + miles" onclick="gtag('event', 'cta_click', { 'event_category': 'funnel', 'event_label': 'view_car_cost' })" class="block text-center py-2 bg-slate-900 text-white rounded-lg text-sm font-bold hover:bg-slate-800">View Car Cost</a>
                </div>

                <!-- Insurance GAP Card -->
                <div class="bg-white p-5 rounded-xl border border-slate-200 hover:border-slate-400 transition-all flex flex-col justify-between relative">
                    <div>
                        <h4 class="font-bold text-slate-900 flex items-center gap-2 mb-2">🛡️ Coverage Risk</h4>
                        <p class="text-xs text-slate-600 mb-4">Insurer policies vary. You may have a coverage gap.</p>
                    </div>
                    <a :href="'/insurance/rideshare-basics?app=' + selectedApp" onclick="gtag('event', 'cta_click', { 'event_category': 'funnel', 'event_label': 'check_coverage' })" class="block text-center py-2 bg-slate-900 text-white rounded-lg text-sm font-bold hover:bg-slate-800">Check Coverage</a>
                </div>
            </div>
        </div>
        @endif
    </div>
</section>
