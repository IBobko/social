package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        try {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            primaryStage.setTitle("Hello World");
            primaryStage.setScene(new Scene(root, 700, 700));
            primaryStage.show();
//        } catch(Exception e) {
//            File f = new File("C:\\log.txt");
//            f.createNewFile();
//            PrintWriter writer = new PrintWriter("C:\\log.txt", "UTF-8");
//            writer.println("The first line");
//            writer.println("The second line");
//            writer.close();
//        }


    }


    public static void main(String[] args) throws IOException {
//        try {
            launch(args);
//        }catch(Exception e){
//            File f = new File("C:\\log.txt");
//            f.createNewFile();
//            PrintWriter writer = new PrintWriter("C:\\log.txt", "UTF-8");
//            writer.println("The first line");
//            writer.println("The second line");
//            writer.close();
//        }
    }
}
