/*************************************************************************
 * Compilation: javac PercolationStats.java
 * Execution: java PercolationStats N T
 * Dependencies: StdStats.java StdRandom.java Percolation.java
 * 
 * This program performs a series of T computational experiments on
 * an N-by-N grid to estimate the percolation threshold. The program
 * returns:
 *     - the mean percolation threshold
 *     - the standard deviation of the experiment
 *     - the 95% confidence interval of the experiment
 * 
 * Author: Andrea Clausen
 * Date: 2015-02-08
 *
 *************************************************************************/

public class PercolationStats {
    
    // stores threshold results of percolation experiments
    private double[] thresholds;
    
    // grid size
    private int n;
    
    // number of experiments
    private int t;
    
    /**
     * perform T independent experiments on an N-by-N grid
     */
    public PercolationStats(int N, int T) {

        if (N <= 0 || T <= 0) throw new IllegalArgumentException("illegal argument");
        
        n = N;
        t = T;
        thresholds = new double[T];
        
        for (int i = 0; i < T; i++) {
            Percolation perc = new Percolation(N);
            
            double sites = 0;
            
            // open new sites while the system doesn't percolate
            while (!perc.percolates()) {
                
                // open random site
                int a = StdRandom.uniform(1, N + 1);
                int b = StdRandom.uniform(1, N + 1);
                
                if (!perc.isOpen(a, b)) {
                    
                    perc.open(a, b);
                    
                    // keep track of number of open sites
                    sites++;
                }
                
                thresholds[i] = sites / (n*n);
            }
        }
    }
    
    /**
     * sample mean of percolation threshold  
     */
    public double mean() {
        double mean = StdStats.mean(thresholds);
        return mean;
    }
    
    /**
     * sample standard deviation of percolation threshold     
     */
    public double stddev() {
        double stddev = StdStats.stddev(thresholds);
        return stddev;
    }
    
    /**
     * low  endpoint of 95% confidence interval     
     */
    public double confidenceLo() {
        double confidenceLo = mean() - (1.96 * stddev()) / java.lang.Math.sqrt(t);
        return confidenceLo;
    }
    
    /**
     * high endpoint of 95% confidence interval    
     */
    public double confidenceHi() {
        double confidenceHi = mean() + (1.96 * stddev()) / java.lang.Math.sqrt(t);
        return confidenceHi;
    }

    /**
     * test client
     */
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        
        PercolationStats stats = new PercolationStats(N, T);
        
        System.out.println("mean\t\t\t= " + stats.mean());
        System.out.println("stddev\t\t\t= " + stats.stddev());
        System.out.println("95% confidence interval\t= " + stats.confidenceLo() + ", " + stats.confidenceHi());
    }
}