package useschemeurl.com.example.choi.deliciousfoodsearch;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * Created by Choi on 2016-11-04.
 */

public class SearchYouTube extends Activity {

    static DrawableManager DM = new DrawableManager();
    private EditText et;
    AsyncTask<?, ?, ?> searchTask;
    ArrayList<SearchYoutubeData> ydata = new ArrayList<SearchYoutubeData>();
    ComFunction cf = new ComFunction();
    KeySet key = new KeySet();
    FloatingActionButton fabMenu;
    FloatingActionButton fabMenuFirst;
    FloatingActionButton fabMenuSecond;
    FloatingActionButton fabMenuThird;
    FloatingActionButton fabMenuForth;
    Animation aniMainMenu;
    Animation aniMainMenuBak;
    boolean fabMenuValid;

    String prevPageToken;
    String nextPageToken;
    String searchText;
    int buttonValid = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_main);

        et = (EditText) findViewById(R.id.eturl);

        Button search = (Button) findViewById(R.id.search);
        Button prevPage = (Button) findViewById(R.id.prevPage);
        Button nextPage = (Button) findViewById(R.id.nextPage);
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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = et.getText().toString();
                if (query == null || query.length() == 0) {
                    Toast.makeText(getApplicationContext(), "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                hideSoftKeyboard();
                buttonValid = 0;
                searchText = cf.delSpace(et.getText().toString());
                searchTask = new searchTask().execute();

            }
        });

        prevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prevPageToken == " ") {
                    Toast.makeText(getApplicationContext(), "첫번째 페이지 입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                buttonValid = 1;
                searchTask = new searchTask().execute();

            }
        });

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nextPageToken == " ") {
                    Toast.makeText(getApplicationContext(), "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                buttonValid = 2;
                searchTask = new searchTask().execute();
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
                Intent youIntent = new Intent(getApplicationContext(), NoticeBoard.class);
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

        initData();

    } //end onCreate Class

    //키보드 숨기기
    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    private class searchTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                paringJsonData(getUtube());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            ListView searchlist = (ListView) findViewById(R.id.searchlist);

            StoreListAdapter mAdapter = new StoreListAdapter(
                    SearchYouTube.this, R.layout.activity_youtube_list_view, ydata);

            searchlist.setAdapter(mAdapter);

        }
    }

    public JSONObject getUtube() {

        String searchUrl = "";

        if (buttonValid == 0) {
            searchUrl = "https://www.googleapis.com/youtube/v3/search?"
                    + "part=snippet&maxResults=50&q=" + searchText
                    + "&key=" + key.getYouTubeServerKey();
        } else if (buttonValid == 1) {
            searchUrl = "https://www.googleapis.com/youtube/v3/search?"
                    + "part=snippet&maxResults=50&q=" + searchText
                    + "&key=" + key.getYouTubeServerKey() + "&pageToken=" + prevPageToken;
        } else if (buttonValid == 2) {
            searchUrl = "https://www.googleapis.com/youtube/v3/search?"
                    + "part=snippet&maxResults=50&q=" + searchText
                    + "&key=" + key.getYouTubeServerKey() + "&pageToken=" + nextPageToken;
        }

        HttpGet httpGet = new HttpGet(searchUrl);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private void paringJsonData(JSONObject jsonObject) throws JSONException {
        ydata.clear();

        if (jsonObject.isNull("nextPageToken")) {
            nextPageToken = " ";
        } else {
            nextPageToken = jsonObject.getString("nextPageToken");
        }

        if (jsonObject.isNull("prevPageToken")) {
            prevPageToken = " ";
        } else {
            prevPageToken = jsonObject.getString("prevPageToken");
        }

        JSONArray contacts = jsonObject.getJSONArray("items");
        for (int i = 0; i < contacts.length(); i++) {
            JSONObject c = contacts.getJSONObject(i);

            if (c.getJSONObject("id").isNull("videoId")) {
                continue;
            }

            String vodid = c.getJSONObject("id").getString("videoId");

            String title = c.getJSONObject("snippet").getString("title");
            String desc = c.getJSONObject("snippet").getString("description");
            try {
                title = new String(title.getBytes("8859_1"), "utf-8");
                desc = new String(desc.getBytes("8859_1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String date = c.getJSONObject("snippet").getString("publishedAt")
                    .substring(0, 10);
            String imgUrl = c.getJSONObject("snippet").getJSONObject("thumbnails")
                    .getJSONObject("default").getString("url");

            ydata.add(new SearchYoutubeData(vodid, title, imgUrl, date, desc));
        }

    }

    String vodid = "";

    public class StoreListAdapter extends ArrayAdapter<SearchYoutubeData> {
        private ArrayList<SearchYoutubeData> items;
        SearchYoutubeData fInfo;

        public StoreListAdapter(Context context, int textViewResourseId,
                                ArrayList<SearchYoutubeData> items) {
            super(context, textViewResourseId, items);
            this.items = items;
        }

        // listview
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            fInfo = items.get(position);

            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = vi.inflate(R.layout.activity_youtube_list_view, null);
            ImageView img = (ImageView) v.findViewById(R.id.img);

            String url = fInfo.getUrl();

            String sUrl = "";
            String eUrl = "";
            sUrl = url.substring(0, url.lastIndexOf("/") + 1);
            eUrl = url.substring(url.lastIndexOf("/") + 1, url.length());
            try {
                eUrl = URLEncoder.encode(eUrl, "EUC-KR").replace("+", "%20");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String new_url = sUrl + eUrl;

            DM.fetchDrawableOnThread(new_url, img);

            v.setTag(position);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer) v.getTag();

                    Intent intent = new Intent(SearchYouTube.this,
                            PlayYouTube.class);
                    intent.putExtra("id", items.get(pos).getVideoId());
                    startActivity(intent);
                }
            });

            ((TextView) v.findViewById(R.id.title)).setText(fInfo.getTitle());
            ((TextView) v.findViewById(R.id.date)).setText(fInfo.getPublishedAt());
            ((TextView) v.findViewById(R.id.desc)).setText(fInfo.getDescription());

            return v;
        }
    }

    private void initData() {
        Uri data = getIntent().getData();
        if (data != null) {
            String query = data.getQueryParameter("query");
            et.setText(query);
        }
    }
}
