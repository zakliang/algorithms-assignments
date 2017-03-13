import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zak on 1/18/17.
 */
public class Solver {
    private Board initial;
    private Iterable<Board> solution;

    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException();
        }

        this.initial = initial;

    }     // find a solution to the initial board (using the A* algorithm)

    public boolean isSolvable() {

        SearchNodeComparator comparator = new SearchNodeComparator();

        SearchNode node1 = new SearchNode(initial, null, 0);
        MinPQ<SearchNode> queue1 = new MinPQ<SearchNode>(comparator);
        queue1.insert(node1);

        SearchNode node2 = new SearchNode(initial.twin(), null, 0);
        MinPQ<SearchNode> queue2 = new MinPQ<SearchNode>(comparator);
        queue2.insert(node2);

        while (!queue1.isEmpty()) {
            SearchNode n1 = queue1.delMin();
            SearchNode n2 = queue2.delMin();

            if (n1.board.isGoal()) {
                if (solution == null) {
                    Stack<Board> stack = new Stack<Board>();
                    createSolution(stack, n1);
                    solution = stack;
                }
                return true;
            } else {
                for (Board b : n1.board.neighbors()) {
                    if (n1.previous == null || !b.equals(n1.previous.board)) {
                        queue1.insert(new SearchNode(b, n1, n1.moves + 1));
                    }
                }
            }

            if (n2.board.isGoal()) {
                return false;
            } else {
                for (Board b : n2.board.neighbors()) {
                    if (n2.previous == null || !b.equals(n2.previous.board)) {
                        queue2.insert(new SearchNode(b, n2, n2.moves + 1));
                    }
                }
            }

        }
        return false;

    }   // is the initial board solvable?

    public int moves() {
        if (!isSolvable()) {
            return -1;
        } else {
            int moves = 0;
            if (solution == null) {
                solution = solution();
            }
            for (Board board : solution) {
                moves++;
            }
            return moves - 1;
        }
    } // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }

        if (solution != null) {
            return solution;
        }

        Stack<Board> result = new Stack<Board>();
        SearchNode node = new SearchNode(initial, null, 0);
        MinPQ<SearchNode> queue = new MinPQ<SearchNode>(new SearchNodeComparator());
        queue.insert(node);

        while (!queue.isEmpty()) {
            node = queue.delMin();

            if (node.board.isGoal()) {
                SearchNode currentNode = node;
                createSolution(result, currentNode);
                break;
            } else {
                for (Board b : node.board.neighbors()) {
                    if (node.previous == null || !b.equals(node.previous.board)) {
                        queue.insert(new SearchNode(b, node, node.moves + 1));
                    }
                }
            }
        }
        this.solution = result;

        return this.solution;

    }    // sequence of boards in a shortest solution; null if unsolvable

    private void createSolution(Stack<Board> stack, SearchNode currentNode) {
        while (currentNode != null) {
            Board b = currentNode.board;
            stack.push(b);
            currentNode = currentNode.previous;
        }
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

    } // solve a slider puzzle (given below)

    private static class SearchNode {

        private Board board;
        private SearchNode previous;
        private int moves;

        public SearchNode(Board board, SearchNode previous, int moves) {
            this.board = board;
            this.previous = previous;
            this.moves = moves;
        }

        public int getPriority() {
            return this.board.manhattan() + moves;
        }

    }

    private static class SearchNodeComparator implements Comparator<SearchNode> {

        @Override
        public int compare(SearchNode o1, SearchNode o2) {
            return o1.getPriority() - o2.getPriority();
        }
    }

}