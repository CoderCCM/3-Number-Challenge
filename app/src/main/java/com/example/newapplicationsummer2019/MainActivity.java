package com.example.newapplicationsummer2019;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {
    private Button toClassicMode;
    private Button toDailyPuzzle;
    private AdView maBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        maBanner = findViewById(R.id.maBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        maBanner.loadAd(adRequest);
        toClassicMode = findViewById(R.id.toClassicMode);
        toDailyPuzzle = findViewById(R.id.toDailyPuzzle);
        toClassicMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClassicMode();
            }
        });
        /*toDailyPuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDailyPuzzle();
            }
        });*/
    }

    public void openClassicMode() {
        Intent myIntent = new Intent(this, ClassicMode.class);
        startActivity(myIntent);
    }

    public void openDailyPuzzle() {
        Intent intent = new Intent(this, DailyPuzzle.class);
        startActivity(intent);
    }
}


