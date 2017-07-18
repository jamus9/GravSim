package bodies;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import utils.Vec2d;

public class SpaceShip implements Body {
	
	private Vec2d pos, vel;
	
	private Trail trail;
	private Circle circle;
	private Label label;
	
	public SpaceShip() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateObjects() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPos(Vec2d pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPos(double x, double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public Vec2d getPos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVel(Vec2d vel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVel(double x, double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public Vec2d getVel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vec2d getAcc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAcc(Vec2d vec) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetAcc() {
		// TODO Auto-generated method stub

	}

	@Override
	public Circle getCircle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Body clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

}
