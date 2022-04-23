package com.savetheplanet.Main;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Game implements Die{

    private static List<Square> board = new ArrayList<>();
    private static List<Player> players = new ArrayList<>();

    private static final Scanner MENU = new Scanner(System.in);
    private static final int COLLECT = 500;

    public Game() {
        playGame();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void playGame() {

        System.out.println("Welcome To Save The Planet");
        System.out.println("Would you like to Play? y/n");

        while (true) {
            switch (MENU.nextLine().toLowerCase()) {
                case "y":
                case "yes":
                    initiateGameOptions();
                    break;
                case "n":
                case "no":
                    quitOutsideOfGamePlay();
                    break;
                default:
                    System.out.println("Sorry please enter y/yes or n/no");

            }
        }
    }

    private static void quitOutsideOfGamePlay() {
        System.out.println("See You Next Time!");
        System.exit(1);
    }

    private static void initiateGameOptions() {
        System.out.println("Game Menu");
        System.out.println("----------");

        System.out.printf("1) new game%n2) restart game%n3) quit%n");
        switch (Integer.parseInt(MENU.nextLine())) {
            case 1:
                System.out.println("Ok Lets Go!");
                playNewGame();
                break;
            case 2:
                loadGame();
                // testing
                System.out.println(players);
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


            // saveGame();
            saveGame();
            // wipes the lists to make sure we have persistence.
            board.clear();
            players.clear();
            System.out.println(board);
            System.out.println(players);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private static void saveGame() {

        System.out.println("Do you wish to save the game? y/n");
        if (!MENU.nextLine().toLowerCase().contains("y"))
            playGame();

        Create.save(MENU, board, players);
        System.out.println("S:A:V:E");
    }

    @SuppressWarnings("unchecked")
    private static void loadGame() {

        System.out.println("Do you want to load a Saved Game? y/n");
        if (!MENU.nextLine().toLowerCase().contains("y"))
            playGame();

        try {
            HashMap<String, Object> load = Create.load(MENU);

            if (load == null)
                playGame();

            assert load != null;
            board = (List<Square>) load.get("Board");
            players = (List<Player>) (load.get("Players"));
            System.out.println("L:O:A:D");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * @param card   - Chance Card player has chosen
     * @param player - Current actor
     */
    public static void parseCard(ChanceCard card, Player player) {

        if (card.getAssigned() == RandomSquareAssignment.PAY) {
            int pay = card.getAction();
            player.setFunding(player.getFunding() - pay);
        } else if (card.getAssigned() == RandomSquareAssignment.RECEIVE) {
            player.setFunding(player.getFunding() + card.getAction());
        } else if (card.getAssigned() == RandomSquareAssignment.COLLECT_FUNDING) {
            player.setFunding(player.getFunding() + COLLECT);
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
     * @param deck The Deck of Chance Cards
     * @return A single Chance Card
     */
    static ChanceCard shuffleDeck(List<ChanceCard> deck) {
        Collections.shuffle(deck);
        return deck.get(0);
    }

    public static void collectFunding(Player player) {
        player.setFunding((player.getFunding() + COLLECT));
    }


    @Override
    public int roll() throws InterruptedException {
        //A message is displayed saying “Dice Rolling...”
        System.out.println("Dice Rolling...");
        //The dice roll takes a few seconds.
        TimeUnit.SECONDS.sleep(3);
        int die1Result = randomNum();
        int die2Result = randomNum();
        int totalResult = die1Result + die2Result;
        //A message is then displayed saying “Die 1 is x, Die 2
        //is y. You will move forward x+y places.”
        System.out.printf("Die 1 is %d, Die 2 is %d...%nYou will move forward %d spaces.%n", die1Result, die2Result, totalResult);
        return totalResult;
    }

    private int randomNum(){
        int min = 1;
        int max = 6;
        int result = (int)Math.floor(Math.random()*(max-min+1)+min);
        return  result;
    }

}
