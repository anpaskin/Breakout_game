package game_anp36;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SplashScreen {

	private Rectangle screen;
	
	/* Make bundle method that returns list of all elements that can be added (addAll) to root */
	
	
	public SplashScreen(Rectangle body) {
		screen = body;
	}
	
	public SplashScreen(Rectangle body, Color color) {
		this(body);
		setColor(color);
	}
	
	public Rectangle getScreen() {
		return screen;
	}
	
	public void setColor(Color color) {
		screen.setFill(color);
	}
}
