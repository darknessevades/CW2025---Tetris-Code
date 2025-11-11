package com.comp2042.view;

import com.comp2042.logic.movement.MatrixOperations;

import java.util.Objects;

/**
 * Immutable data transfer object containing view information for rendering.
 * Holds the current brick, its position, the next brick preview, and ghost piece position.
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int ghostYPosition;

    /**
     * Creates a new ViewData instance.
     *
     * @param brickData is the current brick matrix.
     * @param xPosition is the x-coordinate of the brick.
     * @param yPosition is the y-coordinate of the brick.
     * @param nextBrickData as the next brick matrix for preview.
     * @param ghostYPosition as the y-coordinate where the ghost piece should appear.
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int ghostYPosition) {
        Objects.requireNonNull(brickData, "Brick data cannot be null");
        Objects.requireNonNull(nextBrickData, "Next brick data cannot be null");

        this.brickData = MatrixOperations.copy(brickData);
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = MatrixOperations.copy(nextBrickData);
        this.ghostYPosition = ghostYPosition;
    }

    /**
     * Gets a copy of the current brick matrix.
     *
     * @return a deep copy of the brick data.
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Gets the x-coordinate of the brick.
     *
     * @return the x position.
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Gets the y-coordinate of the brick.
     *
     * @return the y position.
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Gets a copy of the next brick matrix for preview.
     *
     * @return a deep copy of the next brick data.
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Gets the y-coordinate where the ghost piece should be displayed.
     *
     * @return the ghost piece y position.
     */
    public int getGhostYPosition() {
        return ghostYPosition;
    }
}