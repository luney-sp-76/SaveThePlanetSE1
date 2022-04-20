package com.savetheplanet.Main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreatePlayersTest {

    List<Player> players;
    Scanner scan;
    Player p1, p2;
    String validName;

    @BeforeEach
    void setUp() {
        scan = new Scanner(System.in);
        players = new ArrayList<>();
        p1 = new Player();
        p2 = new Player();
        validName = "Bob";

    }

    @Test
    void testValidateName() {
        p1 = new Player(validName);
        players.add(p1);

        CreatePlayers underTest = new CreatePlayers();

        IllegalArgumentException iAE = assertThrows(IllegalArgumentException.class, () ->   underTest.validateName(scan, validName, players,2 ));
        assertEquals("Names must be unique.", iAE.getMessage());
    }
}
