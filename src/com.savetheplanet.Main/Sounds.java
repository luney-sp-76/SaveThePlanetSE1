package com.savetheplanet.Main;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Jaszon
 *
 * Parameter-based player for all the wavs your heart could desire.
 */
public class Sounds {

    static File f;
    static AudioInputStream ais;
    static Clip clip;

    public Sounds() {
    }

    /**
     * @param sound the filename of the clip wanted, without the .wav
     */
    static void play(String sound) {
        try {
            f = new File("./sounds/" + sound + ".wav");
            ais = AudioSystem.getAudioInputStream(f);
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
            ais.close();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
