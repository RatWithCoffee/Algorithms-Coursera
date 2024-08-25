package collinear_points;

import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {
    private final List<LineSegment> lineSegments;

    public FastCollinearPoints(Point[] points) { // finds all line segments containing 4 or more points
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

        lineSegments = new ArrayList<>();
        // find line segments
        for (int i = 0; i < pointsToSort.length; i++) {

            Arrays.sort(pointsToSort); // sort by the natural order
            // check does any points match
            if (i != pointsToSort.length - 1 && pointsToSort[i].compareTo(pointsToSort[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
            Comparator<Point> slopOrder = pointsToSort[i].slopeOrder(); // sort by the slop
            Arrays.sort(pointsToSort, slopOrder);

            // check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect to points[i]
            int numberOfPoints = 1;
            for (int j = 1; j < pointsToSort.length - 1; j++) {

                if (pointsToSort[0].slopeTo(pointsToSort[j]) == pointsToSort[0].slopeTo(pointsToSort[j + 1])) {  // new point in the line
                    numberOfPoints++;
                }

                if (numberOfPoints >= 3 && // add a new line
                        pointsToSort[0].slopeTo(pointsToSort[j]) != pointsToSort[0].slopeTo(pointsToSort[j + 1]) &&
                        pointsToSort[0].compareTo(pointsToSort[j + 1 - numberOfPoints]) < 0) {
                    lineSegments.add(new LineSegment(pointsToSort[0], pointsToSort[j]));
                    numberOfPoints = 1;
                } else if (numberOfPoints >= 3 && // add new line with points in the end of the array
                        pointsToSort[0].slopeTo(pointsToSort[j]) == pointsToSort[0].slopeTo(pointsToSort[j + 1]) &&
                        pointsToSort[0].compareTo(pointsToSort[j + 2 - numberOfPoints]) < 0 &&
                        j + 1 == points.length - 1) {
                    lineSegments.add(new LineSegment(pointsToSort[0], pointsToSort[j + 1]));
                } else if (pointsToSort[0].slopeTo(pointsToSort[j]) != pointsToSort[0].slopeTo(pointsToSort[j + 1])) { // number of points in the line is less than 4
                    numberOfPoints = 1;
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
        int x = 5;
        int y = 5;
        Point[] points = new Point[36];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(x, y);
            y += 5;
            if (y == 35) {
                x += 5;
                y = 5;
            }
        }

        // Point[] points = {new Point(3, 3), new Point(0, 2), new Point(2, 2), new Point(5, 2), new Point(7, 2)};

        Arrays.sort(points);

        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);
        System.out.println(fastCollinearPoints.numberOfSegments());
        System.out.println(Arrays.toString(fastCollinearPoints.segments()));

        // draw points
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLUE);
        for (Point p : points) {
            p.draw();
        }
    }
}