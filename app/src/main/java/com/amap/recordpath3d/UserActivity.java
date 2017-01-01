package com.amap.recordpath3d;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.util.ToastUtils;
import com.example.recordpath3d.R;
import com.zcw.togglebutton.ToggleButton;

/**
 * Created by tree on 16/12/29.
 */
public class UserActivity extends Activity implements View.OnClickListener,ToggleButton.OnToggleChanged, OfflineMapManager.OfflineMapDownloadListener{

    private TextView text_login_name;
    private ImageView image_user_detail;
    private TextView text_path_detail;
    private TextView text_point_detail;
    private TextView text_message_detail;
    private LinearLayout layout_path;
    private LinearLayout layout_point;
    private LinearLayout layout_message;
    private ToggleButton toggle_mapmode;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me);
        initView();
        initEvent();
        preferences = getSharedPreferences("config",MODE_PRIVATE);
        initToggle();

        //downloadOfflineMapByName("宁波市");
    }

    /**
     * 初始化视图组件
     */
    public void initView(){
        text_login_name = (TextView) findViewById(R.id.user_login);
        image_user_detail = (ImageView) findViewById(R.id.user_icon);
        text_path_detail = (TextView) findViewById(R.id.user_path_detail);
        text_point_detail = (TextView) findViewById(R.id.user_poi_detail);
        text_message_detail = (TextView) findViewById(R.id.user_message_detail);

        layout_path = (LinearLayout) findViewById(R.id.layout_path);
        layout_point = (LinearLayout) findViewById(R.id.layout_point);
        layout_message = (LinearLayout) findViewById(R.id.layout_message);
        toggle_mapmode = (ToggleButton) findViewById(R.id.toggle_mapmode);
    }

    public void initEvent(){
        layout_path.setOnClickListener(this);
        layout_point.setOnClickListener(this);
        layout_message.setOnClickListener(this);
        image_user_detail.setOnClickListener(this);
        toggle_mapmode.setOnToggleChanged(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_path:
                Intent intentPath = new Intent(getApplicationContext(),RecordActivity.class);
                startActivity(intentPath);
                break;
            case R.id.layout_point:
                Intent intent = new Intent(getApplicationContext(),PoiListActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_message:
                //TODO
                break;
            case R.id.user_detail:
                //TODO
                break;
            default:
                break;
        }
    }

    @Override
    public void onToggle(boolean on) {
        if(on){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isNormalMap",true);
            editor.commit();
        }else{
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isNormalMap",false);
            editor.commit();
        }
    }

    @Override
    public void onDownload(int i, int i1, String s) {
        Log.e("开始下载",s);
    }

    @Override
    public void onCheckUpdate(boolean b, String s) {
        if(b){
            ToastUtils.showText(getApplicationContext(),s);
        }
    }


    public void initToggle(){
        if(preferences.getBoolean("isNormalMap",true)){
            toggle_mapmode.setToggleOn();
        }else{
            toggle_mapmode.setToggleOff();
        }
    }

    @Override
    public void onRemove(boolean b, String s, String s1) {

    }

    public void downloadOfflineMapByName(String cityname){
        OfflineMapManager mapManager = new OfflineMapManager(getApplicationContext(),this);
        try {
            mapManager.downloadByCityName(cityname);
        }catch (AMapException ex){
            Log.e("城市不存在",ex.getErrorMessage());
        }
    }
    public void downloadOfflineMapByCode(String citycode){
        OfflineMapManager mapManager = new OfflineMapManager(getApplicationContext(),this);
        try {
            mapManager.downloadByCityName("0571");
        }catch (AMapException ex){
            Log.e("城市编码不存在",ex.getErrorMessage());
        }
    }
}
