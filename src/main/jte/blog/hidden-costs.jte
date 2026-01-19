@import com.gigwager.model.SeoMeta
@import com.gigwager.util.AppConstants

@param String customTitle

@template.layout.main(
    seoMeta = new SeoMeta(
        customTitle != null ? customTitle : "The Hidden Cost of Depreciation: Why Your $100 Day is Actually $60",
        "Depreciation is the silent killer of gig worker profits. Learn how to calculate it and why the IRS mileage rate exists.",
        AppConstants.BASE_URL + "/blog/hidden-costs",
        AppConstants.BASE_URL + "/og-image-depreciation.jpg"
    ),
    content = @`
        <div class="bg-white min-h-screen pb-24">
            
            <!-- Article Header -->
            <header class="bg-slate-900 py-20 text-center px-6">
                <div class="max-w-3xl mx-auto">
                    <div class="inline-block bg-emerald-500/20 text-emerald-300 text-xs font-bold px-3 py-1 rounded-full mb-4 border border-emerald-500/30">
                        FINANCE
                    </div>
                    <h1 class="text-3xl md:text-5xl font-bold text-white mb-6 leading-tight">
                        The Silent Killer: Why Your "$100 Day" is Actually Only $60
                    </h1>
                    <div class="flex justify-center items-center gap-4 text-slate-400 text-sm">
                        <span>By Gabi</span>
                        <span>•</span>
                        <span>Jan 13, 2026</span>
                        <span>•</span>
                        <span>6 min read</span>
                    </div>
                </div>
            </header>

            <!-- Article Content -->
            <article class="max-w-3xl mx-auto px-6 -mt-10 relative z-10">
                <div class="bg-white rounded-2xl shadow-xl p-8 md:p-12 border border-slate-100">
                    
                    <div class="prose prose-slate prose-lg max-w-none">
                        <p class="lead text-xl text-slate-600 font-medium mb-8">
                            You drive 100 miles. You make $100. You put $20 in gas. You think you made $80 profit. <strong>You are wrong.</strong> You actually made about $60, and I can prove it.
                        </p>

                        <h2>The Invisible Expense: Depreciation</h2>
                        <p>
                            Most gig drivers treat their car like a free resource. It is not. Every mile you drive is a tiny slice of your car's value disappearing forever.
                        </p>
                        <p>
                            Think about it:
                        </p>
                        <ul>
                            <li>A car with 50,000 miles is worth $15,000.</li>
                            <li>That same car with 150,000 miles is worth $5,000.</li>
                        </ul>
                        <p>
                            That $10,000 difference didn't just vanish. <strong>You sold it to Uber, one mile at a time.</strong>
                        </p>

                        <h2>The Math of Destruction</h2>
                        <p>
                            According to AAA and the IRS, the average cost to operate a vehicle in 2026 is around <strong>72 cents per mile</strong>.
                        </p>
                        <p>
                            "That's too high!" you say. "My Prius is efficient!"
                        </p>
                        <p>
                            Okay, let's look at the costs you <em>don't</em> pay at the pump:
                        </p>
                        <ul class="list-disc pl-5 space-y-2">
                            <li><strong>Tires:</strong> $800 every 40k miles = $0.02/mile</li>
                            <li><strong>Brakes:</strong> $400 every 30k miles = $0.013/mile</li>
                            <li><strong>Oil & Fluids:</strong> $80 every 5k miles = $0.016/mile</li>
                            <li><strong>Major Repairs:</strong> Alternator, Starter, Belts... it adds up.</li>
                            <li><strong>Insurance:</strong> Rideshare endorsement adds cost.</li>
                        </ul>

                        <div class="bg-slate-100 p-6 rounded-xl my-8 not-prose border-l-4 border-slate-900">
                            <h4 class="font-bold text-slate-900 mb-2">The Real Calculation</h4>
                            <ul class="space-y-1 text-sm text-slate-700">
                                <li><strong>Gross Earnings:</strong> $100.00</li>
                                <li><strong>Miles Driven:</strong> 100 miles</li>
                                <li><strong>IRS Deduction Cost:</strong> -$72.50 (100mi * $0.725)</li>
                                <li><strong>Taxable Profit:</strong> $27.50</li>
                            </ul>
                            <p class="mt-4 text-xs text-slate-500">
                                *Note: While the IRS number is a tax deduction, it closely mirrors real-world costs for standard sedans. Even if your cash cost is lower, the <em>value</em> loss is real.
                            </p>
                        </div>

                        <h2>Borrowing From Your Future Self</h2>
                        <p>
                            When you ignore depreciation, you are essentially taking a <strong>high-interest loan from your car's future value</strong>.
                        </p>
                        <p>
                            You get cash today ($100), but you are left with a worthless car sooner. When your transmission fails at 120k miles, that $3,000 repair bill is just all those "profits" coming back to haunt you at once.
                        </p>

                        <h2>How to Fight Back</h2>
                        <ol>
                            <li><strong>Track Every Mile:</strong> Use our <a href="/blog/tax-guide">Tax Guide</a> to ensure you claim every cent of the Standard Mileage Deduction.</li>
                            <li><strong>Drive the Right Car:</strong> Never DoorDash in a new car. Use a 10-year-old Toyota or Honda that has already depreciated.</li>
                            <li><strong>Cherry Pick:</strong> Don't move your car for less than $1.50/mile. If the offer is $5 for 10 miles, you are literally paying to work.</li>
                        </ol>

                        <h2>Conclusion</h2>
                        <p>
                            Don't let the apps fool you with "Revenue." Focus on "Profit." 
                        </p>
                    </div> 

                    <!-- CTA -->
                    <div class="mt-16 pt-8 border-t border-slate-200">
                        <div class="bg-slate-900 rounded-xl p-8 text-center text-white">
                            <h3 class="text-2xl font-bold mb-4">See Your Real Profit</h3>
                            <p class="text-slate-300 mb-6">Our calculator subtracts 2026 depreciation automatically.</p>
                            <div class="flex flex-col sm:flex-row justify-center gap-4">
                                <a href="/" class="px-6 py-3 bg-white text-slate-900 font-bold rounded-lg hover:bg-slate-100 transition">Uber Calc</a>
                                <a href="/" class="px-6 py-3 bg-red-600 text-white font-bold rounded-lg hover:bg-red-700 transition">DoorDash Calc</a>
                            </div>
                        </div>
                    </div>

                </div>
            </article>

        </div>
    `
)
