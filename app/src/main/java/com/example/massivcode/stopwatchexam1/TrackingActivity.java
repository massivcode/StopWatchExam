package com.example.massivcode.stopwatchexam1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TrackingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TrackingActivity.class.getSimpleName();
    private TextView mTimeTextView;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        initializeViews();
    }

    private void initializeViews() {
        mTimeTextView = (TextView) findViewById(R.id.tv_time);

        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);
        findViewById(R.id.btn_terminate).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mIntent = new Intent(getApplicationContext(), TrackingService.class);
        switch (v.getId()) {
            case R.id.btn_start:
                mIntent.setAction(TrackingService.ACTION_START_TRACKING);
                startService(mIntent);
                break;
            case R.id.btn_pause:
                mIntent.setAction(TrackingService.ACTION_PAUSE_TRACKING);
                startService(mIntent);
                break;
            case R.id.btn_terminate:
                mIntent.setAction(TrackingService.ACTION_STOP_TRACKING);
                startService(mIntent);
                break;
        }
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "onRestart: ");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
