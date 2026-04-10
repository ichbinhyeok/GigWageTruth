@param String app = "uber"
@param String appName = "Uber"

<!-- Hub Navigation Spider Web -->
<section class="my-12">
    <h3 class="text-2xl font-bold text-slate-900 mb-2">Explore the Decision Toolkit</h3>
    <p class="text-sm text-slate-500 mb-6">Use the tools first, then open city pages only after you know what question you are trying to answer.</p>
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4">
        <a href="/${app}" class="bg-slate-50 hover:bg-emerald-50 p-5 rounded-xl border border-slate-200 hover:border-emerald-300 transition-colors group">
            <h4 class="font-bold text-slate-900 mb-1 group-hover:text-emerald-700">${appName} Calculator</h4>
            <p class="text-xs text-slate-500">Run your own after-expenses estimate with your real miles and hours.</p>
        </a>
        <a href="/${app}-after-expenses" class="bg-slate-50 hover:bg-emerald-50 p-5 rounded-xl border border-slate-200 hover:border-emerald-300 transition-colors group">
            <h4 class="font-bold text-slate-900 mb-1 group-hover:text-emerald-700">${appName} After Expenses</h4>
            <p class="text-xs text-slate-500">Read the plain-English earnings breakdown before you trust gross payout numbers.</p>
        </a>
        <a href="/taxes/quarterly-estimator" class="bg-slate-50 hover:bg-emerald-50 p-5 rounded-xl border border-slate-200 hover:border-emerald-300 transition-colors group">
            <h4 class="font-bold text-slate-900 mb-1 group-hover:text-emerald-700">Quarterly Tax Estimator</h4>
            <p class="text-xs text-slate-500">Estimate the self-employment tax hit before it surprises you.</p>
        </a>
        <a href="/vehicle-cost/cost-per-mile" class="bg-slate-50 hover:bg-emerald-50 p-5 rounded-xl border border-slate-200 hover:border-emerald-300 transition-colors group">
            <h4 class="font-bold text-slate-900 mb-1 group-hover:text-emerald-700">Cost Per Mile</h4>
            <p class="text-xs text-slate-500">Check whether your car is quietly wiping out your hourly wage.</p>
        </a>
        @if(app.equals("uber"))
        <a href="/uber/where-you-can-drive" class="bg-slate-50 hover:bg-emerald-50 p-5 rounded-xl border border-slate-200 hover:border-emerald-300 transition-colors group">
            <h4 class="font-bold text-slate-900 mb-1 group-hover:text-emerald-700">Uber Coverage Guide</h4>
            <p class="text-xs text-slate-500">Check where Uber actually operates before you compare markets.</p>
        </a>
        @else
        <a href="/doordash/where-you-can-dash" class="bg-slate-50 hover:bg-emerald-50 p-5 rounded-xl border border-slate-200 hover:border-emerald-300 transition-colors group">
            <h4 class="font-bold text-slate-900 mb-1 group-hover:text-emerald-700">DoorDash Availability Guide</h4>
            <p class="text-xs text-slate-500">Check where DoorDash onboarding and active dashing are available before you compare markets.</p>
        </a>
        @endif
    </div>
    <div class="mt-6 text-center">
        <a href="/salary/directory" class="inline-block px-6 py-3 bg-slate-900 text-white font-bold rounded-lg hover:bg-slate-800 transition-colors">Open the City Directory</a>
    </div>
</section>
