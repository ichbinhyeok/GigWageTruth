@import com.gigwager.model.SeoMeta
@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        customTitle,
        "Contact GigWageTruth.",
        "https://gigverdict.com/contact",
        "https://gigverdict.com/og-image.jpg"
    ),
    content = @`
        <div class="max-w-3xl mx-auto px-6 py-12">
            <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-8 text-center">
                <h1 class="text-3xl font-bold text-slate-900 mb-4">Contact Us</h1>
                <p class="text-slate-600 mb-8">
                    We value your feedback. Whether you found a bug, have a feature request, or just want to share your gig story, we're listening.
                </p>
                
                <div class="inline-flex items-center justify-center space-x-2 bg-emerald-50 text-emerald-800 px-6 py-4 rounded-xl">
                    <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"></path></svg>
                    <a href="mailto:painhyeok@gmail.com" class="text-lg font-medium hover:underline">painhyeok@gmail.com</a>
                </div>
                
                <p class="text-sm text-slate-400 mt-8">
                    We try to respond to all inquiries within 24 hours.
                </p>
            </div>
        </div>
    `
)
