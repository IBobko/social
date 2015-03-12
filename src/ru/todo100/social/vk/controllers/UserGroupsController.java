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
    public Label sleepTime;

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
    }

    @SuppressWarnings("UnusedParameters")
    public void publish(ActionEvent actionEvent) {
        loggerArea.appendText("Start publish\n");
        String message = messageArea.getText();
        String attachment = attachmentArea.getText();

        Integer sleep = Integer.parseInt(sleepTime.getText());
        GroupsOperations groups = new GroupsOperations(Engine.accessToken);

        List<GroupData> userGroups = groups.get();
        WallOperations wall = new WallOperations(Engine.accessToken);
        for (GroupData gd : userGroups) {
            if (gd.getCanPost() == 1) {
                loggerArea.appendText("Publish in: " + gd.getName() + "\n");
                wall.post(gd.getId() * -1, 0, 0, message, attachment);
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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


