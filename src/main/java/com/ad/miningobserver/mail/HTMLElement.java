package com.ad.miningobserver.mail;

public class HTMLElement {
    public static final String H1_BOLD = "<h1 style=\"font-weight: bold;\">";
    public static final String H1_CLOSING = "</h1>";
    
    public static final String LIST_CONTAINER_DISC = "<ul style=\"list-style-type:disc;\">";
    public static final String LIST_CONTAINER_CLOSING = "</ul>";
    public static final String LIST_ELEMENT = "<li>";
    public static final String LIST_ELEMENT_CLOSING = "</li>";
    
    public static final String BLANK_PARAGRAPH = "<p></p>";
    
    public static String listItem(final String value) {
        final StringBuilder builder = new StringBuilder();
        return builder.append(LIST_ELEMENT)
                .append(value)
                .append(LIST_ELEMENT_CLOSING)
                .toString();
    }
}
