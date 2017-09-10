package game_anp36;

import java.io.FileInputStream;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class JavaFXPractice extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("My First JavaFX App");
		
		FileInputStream input = new FileInputStream("/Users/aaronpaskin/Documents/workspace/test/forest.jpeg");
		Image forest = new Image(input);
		ImageView forestView = new ImageView(forest);
		
		HBox forestDisplay = new HBox(forestView);
		
		//Label label = new Label("Hello World, JavaFX !");
		Scene scene = new Scene(forestDisplay, 400, 200);
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		System.out.println((int)(Math.random()*10));
		//Application.launch(args);
		
		
		
		int max = 0;
		for(int x = 0; x < arraySize; x++) {
			if(array[x] > max) {
				max = array[x];
			}
		}
		return max;
		
		
		
		
		
		
		
	}
	
}