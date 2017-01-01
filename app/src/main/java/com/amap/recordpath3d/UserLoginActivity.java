package com.amap.recordpath3d;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.util.Config;
import com.amap.util.ToastUtils;
import com.example.recordpath3d.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Created by tree on 17/1/1.
 */
public class UserLoginActivity extends Activity implements View.OnClickListener{
    private EditText login_username;
    private EditText login_password;

    private Button user_login;
    private Button user_register;
    private CloseableHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        initView();
        initEvent();

    }

    public void initView(){
        login_username = (EditText) findViewById(R.id.username);
        login_password = (EditText) findViewById(R.id.password);

        user_login = (Button) findViewById(R.id.btn_login);
        user_login = (Button)findViewById(R.id.btn_login);

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

    public void tryLogin(){
        String username = login_username.getText().toString().trim();
        String password = login_password.getText().toString().trim();
        client = HttpClients.createDefault();
        HttpPost post = new HttpPost(Config.HOST + "/user/login.do");


        Intent intent = new Intent(getApplicationContext(),UserActivity.class);
        intent.putExtra("name",username);
        intent.putExtra("password",password);

    }

    public void  tryRegister(){

    }

    /**
     * 用户名不为空
     * 密码不为空
     * @return 两者不为空返回true
     */
    public boolean isNullorEmpty(){
        return !login_username.getText().equals("") && !login_password.getText().equals("");
    }
}
