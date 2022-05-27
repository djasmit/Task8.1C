package com.example.task81c;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.task81c.api.YoutubePlayerAPIConfig;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoPlayerActivity extends YouTubeBaseActivity {
    private static final String TAG = "VideoPlayerActivity";
    YouTubePlayerView mYoutubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;
    private String _url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        getIntentExtras();

        mYoutubePlayerView = findViewById(R.id.VideoPlayerYoutubeVideoView);
        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "Player initialized.");
                youTubePlayer.loadVideo(_url);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e(TAG, "Player initialization failed.");
                Toast.makeText(getApplicationContext(), "Could not play video.", Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        //play video automatically
        mYoutubePlayerView.initialize(YoutubePlayerAPIConfig.getApiKey(), mOnInitializedListener);
    }

    //gets the intent and extra details from previous activities
    private void getIntentExtras() {
        Intent intent = getIntent();
        _url = intent.getStringExtra("url");
        Log.d(TAG, _url);
    }
}