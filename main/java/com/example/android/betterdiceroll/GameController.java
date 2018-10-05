package com.example.android.betterdiceroll;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameController
        implements Bundleable {
    private Random random = new Random();

    private int currentScore = 0;
    private int rollCount = 3;
    private ArrayList<Dice> dices = new ArrayList<>();

    private boolean gameEnded;
    private boolean rollDisabled;
    private boolean isComputerPlaying;

    {
        for(int i = 0; i < 5; i++) {
            Dice dice = new Dice(i);
            dices.add(i, dice);
        }
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getRollCount() {
        return rollCount;
    }

    public List<Dice> getDices() {
        return Collections.unmodifiableList(dices);
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public boolean isComputerPlaying() { return isComputerPlaying; }

    @NonNull
    @Override
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("currentScore", currentScore);
        bundle.putInt("rollCount", rollCount);
        bundle.putSerializable("dices", dices);
        bundle.putBoolean("gameEnded", gameEnded);
        bundle.putBoolean("rollDisabled", rollDisabled);
        bundle.putBoolean("isComputerPlaying", isComputerPlaying);
        return bundle;
    }

    @Override
    public void fromBundle(@Nullable Bundle bundle) {
        if(bundle != null) {
            currentScore = bundle.getInt("currentScore");
            rollCount = bundle.getInt("rollCount");
            //noinspection unchecked
            dices = ((ArrayList<Dice>) bundle.getSerializable("dices"));
            gameEnded = bundle.getBoolean("gameEnded");
            rollDisabled = bundle.getBoolean("rollDisabled");
            isComputerPlaying = bundle.getBoolean("isComputerPlaying");
        }
    }

    public void roll() {
        if(rollCount > 0) {
            rollDiceIfActive();
            rollCount--;
            if(gameUpdateListener != null) {
                gameUpdateListener.onRollCountChanged(rollCount);
            }
        }

        if(rollCount == 0) {
            rollDisabled = true;
            if(gameUpdateListener != null) {
                gameUpdateListener.onRollDisabled();
                boolean areThereDiceToHold = false;
                for(Dice dice : dices) {
                    if(dice.isActiveDice()) {
                        areThereDiceToHold = true;
                        break;
                    }
                }
                if(!areThereDiceToHold) {
                    gameEnded = true;
                    gameUpdateListener.onGameEnded();
                }
            }
        }
    }

    public boolean isRollDisabled() {
        return rollDisabled;
    }

    public interface GameUpdateListener {
        void onDiceRollChanged(Dice dice, int roll);

        void onDiceHeld(Dice dice);

        void onRollCountChanged(int rollCount);

        void onRollDisabled();

        void onCurrentScoreUpdated(int currentScore);

        void onComputerPlaying();

        void onGameEnded();
    }

    private GameUpdateListener gameUpdateListener;

    public void setGameUpdateListener(GameUpdateListener gameUpdateListener) {
        this.gameUpdateListener = gameUpdateListener;
    }

    public void simulateComputerPlay() {
        boolean hasAtleastOneActiveDice = false;

        for (Dice currentDice : dices) {
            hasAtleastOneActiveDice = currentDice.isActiveDice();
        }

        while ((hasAtleastOneActiveDice == true) & (rollCount > 0)) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                //Do something here
                holdDiceForShipCaptainCrew();
                roll();
                holdDiceForShipCaptainCrew();
            }, 750);
        }
    }

    private void rollDiceIfActive() {
        for(Dice currentDice : dices) {
            if(currentDice.isActiveDice()) {
                int roll = random.nextInt(6) + 1;
                currentDice.setRoll(roll);
                if(gameUpdateListener != null) {
                    gameUpdateListener.onDiceRollChanged(currentDice, roll);
                }
            }
        }
    }

    private void holdDice(Dice dice) {
        dice.holdDice();
        if(gameUpdateListener != null) {
            gameUpdateListener.onDiceHeld(dice);
        }
    }

    public void holdDiceForShipCaptainCrew() {
        boolean sixIsHeld = false;
        boolean fiveIsHeld = false;
        boolean fourIsHeld = false;

        // Loop through diceArrayList, incrementing amountOfHeldDice if a dice is held
        // If dice i is held, it will check to see if its roll is a 6, 5, or a 4
        // If it is, booleans sixIsHeld, or fiveIsHeld, or fourIsHeld will be changed to true
        for(int i = 0; i < dices.size(); i++) {
            Dice currentDice = dices.get(i);
            int currentDiceRoll = currentDice.getRoll();

            if(!currentDice.isActiveDice()) {
                if(currentDiceRoll == 6) {
                    sixIsHeld = true;
                }
                if(currentDiceRoll == 5) {
                    fiveIsHeld = true;
                }
                if(currentDiceRoll == 4) {
                    fourIsHeld = true;
                }
            }
        }

        // If a 6, 5, and 4 is already held, it will hold the rest of the dice
        if(sixIsHeld && fiveIsHeld && fourIsHeld) {
            for(int i = 0; i < dices.size(); i++) {
                Dice currentDice = dices.get(i);

                if(currentDice.isActiveDice()) {
                    holdDice(currentDice);
                }
            }
        } // If a 6 and 5 are held, but not a 4, we check for an active 4
        // If an active 4 is found we hold the rest of the dice
        // If an active 4 is not found, we exit the if
        else if(sixIsHeld && fiveIsHeld) {
            boolean hasActiveFour = false;

            for(int i = 0; i < dices.size(); i++) {
                Dice currentDice = dices.get(i);
                int currentDiceRoll = currentDice.getRoll();

                if(currentDiceRoll == 4) {
                    hasActiveFour = true;
                }
            }

            if(hasActiveFour) {
                for(int i = 0; i < dices.size(); i++) {
                    Dice currentDice = dices.get(i);

                    if(currentDice.isActiveDice()) {
                        holdDice(currentDice);
                    }
                }
            }
        } // If only a 6 is held, we loop through the array, holding ONLY the first 5 and first 4 we see,
        // If there were no 5 or 4 to hold, nothing happens
        else if(sixIsHeld) {
            for(int i = 0; i < dices.size(); i++) {
                Dice currentDice = dices.get(i);
                int currentDiceRoll = currentDice.getRoll();

                if(currentDiceRoll == 5) {
                    if(!fiveIsHeld) {
                        holdDice(currentDice);
                        fiveIsHeld = true;
                    }
                }

                if(currentDiceRoll == 4) {
                    if(!fourIsHeld && fiveIsHeld) {
                        holdDice(currentDice);
                        fourIsHeld = true;
                    }
                }

            }

            if(sixIsHeld && fourIsHeld && fiveIsHeld) {
                for(int i = 0; i < dices.size(); i++) {
                    Dice currentDice = dices.get(i);

                    if(currentDice.isActiveDice()) {
                        holdDice(currentDice);
                    }
                }
            }
        } else {
            boolean sixIsActive = false;
            boolean fiveIsActive = false;

            for(int i = 0; i < dices.size(); i++) {
                Dice currentDice = dices.get(i);
                int currentDiceRoll = currentDice.getRoll();

                if(currentDiceRoll == 6) {
                    sixIsActive = true;
                }
                if(currentDiceRoll == 5) {
                    fiveIsActive = true;
                }
            }

            for(int i = 0; i < dices.size(); i++) {
                Dice currentDice = dices.get(i);
                int currentDiceRoll = currentDice.getRoll();

                if(currentDiceRoll == 6) {
                    if(!sixIsHeld) {
                        holdDice(currentDice);
                        sixIsHeld = true;
                    }
                }

                if(currentDiceRoll == 5) {
                    if((sixIsHeld || sixIsActive) && !fiveIsHeld) {
                        holdDice(currentDice);
                        fiveIsHeld = true;
                    }
                }

                if(currentDiceRoll == 4) {
                    if(((sixIsHeld && fiveIsHeld) || (sixIsActive && fiveIsActive)) && !fourIsHeld) {
                        holdDice(currentDice);
                        fourIsHeld = true;
                    }
                }
            }

            if(sixIsHeld && fiveIsHeld && fourIsHeld) {
                for(int i = 0; i < dices.size(); i++) {
                    Dice currentDice = dices.get(i);

                    if(currentDice.isActiveDice()) {
                        holdDice(currentDice);
                    }
                }
            }
        }

        //
        currentScore = 0;

        if(sixIsHeld && fiveIsHeld && fourIsHeld) {
            currentScore = -(6 + 5 + 4);

            for(int i = 0; i < dices.size(); i++) {
                Dice currentDice = dices.get(i);
                int currentRoll = currentDice.getRoll();

                currentScore += currentRoll;
            }
        }

        if(gameUpdateListener != null) {
            gameUpdateListener.onCurrentScoreUpdated(currentScore);
        }

        if(rollCount == 0) {
            gameEnded = true;
            rollDisabled = true;
            if(gameUpdateListener != null) {
                gameUpdateListener.onRollDisabled();
                gameUpdateListener.onGameEnded();
            }
        }
    }
}