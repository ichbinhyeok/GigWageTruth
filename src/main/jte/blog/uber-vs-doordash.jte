@import com.gigwager.model.SeoMeta

@param String customTitle

@template.layout.main(
    seoMeta = new SeoMeta(
        customTitle != null ? customTitle : "Uber vs DoorDash 2026: The Ultimate Comparison",
        "Data-driven comparison of earnings, stress, and vehicle wear between Uber Rideshare and DoorDash delivery.",
        "https://gigwagetruth.com/blog/uber-vs-doordash",
        "https://gigwagetruth.com/og-image-versus.jpg"
    ),
    content = @`
        <div class="bg-white min-h-screen pb-24">
            
            <!-- Article Header -->
            <header class="bg-white py-20 text-center px-6 border-b border-slate-100">
                <div class="max-w-3xl mx-auto">
                    <div class="inline-block bg-slate-100 text-slate-600 text-xs font-bold px-3 py-1 rounded-full mb-4">
                        APP BATTLE
                    </div>
                    <h1 class="text-3xl md:text-5xl font-bold text-slate-900 mb-6 leading-tight">
                        Uber vs. DoorDash: Which Pays Better in 2026?
                    </h1>
                    <div class="flex justify-center items-center gap-4 text-slate-500 text-sm">
                        <span>By Gabi</span>
                        <span>•</span>
                        <span>Jan 13, 2026</span>
                        <span>•</span>
                        <span>12 min read</span>
                    </div>
                </div>
            </header>

            <!-- Article Content -->
            <article class="max-w-3xl mx-auto px-6 mt-12">
                
                <div class="prose prose-slate prose-lg max-w-none">
                    <p class="lead text-xl text-slate-600 font-medium mb-8">
                        It's the classic debate: invite strangers into your car (Uber) or deal with cold burritos and apartment gates (DoorDash). I've done both for 5,000+ trips. Here is the honest breakdown.
                    </p>

                    <h2>1. Earning Potential</h2>
                    
                    <h3>Uber (Rideshare)</h3>
                    <p>
                        <strong>Ceiling: High ($30-40/hr).</strong><br>
                        During Friday/Saturday nights or events, surges can be insane. You are essentially a taxi during peak demand.
                    </p>

                    <h3>DoorDash (Delivery)</h3>
                    <p>
                        <strong>Ceiling: Medium ($20-25/hr).</strong><br>
                        Tips are generally steady, but "unicorns" ($50+ tips) are rare compared to a massive Uber surge ride.
                    </p>
                    
                    <p><strong>Winner: Uber (if you work weekends).</strong></p>

                    <h2>2. Vehicle Wear & Tear</h2>
                    <p>
                        This is where it gets interesting. 
                    </p>
                    <ul>
                        <li><strong>Uber:</strong> Mostly highway miles. Easier on brakes/transmission.</li>
                        <li><strong>DoorDash:</strong> Brutal setup. Start, stop, park illegally, restart engine, speed bumps in apartments.</li>
                    </ul>
                    <p><strong>Winner: Uber.</strong> Your car will last longer doing highway runs to the airport than delivering Starbucks 50 times a day.</p>

                    <h2>3. Stress & Safety</h2>
                    <p>
                        <strong>Uber:</strong> You have a stranger behind you. You have to maintain conversation (or read the room), keep the car spotless, and deal with drunk passengers.
                    </p>
                    <p>
                        <strong>DoorDash:</strong> You are alone. You can listen to podcasts, talk on the phone, or curse at traffic. The bag of Five Guys won't vomit in your back seat.
                    </p>
                    <p><strong>Winner: DoorDash (by a mile).</strong></p>

                    <h2>4. Barrier to Entry</h2>
                    <p>
                        <strong>Uber:</strong> Strict. newer car (usually < 15 years), strict background check, vehicle inspection.
                    </p>
                    <p>
                        <strong>DoorDash:</strong> Lenient. You can use a beater car, a scooter, or even a bicycle.
                    </p>
                    <p><strong>Winner: DoorDash.</strong></p>

                    <h2>The Verdict</h2>
                    <div class="grid md:grid-cols-2 gap-6 not-prose my-8">
                        <div class="bg-slate-900 text-white p-6 rounded-xl">
                            <h4 class="text-xl font-bold mb-2">Choose Uber If:</h4>
                            <ul class="space-y-2 text-sm text-slate-300">
                                <li>• You have a newer, clean car (Prius/Camry).</li>
                                <li>• You are an extrovert.</li>
                                <li>• You want maximum hourly earnings.</li>
                            </ul>
                        </div>
                        <div class="bg-red-600 text-white p-6 rounded-xl">
                            <h4 class="text-xl font-bold mb-2">Choose DoorDash If:</h4>
                            <ul class="space-y-2 text-red-100">
                                <li>• You value peace and privacy.</li>
                                <li>• Your car is older or "well-loved".</li>
                                <li>• You want to listen to audiobooks all day.</li>
                            </ul>
                        </div>
                    </div>

                    <p>
                        Ultimately, the best strategy is usually to have BOTH. Be ready to pivot. If it's raining, people order food (DoorDash). If it's a concert night, people need rides (Uber).
                    </p>
                </div> 

            </article>

        </div>
    `
)
