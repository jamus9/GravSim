package bodies;

import javafx.scene.shape.Circle;
import utils.Vec2D;

/**
 * An interface for all bodies in the simulation
 * 
 * @author Jan Muskalla
 *
 */
public interface Body {

	public void updateObjects();

	public void setPos(Vec2D pos);

	public void setPos(double x, double y);

	public Vec2D getPos();

	public void setVel(Vec2D vel);

	public void setVel(double x, double y);

	public Vec2D getVel();

	public Vec2D getAcc();

	public void addAcc(Vec2D vec);

	public void resetAcc();

	public Circle getCircle();

	public Body clone();

	public void delete();

}
