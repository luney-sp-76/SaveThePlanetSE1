package com.savetheplanet.Main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CreateTest {
    Create create;
    List<Player> players;
    Scanner scan;
    Player p1, p2;
    String validName, validName2, validName3;

    @BeforeEach
    void setUp() {
        create = new Create();
        scan = new Scanner(System.in);
        players = new ArrayList<>();
        p1 = new Player();
        p2 = new Player();
        validName = "Bob";
        validName2 = "Bill";
        validName3 = "Bert";


    }

    @Test
    void testUniqueName() {
        // 2 players, p1 set to validName, p2 set to validName, then when it says no, is set to validName2.
        ByteArrayInputStream in = new ByteArrayInputStream(("2" + System.lineSeparator() + validName +
                System.lineSeparator() + validName + System.lineSeparator() + validName2).getBytes());
        System.setIn(in);
        players = create.players();

        // Checks that p2's name is validName2.
        assertEquals(validName2, players.get(1).getName());


    }

    @Test
    void testNumberOfPlayers() {
        // Sets a number of invalid player counts, then a valid one and the correct number of players.
        ByteArrayInputStream in = new ByteArrayInputStream(("-1" + System.lineSeparator() + "0" +
                System.lineSeparator() + "dog" + System.lineSeparator() + "6" + System.lineSeparator() +
                "3" + System.lineSeparator() + validName + System.lineSeparator() + validName2 + System.lineSeparator() + validName3).getBytes());
        System.setIn(in);

        players = create.players();

        // checks the system has handled the errors as expected and ended with correct size.
        assertEquals(3, players.size());

        // and that the last player has the correct name
        assertEquals(validName3, players.get(2).getName());
    }

}
