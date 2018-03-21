package com.zly.floatball.floatball;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.zly.floatball.floatball.floatball.FloatBall;
import com.zly.floatball.floatball.floatball.FloatBallCfg;
import com.zly.floatball.floatball.menu.FloatMenu;
import com.zly.floatball.floatball.menu.FloatMenuCfg;
import com.zly.floatball.floatball.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class FloatBallManager {
    private final String TAG = "FloatBallManager";
    public int mScreenWidth, mScreenHeight;
    private int mStatusBarHeight;

    private OnFloatBallClickListener mFloatballClickListener;
    private WindowManager mWindowManager;
    private Context mContext;
    private FloatBall floatBall;
    private FloatMenu floatMenu;
    public int floatballX, floatballY;
    private boolean isShowing = false;
    private List<MenuItem> menuItems = new ArrayList<>();

    public FloatBallManager(Context application, FloatBallCfg ballCfg) {
        this(application, ballCfg, null);
    }

    public FloatBallManager(Context application, FloatBallCfg ballCfg, FloatMenuCfg menuCfg) {

        mContext = application;//.getApplicationContext();
        int statusbarId = application.getResources().getIdentifier("status_bar_height", "dimen", "android");
        Log.d(TAG, "zly --> FloatBallManager statusbarId:" + statusbarId);
        if (statusbarId > 0) {
            mStatusBarHeight = application.getResources().getDimensionPixelSize(statusbarId);
        }
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        computeScreenSize();
        floatBall = new FloatBall(mContext, this, ballCfg);
        floatMenu = new FloatMenu(mContext, this, menuCfg);
    }

    public void buildMenu() {
        inflateMenuItem();
    }

    /**
     * 添加一个菜单条目
     *
     * @param item
     */
    public FloatBallManager addMenuItem(MenuItem item) {
        menuItems.add(item);
        return this;
    }

    public int getMenuItemSize() {
        return menuItems != null ? menuItems.size() : 0;
    }

    /**
     * 设置菜单
     *
     * @param items
     */
    public FloatBallManager setMenu(List<MenuItem> items) {
        menuItems = items;
        return this;
    }

    private void inflateMenuItem() {
        floatMenu.removeAllItemViews();
        for (MenuItem item : menuItems) {
            floatMenu.addItem(item);
        }
    }

    public int getBallSize() {
        return floatBall.getSize();
    }

    public void computeScreenSize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point point = new Point();
            mWindowManager.getDefaultDisplay().getSize(point);
            mScreenWidth = point.x;
            mScreenHeight = point.y;
        } else {
            mScreenWidth = mWindowManager.getDefaultDisplay().getWidth();
            mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();
        }
        Log.d(TAG, "zly --> computeScreenSize mStatusBarHeight:" + mStatusBarHeight + " mScreenHeight:" + mScreenHeight);
        mScreenHeight -= mStatusBarHeight;
    }

    public void show() {
        Log.d(TAG, "zly --> isShowing:" + isShowing);

        if (isShowing) return;
        isShowing = true;
        floatBall.setVisibility(View.VISIBLE);
        floatBall.attachToWindow(mWindowManager);
        floatMenu.detachFromWindow(mWindowManager);
    }

    public void closeMenu() {
        floatMenu.closeMenu();
    }

    public void reset() {
        floatBall.setVisibility(View.VISIBLE);
        floatBall.postSleepRunnable();
        floatMenu.detachFromWindow(mWindowManager);
    }

    public void onFloatBallClick() {
        if (menuItems != null && menuItems.size() > 0) {
            floatMenu.attachToWindow(mWindowManager);
        } else {
            if (mFloatballClickListener != null) {
                mFloatballClickListener.onFloatBallClick();
            }
        }
    }

    public void hide() {
        if (!isShowing) return;
        isShowing = false;
        floatBall.detachFromWindow(mWindowManager);
        floatMenu.detachFromWindow(mWindowManager);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        computeScreenSize();
        reset();
    }

    public void setOnFloatBallClickListener(OnFloatBallClickListener listener) {
        mFloatballClickListener = listener;
    }

    public interface OnFloatBallClickListener {
        void onFloatBallClick();
    }

    public interface IFloatBallPermission {
        /**
         * request the permission of floatball,just use ,
         * or use your custom method.
         *
         * @return return true if requested the permission
         */
        boolean onRequestFloatBallPermission();

        /**
         * detect whether allow  using floatball here or not.
         *
         * @return
         */
        boolean hasFloatBallPermission(Context context);

        void requestFloatBallPermission(Activity activity);
    }
}