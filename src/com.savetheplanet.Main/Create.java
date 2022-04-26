package com.savetheplanet.Main;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Create {

    private Create() {
        throw new UnsupportedOperationException();
    }
    /**
     * @return board - Returns List of Squares as Board.
     * Jaszon
     */
    static List<Square> board() {

        List<Square> board = new ArrayList<>();
        // try-with-resources to make sure the streams get closed.
        try (Stream<String> fieldsIn = Files.lines(Paths.get("fields.txt"));
             Stream<String> squares = Files.lines(Paths.get("squares.txt"))) {

            List<String> fields = fieldsIn.collect(Collectors.toList());

            squares.forEach(line -> {
                String[] arr = line.split(",");
                String name = arr[0];
                int field = Integer.parseInt(arr[1]);

                if (field >= 3) {
                    board.add(new FundableSquare(name, field, fields.get(field).split(",")));
                } else {
                    board.add(new Square(name, field));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return board;
    }

    /**
     * @return players
     * Jaszon
     */
    static List<Player> players() {

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

        return players;
    }

    /**
     * @return Checks that the number of players is a valid number between 2 and 4 and keeps asking until it gets one.
     * Jaszon
     */

    private static int playerCount() {

        Timer timer = timer(60000);
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
                timer = timerReset(timer, 60000);
                str = Game.MENU.nextLine();
            } finally {
                timer.cancel();
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

        Timer timer = timer(60000);
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
                timer = timerReset(timer, 60000);
                str = Game.MENU.nextLine();
            } finally {
                timer.cancel();
            }
        }
    }
    /**
     * 60-second timer with 2 phases.
     *
     * @return Jaszon
     */
    static Timer timer(int t) {
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            boolean warned = false;

            public void run() {

                if (t == 60000) {
                    if (warned) {
                        System.out.println("You have been idle for 2 minutes. The Game will now exit.");
                        System.exit(0);
                    }
                    System.err.printf("\rYou have been idle for 1 minute.%nIf you are idle for another 1 minute the game will exit.%n");
                    warned = true;
                }
                if (t == 15000) {
                    if (warned) {
                        System.out.println("You have been idle for 30s - something is happening.");
                        // action call.
                    }
                    System.err.printf("\rYou have been idle for 15 seconds.%nIf you are idle for another 15 seconds, something will happen%n");
                    warned = true;
                }
            }
        }, t, t);
        return timer;
    }
    // Resets the timer.
    static Timer timerReset(Timer timer, int t) {
        timer.cancel();
        timer = timer(t);
        return timer;
    }
}// class
