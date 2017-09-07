package game_anp36;

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

//how do you get it to move smoothly like that, the paddle

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
	public static final int DEFAULT_BALLXSPEED = 100;
	public static int DEFAULT_BALLYSPEED = 75;
	private Rectangle paddle;
	private Group root;
	private BlockManager blockManager;
	public static final int KEY_INPUT_SPEED = 20;
	
	public GameDriver(int fps, String title) {
		framesPerSecond = fps;
		millisecondDelay = 1000 / fps;
		secondDelay = 1.0 / fps;
		gameTitle = title;
		levelNum = 2;
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
		updateBallPosition(elapsedTime);
		paddleBounce();
		cornerBounce();
		ceilingAndWallBounce();
		floorBounce();
		blockManager.addCollisions();  
		blockBounce();
		blockManager.cleanUp();
		System.out.println("Root Size: " + root.getChildren().size());
		System.out.println("Ball X Speed: " + ballXSpeed);
		System.out.println("Ball Y Speed: " + ballYSpeed);
		removeBlocksFromGame();
	}

	private void updateBallPosition(double elapsedTime) {
		if(ballXSpeed != 0 && ballYSpeed != 0) {
			ball.setCenterX(ball.getCenterX() + ballXSpeed * elapsedTime);
			ball.setCenterY(ball.getCenterY() + ballYSpeed * elapsedTime);
		}
	}

	private void advanceLevel() {
		stopGameLoop();
		levelNum++;
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
	
	private void paddleBounce() {
		if(ball.getCenterY() + ball.getRadius() >= paddle.getY() &&
				ball.getCenterY() - ball.getRadius() <= paddle.getY() + paddle.getHeight() &&
				ball.getCenterX() - ball.getRadius() <= paddle.getX() + paddle.getWidth() &&
				ball.getCenterX() + ball.getRadius() >= paddle.getX()) {
			ballYSpeed *= -1;
			if(ball.getCenterX() > (paddle.getX() + (.75*paddle.getWidth()))) {
				//if(ball.getCenterX() > (paddle.getX() + (.9*paddle.getWidth()))) {
					ballXSpeed *= 1.2;
					ballYSpeed *= 1.2;
				//}
				ballXSpeed = Math.abs(ballXSpeed);
				return;
			}
			else if(ball.getCenterX() < (paddle.getX() + (.25*paddle.getWidth()))) {
				//if(ball.getCenterX() < (paddle.getX() + (.1*paddle.getWidth()))) {
					ballXSpeed *= 1.1;
					ballYSpeed *= 1.1;
				//}
				ballXSpeed = -1*Math.abs(ballXSpeed);
				return;
			}
			ballXSpeed *= .95;
			ballYSpeed *= .95;
		} 
	}
	
	private void blockBounce() {
		for(Block block : blockManager.getCollisions()) {
			if(block.speedToChange(ball).equals("y")) {
				ballYSpeed *= -1;
			}  
			else if(block.speedToChange(ball).equals("x")) {
				ballXSpeed *= -1;
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
	
	private void cornerBounce() {
		if((ball.getCenterX() == 1 && ball.getCenterY() == 1) ||
				(ball.getCenterX() == 449 && ball.getCenterY() == 1) ||
				(ball.getCenterX() == 449 && ball.getCenterY() == 399) ||
				(ball.getCenterX() == 1 && ball.getCenterY() == 399)) {
			ballXSpeed *= -1;
			ballYSpeed *= -1;
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
		resetBall();
		resetPaddle();
		lifeCount = new Text(390, 390, "Lives: " + lives);
		root.getChildren().add(lifeCount);
	}
	
	private void paddleMove(KeyCode code) {
		if(code == KeyCode.RIGHT && paddle.getX() + paddle.getWidth() <= gameSurface.getWidth()) {
			paddle.setX(paddle.getX() + KEY_INPUT_SPEED);
		}
		else if (code == KeyCode.LEFT && paddle.getX() >= 0) {
            paddle.setX(paddle.getX() - KEY_INPUT_SPEED);
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
		paddle = new Rectangle(65, 10, Color.DEEPPINK);
		resetPaddle();
		root.getChildren().add(paddle);
	}
	
	private void resetPaddle() {
		paddle.setX(175);
		paddle.setY(350);
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
		setBlockRow(root, 0, 0, 9, 0);
		setBlockRow(root, 0, 30, 5, 50);
		setBlockRow(root, 50, 60, 4, 50);
		setBlockRow(root, 0, 90, 5, 50);
		setBlockRow(root, 50, 120, 4, 50);
	}
	
	public void setLevelTwo(Group root) {
		for(int x = 0; x < 5; x++) {
			setBlockRow(root, 0, x*30, 5, 50);
		}
	}
	
	public void setLevelThree(Group root) {
		for(int x = 0; x < 5; x++) {
			setBlockRow(root, 0, x*30, 9, 0);
		}
	}
	
	public void setLevelFour(Group root) {
		for(int x = 0; x < 5; x++) {
			setBlockRow(root, 0, x*30, 9, 0);
		}
	}
	
	private void setBlockRow(Group root, int blockXCoordinate, int blockYCoordinate, int blockNum, int gap) {
		for(int x = 0; x < blockNum; x++) {
			Rectangle block = new Rectangle(50, 30, Color.GRAY);
			block.setX(blockXCoordinate);
			block.setY(blockYCoordinate);
			block.setStrokeWidth(5);
			block.setStroke(Color.BLACK);
			root.getChildren().add(block);
			Block BLOCK = new Block(block);
			blockManager.addBlock(BLOCK);
			blockXCoordinate += gap+50;
		}
	}
	
	
	
}
