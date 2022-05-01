package com.savetheplanet.Main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Jaszon & Paul
 *
 * The driver class that handles all the not-in-game requirements- the save/load.
 * As well as the title screen, and the intro audio.
 */
public class SaveThePlanet {

    public static void main(String[] args) {

        Sounds.play("earth");

        System.out.printf("%n .oooooo..o                                      ooooooooooooo oooo%n");
        System.out.println("d8P'    `Y8                                      8'   888   `8 `888");
        System.out.println("Y88bo.       .oooo.   oooo    ooo  .ooooo.            888       888 .oo.    .ooooo.");
        System.out.println(" `\"Y8888o.  `P  )88b   `88.  .8'  d88' `88b           888       888P\"Y88b  d88' `88b");
        System.out.println("     `\"Y88b  .oP\"888    `88..8'   888ooo888           888       888   888  888ooo888");
        System.out.println("oo     .d8P d8(  888     `888'    888    .o           888       888   888  888    .o");
        System.out.println("8\"\"88888P'  `Y888\"\"8o     `8'     `Y8bod8P'          o888o     o888o o888o `Y8bod8P'");
        System.out.println();
        System.out.println("  .oooooo.  ooooooooo.   oooo                                      .     .oooooo.");
        System.out.println(" d'     `b  `888   `Y88. `888                                    .o8    d'     `b");
        System.out.println("d' .d\"bd  8  888   .d88'  888   .oooo.   ooo. .oo.    .ooooo.  .o888oo d' .d\"bd  8");
        System.out.println("8  8. 8  .d  888ooo88P'   888  `P  )88b  `888P\"Y88b  d88' `88b   888   8  8. 8  .d");
        System.out.println("Y.  YoP\"b'   888          888   .oP\"888   888   888  888ooo888   888   Y.  YoP\"b'");
        System.out.println(" 8.      .8  888          888  d8(  888   888   888  888    .o   888 .  8.      .8");
        System.out.println("  YooooooP  o888o        o888o `Y888\"\"8o o888o o888o `Y8bod8P'   \"888\"   YooooooP ");
        System.out.printf("%n%n%n");

        welcome();
    }

    /**
     * Paul & Jaszon
     */
    @SuppressWarnings("InfiniteLoopStatement")
    static void welcome() {
        Game.timer60 = Idle.timerReset(Game.timer60);

        System.out.println("Welcome To Save The Planet");
        System.out.println("Would you like to Play? y/n");

        while (true) {
            switch (Game.MENU.nextLine().toLowerCase()) {
                case "y":
                    initiateGameOptions();
                    break;
                case "n":
                    quitOutsideOfGamePlay();
                    break;
                default:
                    System.out.println("Would you like to Play? y/n");
                    Game.timer60 = Idle.timerReset(Game.timer60);
            }
        }
    }

    /**
     * Paul
     */
    private static void quitOutsideOfGamePlay() {
        System.out.println("See You Next Time!");
        System.exit(1);
    }

    /**
     * Paul & Jaszon
     */
    private static void initiateGameOptions() {
        Game.timer60 = Idle.timerReset(Game.timer60);

        System.out.println("Game Menu");
        System.out.println("----------");
        System.out.printf("1) new game%n2) restart game%n3) quit%n");

        switch (Game.MENU.nextLine()) {
            case "1":
                System.out.println("Ok Lets Go!");
                Game.timer60.cancel();
                new Game();
                break;
            case "2":
                new Game(load());
                break;
            case "3":
                quitOutsideOfGamePlay();
                break;
            default:
                System.err.printf("Invalid input.%n%n ");
                Game.timer60 = Idle.timerReset(Game.timer60);
                initiateGameOptions();
        }
    }

    /**
     * Jaszon
     *
     * @return A HashMap of the saved game data, or null.
     *
     * Asks the player if what file they want to load, then pulls the data in. The nulls are handled.
     */

    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> load() {

        System.out.println("Do you want to load a Saved Game? y/n");
        if (!Game.MENU.nextLine().toLowerCase().contains("y"))
            return null;

        List<File> saves = loadFiles();
        AtomicInteger saveID = new AtomicInteger(0); // why yes, I am willing to import AtomicInteger just so I can use the prettier .forEach Lambda.
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");

        System.out.println("Which file would you like to load?");
        saves.forEach(save -> System.out.println(saveID.incrementAndGet() + " " + save.getName() + " " + dateFormat.format(save.lastModified())));

        String gameToLoad = pickGame(saves);

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
     * Jaszon
     *
     * @param saves List of Files
     * @return name of chosen File
     * It's a menu.
     */
    private static String pickGame(List<File> saves) {

        while (true) {
            switch (Game.MENU.nextLine()) {
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
     * Jaszon
     *
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

    /**
     * Jaszon
     *
     * @param board   list of squares as board
     * @param players list of players
     * @param deck    list of Chance cards
     *
     *                Makes a new HashMap and puts the 3 lists in as values, with their name as the keys.
     *                This could also be done with a List of Lists, but I felt like getting some more practice with maps.
     *
     *                Calls loadFiles to get a list of current saves, validateSaveName to check that the name is valid,
     *                memoryCardFull to see if there are already 3 save files and if the go ahead is given writes a new save to file.
     */
    public static void save(List<Square> board, List<Player> players, Deck deck) {

        HashMap<String, Object> saveGame = new HashMap<>();

        saveGame.put("Board", board);
        saveGame.put("Players", players);
        saveGame.put("Deck", deck);

        List<File> saves = loadFiles();

        String saveName = validateSaveName(saves);
        boolean write = true;

        if (saves.size() == 3) {
            write = memoryCardFull(saves);
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

    /**
     * Jaszon
     *
     * @param saves list of save files
     * @return boolean if there are 3 saves already or not.
     * Checks if they want to overwrite a save.
     */
    private static boolean memoryCardFull(List<File> saves) {
        System.out.println("You already have 3 saved games, by continuing the oldest game " + saves.get(0).getName() + " will be removed. Do you want to continue y/n?");
        if (Game.MENU.nextLine().toLowerCase().contains("y")) {
            return saves.get(0).delete();
        }
        return false;
    }

    /**
     * @param saves list of save files
     * @return a String that passes the name checks.
     *
     * O B A Y (same rules as player names).
     */
    private static String validateSaveName(List<File> saves) {
        System.out.println("Enter name for the Save Game");
        String str = Game.MENU.nextLine() + ".sav";

        while (true) {
            try {
                System.out.println(str);
                if (str.matches("^.*[^a-zA-Z\\d.].*$"))
                    throw new IllegalArgumentException("Name format error. Name contains illegal characters. Alphanumeric only, no spaces.");
                if (str.length() < 6 || str.length() > 34) // .sav
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
                str = Game.MENU.nextLine() + ".sav";
            }
        }
    }
}

