package com.savetheplanet.Main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Create {

    public Create() {
    }

    /**
     * Reads in the data from the provided csv and returns a list
     * - Andrew
     */
    static List<ChanceCard> deck() {
        List<ChanceCard> listFromFile = new ArrayList<>();
        File file = new File("randomSquareAssignment.csv");
        try (FileReader fr = new FileReader(file); BufferedReader reader = new BufferedReader(fr)) {
            String line = reader.readLine();
            line = reader.readLine();
            while (line != null) {
                String[] parts = line.split(",");
                try {
                    if (parts.length == 1) {
                        RandomSquareAssignment random = RandomSquareAssignment.valueOf(parts[0].toUpperCase());
                        ChanceCard card = new ChanceCard(random);
                        listFromFile.add(card);
                    } else {
                        RandomSquareAssignment random = RandomSquareAssignment.valueOf(parts[0].toUpperCase());
                        int move = Integer.parseInt(parts[1]);
                        ChanceCard card = new ChanceCard(random, move);
                        listFromFile.add(card);
                    }
                } catch (IllegalArgumentException illegalArg) {
                    System.out.println(illegalArg.getMessage());
                    System.out.println("Skipping this line");
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found error");
        } catch (IOException e) {
            System.out.println("IO Exception");
        } catch (Exception e) {
            System.out.println("Exception occurred");
            System.out.println(listFromFile.size() + " lines read successfully");
            System.out.println(e.getMessage());
        }
        System.out.println(listFromFile.size() + " lines read successfully");
        return listFromFile;
    }

    /**
     * @return board reads fields and squares, List of Squares as Board.
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
                    board.add(new FundableSquare(name, field, fields.get(Integer.parseInt(arr[1])).split(",")));
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
        Scanner scan = new Scanner(System.in);

        try {
            int playersCount = playerCount(scan);

            for (int i = 1; i < playersCount + 1; i++) {
                Player p = validateName(scan, players, i);
                players.add(p);
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        return players;
    }

    /**
     * @param scan Scanner
     * @return Checks that the number of players is a valid number between 2 and 4 and keeps asking until it gets one.
     * Jaszon
     */

    private static int playerCount(Scanner scan) {

        Timer timer = timer();

        System.out.println("How many players? 2-4");
        String str = scan.nextLine();


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
                timer = timerReset(timer);
                str = scan.nextLine();

            } finally {
                timer.cancel();
            }
        }
    }

    /**
     * @param scan    Scanner
     * @param players List
     * @param i       player number
     * @return Player Checks name validity and keeps asking till they get it right.
     * Jaszon
     */
    private static Player validateName(Scanner scan, List<Player> players, int i) {

        Timer timer = timer();
        Player p;

        System.out.println("Please enter the name for player " + i);
        String str = scan.nextLine();

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
                timer = timerReset(timer);
                str = scan.nextLine();
            } finally {
                timer.cancel();
            }
        }
    }

    /**
     * https://www.youtube.com/watch?v=9jK-NcRmVcw
     *
     * @return Jaszon
     */
    private static Timer timer() {
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            boolean warned = false;

            public void run() {
                if (warned) {
                    System.out.println("You have been idle for 2 minutes. The Game will now exit.");
                    System.exit(0);
                }
                System.err.printf("\rYou have been idle for 1 minute.%nIf you are idle for another 1 minute the game will exit.%n");
                warned = true;
            } // 60000 for production
        }, 5000, 5000);
        return timer;
    }

    // Resets the timer.
    private static Timer timerReset(Timer timer) {
        timer.cancel();
        timer = timer();
        return timer;
    }
}
