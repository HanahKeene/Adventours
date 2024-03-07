package com.example.adventours;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

public class start_splashscreen extends AppCompatActivity {

    VideoView videoView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_splashscreen);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        videoView = findViewById(R.id.viewVideo);
        imageView = findViewById(R.id.image);

        String path = "android.resource://com.example.adventours/" + R.raw.splash;

        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                imageView.setVisibility(View.GONE);
            }
        });

        // If you want the video to play again and again
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Delay for a few seconds before starting the LoginActivity
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Start LoginActivity
                        Intent intent = new Intent(start_splashscreen.this, LoginActivity.class);
                        startActivity(intent);

                        // Finish the current activity
                        finish();
                    }
                }, 0); // Adjust the delay time as needed (3000 milliseconds = 3 seconds)
            }
        });
    }
}
