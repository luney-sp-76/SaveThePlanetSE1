package com.savetheplanet.Main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateTest {

    List<Player> players;
    Scanner scan;
    Player p1, p2;
    String validName, validName2, validName3;

    @BeforeEach
    void setUp() {
        scan = new Scanner(System.in);
        players = new ArrayList<>();
        validName = "Bob";
        validName2 = "Bill";
        validName3 = "Bert";
        p1 = new Player(validName);
        p2 = new Player(validName2);

        ByteArrayInputStream fakeScan = getFakeScan();
        System.setIn(fakeScan);
    }

    //This worked per @Test until we moved to Game.MENU, now it's ONE CHONKY BOY.
    private ByteArrayInputStream getFakeScan() {
        return new ByteArrayInputStream(("2" + System.lineSeparator()   // unique name start - 2 players
                + validName + System.lineSeparator()                    //  valid name
                + validName + System.lineSeparator()                    // same valid name - should fail
                + validName2 + System.lineSeparator()                   // new valid name and unique name assert.
                + "-1" + System.lineSeparator()                         // numbers of player start - invalid: Too Small
                + "0" + System.lineSeparator()                          // invalid: Too small.
                + "dog" + System.lineSeparator()                        // invalid: A dog.
                + "6" + System.lineSeparator()                          // invalid: Too large
                + "3" + System.lineSeparator()                          // Valid: 3 players
                + validName + System.lineSeparator()                    // p1 name
                + validName2 + System.lineSeparator()                   // p2 name
                + validName3 + System.lineSeparator()).getBytes());     // p3 name & number of players assert
    }
    @Test
    void testUniqueName() {
        players = Create.players();
        // Checks that p2's name is validName2.
        assertEquals(validName2, players.get(1).getName());
    }

    @Test
    void testNumberOfPlayers() {
        players = Create.players();
        // checks the system has handled the errors as expected and ended with the correct number of players.
        assertEquals(3, players.size());
        // and that the last player has the expected name
        assertEquals(validName3, players.get(2).getName());
    }
}
