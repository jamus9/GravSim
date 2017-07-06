package window.menuBar;

import bodies.System;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import simulation.Main;
import systems.Systems;

/**
 * The menu to open existing systems from start conditions.
 * 
 * @author Jan Muskalla
 *
 */
public class OpenMenu extends Menu {

	public OpenMenu() {
		super("Open");

		Menu subMenuReal = new Menu("Real Systems");
		this.getItems().add(subMenuReal);
		newMenu(Systems.solarSystem(), subMenuReal);
		newMenu(Systems.earthSystem(), subMenuReal);
		newMenu(Systems.marsSystem(), subMenuReal);
		newMenu(Systems.jupiterSystem(), subMenuReal);
		newMenu(Systems.saturnSystem(), subMenuReal);
		
		Menu subMenuParticles = new Menu("Particles");
		this.getItems().add(subMenuParticles);
		newMenu(Systems.saturnUranusEncounter(),subMenuParticles);
		newMenu(Systems.binaryWithRings(), subMenuParticles);
		newMenu(Systems.particleField(), subMenuParticles);
		newMenu(Systems.flyThroughParticleField(), subMenuParticles);
		
		
		newMenu(Systems.randomMoons());
		newMenu(Systems.randomPlanets());

		
		newMenu(Systems.jupiterFlyby());
		newMenu(Systems.earthMarsCollision());
		newMenu(Systems.binaryStar());
		
		newMenu(Systems.sym8());
		newMenu(Systems.empty);

	}

	private void newMenu(System con) {
		MenuItem temp = new MenuItem(con.getName());
		temp.setOnAction(actionEvent -> Main.restart(con));
		this.getItems().add(temp);
	}

	private void newMenu(System con, Menu subMenu) {
		MenuItem temp = new MenuItem(con.getName());
		temp.setOnAction(actionEvent -> Main.restart(con));
		subMenu.getItems().add(temp);
	}

}
