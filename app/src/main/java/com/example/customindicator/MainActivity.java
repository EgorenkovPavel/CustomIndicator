package com.example.customindicator;

import androidx.appcompat.app.AppCompatActivity;

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
