package com.gigwager.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * Prevents search engines from indexing URLs with query parameters
     * (e.g. ?fbclid=123, ?utm_campaign=xxx) to avoid duplicate content penalties.
     * The canonical tag still points to the clean URL.
     */
    @ModelAttribute("noIndex")
    public Boolean addNoIndex(HttpServletRequest request) {
        String queryString = request.getQueryString();
        return queryString != null && !queryString.isEmpty();
    }
}
