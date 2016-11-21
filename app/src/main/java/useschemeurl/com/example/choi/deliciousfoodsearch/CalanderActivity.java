package useschemeurl.com.example.choi.deliciousfoodsearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Choi on 2016-11-18.
 */

public class CalanderActivity extends Activity {

    String selectDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendar = (CalendarView) findViewById(R.id.calendar);

        long time = System.currentTimeMillis();

        calendar.setMinDate(time);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectDate = String.valueOf(year) + String.valueOf(month + 1) + String.valueOf(dayOfMonth);

                Intent resultIntent = getIntent();

                resultIntent.putExtra("date", selectDate);

                setResult(RESULT_OK, resultIntent);
                finish();

                Toast.makeText(getApplicationContext(), "" + year + "/" + (month + 1) + "/" + dayOfMonth, Toast.LENGTH_SHORT).show();
            }
        });

    }


}
