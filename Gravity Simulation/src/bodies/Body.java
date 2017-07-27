package bodies;

import javafx.scene.shape.Circle;
import utils.Vec2d;
import utils.Vec;

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

	public Vec getPos();

	public void setVel(Vec vel);

	public void setVel(double x, double y);

	public Vec getVel();

	public Vec getAcc();

	public void addAcc(Vec vec);

	public void resetAcc();

	public Circle getCircle();

	public Body clone();

	public void delete();

}
