package com.savetheplanet.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

final class CreatePlayers {

    public CreatePlayers() {}

    static List<Player> createPlayers() {
        List<Player> players = new ArrayList<>();
        Scanner scan = new Scanner(System.in);

        try {
            System.out.println("How many players? 2-4");
            int playersCount = playerCount(scan, scan.nextLine());

            for (int i = 1; i < playersCount + 1; i++) {
                System.out.println("Please enter the name for player " + i);
                Player p = validateName(scan, scan.nextLine(), players, i);
                players.add(p);
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
           return players;

    }

    public static Player validateName(Scanner scan, String str, List<Player> players, int i) {

        Player p;

        while (true) {
            try {
                for (Player pl : players) {
                    if (str.equals(pl.getName())) {
                        throw new IllegalArgumentException("Names must be unique.");
                    }
                }

                p = new Player(str);

                return p;

            } catch (IllegalArgumentException e) {
                System.out.println(e.getLocalizedMessage());
                System.out.println("Please enter the name for player " + i);
                str = scan.nextLine();
            }
        }
    }


    private static int playerCount(Scanner scan, String str) {
        while (true) {
            try {
                int input = Integer.parseInt(str);
                if (input < 2 || input > 4) {
                    throw new NumberFormatException();
                } else {
                    return input;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entry must be a number between 2 and 4.");
                str = scan.nextLine();
            }
        }
    }


}
