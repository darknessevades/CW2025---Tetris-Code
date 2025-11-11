package com.comp2042.view.panel;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Animated notification panel that displays score bonuses and level-up messages.
 * The notification fades out and moves upward before automatically removing itself.
 */
public class NotificationPanel extends BorderPane {

    private static final int MIN_HEIGHT = 100;
    private static final int MIN_WIDTH = 220;
    private static final int MAX_WIDTH = 200;
    private static final double GLOW_LEVEL = 0.6;
    private static final int FADE_DURATION_MS = 2000;
    private static final int TRANSLATE_DURATION_MS = 2500;
    private static final int TRANSLATE_OFFSET_Y = -60;
    private static final double INITIAL_OPACITY = 1.0;
    private static final double FINAL_OPACITY = 0.0;

    /**
     * Creates a new NotificationPanel with the specified text.
     *
     * @param text the text to display in the notification.
     */
    public NotificationPanel(String text) {
        setMinHeight(MIN_HEIGHT);
        setMinWidth(MIN_WIDTH);
        setMaxWidth(MAX_WIDTH);

        Label label = createNotificationLabel(text);
        setCenter(label);
    }

    /**
     * Displays the notification with fade and translate animations.
     * Automatically removes itself from the parent list when animation completes.
     *
     * @param list is the observable list of nodes to remove from after animation.
     */
    public void showScore(ObservableList<Node> list) {
        ParallelTransition transition = createNotificationAnimation();
        transition.setOnFinished(event -> list.remove(this));
        transition.play();
    }

    private Label createNotificationLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("bonusStyle");
        label.setEffect(new Glow(GLOW_LEVEL));
        label.setTextFill(Color.WHITE);
        return label;
    }

    private ParallelTransition createNotificationAnimation() {
        FadeTransition fadeTransition = createFadeTransition();
        TranslateTransition translateTransition = createTranslateTransition();
        return new ParallelTransition(translateTransition, fadeTransition);
    }

    private FadeTransition createFadeTransition() {
        FadeTransition fade = new FadeTransition(Duration.millis(FADE_DURATION_MS), this);
        fade.setFromValue(INITIAL_OPACITY);
        fade.setToValue(FINAL_OPACITY);
        return fade;
    }

    private TranslateTransition createTranslateTransition() {
        TranslateTransition translate = new TranslateTransition(Duration.millis(TRANSLATE_DURATION_MS), this);
        translate.setByY(TRANSLATE_OFFSET_Y);
        return translate;
    }
}