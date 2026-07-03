@import com.gigwager.model.DriverFieldNote
@import java.util.List

@param String appName
@param String cityName
@param double netHourly
@param List<DriverFieldNote> driverFieldNotes = java.util.List.of()
@param String sectionClass = "max-w-5xl mx-auto px-6 mb-12"

@if(driverFieldNotes != null && !driverFieldNotes.isEmpty())
    <section class="${sectionClass}">
        <div class="mb-6">
            <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Driver field notes</p>
            <h2 class="text-3xl font-black text-slate-900 mb-3">
                What drivers check before trusting a ${appName} earnings number in ${cityName}
            </h2>
            <p class="text-slate-600 max-w-3xl">
                These notes come from field tests, official platform docs, driver discussions, and gig-economy reports. They are not guarantees; they are the friction points a real driver would check before treating $${String.format("%.2f", netHourly)}/hr net as realistic.
            </p>
        </div>
        <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
            @for(DriverFieldNote note : driverFieldNotes)
                <article class="bg-white rounded-2xl border border-slate-200 p-5 shadow-sm">
                    <p class="text-xs font-black uppercase tracking-[0.16em] text-slate-500 mb-2">${note.label()}</p>
                    <h3 class="text-lg font-bold text-slate-900 mb-2">${note.title()}</h3>
                    <p class="text-sm text-slate-600 leading-relaxed mb-4">${note.summary()}</p>
                    <a href="${note.sourceUrl()}" target="_blank" rel="noopener nofollow" class="text-sm font-bold text-emerald-700 hover:text-emerald-800">
                        Source: ${note.sourceLabel()} &rarr;
                    </a>
                </article>
            @endfor
        </div>
    </section>
@endif
