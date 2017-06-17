package window;

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
	private Label orbitMode;
	private Label infoLabel;

	/**
	 * Creates the info group.
	 */
	public InfoGroup() {
		super();

		spsLabel = new Label();
		spsLabel.relocate(3, 25);
		spsLabel.setTextFill(Color.BLACK);

		pastTimeLabel = new Label();
		pastTimeLabel.relocate(0, 40);
		pastTimeLabel.setTextFill(Color.BLACK);
		
		orbitMode = new Label();
		orbitMode.relocate(3, 55);

		infoLabel = new Label();
		infoLabel.relocate(3, 85);
		infoLabel.setTextFill(Color.BLACK);
		
		this.getChildren().addAll(spsLabel, pastTimeLabel, orbitMode, infoLabel);
	}

	/**
	 * updates the info labels
	 */
	public void updateInfo() {

		if (!Main.simulation.isPaused()) {
			spsLabel.setText("Steps/Sec: " + (Main.simulation.getSpsCounter() * 60));
			Main.simulation.resetSpsCounter();
			pastTimeLabel.setText(Utils.pastTime());
		}

		Planet selPl = Main.window.getSelectedPlanet();

		if (selPl != null) {
			infoLabel.setText(selPl.getName() + "\nMasse: " + selPl.getMass() + " kg\nRadius: "
					+ (int) (selPl.getRadius() / 1000.0) + " km\nGeschwindigkeit: " + (int) selPl.getVel().norm() + " m/s"
					+ "\nZeit: x" + (int) (Main.simulation.getTime() * Simulation.SPS));
		} else {
			infoLabel.setText(
					Main.simulation.getConstellation().getName() + "\nObjekte: " + Main.simulation.getPlanets().length
							+ "\nZeit: x" + (int) (Main.simulation.getTime() * Simulation.SPS));
		}
	}
	
	/**
	 * updates the orbit mode label
	 * 
	 * @param b
	 */
	public void setOrbitMode(boolean b) {
		if (b) {
			orbitMode.setText("Orbit Mode: On");
			orbitMode.setTextFill(Color.RED);
		}else {
			orbitMode.setText("Orbit Mode: Off");
			orbitMode.setTextFill(Color.BLACK);
		}
	}

}
