import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private final Stack<Board> solutions;
    private boolean isSol;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Argument cannot be null");

        solutions = new Stack<>();
        MinPQ<SearchNode> pq;
        pq = new MinPQ<>();

        isSol = false;
        if (initial.hamming() == 0) {
            isSol = true;
            solutions.push(initial);
        }
        else {
            Board twinBoard = initial.twin();
            pq.insert(new SearchNode(null, initial));
            pq.insert(new SearchNode(null, twinBoard));

            while (!pq.min().board.isGoal()) {

                SearchNode minNode = pq.delMin();

                for (Board board : minNode.board.neighbors()) {
                    if (minNode.prev == null)
                        pq.insert(new SearchNode(minNode, board));
                    else if (!minNode.prev.board.equals(board)) {
                        // StdOut.println("min= " + minNode.board.toString());
                        // StdOut.println("b= " + board.toString());
                        pq.insert(new SearchNode(minNode, board));
                    }
                }
            }
            SearchNode goalNode = pq.delMin();
            while (goalNode.prev != null) {
                solutions.push(goalNode.board);
                goalNode = goalNode.prev;
            }
            solutions.push(goalNode.board);
            if (goalNode.board.equals(initial)) {
                isSol = true;
            }
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private SearchNode prev;
        private Board board;
        private int movesIn;
        private int manhattan;

        private SearchNode(SearchNode prevSearchNode, Board currentBoard) {
            prev = prevSearchNode;
            manhattan = currentBoard.manhattan();
            board = currentBoard;
            if (prevSearchNode != null) {
                movesIn = prev.movesIn + 1;
            }
            else {
                movesIn = 0;
            }
        }

        @Override
        public int compareTo(SearchNode that) {

            if (this.manhattan + this.movesIn == that.manhattan + that.movesIn) {
                // StdOut.println("manhattan1= " + that.movesIn);
                // StdOut.println("manhattan2= " + this.movesIn);
                return this.manhattan - that.manhattan;
            }
            else {
                return (this.manhattan + this.movesIn) - (that.manhattan + that.movesIn);
            }

        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        // StdOut.println("is:=" + isSol);
        return isSol;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return this.solutions.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        return solutions;
    }

    // test client (see below)
    public static void main(String[] args) {
        int[][] t = new int[3][3];
        int c = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                t[i][j] = c;
                c++;
            }
        }
        t[1][2] = 0;
        t[2][2] = 6;

        Board b = new Board(t);
        Solver sl = new Solver(b);
        StdOut.println(sl.isSolvable());
        StdOut.println("Manhattan=" + b.manhattan());
    }
}
