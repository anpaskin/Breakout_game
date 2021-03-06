package game_anp36;

import javafx.application.Application;
import javafx.stage.Stage;

/** MainGame starts the game by initializing a new GameDriver and calling its
 * setLevel and startGameLoop methods.
 * 
 * @author Aaron Paskin
 *
 */
public class MainGame extends Application {
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage mainStage) {
		GameDriver gameDriver = new GameDriver(60);
		gameDriver.setLevel(mainStage, 450, 400);
		gameDriver.startGameLoop();
	}
	

}
