package window;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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
public class InfoPane extends Pane {

	private Label spsLabel;
	private Label pastTimeLabel;
	private Label orbitModeLabel;
	private Label infoLabel;

	Button deccButton, reButton, accButton;

	/**
	 * Creates an information group.
	 */
	public InfoPane() {
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

		deccButton = new Button("<<");
		deccButton.setOnAction(actionEvent -> Main.sim.multTime(0.5));

		reButton = new Button("O");
		reButton.setOnAction(actionEvent -> Main.sim.resetTime());

		accButton = new Button(">>");
		accButton.setOnAction(actionEvent -> Main.sim.multTime(2));
		
		relocateTimeButtons();

		this.getChildren().addAll(spsLabel, pastTimeLabel, orbitModeLabel, infoLabel, deccButton, accButton, reButton);
	}

	int[] spsArray = new int[60];
	int spsFinal = 0;

	/**
	 * updates the info labels
	 */
	public void updateInfo() {

		if (!Main.sim.isPaused()) {

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
			pastTimeLabel.setText(Utils.getTimeString(Main.sim.getSecondsCounter()));
		}

		Planet selPl = Main.win.getSelectedPlanet();

		if (selPl != null) {
			infoLabel.setText(selPl.getName() + "\nMass: " + selPl.getMass() + " kg\nRadius: "
					+ Math.round(selPl.getRadius() / 1000.0) + " km\nVelocity: " + (int) selPl.getVel().norm() + " m/s"
					+ "\nTime: x" + (int) (Main.sim.getTime() * Simulation.SPS));
		} else {
			infoLabel.setText(Main.sim.getConstellation().getName() + "\nObjects: " + Main.sim.getPlanets().length
					+ "\nTime: x" + (int) (Main.sim.getTime() * Simulation.SPS));
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
			orbitModeLabel.setTextFill(Color.BLACK);
		} else {
			orbitModeLabel.setText("Orbit Mode: Off");
			orbitModeLabel.setTextFill(Color.RED);
		}
	}

	public void relocateTimeButtons() {
		deccButton.relocate(Main.win.getX() - 110, 27);
		reButton.relocate(Main.win.getX() - 70, 27);
		accButton.relocate(Main.win.getX() - 38, 27);
	}

}
