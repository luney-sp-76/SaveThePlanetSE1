package com.savetheplanet.Main;

import java.util.ArrayList;
import java.util.List;

public class Players {

    List<Player> players;

    public Players() {

    }

    public void create() {
        List<Player> players = new ArrayList<>();
        try {
            int playersCount = playerCount();
            for (int i = 1; i < playersCount + 1; i++) {
                Player p = validateName(players, i);
                players.add(p);
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        this.players = players;
    }

    /**
     * @return Checks that the number of players is a valid number between 2 and 4 and keeps asking until it gets one.
     * Jaszon
     */

    private static int playerCount() {
        Game.timer60 = Idle.timerReset(Game.timer60, Game.T60);

        System.out.println("How many players? 2-4");
        String str = Game.MENU.nextLine();

        while (true) {
            try {
                // triggers a nfe if the entry does not parse into a number
                int input = Integer.parseInt(str);
                if (input < 2 || input > 4) {
                    throw new NumberFormatException("");
                } else {
                    return input;
                }
            } catch (NumberFormatException e) {
                System.err.println(e.getMessage().replaceFirst(".*", "Invalid number."));
                System.out.println("Please enter a number between 2 and 4.");
                Game.timer60 = Idle.timerReset(Game.timer60, Game.T60);
                str = Game.MENU.nextLine();
            } finally {
                Game.timer60.cancel();
            }
        }
    }

    /**
     * @param players List
     * @param i       player number
     * @return Player Checks name validity and keeps asking till they get it right.
     * Jaszon
     */
    private static Player validateName(List<Player> players, int i) {

        Game.timer60 = Idle.timerReset(Game.timer60, Game.T60);
        Player p;

        System.out.println("Please enter the name for player " + i);
        String str = Game.MENU.nextLine();

        while (true) {
            try {
                // uniqueness check, goes through all existing players and checks entry vs name
                for (Player pl : players) {
                    if (str.equals(pl.getName())) {
                        throw new IllegalArgumentException("Names must be unique.");
                    }
                }
                // The Player class has further validation checks which would trigger here.
                p = new Player(str);
                return p;

            } catch (IllegalArgumentException e) {
                System.err.println(e.getLocalizedMessage());
                System.out.println("Please enter the name for player " + i);
                Game.timer60 = Idle.timerReset(Game.timer60, Game.T60);
                str = Game.MENU.nextLine();
            } finally {
                Game.timer60.cancel();
            }
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getPlayer(int player) {
        return players.get(player);
    }


}

