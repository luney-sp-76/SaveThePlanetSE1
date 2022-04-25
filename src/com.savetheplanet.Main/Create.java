package com.savetheplanet.Main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Timer;
import java.util.TimerTask;
final class Create {

    private Create() {
        throw new UnsupportedOperationException();
    }

    /**
     * Reads in the data from the provided csv and returns a list
     * - Andrew
     */
    static List<ChanceCard> deck() {
        List<ChanceCard> listFromFile = new ArrayList<>();
        File file = new File("randomSquareAssignment.csv");
        try (FileReader fr = new FileReader(file); BufferedReader reader = new BufferedReader(fr)) {
            reader.readLine();
            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                String[] parts = line.split(",");
                try {
                    RandomSquareAssignment random = RandomSquareAssignment.valueOf(parts[0].toUpperCase());
                    if (parts.length == 1) {
                        ChanceCard card = new ChanceCard(random);
                        listFromFile.add(card);
                    } else {
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
//    static List<Player> players() {
    static List<Player> players(Scanner scan) {

        List<Player> players = new ArrayList<>();
//        Scanner scan = new Scanner(System.in);

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


    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> load(Scanner menu) {

        List<File> saves = loadFiles();

        AtomicInteger saveID = new AtomicInteger(0);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");

        System.out.println("Which file would you like to load?");
        saves.forEach(save -> System.out.println(saveID.incrementAndGet() + " " + save.getName() + " " + dateFormat.format(save.lastModified())));

        String gameToLoad = pickGame(menu, saves);

        if (gameToLoad != null)
            try (FileInputStream fis = new FileInputStream("./saves/" + gameToLoad);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                return (HashMap<String, Object>) ois.readObject();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();

            }
        return null;
    }

    /**
     * @param menu  scanner
     * @param saves List of Files
     * @return name of chosen File
     */
    private static String pickGame(Scanner menu, List<File> saves) {
        while (true) {

            switch (menu.nextLine()) {
                case "0":
                    return null;
                case "1":
                    return saves.get(0).getName();
                case "2":
                    return saves.get(1).getName();
                case "3":
                    return saves.get(2).getName();
                default:
                    System.out.println("Invalid input, 1-3 only to load a save, or 0 to exit to menu");
            }
        }
    }

    /**
     * @return saves - List of the most recent 3 files from the save files dir, in chronological order.
     */
    public static List<File> loadFiles() {
        List<File> saves = new ArrayList<>();
        try (Stream<Path> l = Files.list(Paths.get("./saves/"))) {
            saves = l.map(Path::toFile)
                    .filter(f -> f.getName().endsWith(".sav"))
                    .sorted(Comparator.comparing(File::lastModified))
                    .collect(Collectors.toList());

            if (saves.size() > 3)
                saves = saves.subList(saves.size() - 3, saves.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return saves;
    }

    public static void save(Scanner menu, List<Square> board, List<Player> players) {

        HashMap<String, Object> saveGame = new HashMap<>();


        saveGame.put("Board", board);
        saveGame.put("Players", players);

        List<File> saves = loadFiles();

        String saveName = validateSaveName(menu, saves);
        boolean write = true;

        if (saves.size() == 3) {
            write = memoryCardFull(menu, saves);
        }
        System.out.println(saves.size());

        if (write)
            try (FileOutputStream fos = new FileOutputStream("./saves/" + saveName);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(saveGame);


            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private static boolean memoryCardFull(Scanner menu, List<File> saves) {


        System.out.println("You already have 3 saved games, by continuing the oldest game " + saves.get(0).getName() + " will be removed. Do you want to continue y/n?");
        if (menu.nextLine().toLowerCase().contains("y")) {
            return saves.get(0).delete();

        }
        return false;
    }

    private static String validateSaveName(Scanner menu, List<File> saves) {

        System.out.println("Enter name for the Save Game");
        String str = menu.nextLine() + ".sav";

        while (true) {
            try {
                System.out.println(str);
                if (str.matches("^.*[^a-zA-Z\\d.].*$"))
                    throw new IllegalArgumentException("Name format error. Name contains illegal characters. Alphanumeric only, no spaces.");
                if (str.length() < 6 || str.length() > 34)
                    throw new IllegalArgumentException("Name format error. Length must be between 2 and 30 characters.");
                for (File f : saves) {
                    if (str.equals(f.getName())) {
                        throw new IllegalArgumentException("Names must be unique.");
                    }
                }
                return str;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getLocalizedMessage());
                System.out.println("Please enter the name for the Save Game");
                str = menu.nextLine() + ".sav";
            }
        }
    }

    /**
     * 60-second timer with 2 phases.
     *
     * @return Jaszon
     */
    static Timer timer() {
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

            }
        }, 60000, 60000);
        return timer;
    }

    // Resets the timer.
    static Timer timerReset(Timer timer) {
        timer.cancel();
        timer = timer();
        return timer;
    }






}// class
