package com.example.younes.pollutionchecker;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.younes.pollutionchecker.R;

/**
 * Created by younes on 02/04/2017.
 */

public class DetailsActivity extends AppCompatActivity{
    ImageButton send;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        getMenuInflater().inflate(R.menu.details_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle click on item of action bar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                // User chose the "Send" action
                Intent email = new Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto:"));
                email.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.mail_subject));
                email.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.mail_message));
                try {
                    startActivity(Intent.createChooser(email, null));
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(this, getResources().getString(R.string.mail_error), Toast.LENGTH_SHORT).show();
                }

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
