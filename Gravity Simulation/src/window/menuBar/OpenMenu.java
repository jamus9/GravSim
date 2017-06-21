package window.menuBar;

import constellations.StartConditions;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import simulation.Main;

public class OpenMenu extends Menu {

	public OpenMenu() {
		super("Open");
		
		MenuItem earthSystemItem = new MenuItem("Earth System");
		earthSystemItem.setOnAction(actionEvent -> Main.restart(StartConditions.getEarthSystem()));

		MenuItem solarSystemItem = new MenuItem("Solar System");
		solarSystemItem.setOnAction(actionEvent -> Main.restart(StartConditions.getSolarSystem()));

		MenuItem marsSystemItem = new MenuItem("Mars System");
		marsSystemItem.setOnAction(actionEvent -> Main.restart(StartConditions.getMarsSystem()));

		MenuItem jupiterSystemItem = new MenuItem("Jupiter System");
		jupiterSystemItem.setOnAction(actionEvent -> Main.restart(StartConditions.getJupiterSystem()));

		MenuItem randomMoonsItem = new MenuItem("Random Moons");
		randomMoonsItem.setOnAction(actionEvent -> Main.restart(StartConditions.getRandomMoons()));

		MenuItem randomPlanetsItem = new MenuItem("Random Planets");
		randomPlanetsItem.setOnAction(actionEvent -> Main.restart(StartConditions.getRandomPlanets()));

		MenuItem jupiterFlybyItem = new MenuItem("Jupiter Flyby");
		jupiterFlybyItem.setOnAction(actionEvent -> Main.restart(StartConditions.getJupiterFlyby()));

		MenuItem earthMarsCollisionItem = new MenuItem("Earth-Mars Collision");
		earthMarsCollisionItem.setOnAction(actionEvent -> Main.restart(StartConditions.getEarthMarsCollision()));

		MenuItem sym8Item = new MenuItem("Symmetrical 8");
		sym8Item.setOnAction(actionEvent -> Main.restart(StartConditions.getSym8()));

		MenuItem emptyItem = new MenuItem(StartConditions.empty.getName());
		emptyItem.setOnAction(actionEvent -> Main.restart(StartConditions.empty));

		this.getItems().addAll(solarSystemItem, earthSystemItem, marsSystemItem, jupiterSystemItem, randomMoonsItem,
				randomPlanetsItem, jupiterFlybyItem, earthMarsCollisionItem, sym8Item, emptyItem);
	}
}
