package com.gigwager.model;

/**
 * SEO metadata for page rendering
 * selfCanonical: if true, page canonically points to itself (default).
 * if false, canonical is suppressed (used when work-level pages should defer to
 * main city page)
 */
public record SeoMeta(
                String title,
                String description,
                String canonicalUrl,
                String ogImage,
                boolean selfCanonical) {

        // Convenience constructor with default selfCanonical = true
        public SeoMeta(String title, String description, String canonicalUrl, String ogImage) {
                this(title, description, canonicalUrl, ogImage, true);
        }
}
