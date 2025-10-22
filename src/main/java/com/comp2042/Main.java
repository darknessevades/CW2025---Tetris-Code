package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();
        GuiController c = fxmlLoader.getController();

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 360, 510); // Resize to view the whole center
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
        new GameController(c);

        // Fix window size
        primaryStage.setResizable(false);  // Prevent resizing
        primaryStage.sizeToScene();       // Fit window to scene size
    }


    public static void main(String[] args) {
        launch(args);
    }
}
