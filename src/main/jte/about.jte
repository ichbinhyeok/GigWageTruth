@import com.gigwager.model.SeoMeta
@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        customTitle,
        "Meet Gabi and the team behind GigWageTruth. We reveal the hidden costs of gig work.",
        "https://gigverdict.com/about",
        "https://gigverdict.com/og-image.jpg"
    ),
    content = @`
        <div class="max-w-3xl mx-auto px-6 py-12">
            <h1 class="text-4xl font-bold text-slate-900 mb-6">About GigWageTruth</h1>
            
            <div class="prose prose-slate max-w-none">
                <p class="text-lg text-slate-600 mb-8">
                    Revealing the math that apps won't show you.
                </p>

                <h2 class="text-2xl font-bold text-slate-900 mt-12 mb-4">The Story of Gabi</h2>
                <p class="mb-4">
                    Hi, I'm <strong>Gabi</strong>.
                </p>
                <p class="mb-4">
                    I'm a developer, but my best friends are gig drivers. Every weekend, we'd hang out, and they'd show me their earnings app.
                    "$1,000 in a week! Look at this!" they'd say, beaming with pride.
                </p>
                <p class="mb-4">
                    But then I noticed something strange. Despite these "high earnings," their bank accounts were always empty.
                    They were constantly stressed about car repairs, gas money, and tax bills.
                </p>
                <p class="mb-4">
                    <strong>The math didn't add up.</strong>
                </p>
                <p class="mb-4">
                    So I sat down with them and crunched the real numbers. 
                    We subtracted the gas. We calculated the hidden depreciation of their vehicles (which is invisible until your car dies). 
                    We set aside the taxes they owed but hadn't paid yet.
                </p>
                <p class="mb-4">
                    The result was shocking. That "$25/hour" was actually closer to <strong>$8/hour</strong>.
                    They were borrowing equity from their future selves—selling their cars part by part, mile by mile, for quick cash today.
                </p>

                <h2 class="text-2xl font-bold text-slate-900 mt-12 mb-4">Our Mission</h2>
                <p class="mb-4">
                    I built <strong>GigWageTruth</strong> to give every driver the tool I built for my friends. 
                    We are not here to discourage you from gig work. We are here to arm you with the <strong>truth</strong>.
                </p>
                <p class="mb-4">
                    When you know your <em>Real Hourly Wage</em>, you make better decisions. 
                    You drive smarter. You choose better apps. You survive.
                </p>
                
                <h2 class="text-2xl font-bold text-slate-900 mt-12 mb-4">Contact</h2>
                <p>
                    Have questions? Found a bug? Just want to share your story?<br>
                    Email me directly at <a href="mailto:spainhyeok@gmail.com" class="text-emerald-600 hover:underline">spainhyeok@gmail.com</a>.
                </p>
            </div>
        </div>
    `
)
