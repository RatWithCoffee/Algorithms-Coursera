package kd_tree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class PointSET {
    private SET<Point2D> points;

    public PointSET() { // construct an empty set of points
        this.points = new SET<>();
    }


    public boolean isEmpty() { // is the set empty?
        return points.isEmpty();
    }

    public int size() {  // number of points in the set
        return points.size();
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException();

        points.add(p);
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null) throw new IllegalArgumentException();

        return points.contains(p);
    }

    public void draw() { // draw all points to standard draw
        for (Point2D point : points) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException();

        List<Point2D> pointsInside = new ArrayList<>();
        for (Point2D point : points) {
            if (rect.xmin() <= point.x() && rect.xmax() >= point.x() &&
                    rect.ymin() <= point.y() && rect.ymax() >= point.y()) {
                pointsInside.add(point);
            }
        }

        return pointsInside;
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;

        Point2D nearestPoint = points.max();
        double shortestDistance = nearestPoint.distanceSquaredTo(p);
        for (Point2D point : points) {
            if (point.distanceSquaredTo(p) < shortestDistance) { // find a new point
                nearestPoint = point;
                shortestDistance = nearestPoint.distanceSquaredTo(p);
            }
        }

        return nearestPoint;
    }

    public static void main(String[] args) {  // unit testing of the methods (optional)
        PointSET points = new PointSET();
        Point2D[] points2D = {new Point2D(0.1, 0.1), new Point2D(0.5, 0.1), new Point2D(0.4, 0.3), new Point2D(0.7, 0.8), new Point2D(0.1, 0.9)};

        points.nearest(new Point2D(0.1, 0.1));

        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLUE);
        for (Point2D p : points2D) {
            points.insert(p);
            p.draw();
        }

        System.out.println();

        for (Point2D p : points.range(new RectHV(0.3, 0.1, 0.9, 0.4))) {
            System.out.println(p);
        }

        System.out.println();

        Point2D point = new Point2D(0.1, 0.2);
        System.out.println(points.nearest(point));

    }
}