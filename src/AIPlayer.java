import java.util.stream.IntStream;

/**
 * Class defines the behavior (the choice) of AI player
 *
 * @author Amber Cuijpers, Danielle Lam, Khue Nguyen, Yu-Shan Cho, Yuntong Wu
 */
public class AIPlayer extends Player {
    private ScoreSheetAIPlayerGUI gui;

    /**
     * Constructor for the AIPlayer, which initializes the name and the score sheet panel.
     */
    public AIPlayer() {
        super("AI Player");
        this.gui = new ScoreSheetAIPlayerGUI();
    }

    /**
     * The method returns the scoreSheet GUI of this AI Player
     */
    public ScoreSheetAIPlayerGUI getGUI() {
        return this.gui;
    }

    /**
     * The method is used to cross a number in the score sheet.
     *
     * @param color  the row of the corresponding block. (0:red, 1:yellow, 2:green, 3:blue)
     * @param number the number to be crossed.
     */
    public void crossNumber(int color, int number) {
        if (color == 0 || color == 1) {
            sheet.cross(color, number - 2);
            this.gui.crossButton(color, number - 2);
        } else {
            sheet.cross(color, 12 - number);
            this.gui.crossButton(color, 12 - number);
        }
    }

    /**
     * The method returns the .... of each colored row that can be found with the two white dice.
     *
     * @param points an array that stores the values of six dice in a round. (If a die is removed, the related point becomes 0)
     */
    public int[] minGapWhite(int[] points) {
        int combination = getWhiteComb(points);
        int[] gap = new int[4];

        // Compute the gap for red and yellow rows, then for green and blue rows. (Since they switch the order)
        for (int i = 0; i < 2; i++) {
            // If a row is not valid then the gap  for that row is set to -1.
            if (!sheet.getValidRow(i)) {
                gap[i] = -1;
                continue;
            }
            gap[i] = combination - sheet.getLastCrossed(i);
        }
        for (int i = 2; i < 4; i++) {
            // If a row is not valid then the gap  for that row is set to -1.
            if (!sheet.getValidRow(i)) {
                gap[i] = -1;
                continue;
            }
            gap[i] = sheet.getLastCrossed(i) - combination;
        }

        //After computing the four gaps, find the minimum one.
        int minGap = 15;
        int indexMin = -1;
        for (int i = 0; i < 4; i++) {
            if (minGap > gap[i] && gap[i] > 0) {
                minGap = gap[i];
                indexMin = i;
            }
        }

        //Return a list that contains:
        // 1.The index for denoting the color (0 to 4)
        // 2.The corresponding gap counts
        // 3. THIS IS THE SAME AS GETWHITECOMB///////////////////
        return new int[]{indexMin, minGap, combination};
    }


    /**
     * The method returns the .... of each colored row that can be found with a white die and a colored die.
     *
     * @param points an array that stores the values of six dice in a round. (If a die is removed, the related point becomes 0)
     */
    public int[] minGapColor(int[] points) {
        int[] whiteColor = getColorComb(points);

        // Compute the gap for red and yellow rows, then for green and blue rows. (Since they switch the order)
        // Two gap values are computed (as there are two white dice), and the smaller value is stored in the gap[] array.
        int[] gap = new int[4];
        for (int i = 0; i < 2; i++) {
            if (!sheet.getValidRow(i)) {
                gap[i] = -1;
                continue;
            }
            gap[i] = whiteColor[i] - sheet.getLastCrossed(i);
            // Compute and compare the gap value of the same colored die and another white die
            int other = whiteColor[i + 4] - sheet.getLastCrossed(i);
            if ((gap[i] > other && other > 0) || (gap[i] < 0 && other > 0)) {
                gap[i] = other;
            }
        }
        for (int i = 2; i < 4; i++) {
            if (!sheet.getValidRow(i)) {
                gap[i] = -1;
                continue;
            }
            // Compute and compare the gap value of the same colored die and another white die
            gap[i] = sheet.getLastCrossed(i) - whiteColor[i];
            int other = sheet.getLastCrossed(i) - whiteColor[i + 4];
            if ((gap[i] > other && other > 0) || (gap[i] < 0 && other > 0)) {
                gap[i] = other;
            }
        }

        //After computing the four gaps, find the minimum one.
        int minGap = 15;
        int indexMin = -1;
        for (int i = 0; i < 2; i++) {
            if (minGap > gap[i] && gap[i] > 0 && this.sheet.canCross(i, gap[i] + sheet.getLastCrossed(i) - 2)) {
                minGap = gap[i];
                indexMin = i;
            }
        }
        for (int i = 2; i < 4; i++) {
            if (minGap > gap[i] && gap[i] > 0 && this.sheet.canCross(i, 12 + gap[i] - sheet.getLastCrossed(i))) {
                minGap = gap[i];
                indexMin = i;
            }
        }

        if (indexMin == -1) {
            return new int[]{indexMin, minGap, 0};
        }

        //Return a list that contains:
        // 1.The index for denoting the color (0 to 4)
        // 2.The corresponding gap counts
        // 3. ???????
        if (indexMin == 0 || indexMin == 1) {
            return new int[]{indexMin, minGap, minGap + sheet.getLastCrossed(indexMin)};
        } else {
            return new int[]{indexMin, minGap, sheet.getLastCrossed(indexMin) - minGap};
        }
    }

    /**
     * The method makes the AI Player perform (in case of the Active round)
     *
     * @param points an array that stores the values of six dice in a round. (If a die is removed, the related point becomes 0)
     */
    public void bestChoiceActive(int[] points) {
        //Priority Case: Locking a row whenever possible
        int whiteComb = getWhiteComb(points);
        int[] colorComb = getColorComb(points);
        for (int i = 0; i < 4; i++) {
            if (sheet.canCross(i, 10, whiteComb)) {
                sheet.cross(i, 10); //(Note: The cross method embeds a behavior to cross the Lock column)
                this.gui.crossButton(i, 10);
                this.gui.crossButton(i, 11);
                return;
            }
        }
        for (int combValue : colorComb) {
            for (int i = 0; i < 4; i++) {
                if (sheet.canCross(i, 10, combValue)) {
                    sheet.cross(i, 10); //(Note: The cross method embeds a behavior to cross the Lock column)
                    this.gui.crossButton(i, 10);
                    this.gui.crossButton(i, 11);
                    return;
                }
            }
        }
        //Second Priority Case: If 7 is the third crossed number in that row,
        //then 9 is crossed if possible for red and yellow row, and 5 for green or blue row
        if (whiteComb == 9 || whiteComb == 5 || IntStream.of(colorComb).anyMatch(x -> x == 9) || IntStream.of(colorComb).anyMatch(x -> x == 5)) {
            for (int i = 0; i < 2; i++) {
                if (sheet.getLastCrossed(i) == 7 && sheet.getNumberCrossed(i) == 3) {
                    if (sheet.canCross(i, 7, 9)) {
                        sheet.cross(i, 7);
                        this.gui.crossButton(i, 7);
                        return;
                    }
                }
            }
            for (int i = 2; i < 4; i++) {
                if (sheet.getLastCrossed(i) == 7 && sheet.getNumberCrossed(i) == 3) {
                    if (sheet.canCross(i, 7, 5)) {
                        sheet.cross(i, 7);
                        this.gui.crossButton(i, 7);
                        return;
                    }
                }
            }
        }

        //Consider crossing the number based on the two minimum gap counts from the
        //best (white, white) and (white, color) combinations
        int[] wwBestGap = minGapWhite(points);
        int wwRow = wwBestGap[0];  //the index of colored row
        int wwGap = wwBestGap[1];  //the gap of the best (white, white)
        int wwNum = wwBestGap[2];  //the number of (white, white)
        int[] wcBestGap = minGapColor(points);
        //Similar to the code 207-210, but for (white, color) combination
        int wcRow = wcBestGap[0];
        int wcGap = wcBestGap[1];
        int wcNum = wcBestGap[2];

        // If there is no valid gap then we must be penalized
        if (wwRow == -1 && wcRow == -1) {
            this.sheet.addPenalty();
            this.gui.crossPenalty(this.sheet.getPenaltyValue() - 1);
            return;
        }

        /*
         * Case 1: The 2 combinations share the same color
         * Choose the row with smaller gap to continue with
         * checking {@Link #exception1(int, int, int)} for final decision
         */
        if (wwRow == wcRow) {
            if (wwGap < wcGap) {
                // Exception: Cross both combinations when gap of (white, white) is at most 3
                // and also the difference between 2 min gaps is at most 2
                if (wcGap - wwGap <= 2 && wwGap <= 3) {
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

        /*
         * Case 2: The 2 combinations share the same gap
         * Choose the row with more crossed numbers to continue with
         * checking {@Link #exception1(int, int, int)} for final decision
         */
        if (wwGap == wcGap) {
            // Exception: Cross both if min gap = 1
            if (wwGap == 1) {
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

        /*
         * Case 3: The 2 combinations are different in gap and color
         * Choose the row with smaller gap to continue with
         * checking {@Link #exception1(int, int, int)} for final decision
         */
        if (wwGap < wcGap) {
            // Exception: Cross both when the smaller gap is < 3,
            // and the larger one is <= 3
            if (wwGap < 3 && wcGap <= 3) {
                crossNumber(wwRow, wwNum);
                crossNumber(wcRow, wcNum);
                return;
            }
            exception1(wwRow, wwNum, wwGap);
            return;
        } else {
            if (wcGap < 3 && wwGap <= 3) {
                crossNumber(wwRow, wwNum);
                crossNumber(wcRow, wcNum);
                return;
            }
            exception1(wcRow, wcNum, wcGap);
            return;
        }
    }

    /*
     * The method makes the AI Player perform (in case of the NonActive round)
     * Only use the white combination
     *
     * @param points an array that stores the values of six dice in a round.
     *               (If a die is removed, the related point becomes 0)
     */
    public void bestChoiceNonActive(int[] points) {
        //Priority Case: Locking a row whenever possible
        int whiteComb = getWhiteComb(points);
        for (int i = 0; i < 4; i++) {
            if (sheet.canCross(i, 10, whiteComb)) {
                sheet.cross(i, 10);
                this.gui.crossButton(i, 10);
                return;
            }
        }

        int[] input = minGapWhite(points); // list of color (ie. row), corresponding min. gap, and the number to cross

        if (input[0] == -1) { // Do nothing when there is no valid gap
            return;
        }

        //Check if we can cross 12 or 2 to lock///////////////////////////////////////////////////// note
        if (input[0] < 2 && input[2] == 12 && this.sheet.canCross(input[0], 10, 12)) {
            crossNumber(input[0], 12);
            return;
        }
        if (input[0] >= 2 && input[2] == 2 && this.sheet.canCross(input[0], 10, 2)) {
            crossNumber(input[0], 2);
            return;
        }

        // If the min gap is < 3, then we cross the chosen number
        if (input[1] < 3) {
            crossNumber(input[0], input[2]);
            return;
        }

        // If there is an empty row, only cross if gap <= 3
        if (sheet.getLastCrossed(input[0]) == 1 || sheet.getLastCrossed(input[0]) == 13) {
            if (input[1] <= 3) {
                crossNumber(input[0], input[2]);
            }
        } else {
            return;
        }
    }

    /**
     * The method is for checking the Exception 1: When the list of crossed numbers or red or yellow
     * only has 2 and 3 (12 and 11 for green and blue), then we only cross the best number when min. gap < 4
     * Otherwise, we check {@Link #exception2(int, int, int)}
     * Support method for {@Link #bestChoiceActive(int[])}
     *
     * @param color  the index referring to the colored row
     * @param number the possible number to be crossed
     * @param minGap the minimum gap associated with the number
     */
    public void exception1(int color, int number, int minGap) {
        if (color == 0 || color == 1) {
            if (sheet.getLastCrossed(color) == 3 && sheet.getNumberCrossed(color) == 2 && minGap < 4) {
                crossNumber(color, number);
            } else {
                exception2(color, number, minGap);
            }
        }

        if (color == 2 || color == 3) {
            if (sheet.getLastCrossed(color) == 11 && sheet.getNumberCrossed(color) == 2 && minGap < 4) {
                crossNumber(color, number);
            } else {
                exception2(color, number, minGap);
            }
        }
    }

    /**
     * The method is for checking the Exception 2: Only actively cross penalty when there is < 3 penalties,
     * min.gap > 3, and the crossed number's order is after number 7
     * Support method for {@Link #exception2(int, int, int)} and {@Link #bestChoiceActive(int[])}
     *
     * @param color  the index referring to the colored row
     * @param number the possible number to be crossed
     * @param minGap the minimum gap associated with the number
     */
    public void exception2(int color, int number, int minGap) {
        if (color == 0 || color == 1) {
            if (this.sheet.getPenaltyValue() < 3 && minGap > 3 && number > 7) {
                this.sheet.addPenalty();
                this.gui.crossPenalty(this.sheet.getPenaltyValue() - 1);
            } else {
                crossNumber(color, number);
            }
        }

        if (color == 2 || color == 3) {
            if (this.sheet.getPenaltyValue() < 3 && minGap > 3 && number < 7) {
                this.sheet.addPenalty();
                this.gui.crossPenalty(this.sheet.getPenaltyValue() - 1);
            } else {
                crossNumber(color, number);
            }
        }
    }
}
