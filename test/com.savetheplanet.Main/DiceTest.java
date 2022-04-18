package com.savetheplanet.Main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    Dice testDie1;

    @BeforeEach
    void setUp() {
        testDie1 = new Dice();

    }

    @Test
    void testRollResultIsWithinMaximumOf12() throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            int testResult = testDie1.roll();
            if (testResult < 2){
                fail("Number below range 2 - 12");
            } else if (testResult > 12) {
                fail("Number above range 2 - 12");
            }
        }
    }
}