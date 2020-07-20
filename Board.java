import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int[][] tArr;

    // create a board from an n-by-n array of tArr,
    // where tArr[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException("Arguments cannot be null");
        this.tArr = copyArr(tiles);
    }
    
    // Need this method as .clone() method is not creating the copy of the 2D array
    private int[][] copyArr(int[][] arg) {
        int[][] newTiles = new int[arg.length][arg[0].length];
        for (int i = 0; i < arg.length; i++) {
            for (int j = 0; j < arg[i].length; j++) {
                newTiles[i][j] = arg[i][j];
            }
        }
        return newTiles;
    }

    // string representation of this board
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(this.tArr.length + "\n");
        for (int i = 0; i < this.tArr.length; i++) {
            for (int j = 0; j < this.tArr[i].length; j++) str.append(" " + this.tArr[i][j]);
            str.append("\n");
        }
        return str.toString().trim();
    }

    // board dimension n
    public int dimension() {
        return this.tArr.length;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < tArr.length; i++) {
            for (int j = 0; j < tArr[i].length; j++) {
                if (tArr[i][j] != 0 && tArr[i][j] != (i * this.tArr.length + j + 1)) count++;
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < tArr.length; i++) {
            for (int j = 0; j < tArr[i].length; j++) {
                if (tArr[i][j] != 0 && tArr[i][j] != (i * this.tArr.length + j + 1)) {
                    int row = Math.abs(tArr[i][j] / this.tArr.length);
                    int col = ((tArr[i][j]) % this.tArr.length);
                    row = col == 0 ? row - 1 : row;
                    col = col == 0 ? this.tArr.length - 1 : col - 1;
                    int verDist = (row - i) >= 0 ? (row - i) : (i - row);
                    int horDist = (col - j) >= 0 ? (col - j) : (j - col);
                    sum += verDist + horDist;
                }
            }
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == null) return false;
        if (this == y) return true;
        if (this.getClass() != y.getClass()) return false;
        Board b1 = (Board) y;
        if (b1.tArr.length != this.tArr.length) return false;
        for (int i = 0; i < b1.tArr.length; i++) {
            if (b1.tArr[i].length != this.tArr[i].length) {
                return false;
            }
            for (int j = 0; j < b1.tArr[i].length; j++) {
                if (b1.tArr[i][j] != this.tArr[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<>();
        int[] rowCol = getBlank();
        // StdOut.println("rowCol[0]=" + rowCol[0] + ", rowCol[1]=" + rowCol[1] + ", tArr.length= "
        //                        + tArr.length);
        if (rowCol[0] > 0) {
            int[] rows = new int[] { rowCol[0], rowCol[0] - 1 };
            int[] cols = new int[] { rowCol[1], rowCol[1] };
            stack.push(new Board(swap(rows, cols)));
        }
        if (rowCol[0] < tArr.length - 1) {
            int[] rows = new int[] { rowCol[0], rowCol[0] + 1 };
            int[] cols = new int[] { rowCol[1], rowCol[1] };
            stack.push(new Board(swap(rows, cols)));
        }
        if (rowCol[1] > 0) {
            int[] rows = new int[] { rowCol[0], rowCol[0] };
            int[] cols = new int[] { rowCol[1], rowCol[1] - 1 };
            stack.push(new Board(swap(rows, cols)));
        }
        if (rowCol[1] < tArr[0].length - 1) {
            int[] rows = new int[] { rowCol[0], rowCol[0] };
            int[] cols = new int[] { rowCol[1], rowCol[1] + 1 };
            stack.push(new Board(swap(rows, cols)));
        }
        return stack;
    }

    // exchange the tArr between indexes passed in the argument
    private int[][] swap(int rows[], int cols[]) {
        int[][] nT = copyArr(this.tArr);
        int temV = nT[rows[0]][cols[0]];
        //StdOut.println("this1=" + this.toString());
        nT[rows[0]][cols[0]] = nT[rows[1]][cols[1]];
        nT[rows[1]][cols[1]] = temV;
        //StdOut.println("this2=" + this.toString());
        return nT;
    }

    // eget the indexes of the blank tile
    private int[] getBlank() {
        int row = -1;
        int col = -1;

        for (int i = 0; i < tArr.length; i++) {
            for (int j = 0; j < tArr[i].length; j++) {
                if (tArr[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }
        return new int[] { row, col };
    }


    // a board that is obtained by exchanging any pair of tArr
    public Board twin() {

        if (tArr[0][0] == 0 || tArr[0][1] == 0) {
            Board nB = new Board(swap(new int[] { 1, 1 }, new int[] { 0, 1 }));
            return nB;
        }
        else {
            Board nB = new Board(swap(new int[] { 0, 0 }, new int[] { 0, 1 }));
            return nB;
        }
    }

    // unit testing (not graded)
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
        StdOut.println(b.toString().trim());
        StdOut.println(b.hamming());
        StdOut.println(b.manhattan());
    }

}
