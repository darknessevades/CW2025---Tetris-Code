package com.comp2042.model.score;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;


class ScoringSystemTest {

    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
    }

    @Test
    @DisplayName("Should start at zero")
    void testInitialScore() {
        assertEquals(0, score.getValue(), "Score should start at 0");
    }

    @Test
    @DisplayName("Should add points correctly")
    void testAddPoints() {
        score.add(100);
        assertEquals(100, score.getValue(), "Score should be 100");

        score.add(50);
        assertEquals(150, score.getValue(), "Score should be 150");
    }

    @Test
    @DisplayName("Should accumulate points over multiple adds")
    void testAccumulatePoints() {
        for (int i = 0; i < 10; i++) {
            score.add(10);
        }
        assertEquals(100, score.getValue(), "Score should be 100 after 10 adds of 10");
    }

    @Test
    @DisplayName("Should reset to zero")
    void testResetScore() {
        score.add(500);
        assertEquals(500, score.getValue(), "Score should be 500");

        score.reset();
        assertEquals(0, score.getValue(), "Score should reset to 0");
    }

    @Test
    @DisplayName("Should handle large scores")
    void testLargeScores() {
        score.add(1000000);
        assertEquals(1000000, score.getValue(), "Should handle large scores");
    }

    @Test
    @DisplayName("Should bind to property correctly")
    void testScoreProperty() {
        assertNotNull(score.scoreProperty(), "Score property should not be null");

        score.add(250);
        assertEquals(250, score.scoreProperty().get(),
                "Property should reflect score value");
    }
}
