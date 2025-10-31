package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;

    // Added to track current / incoming pieces
    private Brick currentBrick;
    private Brick nextBrick;

    // Added for level system
    private int level = 1;
    private int linesUntilNextLevel = 10;
    private int totalLinesCleared = 0;

    public SimpleBoard(int rows, int cols) {
        this.width = cols;
        this.height = rows;
        currentGameMatrix = new int[rows][cols];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }


    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    @Override
    public boolean createNewBrick() {
        // Handle first brick creation
        if (currentBrick == null && nextBrick == null) {
            currentBrick = brickGenerator.getBrick();
            nextBrick = brickGenerator.getBrick();
        } else {
            currentBrick = nextBrick;
            nextBrick = brickGenerator.getBrick();
        }

        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(4, 0);

        // Check if new piece collides immediately
        boolean collision = MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(),
                (int) currentOffset.getX(), (int) currentOffset.getY());

        // Only check for blocks in danger zone if there's no immediate collision
        // AND only if this isn't the first piece (board isn't empty)
        // This is needed as hard drop might be triggered on empty board, bug fix!
        if (!collision && !isBoardEmpty()) {
            for (int row = 0; row <= 1; row++) {
                for (int col = 0; col < currentGameMatrix[0].length; col++) {
                    if (currentGameMatrix[row][col] != 0) {
                        return true; // Game over
                    }
                }
            }
        }

        return collision;
    }

    // Helper method to check if board is empty, so hard drop could work normally.
    private boolean isBoardEmpty() {
        for (int[] gameMatrix : currentGameMatrix) {
            for (int col = 0; col < currentGameMatrix[0].length; col++) {
                if (gameMatrix[col] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {

        int ghostY = getGhostPosition();

        // Used the stored nextBrick instead of generating a new one
        int[][] nextPieceData = (nextBrick != null)
                ? nextBrick.getShapeMatrix().get(0)  // Default rotation of next piece
                : new int[4][4];  // Empty array if no next piece

        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                nextPieceData,
                ghostY  // Add ghost position
        );
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix, level);
        currentGameMatrix = clearRow.getNewMatrix();

        if (clearRow.getLinesRemoved() > 0) {
            totalLinesCleared += clearRow.getLinesRemoved();
            linesUntilNextLevel -= clearRow.getLinesRemoved();

            // Level up every 10 lines
            if (linesUntilNextLevel <= 0) {
                level++;
                linesUntilNextLevel = 10 + linesUntilNextLevel; // Carry over extra lines
            }
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
        createNewBrick();
        // Reset next brick for new game
        nextBrick = brickGenerator.getBrick();
        createNewBrick();
    }

    public int getLevel() {
        return level;
    }

    public int getTotalLines() {
        return totalLinesCleared;
    }

    // Calculate ghost piece location

    public int getGhostPosition() {
        int ghostY = (int) currentOffset.getY();

        // Use the exact same logic as moveBrickDown()
        int[][] tempMatrix = MatrixOperations.copy(currentGameMatrix);

        // Keep moving down using the same collision detection as regular pieces
        while (ghostY < 25) {  // Go all the way to the limit
            // Check if moving to ghostY + 1 would cause collision (same as moveBrickDown)
            boolean conflict = MatrixOperations.intersect(tempMatrix,
                    brickRotator.getCurrentShape(),
                    (int) currentOffset.getX(),
                    ghostY + 1);

            if (conflict) {
                break;  // Stop here - this is where the piece would land
            }
            ghostY++;
        }

        return ghostY;
    }

}
