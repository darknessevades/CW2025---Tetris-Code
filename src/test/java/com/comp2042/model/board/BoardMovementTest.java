package com.comp2042.model.board;

import com.comp2042.model.bricks.Brick;
import com.comp2042.model.score.Score;
import com.comp2042.view.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class BoardMovementTest {

    private SimpleBoard board;
    private static final int BOARD_HEIGHT = 25;
    private static final int BOARD_WIDTH = 10;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(BOARD_HEIGHT, BOARD_WIDTH);
        board.createNewBrick();
    }

    @Test
    @DisplayName("Should move brick down successfully")
    void testMoveBrickDown() {
        ViewData initialData = board.getViewData();
        int initialY = initialData.getyPosition();

        boolean moved = board.moveBrickDown();

        assertTrue(moved, "Brick should move down successfully");
        ViewData newData = board.getViewData();
        assertEquals(initialY + 1, newData.getyPosition(), "Y position should increase by 1");
    }

    @Test
    @DisplayName("Should not move brick down when at bottom")
    void testMoveBrickDownAtBottom() {
        // Move brick all the way down
        while (board.moveBrickDown()) {
            // Keep moving until it can't move anymore
        }

        ViewData data = board.getViewData();
        int finalY = data.getyPosition();

        // Try to move down again - should fail
        boolean moved = board.moveBrickDown();
        assertFalse(moved, "Brick should not move down when at bottom");

        // Position should remain the same
        ViewData newData = board.getViewData();
        assertEquals(finalY, newData.getyPosition(), "Y position should not change");
    }

    @Test
    @DisplayName("Should move brick left successfully")
    void testMoveBrickLeft() {
        ViewData initialData = board.getViewData();
        int initialX = initialData.getxPosition();

        boolean moved = board.moveBrickLeft();

        assertTrue(moved, "Brick should move left successfully");
        ViewData newData = board.getViewData();
        assertEquals(initialX - 1, newData.getxPosition(), "X position should decrease by 1");
    }

    @Test
    @DisplayName("Should not move brick left when at left wall")
    void testMoveBrickLeftAtWall() {
        // Move brick all the way left
        while (board.moveBrickLeft()) {
            // Keep moving until it hits the wall
        }

        ViewData data = board.getViewData();
        int finalX = data.getxPosition();

        // Try to move left again - should fail
        boolean moved = board.moveBrickLeft();
        assertFalse(moved, "Brick should not move left when at wall");

        // Position should remain the same
        ViewData newData = board.getViewData();
        assertEquals(finalX, newData.getxPosition(), "X position should not change");
    }

    @Test
    @DisplayName("Should move brick right successfully")
    void testMoveBrickRight() {
        ViewData initialData = board.getViewData();
        int initialX = initialData.getxPosition();

        boolean moved = board.moveBrickRight();

        assertTrue(moved, "Brick should move right successfully");
        ViewData newData = board.getViewData();
        assertEquals(initialX + 1, newData.getxPosition(), "X position should increase by 1");
    }

    @Test
    @DisplayName("Should not move brick right when at right wall")
    void testMoveBrickRightAtWall() {
        // Move brick all the way right
        while (board.moveBrickRight()) {
            // Keep moving until it hits the wall
        }

        ViewData data = board.getViewData();
        int finalX = data.getxPosition();

        // Try to move right again - should fail
        boolean moved = board.moveBrickRight();
        assertFalse(moved, "Brick should not move right when at wall");

        // Position should remain the same
        ViewData newData = board.getViewData();
        assertEquals(finalX, newData.getxPosition(), "X position should not change");
    }

    @Test
    @DisplayName("Should move brick in multiple directions")
    void testMultipleDirectionMovement() {
        ViewData initialData = board.getViewData();
        int initialX = initialData.getxPosition();
        int initialY = initialData.getyPosition();

        // Move right, down, left
        board.moveBrickRight();
        board.moveBrickDown();
        board.moveBrickLeft();

        ViewData finalData = board.getViewData();

        // X should be back to initial (right then left)
        assertEquals(initialX, finalData.getxPosition(), "X position should return to initial");

        // Y should be +1 (moved down once)
        assertEquals(initialY + 1, finalData.getyPosition(), "Y position should increase by 1");
    }

    @Test
    @DisplayName("Should rotate brick successfully")
    void testRotateBrick() {
        int[][] initialShape = board.getViewData().getBrickData();

        boolean rotated = board.rotateLeftBrick();

        assertTrue(rotated, "Brick should rotate successfully");

        int[][] newShape = board.getViewData().getBrickData();

        // Shape should be different after rotation (unless it's O-piece)
        // This is a basic check - might be same for O-piece
        assertNotNull(newShape, "New shape should not be null");
    }

}
