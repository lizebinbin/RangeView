package com.mintmedical.rangedemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    RangeView view1;
    RangeView view2;
    RangeView view3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view1 = (RangeView) findViewById(R.id.rangeView1);
        view2 = (RangeView) findViewById(R.id.rangeView2);
        view3 = (RangeView) findViewById(R.id.rangeView3);

//        view1.setRange(20, 200, 80, 120);
//        view2.setRange(0.5f, 20.0f, 8.5f, 12.0f);
//        view3.setRange(5, 30, 6, 28);

//        view1.setValue(80);
//        view2.setValue(14.2f);
//        view3.setValue(8.01f);
        handler.sendEmptyMessageDelayed(2,2000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                view1.setRange(20, 200, 80, 120);
                view2.setRange(0.5f, 20.0f, 8.5f, 12.0f);
                view3.setRange(5, 30, 6, 28);

                view1.setValue(80);
                view2.setValue(14.2f);
                view3.setValue(8.01f);
            }
        }
    };
}
