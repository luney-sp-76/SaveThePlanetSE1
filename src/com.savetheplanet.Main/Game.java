package com.savetheplanet.Main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class Game {
    // This would likely be final if it wasn't for needing to use it in tests.
    public static Scanner MENU = new Scanner(System.in);
    // Players object.
    static final Players PLAYERS = new Players();
    // Collect Funding reward
    private static final int COLLECT = 300;

    static Timer timer60 = Idle.menuTimer();


    private static List<Square> board = new ArrayList<>();
    private static Stats stats;
    private static Deck deck = new Deck();

    public Game() {
        newGame();
        playGame(0);
    }

    public Game(HashMap<String, Object> load) {
        loadGame(load);
        playGame(resumePlay());
    }

    /**
     * TEAM EFFORT
     *
     * Main game loop, runs until the game is over or saved/quit.
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public static void playGame(int nextPlayer) {

        MENU = new Scanner(System.in);
        stats = new Stats(PLAYERS.getPlayers());

        stats.full();

        while (true) {
//            for (Player player : PLAYERS.getPlayers()) {

            for (int i = 0; i < PLAYERS.getPlayers().size(); i++) {

                if (nextPlayer > 0)
                    i = nextPlayer;

                if (PLAYERS.getPlayer(i).turnsTaken != -1) {

                    playersPreRollOptions(PLAYERS.getPlayer(i));
                    PLAYERS.getPlayer(i).setTurnsTaken(PLAYERS.getPlayer(i).getTurnsTaken() + 1);

                    stats.full();

                    System.out.print(PLAYERS.getPlayer(i).getName());
                    loadingMessage("'s turn has ended!");
                    nextPlayer = 0;
                }
            }
        }
    }

    /**
     * Jaszon
     *
     * @param s The message to print.
     */
    private static void loadingMessage(String s) {
        int loadTime = 2000;
        for (char c : s.toCharArray()) {
            System.out.print(c);
            pause(loadTime / s.length());
        }
        System.out.printf("%n%n");
    }

    /**
     * Jaszon
     *
     * Triggers the players object 'create' method, and the board.
     */
    private static void newGame() {
        //create players
        PLAYERS.create();
        // Create Board/Squares
        createBoard();
    }

    /**
     * Jaszon
     *
     * Pulls the new-game data out of game.dat and sets up the board. The logic for deciphering the Deck data
     * is in Deck, and the old version of this method for the board data is in /oldDataFiles/oldCreateBoard.txt and this method. By using a serialized stream, rather than txt or csv files,
     * it stops the player altering the game files and generally looks neater.
     *
     * The relevant files are in ./oldDataFiles/
     */
    @SuppressWarnings("unchecked")
    private static void createBoard() {

//                 try-with-resources to make sure the streams get closed.
//        try (Stream<String> fieldsIn = Files.lines(Paths.get("./oldDataFiles/fields.txt"));
//             Stream<String> squares = Files.lines(Paths.get("./oldDataFiles/squares.txt"))) {
//
//            List<String> fields = fieldsIn.collect(Collectors.toList());
//
//            squares.forEach(line -> {
//                String[] arr = line.split(",");
//                String name = arr[0];
//                int field = Integer.parseInt(arr[1]);
//
//                if (field >= 3) {
//                    board.add(new FundableSquare(name, field, fields.get(field).split(",")));
//                } else {
//                    board.add(new Square(name, field));
//                }
//            });
//
//        } catch (
//                IOException e) {
//            e.printStackTrace();
//        }
//
//        HashMap<String, Object> gameDataOut = new HashMap<>();
//
//        try (FileOutputStream fos = new FileOutputStream("game.dat");
//             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//
//            gameDataOut.put("Board", board);
//            gameDataOut.put("Deck", deck);
//
//            oos.writeObject(gameDataOut);
//        } catch (
//                IOException e) {
//            e.printStackTrace();
//        }
        try (FileInputStream fis = new FileInputStream("game.dat"); ObjectInputStream ois = new ObjectInputStream(fis)) {
            HashMap<String, Object> gameData = (HashMap<String, Object>) ois.readObject();
            board = (List<Square>) gameData.get("Board");
            deck = (Deck) gameData.get("Deck");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Paul
     * (w/ assistance from Jaszon and Sophie for movement and trade options)
     *
     * Calculates the options available to a player based on the number of squares owned
     * distinguishes between 9 variations of menu items numbers
     * 1 roll dice 2 trade 3 develop 4 quit
     * or 1 roll dice 2 trade 3 quit
     * or 1 roll dice 2 quit
     * Provides Logic to call trade or allow development and offers options to quit or save
     *
     * @param currentPlayer current player
     */
    private static void playersPreRollOptions(Player currentPlayer) {
//        Timer timer = Idle.gameplayTimer(currentPlayer);

        int move;

        System.out.println(currentPlayer.getName() + " choose your next move ");
        System.out.println("---------------------");
        String option1 = "1) Roll Dice";
        String option2 = "2) Trade Area";
        String option3 = "3) Develop Area";
        String option4 = "4) Save";
        String option5 = "5) Quit";

        int count;
        boolean goodToTrade = false;
        for (Player player : PLAYERS.getPlayers()) {
            if (player != currentPlayer) {
                if (player.getOwnedSquares().size() != 0) {
                    goodToTrade = true;
                    break;
                }
            }
        }

        if (getCompleteFieldsList(currentPlayer).size() > 0) {
            count = 7;
            System.out.printf("%s%n%s%n%s%n%s%n%s%n", option1, option2, option3, option4, option5);

        } else if (currentPlayer.getOwnedSquares().size() >= 1 && goodToTrade) {
            option4 = "3) Save";
            option5 = "4) Quit";
            count = 3;
            System.out.printf("%s%n%s%n%s%n%s%n", option1, option2, option4, option5);

        } else {
            option4 = "2) Save";
            option5 = "3) Quit";
            count = 0;
            System.out.printf("%s%n%s%n%s%n", option1, option4, option5);
        }

        try {
            pause(1000);
            int option = Integer.parseInt(MENU.nextLine()) + count;
//            timer = Idle.timerReset(timer, currentPlayer);

            switch (option) {
                case 1:
                case 4:
                case 8:
                    //roll dice
//                    timer.cancel();
                    System.out.printf("you have chosen %s%n", option1);

                    move = move();


                    System.out.printf("%n%s moves %d places.%n", currentPlayer.getName(), move);
                    int location = currentPlayer.getLocation() + move;

                    location = collectFundingCheck(currentPlayer, location);

                    currentPlayer.setLocation(location);
                    System.out.println(currentPlayer.getName() + " is on square " + board.get(currentPlayer.getLocation()).getName());
                    Square square = (board.get(currentPlayer.getLocation()));

                    if (square.getField() == 1) {
                        break;
                    }

                    if (square.getField() == 2) {
                        System.out.println("Chance!");
                        ChanceCard chance = deck.shuffle();
                        loadingMessage("Shuffling...");
                        parseCard(chance, currentPlayer);
                    }
                    checkSquareOwnership(square, currentPlayer);
                    break;
                case 5:
                case 9:
//                    timer = Idle.timerReset(timer, currentPlayer);
                    //trade
                    System.out.printf("you have chosen %s%n", option2);
                    int counter = 0;
                    List<Player> tradablePlayers = new ArrayList<>();

                    for (Player player : PLAYERS.getPlayers()) {
                        if (!currentPlayer.getName().equals(player.getName())) {
                            if (!hasNoDevelopments(player).isEmpty()) {
                                counter++;
                                System.out.println(counter + ") trade with " + player.getName());
                                tradablePlayers.add(player);
                            }
                        }
                    }
                    if (counter > 0) {
                        int playerNum = Integer.parseInt(MENU.nextLine()) - 1;
                        Player traderPlayer = tradablePlayers.get(playerNum);
//                        timer.cancel();
                        trade(currentPlayer, traderPlayer);
                    } else {
                        System.out.println("you have no-one to trade with");
                    }
//                    timer.cancel();
                    break;

                case 10:
//                    timer.cancel();
                    System.out.printf("you have chosen %s%n", option3);
                    developField(currentPlayer);
                    break;
                case 2:
                case 6:
                case 11:
                    //save
//                    timer.cancel();
                    System.out.printf("you have chosen %s%n", option4);
                    saveGame(currentPlayer);
                    break;
                case 3:
                case 7:
                case 12:
                    //quit
//                    timer.cancel();
                    System.out.printf("you have chosen %s%n", option5);
                    stats.end();
                    break;
                case -1:
                    break;
                default:
//                    timer = Idle.timerReset(timer, currentPlayer);
                    System.out.println("Oops..lets try that again..");
                    playersPreRollOptions(currentPlayer);
            }
//            timer.cancel();

        } catch (Exception e) {
            System.out.println("Oops..lets try that again..");
            playersPreRollOptions(currentPlayer);

        }
    }

    /**
     * Sophie
     *
     * Method to allow two players to 'swap' ownership of properties.
     * Calls a number of sub-methods to allow player to select their own property and the property of another player.
     *
     * @param traderPlayer    tp
     * @param requestedPlayer rp
     */
    public static void trade(Player traderPlayer, Player requestedPlayer) {

//        Timer timer = Idle.gameplayTimer(traderPlayer);


        FundableSquare offeredProperty;
        FundableSquare requestedProperty;
        boolean correctInput = false;
        boolean confirmTrade = false;

        if (!hasNoDevelopments(traderPlayer).isEmpty() && !hasNoDevelopments(requestedPlayer).isEmpty()) {
//            timer.cancel();
            offeredProperty = selectProperty(traderPlayer, traderPlayer);
            requestedProperty = selectProperty(traderPlayer, requestedPlayer);

//            timer = Idle.timerReset(timer, traderPlayer);

            assert requestedProperty != null;
            assert offeredProperty != null;
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
//                        timer = Idle.timerReset(timer, traderPlayer);
                }
            }
//            Idle.timerReset(timer, traderPlayer);

            if (confirmTrade) {
                int offeredPropertyCost = offeredProperty.getCost();
                int requestedPropertyCost = requestedProperty.getCost();

                if (offeredPropertyCost == requestedPropertyCost) {
                    swapProperties(traderPlayer, requestedPlayer, offeredProperty, requestedProperty);
                } else {

                    int totalOfferedPropertyCost = offeredPropertyCost + (offeredProperty.getDevLevel() * offeredProperty.getDevCost());
                    int totalRequestedPropertyCost = requestedPropertyCost + (requestedProperty.getDevLevel() * requestedProperty.getDevCost());
                    int diff = totalOfferedPropertyCost - totalRequestedPropertyCost;

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
//        timer.cancel();
    }

    /**
     * Sophie
     *
     * Method to allow player to select a property from a player's list of owned properties for use in the 'trade' method.
     * Ensures a player is not able to select a developed property.
     *
     * @param selector      s
     * @param propertyOwner po
     * @return FundableSquare
     */
    private static FundableSquare selectProperty(Player selector, Player propertyOwner) {
        int propertySelection;
        FundableSquare property;

        List<FundableSquare> hasNoDevelopments = hasNoDevelopments(propertyOwner);
        System.out.println(selector.getName() + ", select the number of the property you'd like to trade.");

        for (int i = 0; i < hasNoDevelopments.size(); i++) {
            int position = i + 1;
            System.out.println(position + ". " + hasNoDevelopments.get(i).getName());
        }

        try {
            propertySelection = Integer.parseInt(MENU.nextLine());
            if (propertySelection < 1 || propertySelection > hasNoDevelopments.size()) {
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

    /**
     * Jaszon
     *
     * @param player player
     * @return a List of properties owned by the player which do not have developments.
     */
    private static List<FundableSquare> hasNoDevelopments(Player player) {
        return player.getOwnedSquares().stream().filter(fs -> fs.getDevLevel() <= 0).collect(Collectors.toList());
    }

    /**
     * Sophie
     *
     * Method to swap the ownership of two properties between players.
     *
     * @param player1   p1
     * @param player2   p2
     * @param property1 fs1
     * @param property2 fs2
     */
    private static void swapProperties(Player player1, Player player2, FundableSquare property1, FundableSquare
            property2) {
        player1.ownedSquares.remove(property1);
        property1.setOwner(player2);
        player2.addOwnedSquare(property1);
        player2.ownedSquares.remove(property2);
        property2.setOwner(player1);
        player1.addOwnedSquare(property2);

        setTitle(player1);
        setTitle(player2);
    }


    /**
     * Sophie
     * Method to calculate property value differences during trade.
     * Player funds are adjusted, with the owner of the lower value property having to pay the difference in value to the other player.
     *
     * @param payer payer
     * @param payee payee
     * @param cost  cost
     * @return bool
     */
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
     * Jaszon
     *
     * @param player a player who wishes to develop a field.
     *               Checks for fields eligible for development and lists them.
     *               Shows the fields that are eligible but the player cannot afford in red.
     *               Allows the player to choose a field to upgrade from the list and calls increaseFieldDevLevel();
     */
    private static void developField(Player player) {
//        Timer timer = Idle.gameplayTimer(player);

        String[] fields = {"Conserve", "Reduce", "Reuse", "Create"};
        int in;

        List<FundableSquare> completeFieldsList = getCompleteFieldsList(player);

        while (true) {
            try {
                System.out.printf("%nWhich field do you wish to develop?%n%n");
                for (int i = 0; i < completeFieldsList.size(); i++) {
                    if (player.getFunding() >= completeFieldsList.get(i).getDevCost())
                        System.out.printf("%d: %-10s£%d%n", ++i, fields[completeFieldsList.get(--i).getField() - 3], completeFieldsList.get(i).getDevCost());
                }
                for (FundableSquare fs : completeFieldsList) {
                    if (player.getFunding() < fs.getDevCost()) {
                        // System.err needs this, or the way it's being abused breaks.
                        pause(25);
                        System.out.printf("%c: %-10s", '~', fields[fs.getField() - 3]);
                        System.err.printf("£%d%n", fs.getDevCost());
                        pause(25);
                    }
                }
                System.out.println("0: Return to previous menu.");

//                timer = Idle.timerReset(timer, player);

                in = Integer.parseInt(MENU.nextLine());

//              Negative results.
                if (in == 0) {
                    playersPreRollOptions(player);
//                    timer.cancel();
                    break;
                }
                if (in < 0 || in > completeFieldsList.size())
                    throw new IllegalArgumentException("");
                if (player.getFunding() < completeFieldsList.get(in - 1).getDevCost())
                    throw new IllegalArgumentException("");

//              Positive result.
//                timer.cancel();
                increaseFieldDevLevel(player, completeFieldsList.get(in - 1));
                break;

            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage().replaceAll(".*", "Please enter a valid option."));
//                timer = Idle.timerReset(timer, player);

            }

        }
    }

    /**
     * Jaszon
     *
     * @param player the player whose ownership of squares within each field is required.
     * @return a list of the fields that the player has full ownership of.
     */
    private static List<FundableSquare> getCompleteFieldsList(Player player) {
        Map<Integer, Long> getFieldSquareCountMap = getFieldSquareCountMap(player);
        List<FundableSquare> completeFields = new ArrayList<>();

        player.getOwnedSquares().forEach(fs -> getFieldSquareCountMap.forEach((k, v) -> {
            if (fs.getField() == k && v >= fs.getFieldSize() && completeFields.stream().noneMatch(s -> s.getField() == k)) {
                completeFields.add(fs);
            }
        }));
        return completeFields;
    }

    /**
     * Jaszon
     *
     * @param player the player whose ownership of squares within each field is required.
     * @return A map of the Field# (::getField/.getField()) and the count of same.
     */
    private static Map<Integer, Long> getFieldSquareCountMap(Player player) {
        return player.getOwnedSquares().stream().collect(Collectors.groupingBy(Square::getField, Collectors.counting()));
    }

    /**
     * Jaszon
     *
     * @param player a player whose has chosen to develop their field.
     * @param square a square of the field they are upgraded.
     *               Uses the one square to gather the other squares of the same field and increases their dev level by 1.
     *               Charges the player the cost of said development.
     */
    private static void increaseFieldDevLevel(Player player, FundableSquare square) {
        player.getOwnedSquares().stream().filter(fs -> fs.getField() == square.getField()).forEach(fs -> fs.setDevLevel(fs.getDevLevel() + 1));
        player.setFunding(player.getFunding() - square.getDevCost());
    }

    /**
     * Paul
     *
     * @return Returns the value of the dice throw. Player moves the AVERAGE of the two dice, rounded up.
     * @
     */
    public static int move() {
        Dice die = new Dice();
        int move = die.roll();
        //is y. You will move forward x+y places.”
        System.out.printf("You will move forward the average amount of %d spaces.%n", move);
        return move;
    }

    /**
     * Jaszon and Paul and Sophie
     *
     * @param player   The Player
     * @param location Where They Are At
     * @return Where They Are Still At- but fixed for looping the board.
     * Also calls the Sounds class to play a sound effect
     */
    public static int collectFundingCheck(Player player, int location) {

        if (location >= 16 || location == 0) {
            Sounds.play("cash");
            player.setFunding((player.getFunding() + COLLECT));
            location -= 16;
        }
        return location;
    }

    /**
     * Andrew
     * Paul and Sophie
     *
     * @param card   - Chance Card player has chosen
     * @param player - Current actor
     */
    public static void parseCard(ChanceCard card, Player player) {

        System.out.println("CHANCE CARD");
        card.fullDetails(card);

        if (card.getAssigned() == RandomSquareAssignment.PAY) {
            int pay = card.getAction();

            player.setFunding(player.getFunding() - pay);
            while (player.getFunding() < 0 && player.getTurnsTaken() != -1) {
                liquidate(player);
            }

        } else if (card.getAssigned() == RandomSquareAssignment.RECEIVE) {
            player.setFunding(player.getFunding() + card.getAction());
        } else if (card.getAssigned() == RandomSquareAssignment.COLLECT_FUNDING) {
            player.setFunding(player.getFunding() + COLLECT);
        } else if (card.getAssigned() == RandomSquareAssignment.FORWARD) {
            int newLocation = player.getLocation() + card.getAction();

            newLocation = collectFundingCheck(player, newLocation);

            player.setLocation(newLocation);
            Square updatedLocation = board.get(player.getLocation());
            System.out.println(player.getName() + " is on square " + board.get(player.getLocation()).getName());
            checkSquareOwnership(updatedLocation, player);

        } else if (card.getAssigned() == RandomSquareAssignment.BACK) {
            int newLocation = player.getLocation() - card.getAction();

            if (newLocation < 0) {
                newLocation += 16;
            }

            player.setLocation(newLocation);
            Square updatedLocation = board.get(player.getLocation());
            System.out.println(player.getName() + " is on square " + board.get(player.getLocation()).getName());
            checkSquareOwnership(updatedLocation, player);
        }
    }

    /**
     * Paul Sophie
     *
     * @param square        s
     * @param currentPlayer c
     */
    private static void checkSquareOwnership(Square square, Player currentPlayer) {
//        Timer timer = Idle.gameplayTimer(currentPlayer);

        if (square instanceof FundableSquare) {
            if (((FundableSquare) square).getOwner() == null) {
                System.out.printf("Would you like to purchase %s for £ %d? y/yes or n/no%n", square.getName(), ((FundableSquare) square).getCost());
                switch (MENU.nextLine().toLowerCase()) {
                    case "y":
                    case "yes":
//                        timer.cancel();
                        purchaseSquare(currentPlayer, (FundableSquare) square);
                        break;
                    default:
                }
            } else {
//                timer.cancel();
                payRates(currentPlayer, (FundableSquare) square);
            }
        }
    }


    /**
     * Sophie
     * Method to allow a player to purchase an unowned square for its listed cost.
     *
     * @param player p
     * @param square s
     */
    public static void purchaseSquare(Player player, FundableSquare square) {
        if (square.getOwner() == null) {
            if (player.getFunding() >= square.getCost()) {
                square.setOwner(player);
                setTitle(player);
                player.setFunding(player.getFunding() - square.getCost());
                player.addOwnedSquare(square);
                System.out.println("Congratulations! You are the proud owner of " + square.getName() + ".");
                setTitle(player);
            } else {
                System.out.println("Sorry, you don't have enough money to purchase this.");
            }
        } else {
            System.out.println("Sorry! " + square.getName() + " is not for sale.");
        }
    }

    /**
     * Sophie
     *
     * Method to reduce a player's balance when landing on a square owned by another player.
     * This method triggers the 'liquidate' method if a player has insufficient funds.
     *
     * @param player p
     * @param square s
     */
    public static void payRates(Player player, FundableSquare square) {

        if (square.getOwner() != null && square.getOwner() != player) {
            Player owner = square.getOwner();
            int rates = square.getRatesBill();
            System.out.println("Rates Bill: £" + rates);
            if (player.getFunding() >= rates) {
                player.setFunding(player.getFunding() - rates);
                owner.setFunding(owner.getFunding() + rates);
                System.out.println("You've paid £" + rates);
            } else {
                System.out.println("You can't pay your rates bill! Time to liquidate your property.");
                if (!player.getOwnedSquares().isEmpty()) {
                    liquidate(player);
                    payRates(player, square);
                }
            }
        } else {
            System.out.println("No rates due.");
        }
    }

    /**
     * Sophie
     *
     * Method to remove a player's properties when a bill cannot be paid.
     * Properties are removed based on their development level, with undeveloped and uncontrolled areas being removed first.
     * The player is reimbursed for the associated cost of the lost property or development.
     *
     * @param player p
     */
    public static void liquidate(Player player) {

        int cost = 0;
        FundableSquare square = player.getLowestValueSquare();

        if (player.getOwnedSquares().isEmpty()) {
            System.out.println("You can't pay your bill and have no property! It looks like someone else will have to save the world...");
            playerOut(player);
        } else if (square.getDevLevel() > 0) {
            square.setDevLevel(square.getDevLevel() - 1);
            cost = square.getDevCost();
            System.out.println("Undoing Development: " + square.getName() + ". You will be credited £" + cost);
        } else {
            cost = square.getCost();
            System.out.println("Seizing: " + square.getName() + ". You will be credited £" + cost);
            player.ownedSquares.remove(square);
            square.setOwner(null);
            setTitle(player);
        }
        player.setFunding(player.getFunding() + cost);
        System.out.println("Balance: £" + player.getFunding());
    }

    /**
     * Jaszon
     *
     * @param player - the player who has just gone through a property ownership change of any type.
     *               It checks to see if the player owns all the squares in any field and awards them the
     *               relevant title.
     *               It also checks if they have a title that they no longer qualify for and, if so, removes it.
     */
    private static void setTitle(Player player) {
        String[] titles = {"Custodian of Conserve", "Ruler of Reduce", "Regent of Reuse", "Commander of Create"};

        // woohoo
        for (FundableSquare fs : getCompleteFieldsList(player)) {
            if (!player.getTitles().contains(titles[fs.getField() - 3])) {
                Sounds.play("woohoo");
                System.out.println("Player " + player.getName() + " has earned the " + titles[fs.getField() - 3] + " title!");
                player.addTitle((titles[fs.getField() - 3]));
            }
        }

        // doh
        board.stream().filter(sq -> sq instanceof FundableSquare && !player.getOwnedSquares().contains((FundableSquare) sq)).forEach(sq -> {
            if (player.getTitles().contains(titles[sq.getField() - 3])) {
                player.removeTitle(titles[sq.getField() - 3]);
                Sounds.play("doh");
                System.out.println("Player " + player.getName() + " has lost the " + titles[sq.getField() - 3] + " title!");
            }
        });
    }

    /**
     * Jaszon
     *
     * @param player The Player Who Just Lost.
     *               Sets the player's turns as -1, which is our shorthand for out. If there is only 1 player left the game ends
     *               by calling stats.end().
     */
    private static void playerOut(Player player) {
        System.out.println(player.getName() + " is out of the game!");
        player.setTurnsTaken(-1);
        if (PLAYERS.getPlayers().stream().filter(p -> p.getTurnsTaken() > -1).count() < 2) {
            stats.end();
        }
    }

    static void turnSkip(Player player) {

        player.setTurnsTaken(player.getTurnsTaken() + 1);
        playGame(resumePlay());
    }


    static int resumePlay() {
        System.out.println("Resume?");

        int turn = PLAYERS.getPlayer(0).getTurnsTaken();

        for (Player p : PLAYERS.getPlayers()) {
            if (p.getTurnsTaken() < turn) {
                System.out.println(p.getName() + " "+  p.getTurnsTaken());
                return PLAYERS.getPlayers().indexOf(p);
            }
        }
        return 0;
    }


    /**
     * Jaszon
     *
     * @param length length of time to pause.
     */
    static void pause(int length) {
        try {
            Thread.sleep(length);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Jaszon
     *
     * @param load the hashmap generated in SaveThePlanet.load();
     *             if the data comes back not-null it puts the game-state back to when where it was then game was saved
     *             including which player goes next and the specific deck that was last shuffled.
     *             Otherwise, it bumps to player to the welcome screen, as they have chosen not to load a file.
     */
    @SuppressWarnings("unchecked")
    static void loadGame(HashMap<String, Object> load) {

        timer60 = Idle.timerReset(timer60);

        if (load != null) {
            board = (List<Square>) load.get("Board");
            deck = (Deck) load.get("Deck");
            PLAYERS.setPlayers((List<Player>) (load.get("Players")));
            System.out.println("L:O:A:D");
            timer60.cancel();
        } else {
            SaveThePlanet.welcome();
        }
    }

    /**
     * Jaszon
     *
     * @param player - the player who chose to save.
     *               Confirms if they want to save or not.
     *               if they do, it sends the current Board, Deck and Player data to SaveThePlanet.save()
     */
    private static void saveGame(Player player) {
        timer60 = Idle.timerReset(timer60);

        System.out.println("Do you wish to save the game? y/n");

        if (!MENU.nextLine().toLowerCase().contains("y")) {
            timer60.cancel();

            playersPreRollOptions(player);

        }

        SaveThePlanet.save(board, PLAYERS.getPlayers(), deck);
        System.out.println("S:A:V:E");
        timer60.cancel();
        playersPreRollOptions(player);
    }

} //class
