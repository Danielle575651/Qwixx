public class HumanPlayer extends Player {

    public HumanPlayer(String name) {
        super(name);
    }

    /**
    public void crossNumber(int color, int number) {
        if(!sheet.numberValid(color, number)) {
            System.out.println("Invalid choice of number!");
            return; //let the player choose again by calling again
        }

        sheet.crossNumber(color, number);
    }

    public void crossPenalty() {
        sheet.crossPenalty();
    }

    public void lock() {
        if(!sheet.lockValid()) {
            System.out.println("Invalid lock!");
            return; //have to let the player do sthg instead
        }

        sheet.lock();
    }*/
}
