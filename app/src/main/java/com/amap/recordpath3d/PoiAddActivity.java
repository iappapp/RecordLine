package com.amap.recordpath3d;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.database.DbAdapter;
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
public class PoiAddActivity extends Activity implements View.OnClickListener,ToggleButton.OnToggleChanged{

    private ImageButton poiGoback;
    private Button poiSaveBtn;
    private EditText poiName;
    private EditText poiDescription;
    private EditText poiAddress;
    private EditText poiPosition;
    private ToggleButton poiToggleBtn;
    private LatLng point;
    private DbAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poi_addactivity);

        point = getIntent().getParcelableExtra("point");

        initView();
        initEvent();
        setupPoi();
    }

    public void initView(){
        poiGoback = (ImageButton) findViewById(R.id.poi_add_goback);
        poiSaveBtn = (Button) findViewById(R.id.poi_add_save);
        poiName = (EditText) findViewById(R.id.poi_add_name);
        poiDescription = (EditText) findViewById(R.id.poi_add_description);
        poiAddress = (EditText) findViewById(R.id.poi_add_address);
        poiPosition = (EditText) findViewById(R.id.poi_add_position);
        poiToggleBtn = (ToggleButton) findViewById(R.id.poi_add_load_poi);

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
            case R.id.poi_add_load_poi:
                break;
            default:
                break;
        }
    }

    @Override
    public void onToggle(boolean on) {
        if(on){
            Toast.makeText(PoiAddActivity.this,"开关打开",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(PoiAddActivity.this,"开关关闭",Toast.LENGTH_SHORT).show();
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
