package windows;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import simulation.Main;
import simulation.Planet;
import simulation.Simulation;
import utils.Utils;

/**
 * Implements the information group for the main window.
 * 
 * @author Jan Muskalla
 *
 */
public class InfoGroup extends Group {

	private Label spsLabel;
	private Label pastTimeLabel;
	private Label orbitModeLabel;
	private Label infoLabel;

	/**
	 * Creates an information group.
	 */
	public InfoGroup() {
		super();

		spsLabel = new Label();
		spsLabel.relocate(3, 25);
		spsLabel.setTextFill(Color.BLACK);

		pastTimeLabel = new Label();
		pastTimeLabel.relocate(3, 40);
		pastTimeLabel.setTextFill(Color.BLACK);

		orbitModeLabel = new Label();
		orbitModeLabel.relocate(3, 55);

		infoLabel = new Label();
		infoLabel.relocate(3, 85);
		infoLabel.setTextFill(Color.BLACK);

		this.getChildren().addAll(spsLabel, pastTimeLabel, orbitModeLabel, infoLabel);
	}

	int[] spsArray = new int[60];
	int spsFinal = 0;

	/**
	 * updates the info labels
	 */
	public void updateInfo() {
		
		if (!Main.simulation.isPaused()) {
			
			for (int i = 0; i < spsArray.length; i++) {
				if (spsArray[i] == 0) {
					spsArray[i] = Main.simulation.getSpsCounter() * 60;
					Main.simulation.resetSpsCounter();
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
			pastTimeLabel.setText(Utils.getTimeString());
		}

		Planet selPl = Main.window.getSelectedPlanet();
		
		if (selPl != null) {
			infoLabel.setText(selPl.getName() + "\nMass: " + selPl.getMass() + " kg\nRadius: "
					+ Math.round(selPl.getRadius() / 1000.0) + " km\nVelocity: " + (int) selPl.getVel().norm() + " m/s"
					+ "\nTime: x" + (int) (Main.simulation.getTime() * Simulation.SPS));
		} else {
			infoLabel.setText(
					Main.simulation.getConstellation().getName() + "\nObjects: " + Main.simulation.getPlanets().length
							+ "\nTime: x" + (int) (Main.simulation.getTime() * Simulation.SPS));
		}
	}

	/**
	 * updates the orbit mode label
	 * 
	 * @param b
	 */
	public void setOrbitMode(boolean b) {
		if (b) {
			orbitModeLabel.setText("Orbit Mode: On");
			orbitModeLabel.setTextFill(Color.RED);
		} else {
			orbitModeLabel.setText("Orbit Mode: Off");
			orbitModeLabel.setTextFill(Color.BLACK);
		}
	}

}
