package com.gigwager.model;

/**
 * SEO metadata for page rendering
 */
public record SeoMeta(
                String title,
                String description,
                String canonicalUrl,
                String ogImage) {
}
