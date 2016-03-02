package com.example.massivcode.stopwatchexam1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

/**
 * Created by massivCode on 2016-03-02.
 */
public class TrackingService extends Service {

    private static final String TAG = TrackingService.class.getSimpleName();
    public static final String ACTION_START_TRACKING = "ACTION_START_TRACKING";
    public static final String ACTION_PAUSE_TRACKING = "ACTION_PAUSE_TRACKING";
    public static final String ACTION_STOP_TRACKING = "ACTION_STOP_TRACKING";

    private boolean mIsTracking = false;
    private boolean mIsPause = false;
    private TimerThread mTimerThread = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        String action = intent.getAction();

        switch (action) {
            case ACTION_START_TRACKING:
                trackingStart();
                break;
            case ACTION_PAUSE_TRACKING:
                trackingPause();
                break;
            case ACTION_STOP_TRACKING:
                trackingStop();
                break;
        }

        return START_NOT_STICKY;
    }

    private void trackingStart() {
        if (mIsTracking) {
            System.out.println("이미 트래킹 중 입니다!");
        } else {
            if (mTimerThread == null) {
                mTimerThread = new TimerThread();
                mTimerThread.start();
                mIsTracking = true;
            }
        }
    }

    private void trackingPause() {
        if (mIsTracking) {
            if(mIsPause) {
                mTimerThread.changeRunningState(true);
            } else {
                mTimerThread.changeRunningState(false);
                mIsPause = true;
            }

        } else {
            System.out.println("트래킹 중이지 않습니다!");
        }

    }

    private void trackingStop() {
        if (mTimerThread != null) {
            mTimerThread.interrupt();
            mTimerThread = null;
            mIsTracking = false;
        }
    }

    @Subscribe
    public void onEvent(Intent intent) {
        String action = intent.getAction();

        switch (action) {
            case "시간전달":
                System.out.println(intent.getStringExtra("time"));
                break;
        }
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private class TimerThread extends Thread {
        private int state = 0;
        private boolean isRunning = true;
        private boolean isPause = false;
        private long currentTime = 0;

        @Override
        public void run() {
            loop: while (true) {

                if (isRunning) {  // 타이머 진행중
                    currentTime += 1000;
                } else { // 타이머 일시정지중

                }

                try {
                    System.out.println("현재 시간 : " + getCurrentTime());
                    Intent intent = new Intent("시간전달");
                    intent.putExtra("time", getCurrentTime());
                    EventBus.getDefault().post(intent);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break loop;
                }
            }
        }

        public String getCurrentTime() {
            return milliSecondsToTime(currentTime);
        }

        public void changeRunningState(boolean state) {
            isRunning = state;
        }

        public void changePauseState(boolean state) {
            isPause = state;
        }

        /**
         * 밀리세컨드를 HH:mm:ss 포맷으로 리턴
         *
         * @param milliSeconds
         * @return
         */
        private String milliSecondsToTime(long milliSeconds) {
            return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(milliSeconds),
                    TimeUnit.MILLISECONDS.toMinutes(milliSeconds) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(milliSeconds) % TimeUnit.MINUTES.toSeconds(1));
        }
    }
}
