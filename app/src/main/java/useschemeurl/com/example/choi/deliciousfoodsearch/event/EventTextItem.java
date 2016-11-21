package useschemeurl.com.example.choi.deliciousfoodsearch.event;

import android.graphics.drawable.Drawable;

/**
 * Created by Choi on 2016-11-18.
 */

public class EventTextItem {

    private String[] mData;
    private boolean selectValid = false;

    public EventTextItem(String[] obj) {
        mData = obj;
    }

    public EventTextItem(String title, String address, String time, String contents, String hour, String shopName) {
        mData[0] = title;
        mData[1] = address;
        mData[2] = time;
        mData[3] = contents;
        mData[4] = hour;
        mData[5] = shopName;
    }

    public String[] getData() {
        return mData;
    }

    public void setData(String[] data) {
        mData = data;
    }

    public String getData(int position) {
        return mData[position];
    }

    public boolean getSelectValid() {
        return selectValid;
    }

    public void setSelectValid(boolean valid) {
        this.selectValid = valid;
    }

}
