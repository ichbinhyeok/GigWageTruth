package com.gigwager.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

@Service
public class HtmlSanitizerService {

    private static final Safelist SAFE_HTML = Safelist.none()
            .addTags("p", "ul", "ol", "li", "strong", "em", "b", "i", "h3", "h4", "br", "span", "div", "blockquote", "a")
            .addAttributes(":all", "class")
            .addAttributes("a", "href", "target", "rel")
            .addProtocols("a", "href", "http", "https", "mailto");

    public String sanitize(String rawHtml) {
        if (rawHtml == null || rawHtml.isBlank()) {
            return "";
        }
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        return Jsoup.clean(rawHtml, "", SAFE_HTML, outputSettings);
    }
}

