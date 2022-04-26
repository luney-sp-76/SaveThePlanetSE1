package com.savetheplanet.Main;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Game {

    private static List<Square> board = new ArrayList<>();
    private static List<Player> players = new ArrayList<>();
    // for 30 second with 15 second warning
    static final int T15 = 15000;
    // 120 second with 60 seconds warning.
    static final int T60 = 60000;

    // ticktock motherfucker
    static Timer timer60 = Create.timer(T60);

    private static final int COLLECT = 500;

    public static Scanner MENU = new Scanner(System.in);

    private static int MOVE;

    public Game() {
        newGame();
        playGame();
    }

    public Game(HashMap<String, Object> load) {
        loadGame(load);
        playGame();
    }

    public static void playGame() {
        try {

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
//
//            playerOut(players.get(1));
//            playerOut(players.get(2));
            //players.get(2).setFunding(600);
//            developField(players.get(1));


            //proof of concept testing
            System.out.println("Game initialised: ");// + players.get(0));
            collectFunding(players.get(0));

            playersPreRollOptions(players.get(0));
            //System.out.printf("%n%s moves %d places.%n", players.get(0).getName(), move());
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

            System.out.println("Real estate test");
            if (players.get(1).getOwnedSquares().isEmpty()) {
                System.out.println("Player " + players.get(1).getName() + " has no property");
                System.out.println("This is where his squares would go, IF HE HAD ANY: " + players.get(1).getOwnedSquares());
                System.out.println("Size of list of squares: " + players.get(2).getOwnedSquares().size());
            }
            playersPreRollOptions(players.get(1));


            System.out.printf("%n%s moves %d places.%n", players.get(1).getName(), move());

            saveGame();

            Stats stats = new Stats(players);
            stats.full();
            stats.elide();
            stats.end();
            System.exit(1);

        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private static void newGame() {
        try {
            // Create Players
            players = Create.players();
            // Create Board/Squares
            board = Create.board();
            playGame();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadGame(HashMap<String, Object> load) {

        timer60 = Create.timerReset(timer60, T60);

        System.out.println("Do you want to load a Saved Game? y/n");
        if (!MENU.nextLine().toLowerCase().contains("y"))
            playGame();

        try {
            board = (List<Square>) load.get("Board");
            players = (List<Player>) (load.get("Players"));
            System.out.println("L:O:A:D");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Calculates the options available to a player based on the number of squares owned
     * distinguishes between 9 variations of menu items numbers
     * 1 roll dice 2 trade 3 develop 4 quit
     * or 1 roll dice 2 trade 3 quit
     * or 1 roll dice 2 quit
     *
     * @param currentPlayer current player
     * @throws InterruptedException iae
     */
    private static void playersPreRollOptions(Player currentPlayer) throws InterruptedException {
        System.out.println("Choose your next move");
        System.out.println("---------------------");
        String option1 = "1) Roll Dice";
        String option2 = "2) Trade Area";
        String option3 = "3) Develop Area";
        String option4 = "4) Save";
        String option5 = "5) Quit";

        int count;

        if (canDevelop(currentPlayer)) {
            count = 7;
            System.out.printf("%n%s%n%s%n%s%n%s%n%s%n", option1, option2, option3, option4, option5);

        } else if (currentPlayer.getOwnedSquares().size() == 1) {
            option4 = "3) Save";
            option5 = "4) Quit";
            count = 3;
            System.out.printf("%n%s%n%s%n%s%n%s%n", option1, option2, option4, option5);

        } else {
            option4 = "2) Save";
            option5 = "3) Quit";
            count = 0;
            System.out.printf("%n%s%n%s%n%s%n", option1, option4, option5);
        }

        int option = Integer.parseInt(MENU.nextLine()) + count;
        switch (option) {
            case 1:
            case 4:
            case 8:
                //roll dice
                System.out.printf("you have chosen %s%n", option1);
                System.out.printf("%n%s moves %d places.%n", currentPlayer.getName(), move());
                break;
            case 5:
            case 9:
                System.out.printf("you have chosen %s%n", option2);
                //trade
                System.out.println("Which Player would you like to trade with?");
                int counter = 1;
                for (Player player : players) {
                    if (!currentPlayer.getName().equals(player.getName())) {
                        System.out.println(counter + ") " + player.getName());
                    }
                    counter++;
                }
                int playerNum = Integer.parseInt(MENU.nextLine()) - 1;
                trade(currentPlayer, players.get(playerNum));
                break;

            case 10:
                System.out.printf("you have chosen %s%n", option3);
//               developField(currentPlayer);
                break;
            case 2:
            case 6:
            case 11:
                //save
                System.out.printf("you have chosen %s%n", option4);
                break;
            case 3:
            case 7:
            case 12:
                //quit
                System.out.printf("you have chosen %s%n", option5);
                break;
            default:
                throw new IllegalArgumentException("that's not an option");
                //timer = Create.timerReset(timer);
        }
    }

//    private static void developField(Player currentPlayer) {
//
////        System.out.println(
//
//                currentPlayer.getOwnedSquares().forEach(fs -> {
//                            fs.getField()
//
//
//                }
//                );
//
//    }

    private static void saveGame() {

        timer60 = Create.timerReset(timer60, T60);

        System.out.println("Do you wish to save the game? y/n");
        if (!MENU.nextLine().toLowerCase().contains("y"))
            playGame();

        SaveThePlanet.save(board, players);
        System.out.println("S:A:V:E");
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
        int cost;
        FundableSquare square = player.getLowestValueSquare();
        if (square.getDevLevel() > 0) {
            square.setDevLevel(square.getDevLevel() - 1);
            cost = square.getDevCost();
            System.out.println("Undoing Development: " + square.getName() + ". You will be credited £" + cost);
        } else {
            cost = square.getCost();
            System.out.println("Seizing: " + square.getName() + ". You will be credited £" + cost);
            player.ownedSquares.remove(square);
            square.setOwner(null);
        }
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
                    playerOut(player);

                }
            }
        } else {
            System.out.println("No rates due.");
        }
    }

    private static void playerOut(Player player) {

        Stats stats = new Stats(players);
        System.out.println(player.getName() + " is out of the game!");
        player.setTurnsTaken(-1);

        if (players.stream().filter(p -> p.getTurnsTaken() > -1).count() < 2) {
            Sounds.play("clap");
            stats.end();

        }
    }


    public static void trade(Player traderPlayer, Player requestedPlayer) {

        FundableSquare offeredProperty;
        FundableSquare requestedProperty;
        boolean correctInput = false;
        boolean confirmTrade = false;

        if (!traderPlayer.getOwnedSquares().isEmpty() && !requestedPlayer.getOwnedSquares().isEmpty()) {
            offeredProperty = selectProperty(traderPlayer, traderPlayer);
            requestedProperty = selectProperty(traderPlayer, requestedPlayer);

            System.out.println(requestedPlayer.getName() + ", do you want to trade " + requestedProperty.getName() + " for " + offeredProperty.getName() + "? y/n");

            while (!correctInput) {
                switch (MENU.nextLine().toLowerCase()) {
                    case "y":
                    case "yes":
                        confirmTrade = true;
                        correctInput = true;
                        break;
                    case "n":
                    case "no":
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
                } else {
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

    /**
     * @return Returns the value of the dice throw to a Global MOVE Integer
     * @throws InterruptedException
     */
    public static int move() throws InterruptedException {
        Dice die = new Dice();
        int die1Result = randomNum();
        int die2Result = randomNum();
        die.roll();
        diceGFX(die1Result, die2Result);
        MOVE = die1Result + die2Result;
        TimeUnit.SECONDS.sleep(1);
        //is y. You will move forward x+y places.”
        System.out.printf("You will move forward %d spaces.%n", MOVE);
        return MOVE;
    }

    private static void diceGFX(int die1, int die2) throws InterruptedException {
        String e = "-------";
        String[] d1 = {"|     |", "|  *  |", "|     |"};
        String[] d2 = {"|*    |", "|     |", "|    *|"};
        String[] d3 = {"|*    |", "|  *  |", "|    *|"};
        String[] d4 = {"|*   *|", "|     |", "|*   *|"};
        String[] d5 = {"|*   *|", "|  *  |", "|*   *|"};
        String[] d6 = {"|*   *|", "|*   *|", "|*   *|"};

        String[][] diceGFX = {d1, d2, d3, d4, d5, d6};

        // clearing the console sucks, doesn't work the same way from system to system, I hate this, but it's the most stable I could find.
        String clear = String.format("%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n%n");

        for (int i = 0; i < 20; i++) {
            System.out.printf("%s %s%n", e, e);
            System.out.printf("%s %s%n", diceGFX[(randomNum()) - 1][((randomNum()) - 1) / 2], diceGFX[(randomNum()) - 1][((randomNum()) - 1) / 2]);
            System.out.printf("%s %s%n", diceGFX[(randomNum()) - 1][((randomNum()) - 1) / 2], diceGFX[(randomNum()) - 1][((randomNum()) - 1) / 2]);
            System.out.printf("%s %s%n", diceGFX[(randomNum()) - 1][((randomNum()) - 1) / 2], diceGFX[(randomNum()) - 1][((randomNum()) - 1) / 2]);
            System.out.printf("%s %s%n", e, e);
            Thread.sleep(100);
            System.out.println(clear);
        }

        // The result
        System.out.printf(" %-7s  %-7s%n", "Die 1", "Die 2");
        System.out.printf("%s  %s %n", e, e);
        for (int i = 0; i < 3; i++) {
            System.out.printf(diceGFX[die1 - 1][i] + "  " + diceGFX[die2 - 1][i] + "%n");
        }
        System.out.printf("%s  %s %n", e, e);
    }

    private static int randomNum() {
        int min = 1;
        int max = 6;
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    private static FundableSquare selectProperty(Player selector, Player propertyOwner) {

        int propertySelection;
        FundableSquare property;

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

    private static void swapProperties(Player player1, Player player2, FundableSquare property1, FundableSquare
            property2) {
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

    /**
     * checks the number of the current players owned Fundable Squares against the maximum number of Fundable Squares in each field
     *
     * @param player is the currentPlayer for this turn
     * @return true if the player owns the maximum number of Fundable Squares in any field
     */
    private static boolean canDevelop(Player player) {
        int conserve = 0;//max 2
        int create = 0;//max 2
        int reduce = 0;// max 3
        int reuse = 0;//max 3
        boolean ownsArea = false;
        //check the players owned squares to see if they can develop
        for (int i = 0; i < player.getOwnedSquares().size(); i++) {
            if (player.getOwnedSquares().get(i).getField() == 3) {
                conserve++;
                if (conserve == 2)
                    ownsArea = true;
            }
            if (player.getOwnedSquares().get(i).getField() == 4) {
                reduce++;
                if (reduce == 3)
                    ownsArea = true;
            }
            if (player.getOwnedSquares().get(i).getField() == 5) {
                reuse++;
                if (reuse == 3)
                    ownsArea = true;
            }
            if (player.getOwnedSquares().get(i).getField() == 6) {
                create++;
                if (create == 2)
                    ownsArea = true;
            }
        }
        return ownsArea;
    }
}
