package kd_tree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private Node root;

    private int size;

    public KdTree() { // construct an empty set of points
        root = null;
        size = 0;
    }

    public boolean isEmpty() { // is the set empty?
        return root == null;
    }

    public int size() {  // number of points in the set
        return size;
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = insert(p, root, null, true);
    }

    private Node insert(Point2D p, Node curr, Node parentNode, boolean isVertical) {
        if (size == 0) {
            size++;
            return new Node(isVertical, p, new RectHV(0, 0, 1, 1));
        }

        if (curr == null) { // add new node
            size++;
            RectHV rectLeftBottom, rectRightUpper;
            if (!isVertical) {
                rectLeftBottom = new RectHV(parentNode.rect.xmin(), parentNode.rect.ymin(), parentNode.point.x(), parentNode.rect.ymax());
                rectRightUpper = new RectHV(parentNode.point.x(), parentNode.rect.ymin(), parentNode.rect.xmax(), parentNode.rect.ymax());
            } else {
                rectRightUpper = new RectHV(parentNode.rect.xmin(), parentNode.point.y(), parentNode.rect.xmax(), parentNode.rect.ymax());
                rectLeftBottom = new RectHV(parentNode.rect.xmin(), parentNode.rect.ymin(), parentNode.rect.xmax(), parentNode.point.y());
            }

            int cmp = (!isVertical) ? Double.compare(p.x(), parentNode.point.x()) : Double.compare(p.y(), parentNode.point.y());
            if (cmp < 0) return new Node(isVertical, p, rectLeftBottom);
            else return new Node(isVertical, p, rectRightUpper);

        }

        if (p.equals(curr.point)) return curr; // don't add point that already in BST

        int cmp = (isVertical) ? Double.compare(p.x(), curr.point.x()) : Double.compare(p.y(), curr.point.y());
        if (cmp < 0) curr.left = insert(p, curr.left, curr, !isVertical);
        else curr.right = insert(p, curr.right, curr, !isVertical);

        return curr;
    }


    public boolean contains(Point2D p) { // does the set contains point p?
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Node curr = root;
        while (curr != null) {
            if (curr.point.equals(p)) return true;

            int cmp = (curr.isVertical) ? Double.compare(p.x(), curr.point.x()) : Double.compare(p.y(), curr.point.y());
            if (cmp < 0) curr = curr.left;
            else curr = curr.right;
        }
        return false;
    }

    public void draw() { // draw all points to standard draw

        StdDraw.setPenColor(StdDraw.BLUE);
        draw(root, new RectHV(0, 0, 1, 1));
    }

    private void draw(Node node, RectHV currRect) { // need some fix
        if (node == null) return;

        // draw line
        StdDraw.setPenRadius(0.005);
        if (node.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), currRect.ymin(), node.point.x(), currRect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(currRect.xmin(), node.point.y(), currRect.xmax(), node.point.y());
        }

        // draw point
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLACK);
        node.point.draw();

        // change rectangle
        RectHV rectLeftBottom;
        RectHV rectRightUpper;
        if (node.isVertical) {
            rectLeftBottom = new RectHV(currRect.xmin(), currRect.ymin(), node.point.x(), currRect.ymax());
            rectRightUpper = new RectHV(node.point.x(), currRect.ymin(), currRect.xmax(), currRect.ymax());
        } else {
            rectLeftBottom = new RectHV(currRect.xmin(), currRect.ymin(), currRect.xmax(), node.point.y());
            rectRightUpper = new RectHV(currRect.xmin(), node.point.y(), currRect.xmax(), currRect.ymax());
        }
        draw(node.left, rectLeftBottom);
        draw(node.right, rectRightUpper);
    }

    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> points = new ArrayList<>();
        return range(rect, root, points, new RectHV(0, 0, 1, 1));
    }

    private Iterable<Point2D> range(RectHV rect, Node curr, List<Point2D> points, RectHV currRect) {
        if (curr == null) return points;

        if (!currRect.intersects(rect)) return points; // no need to explore this node or its subtrees

        if (rect.contains(curr.point)) points.add(curr.point);

        // find next rectangles
        if (curr.isVertical) {
            range(rect, curr.left, points, new RectHV(currRect.xmin(), currRect.ymin(), curr.point.x(), currRect.ymax()));
            range(rect, curr.right, points, new RectHV(curr.point.x(), currRect.ymin(), currRect.xmax(), currRect.ymax()));
        } else {
            range(rect, curr.right, points, new RectHV(currRect.xmin(), curr.point.y(), currRect.xmax(), currRect.ymax()));
            range(rect, curr.left, points, new RectHV(currRect.xmin(), currRect.ymin(), currRect.xmax(), curr.point.y()));
        }

        return points;
    }

    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return nearest(p, root, null);
    }

    private Point2D nearest(Point2D p, Node curr, Point2D closest) {
        if (curr == null) return closest;

        if (closest != null  && curr.rect.distanceSquaredTo(p) >= p.distanceSquaredTo(closest)) return closest; // pruning rule

        if (closest == null || curr.point.distanceSquaredTo(p) < p.distanceSquaredTo(closest))
            closest = curr.point; // new closer point

        if (curr.left != null && curr.right != null) {
            if (curr.left.rect.distanceSquaredTo(p) < curr.right.rect.distanceSquaredTo(p)) {
                closest = nearest(p, curr.left, closest);
                closest = nearest(p, curr.right, closest);
            } else {
                closest = nearest(p, curr.right, closest);
                closest = nearest(p, curr.left, closest);
            }
        } else {
            closest = nearest(p, curr.left, closest);
            closest = nearest(p, curr.right, closest);
        }

        return closest;
    }

    private static class Node {
        private final boolean isVertical; // key
        private final Point2D point; // value
        private Node left, right;

        private final RectHV rect;

        public Node(boolean isVertical, Point2D point, RectHV rect) {
            this.isVertical = isVertical;
            this.point = point;
            this.rect = rect;
        }

    }


    public static void main(String[] args) {  // unit testing of the methods (optional)
        KdTree points = new KdTree();

        Point2D[] points2D = {new Point2D(0.7, 0.2),
                new Point2D(0.5, 0.4),
                new Point2D(0.2, 0.3),
                new Point2D(0.4, 0.7),
                new Point2D(0.9, 0.6)};

        for (Point2D p : points2D) {
            points.insert(p);
        }
        points.draw();


        System.out.println("nearest");
        // System.out.println(points.nearest(new Point2D(0.702, 0.93)));
        System.out.println(points.nearest(new Point2D(0.86, 0.38)));

        // - student sequence of k-d tree nodes involved in calls to Point2D methods:
        // A E B D C
        // - reference sequence of k-d tree nodes involved in calls to Point2D methods:
        // A E B C D

    }


}