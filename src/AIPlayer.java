import java.util.Arrays;

public class AIPlayer extends Player {

    public AIPlayer() {
        super("AI Player");
    }

    public void crossNumber(int color, int number) {
        if(color == 0 || color == 1) {
            sheet.cross(color, number - 2);
        } else {
            sheet.cross(color, 12 - number);
        }
    }

    // Get combination of white dice
    public int getWhiteComb(Dice[] dice) {
        return dice[0].getValue() + dice[1].getValue();
    }

    // Finding minimum gap of the white combination
    public int[] minGapWhite(Dice[] dice) {
        int combination = getWhiteComb(dice);
        int[] gap = new int[4];

        // Compute the gap for red and yellow, then green and blue (since they switch the order)
        for (int i = 0; i < 2; i++) {
            gap[i] = combination - sheet.getLastCrossed(i);
        }
        for (int i = 2; i < 4; i++) {
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

    /**
    public int[] whiteWhiteMin(Dice[] whiteDice) {
        int whiteWhite = dices.getWhiteWhite();
        int[] gapWhiteWhite = new int[4];

        for (int i = 0; i < 2; i++) {
            gapWhiteWhite[i] = whiteWhite - sheet.getFinalNumEachRow()[i];
        }
        for (int i = 2; i < 4; i++) {
            gapWhiteWhite[i] = sheet.getFinalNumEachRow()[i] - whiteWhite;
        }

        //Find minimum
        int minimumGap = 15;
        int indexMinimum = -1;
        for (int i = 0; i < 4; i++) {
            if (minimumGap > gapWhiteWhite[i] && gapWhiteWhite[i] > 0) {
                minimumGap = gapWhiteWhite[i];
                indexMinimum = i;
            }
        }

        //return gapWhiteWhite;
        return new int[] {indexMinimum, minimumGap, whiteWhite}; // index corresponds to color + minGap + number to cross
    }*/

    // Compute the color combination
    public int[] getColorComb(Dice[] dice) {
        int numColor = dice.length - 2; // Number of color dice
        int numCombs = 2 * numColor;
        int[] whiteColor = new int[numCombs];

        // Compute the possible combination
        for(int i = 2; i < dice.length; i++) {
            whiteColor[i] = dice[0].getValue() + dice[i].getValue();
            whiteColor[i + numColor] = dice[1].getValue() + dice[i].getValue();
        }

        return whiteColor;
    }

    public int[] minGapColor(Dice[] dice) { //assume that first 2 dice in the array are always white
        int numColor = dice.length - 2; // Number of color dice
        int numCombs = 2 * numColor;
        int[] whiteColor = getColorComb(dice);

        // Compute the gap
        int[] gap = new int[numCombs];
        for (int i = 0; i < 2; i++) {
            gap[i] = whiteColor[i] - sheet.getLastCrossed(i);
            gap[i + numColor] = whiteColor[i + numColor] - sheet.getLastCrossed(i);
        }
        for (int i = 2; i < 4; i++) {
            gap[i] = sheet.getLastCrossed(i) - whiteColor[i];
            gap[i + numColor] = sheet.getLastCrossed(i) - whiteColor[i + numColor];
        }

        //Find minimum
        int minGap = 15;
        int indexMin = -1;
        for (int i = 0; i < numCombs; i++) {
            if (minGap > gap[i]  && gap[i] > 0) {
                minGap = gap[i];
                indexMin = i;
            }
        }

        return new int[] {(int)indexMin%numColor, minGap, whiteColor[indexMin]}; // index corresponds to color + minGap + number to cross
    }
    /**
    public int[] whiteColorMin(Dices dices) {
        int[] whiteColor = dices.getWhiteColor();

        int[] gapWhiteColor = new int[8];
        for (int i = 0; i < 2; i++) {
            gapWhiteColor[i] = whiteColor[i] - sheet.getFinalNumEachRow()[i];
            gapWhiteColor[i+4] = whiteColor[i+4] - sheet.getFinalNumEachRow()[i];
        }
        for (int i = 2; i < 4; i++) {
            gapWhiteColor[i] = sheet.getFinalNumEachRow()[i] - whiteColor[i];
            gapWhiteColor[i+4] = sheet.getFinalNumEachRow()[i] - whiteColor[i+4];
        }

        //Find minimum
        int minimumGap = 15;
        int indexMinimum = -1;
        for (int i = 0; i < 8; i++) {
            if (minimumGap > gapWhiteColor[i]  && gapWhiteColor[i] > 0) {
                minimumGap = gapWhiteColor[i];
                indexMinimum = i;
            }
        }

        return new int[] {(int)indexMinimum%4, minimumGap, whiteColor[indexMinimum]}; // index corresponds to color + minGap + number to cross
    }*/

    public void bestChoiceActive(Dice[] dice) {
        // check if we can lock with the white combination
        int whiteComb = getWhiteComb(dice);
        for(int i = 0; i < 4; i++) {
            if(sheet.validRows[i] && sheet.canCross(i, 10, whiteComb)) { // checking if 12 (or 2) can be crossed
                sheet.cross(i, 10); //cross 12 (or 2)
                sheet.cross(i, 11); //lock
                return;
            }
        }
        // check if we can lock with the color combination
        int[] colorComb = getColorComb(dice);
        for(int combValue : colorComb) {
            for(int i = 0; i < 4; i++) {
                if(sheet.validRows[i] && sheet.canCross(i, 10, combValue)) { // checking if 12 (or 2) can be crossed
                    sheet.cross(i, 10); //cross 12 (or 2)
                    sheet.cross(i, 11); //lock
                    return;
                }
            }
        }

        // Here we have an argument: should this exception 2 be considered at this position or later?
        if(whiteComb == 9 || whiteComb == 5 || Arrays.binarySearch(colorComb, 9) == -1 || Arrays.binarySearch(colorComb, 5) == -1) {
            exception2();
            return;
        }

        int[] wwBestGap = minGapWhite(dice);
        int wwRow = wwBestGap[0];
        int wwGap = wwBestGap[1];
        int wwNum = wwBestGap[2];
        int[] wcBestGap = minGapColor(dice);
        int wcRow = wcBestGap[0];
        int wcGap = wcBestGap[1];
        int wcNum = wcBestGap[2];

        // Case: same color
        if (wwRow == wcRow) {
            if(wwGap < wcGap) {
                exception1(wwRow, wwNum, wwGap);
                return;
            } else {
                exception1(wcRow, wcNum, wcGap);
                return;
            }
        }

        // if 2 min rows give same gap, then pick the one with more crossed numbers
        if (wwGap == wcGap) {
            if (sheet.getNumberCrossed(wwRow) > sheet.getNumberCrossed(wcRow)) {
                crossNumber(wwRow, wwNum);
                return;
            } else {
                crossNumber(wcRow, wcNum);
                return;
            }
        }

        // Different color + gap
        if (wwGap < wcGap) {
            exception1(wwRow, wwNum, wwGap);
            return;
        } else {
            exception1(wcRow, wcNum, wcGap);
            return;
        }
    }

    public void bestChoiceNonActive(Dice[] dice) {
        // check if we can lock with the white combination
        int whiteComb = getWhiteComb(dice);
        for(int i = 0; i < 4; i++) {
            if(sheet.validRows[i] && sheet.canCross(i, 10, whiteComb)) { // checking if 12 (or 2) can be crossed
                sheet.cross(i, 10); //cross 12 (or 2)
                sheet.cross(i, 11); //lock
                return;
            }
        }

        int[] input = minGapWhite(dice); // index corresponds to color + minGap + number to cross

        if (input[0] == -1) { // When there is no valid gap
            return; //Skip the round
        }

        // if the min gap is 1, then we cross the chosen number
        if (input[1] == 1) {
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
            if(sheet.getLastCrossed(color) == 3 && sheet.getNumberCrossed(color) == 2 && minGap >= 4) {
                this.sheet.addPenalty();
            } else {
                crossNumber(color, number);
            }
        }

        if (color == 2 || color == 3) {
            if(sheet.getLastCrossed(color) == 11 && sheet.getNumberCrossed(color) == 2 && minGap >= 4) {
                this.sheet.addPenalty();
            } else {
                crossNumber(color, number);
            }
        }
    }

    // Check exception 2 - if 9 is valid then jump to 9
    public void exception2() {
        for(int i = 0; i < 2; i++) {
            if (sheet.getLastCrossed(i) == 7 && sheet.getNumberCrossed(i) == 3) {
                if (sheet.canCross(i, 7, 9)) {
                    sheet.cross(i, 7);
                    return;
                }
            }
        }

        for(int i = 2; i < 4; i++) {
            if (sheet.getLastCrossed(i) == 7 && sheet.getNumberCrossed(i) == 3) {
                if (sheet.canCross(i, 7, 5)) {
                    sheet.cross(i, 7);
                    return;
                }
            }
        }
    }
}