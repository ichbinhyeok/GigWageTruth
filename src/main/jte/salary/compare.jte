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

                <div class="text-center">
                    <a href="/salary/directory" class="text-slate-500 hover:text-slate-800 font-bold underline transition-colors">See all cities</a>
                </div>
            </div>
        </div>
    `
)
