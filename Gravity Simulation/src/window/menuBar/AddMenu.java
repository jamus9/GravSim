package window.menuBar;

import constellations.StartConditions;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import simulation.Main;

/**
 * The menu to add new planets to the scene
 * 
 * @author Jan Muskalla
 *
 */
public class AddMenu extends Menu {

	/** toggle group for all elements so only one can be selected */
	ToggleGroup toggleGroup = new ToggleGroup();

	public AddMenu() {
		super("Add");

		RadioMenuItem addMoon = new RadioMenuItem(StartConditions.moon.getName());
		addMoon.setOnAction(actionEvent -> Main.win.setNextAddedPlanet(StartConditions.moon));
		addMoon.setSelected(true);

		RadioMenuItem addMerkur = new RadioMenuItem(StartConditions.merkur.getName());
		addMerkur.setOnAction(actionEvent -> Main.win.setNextAddedPlanet(StartConditions.merkur));

		RadioMenuItem addVenus = new RadioMenuItem(StartConditions.venus.getName());
		addVenus.setOnAction(actionEvent -> Main.win.setNextAddedPlanet(StartConditions.venus));

		RadioMenuItem addEarth = new RadioMenuItem(StartConditions.earth.getName());
		addEarth.setOnAction(actionEvent -> Main.win.setNextAddedPlanet(StartConditions.earth));

		RadioMenuItem placeMars = new RadioMenuItem(StartConditions.mars.getName());
		placeMars.setOnAction(actionEvent -> Main.win.setNextAddedPlanet(StartConditions.mars));

		RadioMenuItem addJupiter = new RadioMenuItem(StartConditions.jupiter.getName());
		addJupiter.setOnAction(actionEvent -> Main.win.setNextAddedPlanet(StartConditions.jupiter));

		RadioMenuItem addSaturn = new RadioMenuItem(StartConditions.saturn.getName());
		addSaturn.setOnAction(actionEvent -> Main.win.setNextAddedPlanet(StartConditions.saturn));

		RadioMenuItem addUranus = new RadioMenuItem(StartConditions.uranus.getName());
		addUranus.setOnAction(actionEvent -> Main.win.setNextAddedPlanet(StartConditions.uranus));

		RadioMenuItem addNeptun = new RadioMenuItem(StartConditions.neptun.getName());
		addNeptun.setOnAction(actionEvent -> Main.win.setNextAddedPlanet(StartConditions.neptun));

		RadioMenuItem addSun = new RadioMenuItem(StartConditions.sun.getName());
		addSun.setOnAction(actionEvent -> Main.win.setNextAddedPlanet(StartConditions.sun));

		RadioMenuItem addBlackHole = new RadioMenuItem(StartConditions.blackHole.getName());
		addBlackHole.setOnAction(actionEvent -> Main.win.setNextAddedPlanet(StartConditions.blackHole));

		addAll(addMoon, addMerkur, addVenus, addEarth, placeMars, addJupiter, addSaturn, addUranus, addNeptun, addSun,
				addBlackHole);
	}

	/**
	 * adds all menu items to the menu and the toggle group
	 * 
	 * @param items
	 */
	private void addAll(RadioMenuItem... items) {
		toggleGroup.getToggles().addAll(items);
		this.getItems().addAll(items);
	}

}
