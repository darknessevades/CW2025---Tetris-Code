package com.comp2042;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

// Revamp the game over panel to fit the new window size

public class GameOverPanel extends BorderPane {
    private Label scoreLabel;
    private Label gameOverLabel;  // THIS MUST BE A CLASS FIELD

    public GameOverPanel() {
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);

        gameOverLabel = new Label("GAME OVER");  // NO "Label" type here - just assignment
        gameOverLabel.getStyleClass().add("gameOverStyle");

        scoreLabel = new Label("");
        scoreLabel.setStyle("-fx-font-size: 20; -fx-text-fill: white;");

        Label restartLabel = new Label("Press N to restart");
        restartLabel.setStyle("-fx-font-size: 16; -fx-text-fill: white;");

        content.getChildren().addAll(gameOverLabel, scoreLabel, restartLabel);
        setCenter(content);
    }

    public void setFinalScore(String score, boolean wasForced) {
        scoreLabel.setText("Final Score: " + score);
        if (gameOverLabel != null) {
            if (wasForced) {
                gameOverLabel.setText("GAME ENDED");
                gameOverLabel.setStyle("-fx-font-size: 24;");  // Smaller font
            } else {
                gameOverLabel.setText("GAME OVER");
                gameOverLabel.setStyle("-fx-font-size: 32;");  // Keep original size
            }
        }
    }

    public void setFinalScore(String score) {
        setFinalScore(score, false);
    }
}
