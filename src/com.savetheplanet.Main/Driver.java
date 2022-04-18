package com.savetheplanet.Main;

public class Driver {

    public static void main(String[] args) {

        try {
            Player p1 = new Player("Dog");

            System.out.println(p1.toString());

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
