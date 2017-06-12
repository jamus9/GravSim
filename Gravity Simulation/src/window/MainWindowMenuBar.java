package window;

import constellations.StartConditions;
import javafx.application.Platform;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import simulation.Simulation;

public class MainWindowMenuBar extends MenuBar {

	// CheckMenuItems in settings menu
	private CheckMenuItem infoCMI = new CheckMenuItem("Information");
	private CheckMenuItem orbitsCMI = new CheckMenuItem("Orbits");
	private CheckMenuItem labelsCMI = new CheckMenuItem("Namen");
	private CheckMenuItem vectorsCMI = new CheckMenuItem("Vektoren");

	public MainWindowMenuBar() {
		super();

		/**
		 * simulation menu
		 */

		MenuItem restart = new MenuItem("Neustart");
		restart.setOnAction(actionEvent -> Simulation.window.restart(Simulation.constellation));

		MenuItem resetView = new MenuItem("Ansicht zurücksetzen");
		resetView.setOnAction(actionEven -> Simulation.window.resetView());

		MenuItem exit = new MenuItem("Beenden");
		exit.setOnAction(ActionEvent -> Platform.exit());

		Menu simulation = new Menu("Simulation");
		simulation.getItems().addAll(restart, resetView, exit);

		/**
		 * settings menu
		 */

		orbitsCMI.setOnAction(actionEvent -> Simulation.window.changeOrbitVisibility());
		labelsCMI.setOnAction(actionEvent -> Simulation.window.changeLabelVisibility());
		infoCMI.setOnAction(ActionEvent -> Simulation.window.changeInfoVisibility());
		vectorsCMI.setOnAction(actionEvent -> Simulation.window.changeVectorVisibility());

		Menu settings = new Menu("Einstellungen");
		settings.getItems().addAll(orbitsCMI, labelsCMI, infoCMI, vectorsCMI);

		/**
		 * constellation loading menu
		 */

		MenuItem earthMoonItem = new MenuItem(StartConditions.earthSystem.getName());
		earthMoonItem.setOnAction(actionEvent -> Simulation.window.restart(StartConditions.earthSystem));

		MenuItem solarSystemItem = new MenuItem(StartConditions.solarSystem.getName());
		solarSystemItem.setOnAction(actionEvent -> Simulation.window.restart(StartConditions.solarSystem));

		MenuItem marsItem = new MenuItem(StartConditions.marsSystem.getName());
		marsItem.setOnAction(actionEvent -> Simulation.window.restart(StartConditions.marsSystem));

		MenuItem jupiterFlybyItem = new MenuItem(StartConditions.jupiterFlyby.getName());
		jupiterFlybyItem.setOnAction(actionEvent -> Simulation.window.restart(StartConditions.jupiterFlyby));

		MenuItem earthSunLowItem = new MenuItem(StartConditions.earthSunLow.getName());
		earthSunLowItem.setOnAction(actionEvent -> Simulation.window.restart(StartConditions.earthSunLow));

		MenuItem symItem = new MenuItem(StartConditions.symmetrical.getName());
		symItem.setOnAction(actionEvent -> Simulation.window.restart(StartConditions.symmetrical));

		MenuItem randomItem = new MenuItem("Random");
		randomItem.setOnAction(actionEvent -> Simulation.window.restart(StartConditions.getRandom(40)));

		Menu load = new Menu("Lade");
		load.getItems().addAll(earthMoonItem, solarSystemItem, marsItem, jupiterFlybyItem, earthSunLowItem, symItem,
				randomItem);

		/**
		 * add planet menu
		 */

		RadioMenuItem addMoon = new RadioMenuItem(StartConditions.moon.getName());
		addMoon.setOnAction(actionEvent -> Simulation.window.nextPlanet = StartConditions.moon);
		addMoon.setSelected(true);

		RadioMenuItem addEarth = new RadioMenuItem(StartConditions.earth.getName());
		addEarth.setOnAction(actionEvent -> Simulation.window.nextPlanet = StartConditions.earth);

		RadioMenuItem addMars = new RadioMenuItem(StartConditions.mars.getName());
		addMars.setOnAction(actionEvent -> Simulation.window.nextPlanet = StartConditions.mars);

		RadioMenuItem addJupiter = new RadioMenuItem(StartConditions.jupiter.getName());
		addJupiter.setOnAction(actionEvent -> Simulation.window.nextPlanet = StartConditions.jupiter);

		ToggleGroup toggleGroup = new ToggleGroup();
		toggleGroup.getToggles().addAll(addMoon, addEarth, addMars, addJupiter);

		Menu add = new Menu("Platzieren");
		add.getItems().addAll(addMoon, addEarth, addMars, addJupiter);

		/**
		 * time controls (does not work)
		 */

		Menu timeSlower = new Menu("<<");
		timeSlower.setOnAction(actionEvent -> Simulation.window.setTimeStep(Simulation.time * 0.5));
		Menu timeFaster = new Menu(">>");
		timeFaster.setOnAction(actionEvent -> Simulation.window.setTimeStep(Simulation.time * 2));

		/**
		 * add all menus to the menu bar
		 */
		
		this.getMenus().addAll(simulation, settings, load, add);
	}
	
	/**
	 * Updates the check menu items in the menu "settings".
	 */
	protected void updateCMIs() {
		orbitsCMI.setSelected(Simulation.window.orbits);
		labelsCMI.setSelected(Simulation.window.labels);
		vectorsCMI.setSelected(Simulation.window.vectors);
		infoCMI.setSelected(Simulation.window.infoGroup.isVisible());
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

}
