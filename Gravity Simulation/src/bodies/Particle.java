package bodies;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import simulation.Main;
import utils.Vec2d;
import window.ViewSettings;

/**
 * A particle has no mass, radius, name or trail, so it does not influence other
 * bodies.
 * 
 * @author Jan Muskalla
 *
 */
public class Particle implements Body {

	/** planet properties */
	private Vec2d pos, vel, acc;

	/** drawn objects */
	private Circle circle;

	/**
	 * Creates a new particle with position and velocity
	 * 
	 * @param posX
	 * @param posY
	 * @param velX
	 * @param velY
	 */
	public Particle(double posX, double posY, double velX, double velY) {
		this.pos = new Vec2d(posX, posY);
		this.vel = new Vec2d(velX, velY);
		this.acc = new Vec2d();
		circle = new Circle(ViewSettings.minBodySize);
		circle.setFill(Color.WHITE);
	}

	/** Creates a new particle */
	public Particle() {
		this(0, 0, 0, 0);
	}

	/** updates the circle position */
	@Override
	public void updateObjects() {
		Vec2d tp = Main.win.transform(pos);
		circle.setCenterX(tp.getX());
		circle.setCenterY(tp.getY());
	}

	@Override
	public Particle clone() {
		return new Particle(pos.getX(), pos.getY(), vel.getX(), vel.getY());
	}

	@Override
	public void setPos(Vec2d pos) {
		this.pos = pos;
	}

	@Override
	public void setPos(double x, double y) {
		this.pos = new Vec2d(x, y);
	}

	@Override
	public Vec2d getPos() {
		return pos;
	}

	@Override
	public void setVel(Vec2d vel) {
		this.vel = vel;
	}

	@Override
	public void setVel(double x, double y) {
		vel = new Vec2d(x, y);
	}

	@Override
	public Vec2d getVel() {
		return vel;
	}

	@Override
	public void resetAcc() {
		acc = new Vec2d();
	}

	@Override
	public Vec2d getAcc() {
		return acc;
	}

	@Override
	public void addAcc(Vec2d v) {
		acc = acc.add(v);
	}

	@Override
	public Circle getCircle() {
		return circle;
	}

	@Override
	public void delete() {
		((Pane) circle.getParent()).getChildren().remove(circle);
	}

}
