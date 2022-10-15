public class HumanPlayer extends Player {
    /**
     * Constructor for the HumanPlayer, which stores the player name.
     */
    public HumanPlayer(String name) {
        super(name);
    }

    /**
     * If the player is active and decides to skip round, they get a penalty
     * @param active is true when the player is active
     */
    public void skipRound(boolean active) {
        if(active) {
            sheet.addPenalty();
        } else {
            return;
        }
    }
}
