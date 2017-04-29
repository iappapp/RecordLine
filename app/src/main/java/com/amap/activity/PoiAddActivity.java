package com.amap.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.database.DbAdapter;
import com.amap.util.ToastUtils;
import com.zcw.togglebutton.ToggleButton;
import com.example.recordpath3d.R;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 从主页面传来坐标
 * 名称需要填写
 * 描述可以为空
 * 地址由坐标生成
 * 位置为坐标的字符串
 * 是否加载到地图将兴趣点显示在主页面
 */
public class PoiAddActivity extends Activity implements View.OnClickListener,ToggleButton.OnToggleChanged,GeocodeSearch.OnGeocodeSearchListener{

    private ImageButton poiGoback;
    private Button poiSaveBtn;
    private EditText poiName;
    private EditText poiDescription;
    private EditText poiAddress;
    private EditText poiPosition;
    private ToggleButton poiToggleBtn;
    private LatLng point;
    private DbAdapter dbAdapter;
    private Handler handler;

    private GeocodeSearch search;
    private LatLonPoint latLonPoint;
    private RegeocodeQuery query;
    private String result = "";
    private MapView mapView = null;
    private AMap aMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poi_addactivity);

        point = getIntent().getParcelableExtra("point");
        initView(savedInstanceState);
        initEvent();
        initHandler();
        queryLatlng();
        setupPoi();
        initMap();
    }

    public void initMap(){
        if(aMap == null){
            aMap = mapView.getMap();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if(i == 1000){
            if(regeocodeResult != null &&
                    regeocodeResult.getRegeocodeAddress() != null &&
                    regeocodeResult.getRegeocodeAddress().getFormatAddress() != null){
                result = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }else{
                ToastUtils.showText(getApplicationContext(),"NO RESULT");
            }
        }else{
            ToastUtils.showText(getApplicationContext(),"error code " + i);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    public void queryLatlng(){
        search = new GeocodeSearch(PoiAddActivity.this);
        search.setOnGeocodeSearchListener(this);
        latLonPoint = new LatLonPoint(point.latitude,point.longitude);
        query = new RegeocodeQuery(latLonPoint,10f,GeocodeSearch.AMAP);
        search.getFromLocationAsyn(query);
    }

    /**
     * 初始化Handler
     */
    public void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        poiAddress.setText(result);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void initView(Bundle savedInstanceState){
        poiGoback = (ImageButton) findViewById(R.id.poi_add_goback);
        poiSaveBtn = (Button) findViewById(R.id.poi_add_save);
        poiName = (EditText) findViewById(R.id.poi_add_name);
        poiDescription = (EditText) findViewById(R.id.poi_add_description);
        poiAddress = (EditText) findViewById(R.id.poi_add_address);
        poiPosition = (EditText) findViewById(R.id.poi_add_position);
        poiToggleBtn = (ToggleButton) findViewById(R.id.poi_add_load_poi);
        mapView = (MapView) findViewById(R.id.poi_add_mapview);
        mapView.onCreate(savedInstanceState);
    }

    /**
     * 监听返回按钮，保存按钮，图形开关
     */
    public void initEvent(){
        poiGoback.setOnClickListener(this);
        poiSaveBtn.setOnClickListener(this);
        poiToggleBtn.setOnToggleChanged(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.poi_add_goback:
                finish();
                break;
            case R.id.poi_add_save:
                savePoi();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onToggle(boolean on) {
        if(on){
            Marker marker = aMap.addMarker(new MarkerOptions().position(point).title(result).snippet(result));
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17f));
            //将地图中心移到地位点
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(point));
        }else{
            aMap.clear();
        }
    }

    public void setupPoi(){
        poiPosition.setText(point.latitude + "," + point.longitude);
    }

    public void savePoi(){

        dbAdapter = new DbAdapter(PoiAddActivity.this);
        try{
            dbAdapter.open();
            String name = poiName.getText().toString();
            String description = poiDescription.getText().toString();
            String address = poiAddress.getText().toString();
            String point = poiPosition.getText().toString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            if(dbAdapter.createRecord(name,description,address,point,format.format(date))){
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
            }
            dbAdapter.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
