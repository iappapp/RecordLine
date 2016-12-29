package com.amap.recordpath3d;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.recordpath3d.R;
import com.zcw.togglebutton.ToggleButton;

/**
 * Created by tree on 16/12/29.
 */
public class UserActivity extends Activity implements View.OnClickListener,ToggleButton.OnToggleChanged{

    private TextView text_login_name;
    private ImageView image_user_detail;
    private TextView text_path_detail;
    private TextView text_point_detail;
    private TextView text_message_detail;
    private LinearLayout layout_path;
    private LinearLayout layout_point;
    private LinearLayout layout_message;
    private ToggleButton toggle_sync;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me);
        initView();
        initEvent();
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
        toggle_sync = (ToggleButton) findViewById(R.id.toggle_sync);
    }

    public void initEvent(){
        layout_path.setOnClickListener(this);
        layout_point.setOnClickListener(this);
        layout_message.setOnClickListener(this);
        image_user_detail.setOnClickListener(this);
        toggle_sync.setOnToggleChanged(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_path:
                //TODO
                break;
            case R.id.layout_point:
                //TODO
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
            //TODO
        }else{
            //TODO
        }
    }
}
