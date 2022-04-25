package com.savetheplanet.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Driver {

    public static void main(String[] args) {

        try {
            File f = new File("./sounds/earth.wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            Clip dice = AudioSystem.getClip();
            dice.open(ais);
            dice.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

