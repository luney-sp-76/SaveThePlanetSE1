package com.savetheplanet.Main;

import java.util.concurrent.TimeUnit;

public interface IDie {
    static int roll() throws InterruptedException {
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

    static int randomNum(){
        int min = 1;
        int max = 6;
        int result = (int)Math.floor(Math.random()*(max-min+1)+min);
        return  result;
    }
}

