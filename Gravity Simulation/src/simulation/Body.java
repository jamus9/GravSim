package simulation;

import javafx.scene.shape.Circle;
import utils.Vec2D;

public interface Body {

	public void updateObjects();

	public Body clone();

	public void setPos(Vec2D pos);

	public void setPos(double x, double y);

	public void setVel(Vec2D vel);

	public void setVel(double x, double y);

	public Vec2D getPos();

	public Vec2D getVel();

	public Circle getCircle();

}
