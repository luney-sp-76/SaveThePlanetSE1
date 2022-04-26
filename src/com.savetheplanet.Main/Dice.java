package com.savetheplanet.Main;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Dice implements IDie{
    @Override
    public void roll()  {
        diceAudio();
        //A message is displayed saying “Dice Rolling...”
        System.out.println("Dice Rolling...");
    }


    private static void diceAudio() throws RuntimeException  {
        try {
            File f;
            AudioInputStream ais;

                    f = new File("./sounds/dice.wav");
                    ais = AudioSystem.getAudioInputStream(f);
                    Clip dice = AudioSystem.getClip();
                    dice.open(ais);
                    dice.start();
                    ais.close();

            } catch (UnsupportedAudioFileException ex) {
            throw new RuntimeException(ex);
        } catch (LineUnavailableException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }catch (Exception e) {
            e.printStackTrace();

    }
        }
    }

