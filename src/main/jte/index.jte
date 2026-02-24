@import com.gigwager.util.AppConstants
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new com.gigwager.model.SeoMeta(
        "GigWageTruth - The Only Real Net Profit Calculator for Gig Workers",
        "Stop driving blind. Calculate your net hourly wage for Uber, Lyft, and DoorDash after hidden costs.",
        AppConstants.BASE_URL,
        AppConstants.BASE_URL + "/og-image.jpg"
    ),
    content = @`
        <div class="lg:h-[calc(100vh-4rem)] flex flex-col lg:flex-row overflow-hidden">
            
            <!-- LEFT COLUMN: The Hook (Text) -->
            <!-- Order-1 on mobile (appears first), Order-1 on desktop (Left) -->
            <div class="order-1 lg:order-1 lg:w-1/2 bg-slate-50 border-r border-slate-200 p-8 lg:p-20 flex flex-col justify-end relative">
                <!-- Decorative Abstract shapes -->
                 <div class="absolute top-0 left-0 w-full h-full overflow-hidden flex items-center justify-center opacity-80 pointer-events-none">
                     <canvas
                         id="cobe"
                         style="width: 100%; height: 100%; max-width: 600px; max-height: 600px;"
                         width="1000"
                         height="1000"
                     ></canvas>
                </div>
                
                <script type="module">
                    import { initGlobe } from '/js/globe-component.js';
                    const canvas = document.getElementById('cobe');
                    initGlobe(canvas);
                </script>

                <div class="relative z-10 space-y-6">
                    <h1 class="text-6xl lg:text-8xl font-black tracking-tighter text-slate-900 leading-[0.9]">
                        DON'T<br>
                        DRIVE<br>
                        <span class="text-emerald-600">BLIND.</span>
                    </h1>
                    <p class="text-xl lg:text-2xl font-medium text-slate-500 max-w-md leading-relaxed">
                        You see revenue. We see the truth.<br>
                        Find your <span class="text-emerald-600 font-bold">Real Hourly Wage</span> in seconds.
                    </p>
                    
                    <div class="pt-8 flex items-center space-x-6 opacity-60 grayscale hover:grayscale-0 transition-all duration-500">
                         <span class="text-sm font-bold text-slate-400 uppercase tracking-widest">Powered by</span>
                         <span class="font-bold text-slate-600">2026 IRS Data</span>
                         <span class="font-bold text-slate-600">BLS.gov</span>
                    </div>
                </div>
            </div>

            <!-- RIGHT COLUMN: The Gateway (Input Form) -->
            <!-- Order-2 on mobile (appears below hero), Order-2 on desktop (Right) -->
            <div class="order-2 lg:order-2 lg:w-1/2 bg-white p-6 lg:p-20 flex items-center justify-center overflow-y-auto">
                <div class="w-full max-w-lg space-y-8">
                    
                    <!-- Form Container -->
                    <div x-data="{ app: 'rideshare', actionUrl: '/uber' }" class="bg-white rounded-3xl border-2 border-slate-100 shadow-2xl shadow-slate-200/50 p-6 md:p-10 relative">
                        
                        <!-- Trust Badge -->
                        <div class="absolute -top-4 left-1/2 -translate-x-1/2 bg-slate-900 text-white text-[10px] font-bold px-3 py-1 rounded-full uppercase tracking-widest shadow-lg flex items-center gap-1 whitespace-nowrap">
                            <span>🔒</span> 100% Anonymous & No Sign-up
                        </div>

                        <!-- App Tabs -->
                        <div class="grid grid-cols-2 gap-2 mb-8 p-1 bg-slate-100 rounded-xl">
                            <button @click="app = 'rideshare'; actionUrl = '/uber'" 
                                    :class="app === 'rideshare' ? 'bg-white text-slate-900 shadow-sm' : 'text-slate-500 hover:text-slate-700'"
                                    class="py-2.5 text-sm font-bold rounded-lg transition-all flex items-center justify-center gap-2">
                                <span>🚗</span> Uber / Lyft
                            </button>
                            <button @click="app = 'delivery'; actionUrl = '/doordash'" 
                                    :class="app === 'delivery' ? 'bg-white text-slate-900 shadow-sm' : 'text-slate-500 hover:text-slate-700'"
                                    class="py-2.5 text-sm font-bold rounded-lg transition-all flex items-center justify-center gap-2">
                                <span>🍔</span> DoorDash
                            </button>
                        </div>

                        <form :action="actionUrl" method="GET" class="space-y-6">
                            
                            <!-- Total Earnings -->
                            <div class="space-y-2">
                                <label class="text-xs font-bold text-slate-900 uppercase tracking-wide">Total Weekly Earnings (Payout)</label>
                                <p class="text-xs text-slate-500">Week, month, or any period</p>
                                <div class="relative group">
                                    <span class="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 text-lg font-medium group-focus-within:text-emerald-600 transition-colors">$</span>
                                    <input type="number" inputmode="decimal" name="gross" required placeholder="1000" class="w-full bg-slate-50 border border-slate-200 rounded-xl py-4 pl-10 pr-4 text-xl font-bold text-slate-900 outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-all placeholder:text-slate-300">
                                </div>
                            </div>

                            <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
                                <!-- Miles -->
                                <div class="space-y-2">
                                    <div class="flex justify-between items-baseline">
                                        <label class="text-xs font-bold text-slate-900 uppercase tracking-wide">Miles Driven</label>
                                    </div>
                                    <div class="relative group">
                                        <input type="number" inputmode="decimal" name="miles" required placeholder="800" class="w-full bg-slate-50 border border-slate-200 rounded-xl py-4 px-4 text-xl font-bold text-slate-900 outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-all placeholder:text-slate-300">
                                        <span class="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 text-sm font-medium">mi</span>
                                    </div>
                                </div>

                                <!-- Hours -->
                                <div class="space-y-2">
                                    <div class="flex justify-between items-baseline">
                                        <label class="text-xs font-bold text-slate-900 uppercase tracking-wide">Total Online Time</label>
                                        <span class="text-[10px] text-red-500 font-bold">NO Active Time</span>
                                    </div>
                                    <div class="relative group">
                                        <input type="number" inputmode="decimal" name="hours" required placeholder="40" class="w-full bg-slate-50 border border-slate-200 rounded-xl py-4 px-4 text-xl font-bold text-slate-900 outline-none focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-all placeholder:text-slate-300">
                                        <span class="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 text-sm font-medium">hr</span>
                                    </div>
                                </div>
                            </div>

                            <!-- Helper: Where to find -->
                            <div class="bg-emerald-50 border border-emerald-200 rounded-lg p-3">
                                <p class="text-xs text-emerald-800">
                                    <span class="font-bold">💡 Where to find:</span> 
                                    <span x-text="app === 'rideshare' ? 'Uber/Lyft app → Earnings tab' : 'Earnings tab → Dash Time (Not Active Time)'"></span>
                                </p>
                            </div>

                            <button type="submit" class="w-full bg-emerald-600 hover:bg-emerald-700 text-white font-bold text-xl py-5 rounded-xl shadow-lg shadow-emerald-200 transform transition active:scale-[0.98] hover:-translate-y-1">
                                Calculate My Truth &rarr;
                            </button>
                        </form>
                        
                        <div class="pt-4 text-center">
                            <p class="text-[10px] text-slate-400 font-medium">
                                ⚡ Calculated instantly. No email required. No BS.
                            </p>
                        </div>

                    </div>
                    
                    <!-- Mobile only divider text -->
                    <div class="lg:hidden text-center text-slate-300 text-xs font-medium uppercase tracking-widest">
                        &darr; Why check? &darr;
                    </div>

                </div>
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
            "name": "Home"
          }]
        }
        </script>
    `
)
