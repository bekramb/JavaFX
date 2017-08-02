package ru.svidersky.javafx.objects;

import ru.svidersky.javafx.start.Main;

import java.util.ResourceBundle;

public enum Months {
    Jan("january"),
    Fab("february"),
    Mar("march"),
    Apr("april"),
    May("may"),
    Jun("june"),
    Jul("july"),
    Aug("august"),
    Sep("september"),
    Oct("october"),
    Nov("november"),
    Dec("december");

    private String name;
    ResourceBundle resourceBundle = ResourceBundle.getBundle(Main.BUNDLES_FOLDER);

    Months(String name) {
        this.name = resourceBundle.getString(name);
    }

    public String getName() {
        return name;
    }
}
