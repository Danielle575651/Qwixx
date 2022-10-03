public class ScoreSheet {
    public static final int MIN_CROSS = 5;
    public static final int LOCK_VALUE = 0;
    public static final int PENALTY_VALUE = -5;
    public static final int[][] DEFAULT_NUMBERS = {{2,3,4,5,6,7,8,9,10,11,-12,0},
            {2,3,4,5,6,7,8,9,10,11,-12,0},{12,11,10,9,8,7,6,5,4,3,-2,0},{12,11,10,9,8,7,6,5,4,3,-2,0}};

    private boolean[][] scored;
    private boolean[] validRows;
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


}
