public abstract class Player {
    public Scoresheet sheet;
    private final String name;

    // For a new player, generate a score sheet
    public Player(String name) {
        this.sheet = new Scoresheet();
        this.name = name;
    }

    public final void tossDice(Dice[] dice) {
        for(Dice die : dice) {
            die.rollDice();
        }
    }

    public final void crossNumber(int color, int number) {
        if(color == 0 || color == 1) {
            sheet.cross(color, number - 2);
        } else {
            sheet.cross(color, 12 - number);
        }
    }
}
