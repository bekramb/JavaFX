package ru.svidersky.javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.svidersky.javafx.objects.Person;
import ru.svidersky.javafx.utils.DialogManager;

import java.net.URL;
import java.util.ResourceBundle;


public class EditDialogController implements Initializable {

    @FXML
    private Button btnOk;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField txtFIO;

    @FXML
    private TextField txtMonth;

    @FXML
    private TextField txtDay;

    @FXML
    private CheckBox chboxActive;

    @FXML
    private ComboBox cbMonth;

    private Person person;

    private ResourceBundle resourceBundle;

    private boolean saveClicked = false;// для определения нажатой кнопки


    public void setPerson(Person person) {
        if (person == null) {
            return;
        }
        saveClicked = false;
        this.person = person;
        txtFIO.setText(person.getFio());
        cbMonth.getSelectionModel().select(person.getMonth());
        txtDay.setText(String.valueOf(person.getDay()));
        chboxActive.setSelected(person.isActive());
    }

    public Person getPerson() {
        return person;
    }

    public void actionClose(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }


    public void actionSave(ActionEvent actionEvent) {
        if (!checkValues()) {
            return;
        }

        person.setFio(txtFIO.getText());
        person.setMonth(String.valueOf(cbMonth.getSelectionModel().getSelectedIndex()+1));
        person.setDay(Integer.valueOf(txtDay.getText()));
        person.setActive(chboxActive.isSelected());
        saveClicked = true;
        actionClose(actionEvent);
    }

    private boolean checkValues() {
        if (txtFIO.getText().trim().length() == 0 || cbMonth.getSelectionModel().getSelectedItem() == null
                || txtDay.getText().trim().length() == 0) {
            DialogManager.showInfoDialog(resourceBundle.getString("error"), resourceBundle.getString("fill_field"));
            return false;
        }
        try {
            int day = Integer.valueOf(txtDay.getText());
            if (day < 1 || day > 31) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        cbMonth.getItems().addAll(
                resourceBundle.getString("january"),
                resourceBundle.getString("february"),
                resourceBundle.getString("march"),
                resourceBundle.getString("april"),
                resourceBundle.getString("may"),
                resourceBundle.getString("june"),
                resourceBundle.getString("july"),
                resourceBundle.getString("august"),
                resourceBundle.getString("september"),
                resourceBundle.getString("october"),
                resourceBundle.getString("november"),
                resourceBundle.getString("december")
                );
        cbMonth.getSelectionModel().selectFirst();
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }
}
