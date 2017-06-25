package simulation;

import constellations.Constellation;
import constellations.StartConditions;
import javafx.application.Application;
import javafx.stage.Stage;
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
	 * Starts the first simulation with the default constellation and opens the
	 * main window.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		sim = new Simulation(StartConditions.getSaturnWithRings());
		win = new Window();
		win.start(primaryStage);
		sim.run();
	}

	/**
	 * Starts a new simulation with a given constellation in a default window.
	 * 
	 * @param newConstellation
	 */
	public static void restart(Constellation newConstellation) {
		sim.stop();
		sim = new Simulation(newConstellation);
		sim.run();
		win.resetAndLoad(sim);
	}

	/**
	 * Starts a new simulation with the current constellation in a default
	 * window.
	 */
	public static void restart() {
		sim.stop();
		sim = new Simulation(sim.getConstellation());
		sim.run();
		win.resetAndLoad(sim);
	}

}
