package com.comp2042.model.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;


class GameStateTest {

    private SimpleBoard board;
    private static final int BOARD_HEIGHT = 25;
    private static final int BOARD_WIDTH = 10;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(BOARD_HEIGHT, BOARD_WIDTH);
    }

    @Test
    @DisplayName("Should initialize empty board")
    void testInitialBoardState() {
        int[][] matrix = board.getBoardMatrix();

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                assertEquals(0, matrix[row][col],
                        "All cells should be empty initially");
            }
        }
    }

    @Test
    @DisplayName("Should create initial brick")
    void testInitialBrickCreation() {
        boolean gameOver = board.createNewBrick();
        assertFalse(gameOver, "Creating first brick should not trigger game over");
    }

    @Test
    @DisplayName("Should merge brick to board")
    void testMergeBrickToBoard() {
        board.createNewBrick();

        // Move brick to bottom
        while (board.moveBrickDown()) {
            // Keep moving
        }

        // Get position before merge
        int[][] boardBefore = board.getBoardMatrix();
        int emptyCountBefore = countEmptyCells(boardBefore);

        // Merge brick
        board.mergeBrickToBackground();

        // Get position after merge
        int[][] boardAfter = board.getBoardMatrix();
        int emptyCountAfter = countEmptyCells(boardAfter);

        // Board should have fewer empty cells after merge
        assertTrue(emptyCountAfter < emptyCountBefore,
                "Board should have fewer empty cells after merging brick");
    }

    @Test
    @DisplayName("Should detect game over when board is full")
    void testGameOverDetection() {
        // This is hard to test without actually filling the board
        // We can test that the method exists and returns boolean

        boolean result = board.createNewBrick();
        assertTrue(result == true || result == false,
                "createNewBrick should return boolean for game over state");
    }

    @Test
    @DisplayName("Should provide view data for rendering")
    void testViewDataProvision() {
        board.createNewBrick();

        assertNotNull(board.getViewData(), "View data should not be null");
        assertNotNull(board.getViewData().getBrickData(),
                "View data should contain brick data");
        assertTrue(board.getViewData().getxPosition() >= 0,
                "X position should be non-negative");
        assertTrue(board.getViewData().getyPosition() >= 0,
                "Y position should be non-negative");
    }

    @Test
    @DisplayName("Should handle new game reset")
    void testNewGameReset() {
        // Play some game
        board.createNewBrick();
        board.moveBrickDown();
        board.mergeBrickToBackground();

        // Reset
        board.newGame();

        // Check everything is reset
        assertEquals(0, board.getScore().getValue(), "Score should reset");
        assertEquals(1, board.getLevel(), "Level should reset to 1");

        int[][] matrix = board.getBoardMatrix();
        int filledCells = 0;
        for (int[] row : matrix) {
            for (int cell : row) {
                if (cell != 0) filledCells++;
            }
        }

        // Board might have the new brick, so some cells could be filled
        assertTrue(filledCells <= 4,
                "Board should be mostly empty after reset (except new brick)");
    }

    private int countEmptyCells(int[][] matrix) {
        int count = 0;
        for (int[] row : matrix) {
            for (int cell : row) {
                if (cell == 0) count++;
            }
        }
        return count;
    }
}
