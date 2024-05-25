package sudoku;

public class EmptyPuzzleException extends Exception {
    public EmptyPuzzleException() {
        super("You Didn't Pass Any Numbers!");
    }
}
