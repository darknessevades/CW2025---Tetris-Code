package com.comp2042.view.panel;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Game over panel displayed when the game ends.
 * Shows the final score and restart instructions.
 */
public class GameOverPanel extends BorderPane {

    private static final int CONTENT_SPACING = 10;
    private static final int SCORE_FONT_SIZE = 20;
    private static final int RESTART_FONT_SIZE = 16;
    private static final int GAME_OVER_FONT_SIZE = 32;
    private static final int GAME_ENDED_FONT_SIZE = 24;
    private static final String WHITE_TEXT = "-fx-text-fill: white;";

    private final Label scoreLabel;
    private final Label gameOverLabel;

    /**
     * Creates a new GameOverPanel with default styling.
     */
    public GameOverPanel() {
        VBox content = new VBox(CONTENT_SPACING);
        content.setAlignment(Pos.CENTER);

        gameOverLabel = createGameOverLabel();
        scoreLabel = createScoreLabel();
        Label restartLabel = createRestartLabel();

        content.getChildren().addAll(gameOverLabel, scoreLabel, restartLabel);
        setCenter(content);
    }

    /**
     * Sets the final score and game over message.
     *
     * @param score is the final score to display
     * @param wasForced true if game was manually ended, false if natural game over
     */
    public void setFinalScore(String score, boolean wasForced) {
        scoreLabel.setText("Final Score: " + score);
        updateGameOverLabel(wasForced);
    }

    /**
     * Sets the final score with default game over message.
     *
     * @param score is the final score to display.
     */
    public void setFinalScore(String score) {
        setFinalScore(score, false);
    }

    private Label createGameOverLabel() {
        Label label = new Label("GAME OVER");
        label.getStyleClass().add("gameOverStyle");
        return label;
    }

    private Label createScoreLabel() {
        Label label = new Label("");
        label.setStyle(String.format("-fx-font-size: %d; %s", SCORE_FONT_SIZE, WHITE_TEXT));
        return label;
    }

    private Label createRestartLabel() {
        Label label = new Label("Press N to restart");
        label.setStyle(String.format("-fx-font-size: %d; %s", RESTART_FONT_SIZE, WHITE_TEXT));
        return label;
    }

    private void updateGameOverLabel(boolean wasForced) {
        if (wasForced) {
            gameOverLabel.setText("GAME ENDED");
            gameOverLabel.setStyle(String.format("-fx-font-size: %d;", GAME_ENDED_FONT_SIZE));
        } else {
            gameOverLabel.setText("GAME OVER");
            gameOverLabel.setStyle(String.format("-fx-font-size: %d;", GAME_OVER_FONT_SIZE));
        }
    }
}