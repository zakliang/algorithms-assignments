import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by zak on 1/9/17.
 */

public class BruteCollinearPoints {

    private Point[] points;
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
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

    }  // finds all line segments containing 4 points

    public int numberOfSegments() {
        return segments.length;
    }       // the number of line segments

    public LineSegment[] segments() {
        return Arrays.copyOf(this.segments, numberOfSegments());
    }                // the line segments


    private void createSegments() {
        int i, j, k, l;
        Point p, q, r, s;
        List<LineSegment> lineSegments = new ArrayList<LineSegment>();

        for (i = 0; i < points.length; i++) {

            p = points[i];

            for (j = 0; j < points.length; j++) {

                if (i == j) {
                    continue;
                }
                q = points[j];

                if (p.compareTo(q) > 0) {
                    continue;
                }

                double slope1 = p.slopeTo(q);

                for (k = 0; k < points.length; k++) {

                    if (k == i || k == j) {
                        continue;
                    }

                    r = points[k];

                    if (q.compareTo(r) > 0) {
                        continue;
                    }

                    double slope2 = p.slopeTo(r);
                    for (l = 0; l < points.length; l++) {

                        if (l == i || l == j || l == k) {
                            continue;
                        }

                        s = points[l];

                        double slope3 = p.slopeTo(s);

                        if (r.compareTo(s) < 0 && Double.compare(slope1, slope2) == 0
                                && Double.compare(slope2, slope3) == 0) {

//                            System.out.println("p=" + p + "\tq=" + q + "\tr=" + r + "\ts" + s);
//                            System.out.println(slope1);
                            LineSegment lineSegment = new LineSegment(p, s);
                            lineSegments.add(lineSegment);
                        }

                    }
                }
            }
        }

        this.segments = lineSegments.toArray(new LineSegment[0]);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}