package window.menuBar;

import bodies.Planet;
import constellations.Planets;
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
		newMenu(Planets.getMoon());
		newMenu(Planets.getMerkur());
		newMenu(Planets.getVenus());
		newMenu(Planets.getEarth());
		newMenu(Planets.getVenus());
		newMenu(Planets.getMars());
		newMenu(Planets.getJupiter());
		newMenu(Planets.getSaturn());
		newMenu(Planets.getUranus());
		newMenu(Planets.getNeptun());
		newMenu(Planets.getBlackhole());
	}
	
	private void newMenu(Planet p) {
		RadioMenuItem temp = new RadioMenuItem(p.getName());
		temp.setOnAction(actionEvent -> Main.win.setNextAddedPlanet(p));
		toggleGroup.getToggles().add(temp);
		this.getItems().add(temp);
	}

}
