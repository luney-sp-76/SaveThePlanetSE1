package com.savetheplanet.Main;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Game implements IDie {

    private static List<Square> board = new ArrayList<>();
    private static List<Player> players = new ArrayList<>();

    private static final int T60 = 60000;

    // for 30 second with 15 second warning
    private static final int T15 = 15000;

    static Timer timer60 = Create.timer(T60);

    private static final int COLLECT = 500;

    public static Scanner MENU = new Scanner(System.in);

    private static int MOVE;


    public Game() {
        playGame();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void playGame() {

        System.out.println("Welcome To Save The Planet");
        System.out.println("Would you like to Play? y/n");
        timer60 = Create.timerReset(timer60, T60);

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

        timer60 = Create.timerReset(timer60, T60);

        System.out.printf("1) new game%n2) restart game%n3) quit%n");
        switch (MENU.nextLine()) {
            case "1":
                System.out.println("Ok Lets Go!");
                timer60.cancel();
                playNewGame();
                break;
            case "2":
                loadGame();
                // testing
                loadedGame();
                System.out.println(players);
                break;
            case "3":
                quitOutsideOfGamePlay();
                break;
            default:
                System.out.println("that's not an option");
                timer60 = Create.timerReset(timer60, T60);
                initiateGameOptions();
        }
    }

    private static void loadedGame() {

        Stats stats = new Stats(players);
        stats.full();
        stats.elide();
        stats.end();

    }

    private static void playNewGame() {
        try {

            // Create Players
            players = Create.players();
            // Create Board/Squares
            board = Create.board();

            // light demo
            players.get(1).addOwnedSquare((FundableSquare) board.get(14));
            ((FundableSquare) board.get(14)).setOwner(players.get(1));
            ((FundableSquare) board.get(14)).setDevLevel(4);

            players.get(0).addOwnedSquare((FundableSquare) board.get(13));
            ((FundableSquare) board.get(13)).setOwner(players.get(0));
            ((FundableSquare) board.get(13)).setDevLevel(4);

            players.get(1).addOwnedSquare((FundableSquare) board.get(2));
            ((FundableSquare) board.get(2)).setOwner(players.get(1));
            ((FundableSquare) board.get(2)).setDevLevel(4);

            players.get(1).addOwnedSquare((FundableSquare) board.get(15));
            ((FundableSquare) board.get(15)).setOwner(players.get(1));
            ((FundableSquare) board.get(15)).setDevLevel(4);
            players.get(0).addOwnedSquare((FundableSquare) board.get(4));
            ((FundableSquare) board.get(4)).setOwner(players.get(1));
            ((FundableSquare) board.get(4)).setDevLevel(4);

//            players.get(2).setFunding(600);

            //proof of concept testing
            System.out.println("Game initialised: " + players.get(0));
            collectFunding(players.get(0));
            System.out.printf("%n%s moves %d places.%n", players.get(0).getName(), move());
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


            MOVE = roll();
            System.out.printf("%n%s moves %d places.%n", players.get(1).getName(), MOVE);

            //     saveGame();

            Stats stats = new Stats(players);
            stats.full();
            stats.elide();
            stats.end();
            System.exit(1);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private static void saveGame() {

        timer60 = Create.timerReset(timer60, T60);

        System.out.println("Do you wish to save the game? y/n");
        if (!MENU.nextLine().toLowerCase().contains("y"))
            playGame();

        Create.save(board, players);
        System.out.println("S:A:V:E");
    }

    @SuppressWarnings("unchecked")
    private static void loadGame() {

        timer60 = Create.timerReset(timer60, T60);

        System.out.println("Do you want to load a Saved Game? y/n");
        if (!MENU.nextLine().toLowerCase().contains("y"))
            playGame();

        try {
            HashMap<String, Object> load = Create.load();

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

    public static void trade(Player traderPlayer, Player requestedPlayer) {

        FundableSquare offeredProperty = null;
        FundableSquare requestedProperty = null;
        boolean correctInput = false;
        boolean confirmTrade = false;

        if (!traderPlayer.getOwnedSquares().isEmpty() && !requestedPlayer.getOwnedSquares().isEmpty()) {
            offeredProperty = selectProperty(traderPlayer, traderPlayer);

            requestedProperty = selectProperty(traderPlayer, requestedPlayer);

            System.out.println(requestedPlayer.getName() + ", do you want to trade " + requestedProperty.getName() + " for " + offeredProperty.getName() + "? y/n");

            while (correctInput == false) {
                switch (MENU.nextLine().toLowerCase()) {
                    case "y":
                    case "yes":
                        confirmTrade = true;
                        correctInput = true;
                        break;
                    case "n":
                    case "no":
                        confirmTrade = false;
                        correctInput = true;
                        break;
                    default:
                        System.out.println("Sorry please enter y/yes or n/no");
                }
            }

            if (confirmTrade) {
                int offeredPropertyCost = offeredProperty.getCost();
                int requestedPropertyCost = requestedProperty.getCost();

                if (offeredPropertyCost == requestedPropertyCost) {
                    swapProperties(traderPlayer, requestedPlayer, offeredProperty, requestedProperty);
                } else if (offeredPropertyCost != requestedPropertyCost) {

                    int diff = offeredPropertyCost - requestedPropertyCost;

                    if (diff > 0) {
                        if (payPropertyDifferences(requestedPlayer, traderPlayer, diff)) {
                            swapProperties(traderPlayer, requestedPlayer, offeredProperty, requestedProperty);
                        }
                    } else {
                        diff *= (-1);
                        if (payPropertyDifferences(traderPlayer, requestedPlayer, diff)) {
                            swapProperties(traderPlayer, requestedPlayer, offeredProperty, requestedProperty);
                        }

                    }

                }

            } else {
                System.out.println("Sorry, " + traderPlayer.getName() + ". " + requestedPlayer.getName() + " doesn't want to trade right now.");
            }

        } else {
            System.out.println("Sorry, you and " + requestedPlayer.getName() + " do not have enough properties to trade.");
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

    public static int move() throws InterruptedException {
        MOVE = roll();
        return MOVE;
    }

    private static int roll() throws InterruptedException {
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

    private static int randomNum() {
        int min = 1;
        int max = 6;
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    private static FundableSquare selectProperty(Player selector, Player propertyOwner) {

        int propertySelection = 0;
        FundableSquare property = null;

        System.out.println(selector.getName() + ", select the number of the property you'd like to trade.");
        for (int i = 0; i < propertyOwner.getOwnedSquares().size(); i++) {
            int position = i + 1;
            System.out.println(position + ". " + propertyOwner.getOwnedSquares().get(i).getName());
        }
        try {
            propertySelection = Integer.parseInt(MENU.nextLine());
            if (propertySelection < 1 || propertySelection > propertyOwner.getOwnedSquares().size()) {
                throw new NumberFormatException();
            } else {
                property = propertyOwner.getOwnedSquares().get(propertySelection - 1);
                return property;
            }
        } catch (NumberFormatException e) {
            System.out.println("You have entered an invalid number.");
            selectProperty(selector, propertyOwner);
        }

        return null;

    }

    private static void swapProperties(Player player1, Player player2, FundableSquare property1, FundableSquare property2) {
        player1.ownedSquares.remove(property1);
        property1.setOwner(player2);
        player2.addOwnedSquare(property1);
        player2.ownedSquares.remove(property2);
        property2.setOwner(player1);
        player1.addOwnedSquare(property2);
    }

    private static boolean payPropertyDifferences(Player payer, Player payee, int cost) {
        if (payer.getFunding() >= cost) {
            payer.setFunding(payer.getFunding() - cost);
            System.out.println(payer.getName() + "'s Balance: £" + payer.getFunding());
            payee.setFunding(payee.getFunding() + cost);
            System.out.println(payee.getName() + "'s Balance: £" + payee.getFunding());
            return true;
        } else {
            System.out.println("Sorry, " + payer.getName() + " cannot afford this trade. Trade cancelled.");
            return false;
        }
    }

}
