package com.example.android.betterdiceroll;

import android.widget.ImageView;
import android.widget.LinearLayout;

public class Dice {

    // private boolean computerPlaying = false;
    private int roll;
    private boolean isActiveDice;
    private ImageView rollAsImageView;
    private LinearLayout activeDiceLinearLayout;
    private LinearLayout heldDiceLinearLayout;

    public Dice() {
        roll = 0;
        isActiveDice = true;
    }

    public Dice(int roll) {
        this.roll = roll;
    }

    public void holdDice() {
        if (this.getIsActive()) {
            this.getActiveDiceLinearLayout().removeView(this.getImageView());
            this.getHeldDiceLinearLayout().addView(this.getImageView());
            this.setIsActive(false);
        }
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public void setImageView(ImageView rollAsImageView) {
        this.rollAsImageView = rollAsImageView;
    }

    public ImageView getImageView() {
        return rollAsImageView;
    }

    public int getRoll() {
        return roll;
    }

    public boolean getIsActive() {
        return isActiveDice;
    }

    public void setIsActive(boolean isActive) {
        isActiveDice = isActive;
    }

    public void setActiveDiceLinearLayout(LinearLayout activeDiceLinearLayout) {
        this.activeDiceLinearLayout = activeDiceLinearLayout;
    }

    public LinearLayout getActiveDiceLinearLayout() {
        return activeDiceLinearLayout;
    }

    public void setHeldDiceLinearLayout(LinearLayout heldDiceLinearLayout) {
        this.heldDiceLinearLayout = heldDiceLinearLayout;
    }

    public LinearLayout getHeldDiceLinearLayout() {
        return heldDiceLinearLayout;
    }
}
