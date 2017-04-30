package com.amap.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.amap.util.Config;
import com.amap.util.ToastUtils;
import com.example.recordpath3d.R;
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
    private Handler handler;
    private final static int SUCCEED = 1;

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
                        String string = (String)msg.obj;
                        com.amap.modal.Message message = JSON.parseObject(string,com.amap.modal.Message.class);
                        if(message != null && message.getCode() == 1){
                            ToastUtils.showText(getApplicationContext(),message.getMessageg());
                        }else if(message != null && message.getCode() != 1){
                            ToastUtils.showText(getApplicationContext(),message.getMessageg());
                        }else{
                            ToastUtils.showText(getApplicationContext(),"请检查网络");
                        }
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
        String userName = login_username.getText().toString().trim();
        String password = login_password.getText().toString().trim();
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
        postUrl(url,null);
    }

    public void postUrl(String url,Map<String,String> header){
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

    public void  tryRegister(final String userName, final String password){
        String url = Config.HOST + ":" + Config.PORT +
                "/user/register.do?name=" + userName +
                "&password=" + password;
        this.postUrl(url,null);
    }

}
