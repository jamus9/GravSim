package simulation;

import javafx.scene.shape.Circle;
import utils.Vec2D;

/**
 * A particle has no mass, radius, name or trail, so it does not influence other
 * bodies.
 * 
 * @author Jan Muskalla
 *
 */
public class Particle implements Body {

	/** planet properties */
	private Vec2D pos, vel, acc;

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
		this.pos = new Vec2D(posX, posY);
		this.vel = new Vec2D(velX, velY);
		this.acc = new Vec2D();
		circle = new Circle(1);
	}

	/** Creates a new particle */
	public Particle() {
		this(0, 0, 0, 0);
	}

	/** updates the circle position */
	@Override
	public void updateObjects() {
		Vec2D tp = Main.win.transform(pos);
		circle.setCenterX(tp.getX());
		circle.setCenterY(tp.getY());
	}

	@Override
	public Particle clone() {
		return new Particle(pos.getX(), pos.getY(), vel.getX(), vel.getY());
	}

	@Override
	public void setPos(Vec2D pos) {
		this.pos = pos;
	}

	@Override
	public void setPos(double x, double y) {
		this.pos = new Vec2D(x, y);
	}

	@Override
	public Vec2D getPos() {
		return pos;
	}

	@Override
	public void setVel(Vec2D vel) {
		this.vel = vel;
	}

	@Override
	public void setVel(double x, double y) {
		vel = new Vec2D(x, y);
	}

	@Override
	public Vec2D getVel() {
		return vel;
	}

	@Override
	public void resetAcc() {
		acc = new Vec2D();
	}

	@Override
	public Vec2D getAcc() {
		return acc;
	}

	@Override
	public void addAcc(Vec2D v) {
		acc = acc.add(v);
	}

	@Override
	public Circle getCircle() {
		return circle;
	}

}
