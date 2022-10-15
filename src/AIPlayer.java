import java.util.Arrays;
import java.util.stream.IntStream;
/**
 * Class that contains method for the graphical user interface (GUI) of the score sheet for the AI player
 *
 * @author Amber Cuijpers, Danielle Lam, Khue Nguyen, Yu-Shan Cho, Yuntong Wu
 */
public class AIPlayer extends Player {
    public ScoreSheetAIPlayerGUI gui;
    /**
     * Constructor for the AIPlayer, which initializes the values and panels.
     */
    public AIPlayer() {
        super("AI Player");
        this.gui = new ScoreSheetAIPlayerGUI();
    }

    public ScoreSheetAIPlayerGUI getGUI() {
        return this.gui;
    }

    public void crossNumber(int color, int number) {
        if(color == 0 || color == 1) {
            sheet.cross(color, number - 2);
            this.gui.crossButton(color, number - 2);
        } else {
            sheet.cross(color, 12 - number);
            this.gui.crossButton(color, 12 - number);
        }
    }

    // Finding minimum gap of the white combination
    public int[] minGapWhite(int[] points) {
        int combination = getWhiteComb(points);
        int[] gap = new int[4];

        // Compute the gap for red and yellow, then green and blue (since they switch the order)
        for (int i = 0; i < 2; i++) {
            if(!sheet.getValidRow(i)) { // if a color is not valid then its gaps = 0
                gap[i] = -1;
                continue;
            }
            gap[i] = combination - sheet.getLastCrossed(i);
        }
        for (int i = 2; i < 4; i++) {
            if(!sheet.getValidRow(i)) { // if a color is not valid then its gaps = 0
                gap[i] = -1;
                continue;
            }
            gap[i] = sheet.getLastCrossed(i) - combination;
        }

        //Find minimum
        int minGap = 15;
        int indexMin = -1;
        for (int i = 0; i < 4; i++) {
            if (minGap > gap[i] && gap[i] > 0) {
                minGap = gap[i];
                indexMin = i;
            }
        }

        //return gapWhiteWhite;
        return new int[] {indexMin, minGap, combination};
    }

    public int[] minGapColor(int[] points) {
        int[] whiteColor = getColorComb(points);

        // Compute the gap
        int[] gap = new int[4];
        for (int i = 0; i < 2; i++) {
            if(!sheet.getValidRow(i)) {
                gap[i] = -1;
                continue;
            }
            gap[i] = whiteColor[i] - sheet.getLastCrossed(i);
            int other = whiteColor[i + 4] - sheet.getLastCrossed(i); // the gap value of the other combination of the same color
            if((gap[i] > other && other > 0) || (gap[i] < 0 && other > 0)) {
                gap[i] = other;
            }
        }
        for (int i = 2; i < 4; i++) {
            if(!sheet.getValidRow(i)) {
                gap[i] = -1;
                continue;
            }
            gap[i] = sheet.getLastCrossed(i) - whiteColor[i];
            int other = sheet.getLastCrossed(i) - whiteColor[i + 4]; // the gap value of the other combination of the same color
            if((gap[i] > other && other > 0) || (gap[i] < 0 && other > 0)) {
                gap[i] = other;
            }
        }

        //Find minimum
        int minGap = 15;
        int indexMin = -1;
        for (int i = 0; i < 2; i++) {
            if (minGap > gap[i]  && gap[i] > 0 && this.sheet.canCross(i, gap[i] + sheet.getLastCrossed(i) - 2)) {
                minGap = gap[i];
                indexMin = i;
            }
        }
        for (int i = 2; i < 4; i++) {
            if (minGap > gap[i]  && gap[i] > 0 && this.sheet.canCross(i, 12 + gap[i] - sheet.getLastCrossed(i))) {
                minGap = gap[i];
                indexMin = i;
            }
        }

        if(indexMin == -1) {
            return new int[] {indexMin, minGap, 0};
        }

        // index corresponds to color + minGap + number to cross
        if(indexMin == 0 || indexMin == 1) {
            return new int[] {indexMin, minGap, minGap + sheet.getLastCrossed(indexMin)};
        } else {
            return new int[] {indexMin, minGap, sheet.getLastCrossed(indexMin) - minGap};
        }
    }

    public void bestChoiceActive(int[] points) {
        // check if we can lock with the white combination
        int whiteComb = getWhiteComb(points);
        for(int i = 0; i < 4; i++) {
            if(sheet.canCross(i, 10, whiteComb)) { // checking if 12 (or 2) can be crossed
                sheet.cross(i, 10); //cross 12 (or 2)
                this.gui.crossButton(i,10);
                this.gui.crossButton(i,11);
                return;
            }
        }

        // check if we can lock with the color combination
        int[] colorComb = getColorComb(points);
        for(int combValue : colorComb) {
            for(int i = 0; i < 4; i++) {
                if(sheet.canCross(i, 10, combValue)) { // checking if 12 (or 2) can be crossed
                    sheet.cross(i, 10); //cross 12 (or 2)
                    this.gui.crossButton(i,10);
                    this.gui.crossButton(i,11);
                    return;
                }
            }
        }

        // Here we have an argument: should this exception 2 be considered at this position or later?
        if(whiteComb == 9 || whiteComb == 5 || IntStream.of(colorComb).anyMatch(x -> x == 9) || IntStream.of(colorComb).anyMatch(x -> x == 5)) {
            for(int i = 0; i < 2; i++) {
                if (sheet.getLastCrossed(i) == 7 && sheet.getNumberCrossed(i) == 3) {
                    if (sheet.canCross(i, 7, 9)) {
                        sheet.cross(i, 7);
                        this.gui.crossButton(i,7);
                        return;
                    }
                }
            }

            for(int i = 2; i < 4; i++) {
                if (sheet.getLastCrossed(i) == 7 && sheet.getNumberCrossed(i) == 3) {
                    if (sheet.canCross(i, 7, 5)) {
                        sheet.cross(i, 7);
                        this.gui.crossButton(i,7);
                        return;
                    }
                }
            }
        }

        int[] wwBestGap = minGapWhite(points);
        int wwRow = wwBestGap[0];
        int wwGap = wwBestGap[1];
        int wwNum = wwBestGap[2];
        int[] wcBestGap = minGapColor(points);
        int wcRow = wcBestGap[0];
        int wcGap = wcBestGap[1];
        int wcNum = wcBestGap[2];

        // If there is no valid gap then we must be penalized
        if (wwRow == -1 && wcRow == -1) {
            this.sheet.addPenalty();
            this.gui.crossPenalty(this.sheet.getPenaltyValue()-1);
            return;
        }

        // Case: same color
        if (wwRow == wcRow) {
            if(wwGap < wcGap) {
                if(wcGap - wwGap <= 2 && wwGap <= 3) { //when gap between ww and last is at most 3, and the difference between 2 min gap is 1, then we cross both
                    crossNumber(wwRow, wwNum);
                    crossNumber(wcRow, wcNum);
                    return;
                }
                exception1(wwRow, wwNum, wwGap);
                return;
            } else {
                exception1(wcRow, wcNum, wcGap);
                return;
            }
        }

        // if 2 min rows give same gap, then pick the one with more crossed numbers
        if (wwGap == wcGap) {
            if(wwGap == 1) { // if min gap = 1 for both then we cross both - consider also min gap = 2
                crossNumber(wwRow, wwNum);
                crossNumber(wcRow, wcNum);
                return;
            }
            if (sheet.getNumberCrossed(wwRow) > sheet.getNumberCrossed(wcRow)) {
                exception1(wwRow, wwNum, wwGap);
                return;
            } else {
                exception1(wcRow, wcNum, wcGap);
                return;
            }
        }

        // Different color + gap
        if (wwGap < wcGap) {
            if(wwGap < 3 && wcGap <= 3) { //when gap between ww and last is at most 3, and the difference between 2 min gap is 1, then we cross both
                crossNumber(wwRow, wwNum);
                crossNumber(wcRow, wcNum);
                return;
            }
            exception1(wwRow, wwNum, wwGap);
            return;
        } else {
            if(wcGap < 3 && wwGap <= 3) { //when gap between ww and last is at most 3, and the difference between 2 min gap is 1, then we cross both
                crossNumber(wwRow, wwNum);
                crossNumber(wcRow, wcNum);
                return;
            }
            exception1(wcRow, wcNum, wcGap);
            return;
        }
    }

    public void bestChoiceNonActive(int[] points) {
        // check if we can lock with the white combination
        int whiteComb = getWhiteComb(points);
        for(int i = 0; i < 4; i++) {
            if(sheet.canCross(i, 10, whiteComb)) { // checking if 12 (or 2) can be crossed
                sheet.cross(i, 10); //cross 12 (or 2)
                this.gui.crossButton(i,10);
                return;
            }
        }

        int[] input = minGapWhite(points); // index corresponds to color + minGap + number to cross

        if (input[0] == -1) { // When there is no valid gap
            return; //Skip the round
        }

        // Check if we can cross 12 or 2 to lock
        if (input[0] < 2 && input[2] == 12 && this.sheet.canCross(input[0], 10, 12)) {
            crossNumber(input[0], 12);
            return;
        }
        if (input[0] >= 2 && input[2] == 2 && this.sheet.canCross(input[0], 10, 2)) {
            crossNumber(input[0], 2);
            return;
        }

        // if the min gap is 1, then we cross the chosen number
        if (input[1] < 3) {
            crossNumber(input[0], input[2]);
            return;
        }

        // when the min. gap is > 1, and if there is an empty row --> only cross if gap <= 3
        if (sheet.getLastCrossed(input[0]) == 1 || sheet.getLastCrossed(input[0]) == 13) {
            if (input[1] <= 3) {
                crossNumber(input[0], input[2]);
            }
        } else {
            return;
        }
    }

    public void exception1(int color, int number, int minGap) {
        if (color == 0 || color == 1) {
            if(sheet.getLastCrossed(color) == 3 && sheet.getNumberCrossed(color) == 2 && minGap < 4) {
                crossNumber(color, number);
            } else {
                exception2(color, number, minGap);
            }
        }

        if (color == 2 || color == 3) {
            if(sheet.getLastCrossed(color) == 11 && sheet.getNumberCrossed(color) == 2 && minGap < 4) {
                crossNumber(color, number);
            } else {
                exception2(color, number, minGap);
            }
        }
    }

    // Check exception 2
    public void exception2(int color, int number, int minGap) {
        if (color == 0 || color == 1) {
            if (this.sheet.getPenaltyValue() < 3 && minGap > 3 && number > 7) {
                this.sheet.addPenalty();
                this.gui.crossPenalty(this.sheet.getPenaltyValue()-1);
            } else {
                crossNumber(color, number);
            }
        }
        if (color == 2 || color == 3) {
            if (this.sheet.getPenaltyValue() < 3 && minGap > 3 && number < 7) {
                this.sheet.addPenalty();
                this.gui.crossPenalty(this.sheet.getPenaltyValue()-1);
            } else {
                crossNumber(color, number);
            }
        }
    }
}
