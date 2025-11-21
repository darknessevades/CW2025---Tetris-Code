## GitHub Link

https://github.com/darknessevades/CW2025---Tetris-Code



## Compilation Instructions

1. Clone or download the project from the above GitHub repository.

2. Open the project in any JavaFX supported IDE (IntelliJ, Eclipse).

3. If using IntelliJ, click the **"Main"** button on the top right corner of the screen.

4. Tap on **"Edit Configurations"**.

5. Ensure the following settings are applied:
   - **JDK Version:** 23
   - **JavaFX SDK:** Ensure it is correctly linked under Run Configuration by pasting:
```
     --module-path "path/to/javafx-sdk-25/lib" --add-modules javafx.controls,javafx.fxml
```
     *(You will need to download the JavaFX SDK beforehand)*

6. If starting the program via Maven, dependencies are defined in the `pom.xml` file.

7. Run the program using the Main class: `com.comp2042.Main`



## Implemented Features That Are Working Properly

### Game Over Screen
Added a game over screen when game ends or player restarts the game, also prompts player instructions to restart a game.

### Hard Drop
Added a hard drop game mechanic where the player could instantly drop a Tetromino to the bottom of the game panel. This awards extra points when performed.

### Instruction Section
Added an instruction section that lists out the controls to play the game.

### Level System
Added a level system by keeping track of player's lines cleared and increasing a level if a player clears more than a designated number of lines in game. The higher the level, the higher the score awarded and the faster the Tetrominoes drop.

### Next Piece Preview Panel
Added a preview panel that shows the next Tetromino that drops into the game board.

### Pause Screen
Added a pause screen in game that can be toggled by tapping "P".

### Restart Function
Added a restart function in game that resets the game and shows the score for that game by tapping "N".

### Score System
Improved the score system by creating a score panel that shows the score of the player. Also added score bonuses for increasing levels and number of lines cleared at once.



## New Java Classes

### BrickBounds.java
**Location:** `com.comp2042.view.gui.GuiController`

Helper class to calculate brick bounds for boundary detections.



## Modified Java Classes

### RandomBrickGenerator.java
**Location:** `com.comp2042.model.bricks.RandomBrickGenerator`

- Modified the location of the class to a new package (`logic.bricks` to `model.bricks`)
- Added constants: `INITIAL_BUFFER_SIZE`, `MIN_BUFFER_SIZE`
- Helper methods added:
  - `initializeBrickPool()` - Creates the list of all brick types
  - `fillBuffer(int count)` - Fills buffer with N random bricks
  - `ensureBufferSize()` - Maintains minimum buffer size
  - `getRandomBrick()` - Gets a random brick from pool



### BrickRotator.java
**Location:** `com.comp2042.logic.rotation.BrickRotator`

- Modified the location of the class to a new package (`com.comp2042` to `com.comp2042.logic.rotation`)
- Import statement updated: `logic.bricks.Brick` to `model.bricks.Brick`
- Simplified the code in `getNextShape()` method


### GameController.java
**Location:** `com.comp2042.logic.GameController`

- Modified the location of the class to a new package (`com.comp2042` to `com.comp2042.logic`)
- Added constants: `BOARD_HEIGHT`, `BOARD_WIDTH`, `SOFT_DROP_POINTS`, `HARD_DROP_POINTS_PER_ROW`
- Added fields: `previousLevel`, `totalLines` for new level system
- New helper methods added:
  - `initializeGame()` - Initializes game setup
  - `onHardDropEvent()` - Handles space bar hard drop **(NEW FEATURE)**
  - `handleBrickLanding()` - Extracted brick landing logic
  - `handleSoftDrop(MoveEvent)` - Extracted soft drop scoring
  - `handleRowClearing(ClearRow)` - Extracted row clearing logic
  - `checkAndHandleLevelUp()` - Level progression system logic **(NEW FEATURE)**
  - `calculateDropDistance()` - Calculates hard drop distance
  - `updateScoreForHardDrop(int, ClearRow)` - Updates score for hard drops
  - `refreshView()` - Refreshes all view components
  - `getBoard()` - Helper method for JUnit testing
- Improved variable naming for better readability (`c` to `guiController`)
- `createNewGame()` now resets level and lines



### GameOverPanel.java
**Location:** `com.comp2042.view.panel.GameOverPanel`

- Modified the location of the class to a new package (`com.comp2042` to `com.comp2042.view.panel`)
- Added constants: `CONTENT_SPACING`, `SCORE_FONT_SIZE`, `RESTART_FONT_SIZE`, `GAME_OVER_FONT_SIZE`, `GAME_ENDED_FONT_SIZE`, `WHITE_TEXT`
- Added fields: `scoreLabel`, `gameOverLabel` for dynamic score and game updates
- New methods added:
  - `setFinalScore(String, boolean)` - Sets final score with forced or natural game over distinction (Restart mechanism and game over) **(NEW FEATURE)**
  - `setFinalScore(String)` - Overloaded version for default behavior
  - `createGameOverLabel()` - Factory method for game over label
  - `createScoreLabel()` - Factory method for score label
  - `createRestartLabel()` - Factory method for restart instructions
  - `updateGameOverLabel(boolean)` - Updates label based on game end type
- Now displays score and restart instructions; old version did not show anything as it was just a label
- Layout improved with VBox and proper alignment



### GuiController.java
**Location:** `com.comp2042.view.gui.GuiController`

- Modified the location of the class to a new package (`com.comp2042` to `com.comp2042.view.gui`)
- Added 19 constants: `BOARD_OFFSET_Y`, `HIDDEN_ROWS`, `MAX_BRICK_SIZE`, `NEXT_PIECE_SCALE`, `GHOST_OPACITY`, `INITIAL_SPEED_MS`, `BASE_SPEED_MS`, `SPEED_DECREASE_PER_LEVEL`, `MIN_SPEED_MS`, `LEVEL_UP_NOTIFICATION_OFFSET_Y`
- `BRICK_SIZE` changed from 20 to 28 for enlarged game panel view
- Added new FXML fields: `scoreLabel`, `linesLabel`, `levelLabel`, `nextBrickPanel`, `gameOverOverlay`, `pauseOverlay`
- Added new private fields: `nextPieceRectangles`, `ghostRectangles`, `level`
- Added new features:
  - Ghost piece preview (shows where brick will land)
  - Next piece preview panel
  - Pause functionality with overlay (P key)
  - Level display and tracking
  - Lines cleared display
  - Score binding to label
  - Hard drop support (SPACE key)
  - Force game over (N key during gameplay)
  - Dynamic game speed based on level
- New helper methods added:
  - `showScoreNotification(int)` - Shows score bonus notification
  - `onLevelUp(int)` - Handles level up event and updates speed
  - `updateLinesDisplay(int)` - Updates lines cleared display
- Fixed `bindScore()` - was empty, now actually binds score property
- Fixed `pauseGame()` - was empty, now implements pause functionality
- Made `refreshBrick()` public; was private and interfering with the GameController class
- Removed unused Reflection effect code
- Better keyboard handling and code organization



### Main.java
**Location:** `com.comp2042.Main`

- Added 4 constants: `WINDOW_TITLE`, `FXML_LAYOUT`, `WINDOW_WIDTH`, `WINDOW_HEIGHT`
- Window size increased significantly for better gameplay
- Added null check for FXML resource
- Changed exception type from generic `Exception` to specific `IOException`
- Removed unused `ResourceBundle` variable
- Better variable naming (`c` to `guiController`)
- Added window configuration: `setResizable(false)`, `sizeToScene()`, `centerOnScreen()`
- Import updates for new package locations (`logic.GameController`, `view.gui.GuiController`)



### MatrixOperations.java
**Location:** `com.comp2042.logic.movement.MatrixOperations`

- Modified the location of the class to a new package (`com.comp2042` to `com.comp2042.logic.movement`)
- Added 5 constants: `EMPTY_CELL`, `SINGLE_LINE_SCORE`, `DOUBLE_LINE_SCORE`, `TRIPLE_LINE_SCORE`, `TETRIS_SCORE`
- Removed private constructor and made class `final` instead
- Modified `checkRemoving()` signature; added a `level` parameter for level-based scoring
- Change in scoring system:
  - **OLD:** `50 × count × count` (quadratic: 50, 200, 450, 800)
  - **NEW:** Fixed values multiplied by level (100, 300, 500, 800 base × level)
- Helper methods added:
  - `isRowComplete(int[])` - Checks if row is filled
  - `copyRow(int[])` - Creates copy of single row
  - `calculateScore(int, int)` - Calculates score with level multiplier
- Renamed `checkOutOfBound()` to `isOutOfBounds()` (better naming)
- Simplified `isOutOfBounds()` logic by removing unnecessary variable
- Better variable naming (`tmp` → `updatedMatrix`, `newRows` → `remainingRows`, `myInt` → `copy`)



### NotificationPanel.java
**Location:** `com.comp2042.view.panel.NotificationPanel`

- Modified the location of the class to a new package (`com.comp2042` to `com.comp2042.view.panel.NotificationPanel`)
- Added 8 constants: `MIN_HEIGHT`, `MIN_WIDTH`, `MAX_WIDTH`, `GLOW_LEVEL`, `FADE_DURATION_MS`, `TRANSLATE_DURATION_MS`, `TRANSLATE_OFFSET_Y`, `INITIAL_OPACITY`, `FINAL_OPACITY`
- Added `setMaxWidth(MAX_WIDTH)` which prevents panel from being too wide
- Helper methods added:
  - `createNotificationLabel(String)` - Factory method for label creation
  - `createNotificationAnimation()` - Creates combined animation
  - `createFadeTransition()` - Creates fade animation
  - `createTranslateTransition()` - Creates translate animation
- Changed translate animation: `setToY()` → `setByY()` (now relative positioning)
- Removed unused `Effect` variable; directly uses `new Glow()` instead
- Replaced anonymous class with lambda expression in `setOnFinished()`



### SimpleBoard.java
**Location:** `com.comp2042.model.board.SimpleBoard`

- Modified the location of the class to a new package (`com.comp2042` to `com.comp2042.model.board.SimpleBoard`)
- Added 5 constants: `DEFAULT_SPAWN_X` (4), `DEFAULT_SPAWN_Y` (0), `LINES_PER_LEVEL`, `DANGER_ZONE_ROWS`, `EMPTY_CELL`
- Constructor parameters swapped: `(width, height)` to `(rows, cols)` for better clarity
- Matrix initialization fixed: `new int[width][height]` to `new int[rows][cols]`
- Added new fields:
  - `currentBrick` - Tracks current falling brick
  - `nextBrick` - Tracks next brick for preview
  - `level` - Current game level
  - `linesUntilNextLevel` - Lines needed for next level
  - `totalLinesCleared` - Total lines cleared in game
- New features implemented:
  - Next piece preview system
  - Ghost piece calculation
  - Level progression system
  - Danger zone breach detection
- Modified methods:
  - `moveBrickDown/Left/Right()` - Now use `attemptMove()` helper
  - `createNewBrick()` - Spawn position changed (10→0), now uses brick buffer system
  - `getViewData()` - Now includes ghost position and next piece data
  - `clearRows()` - Now passes level parameter to scoring
  - `newGame()` - Resets level, lines, and brick buffer
- New public method: `getLevel()` - Returns the current game level
- Helper methods added:
  - `attemptMove(Point)` - Unified movement logic for all directions
  - `initializeBricks()` - Manages current/next brick buffer
  - `checkGameOver()` - Checks for game over conditions
  - `isDangerZoneBreached()` - Checks top 2 rows for blocks
  - `isBoardEmpty()` - Checks if board has any blocks
  - `getNextPieceMatrix()` - Gets next piece for preview
  - `updateLevelProgress(int)` - Updates level based on lines cleared
  - `calculateGhostPosition()` - Calculates ghost piece Y position



### Score.java
**Location:** `com.comp2042.model.score.Score`

- Modified the location of the class to a new package (`com.comp2042` to `com.comp2042.model.score.Score`)
- Added a helper method:
  - `getScore()` - Helper method to retrieve score for JUnit testing



## Unexpected Problems

### Problem 1: Ghost Piece Not Previewing Piece Drop Location Correctly

**Issue:**  
The ghost piece (pieces that preview the drop location of a Tetromino) was only hovering in the middle of the game panel and would not show up correctly on the correct position, which is below the game panel or above a prefixed Tetromino. This was because the ghost piece location calculation was not updated in the `refreshBrick()` method, causing the ghost piece to be stuck in a single location and not updating the correct coordinates.

**Fix:**  
Added an `updateGhostPiece()` method call in the `refreshBrick()` method to update ghost position. Also added a `calculateGhostPosition()` method in the `SimpleBoard` class to ensure the ghost piece locations are always accurate and will not overlap with any existing Tetrominoes.



### Problem 2: New Pieces Spawning After Game Ended

**Issue:**  
When game is over due to the game board being filled with pieces and the danger border is reached, new pieces will still generate above the game board. This should not happen as all operations should be over after the game ends. This happens because the `GameController` class is still creating new bricks even after the `gameOver()` method was called.

**Fix:**  
A game over check is implemented in the `onDownEvent()` method in the `GameController` class. Boolean values are used to check if the game over flag is set to true, and if true the game board will stop updating piece locations, essentially freezing the game. The timer is then stopped to prevent automatic piece dropping, and the current brick that is about to be dropped will be hidden. This will create the effect that no pieces will be spawning when game is over.



### Problem 3: Tetromino Drop Speed Decreased on Level Up

**Issue:**  
As per the current level system of the game, the drop speed of a piece will increase as the level number increases. However, there was a problem where on the first level up the drop speed of a piece actually decreases and becomes slower. This is due to the `updateGameSpeed()` method not being called at the start, and there were 2 different initial game speeds, which causes the game speed calculation to be mismatched and slow down on the first level up.

**Fix:**  
The `startGameTimer()` method now calls the `updateGameSpeed()` method at the start to avoid mismatch issues. The `SPEED_DECREASE_PER_LEVEL` variable is also increased to show a more noticeable drop speed for the pieces.

