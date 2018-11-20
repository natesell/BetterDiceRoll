package com.example.android.betterdiceroll;

// TODO (1) Implement dialog box for onClickShipCaptainCrew to choose an intent to play versus a bot
// TODO (1.5) Make sure to properly pass credits between activities (using Room)
// TODO (2) Learn ROOM so we can properly bet credits and begin to build a credits shop
// TODO (3) Learn how to keep bottom bar between all activities (credits, shop button, about button)

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.betterdiceroll.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickShipCaptainCrew(View view ) {
        Intent intent = new Intent(this, ShipCaptainCrewActivity.class);
        startActivity(intent);
    }
}
