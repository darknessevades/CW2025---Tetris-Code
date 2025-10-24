package com.comp2042;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

// Revamp the game over panel to fit the new window size

public class GameOverPanel extends BorderPane {
    private Label scoreLabel;

    public GameOverPanel() {
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);

        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        scoreLabel = new Label("");
        scoreLabel.setStyle("-fx-font-size: 20; -fx-text-fill: white;");

        Label restartLabel = new Label("Press N to restart");
        restartLabel.setStyle("-fx-font-size: 16; -fx-text-fill: white;");

        content.getChildren().addAll(gameOverLabel, scoreLabel, restartLabel);
        setCenter(content);
    }

    public void setFinalScore(String score) {
        scoreLabel.setText("Final Score: " + score);
    }
}
