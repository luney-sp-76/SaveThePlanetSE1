package com.savetheplanet.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Jaszon.
 * Builds the PLAYERS object containing all the players in the game.
 * It also handles player creations validation.
 */
public class Players {

    List<Player> players;


    public Players() {
    }


    public void create() {
        List<Player> players = new ArrayList<>();

        // gets the number of players from the Scanner, with validation.
        int playersCount = playerCount();

        // runs the name validation and insertion to the required number of times. 
        for (int i = 1; i < playersCount + 1; i++) {
            Player p = validateName(players, i);

            // adds valid players to the players list.
            players.add(p);
        }

        this.players = players;
    }

    /**
     * @return Checks that the number of players is a valid number between 2 and 4 and keeps asking until it gets one.
     * Jaszon
     */
    private static int playerCount() {
        Game.timer60 = Idle.timerReset(Game.timer60);
        System.out.println("How many players? 2-4");
        String str = Game.MENU.nextLine();

        while (true) {
            try {
                // triggers a nfe if the entry does not parse into a number
                int input = Integer.parseInt(str);
                // or if it is out of bounds
                if (input < 2 || input > 4) {
                    throw new NumberFormatException("");
                } else {
                    return input;
                }
            } catch (NumberFormatException e) {
                // this was a royal pain.
                System.err.println(e.getMessage().replaceFirst(".*", "Invalid number."));
                System.out.println("Please enter a number between 2 and 4.");
                Game.timer60 = Idle.timerReset(Game.timer60);
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

        Game.timer60 = Idle.timerReset(Game.timer60);
        Player p;

        System.out.println("Please enter the name for player " + i);
        String str = Game.MENU.nextLine();

        while (true) {
            try {
                // uniqueness check, goes through all existing players and checks entry vs name ignoring case.
                for (Player pl : players) {
                    if (str.equalsIgnoreCase(pl.getName())) {
                        throw new IllegalArgumentException("Names must be unique.");
                    }
                }
                // The Player class has further validation checks which will trigger here if needed.
                p = new Player(str);
                return p;

            } catch (IllegalArgumentException e) {
                System.err.println(e.getLocalizedMessage());
                System.out.println("Please enter the name for player " + i);
                Game.timer60 = Idle.timerReset(Game.timer60);
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

