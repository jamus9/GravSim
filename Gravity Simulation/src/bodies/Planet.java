package bodies;

import java.util.LinkedList;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import simulation.Main;
import utils.Utils;
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

	/** planet properties */
	private Vec2D pos, vel, acc;
	private double radius, mass;

	/** drawn objects */
	private Circle circle;
	private Line velocityLine, accelerationLine;
	private Label label;

	/** the trail lines */
	private LinkedList<Line> trailLineList;

	/** the real coordinates of the trail */
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
	public Planet(double posX, double posY, double velX, double velY, double mass, double radius, Color color,
			String name) {
		this.pos = new Vec2D(posX, posY);
		this.vel = new Vec2D(velX, velY);
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
	 * Creates an anonymous Planet with position, velocity and mass (with
	 * density).
	 * 
	 * @param xp
	 * @param yp
	 * @param xv
	 * @param yv
	 * @param radius
	 */
	public Planet(double posX, double posY, double velX, double velY, double mass, double density) {
		this.pos = new Vec2D(posX, posY);
		this.vel = new Vec2D(velX, velY);
		setMass(mass, density);
		initializeObjects(Color.BLACK, "");
	}

	/**
	 * Initializes the circle, vectors, label and trail.
	 * 
	 * @param color
	 * @param name
	 */
	private void initializeObjects(Color color, String name) {
		label = new Label(name);

		circle = new Circle();
		circle.setFill(color);
		circle.setStroke(Color.BLACK);

		velocityLine = new Line();
		velocityLine.setStroke(Color.GREEN);
		velocityLine.setVisible(false);

		accelerationLine = new Line();
		accelerationLine.setStroke(Color.BLUE);
		accelerationLine.setVisible(false);
		acc = new Vec2D();

		trailLineList = new LinkedList<Line>();
		trailPointsList = new LinkedList<Vec2D>();

		// first position of the first trail
		savePosition();
	}

	/**
	 * Updates the position and radius of the circle, the vector line, the label
	 * and the trail of this planet.
	 */
	public void updateObjects() {
		Vec2D tp = Main.win.transform(pos);
		double circleRadius = Main.sim.getScale() * Main.win.getZoom() * radius;

		// update circle
		circle.setCenterX(tp.getX());
		circle.setCenterY(tp.getY());
		circle.setRadius(circleRadius);

		// update vectors
		if (Main.win.isVectors()) {

			// velocity
			double scaleFactor = Main.sim.getScale() * Main.win.getZoom() * 10e4;
			velocityLine.setStartX(tp.getX());
			velocityLine.setStartY(tp.getY());
			velocityLine.setEndX(tp.getX() + vel.getX() * scaleFactor);
			velocityLine.setEndY(tp.getY() - vel.getY() * scaleFactor);

			// acceleration
			double scaleFactor2 = Main.sim.getScale() * Main.win.getZoom() * 10e9;
			accelerationLine.setStartX(tp.getX());
			accelerationLine.setStartY(tp.getY());
			accelerationLine.setEndX(tp.getX() + acc.getX() * scaleFactor2);
			accelerationLine.setEndY(tp.getY() - acc.getY() * scaleFactor2);
		}

		// update label
		if (Main.win.isLabels()) {
			double offset = circleRadius + 5;
			label.relocate(tp.getX() + offset, tp.getY());
		}

		// update trail
		if (Main.win.isTrails()) {
			Vec2D tplast = Main.win.transform(trailPointsList.getLast());

			// only draw new line if planet moved 3 pixel
			if (tp.sub(tplast).norm() > 3) {

				// delete last
				if (trailLineList.size() > 500) {
					Line line = trailLineList.getFirst();
					((Pane) line.getParent()).getChildren().remove(line);
					trailLineList.removeFirst();
					trailPointsList.removeFirst();
				}

				// the new line
				Line line = new Line(tplast.getX(), tplast.getY(), tp.getX(), tp.getY());
				line.setStroke(Color.RED);

				FadeTransition ft = new FadeTransition(Duration.seconds(10), line);
				ft.setFromValue(1.0);
				ft.setToValue(0.0);
				ft.play();

				trailLineList.add(line);
				Main.win.addTrail(line);

				// save position for the next line start
				savePosition();
			}
		}
	}

	/** Translate all Lines in orbitLineList with help of orbitPoints. */
	public void updateTrail() {
		Vec2D newStart, newEnd;
		Line line;

		for (int i = 0; i < trailLineList.size(); i++) {
			line = trailLineList.get(i);

			// get the original position of the trail and use the current
			// transform
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

	/** a selected planet has a white border */
	public void select() {
		circle.setStroke(Color.WHITE);
	}

	/** a deselected planet has a black border */
	public void deselect() {
		circle.setStroke(Color.BLACK);
	}

	public void setPos(Vec2D pos) {
		this.pos = pos;
	}

	public void setPos(double x, double y) {
		pos = new Vec2D(x, y);
	}

	public Vec2D getPos() {
		return pos;
	}

	public void setVel(Vec2D vel) {
		this.vel = vel;
	}

	public void setVel(double x, double y) {
		vel = new Vec2D(x, y);
	}

	public Vec2D getVel() {
		return vel;
	}

//	public void setAcc(Vec2D acc) {
//		this.acc = acc;
//	}

	public void resetAcc() {
		acc = new Vec2D();
	}

	public Vec2D getAcc() {
		return acc;
	}

	public void addAcc(Vec2D v) {
		acc = acc.add(v);
	}

	/** sets the mass and the radius of the planet with with a given density */
	public void setMass(double mass, double density) {
		this.mass = mass;
		this.radius = Math.pow(3 * mass / (4 * Math.PI * density), 1d / 3);
	}

	public double getMass() {
		return mass;
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

	public Line getVelocityLine() {
		return velocityLine;
	}

	public Line getAccelerationLine() {
		return accelerationLine;
	}

	public LinkedList<Line> getTrailLineList() {
		return trailLineList;
	}

	public void setLabelVisibility(boolean b) {
		label.setVisible(b);
	}

	public void setVectorLinesVisibility(boolean b) {
		velocityLine.setVisible(b);
		accelerationLine.setVisible(b);
	}
	
	public void delete() {
		deleteTrail();
		((Pane) circle.getParent()).getChildren().remove(circle);
		((Pane) label.getParent()).getChildren().remove(label);
	}
	
	public void setZero() {
		pos = new Vec2D();
		vel = new Vec2D();
	}
	
	public void setOrbitalVel(Planet parent) {
		setVel(Utils.getOrbitalVelocity(parent, this));
	}

}
