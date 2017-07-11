package window;

import javafx.scene.paint.Color;
import simulation.Main;

/**
 * Settings for the appearance of the application
 * 
 * @author Jan Muskalla
 *
 */
public class ViewSettings {

	/** colors */
	public static Color background = Color.rgb(13, 21, 45);
	public static Color textColor = Color.WHITE;
	public static Color planetSelectionColor = Color.WHITE;
	public static Color bodyColor = Color.WHITE;

	/** sizes */
	public static double minBodySize = 1;
	public static double trailWidth = 1.5;
	public static int trailSeconds = 30;
	public static int trailLength = 3;

	public static void setDarkTheme() {
		background = Color.rgb(13, 21, 45);
		textColor = Color.WHITE;
		planetSelectionColor = Color.WHITE;
		bodyColor = Color.WHITE;
		Main.win.updateColors();
	}
	
	public static void setLightTheme() {
		background = Color.rgb(213, 235, 242);
		textColor = Color.BLACK;
		planetSelectionColor = Color.BLACK;
		bodyColor = Color.BLACK;
		Main.win.updateColors();
	}

}
