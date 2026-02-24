@import com.gigwager.util.AppConstants
@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new com.gigwager.model.SeoMeta(
        customTitle,
        "Is your personal auto insurance enough for Uber or DoorDash? Learn why you need rideshare insurance and how to find affordable coverage in 2026.",
        AppConstants.BASE_URL + "/insurance",
        AppConstants.BASE_URL + "/og-image.jpg"
    ),
    content = @`
        <div class="max-w-4xl mx-auto px-6 py-12 lg:py-20">
            <!-- Hero -->
            <div class="text-center mb-16">
                <span class="text-blue-600 font-bold tracking-widest uppercase text-sm">Revenue Pillar</span>
                <h1 class="text-4xl lg:text-5xl font-black text-slate-900 mt-4 mb-6 tracking-tight">The Rideshare Insurance Trap</h1>
                <p class="text-xl text-slate-600 max-w-2xl mx-auto">One crash while delivering could be a severe financial risk. Find out why your personal auto policy may not cover you while driving for Gig apps.</p>
            </div>

            <!-- Content Grid -->
            <div class="space-y-12">
                <!-- Section 1 -->
                <section class="bg-white rounded-3xl p-8 lg:p-12 border border-slate-200 shadow-sm">
                    <h2 class="text-2xl font-bold text-slate-900 mb-4">1. The "Off-App" vs. "On-App" Gap</h2>
                    <div class="prose prose-slate max-w-none text-slate-600">
                        <p>A standard personal auto insurance policy explicitly excludes "livery services" (driving for hire). If you crash while the Uber or DoorDash app is simply <em>on</em>, your personal insurance can deny the claim and cancel your policy.</p>
                        <p>The apps themselves only provide full coverage during "Period 2" and "Period 3" (when you have accepted a ride or are actively delivering). "Period 1" (app on, waiting for a ping) relies on minimal liability coverage only.</p>
                    </div>
                </section>

                <!-- Section 2 -->
                <section class="bg-white rounded-3xl p-8 lg:p-12 border border-slate-200 shadow-sm">
                    <h2 class="text-2xl font-bold text-slate-900 mb-4">2. What is Rideshare Endorsement?</h2>
                    <div class="prose prose-slate max-w-none text-slate-600">
                        <p>To fill this dangerous "Period 1" gap, you need a <strong>Rideshare Endorsement</strong> or a hybrid policy from your insurer.</p>
                        <ul class="list-disc pl-5 mt-4 space-y-2">
                            <li>It extends your personal coverage to protect you while waiting for requests.</li>
                            <li>It prevents your insurance company from dropping you if they find out you drive for the apps.</li>
                            <li>Surprisingly, it usually only costs an extra $10 to $25 per month.</li>
                        </ul>
                    </div>
                </section>

                <!-- Section 3 -->
                <section class="bg-white rounded-3xl p-8 lg:p-12 border border-slate-200 shadow-sm">
                    <h2 class="text-2xl font-bold text-slate-900 mb-4">3. The Massive App Deductibles</h2>
                    <div class="prose prose-slate max-w-none text-slate-600">
                        <p>Even when Uber or Lyft's insurance kicks in (Period 2/3), their collision deductibles are incredibly high. Uber's deductible is a staggering <strong>$2,500</strong>.</p>
                        <p>A good rideshare endorsement from your personal provider will lower that deductible down to your personal policy amount (e.g., $500), saving you $2,000 out-of-pocket if you wreck your car while on a trip.</p>
                    </div>
                </section>

                <!-- Call to Action (Affiliate/Lead Gen Hook) -->
                <section class="bg-slate-900 rounded-3xl p-8 lg:p-12 text-center shadow-xl">
                    <h2 class="text-3xl font-bold text-white mb-4">Get Covered Before It's Too Late</h2>
                    <p class="text-slate-300 mb-8 max-w-xl mx-auto">Not all insurers offer rideshare coverage in every state. Compare quotes from providers who specialize in protecting gig workers.</p>
                    <form action="https://formspree.io/f/mqkenryp" method="POST" class="flex flex-col sm:flex-row gap-2 max-w-xl" onsubmit="gtag('event','cta_click',{'event_category':'lead','event_label':'insurance_quote'});">
                        <input type="email" name="email" placeholder="Enter your email..." required class="flex-1 px-6 py-4 rounded-xl border-2 border-slate-200 focus:outline-none focus:border-blue-500 text-slate-900 font-medium">
                        <button type="submit" class="bg-blue-600 hover:bg-blue-500 text-white font-bold px-8 py-4 rounded-xl transition duration-200 whitespace-nowrap">Get Free Quote &rarr;</button>
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
            "name": "Insurance"
          }]
        }
        </script>
    `
)
