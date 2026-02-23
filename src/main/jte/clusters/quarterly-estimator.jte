@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new com.gigwager.model.SeoMeta(customTitle, "Estimate your quarterly self-employment taxes easily to avoid IRS penalties. Our estimator makes gig tax prep a breeze.", "https://gigverdict.com/taxes/quarterly-estimator", "https://gigverdict.com/og-image.jpg"),
    content = @`
        <div class="bg-slate-50 min-h-screen py-16 px-6">
            <div class="max-w-3xl mx-auto bg-white p-8 rounded-2xl shadow-sm border border-slate-200 text-center">
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
