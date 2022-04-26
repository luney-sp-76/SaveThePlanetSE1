package com.savetheplanet.Main;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Dice implements IDie {
    @Override
    public void roll() {
        Sounds.play("dice");
        //A message is displayed saying “Dice Rolling...”
        System.out.println("Dice Rolling...");
    }
}

