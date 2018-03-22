package com.zly.floatball;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.zly.floatball.adapter.AppData;
import com.zly.floatball.adapter.RecyclerViewAdapter;
import com.zly.floatball.permission.FloatPermissionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements RecyclerViewAdapter.CallBack {
    private String TAG = "MainActivity";
    private FloatPermissionManager mFloatPermissionManager;
    private Handler mHandler;
    private HandlerThread workThread;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private ArrayList<AppData> mListData = new ArrayList<AppData>();
    private ArrayList<AppData> mSendListData = new ArrayList<AppData>();
    private boolean mIsServiceRunning = true;   //debug ,when the project has db,this values will change to false;
    private int mMenuSize = 0;

    public void showFloatBall(View v) {
        Log.d(TAG, "zly --> showFloatBall.");
        startFloatBall();
    }

    private void startFloatBall() {
        Intent intent = new Intent(MainActivity.this, StartService.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", mSendListData);
        intent.putExtras(bundle);
        startService(intent);
    }

    public void hideFloatBall(View view) {
        Intent intent = new Intent();
        intent.setClass(this, StartService.class);
        stopService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFloatPermission();
        intView();
        initData();
    }

    private void initData() {
        getPackages();
        mAdapter.notifyDataSetChanged();
    }

    private void intView() {
        mRecyclerView = (RecyclerView)findViewById(R.id.recycleView);
        mAdapter = new RecyclerViewAdapter(MainActivity.this, mListData, this, mMenuSize);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setItemAnimator( new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setFloatPermission() {
        mFloatPermissionManager = new FloatPermissionManager();
        if (!mFloatPermissionManager.checkPermission(MainActivity.this)) {
            mFloatPermissionManager.applyPermission(MainActivity.this);
        }
    }

    private void getPackages() {
        List<ResolveInfo> mApps;
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pManager = getPackageManager();
        mApps = pManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);

        Log.d(TAG, "zly --> mApps.size:" + mApps.size());

        for(int i = 0; i < mApps.size(); i++) {
            ResolveInfo resolve = mApps.get(i);
            AppData appData = new AppData();
            appData.setName(resolve.loadLabel(pManager).toString());
            appData.setIcon(((BitmapDrawable)resolve.loadIcon(pManager)).getBitmap());
            appData.setState(false);
            appData.setPackagesName(resolve.activityInfo.packageName);
            appData.setClassName(resolve.activityInfo.name);
//            Log.d(TAG, "zly -> className:" + resolve.activityInfo.applicationInfo.className + " packageName:" + resolve.activityInfo.packageName + " processName:" +
//                    resolve.activityInfo.processName + " className:" + resolve.activityInfo.name);
            mListData.add(appData);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finishLoader() {
        Log.d(TAG, "zly --> finishLoader.isServiceRunning.");
        updateSendList();
        if (mIsServiceRunning) {
            startFloatBall();
        }
    }

    private void updateSendList() {
        final int mListDataLength = mListData.size();
        mSendListData.clear();
        for (int i = 0; i < mListDataLength; i++) {
            if (mListData.get(i).getState()) {
                mSendListData.add(mListData.get(i));
            }
        }
    }
}
