@import com.gigwager.model.SeoMeta
@import com.gigwager.util.AppConstants

@param Double initialGross
@param Double initialMiles  
@param Double initialHours
@param String customTitle

@template.layout.main(
    seoMeta = new SeoMeta(
        customTitle != null ? customTitle : "My Real Hourly Wage - The Shocking Truth",
        "See exactly how much I make after gas, taxes, and depreciation. The number will surprise you.",
        "https://gigverdict.com/uber",
        "https://gigverdict.com/og-image.jpg"
    ),
    content = @`
        <script src="/js/calculator-core.js?v=${AppConstants.CACHE_VERSION}"></script>
        
        <script>
            function calculator() {
                return window.createGigCalculator({
                    appName: 'Uber',
                    gross: '${initialGross}',
                    miles: '${initialMiles}',
                    hours: '${initialHours}',
                    defaultVehicle: 'prius',
                    targetWeeklyIncome: 1000, 
                    shockMessages: {
                        tier1: [
                             { emoji: '💀', text: "You're making LESS than a Target cashier ($15/hr)" },
                             { emoji: '🩸', text: "Bleeding money after car replacement costs" },
                             { emoji: '👮', text: "Illegal wage level in most industries" }
                        ],
                         tier2: [
                             { emoji: '⚠️', text: "Below minimum wage in CA/NY (after expenses)" },
                             { emoji: '🍔', text: "Flipping burgers pays more + has benefits" },
                             { emoji: '📉', text: "Not sustainable long-term" }
                        ],
                        tier3: [
                             { emoji: '✅', text: "Decent side hustle money" },
                             { emoji: '🤔', text: "Comparable to warehouse work (no benefits)" },
                             { emoji: '🚗', text: "Okay if you drive a cheap reliable car" }
                        ],
                        tier4: [
                             { emoji: '👑', text: "Top 10% Driver! You know your market." },
                             { emoji: '🚀', text: "Great profit margin!" },
                             { emoji: '🔥', text: "Crushing it!" }
                        ]
                    }
                });
            }
        </script>
        
        <!-- Main Application Wrapper (x-data hoisted) -->
        <div x-data="calculator()">
        
            <!-- Hero Section (Dynamic) -->
            <div class="bg-slate-900 pt-16 pb-24 text-center px-6">
                <h1 class="text-3xl md:text-5xl font-bold text-white mb-6 tracking-tight">
                    <span x-text="heroTitle"></span>
                    <span class="text-emerald-400" x-show="!heroTitle.includes('Week')">(2026)</span>
                </h1>
                <p class="text-lg md:text-xl text-slate-300 max-w-2xl mx-auto" x-html="heroSubtitle">
                </p>
            </div>

            <!-- Calculator UI Container (Calculator floats over Hero) -->
            <div class="bg-slate-50 pb-24 relative z-10 -mt-10 rounded-t-3xl border-t border-slate-200">
            
            <!-- Mobile Sticky Header -->
            <div class="sm:hidden sticky top-0 bg-white/95 backdrop-blur-md border-b border-slate-200 z-50 rounded-t-3xl">
                <div class="px-6 py-4 flex items-center justify-between">
                     <div class="flex items-center gap-2">
                        <div class="w-2 h-2 rounded-full" :class="netHourly < 15 ? 'bg-red-500' : 'bg-green-500'"></div>
                        <span class="text-xs font-bold uppercase tracking-wider text-slate-500">Real Wage</span>
                    </div>
                     <span class="text-xl font-bold" :class="netHourly < 0 ? 'text-red-600' : 'text-slate-900'">
                    </span>
                    <!-- Mobile Share Icon -->
                    <button @click="shareResult()" class="ml-4 p-2 text-slate-400 hover:text-slate-900">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.684 13.342C8.886 12.938 9 12.482 9 12c0-.482-.114-.938-.316-1.342m0 2.684a3 3 0 110-2.684m0 2.684l6.632 3.316m-6.632-6l6.632-3.316m0 0a3 3 0 105.367-2.684 3 3 0 00-5.367 2.684zm0 9.316a3 3 0 105.368 2.684 3 3 0 00-5.368-2.684z"></path></svg>
                    </button>
                </div>
            </div>

            <!-- Desktop Sticky Summary Bar -->
            <div class="hidden sm:block sticky top-0 bg-white/95 backdrop-blur-md border-b border-slate-200 z-40 transition-all duration-300 shadow-sm">
                <div class="max-w-6xl mx-auto px-6 py-4">
                    <div class="flex items-center justify-between">
                        <div class="flex items-center gap-6">
                            <span class="text-sm font-medium text-slate-500 uppercase tracking-widest">Live Estimate</span>
                            <div class="flex items-baseline gap-2">
                                <span class="text-3xl font-bold tracking-tight" :class="netHourly < 0 ? 'text-red-600' : 'text-slate-900'">
                                    $<span x-text="netHourly.toFixed(2)"></span>
                                </span>
                                <span class="text-sm font-medium text-slate-400">/hr</span>
                            </div>
                            <div class="h-6 w-px bg-slate-200"></div>
                            <div class="flex items-center gap-2 px-3 py-1 rounded-full bg-slate-100">
                                <span class="text-sm font-medium" :class="netHourly < 15 ? 'text-red-600' : 'text-emerald-700'">
                                    <span x-text="shockMessage.emoji"></span>
                                    <span x-text="shockMessage.text"></span>
                                </span>
                            </div>
                        </div>
                    <div class="flex items-center gap-3">
                        <!-- Twitter Button (Desktop) -->
                        <button @click="tweetResult()" class="p-2.5 text-slate-400 hover:text-[#1DA1F2] hover:bg-slate-50 rounded-lg transition" title="Post to X">
                             <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24"><path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z"/></svg>
                        </button>
                        <!-- Main Share Button -->
                        <button @click="shareResult()" class="flex items-center gap-2 px-5 py-2.5 text-sm font-semibold text-white bg-slate-900 hover:bg-slate-800 rounded-lg transition shadow-lg shadow-slate-900/20">
                            <span x-text="shareBtnLabel">Share Result</span>
                            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.684 13.342C8.886 12.938 9 12.482 9 12c0-.482-.114-.938-.316-1.342m0 2.684a3 3 0 110-2.684m0 2.684l6.632 3.316m-6.632-6l6.632-3.316m0 0a3 3 0 105.367-2.684 3 3 0 00-5.367 2.684zm0 9.316a3 3 0 105.368 2.684 3 3 0 00-5.368-2.684z"></path></svg>
                        </button>
                    </div>
                    </div>
                </div>
            </div>

            <!-- Main Grid Layout -->
            <div class="max-w-6xl mx-auto px-6 py-12">
                <div class="grid lg:grid-cols-12 gap-12">
                    
                    <!-- Left: Control Panel (Inputs) -->
                    <div class="lg:col-span-8 space-y-8">

                        <!-- Main Inputs Card -->
                        <div class="bg-white rounded-2xl p-6 md:p-8 shadow-sm border border-slate-200">
                             
                             <!-- App Switcher Tabs -->
                             <div class="flex p-1 bg-slate-100 rounded-xl mb-8">
                                <button class="flex-1 py-3 text-sm font-bold rounded-lg shadow-sm bg-white text-slate-900 flex items-center justify-center gap-2">
                                    <span>🚗</span> Uber / Lyft
                                </button>
                                <a href="/" class="flex-1 py-3 text-sm font-bold rounded-lg text-slate-500 hover:text-slate-900 hover:bg-white/50 transition flex items-center justify-center gap-2">
                                    <span>🍔</span> DoorDash
                                </a>
                             </div>

                             <h3 class="text-lg font-bold text-slate-900 mb-6 flex items-center gap-2">
                                <span class="flex items-center justify-center w-6 h-6 rounded-full bg-slate-100 text-xs text-slate-600">1</span>
                                Enter Your Weekly Stats
                            </h3>
                            <div class="grid md:grid-cols-3 gap-6">
                                <!-- Gross Income -->
                                <div class="space-y-2">
                                    <div class="flex justify-between items-end h-5">
                                        <label class="text-xs font-semibold uppercase tracking-wide text-slate-500 leading-none">Total Pay ($)</label>
                                    </div>
                                    <div class="relative">
                                        <span class="absolute left-4 top-3.5 text-slate-400 font-medium">$</span>
                                        <input type="number" x-model="rawGross" 
                                               class="w-full pl-8 pr-4 py-3 bg-slate-50 border border-slate-200 rounded-xl text-lg font-bold text-slate-900 focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 focus:outline-none transition-all">
                                    </div>
                                </div>
                                <!-- Miles Driven -->
                                <div class="space-y-2">
                                    <div class="flex justify-between items-end h-5">
                                        <label class="text-xs font-semibold uppercase tracking-wide text-slate-500 leading-none">Miles Driven</label>
                                        <label class="flex items-center gap-2 cursor-pointer group leading-none">
                                            <span class="text-xs text-slate-400 group-hover:text-emerald-600 transition">Round Trip x2</span>
                                            <input type="checkbox" x-model="isRoundTrip" class="rounded border-slate-300 text-emerald-600 focus:ring-emerald-500 w-3.5 h-3.5">
                                        </label>
                                    </div>
                                    <div class="relative">
                                        <input type="number" x-model="rawMiles" 
                                               class="w-full pl-4 pr-12 py-3 bg-slate-50 border border-slate-200 rounded-xl text-lg font-bold text-slate-900 focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 focus:outline-none transition-all">
                                        <span class="absolute right-4 top-3.5 text-slate-400 font-medium text-sm">mi</span>
                                    </div>
                                </div>
                                <!-- Hours Worked -->
                                <div class="space-y-2">
                                    <div class="flex justify-between items-end h-5">
                                        <label class="text-xs font-semibold uppercase tracking-wide text-slate-500 leading-none">Total Time Online</label>
                                    </div>
                                    <div class="relative">
                                        <input type="number" x-model="rawHours" 
                                               class="w-full pl-4 pr-12 py-3 bg-slate-50 border border-slate-200 rounded-xl text-lg font-bold text-slate-900 focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 focus:outline-none transition-all">
                                        <span class="absolute right-4 top-3.5 text-slate-400 font-medium text-sm">hrs</span>
                                    </div>
                                    <p class="text-[10px] text-slate-400 leading-tight">
                                        ⚠️ Enter full shift duration (Log on to Log off). <br>
                                        Do <strong>not</strong> use "Active Time" (it flatters the app, not you).
                                    </p>
                                </div>
                            </div>
                        </div>

                        <!-- Weekly Goal (Restored for stickiness) -->
                        <div class="bg-white rounded-2xl p-6 md:p-8 shadow-sm border border-slate-200">
                            <div @click="showTargetHustle = !showTargetHustle" class="flex justify-between items-center cursor-pointer group">
                                <h3 class="text-lg font-bold text-slate-900 flex items-center gap-2">
                                    <span class="flex items-center justify-center w-6 h-6 rounded-full bg-slate-100 text-xs text-slate-600">2</span>
                                    Set Weekly Goal
                                </h3>
                                <svg class="w-5 h-5 text-slate-400 group-hover:text-emerald-500 transition-transform" :class="{'rotate-180': showTargetHustle}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                                </svg>
                            </div>
                            
                            <div x-show="showTargetHustle" x-collapse class="mt-6 border-t border-slate-100 pt-6">
                                <div class="grid md:grid-cols-2 gap-6">
                                    <div>
                                        <label class="block text-xs font-semibold uppercase tracking-wide text-slate-500 mb-2">Target Weekly Income</label>
                                        <div class="relative">
                                            <span class="absolute left-4 top-3.5 text-slate-400 font-medium">$</span>
                                            <input type="number" x-model="rawTargetWeeklyIncome" 
                                                class="w-full pl-8 pr-4 py-3 bg-slate-50 border border-slate-200 rounded-xl font-bold text-slate-900 focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500">
                                        </div>
                                    </div>
                                    
                                    <!-- Progress/Motivation Display -->
                                    <div class="bg-slate-50 rounded-xl p-4 flex flex-col justify-center">
                                        <template x-if="targetWeeklyIncome > 0">
                                            <div>
                                                <div class="flex justify-between text-xs font-semibold uppercase text-slate-500 mb-1">
                                                    <span>Progress</span>
                                                    <span x-text="Math.min(100, Math.round((totalNet / targetWeeklyIncome) * 100)) + '%'"></span>
                                                </div>
                                                <div class="w-full bg-slate-200 rounded-full h-2.5 mb-2">
                                                    <div class="bg-emerald-500 h-2.5 rounded-full transition-all duration-500" :style="'width: ' + Math.min(100, (totalNet / targetWeeklyIncome) * 100) + '%'"></div>
                                                </div>
                                                <p class="text-sm text-slate-600" x-text="(targetWeeklyIncome - totalNet) > 0 ? '$' + (targetWeeklyIncome - totalNet).toFixed(0) + ' (' + hoursToGoal.toFixed(1) + ' hrs) to go!' : 'Goal smashed! 🎉'"></p>
                                            </div>
                                        </template>
                                        <template x-if="targetWeeklyIncome <= 0">
                                            <p class="text-sm text-slate-400 text-center">Set a goal to track progress</p>
                                        </template>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Vehicle Settings -->
                        <div class="bg-white rounded-2xl p-6 md:p-8 shadow-sm border border-slate-200">
                            <h3 class="text-lg font-bold text-slate-900 mb-6 flex items-center gap-2">
                                <span class="flex items-center justify-center w-6 h-6 rounded-full bg-slate-100 text-xs text-slate-600">3</span>
                                Vehicle Expenses
                            </h3>
                            
                            <!-- Calculation Mode Tabs -->
                            <div class="flex p-1 bg-slate-100 rounded-xl mb-6">
                                <button @click="setMode('standard')" 
                                        class="flex-1 py-2.5 text-sm font-semibold rounded-lg transition-all"
                                        :class="calculationMode === 'standard' ? 'bg-white text-slate-900 shadow-sm' : 'text-slate-500 hover:text-slate-700'">
                                    IRS Standard (Easy)
                                </button>
                                <button @click="setMode('advanced')" 
                                        class="flex-1 py-2.5 text-sm font-semibold rounded-lg transition-all"
                                        :class="calculationMode === 'advanced' ? 'bg-white text-slate-900 shadow-sm' : 'text-slate-500 hover:text-slate-700'">
                                    My Car (Precise)
                                </button>
                            </div>

                             <div x-show="calculationMode === 'standard'" class="text-center py-4">
                                <p class="text-3xl font-bold text-slate-900 mb-1">$0.725<span class="text-lg text-slate-400 font-medium">/mi</span></p>
                                <p class="text-sm text-slate-500">2026 IRS Standard Deduction Rate</p>
                                <p class="text-xs text-slate-400 mt-2 max-w-sm mx-auto">Includes gas, depreciation, insurance, and maintenance. Trusted by tax pros.</p>
                            </div>

                            <div x-show="calculationMode === 'advanced'" class="space-y-6">
                                <div>
                                    <label class="block text-xs font-semibold uppercase tracking-wide text-slate-500 mb-2">Select Vehicle</label>
                                    <select x-model="selectedVehicleId" 
                                            class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-white text-base font-medium focus:outline-none focus:ring-2 focus:ring-slate-900">
                                        <template x-for="vehicle in vehiclePresets.filter(v => v.id !== 'irs-standard' && v.type !== 'bike' && v.type !== 'walker')" :key="vehicle.id">
                                            <option :value="vehicle.id" x-text="vehicle.icon + ' ' + vehicle.name"></option>
                                        </template>
                                    </select>
                                </div>
                                
                                <!-- Common Gas Input for ALL Gas/Hybrid Vehicles -->
                                <div class="grid grid-cols-2 gap-6" x-show="selectedVehicle && selectedVehicle.type !== 'electric'">
                                    <div x-show="selectedVehicleId === 'custom'">
                                        <label class="block text-xs font-semibold uppercase tracking-wide text-slate-500 mb-2">MPG</label>
                                        <input type="number" x-model="rawCustomMpg" 
                                               class="w-full px-4 py-3 rounded-xl border border-slate-200 bg-slate-50">
                                    </div>
                                    <div class="col-span-2 md:col-span-1">
                                        <label class="block text-xs font-semibold uppercase tracking-wide text-slate-500 mb-2">Gas ($/gal)</label>
                                        <div class="relative">
                                             <span class="absolute left-4 top-3.5 text-slate-400 font-medium">$</span>
                                            <input type="number" x-model="rawGasPrice" step="0.01" 
                                                   class="w-full pl-8 pr-4 py-3 bg-white border border-slate-300 rounded-xl focus:ring-2 focus:ring-emerald-500 font-bold text-slate-900">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Advanced Options (Wait Time, Tips, Bonuses) -->
                        <div class="bg-white rounded-2xl p-6 md:p-8 shadow-sm border border-slate-200">
                            <div @click="showAdvancedOptions = !showAdvancedOptions" class="flex justify-between items-center cursor-pointer group">
                                <h3 class="text-base font-bold text-slate-900 flex items-center gap-2">
                                    Advanced Options
                                    <span class="text-xs font-normal text-slate-400">(Tips, Wait Time)</span>
                                </h3>
                                <svg class="w-5 h-5 text-slate-400 group-hover:text-emerald-500 transition-transform" :class="{'rotate-180': showAdvancedOptions}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                                </svg>
                            </div>

                            <div x-show="showAdvancedOptions" x-collapse class="mt-6 border-t border-slate-100 pt-6">
                                <div class="grid md:grid-cols-2 gap-6">
                                    <div>
                                        <label class="block text-xs font-semibold uppercase tracking-wide text-slate-500 mb-2">Unpaid Wait Time (hrs)</label>
                                        <div class="relative">
                                            <input type="number" x-model="rawWaitTime" 
                                                class="w-full pr-12 pl-4 py-3 bg-slate-50 border border-slate-200 rounded-xl font-bold text-slate-900 focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500">
                                            <span class="absolute right-4 top-3.5 text-slate-400 font-medium text-sm">hrs</span>
                                        </div>
                                        <p class="text-[10px] text-slate-400 mt-1">Time spent waiting for pings.</p>
                                    </div>
                                    <div>
                                        <label class="block text-xs font-semibold uppercase tracking-wide text-slate-500 mb-2">Cash Tips / Bonuses</label>
                                        <div class="relative">
                                            <span class="absolute left-4 top-3.5 text-slate-400 font-medium">$</span>
                                            <input type="number" x-model="rawTips" 
                                                class="w-full pl-8 pr-4 py-3 bg-slate-50 border border-slate-200 rounded-xl font-bold text-slate-900 focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500">
                                        </div>
                                        <p class="text-[10px] text-slate-400 mt-1">Extra earnings not in app total.</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>

                    <!-- Right: Results Sidebar -->
                    <div class="lg:col-span-4 space-y-6">
                        
                        <!-- Breakdown Card -->
                        <div class="bg-white rounded-2xl p-6 shadow-sm border border-slate-200 sticky top-24">
                            <h3 class="text-base font-bold text-slate-900 mb-6">Weekly Breakdown</h3>
                            
                            <div class="space-y-4">
                                <div class="flex justify-between items-center py-3 border-b border-slate-100">
                                    <span class="text-slate-600">Gross Pay</span>
                                    <span class="font-bold text-slate-900">$<span x-text="gross.toFixed(2)"></span></span>
                                </div>
                                
                                <!-- Split Expenses -->
                                <template x-if="calculationMode !== 'standard' && gasCost > 0">
                                    <div class="flex justify-between items-center py-2 border-b border-slate-50">
                                        <span class="text-sm text-slate-500">⛽ Gas Cost</span>
                                        <span class="font-bold text-red-500">-$<span x-text="gasCost.toFixed(2)"></span></span>
                                    </div>
                                </template>
                                <template x-if="calculationMode !== 'standard'">
                                    <div class="flex justify-between items-center py-2 border-b border-slate-100">
                                        <div class="flex flex-col">
                                            <span class="text-sm text-slate-500">📉 Wear & Depr.</span>
                                        </div>
                                        <span class="font-bold text-red-400">-$<span x-text="otherCost.toFixed(2)"></span></span>
                                    </div>
                                </template>
                                <template x-if="calculationMode === 'standard'">
                                    <div class="flex justify-between items-center py-3 border-b border-slate-100">
                                        <div class="flex flex-col">
                                            <span class="text-slate-600">Mileage Deduct.</span>
                                            <span class="text-xs text-slate-400">IRS Std. Rate</span>
                                        </div>
                                        <span class="font-bold text-red-500">-$<span x-text="totalDeduction.toFixed(2)"></span></span>
                                    </div>
                                </template>

                                <div class="flex justify-between items-center py-3 border-b border-slate-100">
                                    <div class="flex flex-col">
                                        <span class="text-slate-600">Est. Taxes</span>
                                        <span class="text-xs text-slate-400">15.3% SE Tax</span>
                                    </div>
                                    <span class="font-bold text-red-500">-$<span x-text="taxCost.toFixed(2)"></span></span>
                                </div>
                                <div class="flex justify-between items-center pt-4">
                                    <span class="text-lg font-bold text-slate-900">Net Profit</span>
                                    <span class="text-2xl font-bold text-emerald-600">$<span x-text="totalNet.toFixed(2)"></span></span>
                                </div>
                            </div>

                            <div class="mt-8 bg-slate-50 rounded-xl p-4 border border-slate-200">
                                <div class="flex justify-between items-center mb-1">
                                    <span class="text-xs font-bold uppercase text-slate-500">Real Hourly Wage</span>
                                </div>
                                <div class="flex items-baseline gap-1">
                                    <span class="text-4xl font-bold text-slate-900">$<span x-text="netHourly.toFixed(2)"></span></span>
                                    <span class="text-sm font-medium text-slate-400">/hr</span>
                                </div>
                            </div>

                        </div>
                    </div>
                    
                </div>
            </div>

            <!-- SSR Content Section (Analysis & FAQ) -->
            <article class="max-w-4xl mx-auto px-6 mt-12 space-y-16">
                
                <!-- Verdict / Context -->
                <section>
                    <h2 class="text-2xl font-bold text-slate-900 mb-4">Why is your real wage so low?</h2>
                    <div class="prose prose-slate max-w-none text-slate-600">
                        <p>
                            Many Uber drivers are shocked to see their "real" hourly wage drop below minimum wage. 
                            The culprit is usually <strong>hidden vehicle costs</strong>.
                        </p>
                        <p>
                            When you drive for Uber, you are borrowing equity from your car's future value. 
                            <strong>Depreciation</strong> costs about $0.15 per mile for the average sedan. 
                            Add gas ($0.10-$0.15/mi) and maintenance ($0.09/mi), and your operating costs can easily exceed $0.35 per mile.
                        </p>
                        <p>
                            This calculator uses the <strong>2026 IRS Standard Mileage Rate of $0.725/mile</strong> as a baseline, 
                            which includes all these factors plus insurance. This is the most accurate way to understand your true "take-home" pay.
                        </p>
                    </div>
                </section>

                <!-- FAQ Section -->
                <section>
                    <h2 class="text-2xl font-bold text-slate-900 mb-6">Frequently Asked Questions</h2>
                    <div class="space-y-4">
                        
                        <!-- FAQ Item 1 -->
                        <div x-data="{ open: false }" class="border border-slate-200 rounded-xl bg-white overflow-hidden">
                            <button @click="open = !open" class="flex justify-between items-center w-full px-6 py-4 text-left hover:bg-slate-50 transition">
                                <span class="font-semibold text-slate-900">What is the IRS Standard Mileage Rate for 2026?</span>
                                <svg class="w-5 h-5 text-slate-400 transition-transform" :class="{'rotate-180': open}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                                </svg>
                            </button>
                            <div x-show="open" class="px-6 pb-4 text-slate-600">
                                <p>The 2026 IRS Standard Mileage Rate is projected to be <strong>67 cents to 72.5 cents per mile</strong>. This rate covers gas, insurance, wear and tear, and depreciation. We use $0.725/mile in our Standard calculation to be safe.</p>
                            </div>
                        </div>

                        <!-- FAQ Item 2 -->
                        <div x-data="{ open: false }" class="border border-slate-200 rounded-xl bg-white overflow-hidden">
                            <button @click="open = !open" class="flex justify-between items-center w-full px-6 py-4 text-left hover:bg-slate-50 transition">
                                <span class="font-semibold text-slate-900">Is Uber driving worth it in 2026?</span>
                                <svg class="w-5 h-5 text-slate-400 transition-transform" :class="{'rotate-180': open}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                                </svg>
                            </button>
                            <div x-show="open" class="px-6 pb-4 text-slate-600">
                                <p>It depends on your vehicle efficiency. If you drive a hybrid (50+ MPG) and work during peak surge hours, you can earn $25-30/hr net. However, driving a gas-guzzling SUV often results in net earnings below $10/hr.</p>
                            </div>
                        </div>

                        <!-- FAQ Item 3 -->
                        <div x-data="{ open: false }" class="border border-slate-200 rounded-xl bg-white overflow-hidden">
                            <button @click="open = !open" class="flex justify-between items-center w-full px-6 py-4 text-left hover:bg-slate-50 transition">
                                <span class="font-semibold text-slate-900">Do I really need to pay taxes on Uber income?</span>
                                <svg class="w-5 h-5 text-slate-400 transition-transform" :class="{'rotate-180': open}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                                </svg>
                            </button>
                            <div x-show="open" class="px-6 pb-4 text-slate-600">
                                <p>Yes. Uber treats you as an independent contractor. You must pay roughly <strong>15.3% Self-Employment Tax</strong> on your profit. This calculator estimates that tax for you so you can save accordingly.</p>
                            </div>
                        </div>

                    </div>
                </section>
                
                <!-- Methodology Snapshot (Anti-Low Value Content) -->
                <section class="bg-white border border-slate-200 rounded-2xl p-8 shadow-sm mb-12">
                    <h3 class="text-xl font-bold text-slate-900 mb-4 flex items-center gap-2">
                        <svg class="w-6 h-6 text-emerald-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                        Methodology: How We Calculate Real Pay
                    </h3>
                    <div class="prose prose-sm text-slate-600 max-w-none">
                        <p>
                            Most earning calculators are misleading because they only subtract gas. GigWageTruth uses a comprehensive <strong>Total Cost of Ownership (TCO)</strong> model.
                        </p>
                        <ul class="grid md:grid-cols-2 gap-4 mt-4">
                            <li class="flex items-start gap-2">
                                <span class="font-bold text-slate-900 min-w-[4rem]">Revenue:</span>
                                <span>Your gross payout from Uber (fares + tips + bonuses).</span>
                            </li>
                            <li class="flex items-start gap-2">
                                <span class="font-bold text-slate-900 min-w-[4rem]">Expenses:</span>
                                <span>We use the <strong>2026 IRS Standard Mileage Rate ($0.725/mi)</strong> unless you select a custom vehicle. This covers gas, insurance, tires, maintenance, and depreciation.</span>
                            </li>
                            <li class="flex items-start gap-2">
                                <span class="font-bold text-slate-900 min-w-[4rem]">Taxes:</span>
                                <span>We estimate the <strong>15.3% Self-Employment Tax</strong> on your net profit. Note: This does not include Federal/State income tax, which varies by person.</span>
                            </li>
                            <li class="flex items-start gap-2">
                                <span class="font-bold text-slate-900 min-w-[4rem]">Net Hourly:</span>
                                <span>(Revenue - Expenses - Taxes) / Total Hours Online. This is your true "take-home" wage.</span>
                            </li>
                        </ul>
                    </div>
                </section>

                <!-- CTA / Link to Blog -->
                <div class="bg-emerald-50 rounded-2xl p-8 text-center mt-12 mb-12">
                     <h3 class="text-xl font-bold text-emerald-900 mb-2">Stop driving blind</h3>
                     <p class="text-emerald-700 mb-6">Learn professional strategies to double your hourly wage.</p>
                     <a href="/blog/multi-apping-guide" class="inline-block px-6 py-3 bg-emerald-600 text-white font-semibold rounded-xl hover:bg-emerald-700 transition shadow-lg shadow-emerald-600/20">
                        Read Multi-Apping Guide
                     </a>
                </div>

            </article>

            <!-- Structured Data (JSON-LD) -->
            <script type="application/ld+json">
            {
              "@context": "https://schema.org",
              "@type": "WebApplication",
              "name": "Real Uber Driver Pay Calculator",
              "url": "https://gigverdict.com/uber",
              "applicationCategory": "FinanceApplication",
              "operatingSystem": "Any",
              "description": "Calculate your true net profit as an Uber driver after gas, depreciation, and taxes.",
              "offers": {
                "@type": "Offer",
                "price": "0",
                "priceCurrency": "USD"
              }
            }
            </script>
            <script type="application/ld+json">
            {
              "@context": "https://schema.org",
              "@type": "FAQPage",
              "mainEntity": [{
                "@type": "Question",
                "name": "What is the IRS Standard Mileage Rate for 2026?",
                "acceptedAnswer": {
                  "@type": "Answer",
                  "text": "The 2026 IRS Standard Mileage Rate is projected to be 67 cents to 72.5 cents per mile. This rate covers gas, insurance, wear and tear, and depreciation. We use $0.725/mile in our Standard calculation to be safe."
                }
              }, {
                "@type": "Question",
                "name": "Is Uber driving worth it in 2026?",
                "acceptedAnswer": {
                  "@type": "Answer",
                  "text": "It depends on your vehicle efficiency. If you drive a hybrid (50+ MPG) and work during peak surge hours, you can earn $25-30/hr net. However, driving a gas-guzzling SUV often results in net earnings below $10/hr."
                }
              }, {
                "@type": "Question",
                "name": "Do I really need to pay taxes on Uber income?",
                "acceptedAnswer": {
                  "@type": "Answer",
                  "text": "Yes. Uber treats you as an independent contractor. You must pay roughly 15.3% Self-Employment Tax on your profit. This calculator estimates that tax for you so you can save accordingly."
                }
              }]
            }
            </script>

            <!-- Viral Share Modal -->
            <div x-show="showShareModal" 
                 x-transition:enter="transition ease-out duration-200"
                 x-transition:enter-start="opacity-0"
                 x-transition:enter-end="opacity-100"
                 x-transition:leave="transition ease-in duration-150"
                 x-transition:leave-start="opacity-100"
                 x-transition:leave-end="opacity-0"
                 class="fixed inset-0 z-[60] flex items-center justify-center px-4" style="display: none;">
                
                <!-- Backdrop -->
                <div @click="showShareModal = false" class="absolute inset-0 bg-slate-900/60 backdrop-blur-sm"></div>
                
                <!-- Modal Content -->
                <div class="relative bg-white rounded-2xl shadow-2xl max-w-md w-full p-6 overflow-hidden">
                    <button @click="showShareModal = false" class="absolute top-4 right-4 text-slate-400 hover:text-slate-600">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
                    </button>

                    <div class="text-center mb-6">
                        <div class="inline-flex items-center justify-center w-12 h-12 rounded-full bg-emerald-100 text-emerald-600 mb-4">
                            <span class="text-2xl">📢</span>
                        </div>
                        <h3 class="text-xl font-bold text-slate-900">Share the Truth</h3>
                        <p class="text-slate-500 mt-1">Help other drivers see their real wage.</p>
                    </div>

                    <div class="space-y-3">
                        <!-- X / Twitter -->
                        <button @click="shareTo('twitter')" class="w-full flex items-center justify-center gap-3 px-4 py-3 bg-slate-900 hover:bg-slate-800 text-white rounded-xl font-semibold transition group">
                            <svg class="w-5 h-5 text-slate-400 group-hover:text-white transition" fill="currentColor" viewBox="0 0 24 24"><path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z"/></svg>
                            Post to X (Twitter)
                        </button>

                        <!-- Facebook -->
                        <button @click="shareTo('facebook')" class="w-full flex items-center justify-center gap-3 px-4 py-3 bg-[#1877F2] hover:bg-[#166fe5] text-white rounded-xl font-semibold transition">
                            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24"><path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.791-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/></svg>
                            Share on Facebook
                        </button>

                        <!-- Reddit -->
                        <button @click="shareTo('reddit')" class="w-full flex items-center justify-center gap-3 px-4 py-3 bg-[#FF4500] hover:bg-[#e03d00] text-white rounded-xl font-semibold transition">
                            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10"/><path fill="#fff" d="M16.67,13.13C18.04,14.06,19,15.32,19,17C19,19.76,15.87,22,12,22C8.13,22,5,19.76,5,17C5,15.32,5.96,14.06,7.33,13.13C7.33,13.13,7.33,13.13,7.33,13.13C7.14,12.57,7.21,11.97,7.56,11.45C7.99,10.82,8.83,10.66,9.46,11.08C9.55,11.14,9.63,11.21,9.7,11.29C10.37,11.15,11.12,11.07,11.9,11.07C11.93,11.07,11.97,11.07,12,11.07C12.78,11.07,13.53,11.15,14.2,11.29C14.28,11.21,14.36,11.14,14.44,11.08C15.07,10.66,15.91,10.82,16.34,11.45C16.69,11.97,16.76,12.57,16.57,13.13C16.67,13.13,16.67,13.13,16.67,13.13ZM12,20.47C14.97,20.47,17.38,18.91,17.38,17C17.38,15.09,14.97,13.53,12,13.53C9.03,13.53,6.62,15.09,6.62,17C6.62,18.91,9.03,20.47,12,20.47ZM12,16.23C12.52,16.23,12.94,15.81,12.94,15.29C12.94,14.77,12.52,14.35,12,14.35C11.48,14.35,11.06,14.77,11.06,15.29C11.06,15.81,11.48,16.23,12,16.23ZM9.03,16.23C9.55,16.23,9.97,15.81,9.97,15.29C9.97,14.77,9.55,14.35,9.03,14.35C8.51,14.35,8.09,14.77,8.09,15.29C8.09,15.81,8.51,16.23,9.03,16.23ZM14.97,16.23C15.49,16.23,15.91,15.81,15.91,15.29C15.91,14.77,15.49,14.35,14.97,14.35C14.45,14.35,14.03,14.77,14.03,15.29C14.03,15.81,14.45,16.23,14.97,16.23Z"/></svg>
                            Share on Reddit
                        </button>
                        
                        <!-- Copy Link -->
                         <button @click="shareTo('copy')" class="w-full flex items-center justify-center gap-3 px-4 py-3 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-xl font-semibold transition">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z"></path></svg>
                            Copy Link
                        </button>
                    </div>

                    <div class="mt-6 pt-4 border-t border-slate-100">
                         <p class="text-xs text-slate-400 text-center uppercase tracking-wide font-semibold mb-2">Preview</p>
                         <div class="bg-slate-50 p-3 rounded-lg text-sm text-slate-600 italic">
                            "<span x-text="viralText"></span>"
                         </div>
                    </div>
                </div>
            </div>

        </div>
        </div>
    `
)
