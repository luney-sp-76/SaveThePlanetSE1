package com.savetheplanet.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sounds {

    static File f;
    static AudioInputStream ais;
    static Clip clip;

    public Sounds() {
    }
    static void play(String sound) {
        try {
            f = new File("./sounds/" + sound + ".wav");
            ais = AudioSystem.getAudioInputStream(f);
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
            ais.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
