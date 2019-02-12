package com.example.customindicator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private static final int MIN = 0;
    private static final int MAX = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Indicator indicator = findViewById(R.id.indicator);
        SeekBar seekBar = findViewById(R.id.seekBar);

        indicator.setMinValue(MIN);
        indicator.setMaxValue(MAX);
        indicator.addSector(new Indicator.Sector(0, 10, Color.RED));
        indicator.addSector(new Indicator.Sector(10, 70, Color.BLUE));
        indicator.addSector(new Indicator.Sector(70, 100, Color.GREEN));

        seekBar.setLeft(MIN);
        seekBar.setRight(MAX);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                indicator.setValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
