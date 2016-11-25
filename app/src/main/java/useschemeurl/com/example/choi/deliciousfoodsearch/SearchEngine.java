package useschemeurl.com.example.choi.deliciousfoodsearch;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_engine);

        naverSearch = (Button) findViewById(R.id.naver_button);
        daumSearch = (Button) findViewById(R.id.daum_button);
        searchText = (EditText) findViewById(R.id.search_text);
        fabMenu = (FloatingActionButton) findViewById(R.id.fabMenu);

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

        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "플로팅 버튼 클릭 완료", Toast.LENGTH_LONG).show();
            }
        });

    }
}
