package com.savetheplanet.Main;

// testing branch

public class Driver {


    public static void main(String[] args) {
        try {
            Player p1 = new Player("");

            System.out.println(p1.toString());

            Dice diceRoll = new Dice();
            int move = diceRoll.roll();

            //System.out.println(move);


        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
