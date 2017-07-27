package bodies;

import java.util.LinkedList;
import javafx.animation.FadeTransition;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import simulation.Main;
import utils.Vec;
import window.ViewSettings;

/**
 * A trail that shows the path of a planet. A trail is made up of lines saved in
 * lineList and the original coordinates are saved in coordList so the trail can
 * be moved in the window.
 * 
 * @author Jan Muskalla
 *
 */
public class Trail {

	/** the parent planet */
	Planet parent;

	/** the trail lines */
	private LinkedList<Line> lineList;

	/** the real coordinates of the trail */
	private LinkedList<Vec> coordList;

	/**
	 * creates a new trail
	 * 
	 * @param parent
	 */
	public Trail(Planet parent) {
		this.parent = parent;
		lineList = new LinkedList<Line>();
		coordList = new LinkedList<Vec>();
	}

	/**
	 * adds a new trail to the list and the window and removes old lines
	 * 
	 * @param pos
	 */
	public void addLine() {
		Vec tp = Main.win.transform(parent.getPos());
		Vec tplast = Main.win.transform(coordList.getLast());

		// only draw new line if planet moved more than trailLength
		if (tp.sub(tplast).getRadius() > ViewSettings.trailLength) {

			// the new line
			Line newLine = new Line(tplast.getX(), tplast.getY(), tp.getX(), tp.getY());
			newLine.setStroke(parent.getColor());
			newLine.setStrokeWidth(ViewSettings.trailWidth);

			FadeTransition ft = new FadeTransition(Duration.seconds(ViewSettings.trailSeconds), newLine);
			ft.setFromValue(1.0);
			ft.setToValue(0.0);
			ft.play();

			// add the line to the list and the window
			lineList.add(newLine);
			Main.win.addTrail(newLine);

			// delete first line if it is invisible
			if (lineList.getFirst().getOpacity() == 0.0) {
				Line oldLine = lineList.getFirst();
				((Pane) oldLine.getParent()).getChildren().remove(oldLine);
				lineList.removeFirst();
				coordList.removeFirst();
			}

			// save position for the next line start
			savePosition();
		}
	}

	/**
	 * Saves the current position of the planet in coordList.
	 */
	public void savePosition() {
		coordList.add(new Vec(parent.getPos()));
	}

	/**
	 * Translate all Lines in lineList with help of orbitPoints.
	 */
	public void translate() {
		Vec newStart, newEnd;
		Line line;

		for (int i = 0; i < lineList.size(); i++) {
			line = lineList.get(i);

			// get the coordinates and use the current transform
			newStart = Main.win.transform(coordList.get(i));
			newEnd = Main.win.transform(coordList.get(i + 1));

			line.setStartX(newStart.getX());
			line.setStartY(newStart.getY());
			line.setEndX(newEnd.getX());
			line.setEndY(newEnd.getY());
		}
	}

	/**
	 * Deletes all orbit data.
	 */
	public void delete() {
		for (Line line : lineList)
			((Pane) line.getParent()).getChildren().remove(line);
		lineList.clear();
		coordList.clear();
	}

}
