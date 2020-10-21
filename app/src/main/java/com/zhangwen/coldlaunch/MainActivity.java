package com.zhangwen.coldlaunch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private boolean isHomePageCreated = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (!isHomePageCreated) {
            ColdApplication.isColdLaunch = ColdApplication.tempCL;
            ColdApplication.tempCL = false;
            isHomePageCreated = true;
            Log.e("ColdApplication", "------------MainActivity----ColdApplication:onStart");
        }
        Log.e("MainActivity", "---是否是冷启动：" + (ColdApplication.isColdLaunch ? "是冷启动" : "非冷启动"));
    }
}
