@import com.gigwager.util.AppConstants
@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new com.gigwager.model.SeoMeta(
        customTitle,
        "Understand the true cost of driving for Uber and DoorDash. Learn how depreciation, maintenance, and gas eat into your gig economy profits.",
        AppConstants.BASE_URL + "/vehicle-cost",
        AppConstants.BASE_URL + "/og-image.jpg"
    ),
    content = @`
        <div class="max-w-4xl mx-auto px-6 py-12 lg:py-20">
            <!-- Hero -->
            <div class="text-center mb-16">
                <span class="text-orange-600 font-bold tracking-widest uppercase text-sm">Revenue Pillar</span>
                <h1 class="text-4xl lg:text-5xl font-black text-slate-900 mt-4 mb-6 tracking-tight">The Silent Killer: Vehicle Depreciation</h1>
                <p class="text-xl text-slate-600 max-w-2xl mx-auto">You aren't making as much as you think. Discover the hidden costs that destroy your car's value and your actual hourly wage.</p>
            </div>

            <!-- Content Grid -->
            <div class="space-y-12">
                <!-- Section 1 -->
                <section class="bg-white rounded-3xl p-8 lg:p-12 border border-slate-200 shadow-sm">
                    <h2 class="text-2xl font-bold text-slate-900 mb-4">1. Borrowing Equity from Your Future</h2>
                    <div class="prose prose-slate max-w-none text-slate-600">
                        <p>When you cash out your Uber earnings, it feels like pure profit. In reality, you are converting the value of your vehicle into cash today. This is called <strong>Depreciation</strong>.</p>
                        <p>Every mile you drive reduces the resale value of your car. For an average sedan, depreciation costs around $0.15 to $0.25 per mile. If you drive 100 miles to make $100, you just lost $20 in car value.</p>
                    </div>
                </section>

                <!-- Section 2 -->
                <section class="bg-white rounded-3xl p-8 lg:p-12 border border-slate-200 shadow-sm">
                    <h2 class="text-2xl font-bold text-slate-900 mb-4">2. The "Big Three" Running Costs</h2>
                    <div class="prose prose-slate max-w-none text-slate-600">
                        <p>Besides depreciation, you must account for the physical running costs that scale directly with your mileage:</p>
                        <ul class="list-disc pl-5 mt-4 space-y-2">
                            <li><strong>Fuel:</strong> Even at 30 MPG, gas costs $0.10 to $0.15 per mile depending on your location.</li>
                            <li><strong>Maintenance:</strong> Oil changes, brake pads, fluids. Delivery driving (stop-and-go) destroys brakes twice as fast as highway driving. Budget $0.09 per mile.</li>
                            <li><strong>Tires:</strong> A $600 set of tires that lasts 40,000 miles costs you $0.015 for every mile driven.</li>
                        </ul>
                    </div>
                </section>

                <!-- Section 3 -->
                <section class="bg-white rounded-3xl p-8 lg:p-12 border border-slate-200 shadow-sm">
                    <h2 class="text-2xl font-bold text-slate-900 mb-4">3. Why the "New Car Trap" is Fatal</h2>
                    <div class="prose prose-slate max-w-none text-slate-600">
                        <p>The worst financial decision a gig worker can make is buying a brand-new car to drive for Uber or DoorDash. A new car loses 20% of its value the second you drive it off the lot, and racking up 30,000 gig miles a year accelerates that curve drastically.</p>
                        <div class="bg-orange-50 rounded-xl p-6 mt-6 border border-orange-100">
                            <h4 class="font-bold text-orange-900 mb-2">The Golden Rule of Gig Vehicles:</h4>
                            <p class="text-orange-800 text-sm">Drive a fully depreciated, highly reliable, high-MPG vehicle (like a 2012 Prius). Let someone else take the massive initial depreciation hit.</p>
                        </div>
                    </div>
                </section>

                <!-- Call to Action (Affiliate/Lead Gen Hook) -->
                <section class="bg-slate-900 rounded-3xl p-8 lg:p-12 text-center shadow-xl">
                    <h2 class="text-3xl font-bold text-white mb-4">Need a cheaper car payment?</h2>
                    <p class="text-slate-300 mb-8 max-w-xl mx-auto">Stop destroying your personal vehicle. Look into flexible, short-term car rentals specifically designed for rideshare and delivery drivers.</p>
                    <a href="#" class="inline-block bg-orange-500 hover:bg-orange-400 text-slate-900 font-bold px-8 py-4 rounded-xl transition duration-200">View Rideshare Rental Options &rarr;</a>
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
            "name": "Vehicle Cost"
          }]
        }
        </script>
    `
)
