package useschemeurl.com.example.choi.deliciousfoodsearch;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_DAUM_MAP = 1001;
    public static final int REQUEST_CODE_YOUTUBE = 1002;
    public static final int REQUEST_CODE_NOTICE_BOARD = 1003;
    public static final int REQUEST_CODE_SEARCH_ENGINE = 1004;
    public static final int REQUEST_CODE_EVENT_BOARD = 1005;

    LinearLayout mainLayout;
    Button daumSearchButton;
    Button youtubeSearchButton;
    Button noticeBoardButton;
    Button searchEngineButton;
    Button eventButton;

    Animation aniFindMap;
    Animation aniYoutube;
    Animation aniNoticeBoard;
    Animation aniSearchEngine;
    Animation aniEventBoard;
    Animation aniFindMapBak;
    Animation aniYoutubeBak;
    Animation aniNoticeBoardBak;
    Animation aniSearchEngineBak;
    Animation aniEventBoardBak;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

//        로딩화면
        startActivity(new Intent(this, FrontScreenActivity.class));

        Drawable alpha = findViewById(R.id.activity_main).getBackground();
        alpha.setAlpha(220);

        mainLayout = (LinearLayout) findViewById(R.id.activity_main);
        daumSearchButton = (Button) findViewById(R.id.daum_search_button);
        youtubeSearchButton = (Button) findViewById(R.id.youtube_search_button);
        noticeBoardButton = (Button) findViewById(R.id.notice_board_button);
        searchEngineButton = (Button) findViewById(R.id.search_engine_button);
        eventButton = (Button) findViewById(R.id.event_button);

        aniFindMap = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.set_find_map);
        aniYoutube = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.set_youtube);
        aniNoticeBoard = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.set_notice_board);
        aniSearchEngine = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.set_search_engine);
        aniEventBoard = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.set_event_board);
        aniFindMapBak = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.set_find_map_bak);
        aniYoutubeBak = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.set_youtube_bak);
        aniNoticeBoardBak = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.set_notice_board_bak);
        aniSearchEngineBak = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.set_search_engine_bak);
        aniEventBoardBak = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.set_event_board_bak);


        mainLayout.getBackground().setAlpha(255);

        //다음지도 호출
        daumSearchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                daumSearchButton.startAnimation(aniFindMap);

                aniFindMap.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        daumSearchButton.setTextSize(0);
                        youtubeSearchButton.setAlpha(0.2f);
                        noticeBoardButton.setAlpha(0.2f);
                        searchEngineButton.setAlpha(0.2f);
                        eventButton.setAlpha(0.2f);
                        mainLayout.getBackground().setAlpha(100);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent youIntent = new Intent(getApplicationContext(), SearchDaumMap.class);
                        youIntent.putExtra("usage", "origin");
                        startActivityForResult(youIntent, REQUEST_CODE_DAUM_MAP);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        //유튜브 검색 화면
        youtubeSearchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                youtubeSearchButton.startAnimation(aniYoutube);

                aniYoutube.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        daumSearchButton.setAlpha(0.2f);
                        youtubeSearchButton.setTextSize(0);
                        noticeBoardButton.setAlpha(0.2f);
                        searchEngineButton.setAlpha(0.2f);
                        eventButton.setAlpha(0.2f);
                        mainLayout.getBackground().setAlpha(100);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent youIntent = new Intent(getApplicationContext(), SearchYouTube.class);
                        startActivityForResult(youIntent, REQUEST_CODE_YOUTUBE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });

        //게시판 화면
        noticeBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noticeBoardButton.startAnimation(aniNoticeBoard);

                aniNoticeBoard.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        daumSearchButton.setAlpha(0.2f);
                        youtubeSearchButton.setAlpha(0.2f);
                        noticeBoardButton.setTextSize(0);
                        searchEngineButton.setAlpha(0.2f);
                        eventButton.setAlpha(0.2f);
                        mainLayout.getBackground().setAlpha(100);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent youIntent = new Intent(getApplicationContext(), NoticeBoard.class);
                        startActivityForResult(youIntent, REQUEST_CODE_NOTICE_BOARD);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });

        //검색 엔진 화면
        searchEngineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchEngineButton.startAnimation(aniSearchEngine);

                aniSearchEngine.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        daumSearchButton.setAlpha(0.2f);
                        youtubeSearchButton.setAlpha(0.2f);
                        noticeBoardButton.setAlpha(0.2f);
                        searchEngineButton.setTextSize(0);
                        eventButton.setAlpha(0.2f);
                        mainLayout.getBackground().setAlpha(100);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent youIntent = new Intent(getApplicationContext(), SearchEngine.class);
                        startActivityForResult(youIntent, REQUEST_CODE_SEARCH_ENGINE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });

        //이벤트 게시판
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventButton.startAnimation(aniEventBoard);

                aniEventBoard.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        daumSearchButton.setAlpha(0.2f);
                        youtubeSearchButton.setAlpha(0.2f);
                        noticeBoardButton.setAlpha(0.2f);
                        searchEngineButton.setAlpha(0.2f);
                        eventButton.setTextSize(0);
                        mainLayout.getBackground().setAlpha(100);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent youIntent = new Intent(getApplicationContext(), EventBoard.class);
                        startActivityForResult(youIntent, REQUEST_CODE_EVENT_BOARD);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DAUM_MAP) {

            daumSearchButton.setTextSize(25);
            youtubeSearchButton.setAlpha(1);
            noticeBoardButton.setAlpha(1);
            searchEngineButton.setAlpha(1);
            eventButton.setAlpha(1);
            mainLayout.getBackground().setAlpha(255);
            daumSearchButton.startAnimation(aniFindMapBak);

        } else if (requestCode == REQUEST_CODE_YOUTUBE) {

            daumSearchButton.setAlpha(1);
            youtubeSearchButton.setTextSize(25);
            noticeBoardButton.setAlpha(1);
            searchEngineButton.setAlpha(1);
            eventButton.setAlpha(1);
            mainLayout.getBackground().setAlpha(255);
            youtubeSearchButton.startAnimation(aniYoutubeBak);

        } else if (requestCode == REQUEST_CODE_NOTICE_BOARD) {

            daumSearchButton.setAlpha(1);
            youtubeSearchButton.setAlpha(1);
            noticeBoardButton.setTextSize(25);
            searchEngineButton.setAlpha(1);
            eventButton.setAlpha(1);
            mainLayout.getBackground().setAlpha(255);
            noticeBoardButton.startAnimation(aniNoticeBoardBak);

        } else if (requestCode == REQUEST_CODE_SEARCH_ENGINE) {

            daumSearchButton.setAlpha(1);
            youtubeSearchButton.setAlpha(1);
            noticeBoardButton.setAlpha(1);
            searchEngineButton.setTextSize(25);
            eventButton.setAlpha(1);
            mainLayout.getBackground().setAlpha(255);
            searchEngineButton.startAnimation(aniSearchEngineBak);

        } else if (requestCode == REQUEST_CODE_EVENT_BOARD) {
            daumSearchButton.setAlpha(1);
            youtubeSearchButton.setAlpha(1);
            noticeBoardButton.setAlpha(1);
            searchEngineButton.setAlpha(1);
            eventButton.setTextSize(25);
            mainLayout.getBackground().setAlpha(255);
            eventButton.startAnimation(aniEventBoardBak);
        }

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "맛집 나와!를 종료합니다.", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

}
