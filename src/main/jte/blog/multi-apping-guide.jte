@import com.gigwager.model.SeoMeta
@import com.gigwager.util.AppConstants
@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        customTitle != null ? customTitle : "Multi-Apping Guide 2026: Clean Stacking Without Getting Deactivated",
        "Learn the clean stacking system for Uber, DoorDash, Lyft, and Instacart. Reduce dead time, avoid contract violations, and raise net hourly pay without driving faster.",
        AppConstants.BASE_URL + "/blog/multi-apping-guide",
        AppConstants.BASE_URL + "/og-image-multiapping.jpg"
    ),
    content = @`
        <div class="bg-white min-h-screen pb-24">

            <div class="max-w-3xl mx-auto px-6 pt-8">
                <a href="/blog" class="text-sm font-bold text-slate-400 hover:text-orange-600 transition flex items-center gap-2">
                    &larr; Back to Blog
                </a>
            </div>

            <header class="bg-slate-900 py-20 text-center px-6">
                <div class="max-w-3xl mx-auto">
                    <div class="inline-block bg-orange-500/20 text-orange-300 text-xs font-bold px-3 py-1 rounded-full mb-4 border border-orange-500/30">
                        ADVANCED STRATEGY
                    </div>
                    <h1 class="text-3xl md:text-5xl font-bold text-white mb-6 leading-tight">
                        The Multi-Apping Guide:<br>Clean Stacking Without Getting Deactivated
                    </h1>
                    <div class="flex justify-center items-center gap-4 text-slate-400 text-sm">
                        <span>By Gabi</span>
                        <span>|</span>
                        <span>Jan 21, 2026</span>
                        <span>|</span>
                        <span>10 min read</span>
                    </div>
                </div>
            </header>

            <article class="max-w-3xl mx-auto px-6 -mt-10 relative z-10">
                <div class="bg-white rounded-2xl shadow-xl p-8 md:p-12 border border-slate-100">

                    <div class="prose prose-slate prose-lg max-w-none">
                        <p class="lead text-xl text-slate-600 font-medium mb-8">
                            If you only run one app, you accept too much dead time. Clean multi-apping lets you source the next good order faster without juggling two active jobs at once.
                        </p>

                        <h2>What Multi-Apping Actually Means</h2>
                        <p>
                            Multi-apping means keeping multiple gig apps open so you can find the next worthwhile trip or delivery faster. The goal is not chaos. The goal is to reduce dead time between jobs and protect your net hourly pay.
                        </p>
                        <p>
                            Done correctly, multi-apping is a routing and filtering strategy. Done badly, it turns into late deliveries, passenger complaints, contract violations, and deactivation risk.
                        </p>

                        <div class="bg-slate-900 text-white p-8 rounded-2xl my-10 border-l-8 border-orange-500">
                            <h3 class="text-xl font-bold mb-4 text-orange-400">The Golden Rule of Multi-Apping</h3>
                            <p class="text-slate-300 italic">
                                "Multi-app to find work. Single-app to do work."
                            </p>
                        </div>

                        <h2>Clean Stacking vs. Dirty Stacking</h2>
                        <p>
                            There are two versions of multi-apping. One raises pay. The other gets you in trouble.
                        </p>

                        <h3>1. Clean Stacking</h3>
                        <p>
                            You keep Uber and DoorDash on while you are waiting. Uber sends a strong ride. You accept it, then <strong>immediately pause DoorDash</strong> so there is only one active job to finish.
                        </p>
                        <p>
                            Near the end of the ride, you unpause DoorDash and start screening the next offer. By the time the current trip ends, you are already lining up the next move. That is clean stacking: lower idle time without overlapping active work.
                        </p>

                        <h3>2. Dirty Stacking</h3>
                        <p>
                            You accept a DoorDash order, then grab an Uber trip going the opposite direction while the food is still waiting. Or you keep two delivery apps active and try to juggle both pickups at once.
                        </p>
                        <p>
                            <strong>Do not do this.</strong> DoorDash tracks lateness. Uber riders notice detours. Support tickets and low ratings pile up faster than the extra payout is worth.
                        </p>

                        <h2>The Setup That Makes It Work</h2>
                        <p>
                            Clean stacking is easier when the basics are locked down:
                        </p>
                        <ul>
                            <li><strong>Magnetic phone mount:</strong> You need to pause and resume apps with one safe tap.</li>
                            <li><strong>Reliable charging:</strong> A weak cable turns multi-apping into a battery-management problem.</li>
                            <li><strong>Unlimited data or a high cap:</strong> GPS, maps, and multiple gig apps burn through data quickly.</li>
                            <li><strong>CarPlay or Android Auto:</strong> Keep navigation on the dash so your phone stays free for dispatch decisions.</li>
                        </ul>

                        <h2>How to Decide Which Offer Wins</h2>
                        <p>
                            Multi-apping only works if you reject bad work consistently. More apps mean more noise, not automatically more profit.
                        </p>
                        <p>
                            Use a simple filter:
                        </p>
                        <ul>
                            <li><strong>Check net pay, not gross:</strong> A long ride with dead miles can still lose to a shorter delivery loop.</li>
                            <li><strong>Protect your next zone:</strong> Do not accept work that drags you far from the next demand cluster unless the pay premium is clear.</li>
                            <li><strong>Keep platform rules intact:</strong> If the next step would make you late on the current job, skip it.</li>
                        </ul>

                        <h2>What Multi-Apping Cannot Fix</h2>
                        <p>
                            Multi-apping does not rescue a weak market, a bad vehicle cost profile, or a schedule that misses demand windows. It improves utilization. It does not rewrite the economics of a city.
                        </p>
                        <p>
                            If your numbers are still weak after reducing dead time, the better answer may be changing app mix, city, or work window rather than stacking more aggressively.
                        </p>

                        <h2>Conclusion</h2>
                        <p>
                            The safest version of multi-apping is boring on purpose. Use multiple apps to source the next job, pause the rest when you commit, and judge every offer by net pay after costs. That is how you raise hourly earnings without inviting avoidable platform risk.
                        </p>

                        <div class="bg-orange-50 p-8 rounded-2xl border border-orange-200 mt-12">
                            <h4 class="text-xl font-bold text-orange-900 mb-2">Want to see where clean stacking helps most?</h4>
                            <p class="text-orange-800 mb-6">Compare Uber and DoorDash net pay by city before you decide which apps deserve a slot on your screen.</p>
                            <div class="flex flex-wrap gap-3">
                                <a href="/salary/uber" class="inline-block bg-orange-600 text-white px-6 py-3 rounded-lg font-bold hover:bg-orange-700 transition">See Uber pay by city</a>
                                <a href="/salary/doordash" class="inline-block bg-white text-orange-700 px-6 py-3 rounded-lg font-bold border border-orange-300 hover:bg-orange-100 transition">See DoorDash pay by city</a>
                            </div>
                        </div>

                    </div>

                </div>
            </article>

        </div>
    `
)
