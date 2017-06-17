package windows;

import constellations.StartConditions;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import simulation.Main;
import simulation.Planet;
import utils.Utils;
import utils.Vec2D;

/**
 * The main window of the application that handles all drawn objects.
 * 
 * (orbit translate only when needed)
 * 
 * @author Jan Muskalla
 *
 */
public class Window extends Application {

	// flags for orbits label and vectors
	protected boolean orbits, labels, vectors;

	// zoom and translation
	private double zoom;
	private double dx, dy;
	// coordinates for mouse dragging
	private double tempdx, tempdy;
	private double mouseX, mouseY;

	// the selected planet
	private Planet selectedPlanet;

	// the next planet that will be placed
	protected Planet nextPlacedPlanet;

	protected boolean orbitMode;

	// scene of the window
	private Scene scene;

	// group for all orbits
	private Group orbitGroup;
	// group for all circles, vectors, labels
	private Group planetGroup;
	// group for all information an the screen
	protected InfoGroup infoGroup;
	// the menu bar
	private CustomMenuBar menuBar;

	/**
	 * Starts the window for the simulation.
	 * 
	 * @param primaryStage
	 */
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Gravity Simulation");
		primaryStage.setMaximized(true);

		// set scene
		Group root = new Group();
		scene = new Scene(root, 1200, 700, Color.LIGHTBLUE);
		setSceneEvents(scene);

		// add everything to root
		orbitGroup = new Group();
		planetGroup = new Group();
		infoGroup = new InfoGroup();
		menuBar = new CustomMenuBar(primaryStage);
		root.getChildren().addAll(orbitGroup, planetGroup, infoGroup, menuBar);

		// initialize all values to default and load the planet objects
		reset();

		nextPlacedPlanet = StartConditions.moon.clone();
		orbits = true;
		labels = true;
		vectors = false;
		infoGroup.setVisible(true);
		orbitMode = true;
		infoGroup.setOrbitMode(orbitMode);

		menuBar.updateCMIs();

		// starts the updating time line
		runTimeLine();

		// show the scene
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * The main window time line. 60 times per second updates all drawn objects
	 * and adjusts the view.
	 */
	private void runTimeLine() {
		KeyFrame drawObjects = new KeyFrame(Duration.seconds(1.0 / 60), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				// follow a planet
				if (selectedPlanet != null) {
					double scale = Main.simulation.getScale();
					Vec2D pos = selectedPlanet.getPos();
					dx = scale * -pos.x();
					dy = scale * pos.y();
					updateOrbits();
				}

				// update all drawn objects
				for (Planet planet : Main.simulation.getPlanets())
					planet.updateObjects();

				// draw orbits
				if (!Main.simulation.isPaused() && orbits) {
					orbitGroup.getChildren().clear();
					for (Planet planet : Main.simulation.getPlanets())
						for (Line line : planet.getOrbitLineList())
							orbitGroup.getChildren().add(line);
				}

				// update info
				infoGroup.updateInfo();

				// return to the center after a reset of the window
				if (stopResetTimeline && zoom == 1 && dx == 0 && dy == 0) {
					resetTimeline.stop();
					stopResetTimeline = false;
				}
			}
		});

		Timeline timeline = new Timeline(drawObjects);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	/**
	 * Handles keyboard and mouse actions.
	 * 
	 * @param scene
	 */
	private void setSceneEvents(final Scene scene) {

		/*
		 * keyboard input
		 */
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				KeyCode key = event.getCode();

				// restart and exit
				if (key == KeyCode.R)
					Main.restart();
				if (key == KeyCode.ESCAPE)
					Platform.exit();

				// View
				if (key == KeyCode.E)
					resetView();

				// time
				if (key == KeyCode.PERIOD)
					Main.simulation.multTime(2);
				if (key == KeyCode.COMMA)
					Main.simulation.multTime(0.5);
				if (key == KeyCode.MINUS)
					Main.simulation.resetTime();
				if (key == KeyCode.SPACE)
					Main.simulation.setPause(!Main.simulation.isPaused());

				// visibility
				if (key == KeyCode.O)
					changeOrbitVisibility();
				if (key == KeyCode.L)
					changeLabelVisibility();
				if (key == KeyCode.V)
					changeVectorVisibility();
				if (key == KeyCode.I)
					changeInfoVisibility();

				// orbit mode
				if (key == KeyCode.M)
					changeOrbitMode();

				// help window
				if (key == KeyCode.H)
					openHelpWindow();
			}
		});

		/*
		 * Zoom with the mouse wheel.
		 */
		scene.setOnScroll(new EventHandler<ScrollEvent>() {
			public void handle(ScrollEvent event) {

				if (event.getDeltaY() > 0)
					zoom *= 1.05; // zoom in
				else
					zoom /= 1.05; // zoom out

				updateOrbits();
				event.consume();
			}
		});

		/*
		 * Select planets with primary mouse button, reset view with middle
		 * mouse button and add new planet with right mouse button. Saves click
		 * position.
		 */
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				mouseX = event.getX();
				mouseY = event.getY();

				// select planet
				if (event.isPrimaryButtonDown()) {
					deselectPlanet();
					checkForSelectedPlanet(mouseX, mouseY);
				}

				// add new planet
				if (event.isSecondaryButtonDown()) {
					Planet newPlanet = nextPlacedPlanet.clone();

					newPlanet.setPos(transfromBack(new Vec2D(mouseX, mouseY)));

					if (!orbitMode || Main.simulation.getPlanets().length == 0) {
						newPlanet.setVel(0, 0);
					} else {
						Planet biggest = Utils.getBiggest(Main.simulation.getPlanets());
						newPlanet.setVel(Utils.orbVel(biggest, newPlanet.getPos()));
					}

					Main.simulation.addNewPlanet(newPlanet);
				}

				// reset view
				if (event.isMiddleButtonDown())
					resetView();

				event.consume();
			}
		});

		/*
		 * Translates the view if the mouse is dragged with the primary button
		 */
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()) {
					tempdx = (event.getX() - mouseX) / zoom;
					tempdy = (event.getY() - mouseY) / zoom;
					updateOrbits();
				}
			}
		});

		/*
		 * When the mouse is released the current position is saved
		 */
		scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				dx += tempdx;
				dy += tempdy;
				tempdx = tempdy = 0;
			}
		});
	}

	/** Sets the view to default values. */
	public void reset() {
		zoom = 1;
		dx = dy = tempdx = tempdy = 0;
		deselectPlanet();
		updatePlanets();
	}

	/** updates all planet objects (circles, vectors, labels) */
	public void updatePlanets() {
		planetGroup.getChildren().clear();
		for (Planet p : Main.simulation.getPlanets())
			planetGroup.getChildren().addAll(p.getCircle(), p.getVelocityLine(), p.getLabel());

	}

	Timeline resetTimeline;
	boolean stopResetTimeline = false;

	/**
	 * Starts a time line to reset scale and translation of the view. The time
	 * line is stopped in the main window time line if the view is reseted.
	 * Deselects the selected planet.
	 */
	protected void resetView() {
		deselectPlanet();

		KeyFrame returnToCenter = new KeyFrame(Duration.seconds(1.0 / 60), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				// reset zoom
				if (Math.abs(zoom - 1.0) < 0.03)
					zoom = 1;
				else {
					if (zoom < 1)
						zoom *= 1.05;
					else
						zoom /= 1.05;
				}

				// reset translation
				if (Math.abs(dx) < 1.0)
					dx = 0;
				else
					dx /= 1.1;

				if (Math.abs(dy) < 1.0)
					dy = 0;
				else
					dy /= 1.1;

				updateOrbits();
			}
		});

		resetTimeline = new Timeline(returnToCenter);
		resetTimeline.setCycleCount(Animation.INDEFINITE);
		resetTimeline.play();

		stopResetTimeline = true;
	}

	/**
	 * Checks if a planet is clicked. Automatically deselects the previously
	 * selected planet
	 * 
	 * If a planet is clicked, highlight it and set the variables to follow it.
	 */
	private void checkForSelectedPlanet(double mouseX, double mouseY) {
		Circle circle;
		Vec2D mouse, center;
		for (Planet planet : Main.simulation.getPlanets()) {
			circle = planet.getCircle();
			center = new Vec2D(circle.getCenterX(), circle.getCenterY());
			mouse = new Vec2D(mouseX, mouseY);
			if (mouse.sub(center).norm() < circle.getRadius() + 5) {
				selectPlanet(planet);
			}
		}
	}

	/**
	 * Selects a planet and updates the info label
	 * 
	 * @param planet
	 */
	public void selectPlanet(Planet planet) {
		deselectPlanet();
		selectedPlanet = planet;
		planet.select(true);
	}

	/** Deselects the selected planet and updates the info label. */
	public void deselectPlanet() {
		if (selectedPlanet != null)
			selectedPlanet.select(false);
		selectedPlanet = null;
	}

	/**
	 * Translates all Orbits to the new right position if the orbits are
	 * visible.
	 */
	private void updateOrbits() {
		if (orbits)
			for (Planet planet : Main.simulation.getPlanets())
				planet.updateOrbit();
	}

	/**
	 * Changes the visibility of the orbit for all planets and updates the check
	 * menu item.
	 * 
	 * If the orbits are turned off, delete the orbit for all planets and clear
	 * the orbit group. If the orbits are turned on, set the first position of
	 * the orbit for all planets.
	 */
	protected void changeOrbitVisibility() {
		if (orbits) {
			orbits = false;
			orbitGroup.getChildren().clear();
			for (Planet p : Main.simulation.getPlanets())
				p.deleteOrbit();
		} else {
			orbits = true;
			for (Planet p : Main.simulation.getPlanets())
				p.savePosition();
		}
		menuBar.updateCMIs();
	}

	/**
	 * Changes the visibility of the label for all planets and updates the check
	 * menu item.
	 */
	protected void changeLabelVisibility() {
		labels = !labels;
		for (Planet p : Main.simulation.getPlanets())
			p.setLabelVisibility(labels);
		menuBar.updateCMIs();
	}

	/**
	 * Changes the visibility of the velocity vector for all planets and updates
	 * the check menu item.
	 */
	protected void changeVectorVisibility() {
		vectors = !vectors;
		for (Planet p : Main.simulation.getPlanets())
			p.setVelocityLineVisibility(vectors);
		menuBar.updateCMIs();
	}

	/**
	 * Changes the visibility of the information group and updates the check
	 * menu item.
	 */
	protected void changeInfoVisibility() {
		infoGroup.setVisible(!infoGroup.isVisible());
		menuBar.updateCMIs();
	}

	protected void changeOrbitMode() {
		orbitMode = !orbitMode;
		infoGroup.setOrbitMode(orbitMode);
		menuBar.updateCMIs();
	}

	/**
	 * returns the currently selected planet
	 * 
	 * @return the selected planet
	 */
	public Planet getSelectedPlanet() {
		return selectedPlanet;
	}

	/**
	 * Transforms a vector from planet coordinates to screen coordinates.
	 * 
	 * @param vector
	 * @return the transformed vector
	 */
	public Vec2D transfromBack(Vec2D vector) {
		double x = ((vector.x() - getWidth() / 2) / zoom - dx - tempdx) / Main.simulation.getScale();
		double y = -((vector.y() - getHeight() / 2) / zoom - dy - tempdy) / Main.simulation.getScale();
		return new Vec2D(x, y);
	}

	/**
	 * Transforms a vector from planet coordinates to screen coordinates.
	 * 
	 * @param vector
	 * @return the transformed vector
	 */
	public Vec2D transform(Vec2D vector) {
		double x = zoom * (Main.simulation.getScale() * vector.x() + dx + tempdx) + getWidth() / 2.0;
		double y = zoom * (Main.simulation.getScale() * -vector.y() + dy + tempdy) + getHeight() / 2.0;
		return new Vec2D(x, y);
	}

	/** opens the help window */
	protected void openHelpWindow() {
		Stage stage = new Stage();
		HelpWindow hw = new HelpWindow();
		try {
			hw.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double getWidth() {
		return scene.getWidth();
	}

	public double getHeight() {
		return scene.getHeight();
	}

	public double getZoom() {
		return zoom;
	}

	public boolean isOrbits() {
		return orbits;
	}

	public boolean isLabels() {
		return labels;
	}

	public boolean isVectors() {
		return vectors;
	}

}
