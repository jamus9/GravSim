package bodies;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import simulation.Main;
import utils.Orbit;
import utils.PolarVec2d;
import utils.Utils;
import utils.Vec2d;
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
	private Vec2d pos, vel, acc;
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
		this.pos = new Vec2d(posX, posY);
		this.vel = new Vec2d(velX, velY);
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
		this.pos = new Vec2d(posX, posY);
		this.vel = new Vec2d(velX, velY);
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
		circle = new Circle(1, color);
		trail = new Trail(this);
		acc = new Vec2d();
	}

	/**
	 * Updates the position and radius of the circle, the label and the trail of
	 * this planet.
	 */
	@Override
	public void updateObjects() {
		Vec2d tp = Main.win.transform(pos);
		double circleRadius = Main.sim.getScale() * Main.win.getZoom() * radius;

		// update circle
		circle.setCenterX(tp.getX());
		circle.setCenterY(tp.getY());
		if (circleRadius < ViewSettings.minBodySize)
			circle.setRadius(ViewSettings.minBodySize);
		else
			circle.setRadius(circleRadius);

		// update label
		if (Main.win.isLabels())
			label.relocate(tp.getX() + circleRadius + 5, tp.getY());

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

	public Vec2d getPos() {
		return pos;
	}

	public void setPos(Vec2d pos) {
		this.pos = pos;
	}

	public void setPos(PolarVec2d pos) {
		this.pos = pos.toVec2d();
	}

	public void setPos(double x, double y) {
		pos = new Vec2d(x, y);
	}

	public Vec2d getVel() {
		return vel;
	}

	public void setVel(Vec2d vel) {
		this.vel = vel;
	}

	public void setVel(double x, double y) {
		vel = new Vec2d(x, y);
	}

	public Vec2d getAcc() {
		return acc;
	}

	public void addAcc(Vec2d v) {
		acc = acc.add(v);
	}

	public void resetAcc() {
		acc = new Vec2d();
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
		setPos(new PolarVec2d(orb.getPeriapsis(), orb.getArgumentOfPeriapsis()));
		setVel(Utils.getOrbitalVelocityElliptical(parent, this, orb.getSma()));
	}

}
