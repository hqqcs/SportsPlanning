package com.example.a.sportsplanning.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a.sportsplanning.R;
import com.example.a.sportsplanning.db.User;
import com.example.a.sportsplanning.question.SportTargetActivity;
import com.example.a.sportsplanning.service.LoginService;
import com.example.a.sportsplanning.utils.PasswordMD5Util;
import com.example.a.sportsplanning.utils.ToastUtil;
import com.example.a.sportsplanning.utils.UsernameAndPasswordByIs;

public class LoginActivity extends AppCompatActivity {
    private EditText userNameTv,passwordTv;//用户名和密码
    private TextView newUser;//新用户注册
    private Button loginBtn;//登陆按钮

    public static Handler handler; //消息接收
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        handler = new MessageUtil();
        initComponent();
        setData();
        setListeners();
    }
    @Override
    protected void onResume() {
        super.onResume();
        setData();
        Log.v("1","onresume");
    }

    /**
     * 初始化各组件
     */
    private void initComponent() {
        //透明状态栏          
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        userNameTv=findViewById(R.id.user_name);
        passwordTv=findViewById(R.id.password);
        newUser=findViewById(R.id.new_user);
        loginBtn=findViewById(R.id.login);
    }

    /**
     * 为各组件设置数据
     */
    private void setData() {

        if (getIntent().getStringExtra("user_id")!=null&&getIntent().getStringExtra("password")!=null) {
            userNameTv.setText(getIntent().getStringExtra("user_id"));
            passwordTv.setText(getIntent().getStringExtra("password"));
        }
    }

    /**
     * 为各组件设置点击监听事件
     */
    private void setListeners() {
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UsernameAndPasswordByIs.checkusername(userNameTv.getText().toString())&&UsernameAndPasswordByIs.checkPassword(passwordTv.getText().toString())){
                    Intent loginservice = new Intent(LoginActivity.this,LoginService.class);
                    Bundle data = new Bundle();
                    data.putString("user_id",userNameTv.getText().toString());
                    data.putString("password",PasswordMD5Util.generateMD5(passwordTv.getText().toString()));
                    loginservice.putExtras(data);
                    startService(loginservice);
                }else {
                    ToastUtil.showToast(LoginActivity.this,"请输入账户或密码");
                }
            }
        });
    }

    /**
     * 消息处理内部类
     */
    @SuppressLint("HandlerLeak")
    private class MessageUtil extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String str = data.getString("login");
            if(msg.what == 0x003){
                Intent go_main = new Intent(LoginActivity.this,SportTargetActivity.class);
                User.user.setUserName(userNameTv.getText().toString());
                startActivity(go_main);
                LoginActivity.this.finish();
            }else {
                //获得消息中的数据并显示
                ToastUtil.showToast(LoginActivity.this,str);
            }
        }
    }
}
