package useschemeurl.com.example.choi.deliciousfoodsearch;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import useschemeurl.com.example.choi.deliciousfoodsearch.board.IconTextItem;
import useschemeurl.com.example.choi.deliciousfoodsearch.board.IconTextListAdapter;

/**
 * Created by Choi on 2016-11-09.
 */

public class NoticeBoard extends AppCompatActivity {

    public static final int REQUEST_CODE_INSERT = 1001;
    public static final int REQUEST_CODE_UPDATE = 1002;

    ListView listView01;
    IconTextListAdapter adapter;
    Button addListButton;
    Button delListButton;
    Button updateListButton;
    int posForUpdate;
    String allTitle;
    FloatingActionButton fabMenu;
    FloatingActionButton fabMenuFirst;
    FloatingActionButton fabMenuSecond;
    FloatingActionButton fabMenuThird;
    FloatingActionButton fabMenuForth;
    Animation aniMainMenu;
    Animation aniMainMenuBak;
    boolean fabMenuValid;

    int num = 1;
    SharedPreferences mPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);

        listView01 = (ListView) findViewById(R.id.listView);
        addListButton = (Button) findViewById(R.id.addListButton);
        delListButton = (Button) findViewById(R.id.delListButton);
        updateListButton = (Button) findViewById(R.id.updateListButton);
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

        final ArrayList<Integer> list = new ArrayList<Integer>();

        mPref = getSharedPreferences("searchDeliciousNoticeBoard", MODE_PRIVATE);
        editor = mPref.edit();

        adapter = new IconTextListAdapter(this);

        allTitle = mPref.getString("title", null);

        if (allTitle != null) {
            //각각의 제목을 가져온다.
            String[] allTitle1 = allTitle.split("\\^\\&\\(");

            for (int i = 0; i < allTitle1.length; i++) {
                String title = allTitle1[i];
                String contents = null;
                String path = null;
                String otherStr = mPref.getString(title, null);
                String degree = null;
                String[] others = otherStr.split("\\^\\&\\(");

                float point = Float.parseFloat(others[0]);
                contents = others[1];

                if (others.length > 2) {
                    path = others[2];
                    degree = others[3];
                } else {
                    path = null;
                }

                adapter.addItem(new IconTextItem(title, point, contents, path, degree));
                }
            listView01.setAdapter(adapter);

        }

        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), NoticeBoardAnother.class);

                intent.putExtra("usage", "insert");
                intent.putStringArrayListExtra("titleList", adapter.getAllTitle());

                startActivityForResult(intent, REQUEST_CODE_INSERT);

            }
        });

        updateListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), NoticeBoardAnother.class);

                list.clear();

                for (int i = 0; i < adapter.getCount(); i++) {
                    if (((IconTextItem) adapter.getItem(i)).ismSelectValid()) {
                        list.add(i);
                        posForUpdate = i;
                    }
                }

                if (list.size() != 1) {
                    Toast.makeText(getApplicationContext(), "1개의 List를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                IconTextItem item = (IconTextItem) adapter.getItem(posForUpdate);

                intent.putExtra("usage", "update");
                intent.putExtra("title", item.getData(0));
                intent.putExtra("contents", item.getData(1));
                intent.putExtra("point", item.getmPoint());
                intent.putExtra("imagePath", item.getmImagePath());
                intent.putExtra("degree", item.getDegree());

                startActivityForResult(intent, REQUEST_CODE_UPDATE);
            }
        });


        delListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.clear();
                String delTitle = null;
                final String delTitleList;

                for (int i = 0; i < adapter.getCount(); i++) {
                    if (((IconTextItem) adapter.getItem(i)).ismSelectValid()) {
                        list.add(i);
                        if (delTitle == null) {
                            delTitle = ((IconTextItem) adapter.getItem(i)).getData(0);
                        } else {
                            delTitle = delTitle + "^&(" + ((IconTextItem) adapter.getItem(i)).getData(0);
                        }
                    }
                }

                delTitleList = delTitle;

                if (list.size() == 0) {
                    Toast.makeText(getApplicationContext(), "선택된 List가 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(NoticeBoard.this);
                builder.setMessage("선택된 리스트를 삭제하시겠습니까??").setCancelable(false)
                        .setPositiveButton("삭제!!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                adapter.delView(list);
                                listView01.setAdapter(adapter);

                                String[] delTitle1 = delTitleList.split("\\^\\&\\(");
                                String[] allTitle1 = allTitle.split("\\^\\&\\(");
                                allTitle = null;

                                for (int i = 0; i < allTitle1.length; i++) {
                                    int numValid = 0;
                                    for (int j = 0; j < delTitle1.length; j++) {
                                        if (allTitle1[i].equals(delTitle1[j])) {
                                            numValid = 1;
                                        }
                                    }

                                    if (numValid == 0) {
                                        if (allTitle == null) {
                                            allTitle = allTitle1[i];
                                        } else {
                                            allTitle = allTitle + "^&(" + allTitle1[i];
                                        }
                                    }
                                }

                                editor.putString("title", allTitle);

                                editor.commit();

                                for (int i = 0; i < delTitle1.length; i++) {
                                    editor.remove(delTitle1[i]);
                                }

                                editor.commit();

                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
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
                Intent youIntent = new Intent(getApplicationContext(), SearchEngine.class);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_INSERT && resultCode == RESULT_OK) {

            String title = data.getStringExtra("title");
            String contents = data.getStringExtra("contents");
            float point = data.getFloatExtra("point", 3);
            String path = data.getStringExtra("imagePath");
            String degree = data.getStringExtra("degree");
            String saveStr = null;

            if (path == null) {
                saveStr = String.valueOf(point) + "^&(" + contents;
            } else {
                saveStr = String.valueOf(point) + "^&(" + contents + "^&(" + path + "^&(" + degree;
            }

            adapter.addItem(new IconTextItem(title, point, contents, path, degree));

            listView01.setAdapter(adapter);

            //각각 리스트 뷰 제목 저장
            editor.putString(title, saveStr);

            if (allTitle == null) {
                allTitle = title;
            } else {
                allTitle = allTitle + "^&(" + title;
            }

            editor.putString("title", allTitle);

            editor.commit();

            Toast.makeText(getApplicationContext(), "Data 입력 완료", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_UPDATE && resultCode == RESULT_OK) {

            String[] curData = new String[2];

            curData[0] = data.getStringExtra("title");
            curData[1] = data.getStringExtra("contents");

            String title = data.getStringExtra("title");
            String contents = data.getStringExtra("contents");
            float point = data.getFloatExtra("point", 3);
            String path = data.getStringExtra("imagePath");
            String degree = data.getStringExtra("degree");

            String saveStr = null;

            if (path == null) {
                saveStr = String.valueOf(point) + "^&(" + contents;
            } else {
                saveStr = String.valueOf(point) + "^&(" + contents + "^&(" + path + "^&(" + degree;
            }

            ((IconTextItem) adapter.getItem(posForUpdate)).setData(curData);

            ((IconTextItem) adapter.getItem(posForUpdate)).setmPoint(point);

            ((IconTextItem) adapter.getItem(posForUpdate)).setmSelectValid(false);

            ((IconTextItem) adapter.getItem(posForUpdate)).setmImagePath(path);

            ((IconTextItem) adapter.getItem(posForUpdate)).setDegree(degree);

            listView01.setAdapter(adapter);

            editor.putString(title, saveStr);
            editor.commit();

            Toast.makeText(getApplicationContext(), "Data 수정 완료", Toast.LENGTH_SHORT).show();
        }
    }


}
