import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zak on 1/17/17.
 */
public class Board {

    private int[][] blocks;
    private int n;
    private int emptyRow = -1;
    private int emptyCol = -1;
    private int manhattan = -1;

    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new NullPointerException();
        }
        this.n = blocks.length;
        this.blocks = new int[n][n];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < n; j++) {
                this.blocks[i][j] = blocks[i][j];
                if (this.blocks[i][j] == 0) {
                    emptyRow = i;
                    emptyCol = j;
                }
            }
        }
    }

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
        return n;
    }             // board dimension n

    public int hamming() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int value = blocks[i][j];
                if (value != 0 && value != getValue(i, j)) {
                    count++;
                }
            }
        }
        return count;
    }              // number of blocks out of place

    public int manhattan() {
        if (manhattan >= 0) {
            return manhattan;
        }

        int step = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int value = blocks[i][j];
                int position = getValue(i, j);
                if (value == 0 || value == position) {
                    continue;
                }
                int targetRow = getRow(value);
                int targetCol = getCol(value);
                step = step + (targetRow > i ? targetRow - i : i - targetRow) + (targetCol > j ? targetCol - j : j - targetCol);
            }
        }
        manhattan = step;
        return manhattan;

    }            // sum of Manhattan distances between blocks and goal

    public boolean isGoal() {
        return hamming() == 0;
    }          // is this board the goal board?

    public Board twin() {
        int[][] array = copyOf(this.blocks, n);
        if (n > 1) {
            int i1 = -1;
            int j1 = -1;
            int i2 = -1;
            int j2 = -1;
            outer:
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int value = blocks[i][j];
                    if (value != 0) {
                        if (i1 < 0) {
                            i1 = i;
                            j1 = j;
                        } else if (i2 < 0) {
                            i2 = i;
                            j2 = j;
                            int temp = array[i2][j2];
                            array[i2][j2] = array[i1][j1];
                            array[i1][j1] = temp;
                            break outer;
                        }
                    }
                }
            }
        }
        Board board = new Board(array);
        return board;
    }         // a board that is obtained by exchanging any pair of blocks

    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (!y.getClass().equals(this.getClass())) {
            return false;
        }
        Board that = (Board) y;
        if (this == that) {
            return true;
        } else {
            if (this.dimension() != that.dimension()) {
                return false;
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (this.blocks[i][j] != that.blocks[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
    }       // does this board equal y?

    public Iterable<Board> neighbors() {
        List<Board> boards = new ArrayList<Board>();
        if (emptyRow >= 0) {

            if (emptyRow > 0) {
                int[][] newBlocks = copyOf(blocks, n);
                newBlocks[emptyRow][emptyCol] = newBlocks[emptyRow - 1][emptyCol];
                newBlocks[emptyRow - 1][emptyCol] = 0;
                Board b = new Board(newBlocks);
                boards.add(b);
            }

            if (emptyRow < (n - 1)) {
                int[][] newBlocks = copyOf(blocks, n);
                newBlocks[emptyRow][emptyCol] = newBlocks[emptyRow + 1][emptyCol];
                newBlocks[emptyRow + 1][emptyCol] = 0;
                Board b = new Board(newBlocks);
                boards.add(b);
            }

            if (emptyCol > 0) {
                int[][] newBlocks = copyOf(blocks, n);
                newBlocks[emptyRow][emptyCol] = newBlocks[emptyRow][emptyCol - 1];
                newBlocks[emptyRow][emptyCol - 1] = 0;
                Board b = new Board(newBlocks);
                boards.add(b);
            }

            if (emptyCol < (n - 1)) {
                int[][] newBlocks = copyOf(blocks, n);
                newBlocks[emptyRow][emptyCol] = newBlocks[emptyRow][emptyCol + 1];
                newBlocks[emptyRow][emptyCol + 1] = 0;
                Board b = new Board(newBlocks);
                boards.add(b);
            }

        }
        return boards;
    }    // all neighboring boards

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append("\n");
        for (int i = 0; i < n; i++) {
            sb.append(" ");
            for (int j = 0; j < n; j++) {
                sb.append(blocks[i][j]);
                sb.append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();

    }   // string representation of this board (in the output format specified below)

    public static void main(String[] args) {
//        int[][] bArray = new int[3][3];
//        bArray[0][0] = 0;
//        bArray[0][1] = 6;
//        bArray[0][2] = 3;
//        bArray[1][0] = 4;
//        bArray[1][1] = 8;
//        bArray[1][2] = 1;
//        bArray[2][0] = 2;
//        bArray[2][1] = 5;
//        bArray[2][2] = 7;
        int[][] bArray = new int[2][2];
        bArray[0][0] = 1;
        bArray[0][1] = 3;
        bArray[1][0] = 2;
        bArray[1][1] = 0;
        Board b = new Board(bArray);
        System.out.println(b);
        System.out.println(b.manhattan());
//        System.out.println(b.twin());

//        for (Board board : b.neighbors()) {
//            System.out.println(board);
//        }

    } // unit tests (not graded)

    private int getValue(int i, int j) {
        return (i * n) + j + 1;
    }

    private int getRow(int value) {
        return (value - 1) / n;
    }

    private int getCol(int value) {
        return (value - 1) % n;
    }

    private int[][] copyOf(int[][] src, int size) {
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = src[i][j];
            }
        }
        return result;
    }
}
