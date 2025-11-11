package com.comp2042.model.score;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Represents the game score with observable properties for UI binding.
 * Uses JavaFX properties to allow automatic UI updates when score changes.
 */
public final class Score {

    private static final int INITIAL_SCORE = 0;

    private final IntegerProperty score;

    /**
     * Creates a new Score initialized to zero.
     */
    public Score() {
        this.score = new SimpleIntegerProperty(INITIAL_SCORE);
    }

    /**
     * Gets the score property for UI binding.
     *
     * @return the observable score property.
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Adds points to the current score.
     *
     * @param points the number of points to add.
     */
    public void add(int points) {
        score.setValue(score.getValue() + points);
    }

    /**
     * Resets the score to zero.
     */
    public void reset() {
        score.setValue(INITIAL_SCORE);
    }
}