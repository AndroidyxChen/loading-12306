package com.example.yanxu.loading.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yanxu.loading.R;
import com.example.yanxu.loading.view.PassView;

public class MainActivity extends AppCompatActivity {

    private PassView passView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        passView = findViewById(R.id.pv_test);
        passView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        passView.clearAnimation();
    }

}
