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
import javafx.util.Duration;

//how do you get it to move smoothly like that, the paddle

public class GameDriver {
	
	private Scene gameSurface;
	private int framesPerSecond;
	private int millisecondDelay;
	private double secondDelay;
	private String gameTitle;
	private Circle ball;
	private int lives;
	private Text lifeCount;
	private int ballXSpeed = 100;
	private int ballYSpeed = 75;
	Rectangle paddle = new Rectangle(65, 10, Color.DEEPPINK);
	private Group root;
	private BlockManager blockManager;
	public static final int KEY_INPUT_SPEED = 20;
	
	public GameDriver(int fps, String title) {
		framesPerSecond = fps;
		millisecondDelay = 1000 / fps;
		secondDelay = 1.0 / fps;
		gameTitle = title;
	}
	
	protected final void startGameLoop() {
		KeyFrame frame = new KeyFrame(Duration.millis(millisecondDelay),
                e -> step(secondDelay));
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
		gameSurface.setOnKeyPressed(e -> paddleMove(e.getCode()));
	}
	
	private void step(double elapsedTime) {
		System.out.println("Lives: " + lives);
		ball.setCenterX(ball.getCenterX() + ballXSpeed * elapsedTime);
		ball.setCenterY(ball.getCenterY() + ballYSpeed * elapsedTime);
		paddleBounce();
		ceilingAndWallBounce();
		floorBounce();
		blockManager.addCollisions();  
		blockBounce();
		blockManager.cleanUp();
		System.out.println("Root Size: " + root.getChildren().size());
		System.out.println("Step Clean Up: " + blockManager.getCleanUp());
		for(Block x : blockManager.getCleanUp()) {
			Rectangle xRect = x.getRectangle();
			System.out.println("Contains block: " + root.getChildren().contains(xRect));
			root.getChildren().remove(xRect);
		}
		blockManager.removeBlocks();
		System.out.println("Root Size: " + root.getChildren().size());
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
				//}
				ballXSpeed = Math.abs(ballXSpeed);
				return;
			}
			else if(ball.getCenterX() < (paddle.getX() + (.25*paddle.getWidth()))) {
				//if(ball.getCenterX() < (paddle.getX() + (.1*paddle.getWidth()))) {
					ballXSpeed *= 1.2;
				//}
				ballXSpeed = -1*Math.abs(ballXSpeed);
				return;
			}
			ballXSpeed *= .95;
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
			
			/*if(block.leftCollision(ball) || block.rightCollision(ball)) {
				ballXSpeed *= -1;
			}
			if(block.topCollision(ball) || block.bottomCollision(ball)) {
				ballYSpeed *= -1;
			}*/
			
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
	
	public Scene setLevel(int levelNum, double width, double height) {
		root = new Group();
		Scene level = new Scene(root, width, height);
		lives = 5;
		lifeCount = new Text(390, 390, "Lives: " + lives);
		paddle.setX(175);
		paddle.setY(350);
		ball = new Circle(200, 300, 4);
		blockManager = new BlockManager(ball);
		root.getChildren().add(lifeCount);
		root.getChildren().add(paddle);
		root.getChildren().add(ball);
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
		gameSurface = level;
		return level;
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
