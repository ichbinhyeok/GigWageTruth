@import com.gigwager.model.SeoMeta
@import com.gigwager.util.AppConstants

@param String customTitle

@template.layout.main(
    seoMeta = new SeoMeta(
        customTitle != null ? customTitle : "Multi-Apping Guide 2026: Double Your Hourly Wage",
        "Learn the professional strategy of running Uber, DoorDash, and Lyft simultaneously to minimize downtime and maximize profit.",
        AppConstants.BASE_URL + "/blog/multi-apping-guide",
        AppConstants.BASE_URL + "/og-image-multiapp.jpg"
    ),
    content = @`
        <div class="bg-white min-h-screen pb-24">
            
            <!-- Article Header -->
            <header class="bg-slate-900 py-20 text-center px-6">
                <div class="max-w-3xl mx-auto">
                    <div class="inline-block bg-emerald-500/20 text-emerald-300 text-xs font-bold px-3 py-1 rounded-full mb-4 border border-emerald-500/30">
                        STRATEGY GUIDE
                    </div>
                    <h1 class="text-3xl md:text-5xl font-bold text-white mb-6 leading-tight">
                        How to Double Your Hourly Wage with "Multi-Apping"
                    </h1>
                    <div class="flex justify-center items-center gap-4 text-slate-400 text-sm">
                        <span>By Gabi</span>
                        <span>•</span>
                        <span>Jan 12, 2026</span>
                        <span>•</span>
                        <span>10 min read</span>
                    </div>
                </div>
            </header>

            <!-- Article Content -->
            <article class="max-w-3xl mx-auto px-6 -mt-10 relative z-10">
                <div class="bg-white rounded-2xl shadow-xl p-8 md:p-12 border border-slate-100">
                    
                    <div class="prose prose-slate prose-lg max-w-none">
                        <p class="lead text-xl text-slate-600 font-medium mb-8">
                            Most gig drivers stare at their phone, waiting for a ping. They earn $15/hour. The top 5% of earners never wait. They earn $35+/hour. The difference? <strong>Multi-Apping.</strong>
                        </p>

                        <h2>What is Multi-Apping?</h2>
                        <p>
                            Multi-apping is simply the act of being online on multiple gig apps (Uber Eats, DoorDash, GrubHub, Lyft) at the exact same time. 
                        </p>
                        <p>
                            When you rely on one app, you are at the mercy of their algorithm. If DoorDash is slow, you starve. But if you have three apps open, the chance of receiving a profitable offer triples.
                        </p>

                        <div class="bg-yellow-50 border-l-4 border-yellow-400 p-6 my-8 not-prose rounded-r-lg">
                            <h4 class="text-yellow-800 font-bold text-lg mb-2">⚠️ The Golden Rule</h4>
                            <p class="text-yellow-700">
                                <strong>Never accept orders going in opposite directions.</strong> This is the fastest way to get deactivated. Only stack orders that are perfectly along the same route.
                            </p>
                        </div>

                        <h2>The Level 1 Strategy: The "Pauser"</h2>
                        <p>
                            This is the safest way to start. 
                        </p>
                        <ol>
                            <li>Turn on Uber Eats, DoorDash, and GrubHub.</li>
                            <li>Wait for a <strong>good offer</strong> (e.g., $10 for 3 miles) on ANY app.</li>
                            <li><strong>Accept it.</strong></li>
                            <li><strong>IMMEDIATELY PAUSE the other two apps.</strong></li>
                            <li>Complete the delivery.</li>
                            <li>Unpause all apps and repeat.</li>
                        </ol>
                        <p>
                            This method alone eliminates the 15-20 minutes of "dead time" drivers usually spend waiting in parking lots.
                        </p>

                        <h2>The Level 2 Strategy: The "Reviewer"</h2>
                        <p>
                            Once you know your city well, you can leave the other apps on while you are on a delivery. 
                        </p>
                        <p>
                            <strong>Scenario:</strong> You pick up a DoorDash order going North. While driving, Uber Eats pings you with an offer.
                        </p>
                        <ul>
                            <li><strong>Bad Offer:</strong> Going South? Decline immediately.</li>
                            <li><strong>Good Offer:</strong> Going North, pick-up is on the way? Accept it (creating a "Dirty Stack").</li>
                        </ul>
                        <p>
                            This requires nerves of steel and local knowledge. If the second restaurant has a 20-minute wait, you make the first customer late, risk a contract violation, and get a bad rating.
                        </p>

                        <h2>Best App Combinations</h2>
                        <p>Not all apps mix well. Here is my experience:</p>
                        
                        <h3>Uber Eats + DoorDash</h3>
                        <p>
                            <strong>Verdict: Excellent.</strong> Both have high volume. Uber Eats customers are generally more forgiving of small delays than DoorDash customers, who watch the GPS like hawks.
                        </p>

                        <h3>Rideshare + Food Delivery</h3>
                        <p>
                            <strong>Verdict: Difficult.</strong> You cannot have a passenger in the car and stop to pick up a burrito. Do not do this unless you are simply waiting for a ride and want to grab a quick food order in the meantime.
                        </p>

                        <h2>Conclusion</h2>
                        <p>
                            Gig apps are not your employer. They are your clients. You are a business. A business with one client is fragile. A business with three clients is robust.
                        </p>
                        <p>
                            Start with the "Pauser" strategy today. Your wallet will thank you.
                        </p>

                    </div> 

                    <!-- CTA -->
                    <div class="mt-16 pt-8 border-t border-slate-200">
                        <div class="bg-slate-900 rounded-xl p-8 text-center text-white">
                            <h3 class="text-2xl font-bold mb-4">Calculate Your True Hourly Wage</h3>
                            <p class="text-slate-300 mb-6">Are you really making money, or just trading your car's equity for cash?</p>
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
