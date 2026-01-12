@template.layout.main(
    seoMeta = new com.gigwager.model.SeoMeta(
        "GigWageTruth - The Only Real Net Profit Calculator for Gig Workers",
        "Stop driving blind. Calculate your true hourly wage for Uber, Lyft, and DoorDash after hidden costs.",
        "https://gigwagetruth.com",
        "https://gigwagetruth.com/og-image.jpg"
    ),
    content = @`
        <div class="space-y-16 py-12">
            
            <!-- Hero Section -->
            <section class="text-center space-y-6 max-w-2xl mx-auto">
                <div class="inline-block px-4 py-1.5 rounded-full border border-emerald-500/30 bg-emerald-500/10 text-emerald-400 text-xs font-bold tracking-widest uppercase mb-4">
                    The Reality Check Engine
                </div>
                <h1 class="text-5xl md:text-7xl font-black tracking-tighter text-white leading-tight">
                    DON'T DRIVE <br>
                    <span class="text-transparent bg-clip-text bg-gradient-to-r from-emerald-400 to-cyan-500">BLIND.</span>
                </h1>
                <p class="text-xl text-gray-400 leading-relaxed">
                    Apps show you "Revenue". We show you "Profit".<br>
                    With gas, taxes, and depreciation ($0.67/mile), you might be earning less than minimum wage.
                </p>
            </section>

            <!-- Ad Space -->
             <div class="w-full max-w-2xl mx-auto p-4 bg-gray-800/50 border border-gray-700 border-dashed rounded-lg text-center text-gray-500 text-xs tracking-widest uppercase">
                AD SPACE (HOME HERO)
            </div>

            <!-- Platform Selector Grid -->
            <section class="grid grid-cols-1 md:grid-cols-3 gap-6">
                
                <!-- Uber Card (Active) -->
                <a href="/uber" class="group relative bg-gray-800 rounded-3xl p-8 border border-gray-700 hover:border-emerald-500 transition-all duration-300 hover:shadow-2xl hover:shadow-emerald-500/20 flex flex-col items-center text-center">
                    <div class="w-16 h-16 bg-black rounded-2xl flex items-center justify-center text-3xl mb-6 shadow-inner group-hover:scale-110 transition-transform">
                        🚗
                    </div>
                    <h3 class="text-2xl font-bold text-white mb-2">Uber / Lyft</h3>
                    <p class="text-gray-400 text-sm mb-6">Rideshare specialized calculator. Includes deadhead miles usage.</p>
                    <div class="mt-auto px-6 py-2 bg-emerald-600 group-hover:bg-emerald-500 text-white font-bold rounded-lg transition-colors w-full">
                        Calculate Now &rarr;
                    </div>
                </a>

                <!-- DoorDash Card (Coming Soon) -->
                <div class="relative bg-gray-800/50 rounded-3xl p-8 border border-gray-800 flex flex-col items-center text-center opacity-70">
                    <div class="absolute top-4 right-4 text-[10px] font-bold bg-gray-700 text-gray-300 px-2 py-1 rounded">SOON</div>
                    <div class="w-16 h-16 bg-red-900/20 rounded-2xl flex items-center justify-center text-3xl mb-6 grayscale">
                        🍔
                    </div>
                    <h3 class="text-2xl font-bold text-gray-500 mb-2">DoorDash</h3>
                    <p class="text-gray-600 text-sm mb-6">Food delivery specific. Multi-app considerations coming soon.</p>
                    <div class="mt-auto px-6 py-2 bg-gray-700 text-gray-500 font-bold rounded-lg w-full cursor-not-allowed">
                        Coming Soon
                    </div>
                </div>

                <!-- Instacart Card (Coming Soon) -->
                <div class="relative bg-gray-800/50 rounded-3xl p-8 border border-gray-800 flex flex-col items-center text-center opacity-70">
                     <div class="absolute top-4 right-4 text-[10px] font-bold bg-gray-700 text-gray-300 px-2 py-1 rounded">SOON</div>
                    <div class="w-16 h-16 bg-orange-900/20 rounded-2xl flex items-center justify-center text-3xl mb-6 grayscale">
                        🛒
                    </div>
                    <h3 class="text-2xl font-bold text-gray-500 mb-2">Instacart</h3>
                    <p class="text-gray-600 text-sm mb-6">Shopping time vs Driving time analysis.</p>
                    <div class="mt-auto px-6 py-2 bg-gray-700 text-gray-500 font-bold rounded-lg w-full cursor-not-allowed">
                         Coming Soon
                    </div>
                </div>

            </section>
        
             <!-- Trust Signals -->
            <section class="border-t border-gray-800 pt-12 text-center">
                 <p class="text-gray-500 text-sm font-mono uppercase tracking-widest mb-6">Powered By Data From</p>
                 <div class="flex justify-center space-x-8 opacity-50 grayscale hover:grayscale-0 transition-all duration-500">
                     <span class="text-xl font-black text-gray-300">IRS.gov</span>
                     <span class="text-xl font-black text-gray-300">AAA</span>
                     <span class="text-xl font-black text-gray-300">Bureau of Labor Statistics</span>
                 </div>
            </section>

        </div>
    `
)
