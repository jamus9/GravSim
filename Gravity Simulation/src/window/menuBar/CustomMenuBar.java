package window.menuBar;

import javafx.application.Platform;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import simulation.Main;
import window.ViewSettings;

/**
 * Implements the menu bar for the main window.
 * 
 * @author Jan Muskalla
 *
 */
public class CustomMenuBar extends MenuBar {

	/** menu items in view menu */
	private CheckMenuItem infoCMI = new CheckMenuItem("Information");
	private CheckMenuItem trailsCMI = new CheckMenuItem("Trails");
	private CheckMenuItem labelsCMI = new CheckMenuItem("Labels");
	
	private CheckMenuItem darkThemeCMI = new CheckMenuItem("Dark Theme");
	private CheckMenuItem lightThemeCMI = new CheckMenuItem("Light Theme");
	
	/** menu items in settings menu */
	private CheckMenuItem orbitModeCMI = new CheckMenuItem("Orbit Mode");

	/**
	 * Creates the menu bar.
	 * 
	 * @param primaryStage
	 */
	public CustomMenuBar(Stage primaryStage) {
		super();
		prefWidthProperty().bind(primaryStage.widthProperty());

		/*
		 * simulation menu
		 */
		MenuItem restartItem = new MenuItem("Restart");
		restartItem.setOnAction(actionEvent -> Main.restart());

		MenuItem helpItem = new MenuItem("Help");
		helpItem.setOnAction(ActionEvent -> Main.win.openHelpWindow());

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.setOnAction(ActionEvent -> Platform.exit());

		Menu simulationMenu = new Menu("Simulation");
		simulationMenu.getItems().addAll(restartItem, helpItem, exitItem);

		/*
		 * settings menu
		 */
		orbitModeCMI.setOnAction(actionEvent -> Main.win.changeOrbitMode());

		Menu settingsMenu = new Menu("Settings");
		settingsMenu.getItems().addAll(orbitModeCMI);

		/*
		 * view menu
		 */
		trailsCMI.setOnAction(actionEvent -> Main.win.changeTrailsVisibility());
		labelsCMI.setOnAction(actionEvent -> Main.win.changeLabelsVisibility());
		infoCMI.setOnAction(actionEvent -> Main.win.changeInfoVisibility());
		darkThemeCMI.setOnAction(actionEvent -> ViewSettings.setDarkTheme());
		darkThemeCMI.setSelected(ViewSettings.darkTheme);
		lightThemeCMI.setOnAction(actionEvent -> ViewSettings.setLightTheme());

		Menu viewMenu = new Menu("View");
		viewMenu.getItems().addAll(trailsCMI, labelsCMI, infoCMI, darkThemeCMI, lightThemeCMI);

		// open and add menu
		OpenMenu openMenu = new OpenMenu();
		AddMenu addMenu = new AddMenu();

		// add all menus to the menu bar
		this.getMenus().addAll(simulationMenu, settingsMenu, viewMenu, openMenu, addMenu);
	}

	/**
	 * Updates the check menu items in the menu "settings".
	 */
	public void updateCMIs() {
		trailsCMI.setSelected(Main.win.isTrails());
		labelsCMI.setSelected(Main.win.isLabels());
		infoCMI.setSelected(Main.win.isInfoVisible());
		orbitModeCMI.setSelected(Main.win.isOrbitMode());
		darkThemeCMI.setSelected(ViewSettings.darkTheme);
		lightThemeCMI.setSelected(!ViewSettings.darkTheme);
	}

}
