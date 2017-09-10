package game_anp36;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author Aaron Paskin
 *
 */
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
	
	/** ballCollide checks for a ball-block collision and checks the destroyed status of the block.
	 * @param ball		Circle representing ball to check for ball-block collision
	 * @return			Returns true if ball-block collision is detected
	 * 					Returns false otherwise */
	public boolean ballCollide(Circle ball) {
		if(ball.getCenterX() + ball.getRadius() >= BLOCK.getX() &&
				ball.getCenterX() - ball.getRadius() <= BLOCK.getX() + BLOCK.getWidth() &&
				ball.getCenterY() + ball.getRadius() >= BLOCK.getY() &&
				ball.getCenterY() - ball.getRadius() <= BLOCK.getY() + BLOCK.getHeight()) {
			collisions++;
			checkIfDestroyed();
			return true;
		}
		return false;
	}
	
	/** lazerCollide checks for a lazer-block collision and checks the destroyed status of the block.
	 * @param lazer		Rectangle representing lazer to check for lazer-block collision
	 * @return			Returns true if lazer-block collision is detected
	 * 					Returns false otherwise */
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
	
	/** checkIfDestroyed checks and sets the destroyed status of the block based on type and number of collisions.
	 * @return			Returns true if block is destroyed
	 * 					Returns false otherwise*/
	public boolean checkIfDestroyed() {
		if((type.equals("One Hit") || type.equals("Speed")) && collisions >= 1) {
			isDestroyed = true;
		}
		else if(type.equals("Two Hit") || type.equals("Traveling")) {
			if(collisions >= 2) {
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
	
	/** speedToChange tells the GameDriver in which direction to change the speed of the ball following
	 * a ball-block collision. The finds the side of the block to which the ball is closest upon collision
	 * and returns the appropriate direction (x or y) in which to change the speed.
	 * @param ball		Circle representing ball that collides with block
	 * @return			Returns "x" if the ball hits the left or right side of the block, "y" if the ball
	 * 					hits the top or bottom of the block, and "x and y" if the ball hits a corner of the block*/
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
