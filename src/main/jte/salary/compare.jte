@import com.gigwager.model.SeoMeta
@import com.gigwager.model.CityData
@import com.gigwager.model.CityScenario
@import com.gigwager.util.AppConstants

@param CityData city
@param CityScenario uberScenario
@param CityScenario doordashScenario
@param String winningAppName
@param String losingAppName
@param double winningNetHourly
@param double netHourlyGap
@param boolean nearlyTied
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
                        Uber vs DoorDash Earnings in ${city.getCityName()} After Expenses
                    </h1>
                    <p class="text-xl text-slate-600 max-w-2xl mx-auto">
                        @if (nearlyTied)
                            Side-hustle estimates in ${city.getCityName()} are effectively tied, with both apps landing near $${String.format("%.2f", winningNetHourly)}/hr net after IRS mileage proxy and self-employment tax assumptions.
                        @else
                            Current side-hustle estimates show <strong>${winningAppName}</strong> ahead by <strong>$${String.format("%.2f", netHourlyGap)}/hr net</strong> in ${city.getCityName()} after IRS mileage proxy and self-employment tax assumptions.
                        @endif
                    </p>
                </div>

                <div class="bg-white border border-slate-200 rounded-2xl p-5 mb-8">
                    <p class="text-sm text-slate-600">
                        Use this page when your question is <strong>which app pays more in ${city.getCityName()}</strong>.
                        Open the app-specific city reports below when you want the deeper part-time, side-hustle, and full-time breakdowns.
                    </p>
                </div>

                <div class="grid sm:grid-cols-2 lg:grid-cols-4 gap-3 mb-10">
                    <a href="/uber?gross=${uberScenario.getGrossWeekly()}&miles=${uberScenario.getMiles()}&hours=${uberScenario.getHours()}&gasPrice=${city.getGasPrice()}" class="rounded-2xl bg-slate-900 hover:bg-slate-800 text-white font-bold px-5 py-4 transition-colors">
                        Run Uber calculator
                    </a>
                    <a href="/doordash?gross=${doordashScenario.getGrossWeekly()}&miles=${doordashScenario.getMiles()}&hours=${doordashScenario.getHours()}&gasPrice=${city.getGasPrice()}" class="rounded-2xl bg-red-600 hover:bg-red-700 text-white font-bold px-5 py-4 transition-colors">
                        Run DoorDash calculator
                    </a>
                    <a href="/salary/uber/${city.getSlug()}" class="rounded-2xl bg-white border border-slate-200 hover:border-slate-300 text-slate-900 font-bold px-5 py-4 transition-colors">
                        Uber city report
                    </a>
                    <a href="/salary/doordash/${city.getSlug()}" class="rounded-2xl bg-white border border-slate-200 hover:border-slate-300 text-slate-900 font-bold px-5 py-4 transition-colors">
                        DoorDash city report
                    </a>
                </div>

                <div class="bg-indigo-50 border-l-4 border-indigo-500 p-6 rounded-r-xl mb-12 shadow-sm">
                    <h2 class="text-xl font-bold text-indigo-900 mb-2 flex items-center gap-2">
                        <svg class="w-6 h-6 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path></svg>
                        The TL;DR for ${city.getCityName()}
                    </h2>
                    <p class="text-indigo-800 font-medium">
                        In ${city.getCityName()},
                        @if (nearlyTied)
                            <strong>Uber and DoorDash are basically neck and neck</strong>. Our current side-hustle model shows Uber at $${String.format("%.2f", uberScenario.getNetHourly())}/hr net and DoorDash at $${String.format("%.2f", doordashScenario.getNetHourly())}/hr net, so the better choice depends more on your car, schedule, and tolerance for passenger trips.
                        @else
                            @if (uberScenario.getNetHourly() > doordashScenario.getNetHourly())
                                <strong>Uber comes out ahead</strong> at about $${String.format("%.2f", uberScenario.getNetHourly())}/hr net versus $${String.format("%.2f", doordashScenario.getNetHourly())}/hr for DoorDash. That gives Uber roughly a $${String.format("%.2f", netHourlyGap)}/hr edge in the current side-hustle model.
                            @else
                                <strong>DoorDash comes out ahead</strong> at about $${String.format("%.2f", doordashScenario.getNetHourly())}/hr net versus $${String.format("%.2f", uberScenario.getNetHourly())}/hr for Uber. That gives DoorDash roughly a $${String.format("%.2f", netHourlyGap)}/hr edge in the current side-hustle model.
                            @endif
                        @endif
                    </p>
                </div>

                <div class="grid md:grid-cols-2 gap-8 mb-12">
                    <div class="bg-white rounded-3xl p-8 border-2 ${uberScenario.getNetHourly() >= doordashScenario.getNetHourly() ? "border-emerald-400 shadow-xl shadow-emerald-500/10" : "border-slate-200"} relative">
                        @if (uberScenario.getNetHourly() >= doordashScenario.getNetHourly())
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

                    <div class="bg-white rounded-3xl p-8 border-2 ${doordashScenario.getNetHourly() > uberScenario.getNetHourly() ? "border-emerald-400 shadow-xl shadow-emerald-500/10" : "border-slate-200"} relative">
                        @if (doordashScenario.getNetHourly() > uberScenario.getNetHourly())
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

                <section class="bg-white rounded-3xl p-8 border border-slate-200 shadow-sm mb-16">
                    <h2 class="text-2xl font-black text-slate-900 mb-6 text-center">When to Choose Which App</h2>
                    <div class="grid md:grid-cols-2 gap-8">
                        <div>
                            <h3 class="font-bold text-slate-800 mb-4 border-b pb-2 flex items-center gap-2">Choose Uber If:</h3>
                            <ul class="space-y-3 text-sm text-slate-600">
                                <li class="flex items-start gap-2"><span class="text-emerald-500 font-black">+</span><span>You own an electric or hybrid vehicle, so longer rides hurt less on operating cost.</span></li>
                                <li class="flex items-start gap-2"><span class="text-emerald-500 font-black">+</span><span>You would rather drive passengers than manage restaurant pickups and apartment drop-offs.</span></li>
                                <li class="flex items-start gap-2"><span class="text-emerald-500 font-black">+</span><span>You plan to work airport runs, early mornings, or late-night weekend demand windows.</span></li>
                            </ul>
                        </div>
                        <div>
                            <h3 class="font-bold text-slate-800 mb-4 border-b pb-2 flex items-center gap-2">Choose DoorDash If:</h3>
                            <ul class="space-y-3 text-sm text-slate-600">
                                <li class="flex items-start gap-2"><span class="text-red-500 font-black">+</span><span>Your car is older and you want to keep highway mileage and long repositioning drives lower.</span></li>
                                <li class="flex items-start gap-2"><span class="text-red-500 font-black">+</span><span>You prefer short solo trips, less passenger interaction, and more control over when to pause.</span></li>
                                <li class="flex items-start gap-2"><span class="text-red-500 font-black">+</span><span>Your best work windows are lunch and dinner rushes, where delivery demand is easier to stack.</span></li>
                            </ul>
                        </div>
                    </div>
                </section>

                @template.components.related_pages(app = "uber")
            </div>
        </div>
    `
)
