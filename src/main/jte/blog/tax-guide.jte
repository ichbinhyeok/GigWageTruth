@import com.gigwager.model.SeoMeta

@param String customTitle

@template.layout.main(
    seoMeta = new SeoMeta(
        customTitle != null ? customTitle : "Gig Worker Tax Guide 2026",
        "Stop overpaying the IRS. Learn the difference between Standard Mileage Rate and Actual Expenses, and how to track deductions correctly.",
        "https://gigwagetruth.com/blog/tax-guide",
        "https://gigwagetruth.com/og-image-tax.jpg"
    ),
    content = @`
        <div class="bg-white min-h-screen pb-24">
            
            <!-- Article Header -->
            <header class="bg-slate-900 py-20 text-center px-6">
                <div class="max-w-3xl mx-auto">
                    <div class="inline-block bg-emerald-500/20 text-emerald-300 text-xs font-bold px-3 py-1 rounded-full mb-4 border border-emerald-500/30">
                        TAX SAVINGS
                    </div>
                    <h1 class="text-3xl md:text-5xl font-bold text-white mb-6 leading-tight">
                        Gig Worker Tax 101: Don't Overpay the IRS in 2026
                    </h1>
                    <div class="flex justify-center items-center gap-4 text-slate-400 text-sm">
                        <span>By Gabi</span>
                        <span>•</span>
                        <span>Jan 13, 2026</span>
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
                            If you earned more than $400 from Uber, DoorDash, or Lyft, you owe taxes. But here is the secret: <strong>You only pay taxes on PROFIT, not REVENUE.</strong> The game is to lower your profit (legally) by tracking every possible deduction.
                        </p>

                        <h2>Standard Mileage Rate vs. Actual Expenses</h2>
                        <p>
                            This is the most critical decision you will make. You must choose ONE method for the entire year.
                        </p>

                        <h3>Method 1: Standard Mileage Rate (Recommended)</h3>
                        <p>
                            For 2026, the IRS lets you deduct roughly <strong>$0.725 per mile</strong> (subject to final IRS announcement) for every business mile driven.
                        </p>
                        <ul>
                            <li><strong>Pros:</strong> Simple. Covers gas, insurance, repairs, depreciation, and registration.</li>
                            <li><strong>Cons:</strong> You cannot deduct actual gas receipts or repair bills.</li>
                            <li><strong>Best for:</strong> 90% of drivers, especially those with older, fuel-efficient cars (Prius, Corolla).</li>
                        </ul>

                        <h3>Method 2: Actual Expenses</h3>
                        <p>
                            You list every single receipt: gas, oil changes, tires, insurance, lease payments.
                        </p>
                        <ul>
                            <li><strong>Pros:</strong> Can be better if you drive a massive SUV or have expensive repairs.</li>
                            <li><strong>Cons:</strong> A bookkeeping nightmare. Audit magnet if not perfect.</li>
                        </ul>

                        <div class="bg-red-50 border-l-4 border-red-500 p-6 my-8 not-prose rounded-r-lg">
                            <h4 class="text-red-900 font-bold text-lg mb-2">🚫 Common Mistake</h4>
                            <p class="text-red-800">
                                You CANNOT deduct "miles" AND "gas". It is one or the other. Trying to do both is tax fraud.
                            </p>
                        </div>

                        <h2>Hidden Deductions You Might Miss</h2>
                        <p>Even if you take the Standard Mileage Rate, you can STILL deduct these items separately:</p>
                        <ul class="list-disc pl-5 space-y-2">
                            <li><strong>Phone mount & chargers</strong></li>
                            <li><strong>Percentage of your phone bill</strong> (based on business use)</li>
                            <li><strong>Parking fees & Tolls</strong> (NOT traffic tickets!)</li>
                            <li><strong>Hot bags & blankets</strong></li>
                            <li><strong>Roadside assistance memberships</strong> (AAA)</li>
                        </ul>

                        <h2>Quarterly Taxes: The Silent Killer</h2>
                        <p>
                            Unlike a W-2 job, no one withholds taxes for you. The US tax system is "pay as you go."
                        </p>
                        <p>
                            If you expect to owe more than $1,000 in taxes, you generally need to make estimated payments quarterly (April 15, June 15, Sept 15, Jan 15). Failing to do so can result in an "underpayment penalty."
                        </p>

                        <h2>Conclusion</h2>
                        <p>
                            Track every mile. Use a tracking app (like Stride or Gridwise), or keep a physical logbook. Your mileage log is your shield against an audit and your ticket to paying thousands less in taxes.
                        </p>
                    </div> 

                    <!-- CTA -->
                    <div class="mt-16 pt-8 border-t border-slate-200">
                        <div class="bg-emerald-50 rounded-xl p-8 text-center border border-emerald-100">
                            <h3 class="text-2xl font-bold text-emerald-900 mb-4">Check Your Net Profit Now</h3>
                            <p class="text-emerald-800 mb-6">Our calculator applies the Standard Mileage Rate automatically.</p>
                            <a href="/uber" class="inline-block px-8 py-4 bg-emerald-600 text-white font-bold rounded-lg hover:bg-emerald-700 transition shadow-lg shadow-emerald-500/30">
                                Open Calculator
                            </a>
                        </div>
                    </div>

                </div>
            </article>

        </div>
    `
)
