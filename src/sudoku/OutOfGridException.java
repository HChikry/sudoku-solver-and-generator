package sudoku;

public class OutOfGridException extends Exception {
    public OutOfGridException() {
        super("You Passed a Puzzle that is Not 9*9 Numbers!");
    }
}
