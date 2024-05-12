public class Main {
    private static final int GRID_SIZE = 9;
    public static void main(String[] args) {
        int N = 9, K = 20;
        Sudoku sudoku = new Sudoku(N, K);
        sudoku.fillValues();
        sudoku.printSudoku();
        if (solveboard(sudoku.mat)) {
            System.out.println("Solution found");
        } else {
            System.out.println("No solution found");
        }
        sudoku.printSudoku();
    }
    public static class Sudoku {
        int[][] mat;
        int N;
        int SRN;
        int K;
        Sudoku(int N, int K)
        {
            this.N = N;
            this.K = K;
            double SRNd = Math.sqrt(N);
            SRN = (int) SRNd;
            mat = new int[N][N];
        }
        public void fillValues()
        {
            fillDiagonal();
            fillRemaining(0, SRN);
            removeKDigits();
        }
        void fillDiagonal()
        {
            for (int i = 0; i<N; i=i+SRN)
                fillBox(i, i);
        }
        boolean unUsedInBox(int rowStart, int colStart, int num)
        {
            for (int i = 0; i<SRN; i++)
                for (int j = 0; j<SRN; j++)
                    if (mat[rowStart+i][colStart+j]==num)
                        return false;

            return true;
        }
        void fillBox(int row,int col)
        {
            int num;
            for (int i=0; i<SRN; i++)
            {
                for (int j=0; j<SRN; j++)
                {
                    do
                    {
                        num = randomGenerator(N);
                    }
                    while (!unUsedInBox(row, col, num));

                    mat[row+i][col+j] = num;
                }
            }
        }
        int randomGenerator(int num)
        {
            return (int) Math.floor((Math.random()*num+1));
        }
        boolean CheckIfSafe(int i,int j,int num)
        {
            return (unUsedInRow(i, num) &&
                    unUsedInCol(j, num) &&
                    unUsedInBox(i-i%SRN, j-j%SRN, num));
        }
        boolean unUsedInRow(int i,int num)
        {
            for (int j = 0; j<N; j++)
                if (mat[i][j] == num)
                    return false;
            return true;
        }
        boolean unUsedInCol(int j,int num)
        {
            for (int i = 0; i<N; i++)
                if (mat[i][j] == num)
                    return false;
            return true;
        }
        boolean fillRemaining(int i, int j)
        {
            if (j>=N && i<N-1)
            {
                i = i + 1;
                j = 0;
            }
            if (i>=N && j>=N)
                return true;

            if (i < SRN)
            {
                if (j < SRN)
                    j = SRN;
            }
            else if (i < N-SRN)
            {
                if (j==(int)(i/SRN)*SRN)
                    j =  j + SRN;
            }
            else
            {
                if (j == N-SRN)
                {
                    i = i + 1;
                    j = 0;
                    if (i>=N)
                        return true;
                }
            }

            for (int num = 1; num<=N; num++)
            {
                if (CheckIfSafe(i, j, num))
                {
                    mat[i][j] = num;
                    if (fillRemaining(i, j+1))
                        return true;

                    mat[i][j] = 0;
                }
            }
            return false;
        }
        public void removeKDigits()
        {
            int count = K;
            while (count != 0)
            {
                int cellId = randomGenerator(N*N)-1;
                // extract coordinates i  and j
                int i = (cellId/N);
                int j = cellId%N;
                if (mat[i][j] != 0)
                {
                    count--;
                    mat[i][j] = 0;
                }
            }
        }

        // Print sudoku
        public void printSudoku() {
            for (int i = 0; i < GRID_SIZE; i++) {
                if (i % 3 == 0 && i != 0) {
                    System.out.println("-----------------");
                }
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (j % 3 == 0 && j != 0) {
                        System.out.print("|");
                    }
                    System.out.print(mat[i][j] + " ");
                }
                System.out.println();
            }
        }
    }

    private static boolean isNumberInRow(int[][] board, int number, int row) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[row][i] == number) {
                return true;
            }
        }
        return false;
    }
    private static boolean isNumberInColumn(int[][] board, int number, int column) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[i][column] == number) {
                return true;
            }
        }
        return false;
    }
    private static boolean isNumberInBox(int[][] board, int number, int row, int column) {
        int LocalBoxRow = row - row % 3;
        int LocalBoxColumn = column - column % 3;
        for (int i = LocalBoxRow; i < LocalBoxRow + 3; i++) {
            for (int j = LocalBoxColumn; j < LocalBoxColumn + 3; j++) {
                if (board[i][j] == number) {
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean isValidPlacement(int board[][], int number, int row, int column) {
        return !isNumberInRow(board, number, row) && !isNumberInColumn(board, number, column)
                && !isNumberInBox(board, number, row, column);
    }
    private static boolean solveboard(int[][] mat) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                if (mat[row][column] == 0) {
                    for (int numbertry = 1; numbertry <= GRID_SIZE; numbertry++) {
                        if (isValidPlacement(mat, numbertry, row, column)) {
                            mat[row][column] = numbertry;
                            if (solveboard(mat)) {
                                return true;
                            }
                            else {
                                mat[row][column] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
}
