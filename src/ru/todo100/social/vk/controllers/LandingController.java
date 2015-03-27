package ru.todo100.social.vk.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLInputElement;
import ru.todo100.social.vk.Engine;
import ru.todo100.social.vk.datas.UserData;
import ru.todo100.social.vk.strategy.UserOperations;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings({"FieldCanBeLocal", "UnusedParameters", "SpellCheckingInspection"})
public class LandingController {
    final String yourNameText = "Вы вошли как: #YOUR_VK_NAME";
    private final String clientId = "4742608";
    private final String login = "79686264715";
    private final String password = "erjh4iurh48ut5g";
    public GridPane gridPane;

    @FXML
    private Label yourNameLabel;

    @FXML
    private WebView webView;

    @FXML
    private TableView groupsInfo;

    @FXML
    private void initialize() throws IOException {




//        try {
//            URI uri = new URI("http://yandex.ru");
////            java.awt.Desktop.getDesktop().browse(uri);
//
//
//                Class desktopClazz = Class.forName("java.awt.Desktop");
//                Object desktop = desktopClazz.getMethod("getDesktop").invoke(null);
//
//                Method browseMethod = desktopClazz.getMethod("browse", URI.class);
//            browseMethod.invoke(desktop, new URI("http://yandex.ru"));
//            } catch (Exception e) {
//             //       println("Upgrade to Java 6 or later to launch hyperlinks: {url}");
//            }
////        } catch (URISyntaxException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }

        webView.getEngine().load("https://oauth.vk.com/authorize?client_id=" + this.clientId + "&scope=friends,messages,wall,groups&redirect_uri=https://oauth.vk.com/blank.html&display=page&v=5.27N&response_type=token");
        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {
                    if (webView.getEngine().getLocation().contains("#")) {
                        webView.setVisible(false);
                        String[] splitLocation = webView.getEngine().getLocation().split("#access_token=");
                        String[] temp = splitLocation[1].split("&");
                        Engine.accessToken = temp[0];
                        UserOperations user = new UserOperations(Engine.accessToken);
                        UserData userData = user.get();
                        yourNameLabel.setText(yourNameText.replace("#YOUR_VK_NAME", userData.getFirstName() + " " + userData.getLastName()));
                    }

                    NodeList nodes = webView.getEngine().getDocument().getElementsByTagName("input");
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Node element = nodes.item(i).getAttributes().getNamedItem("name");
                        if (element.getNodeValue().equals("email")) {
                            nodes.item(i).getAttributes().getNamedItem("value").setNodeValue(login);
                        }
                        if (element.getNodeValue().equals("pass")) {
                            HTMLInputElement passwordElement = (HTMLInputElement) nodes.item(i);
                            //passwordElement.setValue(password);
                        }
                        if (element.getNodeValue().equals("submit")) {
                            HTMLInputElement submitElement = (HTMLInputElement) nodes.item(i);
                            //submitElement.click();
                        }
                    }
                }
            }
        });
    }

    public void showUserGroups(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(
                    this.getClass().getResource("userGroups.fxml"));

            stage.setScene(new Scene(root));
            stage.setTitle("My modal window");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchGroups(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(
                    this.getClass().getResource("searchGroups.fxml"));

            stage.setScene(new Scene(root));
            stage.setTitle("Поиск групп");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exitAction(ActionEvent actionEvent) {
        java.net.CookieManager manager = new java.net.CookieManager();
        java.net.CookieHandler.setDefault(manager);
        manager.getCookieStore().removeAll();

        webView.setVisible(true);
        try {
            this.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
