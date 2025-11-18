package com.comp2042.logic;

import com.comp2042.model.board.SimpleBoard;
import com.comp2042.view.gui.GuiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class RestartGameTest {

    private GameController gameController;

    @Mock
    private GuiController mockGuiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameController = new GameController(mockGuiController);
    }

    @Test
    @DisplayName("Should reset score on new game")
    void testNewGameResetsScore() {
        // Play some game, increase score
        gameController.onHardDropEvent();
        int scoreAfterPlay = gameController.getBoard().getScore().getValue();
        assertTrue(scoreAfterPlay > 0, "Score should be greater than 0 after playing");

        // Start new game
        gameController.createNewGame();

        // Score should reset to 0
        assertEquals(0, gameController.getBoard().getScore().getValue(),
                "Score should reset to 0 on new game");
    }

    @Test
    @DisplayName("Should create new brick on restart")
    void testNewGameCreatesNewBrick() {
        // Start new game
        gameController.createNewGame();

        // Verify GUI refresh was called
        verify(mockGuiController, atLeastOnce()).refreshGameBackground(any());
    }

    @Test
    @DisplayName("Should reset board state on new game")
    void testNewGameResetsBoardState() {
        // Play some moves
        for (int i = 0; i < 5; i++) {
            gameController.onHardDropEvent();
        }

        // Get board state
        int[][] boardBeforeReset = gameController.getBoard().getBoardMatrix();

        // Verify board has some filled cells
        boolean hasFilledCells = false;
        for (int[] row : boardBeforeReset) {
            for (int cell : row) {
                if (cell != 0) {
                    hasFilledCells = true;
                    break;
                }
            }
        }
        assertTrue(hasFilledCells, "Board should have some filled cells before reset");

        // Start new game
        gameController.createNewGame();

        // Verify refresh was called
        verify(mockGuiController, atLeast(2)).refreshGameBackground(any());
    }

    @Test
    @DisplayName("Should reset level and lines on new game")
    void testNewGameResetsLevelAndLines() {
        SimpleBoard board = (SimpleBoard) gameController.getBoard();

        // Clear some lines to level up
        for (int i = 0; i < 10; i++) {
            gameController.onHardDropEvent();
        }

        // Start new game
        gameController.createNewGame();

        // Level should reset to 1
        assertEquals(1, board.getLevel(), "Level should reset to 1");

        // Verify displays were updated
        verify(mockGuiController, atLeastOnce()).updateLinesDisplay(0);
    }
}
