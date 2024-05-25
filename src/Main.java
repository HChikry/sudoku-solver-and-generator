import sudoku.EmptyPuzzleException;
import sudoku.OutOfGridException;
import sudoku.PuzzleOrder;
import sudoku.Sudoku;
import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws OutOfGridException, EmptyPuzzleException {
        int choice = 0;
        String[] sudokuPuzzle;
        Sudoku game;

        while (true) {
            choice = mainMenu();

            switch (choice) {
                case 1:
                    sudokuPuzzle = getPuzzleFromUser();
                    game = new Sudoku(sudokuPuzzle, PuzzleOrder.ROWS);
                    break;

                case 2:
                    String filepath = getFilePath();
                    game = new Sudoku(filepath, PuzzleOrder.ROWS);
                    break;

                default:
                    endMessagePrinter();
                    System.exit(0);
                    return;
            }

            game.solve();
            game.print();

            System.out.println("\n\t-------------------------------------------------");
            System.out.println("\t|                    Message                    |");
            System.out.println("\t-------------------------------------------------");
            System.out.println("\t|     Do You Want to Enter Another Game?        |");
            System.out.println("\t|            1. Yes         2. No               |");
            System.out.println("\t-------------------------------------------------\n");

            int continueChoice = intInput(1, 2);
            if (continueChoice == 2) break;
        }

        endMessagePrinter();
    }

    private static void endMessagePrinter() {
        System.out.println("\n\t-------------------------------------------------");
        System.out.println("\t|                   Thank YOU!                  |");
        System.out.println("\t-------------------------------------------------");
        System.out.println("\t|         Have a Good Day, Goodbye :)           |");
        System.out.println("\t-------------------------------------------------\n");
    }
    private static void logoPrinter() {
        System.out.println("\n\t\t\t ==================================");
        System.out.println("\t\t\t |    Welcome to Sudoku Solver    |");
        System.out.println("\t\t\t ==================================\n");
    }

    private static  int mainMenu() {
        String input = "";
        int choice = 0;
        boolean isInputValid = false;

        logoPrinter();
        System.out.println("\t-------------------------------------------------");
        System.out.println("\t|                     Menu                      |");
        System.out.println("\t-------------------------------------------------");
        System.out.println("\t|    1. Enter the Sudoku Manually.              |");
        System.out.println("\t|    2. Enter the Sudoku Using txt File.        |");
        System.out.println("\t|    3. Exit.                                   |");
        System.out.println("\t-------------------------------------------------\n");

        choice = intInput(1, 3);
        return choice;
    }

    private static String[] getPuzzleFromUser() {
        System.out.println("\t-------------------------------------------------");
        System.out.println("\t|                     Message                   |");
        System.out.println("\t-------------------------------------------------");
        System.out.println("\t|       Enter the Rows of the Puzzle            |");
        System.out.println("\t|          - Use Spaces as Separators           |");
        System.out.println("\t|          - Use \"0\" for Empty Squares          |");
        System.out.println("\t-------------------------------------------------\n");

        String[] puzzle = new String[9];

        for (int i = 0; i < Sudoku.GRID_SIZE; ++i) {
            System.out.println("=================================================");
            System.out.print(" Row " + (i + 1) + ": ");

            puzzle[i] = inputHandler();

            if (puzzle[i] == null) {
                try {
                    throw new MissingInputException();
                } catch (MissingInputException e) {
                    System.out.println("=================================================\n");
                    System.out.println("\t-------------------------------------------------");
                    System.out.println("\t|                     Message                   |");
                    System.out.println("\t-------------------------------------------------");
                    System.out.println("\t|          Please Enter the Row Again!          |");
                    System.out.println("\t-------------------------------------------------");

                    --i;
                }
            }
        }
        System.out.println("=================================================\n");
        System.out.println("\t-------------------------------------------------");
        System.out.println("\t|                      Message                  |");
        System.out.println("\t-------------------------------------------------");
        System.out.println("\t|                       Done..!                 |");
        System.out.println("\t-------------------------------------------------");

        return puzzle;
    }

    private static String getFilePath() {
        boolean isFilepathValid = false;
        String filePath;

        do {
            logoPrinter();

            System.out.println("\t-------------------------------------------------");
            System.out.println("\t|                     Message                   |");
            System.out.println("\t-------------------------------------------------");
            System.out.println("\t|     Please Enter the Absolute File Path.      |");
            System.out.println("\t|                                               |");
            System.out.println("\t|  e.g.,   C:\\user\\profile\\Desktop\\file.txt     |");
            System.out.println("\t|                                               |");
            System.out.println("\t|  e.g.,   /Users/username/Desktop/file.txt     |");
            System.out.println("\t-------------------------------------------------\n");

            System.out.println("====================================================");
            System.out.print("The Path: ");
            filePath = inputHandler();

            try {
                if (filePath == null) {
                    throw new MissingInputException();
                }

                File path = new File(filePath);
                if (!path.exists()) {
                    throw new InvalidFilePathException();
                }
                isFilepathValid = true;
            } catch (InvalidFilePathException | MissingInputException e) {
                System.out.println("\t-------------------------------------------------");
                System.out.println("\t|                     Message                   |");
                System.out.println("\t-------------------------------------------------");
                System.out.println("\t|          " + e.getMessage() + "         |");
                System.out.println("\t-------------------------------------------------");
            }
        } while (!isFilepathValid);

        return filePath;
    }

    private static String inputHandler() {
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        System.out.println("====================================================");
        return input;
    }

    private static int intInput(int start, int end) {
        String input = "";
        int choice = 0;
        boolean isInputValid = false;

        do {
            try {
                System.out.println("====================================================");
                System.out.print(" Your Choice: ");
                input = inputHandler();
                if (input.isBlank()) throw new NumberFormatException();
                choice = Integer.parseInt(input);

                if (choice < start || choice > end) {
                    throw new NumberFormatException();
                }
                isInputValid = true;
            } catch (NumberFormatException e) {
                System.out.println("\n\t-------------------------------------------------");
                System.out.println("\t|                     Message                   |");
                System.out.println("\t-------------------------------------------------");
                System.out.println("\t|          Please Enter a valid Choice!         |");
                System.out.println("\t-------------------------------------------------\n");
            }
        } while (!isInputValid);

        return choice;
    }
}

