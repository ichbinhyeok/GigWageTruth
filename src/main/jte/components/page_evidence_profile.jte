@import com.gigwager.model.PageEvidenceProfile

@param PageEvidenceProfile pageEvidenceProfile
@param String sectionClass = "max-w-4xl mx-auto px-6 mb-10"

@if(pageEvidenceProfile != null)
    <section class="${sectionClass}">
        <div class="rounded-2xl border border-slate-200 bg-white p-6 md:p-7 shadow-sm">
            <div class="flex flex-col md:flex-row md:items-start md:justify-between gap-4 mb-5">
                <div>
                    <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-700 mb-2">Evidence profile</p>
                    <h2 class="text-2xl md:text-3xl font-black text-slate-900">${pageEvidenceProfile.heading()}</h2>
                </div>
                @if("strong".equals(pageEvidenceProfile.confidenceTone()))
                    <span class="inline-flex w-fit rounded-full bg-emerald-100 px-3 py-1 text-xs font-black uppercase tracking-[0.12em] text-emerald-800">${pageEvidenceProfile.confidenceLabel()}</span>
                @elseif("reviewed".equals(pageEvidenceProfile.confidenceTone()))
                    <span class="inline-flex w-fit rounded-full bg-sky-100 px-3 py-1 text-xs font-black uppercase tracking-[0.12em] text-sky-800">${pageEvidenceProfile.confidenceLabel()}</span>
                @else
                    <span class="inline-flex w-fit rounded-full bg-amber-100 px-3 py-1 text-xs font-black uppercase tracking-[0.12em] text-amber-800">${pageEvidenceProfile.confidenceLabel()}</span>
                @endif
            </div>

            <p class="text-slate-700 leading-relaxed mb-5">${pageEvidenceProfile.summary()}</p>
            <p class="rounded-xl bg-slate-50 border border-slate-200 px-4 py-3 text-sm text-slate-700 mb-5">
                <strong class="text-slate-900">Unique data anchor:</strong> ${pageEvidenceProfile.uniqueDataAnchor()}
            </p>

            <div class="grid sm:grid-cols-2 lg:grid-cols-4 gap-3 text-sm">
                <div class="rounded-xl bg-slate-950 text-white p-4">
                    <p class="text-[11px] font-black uppercase tracking-[0.14em] text-slate-400 mb-1">Cited sources</p>
                    <p class="text-2xl font-black">${pageEvidenceProfile.sourceCount()}</p>
                </div>
                <div class="rounded-xl bg-slate-50 border border-slate-200 p-4">
                    <p class="text-[11px] font-black uppercase tracking-[0.14em] text-slate-500 mb-1">Driver reports</p>
                    <p class="text-2xl font-black text-slate-900">${pageEvidenceProfile.driverReportCount()}</p>
                </div>
                <div class="rounded-xl bg-slate-50 border border-slate-200 p-4">
                    <p class="text-[11px] font-black uppercase tracking-[0.14em] text-slate-500 mb-1">City-specific</p>
                    <p class="text-2xl font-black text-slate-900">${pageEvidenceProfile.citySpecificDriverReportCount()}</p>
                </div>
                <div class="rounded-xl bg-slate-50 border border-slate-200 p-4">
                    <p class="text-[11px] font-black uppercase tracking-[0.14em] text-slate-500 mb-1">Method</p>
                    <p class="text-sm font-black text-slate-900 leading-tight">${pageEvidenceProfile.methodologyVersion()}</p>
                    @if(pageEvidenceProfile.lastVerifiedAt() != null && !pageEvidenceProfile.lastVerifiedAt().isBlank())
                        <p class="text-xs text-slate-500 mt-1">Checked ${pageEvidenceProfile.lastVerifiedAt()}</p>
                    @endif
                </div>
            </div>
        </div>
    </section>
@endif
