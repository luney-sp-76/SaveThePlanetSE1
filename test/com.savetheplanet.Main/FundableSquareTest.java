package com.savetheplanet.Main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FundableSquareTest {

    FundableSquare fs1, fs2;
    Player p1;
    String name;
    int field;
    int fieldSize;
    int cost;
    int devCost;
    int devLevel;
    String status;
    int[] ratesCosts = {30, 50, 100, 200, 350};
    int majorDevCost;
    int level3Rates;

    @BeforeEach
    void setUp() {

        fs2 = new FundableSquare("Water Tap Timers", 3, new String[]{"Conserve", "3", "2", "200", "250", "30|50|100|200|350"});
        name = "Led Light bulbs";
        field = 3;
        fieldSize = 2;
        cost = 200;
        devCost = 250;
        devLevel = 0;
        status = "Available";
        majorDevCost = 600;
        level3Rates = 200;
    }

    @Test
    void constructorGettersAndSetters() {
        fs1 = new FundableSquare("Led Light bulbs", 3, new String[]{"Conserve", "3", "2", "200", "250", "30|50|100|200|350"});

        assertEquals(fs1.getName(), name);
        assertEquals(fs1.getField(), field);
        assertEquals(fs1.getFieldSize(), fieldSize);
        assertEquals(fs1.getCost(), cost);
        assertEquals(fs1.getDevCost(), devCost);
        assertEquals(fs1.getDevLevel(), devLevel);
        assertArrayEquals(fs1.getRatesCosts(), ratesCosts);
        assertEquals(fs1.getStatus(), status);
    }

    @Test
    void devCost() {
        fs2.setDevLevel(3);
        assertEquals(fs2.getDevCost(), majorDevCost);
    }

    @Test
    void ratesBill() {
        fs2.setDevLevel(3);
        assertEquals(fs2.getRatesBill(), level3Rates);
    }
}