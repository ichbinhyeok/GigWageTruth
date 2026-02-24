@import com.gigwager.util.AppConstants
@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new com.gigwager.model.SeoMeta(
        customTitle,
        "What is the real net profit for DoorDash drivers after deducting gas, depreciation, and self-employment taxes? Get the exact breakdown and TL;DR answer.",
        AppConstants.BASE_URL + "/doordash-after-expenses",
        AppConstants.BASE_URL + "/og-image.jpg"
    ),
    content = @`
        <div class="max-w-4xl mx-auto px-6 py-12 lg:py-20">
            <!-- Hero with TL;DR Answer Box -->
            <div class="text-center mb-12">
                <span class="text-red-600 font-bold tracking-widest uppercase text-sm">Earnings Hub</span>
                <h1 class="text-4xl lg:text-5xl font-black text-slate-900 mt-4 mb-8 tracking-tight">DoorDash After Expenses: Real Net Profit</h1>
                
                <div class="bg-gradient-to-br from-slate-900 to-slate-800 rounded-3xl p-8 shadow-xl text-left border border-slate-700 relative overflow-hidden">
                    <div class="absolute -right-8 -top-8 text-white/5 text-9xl font-black">TL;DR</div>
                    <div class="relative z-10">
                        <h2 class="text-red-400 font-bold mb-3 uppercase tracking-wider text-sm flex items-center gap-2">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path></svg>
                            The Short Answer
                        </h2>
                        <p class="text-2xl lg:text-3xl text-white font-medium leading-tight">
                            After deducting the IRS standard mileage rate and estimated self-employment taxes, a typical Dasher's <strong>net profit is roughly 35% to 55% lower</strong> than their gross revenue.
                        </p>
                    </div>
                </div>
            </div>

            <!-- Jump to section links -->
            <div class="flex flex-wrap gap-3 justify-center mb-16">
                <a href="#how-its-calculated" class="px-5 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-full font-medium text-sm transition-colors">How it's Calculated</a>
                <a href="#faqs" class="px-5 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-full font-medium text-sm transition-colors">Top 10 FAQs</a>
                <a href="/salary/doordash" class="px-5 py-2 bg-red-50 hover:bg-red-100 text-red-800 rounded-full font-bold text-sm transition-colors border border-red-200">See Top Dash Cities Ranking</a>
            </div>

            <!-- How It's Calculated -->
            <section id="how-its-calculated" class="mb-16">
                <h2 class="text-3xl font-black text-slate-900 mb-6">How Your Dash Take-Home Pay is Calculated</h2>
                <div class="bg-white rounded-2xl p-8 border border-slate-200 shadow-sm prose prose-slate max-w-none">
                    <p>Gross revenue from a Dash shift can be misleading. To find your actual take-home pay, we use a three-step formula based on realistic assumptions:</p>
                    <ul>
                        <li><strong>1. Gross Dash Earnings:</strong> Base pay plus customer tips, estimated by local market tiers (density, average order value).</li>
                        <li><strong>2. Vehicle Deductions:</strong> @template.components.irs_rate_snippet() Delivering food is highly brutal on a car with stop-and-go driving. We subtract the IRS rate from gross revenue to find taxable profit.</li>
                        <li><strong>3. SE Taxes:</strong> We deduct an estimated 15.3% self-employment tax (FICA) on the remaining profit.</li>
                    </ul>
                    <p class="italic text-slate-500 mt-4 text-sm">Note: This is an estimated proxy and not personalized tax advice. Driving a 15 MPG SUV vs. a 50 MPG Prius drastically alters reality.</p>
                </div>
            </section>

            <!-- Internal Linking Spider Web -->
            @template.components.related_pages(app = "doordash", appName = "DoorDash")

            <!-- FAQs (AEO Component) -->
            <section id="faqs" class="mt-20">
                <h2 class="text-3xl font-black text-slate-900 mb-8 border-b border-slate-200 pb-4">Top 8 FAQs: DoorDash Earnings & Expenses</h2>
                
                <div class="space-y-6">
                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">1. Why does DoorDash feel like it pays more than Uber?</h3>
                        <p class="text-slate-600">DoorDash often involves shorter trips (less mileage) compared to Uber rides. However, it requires more parking, walking, waiting at restaurants, and stop-and-go driving which accelerates wear on brakes and starters.</p>
                    </div>
                    
                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">2. What is the $2 per mile rule?</h3>
                        <p class="text-slate-600">Many experienced Dashers refuse orders that pay less than $2 per mile driven. If you accept a $5 order that takes 5 miles, accounting for the return trip and depreciation, you are essentially delivering the food for free.</p>
                    </div>
                    
                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">3. Does DoorDash reimburse for hot bags or insulated gear?</h3>
                        <p class="text-slate-600">No, you purchase your own gear. However, supplies bought specifically for delivering (drink carriers, large insulated bags, a dashcam) can be deducted on your Schedule C at tax time.</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">4. How bad is the tax hit for Dashers?</h3>
                        <p class="text-slate-600">You are an independent contractor, meaning you owe both income tax and a 15.3% self-employment tax on your net profit. If you do not track your business mileage to lower your profit, your tax liability will be devastating.</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">5. Do I need commercial insurance to deliver food?</h3>
                        <p class="text-slate-600">It depends on your personal auto policy. Some insurers may deny a claim if they discover you were using the vehicle for commercial delivery. Always check if you need a specific rideshare/delivery endorsement.</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">6. Are cash tips taxable?</h3>
                        <p class="text-slate-600">Yes. The IRS requires you to report all tips, including cash tips, as taxable income.</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">7. Should I track return trip miles?</h3>
                        <p class="text-slate-600">If you make a delivery and immediately drive back to a "hot spot" area in search of the next order, those return miles are generally considered deductible business miles. Always log your shifts.</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">8. Can delivering food ruin my car?</h3>
                        <p class="text-slate-600">Yes. Doing 50 short trips a week in a city puts massive strain on your transmission, battery, and brakes compared to standard highway commuting. The IRS 72.5¢ per mile rate exists precisely because cars degrade rapidly when used for commercial service.</p>
                    </div>
                </div>
            </section>
        </div>
    `
)
