package UMLEditor.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

/**
 * Created by beltre on 12/4/16.
 */
public class Dependency extends UmlLine implements NodeEditMenu {

    private Line minSpreadLine;
    private Line maxSpreadLine;
    final Group group = new Group();
    Rotate rotate = new Rotate();
    private Images img = new Images();

    public Dependency(Anchors a1, Anchors a2)
    {
        super(a1, a2);

        setDashed();

        //slope of the lines
        double deltaX = this.getStartY() - this.getEndY();
        double deltaY = this.getStartX() - this.getEndX();
        //gets you the degress of the x axis(on the left) of the second clicked node
        double slopeInDegrees = Math.toDegrees(Math.atan2(deltaX, deltaY));

        minSpreadLine = new Line(0, 0,
                -10, -10);

        maxSpreadLine = new Line(0, 0,
                -10, 10);

        group.getChildren().addAll(minSpreadLine, maxSpreadLine);

        group.getTransforms().add(rotate);
        rotate.setAngle(-slopeInDegrees);

        group.setTranslateX(this.getEndX());
        group.setTranslateY(this.getEndY());

    }

    /**
     * @return Returns the line 135 degrees clockwise to the main line.
     */
    public Node getLine1() {
        return minSpreadLine;
    }

    /**
     * @return Returns the line 135 degrees counterclockwise to the main line.
     */
    public Node getLine2() {
        return maxSpreadLine;
    }

    public Group arrowHead() { return group; }

    @Override
    public void deleteSelf()
    {
        Pane pane = (Pane)this.getParent();
        pane.getChildren().remove(group);
        anchorPoint1.deleteLine(id);
        anchorPoint2.deleteLine(id);
    }

    @Override
    public void updateAnchorPoints()
    {
        double min = 999999999;
        point1Int = 0;
        point2Int = 0;
        for(int i = 0; i < anchorPoint1.getAnchorCount(); ++i)
        {
            startingAnchor = new Point2D(anchorPoint1.getAnchorPoint(i).getX(), anchorPoint1.getAnchorPoint(i).getY());

            for (int j = 0; j < anchorPoint2.getAnchorCount(); ++j)
            {
                endingAnchor = new Point2D(anchorPoint2.getAnchorPoint(j).getX(), anchorPoint2.getAnchorPoint(j).getY());
                if (startingAnchor.distance(endingAnchor) < min)
                {
                    min = startingAnchor.distance(endingAnchor);
                    point1Int = i;
                    point2Int = j;
                }
            }
        }

        this.setStartX(anchorPoint1.getAnchorPoint(point1Int).getX());
        this.setStartY(anchorPoint1.getAnchorPoint(point1Int).getY());
        this.setEndX(anchorPoint2.getAnchorPoint(point2Int).getX());
        this.setEndY(anchorPoint2.getAnchorPoint(point2Int).getY());

        double deltaX = this.getStartY() - this.getEndY();
        double deltaY = this.getEndX() - this.getStartX();
        //gets you the degress of the x axis(on the left) of the second clicked node
        double slopeInDegrees = Math.toDegrees(Math.atan2(deltaX,deltaY));

        if (group != null){
            group.setTranslateX(this.getEndX());
            group.setTranslateY(this.getEndY());
            rotate.setAngle(-slopeInDegrees);
        }

    }

    @Override
    public void generatePanel(VBox v)
    {
        v.getChildren().clear();
        DropShadow dropShadow = new DropShadow();
        dropShadow.setHeight(40.0);
        dropShadow.setWidth(40.0);
        //dropShadow.setColor(Color.RED);

        Button deleteB = new Button();
        deleteB.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(deleteB, Priority.ALWAYS);
        ImageView deleteBImg = new ImageView(img.getDelete());
        deleteBImg.setFitHeight(45.0);
        deleteBImg.setFitWidth(Double.MAX_VALUE);
        deleteBImg.setPreserveRatio(true);
        deleteB.setGraphic(deleteBImg);
        deleteB.setTooltip(new Tooltip("Delete Class"));
        deleteB.setOnAction((ActionEvent e) -> {
            deleteSelf();
            v.getChildren().clear();
        });
        deleteB.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        deleteB.setEffect(dropShadow);
                    }
                });
        deleteB.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        deleteB.setEffect(null);
                    }
                });

        v.getChildren().addAll(deleteB);

    }
}
