package com.example.android.betterdiceroll;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShipCaptainCrewActivity
        extends AppCompatActivity
        implements GameController.GameUpdateListener {
    private TextView textRollCount;
    private TextView textCurrentScore;

    private GameController gameController;

    private Map<Dice, ImageView> imageViews = new LinkedHashMap<>();

    private LinearLayout activeDiceLinearLayout;
    private LinearLayout heldDiceLinearLayout;

    SharedPreferences prefs;

    final String creditPrefsKey = "com.example.app.credits";

    int credits = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_captain_crew);

        prefs = this.getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        credits = prefs.getInt(creditPrefsKey, 1000);
        TextView creditsTextView = findViewById(R.id.mCreditAmountTextView);
        creditsTextView.setText(String.valueOf(credits));

        activeDiceLinearLayout = findViewById(R.id.mActiveDiceLinearLayout);
        heldDiceLinearLayout = findViewById(R.id.mHeldDiceLinearLayout);

        textRollCount = findViewById(R.id.mCurrentRollCountTextView);
        textCurrentScore = findViewById(R.id.mCurrentScoreTextView);

        ViewGroup root = findViewById(R.id.root);

        gameController = new GameController();

        if(savedInstanceState != null) {
            gameController.fromBundle(savedInstanceState.getBundle("gameController"));
        }

        for(Dice dice : gameController.getDices()) {
            ImageView diceView = (ImageView) LayoutInflater.from(this)
                    .inflate(R.layout.dice_item, root, false);
            imageViews.put(dice, diceView);
            onDiceRollChanged(dice, dice.getRoll()); // this line is kinda hacky but it works for our purposes

            if(dice.isHeldDice()) {
                onDiceHeld(dice);
            } else {
                activeDiceLinearLayout.addView(diceView);
            }
        }

        onCurrentScoreUpdated(gameController.getCurrentScore());  // this line is kinda hacky but it works for our purposes
        onRollCountChanged(gameController.getRollCount()); // this line is kinda hacky but it works for our purposes

        if(gameController.isGameEnded()) {
            onGameEnded(); // this line is kinda hacky but it works for our purposes
        }

        if(gameController.isRollDisabled()) {
            onRollDisabled();  // this line is kinda hacky but it works for our purposes
        }

        if(gameController.isComputerPlaying()) {
            onComputerPlaying();
        }

        gameController.setGameUpdateListener(this);
    }

    public void onClickComputerPlayButton(View view) {
        onComputerPlaying();
    }

    public void onClickAdd10CreditsButton(View view) {
        credits = credits + 10;
        prefs.edit().putInt(creditPrefsKey, credits).apply();
        TextView creditsTextView = findViewById(R.id.mCreditAmountTextView);
        creditsTextView.setText(String.valueOf(credits));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("gameController", gameController.toBundle());
    }

    public void onClickRollButton(View view) {
        gameController.roll();
    }

    public void onClickHoldButton(View view) {
        gameController.holdDiceForShipCaptainCrew();
    }

    @Override
    public void onDiceRollChanged(Dice dice, int roll) {
        switch(dice.getRoll()) {
            case 1:
                imageViews.get(dice).setImageResource(R.drawable.dice_roll_1);
                break;
            case 2:
                imageViews.get(dice).setImageResource(R.drawable.dice_roll_2);
                break;
            case 3:
                imageViews.get(dice).setImageResource(R.drawable.dice_roll_3);
                break;
            case 4:
                imageViews.get(dice).setImageResource(R.drawable.dice_roll_4);
                break;
            case 5:
                imageViews.get(dice).setImageResource(R.drawable.dice_roll_5);
                break;
            case 6:
                imageViews.get(dice).setImageResource(R.drawable.dice_roll_6);
                break;
        }
    }

    @Override
    public void onDiceHeld(Dice dice) {
        ImageView diceView = imageViews.get(dice);
        if(dice.isHeldDice()) {
            activeDiceLinearLayout.removeView(diceView);
            heldDiceLinearLayout.addView(diceView);
        }
    }

    @Override
    public void onRollCountChanged(int rollCount) {
        textRollCount.setText(String.valueOf(rollCount));
    }

    @Override
    public void onRollDisabled() {
        Button rollButton = findViewById(R.id.mRollButton);
        rollButton.setAlpha(.2f);
        rollButton.setClickable(false);
    }

    @Override
    public void onComputerPlaying() {
        onRollDisabled();
        Button holdButton = findViewById(R.id.mHoldButton);
        holdButton.setClickable(false);
        holdButton.setAlpha(0.2f);
        gameController.simulateComputerPlay();
    }

    @Override
    public void onCurrentScoreUpdated(int currentScore) {
        textCurrentScore.setText(String.valueOf(currentScore));
    }

    @Override
    public void onGameEnded() {
        TextView staticFinalScoreTextView = findViewById(R.id.mStaticCurrentOrFinalScoreTextView);
        staticFinalScoreTextView.setText("Final Score: ");
    }
}
