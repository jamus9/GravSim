package window;

import constellations.StartConditions;
import javafx.application.Platform;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
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
	private CheckMenuItem labelsCMI = new CheckMenuItem("Namen");
	private CheckMenuItem vectorsCMI = new CheckMenuItem("Vektoren");
	
	private RadioMenuItem addMoon;

	protected MainWindowMenuBar() {
		
		super();

		/**
		 * simulation menu
		 */

		MenuItem restart = new MenuItem("Neustart");
		restart.setOnAction(actionEvent -> Main.restart());

		MenuItem resetView = new MenuItem("Ansicht zurücksetzen");
		resetView.setOnAction(actionEven -> Main.window.resetView());

		MenuItem exit = new MenuItem("Beenden");
		exit.setOnAction(ActionEvent -> Platform.exit());

		Menu simulation = new Menu("Simulation");
		simulation.getItems().addAll(restart, resetView, exit);

		/**
		 * settings menu
		 */

		orbitsCMI.setOnAction(actionEvent -> Main.window.changeOrbitVisibility());
		labelsCMI.setOnAction(actionEvent -> Main.window.changeLabelVisibility());
		infoCMI.setOnAction(ActionEvent -> Main.window.changeInfoVisibility());
		vectorsCMI.setOnAction(actionEvent -> Main.window.changeVectorVisibility());

		Menu settings = new Menu("Einstellungen");
		settings.getItems().addAll(orbitsCMI, labelsCMI, infoCMI, vectorsCMI);

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

		MenuItem symItem = new MenuItem(StartConditions.symmetrical.getName());
		symItem.setOnAction(actionEvent -> Main.restart(StartConditions.symmetrical));

		MenuItem randomItem = new MenuItem("Random");
		randomItem.setOnAction(actionEvent -> Main.restart(StartConditions.getRandom(40)));

		Menu load = new Menu("Lade");
		load.getItems().addAll(earthMoonItem, solarSystemItem, marsItem, jupiterFlybyItem, earthSunLowItem, symItem,
				randomItem);

		/**
		 * add planet menu
		 */

		addMoon = new RadioMenuItem(StartConditions.moon.getName());
		addMoon.setOnAction(actionEvent -> Main.window.nextPlacedPlanet = StartConditions.moon);
		addMoon.setSelected(true);

		RadioMenuItem addEarth = new RadioMenuItem(StartConditions.earth.getName());
		addEarth.setOnAction(actionEvent -> Main.window.nextPlacedPlanet = StartConditions.earth);

		RadioMenuItem addMars = new RadioMenuItem(StartConditions.mars.getName());
		addMars.setOnAction(actionEvent -> Main.window.nextPlacedPlanet = StartConditions.mars);

		RadioMenuItem addJupiter = new RadioMenuItem(StartConditions.jupiter.getName());
		addJupiter.setOnAction(actionEvent -> Main.window.nextPlacedPlanet = StartConditions.jupiter);
		
		RadioMenuItem addBlackHole = new RadioMenuItem(StartConditions.blackHole.getName());
		addBlackHole.setOnAction(actionEvent -> Main.window.nextPlacedPlanet = StartConditions.blackHole);

		ToggleGroup toggleGroup = new ToggleGroup();
		toggleGroup.getToggles().addAll(addMoon, addEarth, addMars, addJupiter, addBlackHole);

		Menu add = new Menu("Platzieren");
		add.getItems().addAll(addMoon, addEarth, addMars, addJupiter, addBlackHole);

		/**
		 * add all menus to the menu bar
		 */
		
		this.getMenus().addAll(simulation, settings, load, add);
	}
	
	/**
	 * Updates the check menu items in the menu "settings".
	 */
	protected void updateCMIs() {
		orbitsCMI.setSelected(Main.window.orbits);
		labelsCMI.setSelected(Main.window.labels);
		vectorsCMI.setSelected(Main.window.vectors);
		infoCMI.setSelected(Main.window.infoGroup.isVisible());
	}

	protected void setOrbitsCMI(boolean b) {
		this.orbitsCMI.setSelected(b);
	}

	protected void setLabelsCMI(boolean b) {
		this.labelsCMI.setSelected(b);
	}

	protected void setVectorsCMI(boolean b) {
		this.vectorsCMI.setSelected(b);
	}
	
	protected void setInfoCMI(boolean b) {
		this.infoCMI.setSelected(b);
	}
	
	protected void resetAddMenu() {
		addMoon.setSelected(true);
	}

}
