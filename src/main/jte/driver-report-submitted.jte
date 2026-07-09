@import com.gigwager.model.SeoMeta
@import com.gigwager.util.AppConstants

@param Boolean noIndex = true

@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        "Driver Report Submitted - GigVerdict",
        "Your driver earnings report was received for editorial review.",
        AppConstants.BASE_URL + "/driver-report-submitted",
        AppConstants.BASE_URL + "/og-image.jpg"
    ),
    content = @`
        <main class="min-h-screen bg-slate-50 py-20 px-6">
            <section class="max-w-2xl mx-auto rounded-2xl border border-slate-200 bg-white p-8 shadow-sm">
                <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-3">Driver report received</p>
                <h1 class="text-3xl font-black text-slate-950 mb-4">Thanks. The report is queued for editorial review.</h1>
                <p class="text-slate-600 leading-relaxed mb-6">
                    Submitted reports are not published automatically. After review, clean shift data can be promoted into the public evidence tables that support city and calculator pages.
                </p>
                <div class="flex flex-wrap gap-3">
                    <a href="/salary/directory" class="inline-flex rounded-xl bg-slate-900 px-5 py-3 font-bold text-white hover:bg-slate-800 transition-colors">
                        Open city directory
                    </a>
                    <a href="/doordash/earnings-calculator" class="inline-flex rounded-xl border border-slate-200 px-5 py-3 font-bold text-slate-800 hover:border-emerald-300 hover:bg-emerald-50 transition-colors">
                        DoorDash earnings calculator
                    </a>
                </div>
            </section>
        </main>
    `
)
