@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new com.gigwager.model.SeoMeta(customTitle, "Estimate how much your car costs to run per mile. Avoid the depreciation trap.", com.gigwager.util.AppConstants.BASE_URL + "/vehicle-cost/cost-per-mile", com.gigwager.util.AppConstants.BASE_URL + "/og-image.jpg"),
    content = @`
        <div class="bg-slate-50 min-h-screen py-16 px-6">
            <div class="max-w-3xl mx-auto bg-white p-8 rounded-2xl shadow-sm border border-slate-200 text-center">
                <div class="inline-flex bg-blue-100 text-blue-600 p-4 rounded-full mb-6">
                    <svg class="w-12 h-12" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.042A8.967 8.967 0 006 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 016 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 016-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0018 18a8.967 8.967 0 00-6 2.292m0-14.25v14.25" /></svg>
                </div>
                <h1 class="text-4xl font-bold text-slate-900 mb-4">True Cost Per Mile</h1>
                <p class="text-xl text-slate-600 mb-8">Stop treating your gas limit as your total cost.</p>
                
                <div class="grid md:grid-cols-3 gap-6 mb-12">
                   <div class="bg-slate-50 p-6 rounded-xl border border-slate-200 text-center">
                       <h3 class="font-bold text-slate-800">Depreciation</h3>
                       <p class="text-3xl text-slate-900 font-extrabold my-2">$0.18</p>
                       <p class="text-xs text-slate-500">Invisible loss per mile</p>
                   </div>
                   <div class="bg-slate-50 p-6 rounded-xl border border-slate-200 text-center">
                       <h3 class="font-bold text-slate-800">Maintenance</h3>
                       <p class="text-3xl text-slate-900 font-extrabold my-2">$0.09</p>
                       <p class="text-xs text-slate-500">Tires, brakes, oil</p>
                   </div>
                   <div class="bg-slate-50 p-6 rounded-xl border border-slate-200 text-center">
                       <h3 class="font-bold text-slate-800">Gas & Oil</h3>
                       <p class="text-3xl text-slate-900 font-extrabold my-2">$0.15</p>
                       <p class="text-xs text-slate-500">Direct wallet hit</p>
                   </div>
                </div>

                <div class="bg-blue-50 p-6 rounded-xl border border-blue-200 text-left mb-8">
                    <p class="text-blue-800 font-medium mb-4">For most sedans, you are losing <strong>around 45 cents to 70 cents</strong> per mile driven. For 2026, the IRS standard mileage rate is 72.5¢ per mile. We use this as an estimation proxy, not tax advice. It is actually highly accurate to the real cost if you use a depreciating asset.</p>
                </div>
                
                <a href="/vehicle-cost" class="inline-block px-6 py-3 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-lg transition-colors shadow shadow-blue-500/30">View Vehicle Strategy Guide</a>
            </div>
        </div>
    `
)
