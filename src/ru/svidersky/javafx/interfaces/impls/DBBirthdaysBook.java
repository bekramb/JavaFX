package ru.svidersky.javafx.interfaces.impls;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.svidersky.javafx.db.SQLiteConnection;
import ru.svidersky.javafx.interfaces.BirthdaysBook;
import ru.svidersky.javafx.objects.Months;
import ru.svidersky.javafx.objects.Person;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBBirthdaysBook implements BirthdaysBook {

    private ObservableList<Person> personList = FXCollections.observableArrayList();

    @Override
    public boolean add(Person person) {
        try (Connection con = SQLiteConnection.getConnection(); PreparedStatement statement =
                con.prepareStatement("insert into person(name, month, day, status) values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, person.getFio());
            statement.setInt(2, Integer.valueOf(person.getMonth()));
            statement.setInt(3, person.getDay());
            statement.setString(4, person.isActive()?"ИСТИНА":"ЛОЖЬ");

            int result = statement.executeUpdate();
            if (result > 0) {
                int id = statement.getGeneratedKeys().getInt(1);// получить сгенерированный id вставленной записи
                person.setId(id);
                personList.add(person);
                return true;
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DBBirthdaysBook.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean delete(Person person) {
        try (Connection con = SQLiteConnection.getConnection(); Statement statement = con.createStatement()) {
            int result = statement.executeUpdate("delete from person where id=" + person.getId());

            if (result > 0) {
                personList.remove(person);
                return true;
            }

        }
        catch (SQLException ex) {
            Logger.getLogger(DBBirthdaysBook.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public ObservableList<Person> findAll() {
        try (Connection con = SQLiteConnection.getConnection(); Statement statement = con.createStatement(); ResultSet rs = statement.executeQuery("select * from person")) {
            while (rs.next()) {
                addPerson(rs);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DBBirthdaysBook.class.getName()).log(Level.SEVERE, null, ex);
        }
        return personList;
    }

    @Override
    public boolean update(Person person) {
        try (Connection con = SQLiteConnection.getConnection(); PreparedStatement statement =
                con.prepareStatement("update person set name=?, month=?, day=?, status=? where id=?")) {
            statement.setString(1, person.getFio());
            statement.setString(2, String.valueOf(person.getMonth()));
            statement.setString(3, String.valueOf(person.getDay()));
            statement.setString(4,  (person.isActive()?"ИСТИНА":"ЛОЖЬ"));
            statement.setInt(5, person.getId());

            int result = statement.executeUpdate();
            if (result > 0) {
                // обновление в коллекции происходит автоматически, после нажатия ОК в окне редактирования
                return true;
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DBBirthdaysBook.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public ObservableList<Person> find(String text) {

        personList.clear();

        try (Connection con = SQLiteConnection.getConnection();
             PreparedStatement statement = con.prepareStatement("select * from person where name like ?")) {

            String searchStr =  text + "%";

            statement.setString(1, searchStr);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                addPerson(rs);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DBBirthdaysBook.class.getName()).log(Level.SEVERE, null, ex);
        }
        return personList;
    }

    public ObservableList<Person> getPersonList() {
        return personList;
    }

    private void addPerson(ResultSet rs) throws SQLException {
        Person person = new Person();
        person.setId(rs.getInt("id"));
        person.setFio(rs.getString("name"));
        person.setMonth(Months.values()[rs.getInt("month")-1].getName());
        person.setDay(rs.getInt("day"));
        person.setActive(rs.getString("status").equals("ИСТИНА")? true:false);
        personList.add(person);
    }
}
