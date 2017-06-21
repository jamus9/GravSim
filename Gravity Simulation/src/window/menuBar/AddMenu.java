package window.menuBar;

import constellations.StartConditions;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import window.Window;

public class AddMenu extends Menu {

	Window win;
	ToggleGroup toggleGroup = new ToggleGroup();

	public AddMenu(Window win) {
		super("Add");
		this.win = win;

		RadioMenuItem addMoon = new RadioMenuItem(StartConditions.moon.getName());
		addMoon.setOnAction(actionEvent -> win.setNextAddedPlanet(StartConditions.moon));
		addMoon.setSelected(true);

		RadioMenuItem addMerkur = new RadioMenuItem(StartConditions.merkur.getName());
		addMerkur.setOnAction(actionEvent -> win.setNextAddedPlanet(StartConditions.merkur));

		RadioMenuItem addVenus = new RadioMenuItem(StartConditions.venus.getName());
		addVenus.setOnAction(actionEvent -> win.setNextAddedPlanet(StartConditions.venus));

		RadioMenuItem addEarth = new RadioMenuItem(StartConditions.earth.getName());
		addEarth.setOnAction(actionEvent -> win.setNextAddedPlanet(StartConditions.earth));

		RadioMenuItem placeMars = new RadioMenuItem(StartConditions.mars.getName());
		placeMars.setOnAction(actionEvent -> win.setNextAddedPlanet(StartConditions.mars));

		RadioMenuItem addJupiter = new RadioMenuItem(StartConditions.jupiter.getName());
		addJupiter.setOnAction(actionEvent -> win.setNextAddedPlanet(StartConditions.jupiter));

		RadioMenuItem addSaturn = new RadioMenuItem(StartConditions.saturn.getName());
		addSaturn.setOnAction(actionEvent -> win.setNextAddedPlanet(StartConditions.saturn));
		
		RadioMenuItem addUranus = new RadioMenuItem(StartConditions.uranus.getName());
		addUranus.setOnAction(actionEvent -> win.setNextAddedPlanet(StartConditions.uranus));
		
		RadioMenuItem addNeptun = new RadioMenuItem(StartConditions.neptun.getName());
		addNeptun.setOnAction(actionEvent -> win.setNextAddedPlanet(StartConditions.neptun));

		RadioMenuItem addSun = new RadioMenuItem(StartConditions.sun.getName());
		addSun.setOnAction(actionEvent -> win.setNextAddedPlanet(StartConditions.sun));

		RadioMenuItem addBlackHole = new RadioMenuItem(StartConditions.blackHole.getName());
		addBlackHole.setOnAction(actionEvent -> win.setNextAddedPlanet(StartConditions.blackHole));

		addAll(addMoon, addMerkur, addVenus, addEarth, placeMars, addJupiter, addSaturn, addUranus, addNeptun, addSun, addBlackHole);
	}

	private void addAll(RadioMenuItem... items) {
		toggleGroup.getToggles().addAll(items);
		this.getItems().addAll(items);
	}

}
