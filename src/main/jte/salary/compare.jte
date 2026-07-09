@import com.gigwager.model.SeoMeta
@import com.gigwager.model.CityData
@import com.gigwager.model.CityLocalData
@import com.gigwager.model.CityScenario
@import com.gigwager.model.DriverShiftReport
@import com.gigwager.util.AppConstants
@import java.util.List

@param CityData city
@param CityScenario uberScenario
@param CityScenario doordashScenario
@param String winningAppName
@param String losingAppName
@param double winningNetHourly
@param double netHourlyGap
@param boolean nearlyTied
@param String lastUpdated
@param CityLocalData localData
@param List<DriverShiftReport> driverShiftReports = java.util.List.of()
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
                        Uber Eats vs DoorDash Pay in ${city.getCityName()} After Expenses
                    </h1>
                    <p class="text-xl text-slate-600 max-w-2xl mx-auto">
                        @if (nearlyTied)
                            Delivery-style side-hustle estimates in ${city.getCityName()} are effectively tied, with both app paths landing near $${String.format("%.2f", winningNetHourly)}/hr net after IRS mileage proxy and self-employment tax assumptions.
                        @else
                            Current delivery-style estimates show <strong>${winningAppName}</strong> ahead by <strong>$${String.format("%.2f", netHourlyGap)}/hr net</strong> in ${city.getCityName()} after IRS mileage proxy and self-employment tax assumptions.
                        @endif
                    </p>
                </div>

                <div class="bg-white border border-slate-200 rounded-2xl p-5 mb-8">
                    <p class="text-sm text-slate-600">
                        Use this page when your question is <strong>Uber Eats vs DoorDash pay in ${city.getCityName()}</strong>.
                        The Uber column is the closest GigVerdict model for Uber-platform earning power; the delivery decision still depends on restaurant density, tips, active time, and mileage.
                    </p>
                </div>

                <div class="grid sm:grid-cols-2 lg:grid-cols-4 gap-3 mb-10">
                    <a href="/uber?gross=${uberScenario.getGrossWeekly()}&miles=${uberScenario.getMiles()}&hours=${uberScenario.getHours()}&gasPrice=${city.getGasPrice()}" class="rounded-2xl bg-slate-900 hover:bg-slate-800 text-white font-bold px-5 py-4 transition-colors">
                        Run Uber calculator
                    </a>
                    <a href="/profit-setup-kit?source=compare_page&app=${winningAppName.toLowerCase()}&gross=${winningAppName.equals("Uber") ? uberScenario.getGrossWeekly() : doordashScenario.getGrossWeekly()}&miles=${winningAppName.equals("Uber") ? uberScenario.getMiles() : doordashScenario.getMiles()}&hours=${winningAppName.equals("Uber") ? uberScenario.getHours() : doordashScenario.getHours()}" class="rounded-2xl bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-bold px-5 py-4 transition-colors">
                        Build weekly profit plan
                    </a>
                    <a href="/salary/uber/${city.getSlug()}" class="rounded-2xl bg-white border border-slate-200 hover:border-slate-300 text-slate-900 font-bold px-5 py-4 transition-colors">
                        Uber city earnings
                    </a>
                    <a href="/salary/doordash/${city.getSlug()}" class="rounded-2xl bg-white border border-slate-200 hover:border-slate-300 text-slate-900 font-bold px-5 py-4 transition-colors">
                        DoorDash city earnings
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

                <section class="bg-white rounded-3xl p-8 border border-slate-200 shadow-sm mb-12">
                    <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Delivery-only decision grid</p>
                    <h2 class="text-2xl font-black text-slate-900 mb-5">When Uber Eats or DoorDash is more likely to win in ${city.getCityName()}</h2>
                    <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-4">
                        <article class="rounded-2xl bg-slate-50 border border-slate-200 p-5">
                            <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-2">Lunch</p>
                            <h3 class="font-bold text-slate-900 mb-2">DoorDash volume edge</h3>
                            <p class="text-sm text-slate-600">DoorDash is more likely to win when office, retail, and quick-service clusters around ${localData.shoppingDistrict()} keep pickup waits low.</p>
                        </article>
                        <article class="rounded-2xl bg-slate-50 border border-slate-200 p-5">
                            <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-2">Dinner</p>
                            <h3 class="font-bold text-slate-900 mb-2">Compare tips and route length</h3>
                            <p class="text-sm text-slate-600">Dinner is the strongest test window because both apps can show enough demand for a fair gross-per-mile comparison.</p>
                        </article>
                        <article class="rounded-2xl bg-slate-50 border border-slate-200 p-5">
                            <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-2">Late night</p>
                            <h3 class="font-bold text-slate-900 mb-2">Zone density decides it</h3>
                            <p class="text-sm text-slate-600">${localData.nightlifeDistrict()} can work only if pickups stay close and parking does not turn the shift into unpaid waiting.</p>
                        </article>
                        <article class="rounded-2xl bg-slate-50 border border-slate-200 p-5">
                            <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-2">Suburbs</p>
                            <h3 class="font-bold text-slate-900 mb-2">Dead miles are the risk</h3>
                            <p class="text-sm text-slate-600">If the route pushes you back onto ${localData.majorHighway()} unpaid, the higher gross app can still lose on net hourly.</p>
                        </article>
                    </div>
                    <div class="mt-6 rounded-2xl bg-emerald-50 border border-emerald-100 p-5">
                        <p class="text-sm text-emerald-900">
                            Best practice: test both apps over the same dinner window, log gross pay, active time, online time, and miles, then compare net hourly instead of trusting app screenshots.
                            <a href="/reports/doordash-driver-shift-evidence-2026" class="font-bold underline">Open the DoorDash shift evidence table</a>.
                        </p>
                    </div>
                </section>

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

                        <a href="/salary/uber/${city.getSlug()}" class="block w-full text-center py-4 bg-slate-900 hover:bg-slate-800 text-white font-bold rounded-xl transition-all">Open Uber Calculator</a>
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

                        <a href="/salary/doordash/${city.getSlug()}" class="block w-full text-center py-4 bg-slate-900 hover:bg-slate-800 text-white font-bold rounded-xl transition-all">Open DoorDash Calculator</a>
                    </div>
                </div>

                <div class="text-center mb-16">
                    <div class="flex flex-col items-center gap-3">
                        <a href="/salary/directory" class="text-slate-500 hover:text-slate-800 font-bold underline transition-colors">See all cities</a>
                        <div class="flex flex-col sm:flex-row gap-3 text-sm">
                            <a href="/uber/where-you-can-drive" class="text-sky-700 hover:text-sky-900 font-bold underline transition-colors">Need Uber coverage first?</a>
                            <a href="/doordash/where-you-can-dash" class="text-amber-700 hover:text-amber-900 font-bold underline transition-colors">Need DoorDash availability first?</a>
                        </div>
                    </div>
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
