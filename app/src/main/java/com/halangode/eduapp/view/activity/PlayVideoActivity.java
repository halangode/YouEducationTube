package com.halangode.eduapp.view.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.halangode.eduapp.R;

import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION;
import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI;

public class PlayVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    private YouTubePlayerView youTubePlayerView;
    private String videoId;
    private String duration;
    private YouTubePlayer player;
    private int milliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        youTubePlayerView.initialize(getString(R.string.youtube_api_key), this);
        videoId = getIntent().getStringExtra("videoId");
        duration = getIntent().getStringExtra("duration");
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b){
            if(videoId != null){
                if(duration == null){
                    if(player == null){
                        player = youTubePlayer;
                    }
                    youTubePlayer.cueVideo(videoId);
                    youTubePlayer.play();
                }
                else {
                    String[] time = duration.split(":");
                    milliseconds = Integer.valueOf(time[0]) * 60000 + Integer.valueOf(time[1]) * 1000;
                    youTubePlayer.cueVideo(videoId, milliseconds);
                    youTubePlayer.play();
                }

            }
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

}
