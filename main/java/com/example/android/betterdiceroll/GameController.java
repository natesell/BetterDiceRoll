package com.example.android.betterdiceroll;

import android.util.Log;

import java.util.ArrayList;

public class GameController {

    public static void rollDiceIfActive(ArrayList<Dice> diceArrayList) {
        for (int i = 0; i < diceArrayList.size(); i++) {
            Dice currentDice = diceArrayList.get(i);

            if (currentDice.getIsActive()) {
                int roll = (int) (Math.random() * ((6 - 1) + 1)) + 1;
                currentDice.setRoll(roll);
                switch (currentDice.getRoll()) {
                    case 1:
                        currentDice.getImageView().setImageResource(R.drawable.dice_roll_1);
                        break;

                    case 2:
                        currentDice.getImageView().setImageResource(R.drawable.dice_roll_2);
                        break;

                    case 3:
                        currentDice.getImageView().setImageResource(R.drawable.dice_roll_3);
                        break;

                    case 4:
                        currentDice.getImageView().setImageResource(R.drawable.dice_roll_4);
                        break;

                    case 5:
                        currentDice.getImageView().setImageResource(R.drawable.dice_roll_5);
                        break;

                    case 6:
                        currentDice.getImageView().setImageResource(R.drawable.dice_roll_6);
                        break;
                }
            }
        }
    }

    public static void holdDiceForShipCaptainCrew(ArrayList<Dice> diceArrayList) {
        boolean sixIsHeld = false;
        boolean fiveIsHeld = false;
        boolean fourIsHeld = false;

        // Loop through diceArrayList, incrementing amountOfHeldDice if a dice is held
        // If dice i is held, it will check to see if its roll is a 6, 5, or a 4
        // If it is, booleans sixIsHeld, or fiveIsHeld, or fourIsHeld will be changed to true
        for (int i = 0; i < diceArrayList.size(); i++) {
            Dice currentDice = diceArrayList.get(i);
            int currentDiceRoll = currentDice.getRoll();

            if (!currentDice.getIsActive()) {
                if (currentDiceRoll == 6)
                    sixIsHeld = true;
                if (currentDiceRoll == 5)
                    fiveIsHeld = true;
                if (currentDiceRoll == 4)
                    fourIsHeld = true;
            }
        }


        // If a 6, 5, and 4 is already held, it will hold the rest of the dice
        if (sixIsHeld && fiveIsHeld && fourIsHeld) {
            for (int i = 0; i < diceArrayList.size(); i++) {
                Dice currentDice = diceArrayList.get(i);

                if (currentDice.getIsActive()) {
                    currentDice.holdDice();
                }
            }
        } // If a 6 and 5 are held, but not a 4, we check for an active 4
            // If an active 4 is found we hold the rest of the dice
            // If an active 4 is not found, we exit the if
        else if (sixIsHeld && fiveIsHeld) {
            boolean hasActiveFour = false;

            for (int i = 0; i < diceArrayList.size(); i++) {
                Dice currentDice = diceArrayList.get(i);
                int currentDiceRoll = currentDice.getRoll();

                if (currentDiceRoll == 4)
                    hasActiveFour = true;
            }

            if (hasActiveFour) {
                for (int i = 0; i < diceArrayList.size(); i++) {
                    Dice currentDice = diceArrayList.get(i);

                    if (currentDice.getIsActive()) {
                        currentDice.holdDice();
                    }
                }
            }
        } // If only a 6 is held, we loop through the array, holding ONLY the first 5 and first 4 we see,
            // If there were no 5 or 4 to hold, nothing happens
        else if (sixIsHeld) {
            for (int i = 0; i < diceArrayList.size(); i++) {
                Dice currentDice = diceArrayList.get(i);
                int currentDiceRoll = currentDice.getRoll();

                if (currentDiceRoll == 5) {
                    if (!fiveIsHeld) {
                        currentDice.holdDice();
                        fiveIsHeld = true;
                    }
                }

                if (currentDiceRoll == 4) {
                    if (!fourIsHeld && fiveIsHeld) {
                        currentDice.holdDice();
                        fourIsHeld = true;
                    }
                }

            }

            if (sixIsHeld && fourIsHeld && fiveIsHeld) {
                for (int i = 0; i < diceArrayList.size(); i++) {
                    Dice currentDice = diceArrayList.get(i);

                    if (currentDice.getIsActive()) {
                        currentDice.holdDice();
                    }
                }
            }
        } else {
            boolean sixIsActive = false;
            boolean fiveIsActive = false;

            for (int i = 0; i < diceArrayList.size(); i++) {
                Dice currentDice = diceArrayList.get(i);
                int currentDiceRoll = currentDice.getRoll();

                if (currentDiceRoll == 6)
                    sixIsActive = true;
                if (currentDiceRoll == 5)
                    fiveIsActive = true;
            }

            for (int i = 0; i < diceArrayList.size(); i++) {
                Dice currentDice = diceArrayList.get(i);
                int currentDiceRoll = currentDice.getRoll();

                if (currentDiceRoll == 6) {
                    if (!sixIsHeld) {
                        currentDice.holdDice();
                        sixIsHeld = true;
                    }
                }

                if (currentDiceRoll == 5) {
                    if ((sixIsHeld || sixIsActive) && !fiveIsHeld) {
                        currentDice.holdDice();
                        fiveIsHeld = true;
                    }
                }

                if (currentDiceRoll == 4) {
                    if (((sixIsHeld && fiveIsHeld) || (sixIsActive && fiveIsActive)) && !fourIsHeld) {
                        currentDice.holdDice();
                        fourIsHeld = true;
                    }
                }
            }

            if (sixIsHeld && fiveIsHeld && fourIsHeld) {
                for (int i = 0; i < diceArrayList.size(); i++) {
                    Dice currentDice = diceArrayList.get(i);

                    if (currentDice.getIsActive()) {
                        currentDice.holdDice();
                    }
                }
            }
        }
    }
}


