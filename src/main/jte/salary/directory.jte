@import com.gigwager.model.SeoMeta
@import com.gigwager.model.CityData
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        "Gig Driver Salary by City - Complete US Directory",
        "Compare real Uber and DoorDash earnings across major US cities. See how gas prices, traffic, and costs affect your hourly wage.",
        "https://gigverdict.com/salary/directory",
        "https://gigverdict.com/og-image.jpg"
    ),
    content = @`
        <div class="bg-slate-50 min-h-screen pb-24">
            
            <!-- Hero -->
            <div class="bg-slate-900 py-20 text-center px-6">
                <h1 class="text-4xl md:text-5xl font-bold text-white mb-6">
                    Gig Driver Salary <span class="text-emerald-400">by City</span>
                </h1>
                <p class="text-lg text-slate-400 max-w-2xl mx-auto">
                    See real earnings after expenses for Uber and DoorDash drivers in major US cities.
                    Every city has unique gas prices, traffic patterns, and market conditions.
                </p>
            </div>

            <!-- Directory Grid -->
            <div class="max-w-6xl mx-auto px-6 -mt-10 relative z-10">
                <div class="bg-white rounded-2xl shadow-xl border border-slate-200 p-8">
                    
                    <h2 class="text-2xl font-bold text-slate-900 mb-8">Browse by City</h2>
                    
                    <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                        @for(var city : CityData.values())
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
                                            🚗 Uber
                                        </a>
                                        <a href="/salary/doordash/${city.getSlug()}" 
                                           class="text-center px-3 py-2 bg-red-600 text-white text-sm font-semibold rounded-lg hover:bg-red-700 transition">
                                            🍔 DoorDash
                                        </a>
                                    </div>
                                </div>
                            </div>
                        @endfor
                    </div>
                </div>
            </div>
        </div>
    `
)
