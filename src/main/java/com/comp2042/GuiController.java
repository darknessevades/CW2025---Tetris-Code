package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    // This class is the UI controller that handles all visual rendering and incoming user input.

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel; // Game board grid

    @FXML
    private Group groupNotification; // Score pop up texts

    @FXML
    private GridPane brickPanel; // Current falling piece

    @FXML
    private GameOverPanel gameOverPanel; // Game Over Panel

    @FXML
    private Label scoreLabel; // Scoreboard

    @FXML
    private Label pauseLabel; // Pause screen

    @FXML
    private Label linesLabel; // Danger Line Indicator

    @FXML
    private Label levelLabel; // Level score

    @FXML
    private GridPane nextBrickPanel; // Piece preview section

    @FXML
    private VBox gameOverOverlay; // The overlay for the Game Over Panel

    @FXML
    private VBox pauseOverlay; // Overlay for the pause screen

    @FXML
    private Pane ghostPane; // Overlay for the ghost piece

    private Rectangle[][] displayMatrix; // For the background / locked pieces

    private InputEventListener eventListener; // Handles game logic, input

    private Rectangle[][] rectangles; // Current rectangle pieces

    private Rectangle[][] nextPieceRectangles; // Incoming rectangle pieces

    private Rectangle[][] ghostRectangles; // Preview rectangles

    private Timeline timeLine; // Controls auto falling

    private Line dangerLine; // Add the danger line

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private int level = 1;              // ADD THIS LINE
    private int totalLines = 0;         // ADD THIS LINE

    @Override
    public void initialize(URL location, ResourceBundle resources) { // Renders the custom font, sets keyboard focus
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        // This sets the keyboard inputs to game actions needed to perform
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                // Block all movement if game is over, locks until a new game is started
                if (keyEvent.getCode() == KeyCode.N) {
                    if (isGameOver.getValue() == Boolean.TRUE) {
                        // Game is over - start new game
                        newGame(null);
                    } else {
                        // Game is running - force game over
                        forceGameOver();
                    }
                    keyEvent.consume();
                    return;
                }

                // Block all other keys if game is over
                if (isGameOver.getValue() == Boolean.TRUE) {
                    keyEvent.consume();
                    return;
                }

                // Add the pause toggle "P" to keyboard inputs
                if (keyEvent.getCode() == KeyCode.P) {
                    togglePause();
                    keyEvent.consume();
                    return;
                }

                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    // Hard drop assigned to space key
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        if (eventListener instanceof GameController) {
                            ((GameController) eventListener).onHardDropEvent();
                        }
                        keyEvent.consume();
                    }
                }

                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }

            }
        });
        gameOverPanel.setVisible(false);

        // Potential BUG : This is created but never applied to any UI element?
        // Fix :
        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    // Creates the background grid
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];


        for (int i = 2; i < boardMatrix.length; i++) { // top row is hidden as that's where the blocks spawn, BUT danger zone is not indicated? (BUG)
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2); // i-2 cuz the above 2 rows are skipped
            }
        }

        // Creates the currently falling piece
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        // Position the falling piece
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

        // Initialize next piece preview
        initNextPiecePreview();

        // The auto fall timer
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

        ghostRectangles = new Rectangle[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle ghost = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                ghost.setFill(Color.WHITE);
                ghost.setOpacity(0.4);  // Semi-transparent
                ghost.setStroke(Color.WHITE);
                ghost.setStrokeWidth(1);
                ghostRectangles[i][j] = ghost;
                ghostPane.getChildren().add(ghost);  // Will position later
            }
        }
    }

    // Added next piece preview initialization
    private void initNextPiecePreview() {
        if (nextBrickPanel != null) {
            nextBrickPanel.getChildren().clear();
            nextPieceRectangles = new Rectangle[4][4]; // Max size for any piece
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    Rectangle rectangle = new Rectangle(BRICK_SIZE * 0.8, BRICK_SIZE * 0.8);
                    rectangle.setFill(Color.TRANSPARENT);
                    nextPieceRectangles[i][j] = rectangle;
                    nextBrickPanel.add(rectangle, j, i);
                }
            }
        }
    }

    private void updateNextPiecePreview(ViewData viewData) {
        if (nextPieceRectangles != null && viewData.getNextBrickData() != null) {
            // Preview
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    nextPieceRectangles[i][j].setFill(Color.TRANSPARENT);
                }
            }

            int[][] nextPiece = viewData.getNextBrickData();

            // Find the actual bounds of the piece (non-zero cells)
            int minRow = 4, maxRow = -1, minCol = 4, maxCol = -1;
            for (int i = 0; i < nextPiece.length; i++) {
                for (int j = 0; j < nextPiece[i].length; j++) {
                    if (nextPiece[i][j] != 0) {
                        minRow = Math.min(minRow, i);
                        maxRow = Math.max(maxRow, i);
                        minCol = Math.min(minCol, j);
                        maxCol = Math.max(maxCol, j);
                    }
                }
            }

            // Calculate centering offset
            int pieceHeight = maxRow - minRow + 1;
            int pieceWidth = maxCol - minCol + 1;
            int offsetRow = (4 - pieceHeight) / 2;
            int offsetCol = (4 - pieceWidth) / 2;

            // Draw centered piece
            for (int i = minRow; i <= maxRow; i++) {
                for (int j = minCol; j <= maxCol; j++) {
                    if (nextPiece[i][j] != 0) {
                        int displayRow = offsetRow + (i - minRow);
                        int displayCol = offsetCol + (j - minCol);
                        nextPieceRectangles[displayRow][displayCol].setFill(getFillColor(nextPiece[i][j]));
                        nextPieceRectangles[displayRow][displayCol].setArcHeight(7);
                        nextPieceRectangles[displayRow][displayCol].setArcWidth(7);
                    }
                }
            }
        }
    }


    // Piece colours
    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }

    // Refresh / Update the falling piece as it goes down the grid
    void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            updateGhostPiece(brick);
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
            // Update next piece preview
            updateNextPiecePreview(brick);
        }
    }

    private void updateGhostPiece(ViewData brick) {
        int[][] shape = brick.getBrickData();
        int ghostY = brick.getGhostYPosition();

        // Clear all ghost rectangles first
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ghostRectangles[i][j].setVisible(false);
            }
        }

        // Don't draw ghost if it's at the same position as the current piece
        if (ghostY <= brick.getyPosition()) {
            return;
        }

        for (int i = 0; i < shape.length && i < 4; i++) {
            for (int j = 0; j < shape[i].length && j < 4; j++) {
                if (shape[i][j] != 0) {
                    Rectangle ghost = ghostRectangles[i][j];
                    ghost.setVisible(true);
                    // The positioning calculation needs to account for the game panel offset
                    // and the fact that the first 2 rows are hidden
                    ghost.setLayoutX(gamePanel.getLayoutX() + (brick.getxPosition() + j) * BRICK_SIZE);
                    ghost.setLayoutY(gamePanel.getLayoutY() + (ghostY + i - 1) * BRICK_SIZE);
                }
            }
        }
    }

    // Refresh the new game background as a piece moves down
    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    // This handles the piece falling / line clearing / score notifications
    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    // BUG : This is completely empty! Hence, score won't display at the start
    // FIX : Score binding is implemented
    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null) {
            scoreLabel.textProperty().bind(integerProperty.asString("Score: %d"));
        }
    }

    // Game Over Panel, BUG: Only shows the panel, no instructions to restart / exit
    // Fix = better game over panel
    public void gameOver() {
        timeLine.stop();
        brickPanel.setVisible(false);

        // Show the overlay container which will darken the entire window
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(true);
        }

        // Update score in GameOverPanel if needed
        String currentScore = "0";
        if (this.scoreLabel != null) {
            currentScore = this.scoreLabel.getText().replace("Score: ", "");
        }
        if (gameOverPanel != null) {
            gameOverPanel.setFinalScore(currentScore, false);
        }

        assert gameOverPanel != null;
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    // Resets everything for a new game
    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        // Hide the overlay
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
        }
        // Continue showing the spawning panel when a new game starts
        brickPanel.setVisible(true);
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);

        // Reset displays
        this.level = 1;
        this.totalLines = 0;
        if (levelLabel != null) levelLabel.setText("1");
        if (linesLabel != null) linesLabel.setText("0");
    }

    // Pause Game function, BUG : Completely Empty, can't pause !
    // FIX: Implement the pause function
    public void pauseGame(ActionEvent actionEvent) {
        togglePause();
    }

    private void togglePause() {
        isPause.setValue(!isPause.getValue());
        if (isPause.getValue()) {
            timeLine.pause();
            if (pauseOverlay != null) {
                pauseOverlay.setVisible(true);  // Show pause overlay
            }
        } else {
            timeLine.play();
            if (pauseOverlay != null) {
                pauseOverlay.setVisible(false);  // Hide pause overlay
            }
        }
        gamePanel.requestFocus();
    }

    // Show notification for hard drop
    public void showScoreNotification(int scoreBonus) {
        if (scoreBonus > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + scoreBonus);
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
        }
    }

    // Force Restart
    public void forceGameOver() {
        timeLine.stop();
        brickPanel.setVisible(false);

        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(true);
        }

        String currentScore = "0";
        if (this.scoreLabel != null) {
            currentScore = this.scoreLabel.getText().replace("Score: ", "");
        }
        if (gameOverPanel != null) {
            ((GameOverPanel) gameOverPanel).setFinalScore(currentScore, true);  // true = forced
        }

        assert gameOverPanel != null;
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    private void updateGameSpeed() {
        // Base speed 800ms, decrease by 50ms per level, minimum 100ms
        int speed = Math.max(100, 800 - (level * 50));

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

    // Call this when level changes
    public void onLevelUp(int newLevel) {
        this.level = newLevel;  // Now 'level' refers to the class field

        // Update display
        if (levelLabel != null) {
            levelLabel.setText(String.valueOf(newLevel));
        }

        updateGameSpeed();

        // Show level up notification
        NotificationPanel levelUpPanel = new NotificationPanel("LEVEL " + newLevel);
        levelUpPanel.setTranslateY(60);
        groupNotification.getChildren().add(levelUpPanel);
        levelUpPanel.showScore(groupNotification.getChildren());
    }

    public void updateLinesDisplay(int lines) {
        this.totalLines = lines;
        if (linesLabel != null) {
            linesLabel.setText(String.valueOf(lines));
        }
    }

}
