package com.gigwager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigwager.model.CalculatorIntentPage;
import com.gigwager.model.SeoMeta;
import com.gigwager.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
public class CalculatorIntentController {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @GetMapping("/doordash/{calculatorSlug:earnings-calculator|gas-calculator|mileage-deduction-calculator}")
    public String doordashCalculatorIntent(@PathVariable String calculatorSlug, Model model) {
        return render("doordash", calculatorSlug, model);
    }

    @GetMapping("/uber/{calculatorSlug:pay-calculator|income-calculator|tlc-pay-calculator}")
    public String uberCalculatorIntent(@PathVariable String calculatorSlug, Model model) {
        return render("uber", calculatorSlug, model);
    }

    @GetMapping({
            "/doordash/doordash-calculator",
            "/doordash/driver-earnings-calculator",
            "/doordash/profit-calculator",
            "/doordash/take-home-calculator"
    })
    public RedirectView redirectDoordashCalculatorAliases() {
        return permanentRedirect("/doordash/earnings-calculator");
    }

    @GetMapping({
            "/doordash/fuel-calculator",
            "/doordash/gas-cost-calculator",
            "/doordash/fuel-cost-calculator"
    })
    public RedirectView redirectDoordashGasAliases() {
        return permanentRedirect("/doordash/gas-calculator");
    }

    @GetMapping({
            "/doordash/mileage-calculator",
            "/doordash/tax-mileage-calculator",
            "/doordash/irs-mileage-calculator"
    })
    public RedirectView redirectDoordashMileageAliases() {
        return permanentRedirect("/doordash/mileage-deduction-calculator");
    }

    @GetMapping({
            "/uber/driver-pay-calculator",
            "/uber/earnings-calculator",
            "/uber/uber-pay-calculator",
            "/uber/after-expenses-calculator"
    })
    public RedirectView redirectUberPayAliases() {
        return permanentRedirect("/uber/pay-calculator");
    }

    @GetMapping({
            "/uber/salary-calculator",
            "/uber/weekly-income-calculator",
            "/uber/take-home-calculator"
    })
    public RedirectView redirectUberIncomeAliases() {
        return permanentRedirect("/uber/income-calculator");
    }

    @GetMapping({
            "/tlc-pay-calculator",
            "/uber/tlc-calculator",
            "/uber/nyc-tlc-pay-calculator",
            "/uber/nyc-pay-calculator"
    })
    public RedirectView redirectUberTlcAliases() {
        return permanentRedirect("/uber/tlc-pay-calculator");
    }

    private String render(String app, String slug, Model model) {
        CalculatorIntentPage intent = CalculatorIntentPage.from(app, slug)
                .orElseThrow(() -> new com.gigwager.exception.ResourceNotFoundException("Calculator page not found"));
        String canonicalUrl = AppConstants.BASE_URL + intent.path();
        String lastUpdated = DateTimeFormatter.ofPattern("MMM yyyy", Locale.US).format(LocalDate.now());

        model.addAttribute("intent", intent);
        model.addAttribute("lastUpdated", lastUpdated);
        model.addAttribute("calculatorIntentJsonLd", buildCalculatorIntentJsonLd(intent, canonicalUrl));
        model.addAttribute("seoMeta",
                new SeoMeta(intent.title(), intent.description(), canonicalUrl, AppConstants.BASE_URL + "/og-image.jpg"));
        return "hubs/calculator-intent";
    }

    private RedirectView permanentRedirect(String targetUrl) {
        RedirectView redirectView = new RedirectView(targetUrl);
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        redirectView.setExposeModelAttributes(false);
        return redirectView;
    }

    private String buildCalculatorIntentJsonLd(CalculatorIntentPage intent, String canonicalUrl) {
        Map<String, Object> breadcrumb = new LinkedHashMap<>();
        breadcrumb.put("@type", "BreadcrumbList");
        breadcrumb.put("itemListElement", List.of(
                breadcrumbItem(1, "Home", AppConstants.BASE_URL + "/"),
                breadcrumbItem(2, intent.appName() + " Calculator", AppConstants.BASE_URL + intent.appCalculatorPath()),
                breadcrumbItem(3, intent.headline(), canonicalUrl)));

        Map<String, Object> offer = new LinkedHashMap<>();
        offer.put("@type", "Offer");
        offer.put("price", "0");
        offer.put("priceCurrency", "USD");

        Map<String, Object> webApplication = new LinkedHashMap<>();
        webApplication.put("@type", "WebApplication");
        webApplication.put("name", intent.headline());
        webApplication.put("url", canonicalUrl);
        webApplication.put("sameAs", AppConstants.BASE_URL + intent.calculatorUrl());
        webApplication.put("applicationCategory", "FinanceApplication");
        webApplication.put("operatingSystem", "Web");
        webApplication.put("isAccessibleForFree", true);
        webApplication.put("description", intent.description());
        webApplication.put("featureList", intent.inputChecks());
        webApplication.put("offers", offer);

        Map<String, Object> faqPage = new LinkedHashMap<>();
        faqPage.put("@type", "FAQPage");
        faqPage.put("mainEntity", intent.faqs().stream()
                .map(faq -> {
                    Map<String, Object> question = new LinkedHashMap<>();
                    question.put("@type", "Question");
                    question.put("name", faq.question());
                    Map<String, Object> answer = new LinkedHashMap<>();
                    answer.put("@type", "Answer");
                    answer.put("text", faq.answer());
                    question.put("acceptedAnswer", answer);
                    return question;
                })
                .toList());

        Map<String, Object> article = new LinkedHashMap<>();
        article.put("@type", "Article");
        article.put("headline", intent.title());
        article.put("url", canonicalUrl);
        article.put("dateModified", AppConstants.SITEMAP_LASTMOD_DATE);
        article.put("description", intent.directAnswer());
        article.put("isAccessibleForFree", true);

        Map<String, Object> graph = new LinkedHashMap<>();
        graph.put("@context", "https://schema.org");
        graph.put("@graph", List.of(breadcrumb, webApplication, faqPage, article));
        return toJsonLd(graph);
    }

    private Map<String, Object> breadcrumbItem(int position, String name, String item) {
        Map<String, Object> crumb = new LinkedHashMap<>();
        crumb.put("@type", "ListItem");
        crumb.put("position", position);
        crumb.put("name", name);
        crumb.put("item", item);
        return crumb;
    }

    private String toJsonLd(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize calculator intent JSON-LD", e);
        }
    }
}
