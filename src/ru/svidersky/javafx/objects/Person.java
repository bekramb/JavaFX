package ru.svidersky.javafx.objects;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Person {

    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty fio = new SimpleStringProperty("");
    private SimpleStringProperty month = new SimpleStringProperty("");
    private SimpleIntegerProperty day = new SimpleIntegerProperty();
    private SimpleBooleanProperty active = new SimpleBooleanProperty();

    public Person() {
    }

    public Person(int id, String fio, String month, int day) {
        this.fio = new SimpleStringProperty(fio);
        this.month = new SimpleStringProperty(month);
        this.id = new SimpleIntegerProperty(id);
        this.day = new SimpleIntegerProperty(day);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFio() {
        return fio.get();
    }

    public SimpleStringProperty fioProperty() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio.set(fio);
    }

    public String getMonth() {
        return month.get();
    }

    public SimpleStringProperty monthProperty() {
        return month;
    }

    public void setMonth(String month) {
        this.month.set(month);
    }

    public int getDay() {
        return day.get();
    }

    public SimpleIntegerProperty dayProperty() {
        return day;
    }

    public void setDay(int day) {
        this.day.set(day);
    }

    public boolean isActive() {
        return active.get();
    }

    public SimpleBooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", fio=" + fio +
                ", month=" + month +
                ", day=" + day +
                ", active=" + active +
                '}';
    }
}

