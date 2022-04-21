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
