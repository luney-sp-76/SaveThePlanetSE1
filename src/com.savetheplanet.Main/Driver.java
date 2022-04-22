package com.savetheplanet.Main;


public class Driver {



    public static Game gameInPlay  = new Game();





    public static void main(String[] args) {
        //test compartmentalize game logic in Game object
        String response;
        response = gameInPlay.open();
        gameInPlay.playGame(response);

    }



}
