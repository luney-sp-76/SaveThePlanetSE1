package com.savetheplanet.Main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class Game {
    public static Scanner MENU = new Scanner(System.in);

    private static List<Square> board = new ArrayList<>();
    private static final Players players = new Players();

    private static Stats stats;
    private static final Deck deck = new Deck();

    // for 30 second with 15 second warning
    static final int T15 = 15000;
    // 120 second with 60 seconds warning.
    static final int T60 = 60000;
    // ticktock motherfucker
    static Timer timer60 = Idle.timer(T60);
    static Timer timer15 = Idle.timer(T15);

    private static final int COLLECT = 300;

    private static int MOVE;

    public Game() {
        newGame();
        playGame();
    }

    public Game(HashMap<String, Object> load) {
        loadGame(load);
        playGame();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void playGame() {
        stats = new Stats(players.getPlayers());

        try {

            System.out.println("Game initialised: ");

            //conserve // field 2 x 2/2
//            players.getPlayer(0).addOwnedSquare((FundableSquare) board.get(2));
//            players.getPlayer(0).addOwnedSquare((FundableSquare) board.get(4));
//            players.getPlayer(0).addOwnedSquare((FundableSquare) board.get(5));
//            players.getPlayer(0).addOwnedSquare((FundableSquare) board.get(6));
//            players.getPlayer(0).addOwnedSquare((FundableSquare) board.get(8));
//            players.getPlayer(0).addOwnedSquare((FundableSquare) board.get(9));
//            players.getPlayer(0).addOwnedSquare((FundableSquare) board.get(12));
//            players.getPlayer(0).addOwnedSquare((FundableSquare) board.get(13));
//            players.getPlayer(0).addOwnedSquare((FundableSquare) board.get(14));
//            players.getPlayer(0).addOwnedSquare((FundableSquare) board.get(15));
//
//
//            setTitle(players.getPlayer(0));
//
//            System.out.println(players.getPlayer(0).getName());
//            System.out.println();
//            System.out.println();
////
//            players.getPlayer(0).ownedSquares.remove((FundableSquare) board.get(2));
//            players.getPlayer(0).ownedSquares.remove((FundableSquare) board.get(4));
//            players.getPlayer(0).ownedSquares.remove((FundableSquare) board.get(5));
//            players.getPlayer(0).ownedSquares.remove((FundableSquare) board.get(6));
//            players.getPlayer(0).ownedSquares.remove((FundableSquare) board.get(8));
//            players.getPlayer(0).ownedSquares.remove((FundableSquare) board.get(9));
//            players.getPlayer(0).ownedSquares.remove((FundableSquare) board.get(12));
//            players.getPlayer(0).ownedSquares.remove((FundableSquare) board.get(13));
////            players.getPlayer(0).ownedSquares.remove((FundableSquare) board.get(14));
////            players.getPlayer(0).ownedSquares.remove((FundableSquare) board.get(15));

//            setTitle(players.getPlayer(0));
//
//            System.out.println(players.getPlayer(0).getTitles());
//
//            setTitle(players.getPlayer(0));
//
//            System.out.println(players.getPlayer(0).getTitles());


            while (true) {
                for (Player player : players.getPlayers()) {

                    stats.full();

                    timer15 = Idle.timerReset(timer15, T15);

                    if (player.turnsTaken != -1) {

                        playersPreRollOptions(player);
                        player.setTurnsTaken(player.getTurnsTaken() + 1);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void newGame() {
        try {
            //create players
            players.create();
            // Create Board/Squares
            board = createBoard();

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * @return board - Returns List of Squares as Board.
     * Jaszon
     */
    @SuppressWarnings("unchecked")
    private static List<Square> createBoard() {

//        List<Square> board = new ArrayList<>();
//         try-with-resources to make sure the streams get closed.
//        try (Stream<String> fieldsIn = Files.lines(Paths.get("fields.txt"));
//             Stream<String> squares = Files.lines(Paths.get("squares.txt"))) {
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


//            try (
//                    FileOutputStream fos = new FileOutputStream("board.dat");
//                    ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//                oos.writeObject(board);
//
//
//            } catch (
//                    IOException e) {
//                e.printStackTrace();
//            }
        try (FileInputStream fis = new FileInputStream("board.dat"); ObjectInputStream ois = new ObjectInputStream(fis)) {
            board = (List<Square>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        }
        return board;
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    @SuppressWarnings("unchecked")
    static void loadGame(HashMap<String, Object> load) {
        timer60 = Idle.timerReset(timer60, T60);

        try {
            if (load != null) {
                board = (List<Square>) load.get("Board");
                players.setPlayers((List<Player>) (load.get("Players")));
                System.out.println("L:O:A:D");
            } else {
                SaveThePlanet.welcome();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void checkSquareOwnership(Square square, Player currentPlayer) {
        timer15 = Idle.timerReset(timer15, T15);
        if (square instanceof FundableSquare) {
            if (((FundableSquare) square).getOwner() == null) {
                System.out.printf("Would you like to purchase %s for £ %d? y/yes or n/no%n", square.getName(), ((FundableSquare) square).getCost());
                switch (MENU.nextLine().toLowerCase()) {
                    case "y":
                    case "yes":
                        purchaseSquare(currentPlayer, (FundableSquare) square);
                        break;
                    default:
                }
            } else {
                payRates(currentPlayer, (FundableSquare) square);
            }
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
     */
    private static void playersPreRollOptions(Player currentPlayer) {
        timer15 = Idle.timerReset(timer15, T15);

        System.out.println(currentPlayer.getName() + " choose your next move");
        System.out.println("---------------------");
        String option1 = "1) Roll Dice";
        String option2 = "2) Trade Area";
        String option3 = "3) Develop Area";
        String option4 = "4) Save";
        String option5 = "5) Quit";

        int count;
        boolean goodToTrade = false;
        for (Player player : players.getPlayers()) {
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
            int option = Integer.parseInt(MENU.nextLine()) + count;
            timer15 = Idle.timerReset(timer15, T15);
            switch (option) {
                case 1:
                case 4:
                case 8:
                    //roll dice
                    System.out.printf("you have chosen %s%n", option1);
                    MOVE = move();
                    System.out.printf("%n%s moves %d places.%n", currentPlayer.getName(), MOVE);
                    int location = currentPlayer.getLocation() + MOVE;


                    location = collectFundingCheck(currentPlayer, location);

                    currentPlayer.setLocation(location);
                    System.out.println(currentPlayer.getName() + " is on square " + board.get(currentPlayer.getLocation()).getName());
                    Square square = (board.get(currentPlayer.getLocation()));

//                    if (square.getField() == 0) {
//                        collectFunding(currentPlayer);
//                        break;
//                    }
                    if (square.getField() == 1) {
                        break;
                    }
                    if (square.getField() == 2) {
                        ChanceCard chance = deck.shuffle();
                        parseCard(chance, currentPlayer);
                    }

                    checkSquareOwnership(square, currentPlayer);
                    MOVE = 0;
                    break;
                case 5:
                case 9:
                    System.out.printf("you have chosen %s%n", option2);
                    //trade

                    int counter = 0;
                    List<Player> tradablePlayers = new ArrayList<>();

                    for (Player player : players.getPlayers()) {
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
                        trade(currentPlayer, traderPlayer);
                    } else {
                        System.out.println("you have no-one to trade with");
                    }
                    break;

                case 10:
                    System.out.printf("you have chosen %s%n", option3);
                    developField(currentPlayer);
                    break;
                case 2:
                case 6:
                case 11:
                    //save
                    System.out.printf("you have chosen %s%n", option4);
                    saveGame(currentPlayer);
                    break;
                case 3:
                case 7:
                case 12:
                    //quit
                    System.out.printf("you have chosen %s%n", option5);
                    Stats stats = new Stats(players.getPlayers());
                    stats.end();
                    break;
                default:
                    throw new IllegalArgumentException("that's not an option");
                    //timer = Create.timerReset(timer);
            }

        } catch (NumberFormatException e) {
            System.out.println("Oops..lets try that again..");
            playersPreRollOptions(currentPlayer);

        }
    }

    @SuppressWarnings("BusyWait")
    private static void developField(Player currentPlayer) {
        timer15 = Idle.timerReset(timer15, T15);

        String[] fields = {"Conserve", "Reduce", "Reuse", "Create"};
        int in;

        List<FundableSquare> fieldsCanDev = getCompleteFieldsList(currentPlayer);

        while (true) {
            try {
                System.out.printf("%nWhich field do you wish to develop?%n%n");
                for (int i = 0; i < fieldsCanDev.size(); i++) {
                    if (currentPlayer.getFunding() >= fieldsCanDev.get(i).getDevCost())
                        System.out.printf("%d: %-10s£%d%n", ++i, fields[fieldsCanDev.get(--i).getField() - 3], fieldsCanDev.get(i).getDevCost());
                }
                for (FundableSquare fs : fieldsCanDev) {
                    if (currentPlayer.getFunding() < fs.getDevCost()) {
                        // System.err needs this or the way it's being abused breaks.
                        Thread.sleep(25);
                        System.out.printf("%c: %-10s", '~', fields[fs.getField() - 3]);
                        System.err.printf("£%d%n", fs.getDevCost());
                        Thread.sleep(25);

                    }

                }
                System.out.println("0: Return to previous menu.");

                in = Integer.parseInt(MENU.nextLine());

//              Negative results.
                if (in == 0) {
                    playersPreRollOptions(currentPlayer);
                    break;
                }
                if (in < 0 || in > fieldsCanDev.size())
                    throw new IllegalArgumentException("");
                if (currentPlayer.getFunding() < fieldsCanDev.get(in - 1).getDevCost())
                    throw new IllegalArgumentException("");

//               Positive result.
                increaseFieldDevLevel(currentPlayer, fieldsCanDev.get(in - 1));
                break;


            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage().replaceAll(".*", "Please enter a valid option."));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void increaseFieldDevLevel(Player currentPlayer, FundableSquare square) {
        currentPlayer.getOwnedSquares().stream().filter(fs -> fs.getField() == square.getField()).forEach(fs -> fs.setDevLevel(fs.getDevLevel() + 1));
        currentPlayer.setFunding(currentPlayer.getFunding() - square.getDevCost());
    }

    private static List<FundableSquare> getCompleteFieldsList(Player currentPlayer) {
        Map<Integer, Long> getFieldSquareCountMap = getFieldSquareCountMap(currentPlayer);
        List<FundableSquare> completeFields = new ArrayList<>();

        currentPlayer.getOwnedSquares().forEach(fs -> getFieldSquareCountMap.forEach((k, v) -> {
            if (fs.getField() == k && v >= fs.getFieldSize() && completeFields.stream().noneMatch(s -> s.getField() == k)) {
                completeFields.add(fs);
            }
        }));

        return completeFields;
    }

    private static Map<Integer, Long> getFieldSquareCountMap(Player currentPlayer) {
        return currentPlayer.getOwnedSquares().stream().collect(Collectors.groupingBy(Square::getField, Collectors.counting()));
    }


    private static void saveGame(Player currentPlayer) {
        timer15.cancel();
        timer60 = Idle.timerReset(timer60, T60);

        System.out.println("Do you wish to save the game? y/n");
        if (!MENU.nextLine().toLowerCase().contains("y")) {
            timer60.cancel();
            playersPreRollOptions(currentPlayer);
            return;
        }

        SaveThePlanet.save(board, players.getPlayers());
        System.out.println("S:A:V:E");
    }

    /**
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
            checkSquareOwnership(updatedLocation, player);


        } else if (card.getAssigned() == RandomSquareAssignment.BACK) {
            int newLocation = player.getLocation() - card.getAction();

            if (newLocation < 0) {
                newLocation += 16;
            }

            player.setLocation(newLocation);
            Square updatedLocation = board.get(player.getLocation());
            checkSquareOwnership(updatedLocation, player);
        }
    }

    public static void liquidate(Player player) {
        timer15 = Idle.timerReset(timer15, T15);
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

    private static void setTitle(Player currentPlayer) {
        String[] titles = {"Custodian of Conserve", "Ruler of Reduce", "Regent of Reuse", "Commander of Create"};

        for (FundableSquare fs : getCompleteFieldsList(currentPlayer)) {
            if (!currentPlayer.getTitles().contains(titles[fs.getField() - 3])) {
                currentPlayer.addTitle((titles[fs.getField() - 3]));
                System.out.println("Player " + currentPlayer.getName() + " has earned the " + titles[fs.getField() - 3] + " title!");
            }
        }

        board.stream()
                .filter(sq -> sq instanceof FundableSquare && !currentPlayer.getOwnedSquares().contains((FundableSquare) sq))
                .forEach(sq -> {
                    if (currentPlayer.getTitles().contains(titles[sq.getField() - 3])) {
                        currentPlayer.removeTitle(titles[sq.getField() - 3]);
                        System.out.println("Player " + currentPlayer.getName() + " has lost the " + titles[sq.getField() - 3] + " title!");
                    }
                });
    }


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

    private static void playerOut(Player player) {
        stats.setPlayers(players.getPlayers());
        System.out.println(player.getName() + " is out of the game!");
        player.setTurnsTaken(-1);
        if (players.getPlayers().stream().filter(p -> p.getTurnsTaken() > -1).count() < 2) {
            Sounds.play("clap");
            stats.end();
        }
    }

    public static void trade(Player traderPlayer, Player requestedPlayer) {
        timer15.cancel();
        FundableSquare offeredProperty;
        FundableSquare requestedProperty;
        boolean correctInput = false;
        boolean confirmTrade = false;

        if (!hasNoDevelopments(traderPlayer).isEmpty() && !hasNoDevelopments(requestedPlayer).isEmpty()) {
            offeredProperty = selectProperty(traderPlayer, traderPlayer);
            requestedProperty = selectProperty(traderPlayer, requestedPlayer);

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
                }
            }

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
    }


    public static int collectFundingCheck(Player player, int location) {

        if (location >= 15) {
            Sounds.play("cash");
            player.setFunding((player.getFunding() + COLLECT));
            location -= 15;
        }

        return location;
    }

    /**
     * @return Returns the value of the dice throw to a Global MOVE Integer
     * @
     */
    public static int move() {
        Dice die = new Dice();
        MOVE = die.roll();
        //is y. You will move forward x+y places.”
        System.out.printf("You will move forward %d spaces.%n", MOVE);
        return MOVE;
    }

    private static FundableSquare selectProperty(Player selector, Player propertyOwner) {
        timer15 = Idle.timerReset(timer15, T15);
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

    private static List<FundableSquare> hasNoDevelopments(Player propertyOwner) {
        return propertyOwner.getOwnedSquares().stream().filter(fs -> fs.getDevLevel() <= 0).collect(Collectors.toList());
    }

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
