import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by zak on 12/27/16.
 */
public class Percolation {

    private byte[] status;
    private int size;
    private WeightedQuickUnionUF uf;
    private boolean percolates;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        size = n;
        status = new byte[n * n];
        uf = new WeightedQuickUnionUF(n * n);

    }
    // create n-by-n grid, with all sites blocked


    public void open(int row, int col) {
        checkRowCol(row, col);
        if (isOpen(row, col)) {
            return;
        }

        if (row == 1) {
            status[getIndex(row, col)] = 2;
        } else if (row == size) {
            status[getIndex(row, col)] = 3;
        } else {
            if (status[getIndex(row, col)] == 0) {
                status[getIndex(row, col)] = 1;
            }
        }
        connect(row, col);

        if (size == 1 && row == 1) {
            percolates = true;
        }

    }   // open site (row, col) if it is not open already


    public boolean isOpen(int row, int col) {
        checkRowCol(row, col);
        return status[getIndex(row, col)] != 0;
    }
    // is site (row, col) open?

    public boolean isFull(int row, int col) {
        if (isOpen(row, col)) {
            int root = uf.find(getIndex(row, col));
            return status[root] == 2;

        }
        return false;
    } // is site (row, col) full?

    public int numberOfOpenSites() {
        int openSites = 0;
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                if (isOpen(i, j)) {
                    openSites++;
                }
            }
        }
        return openSites;
    }  // number of open sites

    public boolean percolates() {
        return percolates;
    }      // does the system percolate?

    public static void main(String[] args) {
        Percolation percolation = new Percolation(100);
        percolation.open(100, 100);
        System.out.println(percolation.isOpen(100, 100));
        System.out.println(percolation.isOpen(1, 1));

    }   // test client (optional)


    private void checkRowCol(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IndexOutOfBoundsException();
        }
        return;
    }

    private void connect(int row, int col) {
        int top = connectTop(row, col);
        int bottom = connectBottom(row, col);
        int left = connectLeft(row, col);
        int right = connectRight(row, col);

        int own = status[getIndex(row, col)];

        int root = uf.find(getIndex(row, col));

        boolean bottomConnected = false;
        if (own == 3 || top == 3 || bottom == 3 || left == 3 || right == 3) {
            bottomConnected = true;
            status[root] = 3;
        }

        boolean topConnected = false;
        if (own == 2 || top == 2 || bottom == 2 || left == 2 || right == 2) {
            topConnected = true;

            status[root] = 2;
        }

        if (topConnected && bottomConnected) {
            percolates = true;
        }
    }

    private int connectTop(int row, int col) {
        if (row > 1) {

            if (isOpen(row - 1, col)) {
                int p = getIndex(row, col);
                int q = getIndex(row - 1, col);
                int root = uf.find(q);
                int s = status[root];
                uf.union(p, q);

                return s;
            }
        }

        return -1;
    }

    private int connectBottom(int row, int col) {
        if (row < size) {

            if (isOpen(row + 1, col)) {
                int p = getIndex(row, col);
                int q = getIndex(row + 1, col);
                int root = uf.find(q);
                int s = status[root];
                uf.union(p, q);
                return s;
            }
        }

        return -1;
    }

    private int connectLeft(int row, int col) {
        if (col > 1) {
            if (isOpen(row, col - 1)) {
                int p = getIndex(row, col);
                int q = getIndex(row, col - 1);
                int root = uf.find(q);
                int s = status[root];
                uf.union(p, q);
                return s;
            }
        }

        return -1;
    }

    private int connectRight(int row, int col) {
        if (col < size) {
            if (isOpen(row, col + 1)) {
                int p = getIndex(row, col);
                int q = getIndex(row, col + 1);
                int root = uf.find(q);
                int s = status[root];
                uf.union(p, q);
                return s;
            }
        }
        return -1;
    }

    private int getIndex(int row, int col) {
        return (row - 1) * size + (col - 1);
    }
}
