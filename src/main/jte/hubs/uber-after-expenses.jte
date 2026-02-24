@import com.gigwager.util.AppConstants
@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new com.gigwager.model.SeoMeta(
        customTitle,
        "What is the real hourly wage for Uber drivers after deducting gas, depreciation, and taxes? Get the exact breakdown and TL;DR answer.",
        AppConstants.BASE_URL + "/uber-after-expenses",
        AppConstants.BASE_URL + "/og-image.jpg"
    ),
    content = @`
        <div class="max-w-4xl mx-auto px-6 py-12 lg:py-20">
            <!-- Hero with TL;DR Answer Box -->
            <div class="text-center mb-12">
                <span class="text-emerald-600 font-bold tracking-widest uppercase text-sm">Earnings Hub</span>
                <h1 class="text-4xl lg:text-5xl font-black text-slate-900 mt-4 mb-8 tracking-tight">Uber After Expenses: The Real Hourly Wage</h1>
                
                <div class="bg-gradient-to-br from-slate-900 to-slate-800 rounded-3xl p-8 shadow-xl text-left border border-slate-700 relative overflow-hidden">
                    <div class="absolute -right-8 -top-8 text-white/5 text-9xl font-black">TL;DR</div>
                    <div class="relative z-10">
                        <h2 class="text-emerald-400 font-bold mb-3 uppercase tracking-wider text-sm flex items-center gap-2">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path></svg>
                            The Short Answer
                        </h2>
                        <p class="text-2xl lg:text-3xl text-white font-medium leading-tight">
                            After deducting the IRS standard mileage rate and estimated self-employment taxes, the typical Uber driver's <strong>net profit is roughly 30% to 50% lower</strong> than their gross revenue.
                        </p>
                    </div>
                </div>
            </div>

            <!-- Jump to section links -->
            <div class="flex flex-wrap gap-3 justify-center mb-16">
                <a href="#how-its-calculated" class="px-5 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-full font-medium text-sm transition-colors">How it's Calculated</a>
                <a href="#faqs" class="px-5 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-full font-medium text-sm transition-colors">Top 10 FAQs</a>
                <a href="/salary/uber" class="px-5 py-2 bg-emerald-50 hover:bg-emerald-100 text-emerald-800 rounded-full font-bold text-sm transition-colors border border-emerald-200">See Top Cities Ranking</a>
            </div>

            <!-- How It's Calculated -->
            <section id="how-its-calculated" class="mb-16">
                <h2 class="text-3xl font-black text-slate-900 mb-6">How Your Take-Home Pay is Calculated</h2>
                <div class="bg-white rounded-2xl p-8 border border-slate-200 shadow-sm prose prose-slate max-w-none">
                    <p>Gross revenue is an illusion. To find what actually hits your bank account (net profit), we use a standard three-step formula based on our core assumptions:</p>
                    <ul>
                        <li><strong>1. Gross Earnings:</strong> We estimate baseline earnings based on local market tiers (traffic thickness and localized minimum wage).</li>
                        <li><strong>2. Vehicle Deductions:</strong> @template.components.irs_rate_snippet() This accounts for gas, maintenance, and vehicle depreciation. It is subtracted from gross to find taxable profit.</li>
                        <li><strong>3. Taxes:</strong> We deduct an estimated 15.3% self-employment tax (FICA) on the remaining profit.</li>
                    </ul>
                    <p class="italic text-slate-500 mt-4 text-sm">Note: This formula provides an estimated proxy, not financial or tax advice. Actual results vary by driving style and vehicle MPG.</p>
                </div>
            </section>

            <!-- Internal Linking Spider Web -->
            @template.components.related_pages(app = "uber")

            <!-- FAQs (AEO Component) -->
            <section id="faqs" class="mt-20">
                <h2 class="text-3xl font-black text-slate-900 mb-8 border-b border-slate-200 pb-4">Top 8 FAQs: Uber Earnings & Expenses</h2>
                
                <div class="space-y-6">
                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">1. Do Uber drivers actually make minimum wage?</h3>
                        <p class="text-slate-600">It depends on the market. In highly congested cities, after deducting gas and vehicle wear-and-tear, many drivers find their net hourly pay falls below their local minimum wage. However, in low-traffic, high-demand areas, it can exceed it.</p>
                    </div>
                    
                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">2. What is the biggest hidden expense for Uber drivers?</h3>
                        <p class="text-slate-600">Vehicle depreciation. Most drivers only count gas, but driving 1,000 miles a week destroys your car's resale value and accelerates major repairs. The IRS estimates this total cost at 72.5¢ per mile for 2026.</p>
                    </div>
                    
                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">3. Does Uber pay for gas?</h3>
                        <p class="text-slate-600">No. Uber drivers are independent contractors (1099), meaning you are responsible for your own gas, maintenance, and vehicle insurance. You can, however, write off business miles on your taxes.</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">4. Should I track my miles?</h3>
                        <p class="text-slate-600">Absolutely. Tracking your miles is the single most important thing you can do to lower your tax burden. Every business mile driven lowers your taxable income.</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">5. Do I have to pay taxes on Uber income?</h3>
                        <p class="text-slate-600">Yes. Regardless of whether you receive a 1099-K form or not, the IRS requires you to report all self-employment income. You are subject to both ordinary income tax and a 15.3% self-employment tax on your net profit.</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">6. Can I deduct a car wash?</h3>
                        <p class="text-slate-600">If you take the Standard Mileage Deduction (the 72.5¢ per mile rate), you cannot separately deduct gas, car washes, or repairs. The standard rate is designed to cover all of those operating costs.</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">7. Does my personal insurance cover Uber accidents?</h3>
                        <p class="text-slate-600">Typically no. If you have the app on and get into an accident, your standard personal auto policy may deny the claim. You usually need a "rideshare endorsement" to protect yourself during Period 1 (app on, waiting for request).</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">8. Is doing Uber full-time worth it?</h3>
                        <p class="text-slate-600">For most people, Uber works best as a side-hustle. Doing it 40+ hours a week puts extreme stress on your vehicle and exposes you to higher quarterly tax liabilities without providing health insurance or paid time off.</p>
                    </div>
                </div>
            </section>
        </div>
    `
)
