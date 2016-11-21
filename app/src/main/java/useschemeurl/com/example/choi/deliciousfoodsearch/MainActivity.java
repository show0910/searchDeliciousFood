package useschemeurl.com.example.choi.deliciousfoodsearch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button daumSearchButton;
    Button youtubeSearchButton;
    Button noticeBoardButton;
    Button searchEngineButton;
    Button eventButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, FrontScreenActivity.class));

        Drawable alpha = findViewById(R.id.activity_main).getBackground();
        alpha.setAlpha(220);

        daumSearchButton = (Button) findViewById(R.id.daum_search_button);
        youtubeSearchButton = (Button) findViewById(R.id.youtube_search_button);
        noticeBoardButton = (Button) findViewById(R.id.notice_board_button);
        searchEngineButton = (Button) findViewById(R.id.search_engine_button);
        eventButton = (Button) findViewById(R.id.event_button);

        //다음지도 호출
        daumSearchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent youIntent = new Intent(getApplicationContext(), SearchDaumMap.class);
                youIntent.putExtra("usage", "origin");
                startActivity(youIntent);
            }
        });

        //유튜브 검색 화면
        youtubeSearchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent youIntent = new Intent(getApplicationContext(), SearchYouTube.class);
                startActivity(youIntent);
            }
        });

        //게시판 화면
        noticeBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youIntent = new Intent(getApplicationContext(), NoticeBoard.class);
                startActivity(youIntent);
            }
        });

        //검색 엔진 화면
        searchEngineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youIntent = new Intent(getApplicationContext(), SearchEngine.class);
                startActivity(youIntent);
            }
        });

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youIntent = new Intent(getApplicationContext(), EventBoard.class);
                startActivity(youIntent);
            }
        });

    }
}
