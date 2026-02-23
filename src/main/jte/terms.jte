@import com.gigwager.model.SeoMeta
@param String customTitle
@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        customTitle,
        "Terms of Service for GigWageTruth.",
        "https://gigverdict.com/terms",
        "https://gigverdict.com/og-image.jpg"
    ),
    content = @`
        <div class="max-w-3xl mx-auto px-6 py-12">
            <h1 class="text-3xl font-bold text-slate-900 mb-6">Terms of Service</h1>
            <div class="prose prose-slate max-w-none text-sm text-slate-600">
                <p>Last updated: January 13, 2026</p>
                
                <h3>1. Acceptance of Terms</h3>
                <p>By accessing GigWageTruth, you agree to be bound by these Terms of Service.</p>

                <h3>2. Disclaimer (Not Financial Advice)</h3>
                <p><strong>GigWageTruth is for informational purposes only.</strong> The calculations provided are estimates based on user inputs and averages. We do not provide financial, legal, or tax advice. You should consult with a qualified professional before making financial decisions.</p>

                <h3>3. Limitation of Liability</h3>
                <p>In no event shall GigWageTruth or its owners be liable for any direct, indirect, incidental, or consequential damages arising out of the use of our website or calculators.</p>

                <h3>4. Changes to Terms</h3>
                <p>We reserve the right to modify these terms at any time. Your continued use of the site constitutes acceptance of those changes.</p>
                
                <h3>5. Contact</h3>
                <p>Questions? Email us at <a href="mailto:painhyeok@gmail.com">painhyeok@gmail.com</a>.</p>
            </div>
        </div>
    `
)
