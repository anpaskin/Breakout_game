package game_anp36;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainGame extends Application {
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage mainStage) {
		GameDriver gameDriver = new GameDriver(60, "Breakout");
		Scene curScene = gameDriver.setLevel(4, 450, 400);
		mainStage.setScene(curScene);
		mainStage.setTitle("Test");
		mainStage.show();
	}
	

}
