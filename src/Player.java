public abstract class Player {
    public Scoresheet sheet;
    private String name;
    private boolean isActive;

    // For a new player, generate a new score sheet
    public Player(String name) {
        this.sheet = new Scoresheet();
        this.name = name;
        isActive = false;
    }

    public Scoresheet getSheet() {
        return this.sheet;
    }

    public String getName() {
        return this.name;
    }

    public void changeName(String newName) {
        this.name = newName;
    }

    public void crossNumber(int color, int number) {
        if (color == 0 || color == 1) {
            sheet.cross(color, number - 2);
        } else {
            sheet.cross(color, 12 - number);
        }
    }

    // Gets the state of the player to determine if the choices made by this player are allowed.
    public boolean isActive() {
        return isActive;
    }

    public final void changeState() {
        isActive = !isActive;
    }

    // Get combination of white dice
    public int getWhiteComb(int[] points) {
        return points[0] + points[1];
    }

    // Compute the color combination
    public int[] getColorComb(int[] points) {
        int numColor = points.length - 2; // Number of color dice
        int numCombs = 2 * numColor;
        int[] whiteColor = new int[numCombs];

        // Compute the possible combination
        for (int i = 0; i < 4; i++) {
            if (!sheet.getValidRow(i)) { // if a color is not valid then its combinations = 0
                whiteColor[i] = 0;
                whiteColor[i + 4] = 0;
                continue;
            }

            whiteColor[i] = points[0] + points[i + 2];
            whiteColor[i + 4] = points[1] + points[i + 2];
        }

        return whiteColor;
    }

    // Check if a value if available in the combination
    public boolean numIsValid(int color, int value, int[] points, boolean active) {
        // for non-active, only check white comb
        int whiteComb = getWhiteComb(points);

        if (!active && value == whiteComb) {
            return true;
        }

        if (!active) {
            return false;
        }

        // for active
        if (value == whiteComb) {
            return true;
        }

        int[] comb = getColorComb(points);
        // Check if the color is invalid
        if (comb[color] == 0 || comb[color + 4] == 0) {
            return false;
        }
        // Check if the value is valid
        return value == comb[color] || value == comb[color + 4];
    }
}
