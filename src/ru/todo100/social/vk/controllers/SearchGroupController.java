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
import javafx.util.StringConverter;
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
    public TableView groupsList;
    public TextField searchString;
    public TableColumn groupIdColumn;
    public TableColumn groupNameColumn;
    public TableColumn groupCanPostedColumn;
    public TableColumn join;
    public TextArea logger;
    public CheckBox onlyPostCheckbox;
    public TableColumn groupType;
    @FXML
    private ComboBox<DatabaseData> countryList;
    @FXML
    private ComboBox<DatabaseData> cityList;

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        countryInit();
        groupType.setCellValueFactory(new PropertyValueFactory<GroupData, String>("type"));

        groupIdColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("id"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("name"));
        groupCanPostedColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("canPost"));
        Callback<TableColumn<TableView, Boolean>, TableCell<TableView, Boolean>> booleanCellFactory =
                p -> new BooleanCell();

        join.setCellValueFactory(new PropertyValueFactory<TableView, Boolean>(""));
        join.setCellFactory(booleanCellFactory);
    }

    void countryInit() {
        DatabaseOperations database = new DatabaseOperations(Engine.accessToken);
        ObservableList<DatabaseData> country = FXCollections.observableArrayList();
        country.addAll(database.getCountries());
        countryList.setItems(country);
        countryList.setCellFactory(new Callback<ListView<DatabaseData>, ListCell<DatabaseData>>() {
            @Override
            public ListCell<DatabaseData> call(ListView<DatabaseData> param) {
                return new ListCell<DatabaseData>(){
                    @Override
                    protected void updateItem(DatabaseData item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getTitle());
                        }
                    }
                };
            }

        });
        countryList.setConverter(new StringConverter<DatabaseData>() {
            @Override
            public String toString(DatabaseData object) {
                return object.getTitle();
            }

            @Override
            public DatabaseData fromString(String string) {
                return null;
            }
        });

        cityList.setCellFactory(new Callback<ListView<DatabaseData>, ListCell<DatabaseData>>() {
            @Override
            public ListCell<DatabaseData> call(ListView<DatabaseData> param) {
                return new ListCell<DatabaseData>(){
                    @Override
                    protected void updateItem(DatabaseData item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getTitle());
                        }
                    }
                };
            }

        });
        cityList.setConverter(new StringConverter<DatabaseData>() {
            @Override
            public String toString(DatabaseData object) {
                return object.getTitle();
            }

            @Override
            public DatabaseData fromString(String string) {
                return null;
            }
        });
        countryList.valueProperty().addListener(new ChangeListener<DatabaseData>() {
            @Override
            public void changed(ObservableValue<? extends DatabaseData> observable, DatabaseData oldValue, DatabaseData newValue) {
                DatabaseOperations database = new DatabaseOperations(Engine.accessToken);
                List<DatabaseData> cities = database.getCities(newValue.getId());
                ObservableList<DatabaseData> citiesData = FXCollections.observableArrayList();
                citiesData.addAll(cities);
                cityList.setItems(citiesData);
            }
        });
    }


    @SuppressWarnings("unchecked")
    public void searchAction() throws InterruptedException {
        final ObservableList<GroupData> data = FXCollections.observableArrayList();
        groupsList.setItems(data);
        groupsList.getItems().clear();
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

        if (countryList.getValue()!=null) {
            country_id = countryList.getValue().getId();
        }

        if (cityList.getValue() != null) {
            city_id = cityList.getValue().getId();
        }
        System.out.println(onlyPostCheckbox.isSelected());

        final List<GroupData> groupsResult = new ArrayList<>();
        for (int i = 0; i < maxCountIter; i++) {
            final List<GroupData> result = groups.search(searchString, i * count, maxCount, country_id, city_id);
            for (GroupData group : result) {

                if (onlyPostCheckbox.isSelected() && group.getCanPost() == 0){
                    continue;
                }
                groupsList.getItems().add(group);
                Thread.sleep(10);
                groupsResult.add(group);
            }
            if (!(groupsResult.size() < maxCount && result.size() == maxCount)) {
                break;
            }
        }

    }

    @SuppressWarnings("UnusedParameters")
    public void invite(ActionEvent actionEvent) {
        GroupsOperations groups = new GroupsOperations(Engine.accessToken);
        Integer closedGroups = 0;
        Integer joinedGroups = 0;
        for (int i = 0; i < groupsList.getItems().size(); i++) {
            GroupData group = (GroupData) groupsList.getItems().get(i);
            groups.join(group.getId());
            joinedGroups++;
            if (group.getCanPost()==0) {
                closedGroups++;
            }
            logger.appendText("You joined in " + group.getName() + "\n");
        }
        logger.appendText("Total groups " + groupsList.getItems().size() + "\n");
        logger.appendText("Joined groups " + joinedGroups + "\n");
        logger.appendText("Closed " + closedGroups + "\n");
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
