package com.savetheplanet.Main;

import java.util.Collections;
import java.util.List;


public class Driver {

    //Player collects £500 when passing "Collect Funding" square
    private static final int COLLECT = 500;

    public static void main(String[] args) {
        try {
            // Create Players
            List<Player> players = Create.players();
            System.out.println(players);

            // Create Board/Squares
            List<Square> board = Create.board();
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
        System.out.println("Game initialised: " + p1);
        p1 = collectFunding(p1);
        System.out.println("Player passes GO: £" + p1.getFunding());
        //read all Chance Cards
        List<ChanceCard> mainDeck = Create.deck();
        //shuffle chance cards
        ChanceCard chance = shuffleDeck(mainDeck);
        System.out.println("Shuffling...\n");
        //trace statements

        parseCard(chance, p1);
        //chance.fullDetails();
        System.out.println("Proof of concept: " + chance.getAssigned());
        chance.fullDetails(chance);
        System.out.println("Player post card: £" + p1.getFunding());

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
     * shuffles deck and returns the top card (first in List)
     *
     * @param deck
     * @return
     */
    private static ChanceCard shuffleDeck(List<ChanceCard> deck) {
        Collections.shuffle(deck);
        return deck.get(0);
    }

    public static Player collectFunding(Player player) {
        player.setFunding((player.getFunding() + COLLECT));
        return player;
    }

}
