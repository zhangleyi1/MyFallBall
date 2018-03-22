package com.zly.floatball.floatball.menu;

import android.graphics.Bitmap;

public abstract class MenuItem {
    /**
     * 菜单icon
     */
    private Bitmap mBitmap;
    private String mPackageName;
    private String mClassName;

    public MenuItem(Bitmap bitmap, String packageName, String className) {
        this.mBitmap = bitmap;
        mPackageName = packageName;
        mClassName = className;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public String getmPackageName() {
        return mPackageName;
    }

    public String getmClassName() {
        return mClassName;
    }

    /**
     * 点击次菜单执行的操作
     */
    public abstract void action(String packageName, String className);
}
