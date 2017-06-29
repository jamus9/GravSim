package window;

import bodies.Body;
import bodies.Particle;
import bodies.Planet;
import constellations.Planets;
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
import simulation.Simulation;
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

	/** flags for trails label and vectors */
	private boolean trails, labels, vectors;

	/** coordinates for zoom and translation */
	private double zoom;
	private double dx, dy, tempdx, tempdy;
	private double mouseX, mouseY;

	/** the current mouse position for adding planets */
	Vec2D mousePos;

	/** the selected planet */
	private Planet selectedPlanet;

	private boolean follow;

	/** the next planet that will be added */
	private Planet nextAddedPlanet;

	/** in orbit mode all planets get placed in orbits */
	private boolean orbitMode;

	/** true if window size was changed */
	private boolean winWasChanged;

	/** the scene of the window */
	private Scene scene;

	/** pane for all trails */
	private Pane trailPane;

	/** pane for all circles, vectors and labels */
	private Pane bodyPane;

	/** pane for all information */
	private InfoPane infoPane;

	/** the menu bar */
	private CustomMenuBar menuBar;

	/**
	 * creates a new window with and initialized local variables
	 */
	public Window() {
		trails = true;
		labels = true;
		vectors = false;

		follow = false;

		nextAddedPlanet = Planets.getMoon();
		orbitMode = true;

		mousePos = new Vec2D();
		winWasChanged = true;
	}

	/**
	 * Starts the window for the simulation.
	 * 
	 * @param primaryStage
	 */
	public void start(Stage primaryStage) {

		// scene and root
		Group root = new Group();
		// light blue 173,216,230
		// 3 tints down current
		scene = new Scene(root, 1200, 700, Color.rgb(213, 235, 242));
		setSceneEvents(scene);

		// stage
		primaryStage.setScene(scene);
		primaryStage.setTitle("Gravity Simulation");
//		 primaryStage.setMaximized(true);

		// add panes and menu to root
		trailPane = new Pane();

		bodyPane = new Pane();

		infoPane = new InfoPane();
		infoPane.setOrbitMode(orbitMode);

		menuBar = new CustomMenuBar(primaryStage);
		menuBar.updateCMIs();

		root.getChildren().addAll(trailPane, bodyPane, infoPane, menuBar);

		// initialize all values to default and load the planet objects
		resetAndLoad(Main.sim);

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
	 * Sets the view to default values, clears trails and bodies and loads all
	 * new bodies from the simulation
	 */
	public void resetAndLoad(Simulation sim) {
		zoom = 1;
		dx = dy = tempdx = tempdy = 0;

		trailPane.getChildren().clear();
		deselectPlanet();
		follow = false;

		bodyPane.getChildren().clear();
		for (Planet p : sim.getPlanets())
			addBodyToWindow(p);
		for (Particle p : sim.getParticles())
			addBodyToWindow(p);
	}

	/**
	 * adds all objects of a body to the bodyPane
	 * 
	 * @param body
	 */
	private void addBodyToWindow(Body body) {
		if (body.getClass() == Planet.class)
			bodyPane.getChildren().addAll(((Planet) body).getVelocityLine(), ((Planet) body).getAccelerationLine(),
					body.getCircle(), ((Planet) body).getLabel());
		else
			bodyPane.getChildren().add(body.getCircle());
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
				if (follow) {
					Vec2D pos = selectedPlanet.getPos().mult(Main.sim.getScale());
					dx = -pos.getX();
					dy = pos.getY();
					updateTrails();
				}

				// update all drawn objects
				for (Planet planet : Main.sim.getPlanets())
					planet.updateObjects();
				for (Particle particle : Main.sim.getParticles())
					particle.updateObjects();

				// update info
				infoPane.updateInfo();

				bodyPane.requestFocus();
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

		// keyboard input
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				KeyCode key = event.getCode();

				// restart and exit
				if (key == KeyCode.R)
					Main.restart();
				if (key == KeyCode.ESCAPE)
					Platform.exit();

				// View options
				if (key == KeyCode.E)
					resetView();
				if (key == KeyCode.T)
					changeTrailsVisibility();
				if (key == KeyCode.L)
					changeLabelsVisibility();
				if (key == KeyCode.V)
					changeVectorsVisibility();
				if (key == KeyCode.I)
					changeInfoVisibility();

				// time controls
				if (key == KeyCode.PERIOD)
					Main.sim.multTime(2);
				if (key == KeyCode.COMMA)
					Main.sim.multTime(0.5);
				if (key == KeyCode.MINUS)
					Main.sim.resetTime();
				if (key == KeyCode.P || key == KeyCode.SPACE)
					Main.sim.setPause(!Main.sim.isPaused());

				// adding planets
				if (key == KeyCode.A)
					addNextPlanet();
				if (key == KeyCode.M)
					changeOrbitMode();

				// help window
				if (key == KeyCode.H)
					openHelpWindow();
			}
		});

		// zoom with the mouse wheel
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
				if (event.isPrimaryButtonDown()) {
					deselectPlanet();
					checkForSelectedPlanet(mouseX, mouseY);
				}

				// reset view
				if (event.isMiddleButtonDown())
					resetView();

				// add new planet
				if (event.isSecondaryButtonDown())
					addNextPlanet();

				event.consume();
			}
		});

		// translates the view if the mouse is dragged with the primary button
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown()) {
					tempdx = (event.getX() - mouseX) / zoom;
					tempdy = (event.getY() - mouseY) / zoom;
					updateTrails();
				}
			}
		});

		// when the mouse is released the current position is saved
		scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				dx += tempdx;
				dy += tempdy;
				tempdx = tempdy = 0;
			}
		});

		// always updates the current mouse position
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				mousePos.set(event.getX(), event.getY());
			}
		});
	}

	/**
	 * Starts a time line to reset scale and translation of the view. The time
	 * line is stopped in the main window time line if the view is reseted.
	 * Deselects the selected planet.
	 */
	private void resetView() {
		deselectPlanet();

		Timeline timeline = new Timeline();
		KeyFrame kf = new KeyFrame(Duration.seconds(1.0 / 60), new EventHandler<ActionEvent>() {
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

				if (zoom == 1 && dx == 0 && dy == 0)
					timeline.stop();
			}
		});
		timeline.getKeyFrames().add(kf);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	private double pancounter;

	/**
	 * moves the view to the selected body after it is selected
	 */
	private void panToBody() {

		// save current position
		double posx = dx;
		double posy = dy;
		pancounter = 0;

		Timeline timeline = new Timeline();
		KeyFrame kf = new KeyFrame(Duration.seconds(1.0 / 60), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				// pan finished -> stop
				if (pancounter >= 1) {
					timeline.stop();
					follow = true;
				}

				if (selectedPlanet == null) {
					timeline.stop();
				} else {
					Vec2D pos = selectedPlanet.getPos().mult(Main.sim.getScale());

					// pancounter from 0 -> 1
					dx = posx * (1 - pancounter) + -pos.getX() * pancounter;
					dy = posy * (1 - pancounter) + pos.getY() * pancounter;
					pancounter += 0.05;
					updateTrails();
				}
			}
		});
		timeline.getKeyFrames().add(kf);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
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
		for (Planet planet : Main.sim.getPlanets()) {
			circle = planet.getCircle();
			center = new Vec2D(circle.getCenterX(), circle.getCenterY());
			mouse = new Vec2D(mouseX, mouseY);
			if (mouse.sub(center).norm() < circle.getRadius() + 5) {
				selectPlanet(planet);
			}
		}
	}

	/** adds a planet to the simulation */
	private void addNextPlanet() {
		Planet newPlanet = nextAddedPlanet.clone();

		// position
		newPlanet.setPos(transfromBack(mousePos));

		// velocity
		if (!orbitMode || Main.sim.getPlanets().length == 0) {
			newPlanet.setVel(0, 0);
		} else {
			Planet biggest = Utils.getBiggestInView(Main.win, Main.sim.getPlanets());
			newPlanet.setVel(Utils.getOrbitalVelocity(biggest, newPlanet));
		}

		// set new trail start
		newPlanet.deleteTrail();
		newPlanet.savePosition();

		// add the planet
		Main.sim.addNewPlanet(newPlanet);

		addBodyToWindow(newPlanet);
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
	 * selects a planet and updates the info label
	 * 
	 * @param planet
	 */
	public void selectPlanet(Planet planet) {
		deselectPlanet();
		selectedPlanet = planet;
		planet.select();
		panToBody();
	}

	/** Deselects the selected planet and updates the info label. */
	public void deselectPlanet() {
		follow = false;
		if (selectedPlanet != null)
			selectedPlanet.deselect();
		selectedPlanet = null;
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
			p.setVectorLinesVisibility(vectors);
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
		HelpWindow helpWin = new HelpWindow();
		try {
			helpWin.start(new Stage());
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
		double x = ((vector.getX() - getWidth() / 2) / zoom - dx - tempdx) / Main.sim.getScale();
		double y = -((vector.getY() - getHeight() / 2) / zoom - dy - tempdy) / Main.sim.getScale();
		return new Vec2D(x, y);
	}

	/**
	 * Transforms a vector from planet coordinates to screen coordinates.
	 * 
	 * @param vector
	 * @return the transformed vector
	 */
	public Vec2D transform(Vec2D vector) {
		double x = zoom * (Main.sim.getScale() * vector.getX() + dx + tempdx) + getWidth() / 2.0;
		double y = zoom * (Main.sim.getScale() * -vector.getY() + dy + tempdy) + getHeight() / 2.0;
		return new Vec2D(x, y);
	}

	/** returns the width of the window */
	public double getWidth() {
		return scene.getWidth();
	}

	/** returns the height of the window */
	public double getHeight() {
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
