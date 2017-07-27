package bodies;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import simulation.Main;
import utils.Orbit;
import utils.PolarVec;
import utils.Utils;
import utils.Vec2d;
import utils.Vec;
import window.ViewSettings;

/**
 * Implements a planet with position, velocity, radius, mass and a name. Each
 * planet has a circle, label and trail that are drawn in the main window.
 * 
 * @author Jan Muskalla
 * 
 */
public class Planet implements Body {

	/** planet properties */
	private Vec pos, vel, acc;
	private double radius, mass;

	/** drawn objects */
	private Circle circle;
	private Label label;
	private Trail trail;

	/**
	 * Creates a new Planet with position, velocity, mass, radius, color and name.
	 * 
	 * @param posX
	 * @param posY
	 * @param velX
	 * @param velY
	 * @param mass
	 * @param radius
	 * @param color
	 * @param name
	 */
	public Planet(double posX, double posY, double velX, double velY, double mass, double radius, Color color,
			String name) {
		this.pos = new Vec(posX, posY);
		this.vel = new Vec(velX, velY);
		this.mass = mass;
		this.radius = radius;
		initializeObjects(color, name);
	}

	/**
	 * Creates a new Planet with only mass, radius, color and name.
	 * 
	 * @param mass
	 * @param radius
	 * @param color
	 * @param name
	 */
	public Planet(double mass, double radius, Color color, String name) {
		this(0, 0, 0, 0, mass, radius, color, name);
	}

	/**
	 * Creates an anonymous Planet with position, velocity and mass (with density).
	 * 
	 * @param posX
	 * @param posY
	 * @param velX
	 * @param velY
	 * @param mass
	 * @param density
	 */
	public Planet(double posX, double posY, double velX, double velY, double mass, double density) {
		this.pos = new Vec(posX, posY);
		this.vel = new Vec(velX, velY);
		setMass(mass, density);
		initializeObjects(ViewSettings.bodyColor, "");
	}

	/**
	 * Initializes the circle, label and trail (and acc).
	 * 
	 * @param color
	 * @param name
	 */
	private void initializeObjects(Color color, String name) {
		label = new Label(name);
		label.setTextFill(ViewSettings.textColor);
		label.setScaleX(0.9);
		label.setScaleY(0.9);
		circle = new Circle(1, color);
		trail = new Trail(this);
		acc = new Vec();
	}

	/**
	 * Updates the position and radius of the circle, the label and the trail of
	 * this planet.
	 */
	@Override
	public void updateObjects() {
		Vec tp = Main.win.transform(pos);
		double circleRadius = Main.sim.getScale() * Main.win.getZoom() * radius;

		// update circle
		circle.setCenterX(tp.getX());
		circle.setCenterY(tp.getY());
		if (circleRadius < ViewSettings.minBodySize)
			circle.setRadius(ViewSettings.minBodySize);
		else
			circle.setRadius(circleRadius);

		// update label
		if (Main.win.isLabels()) {
			double d = (circleRadius / 1.41421) + 2;
			label.relocate(tp.getX() + d, tp.getY() + d);
		}

		// update trail
		if (Main.win.isTrails())
			trail.addLine();
	}

	/** Return a copy of this planet. */
	@Override
	public Planet clone() {
		return new Planet(pos.getX(), pos.getY(), vel.getX(), vel.getY(), mass, radius, (Color) circle.getFill(),
				getName());
	}

	/** deletes the planet from the window */
	@Override
	public void delete() {
		trail.delete();
		((Pane) circle.getParent()).getChildren().remove(circle);
		((Pane) label.getParent()).getChildren().remove(label);
	}

	/** a selected planet has a white border */
	public void select() {
		circle.setStroke(ViewSettings.planetSelectionColor);
	}

	/** a deselected planet has a black border */
	public void deselect() {
		circle.setStroke(null);
	}

	public Vec getPos() {
		return pos;
	}

	public void setPos(Vec2d pos) {
		this.pos.set(pos.getX(), pos.getY());
	}

	// public void setPos(PolarVec2d pos) {
	// this.pos = pos.toVec2d();
	// }

	public void setPos(double x, double y) {
		pos = new Vec(x, y);
	}

	public Vec getVel() {
		return vel;
	}

	public void setVel(Vec vel) {
		this.vel = vel;
	}

	public void setVel(double x, double y) {
		vel = new Vec(x, y);
	}

	public Vec getAcc() {
		return acc;
	}

	public void addAcc(Vec v) {
		acc = acc.add(v);
	}

	public void resetAcc() {
		acc = new Vec();
	}

	public double getMass() {
		return mass;
	}

	/** sets the mass and the radius of the planet with with a given density */
	public void setMass(double mass, double density) {
		this.mass = mass;
		this.radius = Math.pow(3 * mass / (4 * Math.PI * density), 1d / 3);
	}

	public double getDensity() {
		return mass / ((4d / 3) * Math.PI * Math.pow(radius, 3));
	}

	public double getRadius() {
		return radius;
	}

	public Circle getCircle() {
		return circle;
	}

	public Color getColor() {
		return (Color) circle.getFill();
	}

	public void setColor(Color col) {
		this.circle.setFill(col);
	}

	public Label getLabel() {
		return label;
	}

	public String getName() {
		return label.getText();
	}

	public void setName(String str) {
		label.setText(str);
	}

	public Trail getTrail() {
		return trail;
	}

	public void setOrbitalVel(Planet parent) {
		setVel(Utils.getOrbitalVelocityCircular(parent, this));
	}

	public void setOrbitalVel(Planet parent, double sma) {
		setVel(Utils.getOrbitalVelocityElliptical(parent, this, sma));
	}

	public void setOrbit(Planet parent, Orbit orb) {
		setPos(new PolarVec(orb.getPeriapsis(), orb.getArgOfPeriapsis()));
		setVel(Utils.getOrbitalVelocityElliptical(parent, this, orb.getSma()));
	}

}
