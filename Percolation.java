/*************************************************************************
 * Compilation: javac Percolation.java
 * Execution: java Percolation
 * Dependencies: WeightedQuickUnionUF.java
 * 
 * This program creates a data type Percolation that can be used to
 * model the percolation of an N-by-N grid using the weighted
 * quick-union algorithm.
 * 
 * Author: Andrea Clausen
 * Date: 2015-02-08
 *
 *************************************************************************/

public class Percolation {

    // percolation system that stores open and closed sites
    private boolean[][] grid;
    
    // grid size
    private int n;
    
    // class to store which sites are connect to which other sites
    private WeightedQuickUnionUF uf;
    
    /**
     * create N-by-N grid, with all sites blocked    
     */
    public Percolation(int N) {
        
        if (N <= 0) throw new IllegalArgumentException("illegal argument");
        
        uf = new WeightedQuickUnionUF(N * N + 2);
        grid = new boolean[N+1][N+1];
        n = N;
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
               grid[i][j] = false;
            } 
        }  
     }

    /**
     * open site (row i, column j) if it is not open already    
     */
    public void open(int i, int j) {

        // validate the indices of the site
        if (validate(i, j)) {
            
            // mark the site as open
            if (!grid[i][j]) {
         
                grid[i][j] = true;
                
                // if site is in top row, connect with virutal top
                if (i == 1) uf.union(xyTo1D(i, j), 0);
                
                // if site is in bottom row, connect with virtual bottom
                if (i == n) uf.union(xyTo1D(i, j), (n * n) + 1);
                
                // if site above is valid and open, union
                if (validate(i-1, j)) {
                    if (isOpen(i-1, j)) uf.union(xyTo1D(i, j), xyTo1D(i-1, j));
                }
                
                // if site to the right is valid and open, union
                if (validate(i, j+1)) {
                    if (isOpen(i, j+1)) uf.union(xyTo1D(i, j), xyTo1D(i, j+1));
                }
                
                // if site to the left is valid and open, union
                if (validate(i, j-1)) {
                    if (isOpen(i, j-1)) uf.union(xyTo1D(i, j), xyTo1D(i, j-1));
                }
                
                // if site at the bottom is valid and open, union
                if (validate(i+1, j)) {
                    if (isOpen(i+1, j)) uf.union(xyTo1D(i, j), xyTo1D(i+1, j));
                }
            }
        }
        
        else throw new IndexOutOfBoundsException("index out of bounds");
    }
    
    /**
     * is site (row i, column j) open?
     */
    public boolean isOpen(int i, int j) {
        if (validate(i, j)) return grid[i][j];
        else                throw new IndexOutOfBoundsException("index out of bounds");
    }
    
    /**
     * is site (row i, column j) full?
     */
    public boolean isFull(int i, int j) {
        if (validate(i, j)) return uf.connected(0, xyTo1D(i, j));
        else                throw new IndexOutOfBoundsException("index out of bounds");
    }
    
    /**
     * does the system percolate?
     */
    public boolean percolates() {
        return uf.connected(0, n * n + 1);
    }

    // validate that grid[i][j] is a valid site
    private boolean validate(int i, int j) {
        return !(i < 1 || i > n || j < 1 || j > n);
    }
    
    // map from 2-dimensional pair to a 1-dimensional union find object index
    private int xyTo1D(int i, int j) {
        int index = (i - 1) * n + j;
        return index;
    }
    
    /**
     * test client
     */
    public static void main(String[] args) {
    } 

}
