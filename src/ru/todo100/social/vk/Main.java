package ru.todo100.social.vk;

import com.sun.javafx.application.HostServicesDelegate;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Map;

public class Main extends Application {

    final private String capkey = "47a6813d86e034fbf9f74432a2231b0a";
    @Override
    public void start(Stage primaryStage) throws Exception{
        Engine.application = this;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ru/todo100/social/vk/controllers/landing.fxml"));
        primaryStage.setTitle("Вконтакте");
        primaryStage.setScene(new Scene(root, 700, 700));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
