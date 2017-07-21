package window;

import bodies.Particle;
import bodies.Planet;
import javafx.scene.paint.Color;
import simulation.Main;

/**
 * Settings for the appearance of the application
 * 
 * @author Jan Muskalla
 *
 */
public class ViewSettings {
	
	public static boolean darkTheme = true;

	/** colors */
	public static Color background = Color.rgb(13, 21, 45);
	public static Color textColor = Color.WHITE;
	public static Color planetSelectionColor = Color.WHITE;
	public static Color bodyColor = Color.WHITE;
	
	public static double uiOpacity = 0.7;

	/** sizes */
	public static double minBodySize = 1;
	public static double trailWidth = 1;
	public static int trailSeconds = 30;
	public static int trailLength = 3;

	public static void setDarkTheme() {
		darkTheme = true;
		background = Color.rgb(13, 21, 45);
		textColor = Color.WHITE;
		planetSelectionColor = Color.WHITE;
		bodyColor = Color.WHITE;
		updateColors();
	}

	public static void setLightTheme() {
		darkTheme = false;
		background = Color.rgb(213, 235, 242);
		textColor = Color.BLACK;
		planetSelectionColor = Color.BLACK;
		bodyColor = Color.BLACK;
		updateColors();
	}

	private static void updateColors() {
		Main.win.updateColors();

		for (Planet p : Main.sim.getPlanetList()) {
			p.getLabel().setTextFill(ViewSettings.textColor);
		}

		for (Particle p : Main.sim.getParticleList()) {
			p.getCircle().setFill(ViewSettings.bodyColor);
		}

		// system
		for (Planet p : Main.sim.getConstellation().getPlanetList()) {
			p.getLabel().setTextFill(ViewSettings.textColor);
		}

		for (Particle p : Main.sim.getConstellation().getParticleList()) {
			p.getCircle().setFill(ViewSettings.bodyColor);
		}
	}

}
