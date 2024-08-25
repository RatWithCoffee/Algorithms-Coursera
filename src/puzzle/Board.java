package puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private final int[][] board;
    private final int manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        board = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            System.arraycopy(tiles[i], 0, board[i], 0, tiles.length);
        }
        manhattan = countManhattan();
    }

    // string representation of this board
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(board.length).append("\n");
        for (int[] row : board) {
            for (int j = 0; j < board.length; j++) {
                str.append(row[j]).append(" ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    // board dimension n
    public int dimension() {
        return board.length;
    }

    // number of tiles out of place
    public int hamming() {
        int n = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] - 1 != i * board.length + j) {
                    n++;
                }
            }
        }
        return --n; // empty tile is always out of place
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    private int countManhattan() {
        int m = 0;
        int rightRow, rightCol;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    continue;
                }
                rightCol = (board[i][j] - 1) % board.length;
                rightRow = (board[i][j] - 1) / board.length;
                m += Math.abs(j - rightCol) + Math.abs(i - rightRow);
            }
        }
        return m;
    }


    // is this board the goal board?
    public boolean isGoal() {
        return manhattan == 0;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == this) return true; // that and this refers to the same object
        if (y == null) return false; // for any non-null reference value x, x.equals(null) should return false
        if (y.getClass() != getClass()) return false; // if classes is not equal -> return false

        Board that = (Board) y; // y is an object of class Board -> can make a safe conversion
        if (that.dimension() != dimension()) return false;
        // compare object's fields
        for (int i = 0; i < this.board.length; i++) {
            if (!Arrays.equals(this.board[i], that.board[i])) return false;
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();

        // find the empty title
        int emptyTileRow = 0, emptyTileCol = 0;
        cycle:
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    emptyTileRow = i;
                    emptyTileCol = j;
                    break cycle;
                }
            }
        }

        int[] indices = {emptyTileRow, emptyTileCol - 1, emptyTileRow - 1, emptyTileCol, emptyTileRow, emptyTileCol + 1, emptyTileRow + 1, emptyTileCol};
        for (int i = 0; i < indices.length; i += 2) {
            if (isIndexValid(indices[i]) && isIndexValid(indices[i + 1])) {
                neighbors.add(createNeighbourBoard(indices[i], indices[i + 1], emptyTileRow, emptyTileCol));
            }
        }

        return neighbors;
    }

    private boolean isIndexValid(int i) {
        return i >= 0 && i < board.length;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int row;
        if (board[0][0] != 0 && board[0][1] != 0) {
            row = 0;
        } else {
            row = 1;
        }

        return createNeighbourBoard(row, 0, row, 1);
    }

    private Board createNeighbourBoard(int row1, int col1, int row2, int col2) {
        int[][] neighbourBoard = new int[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, neighbourBoard[i], 0, board.length);
        }

        // swap the empty
        int temp = neighbourBoard[row1][col1];
        neighbourBoard[row1][col1] = neighbourBoard[row2][col2];
        neighbourBoard[row2][col2] = temp;
        return new Board(neighbourBoard);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // int[][] b = {{0, 1, 3}, {8, 4, 2}, {7, 6, 5}};
        int[][] b = {{1, 3, 6}, {3, 4, 5}, {6, 7, 8}};
        Board board = new Board(b);
        // System.out.println(board);
        // System.out.println("////////////////////");
        // System.out.println(board.hamming());
        System.out.println(board.manhattan());
        // Iterable<Board> neighbors = board.neighbors();
        // for (Board n : neighbors) {
        //     System.out.println(n);
        //     System.out.println();
        // }
        // System.out.println(board.twin());
        //
        // int[][] b1 = {{1, 3}, {0, 2}};
        // int[][] b2 = {{1, 3}, {0, 2}};
        // // int[][] b2 = {{2, 0}, {3, 1}};
        // Board board1 = new Board(b1);
        // Object board2 = new Board(b2);
        // System.out.println("///");
        // System.out.println(board1.equals(board2));
        // System.out.println(board2.equals(board1));

    }

}