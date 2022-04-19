package com.savetheplanet.Main;
import java.util.List;

public class Driver {

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
    }
}