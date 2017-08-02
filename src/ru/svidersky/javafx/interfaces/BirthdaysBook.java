package ru.svidersky.javafx.interfaces;

import javafx.collections.ObservableList;
import ru.svidersky.javafx.objects.Person;

public interface BirthdaysBook {

    boolean add(Person person);

    boolean update(Person person);

    boolean delete(Person person);

    ObservableList<Person> findAll();

    ObservableList<Person> find(String text);
}
