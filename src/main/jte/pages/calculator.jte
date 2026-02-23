@import com.gigwager.model.SeoMeta
@import com.gigwager.util.AppConstants
@import com.gigwager.model.Verdict

@param Double initialGross
@param Double initialMiles  
@param Double initialHours
@param Double initialGasPrice
@param String app
@param String customTitle
@param Verdict verdict

@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        customTitle != null ? customTitle : (app.equals("uber") ? "My Real Hourly Wage - The Shocking Truth" : "DoorDash Truth - My Real Hourly Wage"),
        app.equals("uber") ? 
            "See exactly how much I make after gas, taxes, and depreciation. The number will surprise you." : 
            "See exactly how much you make on DoorDash after gas, vehicle destruction, and taxes. The truth is shocking.",
        "https://gigverdict.com/" + app,
        "https://gigverdict.com/og-image.jpg"
    ),
    content = @`
        <script src="/js/calculator-core.js?v=${AppConstants.CACHE_VERSION}"></script>

        <!-- Global State for Tab Switching -->
        <div x-data="{ activeTab: '${app}' }" @toggle-app="activeTab = $event.detail" class="min-h-screen bg-slate-50">

            <!-- Uber Container -->
            <div x-show="activeTab === 'uber'" style="display: none;">
                @template.components.calculator(
                    appName = "Uber",
                    appSlug = "uber",
                    themeColor = "emerald",
                    heroBgClass = "bg-slate-900",
                    initialGross = initialGross,
                    initialMiles = initialMiles,
                    initialHours = initialHours,
                    initialGasPrice = initialGasPrice,
                    defaultVehicle = "prius",
                    switchAction = "$dispatch('toggle-app', 'doordash')",
                    activeTabName = "uber",
                    verdictContainerId = "uber-verdict-container",
                    content = @`
                        <!-- Verdict Content Output -->
                        <article class="max-w-4xl mx-auto px-6 mt-12 space-y-16">
                            
                            <div id="uber-verdict-container">
                            @if(verdict != null)
@template.components.verdict_card(verdict = verdict)
                            @else
                            <!-- Fallback/Default Content if no calculation runs (e.g. direct load without params) -->
                            <section>
                                <h2 class="text-2xl font-bold text-slate-900 mb-4">Why is your real wage so low?</h2>
                                <div class="prose prose-slate max-w-none text-slate-600">
                                    <p>Many Uber drivers are shocked to see their "real" hourly wage drop below minimum wage. The culprit is usually <strong>hidden vehicle costs</strong>.</p>
                                    <p>When you drive for Uber, you are borrowing equity from your car's future value. <strong>Depreciation</strong> costs about $0.15 per mile for the average sedan. Add gas ($0.10-$0.15/mi) and maintenance ($0.09/mi), and your operating costs can easily exceed $0.35 per mile.</p>
                                    <p>This calculator uses the <strong>2026 IRS Standard Mileage Rate of $0.725/mile</strong> as a baseline, which includes all these factors plus insurance. This is the most accurate way to understand your true "take-home" pay.</p>
                                </div>
                            </section>
                            @endif
                            </div>

                            <section>
                                <h2 class="text-2xl font-bold text-slate-900 mb-6">Frequently Asked Questions</h2>
                                <div class="space-y-4">
                                    <div x-data="{ open: false }" class="border border-slate-200 rounded-xl bg-white overflow-hidden">
                                        <button @click="open = !open" class="flex justify-between items-center w-full px-6 py-4 text-left hover:bg-slate-50 transition">
                                            <span class="font-semibold text-slate-900">What is the IRS Standard Mileage Rate for 2026?</span>
                                            <svg class="w-5 h-5 text-slate-400 transition-transform" :class="{'rotate-180': open}" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
                                        </button>
                                        <div x-show="open" class="px-6 pb-4 text-slate-600">
                                            <p>The 2026 IRS Standard Mileage Rate is projected to be <strong>67 cents to 72.5 cents per mile</strong>. This rate covers gas, insurance, wear and tear, and depreciation. We use $0.725/mile in our Standard calculation to be safe.</p>
                                        </div>
                                    </div>
                                    <div x-data="{ open: false }" class="border border-slate-200 rounded-xl bg-white overflow-hidden">
                                        <button @click="open = !open" class="flex justify-between items-center w-full px-6 py-4 text-left hover:bg-slate-50 transition">
                                            <span class="font-semibold text-slate-900">Is Uber driving worth it in 2026?</span>
                                            <svg class="w-5 h-5 text-slate-400 transition-transform" :class="{'rotate-180': open}" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
                                        </button>
                                        <div x-show="open" class="px-6 pb-4 text-slate-600">
                                            <p>It depends on your vehicle efficiency. If you drive a hybrid (50+ MPG) and work during peak surge hours, you can earn $25-30/hr net. However, driving a gas-guzzling SUV often results in net earnings below $10/hr.</p>
                                        </div>
                                    </div>
                                    <div x-data="{ open: false }" class="border border-slate-200 rounded-xl bg-white overflow-hidden">
                                        <button @click="open = !open" class="flex justify-between items-center w-full px-6 py-4 text-left hover:bg-slate-50 transition">
                                            <span class="font-semibold text-slate-900">Do I really need to pay taxes on Uber income?</span>
                                            <svg class="w-5 h-5 text-slate-400 transition-transform" :class="{'rotate-180': open}" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
                                        </button>
                                        <div x-show="open" class="px-6 pb-4 text-slate-600">
                                            <p>Yes. Uber treats you as an independent contractor. You must pay roughly <strong>15.3% Self-Employment Tax</strong> on your profit. This calculator estimates that tax for you so you can save accordingly.</p>
                                        </div>
                                    </div>
                                </div>
                            </section>
                            
                            <section class="bg-white border border-slate-200 rounded-2xl p-8 shadow-sm mb-12">
                                <h3 class="text-xl font-bold text-slate-900 mb-4 flex items-center gap-2">
                                    <svg class="w-6 h-6 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                                    Methodology: How We Calculate Real Pay
                                </h3>
                                <div class="prose prose-sm text-slate-600 max-w-none">
                                    <p>Most earning calculators are misleading because they only subtract gas. GigWageTruth uses a comprehensive <strong>Total Cost of Ownership (TCO)</strong> model.</p>
                                    <table class="w-full mt-4 text-left border-collapse">
                                        <tbody class="align-top">
                                            <tr class="border-b border-slate-100 last:border-0">
                                                <td class="py-2 pr-4 font-bold text-slate-900 w-24 shrink-0 whitespace-nowrap">Revenue:</td>
                                                <td class="py-2 text-slate-600">Your gross payout from ${app} (fares + tips + bonuses).</td>
                                            </tr>
                                            <tr class="border-b border-slate-100 last:border-0">
                                                <td class="py-2 pr-4 font-bold text-slate-900 w-24 shrink-0 whitespace-nowrap">Expenses:</td>
                                                <td class="py-2 text-slate-600">We use the <strong>2026 IRS Standard Mileage Rate ($0.725/mi)</strong> unless you select a custom vehicle. This covers gas, insurance, tires, maintenance, and depreciation.</td>
                                            </tr>
                                            <tr class="border-b border-slate-100 last:border-0">
                                                <td class="py-2 pr-4 font-bold text-slate-900 w-24 shrink-0 whitespace-nowrap">Taxes:</td>
                                                <td class="py-2 text-slate-600">We estimate the <strong>15.3% Self-Employment Tax</strong> on your net profit. Note: This does not include Federal/State income tax, which varies by person.</td>
                                            </tr>
                                            <tr class="border-b border-slate-100 last:border-0">
                                                <td class="py-2 pr-4 font-bold text-slate-900 w-24 shrink-0 whitespace-nowrap">Net Hourly:</td>
                                                <td class="py-2 text-slate-600">(Revenue - Expenses - Taxes) / Total Hours Online. This is your true "take-home" wage.</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </section>

                            <div class="bg-emerald-50 rounded-2xl p-8 text-center mt-12 mb-12">
                                 <h3 class="text-xl font-bold text-emerald-900 mb-2">Stop driving blind</h3>
                                 <p class="text-emerald-700 mb-6">Learn professional strategies to protect your profit and double your hourly wage.</p>
                                 
                                 <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-4 text-left">
                                     <a href="/taxes" class="block bg-white p-4 rounded-xl border border-emerald-100 hover:border-emerald-300 hover:shadow-md transition">
                                         <h4 class="font-bold text-slate-900 mb-1">🏦 Tax Guide</h4>
                                         <p class="text-xs text-slate-500">Master the 1099 self-employment tax and deductions.</p>
                                     </a>
                                     <a href="/vehicle-cost" class="block bg-white p-4 rounded-xl border border-emerald-100 hover:border-emerald-300 hover:shadow-md transition">
                                         <h4 class="font-bold text-slate-900 mb-1">🚗 Vehicle Costs</h4>
                                         <p class="text-xs text-slate-500">Calculate the true cost of depreciation and maintenance.</p>
                                     </a>
                                     <a href="/insurance" class="block bg-white p-4 rounded-xl border border-emerald-100 hover:border-emerald-300 hover:shadow-md transition">
                                         <h4 class="font-bold text-slate-900 mb-1">🛡️ Insurance</h4>
                                         <p class="text-xs text-slate-500">Why your personal policy won't cover Uber crashes.</p>
                                     </a>
                                     <a href="/blog/multi-apping-guide" class="block bg-emerald-600 text-white p-4 rounded-xl hover:bg-emerald-700 hover:shadow-md transition">
                                         <h4 class="font-bold mb-1">📱 Multi-Apping</h4>
                                         <p class="text-xs text-emerald-100">Our guide to doubling your hourly wage.</p>
                                     </a>
                                 </div>
                            </div>
                        </article>
                        
                        <script type="application/ld+json">
                        {
                          "@context": "https://schema.org",
                          "@graph": [
                            {
                              "@type": "WebApplication",
                              "name": "Real Uber Driver Pay Calculator",
                              "url": "${AppConstants.BASE_URL}/uber",
                              "applicationCategory": "FinanceApplication",
                              "operatingSystem": "Any",
                              "description": "Calculate your true net profit as an Uber driver after gas, depreciation, and taxes.",
                              "offers": { "@type": "Offer", "price": "0", "priceCurrency": "USD" }
                            },
                            {
                              "@type": "BreadcrumbList",
                              "itemListElement": [{
                                "@type": "ListItem",
                                "position": 1,
                                "name": "Salary Directory",
                                "item": "${AppConstants.BASE_URL}/salary/directory"
                              },{
                                "@type": "ListItem",
                                "position": 2,
                                "name": "Uber Calculator & Pay Truth"
                              }]
                            }
                          ]
                        }
                        </script>
                        <script type="application/ld+json">
                        {
                          "@context": "https://schema.org",
                          "@type": "FAQPage",
                          "mainEntity": [{
                            "@type": "Question",
                            "name": "What is the IRS Standard Mileage Rate for 2026?",
                            "acceptedAnswer": { "@type": "Answer", "text": "The 2026 IRS Standard Mileage Rate is projected to be 67 cents to 72.5 cents per mile. This rate covers gas, insurance, wear and tear, and depreciation. We use $0.725/mile in our Standard calculation to be safe." }
                          }, {
                            "@type": "Question",
                            "name": "Is Uber driving worth it in 2026?",
                            "acceptedAnswer": { "@type": "Answer", "text": "It depends on your vehicle efficiency. If you drive a hybrid (50+ MPG) and work during peak surge hours, you can earn $25-30/hr net. However, driving a gas-guzzling SUV often results in net earnings below $10/hr." }
                          }, {
                            "@type": "Question",
                            "name": "Do I really need to pay taxes on Uber income?",
                            "acceptedAnswer": { "@type": "Answer", "text": "Yes. Uber treats you as an independent contractor. You must pay roughly 15.3% Self-Employment Tax on your profit. This calculator estimates that tax for you so you can save accordingly." }
                          }]
                        }
                        </script>
                    `
                )
            </div>

            <!-- DoorDash Container -->
            <div x-show="activeTab === 'doordash'" style="display: none;">
                @template.components.calculator(
                    appName = "DoorDash",
                    appSlug = "doordash",
                    themeColor = "red",
                    heroBgClass = "bg-gradient-to-br from-[#FF4040] to-[#D92C2C]",
                    initialGross = initialGross,
                    initialMiles = initialMiles,
                    initialHours = initialHours,
                    initialGasPrice = initialGasPrice,
                    defaultVehicle = "civic",
                    switchAction = "$dispatch('toggle-app', 'uber')",
                    activeTabName = "doordash",
                    verdictContainerId = "doordash-verdict-container",
                    content = @`
                         <!-- DoorDash Specific Content -->
                        <article class="max-w-4xl mx-auto px-6 mt-12 space-y-16">
                            
                            <div id="doordash-verdict-container">
                            @if(verdict != null)
@template.components.verdict_card(verdict = verdict)
                            @else
                            <section>
                                <h2 class="text-2xl font-bold text-slate-900 mb-4">Why Dashers earned less than minimum wage in 2026</h2>
                                <div class="prose prose-slate max-w-none text-slate-600">
                                    <p>DoorDash shows you "Active Time" earnings, which look great. But they hide the <strong>unpaid return trips</strong> and <strong>waiting time</strong>.</p>
                                    <p>Furthermore, food delivery takes a massive toll on your vehicle. The frequent start-stop driving accelerates wear on brakes and tires significantly more than highway driving.</p>
                                    <p>This calculator reveals the truth. We deduct the IRS Standard Mileage Rate ($0.725/mi), which accounts for the invisible depreciation of your car. If you are driving a new car for $2 deliveries, you are likely <strong>losing money</strong>.</p>
                                </div>
                            </section>
                            @endif
                            </div>

                            <section>
                                <h2 class="text-2xl font-bold text-slate-900 mb-6">Dasher FAQ</h2>
                                <div class="space-y-4">
                                    <div x-data="{ open: false }" class="border border-slate-200 rounded-xl bg-white overflow-hidden">
                                        <button @click="open = !open" class="flex justify-between items-center w-full px-6 py-4 text-left hover:bg-slate-50 transition">
                                            <span class="font-semibold text-slate-900">Can I write off gas on my taxes?</span>
                                            <svg class="w-5 h-5 text-slate-400 transition-transform" :class="{'rotate-180': open}" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
                                        </button>
                                        <div x-show="open" class="px-6 pb-4 text-slate-600">
                                            <p>You have two choices: Standard Mileage Deduction OR Actual Expenses (gas, repairs, insurance). You cannot pick both. For most Dashers, the <strong>Standard Mileage Deduction ($0.725/mi)</strong> saves more money and is easier to track.</p>
                                        </div>
                                    </div>
                                    <div x-data="{ open: false }" class="border border-slate-200 rounded-xl bg-white overflow-hidden">
                                        <button @click="open = !open" class="flex justify-between items-center w-full px-6 py-4 text-left hover:bg-slate-50 transition">
                                            <span class="font-semibold text-slate-900">Does Acceptance Rate matter for earnings?</span>
                                            <svg class="w-5 h-5 text-slate-400 transition-transform" :class="{'rotate-180': open}" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
                                        </button>
                                        <div x-show="open" class="px-6 pb-4 text-slate-600">
                                            <p>It's controversial. "Top Dashers" get priority, but they often take money-losing orders ($2.50) to keep their stats up. Cherry-picking (only taking profitable orders) often yields a higher <em>net hourly wage</em> even if you sit idle more often.</p>
                                        </div>
                                    </div>
                                    <div x-data="{ open: false }" class="border border-slate-200 rounded-xl bg-white overflow-hidden">
                                        <button @click="open = !open" class="flex justify-between items-center w-full px-6 py-4 text-left hover:bg-slate-50 transition">
                                            <span class="font-semibold text-slate-900">Should I DoorDash with a brand new car?</span>
                                            <svg class="w-5 h-5 text-slate-400 transition-transform" :class="{'rotate-180': open}" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
                                        </button>
                                        <div x-show="open" class="px-6 pb-4 text-slate-600">
                                            <p>Generally, <strong>NO</strong>. The depreciation hit on a new car is massive. Dashing puts thousands of hard city miles on a vehicle. It's usually best to use an older, reliable hybrid (like a Prius) or an e-bike to maximize profit.</p>
                                        </div>
                                    </div>
                                </div>
                            </section>
                            
                            <section class="bg-white border border-slate-200 rounded-2xl p-8 shadow-sm mb-12">
                                <h3 class="text-xl font-bold text-slate-900 mb-4 flex items-center gap-2">
                                    <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                                    Methodology: How We Calculate Real Pay
                                </h3>
                                <div class="prose prose-sm text-slate-600 max-w-none">
                                    <p>DoorDash "Active Time" is a vanity metric. GigWageTruth calculates your <strong>Real Hourly Wage</strong> by accounting for the full picture.</p>
                                    <table class="w-full mt-4 text-left border-collapse">
                                        <tbody class="align-top">
                                            <tr class="border-b border-slate-100 last:border-0">
                                                <td class="py-2 pr-4 font-bold text-slate-900 w-24 shrink-0 whitespace-nowrap">Revenue:</td>
                                                <td class="py-2 text-slate-600">Your gross payout from DoorDash (base pay + tips).</td>
                                            </tr>
                                            <tr class="border-b border-slate-100 last:border-0">
                                                <td class="py-2 pr-4 font-bold text-slate-900 w-24 shrink-0 whitespace-nowrap">Expenses:</td>
                                                <td class="py-2 text-slate-600">We use the <strong>2026 IRS Standard Mileage Rate ($0.725/mi)</strong>. This deduction is critical because city driving (stop-and-go) destroys brakes and tires faster than highway miles.</td>
                                            </tr>
                                            <tr class="border-b border-slate-100 last:border-0">
                                                <td class="py-2 pr-4 font-bold text-slate-900 w-24 shrink-0 whitespace-nowrap">Taxes:</td>
                                                <td class="py-2 text-slate-600">Estimated <strong>15.3% Self-Employment Tax</strong>. This is the "employer portion" of Social Security and Medicare that W-2 employees don't see, but you must pay.</td>
                                            </tr>
                                            <tr class="border-b border-slate-100 last:border-0">
                                                <td class="py-2 pr-4 font-bold text-slate-900 w-24 shrink-0 whitespace-nowrap">Real Wage:</td>
                                                <td class="py-2 text-slate-600">(Revenue - Expenses - Taxes) / Total Dash Time. We use "Total Time" because waiting in a parking lot is still work.</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </section>

                            <div class="bg-red-50 rounded-2xl p-8 text-center mt-12 mb-12">
                                 <h3 class="text-xl font-bold text-red-900 mb-2">Maximize your Dasher Pay</h3>
                                 <p class="text-red-700 mb-6">Learn professional strategies to protect your profit and double your hourly wage.</p>
                                 
                                 <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-4 text-left">
                                     <a href="/taxes" class="block bg-white p-4 rounded-xl border border-red-100 hover:border-red-300 hover:shadow-md transition">
                                         <h4 class="font-bold text-slate-900 mb-1">🏦 Tax Guide</h4>
                                         <p class="text-xs text-slate-500">Master the 1099 self-employment tax and deductions.</p>
                                     </a>
                                     <a href="/vehicle-cost" class="block bg-white p-4 rounded-xl border border-red-100 hover:border-red-300 hover:shadow-md transition">
                                         <h4 class="font-bold text-slate-900 mb-1">🚗 Vehicle Costs</h4>
                                         <p class="text-xs text-slate-500">Calculate the true cost of depreciation and maintenance.</p>
                                     </a>
                                     <a href="/insurance" class="block bg-white p-4 rounded-xl border border-red-100 hover:border-red-300 hover:shadow-md transition">
                                         <h4 class="font-bold text-slate-900 mb-1">🛡️ Insurance</h4>
                                         <p class="text-xs text-slate-500">Why your personal policy won't cover delivery crashes.</p>
                                     </a>
                                     <a href="/blog/multi-apping-guide" class="block bg-red-600 text-white p-4 rounded-xl hover:bg-red-700 hover:shadow-md transition">
                                         <h4 class="font-bold mb-1">📱 Multi-Apping</h4>
                                         <p class="text-xs text-red-100">Our guide to doubling your hourly wage.</p>
                                     </a>
                                 </div>
                            </div>
                        </article>

                        <script type="application/ld+json">
                        {
                          "@context": "https://schema.org",
                          "@graph": [
                            {
                              "@type": "WebApplication",
                              "name": "Real DoorDash Pay Calculator",
                              "url": "${AppConstants.BASE_URL}/doordash",
                              "applicationCategory": "FinanceApplication",
                              "operatingSystem": "Any",
                              "description": "Calculate your true net profit as a DoorDash driver after gas, depreciation, and taxes.",
                              "offers": { "@type": "Offer", "price": "0", "priceCurrency": "USD" }
                            },
                            {
                              "@type": "BreadcrumbList",
                              "itemListElement": [{
                                "@type": "ListItem",
                                "position": 1,
                                "name": "Salary Directory",
                                "item": "${AppConstants.BASE_URL}/salary/directory"
                              },{
                                "@type": "ListItem",
                                "position": 2,
                                "name": "DoorDash Calculator & Pay Truth"
                              }]
                            }
                          ]
                        }
                        </script>
                        <script type="application/ld+json">
                        {
                          "@context": "https://schema.org",
                          "@type": "FAQPage",
                          "mainEntity": [{
                            "@type": "Question",
                            "name": "Can I write off gas on my taxes?",
                            "acceptedAnswer": { "@type": "Answer", "text": "You have two choices: Standard Mileage Deduction OR Actual Expenses (gas, repairs, insurance). You cannot pick both. For most Dashers, the Standard Mileage Deduction ($0.725/mi) saves more money and is easier to track." }
                          }, {
                            "@type": "Question",
                            "name": "Does Acceptance Rate matter for earnings?",
                            "acceptedAnswer": { "@type": "Answer", "text": "It's controversial. \"Top Dashers\" get priority, but they often take money-losing orders ($2.50) to keep their stats up. Cherry-picking (only taking profitable orders) often yields a higher net hourly wage even if you sit idle more often." }
                          }, {
                            "@type": "Question",
                            "name": "Should I DoorDash with a brand new car?",
                            "acceptedAnswer": { "@type": "Answer", "text": "Generally, NO. The depreciation hit on a new car is massive. Dashing puts thousands of hard city miles on a vehicle. It's usually best to use an older, reliable hybrid (like a Prius) or an e-bike to maximize profit." }
                          }]
                        }
                        </script>
                    `
                )
            </div>

        </div>
    `
)
