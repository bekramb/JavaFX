package ru.svidersky.javafx.utils;

import ru.svidersky.javafx.objects.Lang;

import java.util.Locale;

public class LocaleManager {

    public static final Locale RU_LOCALE = new Locale("ru");

    private static Lang currentLang;

    public static Lang getCurrentLang() {
        return currentLang;
    }

    public static void setCurrentLang(Lang currentLang) {
        LocaleManager.currentLang = currentLang;
    }
}
