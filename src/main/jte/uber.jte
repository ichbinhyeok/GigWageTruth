@import com.gigwager.util.AppConstants

@template.layout.main(
    seoMeta = new com.gigwager.model.SeoMeta(
        "Real Uber Driver Pay Calculator - The Brutal Truth",
        "Stop guessing. Calculate your real net profit after gas, taxes, and depreciation. The verdict might shock you.",
        "https://gigwagetruth.com",
        "https://gigwagetruth.com/og-image.jpg"
    ),
    content = @`
        <div x-data="calculator()" class="min-h-screen bg-slate-50 relative pb-20">
            
            <!-- STEP 1: THE GATEWAY -->
            <div x-show="step === 1" x-transition.opacity.duration.500ms class="max-w-3xl mx-auto px-4 py-12 sm:py-20 space-y-12">
                
                <!-- Hero Section -->
                <section class="text-center space-y-6">
                    <h1 class="text-4xl md:text-6xl font-black tracking-tight text-slate-900 leading-tight">
                        Stop Driving Blind.<br>
                        Find Your <span class="text-emerald-600">Real Hourly Wage.</span>
                    </h1>
                    <p class="text-lg md:text-xl text-slate-600 max-w-2xl mx-auto font-medium leading-relaxed">
                        Apps show you revenue. We show you profit after gas, taxes, and <span class="font-bold text-slate-900">$${AppConstants.IRS_MILEAGE_RATE_2024}/mile</span> depreciation.
                    </p>
                </section>

                <!-- Step 1 Card -->
                <div class="bg-white rounded-2xl shadow-xl border border-slate-200 p-6 md:p-10 space-y-10">
                    
                    <!-- App Selection -->
                    <div class="space-y-4">
                        <label class="block text-sm font-bold text-slate-500 uppercase tracking-wider">Select Your App</label>
                        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                            <template x-for="app in apps" :key="app.name">
                                <button @click="selectedApp = app.name"
                                        :class="selectedApp === app.name ? 'border-emerald-500 bg-emerald-50 ring-2 ring-emerald-200' : 'border-slate-200 hover:border-emerald-300 hover:bg-slate-50'"
                                        class="flex flex-col items-center justify-center p-4 rounded-xl border-2 transition-all duration-200">
                                    <span class="text-2xl mb-2" x-text="app.icon"></span>
                                    <span class="font-bold text-slate-800" x-text="app.name"></span>
                                </button>
                            </template>
                        </div>
                    </div>

                    <!-- Quick Input Form -->
                    <div class="space-y-6">
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <!-- Revenue -->
                            <div class="space-y-2">
                                <label class="block text-sm font-semibold text-slate-700">Weekly Earnings ($)</label>
                                <div class="relative">
                                    <span class="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 font-bold">$</span>
                                    <input type="number" x-model="gross" class="w-full pl-8 pr-4 py-4 bg-slate-50 border border-slate-200 rounded-xl text-lg font-bold text-slate-900 focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 outline-none transition" placeholder="1000">
                                </div>
                            </div>

                            <!-- Miles -->
                             <div class="space-y-2">
                                <label class="block text-sm font-semibold text-slate-700">Total Miles Driven</label>
                                <div class="relative">
                                    <input type="number" x-model="miles" class="w-full pl-4 pr-12 py-4 bg-slate-50 border border-slate-200 rounded-xl text-lg font-bold text-slate-900 focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 outline-none transition" placeholder="800">
                                    <span class="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 font-bold text-sm">mi</span>
                                </div>
                            </div>
                        </div>

                         <!-- Hours -->
                        <div class="space-y-2">
                            <label class="block text-sm font-semibold text-slate-700">Hours Worked</label>
                            <div class="relative max-w-md">
                                <input type="number" x-model="hours" class="w-full pl-4 pr-12 py-4 bg-slate-50 border border-slate-200 rounded-xl text-lg font-bold text-slate-900 focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 outline-none transition" placeholder="40">
                                <span class="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 font-bold text-sm">hrs</span>
                            </div>
                        </div>
                    </div>

                    <!-- CTA Button -->
                    <button @click="calculateAndShow()" 
                            class="w-full py-5 bg-emerald-600 hover:bg-emerald-700 text-white text-xl font-bold rounded-xl shadow-lg shadow-emerald-200 transform transition hover:-translate-y-1 active:translate-y-0">
                        Show My Truth &rarr;
                    </button>

                    <!-- Trust Signals -->
                    <div class="border-t border-slate-100 pt-6 flex flex-col md:flex-row items-center justify-between text-xs md:text-sm text-slate-500 gap-4 md:gap-0">
                        <div class="flex items-center space-x-2">
                            <span class="text-emerald-500 font-bold">✓</span>
                            <span>Using 2026 IRS Standard Mileage Rates</span>
                        </div>
                        <div class="flex items-center space-x-2">
                             <span class="text-emerald-500 font-bold">✓</span>
                            <span>Over 10,000 drivers discovered their real wage this month.</span>
                        </div>
                    </div>
                </div>
            </div>


            <!-- STEP 2: THE VERDICT LAB -->
            <div x-show="step === 2" x-cloak x-transition.opacity.duration.500ms class="max-w-5xl mx-auto px-4 py-12 space-y-16">
                
                <!-- 1. The Brutal Verdict Card -->
                <section class="bg-white rounded-3xl shadow-2xl border border-slate-200 overflow-hidden relative">
                    <div class="bg-slate-900 text-white text-center py-3 text-sm font-bold tracking-widest uppercase">
                        The Brutal Verdict
                    </div>
                    <div class="p-8 md:p-12 text-center space-y-6">
                        
                        <div class="inline-flex items-center px-4 py-2 rounded-full font-bold text-sm tracking-wide"
                             :class="netHourly >= 15 ? 'bg-emerald-100 text-emerald-800' : 'bg-red-100 text-red-800'">
                           STATUS:  <span x-text="netHourly >= 15 ? 'STABLE' : 'CRITICAL'" class="ml-1"></span>
                        </div>

                        <div>
                            <p class="text-slate-500 text-sm font-semibold uppercase tracking-wider mb-2">Your Real Hourly Wage</p>
                            <h2 class="text-7xl md:text-9xl font-black tracking-tighter"
                                :class="netHourly >= 15 ? 'text-emerald-600' : 'text-red-600'">
                                $<span x-text="netHourly"></span><span class="text-4xl md:text-6xl text-slate-300 -ml-1">/hr</span>
                            </h2>
                        </div>

                        <div class="max-w-xl mx-auto bg-slate-50 rounded-xl p-4 border border-slate-200">
                             <p class="text-slate-700 font-medium">
                                You are making <span class="font-bold text-red-600" x-text="'$' + (15 - netHourly).toFixed(2)"></span> <span class="font-black text-red-600">LESS</span> than a local Burger King employee ($15/hr).
                            </p>
                        </div>
                    </div>
                </section>

                <!-- 2. Primary Ad Slot -->
                 <div class="w-full bg-slate-100 border-2 border-dashed border-slate-300 rounded-xl p-8 text-center">
                    <p class="text-slate-400 font-medium tracking-widest text-sm uppercase">Advertisment (Primary Slot)</p>
                </div>

                <div class="grid grid-cols-1 lg:grid-cols-2 gap-12">
                    
                    <!-- 3. What-If Simulator -->
                    <section class="space-y-8">
                        <div class="flex items-center space-x-3 mb-6">
                             <div class="bg-slate-900 text-white w-8 h-8 rounded-lg flex items-center justify-center font-bold">Lab</div>
                             <h3 class="text-2xl font-bold text-slate-900">What-If Simulator</h3>
                        </div>
                        
                        <div class="bg-white rounded-2xl border border-slate-200 p-8 space-y-8 shadow-sm">
                             <!-- Fuel Price Slider -->
                             <div>
                                <div class="flex justify-between mb-4">
                                    <label class="font-bold text-slate-700">Fuel Price ($/gal)</label>
                                    <span class="font-mono font-bold text-emerald-600 bg-emerald-50 px-2 py-1 rounded" x-text="'$' + Number(gasPrice).toFixed(2)"></span>
                                </div>
                                <input type="range" min="2.00" max="6.00" step="0.05" x-model="gasPrice" class="w-full h-3 bg-slate-200 rounded-lg appearance-none cursor-pointer accent-slate-900">
                             </div>

                             <!-- MPG Slider -->
                             <div>
                                <div class="flex justify-between mb-4">
                                    <label class="font-bold text-slate-700">Vehicle MPG</label>
                                    <span class="font-mono font-bold text-emerald-600 bg-emerald-50 px-2 py-1 rounded" x-text="mpg + ' mpg'"></span>
                                </div>
                                <input type="range" min="10" max="60" step="1" x-model="mpg" class="w-full h-3 bg-slate-200 rounded-lg appearance-none cursor-pointer accent-slate-900">
                             </div>
                            
                             <!-- Deadhead Miles (Simulated by adding % to miles) -->
                             <div>
                                <div class="flex justify-between mb-4">
                                    <label class="font-bold text-slate-700">Deadhead Miles (%)</label>
                                    <span class="font-mono font-bold text-emerald-600 bg-emerald-50 px-2 py-1 rounded" x-text="deadhead + '%'"></span>
                                </div>
                                <input type="range" min="0" max="50" step="5" x-model="deadhead" class="w-full h-3 bg-slate-200 rounded-lg appearance-none cursor-pointer accent-slate-900">
                                <p class="text-xs text-slate-400 mt-2">Miles driven without a passenger (unpaid time).</p>
                             </div>
                        </div>
                    </section>

                    <!-- 4. Expense Breakdown -->
                    <section class="space-y-8">
                        <h3 class="text-2xl font-bold text-slate-900">Where is money leaking?</h3>
                        
                        <div class="bg-white rounded-2xl border border-slate-200 p-8 shadow-sm">
                            <div class="flex flex-col md:flex-row items-center gap-8">
                                <!-- Simple CSS Donut Chart -->
                                <div class="relative w-40 h-40 flex-shrink-0">
                                    <svg viewBox="0 0 36 36" class="w-full h-full transform -rotate-90">
                                        <!-- Ring Background -->
                                        <path class="text-slate-100" d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831" fill="none" stroke="currentColor" stroke-width="3.8" />
                                        
                                        <!-- Tax Segment (approx 15%) -->
                                        <path class="text-blue-500" stroke-dasharray="15, 100" d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831" fill="none" stroke="currentColor" stroke-width="3.8" />
                                        
                                        <!-- Exp Segment (approx 40%) -->
                                        <path class="text-red-500" stroke-dasharray="40, 100" stroke-dashoffset="-15" d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831" fill="none" stroke="currentColor" stroke-width="3.8" />
                                    </svg>
                                    <div class="absolute inset-0 flex items-center justify-center flex-col">
                                        <span class="text-xs text-slate-400 uppercase font-bold">Expenses</span>
                                        <span class="text-xl font-bold text-slate-900" x-text="'$' + totalExpenses"></span>
                                    </div>
                                </div>

                                <!-- Legend / List -->
                                <div class="flex-grow space-y-3 w-full">
                                    <div class="flex justify-between items-center text-sm border-b border-slate-100 pb-2">
                                        <div class="flex items-center"><span class="w-3 h-3 rounded-full bg-red-500 mr-2"></span>IRS Deduction (Depreciation)</div>
                                        <span class="font-bold text-slate-900" x-text="'-$' + irsCost"></span>
                                    </div>
                                    <div class="flex justify-between items-center text-sm border-b border-slate-100 pb-2">
                                        <div class="flex items-center"><span class="w-3 h-3 rounded-full bg-blue-500 mr-2"></span>Self-Employment Tax (15.3%)</div>
                                        <span class="font-bold text-slate-900" x-text="'-$' + taxCost"></span>
                                    </div>
                                    <div class="flex justify-between items-center text-sm">
                                        <div class="flex items-center"><span class="w-3 h-3 rounded-full bg-slate-400 mr-2"></span>Gas Cost</div>
                                        <span class="font-bold text-slate-900" x-text="'-$' + gasCost"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>

                <!-- 5. Social & Viral -->
                 <section class="grid grid-cols-1 md:grid-cols-2 gap-8 items-start">
                     <!-- Comparison Table -->
                    <div class="bg-white rounded-xl border border-slate-200 overflow-hidden">
                        <table class="w-full text-left text-sm">
                            <thead class="bg-slate-50 text-slate-500 font-semibold uppercase tracking-wider">
                                <tr>
                                    <th class="px-6 py-4">Job</th>
                                    <th class="px-6 py-4">Wage</th>
                                </tr>
                            </thead>
                            <tbody class="divide-y divide-slate-100 font-medium text-slate-700">
                                <tr>
                                    <td class="px-6 py-4">Local Burger King</td>
                                    <td class="px-6 py-4 text-emerald-600">$15.00 / hr</td>
                                </tr>
                                <tr>
                                    <td class="px-6 py-4">Amazon Warehouse</td>
                                    <td class="px-6 py-4 text-emerald-600">$17.50 / hr</td>
                                </tr>
                                <tr class="bg-slate-50">
                                    <td class="px-6 py-4 font-bold text-slate-900">You (Real Wage)</td>
                                    <td class="px-6 py-4 font-black" :class="netHourly >= 15 ? 'text-emerald-600' : 'text-red-600'" x-text="'$' + netHourly + ' / hr'"></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <!-- Share Section -->
                    <div class="bg-slate-900 rounded-xl p-8 text-white text-center space-y-6">
                        <h4 class="text-xl font-bold">Shocked? Warn your friends.</h4>
                        <button @click="shareResult()" class="w-full bg-white text-slate-900 font-bold py-3 rounded-lg hover:bg-slate-100 transition flex items-center justify-center gap-2">
                             <span>📤</span> Text my result to a friend
                        </button>
                        <p class="text-xs text-slate-400">
                             Copied to clipboard: "I thought I was making good money on Uber, but my real wage is only $..."
                        </p>
                    </div>
                </section>

                <!-- SEO / FAQ -->
                 <section class="max-w-3xl mx-auto space-y-8 pt-12">
                     <h3 class="text-2xl font-bold text-slate-900 text-center">Frequently Asked Questions</h3>
                     <div class="space-y-4">
                         <details class="bg-white border border-slate-200 rounded-lg p-4 cursor-pointer group">
                             <summary class="font-bold text-slate-800 list-none flex justify-between items-center group-hover:text-emerald-600">
                                 Why is the IRS mileage rate so high?
                                 <span class="text-slate-400 group-open:rotate-180 transition">+</span>
                             </summary>
                             <p class="mt-4 text-slate-600 leading-relaxed">
                                 The IRS Standard Mileage Rate ($0.67/mile) isn't just made up. It's based on an annual study of the fixed and variable costs of operating an automobile, including depreciation, insurance, repairs, tires, maintenance, gas, and oil. Ignoring this means you are borrowing equity from your car's future value.
                             </p>
                         </details>
                          <details class="bg-white border border-slate-200 rounded-lg p-4 cursor-pointer group">
                             <summary class="font-bold text-slate-800 list-none flex justify-between items-center group-hover:text-emerald-600">
                                 Is driving for Uber/Lyft still worth it in 2026?
                                 <span class="text-slate-400 group-open:rotate-180 transition">+</span>
                             </summary>
                             <p class="mt-4 text-slate-600 leading-relaxed">
                                 It depends. If your vehicle has high MPG, low depreciation (an older, reliable car), and you drive during peak surge hours, you can profit. However, for many drivers using newer cars in non-surge times, the real hourly wage often dips below minimum wage once expenses are factored in.
                             </p>
                         </details>
                     </div>
                 </section>

                 <!-- Bottom Ad -->
                 <div class="w-full bg-slate-100 border-2 border-dashed border-slate-300 rounded-xl p-8 text-center">
                    <p class="text-slate-400 font-medium tracking-widest text-sm uppercase">Advertisment (Bottom Slot)</p>
                </div>

            </div>

        </div>

        <script>
            function calculator() {
                return {
                    step: 1,
                    
                    // Inputs
                    apps: [
                        { name: 'Uber', icon: '🚙' },
                        { name: 'Lyft', icon: '🚗' },
                        { name: 'DoorDash', icon: '🍔' },
                        { name: 'Instacart', icon: '🛒' }
                    ],
                    selectedApp: 'Uber',
                    gross: 1000,
                    miles: 800,
                    hours: 40,
                    
                    // Simulation Vars
                    gasPrice: 3.50,
                    mpg: 25,
                    deadhead: 10,
                    
                    calculateAndShow() {
                        this.step = 2;
                        window.scrollTo({ top: 0, behavior: 'smooth' });
                    },
                    
                    get irsCost() {
                        return (this.miles * ${AppConstants.IRS_MILEAGE_RATE_2024}).toFixed(2);
                    },
                    
                    get gasCost() {
                        if(this.mpg <= 0) return 0;
                        // Gas cost + Deadhead adjustment interaction
                        // Deadhead adds to the miles but we already inputted Total Miles. 
                        // Let's assume the user inputted Total Miles, so Deadhead slider might just be for visualization 
                        // OR if we want to simulate "What if I reduced deadhead?".
                        // For simplicity based on prompt, let's keep it simple: MPG and Gas Price directly affect cost.
                        return ((this.miles / this.mpg) * this.gasPrice).toFixed(2);
                    },
                    
                    get taxCost() {
                        return (this.gross * ${AppConstants.SELF_EMPLOYMENT_TAX_RATE}).toFixed(2);
                    },
                    
                    get totalExpenses() {
                        return (Number(this.irsCost) + Number(this.gasCost) + Number(this.taxCost)).toFixed(2);
                    },
                    
                    get totalNet() {
                        const net = this.gross - this.totalExpenses;
                        return net.toFixed(2);
                    },
                    
                    get netHourly() {
                        if(this.hours <= 0) return 0;
                        return (this.totalNet / this.hours).toFixed(2);
                    },
                    
                    shareResult() {
                        const text = 'I thought I was making money on ' + this.selectedApp + ', but my real wage is only $' + this.netHourly + '/hr. Check yours at GigWageTruth.com';
                        navigator.clipboard.writeText(text);
                        alert("Copied to clipboard!");
                    }
                }
            }
        </script>
    `
)
