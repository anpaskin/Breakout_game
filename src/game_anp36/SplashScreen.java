package game_anp36;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class SplashScreen {

	private Rectangle screen;
	private Text title;
	private ArrayList<Text> allText;
	
	public SplashScreen(Rectangle body, Color color, Text word) {
		screen = body;
		setColor(color);
		title = word;
		formatTitle();
		allText = new ArrayList<Text>();
		allText.add(title);
	}
	
	private void formatTitle() {
		title.setX(200);
		title.setY(50);
		title.setScaleX(5);
		title.setScaleY(5);
	}
	
	public Rectangle getScreen() {
		return screen;
	}
	
	public void setColor(Color color) {
		screen.setFill(color);
	}
	
	public ArrayList<Node> bundle() {
		ArrayList<Node> bundle = new ArrayList<Node>();
		bundle.add(screen);
		bundle.addAll(allText);
		return bundle;
	}
	
	public void addText(int x, int y, String words) {
		allText.add(new Text(x, y, words));
	}
	
	
}
