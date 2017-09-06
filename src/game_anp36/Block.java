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
		if(ball.getCenterX() + ball.getRadius() >= BLOCK.getX() &&
				ball.getCenterX() - ball.getRadius() <= BLOCK.getX() + BLOCK.getWidth() &&
				ball.getCenterY() + ball.getRadius() >= BLOCK.getY() &&
				ball.getCenterY() - ball.getRadius() <= BLOCK.getY() + BLOCK.getHeight()) {
			System.out.println("Ball collision");
			isDestroyed = true;
			return true;
		}
		else return false;
	}
	
	public Rectangle getRectangle() {
		return BLOCK;
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
			((cX + r) - bX < (bY + h) - (cY + r) &&
			 (cX + r) - bX < (cY - r) - bY) ||
			((bX + w) - (cX - r) < (bY + h) - (cY + r) &&
			((bX + w) - (cX - r) < (cY - r) - bY))
				) {
				return "x";
		}
		else if(
					((cY - r) - bY < (cX - r) - bX &&
					 (cY - r) - bY < (bX + w) - (cX + r)) ||
					((bY + h) - (cY + r) < (cX - r) - bX &&
					 (bY + h) - (cY + r) < (bX + w) - (cX + r))
				) {
			return "y";
		}
		else return "No collision";
	}
	
	public boolean leftCollision(Circle ball) {
		return ball.getCenterX() + ball.getRadius() == BLOCK.getX() &&
				(ball.getCenterY() + ball.getRadius() >= BLOCK.getY() &&
				ball.getCenterY() - ball.getRadius() <= BLOCK.getY() + BLOCK.getHeight());
	}
	
	public boolean rightCollision(Circle ball) {
		return ball.getCenterX() - ball.getRadius() == BLOCK.getX() + BLOCK.getWidth() &&
				(ball.getCenterY() + ball.getRadius() >= BLOCK.getY() &&
				ball.getCenterY() - ball.getRadius() <= BLOCK.getY() + BLOCK.getHeight());
	}
	
	public boolean topCollision(Circle ball) {
		return ball.getCenterY() + ball.getRadius() == BLOCK.getY() &&
				(ball.getCenterX() + ball.getRadius() >= BLOCK.getX() &&
				ball.getCenterX() - ball.getRadius() <= BLOCK.getX() + BLOCK.getWidth());
	}
	
	public boolean bottomCollision(Circle ball) {
		return ball.getCenterY() - ball.getRadius() == BLOCK.getY() + BLOCK.getHeight() &&
				(ball.getCenterX() + ball.getRadius() >= BLOCK.getX() &&
				ball.getCenterX() - ball.getRadius() <= BLOCK.getX() + BLOCK.getWidth());
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
