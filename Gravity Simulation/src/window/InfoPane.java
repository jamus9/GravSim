package window;

import bodies.Planet;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import simulation.Main;
import simulation.Simulation;
import utils.Utils;

/**
 * Implements the information group for the main window.
 * 
 * @author Jan Muskalla
 *
 */
public class InfoPane extends Pane {
	
	private static Color color = Color.WHITE;

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
		
		int y = 16;

		spsLabel = new Label();
		spsLabel.relocate(3, 25);
		spsLabel.setTextFill(color);

		pastTimeLabel = new Label();
		pastTimeLabel.relocate(3, spsLabel.getLayoutY() + y);
		pastTimeLabel.setTextFill(color);

		orbitModeLabel = new Label();
		orbitModeLabel.relocate(3, pastTimeLabel.getLayoutY() + y);

		infoLabel = new Label();
		infoLabel.relocate(3, orbitModeLabel.getLayoutY() + y*2);
		infoLabel.setTextFill(color);

		deccButton = new Button("<<");
		deccButton.setOnAction(actionEvent -> Main.sim.multTime(0.5));

		reButton = new Button("R");
		reButton.setOnAction(actionEvent -> Main.sim.resetTime());

		accButton = new Button(">>");
		accButton.setOnAction(actionEvent -> Main.sim.multTime(2));

		// put button at correct position
		relocateTimeButtons();

		this.getChildren().addAll(spsLabel, pastTimeLabel, orbitModeLabel, infoLabel, reButton, deccButton, accButton);
	}

	int[] spsArray = new int[60];
	int spsFinal = 0;

	/**
	 * updates the info labels
	 */
	public void updateInfo() {

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
			pastTimeLabel.setText(Utils.getTimeString(Main.sim.getSecondsCounter()));
		}
		
		// general information
		String infoText = Main.sim.getName() + "\nObjects: " + Main.sim.getNumberOfObjects()
		+ "\nTime: x" + ((int) (Main.sim.getTime() * Simulation.getSps()));

		// info about selected planet
		Planet selPl = Main.win.getSelectedPlanet();
		if (selPl != null) {
			String name = selPl.getName() + "\n";
			String mass = "Mass: " + selPl.getMass() + " kg\n";
			String rad = "Radius: " + Math.round(selPl.getRadius() / 1000.0) + " km\n";
			String vel = "Velocity: " + (int) selPl.getVel().norm() + " m/s" + "\n";
			infoText += "\n\n" + name + mass + rad + vel;
		}
		
		infoLabel.setText(infoText);
	}

	/**
	 * updates the orbit mode label
	 * 
	 * @param boo
	 */
	public void setOrbitMode(boolean boo) {
		if (boo) {
			orbitModeLabel.setText("Orbit Mode: On");
			orbitModeLabel.setTextFill(color);
		} else {
			orbitModeLabel.setText("Orbit Mode: Off");
			orbitModeLabel.setTextFill(Color.RED);
		}
	}

	public void relocateTimeButtons() {
		int y = 30;
		deccButton.relocate(Main.win.getWidth() - 110, y);
		reButton.relocate(Main.win.getWidth() - 70, y);
		accButton.relocate(Main.win.getWidth() - 38, y);
	}

}
