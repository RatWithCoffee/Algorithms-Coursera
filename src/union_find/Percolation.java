package union_find;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] sitesOpen; // true - open site, false - closed site
    private WeightedQuickUnionUF ufMain; // uf with virtual top and bottom sites
    private WeightedQuickUnionUF ufExtra; // uf with a virtual top site
    private final int num; // num-by-num grid
    private int numberOpenSites = 0; // number of open sites

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        ufMain = new WeightedQuickUnionUF(n * n + 2);
        ufExtra = new WeightedQuickUnionUF(n * n + 1);
        num = n;

        sitesOpen = new boolean[n * n + 2];
        // virtual sites are initially opened
        sitesOpen[0] = true;
        sitesOpen[sitesOpen.length - 1] = true;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row == 0 && col != 1 || row == num + 1 && col != 1 || row < 0 || row > num + 1 || col < 1 || col > num) {
            throw new IllegalArgumentException();
        }

        int index = getIndex(row, col);
        if (sitesOpen[index]) { // if site was already opened
            return;
        }
        sitesOpen[index] = true; // open a site
        numberOpenSites++;


        // add connection with virtual sites
        if (row == num) {
            ufMain.union(sitesOpen.length - 1, index);
        }
        if (row == 1) {
            ufMain.union(0, index);
            ufExtra.union(0, index);
        }

        // add connections with other adjacent sites
        if (col > 1 && isOpen(row, col - 1)) { // left neighbour
            ufMain.union(index - 1, index);
            ufExtra.union(index - 1, index);
        }
        if (col < num && isOpen(row, col + 1)) { // right neighbour
            ufMain.union(index + 1, index);
            ufExtra.union(index + 1, index);
        }
        if (row < num && isOpen(row + 1, col)) { // bottom neighbour
            ufMain.union(index + num, index);
            ufExtra.union(index + num, index);
        }
        if (row > 1 && isOpen(row - 1, col)) { // upper neighbour
            ufMain.union(index - num, index);
            ufExtra.union(index - num, index);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row == 0 && col != 1 || row == num + 1 && col != 1 || row < 0 || row > num + 1 || col < 1 || col > num) {
            throw new IllegalArgumentException();
        }
        return sitesOpen[getIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row == 0 && col != 1 || row < 0 || row > num + 1 || col < 1 || col > num) {
            throw new IllegalArgumentException();
        }
        return ufExtra.find(getIndex(row, col)) == ufExtra.find(getIndex(0, 1));
    }

    // returns the number of open sites (virtual sites aren't counted)
    public int numberOfOpenSites() {
        return numberOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return ufMain.find(getIndex(num + 1, 1)) == ufMain.find(getIndex(0, 1));
    }

    private int getIndex(int row, int col) {
        if (row == 0) {
            return 0;
        }
        return (row - 1) * num + col;
    }


    // delete
//    public void print() {
//        System.out.println(sitesOpen[0]);
//        for (int i = 1; i < sitesOpen.length - 1; i++) {
//            System.out.print(sitesOpen[i] + " ");
//            if (i % num == 0) {
//                System.out.println();
//            }
//        }
//        System.out.println(sitesOpen[sitesOpen.length - 1]);
//    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(3);
        perc.open(1, 1);

        perc.open(3, 1);
        perc.open(3, 3);
        perc.open(2, 1);
        perc.isOpen(3, 1);
        // perc.print();
        System.out.println(perc.percolates());
        System.out.println(perc.isFull(1, 1));
    }

}