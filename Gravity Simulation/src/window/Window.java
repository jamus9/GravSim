package window;

import constellations.Constellation;
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
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import simulation.Planet;
import simulation.Simulation;
import utils.Vec2D;

/**
 * The main window of the application that handles all drawn objects.
 * 
 * default values in restart()
 * 
 * info and orbittranslate only when needed
 * 
 * @author Jan Muskalla
 *
 */
public class Window extends Application {

	// window size in pixel
	public int winX = 1200;
	public int winY = 700;

	// flags for orbits label and vectors
	public boolean orbits, labels, vectors;

	// zoom and translation
	public double zoom;
	public double dx, dy;

	// the selected planet
	public Planet selectedPlanet;

	// the next planet that will be placed
	protected Planet nextPlanet;

	// coordinates for mouse dragging
	public double mouseX, mouseY;
	public double tempdx, tempdy;

	// sub group of root for all orbits
	private Group orbitGroup = new Group();
	// sub group of root for all circles, vectors, labels
	private Group planetGroup = new Group();
	// sub group of root for all info an the screen
	protected Group infoGroup = new Group();

	// info label
	private Label infoLabel = new Label();

	// the menu bar
	private MainWindowMenuBar menuBar = new MainWindowMenuBar();

	/**
	 * launches the javafx application
	 */
	public static void main(String[] args) {
		Simulation.run();
		Application.launch(args);
	}

	/**
	 * Starts the window for the simulation.
	 */
	public void start(Stage primaryStage) {
		Simulation.window = this;
		primaryStage.setTitle("Gravity Simulation");
		// primaryStage.setMaximized(true);

		// main group
		Group root = new Group();
		root.getChildren().addAll(orbitGroup, planetGroup, infoGroup);
		Scene scene = new Scene(root, winX, winY, Color.LIGHTBLUE);

		/**
		 * info group
		 */
		Label seconds = new Label();
		seconds.relocate(0, 40);

		Label sps = new Label();
		sps.relocate(3, 25);

		infoLabel.relocate(3, 70);
		infoGroup.getChildren().addAll(infoLabel, seconds, sps);

		/**
		 * add the menu bar
		 */
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		root.getChildren().add(menuBar);

		/**
		 * starts the simulation and initialize all values to default
		 */
		restart(Simulation.constellation);

		/**
		 * The main window time line. 60 times per second updates all drawn
		 * objects and adjusts the view.
		 */
		KeyFrame drawObjects = new KeyFrame(Duration.seconds(1.0 / 60), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				// change center of the screen after resizing the window
				if (winX != (int) scene.getWidth() || winY != (int) scene.getHeight()) {
					winX = (int) scene.getWidth();
					winY = (int) scene.getHeight();
					updateOrbits();
				}

				// follow a planet
				if (selectedPlanet != null) {
					dx = Simulation.scale * -selectedPlanet.getPos().x();
					dy = Simulation.scale * selectedPlanet.getPos().y();
					updateOrbits();
				}

				// update all drawn objects
				for (Planet planet : Simulation.planets)
					planet.updateObjects();

				// draw orbits
				if (!Simulation.pause && orbits) {
					orbitGroup.getChildren().clear();
					for (Planet planet : Simulation.planets)
						for (Line line : planet.getOrbitLineList())
							orbitGroup.getChildren().add(line);
				}

				// update counter
				if (!Simulation.pause) {
					sps.setText("Steps/Sec: " + (Simulation.SPScounter * 60));
					Simulation.SPScounter = 0;
					seconds.setText(Simulation.pastTime());
				}

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

		this.setSceneEvents(scene);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Handles keyboard and mouse actions.
	 * 
	 * @param scene
	 */
	private void setSceneEvents(final Scene scene) {

		/**
		 * keyboard input
		 */
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				KeyCode key = event.getCode();

				// restart and exit
				if (key == KeyCode.R)
					restart(Simulation.constellation);
				if (key == KeyCode.ESCAPE)
					Platform.exit();

				// reset
				if (key == KeyCode.E)
					resetView();

				// time
				if (key == KeyCode.PERIOD)
					setTimeStep(Simulation.time * 2.0);
				if (key == KeyCode.COMMA)
					setTimeStep(Simulation.time * 0.5);
				if (key == KeyCode.MINUS)
					setTimeStep(Simulation.constellation.getTime());
				if (key == KeyCode.SPACE)
					Simulation.pause = !Simulation.pause;

				// visibility
				if (key == KeyCode.O)
					changeOrbitVisibility();
				if (key == KeyCode.L)
					changeLabelVisibility();
				if (key == KeyCode.V)
					changeVectorVisibility();
				if (key == KeyCode.I)
					changeInfoVisibility();
			}
		});

		/**
		 * Zoom with the mouse wheel.
		 */
		scene.setOnScroll(new EventHandler<ScrollEvent>() {
			public void handle(ScrollEvent event) {

				// zoom in
				if (event.getDeltaY() > 0)
					zoom *= 1.05;
				else
					// zoom out
					zoom /= 1.05;

				updateOrbits();
				event.consume();
			}
		});

		/**
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
					Planet newPlanet = nextPlanet.clone();

					newPlanet.setPos(transfromBack(new Vec2D(mouseX, mouseY)));
					newPlanet.setVel(0, 0);

					Simulation.addNewPlanet(newPlanet);
				}

				// reset view
				if (event.isMiddleButtonDown())
					resetView();

				event.consume();
			}
		});

		/**
		 * Translates the view if the mouse is dragged.
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

		/**
		 * When the mouse is released the current position is saved in dx dy and
		 * tempdx tempdy is reset
		 */
		scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				dx += tempdx;
				dy += tempdy;
				tempdx = tempdy = 0;
			}
		});

	}

	/**
	 * Restarts the Simulation with a given constellation and sets all variables
	 * to default values
	 * 
	 * @param constellation
	 *            the constellation of the simulation
	 */
	protected void restart(Constellation constellation) {
		zoom = 1;
		dx = dy = 0;
		tempdx = tempdy = 0;
		selectedPlanet = null;
		nextPlanet = StartConditions.moon.clone();

		{
			orbits = true;
			menuBar.setOrbitsCMI(orbits);

			labels = true;
			menuBar.setLabelsCMI(labels);

			vectors = false;
			menuBar.setVectorsCMI(vectors);

			infoGroup.setVisible(true);
			menuBar.setInfoCMI(true);
		}

		Simulation.loadConstellation(constellation);

		updatePlanets();
	}

	Timeline resetTimeline;
	boolean stopResetTimeline = false;

	/**
	 * Starts a time line to reset scale and translation of the view. The time
	 * line is stopped in the main window time line if the view is reseted.
	 * Deselects the selected planet
	 */
	protected void resetView() {

		KeyFrame returnToCenter = new KeyFrame(Duration.seconds(1.0 / 60), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				// reset zoom
				if (Math.abs(zoom - 1.0) < 0.03)
					zoom = 1;
				else if (zoom < 1)
					zoom *= 1.05;
				else
					zoom /= 1.05;

				// reset translation
				if (Math.abs(dx) < 1.0)
					dx = 0;
				else
					dx /= 1.1;

				if (Math.abs(dy) < 1.0)
					dy = 0;
				else
					dy /= 1.1;

				// update orbits
				updateOrbits();
			}
		});

		resetTimeline = new Timeline(returnToCenter);
		resetTimeline.setCycleCount(Animation.INDEFINITE);
		resetTimeline.play();

		stopResetTimeline = true;

		deselectPlanet();
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
		for (Planet planet : Simulation.planets) {
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
		selectedPlanet = planet;
		planet.select(true);
		updateInfoLabel();
	}

	/**
	 * Deselects the selected planet and updates the info label.
	 */
	public void deselectPlanet() {
		if (selectedPlanet != null) {
			selectedPlanet.select(false);
		}
		selectedPlanet = null;
		updateInfoLabel();
	}

	/**
	 * updates all circles, vectors and labels
	 */
	public void updatePlanets() {
		planetGroup.getChildren().clear();
		for (Planet p : Simulation.planets) {
			planetGroup.getChildren().addAll(p.getCircle(), p.getVelocityLine(), p.getLabel());
		}
		updateInfoLabel();
	}

	/**
	 * Updates the displayed information about the system or planet.
	 */
	private void updateInfoLabel() {
		if (selectedPlanet != null) {
			infoLabel.setText(selectedPlanet.getName() + "\nMasse: " + selectedPlanet.getMass() + " kg\nRadius: "
					+ (int) selectedPlanet.getRadius() / 1000 + " km\nGeschwindigkeit: "
					+ (int) selectedPlanet.getVel().norm() + " m/s" + "\nZeit: x"
					+ (int) (Simulation.time * Simulation.SPS));
		} else {
			infoLabel.setText(Simulation.constellation.getName() + "\nObjekte: " + Simulation.planets.length
					+ "\nZeit: x" + (int) (Simulation.time * Simulation.SPS));
		}
	}

	/**
	 * Translates all Orbits to the new right position if the orbits are
	 * visible.
	 */
	private void updateOrbits() {
		if (orbits)
			for (Planet planet : Simulation.planets)
				planet.updateOrbit();
	}

	/**
	 * Sets the time step for the simulation.
	 * 
	 * @param time
	 *            the new time step
	 */
	protected void setTimeStep(double time) {
		Simulation.time = time;
		updateInfoLabel();
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
			for (Planet p : Simulation.planets)
				p.deleteOrbit();
		} else {
			orbits = true;
			for (Planet p : Simulation.planets)
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
		for (Planet p : Simulation.planets)
			p.setLabelVisibility(labels);
		menuBar.updateCMIs();
	}

	/**
	 * Changes the visibility of the velocity vector for all planets and updates
	 * the check menu item.
	 */
	protected void changeVectorVisibility() {
		vectors = !vectors;
		for (Planet p : Simulation.planets)
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

	private Vec2D transfromBack(Vec2D v) {
		double x = ((v.x() - winX / 2) / zoom - dx - tempdx) / Simulation.scale;
		double y = -((v.y() - winY / 2) / zoom - dy - tempdy) / Simulation.scale;
		return new Vec2D(x, y);
	}

}
