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

	private CheckMenuItem fullscreen = new CheckMenuItem("Fullscreen");

	private CheckMenuItem darkThemeCMI = new CheckMenuItem("Dark Theme");
	private CheckMenuItem lightThemeCMI = new CheckMenuItem("Light Theme");

	/** menu items in settings menu */
	private CheckMenuItem orbitModeCMI = new CheckMenuItem("Place in Orbit");

	/**
	 * Creates the menu bar.
	 * 
	 * @param primaryStage
	 */
	public CustomMenuBar(Stage primaryStage) {
		super();
		// prefWidthProperty().bind(primaryStage.widthProperty());

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
		orbitModeCMI.setOnAction(actionEvent -> Main.win.toggleOrbitMode());

		Menu settingsMenu = new Menu("Settings");
		settingsMenu.getItems().addAll(orbitModeCMI);

		/*
		 * view menu
		 */
		trailsCMI.setOnAction(actionEvent -> Main.win.toggleTrails());
		labelsCMI.setOnAction(actionEvent -> Main.win.toggleLabels());
		infoCMI.setOnAction(actionEvent -> Main.win.toggleInfo());
		fullscreen.setOnAction(actionEvent -> Main.win.toggleFullscreen());
		darkThemeCMI.setOnAction(actionEvent -> ViewSettings.setDarkTheme());
		lightThemeCMI.setOnAction(actionEvent -> ViewSettings.setLightTheme());

		Menu viewMenu = new Menu("View");
		viewMenu.getItems().addAll(trailsCMI, labelsCMI, infoCMI, fullscreen, darkThemeCMI, lightThemeCMI);
		updateCMIs();

		/*
		 * open and add menu
		 */
		OpenMenu openMenu = new OpenMenu();
		AddMenu addMenu = new AddMenu();

		/*
		 * add all menus to the menu bar
		 */
		getMenus().addAll(simulationMenu, settingsMenu, viewMenu, openMenu, addMenu);
		setOpacity(ViewSettings.uiOpacity);
		
		// setBackground(null);
		// setStyle("-fx-background-color: white;");
		// getStylesheets().add("css\\menuBarStyle.css");
		// addMenu.setStyle("-fx-background-color: black");
		// addMenu.setStyle("-fx-text-fill: white;");
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
		fullscreen.setSelected(Main.win.isFullscreen());
	}

}
