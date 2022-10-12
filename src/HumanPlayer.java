public class HumanPlayer extends Player {

    public HumanPlayer(String name) {
        super(name);
    }

    public void skipRound(boolean active) {
        if(active) {
            sheet.addPenalty();
        } else {
            return;
        }
    }

    public boolean cross(int color, int number, int[] points, boolean isActive) {
        boolean valid = numIsValid(color, number, points, isActive);

        if(!valid) {
            // output that is not valid to cross
            return false;
        }

        crossNumber(color, number);
        return true;
    }

    public void setName(String newName) {
        this.name = newName;
    }
}
