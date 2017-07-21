package window.menuBar;

import bodies.Planet;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import simulation.Main;
import systems.PlanetData;

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
		
		newMenu(PlanetData.getPhobos());
		newMenu(PlanetData.getEnceladus());
		
		Planet moon = PlanetData.getMoon();
		RadioMenuItem moonItem = new RadioMenuItem(moon.getName());
		moonItem.setOnAction(actionEvent -> Main.win.setNextAddedPlanet(moon));
		toggleGroup.getToggles().add(moonItem);
		this.getItems().add(moonItem);
		moonItem.setSelected(true);
		
		newMenu(PlanetData.getMercury());
		newMenu(PlanetData.getVenus());
		newMenu(PlanetData.getEarth());
		newMenu(PlanetData.getMars());
		newMenu(PlanetData.getJupiter());
		newMenu(PlanetData.getSaturn());
		newMenu(PlanetData.getUranus());
		newMenu(PlanetData.getNeptune());
		
		newMenu(PlanetData.getSun());
		newMenu(PlanetData.getBetelgeuse());
		newMenu(PlanetData.getSagittariusA());
		
	}
	
	private void newMenu(Planet p) {
		RadioMenuItem temp = new RadioMenuItem(p.getName());
		temp.setOnAction(actionEvent -> Main.win.setNextAddedPlanet(p));
		toggleGroup.getToggles().add(temp);
		this.getItems().add(temp);
	}

}
