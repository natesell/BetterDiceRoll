package com.example.android.betterdiceroll;

// TODO (1) Implement an ever-updating rollCount
// TODO (2) Implement an ever-updating currentScore
// TODO (3) Implement final score when (rollCount == 0 && userJustHeld == true)
// TODO (4) Implement a reset button
// TODO (5) Allow for orientation change by altering onCreate, to see if Bundle is not null
// TODO (5) {ABOVE CONTINUED}, send diceArrayList into a Bundle by overriding onSaveInstanceState
// TODO (5) {ABOVE CONTINUED}, take special care to make certain the LinearLayout's attached to each Dice
// TODO (5) {ABOVE CONTINUED}, is still functional once handled in onCreate, perhaps by
// TODO (5) {ABOVE CONTINUED}, findViewById each LinearLayout and then setting each Dice to those
// TODO (6) Implement a working computer playthrough, with the option to attempt to beat it score

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class ShipCaptainCrewActivity extends AppCompatActivity implements Serializable {

    private int currentScore;
    private int rollCount;
    private ArrayList<Dice> diceArrayList;
    private TextView rollCountTextView;
    private TextView currentScoreTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_captain_crew);

        LinearLayout activeDiceLinearLayout = findViewById(R.id.mActiveDiceLinearLayout);
        LinearLayout heldDiceLinearLayout = findViewById(R.id.mHeldDiceLinearLayout);

        if(savedInstanceState == null) {
            currentScore = 0;
            rollCount = 3;
            rollCountTextView = findViewById(R.id.mCurrentRollCountTextView);
            currentScoreTextView = findViewById(R.id.mCurrentScoreTextView);


           diceArrayList = new ArrayList<Dice>();
           for (int i = 0; i < 5; i++) {
               diceArrayList.add(i, new Dice());
               diceArrayList.get(i).setImageView((ImageView) activeDiceLinearLayout.getChildAt(i));
               diceArrayList.get(i).setActiveDiceLinearLayout(activeDiceLinearLayout);
               diceArrayList.get(i).setHeldDiceLinearLayout(heldDiceLinearLayout);
           }
        }
    }

    public void onClickRollButton(View view) {
        if (rollCount > 0) {
            GameController.rollDiceIfActive(diceArrayList);
            rollCount--;
            rollCountTextView.setText(String.valueOf(rollCount));
        }

        if(rollCount == 0) {
            Button rollButton = findViewById(R.id.mRollButton);
            rollButton.setAlpha(.2f);
            rollButton.setClickable(false);
        }
    }

    public void onClickHoldButton(View view) {
        GameController.holdDiceForShipCaptainCrew(diceArrayList);
        boolean sixIsHeld = false;
        boolean fiveIsHeld = false;
        boolean fourIsHeld = false;

        for (int i = 0; i < diceArrayList.size(); i++) {
            Dice currentDice = diceArrayList.get(i);
            int currentRoll = currentDice.getRoll();

            if (currentRoll == 6)
                sixIsHeld = true;
            if (currentRoll == 5)
                fiveIsHeld = true;
            if (currentRoll == 4)
                fourIsHeld = true;
        }

        currentScore = 0;

        if (sixIsHeld && fiveIsHeld && fourIsHeld) {
            currentScore = -(6 + 5 + 4);

            for (int i = 0; i < diceArrayList.size(); i++) {
                Dice currentDice = diceArrayList.get(i);
                int currentRoll = currentDice.getRoll();

                currentScore += currentRoll;
            }
        }

        currentScoreTextView.setText(String.valueOf(currentScore));

        if (rollCount == 0) {
            TextView staticFinalScoreTextView = findViewById(R.id.mStaticCurrentOrFinalScoreTextView);
            staticFinalScoreTextView.setText("Final Score: ");
            Button rollButton = findViewById(R.id.mHoldButton);
            rollButton.setAlpha(.2f);
            rollButton.setClickable(false);
        }
    }
}
