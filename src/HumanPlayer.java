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
    
    public void cross(int color, int number, Dice[] dice, isActive) {
        boolean valid = numIsValid(color, number, dice, isActive);
        
        if(!valid) {
            // output that is valid to cross
            return;
        }
        
        crossNumber(color, number);
    }

    public void setName(String newName) {
        this.name = newName;
    }
}
