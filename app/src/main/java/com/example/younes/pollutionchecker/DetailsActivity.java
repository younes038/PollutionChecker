package com.example.younes.pollutionchecker;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.younes.pollutionchecker.R;

/**
 * Created by younes on 02/04/2017.
 */

public class DetailsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        int aqi = getIntent().getIntExtra("aqi", 0);

        if (aqi > 100) {
            final MediaPlayer dangerSound = MediaPlayer.create(this, R.raw.alarm);
            dangerSound.start();
        }
    }
}
