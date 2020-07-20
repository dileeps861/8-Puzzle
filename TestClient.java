import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class TestClient {
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(
                "D://ALGO_DS_AI_ML_WS//Coursera Algo//Algorithm Part-I//8puzzle//puzzle14.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println(initial.toString());
        // for (Board b : initial.neighbors()) {
        //     StdOut.println("Neigh:= " + b.toString());
        // }


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
