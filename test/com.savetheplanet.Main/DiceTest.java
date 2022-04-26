package com.savetheplanet.Main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRollResultIsWithinMaximumOf12() throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        for (int i = 0; i < 20; i++) {
            int testResult = Game.move();
            if (testResult < 2) {
                fail("Number below range 2 - 12");
            } else if (testResult > 12) {
                fail("Number above range 2 - 12");
            }
        }
    }
}