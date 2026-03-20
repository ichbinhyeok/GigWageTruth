@param String app = "uber"
@param String appName = "Uber"

<!-- Hub Navigation Spider Web -->
<section class="my-12">
    <h3 class="text-2xl font-bold text-slate-900 mb-6">Explore the Toolkit and Directory</h3>
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4">
        <a href="/salary/${app}" class="bg-slate-50 hover:bg-emerald-50 p-5 rounded-xl border border-slate-200 hover:border-emerald-300 transition-colors group">
            <h4 class="font-bold text-slate-900 mb-1 group-hover:text-emerald-700">${appName} Pay by City Hub</h4>
            <p class="text-xs text-slate-500">Open the hub that links every city report.</p>
        </a>
        <a href="/best-cities/${app}" class="bg-slate-50 hover:bg-emerald-50 p-5 rounded-xl border border-slate-200 hover:border-emerald-300 transition-colors group">
            <h4 class="font-bold text-slate-900 mb-1 group-hover:text-emerald-700">Best Cities Ranking</h4>
            <p class="text-xs text-slate-500">See which markets rank highest by net hourly pay.</p>
        </a>
        <a href="/taxes" class="bg-slate-50 hover:bg-emerald-50 p-5 rounded-xl border border-slate-200 hover:border-emerald-300 transition-colors group">
            <h4 class="font-bold text-slate-900 mb-1 group-hover:text-emerald-700">Tax Guide</h4>
            <p class="text-xs text-slate-500">How to handle 1099 and deductions.</p>
        </a>
        <a href="/insurance" class="bg-slate-50 hover:bg-emerald-50 p-5 rounded-xl border border-slate-200 hover:border-emerald-300 transition-colors group">
            <h4 class="font-bold text-slate-900 mb-1 group-hover:text-emerald-700">Insurance Basics</h4>
            <p class="text-xs text-slate-500">Protect yourself from expensive coverage gaps.</p>
        </a>
        <a href="/vehicle-cost/cost-per-mile" class="bg-slate-50 hover:bg-emerald-50 p-5 rounded-xl border border-slate-200 hover:border-emerald-300 transition-colors group">
            <h4 class="font-bold text-slate-900 mb-1 group-hover:text-emerald-700">Cost Per Mile</h4>
            <p class="text-xs text-slate-500">The hidden cost of driving.</p>
        </a>
    </div>
    <div class="mt-6 text-center">
        <a href="/salary/directory" class="inline-block px-6 py-3 bg-slate-900 text-white font-bold rounded-lg hover:bg-slate-800 transition-colors">See the Full Local Directory</a>
    </div>
</section>
