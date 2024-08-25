package collinear_points;

import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final List<LineSegment> lineSegments;

    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points
        // check
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }

        // the argument must be immutable -> copy the array of points
        Point[] pointsToSort = points.clone();

        // sort the array by natural order
        Arrays.sort(pointsToSort);

        // find line segments
        lineSegments = new ArrayList<>();
        for (int i = 0; i < pointsToSort.length; i++) { // find line segments
            for (int j = i + 1; j < pointsToSort.length; j++) {
                // check match points
                if (pointsToSort[i].compareTo(pointsToSort[j]) == 0) {
                    throw new IllegalArgumentException();
                }

                for (int k = j + 1; k < pointsToSort.length; k++) {
                    for (int m = k + 1; m < pointsToSort.length; m++) {
                        // find the new line segment
                        if (pointsToSort[i].slopeTo(pointsToSort[j]) == pointsToSort[j].slopeTo(pointsToSort[k]) &&
                                pointsToSort[i].slopeTo(pointsToSort[j]) == pointsToSort[k].slopeTo(pointsToSort[m])) {
                            lineSegments.add(new LineSegment(pointsToSort[i], pointsToSort[m]));
                        }
                    }
                }
            }
        }

    }

    public int numberOfSegments() { // the number of line segments
        return lineSegments.size();
    }

    public LineSegment[] segments() { // the line segments
        return lineSegments.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {
        StdDraw.setScale(0, 100);

        // create points
        Point[] points = {new Point(10, 10), new Point(50, 50), new Point(30, 30), new Point(40, 40),
                new Point(40, 20), new Point(25, 20), new Point(77, 88),
                new Point(55, 5), new Point(78, 40), new Point(60, 20), new Point(77, 20)};

        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
        System.out.println(bruteCollinearPoints.numberOfSegments());
        System.out.println(Arrays.toString(bruteCollinearPoints.segments()));

        // draw points
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLUE);
        for (Point p : points) {
            p.draw();
        }
    }
}
