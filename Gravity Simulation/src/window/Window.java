package window;

import java.util.ArrayList;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import simulation.*;
import systems.PlanetData;
import utils.Utils;
import utils.Vec;
import bodies.Constellation;
import bodies.Particle;
import bodies.Planet;
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

	/** the scene of the window */
	private Scene scene;
	private Stage primaryStage;
	private Group root;

	/** panes for circles, trails, labels and Info */
	private Pane bodyPane, trailPane, labelPane;
	private InfoPane infoPane;

	/** the menu */
	private CustomMenuBar menuBar;
	private Button deccButton, pauseButton, accButton;
	private Label pauseLabel;

	/** flags for trails, labels */
	private boolean trails, labels;

	/** coordinates for zoom and translation */
	private double zoom, dx, dy, tempdx, tempdy;
	private double mouseX, mouseY;

	/** the current mouse position for adding planets */
	private Vec mousePos;

	/** the selected planet and a flag for following it*/
	private Planet selectedPlanet;
	private boolean follow;

	/** the next planet that will be added and the mode */
	private Planet nextAddedPlanet;
	private boolean orbitMode;

	/** true if window size was changed */
	private boolean winWasChanged;

	private Constellation save;

	/**
	 * Creates a new window with initialized local variables.
	 */
	public Window() {
		trails = true;
		labels = true;
		follow = false;

		nextAddedPlanet = PlanetData.getMoon();
		orbitMode = true;

		mousePos = new Vec();
		winWasChanged = true;
	}

	/**
	 * Starts the window for the simulation.
	 * 
	 * @param stage
	 */
	public void start(Stage stage) {

		// scene and root
		root = new Group();
		scene = new Scene(root, 1244, 700, ViewSettings.background);
		setSceneEvents(scene);

		// stage
		this.primaryStage = stage;
		primaryStage.setScene(scene);
		primaryStage.setTitle("Gravity Simulation");
		primaryStage.setFullScreenExitHint("");
		// primaryStage.setMaximized(true);
		primaryStage.setFullScreen(true);

		// add panes and menu to root
		trailPane = new Pane();
		bodyPane = new Pane();
		labelPane = new Pane();
		infoPane = new InfoPane();
		menuBar = new CustomMenuBar(primaryStage);

		{
			int x = 285;
			int y = -1;
			deccButton = new Button("<<");
			deccButton.setOnAction(actionEvent -> Main.sim.multTime(0.5));
			deccButton.setOpacity(ViewSettings.uiOpacity);
			deccButton.setTranslateX(x);
			deccButton.setTranslateY(y);

			pauseButton = new Button("||");
			pauseButton.setOnAction(actionEvent -> togglePause());
			pauseButton.setOpacity(ViewSettings.uiOpacity);
			pauseButton.setTranslateX(x + 35);
			pauseButton.setTranslateY(y);
			pauseButton.setPrefWidth(25);

			accButton = new Button(">>");
			accButton.setOnAction(actionEvent -> Main.sim.multTime(2));
			accButton.setOpacity(ViewSettings.uiOpacity);
			accButton.setTranslateX(x + 63);
			accButton.setTranslateY(y);

			pauseLabel = new Label("Pause");
			pauseLabel.setTextFill(Color.RED);
			pauseLabel.setStyle("-fx-font-size: 20");
			pauseLabel.setVisible(false);
			pauseLabel.setTranslateX(x + 100);
			pauseLabel.setTranslateY(-3);
		}

		root.getChildren().addAll(trailPane, bodyPane, labelPane, infoPane, menuBar, deccButton, pauseButton, accButton,
				pauseLabel);

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
	 * The main window time line. 60 times per second updates all drawn objects and
	 * adjusts the view.
	 */
	private void runTimeLine() {
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.0 / 60), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				// transform trails if window size has changed
				if (winWasChanged) {
					translateTrails();
					winWasChanged = false;
				}

				// follow a planet
				if (follow) {
					Vec pos = selectedPlanet.getPos().mult(Main.sim.getScale());
					dx = -pos.getX();
					dy = pos.getY();
					translateTrails();
				}

				// update all drawn objects
				for (Planet planet : Main.sim.getPlanetList())
					planet.updateObjects();
				for (Particle particle : Main.sim.getParticleList())
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

				// restart, help and exit
				if (key == KeyCode.R)
					Main.restart();
				if (key == KeyCode.H)
					openHelpWindow();
				if (key == KeyCode.ESCAPE)
					Platform.exit();

				// View options
				if (key == KeyCode.E)
					resetView();
				if (key == KeyCode.T)
					toggleTrails();
				if (key == KeyCode.L)
					toggleLabels();
				if (key == KeyCode.I)
					toggleInfo();
				if (key == KeyCode.F)
					toggleFullscreen();

				// time controls
				if (key == KeyCode.PERIOD)
					Main.sim.multTime(2);
				if (key == KeyCode.COMMA)
					Main.sim.multTime(0.5);
				if (key == KeyCode.MINUS)
					Main.sim.resetTime();
				if (key == KeyCode.P || key == KeyCode.SPACE)
					togglePause();

				// planets
				if (key == KeyCode.A)
					addNextPlanet();
				if (key == KeyCode.DELETE && selectedPlanet != null) {
					Main.sim.getPlanetList().remove(selectedPlanet);
					selectedPlanet.delete();
					deselectPlanet();
				}
				if (key == KeyCode.O)
					toggleOrbitMode();

				// testing
//				if (key == KeyCode.Q)
//					printInfo();
//				if (key == KeyCode.W) {
//					if (save == null)
//						enterCompareMode();
//					else {
//						Main.restart(save, true);
//						// save = null;
//					}
//				}
			}

		});

		// zoom with the mouse wheel
		scene.setOnScroll(new EventHandler<ScrollEvent>() {
			public void handle(ScrollEvent event) {
				if (event.getDeltaY() > 0)
					zoom *= 1.1; // zoom in
				else
					zoom /= 1.1; // zoom out
				translateTrails();
				event.consume();
			}
		});

		/*
		 * Select planets with primary mouse button, reset view with middle mouse button
		 * and add new planet with right mouse button. Saves click position.
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
					translateTrails();
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
	 * Sets the view to default values, clears trails and bodies and loads all new
	 * bodies from the simulation
	 */
	public void resetAndLoad(Simulation sim) {
		zoom = 1;
		dx = dy = tempdx = tempdy = 0;

		deselectPlanet();
		follow = false;

		pauseLabel.setVisible(false);

		// clear the pane and add the new bodies
		bodyPane.getChildren().clear();
		labelPane.getChildren().clear();
		trailPane.getChildren().clear();

		for (Particle particle : sim.getParticleList())
			bodyPane.getChildren().add(particle.getCircle());

		for (Planet planet : sim.getPlanetList())
			bodyPane.getChildren().add(planet.getCircle());
		for (Planet planet : sim.getPlanetList())
			labelPane.getChildren().add(planet.getLabel());
	}

	/**
	 * Starts a time line to reset scale and translation of the view. The time line
	 * is stopped in the main window time line if the view is reseted. Deselects the
	 * selected planet.
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
						zoom *= 1.1;
					else
						zoom /= 1.1;
				}

				// reset translation
				if (Math.abs(dx) < 1)
					dx = 0;
				else
					dx /= 1.1;
				if (Math.abs(dy) < 1.0)
					dy = 0;
				else
					dy /= 1.1;

				translateTrails();

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
					Vec scaledPos = selectedPlanet.getPos().mult(Main.sim.getScale());

					// pancounter from 0 -> 1
					dx = posx * (1 - pancounter) + -scaledPos.getX() * pancounter;
					dy = posy * (1 - pancounter) + scaledPos.getY() * pancounter;
					pancounter += 0.08;
					translateTrails();
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
		Vec mouse, center;

		for (Planet p : Main.sim.getPlanetList()) {
			circle = p.getCircle();
			center = new Vec(circle.getCenterX(), circle.getCenterY());
			mouse = new Vec(mouseX, mouseY);

			if (mouse.sub(center).getRadius() < circle.getRadius() + 10) {
				selectPlanet(p);
			}
		}
	}

	/**
	 * adds the selected planet to the simulation
	 */
	private void addNextPlanet() {
		Planet newPlanet = nextAddedPlanet.clone();

		// position
		newPlanet.setPos(transfromBack(mousePos));

		// velocity
		if (!orbitMode || Main.sim.getPlanetList().size() == 0) {
			newPlanet.setVel(0, 0);
		} else {
			Planet biggest = Utils.getBiggestInView(Main.win, Main.sim.getPlanetList().toArray(new Planet[] {}));
			newPlanet.setVel(Utils.getOrbitalVelocityCircular(biggest, newPlanet));
		}

		// add the planet
		Main.sim.addNewPlanet(newPlanet);

		bodyPane.getChildren().add(newPlanet.getCircle());
		labelPane.getChildren().add(newPlanet.getLabel());
	}

	public void enterCompareMode() {
		Main.sim.setPause(true);

		save = new Constellation(Main.sim.getName(), Main.sim.getPlanetList(), Main.sim.getParticleList(),
				Main.sim.getScale(), Main.sim.getTime(), Main.sim.getSps());

		for (Planet p : save.getPlanetList()) {
			p.getTrail().delete();
			p.getTrail().savePosition();
		}

		ArrayList<Planet> planetList = new ArrayList<>();
		for (Planet p : Main.sim.getPlanetList()) {
			planetList.add(p.clone());
		}

		// sort the list
		// planetList.sort(new Comparator<Planet>() {
		// public int compare(Planet p1, Planet p2) {
		// if (p1.getRadius() > p2.getRadius())
		// return -1;
		// else
		// return 1;
		// }
		// });

		planetList.get(0).setPos(-planetList.get(0).getRadius(), 0);

		for (int i = 1; i < planetList.size(); i++) {
			Planet p = planetList.get(i);
			Planet pp = planetList.get(i - 1);
			p.setPos(pp.getPos().getX() + pp.getRadius() + p.getRadius(), 0);
		}

		double scale = 400 / Utils.getBiggest(planetList).getRadius();

		Main.restart(new Constellation(save.getName(), planetList, scale, 0), false);
	}

	public void resetSaved() {
		save = null;
	}

	/**
	 * Translates all Trails to the new position if the trails are visible.
	 */
	private void translateTrails() {
		if (trails)
			for (Planet p : Main.sim.getPlanetList())
				p.getTrail().translate();
	}

	/**
	 * adds a trail line to the trail pane
	 * 
	 * @param line
	 */
	public void addTrail(Line line) {
		trailPane.getChildren().add(line);
	}

	/**
	 * Transforms a vector from planet coordinates to screen coordinates.
	 * 
	 * @param vector
	 * @return the transformed vector
	 */
	public Vec transform(Vec vector) {
		double x = zoom * (Main.sim.getScale() * vector.getX() + dx + tempdx) + getWidth() / 2.0;
		double y = zoom * (Main.sim.getScale() * -vector.getY() + dy + tempdy) + getHeight() / 2.0;
		return new Vec(x, y);
	}

	/**
	 * Transforms a vector from planet coordinates to screen coordinates.
	 * 
	 * @param vector
	 * @return the transformed vector
	 */
	public Vec transfromBack(Vec vector) {
		double x = ((vector.getX() - getWidth() / 2) / zoom - dx - tempdx) / Main.sim.getScale();
		double y = -((vector.getY() - getHeight() / 2) / zoom - dy - tempdy) / Main.sim.getScale();
		return new Vec(x, y);
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

	/**
	 * Deselects the selected planet and updates the info label.
	 */
	public void deselectPlanet() {
		follow = false;
		if (selectedPlanet != null)
			selectedPlanet.deselect();
		selectedPlanet = null;
	}

	/**
	 * opens the help window
	 */
	public void openHelpWindow() {
		HelpWindow helpWin = new HelpWindow();
		try {
			helpWin.start(new Stage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the colors of the background and text after a theme change. Also
	 * updates the check menu items in the menu bar.
	 */
	public void updateColors() {
		scene.setFill(ViewSettings.background);
		infoPane.updateTextColor(ViewSettings.textColor);
		menuBar.updateCMIs();
	}

	/**
	 * Changes the visibility of the trails for all planets and updates the check
	 * menu item.
	 */
	public void toggleTrails() {
		if (trails) {
			for (Planet p : Main.sim.getPlanetList())
				p.getTrail().delete();
			trails = false;
		} else {
			for (Planet p : Main.sim.getPlanetList())
				p.getTrail().delete();
			for (Planet p : Main.sim.getPlanetList())
				p.getTrail().savePosition();
			trails = true;
		}
		menuBar.updateCMIs();
	}

	/**
	 * Changes the visibility of the label for all planets and updates the check
	 * menu item.
	 */
	public void toggleLabels() {
		labels = !labels;
		for (Planet p : Main.sim.getPlanetList()) {
			p.getLabel().setVisible(labels);
		}
		menuBar.updateCMIs();
	}

	/**
	 * Changes the visibility of the information group and updates the check menu
	 * item.
	 */
	public void toggleInfo() {
		infoPane.setVisible(!infoPane.isVisible());
		menuBar.updateCMIs();
	}

	public void toggleFullscreen() {
		primaryStage.setFullScreen(!primaryStage.isFullScreen());
		menuBar.updateCMIs();
	}

	private void togglePause() {
		boolean np = !Main.sim.isPaused();
		Main.sim.setPause(np);
		pauseLabel.setVisible(np);
		if (np)
			pauseButton.setText(">");
		else
			pauseButton.setText("||");
	}

	/**
	 * turns the orbitMode on or off
	 */
	public void toggleOrbitMode() {
		orbitMode = !orbitMode;
		menuBar.updateCMIs();
	}

	private void printInfo() {
		System.out.println("System: " + Main.sim.getConstellation().getName());
		System.out.println("Scale: " + zoom * Main.sim.getScale());
		System.out.println("Time: " + Main.sim.getTime());
	}

	public boolean isTrails() {
		return trails;
	}

	public boolean isLabels() {
		return labels;
	}

	public boolean isInfoVisible() {
		return infoPane.isVisible();
	}

	public boolean isFullscreen() {
		return primaryStage.isFullScreen();
	}

	public boolean isOrbitMode() {
		return orbitMode;
	}

	public void setNextAddedPlanet(Planet p) {
		this.nextAddedPlanet = p;
	}

	public Planet getSelectedPlanet() {
		return selectedPlanet;
	}

	public double getZoom() {
		return zoom;
	}

	public double getWidth() {
		return scene.getWidth();
	}

	public double getHeight() {
		return scene.getHeight();
	}

}
