package com.comp2042.model.board;

import com.comp2042.logic.clear.ClearRow;
import com.comp2042.model.score.Score;
import com.comp2042.view.ViewData;

/**
 * Represents the game board for Tetris.
 * Manages brick movement, collision detection, row clearing, and game state.
 */
public interface Board {

    /**
     * Moves the current brick down by one row.
     *
     * @return true if the move was successful, false if the brick cannot move down
     */
    boolean moveBrickDown();

    /**
     * Moves the current brick left by one column.
     *
     * @return true if the move was successful, false if blocked
     */
    boolean moveBrickLeft();

    /**
     * Moves the current brick right by one column.
     *
     * @return true if the move was successful, false if blocked
     */
    boolean moveBrickRight();

    /**
     * Rotates the current brick counter-clockwise.
     *
     * @return true if the rotation was successful, false if blocked
     */
    boolean rotateLeftBrick();

    /**
     * Creates a new brick at the top of the board.
     *
     * @return true if game over (new brick collides immediately), false otherwise.
     */
    boolean createNewBrick();

    /**
     * Gets the current state of the game board as a 2D matrix.
     *
     * @return a 2D array representing the board state.
     */
    int[][] getBoardMatrix();

    /**
     * Gets the view data for rendering the current and next brick.
     *
     * @return the current view data.
     */
    ViewData getViewData();

    /**
     * Merges the current brick into the board background when it lands.
     */
    void mergeBrickToBackground();

    /**
     * Checks for and clears any completed rows.
     *
     * @return a ClearRow object containing clearing results and score bonus.
     */
    ClearRow clearRows();

    /**
     * Gets the current game score.
     *
     * @return the Score object.
     */
    Score getScore();

    /**
     * Resets the board for a new game.
     */
    void newGame();
}