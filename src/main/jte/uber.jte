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
        "https://gigwagetruth.com/uber",
        "https://gigwagetruth.com/og-image.jpg"
    ),
    content = @`
        <script>
            function calculator() {
                return {
                    selectedApp: 'Uber',
                    
                    // Inputs
                    gross: Number('${initialGross}'),
                    miles: Number('${initialMiles}'),
                    hours: Number('${initialHours}'),
                    
                    // State
                    calculationMode: 'standard',
                    selectedVehicleId: 'irs-standard',
                    vehiclePresets: [],
                    customMpg: 25,
                    customMaintenance: 0.12,
                    gasPrice: 3.50,
                    targetWeeklyIncome: 1200,
                    
                    // Phase 3: Optional Advanced Inputs
                    tips: 0,
                    bonuses: 0,
                    waitTime: 0,
                    
                    // UI State
                    showTargetHustle: false,
                    showAdvancedOptions: false,
                    
                    async init() {
                        try {
                            const response = await fetch('/vehicle-presets.json');
                            this.vehiclePresets = await response.json();
                        } catch (e) {
                            this.vehiclePresets = [{id: 'irs-standard', name: 'IRS Standard', costPerMile: 0.725}];
                        }
                    },
                    
                    get selectedVehicle() {
                        return this.vehiclePresets.find(v => v.id === this.selectedVehicleId) || this.vehiclePresets[0];
                    },
                    
                    get deductionCost() {
                        if (this.calculationMode === 'standard') {
                            return this.miles * ${AppConstants.IRS_MILEAGE_RATE_2024};
                        } else {
                            const vehicle = this.selectedVehicle;
                            if (!vehicle || vehicle.id === 'irs-standard') return this.miles * 0.725;
                            
                            let cost = 0;
                            if (vehicle.type === 'electric') {
                                const kwhUsed = (this.miles / 100) * vehicle.kwhPer100Miles;
                                cost += kwhUsed * vehicle.avgElectricityCost;
                            } else {
                                const mpg = vehicle.id === 'custom' ? this.customMpg : vehicle.mpg;
                                cost += (this.miles / mpg) * this.gasPrice;
                            }
                            const maintenanceCost = vehicle.id === 'custom' ? this.customMaintenance : vehicle.maintenanceCostPerMile;
                            cost += this.miles * maintenanceCost;
                            return cost;
                        }
                    },
                    
                    // Phase 3: Advanced Calculations
                    get totalEarnings() {
                        return this.gross + this.tips + this.bonuses;
                    },
                    
                    get activeDriveTime() {
                        return Math.max(0, this.hours - this.waitTime);
                    },
                    
                    get taxCost() {
                        const profitForTax = this.totalEarnings - this.deductionCost;
                        return profitForTax <= 0 ? 0 : profitForTax * ${AppConstants.SELF_EMPLOYMENT_TAX_RATE};
                    },
                    
                    get totalNet() {
                        return this.totalEarnings - this.deductionCost - this.taxCost;
                    },
                    
                    get netHourly() {
                        return this.hours <= 0 ? 0 : this.totalNet / this.hours;
                    },
                    
                    get activeHourlyWage() {
                        return this.activeDriveTime <= 0 ? 0 : this.totalNet / this.activeDriveTime;
                    },
                    
                    get hasAdvancedData() {
                        return this.tips > 0 || this.bonuses > 0 || this.waitTime > 0;
                    },
                    
                    get remainingHours() {
                        if (this.netHourly <= 0) return 0;
                        const needed = Math.max(0, this.targetWeeklyIncome - this.gross);
                        return Math.ceil(needed / this.netHourly);
                    },
                    
                    get shockMessage() {
                        const wage = this.netHourly;
                        
                        if (wage < 12) {
                            const messages = [
                                { emoji: '💀', text: `You're making $${(15 - wage).toFixed(2)} LESS than McDonald's ($15/hr)` },
                                { emoji: '🍔', text: `Burger King cashiers ($15) earn more than you ($${wage.toFixed(2)})` },
                                { emoji: '😱', text: `Target starts at $15. You're at $${wage.toFixed(2)}` },
                                { emoji: '💔', text: `Even Walmart pays $14-16. This is rough.` }
                            ];
                            return messages[Math.floor(Math.random() * messages.length)];
                        }
                        
                        if (wage < 15) {
                            const messages = [
                                { emoji: '⚠️', text: `Below CA/NY minimum wage ($15-16/hr)` },
                                { emoji: '🎯', text: `Barely keeping up with inflation` },
                                { emoji: '💸', text: `Fast food workers are catching up to you` }
                            ];
                            return messages[Math.floor(Math.random() * messages.length)];
                        }
                        
                        if (wage < 18) {
                            const messages = [
                                { emoji: '📊', text: `Beating minimum wage, but barely` },
                                { emoji: '🤔', text: `Chick-fil-A pays $17-19. Worth the switch?` },
                                { emoji: '💪', text: `Hanging in there, but not thriving` }
                            ];
                            return messages[Math.floor(Math.random() * messages.length)];
                        }
                        
                        if (wage < 25) {
                            const messages = [
                                { emoji: '✅', text: `Solid wage! Above most gig workers` },
                                { emoji: '🎉', text: `You're doing better than 60% of drivers` },
                                { emoji: '💼', text: `Respectable hustle money` }
                            ];
                            return messages[Math.floor(Math.random() * messages.length)];
                        }
                        
                        if (wage < 35) {
                            return { emoji: '🔥', text: `Top 20% of gig drivers! Keep it up!` };
                        }
                        
                        return { emoji: '🚀', text: `Crushing it! Top 5% wage` };
                    },
                    
                    setMode(mode) {
                        this.calculationMode = mode;
                        if (mode === 'standard') {
                            this.selectedVehicleId = 'irs-standard';
                        } else if (this.selectedVehicleId === 'irs-standard') {
                            this.selectedVehicleId = 'prius';
                        }
                    },
                    
                    shareResult() {
                        const text = 'I thought I was making money on ' + this.selectedApp + ', but my real wage is only $' + this.netHourly.toFixed(2) + '/hr. Check yours at GigWageTruth.com';
                        navigator.clipboard.writeText(text);
                        alert("Copied to clipboard!");
                    }
                }
            }
        </script>

        <div x-data="calculator()" class="min-h-screen bg-white">
            
            <!-- Sticky Result Header (Mobile/Desktop) -->
            <div class="sticky top-0 bg-white/95 backdrop-blur-sm border-b border-slate-200 z-10">
                <div class="max-w-6xl mx-auto px-6 py-4">
                    <div class="flex items-center justify-between">
                        <div class="flex items-center gap-4">
                            <div class="flex items-center gap-2">
                                <div class="w-2 h-2 rounded-full" :class="netHourly < 15 ? 'bg-red-500' : 'bg-green-500'"></div>
                                <span class="text-xs font-medium text-slate-500 uppercase tracking-wider" x-text="netHourly < 15 ? 'Critical' : 'Stable'"></span>
                            </div>
                            <div class="hidden sm:block h-4 w-px bg-slate-200"></div>
                            <div class="hidden sm:flex items-baseline gap-1">
                                <span class="text-2xl font-semibold" :class="netHourly < 0 ? 'text-red-600' : 'text-slate-900'">$<span x-text="netHourly.toFixed(2)"></span></span>
                                <span class="text-sm text-slate-400">/hr</span>
                            </div>
                        </div>
                        <button @click="shareResult()" class="px-4 py-2 text-sm font-medium text-slate-900 hover:bg-slate-50 rounded-lg transition">
                            Share
                        </button>
                    </div>
                </div>
            </div>

            <!-- 2-Column Layout (Desktop) / Single Column (Mobile) -->
            <div class="max-w-6xl mx-auto px-6 py-12">
                <div class="grid lg:grid-cols-3 gap-12">
                    
                    <!-- Left: Main Result + Breakdown (2/3 width on desktop) -->
                    <div class="lg:col-span-2 space-y-12">
                        
                        <!-- Hero Number (Desktop only, hidden on mobile since it's in sticky) -->
                        <div class="hidden sm:block">
                            <p class="text-sm font-medium text-slate-500 mb-3">Your Real Hourly Wage</p>
                            <div class="flex items-baseline gap-2">
                                <span class="text-7xl font-semibold tracking-tight" :class="netHourly < 0 ? 'text-red-600' : 'text-slate-900'">$<span x-text="netHourly.toFixed(2)"></span></span>
                                <span class="text-3xl font-medium text-slate-400">/hr</span>
                            </div>
                            <p class="text-xs text-slate-400 mt-2">Based on your entered period</p>
                            
                            <!-- Shock Message -->
                            <div class="mt-6 p-4 bg-slate-50 border-l-4 rounded-r-xl" 
                                 :class="netHourly < 15 ? 'border-red-500 bg-red-50' : netHourly < 25 ? 'border-yellow-500 bg-yellow-50' : 'border-green-500 bg-green-50'">
                                <p class="text-sm font-medium flex items-center gap-2">
                                    <span class="text-2xl" x-text="shockMessage.emoji"></span>
                                    <span :class="netHourly < 15 ? 'text-red-800' : netHourly < 25 ? 'text-yellow-800' : 'text-green-800'" x-text="shockMessage.text"></span>
                                </p>
                            </div>
                            
                            <p class="text-sm text-slate-600 mt-4" x-show="netHourly < 15">
                                <span class="font-semibold text-red-600">$<span x-text="(15 - netHourly).toFixed(2)"></span> less</span> than minimum wage
                            </p>
                        </div>

                        <!-- Mobile: Show big number -->
                        <div class="sm:hidden text-center py-8">
                            <div class="flex items-baseline justify-center gap-2">
                                <span class="text-6xl font-semibold" :class="netHourly < 0 ? 'text-red-600' : 'text-slate-900'">$<span x-text="netHourly.toFixed(2)"></span></span>
                                <span class="text-2xl text-slate-400">/hr</span>
                            </div>
                            <div class="mt-4 p-3 bg-slate-50 border-l-4 rounded-r-xl mx-4" 
                                 :class="netHourly < 15 ? 'border-red-500 bg-red-50' : netHourly < 25 ? 'border-yellow-500 bg-yellow-50' : 'border-green-500 bg-green-50'">
                                <p class="text-xs font-medium flex items-center gap-2">
                                    <span class="text-xl" x-text="shockMessage.emoji"></span>
                                    <span :class="netHourly < 15 ? 'text-red-800' : netHourly < 25 ? 'text-yellow-800' : 'text-green-800'" x-text="shockMessage.text"></span>
                                </p>
                            </div>
                        </div>

                        <!-- Cost Breakdown -->
                        <div class="space-y-4">
                            <h3 class="text-base font-semibold text-slate-900">Breakdown</h3>
                            <div class="bg-slate-50 rounded-2xl p-6 space-y-4">
                                <div class="flex justify-between text-sm">
                                    <span class="text-slate-600">Gross Earnings</span>
                                    <span class="font-medium text-slate-900">$<span x-text="gross.toFixed(2)"></span></span>
                                </div>
                                <div class="flex justify-between text-sm">
                                    <span class="text-slate-600">Vehicle Costs</span>
                                    <span class="font-medium text-red-600">-$<span x-text="deductionCost.toFixed(2)"></span></span>
                                </div>
                                <div class="flex justify-between text-sm">
                                    <span class="text-slate-600">Tax (15.3%)</span>
                                    <span class="font-medium text-red-600">-$<span x-text="taxCost.toFixed(2)"></span></span>
                                </div>
                                <div class="pt-4 border-t border-slate-200 flex justify-between">
                                    <span class="font-semibold text-slate-900">Net Profit</span>
                                    <span class="text-lg font-semibold text-slate-900">$<span x-text="totalNet.toFixed(2)"></span></span>
                                </div>
                            </div>
                        </div>

                        <!-- Calculation Method -->
                        <div class="space-y-4">
                            <h3 class="text-base font-semibold text-slate-900">Cost Calculation</h3>
                            <div class="grid grid-cols-2 gap-3">
                                <button @click="setMode('standard')" 
                                        class="py-3 px-4 rounded-xl border transition-all text-sm font-medium text-left"
                                        :class="calculationMode === 'standard' 
                                            ? 'border-slate-900 bg-slate-900 text-white' 
                                            : 'border-slate-200 hover:border-slate-300 text-slate-600'">
                                    IRS Standard
                                    <div class="text-xs mt-0.5 opacity-70">$0.725/mi</div>
                                </button>
                                <button @click="setMode('advanced')" 
                                        class="py-3 px-4 rounded-xl border transition-all text-sm font-medium text-left"
                                        :class="calculationMode === 'advanced' 
                                            ? 'border-slate-900 bg-slate-900 text-white' 
                                            : 'border-slate-200 hover:border-slate-300 text-slate-600'">
                                    My Vehicle
                                    <div class="text-xs mt-0.5 opacity-70">Precise</div>
                                </button>
                            </div>
                            
                            <div x-show="calculationMode === 'advanced'" x-transition class="space-y-3 pt-2">
                                <select x-model="selectedVehicleId" 
                                        class="w-full px-4 py-2.5 rounded-xl border border-slate-200 bg-white text-sm focus:border-slate-400 focus:outline-none">
                                    <template x-for="vehicle in vehiclePresets.filter(v => v.id !== 'irs-standard')" :key="vehicle.id">
                                        <option :value="vehicle.id" x-text="vehicle.icon + ' ' + vehicle.name"></option>
                                    </template>
                                </select>
                                <div x-show="selectedVehicleId === 'custom'">
                                    <input type="number" x-model.number="customMpg" placeholder="MPG" 
                                           class="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:border-slate-400 focus:outline-none">
                                </div>
                                <div x-show="selectedVehicle?.type !== 'electric'" class="flex items-center gap-3">
                                    <span class="text-xs text-slate-500 shrink-0">Gas Price</span>
                                    <input type="range" x-model.number="gasPrice" min="2.00" max="7.00" step="0.10"
                                           class="flex-1 h-1 bg-slate-200 rounded-full accent-slate-900">
                                    <span class="text-sm font-medium text-slate-900 shrink-0">$<span x-text="gasPrice.toFixed(2)"></span></span>
                                </div>
                            </div>
                        </div>
                        
                    </div>

                    <!-- Right: Tools Sidebar (1/3 width on desktop) -->
                    <div class="lg:col-span-1 space-y-6">
                        
                        <!-- Target Hustle - Collapsible -->
                        <div class="border border-slate-200 rounded-2xl overflow-hidden">
                            <button @click="showTargetHustle = !showTargetHustle" 
                                    class="w-full px-6 py-4 flex items-center justify-between hover:bg-slate-50 transition">
                                <span class="text-sm font-semibold text-slate-900">Weekly Goal</span>
                                <svg class="w-5 h-5 text-slate-400 transition-transform" :class="{'rotate-180': showTargetHustle}" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                                </svg>
                            </button>
                            <div x-show="showTargetHustle" x-collapse class="px-6 pb-6 space-y-4">
                                <div class="flex justify-between items-baseline">
                                    <span class="text-xs text-slate-500">Target</span>
                                    <span class="text-xl font-semibold text-slate-900">$<span x-text="targetWeeklyIncome"></span></span>
                                </div>
                                <input type="range" x-model.number="targetWeeklyIncome" min="500" max="3000" step="50" 
                                       class="w-full h-1 bg-slate-200 rounded-full accent-slate-900">
                                <div class="bg-slate-50 rounded-xl p-4">
                                    <p class="text-xs text-slate-500 mb-1">Hours needed</p>
                                    <p class="text-3xl font-semibold text-slate-900">
                                        <span x-text="remainingHours"></span>
                                        <span class="text-lg text-slate-400 ml-1">hrs</span>
                                    </p>
                                    <p class="text-xs text-slate-500 mt-2">
                                        + $<span x-text="(remainingHours * netHourly).toFixed(2)"></span> more
                                    </p>
                                </div>
                            </div>
                        </div>

                        <!-- Quick Stats -->
                        <div class="bg-slate-50 rounded-2xl p-6 space-y-3">
                            <div class="flex justify-between text-sm">
                                <span class="text-slate-500">Miles driven</span>
                                <span class="font-medium text-slate-900" x-text="miles"></span>
                            </div>
                            <div class="flex justify-between text-sm">
                                <span class="text-slate-500">Hours worked</span>
                                <span class="font-medium text-slate-900" x-text="hours"></span>
                            </div>
                            <div class="flex justify-between text-sm">
                                <span class="text-slate-500">Efficiency</span>
                                <span class="font-medium text-slate-900" x-text="(miles/hours).toFixed(1) + ' mi/hr'"></span>
                            </div>
                        </div>

                        <!-- Advanced Options (Phase 3) -->
                        <div class="border border-slate-200 rounded-2xl overflow-hidden">
                            <button @click="showAdvancedOptions = !showAdvancedOptions" 
                                    class="w-full px-6 py-4 flex items-center justify-between hover:bg-slate-50 transition">
                                <div class="text-left">
                                    <span class="text-sm font-semibold text-slate-900">Advanced Options</span>
                                    <p class="text-xs text-slate-500 mt-0.5">For more accurate results</p>
                                </div>
                                <svg class="w-5 h-5 text-slate-400 transition-transform" :class="{'rotate-180': showAdvancedOptions}" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                                </svg>
                            </button>
                            <div x-show="showAdvancedOptions" x-collapse class="px-6 pb-6 space-y-4">
                                <div>
                                    <label class="block text-xs font-medium text-slate-700 mb-2">💰 Tips & Cash</label>
                                    <input type="number" x-model.number="tips" placeholder="$0" min="0" step="10"
                                           class="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:border-slate-400 focus:outline-none">
                                </div>
                                <div>
                                    <label class="block text-xs font-medium text-slate-700 mb-2">🎁 Bonuses & Promos</label>
                                    <input type="number" x-model.number="bonuses" placeholder="$0" min="0" step="10"
                                           class="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:border-slate-400 focus:outline-none">
                                </div>
                                <div>
                                    <label class="block text-xs font-medium text-slate-700 mb-2">⏱️ Wait Time (hours)</label>
                                    <input type="number" x-model.number="waitTime" placeholder="0" min="0" max="40" step="0.5"
                                           class="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:border-slate-400 focus:outline-none">
                                    <p class="text-xs text-slate-400 mt-1">Time not actively driving</p>
                                </div>
                                <div x-show="hasAdvancedData" class="bg-emerald-50 rounded-xl p-4 mt-4">
                                    <p class="text-xs text-emerald-700 mb-1">Active Drive Wage</p>
                                    <p class="text-2xl font-semibold text-emerald-900">
                                        $<span x-text="activeHourlyWage.toFixed(2)"></span><span class="text-sm text-emerald-600">/hr</span>
                                    </p>
                                    <p class="text-xs text-emerald-600 mt-1">
                                        <span x-text="activeDriveTime.toFixed(1)"></span> active hours
                                    </p>
                                </div>
                            </div>
                        </div>

                    </div>
                    
                </div>
            </div>
        </div>
    `
)
