package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.todo100.social.vk.Engine;
import ru.todo100.social.vk.datas.GroupData;
import ru.todo100.social.vk.strategy.GroupsOperations;
import ru.todo100.social.vk.strategy.WallOperations;

import java.util.List;

/**
 * Created by igor on 08.02.15.
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

    @FXML
    void initialize() {
        this.init();
    }

    public void init() {
        loggerArea.appendText("Window is opened\n");

        groupsList.setItems(data);


        //ArrayList<PropertyValueFactory<Person, String>> columns = new ArrayList<>();
        groupIdColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("id"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("name"));
        groupCanPostedColumn.setCellValueFactory(new PropertyValueFactory<GroupData, String>("canPost"));

        GroupsOperations groups = new GroupsOperations(Engine.accessToken);

        List<GroupData> userGroups = groups.get();


        data.addAll(userGroups);

        //groupsList.getSe
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

    public void publish(ActionEvent actionEvent) {
        loggerArea.appendText("Start publish\n");
        String message = messageArea.getText();

        GroupsOperations groups = new GroupsOperations(Engine.accessToken);

        List<GroupData> userGroups = groups.get();
        WallOperations wall = new WallOperations(Engine.accessToken);
        for (GroupData gd : userGroups) {
            if (gd.getCanPost() == 1) {
                loggerArea.appendText("Publish in: " + gd.getName() + "\n");
                wall.post(gd.getId() * -1, 0, 0, message);
            }
        }
    }
}


