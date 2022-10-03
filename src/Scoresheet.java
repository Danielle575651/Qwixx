public class ScoreSheet {
    public static final int MIN_CROSS = 4;
    public static final int LOCK_VALUE = 0;
    public static final int PENALTY_VALUE = -5;
    public static final int[][] DEFAULT_NUMBERS = {{2,3,4,5,6,7,8,9,10,11,12,0},
            {2,3,4,5,6,7,8,9,10,11,12,0},{12,11,10,9,8,7,6,5,4,3,2,0},{12,11,10,9,8,7,6,5,4,3,2,0}};

    private boolean[][] scored;
    private boolean[] validRows; // This ensures that a color and thus a row can disappear
    private int rows;
    private int columns;
    private int penalties;

    public ScoreSheet() {
        rows = DEFAULT_NUMBERS.length;
        columns = DEFAULT_NUMBERS[0].length;
        scored = new boolean[rows][columns];
        validRows = new boolean[rows];
        for (int i = 0; i < rows; i++) {
            validRows[i] = true;
        }
    }

    public void removeColor(int row) {
        validRows[row] = false;
    }

    public int getRows() {
        return  rows;
    }

    public int getColumns() {
        return columns;
    }

    public void addPenalty() {
         penalties++;
    }

    // Gets the number of penalties
    public int getPenaltyValue() {
        return penalties;
    }

    // Always first check if the value can be crossed before crossing!
    public void cross(int row, int column) {
        scored[row][column] = true;

        // If the last colored number is crossed in a row, the lock is also crossed of.
        // Columns starting at 0, number of columns starting at 1, therefore the minus one.
        if (column == getColumns() - 1) {
            scored[row][column + 1] = true;
            validRows[row] = false;
        }
    }

    // Also checks if a lock can be crossed
    public boolean canCross(int row, int column) {
        for (int i = getColumns() - 1; i >= column; i--) {
            // If the number at the index or before are already crossed then cannot be crossed again.
            if (scored[row][i]) {
                return false;
            }
        }
        // If we want to cross for example twelve, but we do not have already four crosses, we cannot lock 12 and
        // cannot lock the color. The lock can only be crossed after crossing a two or twelve. Or the number
        // that wants to be crossed is outside the score sheet.
        if (column == getColumns() - 1 && getNumberCrossed() < MIN_CROSS || getValue(row, column) == LOCK_VALUE
                || !validRows[row] || row < 0 || row > getRows() || column < 0 || column > getColumns()) {
            return false;
        }

        return true;
    }

    // Ensures that a cross can be crossed and that the value on the dices matches the number on the score sheet
    public boolean canCross(int row, int column, int diceValue) {
        return canCross(row, column) && diceValue == getValue(row, column);
    }

    public int getValue(int row, int column) {
        return DEFAULT_NUMBERS[row][column];
    }
    public int getNumberCrossed() {

    }

    // Gets the score per row using the scored 2D array
    public int getScore(int row) {

    }

    public int getTotalScore() {

    }


}
