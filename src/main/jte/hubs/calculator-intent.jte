@import com.gigwager.model.CalculatorIntentPage
@import com.gigwager.model.SeoMeta
@import com.gigwager.util.AppConstants

@param CalculatorIntentPage intent
@param String calculatorIntentJsonLd
@param String lastUpdated
@param SeoMeta seoMeta
@param Boolean noIndex = false

@template.layout.main(
    noIndex = noIndex,
    seoMeta = seoMeta,
    content = @`
        <script type="application/ld+json">
        $unsafe{calculatorIntentJsonLd}
        </script>

        <main class="bg-slate-50 min-h-screen pb-20">
            <section class="bg-slate-950 text-white py-16 px-6">
                <div class="max-w-5xl mx-auto">
                    <nav class="text-sm text-slate-400 mb-6 flex flex-wrap items-center gap-2">
                        <a href="/" class="hover:text-emerald-300">Home</a>
                        <span>&gt;</span>
                        <a href="${intent.appCalculatorPath()}" class="hover:text-emerald-300">${intent.appName()} calculator</a>
                        <span>&gt;</span>
                        <span class="text-white">${intent.headline()}</span>
                    </nav>

                    <p class="text-xs font-black uppercase tracking-[0.22em] text-emerald-300 mb-3">Calculator intent page</p>
                    <h1 class="text-4xl lg:text-6xl font-black tracking-tight max-w-4xl">
                        ${intent.headline()}
                        <span class="block text-emerald-300">${intent.subhead()}</span>
                    </h1>
                    <p class="text-xl text-slate-300 max-w-3xl mt-6">
                        ${intent.directAnswer()} Updated ${lastUpdated}.
                    </p>
                    <div class="mt-8 flex flex-wrap gap-3">
                        <a href="${intent.calculatorUrl()}" onclick="gtag('event','cta_click',{'event_category':'calculator_intent','event_label':'run_${intent.app()}_${intent.slug()}'})" class="inline-flex items-center justify-center rounded-xl bg-emerald-400 px-5 py-3 font-black text-slate-950 hover:bg-emerald-300 transition-colors">
                            ${intent.ctaLabel()}
                        </a>
                        <a href="${intent.appHubPath()}" class="inline-flex items-center justify-center rounded-xl border border-white/15 px-5 py-3 font-bold text-white hover:bg-white/10 transition-colors">
                            Open ${intent.appName()} city hub
                        </a>
                    </div>
                </div>
            </section>

            <section class="max-w-5xl mx-auto px-6 -mt-8 relative z-10">
                <div class="grid md:grid-cols-3 gap-4 mb-10">
                    <div class="rounded-2xl border border-emerald-200 bg-white p-6 shadow-sm">
                        <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-2">Baseline gross</p>
                        <p class="text-3xl font-black text-slate-950">${intent.benchmarkGross()}</p>
                    </div>
                    <div class="rounded-2xl border border-emerald-200 bg-white p-6 shadow-sm">
                        <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-2">Mileage input</p>
                        <p class="text-3xl font-black text-slate-950">${intent.benchmarkMiles()}</p>
                    </div>
                    <div class="rounded-2xl border border-emerald-200 bg-white p-6 shadow-sm">
                        <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-2">Time input</p>
                        <p class="text-3xl font-black text-slate-950">${intent.benchmarkHours()}</p>
                    </div>
                </div>

                <div class="grid lg:grid-cols-[1.15fr_0.85fr] gap-6 mb-12">
                    <section class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">What this calculator answers</p>
                        <h2 class="text-2xl font-black text-slate-950 mb-4">${intent.headline()} inputs that matter</h2>
                        <div class="grid sm:grid-cols-2 gap-3">
                            @for(String check : intent.inputChecks())
                                <div class="rounded-xl border border-slate-200 bg-slate-50 p-4">
                                    <p class="text-sm font-semibold text-slate-700">${check}</p>
                                </div>
                            @endfor
                        </div>
                    </section>

                    <aside class="rounded-2xl border border-slate-200 bg-slate-900 p-6 text-white shadow-sm">
                        <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-2">Fast path</p>
                        <h2 class="text-2xl font-black mb-3">Run the numbers before reading another gross-pay answer</h2>
                        <p class="text-sm text-slate-300 mb-5">
                            This page exists because Google is already showing calculator searches, but generic hubs do not answer the input problem directly enough.
                        </p>
                        <a href="${intent.calculatorUrl()}" class="block rounded-xl bg-white px-4 py-3 text-center font-black text-slate-950 hover:bg-emerald-100 transition-colors">
                            Open prefilled calculator
                        </a>
                    </aside>
                </div>

                <section class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm mb-12">
                    <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Result checks</p>
                    <h2 class="text-2xl font-black text-slate-950 mb-4">Do not stop at gross pay</h2>
                    <div class="grid md:grid-cols-3 gap-4">
                        @for(CalculatorIntentPage.Metric metric : intent.metrics())
                            <div class="rounded-xl border border-slate-200 p-4">
                                <h3 class="font-black text-slate-950 mb-2">${metric.label()}</h3>
                                <p class="text-sm text-slate-600">${metric.detail()}</p>
                            </div>
                        @endfor
                    </div>
                </section>

                <section class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm mb-12">
                    <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Next clicks</p>
                    <h2 class="text-2xl font-black text-slate-950 mb-4">Related ${intent.appName()} money pages</h2>
                    <div class="grid md:grid-cols-3 gap-4">
                        @for(CalculatorIntentPage.RelatedLink link : intent.relatedLinks())
                            <a href="${link.path()}" class="rounded-xl border border-slate-200 p-4 hover:border-emerald-300 hover:bg-emerald-50 transition-colors">
                                <strong class="block text-slate-950">${link.label()}</strong>
                                <span class="text-sm text-slate-600 mt-2 block">${link.description()}</span>
                            </a>
                        @endfor
                    </div>
                </section>

                <section class="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
                    <h2 class="text-2xl font-black text-slate-950 mb-5">Frequently Asked Questions</h2>
                    <div class="space-y-4">
                        @for(CalculatorIntentPage.Faq faq : intent.faqs())
                            <details class="rounded-xl border border-slate-200 p-4">
                                <summary class="cursor-pointer font-bold text-slate-950">${faq.question()}</summary>
                                <p class="text-slate-600 mt-3">${faq.answer()}</p>
                            </details>
                        @endfor
                    </div>
                </section>
            </section>
        </main>
    `
)
