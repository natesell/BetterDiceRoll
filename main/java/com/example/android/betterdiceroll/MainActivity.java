package com.example.android.betterdiceroll;

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
