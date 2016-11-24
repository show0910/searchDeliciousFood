package useschemeurl.com.example.choi.deliciousfoodsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Choi on 2016-11-18.
 */

public class EventBoardAnother extends AppCompatActivity {

    public static final int REQUEST_CODE_CALENDAR = 1003;
    public static final int REQUEST_CODE_DAUMMAP = 1004;
    String hour = null;

    EditText editTitle;
    EditText editAddress;
    TextView editTime;
    EditText editContents;
    Button findMapButton;
    Button inputButton;
    Spinner cmbTime;
    EditText editShopName;
    ArrayList<String> titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_another);

        editTitle = (EditText) findViewById(R.id.editTitle);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editTime = (TextView) findViewById(R.id.editTime);
        editContents = (EditText) findViewById(R.id.editContents);
        inputButton = (Button) findViewById(R.id.inputButton);
        findMapButton = (Button) findViewById(R.id.findMapButton);
        cmbTime = (Spinner) findViewById(R.id.cmbTime);
        editShopName = (EditText) findViewById(R.id.editShopName);

        if (getIntent().getStringExtra("usage").equals("insert")) {
            titleList = getIntent().getStringArrayListExtra("titleList");
        }

        final Spinner cmbTime = (Spinner) findViewById(R.id.cmbTime);
        ArrayAdapter timeAdapter = ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbTime.setAdapter(timeAdapter);

        if (getIntent().getStringExtra("usage").equals("update")) {
            editTitle.setFocusableInTouchMode(false);
            editTitle.setText(getIntent().getStringExtra("title"));
            editShopName.setText(getIntent().getStringExtra("shopName"));
            cmbTime.setSelection(getHourIndex(getIntent().getStringExtra("hour")));
            editAddress.setText(getIntent().getStringExtra("address"));
            editTime.setText(getIntent().getStringExtra("time"));
            editContents.setText(getIntent().getStringExtra("contents"));
        }

        if (getIntent().getStringExtra("usage").equals("view")) {
            inputButton.setVisibility(View.GONE);
            findMapButton.setVisibility(View.GONE);
            editTitle.setFocusableInTouchMode(false);
            editAddress.setFocusableInTouchMode(false);
            cmbTime.setEnabled(false);
            editTime.setEnabled(false);
            editContents.setFocusableInTouchMode(false);
            editShopName.setFocusableInTouchMode(false);

            editTitle.setText(getIntent().getStringExtra("title"));
            editShopName.setText(getIntent().getStringExtra("shopName"));
            cmbTime.setSelection(getHourIndex(getIntent().getStringExtra("hour")));
            editAddress.setText(getIntent().getStringExtra("address"));
            editTime.setText(getIntent().getStringExtra("time"));
            editContents.setText(getIntent().getStringExtra("contents"));
        }

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalanderActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CALENDAR);
            }
        });

        findMapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent youIntent = new Intent(getApplicationContext(), SearchDaumMap.class);
                youIntent.putExtra("usage", "address");
                startActivityForResult(youIntent, REQUEST_CODE_DAUMMAP);

            }
        });

        cmbTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hour = (String) cmbTime.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        inputButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean numValid = false;

                if (TextUtils.isEmpty(editTitle.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "제목을 입력해 주세요!!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(editTime.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "마감 시간을 입력해 주세요!!!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(editShopName.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "가게 이름을 입력해 주세요!!!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(editAddress.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "가게 주소를 입력해 주세요!!!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(editContents.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "이벤트 내용을 입력해 주세요!!!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (getIntent().getStringExtra("usage").equals("insert")) {
                    if (titleList.size() != 0) {
                        for (String title : titleList) {
                            if (title.equals(editTitle.getText().toString())) {
                                Toast.makeText(getApplicationContext(), "동일한 제목이 이미 있습니다!!!", Toast.LENGTH_LONG).show();
                                numValid = true;
                            }
                        }
                    }
                }

                if (numValid) {
                    return;
                }

                Date nowDate = null;

                SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
                try {
                    nowDate = dtFormat.parse(editTime.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long nowTime = nowDate.getTime();
                long hour1 = getHour(hour);

                long nowTime1 = System.currentTimeMillis();

                if (nowTime1 > nowTime + (hour1 * 1000)) {
                    Toast.makeText(getApplicationContext(), "지금보다 이후 시간을 입력해주세요!!", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent resultIntent = getIntent();

                resultIntent.putExtra("title", editTitle.getText().toString());
                resultIntent.putExtra("time", editTime.getText().toString());
                resultIntent.putExtra("hour", hour);
                resultIntent.putExtra("address", editAddress.getText().toString());
                resultIntent.putExtra("contents", editContents.getText().toString());
                resultIntent.putExtra("shopName", editShopName.getText().toString());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CALENDAR && resultCode == RESULT_OK) {
            editTime.setText(data.getStringExtra("date"));
        } else if (requestCode == REQUEST_CODE_DAUMMAP && resultCode == RESULT_OK) {
            editAddress.setText(data.getStringExtra("address"));
            editShopName.setText(data.getStringExtra("shopName"));
        }
    }

    public long getHour(String time) {

        long hour = 0;

        switch (time) {
            case "00시":
                break;
            case "01시":
                hour = 3600;
                break;
            case "02시":
                hour = 3600 * 2;
                break;
            case "03시":
                hour = 3600 * 3;
                break;
            case "04시":
                hour = 3600 * 4;
                break;
            case "05시":
                hour = 3600 * 5;
                break;
            case "06시":
                hour = 3600 * 6;
                break;
            case "07시":
                hour = 3600 * 7;
                break;
            case "08시":
                hour = 3600 * 8;
                break;
            case "09시":
                hour = 3600 * 9;
                break;
            case "10시":
                hour = 3600 * 10;
                break;
            case "11시":
                hour = 3600 * 11;
                break;
            case "12시":
                hour = 3600 * 12;
                break;
            case "13시":
                hour = 3600 * 13;
                break;
            case "14시":
                hour = 3600 * 14;
                break;
            case "15시":
                hour = 3600 * 15;
                break;
            case "16시":
                hour = 3600 * 16;
                break;
            case "17시":
                hour = 3600 * 17;
                break;
            case "18시":
                hour = 3600 * 18;
                break;
            case "19시":
                hour = 3600 * 19;
                break;
            case "20시":
                hour = 3600 * 20;
                break;
            case "21시":
                hour = 3600 * 21;
                break;
            case "22시":
                hour = 3600 * 22;
                break;
            case "23시":
                hour = 3600 * 23;
                break;
        }
        return hour;
    }

    public int getHourIndex(String time) {

        int index = 0;

        switch (time) {
            case "00시":
                index = 0;
                break;
            case "01시":
                index = 1;
                break;
            case "02시":
                index = 2;
                break;
            case "03시":
                index = 3;
                break;
            case "04시":
                index = 4;
                break;
            case "05시":
                index = 5;
                break;
            case "06시":
                index = 6;
                break;
            case "07시":
                index = 7;
                break;
            case "08시":
                index = 8;
                break;
            case "09시":
                index = 9;
                break;
            case "10시":
                index = 10;
                break;
            case "11시":
                index = 11;
                break;
            case "12시":
                index = 12;
                break;
            case "13시":
                index = 13;
                break;
            case "14시":
                index = 14;
                break;
            case "15시":
                index = 15;
                break;
            case "16시":
                index = 16;
                break;
            case "17시":
                index = 17;
                break;
            case "18시":
                index = 18;
                break;
            case "19시":
                index = 19;
                break;
            case "20시":
                index = 20;
                break;
            case "21시":
                index = 21;
                break;
            case "22시":
                index = 22;
                break;
            case "23시":
                index = 23;
                break;
        }
        return index;
    }


}

