package com.savetheplanet.Main;


/**
 * Paul & Jaszon
 */

public class Dice implements IDie {


    /**
     * Paul & Jaszon
     *
     * @return int
     */
    @Override
    public int roll() {
        System.out.println("Dice Rolling...");
        Sounds.play("dice");
        int die1Result = randomNum();
        int die2Result = randomNum();

        diceGFX(die1Result, die2Result);

        return (int) Math.ceil((double) (die1Result + die2Result) / 2);
    }

    /**
     * Paul
     *
     * @return int
     */
    private static int randomNum() {
        int min = 1;
        int max = 6;
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    /**
     * Jaszon
     *
     * @param die1 d1
     * @param die2 d2
     *             Attempts to animate two dice rolling, with sound effects.
     *             Displays the result in graphical ascii form.
     */
    private static void diceGFX(int die1, int die2) {
        String edge = "-------";
        String[] d1 = {"|     |", "|  *  |", "|     |"};
        String[] d2 = {"|*    |", "|     |", "|    *|"};
        String[] d3 = {"|*    |", "|  *  |", "|    *|"};
        String[] d4 = {"|*   *|", "|     |", "|*   *|"};
        String[] d5 = {"|*   *|", "|  *  |", "|*   *|"};
        String[] d6 = {"|*   *|", "|*   *|", "|*   *|"};

        String[][] diceGFX = {d1, d2, d3, d4, d5, d6};

        // clearing the console sucks, doesn't work the same way from system to system, I don't love this, but it's the most stable I could find.
        // The "drag" effect ended up not looking too bad- a bit like dice moving up a table.
        String clear = String.format("%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n");

        // simulates a rolling animation. (kinda). 20x100 ms 'frames' - 2 seconds.
        for (int i = 0; i < 20; i++) {
            System.out.printf("%s %s%n", edge, edge);
            System.out.printf("%s %s%n", diceGFX[(randomNum()) - 1][((randomNum()) - 1) / 2], diceGFX[(randomNum()) - 1][((randomNum()) - 1) / 2]);
            System.out.printf("%s %s%n", diceGFX[(randomNum()) - 1][((randomNum()) - 1) / 2], diceGFX[(randomNum()) - 1][((randomNum()) - 1) / 2]);
            System.out.printf("%s %s%n", diceGFX[(randomNum()) - 1][((randomNum()) - 1) / 2], diceGFX[(randomNum()) - 1][((randomNum()) - 1) / 2]);
            System.out.printf("%s %s%n", edge, edge);

            Game.pause(100);
            System.out.println(clear);
        }

        // The result
        System.out.printf(" %-7s  %-7s%n", "Die 1", "Die 2");

        System.out.printf("%s  %s %n", edge, edge);
        for (int i = 0; i < 3; i++) {
            System.out.printf(diceGFX[die1 - 1][i] + "  " + diceGFX[die2 - 1][i] + "%n");
        }
        System.out.printf("%s  %s %n", edge, edge);
    }
} //class

