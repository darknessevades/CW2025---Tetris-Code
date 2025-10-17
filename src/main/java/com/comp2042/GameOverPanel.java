package com.comp2042;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

// Shows the game over screen

public class GameOverPanel extends BorderPane {

    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        setCenter(gameOverLabel);
    }

}
