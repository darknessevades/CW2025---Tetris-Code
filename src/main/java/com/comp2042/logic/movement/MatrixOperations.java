package com.comp2042.logic.movement;

import com.comp2042.logic.clear.ClearRow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is the class that provides matrix operations for the Tetris game board.
 * Includes collision detection, matrix manipulation, and row clearing logic.
 */
public final class MatrixOperations {

    private static final int EMPTY_CELL = 0;
    private static final int SINGLE_LINE_SCORE = 100;
    private static final int DOUBLE_LINE_SCORE = 300;
    private static final int TRIPLE_LINE_SCORE = 500;
    private static final int TETRIS_SCORE = 800;


    /**
     * Checks if a brick intersects with the board or goes out of bounds.
     *
     * @param matrix , which is the game board matrix.
     * @param brick ,which is the brick matrix to check.
     * @param x  is x-coordinate of the brick.
     * @param y is y-coordinate of the brick.
     * @return true if there is an intersection or out of bounds case, false otherwise.
     */
    public static boolean intersect(int[][] matrix, int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != EMPTY_CELL &&
                        (isOutOfBounds(matrix, targetX, targetY) || matrix[targetY][targetX] != EMPTY_CELL)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the given coordinates are out of bounds.
     *
     * @param matrix which is the game board matrix.
     * @param x is the x-coordinate to check.
     * @param y is the y-coordinate to check.
     * @return true if out of bounds, false otherwise.
     */
    private static boolean isOutOfBounds(int[][] matrix, int x, int y) {
        return x < 0 || y < 0 || y >= matrix.length || x >= matrix[y].length;
    }

    /**
     * Creates a copy of a 2D integer array.
     *
     * @param original is the original array to copy.
     * @return a copy of the array.
     */
    public static int[][] copy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = new int[original[i].length];
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }

    /**
     * Merges a brick into the game board at the desired position.
     *
     * @param filledFields is the current game board.
     * @param brick which is the brick to merge.
     * @param x is the x-coordinate where the brick should be placed.
     * @param y is the y-coordinate where the brick should be placed.
     * @return a new matrix with the brick merged in.
     */
    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] result = copy(filledFields);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                if (brick[j][i] != EMPTY_CELL) {
                    int targetX = x + i;
                    int targetY = y + j;
                    result[targetY][targetX] = brick[j][i];
                }
            }
        }
        return result;
    }

    /**
     * Checks for complete rows and removes them, returning the result with score.
     *
     * @param matrix which is the game board matrix.
     * @param level which is the current game level for score calculation.
     * @return a ClearRow object containing cleared lines count, updated matrix, and score.
     */
    public static ClearRow checkRemoving(int[][] matrix, int level) {
        int[][] updatedMatrix = new int[matrix.length][matrix[0].length];
        Deque<int[]> remainingRows = new ArrayDeque<>();
        int clearedCount = 0;

        for (int i = 0; i < matrix.length; i++) {
            if (isRowComplete(matrix[i])) {
                clearedCount++;
            } else {
                remainingRows.add(copyRow(matrix[i]));
            }
        }

        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = remainingRows.pollLast();
            if (row != null) {
                updatedMatrix[i] = row;
            } else {
                break;
            }
        }

        int scoreBonus = calculateScore(clearedCount, level);
        return new ClearRow(clearedCount, updatedMatrix, scoreBonus);
    }

    /**
     * Checks if a row is completely filled.
     *
     * @param row which is the row to check.
     * @return true if the row has no empty cells, false otherwise.
     */

    private static boolean isRowComplete(int[] row) {
        for (int cell : row) {
            if (cell == EMPTY_CELL) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a copy of a single row.
     *
     * @param row is the row to be copied.
     * @return a copy of the row.
     */
    private static int[] copyRow(int[] row) {
        int[] copy = new int[row.length];
        System.arraycopy(row, 0, copy, 0, row.length);
        return copy;
    }

    /**
     * Calculates the score bonus based on lines cleared and current level.
     *
     * @param linesCleared which is the number of lines cleared.
     * @param level is the current game level.
     * @return the calculated score bonus.
     */
    private static int calculateScore(int linesCleared, int level) {
        int baseScore;
        switch (linesCleared) {
            case 1: baseScore = SINGLE_LINE_SCORE; break;
            case 2: baseScore = DOUBLE_LINE_SCORE; break;
            case 3: baseScore = TRIPLE_LINE_SCORE; break;
            case 4: baseScore = TETRIS_SCORE; break;
            default: baseScore = 0; break;
        }
        return baseScore * level;
    }

    /**
     * Creates a deep copy of a list of 2D integer arrays.
     *
     * @param list is the list to copy.
     * @return a deep copy of the list.
     */
    public static List<int[][]> deepCopyList(List<int[][]> list) {
        return list.stream()
                .map(MatrixOperations::copy)
                .collect(Collectors.toList());
    }
}