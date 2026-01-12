@import com.gigwager.model.SeoMeta
@import com.gigwager.util.AppConstants

@param Double initialGross
@param Double initialMiles  
@param Double initialHours
@param String customTitle

@template.layout.main(
    seo = new SeoMeta(
        customTitle != null ? customTitle : "My Real Hourly Wage - The Shocking Truth",
        "See exactly how much I make after gas, taxes, and depreciation. The number will surprise you.",
        "https://gigwagetruth.com/uber",
        "https://gigwagetruth.com/og-image.jpg"
    ),
    content = @`
        <script>
            function calculator() {
                console.log("Calculator Initialized. Gross:", '${initialGross}');
                return {
                    selectedApp: 'Uber',
                    
                    // Inputs (from URL params)
                    gross: Number('${initialGross}'),
                    miles: Number('${initialMiles}'),
                    hours: Number('${initialHours}'),
                    
                    // Mode: 'standard' (IRS) or 'advanced' (Vehicle-specific)
                    calculationMode: 'standard',
                    
                    // Vehicle Selection (Advanced Mode)
                    selectedVehicleId: 'irs-standard',
                    vehiclePresets: [],
                    customMpg: 25,
                    customMaintenance: 0.12,
                    gasPrice: 3.50,
                    
                    // Target Hustle
                    targetWeeklyIncome: 1200,
                    
                    // Load vehicle data
                    async init() {
                        try {
                            const response = await fetch('/vehicle-presets.json');
                            this.vehiclePresets = await response.json();
                        } catch (e) {
                            console.error('Failed to load vehicle presets:', e);
                            this.vehiclePresets = [{id: 'irs-standard', name: 'IRS Standard', costPerMile: 0.725}];
                        }
                    },
                    
                    get selectedVehicle() {
                        return this.vehiclePresets.find(v => v.id === this.selectedVehicleId) || this.vehiclePresets[0];
                    },
                    
                    // Cost Calculations
                    get deductionCost() {
                        if (this.calculationMode === 'standard') {
                            return this.miles * ${AppConstants.IRS_MILEAGE_RATE_2024};
                        } else {
                            const vehicle = this.selectedVehicle;
                            if (!vehicle || vehicle.id === 'irs-standard') return this.miles * 0.725;
                            
                            let cost = 0;
                            
                            // Gas/Electricity cost
                            if (vehicle.type === 'electric') {
                                const kwhUsed = (this.miles / 100) * vehicle.kwhPer100Miles;
                                cost += kwhUsed * vehicle.avgElectricityCost;
                            } else {
                                const mpg = vehicle.id === 'custom' ? this.customMpg : vehicle.mpg;
                                cost += (this.miles / mpg) * this.gasPrice;
                            }
                            
                            // Maintenance
                            const maintenanceCost = vehicle.id === 'custom' ? this.customMaintenance : vehicle.maintenanceCostPerMile;
                            cost += this.miles * maintenanceCost;
                            
                            return cost;
                        }
                    },
                    
                    get taxCost() {
                        const profitForTax = this.gross - this.deductionCost;
                        if(profitForTax <= 0) return 0;
                        return profitForTax * ${AppConstants.SELF_EMPLOYMENT_TAX_RATE};
                    },
                    
                    get totalNet() {
                        return this.gross - this.deductionCost - this.taxCost;
                    },
                    
                    get netHourly() {
                        if(this.hours <= 0) return 0;
                        return this.totalNet / this.hours;
                    },
                    
                    // Target Hustle
                    get remainingHours() {
                        if (this.netHourly <= 0) return 0;
                        const needed = Math.max(0, this.targetWeeklyIncome - this.gross);
                        return Math.ceil(needed / this.netHourly);
                    },
                    
                    get projectedEarnings() {
                        return this.gross + (this.remainingHours * this.netHourly);
                    },
                    
                    toggleMode() {
                        this.calculationMode = this.calculationMode === 'standard' ? 'advanced' : 'standard';
                        if (this.calculationMode === 'standard') {
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

        <div x-data="calculator()" class="min-h-screen bg-slate-50 relative pb-20">
            
            <!-- VERDICT LAB (Always Visible) -->
            <div class="max-w-4xl mx-auto px-4 py-12 space-y-12">
                
                <!-- Verdict Card -->
                <div class="relative overflow-hidden rounded-3xl bg-white border border-slate-200 shadow-2xl">
                    <div class="bg-slate-50 border-b border-slate-100 p-4 flex justify-between">
                        <div class="flex items-center space-x-2">
                             <span class="w-3 h-3 rounded-full animate-pulse" :class="netHourly < 15 ? 'bg-red-500' : 'bg-emerald-500'"></span>
                             <span class="text-xs font-bold uppercase tracking-widest text-slate-500" x-text="netHourly < 15 ? 'CRITICAL STATUS' : 'STABLE STATUS'"></span>
                        </div>
                    </div>

                    <div class="p-8 md:p-12 text-center space-y-4">
                        <h2 class="text-slate-400 font-medium text-lg uppercase tracking-wide">Your Real Hourly Wage</h2>
                        <div class="flex items-center justify-center gap-2 text-slate-900">
                             <span class="text-6xl md:text-8xl font-black tracking-tighter" :class="netHourly < 0 ? 'text-red-600' : 'text-slate-900'">$</span>
                             <span class="text-6xl md:text-8xl font-black tracking-tighter" :class="netHourly < 0 ? 'text-red-600' : 'text-slate-900'" x-text="netHourly.toFixed(2)"></span>
                             <span class="text-2xl text-slate-400 font-medium">/hr</span>
                        </div>
                         
                        <div class="inline-block px-4 py-2 rounded-xl bg-slate-100 text-slate-600 text-sm font-medium">
                            <span x-show="netHourly < 15">
                                💀 You are making <span class="font-bold text-red-600">$<span x-text="(15 - netHourly).toFixed(2)"></span> LESS</span> than a Burger King employee.
                            </span>
                             <span x-show="netHourly >= 15">
                                🎉 Not bad! You are beating the minimum wage baseline.
                            </span>
                        </div>
                    </div>
                    
                    <!-- Share Bar -->
                    <div class="bg-slate-900 p-4 flex justify-center">
                        <button @click="shareResult()" class="flex items-center space-x-2 text-white font-bold hover:text-emerald-400 transition-colors">
                            <span>📤 Share This Tragedy</span>
                        </button>
                    </div>
                </div>

                <!-- Target Hustle NEW -->
                <div class="bg-gradient-to-r from-emerald-50 to-cyan-50 border-2 border-emerald-200 rounded-3xl p-8 space-y-4">
                    <div class="flex items-center gap-2">
                        <span class="text-2xl">🎯</span>
                        <h3 class="text-xl font-bold text-slate-900">Target Hustle</h3>
                    </div>
                    
                    <div class="space-y-3">
                        <div class="flex justify-between items-center">
                            <span class="text-sm font-medium text-slate-600">Weekly Goal</span>
                            <span class="text-lg font-bold text-emerald-600">$<span x-text="targetWeeklyIncome"></span></span>
                        </div>
                        
                        <input type="range" x-model.number="targetWeeklyIncome" min="500" max="3000" step="50" 
                               class="w-full h-3 bg-emerald-100 rounded-lg appearance-none cursor-pointer accent-emerald-600">
                        
                        <div class="bg-white rounded-xl p-4 border border-emerald-200">
                            <p class="text-slate-600 text-sm mb-2">To reach your goal:</p>
                            <p class="text-2xl font-black text-slate-900">
                                <span x-text="remainingHours"></span> more hours
                            </p>
                            <p class="text-xs text-slate-400 mt-1">at $<span x-text="netHourly.toFixed(2)"></span>/hr = $<span x-text="projectedEarnings.toFixed(2)"></span> total</p>
                        </div>
                    </div>
                </div>

                <!-- Calculation Mode Toggle NEW -->
                <div class="bg-white border border-slate-200 rounded-3xl p-6">
                    <div class="flex items-center justify-between mb-4">
                        <div>
                            <h3 class="text-lg font-bold text-slate-900">Calculation Method</h3>
                            <p class="text-xs text-slate-500">Choose how to calculate vehicle costs</p>
                        </div>
                        <button @click="toggleMode()" 
                                class="px-4 py-2 rounded-lg border-2 transition-all font-medium"
                                :class="calculationMode === 'standard' ? 'border-emerald-500 bg-emerald-50 text-emerald-700' : 'border-slate-300 bg-white text-slate-600 hover:border-slate-400'">
                            <span x-show="calculationMode === 'standard'">📋 IRS Standard</span>
                            <span x-show="calculationMode === 'advanced'">⚙️ My Vehicle</span>
                        </button>
                    </div>
                    
                    <!-- Standard Mode Info -->
                    <div x-show="calculationMode === 'standard'" class="bg-slate-50 rounded-lg p-4">
                        <p class="text-sm text-slate-600">
                            <span class="font-semibold">$0.725/mile</span> (2026 IRS Rate)
                            <span class="cursor-help ml-1" title="Includes gas, maintenance, insurance, depreciation">ℹ️</span>
                        </p>
                        <p class="text-xs text-slate-400 mt-1">Official rate accepted by IRS for tax purposes</p>
                    </div>
                    
                    <!-- Advanced Mode Vehicle Selector -->
                    <div x-show="calculationMode === 'advanced'" class="space-y-4">
                        <div>
                            <label class="block text-sm font-medium text-slate-700 mb-2">Select Your Vehicle</label>
                            <select x-model="selectedVehicleId" 
                                    class="w-full px-4 py-3 rounded-lg border-2 border-slate-200 bg-white focus:border-emerald-500 focus:outline-none">
                                <template x-for="vehicle in vehiclePresets" :key="vehicle.id">
                                    <option :value="vehicle.id" x-text="vehicle.icon + ' ' + vehicle.name"></option>
                                </template>
                            </select>
                            <p class="text-xs text-slate-400 mt-1" x-text="selectedVehicle?.description"></p>
                        </div>
                        
                        <!-- Custom MPG for Custom vehicle -->
                        <div x-show="selectedVehicleId === 'custom'" class="space-y-2">
                            <label class="text-sm font-medium text-slate-700">Your MPG</label>
                            <input type="number" x-model.number="customMpg" min="10" max="60" 
                                   class="w-full px-4 py-2 rounded-lg border-2 border-slate-200 focus:border-emerald-500 focus:outline-none">
                        </div>
                        
                        <!-- Gas Price Adjuster (all vehicles except EV) -->
                        <div x-show="selectedVehicle?.type !== 'electric'" class="space-y-2">
                            <div class="flex justify-between">
                                <label class="text-sm font-medium text-slate-700">Gas Price</label>
                                <span class="text-sm text-emerald-600 font-semibold">$<span x-text="gasPrice.toFixed(2)"></span></span>
                            </div>
                            <input type="range" x-model.number="gasPrice" min="2.00" max="7.00" step="0.10"
                                   class="w-full h-2 bg-slate-100 rounded-lg appearance-none cursor-pointer accent-emerald-600">
                        </div>
                    </div>
                </div>

                <!-- Profit Leak (Updated to use deductionCost) -->
                <div class="bg-white border border-slate-200 rounded-3xl p-8">
                    <h3 class="text-xl font-bold text-slate-900 mb-6 flex items-center gap-2">
                        <span>💸</span>
                        <span>Profit Leak</span>
                    </h3>
                    <div class="space-y-3 text-sm">
                         <div class="flex justify-between items-center text-slate-500">
                             <div class="flex items-center gap-1">
                                 <span>Vehicle Costs</span>
                                 <span class="cursor-help text-xs" title="Gas, maintenance, insurance, depreciation">ℹ️</span>
                             </div>
                             <span class="font-mono text-slate-900 font-bold">-$<span x-text="deductionCost.toFixed(2)"></span></span>
                         </div>
                          <div class="flex justify-between items-center text-slate-500">
                             <span>Tax (15.3%)</span>
                             <span class="font-mono text-slate-900 font-bold">-$<span x-text="taxCost.toFixed(2)"></span></span>
                         </div>
                         <div class="pt-4 border-t border-slate-100 flex justify-between font-black text-lg text-emerald-600">
                             <span>Net Profit</span>
                             <span>$<span x-text="totalNet.toFixed(2)"></span></span>
                         </div>
                    </div>
                </div>

            </div>
        </div>
    `
)
