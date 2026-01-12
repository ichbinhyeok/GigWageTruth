@import com.gigwager.model.SeoMeta
@import com.gigwager.util.AppConstants

@param String app
@param Double initialGross
@param Double initialMiles  
@param Double initialHours
@param String customTitle

@template.layout.main(
    seoMeta = new SeoMeta(
        customTitle != null ? customTitle : "My Real Hourly Wage - The Shocking Truth",
        "See exactly how much I make after gas, taxes, and depreciation. The number will surprise you.",
        "https://gigwagetruth.com/" + app,
        "https://gigwagetruth.com/og-image.jpg"
    ),
    content = @`
        <script>
            function calculator() {
                return {
                    selectedApp: '${app}', // 'uber' or 'doordash'
                    
                    // Separated Input State for each app
                    inputs: {
                        uber: {
                            gross: 0, miles: 0, hours: 0,
                            tips: 0, bonuses: 0, waitTime: 0
                        },
                        doordash: {
                            gross: 0, miles: 0, hours: 0,
                            tips: 0, bonuses: 0, waitTime: 0
                        }
                    },
                    
                    // Shared State (Vehicle settings persist across tabs)
                    calculationMode: 'standard',
                    selectedVehicleId: 'irs-standard',
                    vehiclePresets: [],
                    customMpg: 25,
                    customMaintenance: 0.12,
                    customDepreciation: 0.15,
                    gasPrice: 3.50,
                    targetWeeklyIncome: 1200,
                    
                    // UI State
                    showTargetHustle: false,
                    showAdvancedOptions: false,
                    isRoundTrip: false,
                    
                    async init() {
                        // 1. Initialize Active App Data from Server Params
                        this.inputs[this.selectedApp] = {
                            gross: Number('${initialGross}'),
                            miles: Number('${initialMiles}'),
                            hours: Number('${initialHours}'),
                            tips: 0, bonuses: 0, waitTime: 0
                        };

                        // 2. Load Vehicle Presets
                        try {
                            const response = await fetch('/vehicle-presets.json');
                            this.vehiclePresets = await response.json();
                            const savedVehicle = localStorage.getItem('selectedVehicleId');
                            if (savedVehicle && this.vehiclePresets.some(v => v.id === savedVehicle)) {
                                this.selectedVehicleId = savedVehicle;
                                this.calculationMode = 'advanced';
                            }
                        } catch (e) {
                            this.vehiclePresets = [{id: 'irs-standard', name: 'IRS Standard', costPerMile: 0.725}];
                        }
                        
                        this.$watch('selectedVehicleId', (value) => {
                            localStorage.setItem('selectedVehicleId', value);
                        });
                        
                        if (this.selectedApp === 'doordash') {
                            this.targetWeeklyIncome = 1000;
                        }
                    },
                    
                    switchApp(appName) {
                        this.selectedApp = appName;
                    },
                    
                    // Computed property to get current app's input data
                    get current() {
                        return this.inputs[this.selectedApp];
                    },

                    get isUber() { return this.selectedApp === 'uber'; },
                    get isDoorDash() { return this.selectedApp === 'doordash'; },

                    get selectedVehicle() {
                        return this.vehiclePresets.find(v => v.id === this.selectedVehicleId) || this.vehiclePresets[0];
                    },
                    
                    get deductionCost() {
                        const miles = this.isRoundTrip ? this.current.miles * 2 : this.current.miles;
                        
                        if (this.calculationMode === 'standard') {
                            return miles * ${AppConstants.IRS_MILEAGE_RATE_2024};
                        } else {
                            const vehicle = this.selectedVehicle;
                            if (!vehicle || vehicle.id === 'irs-standard') return miles * 0.725;
                            
                            let cost = 0;
                            if (vehicle.type === 'electric') {
                                const kwhUsed = (miles / 100) * vehicle.kwhPer100Miles;
                                cost += kwhUsed * vehicle.avgElectricityCost;
                            } else {
                                const mpg = vehicle.id === 'custom' ? Number(this.customMpg) : vehicle.mpg;
                                cost += (miles / mpg) * Number(this.gasPrice);
                            }
                            const maintenanceCost = vehicle.id === 'custom' ? Number(this.customMaintenance) : vehicle.maintenanceCostPerMile;
                            const depreciationCost = vehicle.id === 'custom' ? Number(this.customDepreciation) : (vehicle.depreciationCostPerMile || 0);
                            cost += miles * (maintenanceCost + depreciationCost);
                            return cost;
                        }
                    },
                    
                    get effectiveMiles() {
                         return this.isRoundTrip ? this.current.miles * 2 : this.current.miles;
                    },
                    
                    get totalEarnings() {
                        return this.current.gross + Number(this.current.tips) + Number(this.current.bonuses);
                    },
                    
                    get activeDriveTime() {
                        return Math.max(0, this.current.hours - Number(this.current.waitTime));
                    },
                    
                    get taxCost() {
                        const profitForTax = this.totalEarnings - this.deductionCost;
                        return profitForTax <= 0 ? 0 : profitForTax * ${AppConstants.SELF_EMPLOYMENT_TAX_RATE};
                    },
                    
                    get totalNet() {
                        return this.totalEarnings - this.deductionCost - this.taxCost;
                    },
                    
                    get netHourly() {
                        return this.current.hours <= 0 ? 0 : this.totalNet / this.current.hours;
                    },
                    
                    get activeHourlyWage() {
                        return this.activeDriveTime <= 0 ? 0 : this.totalNet / this.activeDriveTime;
                    },
                    
                    get hasAdvancedData() {
                        return Number(this.current.tips) > 0 || Number(this.current.bonuses) > 0 || Number(this.current.waitTime) > 0;
                    },
                    
                    get remainingHours() {
                        if (this.netHourly <= 0) return 0;
                        const needed = Math.max(0, Number(this.targetWeeklyIncome) - this.current.gross);
                        return Math.ceil(needed / this.netHourly);
                    },
                    
                    get shockMessage() {
                        const wage = this.netHourly;
                        
                        if (wage < 12) {
                            const messages = [
                                { emoji: '💀', text: "You're making $" + (15 - wage).toFixed(2) + " LESS than McDonald's ($15/hr) + NO benefits" },
                                { emoji: '🍔', text: "Burger King cashiers ($15) earn more + get paid time off" },
                                { emoji: '😱', text: (this.isDoorDash ? "Walmart Greeters" : "Target") + " starts at $15. No vehicle depreciation there." },
                                { emoji: '💔', text: "Even Walmart pays $14-16. This is rough." }
                            ];
                            return messages[Math.floor(Math.random() * messages.length)];
                        }
                        
                        if (wage < 15) {
                            const messages = [
                                { emoji: '⚠️', text: "Below CA/NY minimum wage ($15-16/hr) and 0 benefits" },
                                { emoji: '🎯', text: "Barely keeping up with inflation + " + (this.isDoorDash ? "car repairs" : "car depreciation") },
                                { emoji: '💸', text: "Fast food workers are catching up + have health insurance" }
                            ];
                            return messages[Math.floor(Math.random() * messages.length)];
                        }
                        
                        if (wage < 18) {
                            const messages = [
                                { emoji: '📊', text: "Beating minimum wage, but barely" },
                                { emoji: '🤔', text: "Chick-fil-A pays $17-19. Worth the switch?" },
                                { emoji: '💪', text: "Hanging in there, but not thriving" }
                            ];
                            return messages[Math.floor(Math.random() * messages.length)];
                        }
                        
                        if (wage < 25) {
                            const messages = [
                                { emoji: '✅', text: "Solid wage! Above most gig workers" },
                                { emoji: '🎉', text: "You're doing better than 60% of drivers" },
                                { emoji: '💼', text: "Respectable hustle money" }
                            ];
                            return messages[Math.floor(Math.random() * messages.length)];
                        }
                        
                        if (wage < 35) {
                            return { emoji: '🔥', text: "Top Tier! Great strategy!" };
                        }
                        
                        return { emoji: '🚀', text: "Crushing it! Top 5% wage" };
                    },
                    
                    setMode(mode) {
                        this.calculationMode = mode;
                        if (mode === 'standard') {
                            this.selectedVehicleId = 'irs-standard';
                        } else if (this.selectedVehicleId === 'irs-standard') {
                            this.selectedVehicleId = this.isDoorDash ? 'civic' : 'prius';
                        }
                    },
                    
                    shareResult() {
                        const text = 'I thought I was making money on ' + (this.isUber ? 'Uber' : 'DoorDash') + ', but my real wage is only $' + this.netHourly.toFixed(2) + '/hr. Check yours at GigWageTruth.com';
                        navigator.clipboard.writeText(text);
                        alert("Link copied to clipboard! Share it on Reddit or Twitter.");
                    }
                }
            }
        </script>
        
        <div x-data="calculator()" class="min-h-screen bg-white transition-colors duration-500" :class="isDoorDash ? 'selection:bg-red-200' : 'selection:bg-slate-200'">
            
            <!-- Sticky Result Header -->
            <div class="sticky top-0 bg-white/95 backdrop-blur-sm border-b border-slate-200 z-10 transition-colors duration-300" 
                 :class="{'border-red-100': isDoorDash}">
                <div class="max-w-6xl mx-auto px-6 py-4">
                    <div class="flex items-center justify-between">
                        <div class="flex items-center gap-4">
                            <!-- App Toggle -->
                            <div class="flex p-1.5 bg-slate-100 rounded-xl">
                                <button @click="switchApp('uber')" 
                                        :class="isUber ? 'bg-white shadow-sm text-slate-900 font-bold' : 'text-slate-500 hover:text-slate-700'"
                                        class="px-4 py-2 text-sm sm:text-base rounded-lg transition-all flex items-center gap-2">
                                    <span>🚗</span>
                                    <span>Uber</span>
                                </button>
                                <button @click="switchApp('doordash')" 
                                        :class="isDoorDash ? 'bg-white shadow-sm text-red-600 font-bold' : 'text-slate-500 hover:text-slate-700'"
                                        class="px-4 py-2 text-sm sm:text-base rounded-lg transition-all flex items-center gap-2">
                                    <span>🍔</span>
                                    <span>DoorDash</span>
                                </button>
                            </div>

                            <div class="hidden sm:block h-6 w-px bg-slate-200"></div>
                            
                            <div class="flex items-center gap-3">
                                <div class="w-2.5 h-2.5 rounded-full" :class="netHourly < 15 ? 'bg-red-500 animate-pulse' : 'bg-green-500'"></div>
                                <span class="text-sm font-bold text-slate-700 uppercase tracking-wider" x-text="netHourly < 15 ? 'CRITICAL' : 'STABLE'"></span>
                            </div>
                            <div class="hidden sm:flex items-baseline gap-1">
                                <span class="text-2xl font-semibold transition-colors" :class="netHourly < 0 ? 'text-red-600' : 'text-slate-900'">$<span x-text="netHourly.toFixed(2)"></span></span>
                                <span class="text-sm text-slate-400">/hr</span>
                            </div>
                        </div>
                        
                        <!-- Minimal Share Button -->
                        <button @click="shareResult()" class="text-slate-400 hover:text-slate-900 transition-colors p-2 rounded-lg hover:bg-slate-100">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.684 13.342C8.886 12.938 9 12.482 9 12c0-.482-.114-.938-.316-1.342m0 2.684a3 3 0 110-2.684m0 2.684l6.632 3.316m-6.632-6l6.632-3.316m0 0a3 3 0 105.367-2.684 3 3 0 00-5.367 2.684zm0 9.316a3 3 0 105.368 2.684 3 3 0 00-5.368-2.684z"></path></svg>
                        </button>
                    </div>
                </div>
            </div>

            <!-- 2-Column Layout -->
            <div class="max-w-6xl mx-auto px-6 py-12">
                <div class="grid lg:grid-cols-3 gap-12">
                    
                    <!-- Left: Main Result + Breakdown -->
                    <div class="lg:col-span-2 space-y-12">
                        
                        <!-- Hero Number -->
                        <div class="hidden sm:block">
                            <p class="text-sm font-medium text-slate-500 mb-3" x-text="'Your Real Hourly Wage (' + (isUber ? 'Uber/Lyft' : 'DoorDash') + ')'"></p>
                            <div class="flex items-baseline gap-2">
                                <span class="text-7xl font-semibold tracking-tight transition-colors" :class="netHourly < 0 ? 'text-red-600' : 'text-slate-900'">$<span x-text="netHourly.toFixed(2)"></span></span>
                                <span class="text-3xl font-medium text-slate-400">/hr</span>
                            </div>
                            <p class="text-xs text-slate-400 mt-2">Based on your entered period</p>
                            
                            <!-- Shock Message -->
                            <div class="mt-6 p-4 bg-slate-50 border-l-4 rounded-r-xl transition-all duration-300" 
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

                        <!-- Mobile -->
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

                        <!-- Breakdown -->
                        <div class="space-y-4">
                            <h3 class="text-base font-semibold text-slate-900">Breakdown</h3>
                            <div class="bg-slate-50 rounded-2xl p-6 space-y-4">
                                <div class="flex justify-between text-sm">
                                    <span class="text-slate-600">Total Weekly Earnings</span>
                                    <span class="font-medium text-slate-900">$<span x-text="current.gross.toFixed(2)"></span></span>
                                </div>
                                <div class="flex justify-between text-sm">
                                    <span class="text-slate-600">Vehicle Costs (Gas + Wear)</span>
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

                        <!-- Vehicle Settings -->
                        <div class="space-y-4">
                            <h3 class="text-base font-semibold text-slate-900">Vehicle Settings (Shared)</h3>
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
                                    <template x-for="vehicle in vehiclePresets.filter(v => v.id !== 'irs-standard' && v.type !== 'bike' && v.type !== 'walker')" :key="vehicle.id">
                                        <option :value="vehicle.id" x-text="vehicle.icon + ' ' + vehicle.name"></option>
                                    </template>
                                </select>
                                <div x-show="selectedVehicleId === 'custom'">
                                    <input type="number" x-model="customMpg" placeholder="MPG" 
                                           class="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:border-slate-400 focus:outline-none">
                                    <div class="mt-2 grid grid-cols-2 gap-2">
                                        <div>
                                            <label class="text-[10px] text-slate-500 uppercase tracking-wide font-semibold">Maint ($/mi)</label>
                                            <input type="number" x-model="customMaintenance" step="0.01" 
                                                class="w-full px-3 py-2 rounded-lg border border-slate-200 text-sm focus:border-slate-400 focus:outline-none">
                                        </div>
                                        <div>
                                            <label class="text-[10px] text-slate-500 uppercase tracking-wide font-semibold">Depr ($/mi)</label>
                                            <input type="number" x-model="customDepreciation" step="0.01" 
                                                class="w-full px-3 py-2 rounded-lg border border-slate-200 text-sm focus:border-slate-400 focus:outline-none">
                                        </div>
                                    </div>
                                </div>
                                <div x-show="selectedVehicle?.type !== 'electric'" class="flex items-center gap-3">
                                    <span class="text-xs text-slate-500 shrink-0">Gas Price</span>
                                    <input type="range" x-model.number="gasPrice" min="2.00" max="7.00" step="0.10"
                                           class="flex-1 h-1 bg-slate-200 rounded-full accent-slate-900">
                                    <span class="text-sm font-medium text-slate-900 shrink-0">$<span x-text="Number(gasPrice).toFixed(2)"></span></span>
                                </div>
                            </div>
                        </div>
                        
                    </div>

                    <!-- Right: Tools Sidebar -->
                    <div class="lg:col-span-1 space-y-6">
                        
                        <!-- Inputs Panel -->
                        <div class="bg-slate-50 rounded-2xl p-6 space-y-4 border border-slate-200">
                             <h3 class="font-bold text-slate-900" x-text="isUber ? 'Uber/Lyft Inputs' : 'DoorDash Inputs'"></h3>
                             
                             <div>
                                <label class="block text-xs font-medium text-slate-500 mb-1">Total Earnings ($)</label>
                                <input type="number" x-model.number="current.gross" 
                                       class="w-full px-3 py-2 rounded-lg border border-slate-200 focus:border-slate-400 focus:outline-none font-semibold text-slate-900">
                             </div>
                             <div>
                                <label class="block text-xs font-medium text-slate-500 mb-1">Total Miles</label>
                                <input type="number" x-model.number="current.miles" 
                                       class="w-full px-3 py-2 rounded-lg border border-slate-200 focus:border-slate-400 focus:outline-none font-semibold text-slate-900">
                             </div>
                             <div>
                                <label class="block text-xs font-medium text-slate-500 mb-1">Online Hours</label>
                                <input type="number" x-model.number="current.hours" 
                                       class="w-full px-3 py-2 rounded-lg border border-slate-200 focus:border-slate-400 focus:outline-none font-semibold text-slate-900">
                             </div>
                        </div>

                        <!-- Target Hustle -->
                        <div class="border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
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
                                    <span class="text-xl font-semibold text-slate-900">$<span x-text="Number(targetWeeklyIncome)"></span></span>
                                </div>
                                <input type="range" x-model.number="targetWeeklyIncome" min="500" max="3000" step="50" 
                                       class="w-full h-1 bg-slate-200 rounded-full accent-slate-900"
                                       :class="isDoorDash ? 'accent-red-600' : 'accent-slate-900'">
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
                                <span class="text-slate-500 flex items-center gap-1">
                                    Miles driven
                                    <button @click="isRoundTrip = !isRoundTrip" 
                                            class="text-[10px] px-1.5 py-0.5 rounded border transition-colors"
                                            :class="isRoundTrip ? 'bg-slate-900 text-white border-slate-900' : 'bg-white text-slate-500 border-slate-200 hover:border-slate-300'">
                                        x2 Return
                                    </button>
                                </span>
                                <span class="font-medium text-slate-900" x-text="effectiveMiles.toFixed(1)"></span>
                            </div>
                            <div class="flex justify-between text-sm">
                                <span class="text-slate-500">Online Time</span>
                                <span class="font-medium text-slate-900" x-text="current.hours"></span>
                            </div>
                            <div class="flex justify-between text-sm">
                                <span class="text-slate-500">Efficiency</span>
                                <span class="font-medium text-slate-900" x-text="(current.miles/current.hours).toFixed(1) + ' mi/hr'"></span>
                            </div>
                        </div>

                        <!-- Advanced Options -->
                        <div class="border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
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
                                    <input type="number" x-model="current.tips" placeholder="$0" min="0" step="1"
                                           class="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:border-slate-400 focus:outline-none">
                                </div>
                                <div>
                                    <label class="block text-xs font-medium text-slate-700 mb-2">🎁 Bonuses & Promos</label>
                                    <input type="number" x-model="current.bonuses" placeholder="$0" min="0" step="1"
                                           class="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:border-slate-400 focus:outline-none">
                                </div>
                                <div>
                                    <label class="block text-xs font-medium text-slate-700 mb-2" x-text="isUber ? '⏱️ Wait Time (hours)' : '⏱️ Restaurant Wait (hours)'"></label>
                                    <input type="number" x-model="current.waitTime" placeholder="0" min="0" max="40" step="0.5"
                                           class="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:border-slate-400 focus:outline-none">
                                    <p class="text-xs text-slate-400 mt-1" x-text="isUber ? 'Time not actively driving' : 'Time waiting at restaurants'"></p>
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
