package window;

import javafx.scene.paint.Color;
import simulation.Main;

public class ViewSettings {

	public static Color background = Color.rgb(13, 21, 45);

	public static Color textColor = Color.WHITE;

	public static Color trailColor = Color.WHITE;

	public static Color planetSelectionColor = Color.WHITE;

	public static double minSize = 2;

	public static void setDarkTheme() {
		background = Color.rgb(13, 21, 45);
		textColor = Color.WHITE;
		trailColor = Color.WHITE;
		planetSelectionColor = Color.WHITE;
		Main.win.updateColors();
	}
	
	public static void setLightTheme() {
		background = Color.rgb(213, 235, 242);
		textColor = Color.BLACK;
		trailColor = Color.RED;
		planetSelectionColor = Color.BLACK;
		Main.win.updateColors();
	}

}
