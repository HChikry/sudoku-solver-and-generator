import sudoku.EmptyPuzzleException;
import sudoku.OutOfGridException;
import sudoku.PuzzleOrder;
import sudoku.Sudoku;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        mainMenu();
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
        System.out.println("\n\t\t    ==================================");
        System.out.println("\t\t    |    Welcome to Sudoku Solver    |");
        System.out.println("\t\t    ==================================\n");
    }

    private static void mainMenu() {
        while (true) {
            logoPrinter();
            System.out.println("\t-------------------------------------------------");
            System.out.println("\t|                     Menu                      |");
            System.out.println("\t-------------------------------------------------");
            System.out.println("\t|    1. Generate a Sudoku Puzzle.               |");
            System.out.println("\t|    2. Solve a Sudoku Puzzle.                  |");
            System.out.println("\t|    3. Exit.                                   |");
            System.out.println("\t-------------------------------------------------\n");

            int choice = intInput(1, 3);

            switch (choice) {
                case 1:
                    generationMenu();
                    break;

                case 2:
                    solutionMenu();
                    break;

                default:
                    return;
            }
        }
    }

    private static void generationMenu() {
        logoPrinter();
        System.out.println("\t-------------------------------------------------");
        System.out.println("\t|                     Message                    |");
        System.out.println("\t-------------------------------------------------");
        System.out.println("\t|    How Many Squares You Want to fill?          |");
        System.out.println("\t|         ( min = 1  and   max = 80)             |");
        System.out.println("\t-------------------------------------------------\n");
        int numberOfSquaresToFill = intInput(1, 80);

        logoPrinter();
        System.out.println("\t-------------------------------------------------");
        System.out.println("\t|                     Menu                      |");
        System.out.println("\t-------------------------------------------------");
        System.out.println("\t|    1. Print the Generated Sudoku.             |");
        System.out.println("\t|    2. Save it to a Txt File.                  |");
        System.out.println("\t|    3. Print and Save it to a Txt File.        |");
        System.out.println("\t|    4. Return.                                 |");
        System.out.println("\t-------------------------------------------------\n");

        int choice = intInput(1, 4);
        int[][] generatedPuzzle = Sudoku.generate(numberOfSquaresToFill);
        String filePath;

        switch (choice) {
            case 1:
                while (true) {
                    try { Sudoku.print(generatedPuzzle); }
                    catch (OutOfGridException | EmptyPuzzleException e) { continue; }
                    break;
                }
                break;

            case 2:
                filePath = getFilePath();
                while (true) {
                    try { Sudoku.saveToFile(generatedPuzzle, filePath); }
                    catch (IOException e) { continue; }
                    break;
                }
                break;

            case 3:
                filePath = getFilePath();
                while (true) {
                    try {
                        Sudoku.print(generatedPuzzle);
                        Sudoku.saveToFile(generatedPuzzle, filePath);
                    } catch (OutOfGridException | EmptyPuzzleException | IOException e) {
                        continue;
                    }
                    break;
                }
                break;

            default:
                break;
        }
    }

    private static void solutionMenu() {
        Sudoku game = null;

        while (true) {
            logoPrinter();
            System.out.println("\t-------------------------------------------------");
            System.out.println("\t|                     Menu                      |");
            System.out.println("\t-------------------------------------------------");
            System.out.println("\t|    1. Enter the Sudoku Manually.              |");
            System.out.println("\t|    2. Enter the Sudoku Using txt File.        |");
            System.out.println("\t|    3. Return.                                 |");
            System.out.println("\t-------------------------------------------------");

            int choice = intInput(1, 3);

            try {
                switch (choice) {
                    case 1:
                        String[] sudokuPuzzle = getPuzzleFromUser();
                        game = new Sudoku(sudokuPuzzle, PuzzleOrder.ROWS);
                        break;

                    case 2:
                        String filepath = getFilePath();
                        try {
                            game = new Sudoku(filepath, PuzzleOrder.ROWS);
                        } catch (IOException e) {
                            System.out.println(e.getMessage() + "\n\n");
                        }
                        break;

                    default:
                        return;
                }
            } catch (OutOfGridException | EmptyPuzzleException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                System.out.println("\n\t-------------------------------------------------");
                System.out.println("\t|                    Message                    |");
                System.out.println("\t-------------------------------------------------");
                System.out.println("\t|     The Puzzle You Gave Is Not Correctly      |");
                System.out.println("\t|    Formatted; Please Use Spaces or Commas     |");
                System.out.println("\t|               as Separators!                  |");
                System.out.println("\t-------------------------------------------------\n");
                continue;
            }
            break;
        }

        assert game != null;
        if (game.solve()) {
            System.out.println("\n\t-------------------------------------------------");
            System.out.println("\t|                    Message                    |");
            System.out.println("\t-------------------------------------------------");
            System.out.println("\t|  Voila, We Found a Solution for Your Puzzle!  |");
            System.out.println("\t-------------------------------------------------");
            game.printSolution();
        } else {
            System.out.println("\n\t-------------------------------------------------");
            System.out.println("\t|                    Message                    |");
            System.out.println("\t-------------------------------------------------");
            System.out.println("\t|     There Is No Solution to This Puzzle!      |");
            System.out.println("\t|            Please Check it Again!             |");
            System.out.println("\t-------------------------------------------------");
            game.print();
        }

        savingSolutionMenu(game);
    }

    private static void savingSolutionMenu(Sudoku game) {
        logoPrinter();
        System.out.println("\n\t-------------------------------------------------");
        System.out.println("\t|                    Message                    |");
        System.out.println("\t-------------------------------------------------");
        System.out.println("\t|  Do You Want to Save The Solution in a File.  |");
        System.out.println("\t|            1. Yes         2. No               |");
        System.out.println("\t-------------------------------------------------\n");

        if (intInput(1, 2) == 2) return;

        String filePath = getFilePath();
        while (true) {
            try {
                game.saveSolutionToFile(filePath);
            } catch (EmptyPuzzleException | IOException e) {
                continue;
            }
            break;
        }
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
            System.out.println("\t|  Please Enter the Absolute File Path, Which   |");
            System.out.println("\t|            Includes the File Name.            |");
            System.out.println("\t|                                               |");
            System.out.println("\t|  ex: C:\\user\\profile\\Desktop\\fileName.txt     |");
            System.out.println("\t|                                               |");
            System.out.println("\t|  ex: /Users/username/Desktop/fileName.txt     |");
            System.out.println("\t-------------------------------------------------\n");

            System.out.println("====================================================");
            System.out.print("The Path: ");
            filePath = inputHandler();

            try {
                if (filePath == null) {
                    throw new MissingInputException();
                }

                File path = new File(filePath);
                if (path.isDirectory()) {
                    throw new InvalidFilePathException();
                }
                isFilepathValid = true;
            } catch (InvalidFilePathException | MissingInputException e) {
                System.out.println("\t-------------------------------------------------");
                System.out.println("\t|                     Message                   |");
                System.out.println("\t-------------------------------------------------");
                System.out.println("\t|      " + e.getMessage() + "      |");
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
        int choice = 0;
        boolean isInputValid = false;

        do {
            try {
                System.out.println("====================================================");
                System.out.print(" Your Choice: ");
                String input = inputHandler();
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

