package window.menuBar;

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

		/*
		 * real systems
		 */
		Menu subMenuReal = new Menu("Real Systems");
		this.getItems().add(subMenuReal);

		MenuItem solarSystem = new MenuItem("Solar System");
		solarSystem.setOnAction(actionEvent -> Main.restart(Systems.solarSystem()));
		subMenuReal.getItems().add(solarSystem);
		
		MenuItem outerSolarSystem = new MenuItem("Outer Solar System");
		outerSolarSystem.setOnAction(actionEvent -> Main.restart(Systems.outerSolarSystem()));
		subMenuReal.getItems().add(outerSolarSystem);

		MenuItem earthSystem = new MenuItem("Earth System");
		earthSystem.setOnAction(actionEvent -> Main.restart(Systems.earthSystem()));
		subMenuReal.getItems().add(earthSystem);

		MenuItem marsSystem = new MenuItem("Mars System");
		marsSystem.setOnAction(actionEvent -> Main.restart(Systems.marsSystem()));
		subMenuReal.getItems().add(marsSystem);

		MenuItem jupiterSystem = new MenuItem("Jupiter System");
		jupiterSystem.setOnAction(actionEvent -> Main.restart(Systems.jupiterSystem()));
		subMenuReal.getItems().add(jupiterSystem);

		MenuItem saturnSystem = new MenuItem("Saturn System");
		saturnSystem.setOnAction(actionEvent -> Main.restart(Systems.saturnSystem()));
		subMenuReal.getItems().add(saturnSystem);

		/*
		 * Particle Tests
		 */
		Menu subMenuParticles = new Menu("Particles");
		this.getItems().add(subMenuParticles);

		MenuItem saturnUranusEncounter = new MenuItem("Saturn Uranus Encounter");
		saturnUranusEncounter.setOnAction(actionEvent -> Main.restart(Systems.saturnUranusEncounter()));
		subMenuParticles.getItems().add(saturnUranusEncounter);

		MenuItem binaryWithRings = new MenuItem("Binary with Rings");
		binaryWithRings.setOnAction(actionEvent -> Main.restart(Systems.binaryWithRings()));
		subMenuParticles.getItems().add(binaryWithRings);

		MenuItem particleField = new MenuItem("Particle Field");
		particleField.setOnAction(actionEvent -> Main.restart(Systems.particleField()));
		subMenuParticles.getItems().add(particleField);

		MenuItem flyThroughParticleField = new MenuItem("Fly through Particle Field");
		flyThroughParticleField.setOnAction(actionEvent -> Main.restart(Systems.flyThroughParticleField()));
		subMenuParticles.getItems().add(flyThroughParticleField);

		/*
		 * Everything else
		 */
		MenuItem randomMoons = new MenuItem("Random Moons");
		randomMoons.setOnAction(actionEvent -> Main.restart(Systems.randomMoons()));
		this.getItems().add(randomMoons);

		MenuItem randomPlanets = new MenuItem("Random Planets");
		randomPlanets.setOnAction(actionEvent -> Main.restart(Systems.randomPlanets()));
		this.getItems().add(randomPlanets);
		
		MenuItem jupiterFlyby = new MenuItem("Jupiter Flyby");
		jupiterFlyby.setOnAction(actionEvent -> Main.restart(Systems.jupiterFlyby()));
		this.getItems().add(jupiterFlyby);
		
		MenuItem earthMarsCollision = new MenuItem("Earth Mars Collision");
		earthMarsCollision.setOnAction(actionEvent -> Main.restart(Systems.earthMarsCollision()));
		this.getItems().add(earthMarsCollision);
		
		MenuItem binaryStar = new MenuItem("Binary Star");
		binaryStar.setOnAction(actionEvent -> Main.restart(Systems.binaryStar()));
		this.getItems().add(binaryStar);
		
		MenuItem sym8 = new MenuItem("Sym 8");
		sym8.setOnAction(actionEvent -> Main.restart(Systems.sym8()));
		this.getItems().add(sym8);
		
		MenuItem empty = new MenuItem("Empty");
		empty.setOnAction(actionEvent -> Main.restart(Systems.empty()));
		this.getItems().add(empty);
		
		MenuItem iss = new MenuItem("ISS Orbit");
		iss.setOnAction(actionEvent -> Main.restart(Systems.iss()));
		this.getItems().add(iss);
	}

}
