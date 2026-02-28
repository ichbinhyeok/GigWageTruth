package com.gigwager;

import com.gigwager.service.HtmlSanitizerService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HtmlSanitizerServiceTest {

    private final HtmlSanitizerService sanitizerService = new HtmlSanitizerService();

    @Test
    public void shouldStripDangerousHtmlAndProtocols() {
        String raw = "<p onclick=\"alert(1)\">hello</p>"
                + "<script>alert('xss')</script>"
                + "<a href=\"javascript:alert(2)\">bad</a>"
                + "<img src=x onerror=\"alert(3)\">";

        String cleaned = sanitizerService.sanitize(raw);

        assertFalse(cleaned.contains("<script"), "script tag must be stripped");
        assertFalse(cleaned.contains("onclick"), "event handlers must be stripped");
        assertFalse(cleaned.contains("onerror"), "event handlers must be stripped");
        assertFalse(cleaned.toLowerCase().contains("javascript:"), "javascript: protocol must be stripped");
    }

    @Test
    public void shouldKeepAllowedFormattingTags() {
        String raw = "<h3 class=\"title\">Header</h3><p><strong>bold</strong> <em>text</em></p>"
                + "<a href=\"https://example.com\" target=\"_blank\" rel=\"noopener\">source</a>";

        String cleaned = sanitizerService.sanitize(raw);

        assertTrue(cleaned.contains("<h3"), "h3 should be preserved");
        assertTrue(cleaned.contains("class=\"title\""), "class attribute should be preserved");
        assertTrue(cleaned.contains("<strong>bold</strong>"), "strong tag should be preserved");
        assertTrue(cleaned.contains("<em>text</em>"), "em tag should be preserved");
        assertTrue(cleaned.contains("href=\"https://example.com\""), "safe href should be preserved");
    }
}

