package useschemeurl.com.example.choi.deliciousfoodsearch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by Choi on 2016-11-04.
 */

public class PlayYouTube extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView ytpv;
    private YouTubePlayer ytp;
    KeySet key = new KeySet();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_play);
        ytpv = (YouTubePlayerView) findViewById(R.id.youtubeplayer);
        ytpv.initialize(key.getYouTubeServerKey(), this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        ytp = youTubePlayer;

        Intent gt = getIntent();
        ytp.loadVideo(gt.getStringExtra("id"));

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Initialization Fail", Toast.LENGTH_LONG).show();
    }
}
