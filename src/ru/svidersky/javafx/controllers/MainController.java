package ru.svidersky.javafx.controllers;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.svidersky.javafx.interfaces.impls.DBBirthdaysBook;
import ru.svidersky.javafx.objects.Lang;
import ru.svidersky.javafx.objects.Months;
import ru.svidersky.javafx.objects.Person;
import ru.svidersky.javafx.start.Main;
import ru.svidersky.javafx.utils.DialogManager;
import ru.svidersky.javafx.utils.LocaleManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class MainController extends Observable implements Initializable {

    private static final String FXML_EDIT = "../fxml/edit.fxml";
    public static final LocalDate TODAY = LocalDate.now();
    private DBBirthdaysBook birthdaysBook = new DBBirthdaysBook();

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnSearch;

    @FXML
    private TableView tableBirthdaysBook;

    @FXML
    private TableColumn<Person, String> columnFIO;

    @FXML
    private TableColumn<Person, String> columnMonth;

    @FXML
    private TableColumn<Person, String> columnDay;

    @FXML
    private Label labelCount;

    @FXML
    private RadioButton onlyActive;

    @FXML
    private ComboBox cbMonthChoice;


    private Parent fxmlEdit;

    private FXMLLoader fxmlLoader = new FXMLLoader();

    private EditDialogController editDialogController;

    private Stage editDialogStage;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        columnFIO.setCellValueFactory(new PropertyValueFactory<Person, String>("fio"));
        columnMonth.setCellValueFactory(new PropertyValueFactory<Person, String>("month"));
        columnDay.setCellValueFactory(new PropertyValueFactory<Person, String>("day"));
        cbMonthChoice.getItems().addAll(
                resourceBundle.getString("all"),
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
        cbMonthChoice.getSelectionModel().selectFirst();
        setupClearButtonField(txtSearch);
        initListeners();
        fillData();
        initLoader();
    }

    private void setupClearButtonField(TextField textField) {
        textField.setText("");
    }


    private void fillData() {
        fillTable();
        setLang();
    }

    private void fillTable() {
        ObservableList<Person> list = birthdaysBook.findAll();
        tableBirthdaysBook.setItems(list);
        findByMonthAndDay();
    }

    private void setLang() {
        Lang langRU = new Lang(resourceBundle.getString("ru"), LocaleManager.RU_LOCALE);
        LocaleManager.setCurrentLang(langRU);
    }

    private void initListeners() {

        // слушает изменения в коллекции для обновления надписи "Кол-во записей"
        birthdaysBook.getPersonList().addListener((ListChangeListener<Person>) c -> updateCountLabel());

        // слушает двойное нажатие для редактирования записи
        tableBirthdaysBook.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                btnEdit.fire();
            }
        });

        // слушает переключение радиокнопки
        onlyActive.setOnAction(event -> {
            if (!onlyActive.isSelected()) {
                birthdaysBook.getPersonList().clear();
                birthdaysBook.findAll();
            } else {
                LinkedList<Person> newList = birthdaysBook.getPersonList().stream()
                        .filter(p -> p.isActive()).collect(Collectors.toCollection(LinkedList::new));
                birthdaysBook.getPersonList().retainAll(newList);
            }
            if (!(cbMonthChoice.getSelectionModel().getSelectedIndex() == 0)) {
                LinkedList<Person> newList = birthdaysBook.getPersonList().stream()
                        .filter(p->p.getMonth().equals(cbMonthChoice.getSelectionModel().getSelectedItem().toString()))
                        .collect(Collectors.toCollection(LinkedList::new));
                birthdaysBook.getPersonList().retainAll(newList);
            }
        });

        txtSearch.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                btnSearch.fire();
            }
        });
    }

    private void initLoader() {
        try {
            fxmlLoader.setLocation(getClass().getResource(FXML_EDIT));
            fxmlLoader.setResources(ResourceBundle.getBundle(Main.BUNDLES_FOLDER, LocaleManager.getCurrentLang().getLocale()));
            fxmlEdit = fxmlLoader.load();
            editDialogController = fxmlLoader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCountLabel() {
        labelCount.setText(resourceBundle.getString("count") + ": " + birthdaysBook.getPersonList().size());
    }

    public void actionButtonPressed(ActionEvent actionEvent) {

        Object source = actionEvent.getSource();

        // если нажата не кнопка - выходим из метода
        if (!(source instanceof Button)) {
            return;
        }

        Person selectedPerson = (Person) tableBirthdaysBook.getSelectionModel().getSelectedItem();


        Button clickedButton = (Button) source;

        boolean research = false;

        switch (clickedButton.getId()) {
            case "btnAdd":
                Person p = new Person();
                p.setMonth(resourceBundle.getString("january"));
                editDialogController.setPerson(p);
                showDialog();

                if (editDialogController.isSaveClicked()) {
                    birthdaysBook.add(editDialogController.getPerson());
                    research = true;
                }
                break;

            case "btnEdit":
                if (!personIsSelected(selectedPerson)) {
                    return;
                }
                editDialogController.setPerson(selectedPerson);
                showDialog();

                if (editDialogController.isSaveClicked()) {
                    // коллекция в birthdaysBook и так обновляется, т.к. мы ее редактируем в диалоговом окне и сохраняем при нажатии на ОК
                    birthdaysBook.update(selectedPerson);
                    research = true;
                }
                break;

            case "btnDelete":
                if (!personIsSelected(selectedPerson) || !(confirmDelete())) {
                    return;
                }

                research = true;
                birthdaysBook.delete(selectedPerson);
                break;
        }
        if (research) {
            actionSearch(actionEvent);
        }
    }

    private boolean confirmDelete() {
        if (DialogManager.showConfirmDialog(resourceBundle.getString("confirm"), resourceBundle.getString("confirm_delete")).get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    private boolean personIsSelected(Person selectedPerson) {
        if (selectedPerson == null) {
            DialogManager.showInfoDialog(resourceBundle.getString("error"), resourceBundle.getString("select_person"));
            return false;
        }
        return true;
    }

    private void findByMonthAndDay() {
        LinkedList<Person> newList = birthdaysBook.getPersonList().stream()
                .filter(p -> p.getDay() == TODAY.getDayOfMonth()
                        && p.getMonth().equals(Months.values()[TODAY.getMonth().getValue() - 1].getName()))
                .collect(Collectors.toCollection(LinkedList::new));
        birthdaysBook.getPersonList().retainAll(newList);
    }


    private void showDialog() {

        if (editDialogStage == null) {
            editDialogStage = new Stage();
            editDialogStage.setTitle(resourceBundle.getString("edit"));
            editDialogStage.setMinHeight(150);
            editDialogStage.setMinWidth(300);
            editDialogStage.setResizable(false);
            editDialogStage.setScene(new Scene(fxmlEdit));
            editDialogStage.initModality(Modality.WINDOW_MODAL);
        }
        editDialogStage.showAndWait(); // для ожидания закрытия окна

    }

    public void actionSearch(ActionEvent actionEvent) {

        if (txtSearch.getText().trim().length() == 0) {
            birthdaysBook.findAll();
        }
        birthdaysBook.find(txtSearch.getText());
        if (!(cbMonthChoice.getSelectionModel().getSelectedIndex() == 0)) {
            LinkedList<Person> newList = birthdaysBook.getPersonList().stream()
                    .filter(p->p.getMonth().equals(cbMonthChoice.getSelectionModel().getSelectedItem().toString()))
                    .collect(Collectors.toCollection(LinkedList::new));
            birthdaysBook.getPersonList().retainAll(newList);
        }
        txtSearch.clear();
        onlyActive.selectedProperty().set(false);
    }
}
