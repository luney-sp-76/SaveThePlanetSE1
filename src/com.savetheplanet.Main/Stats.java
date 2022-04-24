package com.savetheplanet.Main;


import sun.security.util.Length;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Stats {

    private List<Player> players = new ArrayList<>();

    public Stats(List<Player> players) {
        setPlayers(players);
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void listPlayers() {
        AtomicInteger rank = new AtomicInteger(0);

        players.forEach(Player::calcTotalValue);

        System.out.println("####################S:T:A:T:S:#####################");
        System.out.printf("| %-4s | %-32s | %-6s|%n", "Rank", "Player", "Funds");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

//        players.sort(Comparator.comparing(Player::getFunding));
        players.sort(Comparator.comparing(Player::getTotalValue).reversed());

        players.forEach(player -> {

            System.out.printf("|  #%-3d| %-32s | £%-5d|%n", rank.incrementAndGet(), player.getName(), player.getFunding());
            System.out.println("---------------------------------------------------");
            System.out.printf("|%6s| %-32s | %s |%n", "", "Owned Properties", "Value");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            player.getOwnedSquares().forEach(square -> System.out.printf("|%6s| %-32s | %-5d |%n", "", square.getName(), square.getValue()));
            System.out.printf("|%6s| %-32s | %-5d |%n", "", "* Total Value:", player.getTotalValue());
            System.out.println("###################################################");
        });
    }

}
