package com.savetheplanet.Main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;


class GameTest {

    Player p1;
    Player p2;
    FundableSquare s1;
    FundableSquare s2;
    FundableSquare s3;
    FundableSquare s4;
    List<FundableSquare> ownedSquares;


    @BeforeEach
    void setUp() {
        p1 = new Player("Mathew");
        p2 = new Player("Neil");

        s1 = new FundableSquare("Led Light bulbs", 3, new String[]{"Conserve", "3", "2", "200", "250", "1", "30|50|100|200|350"});
        s2 = new FundableSquare("Water Tap Timers", 3, new String[]{"Conserve", "3", "2", "200", "250", "1", "30|50|100|200|350"});
        s4 = new FundableSquare("Public Transport", 4, new String[]{"Reduce", "4", "3", "300", "400", "2", "50|100|175|300|500"});
        s3 = new FundableSquare("Thrift Store", 3, new String[]{"Reuse", "5", "3", "300", "400", "2", "50|100|175|300|500"});

    }

    @Test
    void testPurchaseSquare_success() {
        int initialBalance = 500;
        p1.setFunding(initialBalance);
        int expectedBalance = initialBalance - s1.getCost();
        Game.purchaseSquare(p1, s1);
        ownedSquares = new ArrayList<>();
        ownedSquares.add(s1);
        assertEquals(p1.getFunding(), expectedBalance);
        assertEquals(p1.getOwnedSquares(), ownedSquares);
    }

    @Test
    void testPurchaseSquare_noFunding() {
        int initialBalance = 50;
        p1.setFunding(initialBalance);
        Game.purchaseSquare(p1, s1);
        ownedSquares = new ArrayList<>();
        assertEquals(p1.getFunding(), initialBalance);
        assertEquals(p1.getOwnedSquares(), ownedSquares);
    }

    @Test
    void testPurchaseSquare_owned() {
        int initialBalance = 500;
        p1.setFunding(initialBalance);
        s1.setOwner(p2);
        Game.purchaseSquare(p1, s1);
        ownedSquares = new ArrayList<>();
        assertEquals(p1.getFunding(), initialBalance);
        assertEquals(p1.getOwnedSquares(), ownedSquares);
    }

    @Test
    void testPayRates_success() {
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
    void testPayRates_noFunding() {
        int initialBalance = 5;
        p1.setFunding(initialBalance);
        s1.setOwner(p2);
        p2.addOwnedSquare(s1);
        Game.payRates(p1, s1);
        assertEquals(p1.getFunding(), initialBalance);
    }

    @Test
    void testLiquidate_propertyRemoval() {
        int initialBalance = 100;
        p1.setFunding(initialBalance);
        int expectedBalance = initialBalance + s2.getCost();
        p1.addOwnedSquare(s1);
        p1.addOwnedSquare(s2);
        ownedSquares = new ArrayList<>();
        ownedSquares.add(s1);
        Game.liquidate(p1);
        assertEquals(p1.getFunding(), expectedBalance);
        assertEquals(p1.getOwnedSquares(), ownedSquares);
    }

    @Test
    void testLiquidate_developmentRemoval() {
        int initialBalance = 100;
        int initialDevLevel = 2;
        s2.setDevLevel(initialDevLevel);
        p1.setFunding(initialBalance);
        int expectedBalance = initialBalance + s2.getDevCost();
        int expectedDevLevel = initialDevLevel - 1;
        p1.addOwnedSquare(s1);
        p1.addOwnedSquare(s2);
        ownedSquares = new ArrayList<>();
        ownedSquares.add(s1);
        ownedSquares.add(s2);
        Game.liquidate(p1);
        assertEquals(p1.getFunding(), expectedBalance);
        assertEquals(p1.getOwnedSquares(), ownedSquares);
        assertEquals(s2.getDevLevel(), expectedDevLevel);
    }


    @Test
    void testTrade_noProperties() {
        List<FundableSquare> p1Properties = new ArrayList<>();
        List<FundableSquare> p2Properties = new ArrayList<>();

        p1.addOwnedSquare(s1);
        p1Properties.add(s1);

        Game.trade(p1, p2);
        assertEquals(p1.getOwnedSquares(), p1Properties);
        assertEquals(p2.getOwnedSquares(), p2Properties);
    }

    @Test
    void testTrade_rejectRequest() {
        List<FundableSquare> p1Properties = new ArrayList<>();
        List<FundableSquare> p2Properties = new ArrayList<>();

        p1.addOwnedSquare(s1);
        p2.addOwnedSquare(s2);

        p1Properties.add(s1);
        p2Properties.add(s2);

        ByteArrayInputStream fakeScan = new ByteArrayInputStream(("1" + System.lineSeparator() + "1" + System.lineSeparator() + "n" + System.lineSeparator()).getBytes());
        Game.MENU = new Scanner(fakeScan);

        Game.trade(p1, p2);
        assertEquals(p1.getOwnedSquares(), p1Properties);
        assertEquals(p2.getOwnedSquares(), p2Properties);
    }

    @Test
    void testTrade_directSwap() {
        List<FundableSquare> p1Properties = new ArrayList<>();
        List<FundableSquare> p2Properties = new ArrayList<>();

        p1.addOwnedSquare(s3);
        p2.addOwnedSquare(s2);

        p1Properties.add(s2);
        p2Properties.add(s3);

        ByteArrayInputStream fakeScan = new ByteArrayInputStream(("1" + System.lineSeparator() + "1" + System.lineSeparator() + "y" + System.lineSeparator()).getBytes());
        Game.MENU = new Scanner(fakeScan);

        Game.trade(p1, p2);
        assertEquals(p1.getOwnedSquares(), p1Properties);
        assertEquals(p2.getOwnedSquares(), p2Properties);
    }

    @Test
    void testTrade_swapCostDifference() {
        int initialBalance = 500;

        List<FundableSquare> p1Properties = new ArrayList<>();
        List<FundableSquare> p2Properties = new ArrayList<>();

        p1.setFunding(initialBalance);
        p2.setFunding(initialBalance);

        p1.addOwnedSquare(s1);
        p2.addOwnedSquare(s2);

        p1Properties.add(s2);
        p2Properties.add(s1);

        int s1Cost = s1.getCost() + (s1.getDevCost() * s1.getDevLevel());
        int s2Cost = s2.getCost() + (s2.getDevCost() * s2.getDevLevel());

        int costDifference = s1Cost - s2Cost;

        ByteArrayInputStream fakeScan = new ByteArrayInputStream(("1" + System.lineSeparator() + "1" + System.lineSeparator() + "y" + System.lineSeparator()).getBytes());
        Game.MENU = new Scanner(fakeScan);

        Game.trade(p1, p2);
        assertEquals(p1.getOwnedSquares(), p1Properties);
        assertEquals(p2.getOwnedSquares(), p2Properties);
        assertEquals(p1.getFunding(), (initialBalance + costDifference));
        assertEquals(p2.getFunding(), (initialBalance - costDifference));
    }

    @Test
    void testTrade_swapCostDifference_devDifference() {
        int initialBalance = 500;

        List<FundableSquare> p1Properties = new ArrayList<>();
        List<FundableSquare> p2Properties = new ArrayList<>();

        p1.setFunding(initialBalance);
        p2.setFunding(initialBalance);

        p1.addOwnedSquare(s1);
        p2.addOwnedSquare(s3);

        p1Properties.add(s1);
        p2Properties.add(s3);

        s3.setDevLevel(2);

        int s1Cost = s1.getCost() + (s1.getDevCost() * s1.getDevLevel());
        int s2Cost = s3.getCost() + (s3.getDevCost() * s3.getDevLevel());

        int costDifference = s1Cost - s2Cost;

        ByteArrayInputStream fakeScan = new ByteArrayInputStream(("1" + System.lineSeparator() + "1" + System.lineSeparator() + "y" + System.lineSeparator()).getBytes());
        Game.MENU = new Scanner(fakeScan);

        Game.trade(p1, p2);
        assertEquals(p1.getOwnedSquares(), p1Properties);
        assertEquals(p2.getOwnedSquares(), p2Properties);
        assertEquals(p1.getFunding(), (initialBalance + costDifference));
        assertEquals(p2.getFunding(), (initialBalance - costDifference));
    }

    @Test
    void testTrade_swapCostDifference_noFunding() {
        int p1InitialBalance = 500;
        int p2InitialBalance = 50;

        List<FundableSquare> p1Properties = new ArrayList<>();
        List<FundableSquare> p2Properties = new ArrayList<>();

        p1.setFunding(p1InitialBalance);
        p2.setFunding(p2InitialBalance);

        p1.addOwnedSquare(s3);
        p2.addOwnedSquare(s1);

        p1Properties.add(s3);
        p2Properties.add(s1);

        ByteArrayInputStream fakeScan = new ByteArrayInputStream(("1" + System.lineSeparator() + "1" + System.lineSeparator() + "y" + System.lineSeparator()).getBytes());
        Game.MENU = new Scanner(fakeScan);

        Game.trade(p1, p2);
        System.out.println(p1.getOwnedSquares());
        System.out.println(p1Properties);

        assertEquals(p1.getOwnedSquares(), p1Properties);
        assertEquals(p2.getOwnedSquares(), p2Properties);
        assertEquals(p1.getFunding(), p1InitialBalance);
        assertEquals(p2.getFunding(), p2InitialBalance);
    }

//    @Test
//    void testPlayersPreRollFourOptions() {
//        int p1InitialBalance = 500;
//        int p2InitialBalance = 50;
//
//        List<FundableSquare> p1Properties = new ArrayList<>();
//        List<FundableSquare> p2Properties = new ArrayList<>();
//
//        p1.setFunding(p1InitialBalance);
//        p2.setFunding(p2InitialBalance);
//
//        p1.addOwnedSquare(s1);
//        p1.addOwnedSquare(s2);
//        p1.addOwnedSquare(s3);
//
//
//        p1Properties.add(s1);
//        p1Properties.add(s2);
//        p1Properties.add(s3);
//        p2Properties.add(s4);
//        p1Properties.get(0).setOwner(p1);
//        p1Properties.get(1).setOwner(p1);
//        p1Properties.get(2).setOwner(p1);
//
//
//        ByteArrayInputStream fakeScan = new ByteArrayInputStream(("y" + System.lineSeparator() + "1" + System.lineSeparator() + "2" + System.lineSeparator() + "bob" + System.lineSeparator() + "jim" + System.lineSeparator() + "1" + System.lineSeparator() + "n" + System.lineSeparator()).getBytes());
//        Game.MENU = new Scanner(fakeScan);
//        Game.playGame();
//
//
//    }

    @Test
    void testPlayersPreRollThreeOptions() {


        int p1InitialBalance = 300;
        int p2InitialBalance = 50;

        List<FundableSquare> p1Properties = new ArrayList<>();
        List<FundableSquare> p2Properties = new ArrayList<>();

        p1.setFunding(p1InitialBalance);
        p2.setFunding(p2InitialBalance);

        p1.addOwnedSquare(s1);
        p1.addOwnedSquare(s2);
        p1.addOwnedSquare(s3);


        p1Properties.add(s1);
        p1Properties.add(s2);
        p1Properties.add(s3);
        p2Properties.add(s4);
        p1Properties.get(0).setOwner(p1);
        p1Properties.get(1).setOwner(p1);
        p1Properties.get(2).setOwner(p1);

        Game.PLAYERS.setPlayers(new ArrayList<>());
        Game.PLAYERS.getPlayers().add(p1);
        Game.PLAYERS.getPlayers().add(p2);

        ByteArrayInputStream fakeScan = new ByteArrayInputStream(("y" + System.lineSeparator() + "1" + System.lineSeparator() + "2" + System.lineSeparator() + "bob" + System.lineSeparator() + "jim" + System.lineSeparator() + "1" + System.lineSeparator() + "n" + System.lineSeparator()).getBytes());
        Game.MENU = new Scanner(fakeScan);


    }

}
