package com.comp2042.logic.rotation;

import com.comp2042.model.bricks.Brick;

/**
 * Manages the rotation state and logic for a Tetromino brick.
 * Handles cycling through different rotational states of a brick
 * and provides access to the current shape matrix.
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape;

    /**
     * Creates a new BrickRotator with no brick assigned.
     */
    public BrickRotator() {
        this.currentShape = 0;
    }

    /**
     * Calculates and returns information about the next rotation state.
     *
     * @return a NextShapeInfo object containing the next shape matrix and rotation index.
     */
    public NextShapeInfo getNextShape() {
        int nextShape = (currentShape + 1) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Returns the current shape matrix for the brick's current rotation.
     *
     * @return a 2D array representing the current shape
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the current rotation index.
     *
     * @param currentShape as the rotation index to set.
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets the brick to be managed by this rotator and resets rotation to initial state.
     *
     * @param brick as the brick to manage.
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }
}