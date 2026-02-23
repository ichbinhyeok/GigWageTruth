@import com.gigwager.model.SeoMeta
@param String customTitle

@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        customTitle,
        "How we calculate your real gig wages. Methodology and formula explanation.",
        "https://gigverdict.com/methodology",
        "https://gigverdict.com/og-image.jpg"
    ),
    content = @`
        <div class="max-w-4xl mx-auto px-6 py-12">
            <h1 class="text-4xl font-bold text-slate-900 mb-8">Methodology: The Math Behind the Truth</h1>
            
            <div class="space-y-8">
                <!-- 1. Gross Revenue -->
                <section class="bg-white p-8 rounded-2xl border border-slate-200 shadow-sm">
                    <h2 class="text-2xl font-bold text-slate-900 mb-4 flex items-center">
                        <span class="bg-slate-100 text-slate-600 w-8 h-8 rounded-full flex items-center justify-center text-sm mr-3">1</span>
                        Gross Revenue
                    </h2>
                    <p class="text-slate-600 mb-4">
                        This is the "big number" the apps show you. It includes your trip fares, tips, and bonuses.
                        Most drivers stop here and think this is their income. <strong>It isn't.</strong>
                    </p>
                </section>

                <!-- 2. Vehicle Expenses -->
                <section class="bg-white p-8 rounded-2xl border border-slate-200 shadow-sm">
                    <h2 class="text-2xl font-bold text-red-600 mb-4 flex items-center">
                        <span class="bg-red-50 text-red-600 w-8 h-8 rounded-full flex items-center justify-center text-sm mr-3">2</span>
                        Vehicle Expenses (The Silent Killer)
                    </h2>
                    <p class="text-slate-600 mb-4">
                        Your car is not free. Every mile you drive costs money in gas, tires, oil changes, and most importantly, <strong>depreciation</strong>.
                    </p>
                    <div class="grid md:grid-cols-2 gap-6 mt-6">
                        <div class="bg-slate-50 p-4 rounded-xl">
                            <h3 class="font-semibold text-slate-900 mb-2">IRS Standard Method</h3>
                            <p class="text-sm text-slate-600">
                                We use the 2026 IRS Standard Mileage Rate of <strong>$0.725 per mile</strong>. 
                                This is the government's official estimate of what it costs to operate a vehicle for business.
                            </p>
                        </div>
                        <div class="bg-slate-50 p-4 rounded-xl">
                            <h3 class="font-semibold text-slate-900 mb-2">Actual Expense Method</h3>
                            <p class="text-sm text-slate-600">
                                If you select "My Vehicle", we calculate precise costs:
                                <br>• Gas: (Miles / MPG) × Gas Price
                                <br>• Depreciation: ~$0.15/mile (varies by car)
                                <br>• Maintenance: ~$0.12/mile
                            </p>
                        </div>
                    </div>
                </section>

                <!-- 3. Taxes -->
                <section class="bg-white p-8 rounded-2xl border border-slate-200 shadow-sm">
                    <h2 class="text-2xl font-bold text-slate-900 mb-4 flex items-center">
                        <span class="bg-slate-100 text-slate-600 w-8 h-8 rounded-full flex items-center justify-center text-sm mr-3">3</span>
                        Self-Employment Tax
                    </h2>
                    <p class="text-slate-600 mb-4">
                        Unlike W-2 employees, gig workers must pay the full FICA tax (Social Security + Medicare).
                        <br><strong>Rate: 15.3%</strong> of your net profit.
                    </p>
                    <div class="bg-amber-50 border border-amber-200 p-4 rounded-xl text-amber-800 text-sm">
                        <strong>Note:</strong> We apply this tax only to your <em>profit</em> (Revenue - Deductions), which is how the IRS calculates it. 
                        This is why tracking miles is crucial—it lowers your taxable income!
                    </div>
                </section>
                
                <!-- Formula -->
                <section class="mt-12 text-center">
                    <h2 class="text-xl font-bold text-slate-900 mb-6">The Golden Formula</h2>
                    <div class="inline-block bg-slate-900 text-white p-8 rounded-2xl font-mono text-lg md:text-xl shadow-xl">
                        Net Profit = Gross - (Miles × Cost/Mile) - Taxes
                    </div>
                </section>
            </div>
        </div>
    `
)
