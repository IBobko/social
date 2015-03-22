package ru.todo100.social.vk.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.todo100.social.vk.Engine;
import ru.todo100.social.vk.datas.GroupData;
import ru.todo100.social.vk.strategy.GroupsOperations;
import ru.todo100.social.vk.strategy.WallOperations;

import java.util.List;

/**
 * @author Igor Bobko
 */
public class UserGroupsController {
    private final ObservableList<GroupData> data =
            FXCollections.observableArrayList(

            );
    public TableView groupsList;
    public TableColumn groupNameColumn;
    public TableColumn groupCanPostedColumn;
    public TableColumn groupIdColumn;
    public TextArea loggerArea;
    public Button publishButton;
    public TextArea messageArea;
    public TableColumn groupMemberCount;
    public TextField minMemberCount;
    public TextArea attachmentArea;
    public TextField sleepTime;
    public ComboBox pageGroup;

    @FXML
    @SuppressWarnings("unchecked")
    void initialize() {
        loggerArea.appendText("Window is opened\n");

        groupsList.setItems(data);



        groupIdColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("id"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("name"));
        groupCanPostedColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("canPost"));
        groupMemberCount.setCellValueFactory(new PropertyValueFactory<GroupData, String>("memberCount"));


        GroupsOperations groups = new GroupsOperations(Engine.accessToken);

        List<GroupData> userGroups = groups.get();


        data.addAll(userGroups);


        final ObservableList<String> country =
                FXCollections.observableArrayList(

                );
        country.add("Везде");
        country.add("По страница");
        country.add("По группа");
        pageGroup.setItems(country);


        }

    @SuppressWarnings("UnusedParameters")
    public void publish(ActionEvent actionEvent) {
        System.out.println();
        loggerArea.appendText("Start publish\n");
        String message = messageArea.getText();
        String attachment = attachmentArea.getText();

        Integer sleep = Integer.parseInt(sleepTime.getText());
        GroupsOperations groups = new GroupsOperations(Engine.accessToken);

        List<GroupData> userGroups = groups.get();
        WallOperations wall = new WallOperations(Engine.accessToken);
        for (GroupData gd : userGroups) {
            switch(pageGroup.getSelectionModel().getSelectedIndex()) {
                case 0: wall.post(gd.getId() * -1, 0, 0, message, attachment); loggerArea.appendText("Publish in: " + gd.getName() + "\n"); break;
                case 1: if (gd.getCanPost() == 0) wall.post(gd.getId() * -1, 0, 0, message, attachment); loggerArea.appendText("Publish in: " + gd.getName() + "\n");break;
                case 2: if (gd.getCanPost() == 1) wall.post(gd.getId() * -1, 0, 0, message, attachment); loggerArea.appendText("Publish in: " + gd.getName() + "\n");break;
            }
        }
    }

    @SuppressWarnings("UnusedParameters")
    public void leaveGroups(ActionEvent actionEvent) {
        Integer count = Integer.parseInt(minMemberCount.getText());
        GroupsOperations groups = new GroupsOperations(Engine.accessToken);
        List<GroupData> userGroups = groups.get();
        for (GroupData gd : userGroups) {
            if (gd.getMemberCount() < count) {
                groups.leave(gd.getId().intValue());
            }
        }
    }
}


