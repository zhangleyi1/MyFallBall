package com.zly.floatball;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.IdRes;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.zly.floatball.adapter.AppData;
import com.zly.floatball.adapter.RecyclerViewAdapter;
import com.zly.floatball.db.DBManager;
import com.zly.floatball.db.InitData;
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
    private int mMenuSize = 0;
    private SeekBar mSeekBar;
    private static final String EVENT_RECEIVE_ALPHA = "com.zly.floatball.alpha";
    private static final String EVENT_RECEIVE_TIME = "com.zly.floatball.time";
    private Spinner mSp;
    private int[] timeArray = {2, 4, 6, 10, 100};
    private DBManager mDbManager;
    private InitData mDefaultData;
    private List<String> mPackagesList = new ArrayList<String>(5);
    private RadioGroup mRp;

    public void showFloatBall(View v) {
        Log.d(TAG, "zly --> showFloatBall.");
        startFloatBall();
    }

    private void startFloatBall() {
        Intent intent = new Intent(MainActivity.this, StartService.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", mSendListData);
        intent.putExtras(bundle);
        intent.putExtra("alpha", mDefaultData.alphaValue);
        intent.putExtra("time", mDefaultData.time);
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
        initView();
        initData();
    }

    private void initData() {
        mDbManager = new DBManager(MainActivity.this);
        mDefaultData = mDbManager.query();
        if (null == mDefaultData) {
            mDefaultData = new InitData();
        }

        mPackagesList.add(mDefaultData.packageOne);
        mPackagesList.add(mDefaultData.packageTwo);
        mPackagesList.add(mDefaultData.packageThree);
        mPackagesList.add(mDefaultData.packageFour);
        mPackagesList.add(mDefaultData.packageFive);

        mRp.check(mDefaultData.isServiceRunning?R.id.start_btn:R.id.stop_btn);

        mSeekBar.setProgress(mDefaultData.alphaValue);
        switch(mDefaultData.time) {
            case 2:
                mSp.setSelection(0);
                break;
            case 4:
                mSp.setSelection(1);
                break;
            case 6:
                mSp.setSelection(2);
                break;
            case 10:
                mSp.setSelection(3);
                break;
            case 100:
                mSp.setSelection(4);
                break;
        }

        getPackages();

        mAdapter.notifyDataSetChanged();

    }

    private void initView() {
        mRp = findViewById(R.id.rg);
        mRp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId) {
                    case R.id.start_btn:
                        mDefaultData.isServiceRunning = true;
                        break;
                    case R.id.stop_btn:
                        mDefaultData.isServiceRunning = false;
                        break;
                }
                saveDataToDb();
            }
        });

        mSeekBar = findViewById(R.id.sb);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "zly --> progress:" + seekBar.getProgress());

                if (mDefaultData.isServiceRunning) {
                    Intent intent = new Intent();
                    intent.setAction(EVENT_RECEIVE_ALPHA);
                    intent.putExtra("alpha", seekBar.getProgress());
                    sendBroadcast(intent);
                }

                saveDataToDb();
            }
        });

        mSp = findViewById(R.id.time_sp);
        mSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "zly --> sb position:" + position);
                if (mDefaultData.isServiceRunning) {
                    Intent intent = new Intent();
                    intent.setAction(EVENT_RECEIVE_TIME);
                    intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                    intent.putExtra("time", timeArray[position]);
                    sendBroadcast(intent);
                }
                mDefaultData.time = timeArray[position];
                saveDataToDb();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mRecyclerView = findViewById(R.id.recycleView);
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

            for (int j = 0; j < mPackagesList.size(); j++) {
                if (appData.getName().equals(mPackagesList.get(j))) {
                    appData.setState(true);
                }
            }

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
        if (mDefaultData.isServiceRunning) {
            startFloatBall();
        }
    }

    private void updateSendList() {
        final int mListDataLength = mListData.size();
        mSendListData.clear();
        mPackagesList.clear();

        for (int i = 0; i < mListDataLength; i++) {
            if (mListData.get(i).getState()) {
                mSendListData.add(mListData.get(i));
                mPackagesList.add(mListData.get(i).getName());
            }
        }

        switch(mPackagesList.size()) {
            case 5:
                mDefaultData.packageFive = mPackagesList.get(4);
            case 4:
                mDefaultData.packageFive = mPackagesList.get(3);
            case 3:
                mDefaultData.packageFive = mPackagesList.get(2);
            case 2:
                mDefaultData.packageFive = mPackagesList.get(1);
            case 1:
                mDefaultData.packageFive = mPackagesList.get(0);
                break;
        }

        saveDataToDb();
    }

    private void saveDataToDb() {
        if (null == mDbManager.query()) {
            mDbManager.insert(mDefaultData);
        } else {
            mDbManager.update(mDefaultData);
        }
    }
}
