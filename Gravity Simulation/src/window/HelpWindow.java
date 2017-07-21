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
		Scene scene = new Scene(root, 260, 330, ViewSettings.background);

		Label helpLabel = new Label();
		helpLabel.relocate(10, 10);
		helpLabel.setText(
				  "Restart			R\n"
				+ "Reset View		E\n"
				+ "Pause			Space or P\n"
				+ "Accelerate Time	.\n"
				+ "Decelerate Time	,\n"
				+ "Reset Time		-\n"
				+ "Trails			T\n"
				+ "Labels			L\n"
				+ "Information		I\n"
				+ "Orbit Mode		O\n"
				+ "Select Planet		Left Mouse Button\n"
				+ "Add new Planet	Right Mouse Button or A\n"
				+ "Delete Planet		Delete\n"
				+ "Reset View		Middle Mouse Button\n"
				+ "Drag View		Left Mouse Button\n"
				+ "Zoom			Mouse Scroll\n"
				+ "Fullscreen		F\n"
				+ "Exit				Esc");
		
		helpLabel.setTextFill(ViewSettings.textColor);
		
		root.getChildren().add(helpLabel);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
