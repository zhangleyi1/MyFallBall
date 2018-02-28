package com.huxq17.example.floatball;

import android.util.Log;

import com.huxq17.example.floatball.floatball.runner.ICarrier;

/**
 * Created by Administrator on 2018/1/24.
 */

public class TestRun implements Runnable {

    private ICarrier mICarrier;

    public TestRun(ICarrier iCarrier) {
        mICarrier = iCarrier;
    }

    public void start() {
        mICarrier.removeCallbacks(this);
        mICarrier.post(this);
    }

    @Override
    public void run() {
        Log.d(getClass().getSimpleName(), "zly --> TestRun run.");
    }
}
