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
}
