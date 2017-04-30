package com.amap.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.database.DbAdapter;
import com.amap.util.Config;
import com.amap.util.ToastUtils;
import com.example.recordpath3d.R;
import com.zcw.togglebutton.ToggleButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private DbAdapter db;
    private Handler handler;
    private boolean isLogin = false;
    private final  static int USER_ICON = 1;
    private final  static int LOGIN = 2;
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me);

        preferences = getSharedPreferences("config",MODE_PRIVATE);
        initView();
        initEvent();
        initToggle();
        initRecord();
        initUserIcon();
    }


    public void initRecord(){
        db = new DbAdapter(getApplicationContext());
        db.open();
        int poiCount = db.getAllPoiRecordsCount();
        int pathCount = db.getAllManualRecords() + db.getAllRecords();
        text_point_detail.setText(String.format("共%s条",poiCount));
        text_path_detail.setText(String.format("共%s条",pathCount));
        db.close();
    }

    //新开线程下载图片根据登陆信息
    //设置头像文件下载地址
    public void initUserIcon(){
        isLogin = checkLogin();
        if(isLogin){
            //先检查服务端有无图像文件

        }else{
            image_user_detail.setImageResource(R.drawable.default_user);
        }
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

    /**
     * 初始化事件监听
     */
    public void initEvent(){
        text_login_name.setOnClickListener(this);
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
                //
                break;
            case R.id.user_detail:
                Intent userLogin = new Intent(getApplicationContext(),UserLoginActivity.class);
                if(isLogin){

                }
                else{
                    startActivityForResult(userLogin, LOGIN);
                }
                break;
            case R.id.user_login :
                Intent userLogin2 = new Intent(getApplicationContext(),UserLoginActivity.class);
                startActivity(userLogin2);
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

    /**
     * 地图下载功能
     */
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

    /**
     * 根据城市名下载离线地图
     * @param cityname
     */
    public void downloadOfflineMapByName(String cityname){
        OfflineMapManager mapManager = new OfflineMapManager(getApplicationContext(),this);
        try {
            mapManager.downloadByCityName(cityname);
        }catch (AMapException ex){
            Log.e("城市不存在",ex.getErrorMessage());
        }
    }

    /**
     * 根据城市编码下载地图
     * @param citycode
     */
    public void downloadOfflineMapByCode(String citycode){
        OfflineMapManager mapManager = new OfflineMapManager(getApplicationContext(),this);
        try {
            mapManager.downloadByCityName("0571");
        }catch (AMapException ex){
            Log.e("城市编码不存在",ex.getErrorMessage());
        }
    }

    /**
     * 用户手机上传图片
     */
    public void selectUserIconIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,USER_ICON);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null){
            return;
        }
        if(resultCode == USER_ICON){
            Uri image = data.getData();
            ContentResolver resolver = this.getContentResolver();
            try{
                Bitmap bitmap = BitmapFactory.decodeStream(resolver.openInputStream(image));
                image_user_detail.setImageBitmap(bitmap);
                //上传到某个用户图像
                this.userIconUpload(image,3);

            }catch (FileNotFoundException ex){
                Log.i("File Not Found",ex.getMessage());
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    //图片上传
    public void userIconUpload(Uri uri,Integer id){
        if(null == uri){
            ToastUtils.showText(getApplicationContext(),"选择图片文件出错");
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        String picPath = null;
        Cursor cursor = new CursorLoader(getApplicationContext(),uri,projection ,null,null,null).loadInBackground();
        if(null != cursor){
            int columnIndex = cursor.getColumnIndex(projection[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(0);
            cursor.close();
        }
        if(null != picPath && (picPath.endsWith(".png") || picPath.endsWith(".jpg"))){
            final File picFile = new File(picPath);
            if(null == picFile || !picFile.exists()){
                ToastUtils.showText(getApplicationContext(),"文件不存在");
            }
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("img",picFile.getName(),RequestBody.create(MEDIA_TYPE_JPG,picFile));
            MultipartBody body = builder.build();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(Config.HOST + Config.PORT + "/user/icon/" + id + "/upload.do")
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("上传失败",e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i("上传成功",response.toString());
                }
            });
        }
    }

    public boolean checkLogin(){
        SharedPreferences preferences = this.getSharedPreferences("config",MODE_PRIVATE);
        String username = preferences.getString("name",null);
        String password = preferences.getString("password",null);
        Integer id = preferences.getInt("id",0);
        if(id > 0){
            return true;
        }
        return false;
    }
}
