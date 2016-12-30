package com.amap.recordpath3d;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.database.DbAdapter;
import com.amap.record.PathRecord;
import com.amap.util.Coordinate;
import com.amap.util.ToastUtils;
import com.example.recordpath3d.R;

/**
 * AMapV2地图中介绍如何显示一个基本地图
 */
public class MainActivity extends Activity implements LocationSource,
        AMapLocationListener, OnClickListener, AMap.OnMapClickListener,
        GpsStatus.Listener, AMap.OnMapLongClickListener {

    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private PolylineOptions mPolyoptions;
    private PathRecord record;
    private long starttime;
    private long endtime;
    private ToggleButton btn;
    private DbAdapter DbHepler;

    //TODO
    private TextView locationInfo;
    private ImageButton buttonUserActivity;
    private boolean isNormalMode = false;
    private ImageView scaleImg;
    private TextView scaleInfo;
    private boolean isFirstLocation = true;
    private List<LatLng> latLngs;
    private ToggleButton linedistance;
    private TextView gpsStatus;
    private float scale;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basicmap_activity);
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
        //MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        preferences = getSharedPreferences("config",MODE_PRIVATE);
        init();
        initpolyline();

    }

    /**
     * 初始化AMap对象
     */

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

        //TODO
        //切换地图模式按钮
        locationInfo = (TextView) findViewById(R.id.location_speed_info);
        buttonUserActivity = (ImageButton) findViewById(R.id.user_activity);

        scaleImg = (ImageView) findViewById(R.id.scale_img);
        scaleInfo = (TextView) findViewById(R.id.scale_info);
        //计算线段的长度
        latLngs = new ArrayList<>();
        linedistance = (ToggleButton) findViewById(R.id.linedistance);
        gpsStatus = (TextView) findViewById(R.id.satelitte_number);

        buttonUserActivity.setOnClickListener(this);
        linedistance.setOnClickListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnMapLongClickListener(this);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        manager.addGpsStatusListener(this);


        btn = (ToggleButton) findViewById(R.id.locationbtn);
        btn.setOnClickListener(this);

    }

    protected void saverecord(PathRecord record) {
        if (record != null && record.getPathline().size() > 0) {
            DbHepler = new DbAdapter(this);
            DbHepler.open();
            record.setDuration(String.valueOf((endtime - starttime) / 1000f));
            float distance = 0;
            String pathline = "";
            for (int i = 0; i < record.getPathline().size(); i++) {
                if (i < record.getPathline().size() - 1) {
                    LatLng firstpoint = record.getPathline().get(i);
                    LatLng secoundpoint = record.getPathline().get(i + 1);
                    distance = distance + AMapUtils.calculateLineDistance(firstpoint, secoundpoint);
                }
                LatLng point = record.getPathline().get(i);
                pathline = pathline + point.latitude + "," + point.longitude + ";";
            }
            record.setDistance(String.valueOf(distance));
            record.setStartpoint(record.getPathline().get(0));
            record.setAveragespeed(String.valueOf(distance / (float) (endtime - starttime)));
            record.setEndpoint(record.getPathline().get(record.getPathline().size() - 1));

            String stratpoint = record.getStartpoint().latitude + "," + record.getStartpoint().longitude;
            String endpoint = record.getEndpoint().latitude + "," + record.getEndpoint().longitude;
            DbHepler.createrecord(record.getDistance(),
                    record.getDuration(),
                    record.getAveragespeed(),
                    pathline, stratpoint, endpoint,
                    record.getDate());
            DbHepler.close();
        } else {
            ToastUtils.showText(getApplicationContext(),"没有记录保存");
        }
    }

    private void initpolyline() {
        mPolyoptions = new PolylineOptions();
        mPolyoptions.width(10f);
        mPolyoptions.color(Color.BLUE);
        mPolyoptions.setDottedLine(true);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        if(isCityMap()){
            aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        }else
            aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.getUiSettings().setRotateGesturesEnabled(true);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

    }

    public boolean isCityMap(){
        return preferences.getBoolean("isNormalMap",true);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if(isCityMap()){
            aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        }else
            aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        startlocation();
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();

        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
				/*mListener.onLocationChanged(amapLocation);// 显示系统小蓝点*/
                LatLng mylocation = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                //更新位置改变的后的坐标信息栏
                updateLocationInfo(mylocation);

                if (isFirstLocation) {
                    //设置地图缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17f));
                    //将地图中心移到地位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(mylocation));
                    //点击定位按钮，能够将地图的中心点移到地位点
                    mListener.onLocationChanged(amapLocation);
                    isFirstLocation = false;
                }

                if (btn.isChecked()) {
                    record.addpoint(mylocation);
                    mPolyoptions.add(mylocation);
                    redrawline();
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    private void startlocation() {
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);

            mLocationOption.setInterval(2000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();

        }
    }

    private void redrawline() {
        if (mPolyoptions.getPoints().size() > 0) {
            aMap.clear(true);
            aMap.addPolyline(mPolyoptions);
        }
    }

    /**
     * 将多点连接起来绘制行走路线
     * @param latLngs 坐标点集合
     */
    public void redrawline(List<LatLng> latLngs) {
        PolylineOptions options = new PolylineOptions();
        options.setDottedLine(false);

        for (int i = 0; i < latLngs.size(); i++) {
            options.add(latLngs.get(i));
        }
        if (latLngs.size() >= 2) {
            aMap.addPolyline(options);
        }
    }

    /**
     * 计算集合内点集合的距离
     * @param latLngs 坐标点的集合
     */
    public void distance2Points(List<LatLng> latLngs) {

        double distance = 0.0d;
        if (latLngs.size() >= 2) {
            for (int i = 0; i <= latLngs.size() - 2; i++) {
                for (int j = i + 1; j <= latLngs.size() - 1; j++)
                    distance += AMapUtils.calculateLineDistance(latLngs.get(i), latLngs.get(j));
            }
            ToastUtils.showText(getApplicationContext(),"距离为" + distance + "M");
        }
        //显示距离两点
        else{
            ToastUtils.showText(getApplicationContext(),"请选择两个以上的点");
        }

        latLngs.clear();
    }


    @SuppressLint("SimpleDateFormat")
    private String getcueDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss ");
        Date curDate = new Date(time);
        String date = formatter.format(curDate);
        return date;
    }

    public void record(View view) {
        Intent intent = new Intent(MainActivity.this,PoiListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_activity:
                Intent userActivity = new Intent(getApplicationContext(),UserActivity.class);
                startActivity(userActivity);
                break;

            case R.id.linedistance:
                if (!linedistance.isChecked()) {
                    distance2Points(latLngs);
                    aMap.clear();
                }
                break;
            case R.id.locationbtn:
                if (btn.isChecked()) {
                    Log.i("MY", "isChecked");

                    aMap.clear(true);
                    if (record != null) {
                        record = null;
                    }
                    record = new PathRecord();
                    starttime = System.currentTimeMillis();
                    record.setDate(getcueDate(starttime));
                } else {
                    endtime = System.currentTimeMillis();
                    saverecord(record);
                }
                break;
            default:
                break;
        }
    }


    public void updateScaleInfo() {
        if (scale <= 0.0f) {
            return;
        }

        final int[] ruler = new int[]{
                2, 4, 10, 20, 50,
                100, 200, 400, 900,
                1000, 2000, 4000, 8000, 10000, 16000,
                30000, 60000, 100000, 200000, 400000, 800000, 1000000
        };
        final String[] rulers = new String[]{
                "1m", "2m", "5m", "10m", "25m",
                "50m", "100m", "200m", "450m",
                "500m", "1Km", "2Km", "4Km", "5Km", "8Km",
                "15Km", "30Km", "50Km", "100Km", "200Km", "400Km", "500Km"
        };
        scale = aMap.getScalePerPixel();

        ViewGroup.LayoutParams layoutParams;
        layoutParams = scaleImg.getLayoutParams();
        layoutParams.height = 15;

        String scaleinfo = "";

        for (int i = 0; i < ruler.length; i++) {
            double imggeWidth = (double) ruler[i] / (double) scale;
            int width = (int) imggeWidth;
            if (width >= 60 && width <= 150) {
                layoutParams.width = width;
                scaleinfo = rulers[i];
            }
        }
        scaleImg.setLayoutParams(layoutParams);
        scaleImg.invalidate();
        scaleInfo.setText(scaleinfo);
    }

    public void updateLocationInfo(LatLng latLng) {

        double latitude = latLng.latitude;
        double longitude = latLng.longitude;

        int[] latitudes = Coordinate.toDegree(latitude);
        int[] longitudes = Coordinate.toDegree(longitude);

        String longitudeStr = longitudes[0] + "°" + longitudes[1] + "'" + longitudes[2] + "\"";
        if (longitude >= -180 && longitude < 0) {
            longitudeStr += "W";
        } else {
            longitudeStr += "E";
        }

        String latitudeStr = latitudes[0] + "°" + latitudes[1] + "'" + latitudes[2] + "\"";
        if (latitude > 0 && latitude < 90) {
            latitudeStr += "N";
        } else {
            latitudeStr += "S";
        }
        String speed = " N/A";

        locationInfo.setText(longitudeStr + "," + latitudeStr + speed);
    }

    /**
     * 做手动规划路径使用
     * 点击地图aMap就更新顶部的信息栏
     * @param latLng 当前点击位置的坐标
     */
    @Override
    public void onMapClick(LatLng latLng) {

    }


    private LocationManager manager;

    /**
     * 根据GpsStatus传来的事件类型变更Gps数量信息
     *
     * @param event
     */
    @Override
    public void onGpsStatusChanged(int event) {
        int count = 0;
        GpsStatus status = manager.getGpsStatus(null);
        if (status == null) {
            return;
        } else {
            switch (event) {
                case GpsStatus.GPS_EVENT_STOPPED:
                    gpsStatus.setText("0");
                    break;
                case GpsStatus.GPS_EVENT_STARTED:
                    gpsStatus.setTextColor(ColorStateList.valueOf(Color.GREEN));
                    break;
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    int maxSatellites = status.getMaxSatellites();
                    Iterator<GpsSatellite> satelliteIterator = status.getSatellites().iterator();
                    while (satelliteIterator.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = satelliteIterator.next();
                        if (s.getSnr() > 0) {
                            count++;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        gpsStatus.setText(String.valueOf(count));
    }


    @Override
    public void onMapLongClick(LatLng latLng) {

        LatLonPoint point = new LatLonPoint(latLng.latitude,latLng.longitude);

        Marker marker = aMap.addMarker(new MarkerOptions().
                position(latLng).
                title("").
                snippet(""));

        if (linedistance.isChecked()) {
            latLngs.add(latLng);
            redrawline(latLngs);
        }

        Intent intent = new Intent(this,PoiAddActivity.class);
        intent.putExtra("point",latLng);
        startActivity(intent);
    }
}