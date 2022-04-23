package com.savetheplanet.Main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class GameTest {

    Player p1;
    Player p2;
    FundableSquare s1;
    FundableSquare s2;
    List<FundableSquare> ownedSquares;

    @BeforeEach
    void setUp() {
        p1 = new Player();
        p2 = new Player();
        s1 = new FundableSquare("Led Light bulbs", 3, new String[]{"Conserve", "3", "2", "200", "250", "30|50|100|200|350"});
        s2 = new FundableSquare("Led Light bulbs", 3, new String[]{"Conserve", "3", "2", "100", "250", "30|50|100|200|350"});
    }

    @Test
    void purchaseSquare_success() {
        int initialBalance = 500;
        p1.setFunding(initialBalance);
        int expectedBalance = initialBalance - s1.getCost();
        Game.purchaseSquare(p1, s1);
        ownedSquares = new ArrayList<FundableSquare>();
        ownedSquares.add(s1);
        assertEquals(p1.getFunding(), expectedBalance);
        assertEquals(p1.getOwnedSquares(), ownedSquares);
    }

    @Test
    void purchaseSquare_noFunding() {
        int initialBalance = 50;
        p1.setFunding(initialBalance);
        int expectedBalance = initialBalance;
        Game.purchaseSquare(p1, s1);
        ownedSquares = new ArrayList<FundableSquare>();
        assertEquals(p1.getFunding(), expectedBalance);
        assertEquals(p1.getOwnedSquares(), ownedSquares);
    }

    @Test
    void purchaseSquare_owned() {
        int initialBalance = 500;
        p1.setFunding(initialBalance);
        int expectedBalance = initialBalance;
        s1.setOwner(p2);
        Game.purchaseSquare(p1, s1);
        ownedSquares = new ArrayList<FundableSquare>();
        assertEquals(p1.getFunding(), expectedBalance);
        assertEquals(p1.getOwnedSquares(), ownedSquares);
    }

    @Test
    void payRates_success() {
        int initialBalance = 500;
        p1.setFunding(initialBalance);
        p2.setFunding(initialBalance);
        int rates = s1.getRatesBill();
        int p1_expectedBalance = initialBalance - rates;
        int p2_expectedBalance = initialBalance + rates;
        s1.setOwner(p2);
        Game.payRates(p1, s1);
        assertEquals(p1.getFunding(), p1_expectedBalance);
        assertEquals(p2.getFunding(), p2_expectedBalance);
    }

    @Test
    void payRates_noFunding() {
        int initialBalance = 5;
        p1.setFunding(initialBalance);
        s1.setOwner(p2);
        Game.payRates(p1, s1);
        assertEquals(p1.getFunding(), initialBalance);
    }

    @Test
    void liquidate() {
        int initialBalance = 100;
        p1.setFunding(initialBalance);
        int expectedBalance = initialBalance + s2.getCost();
        p1.addOwnedSquare(s1);
        p1.addOwnedSquare(s2);
        ownedSquares = new ArrayList<FundableSquare>();
        ownedSquares.add(s1);
        Game.liquidate(p1);
        assertEquals(p1.getFunding(), expectedBalance);
        assertEquals(p1.getOwnedSquares(), ownedSquares);
    }

    //Trade tests to implement
    //test no properties to trade
    //test player doesn't want to trade
    //test direct swap
    //test swap w/ value difference
    //test swap w/value difference w/ no funding

}
