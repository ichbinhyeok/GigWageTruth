@import com.gigwager.model.SeoMeta
@import com.gigwager.util.AppConstants
@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        customTitle != null ? customTitle : "Gig Worker Tax Guide 2026: Don't Overpay the IRS",
        "The 2026 IRS mileage rate is 72.5 cents. Learn how to estimate deductions for your car, phone, and hot bags to lower your tax burden.",
        AppConstants.BASE_URL + "/blog/tax-guide",
        AppConstants.BASE_URL + "/og-image-tax.jpg"
    ),
    content = @`
        <div class="bg-white min-h-screen pb-24">
            
            <!-- Breadcrumbs -->
            <div class="max-w-3xl mx-auto px-6 pt-8">
                <a href="/blog" class="text-sm font-bold text-slate-400 hover:text-emerald-600 transition flex items-center gap-2">
                    &larr; Back to Blog
                </a>
            </div>
            
            <!-- Article Header -->
            <header class="bg-slate-900 py-20 text-center px-6">
                <div class="max-w-3xl mx-auto">
                    <div class="inline-block bg-emerald-500/20 text-emerald-300 text-xs font-bold px-3 py-1 rounded-full mb-4 border border-emerald-500/30">
                        TAXES & FINANCE
                    </div>
                    <h1 class="text-3xl md:text-5xl font-bold text-white mb-6 leading-tight">
                        The IRS Is Your Business Partner:<br>A Gig Worker's Guide to 2026 Taxes
                    </h1>
                    <div class="flex justify-center items-center gap-4 text-slate-400 text-sm">
                        <span>By Gabi</span>
                        <span>•</span>
                        <span>Jan 15, 2026</span>
                        <span>•</span>
                        <span>8 min read</span>
                    </div>
                </div>
            </header>

            <!-- Article Content -->
            <article class="max-w-3xl mx-auto px-6 -mt-10 relative z-10">
                <div class="bg-white rounded-2xl shadow-xl p-8 md:p-12 border border-slate-100">
                    
                    <div class="prose prose-slate prose-lg max-w-none">
                        <p class="lead text-xl text-slate-600 font-medium mb-8">
                            Most new drivers freak out about taxes. They save 30% of their income in a panic. <span class="text-emerald-600 font-bold">Stop doing that.</span> If you do this correctly, you might pay nearly zero tax provided you track one specific number.
                        </p>

                        <div class="bg-emerald-50 p-6 rounded-xl border-l-4 border-emerald-500 mb-8 not-prose">
                            <h3 class="font-bold text-emerald-900 mb-2">The Golden Number for 2026</h3>
                            <p class="text-emerald-800">
                                The IRS Standard Mileage Rate for 2026 is <strong>72.5 cents per mile</strong>. Memorize this number. It is your shield.
                            </p>
                        </div>

                        <h2>🪄 The "Standard Mileage" Magic Trick</h2>
                        <p>
                            You have two ways to deduct car expenses:
                        </p>
                        <ol>
                            <li><strong>actual Expenses:</strong> Saving every receipt for gas, oil, tires, insurance, and depreciation. (Painful and usually wrong).</li>
                            <li><strong>Standard Mileage Deduction:</strong> Deducting 72.5 cents for every business mile drove. (Easy and usually saves you more money).</li>
                        </ol>
                        <p>
                            Why is the standard deduction better? Because it includes depreciation. Unless you are driving a brand new Hummer, your actual cost to drive is likely closer to 30-40 cents a mile. The IRS lets you deduct 72.5 cents. <strong>That gap is tax-free profit.</strong>
                        </p>

                        <h2>📍 What Counts as a "Business Mile"?</h2>
                        <p>
                            This is where drivers get audit-slapped. Here is the definitive list:
                        </p>
                        <ul class="list-disc pl-5 space-y-2">
                            <li><strong>✅ Driving to pickup a passenger/food:</strong> This is a business mile.</li>
                            <li><strong>✅ Driving with passenger/food:</strong> This is a business mile.</li>
                            <li><strong>✅ Driving back to a "hot spot" waiting for an order:</strong> This is a business mile.</li>
                            <li><strong>❌ Driving from home to your first spot:</strong> The IRS calls this "commuting." It is largely not deductible unless your home is your "principal place of business" (hard to prove for Uber).</li>
                            <li><strong>❌ Driving home after your "shift" ends:</strong> Commuting.</li>
                        </ul>

                        <h2>🎁 Wait, There's More: "Common Law" Deductions</h2>
                        <p>
                            The mileage rate covers your car. It does <em>not</em> cover your other business tools. You can deduct these <strong>in addition</strong> to your mileage:
                        </p>
                        <ul>
                            <li><strong>📱 Phone Bill:</strong> If you use your phone 50% for work, deduct 50% of the bill.</li>
                            <li><strong>🥡 Hot Bags & Blankets:</strong> 100% deductible as "supplies."</li>
                            <li><strong>🔌 Phone Mounts & Chargers:</strong> 100% deductible.</li>
                            <li><strong>🎵 Music Subscriptions:</strong> If you pay for Spotify solely to entertain passengers (Uber/Lyft), it's a deduction.</li>
                            <li><strong>🧴 Car Wash:</strong> Some CPAs argue this is built into the mileage rate (maintenance), others say it's "cleaning supplies." Be careful here. Detailed professional detailing after a "vomit incident" is definitely deductible.</li>
                        </ul>

                        <h2>⚠️ The "Tracking App" Trap</h2>
                        <p>
                            "I'll just use Google Maps timeline." <strong>Wrong.</strong> The IRS requires a <em>contemporaneous</em> log. That means a log created at the time of the drive.
                        </p>
                        <p>
                            If you get audited, a printout of Google Timeline might not hold up because it doesn't distinguish between "going to the grocery store" and "going to pick up a burrito for Dave."
                        </p>
                        <p>
                            <strong>Action Step:</strong> Use a dedicated app like Stride, Everlance, or Gridwise. Or, buy a $5 paper logbook and write your odometer reading at the start and end of every shift. Paper logs are surprisingly audit-proof because they are hard to fake digitally.
                        </p>

                        <h2>Summary: Should You Worry?</h2>
                        <p>
                            If you track your miles, your taxable income will be significantly lower than your gross income.
                        </p>
                        <ul class="mysimplesummary list-none pl-0 border-t border-slate-200 pt-4 mt-6">
                            <li class="flex justify-between py-2">
                                <span>Gross Income:</span>
                                <span class="font-bold">$50,000</span>
                            </li>
                            <li class="flex justify-between py-2 text-red-500">
                                <span>Mileage Deduction (40k miles * .725):</span>
                                <span class="font-bold">-$29,000</span>
                            </li>
                            <li class="flex justify-between py-2 text-emerald-600 border-t border-slate-200">
                                <span>Taxable Amount:</span>
                                <span class="font-bold big-number">$21,000</span>
                            </li>
                        </ul>
                        <p class="text-sm text-slate-500 mt-2">
                            *This is hypothetical. We are not tax pros. Consult a CPA. But seriously, track your miles.
                        </p>
                    </div> 

                    <!-- CTA -->
                    <div class="mt-16 pt-8 border-t border-slate-200">
                        <div class="bg-indigo-900 rounded-xl p-8 text-center text-white">
                            <h3 class="text-2xl font-bold mb-4">Calculate Your True Net Wage</h3>
                            <p class="text-indigo-200 mb-6">See how taxes and depreciation hit your specific city.</p>
                            <a href="/salary/directory" class="inline-block px-8 py-4 bg-white text-indigo-900 font-bold rounded-lg hover:bg-indigo-50 transition transform hover:-translate-y-1 shadow-lg">
                                Find My City
                            </a>
                        </div>
                    </div>

                </div>
            </article>

        </div>
    `
)
