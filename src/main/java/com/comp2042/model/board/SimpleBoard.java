package com.comp2042.model.board;

import com.comp2042.logic.clear.ClearRow;
import com.comp2042.logic.movement.MatrixOperations;
import com.comp2042.logic.rotation.BrickRotator;
import com.comp2042.logic.rotation.NextShapeInfo;
import com.comp2042.model.bricks.Brick;
import com.comp2042.model.bricks.BrickGenerator;
import com.comp2042.model.bricks.RandomBrickGenerator;
import com.comp2042.model.score.Score;
import com.comp2042.view.ViewData;

import java.awt.Point;

/**
 * Implementation of the Tetris game board.
 * Manages the game grid, brick movements, collisions, and game state.
 */
public class SimpleBoard implements Board {

    private static final int DEFAULT_SPAWN_X = 4;
    private static final int DEFAULT_SPAWN_Y = 0;
    private static final int LINES_PER_LEVEL = 10;
    private static final int DANGER_ZONE_ROWS = 2;
    private static final int EMPTY_CELL = 0;

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private final Score score;

    private int[][] currentGameMatrix;
    private Point currentOffset;
    private Brick currentBrick;
    private Brick nextBrick;

    private int level;
    private int linesUntilNextLevel;
    private int totalLinesCleared;

    /**
     * Creates a new SimpleBoard with the predefined dimensions.
     *
     * @param rows is the number of rows in the board.
     * @param cols is the number of columns in the board.
     */
    public SimpleBoard(int rows, int cols) {
        this.width = cols;
        this.height = rows;
        this.currentGameMatrix = new int[rows][cols];
        this.brickGenerator = new RandomBrickGenerator();
        this.brickRotator = new BrickRotator();
        this.score = new Score();
        this.level = 1;
        this.linesUntilNextLevel = LINES_PER_LEVEL;
        this.totalLinesCleared = 0;
    }

    @Override
    public boolean moveBrickDown() {
        Point newPosition = new Point(currentOffset);
        newPosition.translate(0, 1);
        return attemptMove(newPosition);
    }

    @Override
    public boolean moveBrickLeft() {
        Point newPosition = new Point(currentOffset);
        newPosition.translate(-1, 0);
        return attemptMove(newPosition);
    }

    @Override
    public boolean moveBrickRight() {
        Point newPosition = new Point(currentOffset);
        newPosition.translate(1, 0);
        return attemptMove(newPosition);
    }

    @Override
    public boolean rotateLeftBrick() {
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean hasCollision = MatrixOperations.intersect(
                currentGameMatrix,
                nextShape.getShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );

        if (!hasCollision) {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
        return false;
    }

    @Override
    public boolean createNewBrick() {
        initializeBricks();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(DEFAULT_SPAWN_X, DEFAULT_SPAWN_Y);

        return checkGameOver();
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        int ghostY = calculateGhostPosition();
        int[][] nextPieceData = getNextPieceMatrix();

        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                nextPieceData,
                ghostY
        );
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix, level);
        currentGameMatrix = clearRow.getNewMatrix();

        if (clearRow.getLinesRemoved() > 0) {
            updateLevelProgress(clearRow.getLinesRemoved());
        }

        return clearRow;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void newGame() {
        currentGameMatrix = new int[height][width];
        score.reset();
        level = 1;
        linesUntilNextLevel = LINES_PER_LEVEL;
        totalLinesCleared = 0;
        currentBrick = null;
        nextBrick = null;
        createNewBrick();
    }

    /**
     * Gets the current game level.
     *
     * @return the current level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Attempts to move the brick to a new position.
     *
     * @param newPosition as the target position.
     * @return true if the move was successful, false if blocked.
     */
    private boolean attemptMove(Point newPosition) {
        boolean hasCollision = MatrixOperations.intersect(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                (int) newPosition.getX(),
                (int) newPosition.getY()
        );

        if (!hasCollision) {
            currentOffset = newPosition;
            return true;
        }
        return false;
    }

    /**
     * Initializes the current and next bricks.
     * On first call, generates both bricks. Subsequently, shifts next to current.
     */
    private void initializeBricks() {
        if (currentBrick == null && nextBrick == null) {
            currentBrick = brickGenerator.getBrick();
            nextBrick = brickGenerator.getBrick();
        } else {
            currentBrick = nextBrick;
            nextBrick = brickGenerator.getBrick();
        }
    }

    /**
     * Checks if the game is over due to new brick collision or danger zone breach.
     *
     * @return true if game over, false otherwise.
     */
    private boolean checkGameOver() {
        boolean immediateCollision = MatrixOperations.intersect(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );

        if (immediateCollision) {
            return true;
        }

        if (!isBoardEmpty() && isDangerZoneBreached()) {
            return true;
        }

        return false;
    }

    /**
     * Checks if any blocks exist in the danger zone (top rows).
     *
     * @return true if danger zone is breached, false otherwise.
     */
    private boolean isDangerZoneBreached() {
        for (int row = 0; row < DANGER_ZONE_ROWS; row++) {
            for (int col = 0; col < currentGameMatrix[0].length; col++) {
                if (currentGameMatrix[row][col] != EMPTY_CELL) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the board is completely empty.
     *
     * @return true if board is empty, false otherwise.
     */
    private boolean isBoardEmpty() {
        for (int[] row : currentGameMatrix) {
            for (int cell : row) {
                if (cell != EMPTY_CELL) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the matrix for the next piece to display.
     *
     * @return the next piece matrix or empty array if none exists.
     */
    private int[][] getNextPieceMatrix() {
        if (nextBrick != null) {
            return nextBrick.getShapeMatrix().get(0);
        }
        return new int[4][4];
    }

    /**
     * Updates level progress based on lines cleared.
     *
     * @param linesCleared is the number of lines just cleared.
     */
    private void updateLevelProgress(int linesCleared) {
        totalLinesCleared += linesCleared;
        linesUntilNextLevel -= linesCleared;

        if (linesUntilNextLevel <= 0) {
            level++;
            linesUntilNextLevel += LINES_PER_LEVEL;
        }
    }

    /**
     * Calculates where the ghost piece (preview) should be positioned.
     *
     * @return the Y coordinate where the current brick would land.
     */
    private int calculateGhostPosition() {
        int ghostY = (int) currentOffset.getY();

        while (ghostY < height) {
            boolean collision = MatrixOperations.intersect(
                    currentGameMatrix,
                    brickRotator.getCurrentShape(),
                    (int) currentOffset.getX(),
                    ghostY + 1
            );

            if (collision) {
                break;
            }
            ghostY++;
        }

        return ghostY;
    }
}