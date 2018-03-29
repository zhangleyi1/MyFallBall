package com.zly.floatball;

import android.app.Service;
import android.app.usage.UsageEvents;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.zly.floatball.adapter.AppData;
import com.zly.floatball.floatball.FloatBallManager;
import com.zly.floatball.floatball.floatball.FloatBallCfg;
import com.zly.floatball.floatball.menu.FloatMenuCfg;
import com.zly.floatball.floatball.menu.MenuItem;
import com.zly.floatball.utils.BackGroudSeletor;
import com.zly.floatball.utils.DensityUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/19.
 */
public class StartService extends Service {
    private String TAG = "StartServie";
    boolean mShowMenu = true;
    private FloatBallManager mFloatballManager;
    private static final String EVENT_RECEIVE_ALPHA = "com.zly.floatball.alpha";
    private static final String EVENT_RECEIVE_TIME = "com.zly.floatball.time";
    private IntentFilter mIntentFilter;
    private FloatBallBroadCastReceiver mFloatBallBroadCastReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "zly --> onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "zly --> onStartCommand");
        ArrayList<AppData> listData = intent.getParcelableArrayListExtra("data");

        if (listData != null) {
            log("listDatalength:" + listData.size());
            addFloatMenuItem(listData);
        }

        int alpha = intent.getIntExtra("alpha", 100);
        mFloatballManager.setAlpha(alpha);

        int time = intent.getIntExtra("time", 2);
        mFloatballManager.setEdgeTime(time);

        log("alpha:" + alpha + " time:" + time);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "zly --> onCreate");

        init(mShowMenu);
        Log.d(TAG, "zly --> mFloatballManager.getMenuItemSize():" + mFloatballManager.getMenuItemSize());
        //没有菜单时，悬浮球将变成点击事件
        if (mFloatballManager.getMenuItemSize() == 0) {
            mFloatballManager.setOnFloatBallClickListener(new FloatBallManager.OnFloatBallClickListener() {
                @Override
                public void onFloatBallClick() {
                    toast("当前添加的应用为空。");
                }
            });
        }

        mFloatballManager.show();

        mFloatBallBroadCastReceiver = new FloatBallBroadCastReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(EVENT_RECEIVE_ALPHA);
        mIntentFilter.addAction(EVENT_RECEIVE_TIME);
        registerReceiver(mFloatBallBroadCastReceiver, mIntentFilter);
    }

    private void init(boolean showMenu) {
        //1 初始化悬浮球配置，定义好悬浮球大小和icon的drawable
        int ballSize = DensityUtil.dip2px(this, 45);
        Drawable ballIcon = BackGroudSeletor.getdrawble("ic_floatball", this);
        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon);
        if (showMenu) {
            int menuSize = DensityUtil.dip2px(this, 180);
            int menuItemSize = DensityUtil.dip2px(this, 40);
            Log.d(TAG, "zly --> menuSize:" + menuSize + " menuItemSize:" + menuItemSize);
            FloatMenuCfg menuCfg = new FloatMenuCfg(menuSize, menuItemSize);
            mFloatballManager = new FloatBallManager(StartService.this, ballCfg, menuCfg);

        } else {
            mFloatballManager = new FloatBallManager(this, ballCfg);
        }
    }

    private void addFloatMenuItem(final ArrayList<AppData> list) {
        int len = list.size();
        mFloatballManager.clearAllListMenuItem();
        log("itemSize:" + mFloatballManager.getMenuItemSize());
        for (int i = 0; i < len; i++) {
            MenuItem menuItem = new MenuItem(list.get(i).getIcon(), list.get(i).getPackagesName(),
                    list.get(i).getClassName()) {
                @Override
                public void action(String packageName, String className) {
                    log("packageName:" + packageName + " className:" + className);
                    Intent intent = new Intent();
                    intent.setClassName(packageName, className);
                    startActivity(intent);
                    mFloatballManager.closeMenu();
                }
            };
            mFloatballManager.addMenuItem(menuItem);
        }
        mFloatballManager.buildMenu();
    }

    private class FloatBallBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            log("action:" + intent.getAction());
            if (intent.getAction().equals(EVENT_RECEIVE_ALPHA)) {
                int alphaValue = intent.getIntExtra("alpha", 0xFF);
                log("alphaValue:" + alphaValue + " modifyAlpha:" + alphaValue*1.0/100);
                mFloatballManager.setAlpha((float) (alphaValue*1.0/100));
            } else if (intent.getAction().equals(EVENT_RECEIVE_TIME)) {
                int time = intent.getIntExtra("time", 2);
                log("time:" + time);
                mFloatballManager.setEdgeTime(time);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFloatballManager.hide();
        unregisterReceiver(mFloatBallBroadCastReceiver);
    }

    private void log(String string) {
        Log.d("StartService", "zly --> " + string);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
