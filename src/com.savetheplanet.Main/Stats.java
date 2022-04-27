package com.savetheplanet.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

public class Stats {

    private List<Player> players;
    private AtomicInteger rank;

    public Stats(List<Player> players) {

        List<Player> statsList = new ArrayList<>(players);
        setPlayers(statsList);
    }

    public void setPlayers(List<Player> players) {

        this.players = players;
    }

    void full() {
        rank = new AtomicInteger(0);

        players.forEach(Player::calcTotalValue);

        System.out.println("###################################################");
        System.out.println("################## Leader Board ###################");
        System.out.println("###################################################");
        System.out.printf("| %-4s | %-32s | %-6s|%n", "Rank", "Player", "Funds");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

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
        System.out.println();
    }

    void end() {

        full();
        String winCondition = drawCheck();

        System.out.println("oooooo   oooooo     oooo  o8o");
        System.out.println(" `888.    `888.     .8'   `\"'");
        System.out.println("`888.   .8888.   .8'   oooo  ooo. .oo.   ooo. .oo.    .ooooo.  oooo d8b ");
        System.out.println("`888  .8'`888. .8'    `888  `888P\"Y88b  `888P\"Y88b  d88' `88b `888\"\"8P ");
        System.out.println("`888.8'  `888.8'      888   888   888   888   888  888ooo888  888 ");
        System.out.println("`888'    `888'       888   888   888   888   888  888    .o  888");
        System.out.println("`8'      `8'       o888o o888o o888o o888o o888o `Y8bod8P' d888b");
        System.out.println();

        // this bold ansi escape doesn't work on every OS. Console is so limited man.
        System.out.printf("%-6s\u001B[1m %s!%n%s.%n", "Congratulations", players.get(0).getName(), "Won by " + winCondition);
        try {
            Thread.sleep(6000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SaveThePlanet.welcome();
    }

    /**
     * @return Condition player won by.
     */
    private String drawCheck() {

        // If one player has the most total value they win outright
        if (players.get(0).getTotalValue() > players.get(1).getTotalValue())
            return "Total Value";

        // Otherwise, remove the players with less Total Value than the winning amount.
        players = players.stream().filter(p -> p.getTotalValue() == players.get(0).totalValue).collect(toList());

        // sort by funding, descending.
        players.sort(Comparator.comparing(Player::getFunding).reversed());

        // If one remaining player has the most funding they win by first tie-break rule.
        if (players.get(0).getFunding() > players.get(1).getFunding())
            return "Tie-break 1: Funding";

        // if there is still no clear winner, they are chosen by random selection from those remaining.
        Collections.shuffle(players);
        return "Tie-Break 2: Random Selection";
    }

    void elide() {
        rank = new AtomicInteger(0);
        players.forEach(Player::calcTotalValue);

        System.out.println("################## Leader Board ###################");
        System.out.printf("| %-4s | %-32s | %-6s|%n", "Rank", "Player", "");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        players.sort(Comparator.comparing(Player::getTotalValue).reversed());

        players.forEach(p -> {
            System.out.printf("|  #%-3d| %-32s | £%-5d|%n", rank.incrementAndGet(), p.getName(), p.getFunding());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.printf("|%6s| %-32s | %-5d |%n", "", "* Total Value:", p.getTotalValue());
            System.out.println("---------------------------------------------------");
        });
        System.out.println();
    }
} //class