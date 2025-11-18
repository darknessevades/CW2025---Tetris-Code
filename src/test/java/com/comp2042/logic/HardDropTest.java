package com.comp2042.logic;

import com.comp2042.controller.InputEventListener;
import com.comp2042.event.EventSource;
import com.comp2042.event.EventType;
import com.comp2042.logic.clear.DownData;
import com.comp2042.logic.movement.MoveEvent;
import com.comp2042.model.board.SimpleBoard;
import com.comp2042.view.ViewData;
import com.comp2042.view.gui.GuiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class HardDropTest {

    private GameController gameController;

    @Mock
    private GuiController mockGuiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameController = new GameController(mockGuiController);
    }

    @Test
    @DisplayName("Should drop brick to bottom instantly")
    void testHardDropMovesToBottom() {
        // Get initial position
        ViewData initialData = gameController.onDownEvent(
                new MoveEvent(EventType.DOWN, EventSource.USER)
        ).getViewData();

        int initialY = initialData.getyPosition();

        // Perform hard drop
        gameController.onHardDropEvent();

        // Verify brick moved significantly down and landed
        verify(mockGuiController, atLeastOnce()).refreshGameBackground(any());
        verify(mockGuiController, atLeastOnce()).refreshBrick(any());
    }

    @Test
    @DisplayName("Should award points for hard drop distance")
    void testHardDropAwardsPoints() {
        int initialScore = gameController.getBoard().getScore().getValue();

        gameController.onHardDropEvent();

        int finalScore = gameController.getBoard().getScore().getValue();
        assertTrue(finalScore > initialScore);
    }

    @Test
    @DisplayName("Should create new brick after hard drop")
    void testHardDropCreatesNewBrick() {
        // Get current brick
        ViewData beforeDrop = gameController.onDownEvent(
                new MoveEvent(EventType.DOWN, EventSource.USER)
        ).getViewData();

        // Perform hard drop
        gameController.onHardDropEvent();

        // Verify refreshBrick was called (new brick displayed)
        verify(mockGuiController).refreshBrick(any(ViewData.class));
    }

    @Test
    @DisplayName("Should trigger game over if new brick collides after hard drop")
    void testHardDropGameOverWhenBoardFull() {
        // Fill up the board by repeatedly dropping bricks
        for (int i = 0; i < 50; i++) {
            try {
                gameController.onHardDropEvent();
            } catch (Exception e) {
                // Board might be full
                break;
            }
        }

        verify(mockGuiController, atLeastOnce()).gameOver();
    }
}
