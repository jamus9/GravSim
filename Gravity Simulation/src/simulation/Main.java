package simulation;

import constellations.Constellation;
import constellations.StartConditions;
import javafx.application.Application;
import javafx.stage.Stage;
import windows.Window;

/**
 * Starts and restarts the simulation and window.
 * 
 * @author Jan Muskalla
 */
public class Main extends Application {

	/** the current simulation */
	public static Simulation simulation;

	/** the main window */
	public static Window window;

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
		simulation = new Simulation(StartConditions.getEarthSystem());
		window = new Window();
		window.start(primaryStage);
	}

	/**
	 * Starts a new simulation with a given constellation in a default window.
	 * 
	 * @param newConstellation
	 */
	public static void restart(Constellation newConstellation) {
		simulation = new Simulation(newConstellation);
		window.reset();
	}

	/**
	 * Starts a new simulation with the current constellation in a default
	 * window.
	 */
	public static void restart() {
		simulation = new Simulation(simulation.getConstellation());
		window.reset();
	}

}
