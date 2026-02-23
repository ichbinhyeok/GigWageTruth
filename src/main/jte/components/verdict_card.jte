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
        
        <!-- Personalized Risk Trigger Cards -->
        @if(estimatedTaxes != null && estimatedVehicleCost != null)
        <div class="bg-red-50/50 rounded-2xl p-6 border border-red-200 mt-6">
            <h3 class="text-xl font-bold text-slate-900 mb-2">⚠️ Your Personalized Risk Factors</h3>
            <p class="text-slate-600 mb-6 text-sm">Based on the numbers you just entered.</p>
            
            <div class="grid md:grid-cols-3 gap-4">
                <!-- Tax Risk Card -->
                <div class="bg-white p-5 rounded-xl border-2 border-red-100 hover:border-red-500 transition-all">
                    <h4 class="font-bold text-slate-900 flex items-center gap-2 mb-2">🏦 Self-Employment Tax</h4>
                    <p class="text-xs text-slate-600 mb-3">You will likely owe <strong>$${String.format("%.0f", estimatedTaxes * 50)}</strong> this year. Are you making quarterly estimated payments?</p>
                    <a href="/taxes/quarterly-estimator" class="text-xs font-bold text-red-600 hover:text-red-700">Calculate your tax risk →</a>
                </div>
                
                <!-- Vehicle Cost Card -->
                <div class="bg-white p-5 rounded-xl border-2 border-red-100 hover:border-red-500 transition-all">
                    <h4 class="font-bold text-slate-900 flex items-center gap-2 mb-2">🚗 Vehicle Depreciation</h4>
                    <p class="text-xs text-slate-600 mb-3">Your car lost <strong>$${String.format("%.0f", estimatedVehicleCost)}</strong> in value/costs this week alone. Are you driving the right car?</p>
                    <a href="/vehicle-cost/cost-per-mile" class="text-xs font-bold text-red-600 hover:text-red-700">See true cost per mile →</a>
                </div>

                <!-- Insurance GAP Card -->
                <div class="bg-white p-5 rounded-xl border-2 border-red-100 hover:border-red-500 transition-all relative">
                    <div class="absolute -right-2 -top-2 bg-amber-500 text-white text-[10px] font-bold px-2 py-1 rounded-full uppercase tracking-wide border-2 border-white">CRITICAL</div>
                    <h4 class="font-bold text-slate-900 flex items-center gap-2 mb-2">🛡️ Insurance GAP</h4>
                    <p class="text-xs text-slate-600 mb-3">If you crash tomorrow, your personal insurance <strong>may deny the claim depending on your policy</strong>. Coverage depends on insurer and state. Verify policy terms.</p>
                    <a href="/insurance/rideshare-basics" class="text-xs font-bold text-red-600 hover:text-red-700">Learn how to fix this →</a>
                </div>
            </div>
        </div>
        @endif
    </div>
</section>
