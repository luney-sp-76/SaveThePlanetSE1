package com.savetheplanet.Main;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Jaszon
 */
final class Idle {

    private Idle() {
        throw new UnsupportedOperationException();
    }

    /**
     * Custom 60s Timer with two phases, a warning and an action if the player remains idle.
     *
     * @return Jaszon
     */
    static Timer menuTimer() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            boolean warned = false;

            public void run() {
                if (warned) {
                    System.out.println("You have been idle for 2 minutes. The Game will now exit.");
                    System.exit(0);
                }
                System.err.printf("\rYou have been idle for 1 minute.%nIf you are idle for another 1 minute the game will exit.%n");
                warned = true;
            }
        }, 60000, 60000);
        return timer;
    }

    /**
     * Custom Timer/TimerTask with two phases, a warning and an action if the player remains idle.
     *
     * @return Jaszon
     */
    static Timer gameplayTimer(Player player) {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            boolean warned = false;

            public void run() {
                if (warned) {
                    System.out.println("You have been idle for 30s, you lose your turn");
                    this.cancel();
                    timer.cancel();

                    Game.turnSkip(player);
                }
                System.err.printf("\rYou have been idle for 15 seconds.%n");
                warned = true;
            }
        }, 3000, 3000);
        return timer;
    }

    // Resets the timer.
    static Timer timerReset(Timer timer, Player player) {
        timer.cancel();
        timer = gameplayTimer(player);
        return timer;
    }

    // Resets the timer.
    static Timer timerReset(Timer timer) {
        timer.cancel();
        timer = menuTimer();
        return timer;
    }
}// class
