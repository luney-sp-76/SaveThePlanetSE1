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
     * Custom Timer/TimerTask with two phases, a warning and an action if the player remains idle.
     * They should never be running at the same time.
     *
     * @return Jaszon
     */
    static Timer timer(int t) {
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            boolean warned = false;

            public void run() {

                if (t == 60000) {
                    if (warned) {
                        System.out.println("You have been idle for 2 minutes. The Game will now exit.");
                        System.exit(0);
                    }
                    System.err.printf("\rYou have been idle for 1 minute.%nIf you are idle for another 1 minute the game will exit.%n");
                    warned = true;
                }
                if (t == 15000) {
                    if (warned) {
                        System.out.println("You have been idle for 30s, you lose your turn");

//                        Game.turnSkip();
                    }
                    System.err.printf("\rYou have been idle for 15 seconds.%nIf you are idle for another 15 seconds, something will happen%n");
                    warned = true;
                }
            }
        }, t, t);
        return timer;
    }

    // Resets the timer.
    static Timer timerReset(Timer timer, int t) {
        timer.cancel();
        timer = timer(t);
        return timer;
    }
}// class
