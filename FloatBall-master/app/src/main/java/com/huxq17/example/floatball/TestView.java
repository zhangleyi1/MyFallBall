package com.huxq17.example.floatball;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.huxq17.example.floatball.floatball.runner.ICarrier;
/**
 * Created by Administrator on 2018/1/24.
 */

public class TestView extends ViewGroup implements ICarrier {
    TestRun mTest;

    public TestView(Context context) {
        super(context);
        Log.d("zly", "zly --> TestView begin.");
        mTest = new TestRun(this);
        mTest.start();
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void onMove(int lastX, int lastY, int curX, int curY) {

    }

    @Override
    public void onDone() {

    }
}
