@import com.gigwager.model.SeoMeta

@param String customTitle

@template.layout.main(
    seoMeta = new SeoMeta(
        customTitle != null ? customTitle : "Gig Driver Blog - The Truth About App Pay",
        "Honest guides, data-backed analysis, and strategies to increase your hourly wage on Uber, DoorDash, and Lyft.",
        "https://gigwagetruth.com/blog",
        "https://gigwagetruth.com/og-image.jpg"
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
                                Depreciation is invisible until your car dies. We do the math on what 100 miles really costs you in 2026.
                            </p>
                            <div class="mt-auto flex items-center gap-2 text-xs text-slate-400 font-medium">
                                <span>6 min read</span>
                                <span>•</span>
                                <span>Jan 2026</span>
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
                                Multi-Apping Guide 2026: How to Double Your Hourly Wage
                            </h2>
                            <p class="text-slate-600 text-sm mb-4 line-clamp-3">
                                Stop relying on one app. Learn the art of "stacking" orders from Uber, DoorDash, and GrubHub without getting deactivated.
                            </p>
                            <div class="mt-auto flex items-center gap-2 text-xs text-slate-400 font-medium">
                                <span>10 min read</span>
                                <span>•</span>
                                <span>Jan 2026</span>
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
                                Gig Worker Tax 101: Don't Overpay the IRS
                            </h2>
                            <p class="text-slate-600 text-sm mb-4 line-clamp-3">
                                Standard Mileage vs. Actual Expenses. Learn which method saves you thousands and avoids an audit nightmare.
                            </p>
                            <div class="mt-auto flex items-center gap-2 text-xs text-slate-400 font-medium">
                                <span>8 min read</span>
                                <span>•</span>
                                <span>Jan 2026</span>
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
                                Uber vs. DoorDash: Which App Pays More in 2026?
                            </h2>
                            <p class="text-slate-600 text-sm mb-4 line-clamp-3">
                                Rideshare vs. Delivery. We analyze earnings, vehicle wear, and stress levels to help you choose the right hustle.
                            </p>
                            <div class="mt-auto flex items-center gap-2 text-xs text-slate-400 font-medium">
                                <span>12 min read</span>
                                <span>•</span>
                                <span>Jan 2026</span>
                            </div>
                        </div>
                    </a>

                </div>
            </div>

        </div>
    `
)
