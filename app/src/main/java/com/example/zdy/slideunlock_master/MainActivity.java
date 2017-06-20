package com.example.zdy.slideunlock_master;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private Activity mActivity;
    private View mrootView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        mrootView = findViewById(R.id.rl);

        mrootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                WarningPopUp popUp = new WarningPopUp(mActivity);
                popUp.setWarningRecouse(1,3);
                popUp.showAtLocation(mrootView, Gravity.CENTER,0,-45);
            }
        },200);
    }
}
