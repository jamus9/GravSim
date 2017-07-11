package bodies;

import javafx.scene.shape.Circle;
import utils.Vec2d;

/**
 * An interface for all bodies in the simulation
 * 
 * @author Jan Muskalla
 *
 */
public interface Body {

	public void updateObjects();

	public void setPos(Vec2d pos);

	public void setPos(double x, double y);

	public Vec2d getPos();

	public void setVel(Vec2d vel);

	public void setVel(double x, double y);

	public Vec2d getVel();

	public Vec2d getAcc();

	public void addAcc(Vec2d vec);

	public void resetAcc();

	public Circle getCircle();

	public Body clone();

	public void delete();

}
