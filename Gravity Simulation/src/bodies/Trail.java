package bodies;

import java.util.LinkedList;
import javafx.animation.FadeTransition;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import simulation.Main;
import utils.Vec2d;
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
	private LinkedList<Vec2d> coordList;

	/**
	 * creates a new trail
	 * 
	 * @param parent
	 */
	public Trail(Planet parent) {
		this.parent = parent;
		lineList = new LinkedList<Line>();
		coordList = new LinkedList<Vec2d>();
	}

	/**
	 * adds a new trail to the list and the window and removes old lines
	 * 
	 * @param pos
	 */
	public void addLine() {
		Vec2d tp = Main.win.transform(parent.getPos());
		Vec2d tplast = Main.win.transform(coordList.getLast());

		// only draw new line if planet moved more than trailLength
		if (tp.sub(tplast).norm() > 3) {

			// the new line
			Line line = new Line(tplast.getX(), tplast.getY(), tp.getX(), tp.getY());
			line.setStroke(parent.getColor());
			line.setStrokeWidth(ViewSettings.trailWidth);

			FadeTransition ft = new FadeTransition(Duration.seconds(ViewSettings.trailSeconds), line);
			ft.setFromValue(1.0);
			ft.setToValue(0.0);
			ft.play();

			// add the line to the list and the window
			lineList.add(line);
			Main.win.addTrail(line);

			// delete first line if it is invisible
			deleteInvisibleLine();

			// save position for the next line start
			savePosition();
		}
	}

	/**
	 * deletes the first line in the list from the list and the window, if it is
	 * invisible
	 */
	private void deleteInvisibleLine() {
		if (lineList.getFirst().getOpacity() == 0.0) {
			Line line = lineList.getFirst();
			((Pane) line.getParent()).getChildren().remove(line);
			lineList.removeFirst();
			coordList.removeFirst();
		}
	}

	/**
	 * Translate all Lines in lineList with help of orbitPoints.
	 */
	public void translate() {
		Vec2d newStart, newEnd;
		Line line;

		for (int i = 0; i < lineList.size(); i++) {
			line = lineList.get(i);

			// get the original position of the trail and use the current
			// transform
			newStart = Main.win.transform(coordList.get(i));
			newEnd = Main.win.transform(coordList.get(i + 1));

			line.setStartX(newStart.getX());
			line.setStartY(newStart.getY());
			line.setEndX(newEnd.getX());
			line.setEndY(newEnd.getY());
		}
	}

	/**
	 * saves the current position of the planet in coordList
	 */
	public void savePosition() {
		coordList.add(new Vec2d(parent.getPos()));
	}

	/**
	 * Deletes all orbit data.
	 */
	public void delete() {
		for (Line line : lineList) {
			((Pane) line.getParent()).getChildren().remove(line);
		}
		lineList.clear();
		coordList.clear();
	}

}
