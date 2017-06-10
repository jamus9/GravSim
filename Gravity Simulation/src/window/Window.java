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
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
	public static int winX = 1200;
	public static int winY = 700;

	// zoom and translation
	public static double zoom;
	public static double dx, dy;

	// flags for orbits label and vectors
	public static boolean orbits, labels, vectors;

	// the number of the selected planet
	// -1 if no planet is selected
	public static int selectedPlanet;

	// coordinates for mouse dragging
	public static double mouseX, mouseY;
	public static double tempdx, tempdy;

	// sub group of root for all orbits
	private Group orbitGroup = new Group();
	// sub group of root for all circles, vectors, labels
	private Group planetGroup = new Group();
	// sub group of root for all info an the screen
	private Group infoGroup = new Group();

	// info label
	private Label info = new Label();

	// CheckMenuItems in Options Menu
	private CheckMenuItem infoCMI = new CheckMenuItem("Information");
	private CheckMenuItem orbitsCMI = new CheckMenuItem("Orbits");
	private CheckMenuItem labelsCMI = new CheckMenuItem("Namen");
	private CheckMenuItem vectorsCMI = new CheckMenuItem("Vektoren");

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
		Scene scene = new Scene(root, winX, winY, Color.LIGHTBLUE);

		// add sub groups
		root.getChildren().addAll(orbitGroup, planetGroup, infoGroup);

		// seconds label
		Label seconds = new Label();
		seconds.relocate(0, 40);

		// steps per second label
		Label sps = new Label();
		sps.relocate(3, 25);

		// info label
		info.relocate(3, 70);
		infoGroup.getChildren().addAll(info, seconds, sps);

		// menu bar
		MenuBar menuBar = new MenuBar();
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		root.getChildren().add(menuBar);

		/**
		 * simulation menu
		 */
		Menu simulation = new Menu("Simulation");

		MenuItem restart = new MenuItem("Neustart");
		restart.setOnAction(actionEvent -> restart(Simulation.constellation));

		MenuItem resetView = new MenuItem("Ansicht zurücksetzen");
		resetView.setOnAction(actionEven -> resetView());

		MenuItem exit = new MenuItem("Beenden");
		exit.setOnAction(ActionEvent -> Platform.exit());

		simulation.getItems().addAll(restart, resetView, exit);

		/**
		 * settings menu
		 */
		Menu settings = new Menu("Einstellungen");

		orbitsCMI.setOnAction(actionEvent -> changeOrbitVisibility());
		labelsCMI.setOnAction(actionEvent -> changeLabelVisibility());
		infoCMI.setOnAction(ActionEvent -> changeInfoVisibility());
		vectorsCMI.setOnAction(actionEvent -> changeVectorVisibility());

		settings.getItems().addAll(orbitsCMI, labelsCMI, infoCMI, vectorsCMI);

		/**
		 * constellation loading menu
		 */
		Menu load = new Menu("Lade");

		MenuItem solarSystemItem = new MenuItem(StartConditions.solarSystem.getName());
		solarSystemItem.setOnAction(actionEvent -> restart(StartConditions.solarSystem));

		MenuItem earthMoonItem = new MenuItem(StartConditions.earthSystem.getName());
		earthMoonItem.setOnAction(actionEvent -> restart(StartConditions.earthSystem));

		MenuItem marsItem = new MenuItem(StartConditions.marsSystem.getName());
		marsItem.setOnAction(actionEvent -> restart(StartConditions.marsSystem));

		MenuItem jupiterFlybyItem = new MenuItem(StartConditions.jupiterFlyby.getName());
		jupiterFlybyItem.setOnAction(actionEvent -> restart(StartConditions.jupiterFlyby));

		MenuItem earthSunLowItem = new MenuItem(StartConditions.earthSunLow.getName());
		earthSunLowItem.setOnAction(actionEvent -> restart(StartConditions.earthSunLow));

		MenuItem symItem = new MenuItem(StartConditions.symmetrical.getName());
		symItem.setOnAction(actionEvent -> restart(StartConditions.symmetrical));

		load.getItems().addAll(solarSystemItem, earthMoonItem, marsItem, jupiterFlybyItem, earthSunLowItem, symItem);

		// add all menus to the menu bar
		menuBar.getMenus().addAll(simulation, load, settings);

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
					translateOrbits();
				}

				// follow a planet
				if (selectedPlanet != -1) {
					dx = Simulation.scale * -Simulation.planets[selectedPlanet].getPos().x();
					dy = Simulation.scale * Simulation.planets[selectedPlanet].getPos().y();
					translateOrbits();
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

				// restart
				if (key == KeyCode.R)
					restart(Simulation.constellation);

				// time
				if (key == KeyCode.PERIOD)
					setTimeStep(Simulation.time * 2.0);
				if (key == KeyCode.COMMA)
					setTimeStep(Simulation.time * 0.5);
				if (key == KeyCode.MINUS)
					setTimeStep(Simulation.constellation.getTime());
				if (key == KeyCode.SPACE || key == KeyCode.P)
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

				// zoom out
				if (event.getDeltaY() < 0)
					zoom /= 1.05;

				translateOrbits();

				event.consume();
			}
		});

		/**
		 * Select planets with primary mouse button and reset view with
		 * secondary mouse button. Saves click position.
		 */
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				mouseX = event.getX();
				mouseY = event.getY();

				if (event.isPrimaryButtonDown())
					checkForSelectedPlanet();

				if (event.isSecondaryButtonDown())
					resetView();

				event.consume();
			}
		});

		/**
		 * Translates the view if the mouse is dragged.
		 */
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {

				tempdx = (event.getX() - mouseX) / zoom;
				tempdy = (event.getY() - mouseY) / zoom;

				translateOrbits();
			}
		});

		/**
		 * When the mouse is released the current position is saved in dx dy and
		 * tempdx tempdy is reset
		 */
		scene.setOnMouseReleased(MouseEvent -> {
			dx += tempdx;
			dy += tempdy;
			tempdx = tempdy = 0;
		});
	}

	/**
	 * Restarts the Simulation with a given constellation and sets all variables
	 * to default values
	 * 
	 * @param constellation
	 *            the constellation of the simulation
	 */
	private void restart(Constellation constellation) {
		zoom = 1;
		dx = dy = 0;
		tempdx = tempdy = 0;
		selectedPlanet = -1;

		orbits = true;
		orbitsCMI.setSelected(orbits);

		labels = true;
		labelsCMI.setSelected(labels);

		vectors = false;
		vectorsCMI.setSelected(vectors);

		infoGroup.setVisible(true);
		infoCMI.setSelected(true);

		Simulation.loadConstellation(constellation);
		
		// delete all circles, vectors and labels
		planetGroup.getChildren().clear();

		// add everything again
		for (Planet p : Simulation.planets)
			planetGroup.getChildren().addAll(p.getCircle(), p.getVelocityLine(), p.getLabel());

		updateInfoLabel();
	}

	Timeline resetTimeline;
	boolean stopResetTimeline = false;

	/**
	 * Starts a time line to reset scale and translation of the view. The time
	 * line is stopped in the main window time line if the view is reseted.
	 * Deselects the selected planet
	 */
	private void resetView() {

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
				translateOrbits();
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
	private void checkForSelectedPlanet() {
		deselectPlanet();

		Circle circle;
		Vec2D mouse, center;

		for (int i = 0; i < Simulation.planets.length; i++) {
			circle = Simulation.planets[i].getCircle();
			center = new Vec2D(circle.getCenterX(), circle.getCenterY());
			mouse = new Vec2D(mouseX, mouseY);

			if (mouse.sub(center).norm() < circle.getRadius() + 5)
				selectPlanet(i);
		}
	}

	/**
	 * Selects a planet and updates the info label
	 * 
	 * @param i
	 */
	public void selectPlanet(int i) {
		selectedPlanet = i;
		Simulation.planets[i].select(true);
		updateInfoLabel();
	}

	/**
	 * Deselects the selected planet and updates the info label.
	 */
	public void deselectPlanet() {
		if (selectedPlanet != -1) {
			Simulation.planets[selectedPlanet].select(false);
		}
		selectedPlanet = -1;
		updateInfoLabel();
	}

	/**
	 * Translates all Orbits to the new right position if the orbits are
	 * visible.
	 */
	private void translateOrbits() {
		if (orbits)
			for (Planet planet : Simulation.planets)
				planet.translateOrbit();
	}

	/**
	 * Sets the time step for the simulation.
	 * 
	 * @param time
	 *            the new time step
	 */
	private void setTimeStep(double time) {
		Simulation.time = time;
		updateInfoLabel();
	}

	/**
	 * Updates the displayed information about the system or planet.
	 */
	private void updateInfoLabel() {
		if (selectedPlanet != -1) {
			Planet p = Simulation.planets[selectedPlanet];
			info.setText(p.getName() + "\nMasse: " + p.getMass() + " kg\nRadius: " + (int) p.getRadius() / 1000
					+ " km\nGeschwindigkeit: " + (int) p.getVel().norm() + " m/s" + "\nZeit: x"
					+ (int) (Simulation.time * Simulation.SPS));
		} else {
			info.setText(Simulation.constellation.getName() + "\nObjekte: " + Simulation.planets.length + "\nZeit: x"
					+ (int) (Simulation.time * Simulation.SPS));
		}
	}

	/**
	 * Updates the check menu items in the menu "settings".
	 */
	private void updateCMIs() {
		orbitsCMI.setSelected(orbits);
		labelsCMI.setSelected(labels);
		vectorsCMI.setSelected(vectors);
		infoCMI.setSelected(infoGroup.isVisible());
	}

	/**
	 * Changes the visibility of the orbit for all planets and updates the check
	 * menu item.
	 * 
	 * If the orbits are turned off, delete the orbit for all planets and clear
	 * the orbit group. If the orbits are turned on, set the first position of
	 * the orbit for all planets.
	 */
	private void changeOrbitVisibility() {
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
		updateCMIs();
	}

	/**
	 * Changes the visibility of the label for all planets and updates the check
	 * menu item.
	 */
	private void changeLabelVisibility() {
		labels = !labels;
		for (Planet p : Simulation.planets)
			p.setLabelVisibility(labels);
		updateCMIs();
	}

	/**
	 * Changes the visibility of the velocity vector for all planets and updates
	 * the check menu item.
	 */
	private void changeVectorVisibility() {
		vectors = !vectors;
		for (Planet p : Simulation.planets)
			p.setVelocityLineVisibility(vectors);
		updateCMIs();
	}

	/**
	 * Changes the visibility of the information group and updates the check
	 * menu item.
	 */
	private void changeInfoVisibility() {
		infoGroup.setVisible(!infoGroup.isVisible());
		updateCMIs();
	}
	
	/**
	 * updates all circles, vectors and labels
	 */
	public void updatePlanets() {
		planetGroup.getChildren().clear();
		for (Planet p : Simulation.planets)
			planetGroup.getChildren().addAll(p.getCircle(), p.getVelocityLine(), p.getLabel());
	}

}
