package com.app.dietplan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.app.dietplan.R;
import com.app.dietplan.Util.Constant_Api;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class YoutubePlayActivity extends YouTubeFailureRecoveryActivity {

    private String id;
    YouTubePlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_youtube_play);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        YouTubePlayerView youTubeView = findViewById(R.id.youtube_view);
        youTubeView
                .initialize(
                        Constant_Api.YOUR_DEVELOPER_KEY,
                        YoutubePlayActivity.this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        // TODO Auto-generated method stub
        if (!wasRestored) {
            this.player = player;
            player.loadVideo(id);
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        // TODO Auto-generated method stub
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }
}
