import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import javax.sound.sampled.Line;
import javax.swing.text.Segment;
import java.util.*;

/**
 * Created by zak on 1/9/17.
 */
public class FastCollinearPoints {

    private Point[] points;
    private LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) {
        this.points = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            this.points[i] = points[i];
        }

        Arrays.sort(this.points);

        if (this.points.length > 1) {
            for (int i = 1; i < this.points.length; i++) {
                if (this.points[i].compareTo(this.points[i - 1]) == 0) {
                    throw new IllegalArgumentException();
                }
            }

        }

        createSegments();

    }   // finds all line segments containing 4 or more points

    public int numberOfSegments() {
        return lineSegments.length;

    }   // the number of line segments

    public LineSegment[] segments() {
        return Arrays.copyOf(this.lineSegments, numberOfSegments());
    }

    private void createSegments() {
        Point[] aux = new Point[points.length];
        Point p, q;

        List<LineSegment> results = new ArrayList<LineSegment>();

        HashMap<Double, ArrayList<Point>> slopeEndPoints = new HashMap<Double, ArrayList<Point>>();

        if (points.length > 1) {
            for (int i = 0; i < points.length; i++) {
                aux[i] = points[i];
            }

            for (int i = 0; i < points.length; i++) {
                p = points[i];

                Arrays.sort(aux, i, points.length, p.slopeOrder());

                int count = 0;
                double currentSlope;
                if (i + 1 >= points.length) {
                    currentSlope = Double.NEGATIVE_INFINITY;
                } else {
                    currentSlope = p.slopeTo(aux[i + 1]);
                }
                Point maxPoint = p;

                for (int j = i + 1; j < aux.length; j++) {
                    q = aux[j];
                    if (Double.compare(currentSlope, Double.NEGATIVE_INFINITY) == 0) {
                        continue;
                    }

//                System.out.println("p=" + p + "\tq=" + q + "\tslope=" + p.slopeTo(q) + "\tstatus=" + (p.compareTo(q) < 0));

                    if (Double.compare(currentSlope, p.slopeTo(q)) == 0) {
                        if (q.compareTo(maxPoint) > 0) {
                            maxPoint = q;
                        }
                        count++;
                    } else {
                        lineSegmentCheck(p, results, count, maxPoint, slopeEndPoints);
                        currentSlope = p.slopeTo(q);
                        count = 1;
                        maxPoint = q;
                    }


                }
                lineSegmentCheck(p, results, count, maxPoint, slopeEndPoints);

            }
        }

        this.lineSegments = results.toArray(new LineSegment[0]);

    }

    private void lineSegmentCheck(Point p, List<LineSegment> results, int count, Point maxPoint,
                                  HashMap<Double, ArrayList<Point>> slopEndPoints) {
        if (count >= 3) {
//            System.out.println("!!!p=" + p + "\tq=" + maxPoint);
            double slope = p.slopeTo(maxPoint);
            ArrayList<Point> endPoints = slopEndPoints.get(slope);
            if (endPoints == null) {
                endPoints = new ArrayList<Point>();
                endPoints.add(maxPoint);
                slopEndPoints.put(slope, endPoints);

                results.add(new LineSegment(p, maxPoint));
            } else {
                if (!endPoints.contains(maxPoint)) {
                    endPoints.add(maxPoint);
                    results.add(new LineSegment(p, maxPoint));
                }
            }

        }
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
