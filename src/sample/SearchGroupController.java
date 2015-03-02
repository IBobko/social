package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.todo100.social.vk.Engine;
import ru.todo100.social.vk.datas.GroupData;
import ru.todo100.social.vk.strategy.GroupsOperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor Bobko on 01.03.2015.
 */
public class SearchGroupController {
    private final ObservableList<GroupData> data =
            FXCollections.observableArrayList(

            );
    public TableView groupsList;
    public TextField searchString;
    public TableColumn groupIdColumn;
    public TableColumn groupNameColumn;
    public TableColumn groupCanPostedColumn;
    public TableColumn invite;
    public TextArea logger;
    public CheckBox onlyPostCheckbox;

    public void searchAction(ActionEvent actionEvent) {
        groupIdColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("id"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("name"));
        groupCanPostedColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("canPost"));

        String serchString = searchString.getText();
        GroupsOperations groups = new GroupsOperations(Engine.accessToken);

        Integer count = 100;
        Integer maxCount = 100;
        Integer maxCountIter = 10;

        List<GroupData> groupsResult = new ArrayList<>();
        for (int i = 0; i < maxCountIter; i++) {
            List<GroupData> result = groups.search(serchString,i * count,maxCount);
            for (GroupData group: result){
                if (group.getCanPost()!=0) {
                    groupsResult.add(group);
                }
            }
            if (groupsResult.size() < maxCount && result.size() == 100) {
                continue;
            } else {
                break;
            }
        }





        data.addAll(groupsResult);
        groupsList.setItems(data);
    }

    public void invite(ActionEvent actionEvent) {
        GroupsOperations groups = new GroupsOperations(Engine.accessToken);
        for(int i = 0; i < groupsList.getItems().size(); i++) {
            GroupData group = (GroupData) groupsList.getItems().get(i);
            groups.join(group.getId());

            logger.appendText("вы вступили в " + group.getName() + "\n");

        }
    }

    public class CheckBoxCellFactory<S, T>
            implements Callback<TableColumn<S, T>, TableCell<S, T>> {
        @Override public TableCell<S, T> call(TableColumn<S, T> p) {
            return new CheckBoxTableCell<>();
        }
    }

    public void init() {
        Callback<TableColumn<TableView, Boolean>, TableCell<TableView, Boolean>> booleanCellFactory =
                p -> new BooleanCell();

        invite.setCellValueFactory(new PropertyValueFactory<TableView,Boolean>(""));
        invite.setCellFactory(booleanCellFactory);
    }

    class BooleanCell extends TableCell<TableView, Boolean> {
        private CheckBox checkBox;
        public BooleanCell() {
            checkBox = new CheckBox();
            checkBox.setDisable(true);
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(isEditing())
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
                checkBox.setSelected(item);
            }
        }
    }
}
