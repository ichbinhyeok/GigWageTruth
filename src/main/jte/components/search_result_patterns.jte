@import com.gigwager.model.SearchResultPattern
@import java.util.List

@param String appName
@param String cityName
@param String intentLabel = ""
@param double netHourly
@param List<SearchResultPattern> searchResultPatterns = java.util.List.of()
@param String sectionClass = "max-w-5xl mx-auto px-6 mb-12"

@if(searchResultPatterns != null && !searchResultPatterns.isEmpty())
    <section class="${sectionClass}">
        <div class="bg-slate-950 rounded-3xl border border-slate-800 p-8 text-white shadow-sm">
            <p class="text-xs font-black uppercase tracking-[0.18em] text-emerald-300 mb-2">Top-result comparison</p>
            <h2 class="text-3xl font-black mb-3">
                Where GigVerdict has to beat the pages ranking for ${appName} ${cityName}
                @if(intentLabel != null && !intentLabel.isBlank())
                    ${intentLabel}
                @endif
            </h2>
            <p class="text-slate-300 max-w-3xl mb-6">
                The strongest search results usually win one trust signal at a time: official pay rules, a real driving test, a large driver dataset, fresh salary aggregates, or raw driver discussion. This page has to combine those signals with a city-level $${String.format("%.2f", netHourly)}/hr net estimate.
            </p>
            <div class="grid lg:grid-cols-2 gap-4">
                @for(SearchResultPattern pattern : searchResultPatterns)
                    <article class="rounded-2xl bg-white/5 border border-white/10 p-5">
                        <div class="flex flex-wrap items-center gap-2 mb-3">
                            <span class="text-[11px] font-black uppercase tracking-[0.16em] text-emerald-300">${pattern.resultType()}</span>
                            <span class="text-[11px] text-slate-500">|</span>
                            <span class="text-xs font-bold text-slate-300">${pattern.competitor()}</span>
                        </div>
                        <h3 class="text-lg font-bold text-white mb-2">${pattern.winningSignal()}</h3>
                        <p class="text-sm text-slate-300 leading-relaxed mb-3">
                            <strong class="text-slate-100">Usually missing:</strong> ${pattern.missingCheck()}
                        </p>
                        <p class="text-sm text-emerald-100 leading-relaxed mb-4">
                            <strong>GigVerdict response:</strong> ${pattern.gigVerdictResponse()}
                        </p>
                        <a href="${pattern.sourceUrl()}" target="_blank" rel="noopener nofollow" class="text-sm font-bold text-emerald-300 hover:text-emerald-200">
                            Source: ${pattern.sourceLabel()} &rarr;
                        </a>
                    </article>
                @endfor
            </div>
        </div>
    </section>
@endif
