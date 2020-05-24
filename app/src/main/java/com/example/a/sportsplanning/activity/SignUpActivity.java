package com.example.a.sportsplanning.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a.sportsplanning.R;
import com.example.a.sportsplanning.service.RegisterService;
import com.example.a.sportsplanning.utils.PasswordMD5Util;
import com.example.a.sportsplanning.utils.ToastUtil;
import com.example.a.sportsplanning.utils.UsernameAndPasswordByIs;

public class SignUpActivity extends AppCompatActivity {
    private EditText userNameTv,passwordTv,ensurePasswordTv;
    private Button registerBtn;
    public static Handler handler; //消息接收
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        handler = new MessageUtil();
        initComponent();
        setListeners();
    }
    /**
     * 初始化各组件
     */
    private void initComponent() {
        //透明状态栏          
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        userNameTv=(EditText)findViewById(R.id.user_name);
        passwordTv=(EditText)findViewById(R.id.password);
        ensurePasswordTv=(EditText)findViewById(R.id.ensure_password);
        registerBtn=(Button)findViewById(R.id.register_btn);
    }


    /**
     * 为各组件设置点击监听事件
     */
    private void setListeners() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册验证
                if (userNameTv.getText().toString().equals("")){
                    ToastUtil.showToast(SignUpActivity.this,"账户不能为空");
                    return;
                }
                if(passwordTv.getText().toString().equals("")||ensurePasswordTv.getText().toString().equals("")){
                    ToastUtil.showToast(SignUpActivity.this,"密码不能为空");
                    return;
                }
                if(UsernameAndPasswordByIs.checkusername(userNameTv.getText().toString())){
                    if(UsernameAndPasswordByIs.checkPassword(passwordTv.getText().toString())){
                        if(passwordTv.getText().toString().equals(ensurePasswordTv.getText().toString())){
                            //启动服务向服务器发送请求验证注册信息
                            Intent intent = new Intent(SignUpActivity.this,RegisterService.class);
                            Bundle data = new Bundle();
                            data.putString("user_id",userNameTv.getText().toString());
                            data.putString("password",PasswordMD5Util.generateMD5(passwordTv.getText().toString()));
                            intent.putExtras(data);
                            startService(intent);
                        }else {
                            ToastUtil.showToast(SignUpActivity.this,"两次输入密码不一致");
                        }
                    }else {
                        ToastUtil.showToast(SignUpActivity.this,"密码格式不正确");
                    }
                }else {
                    ToastUtil.showToast(SignUpActivity.this,"账户格式不正确");
                }
            }
        });
    }

    /**
     * 消息处理内部类
     */
    private class MessageUtil extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                //获得消息中的数据并显示
                Bundle data = msg.getData();
                String str = data.getString("register");
                ToastUtil.showToast(SignUpActivity.this,str);
            }else if(msg.what == 2){
                //返回登陆界面
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.putExtra("user_id",userNameTv.getText().toString());
                intent.putExtra("password",passwordTv.getText().toString());
                startActivity(intent);
                SignUpActivity.this.finish();
            }
        }
    }
}
