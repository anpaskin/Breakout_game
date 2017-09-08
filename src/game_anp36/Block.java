package game_anp36;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Block {

	private Rectangle BLOCK;
	private boolean isDestroyed;
	private int collisions;
	private String type;
	private double powerUp;
	
	public Block(Rectangle block) {
		BLOCK = block;
		collisions = 0;
		type = "One Hit";
		Random rand = new Random();
		//powerUp = rand.nextInt(10);
		powerUp = 0;
	}
	
	//FIGURE OUT HOW TO CALL OTHER CONSTRUCTOR WITHIN THIS ONE
	public Block(Rectangle block, String blockType) {
		this(block);
		type = blockType;
		if(type.equals("Two Hit")) {
			BLOCK.setFill(Color.RED);
		}
	}
	
	public boolean ballCollide(Circle ball) {
		if(ball.getCenterX() + ball.getRadius() >= BLOCK.getX() &&
				ball.getCenterX() - ball.getRadius() <= BLOCK.getX() + BLOCK.getWidth() &&
				ball.getCenterY() + ball.getRadius() >= BLOCK.getY() &&
				ball.getCenterY() - ball.getRadius() <= BLOCK.getY() + BLOCK.getHeight()) {
			System.out.println("Ball collision");
			collisions++;
			checkIfDestroyed();
			return true;
		}
		else return false;
	}
	
	public boolean checkIfDestroyed() {
		if(type.equals("One Hit") && collisions >= 1) {
			isDestroyed = true;
		}
		else if(type.equals("Two Hit")) {
			if(collisions == 1) {
				BLOCK.setFill(Color.ORANGE);
			}
			else if(collisions >= 2) {
				isDestroyed = true;
			}
		}
		return isDestroyed;
	}
	
	public Rectangle getRectangle() {
		return BLOCK;
	}
	
	public double getPowerUp() {
		return powerUp;
	}
	
	public String speedToChange(Circle ball) {
		double r = ball.getRadius();
		double cX = ball.getCenterX();
		double cY = ball.getCenterY();
		double bX = BLOCK.getX();
		double bY = BLOCK.getY();
		double w = BLOCK.getWidth();
		double h = BLOCK.getHeight();
		if(
			((cX + r) - bX <= (bY + h) - (cY + r) &&
			 (cX + r) - bX <= (cY - r) - bY) ||
			((bX + w) - (cX - r) <= (bY + h) - (cY + r) &&
			((bX + w) - (cX - r) <= (cY - r) - bY))
				) {
				return "x";
		}
		else if(
					((cY - r) - bY <= (cX - r) - bX &&
					 (cY - r) - bY <= (bX + w) - (cX + r)) ||
					((bY + h) - (cY + r) <= (cX - r) - bX &&
					 (bY + h) - (cY + r) <= (bX + w) - (cX + r))
				) {
			return "y";
		}
		else return "x and y";
	}
	
}
