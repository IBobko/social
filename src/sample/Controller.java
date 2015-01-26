package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLInputElement;
import ru.todo100.social.vk.datas.PostData;
import ru.todo100.social.vk.strategy.WallOperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private String accessToken;

    @FXML
    private WebView webView;

    @FXML
    private TextArea info;

    @FXML
    private TextField groupSearchString;

    private final String clientId = "4742608";

    private final String login = "limit-speed@yandex.ru";

    private final String password = "ineler100";

    @FXML
    private TableView groupsInfo;

    Main mainApp;

    Button b;

    @FXML
    private void initialize() {
        this.init();
        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED){
                    if ( webView.getEngine().getLocation().contains("#")) {
                        String[] splitLocation = webView.getEngine().getLocation().split("#access_token=");
                        String[] temp = splitLocation[1].split("&");
                        accessToken = temp[0];
                    }

                    NodeList nodes = webView.getEngine().getDocument().getElementsByTagName("input");
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Node element = nodes.item(i).getAttributes().getNamedItem("name");
                        if (element.getNodeValue().equals("email")) {
                            nodes.item(i).getAttributes().getNamedItem("value").setNodeValue(login);
                        }
                        if (element.getNodeValue().equals("pass")) {
                            HTMLInputElement passwordElement = (HTMLInputElement) nodes.item(i);
                            passwordElement.setValue(password);
                        }
                        if (element.getNodeValue().equals("submit")) {
                            HTMLInputElement submitElement = (HTMLInputElement) nodes.item(i);
                            submitElement.click();
                        }
                    }
                }
            }
        });

    }

    public static class Person {
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty email;

        private Person(String fName, String lName, String email) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.email = new SimpleStringProperty(email);
        }

        public String getFirstName() {
            return firstName.get();
        }
        public void setFirstName(String fName) {
            firstName.set(fName);
        }

        public String getLastName() {
            return lastName.get();
        }
        public void setLastName(String fName) {
            lastName.set(fName);
        }

        public String getEmail() {
            return email.get();
        }
        public void setEmail(String fName) {
            email.set(fName);
        }

    }

    public void setMain(Main mainApp) {
        this.mainApp = mainApp;

    }

    private final ObservableList<GroupData> data =
            FXCollections.observableArrayList(

            );

    public void handleSubmitButtonAction(ActionEvent actionEvent) throws MalformedURLException {
        String[] pair = webView.getEngine().getLocation().split("#");

        System.out.println(pair[1]);




    }
    @FXML
    TableColumn gid;
    @FXML
    TableColumn name;
    @FXML
    TableColumn screenName;
    @FXML
    TableColumn isClosed;
    @FXML
    TableColumn type;
//    @FXML
//    TableColumn isAdmin;
//    @FXML
//    TableColumn isMember;
//    @FXML
//    TableColumn photo;
//    @FXML
//    TableColumn photoMedium;
//    @FXML
//    TableColumn photoBig;


    public class GroupData {
        private Long gid;
        private String name;
        private String screenName;
        private Boolean isClosed;
        private String type;
        private Boolean isAdmin;
        private Boolean isMember;
        private URI photo;
        private URI photoMedium;
        private URI photoBig;

        public URI getPhotoBig() {
            return photoBig;
        }

        public void setPhotoBig(URI photoBig) {
            this.photoBig = photoBig;
        }

        public Long getGid() {
            return gid;
        }

        public void setGid(Long gid) {
            this.gid = gid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getScreenName() {
            return screenName;
        }

        public void setScreenName(String screenName) {
            this.screenName = screenName;
        }

        public Boolean getIsClosed() {
            return isClosed;
        }

        public void setIsClosed(Boolean isClosed) {
            this.isClosed = isClosed;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Boolean getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(Boolean isAdmin) {
            this.isAdmin = isAdmin;
        }

        public Boolean getIsMember() {
            return isMember;
        }

        public void setIsMember(Boolean isMember) {
            this.isMember = isMember;
        }

        public URI getPhoto() {
            return photo;
        }

        public void setPhoto(URI photo) {
            this.photo = photo;
        }

        public URI getPhotoMedium() {
            return photoMedium;
        }

        public void setPhotoMedium(URI photoMedium) {
            this.photoMedium = photoMedium;
        }
    }




    public void init() {
        groupsInfo.setItems(data);


        ArrayList<PropertyValueFactory<Person, String>> columns = new ArrayList<>();


        gid.setCellValueFactory( new PropertyValueFactory<GroupData, String>("gid"));

        name.setCellValueFactory(new PropertyValueFactory<GroupData, String>("name"));

        screenName.setCellValueFactory(new PropertyValueFactory<GroupData, String>("screenName"));
        isClosed.setCellValueFactory(new PropertyValueFactory<GroupData, String>("isClosed"));
        type.setCellValueFactory(new PropertyValueFactory<GroupData, String>("type"));

//        isAdmin.setCellValueFactory(new PropertyValueFactory<GroupData, String>("isAdmin"));
//        isMember.setCellValueFactory(new PropertyValueFactory<GroupData, String>("isMember"));
//
//        photo.setCellValueFactory(new PropertyValueFactory<GroupData, String>("photo"));
//        photoMedium.setCellValueFactory(new PropertyValueFactory<GroupData, String>("photoMedium"));
//        photoBig.setCellValueFactory(new PropertyValueFactory<GroupData, String>("photoBig"));

        webView.getEngine().load("https://oauth.vk.com/authorize?client_id=" + this.clientId + "&scope=friends,messages,wall&redirect_uri=https://oauth.vk.com/blank.html&display=page&v=5.27N&response_type=token");
    }

    public void searchButtonAction(ActionEvent actionEvent) throws MalformedURLException {
        String serchString = groupSearchString.getText();
        try {
            URL url = new URL("https://api.vk.com/method/groups.search?q=" + serchString + "&access_token=" + accessToken);
            URLConnection connection = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                builder.append(inputLine);

            System.out.println(builder.toString());
            JSONObject o = new JSONObject(builder.toString());
            JSONArray array = o.getJSONArray("response");
            for (int i = 0; i < array.length(); i++) {
                if (array.get(i) instanceof JSONObject) {
                    GroupData groupData = new GroupData();
                    groupData.setGid(array.getJSONObject(i).getLong("gid"));
                    groupData.setName(array.getJSONObject(i).getString("name"));
                    groupData.setScreenName(array.getJSONObject(i).getString("screen_name"));
                    groupData.setIsClosed(Boolean.parseBoolean(String.valueOf(array.getJSONObject(i).getInt("is_closed"))));
                    groupData.setType(array.getJSONObject(i).getString("type"));

                    //groupData.setName(array.getJSONObject(i).getString("photo"));
                    //groupData.setName(array.getJSONObject(i).getString("photo_medium"));
                    //groupData.setName(array.getJSONObject(i).getString("photo_big"));

                    info.setText(info.getText() + "\n + " + array.getJSONObject(i).getString("name") + " " + array.getJSONObject(i).getInt("is_closed"));
                    data.add(groupData);
                }
            }





            WallOperations wall = new WallOperations(accessToken);
            wall.post(99991,0,0,URLEncoder.encode("Всем привет", "UTF-8"));
            List<PostData> posts = wall.get(99991,"",0,0,"", (short) 0);
//            if (wall.delete(99991,posts.get(0).getId())){
//                System.out.println("Deleted");
//            } else {
//                System.out.println("Not Deleted");
//            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public void publishPost(ActionEvent actionEvent) {
        System.out.println(postMessage.getText());
    }
    @FXML
    TextArea postMessage;

}
