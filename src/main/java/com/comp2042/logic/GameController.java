package com.comp2042.logic;

import com.comp2042.controller.InputEventListener;
import com.comp2042.event.EventSource;
import com.comp2042.logic.clear.ClearRow;
import com.comp2042.logic.clear.DownData;
import com.comp2042.logic.movement.MoveEvent;
import com.comp2042.model.board.Board;
import com.comp2042.model.board.SimpleBoard;
import com.comp2042.view.ViewData;
import com.comp2042.view.gui.GuiController;

/**
 * Main game logic controller that acts as a bridge between the game logic (Board)
 * and the user interface (GuiController).
 * Handles player input events and coordinates game state updates.
 */
public class GameController implements InputEventListener {

    private static final int BOARD_HEIGHT = 25;
    private static final int BOARD_WIDTH = 10;
    private static final int SOFT_DROP_POINTS = 1;
    private static final int HARD_DROP_POINTS_PER_ROW = 2;

    private Board board;
    private final GuiController viewGuiController;
    private int previousLevel;
    private int totalLines;

    /**
     * Creates a new GameController and initializes the game.
     *
     * @param guiController is the GUI controller for view updates.
     */
    public GameController(GuiController guiController) {
        this.viewGuiController = guiController;
        this.board = new SimpleBoard(BOARD_HEIGHT, BOARD_WIDTH);
        this.previousLevel = 1;
        this.totalLines = 0;

        initializeGame();
    }

    /**
     * Initializes the game by creating the first brick and setting up the view.
     */
    private void initializeGame() {
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;

        if (!canMove) {
            clearRow = handleBrickLanding();
        } else {
            handleSoftDrop(event);
        }

        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public void createNewGame() {
        board.newGame();
        totalLines = 0;
        previousLevel = 1;
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    /**
     * Handles the hard drop action where the brick instantly falls to the bottom.
     */
    public void onHardDropEvent() {
        int dropDistance = calculateDropDistance();

        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        updateScoreForHardDrop(dropDistance, clearRow);
        handleRowClearing(clearRow);

        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        refreshView();
    }

    /**
     * Handles brick landing logic including merging, clearing rows, and creating new brick.
     *
     * @return the ClearRow result after processing.
     */
    private ClearRow handleBrickLanding() {
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();

        handleRowClearing(clearRow);

        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        return clearRow;
    }

    /**
     * Handles soft drop scoring when user manually moves piece down.
     *
     * @param event the movement event.
     */
    private void handleSoftDrop(MoveEvent event) {
        if (event.getEventSource() == EventSource.USER) {
            board.getScore().add(SOFT_DROP_POINTS);
        }
    }

    /**
     * Handles row clearing, score updates, and level progression.
     *
     * @param clearRow represents the row clearing result.
     */
    private void handleRowClearing(ClearRow clearRow) {
        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());

            totalLines += clearRow.getLinesRemoved();
            viewGuiController.updateLinesDisplay(totalLines);

            checkAndHandleLevelUp();
        }
    }

    /**
     * Checks if the player has leveled up and updates the view accordingly.
     */
    private void checkAndHandleLevelUp() {
        int currentLevel = ((SimpleBoard) board).getLevel();
        if (currentLevel != previousLevel) {
            viewGuiController.onLevelUp(currentLevel);
            previousLevel = currentLevel;
        }
    }

    /**
     * Calculates how far the brick will drop during a hard drop.
     *
     * @return the number of rows the brick dropped
     */
    private int calculateDropDistance() {
        int dropDistance = 0;
        while (board.moveBrickDown()) {
            dropDistance++;
        }
        return dropDistance;
    }

    /**
     * Updates the score for a hard drop action.
     *
     * @param dropDistance is the distance the brick dropped.
     * @param clearRow is the row clearing result.
     */
    private void updateScoreForHardDrop(int dropDistance, ClearRow clearRow) {
        board.getScore().add(dropDistance * HARD_DROP_POINTS_PER_ROW);

        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            viewGuiController.showScoreNotification(clearRow.getScoreBonus());
        }
    }

    /**
     * Refreshes all view components.
     */
    private void refreshView() {
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        viewGuiController.refreshBrick(board.getViewData());
    }
}
