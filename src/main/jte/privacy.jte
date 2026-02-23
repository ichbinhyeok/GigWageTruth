@import com.gigwager.model.SeoMeta
@param String customTitle

@param Boolean noIndex = false
@template.layout.main(
    noIndex = noIndex,
    seoMeta = new SeoMeta(
        customTitle,
        "Privacy Policy for GigWageTruth.",
        "https://gigverdict.com/privacy",
        "https://gigverdict.com/og-image.jpg"
    ),
    content = @`
        <div class="max-w-3xl mx-auto px-6 py-12">
            <h1 class="text-3xl font-bold text-slate-900 mb-6">Privacy Policy</h1>
            <div class="prose prose-slate max-w-none text-sm text-slate-600">
                <p>Last updated: January 13, 2026</p>
                
                <h3>1. Information We Collect</h3>
                <p>We do not collect personal identification information (PII) such as names or emails unless you voluntarily provide them via our contact form. We do not store your calculated data on our servers; all calculations happen in your browser.</p>

                <h3>2. Cookies and Tracking</h3>
                <p>We use cookies to improve user experience and analyze traffic. We use third-party services like Google Analytics and Google AdSense.</p>
                <ul>
                    <li><strong>Google AdSense:</strong> Third-party vendors, including Google, use cookies to serve ads based on your prior visits to our website or other websites. Google's use of advertising cookies enables it and its partners to serve ads to you based on your visit to our sites and/or other sites on the Internet.</li>
                    <li>You may opt out of personalized advertising by visiting <a href="https://www.google.com/settings/ads" target="_blank" rel="nofollow">Google Ad Settings</a>.</li>
                </ul>

                <h3>3. Data Security</h3>
                <p>We implement a variety of security measures to maintain the safety of your personal information.</p>

                <h3>4. Contact Us</h3>
                <p>If you have any questions about this Privacy Policy, please contact us at <a href="mailto:painhyeok@gmail.com">painhyeok@gmail.com</a>.</p>
            </div>
        </div>
    `
)
