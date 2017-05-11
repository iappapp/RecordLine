package com.amap.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.modal.User;
import com.amap.util.Config;
import com.amap.util.ToastUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.util.Map;

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
 * 登陆信息不进行加密处理,直接明文交换数据
 */
public class UserLoginActivity extends Activity implements View.OnClickListener{
    private EditText login_username;
    private EditText login_password;

    private Button user_login;
    private Button user_register;
    private static Handler handler;
    private final static int SUCCEED_LOGIN = 1;
    private final static int SUCCEED_REG = 2;
    private Integer userId = 0;
    private String userName;
    private String password;

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

    /**
     * 处理线程运行返回的结果
     */
    public void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case SUCCEED_LOGIN:
                        String string = (String)msg.obj;
                        User user = JSON.parseObject(string,User.class);
                        saveLoginInfo(user);
                        finish();
                        break;
                    case SUCCEED_REG:
                        String s = (String)msg.obj;
                        ToastUtils.showText(getApplicationContext(),s);
                        break;
                }
            }
        };
    }

    public void initEvent(){
        user_login.setOnClickListener(this);
        user_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        userName = login_username.getText().toString().trim();
        password = login_password.getText().toString().trim();
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
            ToastUtils.showText(getApplicationContext(),"请输入密码或用户名");
            return;
        }
        if(v.getId() == R.id.btn_login){
            tryLogin(userName,password);
        }else if(v.getId() == R.id.btn_register){
            tryRegister(userName,password);
        }
    }

    public void tryLogin(final String userName,final String password){
        String url = Config.HOST + ":" + Config.PORT +
                "/user/login.do?name=" + userName +
                "&password=" + password;
        postUrl(url,null,true);
    }

    public void postUrl(String url, Map<String,String> header, final boolean isLogin){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Failure=",e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String msg = response.body().string();
                Log.i("Message=",msg);
                if(isLogin){
                    Message message = new Message();
                    message.what = SUCCEED_LOGIN;
                    message.obj = msg;
                    handler.sendMessage(message);
                }else{
                    Message message = new Message();
                    message.what = SUCCEED_REG;
                    message.obj = msg;
                    handler.sendMessage(message);
                }
            }
        });

    }

    public void  tryRegister(final String userName, final String password){
        String url = Config.HOST + ":" + Config.PORT +
                "/user/register.do?name=" + userName +
                "&password=" + password;
        this.postUrl(url,null,false);
    }

    public void saveLoginInfo(User user){
        String userName = user.getName();
        String password = user.getPassword();
        Integer userId = user.getId();
        SharedPreferences preferences = this.getSharedPreferences("config",MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name",userName);
        editor.putString("password",password);
        editor.putInt("id",userId);
        editor.commit();
    }

}
