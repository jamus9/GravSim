package window;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Implements the help window
 * 
 * @author Jan Muskalla
 * 
 */
public class HelpWindow extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Help");

		Group root = new Group();
		Scene scene = new Scene(root, 240, 310, ViewSettings.background);

		Label helpLabel = new Label();
		helpLabel.relocate(10, 10);
		helpLabel.setText(
				  "Restart			R\n"
				+ "Reset view		E\n"
				+ "Pause			Space or P\n"
				+ "Accelerate time	.\n"
				+ "Decelerate time	,\n"
				+ "Reset time		-\n"
				+ "Vectors			V\n"
				+ "Trails			T\n"
				+ "Labels			L\n"
				+ "Information		I\n"
				+ "Select planet		Left mouse button\n"
				+ "Add new planet	Right mouse button\n"
				+ "Orbit mode		M\n"
				+ "Reset view		Middle mouse button\n"
				+ "Drag view		Left mouse button\n"
				+ "Zoom			Mouse scroll\n"
				+ "Exit				Esc");
		
		helpLabel.setTextFill(ViewSettings.textColor);
		
		root.getChildren().add(helpLabel);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
