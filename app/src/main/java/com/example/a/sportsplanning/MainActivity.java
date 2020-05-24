package com.example.a.sportsplanning;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.a.sportsplanning.Collector.ActivityCollector;
import com.example.a.sportsplanning.footInterface.UpdateUiCallBack;
import com.example.a.sportsplanning.fragment.EvaluateFragment;
import com.example.a.sportsplanning.fragment.ExerciseFragment;
import com.example.a.sportsplanning.service.BindService;
import com.example.a.sportsplanning.utils.ChangeColor;
import com.example.a.sportsplanning.utils.Util;

public class MainActivity extends AppCompatActivity {
    private boolean isEvaluate;//判断用户是否已经进行评估
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    //计步服务
    private BindService bindService;
    private boolean isBind = false;
    public static double bushu;//步数
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                bushu = msg.arg1;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Util.getPermissions(this);
        ActivityCollector.addActivity(this);
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        editor=pref.edit();
        isEvaluate=pref.getBoolean("isEvaluate",false);

        initView();
        //启动计步服务
        Intent intent = new Intent(this, BindService.class);
        isBind =  bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        System.out.println(isBind);
        startService(intent);
        System.out.println("启动服务");
        ChangeColor.changeColor(this,Color.parseColor("#30464646"));

    }
    /**
     * 初始化组件视图
     */
    private void initView() {

        //如果用户已经完成测评则显示计划的界面，否则显示提示测评的界面
        if(isEvaluate){
            replaceFragment(new ExerciseFragment());
        }
        else{
            replaceFragment(new EvaluateFragment());
        }
    }
    /**
     * fragment替换事件
     *
     * @param fragment
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager ft=getSupportFragmentManager();
        FragmentTransaction ftr=ft.beginTransaction();
        ftr.replace(R.id.fl_container,fragment);
        ftr.commit();
    }
    //和绷定服务数据交换的桥梁，可以通过IBinder service获取服务的实例来调用服务的方法或者数据
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BindService.LcBinder lcBinder = (BindService.LcBinder) service;
            bindService = lcBinder.getService();
            bindService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    //当前接收到stepCount数据，就是最新的步数
                    Message message = Message.obtain();
                    message.what = 1;
                    message.arg1 = stepCount;
                    handler.sendMessage(message);
                    Log.i("MainActivity—updateUi","当前步数"+stepCount);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    public void onDestroy() {  //app被关闭之前，service先解除绑定
        super.onDestroy();
        if (isBind) {
            this.unbindService(serviceConnection);
        }
        ActivityCollector.removeActivity(this);
    }
}
