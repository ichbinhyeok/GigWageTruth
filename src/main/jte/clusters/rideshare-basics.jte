@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new com.gigwager.model.SeoMeta(customTitle, "Find out if you have an insurance GAP as a rideshare or delivery driver. Avoid claim denials.", com.gigwager.util.AppConstants.BASE_URL + "/insurance/rideshare-basics", com.gigwager.util.AppConstants.BASE_URL + "/og-image.jpg"),
    content = @`
        <div class="bg-slate-50 min-h-screen py-16 px-6">
            <div class="max-w-3xl mx-auto bg-white p-8 rounded-2xl shadow-sm border border-slate-200 text-center">
                <div class="inline-flex bg-emerald-100 text-emerald-600 p-4 rounded-full mb-6">
                    <svg class="w-12 h-12" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" /></svg>
                </div>
                <h1 class="text-4xl font-bold text-slate-900 mb-4">Rideshare Insurance Basics</h1>
                <p class="text-xl text-slate-600 mb-8">Are you legally covered if you crash tonight?</p>
                
                <div class="bg-red-50 p-6 rounded-xl border border-red-200 text-left mb-8">
                    <h3 class="text-lg font-bold text-red-800 mb-2">The GAP Problem (Period 1)</h3>
                    <p class="text-red-700 font-medium mb-4">Most drivers don't know they are severely underinsured the moment they turn the app ON, but haven't accepted a ride yet.</p>
                    <p class="text-red-700">Your personal insurance (Geico, State Farm, Allstate) officially <strong>nullifies</strong> your coverage the moment you switch any gig app to "online". The apps (Uber/Lyft) provide State Minimum coverage during this period (no collision) unless you have a rideshare endorsement.</p>
                </div>
                
                <div class="bg-slate-50 p-6 rounded-xl border-2 border-slate-100 mb-8 mt-8">
                    <h2 class="text-xl font-bold text-slate-900 mb-2">Get the endorsement!</h2>
                    <p class="text-slate-600 mb-4">A rideshare endorsement typically costs $10-$25/month. It is an important consideration for your financial safety if you cause an accident between requests. Coverage depends on insurer and state. Verify policy terms.</p>
                    <a href="/insurance" class="inline-block px-6 py-3 bg-emerald-600 hover:bg-emerald-700 text-white font-bold rounded-lg transition-colors">See Complete Insurance Guide</a>
                </div>
            </div>
        </div>
    `
)
