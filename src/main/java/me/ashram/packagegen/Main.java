package me.ashram.packagegen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/package_generator.fxml"));
        primaryStage.setTitle("Package generator");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }
}
