@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new com.gigwager.model.SeoMeta(customTitle, "Estimate your quarterly self-employment taxes easily to avoid IRS penalties. Our estimator makes gig tax prep a breeze.", com.gigwager.util.AppConstants.BASE_URL + "/taxes/quarterly-estimator", com.gigwager.util.AppConstants.BASE_URL + "/og-image.jpg"),
    content = @`
        <div class="bg-slate-50 min-h-screen py-16 px-6" x-data="{
            params: new URLSearchParams(window.location.search),
            get appName() { return this.params.get('app'); },
            get gross() { return parseInt(this.params.get('gross')) || 0; },
            get estimatedQuarterly() { return (this.gross * 13 * 0.153).toFixed(0); },
            get showPersonalizedBanner() { return this.gross > 0 && this.appName != null; }
        }">
            <div class="max-w-3xl mx-auto bg-white p-8 rounded-2xl shadow-sm border border-slate-200 text-center relative overflow-hidden">
                <div class="absolute top-0 left-0 w-full h-2 bg-red-500"></div>
                
                <!-- Phase 3-2: Dynamic CTA Trigger Banner -->
                <div x-show="showPersonalizedBanner" x-cloak class="mt-4 mb-8 bg-amber-50 border border-amber-200 rounded-2xl p-6 shadow-sm text-left relative overflow-hidden">
                    <div class="absolute top-0 left-0 w-2 h-full bg-amber-400"></div>
                    <div class="flex items-start gap-4">
                        <div class="bg-amber-100 p-2 rounded-full text-amber-600 shrink-0 mt-1">
                            <span class="text-xl">⚠️</span>
                        </div>
                        <div>
                            <h3 class="text-lg font-bold text-amber-900 mb-1">Your <span class="capitalize" x-text="appName"></span> Tax Warning</h3>
                            <p class="text-amber-800 text-sm">Based on your weekly gross of <strong><span x-text="gross ? '$' + gross : '$500'">$500</span></strong>, you could owe roughly <strong><span x-text="estimatedQuarterly ? '$' + estimatedQuarterly : '$995'">$995</span></strong> to the IRS every quarter. Scroll down to see how to avoid underpayment penalties.</p>
                        </div>
                    </div>
                </div>

                <div class="inline-flex bg-red-100 text-red-600 p-4 rounded-full mb-6">
                    <svg class="w-12 h-12" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                </div>
                <h1 class="text-4xl font-bold text-slate-900 mb-4">Quarterly Tax Estimator</h1>
                <p class="text-xl text-slate-600 mb-8">Stop guessing at the end of the year. Avoid surprise IRS underpayment penalties.</p>
                
                <div class="bg-slate-50 p-6 rounded-xl border border-slate-200 text-left mb-8">
                    <p class="text-slate-700 font-medium mb-4">If you drive for Uber, DoorDash, or Lyft, you are required to pay the IRS every quarter if you expect to owe more than $1,000 in taxes for the year (which is almost every driver earning over $6,500/year in net profit).</p>
                    <ul class="list-disc pl-5 text-slate-600 space-y-2 mb-4">
                        <li>Q1 Due: April 15</li>
                        <li>Q2 Due: June 15</li>
                        <li>Q3 Due: Sept 15</li>
                        <li>Q4 Due: Jan 15</li>
                    </ul>
                </div>
                
                <!-- Simple Lead Magnet or Tool Placeholder -->
                <div class="bg-red-50 p-6 rounded-xl border-2 border-red-100 mb-8">
                    <h2 class="text-xl font-bold text-red-900 mb-2">Automate Your Estimates</h2>
                    <p class="text-red-700 mb-4">Our automated calculator spreadsheet will be releasing shortly. Join our list to get it for free.</p>
                    <a href="/taxes" class="inline-block px-6 py-3 bg-red-600 hover:bg-red-700 text-white font-bold rounded-lg transition-colors">Read Full Tax Guide</a>
                </div>
            </div>
        </div>
    `
)
