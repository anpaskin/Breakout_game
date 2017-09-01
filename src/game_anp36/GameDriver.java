package game_anp36;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GameDriver {

	private int framesPerSecond;
	private int millisecondDelay;
	private double secondDelay;
	private String gameTitle;
	
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
	}
	
	private void step(double elapsedTime) {
		
	}
	
	public Scene setLevel(double width, double height) {
		Group root = new Group();
		Scene level = new Scene(root, width, height);
		Rectangle paddle = new Rectangle(50, 10, Color.RED);
		root.getChildren().add(paddle);
		return level;
	}
	
	
	
}
