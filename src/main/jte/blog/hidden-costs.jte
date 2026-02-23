@import com.gigwager.model.SeoMeta
@import com.gigwager.util.AppConstants

@param String customTitle

@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        customTitle != null ? customTitle : "The Hidden Cost of Depreciation: Why Your $100 Day is Actually $60",
        "Depreciation is the silent killer of gig worker profits. Learn the math of why every mile you drive is a slice of your car's value disappearing.",
        AppConstants.BASE_URL + "/blog/hidden-costs",
        AppConstants.BASE_URL + "/og-image-depreciation.jpg"
    ),
    content = @`
        <div class="bg-white min-h-screen pb-24">
            
            <!-- Breadcrumbs -->
            <div class="max-w-3xl mx-auto px-6 pt-8">
                <a href="/blog" class="text-sm font-bold text-slate-400 hover:text-red-600 transition flex items-center gap-2">
                    &larr; Back to Blog
                </a>
            </div>
            
            <!-- Article Header -->
            <header class="bg-slate-900 py-20 text-center px-6">
                <div class="max-w-3xl mx-auto">
                    <div class="inline-block bg-red-500/20 text-red-300 text-xs font-bold px-3 py-1 rounded-full mb-4 border border-red-500/30">
                        VEHICLE ECONOMICS
                    </div>
                    <h1 class="text-3xl md:text-5xl font-bold text-white mb-6 leading-tight">
                        The Silent Killer:<br>Why Your "$100 Day" is Actually Only $60
                    </h1>
                    <div class="flex justify-center items-center gap-4 text-slate-400 text-sm">
                        <span>By Gabi</span>
                        <span>•</span>
                        <span>Jan 23, 2026</span>
                        <span>•</span>
                        <span>9 min read</span>
                    </div>
                </div>
            </header>

            <!-- Article Content -->
            <article class="max-w-3xl mx-auto px-6 -mt-10 relative z-10">
                <div class="bg-white rounded-2xl shadow-xl p-8 md:p-12 border border-slate-100">
                    
                    <div class="prose prose-slate prose-lg max-w-none">
                        <p class="lead text-xl text-slate-600 font-medium mb-8">
                            You drive 100 miles. You make $100. You put $20 in gas. You think you made $80 profit. <strong>You are wrong.</strong> You actually made about $60, and I can prove it with one word: **Depreciation.**
                        </p>

                        <h2>👻 The Invisible Expense</h2>
                        <p>
                            Most gig drivers treat their car like a free resource. It is not. It is a **depreciating asset**. Every mile you drive is a tiny slice of your car's resale value disappearing forever.
                        </p>
                        <p>
                            Think about it this way:
                        </p>
                        <ul class="list-none pl-0">
                            <li class="bg-slate-50 p-4 rounded-lg mb-2">🚗 A car with <strong>50,000 miles</strong> is worth $15,000.</li>
                            <li class="bg-slate-50 p-4 rounded-lg">🚗 That same car with <strong>150,000 miles</strong> is worth $5,000.</li>
                        </ul>
                        <p>
                            That $10,000 difference didn't just vanish. **You sold it to Uber, one mile at a time.**
                        </p>

                        <h2>📉 The "50k Mile Cliff"</h2>
                        <p>
                            Cars don't lose value linearly. There are "cliffs" where the value drops significantly:
                        </p>
                        <ul class="list-disc pl-5">
                            <li><strong>36,000 Miles:</strong> Most bumper-to-bumper warranties expires. Value drops.</li>
                            <li><strong>60,000 Miles:</strong> Major powertrain warranties expire. Value drops.</li>
                            <li><strong>100,000 Miles:</strong> In the eyes of a buyer, the car is now "high mileage." Value plummets.</li>
                        </ul>
                        <p>
                            If you are driving a car with 95,000 miles, every delivery you take is pushing you closer to that 100k mark. That single $5 burrito delivery might cost you $500 in resale value if it's the one that flips the odometer.
                        </p>

                        <div class="bg-red-50 p-6 rounded-xl border-l-4 border-red-500 my-8 not-prose">
                            <h4 class="font-bold text-red-900 mb-2">The Real Math of Destruction</h4>
                            <p class="text-red-800 text-sm">
                                According to the IRS, it costs <strong>72.5 cents per mile</strong> to operate a vehicle in 2026. If you drive 1,000 miles a week, that's $725 in *real* economic cost. If you only made $1,000 that week, you didn't "earn" $1,000. You earned $275.
                            </p>
                        </div>

                        <h2>🛡️ How to Fight Back</h2>
                        <p>
                            Is it impossible to make money? No. But you have to be smart:
                        </p>
                        <ol>
                            <li><strong>🛠️ Drive the "Bottom of the Curve":</strong> Don't use a new car. Use a car that has already lost most of its value (e.g., a 10-year-old Toyota). If the car is only worth $4,000, it can't lose much more.</li>
                            <li><strong>📅 Maintain religiously:</strong> A well-documented maintenance history can mitigate the loss of value when you eventually sell the car.</li>
                            <li><strong>👀 Watch the Odometer:</strong> If you are near a major milestone (60k, 100k), consider trading the car in *before* you hit it.</li>
                        </ol>

                        <h2>Conclusion</h2>
                        <p>
                            Don't let the apps fool you with "Gross Revenue." Focus on your **Net Profit**. Your car is a tool, not a toy. Treat it like a business expense, because that's exactly what it is.
                        </p>

                        <div class="mt-12 text-center">
                            <p class="text-sm text-slate-500 mb-6 italic">Want to see how much your car is actually losing? Use our specialized profit calculator.</p>
                            <a href="/" class="inline-block bg-slate-900 text-white px-10 py-4 rounded-full font-bold hover:bg-slate-800 transition shadow-xl">
                                Calculate My Real Hourly Wage
                            </a>
                        </div>
                    </div> 

                </div>
            </article>

        </div>
    `
)
