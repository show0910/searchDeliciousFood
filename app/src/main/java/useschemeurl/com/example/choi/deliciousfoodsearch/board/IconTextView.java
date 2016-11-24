package useschemeurl.com.example.choi.deliciousfoodsearch.board;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import useschemeurl.com.example.choi.deliciousfoodsearch.R;

import static android.R.attr.bitmap;

/**
 * Created by Choi on 2016-11-08.
 */

public class IconTextView extends LinearLayout {

    ImageView mIcon;
    TextView mText01;
    RatingBar ratingPoint;
    LinearLayout layoutList;
    CheckBox validCheckBox;

    public IconTextView(Context context, IconTextItem aItem) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.notice_board_list_view, this, true);

        mIcon = (ImageView) findViewById(R.id.iconItem);
        setImage(aItem.getmImagePath(), aItem.getDegree());

        mText01 = (TextView) findViewById(R.id.dataTitle);
        mText01.setText(aItem.getData(0));

        ratingPoint = (RatingBar) findViewById(R.id.ratingPoint);
        ratingPoint.setRating(aItem.getmPoint());

        layoutList = (LinearLayout) findViewById(R.id.layoutList);

        validCheckBox = (CheckBox) findViewById(R.id.validCheckBox);

    }

    public void setText(int index, String data) {
        if (index == 0) {
            mText01.setText(data);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setRatingPoint(Float point) {
        ratingPoint.setRating(point);
    }

    public void setImage(String path, String degree) {

        if (path != null) {
            File file = new File(path);
            if (file.exists() == true) {

                //이미지 이름
                String fileName = null;
                if (path.contains("DeliciousFood")) {
                    fileName = path.substring(39);
                } else {
                    fileName = path.substring(32);
                }

                Bitmap bitmap = createThumbnail(path, fileName, degree);
                mIcon.setImageBitmap(bitmap);
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.no_image);
                mIcon.setImageDrawable(drawable);
            }
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.no_image);
            mIcon.setImageDrawable(drawable);
        }
    }

    public void setIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
    }

    // Bitmap to File
//bitmap에는 비트맵, strFilePath에 는 파일을 저장할 경로, strFilePath 에는 파일 이름을 할당해주면 됩니다.
    public static Bitmap createThumbnail(String path, String filename, String degree) {

        BitmapFactory.Options options = new BitmapFactory.Options();

        String strFilePath = "/storage/emulated/0/DCIM/DeliciousFood/thumnail/";

        int degreeInt = Integer.parseInt(degree);

        File file = new File(strFilePath);
        File noMediaFile = new File(strFilePath + ".nomedia");

        // 경로에 폴더나 .nomedia폴더가 없으면 만들어라라
        if (!file.exists()) {
            file.mkdirs();
        }

        if (!noMediaFile.exists()) {
            noMediaFile.mkdir();
        }

        File fileCacheItem = new File(strFilePath + filename);
        OutputStream out = null;

        try {
            if (!fileCacheItem.exists()) {
                fileCacheItem.createNewFile();
                out = new FileOutputStream(fileCacheItem);

                //이미지를 가져와서
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                //돌리고
                bitmap = getRotatedBitmap(bitmap, degreeInt);
                //Size조절 해주고
                bitmap = Bitmap.createScaledBitmap(bitmap, 154, 122, true);
                //JPEG 파일로 저장해준다.
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                return bitmap;
            } else {
                return BitmapFactory.decodeFile(strFilePath + filename, options);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap bitmap = null;

        return bitmap;
    }

    public synchronized static Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != b2) {
                    bitmap.recycle();
                    bitmap = b2;
                }
            } catch (OutOfMemoryError e) {
            }
        }

        return bitmap;
    }

}


