package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.todo100.social.vk.Engine;
import ru.todo100.social.vk.datas.GroupData;
import ru.todo100.social.vk.strategy.GroupsOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor on 08.02.15.
 */
public class UserGroupsController {


    public TableView groupsList;
    public TableColumn groupNameColumn;
    public TableColumn groupCanPostedColumn;
    public TableColumn groupIdColumn;

    @FXML
    void initialize() {
        this.init();
    }

    private final ObservableList<GroupData> data =
            FXCollections.observableArrayList(

            );

    public void init() {
        groupsList.setItems(data);


        //ArrayList<PropertyValueFactory<Person, String>> columns = new ArrayList<>();
        groupIdColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("id"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("name"));
        groupCanPostedColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("canPost"));

        GroupsOperations groups = new GroupsOperations(Engine.accessToken);

        List<GroupData> userGroups = groups.get();



        data.addAll(userGroups);

//
//        gid.setCellValueFactory( new PropertyValueFactory<GroupData, String>("gid"));
//
//        name.setCellValueFactory(new PropertyValueFactory<GroupData, String>("name"));
//
//        screenName.setCellValueFactory(new PropertyValueFactory<GroupData, String>("screenName"));
//        isClosed.setCellValueFactory(new PropertyValueFactory<GroupData, String>("isClosed"));
//        type.setCellValueFactory(new PropertyValueFactory<GroupData, String>("type"));

//        isAdmin.setCellValueFactory(new PropertyValueFactory<GroupData, String>("isAdmin"));
//        isMember.setCellValueFactory(new PropertyValueFactory<GroupData, String>("isMember"));
//
//        photo.setCellValueFactory(new PropertyValueFactory<GroupData, String>("photo"));
//        photoMedium.setCellValueFactory(new PropertyValueFactory<GroupData, String>("photoMedium"));
//        photoBig.setCellValueFactory(new PropertyValueFactory<GroupData, String>("photoBig"));

        //webView.getEngine().load("https://oauth.vk.com/authorize?client_id=" + this.clientId + "&scope=friends,messages,wall,groups&redirect_uri=https://oauth.vk.com/blank.html&display=page&v=5.27N&response_type=token");
    }
}


