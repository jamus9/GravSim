package simulation;

import bodies.Constellation;
import javafx.application.Application;
import javafx.stage.Stage;
import systems.Systems;
import window.Window;

/**
 * Starts and restarts the simulation and window.
 * 
 * @author Jan Muskalla
 * 
 */
public class Main extends Application {

	/** the current simulation */
	public static Simulation sim;

	/** the main window */
	public static Window win;

	/** Launches the JavaFX application. */
	public static void main(String... args) {
		Application.launch(args);
	}

	/**
	 * Starts the first simulation with the default Constellation and opens the main
	 * window.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		sim = new Simulation(Systems.solarSystem());
		win = new Window();
		win.start(primaryStage);
		sim.run();
	}

	/**
	 * Starts a new simulation with a given Constellation in a default window.
	 * 
	 * @param newConstellation
	 */
	public static void restart(Constellation newConstellation, boolean resetSave) {
		sim.stop();
		sim = new Simulation(newConstellation);
		sim.run();
		win.resetAndLoad(sim);
		if (resetSave)
			win.resetSaved();
	}

	public static void restart(Constellation newConstellation) {
		restart(newConstellation, true);
	}

	/**
	 * Starts a new simulation with the current Constellation in a default window.
	 */
	public static void restart() {
		restart(sim.getConstellation());
	}

}
