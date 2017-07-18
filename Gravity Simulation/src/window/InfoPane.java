package window;

import bodies.Planet;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import simulation.Main;

/**
 * Implements the information group for the main window.
 * 
 * @author Jan Muskalla
 *
 */
public class InfoPane extends Pane {

//	private static Color textColor = Color.LIGHTGREY;

	/** info label */
	private Label spsLabel;
	private Label pastTimeLabel;
	private Label infoLabel;

	/** time control buttons */
	Button deccButton, reButton, accButton;

	/**
	 * Creates an information group.
	 */
	public InfoPane() {
		super();

		int y = 16;

		spsLabel = new Label();
		spsLabel.relocate(3, 25);
		spsLabel.setTextFill(ViewSettings.textColor);

		pastTimeLabel = new Label();
		pastTimeLabel.relocate(3, spsLabel.getLayoutY() + y);
		pastTimeLabel.setTextFill(ViewSettings.textColor);

		infoLabel = new Label();
		infoLabel.relocate(3, pastTimeLabel.getLayoutY() + y * 2);
		infoLabel.setTextFill(ViewSettings.textColor);

		deccButton = new Button("<<");
		deccButton.setOnAction(actionEvent -> Main.sim.multTime(0.5));

		reButton = new Button("R");
		reButton.setOnAction(actionEvent -> Main.sim.resetTime());

		accButton = new Button(">>");
		accButton.setOnAction(actionEvent -> Main.sim.multTime(2));

		// put button at correct position
		relocateTimeButtons();

		this.getChildren().addAll(spsLabel, pastTimeLabel, infoLabel, reButton, deccButton, accButton);
	}

	int[] spsArray = new int[60];
	int spsFinal = 0;

	/**
	 * updates the info labels
	 */
	public void updateInfo() {

		// time label
		if (!Main.sim.isPaused()) {

			// sps 1s average calculator
			for (int i = 0; i < spsArray.length; i++) {
				if (spsArray[i] == 0) {
					spsArray[i] = Main.sim.getSpsCounter() * 60;
					Main.sim.resetSpsCounter();
					break;
				}
			}
			if (spsArray[spsArray.length - 1] != 0) {
				double added = 0;
				for (int i : spsArray)
					added += i;
				spsFinal = (int) (added / spsArray.length);
				spsArray = new int[spsArray.length];
			}
			spsLabel.setText("Steps/Sec: " + spsFinal);

			// past simulation time
			pastTimeLabel.setText(getTimeString(Main.sim.getSecondsCounter()));
		}

		// general information
		String infoText = Main.sim.getName() + "\nObjects: " + Main.sim.getNumberOfObjects() + "\nTime: x"
				+ ((int) (Main.sim.getTime() * Main.sim.getSps()));

		// info about selected planet
		Planet selPl = Main.win.getSelectedPlanet();
		if (selPl != null) {
			String name = selPl.getName() + "\n";
			String mass = "Mass: " + selPl.getMass() + " kg\n";
			String rad = "Radius: " + Math.round(selPl.getRadius() / 1000.0) + " km\n";
			String dens = "Density: " + (int) selPl.getDensity() + " kg/m^3\n";
			String vel = "Velocity: " + (int) selPl.getVel().norm() + " m/s" + "\n";
			infoText += "\n\n" + name + rad + mass + dens + vel;
		}

		infoLabel.setText(infoText);
	}

	/**
	 * sets the time button to the correct position
	 */
	public void relocateTimeButtons() {
		int y = 30;
		deccButton.relocate(Main.win.getWidth() - 110, y);
		reButton.relocate(Main.win.getWidth() - 70, y);
		accButton.relocate(Main.win.getWidth() - 38, y);
	}
	
	void updateTextColor(Color c) {
		spsLabel.setTextFill(c);
		pastTimeLabel.setTextFill(c);
		infoLabel.setTextFill(c);
	}
	
	/**
	 * Gives the past seconds in a nice time format.
	 * 
	 * @return the past time as a String
	 */
	public static String getTimeString(double seconds) {
		int secs = (int) seconds;
		int mins, hours, days, years;

		years = secs / 31536000;
		secs -= years * 31536000;
		days = secs / 86400;
		secs -= days * 86400;
		hours = secs / 3600;
		secs -= hours * 3600;
		mins = secs / 60;
		secs -= mins * 60;

		if (years > 0)
			return String.format("Y: %1$d D: %2$d", years, days);
		else if (days > 0)
			return String.format("Y: %1$d D: %2$d H: %3$d", years, days, hours);
		else if (hours > 0)
			return String.format("Y: %1$d D: %2$d H: %3$d M: %4$d", years, days, hours, mins);
		else
			return String.format("Y: %1$d D: %2$d H: %3$d M: %4$d S: %5$d", years, days, hours, mins, secs);
	}

}
