package com.savetheplanet.Main;

public class Driver {

    public static void main(String[] args) {

        System.out.println("1: Real, 2: Dummy, else: exit.");

        while (true) {
            switch (Game.MENU.nextLine()) {
                case "1":
                    new Game();
                    break;
                case "2":
                    new DummyGame();
                    break;
                default:
                    System.out.println("¬_¬");
                    System.exit(1);

            }
        }
    }
}
