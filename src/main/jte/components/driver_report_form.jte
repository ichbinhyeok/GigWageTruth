@param String app
@param String appName
@param String cityName
@param String citySlug
@param String sourcePage
@param String sourcePath
@param double netHourly
@param String sectionClass = "max-w-5xl mx-auto px-6 mb-12"

<section class="${sectionClass}" data-nosnippet>
    <div class="bg-slate-950 rounded-3xl border border-slate-800 p-6 md:p-8 text-white shadow-sm">
        <div class="grid lg:grid-cols-[0.9fr_1.1fr] gap-8">
            <div>
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-2">Driver report intake</p>
                <h2 class="text-3xl font-black mb-3">Share a real ${appName} shift in ${cityName}</h2>
                <p class="text-slate-300 leading-relaxed">
                    Modeled earnings are useful, but actual shifts are the data moat. Submit gross pay, miles, hours, and the window you drove so future ${cityName} pages can compare the model against real driver reports.
                </p>
                <div class="mt-5 grid grid-cols-2 gap-3 text-sm">
                    <div class="rounded-2xl bg-white/5 border border-white/10 p-4">
                        <p class="text-xs uppercase tracking-wider text-slate-400 mb-1">Current model</p>
                        <p class="text-2xl font-black text-emerald-300">$${String.format("%.2f", netHourly)}/hr</p>
                    </div>
                    <div class="rounded-2xl bg-white/5 border border-white/10 p-4">
                        <p class="text-xs uppercase tracking-wider text-slate-400 mb-1">Report status</p>
                        <p class="text-lg font-black text-white">Editorial review</p>
                    </div>
                </div>
            </div>

            <form action="https://formspree.io/f/mqkenryp" method="POST" class="grid sm:grid-cols-2 gap-4" onsubmit="gtag('event','cta_click',{'event_category':'driver_report','event_label':'submit_driver_report'});">
                <input type="hidden" name="lead_type" value="driver_earnings_report">
                <input type="hidden" name="source_page" value="${sourcePage}">
                <input type="hidden" name="source_path" value="${sourcePath}">
                <input type="hidden" name="app" value="${app}">
                <input type="hidden" name="app_name" value="${appName}">
                <input type="hidden" name="city" value="${cityName}">
                <input type="hidden" name="city_slug" value="${citySlug}">
                <input type="hidden" name="modeled_net_hourly" value="${String.format("%.2f", netHourly)}">
                <input type="hidden" name="editorial_status" value="unreviewed_user_submission">

                <label class="block">
                    <span class="block text-xs font-bold uppercase tracking-wider text-slate-300 mb-1">Weekly gross</span>
                    <input name="weekly_gross" type="number" min="0" step="1" placeholder="650" class="w-full rounded-xl border border-white/10 bg-white text-slate-900 px-4 py-3 text-sm">
                </label>
                <label class="block">
                    <span class="block text-xs font-bold uppercase tracking-wider text-slate-300 mb-1">Miles driven</span>
                    <input name="weekly_miles" type="number" min="0" step="1" placeholder="250" class="w-full rounded-xl border border-white/10 bg-white text-slate-900 px-4 py-3 text-sm">
                </label>
                <label class="block">
                    <span class="block text-xs font-bold uppercase tracking-wider text-slate-300 mb-1">Active hours</span>
                    <input name="active_hours" type="number" min="0" step="0.5" placeholder="18.5" class="w-full rounded-xl border border-white/10 bg-white text-slate-900 px-4 py-3 text-sm">
                </label>
                <label class="block">
                    <span class="block text-xs font-bold uppercase tracking-wider text-slate-300 mb-1">Online/dash hours</span>
                    <input name="online_hours" type="number" min="0" step="0.5" placeholder="24" class="w-full rounded-xl border border-white/10 bg-white text-slate-900 px-4 py-3 text-sm">
                </label>
                <label class="block">
                    <span class="block text-xs font-bold uppercase tracking-wider text-slate-300 mb-1">Shift window</span>
                    <select name="shift_window" class="w-full rounded-xl border border-white/10 bg-white text-slate-900 px-4 py-3 text-sm">
                        <option value="dinner">Dinner</option>
                        <option value="lunch">Lunch</option>
                        <option value="nights_weekends">Nights/weekends</option>
                        <option value="airport_events">Airport/events</option>
                        <option value="all_day">All day</option>
                    </select>
                </label>
                <label class="block">
                    <span class="block text-xs font-bold uppercase tracking-wider text-slate-300 mb-1">Optional email</span>
                    <input name="email" type="email" placeholder="for follow-up only" class="w-full rounded-xl border border-white/10 bg-white text-slate-900 px-4 py-3 text-sm">
                </label>
                <label class="block sm:col-span-2">
                    <span class="block text-xs font-bold uppercase tracking-wider text-slate-300 mb-1">What changed the result?</span>
                    <textarea name="driver_note" rows="3" placeholder="Example: long restaurant waits, bad parking, strong tips, airport queue, dead miles back home..." class="w-full rounded-xl border border-white/10 bg-white text-slate-900 px-4 py-3 text-sm"></textarea>
                </label>
                <div class="sm:col-span-2 flex flex-col sm:flex-row gap-3 sm:items-center">
                    <button type="submit" class="inline-flex justify-center rounded-xl bg-emerald-500 hover:bg-emerald-400 text-slate-950 font-black px-5 py-3 transition-colors">
                        Submit driver report
                    </button>
                    <p class="text-xs text-slate-400">
                        Do not include phone numbers, addresses, order IDs, customer names, or passenger names. Reports are reviewed before being used on-page.
                    </p>
                </div>
            </form>
        </div>
    </div>
</section>
