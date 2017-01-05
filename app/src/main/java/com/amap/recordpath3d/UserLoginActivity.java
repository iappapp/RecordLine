package com.amap.recordpath3d;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amap.util.Config;
import com.amap.util.ToastUtils;
import com.example.recordpath3d.R;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tree on 17/1/1.
 */
public class UserLoginActivity extends Activity implements View.OnClickListener{
    private EditText login_username;
    private EditText login_password;

    private Button user_login;
    private Button user_register;
    private CloseableHttpClient client;
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
                        ToastUtils.showText(getApplicationContext(),"登陆成功");
                        Intent intent = new Intent(getApplicationContext(),UserActivity.class);
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
        client = HttpClients.createDefault();


        String url = Config.HOST + ":" + Config.PORT +
                "/user/login.do?name=" + username +
                "&uuid=" + password;

        PostThread thread = new PostThread(url,handler);
        thread.run();

    }

    public void  tryRegister(){
        String username = login_username.getText().toString().trim();
        String password = login_password.getText().toString().trim();
        String message = null;
        client = HttpClients.createDefault();
        int code = 0;
        HttpPost post = new HttpPost(Config.HOST + "/user/register.do?uuid=" +
                password + "&name=" + username);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
            InputStream inputStream = response.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(inputStream, "utf-8");
            char[] buffer = new char[1024];
            int length = 0;
            StringBuilder sb = new StringBuilder();
            while ((length = reader.read(buffer, 0, buffer.length)) != -1) {
                sb.append(buffer, 0, length);
            }
            reader.close();
            inputStream.close();
            message = sb.toString();
        }catch (IOException ex){
            Log.e("IO异常",ex.getMessage());
        }finally {
            try{
                if(response !=null){
                    response.close();
                }
            }catch (IOException ex){
                Log.e("IO异常,关闭异常",ex.getMessage());
            }
        }
        try {
            JSONObject object = new JSONObject(message);
            code = object.getInt("code");
        }catch (JSONException ex){
            Log.e("解析Json字符串失败",ex.getMessage());
        }
        if(code == 1){
            //登陆成功
            ToastUtils.showText(getApplicationContext(),"注册成功");
        }else{
            ToastUtils.showText(getApplicationContext(),"用户已存在,请重新输入用户名");
        }
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
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(this.url);
            CloseableHttpResponse response = null;
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
