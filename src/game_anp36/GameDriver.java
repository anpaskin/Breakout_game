package game_anp36;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.xml.soap.Node;

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


public class GameDriver {
	
	private Stage gameStage;
	private Scene gameSurface;
	private int framesPerSecond;
	private int millisecondDelay;
	private double secondDelay;
	private Timeline animation;
	private String gameTitle;
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
	public static final int KEY_INPUT_SPEED = 20;
	public static final int NUM_ROWS = 5;
	public static final int NUM_COLS = 9;
	
	public GameDriver(int fps, String title) {
		framesPerSecond = fps;
		millisecondDelay = 1000 / fps;
		secondDelay = 1.0 / fps;
		gameTitle = title;
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
		gameSurface.setOnKeyPressed(e -> startBall(e.getCode()));
	}
	
	protected final void stopGameLoop() {
		animation.stop();
	}
	
	private void step(double elapsedTime) {
		if(blockManager.getBlockList().size() == 0) {
			advanceLevel();
		}
		double ballYBefore = ball.getCenterY();
		updateBallPosition(elapsedTime);
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
		updateLazerPosition(elapsedTime);
		blockBounce();
		deliverPowerUp();
		blockManager.cleanUp();
		removeBlocksFromGame();
	}

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

	private void updateBallPosition(double elapsedTime) {
		ball.setCenterX(ball.getCenterX() + ballXSpeed * elapsedTime);
		ball.setCenterY(ball.getCenterY() + ballYSpeed * elapsedTime);
	}

	private void advanceLevel() {
		stopGameLoop();
		levelNum++;
		deactivatePowerUp();
		lazer = null;
		setLevel(gameStage, 450, 400);
		startGameLoop();
	}

	private void removeBlocksFromGame() {
		for(Block x : blockManager.getCleanUp()) {
			Rectangle xRect = x.getRectangle();
			root.getChildren().remove(xRect);
		}
		blockManager.removeBlocks();
	}
	
	private boolean paddleBounce() {
		if(ball.getCenterY() + ball.getRadius() >= paddle.getY() &&
				ball.getCenterY() - ball.getRadius() <= paddle.getY() + paddle.getHeight() &&
				ball.getCenterX() - ball.getRadius() <= paddle.getX() + paddle.getWidth() &&
				ball.getCenterX() + ball.getRadius() >= paddle.getX()) {
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
	
	private void blockBounce() {
		for(Block block : blockManager.getCollisions()) {
			if(!block.getLazerCollision()) {
				System.out.println("Collisions: " + block.getCollisions());
				if(block.speedToChange(ball).equals("y")) {
					ballYSpeed *= -1;
				}  
				else ballXSpeed *= -1;
			}
		}
	}
	
	private boolean checkForLazerCollision() {
		for(Block block : blockManager.getCollisions()) {
			if(block.lazerCollide(lazer)) {
				System.out.println("Lazer Collision");
				return true;
			}
		}
		return false;
	}
	
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
	
	private void floorBounce() {
		if(ball.getCenterY() + ball.getRadius() >= gameSurface.getHeight()) {
			decrementLives();
			ballYSpeed *= -1;
		}
	}
	
	private void decrementLives() {
		root.getChildren().remove(lifeCount);
		lives--;
		deactivatePowerUp();
		resetBall();
		resetPaddlePosition();
		lifeCount = new Text(390, 390, "Lives: " + lives);
		root.getChildren().add(lifeCount);
	}
	
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
	
	private Rectangle makeLazer() {
		Rectangle newlazer = new Rectangle(paddle.getX() + .5*paddle.getWidth(), paddle.getY(), 3, 20);
		newlazer.setFill(Color.YELLOW);
		lazer = newlazer;
		root.getChildren().add(lazer);
		System.out.println("Added Lazer");
		return newlazer;
	}
	
	private void updateLazerPosition(double elapsedTime) {
		if(lazer != null && ammo > 0) {
			if(lazer.getY() < 0 || checkForLazerCollision()) {
				lazer.setX(paddle.getX() + .5*paddle.getWidth());
				lazer.setY(paddle.getY() - lazer.getHeight());
				ammo--;
			}
			lazer.setY(lazer.getY() - 5*DEFAULT_BALLYSPEED*elapsedTime);
		}
	}

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
		myStage.setScene(level);
		myStage.setTitle("Level " + levelNum);
		myStage.show();
	}
	
	private void setAndDisplayLives() {
		lives = 5;
		lifeCount = new Text(390, 390, "Lives: " + lives);
		root.getChildren().add(lifeCount);
	}
	
	private void makePaddle() {
		paddle = new Rectangle(65, 10);
		setPaddleColor();
		resetPaddlePosition();
		root.getChildren().add(paddle);
	}
	
	private void resetPaddlePosition() {
		paddle.setX(175);
		paddle.setY(350);
	}
	
	private void resetPaddleLength() {
		paddle.setWidth(65);
	}
	
	private void makeBall() {
		ball = new Circle(200, 300, 4);
		root.getChildren().add(ball);
	}
	
	private void resetBall() {
		ball.setCenterX(200);
		ball.setCenterY(300);
		ballXSpeed = 0;
		ballYSpeed = 0;
	}
	
	private void startBall(KeyCode code) {
		if((code == KeyCode.RIGHT || code == KeyCode.LEFT) && ballXSpeed == 0 && ballYSpeed == 0) {
			ballXSpeed = DEFAULT_BALLXSPEED;
			ballYSpeed = DEFAULT_BALLYSPEED;
		}
		paddleMove(code);
		pause(code);
	}
	
	private void pause(KeyCode code) {
		if(code == KeyCode.P) {
			if(!paused) {
				paused = true;
				pauseScreen = new SplashScreen(new Rectangle(450, 400), Color.PINK);
				root.getChildren().add(pauseScreen.getScreen());
			}
			else {
				paused = false;
				root.getChildren().remove(pauseScreen.getScreen());
			}
		}
	}
	
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
		if(levelNum == 4) {
			setLevelFour(root);
		}
	}
	
	public void setLevelOne(Group root) {
		setBlockRow(root, 50, 0, 4, 50);
		setBlockRow(root, 0, 30, 5, 50);
		setBlockRow(root, 50, 60, 4, 50);
		setBlockRow(root, 0, 90, 5, 50);
		setBlockRow(root, 50, 120, 4, 50);
	}
	
	public void setLevelTwo(Group root) {
		for(int x = 0; x < NUM_ROWS; x++) {
			setBlockRow(root, 0, x*30, 5, 50);
		}
	}
	
	public void setLevelThree(Group root) {
		for(int x = 0; x < NUM_ROWS; x++) {
			setBlockRow(root, 0, x*30, 9, 0);
		}
	}
	
	public void setLevelFour(Group root) {
		for(int x = 0; x < NUM_ROWS; x++) {
			setBlockRow(root, 0, x*30, 9, 0);
		}
	}
	
	private void setBlockRow(Group root, int blockXCoordinate, int blockYCoordinate, int blockNum, int gap) {
		for(int x = 0; x < blockNum; x++) {
			Rectangle block = new Rectangle(Block.WIDTH, Block.HEIGHT, Color.GRAY);
			block.setX(blockXCoordinate);
			block.setY(blockYCoordinate);
			block.setStrokeWidth(5);
			block.setStroke(Color.BLACK);
			root.getChildren().add(block);
			Block BLOCK;
			if(x % 3 == 0) {
				BLOCK = new Block(block, "Traveling");
			}
			else if(x % 2 == 0) {
				BLOCK = new Block(block, "Two Hit");
			}
			else {
				BLOCK = new Block(block, "One Hit");
			}
			blockManager.addBlock(BLOCK);
			blockXCoordinate += gap+50;
		}
	}

}