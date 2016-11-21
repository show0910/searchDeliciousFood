package useschemeurl.com.example.choi.deliciousfoodsearch;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Choi on 2016-11-11.
 */

public class NoticeBoardAnother extends AppCompatActivity {

    File file = null;
    EditText editTextTitle;
    EditText editTextContents;
    RatingBar ratingPoint;
    Button inputButton;
    ImageView imageView1;
    String imagePath = null;
    ArrayList<String> titleList;

    public static final int REQUEST_IMAGE_CAPTURE = 1003;
    public static final int REQUEST_FROM_GALLERY = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_another);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextContents = (EditText) findViewById(R.id.editTextContents);
        ratingPoint = (RatingBar) findViewById(R.id.ratingPoint);
        inputButton = (Button) findViewById(R.id.inputButton);
        imageView1 = (ImageView) findViewById(R.id.imageView1);

        if (getIntent().getStringExtra("usage").equals("insert")) {
            titleList = getIntent().getStringArrayListExtra("titleList");
        }

        //업데이트시 Data 세팅
        if (getIntent().getStringExtra("usage").equals("update")) {
            editTextTitle.setFocusableInTouchMode(false);

            editTextTitle.setText(getIntent().getStringExtra("title"));
            ratingPoint.setRating(getIntent().getFloatExtra("point", 3));
            editTextContents.setText(getIntent().getStringExtra("contents"));
            imagePath = getIntent().getStringExtra("imagePath");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            if (imagePath != null) {
                File file = new File(imagePath);
                if (file.exists() == true) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
                    imageView1.setImageBitmap(bitmap);
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.touching);
                    imageView1.setImageDrawable(drawable);
                }
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.touching);
                imageView1.setImageDrawable(drawable);
            }
        }

        //View시 Data 세팅 수정 X
        if (getIntent().getStringExtra("usage").equals("view")) {
            inputButton.setVisibility(View.GONE);
            editTextTitle.setFocusableInTouchMode(false);
            editTextContents.setFocusableInTouchMode(false);
            ratingPoint.setIsIndicator(true);
            imageView1.setEnabled(false);

            editTextTitle.setText(getIntent().getStringExtra("title"));
            ratingPoint.setRating(getIntent().getFloatExtra("point", 3));
            editTextContents.setText(getIntent().getStringExtra("contents"));

            String path = getIntent().getStringExtra("imagePath");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            if (path != null) {
                File file = new File(path);
                if (file.exists() == true) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                    imageView1.setImageBitmap(bitmap);
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.no_image);
                    imageView1.setImageDrawable(drawable);
                }
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.no_image);
                imageView1.setImageDrawable(drawable);
            }
        }

        //imageClick시 발생
        imageView1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(NoticeBoardAnother.this);
                builder.setMessage("어디서 사진을 가져오겠습니까??").setCancelable(true)
                        .setPositiveButton("카메라", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                try {
                                    file = NoticeBoardAnother.createFile();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }

                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                                }

                            }
                        }).setNegativeButton("갤러리", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_FROM_GALLERY);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        inputButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int numValid = 0;

                if (TextUtils.isEmpty(editTextTitle.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "제목을 입력해 주세요!!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(editTextContents.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "내용을 입력해 주세요!!!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (getIntent().getStringExtra("usage").equals("insert")) {
                    if (titleList.size() != 0) {
                        for (String title : titleList) {
                            if (title.equals(editTextTitle.getText().toString())) {
                                Toast.makeText(getApplicationContext(), "동일한 제목이 이미 있습니다!!!", Toast.LENGTH_LONG).show();
                                numValid = 1;
                            }
                        }
                    }
                }


                if (numValid == 1) {
                    return;
                }

                Intent resultIntent = getIntent();

                resultIntent.putExtra("title", editTextTitle.getText().toString());
                resultIntent.putExtra("contents", editTextContents.getText().toString());
                resultIntent.putExtra("point", ratingPoint.getRating());
                resultIntent.putExtra("imagePath", imagePath);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        checkDangerousPermissions();

    }

    private static File createFile() throws IOException {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String strNow = dateFormat.format(date);

        String imageFileName = strNow + ".jpg";
        File curFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/DeliciousFood/", imageFileName);

        return curFile;
    }

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            if (file != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

                ExifInterface exif = null;
                int degree = 0;

                try {
                    exif = new ExifInterface(file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

                if (orientation == 6) {
                    degree = 90;
                }

                bitmap = getRotatedBitmap(bitmap, degree);

                imagePath = file.getAbsolutePath();
                imageView1.setImageBitmap(bitmap);

            } else {
                Toast.makeText(getApplicationContext(), "File is null.", Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == REQUEST_FROM_GALLERY && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            imagePath = getPath(data.getData());
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

            ExifInterface exif = null;
            int degree = 0;

            try {
                exif = new ExifInterface(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

            if (orientation == 6) {
                degree = 90;
            }

            bitmap = getRotatedBitmap(bitmap, degree);

            imageView1.setImageBitmap(bitmap);
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
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

}
