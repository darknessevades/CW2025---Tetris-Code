package com.comp2042.view.gui;

import com.comp2042.logic.GameController;
import com.comp2042.controller.InputEventListener;
import com.comp2042.event.EventSource;
import com.comp2042.event.EventType;
import com.comp2042.logic.clear.DownData;
import com.comp2042.logic.movement.MoveEvent;
import com.comp2042.view.ViewData;
import com.comp2042.view.panel.GameOverPanel;
import com.comp2042.view.panel.NotificationPanel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main GUI controller for the Tetris game.
 * Handles all visual rendering, user input, and UI updates.
 */
public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 28;
    private static final int BOARD_OFFSET_Y = -42;
    private static final int HIDDEN_ROWS = 2;
    private static final int MAX_BRICK_SIZE = 4;
    private static final double NEXT_PIECE_SCALE = 0.7;
    private static final double GHOST_OPACITY = 0.4;
    private static final int INITIAL_SPEED_MS = 400;
    private static final int BASE_SPEED_MS = 800;
    private static final int SPEED_DECREASE_PER_LEVEL = 50;
    private static final int MIN_SPEED_MS = 100;
    private static final int LEVEL_UP_NOTIFICATION_OFFSET_Y = 60;

    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private GridPane brickPanel;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private Label scoreLabel;
    @FXML private Label linesLabel;
    @FXML private Label levelLabel;
    @FXML private GridPane nextBrickPanel;
    @FXML private VBox gameOverOverlay;
    @FXML private VBox pauseOverlay;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;
    private Rectangle[][] nextPieceRectangles;
    private Rectangle[][] ghostRectangles;
    private InputEventListener eventListener;
    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    private int level = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCustomFont();
        setupKeyboardControls();
        gameOverPanel.setVisible(false);
    }

    /**
     * Initializes the game view with the board matrix and initial brick.
     *
     * @param boardMatrix is the game board state
     * @param brick is the initial brick data.
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        initializeDisplayMatrix(boardMatrix);
        initializeBrickPanel(brick);
        initializeNextPiecePreview();
        initializeGhostPiece();
        startGameTimer();
    }

    /**
     * Refreshes the current falling brick display.
     *
     * @param brick the updated brick data.
     */
    public void refreshBrick(ViewData brick) {
        if (isPause.getValue()) {
            return;
        }

        updateGhostPiece(brick);
        updateBrickPosition(brick);
        updateBrickAppearance(brick);
        updateNextPiecePreview(brick);
    }

    /**
     * Refreshes the game background after a brick lands.
     *
     * @param board the updated board state.
     */
    public void refreshGameBackground(int[][] board) {
        for (int i = HIDDEN_ROWS; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    /**
     * Displays the game over screen.
     */
    public void gameOver() {
        stopGame();
        showGameOverScreen(false);
    }

    /**
     * Starts a new game.
     *
     * @param actionEvent is the action event (can be null).
     */
    public void newGame(ActionEvent actionEvent) {
        resetGameState();
        hideGameOverScreen();
        startNewGame();
    }


    /**
     * Shows a score notification popup.
     *
     * @param scoreBonus which is the score bonus to display.
     */
    public void showScoreNotification(int scoreBonus) {
        if (scoreBonus > 0) {
            showNotification("+" + scoreBonus, 0);
        }
    }

    /**
     * Handles level up event and updates game speed.
     *
     * @param newLevel is the new level.
     */
    public void onLevelUp(int newLevel) {
        this.level = newLevel;
        updateLevelDisplay(newLevel);
        updateGameSpeed();
        showNotification("LEVEL " + newLevel, LEVEL_UP_NOTIFICATION_OFFSET_Y);
    }

    /**
     * Updates the lines cleared display.
     *
     * @param lines is the total lines cleared.
     */
    public void updateLinesDisplay(int lines) {
        if (linesLabel != null) {
            linesLabel.setText(String.valueOf(lines));
        }
    }

    /**
     * Binds the score property to the score label.
     *
     * @param integerProperty is the score property.
     */
    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null) {
            scoreLabel.textProperty().bind(integerProperty.asString("Score: %d"));
        }
    }

    /**
     * Sets the input event listener.
     *
     * @param eventListener as the event listener.
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    // ======================== Private Helper Methods ========================

    private void loadCustomFont() {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
    }

    private void setupKeyboardControls() {
        gamePanel.setOnKeyPressed(this::handleKeyPress);
    }

    private void handleKeyPress(KeyEvent keyEvent) {
        if (handleNewGameKey(keyEvent)) return;
        if (isGameOver.getValue()) {
            keyEvent.consume();
            return;
        }
        if (handlePauseKey(keyEvent)) return;
        if (isPause.getValue()) return;

        handleMovementKeys(keyEvent);
    }

    private boolean handleNewGameKey(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.N) {
            if (isGameOver.getValue()) {
                newGame(null);
            } else {
                forceGameOver();
            }
            keyEvent.consume();
            return true;
        }
        return false;
    }

    private boolean handlePauseKey(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.P) {
            togglePause();
            keyEvent.consume();
            return true;
        }
        return false;
    }

    private void handleMovementKeys(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();

        if (code == KeyCode.LEFT || code == KeyCode.A) {
            refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
            keyEvent.consume();
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
            keyEvent.consume();
        } else if (code == KeyCode.UP || code == KeyCode.W) {
            refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
            keyEvent.consume();
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
            keyEvent.consume();
        } else if (code == KeyCode.SPACE) {
            handleHardDrop();
            keyEvent.consume();
        }
    }

    private void handleHardDrop() {
        if (eventListener instanceof GameController) {
            ((GameController) eventListener).onHardDropEvent();
        }
    }

    private void initializeDisplayMatrix(int[][] boardMatrix) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        for (int i = HIDDEN_ROWS; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - HIDDEN_ROWS);
            }
        }
    }

    private void initializeBrickPanel(ViewData brick) {
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        updateBrickPosition(brick);
    }

    private void initializeNextPiecePreview() {
        if (nextBrickPanel == null) return;

        nextBrickPanel.getChildren().clear();
        nextPieceRectangles = new Rectangle[MAX_BRICK_SIZE][MAX_BRICK_SIZE];

        for (int i = 0; i < MAX_BRICK_SIZE; i++) {
            for (int j = 0; j < MAX_BRICK_SIZE; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE * NEXT_PIECE_SCALE, BRICK_SIZE * NEXT_PIECE_SCALE);
                rectangle.setFill(Color.TRANSPARENT);
                nextPieceRectangles[i][j] = rectangle;
                nextBrickPanel.add(rectangle, j, i);
            }
        }
    }

    private void initializeGhostPiece() {
        ghostRectangles = new Rectangle[MAX_BRICK_SIZE][MAX_BRICK_SIZE];

        for (int i = 0; i < MAX_BRICK_SIZE; i++) {
            for (int j = 0; j < MAX_BRICK_SIZE; j++) {
                Rectangle ghost = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                ghost.setFill(Color.WHITE);
                ghost.setOpacity(GHOST_OPACITY);
                ghost.setStroke(Color.WHITE);
                ghost.setStrokeWidth(0.5);
                ghost.setArcHeight(9);
                ghost.setArcWidth(9);
                ghostRectangles[i][j] = ghost;
            }
        }
    }

    private void startGameTimer() {
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(INITIAL_SPEED_MS),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    private void updateBrickPosition(ViewData brick) {
        brickPanel.setLayoutX(
                gamePanel.getLayoutX() +
                        brick.getxPosition() * (brickPanel.getVgap() + BRICK_SIZE)
        );
        brickPanel.setLayoutY(
                BOARD_OFFSET_Y + gamePanel.getLayoutY() +
                        brick.getyPosition() * (brickPanel.getHgap() + BRICK_SIZE)
        );
    }

    private void updateBrickAppearance(ViewData brick) {
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
            }
        }
    }

    private void updateNextPiecePreview(ViewData viewData) {
        if (nextPieceRectangles == null || viewData.getNextBrickData() == null) {
            return;
        }

        clearNextPiecePreview();
        int[][] nextPiece = viewData.getNextBrickData();
        BrickBounds bounds = calculateBrickBounds(nextPiece);
        drawCenteredPiece(nextPiece, bounds);
    }

    private void clearNextPiecePreview() {
        for (int i = 0; i < MAX_BRICK_SIZE; i++) {
            for (int j = 0; j < MAX_BRICK_SIZE; j++) {
                nextPieceRectangles[i][j].setFill(Color.TRANSPARENT);
            }
        }
    }

    private BrickBounds calculateBrickBounds(int[][] piece) {
        int minRow = MAX_BRICK_SIZE, maxRow = -1;
        int minCol = MAX_BRICK_SIZE, maxCol = -1;

        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                if (piece[i][j] != 0) {
                    minRow = Math.min(minRow, i);
                    maxRow = Math.max(maxRow, i);
                    minCol = Math.min(minCol, j);
                    maxCol = Math.max(maxCol, j);
                }
            }
        }

        return new BrickBounds(minRow, maxRow, minCol, maxCol);
    }

    private void drawCenteredPiece(int[][] piece, BrickBounds bounds) {
        int pieceHeight = bounds.maxRow - bounds.minRow + 1;
        int pieceWidth = bounds.maxCol - bounds.minCol + 1;
        int offsetRow = (MAX_BRICK_SIZE - pieceHeight) / 2;
        int offsetCol = (MAX_BRICK_SIZE - pieceWidth) / 2;

        for (int i = bounds.minRow; i <= bounds.maxRow; i++) {
            for (int j = bounds.minCol; j <= bounds.maxCol; j++) {
                if (piece[i][j] != 0) {
                    int displayRow = offsetRow + (i - bounds.minRow);
                    int displayCol = offsetCol + (j - bounds.minCol);
                    Rectangle rect = nextPieceRectangles[displayRow][displayCol];
                    rect.setFill(getFillColor(piece[i][j]));
                    rect.setArcHeight(7);
                    rect.setArcWidth(7);
                }
            }
        }
    }

    private void updateGhostPiece(ViewData brick) {
        clearGhostPiece();
        int[][] shape = brick.getBrickData();
        int ghostY = brick.getGhostYPosition();

        for (int i = 0; i < shape.length && i < MAX_BRICK_SIZE; i++) {
            for (int j = 0; j < shape[i].length && j < MAX_BRICK_SIZE; j++) {
                if (shape[i][j] != 0) {
                    int gridRow = ghostY + i - HIDDEN_ROWS;
                    if (gridRow >= 0) {
                        Rectangle ghost = ghostRectangles[i][j];
                        ghost.setVisible(true);
                        gamePanel.add(ghost, brick.getxPosition() + j, gridRow);
                    }
                }
            }
        }
    }

    private void clearGhostPiece() {
        for (int i = 0; i < MAX_BRICK_SIZE; i++) {
            for (int j = 0; j < MAX_BRICK_SIZE; j++) {
                gamePanel.getChildren().remove(ghostRectangles[i][j]);
            }
        }
    }

    private Paint getFillColor(int colorCode) {
        switch (colorCode) {
            case 0: return Color.TRANSPARENT;
            case 1: return Color.AQUA;
            case 2: return Color.BLUEVIOLET;
            case 3: return Color.DARKGREEN;
            case 4: return Color.YELLOW;
            case 5: return Color.RED;
            case 6: return Color.BEIGE;
            case 7: return Color.BURLYWOOD;
            default: return Color.WHITE;
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue()) {
            return;
        }

        DownData downData = eventListener.onDownEvent(event);
        handleRowClearNotification(downData);
        refreshBrick(downData.getViewData());
        gamePanel.requestFocus();
    }

    private void handleRowClearNotification(DownData downData) {
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            showNotification("+" + downData.getClearRow().getScoreBonus(), 0);
        }
    }

    private void showNotification(String message, int yOffset) {
        NotificationPanel notification = new NotificationPanel(message);
        if (yOffset != 0) {
            notification.setTranslateY(yOffset);
        }
        groupNotification.getChildren().add(notification);
        notification.showScore(groupNotification.getChildren());
    }

    private void togglePause() {
        isPause.setValue(!isPause.getValue());

        if (isPause.getValue()) {
            timeLine.pause();
            showPauseOverlay();
        } else {
            timeLine.play();
            hidePauseOverlay();
        }

        gamePanel.requestFocus();
    }

    private void showPauseOverlay() {
        if (pauseOverlay != null) {
            pauseOverlay.setVisible(true);
        }
    }

    private void hidePauseOverlay() {
        if (pauseOverlay != null) {
            pauseOverlay.setVisible(false);
        }
    }

    private void stopGame() {
        timeLine.stop();
        brickPanel.setVisible(false);
    }

    private void showGameOverScreen(boolean forced) {
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(true);
        }

        String currentScore = extractCurrentScore();
        if (gameOverPanel != null) {
            gameOverPanel.setFinalScore(currentScore, forced);
            gameOverPanel.setVisible(true);
        }

        isGameOver.setValue(true);
    }

    private void hideGameOverScreen() {
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
        }
        gameOverPanel.setVisible(false);
    }

    private String extractCurrentScore() {
        if (scoreLabel != null) {
            return scoreLabel.getText().replace("Score: ", "");
        }
        return "0";
    }

    private void resetGameState() {
        timeLine.stop();
        level = 1;
        isPause.setValue(false);
        isGameOver.setValue(false);
    }

    private void startNewGame() {
        brickPanel.setVisible(true);
        eventListener.createNewGame();
        updateDisplays();
        gamePanel.requestFocus();
        timeLine.play();
    }

    private void updateDisplays() {
        updateLevelDisplay(1);
        updateLinesDisplay(0);
    }

    private void updateLevelDisplay(int newLevel) {
        if (levelLabel != null) {
            levelLabel.setText(String.valueOf(newLevel));
        }
    }

    private void updateGameSpeed() {
        int speed = Math.max(MIN_SPEED_MS, BASE_SPEED_MS - (level * SPEED_DECREASE_PER_LEVEL));

        if (timeLine != null) {
            timeLine.stop();
        }

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(speed),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    private void forceGameOver() {
        stopGame();
        showGameOverScreen(true);
    }

    // ======================== Inner Classes ========================

    /**
     * Helper class to store brick bounding box coordinates.
     */
    private static class BrickBounds {
        final int minRow, maxRow, minCol, maxCol;

        BrickBounds(int minRow, int maxRow, int minCol, int maxCol) {
            this.minRow = minRow;
            this.maxRow = maxRow;
            this.minCol = minCol;
            this.maxCol = maxCol;
        }
    }
}