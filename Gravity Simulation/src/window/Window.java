package window;

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
import simulation.Main;
import simulation.Planet;
import simulation.Simulation;
import utils.Utils;
import utils.Vec2D;

/**
 * The main window of the application that handles all drawn objects.
 * 
 * (info update and orbit translate only when needed)
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

	// coordinates for mouse dragging
	public double tempdx, tempdy;
	private double mouseX, mouseY;

	// the selected planet
	private Planet selectedPlanet;

	// the next planet that will be placed
	protected Planet nextPlacedPlanet;

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
	 * Starts the window for the simulation.
	 */
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Gravity Simulation");
		primaryStage.setMaximized(true);
		
		Group root = new Group();
		root.getChildren().addAll(orbitGroup, planetGroup, infoGroup);
		Scene scene = new Scene(root, winX, winY, Color.LIGHTBLUE);

		/**
		 * info group
		 */
		Label spsLabel = new Label();
		spsLabel.relocate(3, 25);
		Label pastTimeLabel = new Label();
		pastTimeLabel.relocate(0, 40);

		infoLabel.relocate(3, 70);
		infoGroup.getChildren().addAll(spsLabel, pastTimeLabel, infoLabel);

		/**
		 * add the menu bar
		 */
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		root.getChildren().add(menuBar);

		/**
		 * initialize all values to default and load the planet objects
		 */
		setToDefault();
		updatePlanets();

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
					dx = Main.simulation.getScale() * -selectedPlanet.getPos().x();
					dy = Main.simulation.getScale() * selectedPlanet.getPos().y();
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

				// update counter
				if (!Main.simulation.isPaused()) {
					spsLabel.setText("Steps/Sec: " + (Main.simulation.getSpsCounter() * 60));
					Main.simulation.resetSpsCounter();
					pastTimeLabel.setText(Utils.pastTime());
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
					Main.restart();
				if (key == KeyCode.ESCAPE)
					Platform.exit();

				// View
				if (key == KeyCode.E)
					resetView();

				// time
				if (key == KeyCode.PERIOD) {
					Main.simulation.multTime(2);
					updateInfoLabel();
				}
				if (key == KeyCode.COMMA) {
					Main.simulation.multTime(0.5);
					updateInfoLabel();
				}
				if (key == KeyCode.MINUS) {
					Main.simulation.resetTime();
					updateInfoLabel();
				}
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
			}
		});

		/**
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
					Planet newPlanet = nextPlacedPlanet.clone();

					newPlanet.setPos(Utils.transfromBack(new Vec2D(mouseX, mouseY)));
					newPlanet.setVel(0, 0);

					Main.simulation.addNewPlanet(newPlanet);
				}

				// reset view
				if (event.isMiddleButtonDown())
					resetView();

				event.consume();
			}
		});

		/**
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

		/**
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

	/**
	 * Resets the window to default values
	 */
	public void setToDefault() {
		zoom = 1;
		dx = dy = tempdx = tempdy = 0;
		selectedPlanet = null;
		nextPlacedPlanet = StartConditions.moon.clone();
		menuBar.resetAddMenu();

		orbits = true;
		labels = true;
		vectors = false;
		infoGroup.setVisible(true);

		menuBar.updateCMIs();
	}

	/**
	 * updates all planet objects (circles, vectors, labels)
	 */
	public void updatePlanets() {
		planetGroup.getChildren().clear();
		for (Planet p : Main.simulation.getPlanets()) {
			planetGroup.getChildren().addAll(p.getCircle(), p.getVelocityLine(), p.getLabel());
		}
		updateInfoLabel();
	}

	Timeline resetTimeline;
	boolean stopResetTimeline = false;

	/**
	 * Starts a time line to reset scale and translation of the view. The time
	 * line is stopped in the main window time line if the view is reseted.
	 * Deselects the selected planet.
	 */
	protected void resetView() {
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
	 * Updates the displayed information about the system or planet.
	 */
	private void updateInfoLabel() {
		if (selectedPlanet != null) {
			infoLabel.setText(selectedPlanet.getName() + "\nMasse: " + selectedPlanet.getMass() + " kg\nRadius: "
					+ (int) selectedPlanet.getRadius() / 1000 + " km\nGeschwindigkeit: "
					+ (int) selectedPlanet.getVel().norm() + " m/s" + "\nZeit: x"
					+ (int) (Main.simulation.getTime() * Simulation.SPS));
		} else {
			infoLabel.setText(
					Main.simulation.getConstellation().getName() + "\nObjekte: " + Main.simulation.getPlanets().length
							+ "\nZeit: x" + (int) (Main.simulation.getTime() * Simulation.SPS));
		}
	}

	/**
	 * Translates all Orbits to the new right position if the orbits are
	 * visible.
	 */
	private void updateOrbits() {
		if (orbits) {
			for (Planet planet : Main.simulation.getPlanets()) {
				planet.updateOrbit();
			}
		}
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

	/**
	 * returns the currently selected planet
	 * 
	 * @return the selected planet
	 */
	public Planet getSelectedPlanet() {
		return selectedPlanet;
	}

}
