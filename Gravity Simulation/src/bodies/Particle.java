package bodies;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import simulation.Main;
import utils.Vec2d;
import utils.Vec;
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
	private Vec pos, vel, acc;

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
		this.pos = new Vec(posX, posY);
		this.vel = new Vec(velX, velY);
		this.acc = new Vec();
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
		Vec tp = Main.win.transform(pos);
		circle.setCenterX(tp.getX());
		circle.setCenterY(tp.getY());
	}

	@Override
	public Particle clone() {
		return new Particle(pos.getX(), pos.getY(), vel.getX(), vel.getY());
	}

	@Override
	public void setPos(Vec2d pos) {
		this.pos.set(pos.getX(), pos.getY());
	}

	@Override
	public void setPos(double x, double y) {
		this.pos = new Vec(x, y);
	}

	@Override
	public Vec getPos() {
		return pos;
	}

	@Override
	public void setVel(Vec vel) {
		this.vel = vel;
	}

	@Override
	public void setVel(double x, double y) {
		vel = new Vec(x, y);
	}

	@Override
	public Vec getVel() {
		return vel;
	}

	@Override
	public void resetAcc() {
		acc = new Vec();
	}

	@Override
	public Vec getAcc() {
		return acc;
	}

	@Override
	public void addAcc(Vec v) {
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
