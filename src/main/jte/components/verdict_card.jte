@import com.gigwager.model.Verdict

@param Verdict verdict

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
        <p class="text-lg font-bold text-${verdict.themeColor()}-700 italic">"${verdict.cta()}"</p>
    </div>
</section>
