import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zak on 2/4/17.
 */
public class KdTree {

    private Node root;
    private int size = 0;

    public KdTree() {
    }                          // construct an empty set of points

    public boolean isEmpty() {
        return size == 0;
    }                  // is the set empty?

    public int size() {
        return size;
    }                // number of points in the set

    public void insert(Point2D p) {
        put(p);
    }             // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        return get(p) != null;
    }           // does the set contain point p?

    public void draw() {

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        draw(root, 0, 1);
    }         // draw all points to standard draw

    private void draw(Node x, double min, double max) {
        if (x == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(x.p.x(), x.p.y());

        if (x.even) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.001);
            StdDraw.line(x.p.x(), min, x.p.x(), max);
            draw(x.lb, 0, x.p.x());
            draw(x.rt, x.p.x(), 1);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.001);
            StdDraw.line(min, x.p.y(), max, x.p.y());
            draw(x.lb, 0, x.p.y());
            draw(x.rt, x.p.y(), 1);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException();
        }
        List<Point2D> result = new ArrayList<Point2D>();
        range(rect, root, 0, 1, result);
        return result;
    }       // all points that are inside the rectangle

    private void range(RectHV rect, Node node, double min, double max, List<Point2D> result) {
        if (node == null) return;

        if (rect.contains(node.p)) {
            result.add(node.p);
        }

        double x = node.p.x();
        double y = node.p.y();

        if (intersects(rect, node, min, max, x, y)) {
            if (node.even) {
                range(rect, node.lb, 0, x, result);
                range(rect, node.rt, x, 1, result);
            } else {
                range(rect, node.lb, 0, y, result);
                range(rect, node.rt, y, 1, result);
            }
        } else {
            if (node.even) {
                if (rect.xmax() < x) {
                    range(rect, node.lb, 0, x, result);
                } else {
                    range(rect, node.rt, x, 1, result);
                }
            } else {
                if (rect.ymax() < y) {
                    range(rect, node.lb, 0, y, result);
                } else {
                    range(rect, node.rt, y, 1, result);
                }
            }
        }

    }

    private boolean intersects(RectHV rect, Node node, double min, double max, double x, double y) {
        if (node == null) return false;
        if (node == root) return true;
        if (node.even) {
            return x >= rect.xmin() && x <= rect.xmax() && !(min > rect.ymax()) && !(max < rect.ymin());
        } else {
            return y >= rect.ymin() && y <= rect.ymax() && !(min > rect.xmax()) && !(max < rect.xmin());
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }

        return nearest(p, root, 0, 0, 1, 1, new RectHV(0, 0, 1, 1), null);
    }     // a nearest neighbor in the set to point p; null if the set is empty


    private Point2D nearest(Point2D p, Node node, double xmin, double ymin, double xmax, double ymax, RectHV currentRect, Point2D champion) {

        if (node != null) {
            if (champion == null) {
                champion = node.p;
            }

            double x = node.p.x();
            double y = node.p.y();

            double distanceSq = champion.distanceSquaredTo(p);

            if (currentRect.distanceSquaredTo(p) < distanceSq) {

                if (node.p.distanceSquaredTo(p) < distanceSq) {
                    champion = node.p;
                }

                if (node.even) {

                    RectHV left = new RectHV(xmin, ymin, x, ymax);
                    RectHV right = new RectHV(x, ymin, xmax, ymax);

                    if (node.lb != null && left.contains(p)) {
                        champion = nearest(p, node.lb, xmin, ymin, x, ymax, left, champion);
                        champion = nearest(p, node.rt, x, ymin, xmax, ymax, right, champion);
                    } else {
                        champion = nearest(p, node.rt, x, ymin, xmax, ymax, right, champion);
                        champion = nearest(p, node.lb, xmin, ymin, x, ymax, left, champion);
                    }

                } else {

                    RectHV bottom = new RectHV(xmin, ymin, xmax, y);
                    RectHV top = new RectHV(xmin, y, xmax, ymax);

                    if (node.lb != null && bottom.contains(p)) {
                        champion = nearest(p, node.lb, xmin, ymin, xmax, y, bottom, champion);
                        champion = nearest(p, node.rt, xmin, y, xmax, ymax, top, champion);
                    } else {
                        champion = nearest(p, node.rt, xmin, y, xmax, ymax, top, champion);
                        champion = nearest(p, node.lb, xmin, ymin, xmax, y, bottom, champion);
                    }

                }

            }
        }

        return champion;
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.1, 0.2));
        kdTree.insert(new Point2D(0.1, 0.4));
        kdTree.insert(new Point2D(0.3, 0.4));
        kdTree.insert(new Point2D(0.5, 0.6));
        kdTree.insert(new Point2D(0.05, 0.4));

//        System.out.println(kdTree.size());
//        System.out.println(kdTree.contains(new Point2D(0.1, 0.2)));
//        kdTree.draw();

//        RectHV rect = new RectHV(0.2, 0.2, 0.4, 0.5);
//        for (Point2D p : kdTree.range(rect)) {
//            System.out.println(p);
//        }

        System.out.println(kdTree.nearest(new Point2D(0.7, 0.8)));

    }   // unit testing of the methods (optional)

    private static class Node {
        private boolean even;
        private Point2D p;      // the point
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D point, boolean even) {
            this.p = point;
            this.even = even;
        }

    }


    private void put(Point2D point) {
        root = put(root, point, false);
    }

    private Node put(Node node, Point2D point, boolean even) {

        if (node == null) {
            size++;
            return new Node(point, !even);
        }
        double cmp;
        if (node.even) {
            cmp = point.x() - node.p.x();
        } else {
            cmp = point.y() - node.p.y();
        }

        if (cmp < 0) {
            node.lb = put(node.lb, point, !even);
        } else if (cmp > 0) {
            node.rt = put(node.rt, point, !even);
        } else {
            if (node.even && node.p.y() == point.y()) {
                node.p = point;
            } else if (!node.even && node.p.x() == point.x()) {
                node.p = point;
            } else {
                node.rt = put(node.rt, point, !even);
            }
        }
        return node;
    }

    private Point2D get(Point2D key) {
        return get(root, key);
    }

    private Point2D get(Node node, Point2D key) {  // Return value associated with key in the subtree rooted at node;
        // return null if key not present in subtree rooted at node.
        if (node == null) return null;
        double cmp;
        if (node.even) {
            cmp = key.x() - node.p.x();
        } else {
            cmp = key.y() - node.p.y();
        }
        if (cmp < 0) {
            return get(node.lb, key);
        } else if (cmp > 0) {
            return get(node.rt, key);
        } else {
            if (node.even && node.p.y() == key.y()) {
                return node.p;
            } else if (!node.even && node.p.x() == key.x()) {
                return node.p;
            } else {
                return get(node.rt, key);
            }
        }
    }
}
