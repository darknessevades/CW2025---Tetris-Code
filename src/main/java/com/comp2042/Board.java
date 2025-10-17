package com.comp2042;

public interface Board {

    boolean moveBrickDown(); // Moves the current piece down 1 row

    boolean moveBrickLeft(); // Moves the current piece left 1 column

    boolean moveBrickRight(); // Moves the current piece right 1 column

    boolean rotateLeftBrick(); // Rotates the current piece counter-clockwise

    boolean createNewBrick(); // Creates a new piece at the top

    int[][] getBoardMatrix(); // Returns the 2D array for game board state (rendering purposes)

    ViewData getViewData(); // Get the current piece and the info of next piece

    void mergeBrickToBackground(); // Locks down the piece to the bottom when it reaches there

    ClearRow clearRows(); // Checks for cleared rows

    Score getScore(); // Return score

    void newGame(); // Resets board for a new game
}
