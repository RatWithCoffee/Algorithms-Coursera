package union_find;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        double[] results = new double[trials]; // array for results of trials

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            int row, col;
            while (!perc.percolates()) {
                row = StdRandom.uniformInt(1, n + 1);
                col = StdRandom.uniformInt(1, n + 1);
                perc.open(row, col);
            }
            results[i] = (double) perc.numberOfOpenSites() / (n * n);
        }

        // statistics
        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
        confidenceHi = mean - CONFIDENCE_95 * stddev / Math.sqrt(trials);
        confidenceLo = mean + CONFIDENCE_95 * stddev / Math.sqrt(trials);

    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    // test client (see below)
    public static void main(String[] args) {
//        int n = 100;
//        int trials = 100;
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        StdOut.println("mean = " + ps.mean());
        StdOut.println("stddev = " + ps.stddev());
        StdOut.println("95% confidence interval = " + "[" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");

    }

}