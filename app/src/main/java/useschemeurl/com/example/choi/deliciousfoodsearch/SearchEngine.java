package useschemeurl.com.example.choi.deliciousfoodsearch;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Choi on 2016-11-05.
 */

public class SearchEngine extends Activity {

    Button naverSearch;
    Button daumSearch;
    EditText searchText;
    FloatingActionButton fabMenu;
    FloatingActionButton fabMenuFirst;
    FloatingActionButton fabMenuSecond;
    FloatingActionButton fabMenuThird;
    FloatingActionButton fabMenuForth;
    Animation aniMainMenu;
    Animation aniMainMenuBak;
    boolean fabMenuValid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_engine);

        naverSearch = (Button) findViewById(R.id.naver_button);
        daumSearch = (Button) findViewById(R.id.daum_button);
        searchText = (EditText) findViewById(R.id.search_text);
        fabMenu = (FloatingActionButton) findViewById(R.id.fabMenu);
        fabMenuFirst = (FloatingActionButton) findViewById(R.id.fabMenuFirst);
        fabMenuSecond = (FloatingActionButton) findViewById(R.id.fabMenuSecond);
        fabMenuThird = (FloatingActionButton) findViewById(R.id.fabMenuThird);
        fabMenuForth = (FloatingActionButton) findViewById(R.id.fabMenuForth);

        aniMainMenu = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_main_menu);
        aniMainMenuBak = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_main_menu_bak);

        fabMenuFirst.setVisibility(View.INVISIBLE);
        fabMenuSecond.setVisibility(View.INVISIBLE);
        fabMenuThird.setVisibility(View.INVISIBLE);
        fabMenuForth.setVisibility(View.INVISIBLE);

        daumSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchText.getText().toString();
                if (query == null || query.length() == 0) {
                    Toast.makeText(getApplicationContext(), "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = "daumapps://search?keyword=" + query;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

            }
        });

        naverSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String query = searchText.getText().toString();
                if (query == null || query.length() == 0) {
                    Toast.makeText(getApplicationContext(), "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                PackageManager pm = getApplicationContext().getPackageManager();
                String url = "naversearchapp://keywordsearch?mode=result&query=" + query + "&version=10";
                try {
                    String strAppPackage = "com.nhn.android.search";
                    pm.getApplicationIcon(strAppPackage).getClass();

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);

                } catch (Exception e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("http://m.search.naver.com/search.naver?query=" + query));
                    startActivity(intent);
                }
            }
        });

        fabMenuValid = true;

        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fabMenuValid) {
                    fabMenu.startAnimation(aniMainMenu);
                } else {
                    fabMenu.startAnimation(aniMainMenuBak);
                }
            }
        });

        aniMainMenu.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                fabMenuFirst.setVisibility(View.VISIBLE);
                fabMenuSecond.setVisibility(View.VISIBLE);
                fabMenuThird.setVisibility(View.VISIBLE);
                fabMenuForth.setVisibility(View.VISIBLE);

                ObjectAnimator moverFirst = ObjectAnimator.ofFloat(fabMenuFirst, "translationY", 0, -1050);
                moverFirst.setDuration(1000);
                moverFirst.start();

                ObjectAnimator moverSecond = ObjectAnimator.ofFloat(fabMenuSecond, "translationY", 0, -800);
                moverSecond.setDuration(1000);
                moverSecond.start();

                ObjectAnimator moverThird = ObjectAnimator.ofFloat(fabMenuThird, "translationY", 0, -550);
                moverThird.setDuration(1000);
                moverThird.start();

                ObjectAnimator moverForth = ObjectAnimator.ofFloat(fabMenuForth, "translationY", 0, -300);
                moverForth.setDuration(1000);
                moverForth.start();

                fabMenuValid = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        aniMainMenuBak.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                ObjectAnimator moverFirst = ObjectAnimator.ofFloat(fabMenuFirst, "translationY", 1050);
                moverFirst.setDuration(1000);
                moverFirst.start();

                ObjectAnimator moverSecond = ObjectAnimator.ofFloat(fabMenuSecond, "translationY", 800);
                moverSecond.setDuration(1000);
                moverSecond.start();

                ObjectAnimator moverThird = ObjectAnimator.ofFloat(fabMenuThird, "translationY", 550);
                moverThird.setDuration(1000);
                moverThird.start();

                ObjectAnimator moverForth = ObjectAnimator.ofFloat(fabMenuForth, "translationY", 300);
                moverForth.setDuration(1000);
                moverForth.start();

                fabMenuValid = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                fabMenuFirst.setVisibility(View.INVISIBLE);
                fabMenuSecond.setVisibility(View.INVISIBLE);
                fabMenuThird.setVisibility(View.INVISIBLE);
                fabMenuForth.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fabMenuFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youIntent = new Intent(getApplicationContext(), SearchDaumMap.class);
                youIntent.putExtra("usage", "origin");
                startActivity(youIntent);
                finish();
            }
        });

        fabMenuSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youIntent = new Intent(getApplicationContext(), SearchYouTube.class);
                startActivity(youIntent);
                finish();
            }
        });

        fabMenuThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youIntent = new Intent(getApplicationContext(), NoticeBoard.class);
                startActivity(youIntent);
                finish();
            }
        });

        fabMenuForth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youIntent = new Intent(getApplicationContext(), EventBoard.class);
                startActivity(youIntent);
                finish();
            }
        });

    }
}
