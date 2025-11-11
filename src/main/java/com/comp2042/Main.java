package com.comp2042;

import com.comp2042.logic.GameController;
import com.comp2042.view.gui.GuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Main application entry point for the Tetris game.
 * Initializes the JavaFX application, loads the UI layout, and starts the game.
 */
public class Main extends Application {

    private static final String WINDOW_TITLE = "TetrisJFX";
    private static final String FXML_LAYOUT = "gameLayout.fxml";
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 700;

    /**
     * Starts the JavaFX application and initializes the game.
     *
     * @param primaryStage the primary stage for this application.
     * @throws IOException if the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        GuiController guiController = loadGameLayout();
        setupStage(primaryStage);
        new GameController(guiController);
    }

    /**
     * Application entry point.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Loads the game layout from FXML and returns the GUI controller.
     *
     * @return the initialized GuiController.
     * @throws IOException if the FXML file cannot be loaded.
     */
    private GuiController loadGameLayout() throws IOException {
        URL location = getClass().getClassLoader().getResource(FXML_LAYOUT);
        if (location == null) {
            throw new IOException("Cannot find FXML file: " + FXML_LAYOUT);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.load();
        return fxmlLoader.getController();
    }

    /**
     * Configures the primary stage with window properties and scene.
     *
     * @param primaryStage as the stage to configure.
     */
    private void setupStage(Stage primaryStage) throws IOException {
        URL location = getClass().getClassLoader().getResource(FXML_LAYOUT);
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
        primaryStage.centerOnScreen();
    }
}