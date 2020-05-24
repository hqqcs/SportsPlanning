package com.example.a.sportsplanning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a.sportsplanning.activity.LoginActivity;
import com.example.a.sportsplanning.utils.FullScreenUtil;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    protected int splashTime=3000;
    private Thread splashThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //设置全屏
        FullScreenUtil.setFullScreen(this);
        final SplashActivity splashActivity=this;
        pref=PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        editor=pref.edit();

        splashThread=new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    synchronized (this){
                        wait(splashTime);
                    }
                }catch (InterruptedException e){}
                finally {
                    finish();
                    boolean isFirst = pref.getBoolean("isFirst",true);
                    if(isFirst){
                        Intent intent=new Intent(splashActivity,LoginActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent =new Intent(splashActivity,MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
        splashThread.start();
    }
}
