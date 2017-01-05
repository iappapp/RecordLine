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
import android.widget.Button;
import android.widget.EditText;

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

/**
 * Created by tree on 17/1/1.
 */
public class UserLoginActivity extends Activity implements View.OnClickListener{
    private EditText login_username;
    private EditText login_password;

    private Button user_login;
    private Button user_register;
    private Handler handler;
    private final static int SUCCEED = 1;
    private final static int FAIL = 0;

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
                    case SUCCEED:
                        String message = (String)msg.obj;
                        Pair<Integer,String> pair = handleJsonString(message);
                        if(pair.first == 1){
                            ToastUtils.showText(getApplicationContext(),pair.second);
                        }else{
                            ToastUtils.showText(getApplicationContext(),pair.second);
                        }

                }
            }
        };
    }

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
    //TODO
    //TEST
    public void tryLogin(){
        String username = login_username.getText().toString().trim();
        String password = login_password.getText().toString().trim();



        String url = Config.HOST + ":" + Config.PORT +
                "/user/login.do?name=" + username +
                "&uuid=" + password;

        PostThread thread = new PostThread(url,handler);
        thread.run();

    }

    public void  tryRegister(){
        String username = login_username.getText().toString().trim();
        String password = login_password.getText().toString().trim();
        String url = Config.HOST + ":" + Config.PORT +
                "/user/register.do?name=" + username +
                "&uuid=" + password;
        PostThread thread = new PostThread(url,handler);
        thread.run();
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

    public class PostThread extends Thread{
        private String url;
        private Handler handler;

        public PostThread(String url, Handler handler) {
            this.url = url;
            this.handler = handler;
        }

        @Override
        public void run() {
            HttpClient client = AndroidHttpClient.newInstance("");
            HttpPost post = new HttpPost(this.url);
            HttpResponse response = null;
            HttpEntity entity = null;
            try {
                response = client.execute(post);
                if(response.getStatusLine().getStatusCode() == 200){
                    entity = response.getEntity();
                    Message message = new Message();
                    message.what = SUCCEED;
                    message.obj = EntityUtils.toString(entity);
                    this.handler.sendMessage(message);
                }else{
                    Message message = new Message();
                    message.what = FAIL;
                    this.handler.sendMessage(message);
                }
            }catch (IOException ex){

            }
        }
    }
}
