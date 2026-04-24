@import com.gigwager.model.SeoMeta
@import com.gigwager.model.CityData
@param java.util.Set<String> comparableCitySlugs
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        "Uber and DoorDash City Pay Calculators: US Directory",
        "Browse Uber and DoorDash city pay calculators, then jump into compare pages, app hubs, rankings, and calculator-based city estimates after expenses.",
        com.gigwager.util.AppConstants.BASE_URL + "/salary/directory",
        com.gigwager.util.AppConstants.BASE_URL + "/og-image.jpg"
    ),
    content = @`
        <div class="bg-slate-50 min-h-screen pb-24">
            
            <!-- Hero -->
            <div class="bg-slate-900 py-20 text-center px-6">
                <h1 class="text-4xl md:text-5xl font-bold text-white mb-6">
                    Uber and DoorDash <span class="text-emerald-400">City Pay Calculators</span>
                </h1>
                <p class="text-lg text-slate-400 max-w-2xl mx-auto">
                    Browse the full city calculator directory when you want a market list first, then jump into compare pages, app hubs,
                    rankings, or city calculators for deeper after-expenses analysis.
                </p>
            </div>

            <!-- Directory Grid -->
            <div class="max-w-6xl mx-auto px-6 -mt-10 relative z-10">
                <div class="bg-white rounded-2xl shadow-xl border border-slate-200 p-8">
                    <div class="grid md:grid-cols-2 gap-4 mb-8">
                        <a href="/uber/where-you-can-drive" class="rounded-2xl border border-sky-200 bg-sky-50/70 p-5 hover:bg-sky-50 transition-colors">
                            <p class="text-xs font-black uppercase tracking-[0.18em] text-sky-700 mb-2">Coverage Intent</p>
                            <h2 class="text-xl font-black text-slate-900 mb-2">Uber coverage guide</h2>
                            <p class="text-sm text-slate-600">Use this first if your question is whether Uber is active in a city before comparing pay reports.</p>
                        </a>
                        <a href="/doordash/where-you-can-dash" class="rounded-2xl border border-amber-200 bg-amber-50/70 p-5 hover:bg-amber-50 transition-colors">
                            <p class="text-xs font-black uppercase tracking-[0.18em] text-amber-700 mb-2">Coverage Intent</p>
                            <h2 class="text-xl font-black text-slate-900 mb-2">DoorDash availability guide</h2>
                            <p class="text-sm text-slate-600">Use this first if your question is whether you can dash in a market before comparing city earnings.</p>
                        </a>
                    </div>
                    
                    <h2 class="text-2xl font-bold text-slate-900 mb-8">Browse City Pay Calculators</h2>
                    
                    <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                        @for(var city : CityData.values())
                            @if(!city.getMarketTier().name().equals("LOW"))
                            <div class="group">
                                <div class="bg-slate-50 border border-slate-200 rounded-xl p-6 hover:shadow-lg hover:border-emerald-500 transition-all">
                                    <div class="flex items-start justify-between mb-3">
                                        <div>
                                            <h3 class="text-xl font-bold text-slate-900 group-hover:text-emerald-600 transition">
                                                ${city.getCityName()}, ${city.getState()}
                                            </h3>
                                            <div class="flex items-center gap-2 mt-1">
                                                <span class="text-xs px-2 py-1 rounded-md font-semibold
                                                    ${city.getMarketTier().name().equals("HIGH") ? "bg-red-100 text-red-700" : 
                                                      city.getMarketTier().name().equals("MED") ? "bg-amber-100 text-amber-700" : 
                                                      "bg-emerald-100 text-emerald-700"}">
                                                    ${city.getMarketTier()} Cost Market
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div class="text-sm text-slate-600 mb-4">
                                        <div class="flex justify-between">
                                            <span>Gas Price:</span>
                                            <span class="font-bold">$${String.format("%.2f", city.getGasPrice())}/gal</span>
                                        </div>
                                        <div class="flex justify-between">
                                            <span>Min Wage:</span>
                                            <span class="font-bold">$${String.format("%.2f", city.getMinWage())}/hr</span>
                                        </div>
                                    </div>
                                    
                                    <div class="grid grid-cols-2 gap-2">
                                        <a href="/salary/uber/${city.getSlug()}" 
                                           class="text-center px-3 py-2 bg-slate-900 text-white text-sm font-semibold rounded-lg hover:bg-slate-800 transition">
                                            Uber Calculator
                                        </a>
                                        <a href="/salary/doordash/${city.getSlug()}" 
                                           class="text-center px-3 py-2 bg-red-600 text-white text-sm font-semibold rounded-lg hover:bg-red-700 transition">
                                            DoorDash Calculator
                                        </a>
                                    </div>
                                    @if(comparableCitySlugs.contains(city.getSlug()))
                                        <a href="/compare/${city.getSlug()}/uber-vs-doordash"
                                           class="mt-2 block text-center px-3 py-2 bg-white border border-slate-300 text-slate-900 text-sm font-semibold rounded-lg hover:bg-slate-100 transition">
                                            Compare Uber vs DoorDash
                                        </a>
                                    @endif
                                </div>
                            </div>
                            @else
                            <!-- Low Data City -->
                            <div class="group opacity-50 grayscale hover:grayscale-0 transition-all">
                                <div class="bg-slate-50 border border-slate-200 rounded-xl p-6">
                                    <div class="flex items-start justify-between mb-3">
                                        <div>
                                            <h3 class="text-xl font-bold text-slate-900">
                                                ${city.getCityName()}, ${city.getState()}
                                            </h3>
                                            <div class="flex items-center gap-2 mt-1">
                                                <span class="text-xs px-2 py-1 rounded-md font-semibold bg-slate-200 text-slate-600">
                                                    Low-Data Market
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="mt-4">
                                        <p class="text-xs text-slate-500">Full verified earnings data coming soon.</p>
                                    </div>
                                </div>
                            </div>
                            @endif
                        @endfor
                    </div>
                </div>
            </div>
        </div>
    `
)
