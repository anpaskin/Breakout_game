package game_anp36;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Block {

	private Rectangle BLOCK;
	private boolean isDestroyed;
	private boolean lazerCollision;
	private int collisions;
	private String type;
	private double powerUp;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 30;
	
	public Block(Rectangle block) {
		BLOCK = block;
		lazerCollision = false;
		collisions = 0;
		type = "One Hit";
		Random rand = new Random();
		powerUp = rand.nextInt(10);
	}
	
	public Block(Rectangle block, String blockType) {
		this(block);
		type = blockType;
		if(type.equals("Two Hit")) {
			BLOCK.setFill(Color.RED);
		}
		else if(type.equals("Traveling")) {
			BLOCK.setFill(Color.WHITE);
		}
		else if(type.equals("Speed")) {
			BLOCK.setFill(Color.GREENYELLOW);
		}
	}
	
	public int getCollisions() {
		return collisions;
	}

	public boolean getLazerCollision() {
		return lazerCollision;
	}
	
	public boolean getIsDestroyed() {
		return isDestroyed;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean ballCollide(Circle ball) {
		if(ball.getCenterX() + ball.getRadius() >= BLOCK.getX() &&
				ball.getCenterX() - ball.getRadius() <= BLOCK.getX() + BLOCK.getWidth() &&
				ball.getCenterY() + ball.getRadius() >= BLOCK.getY() &&
				ball.getCenterY() - ball.getRadius() <= BLOCK.getY() + BLOCK.getHeight()) {
			collisions++;
			System.out.println("Collisions: " + collisions);
			checkIfDestroyed();
			return true;
		}
		return false;
	}
	
	public boolean lazerCollide(Rectangle lazer) {
		if(lazer != null &&
				lazer.getY() <= BLOCK.getY() + BLOCK.getHeight() &&
				lazer.getX() + lazer.getWidth() >= BLOCK.getX() &&
				lazer.getX() <= BLOCK.getX() + BLOCK.getWidth()) {
			lazerCollision = true;
			collisions++;
			checkIfDestroyed();
			return true;
		}
		lazerCollision = false;
		return false;
	}
	
	public boolean checkIfDestroyed() {
		if((type.equals("One Hit") || type.equals("Speed")) && collisions >= 1) {
			isDestroyed = true;
		}
		else if(type.equals("Two Hit") || type.equals("Traveling")) {
			if(collisions == 1) {
				if(type.equals("Two Hit")) {
					BLOCK.setFill(Color.ORANGE);
				}
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
