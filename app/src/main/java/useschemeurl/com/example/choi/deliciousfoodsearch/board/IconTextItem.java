package useschemeurl.com.example.choi.deliciousfoodsearch.board;

import android.graphics.drawable.Drawable;

/**
 * Created by Choi on 2016-11-08.
 */

public class IconTextItem {

    private Drawable mIcon;
    private String[] mData;
    private float mPoint;
    private String mImagePath = null;
    private boolean mSelectable = true;
    private boolean mSelectValid = false;

    public IconTextItem(String[] obj, float point, String path) {
        mData = obj;
        mPoint = point;
        mImagePath = path;
    }

    public IconTextItem(String title, float point, String contents, String path) {
        mData = new String[2];
        mData[0] = title;
        mData[1] = contents;

        mPoint = point;
        mImagePath = path;
    }

    public boolean ismSelectable() {
        return mSelectable;
    }

    public void setmSelectable(boolean mSelectable) {
        this.mSelectable = mSelectable;
    }

    public boolean ismSelectValid() {
        return mSelectValid;
    }

    public void setmSelectValid(boolean mSelectValid) {
        this.mSelectValid = mSelectValid;
    }

    public float getmPoint() {
        return mPoint;
    }

    public String[] getData() {
        return mData;
    }

    public String getData(int index) {
        if (mData == null || index >= mData.length) {
            return null;
        }
        return mData[index];
    }

    public void setData(String[] obj) {
        mData = obj;
    }

    public void setmPoint(float point) {
        mPoint = point;
    }

    public void setmImagePath(String path) {
        mImagePath = path;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setIcon(Drawable Icon) {
        mIcon = Icon;
    }

    public Drawable getIcon() {
        return mIcon;
    }
}
