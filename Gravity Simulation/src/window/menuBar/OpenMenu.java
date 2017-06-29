package window.menuBar;

import bodies.Constellation;
import constellations.Constellations;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import simulation.Main;

/**
 * The menu to open existing constellations from start conditions.
 * 
 * @author Jan Muskalla
 *
 */
public class OpenMenu extends Menu {

	public OpenMenu() {
		super("Open");

		newMenu(Constellations.solarSystem());
		newMenu(Constellations.earthSystem());
		newMenu(Constellations.marsSystem());
		newMenu(Constellations.jupiterSystem());

		newMenu(Constellations.saturnWithRings());
		newMenu(Constellations.saturnUranusEncounter());

		newMenu(Constellations.randomMoons());
		newMenu(Constellations.randomPlanets());

		newMenu(Constellations.jupiterFlyby());
		newMenu(Constellations.earthMarsCollision());
		newMenu(Constellations.binaryStar());
		newMenu(Constellations.binaryWithRings());

		newMenu(Constellations.sym8());
		newMenu(Constellations.empty);
	}

	private void newMenu(Constellation con) {
		MenuItem temp = new MenuItem(con.getName());
		temp.setOnAction(actionEvent -> Main.restart(con));
		this.getItems().add(temp);
	}

}
