package useschemeurl.com.example.choi.deliciousfoodsearch;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.internal.v;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraPosition;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.CancelableCallback;
import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import useschemeurl.com.example.choi.deliciousfoodsearch.search.Item;
import useschemeurl.com.example.choi.deliciousfoodsearch.search.OnFinishSearchListener;
import useschemeurl.com.example.choi.deliciousfoodsearch.search.Searcher;

/**
 * Created by Choi on 2016-11-04.
 */

public class SearchDaumMap extends Activity implements MapView.OpenAPIKeyAuthenticationResultListener, MapView.MapViewEventListener, MapView.POIItemEventListener {

    private MapView mMapView;
    double currentLatitude = 0;
    double currentLongitude = 0;
    double targetLatitude = 0;
    double targetLongitude = 0;
    String address = null;
    String shopName = null;
    KeySet key = new KeySet();

    Button currentButton;
    Button findRoadCarButton;
    Button findRoadBusButton;
    Button findRoadWalkButton;
    EditText searchText;
    Button searchButton;
    Button addressButton;
    TextView txtView1;
    LinearLayout layout1;
    TextView mainBanner;
    FloatingActionButton fabMenu;
    FloatingActionButton fabMenuFirst;
    FloatingActionButton fabMenuSecond;
    FloatingActionButton fabMenuThird;
    FloatingActionButton fabMenuForth;
    Animation aniMainMenu;
    Animation aniMainMenuBak;
    boolean fabMenuValid;

    MapPOIItem poiItemHome;
    MapPOIItem poiItemTarget;
    MapPOIItem[] poiItemTargetSet = new MapPOIItem[20];

    private HashMap<Integer, Item> mTagItemMap = new HashMap<Integer, Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daum_map);

        currentButton = (Button) findViewById(R.id.currentButton);
        findRoadCarButton = (Button) findViewById(R.id.findRoadCarButton);
        findRoadBusButton = (Button) findViewById(R.id.findRoadBusButton);
        findRoadWalkButton = (Button) findViewById(R.id.findRoadWalkButton);
        addressButton = (Button) findViewById(R.id.address_button);
        searchText = (EditText) findViewById(R.id.search_text);
        searchButton = (Button) findViewById(R.id.search_button);
        txtView1 = (TextView) findViewById(R.id.txtView1);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        mainBanner = (TextView) findViewById(R.id.main_banner);
        fabMenu = (FloatingActionButton) findViewById(R.id.fabMenu);
        fabMenuFirst = (FloatingActionButton) findViewById(R.id.fabMenuFirst);
        fabMenuSecond = (FloatingActionButton) findViewById(R.id.fabMenuSecond);
        fabMenuThird = (FloatingActionButton) findViewById(R.id.fabMenuThird);
        fabMenuForth = (FloatingActionButton) findViewById(R.id.fabMenuForth);

        aniMainMenu = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_main_menu);
        aniMainMenuBak = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_main_menu_bak);

        fabMenuFirst.setVisibility(View.INVISIBLE);
        fabMenuSecond.setVisibility(View.INVISIBLE);
        fabMenuThird.setVisibility(View.INVISIBLE);
        fabMenuForth.setVisibility(View.INVISIBLE);

        MapLayout mapLayout = new MapLayout(this);
        mMapView = mapLayout.getMapView();

        mMapView.setDaumMapApiKey(key.getDaumMapServerKey());
        mMapView.setOpenAPIKeyAuthenticationResultListener(this);
        mMapView.setMapViewEventListener(this);
        mMapView.setPOIItemEventListener(this);
        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        mMapView.setMapType(MapView.MapType.Standard);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapLayout);

        if (getIntent().getStringExtra("usage").equals("origin")) {
            addressButton.setVisibility(View.GONE);
        }

        if (getIntent().getStringExtra("usage").equals("address")) {
            txtView1.setVisibility(View.GONE);
            layout1.setVisibility(View.GONE);
            fabMenu.setVisibility(View.GONE);
            mainBanner.setText("가게이름&주소 찾기");
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchText.getText().toString();
                if (query == null || query.length() == 0) {
                    showToast("검색어를 입력하세요");
                    return;
                }
                hideSoftKeyboard();
                MapPoint.GeoCoordinate geoCoordinate = mMapView.getMapCenterPoint().getMapPointGeoCoord();
                double latitude = geoCoordinate.latitude; //위도
                double longitude = geoCoordinate.longitude; //경도
                int radius = 10000;
                int page = 1;

                Searcher searcher = new Searcher();
                searcher.searchKeyword(getApplicationContext(), query, latitude, longitude, radius, page, key.getDaumMapServerKey(), new OnFinishSearchListener() {
                    @Override
                    public void onSuccess(List<Item> itemList) {
//                      mMapView.removeAllPOIItems(); // 기존 검색 결과 삭제

                        for (int i = 0; i < poiItemTargetSet.length; i++) {
                            if (poiItemTargetSet[i] == null) {
                                break;
                            }
                            mMapView.removePOIItem(poiItemTargetSet[i]);
                        }

                        showResult(itemList); // 검색 결과 보여줌
                    }

                    @Override
                    public void onFail() {
                        showToast("API_KEY의 제한 트래픽이 초과되었습니다.");
                    }
                });

            }
        });

        addressButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent resultIntent = getIntent();
                resultIntent.putExtra("address", address);
                resultIntent.putExtra("shopName", shopName);
                setResult(RESULT_OK, resultIntent);
                finish();

            }
        });

        //차량 길찾기 버튼
        findRoadCarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = "daummaps://route?sp=" + currentLatitude + "," + currentLongitude + "&ep=" + targetLatitude + "," + targetLongitude + "&by=CAR";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        //버스 길찾기 버튼
        findRoadBusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "daummaps://route?sp=" + currentLatitude + "," + currentLongitude + "&ep=" + targetLatitude + "," + targetLongitude + "&by=PUBLICTRANSIT";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        //길찾기 버튼
        findRoadWalkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = "daummaps://route?sp=" + currentLatitude + "," + currentLongitude + "&ep=" + targetLatitude + "," + targetLongitude + "&by=FOOT";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        fabMenuValid = true;

        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fabMenuValid) {
                    fabMenu.startAnimation(aniMainMenu);
                } else {
                    fabMenu.startAnimation(aniMainMenuBak);
                }
            }
        });

        aniMainMenu.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                fabMenuFirst.setVisibility(View.VISIBLE);
                fabMenuSecond.setVisibility(View.VISIBLE);
                fabMenuThird.setVisibility(View.VISIBLE);
                fabMenuForth.setVisibility(View.VISIBLE);

                ObjectAnimator moverFirst = ObjectAnimator.ofFloat(fabMenuFirst, "translationY", 0, -1050);
                moverFirst.setDuration(1000);
                moverFirst.start();

                ObjectAnimator moverSecond = ObjectAnimator.ofFloat(fabMenuSecond, "translationY", 0, -800);
                moverSecond.setDuration(1000);
                moverSecond.start();

                ObjectAnimator moverThird = ObjectAnimator.ofFloat(fabMenuThird, "translationY", 0, -550);
                moverThird.setDuration(1000);
                moverThird.start();

                ObjectAnimator moverForth = ObjectAnimator.ofFloat(fabMenuForth, "translationY", 0, -300);
                moverForth.setDuration(1000);
                moverForth.start();

                fabMenuValid = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        aniMainMenuBak.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                ObjectAnimator moverFirst = ObjectAnimator.ofFloat(fabMenuFirst, "translationY", 1050);
                moverFirst.setDuration(1000);
                moverFirst.start();

                ObjectAnimator moverSecond = ObjectAnimator.ofFloat(fabMenuSecond, "translationY", 800);
                moverSecond.setDuration(1000);
                moverSecond.start();

                ObjectAnimator moverThird = ObjectAnimator.ofFloat(fabMenuThird, "translationY", 550);
                moverThird.setDuration(1000);
                moverThird.start();

                ObjectAnimator moverForth = ObjectAnimator.ofFloat(fabMenuForth, "translationY", 300);
                moverForth.setDuration(1000);
                moverForth.start();

                fabMenuValid = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                fabMenuFirst.setVisibility(View.INVISIBLE);
                fabMenuSecond.setVisibility(View.INVISIBLE);
                fabMenuThird.setVisibility(View.INVISIBLE);
                fabMenuForth.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fabMenuFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youIntent = new Intent(getApplicationContext(), SearchYouTube.class);
                startActivity(youIntent);
                finish();
            }
        });

        fabMenuSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youIntent = new Intent(getApplicationContext(), NoticeBoard.class);
                startActivity(youIntent);
                finish();
            }
        });

        fabMenuThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youIntent = new Intent(getApplicationContext(), SearchEngine.class);
                startActivity(youIntent);
                finish();
            }
        });

        fabMenuForth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent youIntent = new Intent(getApplicationContext(), EventBoard.class);
                startActivity(youIntent);
                finish();
            }
        });

    } //end onCreate Class

    public void currentClick(View v) {
        Toast.makeText(getApplicationContext(), "현재 위치 찾기가 시작되었습니다.", Toast.LENGTH_SHORT).show();

        //GPS로부터 현재위치 가지고 오는 메소드
        if (startLocationService() == 1) {
            return;
        }

        MapPoint MAP_POINT_HOME = MapPoint.mapPointWithGeoCoord(currentLatitude, currentLongitude);
        CameraPosition CAMERA_POSITION_HOME = new CameraPosition(MapPoint.mapPointWithGeoCoord(currentLatitude, currentLongitude), 2);

        mMapView.animateCamera(CameraUpdateFactory.newCameraPosition(CAMERA_POSITION_HOME), 1000, new CancelableCallback() {
            @Override
            public void onFinish() {
//              Toast.makeText(getBaseContext(), "Move to CurrentPosition complete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "Move to CurrentPosition canceled", Toast.LENGTH_SHORT).show();
            }
        });

        if (poiItemHome != null) {
            mMapView.removePOIItem(poiItemHome);
        }

        Item itemHome = new Item();

        poiItemHome = new MapPOIItem();
        poiItemHome.setItemName("현재 위치");
        poiItemHome.setTag(16);
        poiItemHome.setMapPoint(MAP_POINT_HOME);
        poiItemHome.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        poiItemHome.setCustomImageResourceId(R.drawable.map_pin_red);
        poiItemHome.setCustomImageAutoscale(false);
        poiItemHome.setCustomImageAnchor(0.5f, 1.0f);
        mMapView.addPOIItem(poiItemHome);

        itemHome.title = poiItemHome.getItemName();
        mTagItemMap.put(poiItemHome.getTag(), itemHome);
        mMapView.selectPOIItem(poiItemHome, false);

    }

    //결과값 보여주기
    private void showResult(List<Item> itemList) {
        MapPointBounds mapPointBounds = new MapPointBounds();
        poiItemTargetSet = new MapPOIItem[20];

        for (int i = 0; i < itemList.size(); i++) {

            Item item = itemList.get(i);

            poiItemTarget = new MapPOIItem();
            poiItemTarget.setItemName(item.title);
            poiItemTarget.setTag(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item.latitude, item.longitude);
            poiItemTarget.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);
            poiItemTarget.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItemTarget.setCustomImageResourceId(R.drawable.map_pin_blue);
            poiItemTarget.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItemTarget.setCustomSelectedImageResourceId(R.drawable.map_pin_yellow);
            poiItemTarget.setCustomImageAutoscale(false);
            poiItemTarget.setCustomImageAnchor(0.5f, 1.0f);

            poiItemTargetSet[i] = poiItemTarget;

            mMapView.addPOIItem(poiItemTarget);
            mTagItemMap.put(poiItemTarget.getTag(), item);
        }
        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));

        MapPOIItem[] poiItems = mMapView.getPOIItems();
        if (poiItems.length > 0) {
            mMapView.selectPOIItem(poiItems[0], false);
        }
    }


    //키보드 숨기기
    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
        return;
    }

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //Toast로 메세지 띄워주기
    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int startLocationService() {
        // 위치 관리자 객체 참조
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alertCheckGPS();
            return 1;
        }

        // 위치 정보를 받을 리스너 생성
        GPSListener gpsListener = new GPSListener();
        long minTime = 1;
        float minDistance = 0;

        try {
            // GPS를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 네트워크를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                currentLatitude = lastLocation.getLatitude();
                currentLongitude = lastLocation.getLongitude();
            }

        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
        return 0;
    }


    /**
     * 리스너 클래스 정의
     */
    private class GPSListener implements LocationListener {
        /**
         * 위치 정보가 확인될 때 자동 호출되는 메소드
         */
        public void onLocationChanged(Location location) {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

//            String msg = "Latitude : "+ currentLatitude + "\nLongitude:"+ currentLongitude;
//            Log.i("GPSListener", msg);

//            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private void alertCheckGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS 기능이 Off 되어 있습니다. On 하시겠습니까??").setCancelable(false)
                .setPositiveButton("GPS On", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveConfigGPS();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // GPS 설정화면으로 이동
    private void moveConfigGPS() {
        Intent gpsOptionsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }

    private class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {

        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.activity_call_balloon, null);

        }

        @Override
        public View getCalloutBalloon(MapPOIItem mapPOIItem) {
            if (mapPOIItem == null) {
                return null;
            }
            Item item = mTagItemMap.get(mapPOIItem.getTag());
            if (item == null) {
                return null;
            }
            ImageView imageViewBadge = (ImageView) mCalloutBalloon.findViewById(R.id.badge);
            TextView textViewTitle = (TextView) mCalloutBalloon.findViewById(R.id.title);
            textViewTitle.setText(item.title);
            TextView textViewDesc = (TextView) mCalloutBalloon.findViewById(R.id.desc);
            textViewDesc.setText(item.newAddress);
            imageViewBadge.setImageDrawable(createDrawableFromUrl(item.imageUrl));

            if (mapPOIItem.getTag() < 16) {
                targetLatitude = item.latitude;
                targetLongitude = item.longitude;
            }

            address = item.newAddress;
            shopName = item.title;

            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
            return null;
        }
    }

    private Drawable createDrawableFromUrl(String url) {
        try {
            InputStream is = (InputStream) this.fetch(url);
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object fetch(String address) throws MalformedURLException, IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        if (mapPOIItem.getItemName() != "현재 위치") {
            targetLatitude = mapPOIItem.getMapPoint().getMapPointGeoCoord().latitude;
            targetLongitude = mapPOIItem.getMapPoint().getMapPointGeoCoord().longitude;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Intent resultIntent = getIntent();

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

    }


}
