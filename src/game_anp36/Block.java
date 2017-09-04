package game_anp36;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Block extends Node {

	private Rectangle BLOCK;
	private boolean isDestroyed;
	
	public Block(Rectangle block) {
		BLOCK = block;
	}
	
	public boolean isDestroyed() {
		return isDestroyed;
	}
	
	public boolean ballCollide(Circle ball) {
		boolean ret = false;
		if(ball.getCenterX() + ball.getRadius() >= BLOCK.getX() &&
				ball.getCenterX() - ball.getRadius() <= BLOCK.getX() + BLOCK.getWidth() &&
				ball.getCenterY() + ball.getRadius() >= BLOCK.getY() &&
				ball.getCenterY() - ball.getRadius() <= BLOCK.getY() + BLOCK.getHeight()) {
			System.out.println("Ball collision");
			isDestroyed = true;
			ret = true;
			return ret;
		}
		else return ret;
	}
	
	public Rectangle getRectangle() {
		return BLOCK;
	}
	
	public String speedToChange(Circle ball) {
		if(ball.getCenterX() + ball.getRadius() > BLOCK.getX() ||
				ball.getCenterX() - ball.getRadius() < BLOCK.getX() + BLOCK.getWidth()) {
			return "x";
		} 
		else if(ball.getCenterY() + ball.getRadius() > BLOCK.getY() ||
				ball.getCenterY() - ball.getRadius() < BLOCK.getY() + BLOCK.getHeight()) {
			return "y";
		}
		else return "No collision";
	}
	
	public boolean leftCollision(Circle ball) {
		if(ball.getCenterX() + ball.getRadius() == BLOCK.getX() &&
				(ball.getCenterY() + ball.getRadius() >= BLOCK.getY() &&
				ball.getCenterY() - ball.getRadius() <= BLOCK.getY() + BLOCK.getHeight())) {
			return true;
		}
		else return false;
	}
	
	public boolean rightCollision(Circle ball) {
		if(ball.getCenterX() - ball.getRadius() == BLOCK.getX() + BLOCK.getWidth() &&
				(ball.getCenterY() + ball.getRadius() >= BLOCK.getY() &&
				ball.getCenterY() - ball.getRadius() <= BLOCK.getY() + BLOCK.getHeight())) {
			return true;
		}
		else return false;
	}
	
	public boolean topCollision(Circle ball) {
		if(ball.getCenterY() + ball.getRadius() == BLOCK.getY() &&
				(ball.getCenterX() + ball.getRadius() >= BLOCK.getX() &&
				ball.getCenterX() - ball.getRadius() <= BLOCK.getX() + BLOCK.getWidth())) {
			return true;
		}
		else return false;
	}
	
	public boolean bottomCollision(Circle ball) {
		if(ball.getCenterY() - ball.getRadius() == BLOCK.getY() + BLOCK.getHeight() &&
				(ball.getCenterX() + ball.getRadius() >= BLOCK.getX() &&
				ball.getCenterX() - ball.getRadius() <= BLOCK.getX() + BLOCK.getWidth())) {
			return true;
		}
		else return false;
	}

	@Override
	protected NGNode impl_createPeer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean impl_computeContains(double localX, double localY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
