package com.comp2042.logic.clear;

import com.comp2042.logic.movement.MatrixOperations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for matrix utility operations.
 */
class MatrixOperationsTest {

    @Test
    @DisplayName("Should create deep copy of matrix")
    void testMatrixCopy() {
        int[][] original = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        int[][] copy = MatrixOperations.copy(original);

        // Modify copy
        copy[0][0] = 999;

        // Original should be unchanged
        assertEquals(1, original[0][0], "Original matrix should not be modified");
        assertEquals(999, copy[0][0], "Copy should be modified");
    }

    @Test
    @DisplayName("Should merge brick into board correctly")
    void testMergeBrick() {
        int[][] board = new int[5][5];
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        int[][] result = MatrixOperations.merge(board, brick, 1, 1);

        // Check merged positions
        assertEquals(1, result[1][1], "Position should be filled");
        assertEquals(1, result[1][2], "Position should be filled");
        assertEquals(1, result[2][1], "Position should be filled");
        assertEquals(1, result[2][2], "Position should be filled");

        // Check other positions remain empty
        assertEquals(0, result[0][0], "Other positions should remain empty");
        assertEquals(0, result[4][4], "Other positions should remain empty");
    }

    @Test
    @DisplayName("Should not modify original board when merging")
    void testMergeDoesNotModifyOriginal() {
        int[][] board = new int[5][5];
        int[][] brick = {{1}};

        MatrixOperations.merge(board, brick, 2, 2);

        // Original board should be unchanged
        assertEquals(0, board[2][2], "Original board should not be modified");
    }

    @Test
    @DisplayName("Should handle empty board merge")
    void testMergeOnEmptyBoard() {
        int[][] board = new int[10][10];

        // Use a 2x2 square brick (safe and simple)
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        int[][] result = MatrixOperations.merge(board, brick, 5, 0);

        // Check brick is placed correctly
        assertEquals(1, result[0][5], "Brick should be placed");
        assertEquals(1, result[0][6], "Brick should be placed");
        assertEquals(1, result[1][5], "Brick should be placed");
        assertEquals(1, result[1][6], "Brick should be placed");
    }
}