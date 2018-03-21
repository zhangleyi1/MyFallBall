package com.zly.floatball;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.zly.floatball.floatball.FloatBallManager;
import com.zly.floatball.floatball.floatball.FloatBallCfg;
import com.zly.floatball.floatball.menu.FloatMenuCfg;
import com.zly.floatball.floatball.menu.MenuItem;
import com.zly.floatball.utils.BackGroudSeletor;
import com.zly.floatball.utils.DensityUtil;


/**
 * Created by Administrator on 2018/3/19.
 */
public class StartService extends Service {

    private FloatBallManager mFloatballManager;
    private String TAG = "StartServie";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "zly --> onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "zly --> onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "zly --> onCreate");
        boolean showMenu = true;
        init(showMenu);
        Log.d(TAG, "zly --> mFloatballManager.getMenuItemSize():" + mFloatballManager.getMenuItemSize());
        //没有菜单时，悬浮球将变成点击事件
        if (mFloatballManager.getMenuItemSize() == 0) {
            mFloatballManager.setOnFloatBallClickListener(new FloatBallManager.OnFloatBallClickListener() {
                @Override
                public void onFloatBallClick() {
                    toast("点击了悬浮球");
                }
            });
        }

        mFloatballManager.show();
    }

    private void init(boolean showMenu) {
        //1 初始化悬浮球配置，定义好悬浮球大小和icon的drawable
        int ballSize = DensityUtil.dip2px(this, 45);
        Drawable ballIcon = BackGroudSeletor.getdrawble("ic_floatball", this);
        FloatBallCfg ballCfg = new FloatBallCfg(ballSize, ballIcon);
        if (showMenu) {
            //2 需要显示悬浮菜单
            //2.1 初始化悬浮菜单配置，有菜单item的大小和菜单item的个数
            int menuSize = DensityUtil.dip2px(this, 180);
            int menuItemSize = DensityUtil.dip2px(this, 40);
            Log.d(TAG, "zly --> menuSize:" + menuSize + " menuItemSize:" + menuItemSize);
            FloatMenuCfg menuCfg = new FloatMenuCfg(menuSize, menuItemSize);
            //3 生成floatballManager
            mFloatballManager = new FloatBallManager(StartService.this, ballCfg, menuCfg);
            addFloatMenuItem();
        } else {
            mFloatballManager = new FloatBallManager(this, ballCfg);
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void addFloatMenuItem() {
        MenuItem personItem = new MenuItem(BackGroudSeletor.getdrawble("ic_weixin", this)) {
            @Override
            public void action() {
                toast("打开微信");
                mFloatballManager.closeMenu();
            }
        };

        MenuItem walletItem = new MenuItem(BackGroudSeletor.getdrawble("ic_weibo", this)) {
            @Override
            public void action() {
                toast("打开微博");
            }
        };

        MenuItem settingItem = new MenuItem(BackGroudSeletor.getdrawble("ic_email", this)) {
            @Override
            public void action() {
                toast("打开邮箱");
                mFloatballManager.closeMenu();
            }
        };

        mFloatballManager.addMenuItem(personItem)
                .addMenuItem(walletItem)
                .addMenuItem(settingItem)
                .buildMenu();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFloatballManager.hide();
    }

//    private
}
