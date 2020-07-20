import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver {

    private final Stack<Board> solutions;
    private boolean isSol;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Argument cannot be null");
        solutions = new Stack<>();
        isSol = false;
        aStarSearch(initial);
    }
    
    private void aStarSearch(Board initial){
        MinPQ<SearchNode> pq = new MinPQ<>();       // MinPQ to create neighbour tree
        if (initial.hamming() == 0) {       // To avoid unnecessary calculation if it bord is already solved
            isSol = true;
            solutions.push(initial);
        }
        else {
            Board twinBoard = initial.twin();
            pq.insert(new SearchNode(null, initial));       
            pq.insert(new SearchNode(null, twinBoard));     // Twin is needed in case of no solution, below while loop can be terminated if
                                                            //  if Twin's solution is found

            while (!pq.min().board.isGoal()) {

                SearchNode minNode = pq.delMin();
                for (Board board : minNode.board.neighbors()) {
                    if (minNode.prev == null)
                        pq.insert(new SearchNode(minNode, board));
                    else if (!minNode.prev.board.equals(board)) {
                        pq.insert(new SearchNode(minNode, board));  // Add the node to pq if the node is not already explored 
                    }
                }
            }
            SearchNode goalNode = pq.delMin();
            
            // Below loop will populate the solution queue if solution is found
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
    private class SearchNode implements Comparable<SearchNode> {    // Search node for comparison 
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
                return this.manhattan - that.manhattan;
            }
            else {
                return (this.manhattan + this.movesIn) - (that.manhattan + that.movesIn);
            }

        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
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
       // create initial board from file
        In in = new In(arg[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println(initial.toString());


        //solve the puzzle
        Solver solver = new Solver(initial);

        //print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
