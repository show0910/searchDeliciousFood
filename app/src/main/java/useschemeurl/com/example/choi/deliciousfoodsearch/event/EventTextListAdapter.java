package useschemeurl.com.example.choi.deliciousfoodsearch.event;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import useschemeurl.com.example.choi.deliciousfoodsearch.EventBoard;
import useschemeurl.com.example.choi.deliciousfoodsearch.EventBoardAnother;
import useschemeurl.com.example.choi.deliciousfoodsearch.NoticeBoardAnother;


/**
 * Created by Choi on 2016-11-18.
 */

public class EventTextListAdapter extends BaseAdapter {

    private Context mContext;
    private List<EventTextItem> mItems = new ArrayList<EventTextItem>();

    public EventTextListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addItem(EventTextItem it) {
        mItems.add(it);
    }

    public void setmItems(List<EventTextItem> mItems) {
        this.mItems = mItems;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    public ArrayList<String> getAllTitle() {

        ArrayList<String> titleList = new ArrayList<String>();

        for (EventTextItem item : mItems) {
            titleList.add(item.getData(0));
        }

        return titleList;
    }

    public void delView(ArrayList<Integer> delList) {
        for (int i = 0; i < delList.size(); i++) {
            mItems.remove(delList.get(i) - i);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final EventTextView itemView;
        Thread calcThread = null;
        String eventTime = mItems.get(position).getData(2);
        Handler mHandler;

        itemView = new EventTextView(mContext, mItems.get(position));

        if (mItems.get(position).getSelectValid()) {
            itemView.setBackgroundColor(Color.parseColor("#EAEAEA"));
            itemView.validCheckBox.setChecked(true);
        } else {
            if (position % 2 == 0) {
                itemView.setBackgroundColor(Color.parseColor("#D9E5FF"));
                itemView.validCheckBox.setChecked(false);
            } else if (position % 2 == 1) {
                itemView.setBackgroundColor(Color.parseColor("#B2CCFF"));
                itemView.validCheckBox.setChecked(false);
            }
        }

        itemView.layoutList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EventBoardAnother.class);

                EventTextItem item = (EventTextItem) getItem(position);

                intent.putExtra("usage", "view");
                intent.putExtra("title", item.getData(0));
                intent.putExtra("address", item.getData(1));
                intent.putExtra("time", item.getData(2));
                intent.putExtra("contents", item.getData(3));
                intent.putExtra("hour", item.getData(4));
                intent.putExtra("shopName", item.getData(5));

                mContext.startActivity(intent);
            }
        });

        itemView.validCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    itemView.setBackgroundColor(Color.parseColor("#EAEAEA"));
                    mItems.get(position).setSelectValid(true);
                } else {
                    if (position % 2 == 0) {
                        itemView.setBackgroundColor(Color.parseColor("#D9E5FF"));
                        mItems.get(position).setSelectValid(false);
                    } else if (position % 2 == 1) {
                        itemView.setBackgroundColor(Color.parseColor("#B2CCFF"));
                        mItems.get(position).setSelectValid(false);
                    }
                }
            }
        });

        Date nowDate = null;

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            nowDate = dtFormat.parse(eventTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //시를 넣어 줬기 때문에 1일을 빼줘야 한다.
        long nowTime = nowDate.getTime() - 86400000;

        long hour = getHour(mItems.get(position).getData(4));


        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        itemView.remainTime.setText((String) msg.obj);
                        break;
                    case 1:
                        break;
                }
            }
        };

        if (position == 0) {
            if (calcThread != null && calcThread.isAlive()) {
                calcThread.interrupt();
            }
        }

        if (mItems.get(position).getData(0).equals("치트!@#")) {
            long nowTime1 = System.currentTimeMillis();
            calcThread = new CalcThread(nowTime1 - 86394000, mHandler);
        } else {
            calcThread = new CalcThread(nowTime + (hour * 1000), mHandler);
        }

        calcThread.start();


        return itemView;
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
} // Activity End

class CalcThread extends Thread {

    long nowTime;
    Handler mHandler;

    CalcThread(long nowTime, Handler handler) {
        this.nowTime = nowTime;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            SystemClock.sleep(1);
            Message msg = new Message();
            String remainTime = null;

            long time1 = nowTime + 86399000;
            long nowTime = System.currentTimeMillis();

            long newTime = (time1 - nowTime) / 1000;
            long remainDay = newTime / (60 * 60 * 24);
            newTime = newTime - (remainDay * 60 * 60 * 24);

            long remainHour = newTime / (60 * 60);
            newTime = newTime - (remainHour * 60 * 60);

            long remainMinute = newTime / (60);
            long remainSecond = newTime - (remainMinute * 60);

            remainTime = remainDay + "일 " + remainHour + "시" + remainMinute + "분" + remainSecond + "초";

            if (remainDay < 0 || remainHour < 0 || remainMinute < 0 || remainSecond < 0) {
                remainTime = "이벤트 끝!!";
            }

            msg.what = 0;
            msg.obj = remainTime;
            mHandler.sendMessage(msg);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
