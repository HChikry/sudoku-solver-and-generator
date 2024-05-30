package sudoku;

import java.io.*;
import java.security.SecureRandom;
import java.util.Arrays;

public class Sudoku {
    private String filePath;
    private final int[][] board;
    private int[][] solvedBoard;
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

    public Sudoku(String[] puzzle, PuzzleOrder order) throws OutOfGridException, EmptyPuzzleException, NumberFormatException {
        if (puzzle.length != 9) {
            throw new OutOfGridException();
        }

        for (String var : puzzle) {
            if (var == null || var.isBlank()) {
                throw new EmptyPuzzleException();
            }
        }

        if (order == PuzzleOrder.ROWS) {
            this.board = parseRows(puzzle);
        } else {
            this.board = parseColumns(puzzle);
        }
    }

    public Sudoku(String filePath, PuzzleOrder order) throws OutOfGridException, EmptyPuzzleException, NumberFormatException, IOException {
        this.filePath = filePath;
        String[] puzzle;

        try {
            puzzle = getPuzzleFromFile();
        } catch (IOException e) {
            throw e;
        }

        if (puzzle.length != 9) {
            throw new OutOfGridException();
        }

        for (String var : puzzle) {
            if (var == null || var.isBlank()) {
                throw new EmptyPuzzleException();
            }
        }

        if (order == PuzzleOrder.ROWS) {
            this.board = parseRows(puzzle);
        } else {
            this.board = parseColumns(puzzle);
        }
    }

    public static int[][] parseRows(String[] puzzleRows) throws NumberFormatException {
        int[][] sudokuBoard = new int[9][9];

        for (int i = 0; i < GRID_SIZE; ++i) {
            String[] parts = puzzleRows[i].split("[,\\s]+");
            for (int j = 0; j < GRID_SIZE; ++j) {
                sudokuBoard[i][j] = Integer.parseInt(parts[j]);
            }
        }

        return sudokuBoard;
    }

    public static int[][] parseColumns(String[] puzzleColums) throws NumberFormatException {
        int[][] sudokuBoard = new int[9][9];

        for (int i = 0; i < GRID_SIZE; ++i) {
            String[] parts = puzzleColums[i].split("[,\\s]+");
            for (int j = 0; j < GRID_SIZE; ++j) {
                sudokuBoard[j][i] = Integer.parseInt(parts[j]);
            }
        }

        return sudokuBoard;
    }

    private String[] getPuzzleFromFile() throws IOException {
        String[] puzzle = new String[9];
        String buffer;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.filePath));
            int i = 0;
            while ((buffer = reader.readLine()) != null) {
                if (buffer.isBlank()) continue;
                puzzle[i++] = buffer;
            }
            reader.close();
        } catch (IOException e) {
            throw e;
        }

        return puzzle;
    }

    public boolean solve() {
        this.solvedBoard = new int[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; ++i) {
            this.solvedBoard[i] = Arrays.copyOf(this.board[i], this.board[i].length);
        }

        return solver(this.solvedBoard, 0, 0);
    }

    public static int[][] solve(int[][] game) throws OutOfGridException, EmptyPuzzleException {
        if (game.length != 9) throw new OutOfGridException();

        boolean areAllValuesEmpty = true;
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                if (game[i][j] != 0) {
                    areAllValuesEmpty = false;
                }
            }
        }

        if (areAllValuesEmpty) throw new EmptyPuzzleException();

        int[][] clone = new int[GRID_SIZE][];
        for (int i = 0; i < GRID_SIZE; ++i) {
            clone[i] = Arrays.copyOf(game[i], game[i].length);
        }
        if (!solver(clone, 0, 0)) return null;

        return clone;
    }

    private static boolean solver(int[][] game, int currentRow, int currentColumn) {
        if (currentRow == GRID_SIZE) return true;

        for (int r = currentRow; r < GRID_SIZE; ++r) {
            for (int c = currentColumn; c < GRID_SIZE; ++c) {
                if (game[r][c] != 0) continue;

                for (int n = 1; n < 10; ++n) {
                    if (!isNumberValid(game, n, r, c)) continue;

                    game[r][c] = n;
                    if (solver(game, r, c+1)) return true;
                    game[r][c] = 0;
                }

                return false;
            }
            currentColumn = 0;
        }

        return true;
    }

    public static int[][] generate(int numberOfSquaresToFill) {
        int[][] game = new int[GRID_SIZE][GRID_SIZE];
        creator(game, 0, 0);

        SecureRandom secureRandom = new SecureRandom();
        int r = 0, c = 0;
        for (int i = 0; i < 81 - numberOfSquaresToFill; ++i) {
            do {
                r = secureRandom.nextInt(9);
                c = secureRandom.nextInt(9);
            } while (game[r][c] == 0);
            game[r][c] = 0;
        }

        return game;
    }

   private static boolean creator(int[][] game, int currentRow, int currentColumn) {
       if (currentRow == GRID_SIZE) return true;

       SecureRandom secureRandom = new SecureRandom();
       int[] possibleNumbers = new int[GRID_SIZE];
       for (int i = 0; i < GRID_SIZE; i++) {
           possibleNumbers[i] = i + 1;
       }

       int n;
       for (int r = currentRow; r < GRID_SIZE; ++r) {
           for (int c = currentColumn; c < GRID_SIZE; ++c) {
               if (game[r][c] != 0) continue;

               // finding the number
               for (int i = 0; i < GRID_SIZE; ++i) {
                   // selecting a possible number
                   do {
                       n = secureRandom.nextInt(9) + 1;
                   } while (possibleNumbers[n-1] == 0);

                   possibleNumbers[n-1] = 0; // making that number not possible anymore
                   if (!isNumberValid(game, n, r, c)) continue;

                   game[r][c] = n;
                   if (creator(game, r, c+1)) return true;
                   game[r][c] = 0;
               }

               return false;
           }
           currentColumn = 0;
       }

       return true;
   }

    private static boolean isNumberValid(int[][] board, int number, int numberRow, int numberColumn) {
        // searching  in each column of the same square's row
        for (int i = 0; i < GRID_SIZE; ++i) {
            if (i == numberColumn) continue;
            if (board[numberRow][i] == number) return false;
        }

        // searching in each row of the same square's column
        for (int i = 0; i < GRID_SIZE; ++i) {
            if (i == numberRow) continue;
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
                if (i == numberRow && j == numberColumn) continue;
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
                System.out.print(this.board[i][j] + " ");
            }

            System.out.println("|");
        }

        System.out.println("\t-------------------------");
    }

    public void printSolution() {
        System.out.println();

        for (int i = 0; i < GRID_SIZE; ++i) {
            if (i % 3 == 0) {
                System.out.println("\t-------------------------");
            }

            System.out.print("\t");
            for (int j = 0; j < GRID_SIZE; ++j) {
                if (j % 3 == 0) System.out.print("| ");
                System.out.print(this.solvedBoard[i][j] + " ");
            }

            System.out.println("|");
        }

        System.out.println("\t-------------------------");
    }

    public static void print(int[][] game) throws OutOfGridException, EmptyPuzzleException {
        if (game == null) throw new EmptyPuzzleException();
        if (game.length != 9) throw new OutOfGridException();

        System.out.println();

        for (int i = 0; i < GRID_SIZE; ++i) {
            if (i % 3 == 0) {
                System.out.println("\t-------------------------");
            }

            System.out.print("\t");
            for (int j = 0; j < GRID_SIZE; ++j) {
                if (j % 3 == 0) System.out.print("| ");
                System.out.print(game[i][j] + " ");
            }

            System.out.println("|");
        }

        System.out.println("\t-------------------------");
    }

    public static void saveToFile(int[][] game, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                writer.write(game[i][j] + " ");
                if ((j+1) % 3 == 0) {
                    writer.write("  ");
                }
            }
            writer.newLine();
            if ((i+1) % 3 == 0) writer.newLine();
        }
        writer.close();
    }

    public void saveSolutionToFile(String filePath) throws EmptyPuzzleException, IOException {
        if (this.solvedBoard == null) throw new EmptyPuzzleException();

        saveToFile(this.solvedBoard, filePath);
    }

    public static void generateToFile(String filePath, int numberOfSquaresToFill) throws IOException {
        saveToFile(Sudoku.generate(numberOfSquaresToFill), filePath);
    }
}
