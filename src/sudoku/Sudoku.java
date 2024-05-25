package sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Sudoku {
    private String filePath;
    private String[] puzzle;
    private final int[][] board;
    public static final int GRID_SIZE = 9;

    public Sudoku(int[][] board) throws OutOfGridException, EmptyPuzzleException {
        if (board.length != 9) {
            throw new OutOfGridException();
        }
        boolean areAllValuesEmpty = true;
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                if (board[i][j] != 0) {
                    areAllValuesEmpty = false;
                }
            }
        }

        if (areAllValuesEmpty) {
            throw new EmptyPuzzleException();
        }
        this.board = board;
    }

    public Sudoku(String[] puzzle, PuzzleOrder order) throws OutOfGridException, EmptyPuzzleException {
        if (puzzle.length != 9) {
            throw new OutOfGridException();
        }

        for (String var : puzzle) {
            if (var == null || var.isBlank()) {
                throw new EmptyPuzzleException();
            }
        }

        this.puzzle = puzzle;
        if (order == PuzzleOrder.ROWS) {
            this.board = parseRows(puzzle);
        } else {
            this.board = parseColumns(puzzle);
        }
    }

    public Sudoku(String filePath, PuzzleOrder order) throws OutOfGridException, EmptyPuzzleException {
        this.filePath = filePath;
        String[] puzzle = getPuzzleFromFile();

        if (puzzle.length != 9) {
            throw new OutOfGridException();
        }

        for (String var : puzzle) {
            if (var == null || var.isBlank()) {
                throw new EmptyPuzzleException();
            }
        }

        this.puzzle = puzzle;
        if (order == PuzzleOrder.ROWS) {
            this.board = parseRows(puzzle);
        } else {
            this.board = parseColumns(puzzle);
        }
    }

    public static int[][] parseRows(String[] puzzleRows) {
        int[][] sudokuBoard = new int[9][9];

        for (int i = 0; i < GRID_SIZE; ++i) {
            String[] parts = puzzleRows[i].split("[,\\s]+");
            for (int j = 0; j < GRID_SIZE; ++j) {
                try {
                    sudokuBoard[i][j] = Integer.parseInt(parts[j]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + parts[j]);
                }
            }
        }

        return sudokuBoard;
    }

    public static int[][] parseColumns(String[] puzzleColums) {
        int[][] sudokuBoard = new int[9][9];

        for (int i = 0; i < GRID_SIZE; ++i) {
            String[] parts = puzzleColums[i].split("[,\\s]+");
            for (int j = 0; j < GRID_SIZE; ++j) {
                try {
                    sudokuBoard[j][i] = Integer.parseInt(parts[j]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format: " + parts[j]);
                }
            }
        }

        return sudokuBoard;
    }

    private String[] getPuzzleFromFile() {
        String[] puzzle = new String[9];
        String buffer = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.filePath));
            int i = 0;
            while ((buffer = reader.readLine()) != null) {
                if (buffer.isBlank()) continue;
                puzzle[i++] = buffer;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return puzzle;
    }

    public boolean solve() {
        return solver((int) 0, (int) 0);
    }

    private boolean solver(int currentRow, int currentColumn) {
        if (currentRow == GRID_SIZE) return true;

        for (int r = currentRow; r < GRID_SIZE; ++r) {
            for (int c = 0; c < GRID_SIZE; ++c) {
                if (board[r][c] != 0) continue;

                for (int n = 1; n < 10; ++n) {
                    if (!isNumberValid(n, r, c)) continue;

                    board[r][c] = n;
                    if (solver(r, c)) return true;
                    board[r][c] = 0;
                }

                return false;
            }
        }

        return true;
    }

    private boolean isNumberValid(int number, int numberRow, int numberColumn) {
        // searching  in each column of the same square's row
        for (int i = 0; i < GRID_SIZE; ++i) {
            if (board[numberRow][i] == number) return false;
        }

        // searching in each row of the same square's column
        for (int i = 0; i < GRID_SIZE; ++i) {
            if (board[i][numberColumn] == number) return false;
        }

        // searching in the big square of our square
        int startingRowIndex = 0, startingColumnIndex = 0;
        if (numberRow < GRID_SIZE) startingRowIndex = 6;
        if (numberRow < 6) startingRowIndex = 3;
        if (numberRow < 3 ) startingRowIndex = 0;

        if (numberColumn < GRID_SIZE) startingColumnIndex = 6;
        if (numberColumn < 6) startingColumnIndex = 3;
        if (numberColumn < 3 ) startingColumnIndex = 0;

        for (int i = startingRowIndex; i < startingRowIndex + 3; ++i) {
            for (int j = startingColumnIndex; j < startingColumnIndex + 3; ++j) {
                if (board[i][j] == number) return false;
            }
        }

        return true;
    }

    public void print() {
        System.out.println();

        for (int i = 0; i < GRID_SIZE; ++i) {
            if (i % 3 == 0) {
                System.out.println("\t-------------------------");
            }

            System.out.print("\t");
            for (int j = 0; j < GRID_SIZE; ++j) {
                if (j % 3 == 0) System.out.print("| ");
                System.out.print("" + board[i][j]);
            }

            System.out.println("|");
        }

        System.out.println("\t-------------------------");
    }
}
