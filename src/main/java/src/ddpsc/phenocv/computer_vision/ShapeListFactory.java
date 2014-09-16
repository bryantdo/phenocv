package src.ddpsc.phenocv.computer_vision;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import src.ddpsc.phenocv.utility.ReversableObjectFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjmcentee
 */
class ShapeListFactory extends ReversableObjectFactory<List<Shape>, ShapeCollection> implements Releasable{

    public ShapeListFactory() {
        super();
    }

    @Override
    public void release() {
        ReleaseContainer.releaseAll(calculatedObject);
    }

    @Override
    protected List<Shape> calculate(ShapeCollection shapeCollection) {
        List<Shape> shapes = new ArrayList<Shape>();
        List<MatOfPoint> contours = shapeCollection.contours;

        Mat hierarchy = shapeCollection.hierarchy;
        int linearRelations[] = new int[contours.size() * 4];
        hierarchy.get(0, 0, linearRelations);
        hierarchy.release();

        int relationArray[][] = convertRelations(linearRelations);

        int relationIndex = 0;
        while(relationIndex < relationArray.length) {
            int relation[] = relationArray[relationIndex];
            MatOfPoint parentContour = contours.get(relationIndex);

            // No children: add contour to shape list
            if (relation[Shape.FIRST_CHILD] == -1) {
                shapes.add(new Shape(parentContour));
                relationIndex++;
            }

            // Has children: get children, build the shape, add it to the list
            else {
                List<MatOfPoint> childContours = new ArrayList<MatOfPoint>();

                // The children are listed in order directly below the parent in CCOMP
                relationIndex++; // At first child

                do {
                    relation = relationArray[relationIndex];
                    childContours.add(contours.get(relationIndex));
                    relationIndex++; // on exiting do-while loop, leaves us at the next parent
                } while (relation[Shape.NEXT] != -1);

                shapes.add(new Shape(parentContour, childContours));
            }
        }

        return shapes;
    }

    private int[][] convertRelations(int[] relations) {
        int fixedRelations[][] = new int[relations.length/4][4];
        for (int i = 0; i < relations.length / 4; i++) {
            int row[] = new int[4];
            row[0] = relations[4*i];
            row[1] = relations[4*i+1];
            row[2] = relations[4*i+2];
            row[3] = relations[4*i+3];

            fixedRelations[i] = row;
        }

        return fixedRelations;
    }
}
