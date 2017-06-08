package simulation;

import java.util.LinkedList;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import utils.Vec2D;
import utils.Vec4D;
import window.Window;

public class Planet {

	private Vec2D pos;
	private Vec2D vel;
	private Vec2D oldPos;

	private double radius;
	private double mass;

	private String name;

	private Circle circle;
	private Line velocityLine;
	private Label label;

	// the orbit lines
	private LinkedList<Line> orbitLineList;

	// the coordinates of the orbit
	private LinkedList<Vec4D> orbitPoints;

	// x y x y mass radius color name
	public Planet(double xp, double yp, double xv, double yv, double mass, double radius, Color color, String name) {
		this.pos = new Vec2D(xp, yp);
		this.vel = new Vec2D(xv, yv);
		this.mass = mass;
		this.radius = radius;
		this.name = name;
		initializeObjects(color, this.name);
	}

	// position and velocity = 0,0
	public Planet(double mass, double radius, Color color, String name) {
		this(0, 0, 0, 0, mass, radius, color, name);
	}

	// no name, black, standard mass
	public Planet(double xp, double yp, double xv, double yv, double radius) {
		this(xp, yp, xv, yv, standardMass(radius), radius, Color.BLACK, "");
	}

	/**
	 * Private methods --------------------------------------------------
	 */

	/**
	 * Initializes the circle, vector, label and orbit.
	 * 
	 * @param color
	 * @param name
	 */
	private void initializeObjects(Color color, String name) {
		Vec2D tp = transform(pos);

		circle = new Circle(tp.x(), tp.y(), Simulation.scale * radius, color);
		circle.setStroke(Color.BLACK);

		velocityLine = new Line(0, 0, 0, 0);
		velocityLine.setStroke(Color.RED);
		velocityLine.setVisible(false);

		label = new Label(name);
		label.relocate(tp.x() + radius + 10, tp.y());

		orbitLineList = new LinkedList<Line>();
		orbitPoints = new LinkedList<Vec4D>();

		setOldPos();
	}

	/**
	 * Sets the next orbit line in coordinates of the planet and saves the real position.
	 */
	private void setNextOrbitLine() {
		Vec2D tp = transform(pos);
		Vec2D top = transform(oldPos);

		if (tp.sub(top).norm() > 4) {

			if (orbitLineList.size() > 300) {
				orbitLineList.removeFirst();
				orbitPoints.removeFirst();
			}

			Line line = new Line(tp.x(), tp.y(), top.x(), top.y());
			line.setStroke(Color.RED);

			orbitLineList.addLast(line);
			orbitPoints.addLast(new Vec4D(oldPos.x(), oldPos.y(), pos.x(), pos.y()));

			setOldPos();

			// FadeTransition ft = new FadeTransition(new Duration(30000),
			// newLine);
			// ft.setFromValue(1.0);
			// ft.setToValue(0.0);
			// ft.play();
		}
	}

	// typical asteroid?
	private static final double DENSITY = 2000; // kg/m^3

	/**
	 * Returns the mass of a 3D planet with a given radius and a default density.
	 * 
	 * @param radius
	 *            the radius of the planet
	 * @return the mass of the planet
	 */
	private static double standardMass(double radius) {
		return (4.0 / 3.0) * Math.PI * Math.pow(radius, 3) * DENSITY;
	}

	// transforms a vector from planet coordinates to screen coordinates
	private static Vec2D transform(Vec2D v) {
		double x = Window.zoom * (Simulation.scale * v.x() + Window.dx + Window.tempdx) + Window.winX / 2.0;
		double y = Window.zoom * (Simulation.scale * -v.y() + Window.dy + Window.tempdy) + Window.winY / 2.0;
		return new Vec2D(x, y);
	}

	/**
	 * Public methods --------------------------------------------------
	 */

	/**
	 * Updates the position and radius of the circle, the vector line, the label
	 * and the orbit of this planet
	 */
	public void updateObjects() {
		Vec2D tpos = transform(pos);

		circle.setCenterX(tpos.x());
		circle.setCenterY(tpos.y());
		circle.setRadius(Simulation.scale * Window.zoom * radius);

		if (Window.vectors) {
			velocityLine.setStartX(tpos.x());
			velocityLine.setStartY(tpos.y());
			velocityLine.setEndX(vel.x() + tpos.x());
			velocityLine.setEndY(-vel.y() + tpos.y());
		}

		if (Window.labels)
			label.relocate(tpos.x() + Simulation.scale * Window.zoom * radius + 5, tpos.y());

		if (Window.orbits)
			setNextOrbitLine();
	}

	// translates all Lines in orbitLineList
	public void translateOrbit() {
		Vec2D newStart, newEnd;
		Vec4D point;
		Line line;

		for (int i = 0; i < orbitLineList.size(); i++) {
			point = orbitPoints.get(i);

			newStart = transform(new Vec2D(point.x1(), point.x2()));
			newEnd = transform(new Vec2D(point.x3(), point.x4()));

			line = orbitLineList.get(i);

			line.setStartX(newStart.x());
			line.setStartY(newStart.y());
			line.setEndX(newEnd.x());
			line.setEndY(newEnd.y());
		}
	}

	// deletes the orbit
	public void deleteOrbit() {
		orbitLineList.clear();
		orbitPoints.clear();
	}

	// return a copy of this planet
	@Override
	public Planet clone() {
		return new Planet(pos.x(), pos.y(), vel.x(), vel.y(), mass, radius, (Color) circle.getFill(), name);
	}

	// if the planet is selected turn the border white
	public void select(boolean selected) {
		if (selected)
			circle.setStroke(Color.WHITE);
		else
			circle.setStroke(Color.BLACK);
	}

	// setter

	public void setPos(Vec2D pos) {
		this.pos = pos;
	}

	public void setPos(double x, double y) {
		pos = new Vec2D(x, y);
	}

	public void setVel(Vec2D vel) {
		this.vel = vel;
	}

	public void setVel(double x, double y) {
		vel = new Vec2D(x, y);
	}

	public void setOldPos() {
		this.oldPos = this.pos;
	}

	public void setVelocityLineVisibility(boolean b) {
		velocityLine.setVisible(b);
	}

	public void setLabelVisibility(boolean b) {
		label.setVisible(b);
	}

	// getter

	public Vec2D getPos() {
		return pos;
	}

	public Vec2D getOldPos() {
		return oldPos;
	}

	public Vec2D getVel() {
		return vel;
	}

	public double getMass() {
		return mass;
	}

	public double getRadius() {
		return radius;
	}

	public Circle getCircle() {
		return circle;
	}

	public Line getVelocityLine() {
		return velocityLine;
	}

	public Label getLabel() {
		return label;
	}

	public String getName() {
		return name;
	}

	public LinkedList<Line> getOrbitLineList() {
		return orbitLineList;
	}

}
