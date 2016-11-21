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
        setImage(aItem.getmImagePath());

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

    public void setImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 8;

        if (path != null) {
            File file = new File(path);
            if (file.exists() == true) {
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                String fileName = null;
                if (path.contains("DeliciousFood")) {
                    fileName = path.substring(39);
                } else {
                    fileName = path.substring(32);
                }

                Bitmap bitmap1 = createThumbnail(bitmap, fileName);
                mIcon.setImageBitmap(bitmap1);
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
    public static Bitmap createThumbnail(Bitmap bitmap, String filename) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        String strFilePath = "/storage/emulated/0/DCIM/DeliciousFood/thumnail/";

        int degree = getPhotoOrientationDegree(strFilePath + filename);

        File file = new File(strFilePath);

        // If no folders
        if (!file.exists()) {
            file.mkdirs();
            // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }

        File fileCacheItem = new File(strFilePath + filename);
        OutputStream out = null;

        try {
            if (!fileCacheItem.exists()) {
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();

                fileCacheItem.createNewFile();
                out = new FileOutputStream(fileCacheItem); //160 부분을 자신이 원하는 크기로 변경할 수 있습니다.
//                bitmap = Bitmap.createScaledBitmap(bitmap, 160, height/(width/160), true);
                bitmap = Bitmap.createScaledBitmap(bitmap, 154, 122, true);

                bitmap = getRotatedBitmap(bitmap, degree);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                return BitmapFactory.decodeFile(strFilePath + filename, options);
            } else {
                return BitmapFactory.decodeFile(strFilePath + filename, options);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    //사진의 각도 알아오기
    public synchronized static int getPhotoOrientationDegree(String filepath) {
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException e) {
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }
        return degree;
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


