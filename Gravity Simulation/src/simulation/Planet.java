package simulation;

import java.util.LinkedList;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import utils.Vec2D;
import window.Window;

/**
 * Implements a planet with position and velocity, radius, mass and a name. Each
 * planet has a circle, velocity line and label that are drawn in the main
 * window. The orbitLineList contains the orbit that gets drawn and the list
 * orbitPoints saves the real coordinates of the orbit for later transformation.
 * 
 * @author Jan Muskalla
 * 
 */
public class Planet {
	private static int globalID = 1;
	
	// identification
	private int id;
	
	// planet properties
	private Vec2D pos;
	private Vec2D vel;
	private double radius;
	private double mass;

	// drawn objects
	private Circle circle;
	private Line velocityLine;
	private Label label;

	// the orbit lines
	private LinkedList<Line> orbitLineList;
	// the real coordinates of the orbit
	private LinkedList<Vec2D> orbitPoints;

	/**
	 * Creates a new Planet with position, velocity, mass, radius, color and
	 * name.
	 * 
	 * @param xp
	 * @param yp
	 * @param xv
	 * @param yv
	 * @param mass
	 * @param radius
	 * @param color
	 * @param name
	 */
	public Planet(double xp, double yp, double xv, double yv, double mass, double radius, Color color, String name) {
		this.pos = new Vec2D(xp, yp);
		this.vel = new Vec2D(xv, yv);
		this.mass = mass;
		this.radius = radius;
		// this.name = name;
		initializeObjects(color, name);
		id = globalID++;
	}

	/**
	 * Creates a new Planet with mass, radius, color and name.
	 * 
	 * @param mass
	 * @param radius
	 * @param color
	 * @param name
	 */
	public Planet(double mass, double radius, Color color, String name) {
		this(0, 0, 0, 0, mass, radius, color, name);
	}

	// no name, black, standard mass
	/**
	 * Creates a new Planet with position, velocity and radius.
	 * 
	 * @param xp
	 * @param yp
	 * @param xv
	 * @param yv
	 * @param radius
	 */
	public Planet(double xp, double yp, double xv, double yv, double radius) {
		this(xp, yp, xv, yv, standardMass(radius), radius, Color.BLACK, "");
	}

	/**
	 * Initializes the circle, vector, label and orbit.
	 * 
	 * @param color
	 * @param name
	 */
	private void initializeObjects(Color color, String name) {
		circle = new Circle();
		circle.setFill(color);
		circle.setStroke(Color.BLACK);

		velocityLine = new Line();
		velocityLine.setStroke(Color.RED);
		velocityLine.setVisible(false);

		label = new Label(name);

		orbitLineList = new LinkedList<Line>();
		orbitPoints = new LinkedList<Vec2D>();

		savePosition();
	}

	/**
	 * Returns the mass of a 3D planet with a given radius and a default
	 * density.
	 * 
	 * @param radius
	 *            the radius of the planet
	 * @return the mass of the planet
	 */
	private static double standardMass(double radius) {
		// typical asteroid? in kg/m^3
		double density = 2000;
		return (4.0 / 3.0) * Math.PI * Math.pow(radius, 3) * density;
	}

	/**
	 * Transforms a vector from planet coordinates to screen coordinates.
	 * 
	 * @param v
	 *            the vector to transform
	 * @return the transformed vector
	 */
	private static Vec2D transform(Vec2D v) {
		double x = Window.zoom * (Simulation.scale * v.x() + Window.dx + Window.tempdx) + Window.winX / 2.0;
		double y = Window.zoom * (Simulation.scale * -v.y() + Window.dy + Window.tempdy) + Window.winY / 2.0;
		return new Vec2D(x, y);
	}

	/**
	 * Updates the position and radius of the circle, the vector line, the label
	 * and the orbit of this planet.
	 */
	public void updateObjects() {
		Vec2D tp = transform(pos);

		// update circle
		circle.setCenterX(tp.x());
		circle.setCenterY(tp.y());
		circle.setRadius(getCircleRadius());

		// update vectors
		if (Window.vectors) {
			double scaleFactor = Simulation.scale * Window.zoom * 100000.0;
			velocityLine.setStartX(tp.x());
			velocityLine.setStartY(tp.y());
			velocityLine.setEndX(vel.x() * scaleFactor + tp.x());
			velocityLine.setEndY(-vel.y() * scaleFactor + tp.y());
		}

		// update labels
		if (Window.labels) {
			double offset = getCircleRadius() + 5;
			label.relocate(tp.x() + offset, tp.y());
		}

		// update orbits
		if (Window.orbits) {
			Vec2D tplast = transform(orbitPoints.getLast());

			if (tp.sub(tplast).norm() > 3) {
				
				if (orbitLineList.size() > 300) {
					orbitLineList.removeFirst();
					orbitPoints.removeFirst();
				}

				Line line = new Line(tplast.x(), tplast.y(), tp.x(), tp.y());
				line.setStroke(Color.RED);
				orbitLineList.add(line);

				savePosition();
			}
		}
	}

	/**
	 * Translate all Lines in orbitLineList with help of orbitPoints.
	 */
	public void updateOrbit() {
		Vec2D newStart, newEnd;
		Line line;

		for (int i = 0; i < orbitLineList.size(); i++) {
			newStart = transform(orbitPoints.get(i));
			newEnd = transform(orbitPoints.get(i + 1));

			line = orbitLineList.get(i);

			line.setStartX(newStart.x());
			line.setStartY(newStart.y());
			line.setEndX(newEnd.x());
			line.setEndY(newEnd.y());
		}
	}

	/**
	 * Deletes all orbit data.
	 */
	public void deleteOrbit() {
		orbitLineList.clear();
		orbitPoints.clear();
	}

	/**
	 * Return a copy of this planet.
	 */
	@Override
	public Planet clone() {
		return new Planet(pos.x(), pos.y(), vel.x(), vel.y(), mass, radius, (Color) circle.getFill(), getName());
	}

	/**
	 * If the planet is selected turn the border white or turn it black again.
	 * 
	 * @param selected
	 *            true if selected, false if not
	 */
	public void select(boolean selected) {
		if (selected)
			circle.setStroke(Color.WHITE);
		else
			circle.setStroke(Color.BLACK);
	}

	/**
	 * saves the current position in the list orbitPoints
	 */
	public void savePosition() {
		orbitPoints.add(pos.clone());
	}

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

	public void setVelocityLineVisibility(boolean b) {
		velocityLine.setVisible(b);
	}

	public void setLabelVisibility(boolean b) {
		label.setVisible(b);
	}
	
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}

	public Vec2D getPos() {
		return pos;
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
		return label.getText();
	}
	
	public int getID() {
		return id;
	}

	public LinkedList<Line> getOrbitLineList() {
		return orbitLineList;
	}
	
	private double getCircleRadius() {
		return Simulation.scale * Window.zoom * radius;
	}
	
	public boolean equals(Planet p) {
		return getID() == p.getID();
	}

}
