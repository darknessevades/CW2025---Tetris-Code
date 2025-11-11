package com.comp2042.logic.rotation;

import com.comp2042.logic.movement.MatrixOperations;

import java.util.Arrays;
import java.util.Objects;

/**
 * Immutable data class representing rotation information for a Tetromino brick.
 * Contains the shape matrix and the corresponding rotation index.
 */
public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Creates a new NextShapeInfo.
     *
     * @param shape is the 2D array representing the brick shape.
     * @param position is the rotation index (0-based).
     * @throws IllegalArgumentException if shape is null or position is negative.
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        Objects.requireNonNull(shape, "Shape matrix cannot be null");

        if (position < 0) {
            throw new IllegalArgumentException("Position cannot be negative");
        }

        this.shape = MatrixOperations.copy(shape);
        this.position = position;
    }

    /**
     * Gets a copy of the shape matrix.
     *
     * @return a deep copy of the shape matrix.
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Gets the rotation index.
     *
     * @return the rotation index.
     */
    public int getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        NextShapeInfo that = (NextShapeInfo) obj;
        return position == that.position &&
                Arrays.deepEquals(shape, that.shape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(shape), position);
    }

    @Override
    public String toString() {
        return String.format("NextShapeInfo{position=%d, shapeMatrix=%s}",
                position, Arrays.deepToString(shape));
    }
}
