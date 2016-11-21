package useschemeurl.com.example.choi.deliciousfoodsearch;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Choi on 2016-11-03.
 */

public class FrontScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_screen);

//        Drawable alpha = findViewById(R.id.activity_front_screen).getBackground();
//        alpha.setAlpha(0);

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }
}
