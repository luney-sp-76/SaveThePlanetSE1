package com.savetheplanet.Main;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Driver {

    //Player collects £500 when passing "Collect Funding" square
    private static final int COLLECT = 500;

    public static void main(String[] args) {
        try {

            // Create Players
            List<Player> players = CreatePlayers.createPlayers();
            System.out.println(players);

            // Create Board/Squares
            List<Square> board = CreateBoard.createBoard();
            System.out.println(board);

            // light demo
            System.out.println(board.get(2));
            players.get(1).addOwnedSquare((FundableSquare) board.get(2));
            ((FundableSquare) board.get(2)).setStatus("Owned by: " + players.get(1).getName());
            System.out.println(players.get(1).getOwnedSquares());
            ((FundableSquare) board.get(2)).setDevLevel(2);
            System.out.println(players.get(1).getOwnedSquares());


        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        //proof of concept testing
        Player p1 = new Player("abc");
		System.out.println("Game initialised: "+p1);
		p1 = collectFunding(p1);
		System.out.println("Player passes GO: £"+p1.getFunding());
        //read all Chance Cards
        List<ChanceCard> mainDeck = readData();
        //shuffle chance cards
        ChanceCard chance = shuffleDeck(mainDeck);
        System.out.println("Shuffling...\n");
        //trace statements
		parseCard(chance, p1);
		//chance.fullDetails();
		System.out.println("Proof of concept: "+chance.getAssigned());
		chance.fullDetails(chance);
		System.out.println("Player post card: £"+p1.getFunding());


    }

    /**
	 * 
	 * @param card - Chance Card player has chosen
	 * @param player - Current actor
	 * @return
	 */
	public static Player parseCard(ChanceCard card, Player player) {
		
		if(card.getAssigned() == RandomSquareAssignment.PAY){
			int pay = card.getAction();
			player.setFunding(player.getFunding()-pay);
			return player;
		}
		
		if(card.getAssigned() == RandomSquareAssignment.RECEIVE){
			player.setFunding(player.getFunding()+card.getAction());
			return player;
		}
		
		if(card.getAssigned() == RandomSquareAssignment.COLLECT_FUNDING){
			player.setFunding(player.getFunding()+COLLECT);
			return player;
		}
		
		else {
			return player;	
		}				
	}
		
	/**
	 * shuffles deck and returns the top card (first in List)
	 * @param deck
	 * @return
	 */
	private static ChanceCard shuffleDeck(List<ChanceCard> deck) {
		Collections.shuffle(deck);
		return deck.get(0);
	}
	
	public static Player collectFunding(Player player) {
		player.setFunding((player.getFunding()+COLLECT));
		return player;
	}

	public static void purchaseSquare(Player player, FundableSquare square){
		if(square.getOwner() == null){
			if(player.getFunding() >= square.getCost()){
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

	public static void liquidate(Player player){
		FundableSquare square = player.getLowestValueSquare();
		int cost = square.getCost();
		System.out.println("Seizing: " + square.getName() + ". You will be credited £" + cost);
		player.ownedSquares.remove(square);
		square.setOwner(null);
		player.setFunding(player.getFunding() + cost);
		System.out.println("Balance: £" + player.getFunding());
	}

	public static void payRates(Player player, FundableSquare square){
		if(square.getOwner() != null && square.getOwner() != player){
			Player owner = square.getOwner();
			int rates = square.getRatesBill();
			if(player.getFunding() >= rates) {
				player.setFunding(player.getFunding() - rates);
				owner.setFunding(owner.getFunding() + rates);
			} else {
				System.out.println("You can't pay your rates bill! Time to liquidate your property.");

				if(!player.getOwnedSquares().isEmpty()) {
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
	 * Reads in the data from the provided csv and returns a list
	 */
	public static List<ChanceCard> readData() {

		List<ChanceCard> listFromFile = new ArrayList<ChanceCard>();
		File file = new File("randomSquareAssignment.csv"); 
		try (FileReader fr = new FileReader(file); BufferedReader reader = new BufferedReader(fr);) {

			String line = reader.readLine(); 
			line = reader.readLine();

			while (line != null) {

				String[] parts = line.split(",");

				try {
					if(parts.length==1) {
					RandomSquareAssignment random = RandomSquareAssignment.valueOf(parts[0].toUpperCase());
					ChanceCard card = new ChanceCard(random);
					listFromFile.add(card);
					} else {
						RandomSquareAssignment random = RandomSquareAssignment.valueOf(parts[0].toUpperCase());
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
			System.out.println("Exception occured");
			System.out.println(listFromFile.size() + " lines read successfully");
			System.out.println(e.getMessage());
		}
		System.out.println(listFromFile.size() + " lines read successfully");
		return listFromFile;
	}
}
