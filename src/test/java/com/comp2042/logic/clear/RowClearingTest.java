package com.comp2042.logic.clear;

import com.comp2042.logic.movement.MatrixOperations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;


class RowClearingTest {

    private int[][] testMatrix;
    private static final int ROWS = 10;
    private static final int COLS = 10;

    @BeforeEach
    void setUp() {
        testMatrix = new int[ROWS][COLS];
    }

    @Test
    @DisplayName("Should clear single complete row")
    void testClearSingleRow() {
        // Fill bottom row completely
        for (int col = 0; col < COLS; col++) {
            testMatrix[ROWS - 1][col] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(testMatrix, 1);

        assertEquals(1, result.getLinesRemoved(), "Should clear 1 line");
        assertEquals(100, result.getScoreBonus(), "Should award 100 points (level 1)");

        // Check bottom row is now empty
        int[][] newMatrix = result.getNewMatrix();
        for (int col = 0; col < COLS; col++) {
            assertEquals(0, newMatrix[ROWS - 1][col], "Bottom row should be empty after clearing");
        }
    }

    @Test
    @DisplayName("Should clear multiple complete rows")
    void testClearMultipleRows() {
        // Fill bottom 3 rows completely
        for (int row = ROWS - 3; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                testMatrix[row][col] = 1;
            }
        }

        ClearRow result = MatrixOperations.checkRemoving(testMatrix, 1);

        assertEquals(3, result.getLinesRemoved(), "Should clear 3 lines");
        assertEquals(500, result.getScoreBonus(), "Should award 500 points for triple (level 1)");
    }

    @Test
    @DisplayName("Should award Tetris bonus for 4 lines")
    void testTetrisBonus() {
        // Fill bottom 4 rows completely
        for (int row = ROWS - 4; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                testMatrix[row][col] = 1;
            }
        }

        ClearRow result = MatrixOperations.checkRemoving(testMatrix, 1);

        assertEquals(4, result.getLinesRemoved(), "Should clear 4 lines (Tetris!)");
        assertEquals(800, result.getScoreBonus(), "Should award 800 points for Tetris (level 1)");
    }

    @Test
    @DisplayName("Should not clear incomplete rows")
    void testNoIncompleteRowClearing() {
        // Fill bottom row but leave one gap
        for (int col = 0; col < COLS - 1; col++) {
            testMatrix[ROWS - 1][col] = 1;
        }
        testMatrix[ROWS - 1][COLS - 1] = 0; // Gap

        ClearRow result = MatrixOperations.checkRemoving(testMatrix, 1);

        assertEquals(0, result.getLinesRemoved(), "Should not clear incomplete row");
        assertEquals(0, result.getScoreBonus(), "Should award 0 points");
    }

    @Test
    @DisplayName("Should drop rows above cleared lines")
    void testRowsDrop() {
        // Put a block in row 0
        testMatrix[0][5] = 7;

        // Fill bottom row completely
        for (int col = 0; col < COLS; col++) {
            testMatrix[ROWS - 1][col] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(testMatrix, 1);
        int[][] newMatrix = result.getNewMatrix();

        // Block from row 0 should now be in row 1 (dropped down)
        assertEquals(0, newMatrix[0][5], "Top row should be empty");
        assertEquals(7, newMatrix[1][5], "Block should drop one row");
    }

    @Test
    @DisplayName("Should multiply score by level")
    void testLevelScoreMultiplier() {
        // Fill one complete row
        for (int col = 0; col < COLS; col++) {
            testMatrix[ROWS - 1][col] = 1;
        }

        // Level 1
        ClearRow level1 = MatrixOperations.checkRemoving(testMatrix, 1);
        assertEquals(100, level1.getScoreBonus(), "Level 1: 100 points");

        // Level 3
        ClearRow level3 = MatrixOperations.checkRemoving(testMatrix, 3);
        assertEquals(300, level3.getScoreBonus(), "Level 3: 300 points (100 × 3)");

        // Level 5
        ClearRow level5 = MatrixOperations.checkRemoving(testMatrix, 5);
        assertEquals(500, level5.getScoreBonus(), "Level 5: 500 points (100 × 5)");
    }

    @Test
    @DisplayName("Should preserve non-cleared rows")
    void testPreserveNonClearedRows() {
        // Put some blocks in middle rows
        testMatrix[5][0] = 2;
        testMatrix[5][5] = 3;

        // Fill bottom row
        for (int col = 0; col < COLS; col++) {
            testMatrix[ROWS - 1][col] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(testMatrix, 1);
        int[][] newMatrix = result.getNewMatrix();

        // Middle row blocks should drop down by 1
        assertEquals(2, newMatrix[6][0], "Block should drop to row 6");
        assertEquals(3, newMatrix[6][5], "Block should drop to row 6");
    }
}
