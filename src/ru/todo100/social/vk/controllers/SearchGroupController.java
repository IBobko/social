package ru.todo100.social.vk.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.springframework.util.StringUtils;
import ru.todo100.social.vk.Engine;
import ru.todo100.social.vk.datas.DatabaseData;
import ru.todo100.social.vk.datas.GroupData;
import ru.todo100.social.vk.strategy.DatabaseOperations;
import ru.todo100.social.vk.strategy.GroupsOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Igor Bobko
 */
public class SearchGroupController {
    private final ObservableList<GroupData> data =
            FXCollections.observableArrayList(

            );
    private final ObservableList<String> country =
            FXCollections.observableArrayList(

            );
    public TableView groupsList;
    public TextField searchString;
    public TableColumn groupIdColumn;
    public TableColumn groupNameColumn;
    public TableColumn groupCanPostedColumn;
    public TableColumn join;
    public TextArea logger;
    public CheckBox onlyPostCheckbox;
    public ComboBox countryList;
    public ComboBox cityList;

    @SuppressWarnings({"unchecked", "Convert2streamapi"})
    @FXML
    void initialize() {
        groupIdColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("id"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("name"));
        groupCanPostedColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("canPost"));

        Callback<TableColumn<TableView, Boolean>, TableCell<TableView, Boolean>> booleanCellFactory =
                p -> new BooleanCell();

        join.setCellValueFactory(new PropertyValueFactory<TableView, Boolean>(""));
        join.setCellFactory(booleanCellFactory);


        DatabaseOperations database = new DatabaseOperations(Engine.accessToken);

        for (DatabaseData cnt : database.getCountries()) {
            country.add(cnt.getTitle());
        }
        countryList.setItems(country);

        countryList.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                DatabaseOperations database = new DatabaseOperations(Engine.accessToken);
                cityList.getItems().clear();
                for (DatabaseData cnt : database.getCountries()) {
                    if (cnt.getTitle().equals(t1)) {
                        List<DatabaseData> cities = database.getCities(cnt.getId());
                        for (DatabaseData city : cities) {
                            cityList.getItems().add(city.getTitle());
                        }
                    }
                }
            }
        });
    }

    @SuppressWarnings({"unchecked", "UnusedParameters", "Convert2streamapi"})
    public void searchAction(ActionEvent actionEvent) {
        final String searchString = this.searchString.getText();
        if (StringUtils.isEmpty(searchString)) {
            return;
        }

        final GroupsOperations groups = new GroupsOperations(Engine.accessToken);

        Integer count = 100;
        Integer maxCount = 100;
        Integer maxCountIter = 10;
        Integer country_id = null;
        Integer city_id = null;

        String country = (String) countryList.getValue();
        String city = (String) cityList.getValue();

        DatabaseOperations database = new DatabaseOperations(Engine.accessToken);

        if (!StringUtils.isEmpty(country)) {
            for (DatabaseData cnt : database.getCountries()) {
                if (cnt.getTitle().equals(country)) {
                    country_id = cnt.getId();
                }
            }
        }

        if (!StringUtils.isEmpty(city)) {
            for (DatabaseData cnt : database.getCities(country_id)) {
                if (cnt.getTitle().equals(city)) {
                    city_id = cnt.getId();
                }
            }
        }

        final List<GroupData> groupsResult = new ArrayList<>();
        for (int i = 0; i < maxCountIter; i++) {
            final List<GroupData> result = groups.search(searchString, i * count, maxCount, country_id, city_id);
            for (GroupData group : result) {
                if (group.getCanPost() != 0) {
                    groupsResult.add(group);
                }
            }
            if (!(groupsResult.size() < maxCount && result.size() == maxCount)) {
                break;
            }
        }
        data.addAll(groupsResult);
        groupsList.setItems(data);
    }

    @SuppressWarnings("UnusedParameters")
    public void invite(ActionEvent actionEvent) {
        GroupsOperations groups = new GroupsOperations(Engine.accessToken);
        for (int i = 0; i < groupsList.getItems().size(); i++) {


            GroupData group = (GroupData) groupsList.getItems().get(i);
            groups.join(group.getId());
            logger.appendText("You joined in " + group.getName() + "\n");


        }
    }

    class BooleanCell extends TableCell<TableView, Boolean> {
        private CheckBox checkBox;

        public BooleanCell() {
            checkBox = new CheckBox();
            checkBox.setDisable(true);
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (isEditing())
                        commitEdit(newValue == null ? false : newValue);
                }
            });
            this.setGraphic(checkBox);
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            this.setEditable(true);
        }

        @Override
        public void startEdit() {
            super.startEdit();
            if (isEmpty()) {
                return;
            }
            checkBox.setDisable(false);
            checkBox.requestFocus();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            checkBox.setDisable(true);
        }

        public void commitEdit(Boolean value) {
            super.commitEdit(value);
            checkBox.setDisable(true);
        }

        @Override
        public void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (!isEmpty()) {
                checkBox.setSelected(true);
            }
        }
    }
}
