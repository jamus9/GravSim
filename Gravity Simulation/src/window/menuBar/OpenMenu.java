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

		Menu subMenuReal = new Menu("Real Systems");
		this.getItems().add(subMenuReal);
		newMenu(Constellations.solarSystem(), subMenuReal);
		newMenu(Constellations.earthSystem(), subMenuReal);
		newMenu(Constellations.marsSystem(), subMenuReal);
		newMenu(Constellations.jupiterSystem(), subMenuReal);
		newMenu(Constellations.saturnSystem(), subMenuReal);
		
		Menu subMenuParticles = new Menu("Particles");
		this.getItems().add(subMenuParticles);
		newMenu(Constellations.saturnUranusEncounter(),subMenuParticles);
		newMenu(Constellations.binaryWithRings(), subMenuParticles);
		newMenu(Constellations.particleField(), subMenuParticles);
		newMenu(Constellations.flyThroughParticleField(), subMenuParticles);
		
		
		newMenu(Constellations.randomMoons());
		newMenu(Constellations.randomPlanets());

		
		newMenu(Constellations.jupiterFlyby());
		newMenu(Constellations.earthMarsCollision());
		newMenu(Constellations.binaryStar());
		
		newMenu(Constellations.sym8());
		newMenu(Constellations.empty);

	}

	private void newMenu(Constellation con) {
		MenuItem temp = new MenuItem(con.getName());
		temp.setOnAction(actionEvent -> Main.restart(con));
		this.getItems().add(temp);
	}

	private void newMenu(Constellation con, Menu subMenu) {
		MenuItem temp = new MenuItem(con.getName());
		temp.setOnAction(actionEvent -> Main.restart(con));
		subMenu.getItems().add(temp);
	}

}
