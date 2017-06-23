package simulation;

import java.util.LinkedList;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import utils.Vec2D;
import javafx.scene.layout.Pane;

/**
 * Implements a planet with position and velocity, radius, mass and a name. Each
 * planet has a circle, velocity line and label that are drawn in the main
 * window. The orbitLineList contains the orbit that gets drawn and the list
 * orbitPoints saves the real coordinates of the orbit for later transformation.
 * 
 * @author Jan Muskalla
 * 
 */
public class Planet implements Body {
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
	private LinkedList<Line> trailLineList;
	// the real coordinates of the orbit
	private LinkedList<Vec2D> trailPointsList;

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
		id = globalID++;
		this.pos = new Vec2D(xp, yp);
		this.vel = new Vec2D(xv, yv);
		this.mass = mass;
		this.radius = radius;
		initializeObjects(color, name);
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

	/**
	 * Creates a new Planet with position, velocity and mass.
	 * 
	 * @param xp
	 * @param yp
	 * @param xv
	 * @param yv
	 * @param radius
	 */
	public Planet(double xp, double yp, double xv, double yv, double mass, double density) {
		id = globalID++;
		this.pos = new Vec2D(xp, yp);
		this.vel = new Vec2D(xv, yv);
		setMass(mass, density);
		initializeObjects(Color.BLACK, "");
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

		label = new Label(name);

		velocityLine = new Line();
		velocityLine.setStroke(Color.GREEN);
		velocityLine.setVisible(false);

		trailLineList = new LinkedList<Line>();
		trailPointsList = new LinkedList<Vec2D>();

		savePosition();
	}

	/**
	 * Updates the position and radius of the circle, the vector line, the label
	 * and the trail of this planet.
	 */
	public void updateObjects() {
		Vec2D tp = Main.win.transform(pos);

		// update circle
		circle.setCenterX(tp.getX());
		circle.setCenterY(tp.getY());
		circle.setRadius(getCircleRadius());

		// update vector
		if (Main.win.isVectors()) {
			double scaleFactor = Main.sim.getScale() * Main.win.getZoom() * 100000.0;
			velocityLine.setStartX(tp.getX());
			velocityLine.setStartY(tp.getY());
			velocityLine.setEndX(vel.getX() * scaleFactor + tp.getX());
			velocityLine.setEndY(-vel.getY() * scaleFactor + tp.getY());
		}

		// update label
		if (Main.win.isLabels()) {
			double offset = getCircleRadius() + 5;
			label.relocate(tp.getX() + offset, tp.getY());
		}

		// update trail
		if (Main.win.isTrails()) {
			Vec2D tplast = Main.win.transform(trailPointsList.getLast());

			if (tp.sub(tplast).norm() > 3) {
				if (trailLineList.size() > 500) {
					Line line = trailLineList.getFirst();
					((Pane) line.getParent()).getChildren().remove(line);
					trailLineList.removeFirst();
					trailPointsList.removeFirst();
				}

				Line line = new Line(tplast.getX(), tplast.getY(), tp.getX(), tp.getY());
				line.setStroke(Color.RED);

				FadeTransition ft = new FadeTransition(Duration.seconds(10), line);
				ft.setFromValue(1.0);
				ft.setToValue(0.0);
				ft.play();

				trailLineList.add(line);
				Main.win.addTrail(line);

				savePosition();
			}
		}
	}

	/** returns the scaled radius of the circle */
	private double getCircleRadius() {
		return Main.sim.getScale() * Main.win.getZoom() * radius;
	}

	/** Translate all Lines in orbitLineList with help of orbitPoints. */
	public void updateTrail() {
		Vec2D newStart, newEnd;
		Line line;

		for (int i = 0; i < trailLineList.size(); i++) {
			line = trailLineList.get(i);

			newStart = Main.win.transform(trailPointsList.get(i));
			newEnd = Main.win.transform(trailPointsList.get(i + 1));

			line.setStartX(newStart.getX());
			line.setStartY(newStart.getY());
			line.setEndX(newEnd.getX());
			line.setEndY(newEnd.getY());
		}
	}

	/** Deletes all orbit data. */
	public void deleteTrail() {
		for (Line line : trailLineList) {
			((Pane) line.getParent()).getChildren().remove(line);
		}
		trailLineList.clear();
		trailPointsList.clear();
	}

	/** Return a copy of this planet. */
	@Override
	public Planet clone() {
		return new Planet(pos.getX(), pos.getY(), vel.getX(), vel.getY(), mass, radius, (Color) circle.getFill(),
				getName());
	}

	/** saves the current position in the list orbitPoints */
	public void savePosition() {
		trailPointsList.add(pos.clone());
	}

	public void select() {
		circle.setStroke(Color.WHITE);
	}

	public void deselect() {
		circle.setStroke(Color.BLACK);
	}

	public boolean equals(Planet p) {
		return getID() == p.getID();
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

	/** sets the mass and the radius of the planet with with a given density */
	public void setMass(double mass, double density) {
		this.mass = mass;
		this.radius = Math.pow(3 * mass / (4 * Math.PI * density), 1d / 3);
	}

	public void setColor(Color col) {
		this.circle.setFill(col);
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

	public double getDensity() {
		return mass / ((4d / 3) * Math.PI * radius * radius * radius);
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
		return trailLineList;
	}

}
