package windows;

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
public class CustomMenuBar extends MenuBar {

	/** CheckMenuItems in settings menu */
	private CheckMenuItem infoCMI = new CheckMenuItem("Information");
	private CheckMenuItem trailsCMI = new CheckMenuItem("Trails");
	private CheckMenuItem labelsCMI = new CheckMenuItem("Labels");
	private CheckMenuItem vectorsCMI = new CheckMenuItem("Vectors");
	private CheckMenuItem orbitModeCMI = new CheckMenuItem("Orbit Mode");

	/**
	 * Creates the menu bar.
	 * 
	 * @param primaryStage
	 */
	protected CustomMenuBar(Stage primaryStage) {
		super();
		prefWidthProperty().bind(primaryStage.widthProperty());

		/*
		 * simulation menu
		 */
		MenuItem restartItem = new MenuItem("Neustart");
		restartItem.setOnAction(actionEvent -> Main.restart());

		MenuItem resetViewItem = new MenuItem("Reset View");
		resetViewItem.setOnAction(actionEven -> Main.window.resetView());

		MenuItem helpItem = new MenuItem("Help");
		helpItem.setOnAction(ActionEvent -> Main.window.openHelpWindow());

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.setOnAction(ActionEvent -> Platform.exit());

		Menu simulationMenu = new Menu("Simulation");
		simulationMenu.getItems().addAll(restartItem, resetViewItem, helpItem, exitItem);

		/*
		 * settings menu
		 */
		orbitModeCMI.setOnAction(actionEvent -> Main.window.changeOrbitMode());

		Menu settingsMenu = new Menu("Settings");
		settingsMenu.getItems().addAll(orbitModeCMI);

		/*
		 * view menu
		 */
		trailsCMI.setOnAction(actionEvent -> Main.window.changeTrailsVisibility());
		labelsCMI.setOnAction(actionEvent -> Main.window.changeLabelsVisibility());
		infoCMI.setOnAction(ActionEvent -> Main.window.changeInfoVisibility());
		vectorsCMI.setOnAction(actionEvent -> Main.window.changeVectorsVisibility());

		Menu viewMenu = new Menu("View");
		viewMenu.getItems().addAll(trailsCMI, labelsCMI, infoCMI, vectorsCMI);

		/*
		 * open constellation menu
		 */
		MenuItem earthSystemItem = new MenuItem("Earth System");
		earthSystemItem.setOnAction(actionEvent -> Main.restart(StartConditions.getEarthSystem()));

		MenuItem solarSystemItem = new MenuItem("Solar System");
		solarSystemItem.setOnAction(actionEvent -> Main.restart(StartConditions.getSolarSystem()));

		MenuItem marsSystemItem = new MenuItem("Mars System");
		marsSystemItem.setOnAction(actionEvent -> Main.restart(StartConditions.getMarsSystem()));

		MenuItem jupiterSystemItem = new MenuItem("Jupiter System");
		jupiterSystemItem.setOnAction(actionEvent -> Main.restart(StartConditions.getJupiterSystem()));

		MenuItem jupiterFlybyItem = new MenuItem("Jupiter Flyby");
		jupiterFlybyItem.setOnAction(actionEvent -> Main.restart(StartConditions.getJupiterFlyby()));

		MenuItem earthMarsCollisionItem = new MenuItem("Earth-Mars Collision");
		earthMarsCollisionItem.setOnAction(actionEvent -> Main.restart(StartConditions.getEarthMarsCollision()));

		MenuItem sym8Item = new MenuItem("Symmetrical 8");
		sym8Item.setOnAction(actionEvent -> Main.restart(StartConditions.getSym8()));

		MenuItem randomPlanetsItem = new MenuItem("Random Planets");
		randomPlanetsItem.setOnAction(actionEvent -> Main.restart(StartConditions.getRandomPlanets()));

		MenuItem randomMoonsItem = new MenuItem("Random Moons");
		randomMoonsItem.setOnAction(actionEvent -> Main.restart(StartConditions.getRandomMoons()));

		MenuItem emptyItem = new MenuItem(StartConditions.empty.getName());
		emptyItem.setOnAction(actionEvent -> Main.restart(StartConditions.empty));

		Menu openMenu = new Menu("Open");
		openMenu.getItems().addAll(solarSystemItem, earthSystemItem, marsSystemItem, jupiterSystemItem,
				jupiterFlybyItem, earthMarsCollisionItem, sym8Item, randomMoonsItem, randomPlanetsItem, emptyItem);

		/*
		 * add planet menu
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

		ToggleGroup addToggleGroup = new ToggleGroup();
		addToggleGroup.getToggles().addAll(placeMoon, placeEarth, placeMars, placeJupiter, placeSun, placeBlackHole);

		Menu addMenu = new Menu("Add");
		addMenu.getItems().addAll(placeMoon, placeEarth, placeMars, placeJupiter, placeSun, placeBlackHole);

		/*
		 * add all menus to the menu bar
		 */
		this.getMenus().addAll(simulationMenu, settingsMenu, viewMenu, openMenu, addMenu);
	}

	/**
	 * Updates the check menu items in the menu "settings".
	 */
	protected void updateCMIs() {
		trailsCMI.setSelected(Main.window.trails);
		labelsCMI.setSelected(Main.window.labels);
		vectorsCMI.setSelected(Main.window.vectors);
		infoCMI.setSelected(Main.window.infoGroup.isVisible());
		orbitModeCMI.setSelected(Main.window.orbitMode);
	}

}
