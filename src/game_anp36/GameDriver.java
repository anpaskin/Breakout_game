package game_anp36;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/** GameDriver is the main game engine. MainGame initializes a new GameDriver, calls
 * setLevel, and calls startGameLoop to start the game.
 *
 * @author Aaron Paskin
 *
 */
public class GameDriver {
	
	private Stage gameStage;
	private Scene gameSurface;
	private int millisecondDelay;
	private double secondDelay;
	private Timeline animation;
	private int levelNum;
	private Circle ball;
	private int lives;
	private Text lifeCount;
	private int ballXSpeed;
	private int ballYSpeed;
	public static final int DEFAULT_BALLXSPEED = 200;
	public static int DEFAULT_BALLYSPEED = 150;
	private Rectangle paddle;
	private Rectangle lazer;
	private boolean powerUpActive;
	private boolean stickyPaddle;
	private int powerUpClock;
	private Group root;
	private BlockManager blockManager;
	private final int POWER_UP_MAX = 3;
	private int ammo;
	private boolean paused;
	private SplashScreen pauseScreen;
	private boolean homeScreenActive;
	private SplashScreen homeScreen;
	private boolean gameOver;
	private SplashScreen gameOverScreen;
	private int consecBlocks;
	public static final int KEY_INPUT_SPEED = 20;
	public static final int NUM_ROWS = 5;
	public static final int NUM_COLS = 9;
	
	public GameDriver(int fps) {
		millisecondDelay = 1000 / fps;
		secondDelay = 1.0 / fps;
		levelNum = 1;
		powerUpActive = false;
	}
	
	protected final void startGameLoop() {
		KeyFrame frame = new KeyFrame(Duration.millis(millisecondDelay),
                e -> step(secondDelay));
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
		gameSurface.setOnKeyPressed(e -> input(e.getCode()));
	}
	
	protected final void stopGameLoop() {
		animation.stop();
	}
	
	/** step updates the positions and statuses of all elements of the game. It ends
	 * the game if the player runs out of lives, advances to the next level once a player
	 * destroys all of the blocks, updates the position of the ball, delivers and 
	 * deactivates power-ups, detects collisions with the walls, paddle, and blocks, 
	 * removes blocks when destroyed, and adds bonus lives when appropriate.
	 * @param elapsedTime		time since last call to step*/
	private void step(double elapsedTime) {
		if(lives == 0) {
			gameOver();
		}
		if(blockManager.getBlockList().size() == 0) {
			advanceLevel();
		}
		double ballYBefore = ball.getCenterY();
		if(!paused && !homeScreenActive) updateBallPosition(elapsedTime);
		double ballYAfter = ball.getCenterY();
		if(powerUpActive) {
			if(ballYBefore >= 300 && ballYAfter < 300) {
				powerUpClock++;
			}
			if((powerUpClock > POWER_UP_MAX || paddle.getFill().equals(Color.MIDNIGHTBLUE)) && ammo == 0) {
				deactivatePowerUp();
			}
		}
		setPaddleColor();
		paddleBounce();
		ceilingAndWallBounce();
		floorBounce();
		blockManager.addCollisions(lazer);
		if(!paused && !homeScreenActive) updateLazerPosition(elapsedTime);
		blockBounce();
		deliverPowerUp();
		blockManager.cleanUp();
		removeBlocksFromGame();
		getLifeBonus();
	}
	
	/** getLifeBonus rewards the player with a bonus life if he/she destroys
	 * five or more blocks in one paddle hit.*/
	private void getLifeBonus() {
		if(consecBlocks >= 5) {
			incrementLives();
			consecBlocks = 0;
		}
	}
	
	/** gameOver initializes and displays the game over screen. */
	private void gameOver() {
		gameOver = true;
		gameOverScreen = new SplashScreen(new Rectangle(450, 400), Color.PINK, new Text("GAME OVER"));
		gameOverScreen.addText(130, 110, "Press the space bar to restart.");
		root.getChildren().addAll(gameOverScreen.bundle());
	}

	/** setPaddleColor sets the color of the paddle depending on what power-up is active.*/
	private void setPaddleColor() {
		if(paddle.getWidth() > 65) {
			paddle.setFill(Color.FIREBRICK);
		}
		else if(stickyPaddle) {
			paddle.setFill(Color.PURPLE);
		}
		else if(ammo > 0) {
			paddle.setFill(Color.MIDNIGHTBLUE);
		}
		else paddle.setFill(Color.GREEN);
	}
	
	/** deactivatePowerUp sets the paddle back to its default status after losing its power-up.
	 * It also resets the powerUpClock and ammo.*/
	private void deactivatePowerUp() {
		powerUpClock = 0;
		powerUpActive = false;
		if(paddle.getWidth() > 65) {
			resetPaddleLength();
		}
		stickyPaddle = false;
		ammo = 0;
		if(lazer != null) lazer.setFill(Color.TRANSPARENT);
		paddle.setFill(Color.GREEN);
	}

	/** updateBallPosition uses an elapsed time to update the position of the ball
	 * by adding to its previous coordinates.
	 * @param elapsedTime		time since last call to step*/
	private void updateBallPosition(double elapsedTime) {
		ball.setCenterX(ball.getCenterX() + ballXSpeed * elapsedTime);
		ball.setCenterY(ball.getCenterY() + ballYSpeed * elapsedTime);
	}

	/** advanceLevel moves the game to the next level after all blocks are destroyed.*/
	private void advanceLevel() {
		stopGameLoop();
		levelNum++;
		setLevel(gameStage, 450, 400);
		startGameLoop();
	}

	/** removeBlocksFromGame destroys appropriate blocks by iterating through
	 * the BlockManager's clean up list. Destroying blocks includes removing them
	 * from the game scene as well as from the BlockManager. Method also updates 
	 * consecBlocks so that a bonus life may be added when necessary.*/
	private void removeBlocksFromGame() {
		for(Block x : blockManager.getCleanUp()) {
			consecBlocks++;
			Rectangle xRect = x.getRectangle();
			root.getChildren().remove(xRect);
			
		}
		blockManager.removeBlocks();
	}
	
	/** paddleBounce checks for a ball-paddle collision and updates the ball speed
	 * depending on where on the paddle the ball collides.*/
	private boolean paddleBounce() {
		if(ball.getCenterY() + ball.getRadius() >= paddle.getY() &&
				ball.getCenterY() - ball.getRadius() <= paddle.getY() + paddle.getHeight() &&
				ball.getCenterX() - ball.getRadius() <= paddle.getX() + paddle.getWidth() &&
				ball.getCenterX() + ball.getRadius() >= paddle.getX()) {
			consecBlocks = 0;
			if(stickyPaddle) {
				ballYSpeed = 0;
				ballXSpeed = 0;
			}
			ballYSpeed *= -1;
			if(ball.getCenterX() > (paddle.getX() + (.75*paddle.getWidth()))) {
				ballXSpeed *= 1.2;
				ballYSpeed *= 1.2;
				ballXSpeed = Math.abs(ballXSpeed);
				return true;
			}
			else if(ball.getCenterX() < (paddle.getX() + (.25*paddle.getWidth()))) {
				ballXSpeed *= 1.1;
				ballYSpeed *= 1.1;
				ballXSpeed = -1*Math.abs(ballXSpeed);
				return true;
			}
			ballXSpeed *= .95;
			ballYSpeed *= .95;
			return true;
		} 
		return false;
	}
	
	/** blockBounce checks for a ball-block collision and updates the ball speed accordingly.*/
	private void blockBounce() {
		for(Block block : blockManager.getCollisions()) {
			if(!block.getLazerCollision()) {
				if(block.speedToChange(ball).equals("y")) {
					ballYSpeed *= -1;
				}  
				else ballXSpeed *= -1;
				if(block.getType().equals("Speed")) {
					ballYSpeed *= 1.2;
					ballXSpeed *= 1.2;
				}
			}
		}
	}
	
	/** checkForLazerCollision checks for a lazer-block collision.
	 * @return		true if lazer-block collision exists
	 * 				false otherwise */
	private boolean checkForLazerCollision() {
		for(Block block : blockManager.getCollisions()) {
			if(block.lazerCollide(lazer)) {
				return true;
			}
		}
		return false;
	}
	
	/** deliverPowerUp checks the power-up status of the paddle and delivers the
	 * appropriate power-up depending on the power-up held by the hit block.*/
	private void deliverPowerUp() {
		if(!blockManager.getCollisions().isEmpty() && !powerUpActive) {
			if(blockManager.getCollisions().get(0).getPowerUp() == 0) {
				paddle.setWidth(paddle.getWidth()*1.5);
				powerUpClock = 0;
				powerUpActive = true;
			}
			else if(blockManager.getCollisions().get(0).getPowerUp() == 1) {
				stickyPaddle = true;
				powerUpClock = 0;
				powerUpActive = true;
			}
			else if(blockManager.getCollisions().get(0).getPowerUp() == 2) {
				ammo = 5;
				powerUpClock = 0;
				powerUpActive = true;
			}
		}
	}
	
	/** ceilingAndWallBounce checks for ball-ceiling and ball-wall collisions
	 * and updates the ball speed accordingly.*/
	private void ceilingAndWallBounce() {
		if(ball.getCenterY() - ball.getRadius() < 0) {
			ballYSpeed *= -1;
			return;
		}
		if(ball.getCenterX() - ball.getRadius() <= 0 ||
				ball.getCenterX() + ball.getRadius() >= gameSurface.getWidth()) {
			ballXSpeed *= -1;
			return;
		}
	}
	
	/** floorBounce decrements number of lives by one if the ball hits the bottom of the window.*/
	private void floorBounce() {
		if(ball.getCenterY() + ball.getRadius() >= gameSurface.getHeight()) {
			decrementLives();
		}
	}
	
	/** decrementLives subtracts one life, updates the lifeCount display, deactivates any active
	 * power-ups, and resets the ball and paddle positions.*/
	private void decrementLives() {
		root.getChildren().remove(lifeCount);
		lives--;
		deactivatePowerUp();
		resetBall();
		resetPaddlePosition();
		lifeCount = new Text(390, 390, "Lives: " + lives);
		root.getChildren().add(lifeCount);
	}
	
	/** incrementLives adds one life and updates the lifeCount display.*/
	private void incrementLives() {
		root.getChildren().remove(lifeCount);
		lives++;
		lifeCount = new Text(390, 390, "Lives: " + lives);
		root.getChildren().add(lifeCount);
	}
	
	/** paddleMove uses a KeyCode input to update the paddle position. Method also 
	 * updates ball position when the ball is caught on a sticky paddle, and shoots
	 * lazers when necessary.
	 * @param code		User keyboard input*/
	private void paddleMove(KeyCode code) {
		if(code == KeyCode.RIGHT && paddle.getX() + paddle.getWidth() <= gameSurface.getWidth()) {
			paddle.setX(paddle.getX() + KEY_INPUT_SPEED);
			if(stickyPaddle && paddleBounce()) {
				ball.setCenterX(ball.getCenterX() + KEY_INPUT_SPEED);
			}
		}
		else if (code == KeyCode.LEFT && paddle.getX() >= 0) {
            paddle.setX(paddle.getX() - KEY_INPUT_SPEED);
            if(stickyPaddle && paddleBounce()) {
				ball.setCenterX(ball.getCenterX() - KEY_INPUT_SPEED);
			}
        }
		if(stickyPaddle) {
			releaseBall(code);
		}
		if(code == KeyCode.W && ammo > 0) {
			lazer = makeLazer();
		}
	}
	
	/** makeLazer creates and positions the lazer to be shot by the lazer paddle.*/
	private Rectangle makeLazer() {
		Rectangle newlazer = new Rectangle(paddle.getX() + .5*paddle.getWidth(), paddle.getY(), 3, 20);
		newlazer.setFill(Color.YELLOW);
		lazer = newlazer;
		root.getChildren().add(lazer);
		return newlazer;
	}
	
	/** updateLazerPosition moves the lazer by adding a factor of elapsedTime to 
	 * its previous position. Method also checks if the lazer has reached the ceiling of the window
	 * or hit a block and resets the position of the lazer if it has.
	 * @param elapsedTime		time since last call to step*/
	private void updateLazerPosition(double elapsedTime) {
		if(lazer != null && ammo > 0) {
			if(lazer.getY() < 0 || checkForLazerCollision()) {
				lazer.setX(paddle.getX() + .5*paddle.getWidth());
				lazer.setY(paddle.getY() - lazer.getHeight());
				ammo--;
			}
			lazer.setY(lazer.getY() - 5 * DEFAULT_BALLYSPEED * elapsedTime);
		}
	}

	/** releaseBall is called when the ball is caught on a sticky paddle. Player can
	 * release the ball in the northwest direction by pressing the 'Q' key and in the northeast
	 * direction by pressing the 'E' key.
	 * @param code		User keyboard input*/
	private void releaseBall(KeyCode code) {
		if(code == KeyCode.E) {
			ballYSpeed = -1*DEFAULT_BALLYSPEED;
			ball.setCenterY(ball.getCenterY() + ballYSpeed*secondDelay);
			ballXSpeed = DEFAULT_BALLXSPEED;
			ball.setCenterX(ball.getCenterX() + ballXSpeed*secondDelay);
		}
		else if(code == KeyCode.Q) {
			ballYSpeed = -1*DEFAULT_BALLYSPEED;
			ball.setCenterY(ball.getCenterY() + ballYSpeed*secondDelay);
			ballXSpeed = -1*DEFAULT_BALLXSPEED;
			ball.setCenterX(ball.getCenterX() + ballXSpeed*secondDelay);
		}
	}
	
	/** setLevel initializes the level scene. It initializes the game Stage and Group, sets and displays
	 * lives, makes the paddle and ball, initializes the BlockManager, declares and initializes the game 
	 * Scene, and deactivates power-ups.
	 * @param myStage		level stage
	 * @param width			width of the level scene
	 * @param height		height of the level scene*/
	public void setLevel(Stage myStage, double width, double height) {
		gameStage = myStage;
		root = new Group();
		setAndDisplayLives();
		makePaddle();
		makeBall();
		resetBall();
		blockManager = new BlockManager(ball);
		chooseLevel(levelNum);
		Scene level = new Scene(root, width, height);
		level.setFill(Color.LIGHTBLUE);
		gameSurface = level;
		deactivatePowerUp();
		lazer = null;
		myStage.setScene(level);
		myStage.setTitle("Level " + levelNum);
		myStage.show();
	}
	
	/** setAndDisplayLives give five lives and displays the life count.*/
	private void setAndDisplayLives() {
		lives = 5;
		lifeCount = new Text(390, 390, "Lives: " + lives);
		root.getChildren().add(lifeCount);
	}
	
	/** makePaddle makes the paddle (a Rectangle) and sets its color and initial position.*/
	private void makePaddle() {
		paddle = new Rectangle(65, 10);
		setPaddleColor();
		resetPaddlePosition();
		root.getChildren().add(paddle);
	}
	
	/**resetPaddlePosition places the paddle in its default position */
	private void resetPaddlePosition() {
		paddle.setX(175);
		paddle.setY(350);
	}
	
	/**resetPaddleLength sets the width of the paddle Rectangle to its default width.
	 * It is generally called to deactivate the long paddle power-up.*/
	private void resetPaddleLength() {
		paddle.setWidth(65);
	}
	
	/**makeBall initializes the ball as a circle.*/
	private void makeBall() {
		ball = new Circle(200, 300, 4);
		root.getChildren().add(ball);
	}
	
	/** resetBall places the ball at its default position and sets its speed to zero.*/
	private void resetBall() {
		ball.setCenterX(200);
		ball.setCenterY(300);
		ballXSpeed = 0;
		ballYSpeed = 0;
	}
	
	/** input reads all user keyboard input. It then calls helper methods to start the ball,
	 * move the paddle, pause the game, leave the home screen, leave the game over screen,
	 * and activate cheat codes.
	 * @param code		user keyboard input*/
	private void input(KeyCode code) {
		if(!homeScreenActive) {
			startBall(code);
			paddleMove(code);
			pause(code);
		}
		leaveHomeScreen(code);
		leaveGameOverScreen(code);
		cheatCodes(code);
	}
	
	/** leaveGameOverScreen restarts the game at level one with the home screen displayed
	 * upon the player pressing the space bar.
	 * @param code		user keyboard input*/
	private void leaveGameOverScreen(KeyCode code) {
		if(gameOver && code == KeyCode.SPACE) {
			stopGameLoop();
			levelNum = 1;
			setLevel(gameStage, 450, 400);
			gameOver = false;
			startGameLoop();
		}
	}
	
	/** cheatCodes reads user keyboard input and activates the appropriate
	 * cheat code. The user can press the 'enter' key to advance to the next level,
	 * the 'D' key to deactivate an active power-up, the 'A' key to add a life, the
	 * 'tab' key to increase the ball speed, and the 'shift' key to decrease the ball 
	 * speed.
	 * @param code		user keyboard input*/
	private void cheatCodes(KeyCode code) {
		if(code == KeyCode.ENTER && levelNum < 3) {
			advanceLevel();
		}
		if(code == KeyCode.D) {
			deactivatePowerUp();
		}
		if(code == KeyCode.A) {
			incrementLives();
		}
		if(code == KeyCode.TAB) {
			ballXSpeed *= 1.5;
			ballYSpeed *= 1.5;
		}
		if(code == KeyCode.SHIFT) {
			ballXSpeed /= 1.5;
			ballYSpeed /= 1.5;
		}
	}

	/** startBall sets the ball into movement when the user presses the right or left arrow key at the 
	 * beginning of a level.
	 * @param code		user keyboard input*/
	private void startBall(KeyCode code) {
		if((code == KeyCode.RIGHT || code == KeyCode.LEFT) && ballXSpeed == 0 && ballYSpeed == 0) {
			ballXSpeed = DEFAULT_BALLXSPEED;
			ballYSpeed = DEFAULT_BALLYSPEED;
		}
	}
	
	/** pause pauses or unpauses the game by creating and displaying or removing the pause screen upon
	 * the user pressing the 'P' key. Method also adjusts the 'paused' field so that the game stops and resumes
	 * appropriately.
	 * @param code		user keyboard input*/
	private void pause(KeyCode code) {
		if(code == KeyCode.P) {
			if(!paused) {
				paused = true;
				pauseScreen = new SplashScreen(new Rectangle(450, 400), Color.PINK, new Text("PAUSED"));
				pauseScreen.addText(165, 100, "Press 'P' to resume");
				root.getChildren().addAll(pauseScreen.bundle());
			}
			else {
				paused = false;
				root.getChildren().removeAll(pauseScreen.bundle());
			}
		}
	}
	
	/**leaveHomeScreen removes the home screen upon the player pressing the space bar.
	 * @param code		user keyboard input */
	private void leaveHomeScreen(KeyCode code) {
		if(code == KeyCode.SPACE) {
			if(homeScreenActive) {
				homeScreenActive = false;
				root.getChildren().removeAll(homeScreen.bundle());
			}
		}
	}
	
	/** chooseLevel, a helper method for setLevel, choosing the appropriate level and sets it.
	 * @param levelNum		number of the level to set*/
	private void chooseLevel(int levelNum) {
		if(levelNum == 1) {
			setLevelOne(root);
		}
		if(levelNum == 2) {
			setLevelTwo(root);
		}
		if(levelNum == 3) {
			setLevelThree(root);
		}
	}
	
	/** setLevelOne places the blocks in the appropriate locations and creates and
	 * displays the home screen.
	 * @param root		Group to which to add the blocks*/
	public void setLevelOne(Group root) {
		setBlockRow(root, 50, 0, 4, 50);
		setBlockRow(root, 0, 30, 5, 50);
		setBlockRow(root, 50, 60, 4, 50);
		setBlockRow(root, 0, 90, 5, 50);
		setBlockRow(root, 50, 120, 4, 50);
		initializeHomeScreen();
	}
	
	/** initializeHomeScreen creates the home screen as a SplashScreen and displayes it.*/
	private void initializeHomeScreen() {
		homeScreenActive = true;
		homeScreen = new SplashScreen(new Rectangle(450, 400), Color.PINK, new Text("HOME"));
		homeScreen.addText(5, 100, "Welcome to Block Breaker!");
		homeScreen.addText(5, 115, "Use the left and right arrow keys to move the paddle.");
		homeScreen.addText(5, 130, "Gray blocks are destroyed on 1st collision.");
		homeScreen.addText(5, 145, "Red blocks turn orange on 1st collision and are destroyed on 2nd.");
		homeScreen.addText(5, 160, "White blocks turn gray and travel to a new location on 1st collision,");
		homeScreen.addText(15, 175, "and they are destroyed on 2nd.");
		homeScreen.addText(5, 190, "Green blocks speed up the ball are destroyed on 1st collision.");
		homeScreen.addText(5, 205, "Random blocks deliver one of three different powerups on collision.");
		homeScreen.addText(5, 220, "Powerups include the long paddle, sticky paddle, and lazer paddle.");
		homeScreen.addText(5, 235, "Release the ball from the sticky paddle using the 'Q' and 'E' keys.");
		homeScreen.addText(5, 250, "Shoot lazers using the 'W' key.");
		homeScreen.addText(5, 265, "Destroy five blocks in a single paddle hit and you'll earn a bonus life!");
		homeScreen.addText(5, 280, "Press space to go to Level 1, then press the right arrow key to start!");
		root.getChildren().addAll(homeScreen.bundle());
	}
	
	/** setLevelTwo places the blocks in the appropriate locations.
	 * @param root		Group to which to add the blocks*/
	public void setLevelTwo(Group root) {
		for(int x = 0; x < NUM_ROWS; x++) {
			setBlockRow(root, 0, x*30, 5, 50);
		}
	}
	
	/** setLevelThree places the blocks in the appropriate locations.
	 * @param root		Group to which to add the blocks*/
	public void setLevelThree(Group root) {
		for(int x = 0; x < NUM_ROWS; x++) {
			setBlockRow(root, 0, x*30, 9, 0);
		}
	}
	
	/** setBlockRow creates and places the blocks in a single row. It uses the mod operation to determine the type
	 * of block being places in a given location. The method updates the BlockManager accordingly.
	 * @param root					Group to which to add the blocks
	 * @param blockXCoordinate		x coordinate at which to place the first block
	 * @param blockYCoordinate		y coordinate at which to place all blocks in the row
	 * @param blockNum				number of blocks to place in the row
	 * @param gap					amount of horizontal space to leave between consecutively placed blocks*/
	private void setBlockRow(Group root, int blockXCoordinate, int blockYCoordinate, int blockNum, int gap) {
		for(int x = 0; x < blockNum; x++) {
			Rectangle block = new Rectangle(Block.WIDTH, Block.HEIGHT, Color.GRAY);
			block.setX(blockXCoordinate);
			block.setY(blockYCoordinate);
			block.setStrokeWidth(5);
			block.setStroke(Color.BLACK);
			root.getChildren().add(block);
			Block BLOCK;
			if(x % 5 == 0) {
				BLOCK = new Block(block, "Speed");
			}
			else if(x % 3 == 0) {
				BLOCK = new Block(block, "Traveling");
			}
			else if(x % 2 == 0) {
				BLOCK = new Block(block, "Two Hit");
			}
			else {
				BLOCK = new Block(block, "One Hit");
			}
			blockManager.addBlock(BLOCK);
			blockXCoordinate += gap+Block.WIDTH;
		}
	}

}