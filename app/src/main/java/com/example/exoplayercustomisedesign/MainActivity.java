package com.example.exoplayercustomisedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {

    PlayerView playerView;
    ProgressBar progressBar;
    ImageView btFullScreen;
    SimpleExoPlayer simpleExoPlayer;

    boolean flag = false;
    ImageView btQuality;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    @Override
    public void onBackPressed() {
        saveProgress();
        super.onBackPressed();
    }

    void saveProgress() {
        if (simpleExoPlayer != null) {
            int sec = (int) (simpleExoPlayer.getCurrentPosition() / 1000);
            Log.e("onBackPressed", sec + "");
            //todo save video id here as well
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt("PROGRESS", sec);
            editor.putString("MY_ID", "sec");
            //todo save other things here like ID(Like: Video id)
            editor.apply();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        playerView = findViewById(R.id.player_view);
        progressBar = findViewById(R.id.progress_bar);
        btFullScreen = playerView.findViewById(R.id.bt_fullscreen);
        btQuality = playerView.findViewById(R.id.bt_quality);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //   Uri videoUrl= Uri.parse("https://www.radiantmediaplayer.com/media/big-buck-bunny-360p.mp4");
        Uri videoUrl = Uri.parse("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8");
        //Uri videoUrl= Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4pall.m3u8");
        //Uri videoUrl = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"); //todo running url 1
        //Uri videoUrl = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"); //todo running url 2
        Uri subtitleUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/findandfix-2f4a9.appspot.com/o/Despacito%20Remix%20Luis%20Fonsi%20ft.Daddy%20Yankee%20Justin%20Bieber%20Lyrics%20%5BSpanish%5D.srt?alt=media&token=63344d04-af1c-4e2c-9d15-381bf7159308");


        LoadControl loadControl = new DefaultLoadControl();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(MainActivity.this, trackSelector, loadControl);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(
                this, "ExoPlayer"));
        HlsMediaSource hlsMediaSource1 = new HlsMediaSource.Factory(dataSourceFactory)
                .setAllowChunklessPreparation(true)
                .createMediaSource(videoUrl);

        playerView.setPlayer(simpleExoPlayer);
        playerView.setKeepScreenOn(true);
        simpleExoPlayer.prepare(hlsMediaSource1);
        simpleExoPlayer.setPlayWhenReady(true);
        btQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(MainActivity.this, view);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        Toast.makeText(getApplicationContext(), "Please wait!", Toast.LENGTH_LONG).show();

                        return false;
                    }
                });


                Menu menu = popup.getMenu();
                menu.add(Menu.NONE, 0, 0, "Video quality");
                popup.show();

            }
        });

        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                if (playbackState == Player.STATE_BUFFERING) {

                    progressBar.setVisibility(View.VISIBLE);

                } else if (playbackState == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);


                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

        btFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {

                    btFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_fullscreen_24));

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    flag = false;
                } else {

                    btFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_fullscreen_exit_24));

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    flag = true;

                }

            }
        });

        getAndPlayProgress();

    }
    @Override
    protected void onResume() {
        super.onResume();
        getAndPlayProgress();
    }

    /**
     * method to play video from specified progress
     */
    private void getAndPlayProgress() {
        int pr = sharedpreferences.getInt("PROGRESS", 0);
        Log.e("pr", pr + "");
        //todo get video id here
        simpleExoPlayer.seekTo(pr * 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveProgress();
    }

    @Override
    protected void onPause() {
        super.onPause();

        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();

        saveProgress();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.getPlaybackState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}