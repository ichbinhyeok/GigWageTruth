@import com.gigwager.model.SeoMeta
@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        customTitle != null ? customTitle : "Gig Driver Blog - The Truth About App Pay",
        "Honest guides, data-backed analysis, and strategies to increase your hourly wage on Uber, DoorDash, and Lyft.",
        "https://gigverdict.com/blog",
        "https://gigverdict.com/og-image.jpg"
    ),
    content = @`
        <div class="bg-slate-50 min-h-screen pb-24">
            
            <!-- Hero -->
            <div class="bg-slate-900 py-20 text-center px-6">
                <h1 class="text-4xl md:text-5xl font-bold text-white mb-6">Gig Driver <span class="text-emerald-400">Truth</span> Blog</h1>
                <p class="text-lg text-slate-400 max-w-2xl mx-auto">
                    No fluff. No corporate PR. Just the raw data and strategies you need to actually make money in 2026.
                </p>
            </div>

                <!-- Article Grid -->
            <div class="max-w-6xl mx-auto px-6 -mt-10 relative z-10">
                <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-8">

                    <!-- Article Card 0: Hidden Costs (New) -->
                    <a href="/blog/hidden-costs" class="group bg-white rounded-2xl overflow-hidden shadow-sm hover:shadow-xl transition-all duration-300 border border-slate-200 flex flex-col">
                        <div class="h-48 bg-slate-200 relative overflow-hidden">
                             <div class="absolute inset-0 bg-gradient-to-br from-slate-700 to-black group-hover:scale-105 transition-transform duration-500"></div>
                             <div class="absolute inset-0 flex items-center justify-center text-white/20 text-6xl font-bold">LOSS</div>
                             <div class="absolute top-4 left-4 bg-slate-900 text-white text-xs font-bold px-2 py-1 rounded-md shadow-sm border border-slate-700">MUST READ</div>
                        </div>
                        <div class="p-6 flex-1 flex flex-col">
                            <h2 class="text-xl font-bold text-slate-900 mb-3 group-hover:text-emerald-600 transition-colors">
                                The Silent Killer: Why Your "$100 Day" is Actually Only $60
                            </h2>
                            <p class="text-slate-600 text-sm mb-4 line-clamp-3">
                                Depreciation is the silent killer of gig worker profits. Learn the math of why every mile you drive is a slice of your car's value disappearing.
                            </p>
                            <div class="mt-auto flex items-center gap-2 text-xs text-slate-400 font-medium">
                                <span>9 min read</span>
                                <span>•</span>
                                <span>Jan 23, 2026</span>
                            </div>
                        </div>
                    </a>
                    
                    <!-- Article Card 1: Multi-Apping -->
                    <a href="/blog/multi-apping-guide" class="group bg-white rounded-2xl overflow-hidden shadow-sm hover:shadow-xl transition-all duration-300 border border-slate-200 flex flex-col">
                        <div class="h-48 bg-slate-200 relative overflow-hidden">
                             <div class="absolute inset-0 bg-gradient-to-br from-emerald-600 to-slate-800 group-hover:scale-105 transition-transform duration-500"></div>
                             <div class="absolute inset-0 flex items-center justify-center text-white/20 text-6xl font-bold">$$$</div>
                             <div class="absolute top-4 left-4 bg-emerald-500 text-white text-xs font-bold px-2 py-1 rounded-md shadow-sm">STRATEGY</div>
                        </div>
                        <div class="p-6 flex-1 flex flex-col">
                            <h2 class="text-xl font-bold text-slate-900 mb-3 group-hover:text-emerald-600 transition-colors">
                                Multi-Apping Masterclass 2026: Double Your Income
                            </h2>
                            <p class="text-slate-600 text-sm mb-4 line-clamp-3">
                                The secret to a $30/hr gig career isn't working harder. It's working smarter. Learn the Clean Stacking strategy to maximize every mile.
                            </p>
                            <div class="mt-auto flex items-center gap-2 text-xs text-slate-400 font-medium">
                                <span>10 min read</span>
                                <span>•</span>
                                <span>Jan 21, 2026</span>
                            </div>
                        </div>
                    </a>

                    <!-- Article Card 2: Tax Guide -->
                    <a href="/blog/tax-guide" class="group bg-white rounded-2xl overflow-hidden shadow-sm hover:shadow-xl transition-all duration-300 border border-slate-200 flex flex-col">
                        <div class="h-48 bg-slate-200 relative overflow-hidden">
                             <div class="absolute inset-0 bg-gradient-to-br from-blue-600 to-slate-800 group-hover:scale-105 transition-transform duration-500"></div>
                             <div class="absolute inset-0 flex items-center justify-center text-white/20 text-6xl font-bold">TAX</div>
                             <div class="absolute top-4 left-4 bg-blue-500 text-white text-xs font-bold px-2 py-1 rounded-md shadow-sm">FINANCE</div>
                        </div>
                        <div class="p-6 flex-1 flex flex-col">
                            <h2 class="text-xl font-bold text-slate-900 mb-3 group-hover:text-blue-600 transition-colors">
                                Gig Worker Tax Guide 2026: Don't Overpay the IRS
                            </h2>
                            <p class="text-slate-600 text-sm mb-4 line-clamp-3">
                                The 2026 IRS mileage rate is 72.5 cents. Learn exactly how to deduct your car, phone, and hot bags to pay zero taxes legally.
                            </p>
                            <div class="mt-auto flex items-center gap-2 text-xs text-slate-400 font-medium">
                                <span>8 min read</span>
                                <span>•</span>
                                <span>Jan 15, 2026</span>
                            </div>
                        </div>
                    </a>

                    <!-- Article Card 3: Comparison -->
                    <a href="/blog/uber-vs-doordash" class="group bg-white rounded-2xl overflow-hidden shadow-sm hover:shadow-xl transition-all duration-300 border border-slate-200 flex flex-col">
                        <div class="h-48 bg-slate-200 relative overflow-hidden">
                             <div class="absolute inset-0 bg-gradient-to-br from-red-600 to-slate-900 group-hover:scale-105 transition-transform duration-500"></div>
                             <div class="absolute inset-0 flex items-center justify-center text-white/20 text-6xl font-bold">VS</div>
                             <div class="absolute top-4 left-4 bg-red-500 text-white text-xs font-bold px-2 py-1 rounded-md shadow-sm">COMPARISON</div>
                        </div>
                        <div class="p-6 flex-1 flex flex-col">
                            <h2 class="text-xl font-bold text-slate-900 mb-3 group-hover:text-red-600 transition-colors">
                                Uber vs. DoorDash 2026: Which App Actually Pays More?
                            </h2>
                            <p class="text-slate-600 text-sm mb-4 line-clamp-3">
                                Don't just look at the gross pay. We compare stress, vehicle wear, and hidden expenses to see which app is better for your personality.
                            </p>
                            <div class="mt-auto flex items-center gap-2 text-xs text-slate-400 font-medium">
                                <span>7 min read</span>
                                <span>•</span>
                                <span>Jan 18, 2026</span>
                            </div>
                        </div>
                    </a>

                </div>
            </div>

        </div>
    `
)
