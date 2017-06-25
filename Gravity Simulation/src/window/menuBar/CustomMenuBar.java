package window.menuBar;

import javafx.application.Platform;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import simulation.Main;
import window.Window;

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

	Window win;

	/**
	 * Creates the menu bar.
	 * 
	 * @param primaryStage
	 */
	public CustomMenuBar(Stage primaryStage, Window win) {
		super();
		prefWidthProperty().bind(primaryStage.widthProperty());
		this.win = win;

		/*
		 * simulation menu
		 */
		MenuItem restartItem = new MenuItem("Restart");
		restartItem.setOnAction(actionEvent -> Main.restart());

		MenuItem helpItem = new MenuItem("Help");
		helpItem.setOnAction(ActionEvent -> win.openHelpWindow());

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.setOnAction(ActionEvent -> Platform.exit());

		Menu simulationMenu = new Menu("Simulation");
		simulationMenu.getItems().addAll(restartItem, helpItem, exitItem);

		/*
		 * settings menu
		 */
		orbitModeCMI.setOnAction(actionEvent -> win.changeOrbitMode());

		Menu settingsMenu = new Menu("Settings");
		settingsMenu.getItems().addAll(orbitModeCMI);

		/*
		 * view menu
		 */
		trailsCMI.setOnAction(actionEvent -> win.changeTrailsVisibility());
		labelsCMI.setOnAction(actionEvent -> win.changeLabelsVisibility());
		infoCMI.setOnAction(ActionEvent -> win.changeInfoVisibility());
		vectorsCMI.setOnAction(actionEvent -> win.changeVectorsVisibility());

		Menu viewMenu = new Menu("View");
		viewMenu.getItems().addAll(trailsCMI, labelsCMI, infoCMI, vectorsCMI);

		// open and add menu
		OpenMenu openMenu = new OpenMenu();
		AddMenu addMenu = new AddMenu(win);

		// add all menus to the menu bar
		this.getMenus().addAll(simulationMenu, settingsMenu, viewMenu, openMenu, addMenu);
	}

	/**
	 * Updates the check menu items in the menu "settings".
	 */
	public void updateCMIs() {
		trailsCMI.setSelected(win.isTrails());
		labelsCMI.setSelected(win.isLabels());
		vectorsCMI.setSelected(win.isVectors());
		infoCMI.setSelected(win.isInfoVisible());
		orbitModeCMI.setSelected(win.isOrbitMode());
	}

}
