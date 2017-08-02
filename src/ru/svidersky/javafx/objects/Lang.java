package ru.svidersky.javafx.objects;

import java.util.Locale;

public class Lang {

    private String name;
    private Locale locale;

    public Lang( String name, Locale locale) {
        this.name = name;
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
