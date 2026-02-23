@import com.gigwager.model.SeoMeta
@param Boolean noIndex = true

!{
    var seoMeta = new SeoMeta(
        "Page Not Found - GigWageTruth",
        "The page you are looking for does not exist.",
        "https://gigverdict.com/404",
        "https://gigverdict.com/og-image.jpg"
    );
}

@template.layout.main(
    seoMeta = seoMeta,
    noIndex = noIndex,
    content = @`
        <div class="flex flex-col items-center justify-center min-h-[60vh] text-center px-4">
            <h1 class="text-6xl font-black text-slate-900 mb-4">404</h1>
            <p class="text-xl text-slate-600 mb-8">We couldn't find the page you're looking for.</p>
            <a href="/salary/directory" class="px-6 py-3 bg-emerald-500 text-white font-semibold rounded-lg hover:bg-emerald-600 transition">
                View Salary Directory
            </a>
        </div>
    `
)
