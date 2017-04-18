package com.amap.recordpath3d;

import android.app.Activity;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.amap.util.Config;
import com.amap.util.ToastUtils;
import com.example.recordpath3d.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by tree on 17/1/1.
 */

/**
 * 登陆逻辑固定
 * 设置登陆布尔值
 * 如为登陆则进行登陆尝试,如不成功则返回消息
 * 登陆成功将布尔值修改,同时保存登录信息
 * 登陆信息不进行加密处理,直接明文减少系统设计复杂度
 */
public class UserLoginActivity extends Activity implements View.OnClickListener{
    private EditText login_username;
    private EditText login_password;

    private Button user_login;
    private Button user_register;
    private Handler handler;
    private final static int SUCCEED = 1;
    private final static int FAIL = 0;
    private final static int LOGIN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        initView();
        initEvent();
        initHandler();
    }

    public void initView(){
        login_username = (EditText) findViewById(R.id.username);
        login_password = (EditText) findViewById(R.id.password);

        user_login = (Button) findViewById(R.id.btn_login);
        user_register = (Button)findViewById(R.id.btn_register);


    }
    //TODO同一消息格式
    /**
     * 处理线程运行返回的结果
     */
    public void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case SUCCEED:
                        String message = (String)msg.obj;
                        Pair<Integer,String> pair = handleJsonString(message);
                        if(pair != null && pair.first == 1){
                            ToastUtils.showText(getApplicationContext(),pair.second);
                        }else{
                            ToastUtils.showText(getApplicationContext(),"消息解析出错");
                        }

                }
            }
        };
    }

    //直接返回消息类的JSON数据
    //json数据直接解析对应类
    @Deprecated
    public Pair<Integer,String> handleJsonString(String message){
        try {
            JSONObject object = new JSONObject(message);
            String msg = object.getString("message");
            int code = object.getInt("code");
            return new Pair<>(code,msg);
        }catch (JSONException ex){
            Log.e("解析错误",ex.getMessage());
            return null;
        }

    }


    public void initEvent(){
        user_login.setOnClickListener(this);
        user_register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if(!isNullorEmpty()){
            ToastUtils.showText(getApplicationContext(),"请输入密码或用户名");
            return;
        }
        if(v.getId() == R.id.btn_login){
            tryLogin();
        }else if(v.getId() == R.id.btn_register){
            tryRegister();
        }
    }
    //TODO统一消息格式
    //TEST

    public void tryLogin(){
        final String username = login_username.getText().toString().trim();
        String password = login_password.getText().toString().trim();
        String url = Config.HOST + ":" + Config.PORT +
                "/user/login.do?name=" + username +
                "&uuid=" + password;
        postUrl(url);
    }

    public void postUrl(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(new RequestBody() {
                    @Override
                    public MediaType contentType() {
                        return MediaType.parse("json/application");
                    }

                    @Override
                    public void writeTo(BufferedSink sink) throws IOException {

                    }
                })
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Failure=",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String message = response.body().string();
                Log.i("Message=",message);
                Message message1 = new Message();
                message1.what = SUCCEED;
                message1.obj = message;
                handler.sendMessage(message1);
            }
        });

    }

    public void  tryRegister(){
        String username = login_username.getText().toString().trim();
        String password = login_password.getText().toString().trim();
        String url = Config.HOST + ":" + Config.PORT +
                "/user/register.do?name=" + username +
                "&uuid=" + password;
        postUrl(url);
    }

    /**
     * 用户名不为空
     * 密码不为空
     * @return 两者不为空返回true
     */
    public boolean isNullorEmpty(){
        return !login_username.getText().toString().equals("")
                && !login_password.getText().toString().equals("");
    }

}
