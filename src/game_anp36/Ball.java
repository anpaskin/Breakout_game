package game_anp36;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;

import javafx.scene.Node;
import javafx.scene.shape.Circle;

public class Ball extends Node {

	private Circle BALL;
	private double vX;
	private double vY;
	
	public Ball(Circle ball, double velocityX, double velocityY) {
		BALL = ball;
		vX = velocityX;
		vY = velocityY;
	}
	
	public double getCenterX() {
		return BALL.getCenterX();
	}
	
	public double getCenterY() {
		return BALL.getCenterY();
	}
	
	public void setCenterX(double x) {
		BALL.setCenterX(x);
	}
	
	public void setCenterY(double y) {
		BALL.setCenterY(y);
	}
	
	public double getRadius() {
		return BALL.getRadius();
	}
	
	public double get_vX() {
		return vX;
	}
	
	public double get_vY() {
		return vY;
	}
	
	public void set_vX(double velocityX) {
		vX = velocityX;
	}
	
	public void set_vY(double velocityY) {
		vY = velocityY;
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
