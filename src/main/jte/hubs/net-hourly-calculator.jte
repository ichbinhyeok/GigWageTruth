@import com.gigwager.util.AppConstants
@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new com.gigwager.model.SeoMeta(
        customTitle,
        "Calculate take-home per hour across gig apps after vehicle costs and a tax reserve. Use total online time, not active time.",
        AppConstants.BASE_URL + "/net-hourly-calculator",
        AppConstants.BASE_URL + "/og-image.jpg"
    ),
    content = @`
        <div class="max-w-4xl mx-auto px-6 py-12 lg:py-20">
            <!-- Hero with TL;DR Answer Box -->
            <div class="text-center mb-12">
                <span class="text-indigo-600 font-bold tracking-widest uppercase text-sm">Tool Hub</span>
                <h1 class="text-4xl lg:text-5xl font-black text-slate-900 mt-4 mb-4 tracking-tight">Gig Worker Net Hourly Calculator</h1>
                <p class="text-xl text-slate-600 max-w-2xl mx-auto mb-8">Stop confusing Gross Revenue with Take-Home Pay. Find out exactly what your time and car are worth.</p>
                
                <div class="bg-gradient-to-br from-indigo-900 to-indigo-800 rounded-3xl p-8 shadow-xl text-left border border-indigo-700 relative overflow-hidden">
                    <div class="absolute -right-12 -top-12 text-white/5 text-9xl font-black">MATH</div>
                    <div class="relative z-10 text-center">
                        <h2 class="text-indigo-300 font-bold mb-3 uppercase tracking-wider text-sm">
                            The Golden Breakdown
                        </h2>
                        
                        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 text-white mb-6">
                            <div class="p-4 bg-white/10 rounded-xl">
                                <span class="block text-3xl font-black mb-1">100%</span>
                                <span class="text-sm font-medium opacity-80">Gross Payout</span>
                            </div>
                            <div class="p-4 bg-red-500/20 rounded-xl border border-red-500/30">
                                <span class="block text-3xl font-black mb-1">- 30%</span>
                                <span class="text-sm font-medium text-red-200">Car Expenses</span>
                            </div>
                            <div class="p-4 bg-red-500/20 rounded-xl border border-red-500/30">
                                <span class="block text-3xl font-black mb-1">15.3% rate</span>
                                <span class="text-sm font-medium text-red-200">on 92.35% of modeled profit</span>
                            </div>
                        </div>
                        <p class="text-xl lg:text-2xl text-white font-medium">
                            If you gross $25/hr on the app, your true net hourly take-home is typically closer to <span class="text-green-400 font-black decoration-4 underline underline-offset-4">$13 to $16/hr</span>.
                        </p>
                    </div>
                </div>
            </div>
            
            <div class="text-center mb-16">
                <!-- Directing users to use the specific calculators on home pages -->
                <a href="/" class="inline-block px-8 py-4 bg-slate-900 text-white font-bold rounded-xl hover:bg-slate-800 transition-colors shadow-lg shadow-slate-900/20 mr-4">Uber Calculator</a>
                <a href="/doordash" class="inline-block px-8 py-4 bg-slate-900 text-white font-bold rounded-xl hover:bg-slate-800 transition-colors shadow-lg shadow-slate-900/20">DoorDash Calculator</a>
            </div>

            <div class="grid sm:grid-cols-3 gap-4 mb-16">
                <a href="/taxes/quarterly-estimator" class="rounded-2xl border border-slate-200 bg-white p-5 hover:border-indigo-200 hover:bg-indigo-50 transition-colors">
                    <h3 class="font-bold text-slate-900 mb-2">Quarterly Tax Estimator</h3>
                    <p class="text-sm text-slate-600">Check how much of your weekly profit needs to be set aside for taxes.</p>
                </a>
                <a href="/vehicle-cost/cost-per-mile" class="rounded-2xl border border-slate-200 bg-white p-5 hover:border-indigo-200 hover:bg-indigo-50 transition-colors">
                    <h3 class="font-bold text-slate-900 mb-2">Cost Per Mile Tool</h3>
                    <p class="text-sm text-slate-600">Test whether your vehicle is strong enough for gig work at your mileage level.</p>
                </a>
                <a href="/multi-apping" class="rounded-2xl border border-slate-200 bg-white p-5 hover:border-indigo-200 hover:bg-indigo-50 transition-colors">
                    <h3 class="font-bold text-slate-900 mb-2">Multi-App Strategy</h3>
                    <p class="text-sm text-slate-600">Learn how drivers turn weak idle time into stronger hourly pay.</p>
                </a>
            </div>

            <!-- Internal Linking Spider Web -->
            @template.components.related_pages(app = "uber")

            <!-- FAQs (AEO Component) -->
            <section id="faqs" class="mt-20">
                <h2 class="text-3xl font-black text-slate-900 mb-8 border-b border-slate-200 pb-4">Top 5 FAQs: Gig Work Economics</h2>
                
                <div class="space-y-6">
                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">1. Why is "Net Hourly" lower than local minimum wage?</h3>
                        <p class="text-slate-600">Gig drivers are independent contractors, meaning the app company (Uber, DoorDash, Lyft) is not required to guarantee local minimum wage. After funding your own gas, car maintenance, and self-employment taxes, your actual net margin can drop significantly depending on city traffic.</p>
                    </div>
                    
                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">2. How is Net Profit calculated mathematically?</h3>
                        <p class="text-slate-600">Modeled take-home = gross earnings minus miles driven × the current $0.76 IRS mileage proxy, then an estimated SE-tax calculation on 92.35% of the remaining profit. Divide by total online/work hours, not only active app hours.</p>
                    </div>
                    
                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">3. Does taking $3 orders ever make mathematical sense?</h3>
                        <p class="text-slate-600">Rarely. In a $3 order, you spend time picking up and dropping off. Even if it takes only 15 minutes and 2 miles round trip, the depreciation and gas (2 miles x @template.components.irs_rate_snippet()) wipes out half the earnings before factoring in taxes or your time.</p>
                    </div>
                    
                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">4. Can I write off my car payment instead of mileage?</h3>
                        <p class="text-slate-600">Taxpayers generally choose between the standard mileage method and eligible actual vehicle expenses for a vehicle and tax year. The better method depends on your eligibility and records; compare both with a qualified tax professional.</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">5. Do I count time spent waiting for orders/rides?</h3>
                        <p class="text-slate-600">Yes! The apps only show "Active Hours" (when you have a passenger/food), which inflates your hourly wage. If you left home at 5 PM and returned at 10 PM, your divisor is 5 hours, regardless of whether you spent 2 hours parked in a lot waiting for a ping.</p>
                    </div>
                </div>
            </section>
        </div>
    `
)
