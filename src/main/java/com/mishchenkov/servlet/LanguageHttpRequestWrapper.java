package com.mishchenkov.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.Locale;

public class LanguageHttpRequestWrapper extends HttpServletRequestWrapper {

    private final Locale locale;

    public LanguageHttpRequestWrapper(HttpServletRequest request, Locale locale) {
        super(request);
        this.locale = locale;
    }

    @Override
    public Locale getLocale() {
        return new Locale(locale.getLanguage());
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return new Enumeration<Locale>() {

            private final Locale[] locale = {new Locale(LanguageHttpRequestWrapper.this.locale.getLanguage())};
            private int cursor;

            @Override
            public boolean hasMoreElements() {
                return cursor < locale.length;
            }

            @Override
            public Locale nextElement() {
                return locale[cursor++];
            }
        };
    }
}
