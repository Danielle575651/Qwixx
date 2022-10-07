public abstract class Player {
    public Scoresheet sheet;
    private final String name;

    // For a new player, generate a score sheet
    public Player(String name) {
        this.sheet = new Scoresheet();
        this.name = name;
    }

    /**
    public final int[] tossDice() {
        Dices dices = new Dices();
        return dices.getValues();
    }*/

    // For non-active player - choose to skip the round
    public final void skipNonActive() {
        // do something to skip
    }



    //public abstract void crossNumber(int color, int number);
}