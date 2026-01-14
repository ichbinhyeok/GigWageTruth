@import com.gigwager.model.SeoMeta
@import gg.jte.Content

@param Content content
@param SeoMeta seoMeta

<!DOCTYPE html>
<html lang="en" class="scroll-smooth">
<head>
    <!-- Google tag (gtag.js) -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=G-GJVLR0KTTS"></script>
    <script>
      window.dataLayer = window.dataLayer || [];
      function gtag(){dataLayer.push(arguments);}
      gtag('js', new Date());

      gtag('config', 'G-GJVLR0KTTS');
    </script>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <!-- Google AdSense -->
    <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-3533137832089555"
     crossorigin="anonymous"></script>
    <link rel="icon" type="image/png" href="/favicon.png">
    
    <title>${seoMeta.title()}</title>
    <meta name="description" content="${seoMeta.description()}">
    <link rel="canonical" href="${seoMeta.canonicalUrl()}">
    
    <meta property="og:title" content="${seoMeta.title()}">
    <meta property="og:description" content="${seoMeta.description()}">
    <meta property="og:image" content="${seoMeta.ogImage()}">
    <meta property="og:type" content="website">

    <!-- Fonts: Inter for Professional FinTech Look -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    
    <!-- Alpine.js -->
    <!-- Alpine Plugins -->
    <script defer src="https://cdn.jsdelivr.net/npm/@alpinejs/collapse@3.x.x/dist/cdn.min.js"></script>
    
    <!-- Alpine.js -->
    <script defer src="https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js"></script>
    
    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    fontFamily: {
                        sans: ['Inter', 'sans-serif'],
                    },
                    colors: {
                        brand: '#10B981', // Emerald 500
                        primary: '#0F172A', // Slate 900
                        secondary: '#64748B', // Slate 500
                        surface: '#FFFFFF',
                        background: '#F8FAFC', // Slate 50
                    }
                }
            }
        }
    </script>
    <style>
        [x-cloak] { display: none !important; }
    </style>
</head>
<body class="flex flex-col min-h-screen bg-slate-50 text-slate-900 antialiased font-sans">

    <!-- Header -->
    <header class="w-full bg-white border-b border-slate-200 sticky top-0 z-50">
        <div class="w-full px-8 h-16 flex justify-between items-center">
            <!-- Logo -->
            <a href="/" class="flex items-center space-x-2 group">
                <span class="text-xl font-bold tracking-tight text-slate-900 group-hover:text-emerald-600 transition-colors">
                    GigWageTruth
                </span>
            </a>

            <!-- Simple Menu -->
            <nav class="hidden md:flex space-x-8 text-sm font-medium text-slate-600">
                <a href="/" class="hover:text-emerald-600 transition">Calculator</a>
            </nav>
        </div>
    </header>

    <!-- Main Content -->
    <main class="flex-grow w-full">
        ${content}
    </main>

    <!-- Footer -->
    <footer class="bg-white border-t border-slate-200 py-12 mt-12">
        <div class="max-w-5xl mx-auto px-4 text-center">
            <p class="text-slate-900 font-semibold mb-2">GigWageTruth</p>
            <div class="flex flex-wrap justify-center gap-x-6 gap-y-3 mb-6 text-sm text-slate-500">
                <a href="/about" class="hover:text-emerald-600 transition">About (Gabi)</a>
                <a href="/blog" class="hover:text-emerald-600 transition">Blog</a>
                <a href="/salary/directory" class="hover:text-emerald-600 transition">Salary by City</a>
                <a href="/methodology" class="hover:text-emerald-600 transition">Methodology</a>
                <a href="/privacy" class="hover:text-emerald-600 transition">Privacy</a>
                <a href="/terms" class="hover:text-emerald-600 transition">Terms</a>
                <a href="/contact" class="hover:text-emerald-600 transition">Contact</a>
            </div>
            <p class="text-slate-500 text-sm mb-4">
                Revealing the true cost of the gig economy.
            </p>
            <p class="text-xs text-slate-400">
                &copy; 2026 GigWageTruth. All rights reserved. <br>
                Calculations based on 2026 IRS Standard Mileage Rate ($0.725/mile). Not financial advice.
            </p>
        </div>
    </footer>

</body>
</html>
