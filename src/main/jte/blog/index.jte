@import com.gigwager.model.SeoMeta
@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        customTitle != null ? customTitle : "Gig Driver Guides - Taxes, Multi-Apping, and Real Pay",
        "Decision-first guides for gig workers: quarterly taxes, multi-apping, hidden costs, and after-expenses earnings.",
        com.gigwager.util.AppConstants.BASE_URL + "/blog",
        com.gigwager.util.AppConstants.BASE_URL + "/og-image.jpg"
    ),
    content = @`
        <div class="bg-slate-50 min-h-screen pb-24">
            
            <!-- Hero -->
            <div class="bg-slate-900 py-20 text-center px-6">
                <h1 class="text-4xl md:text-5xl font-bold text-white mb-6">Gig Driver <span class="text-emerald-400">Decision Guides</span></h1>
                <p class="text-lg text-slate-400 max-w-2xl mx-auto">
                    Start with the question you are trying to answer: taxes, hidden car costs, app comparison, or multi-apping without getting deactivated.
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
                                Hidden Costs of Uber and DoorDash: Why "$100 Gross" Can Mean "$60 Net"
                            </h2>
                            <p class="text-slate-600 text-sm mb-4 line-clamp-3">
                                Depreciation, return miles, and maintenance turn decent-looking gross pay into weak net pay. This guide shows where the money disappears.
                            </p>
                            <div class="mt-auto flex items-center gap-2 text-xs text-slate-400 font-medium">
                                <span>9 min read</span>
                                <span>|</span>
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
                                How to Multi-App Uber and DoorDash Without Getting Deactivated
                            </h2>
                            <p class="text-slate-600 text-sm mb-4 line-clamp-3">
                                Learn the clean stacking system for Uber and DoorDash. Cut dead time, avoid platform violations, and raise net hourly pay without juggling two active jobs.
                            </p>
                            <div class="mt-auto flex items-center gap-2 text-xs text-slate-400 font-medium">
                                <span>10 min read</span>
                                <span>|</span>
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
                                Gig Worker Tax Guide 2026: Quarterly Taxes, Mileage, and Write-Offs
                            </h2>
                            <p class="text-slate-600 text-sm mb-4 line-clamp-3">
                                Learn how quarterly taxes, mileage tracking, and common deductions actually work for 1099 gig workers.
                            </p>
                            <div class="mt-auto flex items-center gap-2 text-xs text-slate-400 font-medium">
                                <span>8 min read</span>
                                <span>|</span>
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
                                Uber vs DoorDash: Which App Pays More After Expenses?
                            </h2>
                            <p class="text-slate-600 text-sm mb-4 line-clamp-3">
                                Compare the two apps after mileage, taxes, wait time, and car wear so you can choose the better fit for your market and routine.
                            </p>
                            <div class="mt-auto flex items-center gap-2 text-xs text-slate-400 font-medium">
                                <span>7 min read</span>
                                <span>|</span>
                                <span>Jan 18, 2026</span>
                            </div>
                        </div>
                    </a>

                </div>
            </div>

        </div>
    `
)
