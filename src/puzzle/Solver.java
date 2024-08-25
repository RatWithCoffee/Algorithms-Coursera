package puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solver {

    private List<Board> shortestSolution;

    private boolean isSolvable = true;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        int currMove = 0;
        int currMoveTwin = 0;

        // initialize main priority queue
        MinPQ<SearchNode> nodes = new MinPQ<>(new ManhattanPriority());
        nodes.insert(new SearchNode(initial, currMove, null));
        // initialize twin queue
        MinPQ<SearchNode> nodesTwin = new MinPQ<>(new ManhattanPriority());
        nodesTwin.insert(new SearchNode(initial.twin(), currMoveTwin, null));

        // find sequence of boards
        SearchNode parentNode; // main puzzle
        SearchNode grandparentNode;
        SearchNode parentNodeTwin; // twin puzzle
        SearchNode grandparentNodeTwin;
        while (!nodes.min().board.isGoal()) {
            parentNode = nodes.delMin();
            currMove = parentNode.moves + 1;
            grandparentNode = parentNode.prevSearchNode;

            parentNodeTwin = nodesTwin.delMin();
            currMoveTwin = parentNodeTwin.moves + 1;
            grandparentNodeTwin = parentNodeTwin.prevSearchNode;

            for (Board neighbor : parentNode.board.neighbors()) {
                if (grandparentNode == null || !grandparentNode.board.equals(neighbor)) {
                    nodes.insert(new SearchNode(neighbor, currMove, parentNode));
                }
            }

            for (Board neighbor : parentNodeTwin.board.neighbors()) {
                if (grandparentNodeTwin == null || !grandparentNodeTwin.board.equals(neighbor)) {
                    nodesTwin.insert(new SearchNode(neighbor, currMoveTwin, parentNodeTwin));
                }
            }

            // did twin puzzle find the solution?
            if (nodesTwin.min().board.isGoal()) {
                isSolvable = false;
                return;
            }
        }

        // create an array of boards in the shortest solution
        parentNode = nodes.min();
        shortestSolution = new ArrayList<>();
        while (parentNode != null) {
            shortestSolution.add(parentNode.board);
            parentNode = parentNode.prevSearchNode;
        }

    }

    private static class SearchNode {
        private final Board board;
        private final int moves;
        private final SearchNode prevSearchNode;

        private final int manhattan;

        public SearchNode(Board board, int moves, SearchNode prevSearchNode) {
            this.board = board;
            this.moves = moves;
            this.prevSearchNode = prevSearchNode;
            this.manhattan = board.manhattan();
        }

    }

    private static class ManhattanPriority implements Comparator<SearchNode> {

        @Override
        public int compare(SearchNode sn1, SearchNode sn2) {
            return Integer.compare(sn1.moves + sn1.manhattan, sn2.moves + sn2.manhattan);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable) return -1;
        return shortestSolution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable) return null;
        List<Board> reverseSolution = new ArrayList<>();
        for (int i = shortestSolution.size() - 1; i > -1; i--) {
            reverseSolution.add(shortestSolution.get(i));
        }
        return reverseSolution;
    }

    // test client (see below)
    public static void main(String[] args) {
        // int[][] b = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        int[][] b = {{1, 3, 6, 11}, {2, 5, 9, 0}, {15, 10, 8, 12}, {13, 14, 4, 7}};
        // int[][] b = {{1, 4, 3}, {7, 2, 6}, {0, 5, 8}};

        // int[][] b = {{0, 1, 3}, {2, 4, 5}, {7, 8, 6}};
        // int[][] b = {{1, 2, 3}, {4, 5, 6}, {8, 7, 0}};
        // // int[][] b = {{1, 2, 3}, {0, 7, 6}, {5, 4, 8}};
        // // int[][] b = {{1, 2}, {3, 0}};
        // //
        Board board = new Board(b);
        Solver solver = new Solver(board);
        System.out.println(solver.moves());
        System.out.println(solver.isSolvable);
        for (Board bb : solver.solution()) {
            System.out.println(bb);
        }


        // create initial board from file
        // In in = new In(args[0]);
        // int n = in.readInt();
        // int[][] tiles = new int[n][n];
        // for (int i = 0; i < n; i++)
        //     for (int j = 0; j < n; j++)
        //         tiles[i][j] = in.readInt();
        // Board initial = new Board(tiles);

        // solve the puzzle
        // Solver solver = new Solver(initial);
        //
        // // print solution to standard output
        // if (!solver.isSolvable())
        //     StdOut.println("No solution possible");
        // else {
        //     StdOut.println("Minimum number of moves = " + solver.moves());
        //     for (Board board : solver.solution())
        //         StdOut.println(board);
        // }
    }

}