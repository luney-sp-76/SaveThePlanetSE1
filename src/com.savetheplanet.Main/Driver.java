package com.savetheplanet.Main;


import java.util.Scanner;


public class Driver {
    public static Game gameInPlay  = new Game();


    public static void main(String[] args) {
        //test compartmentalize game logic in Game object
        String response;
        response = gameInPlay.open();
        gameInPlay.playGame(response);

    }



}
