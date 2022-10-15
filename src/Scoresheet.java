/**
 * Class that contains the methods for the ScoreSheet of the Qwixx game.
 *
 * @author Amber Cuijpers, Danielle Lam, Khue Nguyen, Yu-Shan Cho, Yuntong Wu
 */
public class Scoresheet {
    public static final int MIN_CROSS = 5;
    public static final int LOCK_VALUE = 0;
    public static final int PENALTY_VALUE = -5;
    public static final int[][] DEFAULT_NUMBERS = {{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0},
            {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0}, {12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 0}, {12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 0}};

    private boolean[][] scored;
    // if possible, change to private for validRows
    public boolean[] validRows; // This ensures that a color and thus a row can disappear
    private int rows;
    private int columns;
    private int penalties;

    /**
     * Constructor for the Scoresheet, which initializes the values.
     */
    public Scoresheet() {
        rows = DEFAULT_NUMBERS.length;
        columns = DEFAULT_NUMBERS[0].length;
        scored = new boolean[rows][columns];
        validRows = new boolean[rows];
        //for every row, set the initial row on true, when being false a row disappears
        for (int i = 0; i < rows; i++) {
            validRows[i] = true;
        }
    }

    /**
     * Method which sets the row to false of the specific color which needs to be removed.
     * @param row row which belongs to the color which needs to be removed
     */
    public void removeColor(int row) {
        validRows[row] = false;
    }

    /**
     * Method which sets the row to true of the specific color which needs to be added.
     * @param row row which belongs to the color which needs to be added
     */
    public void addColor(int row) {
        validRows[row] = true;
    }

    /**
     * Method that returns the amount of rows.
     * @return the amount of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Method that returns the amount of columns.
     * @return the amount of columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Method that adds a penalty to the total amount of already existing penalties.
     */
    public void addPenalty() {
        penalties++;
    }

    /**
     * Method that returns the total amount of penalties that are given
     * @return the number of penalties
     */
    public int getPenaltyValue() {
        return penalties;
    }

    /**
     * Method that crosses a specific element (a given number of a given color), and when the last colored number of
     * the row is crossed, the last element also will be crossed.
     * @param row the row, corresponding to one of the four colors
     * @param column the column, which indicates which number needs to be crossed
     */
    public void cross(int row, int column) {
        scored[row][column] = true;

        // If the last colored number is crossed in a row, the lock is also crossed of.
        if (column == getColumns() - 2) {
            scored[row][column + 1] = true;
            removeColor(row);
        }
    }

    /**
     * Method that removes the cross from the belonging element, corresponding to a specific row and column.
     * @param row the row, corresponding to one of the four colors
     * @param column the column, which indicates which number needs to be crossed
     */
    public void removeCross(int row, int column) {
        scored[row][column] = false;

        // If 2 or 12 is uncrosses and the color is not added back yet, we add the color back to the game
        if (column == getColumns() - 2) {
            addColor(row);
        }
    }

    /**
     * Method that checks whether a cross is allowed on the place of the specific row and column. It also checks whether
     * a lock is allowed.
     * @param row the row, corresponding to one of the four colors
     * @param column the column, which indicates which number needs to be crossed
     * @return a boolean whether the cross is allowed to place
     */
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
        if ((column == getColumns() - 2 && getNumberCrossed(row) < MIN_CROSS) || getValue(row, column) == LOCK_VALUE
                || !validRows[row] || row < 0 || row > getRows() || column < 0 || column > getColumns()) {
            return false;
        }

        return true;
    }

    /**
     * Method that ensures whether a cross that wants to be placed is allowed and if the number of the scoresheet matches
     * with the value on the dices.
     * @param row the row, corresponding to one of the four colors
     * @param column the column, which indicates which number needs to be crossed
     * @param diceValue integer that gives the value on the dice
     * @return a boolean whether the cross is allowed to place and matches the dice value.
     * @see #canCross(int, int)
     */
    public boolean canCross(int row, int column, int diceValue) {
        return canCross(row, column) && diceValue == getValue(row, column);
    }

    /**
     * Method that gets the value of the corresponding row and column.
     * @param row the row, corresponding to one of the four colors
     * @param column the column, which indicates which number needs to be crossed
     * @return the value of the corresponding row and column
     */
    public int getValue(int row, int column) {
        return DEFAULT_NUMBERS[row][column];
    }

    /**
     * Method that returns the amount of numbers that are crossed in a specific row.
     * @param row the row, corresponding to one of the four colors
     * @return an integer with the amount of numbers in a row that are crossed
     */
    public int getNumberCrossed(int row) {
        int numbers = 0;
        if (row >= 0 && row < getRows()) {
            for (boolean var : scored[row]) {
                if (var) {
                    numbers++;
                }
            }
        }
        return numbers;
    }

    /**
     * Method that returns the score for a given row.
     * @param row the row, corresponding to one of the four colors
     * @return an integer with the total score of one row
     */
    public int getScore(int row) {
        int score = 0;
        int numbersCrossed = getNumberCrossed(row);
        for (int i = 0; i <= numbersCrossed; i++) {
            score += i;
        }
        return score;
    }

    /**
     * Method that returns the overall score, when summing up the scores of all the rows and subtracting the penalty
     * value times the amount of penalties.
     * @return the total score of the score sheet
     */
    public int getTotalScore() {
        int score = 0;
        for (int row = 0; row < getRows(); row++) {
            score += getScore(row);
        }
        return score + PENALTY_VALUE * getPenaltyValue();
    }

    /**
     * Method that returns the last value that is crossed of a row.
     * @param row the row, corresponding to one of the four colors
     * @return an integer with the last value that is crossed in a row
     */
    public int getLastCrossed(int row) {
        int lastValue = 1;
        if (row == 2 || row == 3) {
            lastValue = 13;
        }

        for (int i = 0; i < getColumns(); i++) {
            if (scored[row][i]) {
                lastValue = getValue(row, i);
            }
        }
        return lastValue;
    }

    /**
     * Method which counts the total amount of locks that are crossed.
     * @return the amount of locks that are already crossed
     */
    public int getLocks() {
        int nLock = 0;
        for (boolean validRow : validRows) {
            if (!validRow) {
                nLock++;
            }
        }
        return nLock;
    }

    /**
     * Method which checks whether a specific row is still existing and not removed.
     * @param i, an integer which gives the row which will be checked whether it is removed
     * @return an boolean whether a row is still valid (existing) or not valid (removed)
     */
    public boolean getValidRow(int i) {
        return validRows[i];
    }

    /**
     * Method that returns the value of a penalty.
     * @return the value of one penalty
     */
    public int getPenalty() {
        return -PENALTY_VALUE;
    }
}
