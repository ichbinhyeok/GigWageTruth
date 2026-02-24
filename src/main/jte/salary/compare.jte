@import com.gigwager.model.SeoMeta
@import com.gigwager.model.CityData
@import com.gigwager.model.CityScenario
@import com.gigwager.util.AppConstants

@param CityData city
@param CityScenario uberScenario
@param CityScenario doordashScenario
@param String lastUpdated
@param SeoMeta seoMeta
@param Boolean noIndex = false

@template.layout.main(
    seoMeta = seoMeta,
    noIndex = noIndex,
    content = @`
        <div class="bg-slate-50 min-h-screen py-16 px-6">
            <div class="max-w-4xl mx-auto">
                <div class="text-center mb-12">
                    <span class="inline-block px-4 py-1 rounded-full bg-slate-200 text-slate-800 font-bold text-sm tracking-wider mb-4 uppercase">
                        ${city.getCityName()} Comparison
                    </span>
                    <h1 class="text-4xl lg:text-5xl font-black text-slate-900 mb-6 tracking-tight">
                        Uber vs DoorDash in ${city.getCityName()}
                    </h1>
                    <p class="text-xl text-slate-600 max-w-2xl mx-auto">
                        Which app actually pays more per hour in ${city.getCityName()}? We compared net hourly pay after local gas prices and self-employment taxes.
                    </p>
                </div>

                <!-- AEO Answer-First Block -->
                <div class="bg-indigo-50 border-l-4 border-indigo-500 p-6 rounded-r-xl mb-12 shadow-sm">
                    <h2 class="text-xl font-bold text-indigo-900 mb-2 flex items-center gap-2">
                        <svg class="w-6 h-6 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path></svg>
                        The TL;DR for ${city.getCityName()}
                    </h2>
                    <p class="text-indigo-800 font-medium">
                        In ${city.getCityName()}, 
                        @if(uberScenario.getNetHourly() > doordashScenario.getNetHourly())
                            <strong>Uber tends to pay more per hour</strong> due to higher base fares and longer trip consistency, while DoorDash is better for short-distance drivers trying to avoid high depreciation.
                        @else
                            <strong>DoorDash tends to pay more per hour</strong>, as frequent short trips scale better against local traffic patterns compared to Uber's longer waits.
                        @endif
                    </p>
                </div>

                <div class="grid md:grid-cols-2 gap-8 mb-12">
                    <!-- Uber Card -->
                    <div class="bg-white rounded-3xl p-8 border-2 ${uberScenario.getNetHourly() >= doordashScenario.getNetHourly() ? "border-emerald-400 shadow-xl shadow-emerald-500/10" : "border-slate-200"} relative">
                        @if(uberScenario.getNetHourly() >= doordashScenario.getNetHourly())
                            <div class="absolute -top-4 left-1/2 -translate-x-1/2 bg-emerald-500 text-white font-bold px-4 py-1 rounded-full text-sm">
                                HIGHER PAY
                            </div>
                        @endif
                        <h2 class="text-3xl font-black text-slate-900 mb-2">Uber</h2>
                        <p class="text-slate-500 mb-6 pb-6 border-b border-slate-100">Side-Hustle Scenario (25 hrs/wk)</p>
                        
                        <div class="mb-8">
                            <span class="block text-sm font-bold text-slate-500 uppercase tracking-widest mb-1">Net Hourly Pay</span>
                            <span class="text-5xl font-black text-${uberScenario.getNetHourly() >= doordashScenario.getNetHourly() ? "emerald-600" : "slate-900"}">
                                $${String.format("%.2f", uberScenario.getNetHourly())}
                            </span>
                        </div>

                        <div class="space-y-4 mb-8">
                            <div class="flex justify-between items-center text-sm">
                                <span class="text-slate-500">Gross Weekly</span>
                                <span class="font-bold text-slate-700">$${uberScenario.getGrossWeekly()}</span>
                            </div>
                            <div class="flex justify-between items-center text-sm">
                                <span class="text-slate-500">Est. Miles Driven</span>
                                <span class="font-bold text-slate-700">${uberScenario.getMiles()} mi</span>
                            </div>
                            <div class="flex justify-between items-center text-sm">
                                <span class="text-red-500">Tax Risk (Quarterly)</span>
                                <span class="font-bold text-red-600">~$${String.format("%.0f", Math.max(0, (uberScenario.getGrossWeekly() - (uberScenario.getMiles() * AppConstants.IRS_MILEAGE_RATE)) * AppConstants.SELF_EMPLOYMENT_TAX_RATE) * 13 )}</span>
                            </div>
                        </div>

                        <a href="/salary/uber/${city.getSlug()}" class="block w-full text-center py-4 bg-slate-900 hover:bg-slate-800 text-white font-bold rounded-xl transition-all">View Uber Details</a>
                    </div>

                    <!-- DoorDash Card -->
                    <div class="bg-white rounded-3xl p-8 border-2 ${doordashScenario.getNetHourly() > uberScenario.getNetHourly() ? "border-emerald-400 shadow-xl shadow-emerald-500/10" : "border-slate-200"} relative">
                        @if(doordashScenario.getNetHourly() > uberScenario.getNetHourly())
                            <div class="absolute -top-4 left-1/2 -translate-x-1/2 bg-emerald-500 text-white font-bold px-4 py-1 rounded-full text-sm">
                                HIGHER PAY
                            </div>
                        @endif
                        <h2 class="text-3xl font-black text-slate-900 mb-2">DoorDash</h2>
                        <p class="text-slate-500 mb-6 pb-6 border-b border-slate-100">Side-Hustle Scenario (25 hrs/wk)</p>
                        
                        <div class="mb-8">
                            <span class="block text-sm font-bold text-slate-500 uppercase tracking-widest mb-1">Net Hourly Pay</span>
                            <span class="text-5xl font-black text-${doordashScenario.getNetHourly() > uberScenario.getNetHourly() ? "emerald-600" : "slate-900"}">
                                $${String.format("%.2f", doordashScenario.getNetHourly())}
                            </span>
                        </div>

                        <div class="space-y-4 mb-8">
                            <div class="flex justify-between items-center text-sm">
                                <span class="text-slate-500">Gross Weekly</span>
                                <span class="font-bold text-slate-700">$${doordashScenario.getGrossWeekly()}</span>
                            </div>
                            <div class="flex justify-between items-center text-sm">
                                <span class="text-slate-500">Est. Miles Driven</span>
                                <span class="font-bold text-slate-700">${doordashScenario.getMiles()} mi</span>
                            </div>
                            <div class="flex justify-between items-center text-sm">
                                <span class="text-red-500">Tax Risk (Quarterly)</span>
                                <span class="font-bold text-red-600">~$${String.format("%.0f", Math.max(0, (doordashScenario.getGrossWeekly() - (doordashScenario.getMiles() * AppConstants.IRS_MILEAGE_RATE)) * AppConstants.SELF_EMPLOYMENT_TAX_RATE) * 13 )}</span>
                            </div>
                        </div>

                        <a href="/salary/doordash/${city.getSlug()}" class="block w-full text-center py-4 bg-slate-900 hover:bg-slate-800 text-white font-bold rounded-xl transition-all">View DoorDash Details</a>
                    </div>
                </div>

                <div class="text-center mb-16">
                    <a href="/salary/directory" class="text-slate-500 hover:text-slate-800 font-bold underline transition-colors">See all cities</a>
                </div>

                <!-- When to choose which app -->
                <section class="bg-white rounded-3xl p-8 border border-slate-200 shadow-sm mb-16">
                    <h2 class="text-2xl font-black text-slate-900 mb-6 text-center">When to Choose Which App</h2>
                    <div class="grid md:grid-cols-2 gap-8">
                        <div>
                            <h3 class="font-bold text-slate-800 mb-4 border-b pb-2 flex items-center gap-2">Choose Uber If:</h3>
                            <ul class="space-y-3 text-sm text-slate-600">
                                <li class="flex items-start gap-2"><span class="text-emerald-500">✓</span> You own an electric/hybrid vehicle (high MPG mitigates longer rides).</li>
                                <li class="flex items-start gap-2"><span class="text-emerald-500">✓</span> You prefer driving passengers over finding apartment numbers and carrying food.</li>
                                <li class="flex items-start gap-2"><span class="text-emerald-500">✓</span> You plan to drive early mornings or late-night weekends (bar rush).</li>
                            </ul>
                        </div>
                        <div>
                            <h3 class="font-bold text-slate-800 mb-4 border-b pb-2 flex items-center gap-2">Choose DoorDash If:</h3>
                            <ul class="space-y-3 text-sm text-slate-600">
                                <li class="flex items-start gap-2"><span class="text-red-500">✓</span> Your car is older and you want to minimize highway mileage.</li>
                                <li class="flex items-start gap-2"><span class="text-red-500">✓</span> You hate small talk and prefer listening to audiobooks uninterrupted.</li>
                                <li class="flex items-start gap-2"><span class="text-red-500">✓</span> Your primary driving hours are 11 AM - 1 PM or 5 PM - 8 PM (meal times).</li>
                            </ul>
                        </div>
                    </div>
                </section>

                <!-- Internal Links Web -->
                @template.components.related_pages(app = "uber")
            </div>
        </div>
    `
)
