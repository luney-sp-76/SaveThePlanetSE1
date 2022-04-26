package com.savetheplanet.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    List<ChanceCard> deck;

    public Deck() {
        makeDeck();
    }

    /**
     * Reads in the data from the provided csv and returns a list
     * - Andrew
     */
    private void makeDeck() {
        List<ChanceCard> listFromFile = new ArrayList<>();
        File file = new File("randomSquareAssignment.csv");
        try (FileReader fr = new FileReader(file); BufferedReader reader = new BufferedReader(fr)) {
            reader.readLine();
            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                String[] parts = line.split(",");
                try {
                    RandomSquareAssignment random = RandomSquareAssignment.valueOf(parts[0].toUpperCase());
                    if (parts.length == 1) {
                        ChanceCard card = new ChanceCard(random);
                        listFromFile.add(card);
                    } else {
                        int move = Integer.parseInt(parts[1]);
                        ChanceCard card = new ChanceCard(random, move);
                        listFromFile.add(card);
                    }
                } catch (IllegalArgumentException illegalArg) {
                    System.out.println(illegalArg.getMessage());
                    System.out.println("Skipping this line");
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found error");
        } catch (IOException e) {
            System.out.println("IO Exception");
        } catch (Exception e) {
            System.out.println("Exception occurred");
            System.out.println(listFromFile.size() + " lines read successfully");
            System.out.println(e.getMessage());
        }
        System.out.println(listFromFile.size() + " lines read successfully");

        this.deck = listFromFile;
    }

    public List<ChanceCard> getDeck() {
        return deck;
    }

    /**
     * shuffles deck and returns the top card (first in List)
     *
     * @return A single Chance Card
     */
    public ChanceCard shuffle() {
        Collections.shuffle(deck);
        System.out.println("Shuffling...\n");
        return deck.get(0);
    }
}
