package game_anp36;

import javax.xml.soap.Node;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GameDriver {
	
	private Scene gameSurface;
	private int framesPerSecond;
	private int millisecondDelay;
	private double secondDelay;
	private String gameTitle;
	private Circle ball = new Circle();
	private int ballXSpeed = 15;
	private int ballYSpeed = 50;
	Rectangle paddle = new Rectangle(65, 10, Color.DEEPPINK);
	private Group root;
	private BlockManager blockManager;
	
	public GameDriver(int fps, String title) {
		framesPerSecond = fps;
		millisecondDelay = 1000 / fps;
		secondDelay = 1.0 / fps;
		gameTitle = title;
		blockManager = new BlockManager(ball);
	}
	
	protected final void startGameLoop() {
		
		KeyFrame frame = new KeyFrame(Duration.millis(millisecondDelay),
                e -> step(secondDelay));
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}
	
	private void step(double elapsedTime) {
		paddleBounce(elapsedTime);
		blockManager.addCollisions();
		System.out.println("Root Size: " + root.getChildren().size());
		System.out.println("Step Clean Up: " + blockManager.cleanUp());
		for(Block x : blockManager.cleanUp()) {
			System.out.println("Contains block: " + root.getChildren().contains(x));
			root.getChildren().remove(x);
		}
		System.out.println("Root Size: " + root.getChildren().size());
	}
	
	private void paddleBounce(double elapsedTime) {
		ball.setCenterX(ball.getCenterX() + ballXSpeed * elapsedTime);
		ball.setCenterY(ball.getCenterY() + ballYSpeed * elapsedTime);
		if(ball.getCenterY() > paddle.getY() &&
				ball.getCenterX() < paddle.getX() + paddle.getWidth() &&
				ball.getCenterX() > paddle.getX()) {
			ballYSpeed *= -1;
		}
	}
	
	public Scene setLevel(int levelNum, double width, double height) {
		root = new Group();
		Scene level = new Scene(root, width, height);
		paddle.setX(175);
		paddle.setY(350);
		root.getChildren().add(paddle);
		ball.setRadius(4);
		ball.setCenterX(200);
		ball.setCenterY(300);
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
			blockManager.addBlock(new Block(block));
			blockXCoordinate += gap+50;
		}
	}
	
	
	
}
