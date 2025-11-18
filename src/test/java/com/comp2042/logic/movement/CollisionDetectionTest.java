package com.comp2042.logic.movement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for collision detection logic.
 */
class CollisionDetectionTest {

    private int[][] emptyBoard;
    private int[][] partiallyFilledBoard;
    private int[][] testBrick;

    private static final int BOARD_HEIGHT = 20;
    private static final int BOARD_WIDTH = 10;

    @BeforeEach
    void setUp() {
        emptyBoard = new int[BOARD_HEIGHT][BOARD_WIDTH];
        partiallyFilledBoard = new int[BOARD_HEIGHT][BOARD_WIDTH];

        // Use a 2x2 square brick (simple and safe)
        testBrick = new int[][] {
                {1, 1},
                {1, 1}
        };

        // Fill bottom row of partially filled board
        for (int col = 0; col < BOARD_WIDTH; col++) {
            partiallyFilledBoard[BOARD_HEIGHT - 1][col] = 1;
        }
    }

    @Test
    @DisplayName("Should not detect collision on empty board at valid position")
    void testNoCollisionEmptyBoard() {
        boolean collision = MatrixOperations.intersect(emptyBoard, testBrick, 5, 5);
        assertFalse(collision, "Should not detect collision on empty board");
    }

    @Test
    @DisplayName("Should detect collision with left wall")
    void testCollisionLeftWall() {
        boolean collision = MatrixOperations.intersect(emptyBoard, testBrick, -1, 5);
        assertTrue(collision, "Should detect collision with left wall");
    }

    @Test
    @DisplayName("Should detect collision with right wall")
    void testCollisionRightWall() {
        boolean collision = MatrixOperations.intersect(emptyBoard, testBrick, BOARD_WIDTH, 5);
        assertTrue(collision, "Should detect collision with right wall");
    }

    @Test
    @DisplayName("Should detect collision with bottom")
    void testCollisionBottom() {
        boolean collision = MatrixOperations.intersect(emptyBoard, testBrick, 5, BOARD_HEIGHT);
        assertTrue(collision, "Should detect collision with bottom");
    }

    @Test
    @DisplayName("Should detect collision with existing blocks")
    void testCollisionWithBlocks() {
        // Try to place brick overlapping with filled bottom row
        boolean collision = MatrixOperations.intersect(
                partiallyFilledBoard,
                testBrick,
                0,
                BOARD_HEIGHT - 2  // Place so bottom of brick touches filled row
        );
        assertTrue(collision, "Should detect collision with existing blocks");
    }

    @Test
    @DisplayName("Should not detect collision when brick fits in gap")
    void testNoCollisionInGap() {
        // Create a 3-column gap in the filled row
        for (int col = 3; col < 6; col++) {
            partiallyFilledBoard[BOARD_HEIGHT - 1][col] = 0;
        }

        // Use a single 1x1 brick that fits in the gap
        int[][] singleBlock = new int[][]{{1}};

        boolean collision = MatrixOperations.intersect(
                partiallyFilledBoard,
                singleBlock,
                4,  // Middle of gap
                BOARD_HEIGHT - 1
        );
        assertFalse(collision, "Should not detect collision when brick fits in gap");
    }

    @Test
    @DisplayName("Should detect collision at top boundary")
    void testCollisionTopBoundary() {
        boolean collision = MatrixOperations.intersect(emptyBoard, testBrick, 5, -1);
        assertTrue(collision, "Should detect collision at top boundary");
    }

    @Test
    @DisplayName("Should handle different brick shapes correctly")
    void testDifferentBrickShapes() {
        // Test with 2x2 square
        int[][] square = new int[][] {
                {1, 1},
                {1, 1}
        };

        // Should fit on empty board
        assertFalse(MatrixOperations.intersect(emptyBoard, square, 5, 5),
                "Square brick should fit on empty board");

        // Should collide at right edge
        assertTrue(MatrixOperations.intersect(emptyBoard, square, BOARD_WIDTH - 1, 5),
                "Square brick should collide at right edge");

        // Should collide at bottom
        assertTrue(MatrixOperations.intersect(emptyBoard, square, 5, BOARD_HEIGHT - 1),
                "Square brick should collide at bottom");
    }
}