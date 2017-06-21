package window;

import constellations.StartConditions;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import simulation.Main;
import simulation.Planet;
import utils.Utils;
import utils.Vec2D;
import window.menuBar.CustomMenuBar;

/**
 * The main window of the application that handles all drawn objects.
 * 
 * (orbit translate only when needed)
 * 
 * @author Jan Muskalla
 *
 */
public class Window extends Application {

	// flags for trails label and vectors
	private boolean trails, labels, vectors;

	// zoom and translation
	private double zoom;
	private double dx, dy;
	private double tempdx, tempdy;
	private double mouseX, mouseY;
	
	// mouse position
	Vec2D mousePos;

	// the selected planet
	private Planet selectedPlanet;

	// the next planet that will be placed
	private Planet nextAddedPlanet;

	// in orbit mode all planets get placed in orbits
	private boolean orbitMode;

	// true if window size was changed
	private boolean winWasChanged;

	// the scene of the window
	private Scene scene;
	// group for all trails
	private Pane trailPane;
	// group for all circles, vectors, labels
	private Pane planetPane;
	// group for all information
	private InfoPane infoPane;
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
		// primaryStage.setMaximized(true);

		// set scene
		Group root = new Group();
		scene = new Scene(root, 1200, 700, Color.LIGHTBLUE);
		setSceneEvents(scene);
		primaryStage.setScene(scene);

		// add everything to root
		trailPane = new Pane();
		planetPane = new Pane();
		infoPane = new InfoPane();
		menuBar = new CustomMenuBar(primaryStage, this);
		root.getChildren().addAll(trailPane, planetPane, infoPane, menuBar);

		// initialize all values to default and load the planet objects
		reset();

		nextAddedPlanet = StartConditions.moon.clone();
		trails = true;
		labels = true;
		vectors = false;
		infoPane.setVisible(true);
		orbitMode = true;
		infoPane.setOrbitMode(orbitMode);
		mousePos = new Vec2D();
		winWasChanged = true;
		
		menuBar.updateCMIs();

		// starts the updating time line
		runTimeLine();

		// listen for window size changes
		ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> winWasChanged = true;
		ChangeListener<? super Boolean> stageMaxListener = (observable, oldValue, newValue) -> winWasChanged = true;
		primaryStage.widthProperty().addListener(stageSizeListener);
		primaryStage.heightProperty().addListener(stageSizeListener);
		primaryStage.maximizedProperty().addListener(stageMaxListener);

		// show the scene
		
		primaryStage.show();
	}

	/**
	 * The main window time line. 60 times per second updates all drawn objects
	 * and adjusts the view.
	 */
	private void runTimeLine() {
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.0 / 60), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				// transform trails if window size has changed
				if (winWasChanged) {
					updateTrails();
					infoPane.relocateTimeButtons();
					winWasChanged = false;
				}

				// follow a planet
				if (selectedPlanet != null) {
					double scale = Main.sim.getScale();
					Vec2D pos = selectedPlanet.getPos();
					dx = scale * -pos.getX();
					dy = scale * pos.getY();
					updateTrails();
				}

				// update all drawn objects
				for (Planet planet : Main.sim.getPlanets())
					planet.updateObjects();

				// update info
				infoPane.updateInfo();

				// return to the center after a reset of the window
				if (stopResetTimeline && zoom == 1 && dx == 0 && dy == 0) {
					resetTimeline.stop();
					stopResetTimeline = false;
				}
			}
		}));
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
					Main.sim.multTime(2);
				if (key == KeyCode.COMMA)
					Main.sim.multTime(0.5);
				if (key == KeyCode.MINUS)
					Main.sim.resetTime();
				if (key == KeyCode.SPACE)
					Main.sim.setPause(!Main.sim.isPaused());

				// visibility
				if (key == KeyCode.T)
					changeTrailsVisibility();
				if (key == KeyCode.L)
					changeLabelsVisibility();
				if (key == KeyCode.V)
					changeVectorsVisibility();
				if (key == KeyCode.I)
					changeInfoVisibility();

				// adding
				if (key == KeyCode.A)
					addNextPlanet();
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
					zoom *= 1.1; // zoom in
				else
					zoom /= 1.1; // zoom out
				updateTrails();
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
				if (event.isPrimaryButtonDown())
					checkForSelectedPlanet(mouseX, mouseY);

				// reset view
				if (event.isMiddleButtonDown())
					resetView();

				// add new planet
				if (event.isSecondaryButtonDown()) {
					addNextPlanet();
				}

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
					updateTrails();
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
		
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				mousePos.set(event.getX(), event.getY());
			}
		});
	}

	/** Sets the view to default values. */
	public void reset() {
		zoom = 1;
		dx = dy = tempdx = tempdy = 0;
		trailPane.getChildren().clear();
		deselectPlanet();
		updatePlanets(Main.sim.getPlanets());
	}

	/** updates all planet objects (circles, vectors, labels) */
	public void updatePlanets(Planet[] planets) {
		planetPane.getChildren().clear();
		for (Planet p : planets)
			planetPane.getChildren().addAll(p.getVelocityLine(), p.getCircle(), p.getLabel());

	}

	private Timeline resetTimeline;
	private boolean stopResetTimeline = false;

	/**
	 * Starts a time line to reset scale and translation of the view. The time
	 * line is stopped in the main window time line if the view is reseted.
	 * Deselects the selected planet.
	 */
	public void resetView() {
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

				updateTrails();
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
		deselectPlanet();
		Circle circle;
		Vec2D mouse, center;
		for (Planet planet : Main.sim.getPlanets()) {
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
		planet.select();
	}

	/** Deselects the selected planet and updates the info label. */
	public void deselectPlanet() {
		if (selectedPlanet != null)
			selectedPlanet.deselect();
		selectedPlanet = null;
	}
	
	/**
	 * does not work
	 */
	private void addNextPlanet() {
		Planet newPlanet = nextAddedPlanet.clone();

		// position
		newPlanet.setPos(transfromBack(mousePos));

		// velocity
		if (!orbitMode || Main.sim.getPlanets().length == 0) {
			newPlanet.setVel(0, 0);
		} else {
			Planet biggest = Utils.getBiggestInView(Main.win, Main.sim.getPlanets());
			newPlanet.setVel(Utils.orbVel(biggest, newPlanet.getPos()));
		}

		// add
		Main.sim.addNewPlanet(newPlanet);
	}

	/**
	 * Translates all Orbits to the new right position if the orbits are
	 * visible.
	 */
	private void updateTrails() {
		if (trails)
			for (Planet planet : Main.sim.getPlanets())
				planet.updateTrail();
	}

	/**
	 * Changes the visibility of the orbit for all planets and updates the check
	 * menu item.
	 * 
	 * If the orbits are turned off, delete the orbit for all planets and clear
	 * the orbit group. If the orbits are turned on, set the first position of
	 * the orbit for all planets.
	 */
	public void changeTrailsVisibility() {
		if (trails) {
			trails = false;
			for (Planet p : Main.sim.getPlanets())
				p.deleteTrail();
		} else {
			trails = true;
			for (Planet p : Main.sim.getPlanets())
				p.savePosition();
		}
		menuBar.updateCMIs();
	}

	/**
	 * Changes the visibility of the label for all planets and updates the check
	 * menu item.
	 */
	public void changeLabelsVisibility() {
		labels = !labels;
		for (Planet p : Main.sim.getPlanets())
			p.setLabelVisibility(labels);
		menuBar.updateCMIs();
	}

	/**
	 * Changes the visibility of the velocity vector for all planets and updates
	 * the check menu item.
	 */
	public void changeVectorsVisibility() {
		vectors = !vectors;
		for (Planet p : Main.sim.getPlanets())
			p.setVelocityLineVisibility(vectors);
		menuBar.updateCMIs();
	}

	/**
	 * Changes the visibility of the information group and updates the check
	 * menu item.
	 */
	public void changeInfoVisibility() {
		infoPane.setVisible(!infoPane.isVisible());
		menuBar.updateCMIs();
	}

	public void changeOrbitMode() {
		orbitMode = !orbitMode;
		infoPane.setOrbitMode(orbitMode);
		menuBar.updateCMIs();
	}

	/** opens the help window */
	public void openHelpWindow() {
		Stage stage = new Stage();
		HelpWindow hw = new HelpWindow();
		try {
			hw.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Transforms a vector from planet coordinates to screen coordinates.
	 * 
	 * @param vector
	 * @return the transformed vector
	 */
	public Vec2D transfromBack(Vec2D vector) {
		double x = ((vector.getX() - getX() / 2) / zoom - dx - tempdx) / Main.sim.getScale();
		double y = -((vector.getY() - getY() / 2) / zoom - dy - tempdy) / Main.sim.getScale();
		return new Vec2D(x, y);
	}

	/**
	 * Transforms a vector from planet coordinates to screen coordinates.
	 * 
	 * @param vector
	 * @return the transformed vector
	 */
	public Vec2D transform(Vec2D vector) {
		double x = zoom * (Main.sim.getScale() * vector.getX() + dx + tempdx) + getX() / 2.0;
		double y = zoom * (Main.sim.getScale() * -vector.getY() + dy + tempdy) + getY() / 2.0;
		return new Vec2D(x, y);
	}

	/** returns the width of the window */
	public double getX() {
		return scene.getWidth();
	}

	/** returns the height of the window */
	public double getY() {
		return scene.getHeight();
	}

	public double getZoom() {
		return zoom;
	}

	public Planet getSelectedPlanet() {
		return selectedPlanet;
	}

	public boolean isTrails() {
		return trails;
	}

	public boolean isLabels() {
		return labels;
	}

	public boolean isVectors() {
		return vectors;
	}

	public void addTrail(Line line) {
		trailPane.getChildren().add(line);
	}

	public void setNextAddedPlanet(Planet p) {
		this.nextAddedPlanet = p;
	}

	public boolean isOrbitMode() {
		return orbitMode;
	}

	public boolean isInfoVisible() {
		return infoPane.isVisible();
	}

}
