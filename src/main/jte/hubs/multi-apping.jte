@import com.gigwager.util.AppConstants
@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new com.gigwager.model.SeoMeta(
        customTitle,
        "Master multi-apping by combining Uber and DoorDash. Learn the strategy to never wait for an order and double your net hourly wage.",
        AppConstants.BASE_URL + "/multi-apping",
        AppConstants.BASE_URL + "/og-image.jpg"
    ),
    content = @`
        <div class="max-w-4xl mx-auto px-6 py-12 lg:py-20">
            <!-- Hero with TL;DR Answer Box -->
            <div class="text-center mb-12">
                <span class="text-amber-600 font-bold tracking-widest uppercase text-sm">Strategy Hub</span>
                <h1 class="text-4xl lg:text-5xl font-black text-slate-900 mt-4 mb-4 tracking-tight">Mastering Multi-Apping: Uber + DoorDash</h1>
                <p class="text-xl text-slate-600 max-w-2xl mx-auto mb-8">Stop waiting in parking lots. Run two apps simultaneously to maximize active time and increase net profit.</p>
                
                <div class="bg-gradient-to-br from-amber-600 to-amber-700 rounded-3xl p-8 shadow-xl text-left border border-amber-500 relative overflow-hidden">
                    <div class="absolute -right-8 -top-8 text-white/10 text-9xl font-black">TL;DR</div>
                    <div class="relative z-10 text-center">
                        <h2 class="text-amber-200 font-bold mb-3 uppercase tracking-wider text-sm flex justify-center items-center gap-2">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path></svg>
                            The Short Answer
                        </h2>
                        <p class="text-2xl lg:text-3xl text-white font-medium leading-tight">
                            Multi-apping is the only way to consistently generate $20+/hr net profit. You run both Uber and DoorDash simultaneously, accept the highest paying ping, and <strong class="underline decoration-amber-300">pause the other app immediately</strong>.
                        </p>
                    </div>
                </div>
            </div>

            <!-- Jump to section links -->
            <div class="flex flex-wrap gap-3 justify-center mb-16">
                <a href="#how-to" class="px-5 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-full font-medium text-sm transition-colors">How To Multi-App</a>
                <a href="#faqs" class="px-5 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-full font-medium text-sm transition-colors">Top 10 FAQs</a>
                <a href="/salary/directory" class="px-5 py-2 bg-amber-50 hover:bg-amber-100 text-amber-800 rounded-full font-bold text-sm transition-colors border border-amber-200">See Your City's Data</a>
            </div>

            <!-- The Strategy -->
            <section id="how-to" class="mb-16">
                <h2 class="text-3xl font-black text-slate-900 mb-6">The "Pause and Play" Strategy</h2>
                <div class="bg-white rounded-2xl p-8 border border-slate-200 shadow-sm prose prose-slate max-w-none">
                    <p>Both Uber and DoorDash algorithmically throttle drivers who reject too many low-paying orders. By running both, you double your order volume. Here is the strict protocol you must follow to avoid deactivation:</p>
                    <ul>
                        <li><strong>1. Go Online with Both:</strong> Turn on both driver apps. Position yourself centrally between residential areas (Uber rides) and restaurant hubs (DoorDash pick-ups).</li>
                        <li><strong>2. Cherry-Pick Ruthlessly:</strong> Wait for a request that pays at least $1.50 to $2.00 per mile. Ignore the $3 trash.</li>
                        <li><strong>3. Accept & Pause:</strong> The moment you accept an excellent Uber ride, immediately switch to the DoorDash app and click "Pause Orders" (or go offline). <strong>Do not</strong> attempt to pick up a passenger and hot food simultaneously.</li>
                        <li><strong>4. Complete & Unpause:</strong> Once the ride or delivery is finished, immediately turn the paused app back online and repeat.</li>
                    </ul>
                    <p class="italic text-slate-500 mt-4 text-sm">Warning: Never accept an Uber ride and a DoorDash delivery at the exact same time going in opposite directions. You will be late to both, receive 1-star ratings, and get banned.</p>
                </div>
            </section>

            <!-- Internal Linking Spider Web -->
            @template.components.related_pages(app = "uber")

            <!-- FAQs (AEO Component) -->
            <section id="faqs" class="mt-20">
                <h2 class="text-3xl font-black text-slate-900 mb-8 border-b border-slate-200 pb-4">Top 8 FAQs: Multi-Apping</h2>
                
                <div class="space-y-6">
                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">1. Will DoorDash or Uber ban me for multi-apping?</h3>
                        <p class="text-slate-600">No. As an independent contractor (1099), you legally have the right to work for multiple platforms. However, they <em>can</em> deactivate you for consistent lateness. That's why you must pause the other app once you accept a trip.</p>
                    </div>
                    
                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">2. Should I run Uber Eats and DoorDash at the same time?</h3>
                        <p class="text-slate-600">Yes, this is common. Food delivery multi-apping is slightly more flexible. Advanced drivers sometimes take two orders from two different apps <em>only</em> if the restaurants are next to each other and the drop-offs are in the exact same neighborhood. Otherwise, stick to the 'Pause and Play' method.</p>
                    </div>
                    
                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">3. Does it consume more data or battery?</h3>
                        <p class="text-slate-600">Yes, running two GPS-heavy apps simultaneously drains phone batteries 2-3x faster. You absolutely must have a high-wattage car charger plugged in at all times.</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">4. Can I use Lyft as well?</h3>
                        <p class="text-slate-600">Absolutely. Running Uber, Lyft, and DoorDash simultaneously means you rarely wait more than 3 minutes for a ping, dramatically increasing your active driving hours and gross revenue.</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">5. How do I handle taxes when multi-apping?</h3>
                        <p class="text-slate-600">You will receive a separate 1099 form from each company (if you cross their reporting threshold). The good news is you can combine all your business mileage driven into one massive deduction on your Schedule C tax filing.</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">6. Are acceptance rates important?</h3>
                        <p class="text-slate-600">DoorDash heavily incentivizes keeping your Acceptance Rate (AR) above 70% or 50% for high-paying order priority ("Top Dasher"). Multi-apping often tanks your AR on both apps since you are declining many orders. You must decide if picking cherries is more profitable in your specific city than maintaining Top Dasher status.</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">7. Which app is better for mornings?</h3>
                        <p class="text-slate-600">Uber is generally much stronger in the early morning (4 AM - 9 AM) for airport runs and corporate commuting. DoorDash is stronger during lunch (11 AM - 1 PM) and dinner rushes (5 PM - 8 PM).</p>
                    </div>

                    <div class="bg-white p-6 rounded-xl border border-slate-200">
                        <h3 class="text-xl font-bold text-slate-900 mb-2">8. What happens if I forget to pause one app?</h3>
                        <p class="text-slate-600">If you are midway through an Uber ride and a DoorDash notification pops up, simply ignore it. Your DoorDash AR will drop slightly, but the app will eventually pause itself if you miss several consecutive requests.</p>
                    </div>
                </div>
            </section>
        </div>
    `
)
