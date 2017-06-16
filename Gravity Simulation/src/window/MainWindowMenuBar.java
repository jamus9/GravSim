package window;

import constellations.StartConditions;
import javafx.application.Platform;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import simulation.Main;

/**
 * Implements the menu bar for the main window.
 * 
 * @author Jan Muskalla
 *
 */
public class MainWindowMenuBar extends MenuBar {

	// CheckMenuItems in settings menu
	private CheckMenuItem infoCMI = new CheckMenuItem("Information");
	private CheckMenuItem orbitsCMI = new CheckMenuItem("Orbits");
	private CheckMenuItem labelsCMI = new CheckMenuItem("Labels");
	private CheckMenuItem vectorsCMI = new CheckMenuItem("Vectors");
	private CheckMenuItem orbitModeCMI = new CheckMenuItem("Orbit Mode");

	protected MainWindowMenuBar(Stage primaryStage) {

		super();

		prefWidthProperty().bind(primaryStage.widthProperty());

		/**
		 * simulation menu
		 */

		MenuItem restart = new MenuItem("Neustart");
		restart.setOnAction(actionEvent -> Main.restart());

		MenuItem resetView = new MenuItem("Reset View");
		resetView.setOnAction(actionEven -> Main.window.resetView());

		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(ActionEvent -> Platform.exit());

		Menu simulationMenu = new Menu("Simulation");
		simulationMenu.getItems().addAll(restart, resetView, exit);

		/**
		 * settings menu
		 */

		orbitsCMI.setOnAction(actionEvent -> Main.window.changeOrbitVisibility());
		labelsCMI.setOnAction(actionEvent -> Main.window.changeLabelVisibility());
		infoCMI.setOnAction(ActionEvent -> Main.window.changeInfoVisibility());
		vectorsCMI.setOnAction(actionEvent -> Main.window.changeVectorVisibility());
		orbitModeCMI.setOnAction(actionEvent -> Main.window.changeOrbitMode());

		Menu settingsMenu = new Menu("Settings");
		settingsMenu.getItems().addAll(orbitsCMI, labelsCMI, infoCMI, vectorsCMI, orbitModeCMI);

		/**
		 * constellation loading menu
		 */

		MenuItem earthMoonItem = new MenuItem(StartConditions.earthSystem.getName());
		earthMoonItem.setOnAction(actionEvent -> Main.restart(StartConditions.earthSystem));

		MenuItem solarSystemItem = new MenuItem(StartConditions.solarSystem.getName());
		solarSystemItem.setOnAction(actionEvent -> Main.restart(StartConditions.solarSystem));

		MenuItem marsItem = new MenuItem(StartConditions.marsSystem.getName());
		marsItem.setOnAction(actionEvent -> Main.restart(StartConditions.marsSystem));

		MenuItem jupiterFlybyItem = new MenuItem(StartConditions.jupiterFlyby.getName());
		jupiterFlybyItem.setOnAction(actionEvent -> Main.restart(StartConditions.jupiterFlyby));

		MenuItem earthSunLowItem = new MenuItem(StartConditions.earthSunLow.getName());
		earthSunLowItem.setOnAction(actionEvent -> Main.restart(StartConditions.earthSunLow));

		MenuItem collisionItem = new MenuItem(StartConditions.collision.getName());
		collisionItem.setOnAction(actionEvent -> Main.restart(StartConditions.collision));

		MenuItem symItem = new MenuItem(StartConditions.symmetrical.getName());
		symItem.setOnAction(actionEvent -> Main.restart(StartConditions.symmetrical));

		MenuItem randomItem = new MenuItem("Random");
		randomItem.setOnAction(actionEvent -> Main.restart(StartConditions.getRandom(40)));

		Menu loadMenu = new Menu("Load");
		loadMenu.getItems().addAll(earthMoonItem, solarSystemItem, marsItem, jupiterFlybyItem, earthSunLowItem,
				collisionItem, symItem, randomItem);

		/**
		 * place planet menu
		 */

		RadioMenuItem placeMoon = new RadioMenuItem(StartConditions.moon.getName());
		placeMoon.setOnAction(actionEvent -> Main.window.nextPlacedPlanet = StartConditions.moon);
		placeMoon.setSelected(true);

		RadioMenuItem placeEarth = new RadioMenuItem(StartConditions.earth.getName());
		placeEarth.setOnAction(actionEvent -> Main.window.nextPlacedPlanet = StartConditions.earth);

		RadioMenuItem placeMars = new RadioMenuItem(StartConditions.mars.getName());
		placeMars.setOnAction(actionEvent -> Main.window.nextPlacedPlanet = StartConditions.mars);

		RadioMenuItem placeJupiter = new RadioMenuItem(StartConditions.jupiter.getName());
		placeJupiter.setOnAction(actionEvent -> Main.window.nextPlacedPlanet = StartConditions.jupiter);

		RadioMenuItem placeBlackHole = new RadioMenuItem(StartConditions.blackHole.getName());
		placeBlackHole.setOnAction(actionEvent -> Main.window.nextPlacedPlanet = StartConditions.blackHole);

		RadioMenuItem placeSun = new RadioMenuItem(StartConditions.sun.getName());
		placeSun.setOnAction(actionEvent -> Main.window.nextPlacedPlanet = StartConditions.sun);

		ToggleGroup placeToggleGroup = new ToggleGroup();
		placeToggleGroup.getToggles().addAll(placeMoon, placeEarth, placeMars, placeJupiter, placeSun, placeBlackHole);

		Menu placeMenu = new Menu("Place");
		placeMenu.getItems().addAll(placeMoon, placeEarth, placeMars, placeJupiter, placeSun, placeBlackHole);

		/**
		 * add all menus to the menu bar
		 */

		this.getMenus().addAll(simulationMenu, settingsMenu, loadMenu, placeMenu);
	}

	/**
	 * Updates the check menu items in the menu "settings".
	 */
	protected void updateCMIs() {
		orbitsCMI.setSelected(Main.window.orbits);
		labelsCMI.setSelected(Main.window.labels);
		vectorsCMI.setSelected(Main.window.vectors);
		infoCMI.setSelected(Main.window.infoGroup.isVisible());
		orbitModeCMI.setSelected(Main.window.orbitMode);
	}

}
