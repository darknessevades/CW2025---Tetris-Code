package com.comp2042;

public class GameController implements InputEventListener {

    // This is the main game logic controller

    // Acts like a bridge between the game logic (board) and the UI (GUI Controller)

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    // Constructor for a new game
    // Creates a 25 x 10 board
    // Create the first piece
    // Sets up the UI
    // Binds score display to score model

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {

        // Most complex method as it handles falling pieces

        boolean canMove = board.moveBrickDown(); // code tries to move piece down
        ClearRow clearRow = null; // initialize to null
        if (!canMove) {
            board.mergeBrickToBackground(); // merge piece to board
            clearRow = board.clearRows(); // clears completed rows (if present)
            if (clearRow.getLinesRemoved() > 0) { // for rows cleared add scores
                board.getScore().add(clearRow.getScoreBonus());
            }
            if (!board.createNewBrick()) { // creates a new piece
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix()); // refreshes the display

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) { // move piece to left
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) { // move piece to right
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) { // rotate the pieces
        board.rotateLeftBrick();
        return board.getViewData();
    }


    @Override
    public void createNewGame() { // reset for a new game!
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }
}
