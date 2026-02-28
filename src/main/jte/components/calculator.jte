@import com.gigwager.util.AppConstants
@import gg.jte.Content
@param String appName
@param String appSlug
@param String themeColor
@param String heroBgClass
@param Double initialGross
@param Double initialMiles
@param Double initialHours
@param Double initialGasPrice
@param String defaultVehicle
@param Content content
@param String switchAction
@param String activeTabName
@param String verdictContainerId

<div x-data="window.createGigCalculator({
    appName: '${appName}',
    verdictContainerId: '${verdictContainerId}',
    gross: '${initialGross}',
    miles: '${initialMiles}',
    hours: '${initialHours}',
    gasPrice: '${initialGasPrice}',
    defaultVehicle: '${defaultVehicle}',
    targetWeeklyIncome: 1000,
    shockMessages: ${appName.equals("Uber") ? 
        "{ tier1: [{emoji:'💀',text:\"Less than Target cashier ($15/hr)\"},{emoji:'🩸',text:\"Bleeding equity\"},{emoji:'👮',text:\"Illegal wage\"}], tier2: [{emoji:'⚠️',text:\"Below min wage\"},{emoji:'🍔',text:\"Better off flipping burgers\"},{emoji:'📉',text:\"Not sustainable\"}], tier3: [{emoji:'✅',text:\"Decent side hustle\"},{emoji:'🤔',text:\"Comparable to warehouse\"},{emoji:'🚗',text:\"Okay with cheap car\"}], tier4: [{emoji:'👑',text:\"Top 10% Driver!\"},{emoji:'🚀',text:\"Great profit!\"},{emoji:'🔥',text:\"Crushing it!\"}] }" :
        "{ tier1: [{emoji:'💀',text:\"Less than Target cashier ($15/hr)\"},{emoji:'🩸',text:\"Bleeding equity\"},{emoji:'👮',text:\"Illegal wage\"}], tier2: [{emoji:'⚠️',text:\"Below min wage\"},{emoji:'🍔',text:\"Better off flipping burgers\"},{emoji:'📉',text:\"Not sustainable\"}], tier3: [{emoji:'✅',text:\"Decent side hustle\"},{emoji:'🤔',text:\"Comparable to warehouse\"},{emoji:'🚗',text:\"Okay with cheap car\"}], tier4: [{emoji:'👑',text:\"Top 10% Driver!\"},{emoji:'🚀',text:\"Great profit!\"},{emoji:'🔥',text:\"Crushing it!\"}] }" 
    }
})">

    <!-- Hero Section -->
    <div class="${heroBgClass} pt-16 pb-24 text-center px-6">
        <h1 class="text-3xl md:text-5xl font-bold text-white mb-6 tracking-tight">
            <span x-text="heroTitle"></span>
            <span class="${themeColor == "red" ? "text-red-200" : "text-emerald-400"}" x-show="!heroTitle.includes('Week')">(2026)</span>
        </h1>
        <p class="text-lg md:text-xl ${themeColor == "red" ? "text-white/80" : "text-slate-300"} max-w-2xl mx-auto" x-html="heroSubtitle"></p>
    </div>

    <!-- Calculator UI Container -->
    <div class="bg-slate-50 pb-24 relative z-10 -mt-10 rounded-t-3xl border-t border-slate-200">
        
        <!-- Mobile Sticky Bottom Bar -->
        <div class="sm:hidden fixed bottom-0 left-0 right-0 bg-white/95 backdrop-blur-md border-t border-slate-200 z-50 shadow-[0_-4px_6px_-1px_rgba(0,0,0,0.1)]">
            <div class="px-6 py-4 flex items-center justify-between">
                 <div class="flex items-center gap-2">
                    <div class="w-2 h-2 rounded-full" :class="netHourly < 15 ? 'bg-red-500' : 'bg-green-500'"></div>
                    <span class="text-xs font-bold uppercase tracking-wider text-slate-500">Real Wage</span>
                </div>
                 <span class="text-xl font-bold" :class="netHourly < 0 ? 'text-red-600' : 'text-slate-900'">
                     $<span x-text="netHourly.toFixed(2)"></span>
                </span>
                <button @click="shareResult()" class="ml-4 p-2 text-slate-400 hover:text-slate-900">
                    <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.684 13.342C8.886 12.938 9 12.482 9 12c0-.482-.114-.938-.316-1.342m0 2.684a3 3 0 110-2.684m0 2.684l6.632 3.316m-6.632-6l6.632-3.316m0 0a3 3 0 105.367-2.684 3 3 0 00-5.367 2.684zm0 9.316a3 3 0 105.368 2.684 3 3 0 00-5.368-2.684z"></path></svg>
                </button>
            </div>
        </div>

        <!-- Desktop Sticky Summary Bar (Tablet Only now, hidden on LG desktop where sidebar exists) -->
        <div class="hidden sm:block lg:hidden sticky top-16 bg-white/95 backdrop-blur-md border-b border-slate-200 z-40 transition-all duration-300 shadow-sm">
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
                    <button @click="tweetResult()" class="p-2.5 text-slate-400 hover:text-[#1DA1F2] hover:bg-slate-50 rounded-lg transition" title="Post to X">
                         <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24"><path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z"/></svg>
                    </button>
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
                            <button @click="${activeTabName.equals("uber") ? "" : switchAction}" 
                                class="flex-1 py-3 text-sm font-bold rounded-lg flex items-center justify-center gap-2 transition-all ${activeTabName.equals("uber") ? "bg-white text-slate-900 shadow-sm" : "text-slate-500 hover:text-slate-900 hover:bg-white/50"}">
                                <span>🚗</span> Uber / Lyft
                            </button>
                            <button @click="${activeTabName.equals("doordash") ? "" : switchAction}" 
                                class="flex-1 py-3 text-sm font-bold rounded-lg flex items-center justify-center gap-2 transition-all ${activeTabName.equals("doordash") ? "bg-white text-slate-900 shadow-sm" : "text-slate-500 hover:text-slate-900 hover:bg-white/50"}">
                                <span>🍔</span> DoorDash
                            </button>
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
                                           class="w-full pl-8 pr-4 py-3 bg-slate-50 border border-slate-200 rounded-xl text-lg font-bold text-slate-900 focus:ring-2 focus:ring-${themeColor}-500 focus:border-${themeColor}-500 focus:outline-none transition-all">
                                </div>
                            </div>
                            <!-- Miles Driven -->
                            <div class="space-y-2">
                                <div class="flex justify-between items-end h-5">
                                    <label class="text-xs font-semibold uppercase tracking-wide text-slate-500 leading-none">Miles Driven</label>
                                    <label class="flex items-center gap-2 cursor-pointer group leading-none">
                                        <span class="text-xs text-slate-400 group-hover:text-${themeColor}-600 transition">Round Trip x2</span>
                                        <input type="checkbox" x-model="isRoundTrip" class="rounded border-slate-300 text-${themeColor}-600 focus:ring-${themeColor}-500 w-3.5 h-3.5">
                                    </label>
                                </div>
                                <div class="relative">
                                    <input type="number" x-model="rawMiles" 
                                           class="w-full pl-4 pr-12 py-3 bg-slate-50 border border-slate-200 rounded-xl text-lg font-bold text-slate-900 focus:ring-2 focus:ring-${themeColor}-500 focus:border-${themeColor}-500 focus:outline-none transition-all">
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
                                           class="w-full pl-4 pr-12 py-3 bg-slate-50 border border-slate-200 rounded-xl text-lg font-bold text-slate-900 focus:ring-2 focus:ring-${themeColor}-500 focus:border-${themeColor}-500 focus:outline-none transition-all">
                                    <span class="absolute right-4 top-3.5 text-slate-400 font-medium text-sm">hrs</span>
                                </div>
                                <p class="text-[10px] text-slate-400 leading-tight" x-html="${activeTabName.equals("doordash") ? "'⚠️ Enter \\'Dash Time\\', NOT \\'Active Time\\'. <br> Waiting is work!'" : "'⚠️ Enter full shift duration (Log on to Log off). <br> Do <strong>not</strong> use \\'Active Time\\'.'" }"></p>
                            </div>
                        </div>
                    </div>

                    <!-- Weekly Goal -->
                    <div class="bg-white rounded-2xl p-6 md:p-8 shadow-sm border border-slate-200">
                        <div @click="showTargetHustle = !showTargetHustle" class="flex justify-between items-center cursor-pointer group">
                            <h3 class="text-lg font-bold text-slate-900 flex items-center gap-2">
                                <span class="flex items-center justify-center w-6 h-6 rounded-full bg-slate-100 text-xs text-slate-600">2</span>
                                Set Weekly Goal
                            </h3>
                            <svg class="w-5 h-5 text-slate-400 group-hover:text-${themeColor}-500 transition-transform" :class="{'rotate-180': showTargetHustle}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
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
                                            class="w-full pl-8 pr-4 py-3 bg-slate-50 border border-slate-200 rounded-xl font-bold text-slate-900 focus:ring-2 focus:ring-${themeColor}-500 focus:border-${themeColor}-500">
                                    </div>
                                </div>
                                
                                <div class="bg-slate-50 rounded-xl p-4 flex flex-col justify-center">
                                    <template x-if="targetWeeklyIncome > 0">
                                        <div>
                                            <div class="flex justify-between text-xs font-semibold uppercase text-slate-500 mb-1">
                                                <span>Progress</span>
                                                <span x-text="Math.min(100, Math.round((totalNet / targetWeeklyIncome) * 100)) + '%'"></span>
                                            </div>
                                            <div class="w-full bg-slate-200 rounded-full h-2.5 mb-2">
                                                <div class="bg-${themeColor}-500 h-2.5 rounded-full transition-all duration-500" :style="'width: ' + Math.min(100, (totalNet / targetWeeklyIncome) * 100) + '%'"></div>
                                            </div>
                                            <div class="mt-2 text-xs text-slate-500 bg-slate-100 p-2 rounded-lg">
                                                <span>💡 To pocket <strong>$<span x-text="targetWeeklyIncome"></span></strong>, your <span x-text="selectedApp"></span> Earnings needs to show <strong>$<span x-text="requiredGrossForGoal.toFixed(0)"></span></strong>.</span>
                                                <span class="block text-[10px] text-slate-400 mt-1">(Based on your current expense ratio)</span>
                                            </div>
                                            <p class="text-sm text-slate-600 mt-2" x-text="(targetWeeklyIncome - totalNet) > 0 ? '$' + (targetWeeklyIncome - totalNet).toFixed(0) + ' (' + hoursToGoal.toFixed(1) + ' hrs) to go!' : 'Goal smashed! 🎉'"></p>
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
                            <p class="text-xs text-slate-400 mt-2 max-w-sm mx-auto">Includes gas, depreciation, insurance, and maintenance. Based on standard TCO methodologies.</p>
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
                                               class="w-full pl-8 pr-4 py-3 bg-white border border-slate-300 rounded-xl focus:ring-2 focus:ring-${themeColor}-500 font-bold text-slate-900">
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
                            <svg class="w-5 h-5 text-slate-400 group-hover:text-${themeColor}-500 transition-transform" :class="{'rotate-180': showAdvancedOptions}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                            </svg>
                        </div>

                        <div x-show="showAdvancedOptions" x-collapse class="mt-6 border-t border-slate-100 pt-6">
                            <div class="grid md:grid-cols-2 gap-6">
                                <div>
                                    <label class="block text-xs font-semibold uppercase tracking-wide text-slate-500 mb-2">Active Time (Optional)</label>
                                    <div class="relative">
                                        <input type="number" x-model="rawActiveTime" 
                                            :max="hours"
                                            step="0.1"
                                            class="w-full pr-12 pl-4 py-3 bg-slate-50 border border-slate-200 rounded-xl font-bold text-slate-900 focus:ring-2 focus:ring-${themeColor}-500 focus:border-${themeColor}-500">
                                        <span class="absolute right-4 top-3.5 text-slate-400 font-medium text-sm">hrs</span>
                                    </div>
                                    <p class="text-[10px] text-slate-400 mt-1">Time actually driving/delivering (from your app). Shows "busy hours" illusion.</p>
                                </div>
                                <div>
                                    <label class="block text-xs font-semibold uppercase tracking-wide text-slate-500 mb-2">Cash Tips / Bonuses</label>
                                    <div class="relative">
                                        <span class="absolute left-4 top-3.5 text-slate-400 font-medium">$</span>
                                        <input type="number" x-model="rawTips" 
                                            class="w-full pl-8 pr-4 py-3 bg-slate-50 border border-slate-200 rounded-xl font-bold text-slate-900 focus:ring-2 focus:ring-${themeColor}-500 focus:border-${themeColor}-500">
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
                    <div class="bg-white rounded-2xl p-5 shadow-sm border border-slate-200 sticky top-24">
                        <h3 class="text-base font-bold text-slate-900 mb-4">Weekly Breakdown</h3>
                        
                        <div class="space-y-3">
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

                            <div class="flex justify-between items-center py-2 border-b border-slate-100">
                                <div class="flex flex-col">
                                    <span class="text-slate-600">Est. Taxes</span>
                                    <span class="text-xs text-slate-400">15.3% SE Tax</span>
                                </div>
                                <span class="font-bold text-red-500">-$<span x-text="taxCost.toFixed(2)"></span></span>
                            </div>
                            <div class="flex justify-between items-center pt-2">
                                <span class="text-lg font-bold text-slate-900">Net Profit</span>
                                <span class="text-xl font-bold" :class="(totalNet >= 0) ? 'text-emerald-600' : 'text-red-500'">$<span x-text="totalNet.toFixed(2)"></span></span>
                            </div>
                        </div>

                        <div class="mt-6 bg-slate-50 rounded-xl p-4 border border-slate-200">
                            <div class="flex justify-between items-center mb-1">
                                <span class="text-xs font-bold uppercase text-slate-500">Real Hourly Wage</span>
                            </div>
                            <div class="flex items-baseline gap-1 mb-3">
                                <span class="text-4xl font-bold text-slate-900">$<span x-text="netHourly.toFixed(2)"></span></span>
                                <span class="text-sm font-medium text-slate-400">/hr</span>
                            </div>

                             <!-- W-2 Equivalent (New Feature) -->
                            <div class="mb-3 flex items-center justify-between text-xs bg-slate-100 p-2 rounded-lg border border-slate-200">
                                <span class="font-semibold text-slate-600">🆚 W-2 Job Equiv:</span>
                                <span class="font-bold text-slate-800">$<span x-text="w2Equivalent.toFixed(2)"></span>/hr</span>
                            </div>
                            <p class="text-[10px] text-slate-400 mb-3 leading-tight">
                                *Comparing to a job with benefits (Health, PTO), your gig wage is worth less.
                            </p>

                             <!-- Wait Time Analysis (Only if wait time entered) -->
                            <template x-if="waitTime > 0">
                                <div class="mb-4 bg-orange-50 rounded-lg p-3 border border-orange-100">
                                    <div class="flex justify-between items-center mb-1">
                                        <span class="text-xs font-bold uppercase text-orange-600">The "Active" Illusion</span>
                                    </div>
                                    <div class="flex justify-between items-end">
                                        <div class="text-right">
                                            <span class="block text-xs text-slate-500">Only Driving</span>
                                            <span class="block font-bold text-slate-900">$<span x-text="activeHourly.toFixed(2)"></span>/hr</span>
                                        </div>
                                        <div class="text-xs text-orange-400 font-bold px-2 mb-1">vs</div>
                                        <div>
                                            <span class="block text-xs text-slate-500">Real Reality</span>
                                            <span class="block font-bold text-red-600">$<span x-text="netHourly.toFixed(2)"></span>/hr</span>
                                        </div>
                                    </div>
                                    <p class="text-[10px] text-orange-700 mt-2 leading-tight">
                                        You spent <strong x-text="waitTime"></strong> hrs waiting for free. That unpaid time dragged your wage down by $<span x-text="(activeHourly - netHourly).toFixed(2)"></span>/hr.
                                    </p>
                                </div>
                            </template>

                             <!-- Shock Message (Verdict) - Redesigned as a "Fact Check Box" -->
                            <div class="mt-4 rounded-lg p-4 border transition-colors duration-300" 
                                 :class="netHourly < 15 ? 'bg-red-100 border-red-200' : 'bg-emerald-100 border-emerald-200'">
                                <div class="flex items-center gap-2 mb-2">
                                    <svg x-show="netHourly < 15" class="w-5 h-5 text-red-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/></svg>
                                    <svg x-show="netHourly >= 15" class="w-5 h-5 text-emerald-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
                                    <span class="text-xs font-black uppercase tracking-widest" :class="netHourly < 15 ? 'text-red-700' : 'text-emerald-700'">Verdict</span>
                                </div>
                                <p class="text-base font-bold leading-snug" 
                                   :class="netHourly < 15 ? 'text-red-900' : 'text-emerald-900'" 
                                   x-text="shockMessage.text">
                                </p>
                            </div>

                            <div class="mt-4 bg-white rounded-lg p-4 border border-slate-200">
                                <h4 class="text-xs font-black uppercase tracking-widest text-slate-700 mb-3">Recommended Next Step</h4>
                                <template x-if="netHourly < 12">
                                    <a :href="'/vehicle-cost/cost-per-mile?app=' + selectedApp + '&miles=' + miles"
                                       class="block w-full text-center px-3 py-2.5 rounded-lg bg-red-600 text-white text-sm font-bold hover:bg-red-700 transition">
                                        1) Fix cost per mile first
                                    </a>
                                </template>
                                <template x-if="netHourly >= 12 && netHourly < 18">
                                    <a :href="'/taxes/quarterly-estimator?app=' + selectedApp + '&gross=' + gross"
                                       class="block w-full text-center px-3 py-2.5 rounded-lg bg-amber-600 text-white text-sm font-bold hover:bg-amber-700 transition">
                                        1) Lock your tax buffer now
                                    </a>
                                </template>
                                <template x-if="netHourly >= 18">
                                    <a :href="'/blog/multi-apping-guide?app=' + selectedApp"
                                       class="block w-full text-center px-3 py-2.5 rounded-lg bg-emerald-600 text-white text-sm font-bold hover:bg-emerald-700 transition">
                                        1) Scale with multi-app strategy
                                    </a>
                                </template>
                                <div class="grid grid-cols-2 gap-2 mt-2">
                                    <a :href="'/taxes/quarterly-estimator?app=' + selectedApp + '&gross=' + gross"
                                       class="text-center px-3 py-2 rounded-lg border border-slate-300 text-xs font-semibold text-slate-700 hover:bg-slate-50 transition">
                                        Tax plan
                                    </a>
                                    <a :href="'/insurance/rideshare-basics?app=' + selectedApp"
                                       class="text-center px-3 py-2 rounded-lg border border-slate-300 text-xs font-semibold text-slate-700 hover:bg-slate-50 transition">
                                        Coverage check
                                    </a>
                                </div>
                            </div>

                            <div class="mt-3 bg-slate-50 rounded-lg p-4 border border-slate-200">
                                <h4 class="text-xs font-black uppercase tracking-widest text-slate-700 mb-2">Standard Assumptions</h4>
                                <ul class="text-xs text-slate-600 space-y-1">
                                    <li>IRS mileage proxy: <strong>$0.725/mi</strong> (2026).</li>
                                    <li>Self-employment tax estimate: <strong>15.3%</strong>.</li>
                                    <li>Hourly base uses <strong>total online time</strong> (not active time).</li>
                                </ul>
                                <p class="text-[10px] text-slate-400 mt-2">Educational estimate only, not tax or legal advice.</p>
                            </div>
                        </div>

                    </div>
                </div>
                
            </div>
        </div>

        <!-- Render Content Param (Verdict + FAQ) -->
        ${content}

    </div>

    <!-- Viral Share Modal (Using Theme Color? keeping defaults for now) -->
    <div x-show="showShareModal" 
         x-transition:enter="transition ease-out duration-200"
         x-transition:enter-start="opacity-0"
         x-transition:enter-end="opacity-100"
         x-transition:leave="transition ease-in duration-150"
         x-transition:leave-start="opacity-100"
         x-transition:leave-end="opacity-0"
         style="display: none;"
         class="fixed inset-0 z-[60] flex items-center justify-center px-4">
        
        <div @click="showShareModal = false" class="absolute inset-0 bg-slate-900/60 backdrop-blur-sm"></div>
        
        <div class="relative bg-white rounded-2xl shadow-2xl max-w-md w-full p-6 overflow-hidden">
            <button @click="showShareModal = false" class="absolute top-4 right-4 text-slate-400 hover:text-slate-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
            </button>

            <div class="text-center mb-6">
                <div class="inline-flex items-center justify-center w-12 h-12 rounded-full bg-${themeColor}-100 text-${themeColor}-600 mb-4">
                    <span class="text-2xl">📢</span>
                </div>
                <h3 class="text-xl font-bold text-slate-900">Share the Truth</h3>
                <p class="text-slate-500 mt-1">Help other drivers see their real wage.</p>
            </div>

            <div class="space-y-3">
                <button @click="shareTo('twitter')" class="w-full flex items-center justify-center gap-3 px-4 py-3 bg-slate-900 hover:bg-slate-800 text-white rounded-xl font-semibold transition group">
                    <svg class="w-5 h-5 text-slate-400 group-hover:text-white transition" fill="currentColor" viewBox="0 0 24 24"><path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z"/></svg>
                    Post to X (Twitter)
                </button>
                <button @click="shareTo('facebook')" class="w-full flex items-center justify-center gap-3 px-4 py-3 bg-[#1877F2] hover:bg-[#166fe5] text-white rounded-xl font-semibold transition">
                     <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24"><path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.791-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"/></svg>
                    Share on Facebook
                </button>
                <button @click="shareTo('reddit')" class="w-full flex items-center justify-center gap-3 px-4 py-3 bg-[#FF4500] hover:bg-[#e03d00] text-white rounded-xl font-semibold transition">
                    <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10"/><path fill="#fff" d="M16.67,13.13C18.04,14.06,19,15.32,19,17C19,19.76,15.87,22,12,22C8.13,22,5,19.76,5,17C5,15.32,5.96,14.06,7.33,13.13C7.33,13.13,7.33,13.13,7.33,13.13C7.14,12.57,7.21,11.97,7.56,11.45C7.99,10.82,8.83,10.66,9.46,11.08C9.55,11.14,9.63,11.21,9.7,11.29C10.37,11.15,11.12,11.07,11.9,11.07C11.93,11.07,11.97,11.07,12,11.07C12.78,11.07,13.53,11.15,14.2,11.29C14.28,11.21,14.36,11.14,14.44,11.08C15.07,10.66,15.91,10.82,16.34,11.45C16.69,11.97,16.76,12.57,16.57,13.13C16.67,13.13,16.67,13.13,16.67,13.13ZM12,20.47C14.97,20.47,17.38,18.91,17.38,17C17.38,15.09,14.97,13.53,12,13.53C9.03,13.53,6.62,15.09,6.62,17C6.62,18.91,9.03,20.47,12,20.47ZM12,16.23C12.52,16.23,12.94,15.81,12.94,15.29C12.94,14.77,12.52,14.35,12,14.35C11.48,14.35,11.06,14.77,11.06,15.29C11.06,15.81,11.48,16.23,12,16.23ZM9.03,16.23C9.55,16.23,9.97,15.81,9.97,15.29C9.97,14.77,9.55,14.35,9.03,14.35C8.51,14.35,8.09,14.77,8.09,15.29C8.09,15.81,8.51,16.23,9.03,16.23ZM14.97,16.23C15.49,16.23,15.91,15.81,15.91,15.29C15.91,14.77,15.49,14.35,14.97,14.35C14.45,14.35,14.03,14.77,14.03,15.29C14.03,15.81,14.45,16.23,14.97,16.23Z"/></svg>
                    Share on Reddit
                </button>
                 <button @click="shareTo('copy')" class="w-full flex items-center justify-center gap-3 px-4 py-3 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-xl font-semibold transition">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z"></path></svg>
                    Copy Link
                </button>
            </div>
        </div>
    </div>
</div>
