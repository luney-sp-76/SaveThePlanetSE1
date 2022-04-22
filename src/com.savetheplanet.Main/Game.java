package com.savetheplanet.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Game {

    public static List<Square> board = new ArrayList<>();

    public static List<Player> players = new ArrayList<>();

    private static final int COLLECT = 500;

    public Game(){

    }




    public static void playGame(String response){
        response =response.toLowerCase();
        switch(response){
            case "y":
            case "yes":
                initiateGameOptions();
                break;
            case "n":
            case "no":
                quitOutsideOfGamePlay();
                break;
            default:
               Scanner scanStart = new Scanner(System.in);
                System.out.println("Sorry please enter Y/yes or N/no");
                playGame(scanStart.next());

        }

    }

    private static void quitOutsideOfGamePlay() {
        System.out.println("See You Next Time!");
        System.exit(1);

    }

    private static void initiateGameOptions() {
        System.out.println("Game Menu");
        System.out.println("----------");
        Scanner menu = new Scanner(System.in);
        System.out.printf("1) new game%n2) restart game%n3)quit%n");
        switch(menu.nextInt()){
            case 1:
                System.out.println("Ok Lets Go!");
                playNewGame();
                break;
            case 2:
                loadGame();
                break;
            case 3:
                quitOutsideOfGamePlay();
                break;
            default:
                System.out.println("that's not an option");
                initiateGameOptions();
        }
    }

    private static void playNewGame() {
        try {
            // Create Players
            players = Create.players();
            System.out.println(players);

            // Create Board/Squares
            board = Create.board();
            System.out.println(board);

            // light demo
            System.out.println(board.get(2));
            players.get(1).addOwnedSquare((FundableSquare) board.get(2));
            ((FundableSquare) board.get(2)).setStatus("Owned by: " + players.get(1).getName());
            System.out.println(players.get(1).getOwnedSquares());
            ((FundableSquare) board.get(2)).setDevLevel(2);
            System.out.println(players.get(1).getOwnedSquares());


            //proof of concept testing

            System.out.println("Game initialised: " + players.get(0));
            collectFunding(players.get(0));
            System.out.println("Player passes GO: £" + players.get(0).getFunding());
            //read all Chance Cards
            List<ChanceCard> mainDeck = Create.deck();
            //shuffle chance cards
            ChanceCard chance = shuffleDeck(mainDeck);
            System.out.println("Shuffling...\n");
            //trace statements
            parseCard(chance, players.get(0));
            //chance.fullDetails();
            System.out.println("Proof of concept: " + chance.getAssigned());
            chance.fullDetails(chance);
            System.out.println(players.get(0).getName() + " post card: £" + players.get(0).getFunding());


            saveGame();
            board.clear();
            players.clear();
            System.out.println(board);
            System.out.println(players);
            loadGame();
            System.out.println(board);
            System.out.println(players);


        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }


    static void saveGame() {

        try {
            FileOutputStream fos = new FileOutputStream("board.sav");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(board);

            fos = new FileOutputStream("players.sav");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(players);

            oos.close();
        } catch (IOException e) {
            System.out.println("Save error.");
            System.out.println(e.getMessage());

        }

    }

    @SuppressWarnings("unchecked")
    public static void loadGame() {

        try {
            FileInputStream fis = new FileInputStream("board.sav");
            ObjectInputStream ois = new ObjectInputStream(fis);
            board = (List<Square>) ois.readObject();


            fis = new FileInputStream("players.sav");
            ois = new ObjectInputStream(fis);
            players = (List<Player>) ois.readObject();

            ois.close();
        } catch (Exception e) {
            System.out.println("Load game error");
            System.out.println(e.getMessage());
        }
    }


    /**
     * @param card   - Chance Card player has chosen
     * @param player - Current actor
     * @return
     */
    public static Player parseCard(ChanceCard card, Player player) {

        if (card.getAssigned() == RandomSquareAssignment.PAY) {
            int pay = card.getAction();
            player.setFunding(player.getFunding() - pay);
            return player;
        }

        if (card.getAssigned() == RandomSquareAssignment.RECEIVE) {
            player.setFunding(player.getFunding() + card.getAction());
            return player;
        }

        if (card.getAssigned() == RandomSquareAssignment.COLLECT_FUNDING) {
            player.setFunding(player.getFunding() + COLLECT);
            return player;
        } else {
            return player;
        }
    }

    public static void purchaseSquare(Player player, FundableSquare square) {
        if (square.getOwner() == null) {
            if (player.getFunding() >= square.getCost()) {
                square.setOwner(player);
                player.setFunding(player.getFunding() - square.getCost());
                player.addOwnedSquare(square);
                System.out.println("Congratulations! You are the proud owner of " + square.getName() + ".");
            } else {
                System.out.println("Sorry, you don't have enough money to purchase this.");
            }
        } else {
            System.out.println("Sorry! " + square.getName() + " is not for sale.");
        }
    }

    public static void liquidate(Player player) {
        FundableSquare square = player.getLowestValueSquare();
        int cost = square.getCost();
        System.out.println("Seizing: " + square.getName() + ". You will be credited £" + cost);
        player.ownedSquares.remove(square);
        square.setOwner(null);
        player.setFunding(player.getFunding() + cost);
        System.out.println("Balance: £" + player.getFunding());
    }

    public static void payRates(Player player, FundableSquare square) {
        if (square.getOwner() != null && square.getOwner() != player) {
            Player owner = square.getOwner();
            int rates = square.getRatesBill();
            if (player.getFunding() >= rates) {
                player.setFunding(player.getFunding() - rates);
                owner.setFunding(owner.getFunding() + rates);
            } else {
                System.out.println("You can't pay your rates bill! Time to liquidate your property.");

                if (!player.getOwnedSquares().isEmpty()) {
                    liquidate(player);
                    payRates(player, square);
                } else {
                    System.out.println("You can't pay your rates bill and have no more property! It looks like someone else will have to save the world...");
                    //player out
                }
            }
        } else {
            System.out.println("No rates due.");
        }
    }

    /**
     * shuffles deck and returns the top card (first in List)
     *
     * @param deck
     * @return
     */
    static ChanceCard shuffleDeck(List<ChanceCard> deck) {
        Collections.shuffle(deck);
        return deck.get(0);
    }

    public static Player collectFunding(Player player) {
        player.setFunding((player.getFunding() + COLLECT));
        return player;
    }

    public String open() {
        Scanner prompt = new Scanner(System.in);
        System.out.println("Welcome To Save The Planet");
        System.out.println("Would you like to Play? y/n");
        return prompt.next();
    }
}
