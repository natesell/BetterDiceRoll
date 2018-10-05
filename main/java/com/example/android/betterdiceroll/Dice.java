package com.example.android.betterdiceroll;

import java.io.Serializable;
import java.util.Objects;

public class Dice
        implements Serializable {
    private int index;
    private int roll;
    private boolean isActiveDice;
    private boolean isHeldDice;

    public Dice(int index) {
        this.index = index;

        roll = 0;
        isActiveDice = true;
    }

    public void holdDice() {
        if(isActiveDice) {
            isActiveDice = false;
            isHeldDice = true;
        }
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public int getRoll() {
        return roll;
    }

    public boolean isActiveDice() {
        return isActiveDice;
    }

    public boolean isHeldDice() {
        return isHeldDice;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        Dice dice = (Dice) o;
        return index == dice.index;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + index;
        return hash;
    }
}
