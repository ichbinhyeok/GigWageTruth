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
            <!-- Verdict Section -->
            <section class="space-y-6 animate-fade-in-up">
                
                <!-- The Brutal Verdict Card -->
                <div class="relative overflow-hidden rounded-3xl bg-white border border-slate-200 shadow-2xl shadow-slate-200/50">
                    
                    <!-- Top Bar: Status -->
                    <div class="bg-slate-50 border-b border-slate-100 p-4 flex justify-between items-center">
                        <div class="flex items-center space-x-2">
                             <span class="w-3 h-3 rounded-full animate-pulse" 
                                   :class="netHourly < 15 ? 'bg-red-500' : 'bg-emerald-500'"></span>
                             <span class="text-xs font-bold uppercase tracking-widest text-slate-500" x-text="netHourly < 15 ? 'CRITICAL STATUS' : 'STABLE STATUS'"></span>
                        </div>
                        <div class="text-xs font-mono text-slate-400">ID: <span x-text="Math.floor(Math.random() * 99999)"></span></div>
                    </div>

                    <div class="p-8 md:p-12 text-center space-y-4">
                        <h2 class="text-slate-400 font-medium text-lg uppercase tracking-wide">Your Real Hourly Wage</h2>
                        
                        <!-- The BIG Number -->
                        <div class="flex items-baseline justify-center gap-2">
                            <span class="text-6xl md:text-8xl font-black tracking-tighter" 
                                  :class="netHourly < 0 ? 'text-red-600' : 'text-slate-900'">
                                $<span x-text="netHourly"></span>
                            </span>
                            <span class="text-2xl text-slate-400 font-medium">/hr</span>
                        </div>

                        <!-- Comparison -->
                         <div class="inline-block px-4 py-2 rounded-xl bg-slate-100 text-slate-600 text-sm font-medium">
                            <span x-show="netHourly < 15">
                                💀 You are making <span class="font-bold text-red-600">$<span x-text="(15 - netHourly).toFixed(2)"></span> LESS</span> than a Burger King employee.
                            </span>
                             <span x-show="netHourly >= 15">
                                🎉 Not bad! You are beating the minimum wage baseline.
                            </span>
                        </div>
                    </div>

                     <!-- Action Bar -->
                    <div class="bg-slate-900 p-4 flex justify-center">
                        <button @click="shareResult()" class="flex items-center space-x-2 text-white font-bold hover:text-emerald-400 transition-colors">
                            <span>📤 Share This Tragedy</span>
                        </button>

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
