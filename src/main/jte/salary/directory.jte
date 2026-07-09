@import com.gigwager.model.SeoMeta
@import com.gigwager.model.CityData
@param java.util.Set<String> comparableCitySlugs
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        "Uber and DoorDash Driver Earnings by City: US Directory",
        "Browse Uber and DoorDash city earnings pages, then jump into compare pages, app hubs, rankings, and calculator-based estimates after expenses.",
        com.gigwager.util.AppConstants.BASE_URL + "/salary/directory",
        com.gigwager.util.AppConstants.BASE_URL + "/og-image.jpg"
    ),
    content = @`
        <div class="bg-slate-50 min-h-screen pb-24">
            
            <!-- Hero -->
            <div class="bg-slate-900 py-20 text-center px-6">
                <h1 class="text-4xl md:text-5xl font-bold text-white mb-6">
                    Uber and DoorDash <span class="text-emerald-400">Driver Earnings by City</span>
                </h1>
                <p class="text-lg text-slate-400 max-w-2xl mx-auto">
                    Browse the full city earnings directory when you want a market list first, then jump into compare pages, app hubs,
                    rankings, or city pages for deeper after-expenses analysis.
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

                    <section class="bg-slate-900 rounded-2xl p-6 mb-8 text-white">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-2">Priority earnings reports</p>
                        <h2 class="text-2xl font-black mb-3">Start with the pages that have driver evidence</h2>
                        <p class="text-sm text-slate-300 mb-5">These pages are the fastest path into city-specific field notes, active-time checks, and after-expenses estimates.</p>
                        <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-3">
                            <a href="/salary/doordash/denver/side-hustle" class="rounded-xl bg-white/5 border border-white/10 px-4 py-3 hover:bg-white/10 transition-colors">
                                <strong class="block">DoorDash Denver side-hustle</strong>
                                <span class="text-xs text-slate-300">Suburb mileage check</span>
                            </a>
                            <a href="/salary/doordash/denver/hourly-pay" class="rounded-xl bg-white/5 border border-white/10 px-4 py-3 hover:bg-white/10 transition-colors">
                                <strong class="block">DoorDash Denver hourly pay</strong>
                                <span class="text-xs text-slate-300">City pay query page</span>
                            </a>
                            <a href="/salary/doordash/phoenix" class="rounded-xl bg-white/5 border border-white/10 px-4 py-3 hover:bg-white/10 transition-colors">
                                <strong class="block">DoorDash Phoenix</strong>
                                <span class="text-xs text-slate-300">Heat and spread check</span>
                            </a>
                            <a href="/salary/uber/chicago" class="rounded-xl bg-white/5 border border-white/10 px-4 py-3 hover:bg-white/10 transition-colors">
                                <strong class="block">Uber Chicago</strong>
                                <span class="text-xs text-slate-300">Airport and bar-close check</span>
                            </a>
                            <a href="/best-cities/doordash" class="rounded-xl bg-white/5 border border-white/10 px-4 py-3 hover:bg-white/10 transition-colors">
                                <strong class="block">DoorDash highest-paying cities</strong>
                                <span class="text-xs text-slate-300">Ranking intent page</span>
                            </a>
                            <a href="/reports/doordash-driver-hourly-pay-2026" class="rounded-xl bg-white/5 border border-white/10 px-4 py-3 hover:bg-white/10 transition-colors">
                                <strong class="block">DoorDash hourly pay 2026 report</strong>
                                <span class="text-xs text-slate-300">City pay dataset</span>
                            </a>
                        </div>
                    </section>

                    <section class="bg-emerald-50 rounded-2xl border border-emerald-100 p-6 mb-8">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Hourly earnings quick wins</p>
                        <h2 class="text-2xl font-black text-slate-900 mb-3">Hourly pay reports to check first</h2>
                        <p class="text-sm text-slate-600 mb-5">These reports answer the hourly earnings, city pay, and after-expenses questions drivers usually compare before choosing a market.</p>
                        <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-3">
                            <a href="/reports/uber-driver-hourly-earnings-2026" class="rounded-xl bg-white border border-emerald-200 px-4 py-3 hover:border-emerald-300 transition-colors">
                                <strong class="block text-slate-900">Uber driver hourly earnings 2026 report</strong>
                                <span class="text-xs text-slate-600">Six-city comparison and citation page.</span>
                            </a>
                            <a href="/reports/doordash-driver-hourly-pay-2026" class="rounded-xl bg-white border border-emerald-200 px-4 py-3 hover:border-emerald-300 transition-colors">
                                <strong class="block text-slate-900">DoorDash driver hourly pay 2026 report</strong>
                                <span class="text-xs text-slate-600">Eight-city pay dataset and $100/day bridge.</span>
                            </a>
                            <a href="/salary/doordash/denver/hourly-pay" class="rounded-xl bg-white border border-emerald-100 px-4 py-3 hover:border-emerald-300 transition-colors">
                                <strong class="block text-slate-900">DoorDash hourly pay Denver CO</strong>
                                <span class="text-xs text-slate-600">City-specific hourly pay after expenses.</span>
                            </a>
                            <a href="/salary/doordash/phoenix/how-much-can-you-make" class="rounded-xl bg-white border border-emerald-100 px-4 py-3 hover:border-emerald-300 transition-colors">
                                <strong class="block text-slate-900">How much can you make DoorDashing in Phoenix?</strong>
                                <span class="text-xs text-slate-600">Weekly, monthly, and daily target math.</span>
                            </a>
                            <a href="/salary/uber/atlanta" class="rounded-xl bg-white border border-emerald-100 px-4 py-3 hover:border-emerald-300 transition-colors">
                                <strong class="block text-slate-900">Uber driver hourly earnings Atlanta GA 2026</strong>
                                <span class="text-xs text-slate-600">After-expenses hourly pay estimate.</span>
                            </a>
                            <a href="/salary/uber/los-angeles" class="rounded-xl bg-white border border-emerald-100 px-4 py-3 hover:border-emerald-300 transition-colors">
                                <strong class="block text-slate-900">Rideshare driver hourly earnings Los Angeles CA</strong>
                                <span class="text-xs text-slate-600">Traffic, miles, and net hourly reality check.</span>
                            </a>
                            <a href="/salary/uber/austin" class="rounded-xl bg-white border border-emerald-100 px-4 py-3 hover:border-emerald-300 transition-colors">
                                <strong class="block text-slate-900">Uber driver hourly earnings Austin TX 2026</strong>
                                <span class="text-xs text-slate-600">Weekend and selective-shift planning.</span>
                            </a>
                            <a href="/salary/uber/chicago" class="rounded-xl bg-white border border-emerald-100 px-4 py-3 hover:border-emerald-300 transition-colors">
                                <strong class="block text-slate-900">Uber driver hourly earnings Chicago IL 2026</strong>
                                <span class="text-xs text-slate-600">Airport and bar-close earning checks.</span>
                            </a>
                            <a href="/salary/uber/houston" class="rounded-xl bg-white border border-emerald-100 px-4 py-3 hover:border-emerald-300 transition-colors">
                                <strong class="block text-slate-900">Rideshare driver hourly earnings Houston TX</strong>
                                <span class="text-xs text-slate-600">Mileage and spread-out market check.</span>
                            </a>
                            <a href="/salary/uber/orlando" class="rounded-xl bg-white border border-emerald-100 px-4 py-3 hover:border-emerald-300 transition-colors">
                                <strong class="block text-slate-900">Uber driver earnings Orlando Florida 2026</strong>
                                <span class="text-xs text-slate-600">Tourism demand and fuel-cost baseline.</span>
                            </a>
                        </div>
                    </section>

                    <section class="bg-indigo-50 rounded-2xl border border-indigo-100 p-6 mb-8">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-indigo-700 mb-2">Daily click targets</p>
                        <h2 class="text-2xl font-black text-slate-900 mb-3">Goal-based earnings pages</h2>
                        <p class="text-sm text-slate-600 mb-5">These links give Google a direct crawl path into the new $100/day, $1,000/month, and nights/weekends pages.</p>
                        <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-3">
                            <a href="/salary/doordash/phoenix/100-a-day" class="rounded-xl bg-white border border-indigo-100 px-4 py-3 hover:border-indigo-200 transition-colors">
                                <strong class="block text-slate-900">DoorDash Phoenix $100/day</strong>
                                <span class="text-xs text-slate-600">Daily net target</span>
                            </a>
                            <a href="/salary/uber/chicago/100-a-day" class="rounded-xl bg-white border border-indigo-100 px-4 py-3 hover:border-indigo-200 transition-colors">
                                <strong class="block text-slate-900">Uber Chicago $100/day</strong>
                                <span class="text-xs text-slate-600">Daily net target</span>
                            </a>
                            <a href="/salary/doordash/denver/1000-a-month" class="rounded-xl bg-white border border-indigo-100 px-4 py-3 hover:border-indigo-200 transition-colors">
                                <strong class="block text-slate-900">DoorDash Denver $1,000/month</strong>
                                <span class="text-xs text-slate-600">Monthly side-income plan</span>
                            </a>
                            <a href="/salary/uber/los-angeles/1000-a-month" class="rounded-xl bg-white border border-indigo-100 px-4 py-3 hover:border-indigo-200 transition-colors">
                                <strong class="block text-slate-900">Uber Los Angeles $1,000/month</strong>
                                <span class="text-xs text-slate-600">Monthly side-income plan</span>
                            </a>
                            <a href="/salary/doordash/dallas/nights-weekends" class="rounded-xl bg-white border border-indigo-100 px-4 py-3 hover:border-indigo-200 transition-colors">
                                <strong class="block text-slate-900">DoorDash Dallas nights/weekends</strong>
                                <span class="text-xs text-slate-600">Shift-window plan</span>
                            </a>
                            <a href="/salary/doordash/dallas/best-areas" class="rounded-xl bg-white border border-indigo-100 px-4 py-3 hover:border-indigo-200 transition-colors">
                                <strong class="block text-slate-900">Best areas to DoorDash in Dallas</strong>
                                <span class="text-xs text-slate-600">Zone and mileage plan</span>
                            </a>
                            <a href="/salary/doordash/chicago/uber-eats-vs-doordash" class="rounded-xl bg-white border border-indigo-100 px-4 py-3 hover:border-indigo-200 transition-colors">
                                <strong class="block text-slate-900">Uber Eats vs DoorDash Chicago</strong>
                                <span class="text-xs text-slate-600">Delivery-app comparison</span>
                            </a>
                            <a href="/salary/uber/nashville/nights-weekends" class="rounded-xl bg-white border border-indigo-100 px-4 py-3 hover:border-indigo-200 transition-colors">
                                <strong class="block text-slate-900">Uber Nashville nights/weekends</strong>
                                <span class="text-xs text-slate-600">Shift-window plan</span>
                            </a>
                        </div>
                    </section>
                    
                    <h2 class="text-2xl font-bold text-slate-900 mb-8">Browse City Earnings Reports</h2>
                    
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
                                            Uber Earnings
                                        </a>
                                        <a href="/salary/doordash/${city.getSlug()}" 
                                           class="text-center px-3 py-2 bg-red-600 text-white text-sm font-semibold rounded-lg hover:bg-red-700 transition">
                                            DoorDash Earnings
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
