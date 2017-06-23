package simulation;

import javafx.scene.shape.Circle;
import utils.Vec2D;

/**
 * An asteroid has no mass and no radius so it does not influence other bodies. But it has a name and a trail
 * 
 * @author Jan Muskalla
 *
 */
public class Asteroid implements Body {

	@Override
	public void updateObjects() {
		// TODO Auto-generated method stub

	}

	@Override
	public Planet clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Planet p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPos(Vec2D pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPos(double x, double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVel(Vec2D vel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVel(double x, double y) {
		// TODO Auto-generated method stub

	}

	@Override
	public Vec2D getPos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vec2D getVel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Circle getCircle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
