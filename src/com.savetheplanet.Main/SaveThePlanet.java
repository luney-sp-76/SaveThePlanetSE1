package com.savetheplanet.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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

    @SuppressWarnings("InfiniteLoopStatement")
    static void welcome() {
        System.out.println("Welcome To Save The Planet");
        System.out.println("Would you like to Play? y/n");

        while (true) {
            switch (Game.MENU.nextLine()) {
                case "y":
                    initiateGameOptions();
                    break;
                case "n":
                    quitOutsideOfGamePlay();
                    break;
                default:
                    System.err.println("Invalid input.");
                    System.out.println("Would you like to Play? y/n");
                    Game.timer60 = Create.timerReset(Game.timer60, Game.T60);
            }
        }
    }

    private static void quitOutsideOfGamePlay() {
        System.out.println("See You Next Time!");
        System.exit(1);
    }

    private static void initiateGameOptions() {
        Game.timer60 = Create.timerReset(Game.timer60, Game.T60);

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
                Game.timer60 = Create.timerReset(Game.timer60, Game.T60);
                initiateGameOptions();
        }
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> load() {
        List<File> saves = loadFiles();

        AtomicInteger saveID = new AtomicInteger(0);
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
     * @param saves List of Files
     * @return name of chosen File
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

    public static void save(List<Square> board, List<Player> players) {

        HashMap<String, Object> saveGame = new HashMap<>();

        saveGame.put("Board", board);
        saveGame.put("Players", players);

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

    private static boolean memoryCardFull(List<File> saves) {


        System.out.println("You already have 3 saved games, by continuing the oldest game " + saves.get(0).getName() + " will be removed. Do you want to continue y/n?");
        if (Game.MENU.nextLine().toLowerCase().contains("y")) {
            return saves.get(0).delete();

        }
        return false;
    }

    private static String validateSaveName(List<File> saves) {

        System.out.println("Enter name for the Save Game");
        String str = Game.MENU.nextLine() + ".sav";

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
                str = Game.MENU.nextLine() + ".sav";
            }
        }
    }
}

