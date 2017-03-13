import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Created by zak on 12/27/16.
 */
public class PercolationStats {

    private double[] fractions;
    private int times;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        int count = n * n;
        times = trials;
        fractions = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }
            fractions[i] = (double) p.numberOfOpenSites() / count;


        }
    }  // perform trials independent experiments on an n-by-n grid

    public double mean() {
        if (fractions != null) {
            return StdStats.mean(fractions);
        }
        return 0;
    } // sample mean of percolation threshold

    public double stddev() {
        if (fractions != null) {
            return StdStats.stddev(fractions);
        }
        return 0;
    } // sample standard deviation of percolation threshold

    public double confidenceLo() {
        if (fractions != null) {
            return mean() - (1.96 * stddev() / Math.sqrt(times));
        }
        return 0;
    } // low  endpoint of 95% confidence interval

    public double confidenceHi() {
        if (fractions != null) {
            return mean() + (1.96 * stddev() / Math.sqrt(times));
        }
        return 0;
    } // high endpoint of 95% confidence interval

    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(200, 100);
        System.out.println(stats.mean());
        System.out.println(stats.stddev());
        System.out.println(stats.confidenceLo());
        System.out.println(stats.confidenceHi());
    } // test client (described below)
}
