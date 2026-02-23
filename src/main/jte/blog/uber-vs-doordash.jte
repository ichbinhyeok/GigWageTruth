@import com.gigwager.model.SeoMeta
@import com.gigwager.util.AppConstants

@param String customTitle

@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        customTitle != null ? customTitle : "Uber vs DoorDash 2026: Which App Actually Pays More?",
        "Don't just look at the gross pay. We compare stress, vehicle wear, and hidden expenses to see which app is better for your personality.",
        AppConstants.BASE_URL + "/blog/uber-vs-doordash",
        AppConstants.BASE_URL + "/og-image-comparison.jpg"
    ),
    content = @`
        <div class="bg-white min-h-screen pb-24">
            
            <!-- Breadcrumbs -->
            <div class="max-w-3xl mx-auto px-6 pt-8">
                <a href="/blog" class="text-sm font-bold text-slate-400 hover:text-blue-600 transition flex items-center gap-2">
                    &larr; Back to Blog
                </a>
            </div>
            
            <!-- Article Header -->
            <header class="bg-slate-900 py-20 text-center px-6">
                <div class="max-w-3xl mx-auto">
                    <div class="inline-block bg-blue-500/20 text-blue-300 text-xs font-bold px-3 py-1 rounded-full mb-4 border border-blue-500/30">
                        COMPARISON
                    </div>
                    <h1 class="text-3xl md:text-5xl font-bold text-white mb-6 leading-tight">
                        Uber vs. DoorDash:<br>Food Doesn't Complain, But People Pay More
                    </h1>
                    <div class="flex justify-center items-center gap-4 text-slate-400 text-sm">
                        <span>By Gabi</span>
                        <span>•</span>
                        <span>Jan 18, 2026</span>
                        <span>•</span>
                        <span>7 min read</span>
                    </div>
                </div>
            </header>

            <!-- Article Content -->
            <article class="max-w-3xl mx-auto px-6 -mt-10 relative z-10">
                <div class="bg-white rounded-2xl shadow-xl p-8 md:p-12 border border-slate-100">
                    
                    <div class="prose prose-slate prose-lg max-w-none">
                        <p class="lead text-xl text-slate-600 font-medium mb-8">
                            It's the classic gig worker deliema: Do you want to drive strangers around in your car, or do you want to deliver lukewarm tacos? One pays more per hour, but the other lets you listen to your own music in peace.
                        </p>

                        <h2>💰 The Gross Income Reality</h2>
                        <p>
                            Historically, **Uber (Rideshare) pays 20-30% more in gross revenue** than DoorDash. Why? Because people are more expensive to move than packages. 
                        </p>
                        <p>
                            In a market like Austin or Chicago, a seasoned Uber driver can gross $30-$35/hr during peak times. A DoorDash "Top Dasher" is lucky to hit $25/hr in the same window. But as we always say here: **Gross is a vanity metric. Net is sanity.**
                        </p>

                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 my-10 not-prose">
                            <div class="bg-slate-50 p-6 rounded-xl border border-slate-200">
                                <h3 class="font-bold text-slate-900 mb-2">🚗 Uber Pros/Cons</h3>
                                <ul class="text-sm space-y-2 text-slate-700">
                                    <li>✅ Higher hourly gross potential</li>
                                    <li>✅ Longer trips = less downtime</li>
                                    <li>❌ Passengers can be rude/messy</li>
                                    <li>❌ High vehicle requirements (4-door, new-ish)</li>
                                    <li>❌ Constant need to keep car pristine</li>
                                </ul>
                            </div>
                            <div class="bg-red-50 p-6 rounded-xl border border-red-100">
                                <h3 class="font-bold text-red-900 mb-2">🍔 DoorDash Pros/Cons</h3>
                                <ul class="text-sm space-y-2 text-red-800">
                                    <li>✅ No strangers in your car</li>
                                    <li>✅ Low vehicle standards (beaters welcome)</li>
                                    <li>✅ Dress however you want</li>
                                    <li>❌ Constant parking/traffic headaches</li>
                                    <li>❌ High engine wear (frequent stop/start)</li>
                                </ul>
                            </div>
                        </div>

                        <h2>👤 The Hidden Cost of "People"</h2>
                        <p>
                            When you drive for Uber, you aren't just a driver; you're a hospitality worker. 
                        </p>
                        <p>
                            If a passenger spills a drink, your shift is over. If they rate you 1-star because they didn't like your air freshener, your account is at risk. You also have to pay for higher-tier insurance (Rideshare Endorsement) which is often more expensive for passengers than for food delivery.
                        </p>

                        <h2>📦 The Hidden Cost of "Food"</h2>
                        <p>
                            DoorDash kills your car in a different way: **Short-trip thermal cycles.** 
                        </p>
                        <p>
                            You turn your engine on and off 30 times a day. You're constantly idling in drive-thrus. You're parking illegally in red zones hoping you don't get a $60 ticket that wipes out your entire day's earnings.
                        </p>

                        <h2>🤔 Which One Should You Choose?</h2>
                        <p>
                            Take our advice: **Introverts drive food, Extroverts drive people.**
                        </p>
                        <p>
                            If you have a 2024 Toyota Camry and you enjoy chatting, Uber will make you more money. If you have a 2012 Honda Civic and you want to listen to podcasts without being interrupted, stick to DoorDash.
                        </p>

                        <div class="bg-slate-900 p-8 rounded-2xl text-white my-12 text-center">
                            <h4 class="text-2xl font-bold mb-4">Are you actually making a profit?</h4>
                                <p class="text-slate-400 mb-6">Our calculator knows the difference between Uber's insurance costs and DoorDash's gas consumption.</p>
                                <div class="flex flex-col sm:flex-row justify-center gap-4">
                                    <a href="/salary/uber/austin" class="bg-blue-600 hover:bg-blue-700 px-6 py-3 rounded-lg font-bold transition">Uber Profit Calc</a>
                                    <a href="/salary/doordash/austin" class="bg-red-600 hover:bg-red-700 px-6 py-3 rounded-lg font-bold transition">DoorDash Profit Calc</a>
                                </div>
                        </div>

                        <h2>The Pro Move: The Hybrid Strategy</h2>
                        <p>
                            The most successful gig workers in 2026 don't choose. They use **Uber for the morning commute** (high surges) and **DoorDash for the dinner rush** (high tips, consistent volume). 
                        </p>
                        <p>
                            Check out our <a href="/blog/multi-apping-guide">Multi-Apping Guide</a> to see how to run both simultaneously without getting deactivated.
                        </p>

                    </div> 

                </div>
            </article>

        </div>
    `
)
