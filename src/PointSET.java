import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by zak on 2/4/17.
 */
public class PointSET {

    private TreeSet<Point2D> treeSet;

    public PointSET() {
        treeSet = new TreeSet<Point2D>();
    }                           // construct an empty set of points

    public boolean isEmpty() {

        return treeSet.isEmpty();
    }                      // is the set empty?

    public int size() {
        return treeSet.size();
    }                    // number of points in the set

    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        treeSet.add(p);
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
        return treeSet.contains(p);
    }           // does the set contain point p?

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D point : treeSet) {
            StdDraw.point(point.x(), point.y());
        }
    }         // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException();
        }
        TreeSet<Point2D> result = new TreeSet<Point2D>();

        for (Point2D point : treeSet) {
            if (rect.contains(point)) {
                result.add(point);
            }
        }
        return result;

    }       // all points that are inside the rectangle

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }

        Point2D result = null;
        double min = -1;
        for (Point2D point : treeSet) {
            double distance = p.distanceTo(point);
            if (min == -1) {
                min = distance;
                result = point;
            } else if (distance < min) {
                min = distance;
                result = point;
            }
        }
        return result;

    }     // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {
//        PointSET set = new PointSET();
//        set.insert(new Point2D(0.1, 0.2));
//        set.insert(new Point2D(0.3, 0.4));
//        set.draw();

        PointSET set = new PointSET();
        set.insert(new Point2D(0.1, 0.2));
        set.insert(new Point2D(0.1, 0.4));
        set.insert(new Point2D(0.3, 0.4));
        set.insert(new Point2D(0.5, 0.6));
        set.insert(new Point2D(0.05, 0.4));

//        System.out.println(set.size());
//        System.out.println(set.contains(new Point2D(0.1, 0.2)));
        set.draw();

        RectHV rect = new RectHV(0.2, 0.2, 0.4, 0.5);
        for (Point2D p : set.range(rect)) {
            System.out.println(p);
        }

    }   // unit testing of the methods (optional)
}
