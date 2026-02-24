@import com.gigwager.util.AppConstants
@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new com.gigwager.model.SeoMeta(
        customTitle,
        "Master gig work taxes in 2026. Learn about the self-employment tax, standard mileage deduction, and how to avoid IRS penalties as an Uber or DoorDash driver.",
        AppConstants.BASE_URL + "/taxes",
        AppConstants.BASE_URL + "/og-image.jpg"
    ),
    content = @`
        <div class="max-w-4xl mx-auto px-6 py-12 lg:py-20">
            <!-- Hero -->
            <div class="text-center mb-16">
                <span class="text-emerald-600 font-bold tracking-widest uppercase text-sm">Revenue Pillar</span>
                <h1 class="text-4xl lg:text-5xl font-black text-slate-900 mt-4 mb-6 tracking-tight">Gig Worker Taxes Don't Have to be Scary</h1>
                <p class="text-xl text-slate-600 max-w-2xl mx-auto">Keep more of what you earn. The ultimate 2026 guide to 1099 taxes, deductions, and staying out of trouble with the IRS.</p>
            </div>

            <!-- Content Grid -->
            <div class="space-y-12">
                <!-- Section 1 -->
                <section class="bg-white rounded-3xl p-8 lg:p-12 border border-slate-200 shadow-sm">
                    <h2 class="text-2xl font-bold text-slate-900 mb-4">1. The Self-Employment Tax (15.3%)</h2>
                    <div class="prose prose-slate max-w-none text-slate-600">
                        <p>Unlike regular W-2 jobs where the employer splits Medicare and Social Security taxes, as an independent contractor (1099), you pay the full amount. This is known as the Self-Employment (SE) tax.</p>
                        <ul class="list-disc pl-5 mt-4 space-y-2">
                            <li><strong>Social Security:</strong> 12.4%</li>
                            <li><strong>Medicare:</strong> 2.9%</li>
                            <li><strong>Total:</strong> 15.3% on your <em>net</em> profit (not gross earnings!)</li>
                        </ul>
                    </div>
                </section>

                <!-- Section 2 -->
                <section class="bg-white rounded-3xl p-8 lg:p-12 border border-slate-200 shadow-sm">
                    <h2 class="text-2xl font-bold text-slate-900 mb-4">2. The Magic Eraser: The Standard Mileage Deduction</h2>
                    <div class="prose prose-slate max-w-none text-slate-600">
                        <p>If you made $10,000 driving for Uber, you <strong>do not</strong> pay 15.3% on $10,000. You only pay taxes on the profit <em>after</em> deducting your business expenses.</p>
                        <p>For most drivers, the easiest and largest deduction is the <strong>IRS Standard Mileage Rate</strong>. For 2026, the IRS standard mileage rate is 72.5¢ per mile (<a href="https://www.irs.gov/tax-professionals/standard-mileage-rates" class="text-emerald-600 hover:text-emerald-700 underline" target="_blank" rel="noopener noreferrer">Official IRS Source</a>). We use this as an estimation proxy, not tax advice.</p>
                        <div class="bg-emerald-50 rounded-xl p-6 mt-6 border border-emerald-100">
                            <h4 class="font-bold text-emerald-900 mb-2">Example:</h4>
                            <p class="text-emerald-800 text-sm">You drive 10,000 miles to earn that $10,000. Your deduction is 10,000 * 0.725 = $7,250. You only pay SE tax on the remaining <strong>$2,750</strong> profit.</p>
                        </div>
                        <p class="mt-4 text-sm text-red-600 font-bold">⚠️ Warning: If you claim the standard mileage rate, you cannot also deduct gas, repairs, or depreciation. The rate covers all of that.</p>
                    </div>
                </section>

                <!-- Section 3 -->
                <section class="bg-white rounded-3xl p-8 lg:p-12 border border-slate-200 shadow-sm">
                    <h2 class="text-2xl font-bold text-slate-900 mb-4">3. Estimated Quarterly Payments</h2>
                    <div class="prose prose-slate max-w-none text-slate-600">
                        <p>Because Uber and DoorDash don't withhold taxes from your paycheck, the IRS expects you to pay as you go. If you expect to owe more than $1,000 in taxes for the year, you must make Estimated Quarterly Payments.</p>
                        <p>If you wait until April to pay your entire tax bill, you'll be hit with an underpayment penalty.</p>
                    </div>
                </section>

                <!-- Call to Action (Affiliate/Lead Gen Hook) -->
                <section class="bg-slate-900 rounded-3xl p-8 lg:p-12 text-center shadow-xl">
                    <h2 class="text-3xl font-bold text-white mb-4">Need help tracking your miles?</h2>
                    <p class="text-slate-300 mb-8 max-w-xl mx-auto">Stop guessing and risking an audit. Use a premium mileage tracker to automatically log every drive.</p>
                    <form action="https://formspree.io/f/mqkenryp" method="POST" class="flex flex-col sm:flex-row gap-2 max-w-xl" onsubmit="gtag('event','cta_click',{'event_category':'lead','event_label':'tax_tracker_app'});">
                        <input type="email" name="email" placeholder="Enter your email..." required class="flex-1 px-6 py-4 rounded-xl border-2 border-slate-200 focus:outline-none focus:border-emerald-500 text-slate-900 font-medium">
                        <button type="submit" class="bg-emerald-600 hover:bg-emerald-500 text-white font-bold px-8 py-4 rounded-xl transition duration-200 whitespace-nowrap">Get Tracker List &rarr;</button>
                    </form>
                </section>
            </div>
        </div>

        <!-- Structured Data (JSON-LD) -->
        <script type="application/ld+json">
        {
          "@context": "https://schema.org",
          "@type": "BreadcrumbList",
          "itemListElement": [{
            "@type": "ListItem",
            "position": 1,
            "name": "Home",
            "item": "${AppConstants.BASE_URL}/"
          },{
            "@type": "ListItem",
            "position": 2,
            "name": "Taxes"
          }]
        }
        </script>
    `
)
